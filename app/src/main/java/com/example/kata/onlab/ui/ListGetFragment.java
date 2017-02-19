package com.example.kata.onlab.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.DataManager;
import com.example.kata.onlab.R;
import com.example.kata.onlab.event.ErrorEvent;
import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.recyclerview.ItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 18..
 */
public class ListGetFragment extends Fragment {

    private RecyclerView recyclerView;
    ItemAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    View view;
    Context context;
    private static final String TAG = "ListGetFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        initRecycleView();
        context=getContext();
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddPlaceFragment().show(getActivity().getSupportFragmentManager(), AddPlaceFragment.TAG);
            }
        });

        return view;

    }


    public void loadData() {
        DataManager.getInstance(this.getContext()).getData();
    }


    private void initRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.MainRecyclerView);
        adapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetData(GetDataEvent<List<Data>> event) {
        adapter.update(event.getData());
        swipeRefresh.setRefreshing(false);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event) {
        event.getE().printStackTrace();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        loadData();
    }

    @Override
    public void onPause() {

        EventBus.getDefault().unregister(this);
        super.onPause();


    }

}
