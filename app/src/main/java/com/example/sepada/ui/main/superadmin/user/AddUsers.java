package com.example.sepada.ui.main.superadmin.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.databinding.FragmentAddUsersBinding;
import com.example.sepada.util.AdminService;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddUsers extends Fragment {

    private FragmentAddUsersBinding binding;
    private AdminService adminService;
    private AlertDialog progressDialog;
    private String jenisKelamin;
    private String [] opsiJk = {"Laki-Laki", "Perempuan"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddUsersBinding.inflate(inflater, container, false);
        ArrayAdapter jkAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opsiJk);
        jkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJenisKelamin.setAdapter(jkAdapter);
        adminService = ApiConfig.getClient().create(AdminService.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void listener() {
        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etUsername.getText().toString().isEmpty()) {
                    binding.etUsername.setError("Username tidak boleh kosong");
                }else if (binding.etEmail.getText().toString().isEmpty()) {
                    binding.etEmail.setError("Email tidak boleh kosong");
                }else if (binding.etPassword.getText().toString().isEmpty()) {
                    binding.etPassword.setError("Password tidak boleh kosong");
                }else if (binding.etNamaInstansi.getText().toString().isEmpty()) {
                    binding.etNamaInstansi.setError("Nama Instansi tidak boleh kosong");
                } else if (binding.etTelepon.getText().toString().isEmpty()) {
                    binding.etTelepon.setError("Telepon tidak boleh kosong");
                }else if (binding.etAlamatInstansi.getText().toString().isEmpty()) {
                    binding.etAlamatInstansi.setError("Alamat Instansi tidak boleh kosong");
                }else {
                    simpan();
                }


            }
        });
        binding.spJenisKelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jenisKelamin = opsiJk[position];
                if (jenisKelamin.equals("Lakilaki")) {
                    jenisKelamin = "1";
                }else {
                    jenisKelamin ="2";
                }
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
    }

    private void simpan() {
        showProgressBar("Loading", "Menyimpan data...", true);
        adminService.addUser(
                binding.etUsername.getText().toString(),
                binding.etPassword.getText().toString(),
                binding.etEmail.getText().toString(),
                Integer.parseInt(jenisKelamin),
                binding.etNamaInstansi.getText().toString(),
                binding.etTelepon.getText().toString(),
                binding.etAlamatInstansi.getText().toString()

        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("d","d", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "Berhasil menambahkan pengguna baru");
                    getActivity().onBackPressed();

                }else {
                    showToast("err", "Terjadi kesalahan");

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
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

}