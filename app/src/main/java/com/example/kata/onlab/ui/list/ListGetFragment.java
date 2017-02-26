package com.example.kata.onlab.ui.list;

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

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.ui.AddPlaceFragment;
import com.example.kata.onlab.ui.main.MainPresenter;

import java.util.List;

/**
 * Created by Kata on 2017. 02. 18..
 */
public class ListGetFragment extends Fragment implements ListScreen{

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
                updateData();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListPresenter.getInstance().newItemView();

            }
        });
        getData();
        return view;

    }

    public void newItemView(){
        new AddPlaceFragment().show(getActivity().getSupportFragmentManager(), AddPlaceFragment.TAG);
    }

    public void getData() {
        swipeRefresh.setRefreshing(true);
        adapter.update(ListPresenter.getInstance().getNetworkData());
        swipeRefresh.setRefreshing(false);
    }


    public void updateData() {
        swipeRefresh.setRefreshing(true);
        ListPresenter.getInstance().updateNetworkData();
    }

    public void postDataCallback(Data item){
        adapter.addItem(item);
    }

    public void updateDataCallback(List<Data> list) {
        adapter.update(list);
        swipeRefresh.setRefreshing(false);
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.MainRecyclerView);
        adapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
        ListPresenter.getInstance().attachScreen(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        MainPresenter.getInstance().detachScreen();
    }

}
