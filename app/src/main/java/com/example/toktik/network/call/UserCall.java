package com.example.toktik.network.call;

import com.example.toktik.network.reception.Reception;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserCall {

    @POST("user/register")
    Call<Reception> register(
            @Query("username") String username,
            @Query("password") String passwd
    );

    @POST("user/login")
    Call<Reception> login(
            @Query("username") String username,
            @Query("password") String passwd
            );

}
