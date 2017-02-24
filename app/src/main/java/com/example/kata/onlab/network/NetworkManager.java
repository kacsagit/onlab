package com.example.kata.onlab.network;

import android.util.Log;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.PostDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Kata on 2017. 02. 11..
 */

public class NetworkManager {

    private static final String ENDPOINT_ADDRESS = "https://still-dawn-67153.herokuapp.com/";

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();

        }
        return instance;
    }

    private Retrofit retrofit;
    private NetApi netApi;
    public List<Data> items = new ArrayList<Data>();

    private NetworkManager() {

        retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_ADDRESS).client(new OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).build();
        netApi = retrofit.create(NetApi.class);
        getData();

    }

    public void getData() {
        netApi.getData().enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    items = new ArrayList<Data>((response.body()));
                    GetDataEvent getDataEvent=new GetDataEvent();
                    getDataEvent.setData(items);
                    EventBus.getDefault().post(getDataEvent);
                } else {
                    // Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {

            }
        });
    }

    public void postData(final Data d) {
        netApi.postData(d).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body());
                    items.add(d);
                    PostDataEvent postDataEvent=new PostDataEvent();
                    postDataEvent.setData(d);
                    EventBus.getDefault().post(postDataEvent);
                } else {
                    //     Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    public interface ResponseListener<T> {
        void onResponse(T t);
        void onError(Exception e);
    }

}
