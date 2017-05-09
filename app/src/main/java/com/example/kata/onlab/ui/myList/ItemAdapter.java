package com.example.kata.onlab.ui.myList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.MyData;
import com.example.kata.onlab.network.DataDetails;
import com.example.kata.onlab.network.NetApi;
import com.example.kata.onlab.network.NetworkManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public final List<DataDetails> items;
    Context mContext;
    int mExpandedPosition = -1;


    public ItemAdapter() {
        items = new ArrayList<>();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_recyclerview, parent, false);
        mContext = parent.getContext();
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final DataDetails item = items.get(position);
        if (item.image != null) {
            String url = NetApi.GETIMEAGE + item.image;
            Picasso.with(mContext).load(url).placeholder(R.drawable.avatar).into(holder.image);

        }
        holder.place.setText(item.place);

        final boolean isExpanded = position == mExpandedPosition;
        holder.details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                NetworkManager.getInstance().getDataDetails(item.id);
                /*TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();*/
            }
        });
        holder.description.setText(item.description);

        holder.latitude.setText(String.format("%.3f",item.latitude));
        holder.longitude.setText(String.format("%.3f",item.longitude));
        holder.checkBox.setChecked(item.done> 0);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NetworkManager.getInstance().pushNotif(item);
                }
            }
        });


    }

    public void addItem(MyData item) {
        items.add(new DataDetails(item));
        notifyItemInserted(items.size() - 1);

    }


    public void onItemDismiss(int position) {
        DataDetails removed = items.remove(position);
        // removed.delete();
        notifyItemRemoved(position);
        if (position < items.size()) {
            notifyItemRangeChanged(position, items.size() - position);
        }
    }

    public void removeAllItems() {
        int count = items.size();
        items.clear();
        notifyItemRangeRemoved(0, count);
    }


    public void onItemMove(int fromPosition, int toPosition) {
        DataDetails prev = items.remove(fromPosition);
        items.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(List<MyData> itemsrec) {
        items.clear();
        for (MyData item : itemsrec) {
            items.add(new DataDetails(item));
        }
        notifyDataSetChanged();
        mExpandedPosition = -1;

    }

    public void sort(int id) {

    }

    public void all() {
    }

    public void updateData(DataDetails data) {
        int indx = 0;
        for (DataDetails item : items) {
            if (item.id == data.id) {
                items.set(indx, data);
            }
            indx++;
        }
        notifyDataSetChanged();

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView place;
        ImageView image;
        LinearLayout details;
        TextView description;
        TextView longitude;
        TextView latitude;
        CheckBox checkBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            place = (TextView) itemView.findViewById(R.id.place);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            details = (LinearLayout) itemView.findViewById(R.id.details);
            description = (TextView) itemView.findViewById(R.id.description);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            longitude = (TextView) itemView.findViewById(R.id.longitude);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"clicked="+ getPosition(),Toast.LENGTH_SHORT).show();

                }
            });*/

        }


    }
}
