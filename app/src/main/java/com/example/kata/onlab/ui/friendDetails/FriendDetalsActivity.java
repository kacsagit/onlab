package com.example.kata.onlab.ui.friendDetails;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.FriendDetail;
import com.example.kata.onlab.network.NetworkManager;

public class FriendDetalsActivity extends AppCompatActivity implements FriendDetailsScreen {
    TextView email;
    ImageView image;
    Toolbar toolbar;
    public static final String ID = "id";
    public static final String NAME = "name";
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detals);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle extras = getIntent().getExtras();
        NetworkManager.getInstance().getuser(extras.getInt(ID));
        getSupportActionBar().setTitle(extras.getString(NAME));

        email= (TextView) findViewById(R.id.email);
        image = (ImageView) findViewById(R.id.imageView);
        fab= (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FriendDetailsPresenter.getInstance().attachScreen(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FriendDetailsPresenter.getInstance().detachScreen();
    }

    @Override
    public void updateUserCallback(FriendDetail data) {
        email.setText(data.email);
        fab.setImageResource(R.mipmap.ic_launcher);
    }
}
