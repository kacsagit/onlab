package com.example.kata.onlab.ui.friendsfragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.FriendDetail;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.NetApi;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kata on 2017. 04. 16..
 */

public class FriendsRecAdapter extends RecyclerView.Adapter<FriendsRecAdapter.ItemViewHolder> {
    List<Friends> friends = new ArrayList<>();
    Context mContext;
    private MyInterface listener;

    public FriendsRecAdapter(MyInterface listen) {
        listener = listen;

    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.friends_itemrecycler, parent, false);
        mContext = parent.getContext();
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        Picasso.with(holder.imageView.getContext()).cancelRequest(holder.imageView);
        if (position == 0) {
            holder.imageView.setImageDrawable(null);
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.all));
            final TypedValue value = new TypedValue ();
            mContext.getTheme().resolveAttribute (R.attr.colorPrimaryDark, value, true);
            holder.imageView.setBorderColor(value.data);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.unsort();
                }
            });
        } else {
            final Friends friend = friends.get(position - 1);
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.avatar));
            holder.imageView.setBorderColor(Color.parseColor("#EEEEEE"));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.sort(friend.id);
                }
            });
            if (friend.image != null) {
                String url = NetApi.GETIMEAGE +friend.image;
                Picasso.with(mContext).load(url).placeholder(R.drawable.avatar).into(holder.imageView);

            }
        }

    }

    public interface MyInterface {
        public void sort(int id);

        public void unsort();
    }


    @Override
    public int getItemCount() {
        return friends.size() + 1;
    }


    public void update(List<Friends> itemsrec) {
        friends.clear();
        friends.addAll(itemsrec);
        notifyDataSetChanged();

    }

    public void add(FriendDetail friendd) {
        Friends friend = new Friends(friendd.id, friendd.name, friendd.image);
        friends.add(friend);
        notifyItemInserted(friends.size() - 1 + 1);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        CircularImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (CircularImageView) itemView.findViewById(R.id.item);

        }
    }
}
