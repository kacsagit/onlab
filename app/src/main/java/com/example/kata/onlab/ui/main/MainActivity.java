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
    private TablayoutAdapter tablayoutAdapter;

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
        NetworkManager.getInstance().updateData();
    }

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
        for (int i = 0; i < tablayoutAdapter.getCount(); i++) {
            Fragment fragment = getFragment(i);

            if (fragment instanceof ListGetFragment) {
                ((ListGetFragment) fragment).updateDataCallback(items);
            }
            if (fragment instanceof MapFragment) {
                ((MapFragment) fragment).updateDataCallback(items);
            }
        }
    }
}
