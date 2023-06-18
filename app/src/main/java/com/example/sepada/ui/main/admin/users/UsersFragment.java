package com.example.sepada.ui.main.admin.users;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.L;
import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.UserDetailModel;
import com.example.sepada.databinding.FragmentUsersBinding;
import com.example.sepada.ui.main.admin.adapter.tamu.PengajuanTamuAdapter;
import com.example.sepada.ui.main.admin.adapter.user.UsersAdapter;
import com.example.sepada.util.AdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UsersFragment extends Fragment {
    private FragmentUsersBinding binding;
    private AlertDialog progressDialog;
    private List<UserDetailModel> userDetailModelList;
    private LinearLayoutManager linearLayoutManager;
    private AdminService adminService;
    private UsersAdapter usersAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllUsers(1);

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


    }


    private void getAllUsers(Integer role) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllUsersByRole(role).enqueue(new Callback<List<UserDetailModel>>() {
            @Override
            public void onResponse(Call<List<UserDetailModel>> call, Response<List<UserDetailModel>> response) {
                showProgressBar("d", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    userDetailModelList = response.body();
                    usersAdapter = new UsersAdapter(getContext(), userDetailModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvUsers.setLayoutManager(linearLayoutManager);
                    binding.rvUsers.setAdapter(usersAdapter);
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

            usersAdapter.filter(filterList);
            if (filterList.isEmpty()) {

            }else {
                usersAdapter.filter(filterList);
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