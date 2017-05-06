package com.example.kata.onlab.ui.myMap;

import com.example.kata.onlab.event.ErrorEvent;
import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.PostDataEvent;
import com.example.kata.onlab.network.MyData;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.Presenter;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public class MapPresenter extends Presenter<MapScreen> {

    private static MapPresenter instance = null;

    private MapPresenter() {
    }

    public static MapPresenter getInstance() {
        if (instance == null) {
            instance = new MapPresenter();
        }
        return instance;
    }


    public void updateNetworkData(){
        NetworkManager.getInstance().updateData();
    }


    public void newItemView(LatLng point){
        screen.newItemView(point);
    }

    @Override
    public void attachScreen(MapScreen screen) {
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
    public void onGetData(GetDataEvent<List<MyData>> event) {
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
    public void onError(ErrorEvent event) {
        event.getE().printStackTrace();
    }

}