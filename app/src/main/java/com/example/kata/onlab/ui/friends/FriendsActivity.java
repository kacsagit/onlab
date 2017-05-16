package com.example.kata.onlab.ui.friends;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kata.onlab.R;
import com.example.kata.onlab.adapter.FriendsRecAdapter;
import com.example.kata.onlab.db.Friends;
import com.example.kata.onlab.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.example.kata.onlab.R.id.recyclerView;

public class FriendsActivity extends AppCompatActivity implements FriendScreen {
    List<Friends> friends;
    FriendsRecAdapter friendsAdapter;
    Realm realm;
    RealmResults<Friends> results;
    RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "friends").build();
        //Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(Friends.class).findAll();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        view = (RecyclerView) findViewById(recyclerView);


        friends = new ArrayList<Friends>();


    }

    protected void onResume() {
        super.onResume();
        NetworkManager.getInstance().getfriends();
        int numberOfColumns = 2;
        if (this.getResources().getConfiguration().orientation == this.getResources().getConfiguration() .ORIENTATION_LANDSCAPE)
            numberOfColumns = 4;

        view.setLayoutManager(new GridLayoutManager(this, numberOfColumns, GridLayoutManager.VERTICAL, false));
        friendsAdapter = new FriendsRecAdapter();
        view.setAdapter(friendsAdapter);
        friendsAdapter.update(new ArrayList<Friends>(results));
        updateUserCallback(new ArrayList<Friends>(results));
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
