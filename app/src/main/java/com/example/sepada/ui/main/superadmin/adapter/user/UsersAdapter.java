package com.example.sepada.ui.main.superadmin.adapter.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sepada.R;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.UserDetailModel;
import com.example.sepada.ui.main.admin.users.UpdateUsers;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    Context context;
    private AlertDialog progressDialog;
    List<UserDetailModel> userDetailModelList;
    public UsersAdapter(Context context, List<UserDetailModel> userDetailModelList) {
        this.context = context;
        this.userDetailModelList = userDetailModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsername, tvInstansi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvInstansi = itemView.findViewById(R.id.tvInstansi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Fragment fragment = new UpdateUsers();
            Bundle bundle = new Bundle();
            bundle.putString("id", userDetailModelList.get(getAdapterPosition()).getUserId());
            bundle.putString("username", userDetailModelList.get(getAdapterPosition()).getUsername());
            bundle.putString("email", userDetailModelList.get(getAdapterPosition()).getEmail());
            bundle.putString("password", userDetailModelList.get(getAdapterPosition()).getPassword());
            bundle.putString("nama_instansi", userDetailModelList.get(getAdapterPosition()).getNamaLengkap());
            bundle.putString("telepon", userDetailModelList.get(getAdapterPosition()).getNoTelp());
            bundle.putString("alamat_instansi", userDetailModelList.get(getAdapterPosition()).getAlamat());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameSuperAdmin, fragment).addToBackStack(null)
                    .commit();


        }


        }
    }



