package com.example.kata.onlab.ui.myList;

import com.example.kata.onlab.network.DataDetails;
import com.example.kata.onlab.network.MyData;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface ListScreen {

    void postDataCallback(MyData data);

    void updateDataCallback(List<MyData> data);

    void getDataDetailsCallback(DataDetails data);
}
