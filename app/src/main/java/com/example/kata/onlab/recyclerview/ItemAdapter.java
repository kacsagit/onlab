package com.example.kata.onlab.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.R;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public final List<Data> items;

    public ItemAdapter() {
        items = new ArrayList<>();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_recyclerview, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Data item = items.get(position);
        holder.longitude.setText(Float.toString(item.longitude));
        holder.latitude.setText(Float.toString(item.latitude));
        holder.place.setText(item.place);

    }

    public void addItem(Data item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);

    }


    public void onItemDismiss(int position) {
        Data removed = items.remove(position);
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
        Data prev = items.remove(fromPosition);
        items.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(List<Data> itemsrec) {
        items.clear();
        items.addAll(itemsrec);
        notifyDataSetChanged();

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView place;
        TextView longitude;
        TextView latitude;

        public ItemViewHolder(View itemView) {
            super(itemView);
            place = (TextView) itemView.findViewById(R.id.place);
            longitude = (TextView) itemView.findViewById(R.id.longitude);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"clicked="+ getPosition(),Toast.LENGTH_SHORT).show();

                }
            });*/

        }


    }
}
