package com.example.kata.onlab.ui.friendDetails;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.FriendDetail;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.NetApi;
import com.example.kata.onlab.network.NetworkManager;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FriendDetalsActivity extends AppCompatActivity implements FriendDetailsScreen {
    TextView email;
    ImageView image;
    Toolbar toolbar;
    FriendDetail friend;
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

        getSupportActionBar().setTitle(extras.getString(NAME));


        email = (TextView) findViewById(R.id.email);
        image = (ImageView) findViewById(R.id.imageView);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friend != null) {
                    if (fab.isActivated()) {
                        NetworkManager.getInstance().deleteFriend(friend);
                    } else {
                        NetworkManager.getInstance().postFriend(friend);

                    }
                }
            }
        });
        NetworkManager.getInstance().getuser(extras.getInt(ID));
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
        friend = data;
        email.setText(friend.email);
        if (friend.image != null) {
            String url = NetApi.GETIMEAGE +friend.image;
            url = url.replace("\\", "/");
            Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher).into(image);
        }

        if (friend.isfriend == 1) {
            fab.setActivated(true);

        }
    }

    @Override
    public void updateFriendCallback(FriendDetail friend) {
        friend.isfriend = 1;
        fab.setActivated(true);
    }
    @Override
    public void deleteFriendCallback(FriendDetail data){
        friend.isfriend = 0;
        fab.setActivated(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "friends").build();
        //Realm.deleteRealm(realmConfiguration);
        Realm realm = Realm.getInstance(realmConfiguration);
        final RealmResults<Friends> result = realm.where(Friends.class).equalTo("id",friend.id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
        realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "data").build();
        //Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        final RealmResults<Data> results = realm.where(Data.class).equalTo("ownerid",friend.id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    };
}
