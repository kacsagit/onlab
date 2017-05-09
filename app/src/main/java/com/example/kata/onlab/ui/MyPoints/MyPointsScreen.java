package com.example.kata.onlab.ui.MyPoints;

import com.example.kata.onlab.network.Data;

import java.util.List;

/**
 * Created by Kata on 2017. 04. 09..
 */

public interface MyPointsScreen {

    void updateDataCallback(List<Data> data);

    void postDataCallback(Data data);
}
