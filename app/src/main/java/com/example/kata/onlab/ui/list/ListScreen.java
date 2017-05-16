package com.example.kata.onlab.ui.list;

import com.example.kata.onlab.db.Data;
import com.example.kata.onlab.db.DataDetails;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface ListScreen {

    void updateDataCallback(List<Data> data);

    void getDataDetailsCallback(DataDetails data);
}
