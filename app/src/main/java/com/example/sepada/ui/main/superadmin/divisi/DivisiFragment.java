package com.example.sepada.ui.main.superadmin.divisi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.DivisiModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.databinding.FragmentDivisiBinding;
import com.example.sepada.databinding.FragmentUsersBinding;
import com.example.sepada.ui.main.admin.users.AddUsers;
import com.example.sepada.ui.main.superadmin.adapter.divisi.DivisiAdapter;
import com.example.sepada.ui.main.superadmin.adapter.user.UsersAdapter;
import com.example.sepada.util.AdminService;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DivisiFragment extends Fragment {
    private FragmentDivisiBinding binding;
    private AlertDialog progressDialog;
    private List<DivisiModel> divisiModelList;
    private LinearLayoutManager linearLayoutManager;
    private SuperAdminService superAdminService;
    private DivisiAdapter divisiAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDivisiBinding.inflate(inflater, container, false);
        superAdminService = ApiConfig.getClient().create(SuperAdminService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllDivisi();

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

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDivisi();

            }
        });




    }


    private void getAllDivisi (){
        showProgressBar("Loading", "Memuat data...", true);
        superAdminService.getAllDivisi().enqueue(new Callback<List<DivisiModel>>() {
            @Override
            public void onResponse(Call<List<DivisiModel>> call, Response<List<DivisiModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    divisiModelList = response.body();
                    divisiAdapter = new DivisiAdapter(getContext(), divisiModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvDivisi.setLayoutManager(linearLayoutManager);
                    binding.rvDivisi.setAdapter(divisiAdapter);
                    binding.rvDivisi.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<DivisiModel>> call, Throwable t) {
                showProgressBar("d", "ds", false);
                binding.tvEmpty.setVisibility(View.GONE);
                showToast("err", "Tidak ada koneksi internet");
            }
        });


    }

    private void insertDivisi() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_add_divisi);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        EditText etNamaDivis;
        Button btnSimpan, btnBatal;
        etNamaDivis = dialog.findViewById(R.id.etNamaDivisi);
        btnBatal = dialog.findViewById(R.id.btnBatal);
        btnSimpan = dialog.findViewById(R.id.btnSimpan);
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
                if (etNamaDivis.getText().toString().isEmpty()) {
                    etNamaDivis.setError("Tidak boleh kosong");
                    etNamaDivis.requestFocus();
                }else {
                    showProgressBar("Loadinhg", "Menyimpan data...", true);
                    superAdminService.insertDivisi(etNamaDivis.getText().toString())
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("S", "s", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        showToast("success", "Berhasil menambahkan divisi baru");
                                        dialog.dismiss();
                                        getAllDivisi();
                                    }else {
                                        showToast("err", "Terjadi kesalahan");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showProgressBar("S", "s", false);
                                    showToast("err", "Tidak ada koneksi internet");



                                }
                            });
                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<DivisiModel> filterList = new ArrayList<>();
        for (DivisiModel item : divisiModelList) {
            if (item.getNamaDivisi().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }

            divisiAdapter.filter(filterList);
            if (filterList.isEmpty()) {

            }else {
                divisiAdapter.filter(filterList);
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