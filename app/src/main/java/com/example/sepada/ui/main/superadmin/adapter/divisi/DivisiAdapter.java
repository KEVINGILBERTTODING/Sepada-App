package com.example.sepada.ui.main.superadmin.adapter.divisi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.DivisiModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisiAdapter extends RecyclerView.Adapter<DivisiAdapter.ViewHolder> {
    private Context context;
    private List<DivisiModel> divisiModelList;
    private AlertDialog progressDialog;
    private SuperAdminService superAdminService;

    public DivisiAdapter(Context context, List<DivisiModel> divisiModelList) {
        this.context = context;
        this.divisiModelList = divisiModelList;
    }

    @NonNull
    @Override
    public DivisiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_divisi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DivisiAdapter.ViewHolder holder, int position) {
        holder.tvDivisi.setText(divisiModelList.get(holder.getAdapterPosition()).getNamaDivisi());
    }

    @Override
    public int getItemCount() {
        return divisiModelList.size();
    }

    public void filter(ArrayList<DivisiModel> filteredList) {
        divisiModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDivisi;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDivisi = itemView.findViewById(R.id.tvDivisi);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            superAdminService = ApiConfig.getClient().create(SuperAdminService.class);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menghapus data...", true);
                    superAdminService.deleteDivisi(divisiModelList.get(getAdapterPosition()).getId())
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("l","s", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        showToast("success", "Berhasil menghapus divisi");
                                        divisiModelList.remove(getAdapterPosition());
                                        notifyDataSetChanged();
                                    }else {
                                        showToast("err", "Terjadi kesalahan");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showProgressBar("l","s", false);
                                    showToast("err", "Tidak ada koneksi internet");

                                }
                            });


                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.layout_update_divisi);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    EditText etNamaDivisi = dialog.findViewById(R.id.etNamaDivisi);
                    Button btnSimpan, btnBatal;
                    btnSimpan = dialog.findViewById(R.id.btnSimpan);
                    btnBatal = dialog.findViewById(R.id.btnBatal);

                    etNamaDivisi.setText(divisiModelList.get(getAdapterPosition()).getNamaDivisi());
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
                            if (etNamaDivisi.getText().toString().isEmpty()) {
                                etNamaDivisi.setError("Tidak boleh kosong");
                                etNamaDivisi.requestFocus();
                            }else {
                                showProgressBar("Loading", "Menyimpan perubahan", true);
                                superAdminService.updateDivisi(
                                        etNamaDivisi.getText().toString(),
                                        divisiModelList.get(getAdapterPosition()).getId()
                                ).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        showProgressBar("s", "S", false);
                                        if (response.isSuccessful() && response.body().getCode() == 200) {
                                            showToast("success", "Berhasil mengubah divisi");
                                            divisiModelList.get(getAdapterPosition()).setNamaDivisi(etNamaDivisi.getText().toString());
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                        }else {
                                            showToast("err", "Tidak ada koneksi internet");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        showProgressBar("s", "S", false);
                                        showToast("success", "Berhasil mengubah divisi");


                                    }
                                });
                            }
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
