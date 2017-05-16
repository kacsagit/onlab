package com.example.kata.onlab.adapter;

/**
 * Created by Kata on 2017. 04. 16..
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kata.onlab.databinding.ItemWordBinding;
import com.example.kata.onlab.db.FriendsComp;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;

public class PeopleAdapter extends SortedListAdapter<FriendsComp> {

    public interface Listener {
        void onExampleModelClicked(FriendsComp model);
    }

    private final Listener mListener;

    public PeopleAdapter(Context context, Comparator<FriendsComp> comparator, Listener listener) {
        super(context, FriendsComp.class, comparator);
        mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder<? extends FriendsComp> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final ItemWordBinding binding = ItemWordBinding.inflate(inflater, parent, false);
        return new PeopleAdapter.WordViewHolder(binding, mListener);
    }



    public class WordViewHolder extends SortedListAdapter.ViewHolder<FriendsComp> {

        private final ItemWordBinding mBinding;

        public WordViewHolder(ItemWordBinding binding, PeopleAdapter.Listener listener) {
            super(binding.getRoot());
            binding.setListener(listener);

            mBinding = binding;
        }

        @Override
        protected void performBind(@NonNull FriendsComp item) {
            mBinding.setModel(item);
        }
    }
}