package com.example.kata.onlab;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kata on 2017. 02. 11..
 */
public class NetworkManager {

        private static final String ENDPOINT_ADDRESS = "http://onlab.j.layershift.co.uk";

        private static NetworkManager instance;

        public static NetworkManager getInstance() {
            if (instance == null) {
                instance = new NetworkManager();
            }
            return instance;
        }

        private Retrofit retrofit;
        private NetApi netApi;

        private NetworkManager() {
            retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_ADDRESS).client(new OkHttpClient.Builder().build()).addConverterFactory(GsonConverterFactory.create()).build();
            netApi = retrofit.create(NetApi.class);
        }

        public Call<Data> getData() {
            return netApi.getData();
        }

}
