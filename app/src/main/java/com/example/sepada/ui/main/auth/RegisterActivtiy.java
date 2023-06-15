package com.example.sepada.ui.main.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.AuthModel;
import com.example.sepada.util.AuthService;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivtiy extends AppCompatActivity {

    private EditText etEmail, etUsername, etPassword, etPassKonfir;
    private Button btnRegister;
    private AuthService authService;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activtiy);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etPassKonfir = findViewById(R.id.etPassKonfirmasi);
        authService = ApiConfig.getClient().create(AuthService.class);
        listener();
    }

    private void listener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Email tidak boleh kosong");
                    etEmail.requestFocus();
                }else  if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Username tidak boleh kosong");
                    etUsername.requestFocus();
                }else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Password tidak boleh kosong");
                    etPassword.requestFocus();
                }else if (etPassKonfir.getText().toString().isEmpty()) {
                    etPassKonfir.setError("Password konfirmasi tidak boleh kosong");
                    etPassKonfir.requestFocus();
                } else if (!etPassword.getText().toString().equals(etPassKonfir.getText().toString())) {
                    etPassKonfir.setError("Password tidak sesuai");
                    etPassKonfir.requestFocus();
                }else {
                    register();
                }

            }
        });

    }

    private void register(){
        showProgressBar("Loading", "Validasi pendaftar...", true);
        authService.register(
                etUsername.getText().toString(),
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showProgressBar("sd", "d", false);
                    showToast("success", "Berhasil registrasi");
                    startActivity(new Intent(RegisterActivtiy.this, LoginActivity.class));
                    finish();
                }else {
                    showProgressBar("sd", "d", false);
                    showToast("error", "Terjadi kesalahan...");
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                showProgressBar("sd", "d", false);
                showToast("error", "Tidak ada koneksi internet...");

            }
        });

    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivtiy.this);
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
            Toasty.success(RegisterActivtiy.this, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(RegisterActivtiy.this, text, Toasty.LENGTH_SHORT).show();
        }
    }
}