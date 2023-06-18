package com.example.sepada.ui.main.superadmin.admin;

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
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.UserDetailModel;
import com.example.sepada.databinding.FragmentUsersBinding;
import com.example.sepada.ui.main.admin.adapter.user.UsersAdapter;
import com.example.sepada.ui.main.admin.users.AddUsers;
import com.example.sepada.ui.main.superadmin.adapter.admin.AdminAdapter;
import com.example.sepada.util.AdminService;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminFragment extends Fragment {
    private FragmentUsersBinding binding;
    private AlertDialog progressDialog;
    private List<UserDetailModel> userDetailModelList;
    private LinearLayoutManager linearLayoutManager;
    private SuperAdminService superAdminService;
    private AdminService adminService;
    private AdminAdapter adminAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);
        superAdminService = ApiConfig.getClient().create(SuperAdminService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllUsers(2);

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
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameAdmin, new AddUsers()).addToBackStack(null).commit();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAdmin();
            }
        });


    }


    private void getAllUsers(Integer role) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllUsersByRole(role).enqueue(new Callback<List<UserDetailModel>>() {
            @Override
            public void onResponse(Call<List<UserDetailModel>> call, Response<List<UserDetailModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    userDetailModelList = response.body();
                    adminAdapter = new AdminAdapter(getContext(), userDetailModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvUsers.setLayoutManager(linearLayoutManager);
                    binding.rvUsers.setAdapter(adminAdapter);
                    binding.rvUsers.setHasFixedSize(true);

                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<UserDetailModel>> call, Throwable t) {
                showProgressBar("d", "ds", false);
                showToast("err", "Tidak ada koneksi internet");
            }
        });


    }

    private void filter(String text) {
        ArrayList<UserDetailModel> filterList = new ArrayList<>();
        for (UserDetailModel item : userDetailModelList) {
            if (item.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }

            adminAdapter.filter(filterList);
            if (filterList.isEmpty()) {

            }else {
                adminAdapter.filter(filterList);
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

    private void addAdmin() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_add_admin);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnSimpan, btnBatal;
        EditText etUsername, etPassword, etEmail;
        btnBatal = dialog.findViewById(R.id.btnBatal);
        btnSimpan = dialog.findViewById(R.id.btnSimpan);
        etUsername = dialog.findViewById(R.id.etUsername);
        etPassword = dialog.findViewById(R.id.etPassword);
        etEmail = dialog.findViewById(R.id.etEmail);
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
                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Tidak boleh kosong");
                }else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Tidak boleh kosong");
                }else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Tidak boleh kosong");
                }else {
                    showProgressBar("Loading", "Menyimpan data...", true);
                    superAdminService.addAdmin(
                            etUsername.getText().toString(),
                            etPassword.getText().toString(),
                            etEmail.getText().toString()
                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("d", "s", false);
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                showToast("success", "Berhasil menambahkan admin");
                                dialog.dismiss();
                                getAllUsers(2);
                            }else {
                                showToast("err", response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            showProgressBar("d", "s", false);
                            showToast("err", "Tidak ada koneksi internet");


                        }
                    });
                }
            }
        });
    }


}