package com.example.kata.onlab.ui.list;

import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.Presenter;

/**
 * Created by Kata on 2017. 02. 26..
 */

public class ListPresenter  extends Presenter<ListScreen> {

    private static ListPresenter instance = null;

    private ListPresenter() {
    }

    public static ListPresenter getInstance() {
        if (instance == null) {
            instance = new ListPresenter();
        }
        return instance;
    }


    public void updateNetworkData(){
        NetworkManager.getInstance().updateData();
    }


    public void newItemView(){
        screen.newItemView();
    }

    @Override
    public void attachScreen(ListScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }
}
