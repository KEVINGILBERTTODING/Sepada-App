package com.example.sepada.ui.main.admin.tamu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.data.model.UserDetailModel;
import com.example.sepada.databinding.FragmentTamuBinding;
import com.example.sepada.databinding.FragmentUserHomeBinding;
import com.example.sepada.ui.main.admin.adapter.PengajuanTamuAdapter;
import com.example.sepada.ui.main.user.adapter.RiwayatTamuAdapter;
import com.example.sepada.ui.main.user.profile.UpdateProfileFragment;
import com.example.sepada.ui.main.user.tamu.PengajuanTamuFragment;
import com.example.sepada.util.AdminService;
import com.example.sepada.util.Constans;
import com.example.sepada.util.UserService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TamuFragment extends Fragment {
    private FragmentTamuBinding binding;
    private AlertDialog progressDialog;
    private GridLayoutManager gridLayoutManager;
    private List<TamuModel> tamuModelList;
    private AdminService adminService;
    private String status;
    private PengajuanTamuAdapter pengajuanTamuAdapter;
    private String [] opsiStatus = {
            "Semua", "Disetujui", "Ditolak", "Diproses"
    };
    private Spinner spStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTamuBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);




        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Semua"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Disetujui"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Menunggu"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ditolak"));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllPengajuan("all");

    }

    private void listener() {





        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.rvTamu.setAdapter(null);
                    getAllPengajuan("all");
                }else if (tab.getPosition() == 1) {
                    binding.rvTamu.setAdapter(null);

                    getAllPengajuan("2");
                }else if (tab.getPosition() == 2) {
                    binding.rvTamu.setAdapter(null);

                    getAllPengajuan("1");
                }else if (tab.getPosition() == 3) {
                    binding.rvTamu.setAdapter(null);

                    getAllPengajuan("3");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_filter);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                spStatus = dialog.findViewById(R.id.spStatus);
                TextView tvTanggalAwal, tvTanggalAkhir;
                tvTanggalAwal = dialog.findViewById(R.id.tvTanggalAwal);
                tvTanggalAkhir = dialog.findViewById(R.id.tvtglAkhir);
                Button btnDownload, btnBatal;
                btnBatal = dialog.findViewById(R.id.btnBatal);
                btnDownload = dialog.findViewById(R.id.btnDownload);
                dialog.show();

                ArrayAdapter statusAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiStatus);
                statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spStatus.setAdapter(statusAdapter);

                spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        status = opsiStatus[position];
                        if (status.equals("Semua")) {
                            status = "all";
                        }else if (status.equals("Disetujui")) {
                            status = "2";
                        }else if (status.equals("Ditolak")) {
                            status = "3";
                        }else if (status.equals("Diproses")) {
                            status = "1";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                tvTanggalAwal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDatePicker(tvTanggalAwal);
                    }
                });

                tvTanggalAkhir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDatePicker(tvTanggalAkhir);
                    }
                });

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tvTanggalAkhir.getText().toString().isEmpty()) {
                            tvTanggalAkhir.requestFocus();
                        }else if (tvTanggalAkhir.getText().toString().isEmpty()) {
                            tvTanggalAkhir.requestFocus();
                        }else {
                            String url = Constans.URL_DOWNLOAD_REKAP_DATA + status + "/" + tvTanggalAwal.getText().toString() +
                                    "/" + tvTanggalAkhir.getText().toString();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }


    private void getAllPengajuan(String status) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllPengajuanByStatus(status).enqueue(new Callback<List<TamuModel>>() {
            @Override
            public void onResponse(Call<List<TamuModel>> call, Response<List<TamuModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    tamuModelList = response.body();
                    pengajuanTamuAdapter = new PengajuanTamuAdapter(getContext(), tamuModelList);
                    gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
                    binding.rvTamu.setLayoutManager(gridLayoutManager);
                    binding.rvTamu.setAdapter(pengajuanTamuAdapter);
                    binding.rvTamu.setHasFixedSize(true);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<TamuModel>> call, Throwable t) {
                showProgressBar("d", "ds", false);
                showToast("err", "Tidak ada koneksi internet");
            }
        });


    }

    private void filter(String text) {
        ArrayList<TamuModel> filterList = new ArrayList<>();
        for (TamuModel item : tamuModelList) {
            if (item.getNamaLengkap().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }

            pengajuanTamuAdapter.filter(filterList);
            if (filterList.isEmpty()) {

            }else {
                pengajuanTamuAdapter.filter(filterList);
            }
        }
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

    private void getDatePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }
        });
        datePickerDialog.show();
    }


}