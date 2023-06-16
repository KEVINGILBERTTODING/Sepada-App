package com.example.sepada.ui.main.user.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.databinding.FragmentUserHomeBinding;
import com.example.sepada.ui.main.auth.LoginActivity;
import com.example.sepada.ui.main.user.adapter.RiwayatTamuAdapter;
import com.example.sepada.util.Constans;
import com.example.sepada.util.UserService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserHomeFragment extends Fragment {
    private FragmentUserHomeBinding binding;
    private AlertDialog progressDialog;
    private GridLayoutManager gridLayoutManager;
    private List<TamuModel> tamuModelList;
    private SharedPreferences sharedPreferences;
    private UserService userService;
    private RiwayatTamuAdapter riwayatTamuAdapter;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constans.USER_ID, null);
        userService = ApiConfig.getClient().create(UserService.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getMyHistory();
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

    }

    private void getMyHistory() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getMyCuti(userId).enqueue(new Callback<List<TamuModel>>() {
            @Override
            public void onResponse(Call<List<TamuModel>> call, Response<List<TamuModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    tamuModelList = response.body();
                    riwayatTamuAdapter = new RiwayatTamuAdapter(getContext(), tamuModelList);
                    gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
                    binding.rvTamu.setLayoutManager(gridLayoutManager);
                    binding.rvTamu.setAdapter(riwayatTamuAdapter);
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
            if (item.getTanggal().toString().contains(text.toLowerCase())) {
                filterList.add(item);
            }

            riwayatTamuAdapter.filter(filterList);
            if (filterList.isEmpty()) {

            }else {
                riwayatTamuAdapter.filter(filterList);
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