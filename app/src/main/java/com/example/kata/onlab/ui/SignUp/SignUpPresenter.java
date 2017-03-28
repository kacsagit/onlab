package com.example.kata.onlab.ui.SignUp;

import com.example.kata.onlab.event.SignUpDataEvent;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Kata on 2017. 03. 28..
 */

public class SignUpPresenter extends Presenter<SignUpScreen> {

    private static SignUpPresenter instance = null;

    private SignUpPresenter() {
    }

    public static SignUpPresenter getInstance() {
        if (instance == null) {
            instance = new SignUpPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(SignUpScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUp(SignUpDataEvent<Integer> event) {
        if (screen != null) {
            screen.onSignUp();
        }


    }


}
