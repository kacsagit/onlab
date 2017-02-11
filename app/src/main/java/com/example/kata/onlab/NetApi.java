package com.example.kata.onlab;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kata on 2017. 02. 11..
 */
public interface NetApi {
    @GET("/")
    Call<Data> getData();
}
