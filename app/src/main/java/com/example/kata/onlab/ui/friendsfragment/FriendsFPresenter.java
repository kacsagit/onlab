package com.example.kata.onlab.ui.friendsfragment;

import com.example.kata.onlab.event.ErrorEvent;
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

public class FriendsFPresenter extends Presenter<FriendsFScreen> {

    private static FriendsFPresenter instance = null;

    private FriendsFPresenter() {
    }

    public static FriendsFPresenter getInstance() {
        if (instance == null) {
            instance = new FriendsFPresenter();
        }
        return instance;
    }



    @Override
    public void attachScreen(FriendsFScreen screen) {
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
        screen.updateUserCallback(event.getData());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event) {
        event.getE().printStackTrace();
    }


}
