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
import com.example.kata.onlab.network.FriendDetail;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.ui.friendDetails.FriendDetalsActivity;
import com.example.kata.onlab.ui.friendsearch.FriendSearchActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kata on 2017. 04. 16..
 */

public class FriendsAdapter extends BaseAdapter {
    List<Friends> friends = new ArrayList<>();
    Context mContext;

    public FriendsAdapter(Context context, List<Friends> friends) {
        this.mContext = context;
        this.friends = friends;
    }


    @Override
    public int getCount() {
        return 1 + friends.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.friend_item, null);
        CircularImageView cimage = (CircularImageView) convertView.findViewById(R.id.item);
        if (position == 0) {
            cimage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_add_white_36dp));
            cimage.setBorderColor(R.color.blue_normal);
            cimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendSearchActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {
            cimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendDetalsActivity.class);
                    intent.putExtra(FriendDetalsActivity.ID, friends.get(position - 1).id);
                    intent.putExtra(FriendDetalsActivity.NAME, friends.get(position - 1).name);
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    public void update(List<Friends> itemsrec) {
        friends.clear();
        friends.addAll(itemsrec);
        notifyDataSetChanged();

    }

    public void add(FriendDetail friendd) {
        Friends friend=new Friends(friendd.id,friendd.name);
        friends.add(friend);
        notifyDataSetChanged();
    }
}
