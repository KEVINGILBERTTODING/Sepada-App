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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.AnggotaModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.databinding.FragmentAdminDetailPengajuanBinding;
import com.example.sepada.databinding.FragmentSuperAdminDetailPengajuanBinding;
import com.example.sepada.ui.main.admin.adapter.anggota.AnggotaAdapter;
import com.example.sepada.ui.main.superadmin.adapter.anggota.SpinnerAnggotaAdapter;
import com.example.sepada.util.AdminService;
import com.example.sepada.util.Constans;
import com.example.sepada.util.SuperAdminService;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPengajuanFragment extends Fragment {

    private String id;
    private FragmentSuperAdminDetailPengajuanBinding binding;
    private AlertDialog progressDialog;
    private SuperAdminService superAdminService;
    private String dayName, divisiId, anggotaId;
    private SpinnerAnggotaAdapter spinnerAnggotaAdapter;
    private AdminService adminService;
    private Spinner spAnggota;
    private List<AnggotaModel> anggotaModelList;
    private Boolean cekAnggota;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSuperAdminDetailPengajuanBinding.inflate(inflater, container, false);
        superAdminService = ApiConfig.getClient().create(SuperAdminService.class);
        adminService = ApiConfig.getClient().create(AdminService.class);

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
        getDayName();
        cekDivisiId(getArguments().getString("tujuan"));
        getTamuById();
        listener();
    }

    private void cekDivisiId(String divisiName)
    {
        if (divisiName.equals("Umum")) {
            divisiId = String.valueOf("1");
        }else if (divisiName.equals("Keuangan")) {
            divisiId = String.valueOf("2");
        }else if (divisiName.equals("Persidangan")) {
            divisiId = String.valueOf("3");
        }else if (divisiName.equals("Humas")) {
            divisiId = String.valueOf("4");
        }else if (divisiName.equals("Komisi A")) {
            divisiId = String.valueOf("5");
        }else if (divisiName.equals("Komisi B")) {
            divisiId = String.valueOf("6");
        }else if (divisiName.equals("Komisi C")) {
            divisiId = String.valueOf("7");
        }else if (divisiName.equals("Komisi D")) {
            divisiId = String.valueOf("8");
        }

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
        spAnggota = dialog.findViewById(R.id.spAnggota);
        dialog.show();
        getAllAnggota();

        spAnggota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                anggotaId = spinnerAnggotaAdapter.getCategoriesId(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cekAnggota == false) {
                    showToast("err", "Tidak ada jadwal anggota yang hadir");
                }
                else {
                    showProgressBar("Loading", "Menyimpan keputusan...", true);
                    superAdminService.keputusanAcc(2, id, anggotaId).enqueue(new Callback<ResponseModel>() {
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

    private void getDayName() {
        showProgressBar("Loading", "Memuat data...", true);
        superAdminService.getDay(getArguments().getString("id")).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body() != null) {
                    dayName = response.body().getDay();

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


    private void getAllAnggota(){
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAnggotaJadwal(divisiId, dayName).enqueue(new Callback<List<AnggotaModel>>() {
            @Override
            public void onResponse(Call<List<AnggotaModel>> call, Response<List<AnggotaModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    anggotaModelList = response.body();
                    spinnerAnggotaAdapter = new SpinnerAnggotaAdapter(getContext(), anggotaModelList);
                    spAnggota.setAdapter(spinnerAnggotaAdapter);
                    cekAnggota = true;

                }else {
                    cekAnggota = false;


                }
            }

            @Override
            public void onFailure(Call<List<AnggotaModel>> call, Throwable t) {
                showProgressBar("d", "ds", false);
                cekAnggota = false;
                showToast("err", "Tidak ada koneksi internet");

            }
        });


    }

    private void getTamuById() {
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