package com.example.sepada.ui.main.admin.home;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.databinding.FragmentAdminHomeBinding;
import com.example.sepada.databinding.FragmentUserProfileBinding;
import com.example.sepada.ui.main.admin.tamu.TamuFragment;
import com.example.sepada.util.AdminService;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends Fragment {

    private FragmentAdminHomeBinding binding;
    private AdminService adminService;
    private AlertDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);

        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllPengajuan();
    }

    private void listener() {
        binding.cvMenuTamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new TamuFragment());

            }
        });
    }

    private void getAllPengajuan() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllPengajuan().enqueue(new Callback<List<TamuModel>>() {
            @Override
            public void onResponse(Call<List<TamuModel>> call, Response<List<TamuModel>> response) {
                showProgressBar("sd", "ds", false);
                if (response.isSuccessful() && response.body().size() > 0) {
                    binding.tvTotalPengajuan.setText(String.valueOf(response.body().size()));
                }else {
                    binding.tvTotalPengajuan.setText("0");
                }

            }

            @Override
            public void onFailure(Call<List<TamuModel>> call, Throwable t) {
                showProgressBar("D", "D", false);
                showToast("err", "Tidak ada koneksi internt");

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

    private void replace(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameAdmin, fragment).addToBackStack(null).commit();
    }
}