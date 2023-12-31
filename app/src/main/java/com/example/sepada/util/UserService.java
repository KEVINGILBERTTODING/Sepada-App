package com.example.sepada.util;

import com.example.sepada.data.model.ResponseModel;
import com.example.sepada.data.model.TamuModel;
import com.example.sepada.data.model.UserDetailModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface UserService {
    @GET("user/getmycuti")
    Call<List<TamuModel>> getMyCuti(
            @Query("id") String id
    );

    @GET("user/getDetailProfile")
    Call<UserDetailModel> getDetailProfile(
            @Query("id") String id
    );
    @FormUrlEncoded
    @POST("user/insertDetailUser")
    Call<ResponseModel> insertDetailUser(
            @Field("id_user_detail") String id_user_detail,
            @Field("nama_lengkap") String nama_lengkap,
            @Field("id_jenis_kelamin") String id_jenis_kelamin,
            @Field("no_telp") String no_telp,
            @Field("alamat") String alamat,
            @Field("nip") String nip,
            @Field("pangkat") String pangkat,
            @Field("jabatan") String jabatan
    );

    @Multipart
    @POST("user/insertPengajuan")
    Call<ResponseModel> insertPengajuan(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part file
            );

    @GET("user/getTamuByStatus")
    Call<List<TamuModel>> getTamuByStatus(
            @Query("id") String id,
            @Query("status") String status
    );

    @Multipart
    @POST("user/updatePhotoProfile")
    Call<ResponseModel> updateProfile(
            @PartMap Map<String, RequestBody> textDat,
            @Part MultipartBody.Part filePart
    );

    @FormUrlEncoded
    @POST("user/updateDetailUser")
    Call<ResponseModel> updateDetailUser(
            @Field("id") String id,
            @Field("nama_lengkap") String nama_lengkap,
            @Field("id_jenis_kelamin") String id_jenis_kelamin,
            @Field("no_telp") String no_telp,
            @Field("alamat") String alamat,
            @Field("nip") String nip,
            @Field("pangkat") String pangkat,
            @Field("jabatan") String jabatan
    );

    @FormUrlEncoded
    @POST("user/updateProfile")
    Call<ResponseModel> updateProfile(
            @Field("id") String id,
            @Field("username") String username,
            @Field("password") String password
    );









}
