package com.example.kata.onlab.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.AddPlaceFragment;
import com.example.kata.onlab.ui.list.ListGetFragment;
import com.example.kata.onlab.ui.map.MapFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddPlaceFragment.IAddPlaceFragment, MainScreen {

    private ViewPager viewPager;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
           }

    private void setupViewPager(ViewPager viewPager) {
        TablayoutAdapter tablayoutAdapter = new TablayoutAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tablayoutAdapter);
    }


    @Override
    public void onNewItemCreated(Data newItem) {
        NetworkManager.getInstance().postData(newItem);
    }


    public Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
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
        Fragment fragment = getFragment();

        if (fragment instanceof ListGetFragment) {
            ((ListGetFragment) fragment).postDataCallback(item);
        }
        if (fragment instanceof MapFragment) {
            ((MapFragment) fragment).postDataCallback(item);
        }
    }

    @Override
    public void updateDataCallback(List<Data> items) {
        Fragment fragment = getFragment();

        if (fragment instanceof ListGetFragment)
        {
            ((ListGetFragment) fragment).updateDataCallback(items);
        }
        if (fragment instanceof MapFragment)
        {
            ((MapFragment) fragment).updateDataCallback(items);
        }
    }
}
