package com.example.kata.onlab.event;

public class GetUserEvent<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}