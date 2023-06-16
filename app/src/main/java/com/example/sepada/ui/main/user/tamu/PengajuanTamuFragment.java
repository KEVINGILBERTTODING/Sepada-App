package com.example.sepada.ui.main.user.tamu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.databinding.FragmentPengajuanTamuFragmentBinding;
import com.example.sepada.util.Constans;
import com.example.sepada.util.UserService;

import es.dmoral.toasty.Toasty;

public class PengajuanTamuFragment extends Fragment {
    private FragmentPengajuanTamuFragmentBinding binding;
    private UserService userService;
    private SharedPreferences sharedPreferences;
    private String userId, bagian;
    private String [] opsiBagian = {
            "Umum",
            "Keuangan",
            "Persidangan",
            "Humas",
            "Komisi A",
            "Komisi B",
            "Komisi C",
            "Komisi D",
    };
    private AlertDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentPengajuanTamuFragmentBinding.inflate(inflater, container, false);
        userService = ApiConfig.getClient().create(UserService.class);
        sharedPreferences = getContext().getSharedPreferences(Constans.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constans.USER_ID, null);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter adapterBagian = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiBagian);
        adapterBagian.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spBagian.setAdapter(adapterBagian);
        listener();
    }

    private void listener() {
        binding.tvTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker(binding.tvTanggal);

            }
        });
        binding.tvJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(binding.tvJam);

            }
        });
        binding.spBagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bagian = opsiBagian[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    private void getDatePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + datePickerDialog);
            }
        });
        datePickerDialog.show();
    }
    private void timePicker(TextView tvTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String timeFormatted, minuteFormatted;
            if (hourOfDay < 10) {
                timeFormatted = String.format("%02d", hourOfDay);
            }else {
                timeFormatted = String.valueOf(hourOfDay);
            }

            if (minute < 10) {
                minuteFormatted = String.format("%02d", minute);
            }else {
                minuteFormatted = String.valueOf(minute);
            }

            tvTime.setText(timeFormatted + ":" + minuteFormatted);
        }, 0, 0, true);

        timePickerDialog.show();

    }
}