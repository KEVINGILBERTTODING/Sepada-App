package com.example.sepada.ui.main.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.AuthModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.ui.main.user.UserMainActivity;
import com.example.sepada.util.AuthService;
import com.example.sepada.util.Constans;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private AlertDialog progressDialog;
    private AuthService authService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        authService = ApiConfig.getClient().create(AuthService.class);
        sharedPreferences = getSharedPreferences(Constans.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("logged_in", false)) {
            if (sharedPreferences.getString(Constans.ROLE, null).equals("1")) {
                startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                finish();
            }

        }


        listener();


    }




    private void listener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Username tidak boleh kosong");
                    etUsername.requestFocus();
                }else if (etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Password tidak boleh kosong");
                    etPassword.requestFocus();
                }else {
                    login();
                }
            }
        });

    }


    private void login(){
        showProgressBar("Loading", "Validasi user...", true);
        authService.login(etUsername.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<AuthModel>() {

            @Override
            public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                showProgressBar("sd", "Sd", false);

                if (response.isSuccessful() && response.body().getCode() == 200) {
                    if (response.body().getRole().equals("1")) {
                        editor.putBoolean("logged_in", true);
                        editor.putString(Constans.USER_ID, response.body().getUserId());
                        editor.putString(Constans.ROLE, response.body().getRole());
                        editor.putString(Constans.USERNAME, response.body().getUsername());
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                        finish();

                    }else  if (response.body().getRole().equals("2")) {
                        showToast("success",  "admin");
                    }else if (response.body().getRole().equals("3")) {
                        showToast("success",  "super admin");
                    }


                }else {
                    showProgressBar("d", "d", false);
                    showToast("error", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {

            }
        });


    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
            Toasty.success(LoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(LoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }
    }
}