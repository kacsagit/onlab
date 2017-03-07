package com.example.kata.onlab.ui.map;

import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.Presenter;
import com.google.android.gms.maps.model.LatLng;

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

    public List<Data> getNetworkData(){
        return NetworkManager.getInstance().getData();
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
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }
}