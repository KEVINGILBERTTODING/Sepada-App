package com.example.sepada.ui.main.admin.adapter.divisi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.DivisiModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.ui.main.admin.jadwal.JadwalFragment;
import com.example.sepada.util.SuperAdminService;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisiAdapter extends RecyclerView.Adapter<DivisiAdapter.ViewHolder> {
    private Context context;
    private List<DivisiModel> divisiModelList;
    private AlertDialog progressDialog;

    private SuperAdminService superAdminService;

    public DivisiAdapter(Context context, List<DivisiModel> divisiModelList) {
        this.context = context;
        this.divisiModelList = divisiModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_divisi_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDivisi.setText(divisiModelList.get(holder.getAdapterPosition()).getNamaDivisi());
    }

    @Override
    public int getItemCount() {
        return divisiModelList.size();
    }

    public void filter(ArrayList<DivisiModel> filteredList) {
        divisiModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDivisi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDivisi = itemView.findViewById(R.id.tvDivisi);
            superAdminService = ApiConfig.getClient().create(SuperAdminService.class);
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            Fragment fragment = new JadwalFragment();
            Bundle bundle = new Bundle();
            bundle.putString("divisi_id", divisiModelList.get(getAdapterPosition()).getId());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, fragment)
                    .addToBackStack(null).commit();

        }
    }


}
