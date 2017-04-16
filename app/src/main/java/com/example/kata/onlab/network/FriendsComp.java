package com.example.kata.onlab.network;

import android.support.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

/**
 * Created by Kata on 2017. 04. 16..
 */

public class FriendsComp implements SortedListAdapter.ViewModel {
    public int id;
    public String name;

    public FriendsComp(int id_, String name_) {
        id = id_;
        name = name_;

    }

    @Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof FriendsComp) {
            final FriendsComp friend = (FriendsComp) item;
            return friend.id == id;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof FriendsComp) {
            final FriendsComp other = (FriendsComp) item;
            if (name != other.name) {
                return false;
            }
        }
        return false;
    }
}
