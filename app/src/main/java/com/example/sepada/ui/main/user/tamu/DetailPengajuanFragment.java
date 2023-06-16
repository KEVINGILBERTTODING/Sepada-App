package com.example.sepada.ui.main.user.tamu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sepada.R;
import com.example.sepada.databinding.FragmentDetailPengajuanBinding;
import com.example.sepada.util.Constans;

public class DetailPengajuanFragment extends Fragment {

    private String id;
    private FragmentDetailPengajuanBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailPengajuanBinding.inflate(inflater, container, false);

        binding.etNamInstansi.setText(getArguments().getString("nama_instansi"));
        binding.etTujuanBagian.setText(getArguments().getString("tujuan"));
        binding.etTglKedatangan.setText(getArguments().getString("tanggal"));
        binding.etWaktuKedatangan.setText(getArguments().getString("waktu"));
        binding.etJumlah.setText(getArguments().getString("jumlah"));
        binding.etAlasan.setText(getArguments().getString("alasan"));
        id = getArguments().getString("id");

        if (getArguments().getString("status").equals("2")) {
            binding.btnDownload.setVisibility(View.VISIBLE);
        }else {
            binding.btnDownload.setVisibility(View.GONE);
        }


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void listener() {
        binding.btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constans.URL_DOWNLOAD_SURAT_PERSETUJUAN + id));
                startActivity(intent);
            }
        });

    }
}