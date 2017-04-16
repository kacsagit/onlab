package com.example.kata.onlab.network;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Kata on 2017. 04. 16..
 */


public class Friends extends RealmObject {
    @PrimaryKey
    public int id;
    public String name;



}
