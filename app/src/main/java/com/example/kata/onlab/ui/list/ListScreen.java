package com.example.kata.onlab.ui.list;

import com.example.kata.onlab.network.Data;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface ListScreen {
    void newItemView();

    void postDataCallback(Data data);

    void updateDataCallback(List<Data> data);
}
