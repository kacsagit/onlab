package com.example.kata.onlab.ui.friends;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FriendsActivity extends AppCompatActivity implements  FriendScreen {
    List<Friends> friends;
    GridView view;
    FriendsAdapter friendsAdapter;
    Realm realm;
    RealmResults<Friends> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Friends");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "friends").build();
        //Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(Friends.class).findAll();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GridView view= (GridView) findViewById(R.id.gridview);
        friends=new ArrayList<Friends>();
        friendsAdapter=new FriendsAdapter(this, friends);
        view.setAdapter(friendsAdapter);


    }
    protected void onResume(){
        super.onResume();
        NetworkManager.getInstance().getfriends();
        friendsAdapter.update(new ArrayList<Friends>(results));
    }


    @Override
    protected void onStart() {
        super.onStart();
        FriendPresenter.getInstance().attachScreen(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FriendPresenter.getInstance().detachScreen();
    }

    @Override
    public void updateUserCallback(List<Friends> data) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
        results = realm.where(Friends.class).findAll();
        friendsAdapter.update(new ArrayList<Friends>(results));

    }

}
