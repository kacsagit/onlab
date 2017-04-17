package com.example.kata.onlab.ui.friends;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements  FriendScreen {
    List<Friends> friends;
    GridView view;
    FriendsAdapter friendsAdapter;

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
        NetworkManager.getInstance().getfriends();
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
        friendsAdapter.update(data);
    }

}
