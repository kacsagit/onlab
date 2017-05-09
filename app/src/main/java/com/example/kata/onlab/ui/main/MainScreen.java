package com.example.kata.onlab.ui.main;

import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.MyData;

import java.util.List;

public interface MainScreen {

    void updateProfile(Friends data);

    void updateDataCallback(List<Data> data);

    void updateDataCallbackMy(List<MyData> data);

    void photoUploaded(String data);
}
