package com.example.kata.onlab;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;

import com.example.kata.onlab.network.TokenInterceptor;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import okhttp3.OkHttpClient;

/**
 * Created by Kata on 2017. 03. 12..
 */

public class MyCustomApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        setupPicasso();

    }
    public void setupPicasso(){
        final Context context=this;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor()).build();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        builder.downloader(new OkHttp3Downloader(client));
        final Picasso picasso = builder.build();
        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}