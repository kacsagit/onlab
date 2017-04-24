package com.example.kata.onlab.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kata on 2017. 04. 24..
 */

public final class TokenInterceptor implements Interceptor {
    private volatile static String token = null;

    public void setSomeAuthToken(String someAuthToken) {
        token = someAuthToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Authorization", token != null ? token : "").build();
        return chain.proceed(request);
    }


}