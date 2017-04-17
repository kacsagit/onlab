package com.example.kata.onlab.ui.MyPoints;

import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.PostDataEvent;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Kata on 2017. 04. 09..
 */

public class MyPointsPresenter extends Presenter<MyPointsScreen> {

    private static MyPointsPresenter instance = null;

    private MyPointsPresenter() {
    }

    public static MyPointsPresenter getInstance() {
        if (instance == null) {
            instance = new MyPointsPresenter();
        }
        return instance;
    }


    public void updateNetworkData() {
        NetworkManager.getInstance().updateDataMy();
    }


    public void newItemView() {
        screen.newItemView();
    }

    @Override
    public void attachScreen(MyPointsScreen screen) {
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
    public void onPostData(PostDataEvent<Data> event) {
        if (screen != null) {
            screen.postDataCallback(event.getData());
        }
    }

}
