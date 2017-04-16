package com.example.kata.onlab.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kata.onlab.ui.FriendsFragment;
import com.example.kata.onlab.ui.list.ListGetFragment;
import com.example.kata.onlab.ui.map.MapFragment;

/**
 * Created by Kata on 2017. 02. 18..
 */
public class TablayoutAdapter extends FragmentPagerAdapter {

    public TablayoutAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FriendsFragment();
            case 1:
                return new MapFragment();
            default:
                return new ListGetFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "List";
            case 1:
                return "Map";
            default:
                return "unknown";
        }
    }
}

