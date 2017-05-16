package com.example.kata.onlab.ui.friendDetails;

import com.example.kata.onlab.db.FriendDetail;

/**
 * Created by Kata on 2017. 02. 26..
 */

public interface FriendDetailsScreen {
    void updateUserCallback(FriendDetail data);

    void updateFriendCallback(FriendDetail friend);

    void deleteFriendCallback(FriendDetail data);
}
