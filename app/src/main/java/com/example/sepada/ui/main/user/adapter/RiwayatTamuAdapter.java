package com.example.sepada.ui.main.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.model.TamuModel;

import java.util.ArrayList;
import java.util.List;

public class RiwayatTamuAdapter extends RecyclerView.Adapter<RiwayatTamuAdapter.ViewHolder> {
    Context context;
    List<TamuModel> tamuModelList;

    public RiwayatTamuAdapter(Context context, List<TamuModel> tamuModelList) {
        this.context = context;
        this.tamuModelList = tamuModelList;
    }

    @NonNull
    @Override
    public RiwayatTamuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_tamu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatTamuAdapter.ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvDate, tvInstansi, tvTotalPerson, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInstansi = itemView.findViewById(R.id.tvInstansi);
            tvTotalPerson = itemView.findViewById(R.id.tvTotalPerson);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }



    }
}
