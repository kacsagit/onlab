package com.example.kata.onlab.ui.main;

import com.example.kata.onlab.network.Data;

import java.util.List;

public interface MainScreen {
    void postDataCallback(Data item);
    void updateDataCallback(List<Data> items);
}
