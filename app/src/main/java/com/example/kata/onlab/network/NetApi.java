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

    @GET("/api/get")
    Call<List<Data>> getDataSpec(@Header("Authorization") String token);

    @POST("/login")
    Call<String> logIn(@Body Login log);

    @POST("/api")
    Call<Integer> postData(@Header("Authorization") String token,@Body Data dat);

    @POST("/auth/facebook/token")
    Call<LoginData> postTokenFB(@Body FB fb);

    @POST("/auth/google")
    Call<LoginData> postTokenGoogle(@Body Google g);

    @POST("/signup")
    Call<Integer> signupData(@Body Login dat);

    @POST("/api/device")
    Call<Void> signDevice(@Header("Authorization") String token,@Body Deviceid s);


}
