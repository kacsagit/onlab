package com.example.kata.onlab.event;

/**
 * Created by Kata on 2017. 03. 28..
 */

public class ErrorResponseEvent<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
