package com.example.sepada.util;

import com.example.sepada.data.model.AnggotaModel;
import com.example.sepada.data.model.DivisiModel;
import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.TamuModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SuperAdminService {
    @FormUrlEncoded
    @POST("superadmin/keputusan")
    Call<ResponseModel> keputusan(
            @Field("id_status") Integer idStatus,
            @Field("id_tamu") String idTamu,
            @Field("alasan") String alasan
    );

    @FormUrlEncoded
    @POST("superadmin/keputusanAcc")
    Call<ResponseModel> keputusanAcc(
            @Field("id_status") Integer idStatus,
            @Field("id_tamu") String idTamu,
            @Field("anggota_id") String anggotaId
    );



    @FormUrlEncoded
    @POST("superadmin/addAdmin")
    Call<ResponseModel> addAdmin(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("superadmin/updateAdmin")
    Call<ResponseModel> updateAdmin(
            @Field("id") String id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("superadmin/deleteAdmin")
    Call<ResponseModel> deleteAdmin(
            @Field("id") String id
    );

    @GET("superadmin/getalldivisi")
    Call<List<DivisiModel>> getAllDivisi();


    @FormUrlEncoded
    @POST("superadmin/insertDivisi")
    Call<ResponseModel> insertDivisi(
            @Field("nama_divisi") String namaDivisi
    );
    @FormUrlEncoded
    @POST("superadmin/deletedivisi")
    Call<ResponseModel> deleteDivisi(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("superadmin/updateDivisi")
    Call<ResponseModel> updateDivisi(
            @Field("nama_divisi") String namaDivisi,
            @Field("id") String id
    );

    @GET("superadmin/getallanggota")
    Call<List<AnggotaModel>> getAllAnggota();

    @FormUrlEncoded
    @POST("superadmin/insertanggota")
    Call<ResponseModel> insertAnggota(
            @Field("nama_lengkap") String namaLengkap,
            @Field("divisi_id") String divisiId,
            @Field("no_telp") String noTelp
    );

    @FormUrlEncoded
    @POST("superadmin/deleteAnggota")
    Call<ResponseModel> deleteAnggota(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("superadmin/updateanggota")
    Call<ResponseModel> updateAnggota(
            @Field("id") String id,
            @Field("nama_lengkap") String namaLengkap,
            @Field("divisi_id") String divisiId,
            @Field("no_telp") String noTelp
    );

    @FormUrlEncoded
    @POST("superadmin/getDay")
    Call<ResponseModel> getDay(
            @Field("id") String id
    );
    @GET("superadmin/getcutibyid")
    Call<TamuModel> getTamuById(
            @Query("id") String id
    );









}
