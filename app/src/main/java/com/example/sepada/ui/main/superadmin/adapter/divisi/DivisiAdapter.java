package com.example.sepada.ui.main.superadmin.adapter.divisi;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.model.DivisiModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DivisiAdapter extends RecyclerView.Adapter<DivisiAdapter.ViewHolder> {
    private Context context;
    private List<DivisiModel> divisiModelList;
    private AlertDialog progressDialog;

    public DivisiAdapter(Context context, List<DivisiModel> divisiModelList) {
        this.context = context;
        this.divisiModelList = divisiModelList;
    }

    @NonNull
    @Override
    public DivisiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_divisi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DivisiAdapter.ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDivisi;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDivisi = itemView.findViewById(R.id.tvDivisi);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            Toasty.success(context, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(context, text, Toasty.LENGTH_SHORT).show();
        }
    }

}
