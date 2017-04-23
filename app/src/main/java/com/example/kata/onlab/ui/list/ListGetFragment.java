package com.example.kata.onlab.ui.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.AddPlaceFragment;
import com.example.kata.onlab.ui.friendsfragment.FriendsRecAdapter;
import com.example.kata.onlab.ui.friendsfragment.OnMenuSelectionSetListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Kata on 2017. 02. 18..
 */
public class ListGetFragment extends Fragment implements ListScreen, FriendsRecAdapter.MyInterface {

    Realm realm;
    RealmResults<Data> results;
    protected RecyclerView recyclerView;
    protected ItemAdapter adapter;
    protected SwipeRefreshLayout swipeRefresh;
    protected View view;
    protected Context context;
    protected  List<Data> items;
    private static final String TAG = "ListGetFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        getActivity().invalidateOptionsMenu();
        onAttachToParentFragment(getParentFragment());
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


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "data").build();
        // Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(Data.class).findAll();
        items=new ArrayList<>(results);
        setHasOptionsMenu(true);
        return view;

    }

    public void newItemView(){
        new AddPlaceFragment().show(getActivity().getSupportFragmentManager(), AddPlaceFragment.TAG);
    }


    public void updateData() {
        swipeRefresh.setRefreshing(true);
        ListPresenter.getInstance().updateNetworkData();
    }

    public void postDataCallback(Data item){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();
        items.add(item);
        adapter.addItem(item);
    }

    public void updateDataCallback(List<Data> list) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(items);
        realm.commitTransaction();
        results = realm.where(Data.class).findAll();
        items=list;
        adapter.update(items);
        swipeRefresh.setRefreshing(false);
    }

    protected void initRecycleView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.MainRecyclerView);
        adapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_list, menu);
    }


    @Override
    public void sort(int id) {
        List<Data> temp = new ArrayList<Data>();
        for (Data d : items) {
            if (d.ownerid == id)
                temp.add(d);
        }
        adapter.update(temp);
    }


    @Override
    public void unsort() {
        adapter.update(items);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_map:
                // do stuff
                mOnPlayerSelectionSetListener.onPlayerSelectionSet(id);
                /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new MapFragment());
                ft.commit();*/
                return true;

        }

        return false;
    }



    @Override
    public void onResume() {
        super.onResume();
        updateDataCallback(items);
        NetworkManager.getInstance().updateData();

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
        ListPresenter.getInstance().detachScreen();
    }

    OnMenuSelectionSetListener mOnPlayerSelectionSetListener;


    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            mOnPlayerSelectionSetListener = (OnMenuSelectionSetListener)fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnMenuSelectionSetListener");
        }
    }

}
