package com.example.kata.onlab.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.R;
import com.example.kata.onlab.TablayoutAdapter;
import com.example.kata.onlab.event.ErrorEvent;
import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.PostDataEvent;
import com.example.kata.onlab.network.NetworkManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddPlaceFragment.IAddPlaceFragment {

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetData(GetDataEvent<List<Data>> event) {
        Fragment fragment = getFragment();

        if (fragment instanceof ListGetFragment)
        {
            ((ListGetFragment) fragment).updateDataCallback(event.getData());
        }
        if (fragment instanceof MapFragment)
        {
            ((MapFragment) fragment).updateDataCallback(event.getData());
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostData(PostDataEvent<Data> event) {
        Fragment fragment = getFragment();

        if (fragment instanceof ListGetFragment)
        {
            ((ListGetFragment) fragment).postDataCallback(event.getData());
        }
        if (fragment instanceof MapFragment)
        {
            ((MapFragment) fragment).postDataCallback(event.getData());
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event) {
        event.getE().printStackTrace();
    }

    public Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();


    }
}
