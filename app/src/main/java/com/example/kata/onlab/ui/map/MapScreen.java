package com.example.kata.onlab.ui.map;

import com.example.kata.onlab.network.Data;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface MapScreen {
    void newItemView(LatLng point);

    void updateDataCallback(List<Data> data);

    void postDataCallback(Data data);
}
