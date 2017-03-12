package com.example.kata.onlab.network;

import android.util.Log;

import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.LoginDataEvent;
import com.example.kata.onlab.event.PostDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kata on 2017. 02. 11..
 */

public class NetworkManager {

    private static final String ENDPOINT_ADDRESS = "https://still-dawn-67153.herokuapp.com/";

    private static final String TAG = "NetworkManager";
    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();

        }
        return instance;
    }

    private Retrofit retrofit;
    private NetApi netApi;
    Realm realm;

    private NetworkManager() {

        retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_ADDRESS).client(new OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).build();
        netApi = retrofit.create(NetApi.class);
        updateData();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        //Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);

    }

    public void updateData() {
        netApi.getData().enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    GetDataEvent getDataEvent = new GetDataEvent();
                    List<Data> resp = response.body();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(resp);
                    realm.commitTransaction();
                    RealmResults<Data> results = realm.where(Data.class).findAll();
                    getDataEvent.setData(results);
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

    public void postToken(String token) {
        netApi.postToken(token).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().id);
                    LoginDataEvent loginDataEvent=new LoginDataEvent();
                    loginDataEvent.setData(response.body());
                    EventBus.getDefault().post(loginDataEvent);
                }else{
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {

            }
        });
    }

    public List<Data> getData() {

        RealmResults<Data> results = realm.where(Data.class).findAll();
        return results;
    }

    public void postData(final Data d) {
        netApi.postData(d).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    d.id = response.body();

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(d);
                    realm.commitTransaction();

                    PostDataEvent postDataEvent = new PostDataEvent();
                    postDataEvent.setData(d);
                    EventBus.getDefault().post(postDataEvent);

                } else {
                    //     Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }


    public interface ResponseListener<T> {
        void onResponse(T t);

        void onError(Exception e);
    }

}
