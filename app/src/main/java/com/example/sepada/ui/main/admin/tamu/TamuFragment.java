package com.example.sepada.ui.main.admin.tamu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private PengajuanTamuAdapter pengajuanTamuAdapter;

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


}