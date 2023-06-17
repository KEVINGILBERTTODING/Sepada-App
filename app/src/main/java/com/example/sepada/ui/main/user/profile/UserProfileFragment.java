package com.example.sepada.ui.main.user.profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.data.model.UserDetailModel;
import com.example.sepada.databinding.FragmentUserProfileBinding;
import com.example.sepada.ui.main.auth.LoginActivity;
import com.example.sepada.util.Constans;
import com.example.sepada.util.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private UserService userService;
    private String userId, username, password;
    private SharedPreferences.Editor editor;
    private AlertDialog progressDialog;
    private File file;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentUserProfileBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        userService = ApiConfig.getClient().create(UserService.class);
        editor = sharedPreferences.edit();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getTamu("1", binding.tvTotalProccess);
        getTamu("2", binding.tvTotalSetuju);
        getTamu("3", binding.tvTotalTolak);
        getTamu("all", binding.tvTotalPengajuan);
        getDetailProfile();
    }

    private void listener() {
        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        binding.tvSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhotoProfile();
            }
        });

        binding.menuLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        binding.menuUbahDataPengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameUser, new UpdateProfile2Fragment())
                        .addToBackStack(null).commit();
            }
        });

        binding.menuUbahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_ubah_profile);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Button btnBatal, btnSimpan;
                btnSimpan = dialog.findViewById(R.id.btnSimpan);
                EditText etUsername, etPassword;
                etUsername = dialog.findViewById(R.id.etUsername);
                etPassword = dialog.findViewById(R.id.etPassword);
                btnBatal = dialog.findViewById(R.id.btnBatal);
                etUsername.setText(username);
                etPassword.setText(password);

                dialog.show();

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etUsername.getText().toString().isEmpty()) {
                            etUsername.requestFocus();
                            etUsername.setError("Tidak boleh kosong");
                        }else if (etPassword.getText().toString().isEmpty()) {
                            etPassword.requestFocus();
                            etPassword.setError("Tidak boleh kosong");
                        }else {
                            showProgressBar("Loading", "Memperbaharui data...", true);
                            userService.updateProfile(userId, etUsername.getText().toString(),
                                    etPassword.getText().toString()).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("ds", "ds", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        showToast("success", "Berhasil mengubah data profil");
                                        dialog.dismiss();
                                        getDetailProfile();

                                    }else {
                                        showToast("err", "Terjadi kesalahan");
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showProgressBar("sd", "D", false);

                                    showToast("err", "Tidak ada koneksi internet");
                                    dialog.dismiss();

                                }
                            });

                        }
                    }
                });
            }
        });

    }


    private void getDetailProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getDetailProfile(userId).enqueue(new Callback<UserDetailModel>() {
            @Override
            public void onResponse(Call<UserDetailModel> call, Response<UserDetailModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.tvUsername.setText(response.body().getUsername());
                    binding.tvInstansi.setText(response.body().getNamaLengkap());
                    binding.tvJabatan.setText(response.body().getJabatan());
                    username = response.body().getUsername();
                    password = response.body().getPassword();
                    showProgressBar("sd", "Ds", false);

                    Glide.with(getContext()).load(response.body().getFoto()).centerCrop()
                            .into(binding.ivProfile);

                }else {
                    binding.tvUsername.setText("-");
                    binding.tvInstansi.setText("-");
                    binding.tvJabatan.setText("-");
                    showProgressBar("sd", "Ds", false);
                    showToast("err", "Terjadi kesalahan");

                }
            }

            @Override
            public void onFailure(Call<UserDetailModel> call, Throwable t) {
                showProgressBar("sd", "Ds", false);
                showToast("err", "Tidak ada koneksi internet");

            }
        });
    }



    private void getTamu(String status, TextView tvTotal){
        showProgressBar("Loading", "Memuat data...", true);
        userService.getTamuByStatus(userId, status).enqueue(new Callback<List<TamuModel>>() {
            @Override
            public void onResponse(Call<List<TamuModel>> call, Response<List<TamuModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    tvTotal.setText(String.valueOf(response.body().size()));
                    showProgressBar("d", "d", false);
                }else {
                    tvTotal.setText("0");
                    showProgressBar("d", "d", false);
                }
            }

            @Override
            public void onFailure(Call<List<TamuModel>> call, Throwable t) {
                tvTotal.setText("0");
                showProgressBar("d", "d", false);
                showToast("err", "Tidak ada koneksi internet");

            }
        });
    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                progressDialog = builder.create();
            }
            progressDialog.show(); // Menampilkan progress dialog
        } else {
            // Menyembunyikan progress dialog jika ada
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    private void showToast(String jenis, String text) {
        if (jenis.equals("success")) {
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }

    private void updatePhotoProfile() {
        showProgressBar("Loading", "Menyimpan perubahan...", true);
        HashMap map  = new HashMap();
        map.put("id", RequestBody.create(MediaType.parse("text/plain"), userId));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("foto", file.getName(), requestBody);
        userService.updateProfile(map, filePart).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("sd", "ds", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "Berhasil mengubah foto profil");
                    getDetailProfile();
                    binding.tvSimpan.setVisibility(View.GONE);

                }else {
                    showToast("success", response.body().getMessage());


                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("d", "d", false);
                showToast("success", "Tidak ada koneksi internet");


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                binding.ivProfile.setImageURI(uri);
                binding.tvSimpan.setVisibility(View.VISIBLE);

            }
        }
    }


    public String getRealPathFromUri(Uri uri) {
        String filePath = "";
        if (getContext().getContentResolver() != null) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                File file = new File(getContext().getCacheDir(), getFileName(uri));
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void writeFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }


    private void logOut() {
        editor.clear().apply();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }
}