package com.example.kata.onlab.ui.newPlace;

import com.example.kata.onlab.event.PostDataEvent;
import com.example.kata.onlab.db.DataDetails;
import com.example.kata.onlab.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Kata on 2017. 03. 28..
 */

public class PlacePickerPresenter extends Presenter<PlacePickerScreen> {

    private static PlacePickerPresenter instance = null;

    private PlacePickerPresenter() {
    }

    public static PlacePickerPresenter getInstance() {
        if (instance == null) {
            instance = new PlacePickerPresenter();
        }
        return instance;
    }

    @Override
    public void attachScreen(PlacePickerScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostData(PostDataEvent<DataDetails> event) {
        if (screen != null) {
            screen.postDataCallback(event.getData());
        }
    }



}
