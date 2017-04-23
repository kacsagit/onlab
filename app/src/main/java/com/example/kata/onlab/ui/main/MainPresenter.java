package com.example.kata.onlab.ui.main;


import com.example.kata.onlab.event.GetMeEvent;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    public void onGetData(GetMeEvent<Friends> event) {
        if (screen != null) {
            screen.updateProfile(event.getData());
        }


    }



}
