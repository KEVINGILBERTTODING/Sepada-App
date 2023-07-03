package com.example.sepada.ui.main.superadmin.adapter.anggota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.AnggotaModel;
import com.example.sepada.data.model.DivisiModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.ui.main.superadmin.adapter.divisi.SpinnerDivisiAdapter;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.ViewHolder> {
    private Context context;
    private List<AnggotaModel> anggotaModelList;
    private List<DivisiModel> divisiModelList;
    private AlertDialog progressDialog;
    private SuperAdminService superAdminService;
    private SpinnerDivisiAdapter spinnerDivisiAdapter;
    private Spinner spDivisi;
    private Integer divisiId;


    public AnggotaAdapter(Context context, List<AnggotaModel> anggotaModelList) {
        this.context = context;
        this.anggotaModelList = anggotaModelList;
    }

    @NonNull
    @Override
    public AnggotaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_anggota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnggotaAdapter.ViewHolder holder, int position) {
        holder.tvNamaAnggota.setText(anggotaModelList.get(holder.getAdapterPosition()).getNamaLengkap());
        holder.tvDivisi.setText(anggotaModelList.get(holder.getAdapterPosition()).getNamaDivisi());

    }

    @Override
    public int getItemCount() {
        return anggotaModelList.size();
    }

    public void filter(ArrayList<AnggotaModel> filteredList){
        anggotaModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaAnggota, tvDivisi;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaAnggota = itemView.findViewById(R.id.tvNamaAnggota);
            tvDivisi = itemView.findViewById(R.id.tvDivisi);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            superAdminService = ApiConfig.getClient().create(SuperAdminService.class);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menghapus data...", true);
                    superAdminService.deleteAnggota(anggotaModelList.get(getAdapterPosition()).getAnggotaId())
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("l","s", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        showToast("success", "Berhasil menghapus anggota");
                                        anggotaModelList.remove(getAdapterPosition());
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
                    Dialog dialogUpdateAnggota = new Dialog(context);
                    dialogUpdateAnggota.setContentView(R.layout.layout_update_anggota);
                    dialogUpdateAnggota.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    EditText etNamaAnggota, etNoTelp;
                    spDivisi = dialogUpdateAnggota.findViewById(R.id.spDivisi);
                    Button btnSimpan, btnBatal;
                    etNamaAnggota = dialogUpdateAnggota.findViewById(R.id.etNamaAnggota);
                    etNoTelp = dialogUpdateAnggota.findViewById(R.id.etNoTelp);
                    btnSimpan = dialogUpdateAnggota.findViewById(R.id.btnSimpan);
                    btnBatal = dialogUpdateAnggota.findViewById(R.id.btnBatal);

                    etNamaAnggota.setText(anggotaModelList.get(getAdapterPosition()).getNamaLengkap());
                    etNoTelp.setText(anggotaModelList.get(getAdapterPosition()).getNoTelp());
                    dialogUpdateAnggota.show();

                    getDivisi();

                    spDivisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            divisiId = Integer.parseInt(spinnerDivisiAdapter.getCategoriesId(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etNamaAnggota.getText().toString().isEmpty()) {
                                etNamaAnggota.setError("Tidak boleh kosong");
                                etNamaAnggota.requestFocus();
                            }else if (etNoTelp.getText().toString().isEmpty()) {

                                etNoTelp.setError("Tidak boleh kosong");
                                etNoTelp.requestFocus();
                            }else {
                                showProgressBar("Loading", "Menyimpan perubahan...",true);
                                superAdminService.updateAnggota(
                                        anggotaModelList.get(getAdapterPosition()).getAnggotaId(),
                                        etNamaAnggota.getText().toString(),
                                        String.valueOf(divisiId),
                                        etNoTelp.getText().toString()
                                ).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        showProgressBar("", "", false);
                                        if (response.isSuccessful() && response.body().getCode() == 200) {
                                            dialogUpdateAnggota.dismiss();
                                            anggotaModelList.get(getAdapterPosition()).setNamaLengkap(etNamaAnggota.getText().toString());
                                            anggotaModelList.get(getAdapterPosition()).setNoTelp(etNoTelp.getText().toString());
                                            notifyDataSetChanged();
                                            showToast("success", "Berhasil mengubah anggota");

                                        }else {
                                            showToast("err", "Terjadi kesalahan");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        showProgressBar("s", "s", false);
                                        showToast("err", "Tidak ada koneksi internet");
                                    }
                                });
                            }
                        }
                    });


                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogUpdateAnggota.dismiss();
                        }
                    });
                }
            });
        }
    }

    private void getDivisi() {
        showProgressBar("Loading", "Memuat data...", true);
        superAdminService.getAllDivisi().enqueue(new Callback<List<DivisiModel>>() {
            @Override
            public void onResponse(Call<List<DivisiModel>> call, Response<List<DivisiModel>> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    divisiModelList = response.body();
                    spinnerDivisiAdapter = new SpinnerDivisiAdapter(context, divisiModelList);
                    spDivisi.setAdapter(spinnerDivisiAdapter);

                }else {
                    showToast("err", "Gagal memuat data");
                }
            }

            @Override
            public void onFailure(Call<List<DivisiModel>> call, Throwable t) {
                showProgressBar("s", "s", false);
                showToast("err", "Tidak ada koneksi internet");


            }
        });
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
