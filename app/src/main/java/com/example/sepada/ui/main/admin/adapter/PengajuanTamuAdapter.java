package com.example.sepada.ui.main.admin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.ui.main.admin.tamu.DetailPengajuanFragment;

import java.util.ArrayList;
import java.util.List;

public class PengajuanTamuAdapter extends RecyclerView.Adapter<PengajuanTamuAdapter.ViewHolder> {
    Context context;
    List<TamuModel> tamuModelList;

    public PengajuanTamuAdapter(Context context, List<TamuModel> tamuModelList) {
        this.context = context;
        this.tamuModelList = tamuModelList;
    }

    @NonNull
    @Override
    public PengajuanTamuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_tamu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengajuanTamuAdapter.ViewHolder holder, int position) {
        holder.tvDate.setText(tamuModelList.get(holder.getAdapterPosition()).getTanggal());
        holder.tvTime.setText(tamuModelList.get(holder.getAdapterPosition()).getJam());
        holder.tvInstansi.setText(tamuModelList.get(holder.getAdapterPosition()).getNamaLengkap());
        holder.tvTotalPerson.setText(tamuModelList.get(holder.getAdapterPosition()).getJumlah());

        if (tamuModelList.get(holder.getAdapterPosition()).getIdStatus().equals("1")) {
            holder.tvStatus.setText("Menunggu");
            holder.tvStatus.setTextColor(context.getColor(R.color.yellow));
        }else if (tamuModelList.get(holder.getAdapterPosition()).getIdStatus().equals("2")) {
            holder.tvStatus.setText("Diterima");
            holder.tvStatus.setTextColor(context.getColor(R.color.green_light));
        }else if (tamuModelList.get(holder.getAdapterPosition()).getIdStatus().equals("3")) {
            holder.tvStatus.setText("Ditolak");
            holder.tvStatus.setTextColor(context.getColor(R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return tamuModelList.size();
    }

    public void filter(ArrayList<TamuModel> filteredList) {
        tamuModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTime, tvDate, tvInstansi, tvTotalPerson, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInstansi = itemView.findViewById(R.id.tvInstansi);
            tvTotalPerson = itemView.findViewById(R.id.tvTotalPerson);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Fragment fragment = new DetailPengajuanFragment();
            Bundle bundle = new Bundle();
            bundle.putString("nama_instansi", tamuModelList.get(getAdapterPosition()).getNamaLengkap());
            bundle.putString("tujuan", tamuModelList.get(getAdapterPosition()).getTujuan());
            bundle.putString("tanggal", tamuModelList.get(getAdapterPosition()).getTanggal());
            bundle.putString("waktu", tamuModelList.get(getAdapterPosition()).getJam());
            bundle.putString("status", tamuModelList.get(getAdapterPosition()).getIdStatus());
            bundle.putString("alasan_verifikasi", tamuModelList.get(getAdapterPosition()).getAlasanVerifikasi());
            bundle.putString("jumlah", tamuModelList.get(getAdapterPosition()).getJumlah());
            bundle.putString("alasan", tamuModelList.get(getAdapterPosition()).getAlasan());
            bundle.putString("id", tamuModelList.get(getAdapterPosition()).getIdTamu());
            bundle.putString("file", tamuModelList.get(getAdapterPosition()).getFile());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameAdmin, fragment).addToBackStack(null).commit();

        }
    }
}
