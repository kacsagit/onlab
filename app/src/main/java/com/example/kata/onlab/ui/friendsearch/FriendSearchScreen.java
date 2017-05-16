package com.example.kata.onlab.ui.friendsearch;

import com.example.kata.onlab.db.Friends;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface FriendSearchScreen {
    void updateUserCallback(List<Friends> data);
}
