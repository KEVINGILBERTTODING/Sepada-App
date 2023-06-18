package com.example.sepada.ui.main.superadmin.adapter.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.UserDetailModel;
import com.example.sepada.ui.main.admin.users.UpdateUsers;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    Context context;
    List<UserDetailModel> userDetailModelList;
    private SuperAdminService superAdminService;
    private AlertDialog progressDialog;

    public AdminAdapter(Context context, List<UserDetailModel> userDetailModelList) {
        this.context = context;
        this.userDetailModelList = userDetailModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUsername.setText(userDetailModelList.get(holder.getAdapterPosition()).getUsername());
        holder.tvInstansi.setText(userDetailModelList.get(holder.getAdapterPosition()).getEmail());

    }

    @Override
    public int getItemCount() {
        return userDetailModelList.size();
    }

    public void filter(ArrayList<UserDetailModel> filteredList) {
        userDetailModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsername, tvInstansi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvInstansi = itemView.findViewById(R.id.tvInstansi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            superAdminService = ApiConfig.getClient().create(SuperAdminService.class);

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_update_admin);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button btnSimpan, btnBatal, btnDelete;
            EditText etUsername, etPassword, etEmail;
            btnBatal = dialog.findViewById(R.id.btnBatal);
            btnSimpan = dialog.findViewById(R.id.btnSimpan);
            etUsername = dialog.findViewById(R.id.etUsername);
            etPassword = dialog.findViewById(R.id.etPassword);
            etEmail = dialog.findViewById(R.id.etEmail);
            btnDelete = dialog.findViewById(R.id.btnDelete);

            etUsername.setText(userDetailModelList.get(getAdapterPosition()).getUsername());
            etEmail.setText(userDetailModelList.get(getAdapterPosition()).getEmail());
            etPassword.setText(userDetailModelList.get(getAdapterPosition()).getPassword());
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
                    if (etEmail.getText().toString().isEmpty()) {
                        etEmail.setError("Tidak boleh kosong");
                    }else if (etUsername.getText().toString().isEmpty()) {
                        etUsername.setError("Tidak boleh kosong");
                    }else if (etPassword.getText().toString().isEmpty()) {
                        etPassword.setError("Tidak boleh kosong");
                    }else {
                        showProgressBar("Loading", "Menyimpan data...", true);
                        superAdminService.updateAdmin(
                                userDetailModelList.get(getAdapterPosition()).getUserId(),
                                etUsername.getText().toString(),
                                etPassword.getText().toString(),
                                etEmail.getText().toString()
                        ).enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                showProgressBar("d", "s", false);
                                if (response.isSuccessful() && response.body().getCode() == 200) {
                                    userDetailModelList.get(getAdapterPosition()).setUsername(etUsername.getText().toString());
                                    userDetailModelList.get(getAdapterPosition()).setEmail(etEmail.getText().toString());
                                    userDetailModelList.get(getAdapterPosition()).setPassword(etPassword.getText().toString());
                                    notifyDataSetChanged();
                                    showToast("success", "Berhasil menperbaharui data");
                                    dialog.dismiss();

                                }else {
                                    showToast("err", response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                showProgressBar("d", "s", false);
                                showToast("err", "Tidak ada koneksi internet");


                            }
                        });
                    }
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menghapus data...", true);
                    superAdminService.deleteAdmin(userDetailModelList.get(getAdapterPosition()).getUserId())
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("d", "d", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        showToast("success", "Berhasil menghapus admin");
                                        userDetailModelList.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }else {
                                        showToast("err", "Terjadi kesalahan");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showToast("err", "Tidak ada koneksi internet");
                                    showProgressBar("d", "d", false);                                        showToast("err", "Terjadi kesalahan");


                                }
                            });
                }
            });

        }
    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            Toasty.success(context, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(context, text, Toasty.LENGTH_SHORT).show();
        }
    }
}
