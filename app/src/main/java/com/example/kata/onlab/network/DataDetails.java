package com.example.kata.onlab.network;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Kata on 2017. 04. 24..
 */

public class DataDetails extends RealmObject {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey
    public int id;
    public String place;
    public float longitude;
    public float latitude;
    public int ownerid;
    public int done;
    public String description;
    public String image;


    public DataDetails(Data d){
        id=d.id;
        place=d.place;
        longitude=d.longitude;
        latitude=d.latitude;
        ownerid=d.ownerid;
        image=d.image;
    }

    public DataDetails(MyData d){
        id=d.id;
        place=d.place;
        longitude=d.longitude;
        latitude=d.latitude;
        ownerid=d.ownerid;
        image=d.image;
    }


    public int isDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }


    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }


    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    public DataDetails() {
    }
}