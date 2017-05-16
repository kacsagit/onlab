package com.example.kata.onlab.ui.friendDetails;

import com.example.kata.onlab.event.DeleteFriendEvent;
import com.example.kata.onlab.event.GetUserEvent;
import com.example.kata.onlab.event.PostFriendEvent;
import com.example.kata.onlab.db.FriendDetail;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Kata on 2017. 02. 26..
 */

public class FriendDetailsPresenter extends Presenter<FriendDetailsScreen> {

    private static FriendDetailsPresenter instance = null;

    private FriendDetailsPresenter() {
    }

    public static FriendDetailsPresenter getInstance() {
        if (instance == null) {
            instance = new FriendDetailsPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(FriendDetailsScreen screen) {
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
    public void onUserData(GetUserEvent<FriendDetail> event) {
        if (screen != null) {
            screen.updateUserCallback(event.getData());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostFirend(PostFriendEvent<FriendDetail> event) {
        if (screen != null) {
            screen.updateFriendCallback(event.getData());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteFirend(DeleteFriendEvent<FriendDetail> event) {
        if (screen != null) {
            screen.deleteFriendCallback(event.getData());
        }
    }



}
