package com.example.sepada.ui.main.admin.jadwal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.AnggotaModel;
import com.example.sepada.data.model.DivisiModel;
import com.example.sepada.databinding.FragmentAdminDivisiBinding;
import com.example.sepada.databinding.FragmentJadwalBinding;
import com.example.sepada.ui.main.admin.adapter.anggota.AnggotaAdapter;
import com.example.sepada.ui.main.admin.adapter.divisi.DivisiAdapter;
import com.example.sepada.util.AdminService;
import com.example.sepada.util.SuperAdminService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JadwalFragment extends Fragment {
    private FragmentJadwalBinding binding;
    private AlertDialog progressDialog;
    private List<AnggotaModel> anggotaModelList;
    private LinearLayoutManager linearLayoutManager;
    private AdminService adminService;
    private AnggotaAdapter anggotaAdapter;
    private String divisiId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJadwalBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Senin"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Selasa"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Rabu"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Kamis"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Jumat"));
        divisiId = getArguments().getString("divisi_id");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllAnggota("Senin");


    }

    private void listener() {

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

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.rvUsers.setAdapter(null);
                    getAllAnggota("Senin");
                }else if (tab.getPosition() == 1) {
                    binding.rvUsers.setAdapter(null);
                    getAllAnggota("Selasa");
                }else if (tab.getPosition() == 2) {
                    binding.rvUsers.setAdapter(null);
                    getAllAnggota("Rabu");
                }else if (tab.getPosition() == 3) {
                    binding.rvUsers.setAdapter(null);
                    getAllAnggota("Kamis");
                }else if (tab.getPosition() == 4) {
                    binding.rvUsers.setAdapter(null);
                    getAllAnggota("Jumat");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });






    }


    private void getAllAnggota (String day){
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAnggotaJadwal(divisiId, day).enqueue(new Callback<List<AnggotaModel>>() {
            @Override
            public void onResponse(Call<List<AnggotaModel>> call, Response<List<AnggotaModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    anggotaModelList = response.body();
                    anggotaAdapter = new AnggotaAdapter(getContext(), anggotaModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvUsers.setLayoutManager(linearLayoutManager);
                    binding.rvUsers.setAdapter(anggotaAdapter);
                    binding.rvUsers.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<AnggotaModel>> call, Throwable t) {
                showProgressBar("d", "ds", false);
                binding.tvEmpty.setVisibility(View.GONE);
                showToast("err", "Tidak ada koneksi internet");
            }
        });


    }

    private void filter(String text) {
        ArrayList<AnggotaModel> filterList = new ArrayList<>();
        for (AnggotaModel item : anggotaModelList) {
            if (item.getNamaDivisi().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }

            anggotaAdapter.filter(filterList);
            if (filterList.isEmpty()) {

            }else {
                anggotaAdapter.filter(filterList);
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


}