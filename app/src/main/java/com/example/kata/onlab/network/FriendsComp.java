package com.example.kata.onlab.network;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.example.kata.onlab.R;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Kata on 2017. 04. 16..
 */

public class FriendsComp implements SortedListAdapter.ViewModel {
    public int id;
    public String name;
    public String imageUrl;

    public FriendsComp(int id_, String name_,String image_) {
        id = id_;
        name = name_;
        imageUrl=image_;

    }

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext()).cancelRequest(view);
        Picasso.with(view.getContext())
                .load(NetApi.GETIMEAGE +imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(view);
    }

    @Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof FriendsComp) {
            final FriendsComp friend = (FriendsComp) item;
            return friend.id == id;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof FriendsComp) {
            final FriendsComp other = (FriendsComp) item;
            if (name != other.name) {
                return false;
            }
        }
        return false;
    }
}
