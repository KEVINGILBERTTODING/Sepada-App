package com.example.sepada.util;

import com.example.sepada.data.model.AuthModel;
import com.example.sepada.data.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {
    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthModel> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
