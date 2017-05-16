package com.example.kata.onlab.ui.friendsfragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kata.onlab.R;
import com.example.kata.onlab.adapter.FriendsFragmentAdapter;
import com.example.kata.onlab.db.Friends;
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
    FriendsFragmentAdapter friendsAdapter;
    Fragment fragment;
    RecyclerView reciew;

    private static final int LIST = R.id.action_list;
    private static final int MAP = R.id.action_map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Friends todos");

        reciew = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        reciew.setLayoutManager(horizontalLayoutManagaer);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            int frag = savedInstanceState.getInt("curChoice", LIST);
            onPlayerSelectionSet(frag);
        } else {
            onPlayerSelectionSet(LIST);
        }
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
                fragmentTransaction.replace(R.id.content_frame, fragment, "MAP");
                break;

            case R.id.action_list:
                fragment = new ListGetFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment, "LIST");
                break;

        }
        fragmentTransaction.commit();
        try {
            FriendsFragmentAdapter.MyInterface callback = (FriendsFragmentAdapter.MyInterface) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement FriendsFragmentAdapter.MyInterface");
        }
        friendsAdapter = new FriendsFragmentAdapter((FriendsFragmentAdapter.MyInterface) fragment);
        reciew.setAdapter(friendsAdapter);
        updateUserCallback(new ArrayList<Friends>(results));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListGetFragment myFragmentlist = (ListGetFragment) getChildFragmentManager().findFragmentByTag("LIST");
        if (myFragmentlist != null && myFragmentlist.isVisible()) {
            outState.putInt("curChoice", LIST);
        } else {
            MapFragment myFragmentmap = (MapFragment) getChildFragmentManager().findFragmentByTag("MAP");
            if (myFragmentmap != null && myFragmentmap.isVisible()) {
                outState.putInt("curChoice", MAP);
            }
        }

    }


}

