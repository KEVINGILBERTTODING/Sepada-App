package com.example.sepada.ui.main.admin.tamu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sepada.R;
import com.example.sepada.data.api.ApiConfig;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.databinding.FragmentAdminDetailPengajuanBinding;
import com.example.sepada.databinding.FragmentDetailPengajuanBinding;
import com.example.sepada.util.AdminService;
import com.example.sepada.util.Constans;
import com.example.sepada.util.SuperAdminService;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPengajuanFragment extends Fragment {

    private String id;
    private FragmentAdminDetailPengajuanBinding binding;
    private AlertDialog progressDialog;
    private AdminService adminService;
    private SuperAdminService superAdminService;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminDetailPengajuanBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);
        superAdminService  = ApiConfig.getClient().create(SuperAdminService.class);

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

        if (getArguments().getString("file").equals("")) {
            binding.downloadSuratLampiran.setVisibility(View.GONE);
        }else {
            binding.downloadSuratLampiran.setVisibility(View.VISIBLE);
        }




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTamuById();
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
        binding.downloadSuratLampiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url =  Constans.URL_DOWNLOAD_SURAT_LAMPIRAN + id;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_ubah_pengajuan);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                EditText etAlasan, etJumlah;
                TextView tvJam, tvTanggal;
                Button btnSimpan, btnBatal;

                etAlasan = dialog.findViewById(R.id.etAlasan);
                etJumlah = dialog.findViewById(R.id.etJumlah);
                tvJam = dialog.findViewById(R.id.tvJam);
                tvTanggal = dialog.findViewById(R.id.tvTanggal);
                btnSimpan = dialog.findViewById(R.id.btnSimpan);
                btnBatal = dialog.findViewById(R.id.btnBatal);

                etAlasan.setText(getArguments().getString("alasan"));
                etJumlah.setText(getArguments().getString("jumlah"));
                tvJam.setText(getArguments().getString("waktu"));
                tvTanggal.setText(getArguments().getString("tanggal"));
                dialog.show();

                tvJam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker(tvJam);
                    }
                });
                tvTanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDatePicker(tvTanggal);

                    }
                });

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etAlasan.getText().toString().isEmpty()) {
                            etAlasan.setError("Tidak boleh kosong");
                        }else if (etJumlah.getText().toString().isEmpty()) {
                            etJumlah.setError("Tidak boleh kosong");
                        }else if (tvJam.getText().toString().isEmpty()) {
                            tvJam.setError("Tidak boleh kosong");
                        }else if (tvTanggal.getText().toString().isEmpty()) {
                            tvTanggal.setError("Tidak boleh kosong");
                        } else if (tvJam.getText().toString().compareTo("08:00") < 0 || tvJam.getText().toString().compareTo("12:00") > 0) {
                            showToast("err", "Anda hanya dapat mengajukan pada jam 08:00 - 12:00");
                            tvJam.setError("");
                        }else {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date = dateFormat.parse(tvTanggal.getText().toString());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);

                                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                                if (dayOfWeek == Calendar.SATURDAY) {
                                    showToast("err", "Anda tidak dapat mengajukan pada hari sabtu");
                                    tvTanggal.setError("");
                                } else if (dayOfWeek == Calendar.SUNDAY) {
                                    showToast("err", "Anda tidak dapat mengajukan pada hari minggu");
                                    tvTanggal.setError("");

                                }
                                else {

                                    showProgressBar("Loading", "Menyimpan perubahan", true);
                                    adminService.updatePengajuan(id, tvJam.getText().toString(),
                                            tvTanggal.getText().toString(), etJumlah.getText().toString(), etAlasan.getText().toString())
                                            .enqueue(new Callback<ResponseModel>() {
                                                @Override
                                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                    showProgressBar("sd", "ds", false);
                                                    if (response.isSuccessful() && response.body().getCode() == 200){
                                                        showToast("success", "Berhasil mengubah data tamu");
                                                        getActivity().onBackPressed();
                                                        dialog.dismiss();

                                                    }else {
                                                        showToast("error", "Terjadi kesalahan");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                                    showToast("error", "Tidak ada koneksi internet");
                                                    showProgressBar("d", "D", false);
                                                }

                                            });


                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Konfirmasi");
                alert.setMessage("Apakah anda yakin ingin menghapus data ini?");
                alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

            }
        });

    }

    private void timePicker(TextView tvTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String timeFormatted, minuteFormatted, secondFormatted;

            if (hourOfDay < 10) {
                timeFormatted = String.format("%02d", hourOfDay);
            } else {
                timeFormatted = String.valueOf(hourOfDay);
            }

            if (minute < 10) {
                minuteFormatted = String.format("%02d", minute);
            } else {
                minuteFormatted = String.valueOf(minute);
            }

            int second = Calendar.getInstance().get(Calendar.SECOND);
            if (second < 10) {
                secondFormatted = String.format("%02d", second);
            } else {
                secondFormatted = String.valueOf(second);
            }

            tvTime.setText(timeFormatted + ":" + minuteFormatted + ":" + secondFormatted);
        }, 0, 0, true);

        timePickerDialog.show();
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

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }
        });
        datePickerDialog.show();
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


    private void getTamuById() {
        showProgressBar("Loading", "Memuat data...", true);
        superAdminService.getTamuById(id).enqueue(new Callback<TamuModel>() {
            @Override
            public void onResponse(Call<TamuModel> call, Response<TamuModel> response) {
                showProgressBar("", "",false);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getNamaAnggota() != null) {

                        binding.etNamaAnggota.setText(response.body().getNamaAnggota());
                    } else {
                        binding.etNamaAnggota.setText("Tidak ada data");
                    }
                }else {
                    showToast("err", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<TamuModel> call, Throwable t) {
                showProgressBar("", "",false);
                binding.etNamaAnggota.setText("Tidak ada data");
                showToast("err", "Tidak ada koneksi internet");



            }
        });
    }

    private void showToast(String jenis, String text) {
        if (jenis.equals("success")) {
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }

    private void delete() {
        showProgressBar("Loading", "Menghapus data...", true);
        adminService.deletePengajuan(id).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("dd", "Dd", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "Berhasill menghapus data");
                    getActivity().onBackPressed();

                }else {
                    showToast("err", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("d", "d", false);
                showToast("err", "Tidak ada koneksi internet");

            }
        });
    }


}