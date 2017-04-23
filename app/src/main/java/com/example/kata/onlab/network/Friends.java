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
    public String image;

    public Friends() {

    }

    public Friends(int id_, String name_,String image_) {
        id = id_;
        name = name_;
        image=image_;

    }

}
