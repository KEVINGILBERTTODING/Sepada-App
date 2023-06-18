package com.example.sepada.ui.main.superadmin.tamu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.databinding.FragmentAdminDetailPengajuanBinding;
import com.example.sepada.databinding.FragmentSuperAdminDetailPengajuanBinding;
import com.example.sepada.util.AdminService;
import com.example.sepada.util.Constans;
import com.example.sepada.util.SuperAdminService;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPengajuanFragment extends Fragment {

    private String id;
    private FragmentSuperAdminDetailPengajuanBinding binding;
    private AlertDialog progressDialog;
    private SuperAdminService superAdminService;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuperAdminDetailPengajuanBinding.inflate(inflater, container, false);
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

        if (getArguments().getString("file").equals("")) {
            binding.downloadSuratLampiran.setVisibility(View.GONE);
        }else {
            binding.downloadSuratLampiran.setVisibility(View.VISIBLE);
        }

        if (getArguments().getString("alasan_verifikasi") != null) {
            binding.etAlasanverifikasi.setVisibility(View.VISIBLE);
            binding.etAlasanverifikasi.setText(getArguments().getString("alasan_verifikasi"));
        }else {
            binding.etAlasanverifikasi.setVisibility(View.GONE);
        }

        if (getArguments().getString("status").equals("2")) {
            binding.btnSetuju.setVisibility(View.GONE);
            binding.btnTolak.setVisibility(View.VISIBLE);

        }else if (getArguments().getString("status").equals("3")){
            binding.btnTolak.setVisibility(View.GONE);
            binding.btnSetuju.setVisibility(View.VISIBLE);
        }




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        listener();
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
        binding.downloadSuratLampiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url =  Constans.URL_DOWNLOAD_SURAT_LAMPIRAN + id;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        binding.btnSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setuju();
            }

        });

        binding.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tolak();
            }
        });



    }

    private void setuju() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_persetujuan);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnBatal, btnSetuju;
        EditText etAlasan = dialog.findViewById(R.id.etAlasan);
        btnSetuju = dialog.findViewById(R.id.btnSimpan);
        btnBatal = dialog.findViewById(R.id.btnBatal);
        dialog.show();

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAlasan.getText().toString().isEmpty()) {
                    etAlasan.setError("Tidak boleh kosong");
                }else {
                    showProgressBar("Loading", "Menyimpan keputusan...", true);
                    superAdminService.keputusan(2, id, etAlasan.getText().toString()).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("d", "d", false);

                            if (response.isSuccessful() && response.body().getCode() == 200){
                                showToast("success", "Berhasil menyetujui tamu");
                                getActivity().onBackPressed();
                                dialog.dismiss();

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
            }
        });
    }
    private void tolak() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_penolakan);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnBatal, btnSetuju;
        EditText etAlasan = dialog.findViewById(R.id.etAlasan);
        btnSetuju = dialog.findViewById(R.id.btnSimpan);
        btnBatal = dialog.findViewById(R.id.btnBatal);
        dialog.show();

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAlasan.getText().toString().isEmpty()) {
                    etAlasan.setError("Tidak boleh kosong");
                }else {
                    showProgressBar("Loading", "Menyimpan keputusan...", true);
                    superAdminService.keputusan(3, id, etAlasan.getText().toString()).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("d", "d", false);

                            if (response.isSuccessful() && response.body().getCode() == 200){
                                showToast("success", "Berhasil menolak tamu");
                                getActivity().onBackPressed();
                                dialog.dismiss();

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