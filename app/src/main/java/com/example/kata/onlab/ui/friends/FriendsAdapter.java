package com.example.kata.onlab.ui.friends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.ui.friendsearch.FriendSearchActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kata on 2017. 04. 16..
 */

public class FriendsAdapter extends BaseAdapter {
    List<Friends> friends=new ArrayList<>();
    Context mContext;

    public FriendsAdapter(Context context, List<Friends> friends) {
        this.mContext = context;
        this.friends = friends;
    }


    @Override
    public int getCount() {
        return 1+friends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView=mInflater.inflate(R.layout.friend_item, null);
        if (position==0){
            CircularImageView cimage= (CircularImageView) convertView.findViewById(R.id.item);
            cimage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_add_white_36dp));
            cimage.setBorderColor(R.color.blue_normal);
            cimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, FriendSearchActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }
}
