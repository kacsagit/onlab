package com.example.kata.onlab.event;

/**
 * Created by Kata on 2017. 03. 26..
 */

public class SignUpDataEvent<T> {
        private T data;

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

    }

