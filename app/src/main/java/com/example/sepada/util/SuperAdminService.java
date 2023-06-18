package com.example.sepada.util;

import com.example.sepada.data.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SuperAdminService {
    @FormUrlEncoded
    @POST("superadmin/keputusan")
    Call<ResponseModel> keputusan(
            @Field("id_status") Integer idStatus,
            @Field("id_tamu") String idTamu,
            @Field("alasan") String alasan
    );
}
