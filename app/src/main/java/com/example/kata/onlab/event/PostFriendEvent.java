package com.example.kata.onlab.event;

/**
 * Created by Kata on 2017. 02. 24..
 */

public class PostFriendEvent<T> {
        private T data;

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

}
