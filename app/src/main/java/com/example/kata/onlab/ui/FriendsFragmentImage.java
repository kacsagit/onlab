package com.example.kata.onlab.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.kata.onlab.R;
import com.example.kata.onlab.event.GetFriendsEvent;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.list.ListGetFragment;
import com.example.kata.onlab.ui.list.ListPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kata on 2017. 04. 17..
 */

public class FriendsFragmentImage extends ListGetFragment {
    List<Friends> data;
    RadioGroup rgroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.friend_fragment_image, container, false);
        initRecycleView();
        context = getContext();
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                NetworkManager.getInstance().getfriends();

            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListPresenter.getInstance().newItemView();

            }
        });
        data = new ArrayList<>();
        rgroup = (RadioGroup) view.findViewById(R.id.radioSex);

        NetworkManager.getInstance().getfriends();
        return view;
    }

    void sort(int id) {
        List<Data> temp = new ArrayList<Data>();
        for (Data d : items) {
            if (d.ownerid == id)
                temp.add(d);
        }
        adapter.update(temp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserData(GetFriendsEvent<List<Friends>> event) {
        updateUserCallback(event.getData());
    }

    @Override
    public void updateDataCallback(List<Data> list) {
        items = list;

        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void updateUserCallback(List<Friends> data) {
        this.data = data;

    }
}
