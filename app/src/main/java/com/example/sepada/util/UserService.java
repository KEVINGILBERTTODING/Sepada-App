package com.example.sepada.util;

import com.example.sepada.data.model.TamuModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {
    @GET("user/getmycuti")
    Call<List<TamuModel>> getMyCuti(
            @Query("id") String id
    );



}
