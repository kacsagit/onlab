package com.example.kata.onlab.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Kata on 2017. 02. 11..
 */
public interface NetApi {
    @GET("/")
    Call<List<Data>> getData();

    @GET("/get")
    Call<List<Data>> getDataSpec(@Header("Authorization") String token);

    @POST("/login")
    Call<String> logIn(@Body Login log);

    @POST("/")
    Call<Integer> postData(@Header("Authorization") String token,@Body Data dat);

    @POST("/auth/facebook/token")
    Call<LoginData> postToken(@Body FB fb);


    @POST("/auth/google")
    Call<LoginData> postTokenGoogle(@Body Google g);

    @POST("/signup")
    Call<Integer> signupData(@Body Login dat);


}
