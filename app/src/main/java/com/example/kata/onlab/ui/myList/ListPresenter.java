package com.example.kata.onlab.ui.myList;

import com.example.kata.onlab.event.ErrorEvent;
import com.example.kata.onlab.event.GetDataDetailsEvent;
import com.example.kata.onlab.event.GetDataMyEvent;
import com.example.kata.onlab.event.PostDataEvent;
import com.example.kata.onlab.network.DataDetails;
import com.example.kata.onlab.network.MyData;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public class ListPresenter  extends Presenter<ListScreen> {

    private static ListPresenter instance = null;

    private ListPresenter() {
    }

    public static ListPresenter getInstance() {
        if (instance == null) {
            instance = new ListPresenter();
        }
        return instance;
    }


    public void updateNetworkData(){
        NetworkManager.getInstance().updateData();
    }

    public void updateDataMy(){
        NetworkManager.getInstance().updateDataMy();
    }



    @Override
    public void attachScreen(ListScreen screen) {
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
    public void onGetData(GetDataMyEvent<List<MyData>> event) {
        if (screen != null) {
            screen.updateDataCallback(event.getData());
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostData(PostDataEvent<MyData> event) {
        if (screen != null) {
            screen.postDataCallback(event.getData());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetDataDetails(GetDataDetailsEvent<DataDetails> event) {
        if (screen != null) {
            screen.getDataDetailsCallback(event.getData());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event) {
        event.getE().printStackTrace();
    }


}
