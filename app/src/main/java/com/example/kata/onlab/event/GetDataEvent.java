package com.example.kata.onlab.event;

/**
 * Created by android on 2016. 11. 08..
 */
public class GetDataEvent<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
