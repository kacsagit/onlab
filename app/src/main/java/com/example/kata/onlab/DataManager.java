package com.example.kata.onlab;

import android.content.Context;
import android.os.Handler;

import com.example.kata.onlab.event.ErrorEvent;
import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.network.NetworkManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Kata on 2017. 02. 19..
 */
public class DataManager {
    private static volatile DataManager instance = null;

    public List<Data> items=new ArrayList<>();
    private static final String TAG = "DataManager";
    private DataManager() {}
    private static Context context;

    public static DataManager getInstance(Context ctx) {
        if (instance == null) {
            synchronized(DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                    context=ctx.getApplicationContext();
                }
            }
        }
        return instance;
    }

    private static <T> void runCallOnBackgroundThread(final Call<T> call) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final T photos = call.execute().body();
                    GetDataEvent getPhotosEvent=new GetDataEvent();
                    getPhotosEvent.setData(photos);
                    EventBus.getDefault().post(getPhotosEvent);

                } catch (final Exception e) {
                    e.printStackTrace();
                    ErrorEvent errorEvent=new ErrorEvent();
                    errorEvent.setE(e);
                    EventBus.getDefault().post(errorEvent);
                }
            }
        }).start();
    }

    public void getData() {
        Call<List<Data>> getDataRequest = NetworkManager.getInstance().getData();
        runCallOnBackgroundThread(getDataRequest);
    }

    public void uploadData(Data data) {
        Call<String> uploadPhotoRequest =NetworkManager.getInstance().postData(data);
        runCallOnBackgroundThread(uploadPhotoRequest);
    }



    public interface ResponseListener<T> {
        void onResponse(T t);
        void onError(Exception e);
    }
}
