package com.example.kata.onlab.ui.MyPoints;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.kata.onlab.R;
import com.example.kata.onlab.db.Data;
import com.example.kata.onlab.adapter.ListItemAdapter;

import java.util.List;

public class MyPoints extends AppCompatActivity implements MyPointsScreen {
    private RecyclerView recyclerView;
    ListItemAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points);
        initRecycleView();
        context=this;
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }




    public void updateData() {
        swipeRefresh.setRefreshing(true);
        MyPointsPresenter.getInstance().updateNetworkData();
    }
    @Override
    public void postDataCallback(Data item){
        adapter.addItem(item);
    }

    @Override
    public void updateDataCallback(List<Data> list) {
        adapter.update(list);
        swipeRefresh.setRefreshing(false);
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.MainRecyclerView);
        adapter = new ListItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }



    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onStart() {
        super.onStart();
        MyPointsPresenter.getInstance().attachScreen(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyPointsPresenter.getInstance().detachScreen();
    }

}