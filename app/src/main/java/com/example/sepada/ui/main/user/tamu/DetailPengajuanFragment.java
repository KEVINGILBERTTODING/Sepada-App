package com.example.sepada.ui.main.user.tamu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.databinding.FragmentDetailPengajuanBinding;
import com.example.sepada.util.Constans;
import com.example.sepada.util.SuperAdminService;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPengajuanFragment extends Fragment {

    private String id;
    private FragmentDetailPengajuanBinding binding;
    private SuperAdminService superAdminService;
    private AlertDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailPengajuanBinding.inflate(inflater, container, false);
        superAdminService = ApiConfig.getClient().create(SuperAdminService.class);

        binding.etNamInstansi.setText(getArguments().getString("nama_instansi"));
        binding.etTujuanBagian.setText(getArguments().getString("tujuan"));
        binding.etTglKedatangan.setText(getArguments().getString("tanggal"));
        binding.etWaktuKedatangan.setText(getArguments().getString("waktu"));
        binding.etJumlah.setText(getArguments().getString("jumlah"));
        binding.etAlasan.setText(getArguments().getString("alasan"));
        id = getArguments().getString("id");

        if (getArguments().getString("status").equals("2")) {
            binding.btnDownload.setVisibility(View.VISIBLE);
        }else {
            binding.btnDownload.setVisibility(View.GONE);
        }

        if (!getArguments().getString("status").equals("1")) {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.layout_alasan_verifikasi);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            EditText etAlasan = dialog.findViewById(R.id.etAlasanVerifikasi);
            TextView tvStatus = dialog.findViewById(R.id.tvStatus);

            Button btnOKe = dialog.findViewById(R.id.btnOke);

            if (getArguments().getString("status").equals("2")) {
                tvStatus.setText("Disetujui");
                etAlasan.setVisibility(View.GONE);
                tvStatus.setTextColor(getContext().getColor(R.color.main));
            }else {
                tvStatus.setText("Ditolak");
                tvStatus.setTextColor(getContext().getColor(R.color.red));

            }

            etAlasan.setText(getArguments().getString("alasan_verifikasi"));
            dialog.show();
            btnOKe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTamuById();
        listener();
    }


    private void getTamuById() {
        Log.d("tamu id", "getTamuById: " + id);
        showProgressBar("Loading", "Memuat data...", true);
        superAdminService.getTamuById(id).enqueue(new Callback<TamuModel>() {
            @Override
            public void onResponse(Call<TamuModel> call, Response<TamuModel> response) {
                showProgressBar("", "",false);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getNamaAnggota() != null) {
                        binding.etNamaAnggota.setText(response.body().getNamaAnggota());
                    } else {
                        binding.etNamaAnggota.setText("Tidak ada data");
                    }
                }else {
                    showToast("err", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<TamuModel> call, Throwable t) {
                showProgressBar("", "",false);
                binding.etNamaAnggota.setText("Tidak ada data");
                showToast("err", "Tidak ada koneksi internet");



            }
        });
    }

    private void listener() {
        binding.btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constans.URL_DOWNLOAD_SURAT_PERSETUJUAN + id));
                startActivity(intent);
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