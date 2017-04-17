package com.example.kata.onlab.ui.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kata.onlab.R;
import com.example.kata.onlab.event.GetFriendsEvent;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.NetworkManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Kata on 2017. 04. 16..
 */

public class FriendsFragment extends ListGetFragment implements FriendsRecAdapter.MyInterface {
    Realm realm;
    RealmResults<Friends> results;
    FriendsRecAdapter friendsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.friends_fragment, container, false);
        initRecycleView();
        context=getContext();
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
        items=new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "friends").build();
        //Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(Friends.class).findAll();
        NetworkManager.getInstance().getfriends();
        RecyclerView reciew = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        reciew.setLayoutManager(horizontalLayoutManagaer);
        friendsAdapter=new FriendsRecAdapter(this);
        reciew.setAdapter(friendsAdapter);
        updateUserCallback(new ArrayList<Friends>(results));



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        friendsAdapter.update(new ArrayList<Friends>(results));
    }

    @Override
    protected void initRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.MainRecyclerView);
        adapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void updateDataCallback(List<Data> list) {
        items=list;
        adapter.update(list);
        swipeRefresh.setRefreshing(false);
        updateUserCallback(new ArrayList<>(results));
    }

    @Override
    public void sort(int id) {
        List<Data> temp = new ArrayList<Data>();
        for (Data d : items) {
            if (d.ownerid == id)
                temp.add(d);
        }
        adapter.update(temp);
    }

    @Override
    public void unsort() {
        adapter.update(items);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserData(GetFriendsEvent<List<Friends>> event) {
        updateUserCallback(event.getData());
    }




    private void updateUserCallback(List<Friends> data) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
        results = realm.where(Friends.class).findAll();
        List<Friends> freinds=new ArrayList<>(results);
        friendsAdapter.update(new ArrayList<Friends>(results));

    }


}
