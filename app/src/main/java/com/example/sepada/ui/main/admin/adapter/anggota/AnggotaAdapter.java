package com.example.sepada.ui.main.admin.adapter.anggota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.sepada.util.AdminService;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.ViewHolder> {
    private Context context;
    private List<AnggotaModel> anggotaModelList;
    private List<DivisiModel> divisiModelList;
    private AlertDialog progressDialog;
    private AdminService adminService;
    private SpinnerDivisiAdapter spinnerDivisiAdapter;
    private Spinner spDivisi;
    private Integer divisiId;
    private String day;
    private String [] dayOption = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat"};


    public AnggotaAdapter(Context context, List<AnggotaModel> anggotaModelList) {
        this.context = context;
        this.anggotaModelList = anggotaModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_anggota_jadwal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNamaAnggota, tvDivisi;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaAnggota = itemView.findViewById(R.id.tvNamaAnggota);
            tvDivisi = itemView.findViewById(R.id.tvDivisi);
            adminService = ApiConfig.getClient().create(AdminService.class);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Dialog dialogJadwal = new Dialog(context);
            dialogJadwal.setContentView(R.layout.layout_update_jadwal);
            dialogJadwal.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button btnSimpan, btnBatal;
            btnSimpan = dialogJadwal.findViewById(R.id.btnSimpan);
            btnBatal = dialogJadwal.findViewById(R.id.btnBatal);
            Spinner spDay = dialogJadwal.findViewById(R.id.spDay);

            ArrayAdapter dayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dayOption);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spDay.setAdapter(dayAdapter);
            dialogJadwal.show();

            spDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    day = dayOption[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogJadwal.dismiss();
                }
            });

            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar("Loading", "Menyimpan perubahan...", true);
                    adminService.updateAnggota(
                            anggotaModelList.get(getAdapterPosition()).getAnggotaId(),
                            day
                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("", "", false);
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                showToast("success", "Berhasil mengubah jadwal");
                                anggotaModelList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                dialogJadwal.dismiss();
                            }else {
                                showToast("err", "Gagal mengubah jadwal");

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            showProgressBar("s", "s", false);
                            showToast("err", "terjadi kesalahan");

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
