package com.example.kata.onlab.ui.map;

import com.example.kata.onlab.db.Data;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface MapScreen {


    void updateDataCallback(List<Data> data);

    void postDataCallback(Data data);
}
