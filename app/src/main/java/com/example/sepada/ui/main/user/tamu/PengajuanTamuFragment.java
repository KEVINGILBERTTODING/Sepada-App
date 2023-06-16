package com.example.sepada.ui.main.user.tamu;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.databinding.FragmentPengajuanTamuFragmentBinding;
import com.example.sepada.util.Constans;
import com.example.sepada.util.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class PengajuanTamuFragment extends Fragment {
    private FragmentPengajuanTamuFragmentBinding binding;
    private UserService userService;
    private SharedPreferences sharedPreferences;
    private String userId, bagian;
    private File file;
    private String [] opsiBagian = {
            "Umum",
            "Keuangan",
            "Persidangan",
            "Humas",
            "Komisi A",
            "Komisi B",
            "Komisi C",
            "Komisi D",
    };
    private AlertDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentPengajuanTamuFragmentBinding.inflate(inflater, container, false);
        userService = ApiConfig.getClient().create(UserService.class);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constans.USER_ID, null);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter adapterBagian = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiBagian);
        adapterBagian.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spBagian.setAdapter(adapterBagian);
        listener();
    }

    private void listener() {
        binding.tvTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker(binding.tvTanggal);

            }
        });
        binding.tvJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(binding.tvJam);

            }
        });
        binding.spBagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bagian = opsiBagian[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 1);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etAlasan.getText().toString().isEmpty()){
                    binding.etAlasan.setError("Tidak boleh kosong");
                    binding.etAlasan.requestFocus();
                }else if (binding.etAlasan.getText().toString().isEmpty()){
                    binding.etAlasan.setError("Tidak boleh kosong");
                    binding.etAlasan.requestFocus();
                }else if (binding.tvJam.getText().toString().isEmpty()){
                    binding.tvJam.setError("Tidak boleh kosong");
                    binding.tvJam.requestFocus();
                }else if (binding.tvTanggal.getText().toString().isEmpty()){
                    binding.tvTanggal.setError("Tidak boleh kosong");
                    binding.tvTanggal.requestFocus();
                }else if (binding.etJumlah.getText().toString().isEmpty()){
                    binding.etJumlah.setError("Tidak boleh kosong");
                    binding.etJumlah.requestFocus();
                }else if (binding.etFilePath.getText().toString().isEmpty()){
                    binding.etFilePath.setError("Tidak boleh kosong");
                    showToast("err", "Anda belum memilih file");
                } // hanya dapat mengajukan jam 08:00 - 12:00
                else if (binding.tvJam.getText().toString().compareTo("08:00") < 0 || binding.tvJam.getText().toString().compareTo("12:00") > 0) {
                    showToast("err", "Anda hanya dapat mengajukan pada jam 08:00 - 12:00");
                    binding.tvJam.setError("");
                }else {
                    String dateString = binding.tvTanggal.getText().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = dateFormat.parse(binding.tvTanggal.getText().toString());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                        if (dayOfWeek == Calendar.SATURDAY) {
                           showToast("err", "Anda tidak dapat mengajukan pada hari sabtu");
                           binding.tvTanggal.setError("");
                        } else if (dayOfWeek == Calendar.SUNDAY) {
                            showToast("err", "Anda tidak dapat mengajukan pada hari minggu");
                            binding.tvTanggal.setError("");

                        }
                        else {

                            insertPengajuan();

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
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
    private void getDatePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }
        });
        datePickerDialog.show();
    }
    private void timePicker(TextView tvTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String timeFormatted, minuteFormatted, secondFormatted;

            if (hourOfDay < 10) {
                timeFormatted = String.format("%02d", hourOfDay);
            } else {
                timeFormatted = String.valueOf(hourOfDay);
            }

            if (minute < 10) {
                minuteFormatted = String.format("%02d", minute);
            } else {
                minuteFormatted = String.valueOf(minute);
            }

            int second = Calendar.getInstance().get(Calendar.SECOND);
            if (second < 10) {
                secondFormatted = String.format("%02d", second);
            } else {
                secondFormatted = String.valueOf(second);
            }

            tvTime.setText(timeFormatted + ":" + minuteFormatted + ":" + secondFormatted);
        }, 0, 0, true);

        timePickerDialog.show();
    }

    private void insertPengajuan() {
        showProgressBar("Loading", "Menyimpan data...", true);
        HashMap map = new HashMap();
        map.put("id_user", RequestBody.create(MediaType.parse("text/plain"), userId));
        map.put("tujuan", RequestBody.create(MediaType.parse("text/plain"), bagian));
        map.put("alasan", RequestBody.create(MediaType.parse("text/plain"), binding.etAlasan.getText().toString()));
        map.put("tanggal", RequestBody.create(MediaType.parse("text/plain"), binding.tvTanggal.getText().toString()));
        map.put("jam", RequestBody.create(MediaType.parse("text/plain"), binding.tvJam.getText().toString()));
        map.put("jumlah", RequestBody.create(MediaType.parse("text/plain"), binding.etJumlah.getText().toString()));

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("surat", file.getName(), requestBody);
        userService.insertPengajuan(map, filePart).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("sd", "ds", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.layout_alert_success);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnOke = dialog.findViewById(R.id.btnOke);
                    dialog.show();

                    btnOke.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    getActivity().onBackPressed();

                }else {
                    showToast("err", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("sd", "ds", false);
                showToast("err", "Tidak ada koneksi internet");

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
                binding.etFilePath.setText(file.getName());

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

}