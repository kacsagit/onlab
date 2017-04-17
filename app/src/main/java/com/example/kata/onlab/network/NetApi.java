package com.example.kata.onlab.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Kata on 2017. 02. 11..
 */
public interface NetApi {
    @GET("/")
    Call<List<Data>> getData();

    @GET("/api/get")
    Call<List<Data>> getDataSpec(@Header("Authorization") String token);

    @GET("/api/users")
    Call<List<Friends>> getusers(@Header("Authorization") String token);


    @GET("/api/user")
    Call<FriendDetail> getuser(@Header("Authorization") String token, @Query("id") int id);


    @GET("/api/getfriends")
    Call<List<Friends>> getFriends(@Header("Authorization") String token);

    @GET("/api/getme")
    Call<List<Data>> getDataSpecMy(@Header("Authorization") String token);

    @POST("/login")
    Call<String> logIn(@Body Login log);

    @POST("/api")
    Call<Integer> postData(@Header("Authorization") String token,@Body Data dat);

    @POST("/api/addfirend")
    Call<Integer> postFriend(@Header("Authorization") String token,@Body Friend dat);

    @DELETE("/api/deletefirend")
    Call<Integer> deleteFriend(@Header("Authorization") String token,@Query("id") int id);

    @POST("/auth/facebook/token")
    Call<LoginData> postTokenFB(@Body FB fb);

    @POST("/auth/google")
    Call<LoginData> postTokenGoogle(@Body Google g);

    @POST("/signup")
    Call<Integer> signupData(@Body Login dat);

    @POST("/api/device")
    Call<Void> signDevice(@Header("Authorization") String token,@Body Deviceid s);

    @POST("/api/push")
    Call<Void> pushNotif(@Header("Authorization") String token,@Body PushId id);

}
