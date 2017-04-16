package com.example.kata.onlab.ui.friendsearch;

import com.example.kata.onlab.event.GetUserEvent;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public class FriendSearchPresenter extends Presenter<FriendSearchScreen> {

    private static FriendSearchPresenter instance = null;

    private FriendSearchPresenter() {
    }

    public static FriendSearchPresenter getInstance() {
        if (instance == null) {
            instance = new FriendSearchPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(FriendSearchScreen screen) {
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
    public void onUserData(GetUserEvent<List<Friends>> event) {
        if (screen != null) {
            screen.updateUserCallback(event.getData());
        }


    }


}
