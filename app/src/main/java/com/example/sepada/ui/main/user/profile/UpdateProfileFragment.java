package com.example.sepada.ui.main.user.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.databinding.FragmentUpdateProfileBinding;
import com.example.sepada.util.Constans;
import com.example.sepada.util.UserService;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileFragment extends Fragment {

    private FragmentUpdateProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private UserService userService;
    private AlertDialog progressDialog;
    private String userId, jk;
    private String [] opsiJK = {"Laki-laki", "Perempuan"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentUpdateProfileBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        userService = ApiConfig.getClient().create(UserService.class);
        ArrayAdapter adapterjk = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiJK);
        adapterjk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJenisKelamin.setAdapter(adapterjk);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener();
    }

    private void listener() {
        binding.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        binding.spJenisKelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jk = opsiJK[position];
                if (jk.equals("Laki-laki")) {
                    jk = "1";
                }else {
                    jk ="2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateProfile() {
        showProgressBar("Loading", "Menyimpan data...", true);
        userService.insertDetailUser(
                userId, binding.etNamaInstansi.getText().toString(),jk, binding.etTelepon.getText().toString(),
                binding.etAlamat.getText().toString(), binding.etNip.getText().toString(),
                binding.etPangkat.getText().toString(), binding.etJabatan.getText().toString()
        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("d", "d", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {

                    showToast("success", "Berhasil mengubah profile");
                    getActivity().onBackPressed();
                }else {
                    showToast("error", "Gagal mengubah profile");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("d", "d", false);

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
}