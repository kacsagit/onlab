package com.example.kata.onlab.ui.myMap;

import com.example.kata.onlab.db.MyData;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface MapScreen {

    void updateDataCallback(List<MyData> data);

    void postDataCallback(MyData data);
}
