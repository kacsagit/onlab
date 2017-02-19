package com.example.kata.onlab.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.DataManager;
import com.example.kata.onlab.R;
import com.example.kata.onlab.TablayoutAdapter;
import com.example.kata.onlab.event.ErrorEvent;
import com.example.kata.onlab.event.GetDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddPlaceFragment.IAddPlaceFragment{

    ListGetFragment listGetFragment;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        listGetFragment=new ListGetFragment();
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        TablayoutAdapter tablayoutAdapter = new TablayoutAdapter(getSupportFragmentManager());
        tablayoutAdapter.addFragment(listGetFragment, "List");
        tablayoutAdapter.addFragment(new MapFragment(), "Map");
        viewPager.setAdapter(tablayoutAdapter);
    }

    private void uploadData(Data data) {
        DataManager.getInstance(this).uploadData(data);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetData(GetDataEvent<List<Data>> event) {
        listGetFragment.loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event) {
        event.getE().printStackTrace();
    }

    @Override
    public void onNewItemCreated(Data newItem) {
        uploadData(newItem);
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
