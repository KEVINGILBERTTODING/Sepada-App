package com.example.sepada.util;

import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.TamuModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AdminService {

    @GET("admin/getAllPengajuan")
    Call<List<TamuModel>> getAllPengajuan();

    @GET("admin/getAllPengajuanByStatus")
    Call<List<TamuModel>> getAllPengajuanByStatus(
            @Query("status") String status
    );

    @FormUrlEncoded
    @POST("admin/updatePengajuan")
    Call<ResponseModel> updatePengajuan(
            @Field("id") String id,
            @Field("jam") String jam,
            @Field("tanggal") String tanggal,
            @Field("jumlah") String jumlah,
            @Field("alasan") String alasan
    );


}
