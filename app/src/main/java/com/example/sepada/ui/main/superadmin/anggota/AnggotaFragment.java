package com.example.sepada.ui.main.superadmin.anggota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.AnggotaModel;
import com.example.sepada.data.model.DivisiModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.databinding.FragmentAnggotaSuperadminBinding;
import com.example.sepada.databinding.FragmentDivisiBinding;
import com.example.sepada.ui.main.superadmin.adapter.anggota.AnggotaAdapter;
import com.example.sepada.ui.main.superadmin.adapter.divisi.DivisiAdapter;
import com.example.sepada.ui.main.superadmin.adapter.divisi.SpinnerDivisiAdapter;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnggotaFragment extends Fragment {
    private FragmentAnggotaSuperadminBinding binding;
    private AlertDialog progressDialog;
    private List<AnggotaModel> anggotaModelList;
    private LinearLayoutManager linearLayoutManager;
    private SuperAdminService superAdminService;
    private AnggotaAdapter anggotaAdapter;
    private SpinnerDivisiAdapter spinnerDivisiAdapter;
    private List<DivisiModel> divisiModelList;
    private Integer divisiId;
    private Spinner spDivisi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAnggotaSuperadminBinding.inflate(inflater, container, false);
        superAdminService = ApiConfig.getClient().create(SuperAdminService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllAnggota();

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
                insertAnggota();

            }
        });




    }


    private void getAllAnggota(){
        showProgressBar("Loading", "Memuat data...", true);
        superAdminService.getAllAnggota().enqueue(new Callback<List<AnggotaModel>>() {
            @Override
            public void onResponse(Call<List<AnggotaModel>> call, Response<List<AnggotaModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    anggotaModelList = response.body();
                    anggotaAdapter = new AnggotaAdapter(getContext(), anggotaModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvAnggota.setLayoutManager(linearLayoutManager);
                    binding.rvAnggota.setAdapter(anggotaAdapter);
                    binding.rvAnggota.setHasFixedSize(true);
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

    private void insertAnggota() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_add_anggota);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        EditText etNamaAnggota, etNoTelp;
        Button btnSimpan, btnBatal;
        etNamaAnggota = dialog.findViewById(R.id.etNamaAnggota);
        etNoTelp = dialog.findViewById(R.id.etNoTelp);
        spDivisi = dialog.findViewById(R.id.spDivisi);
        btnBatal = dialog.findViewById(R.id.btnBatal);
        btnSimpan = dialog.findViewById(R.id.btnSimpan);
        getAllDivisi();
        dialog.show();

        spDivisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                divisiId = Integer.parseInt(spinnerDivisiAdapter.getCategoriesId(position));
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
                    showProgressBar("Loading", "Menyimpan data...", true);
                    superAdminService.insertAnggota(
                            etNamaAnggota.getText().toString(),
                            String.valueOf(divisiId),
                            etNoTelp.getText().toString()
                            )
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("S", "s", false);
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        showToast("success", "Berhasil menambahkan anggota baru");
                                        dialog.dismiss();
                                        getAllAnggota();
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

    private void getAllDivisi() {
        showProgressBar("Loading", "Memuat data...", true);
        superAdminService.getAllDivisi().enqueue(new Callback<List<DivisiModel>>() {
            @Override
            public void onResponse(Call<List<DivisiModel>> call, Response<List<DivisiModel>> response) {
                showProgressBar("S", "S", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    divisiModelList = response.body();
                    spinnerDivisiAdapter = new SpinnerDivisiAdapter(getContext(), divisiModelList);
                    spDivisi.setAdapter(spinnerDivisiAdapter);
                }else {
                    showToast("err", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<List<DivisiModel>> call, Throwable t) {
                showProgressBar("S", "S", false);
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