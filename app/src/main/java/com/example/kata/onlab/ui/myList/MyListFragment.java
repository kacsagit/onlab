package com.example.kata.onlab.ui.myList;

/**
 * Created by Kata on 2017. 05. 06..
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.DataDetails;
import com.example.kata.onlab.network.MyData;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.AddPlaceFragment;
import com.example.kata.onlab.ui.friendsfragment.OnMenuSelectionSetListener;
import com.example.kata.onlab.ui.newPlace.PlacePickerActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Kata on 2017. 02. 18..
 */
public class MyListFragment extends Fragment implements ListScreen {

    Realm realm;
    RealmResults<MyData> results;
    protected RecyclerView recyclerView;
    protected ItemAdapter adapter;
    protected SwipeRefreshLayout swipeRefresh;
    protected View view;
    protected Context context;
    protected  List<MyData> items;
    private static final String TAG = "ListGetFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_list, container, false);
        getActivity().invalidateOptionsMenu();
        onAttachToParentFragment(getActivity());
        initRecycleView();
        context=getContext();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My todos");
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
                //ListPresenter.getInstance().newItemView();
                Intent intent=new Intent(context, PlacePickerActivity.class);
                startActivity(intent);

            }
        });
        updateData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "data").build();
        // Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(MyData.class).findAll();
        items=new ArrayList<>(results);
        setHasOptionsMenu(true);
        return view;

    }

    public void newItemView(){
        new AddPlaceFragment().show(getActivity().getSupportFragmentManager(), AddPlaceFragment.TAG);
    }


    public void updateData() {
        swipeRefresh.setRefreshing(true);
        ListPresenter.getInstance().updateDataMy();
    }

    public void postDataCallback(MyData item){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();
        items.add(item);
        adapter.addItem(item);
    }

    public void updateDataCallback(List<MyData> list) {

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(items);
        realm.commitTransaction();
        results = realm.where(MyData.class).findAll();
        items=list;
        adapter.update(items);
        swipeRefresh.setRefreshing(false);
    }

    public void getDataDetailsCallback(DataDetails data){
        adapter.updateData(data);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_map:
                // do stuff
                mOnPlayerSelectionSetListener.onPlayerSelectionSet(id);
                /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new MyMapFragment());
                ft.commit();*/
                return true;

        }

        return false;
    }



    @Override
    public void onResume() {
        super.onResume();
        updateDataCallback(items);
        NetworkManager.getInstance().updateDataMy();

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


    public void onAttachToParentFragment(Activity fragment)
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
