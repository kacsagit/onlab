package com.example.kata.onlab.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.R;
import com.example.kata.onlab.TablayoutAdapter;
import com.example.kata.onlab.network.NetworkManager;

public class MainActivity extends AppCompatActivity implements AddPlaceFragment.IAddPlaceFragment{


    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);


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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();


    }

}
