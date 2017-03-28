package com.example.kata.onlab.ui.login;

import com.example.kata.onlab.network.LoginData;

/**
 * Created by Kata on 2017. 03. 12..
 */

public interface LoginScreen {
    void logedIn(LoginData data);

    void logedError();
}
