package com.example.sepada.ui.main.admin.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.model.UserDetailModel;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    Context context;
    List<UserDetailModel> userDetailModelList;

    public UsersAdapter(Context context, List<UserDetailModel> userDetailModelList) {
        this.context = context;
        this.userDetailModelList = userDetailModelList;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        holder.tvUsername.setText(userDetailModelList.get(holder.getAdapterPosition()).getUsername());
        holder.tvInstansi.setText(userDetailModelList.get(holder.getAdapterPosition()).getNamaLengkap());

    }

    @Override
    public int getItemCount() {
        return userDetailModelList.size();
    }

    public void filter(ArrayList<UserDetailModel> filteredList) {
        userDetailModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvInstansi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvInstansi = itemView.findViewById(R.id.tvInstansi);
        }
    }
}
