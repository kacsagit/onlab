package com.example.kata.onlab.ui.login;

import com.example.kata.onlab.event.ErrorResponseEvent;
import com.example.kata.onlab.event.LoginDataEvent;
import com.example.kata.onlab.network.LoginData;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Kata on 2017. 03. 12..
 */

public class LoginPresenter extends Presenter<LoginScreen> {

    private static LoginPresenter instance = null;

    private LoginPresenter() {
    }

    public static LoginPresenter getInstance() {
        if (instance == null) {
            instance = new LoginPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(LoginScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetData(LoginDataEvent<LoginData> event) {
        if (screen != null) {
            screen.logedIn(event.getData());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetDataError(ErrorResponseEvent event) {
        if (screen != null) {
            screen.logedError();
        }


    }


}
