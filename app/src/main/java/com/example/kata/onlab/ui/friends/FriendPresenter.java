package com.example.kata.onlab.ui.friends;

import com.example.kata.onlab.event.GetFriendsEvent;
import com.example.kata.onlab.db.Friends;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public class FriendPresenter extends Presenter<FriendScreen> {

    private static FriendPresenter instance = null;

    private FriendPresenter() {
    }

    public static FriendPresenter getInstance() {
        if (instance == null) {
            instance = new FriendPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(FriendScreen screen) {
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
    public void onUserData(GetFriendsEvent<List<Friends>> event) {
        if (screen != null) {
            screen.updateUserCallback(event.getData());
        }


    }




}
