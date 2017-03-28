package com.example.kata.onlab.network;

import android.util.Log;

import com.example.kata.onlab.event.ErrorResponseEvent;
import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.LoginDataEvent;
import com.example.kata.onlab.event.PostDataEvent;
import com.example.kata.onlab.event.SignUpDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    private String token;
    private String usertablename;

    Realm realm;

    private NetworkManager() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_ADDRESS).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        netApi = retrofit.create(NetApi.class);


    }

    public void updateData() {
        if (token!=null) {
            String mToken = "Bearer " +token;
            netApi.getDataSpec(mToken).enqueue(new Callback<List<Data>>() {
                @Override
                public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                    if (response.isSuccessful()) {
                        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(usertablename).build();
                        // Realm.deleteRealm(realmConfiguration);
                        realm = Realm.getInstance(realmConfiguration);
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
                        ErrorResponseEvent errorResponseEvent=new ErrorResponseEvent();
                        EventBus.getDefault().post(errorResponseEvent);
                        // Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Data>> call, Throwable t) {

                }
            });
        }
    }

    public void postTokenFB(final String tokenFB) {
        FB fb = new FB();
        fb.access_token = tokenFB;
        netApi.postTokenFB(fb).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.isSuccessful()) {
                    onTokenPostSuccesSocial(response.body());
                } else {
                    Log.d(TAG, response.message());
                    ErrorResponseEvent errorResponseEvent=new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {

            }
        });
    }

    public void onTokenPostSuccesSocial(LoginData data){
        usertablename=data.email;
        Log.d(TAG, data.email);
        LoginDataEvent loginDataEvent = new LoginDataEvent();
        loginDataEvent.setData(data);
        token=data.token;
        EventBus.getDefault().post(loginDataEvent);
    }

    public void postTokenGoogle(final String tokenG) {
        Google g = new Google();
        g.id_token = tokenG;
        netApi.postTokenGoogle(g).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.isSuccessful()) {
                    onTokenPostSuccesSocial(response.body());
                } else {
                    Log.d(TAG, response.message());
                    ErrorResponseEvent errorResponseEvent=new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {

            }
        });
    }

    public List<Data> getData() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(usertablename).build();
        // Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        RealmResults<Data> results = realm.where(Data.class).findAll();
        return results;
    }

    public void logIn(final String username, String password) {
        final Login login = new Login(username,password);
        netApi.logIn(login).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    usertablename=username;
                    Log.d(TAG, response.body());
                    token=response.body();
                    LoginDataEvent loginDataEvent = new LoginDataEvent();
                    LoginData loginData=new LoginData();
                    loginData.email=username;
                    loginDataEvent.setData(loginData);
                    EventBus.getDefault().post(loginDataEvent);
                } else {
                    ErrorResponseEvent errorResponseEvent=new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    public void signupData(final Login d){
        netApi.signupData(d).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    SignUpDataEvent signupDataEvent = new SignUpDataEvent();
                    signupDataEvent.setData(d);
                    EventBus.getDefault().post(signupDataEvent);

                } else {
                    ErrorResponseEvent errorResponseEvent=new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                    //     Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }



    public void postData(final Data d) {
        if (token!=null) {
            netApi.postData("Bearer "+token, d).enqueue(new Callback<Integer>() {
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
                        ErrorResponseEvent errorResponseEvent=new ErrorResponseEvent();
                        EventBus.getDefault().post(errorResponseEvent);
                        //     Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }


    public interface ResponseListener<T> {
        void onResponse(T t);

        void onError(Exception e);
    }

}
