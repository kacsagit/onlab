package com.example.kata.onlab.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Kata on 2017. 02. 11..
 */
public interface NetApi {
    @GET("/")
    Call<List<Data>> getData();

    @POST("/login")
    Call<String> logIn(@Body Login log);

    @POST("/")
    Call<Integer> postData(@Body Data dat);

    @GET("/auth/facebook/token")
    Call<LoginData> postToken(@Query("access_token")  String token);


    @POST("/auth/google")
    Call<LoginData> postTokenGoogle(@Body Google g);


}
