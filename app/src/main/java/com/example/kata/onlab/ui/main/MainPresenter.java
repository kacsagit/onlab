package com.example.kata.onlab.ui.main;


import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.GetDataMyEvent;
import com.example.kata.onlab.event.GetMeEvent;
import com.example.kata.onlab.event.PhotoUploadedEvent;
import com.example.kata.onlab.db.Data;
import com.example.kata.onlab.db.Friends;
import com.example.kata.onlab.db.MyData;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainPresenter extends Presenter<MainScreen> {

    private static MainPresenter instance = null;

    private MainPresenter() {
    }

    public static MainPresenter getInstance() {
        if (instance == null) {
            instance = new MainPresenter();
        }
        return instance;
    }


    @Override
    public void attachScreen(MainScreen screen) {
        super.attachScreen(screen);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetData(GetDataEvent<List<Data>> event) {
        if (screen != null) {
            screen.updateDataCallback(event.getData());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetDataMy(GetDataMyEvent<List<MyData>> event) {
        if (screen != null) {
            screen.updateDataCallbackMy(event.getData());
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetDataprofile(GetMeEvent<Friends> event) {
        if (screen != null) {
            screen.updateProfile(event.getData());
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetDataprofileupdate(PhotoUploadedEvent<String> event) {
        if (screen != null) {
            screen.photoUploaded(event.getData());
        }


    }



}
