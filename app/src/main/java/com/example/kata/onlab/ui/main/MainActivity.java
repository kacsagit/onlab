package com.example.kata.onlab.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.AddPlaceFragment;
import com.example.kata.onlab.ui.MyPoints.MyPoints;
import com.example.kata.onlab.ui.friends.FriendsActivity;
import com.example.kata.onlab.ui.list.FriendsFragment;
import com.example.kata.onlab.ui.list.ListGetFragment;
import com.example.kata.onlab.ui.login.LoginActivity;
import com.example.kata.onlab.ui.map.MapFragment;
import com.example.kata.onlab.ui.map.ServiceLocation;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements AddPlaceFragment.IAddPlaceFragment, MainScreen,NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener{

    Realm realm;
    RealmResults<Data> results;
    private ViewPager viewPager;
    private TablayoutAdapter tablayoutAdapter;

    private static final String TAG = "MainActivity";
    public static final String KEY_START_SERVICE = "start_service";
    SwitchCompat switchCompat;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        View header = navigationView.getHeaderView(0);
        TextView user= (TextView) header.findViewById(R.id.textView);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        String email = preferences.getString("Email", "");
        user.setText(email);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "data").build();
        // Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(Data.class).findAll();


        MenuItem menu=navigationView.getMenu().findItem(R.id.nav_number);
        LinearLayout i= (LinearLayout) menu.getActionView();
        switchCompat = (SwitchCompat) i.findViewById(R.id.toggleButton);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_START_SERVICE, isChecked);
                editor.apply();
            }
        });

        if (preferences.getBoolean(KEY_START_SERVICE, true)) {
            switchCompat.setChecked(true);
            Intent intetn2 = new Intent(this, ServiceLocation.class);
            startService(intetn2);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mMessageReceiver,
                    new IntentFilter(ServiceLocation.BR_NEW_LOCATION));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateFragments();
        NetworkManager.getInstance().updateData();


    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location currentLocation = intent.getParcelableExtra(ServiceLocation.KEY_LOCATION);

        }
    };

    private void setupViewPager(ViewPager viewPager) {
        tablayoutAdapter = new TablayoutAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tablayoutAdapter);
    }


    @Override
    public void onNewItemCreated(Data newItem) {
        NetworkManager.getInstance().postData(newItem);
    }


    public Fragment getFragment(int id) {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + id);
    }


    @Override
    protected void onStart() {
        super.onStart();
        MainPresenter.getInstance().attachScreen(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainPresenter.getInstance().detachScreen();
    }

    @Override
    public void postDataCallback(Data item) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();

        for (int i = 0; i < tablayoutAdapter.getCount(); i++) {
            Fragment fragment = getFragment(i);

            if (fragment instanceof ListGetFragment) {
                ((ListGetFragment) fragment).postDataCallback(item);
            }
            if (fragment instanceof MapFragment) {
                ((MapFragment) fragment).postDataCallback(item);
            }
        }
    }

    @Override
    public void updateDataCallback(List<Data> items) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(items);
        realm.commitTransaction();
        results = realm.where(Data.class).findAll();
        updateFragments();
    }


    public void updateFragments(){
        for (int i = 0; i < tablayoutAdapter.getCount(); i++) {
            Fragment fragment = getFragment(i);

            if (fragment instanceof FriendsFragment) {
                ((FriendsFragment) fragment).updateDataCallback(new ArrayList<Data>(results));
            }else if (fragment instanceof ListGetFragment) {
                ((ListGetFragment) fragment).updateDataCallback(new ArrayList<Data>(results));
            }
            if (fragment instanceof MapFragment) {
                ((MapFragment) fragment).updateDataCallback(new ArrayList<Data>(results));
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

     /*   //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Token", "");
            editor.putString("Email", "");
            editor.apply();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                        String token=FirebaseInstanceId.getInstance().getToken();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            LoginManager.getInstance().logOut();
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(this, MyPoints.class);
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(this, FriendsActivity.class);
            startActivity(intent);

        }/* else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/
        if (id == R.id.nav_number) {

            switchCompat.toggle();
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (KEY_START_SERVICE.equals(key)) {
            boolean startService = sharedPreferences.getBoolean(KEY_START_SERVICE, false);
            Intent i = new Intent(getApplicationContext(),ServiceLocation.class);
            if (startService) {
                startService(i);
                LocalBroadcastManager.getInstance(this).registerReceiver(
                        mMessageReceiver,
                        new IntentFilter(ServiceLocation.BR_NEW_LOCATION));
            } else {
                stopService(i);
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            }
        }
    }
}
