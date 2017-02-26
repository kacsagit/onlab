package com.example.kata.onlab.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Kata on 2017. 02. 11..
 */
public interface NetApi {
    @GET("/")
    Call<List<Data>> getData();

    @POST("/")
    Call<String> postData(@Body Data dat);
}
