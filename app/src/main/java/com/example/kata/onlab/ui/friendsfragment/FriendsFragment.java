package com.example.kata.onlab.ui.friendsfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.list.ListGetFragment;
import com.example.kata.onlab.ui.map.MapFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Kata on 2017. 04. 16..
 */

public class FriendsFragment extends Fragment implements FriendsFScreen, OnMenuSelectionSetListener {
    Realm realm;

    protected View view;
    protected Context context;
    RealmResults<Friends> results;
    FriendsRecAdapter friendsAdapter;
    Fragment fragment;
    RecyclerView reciew;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.friends_fragment, container, false);
        context = getContext();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "friends").build();
        //Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(Friends.class).findAll();
        NetworkManager.getInstance().getfriends();


        reciew = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        reciew.setLayoutManager(horizontalLayoutManagaer);
        onPlayerSelectionSet(R.id.action_list);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        friendsAdapter.update(new ArrayList<Friends>(results));
    }


    public void updateUserCallback(List<Friends> data) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
        results = realm.where(Friends.class).findAll();
        List<Friends> freinds = new ArrayList<>(results);
        friendsAdapter.update(new ArrayList<Friends>(results));

    }

    @Override
    public void onStart() {
        super.onStart();
        FriendsFPresenter.getInstance().attachScreen(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        FriendsFPresenter.getInstance().detachScreen();
    }

    @Override
    public void onPlayerSelectionSet(int id) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (id) {

            case R.id.action_map:
                fragment = new MapFragment();
                break;

            case R.id.action_list:
                fragment = new ListGetFragment();
                break;

        }
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        try {
            FriendsRecAdapter.MyInterface callback = (FriendsRecAdapter.MyInterface) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement FriendsRecAdapter.MyInterface");
        }
        friendsAdapter = new FriendsRecAdapter((FriendsRecAdapter.MyInterface) fragment);
        reciew.setAdapter(friendsAdapter);
        updateUserCallback(new ArrayList<Friends>(results));

    }


}

