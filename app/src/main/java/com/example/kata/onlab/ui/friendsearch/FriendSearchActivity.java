package com.example.kata.onlab.ui.friendsearch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.kata.onlab.R;
import com.example.kata.onlab.databinding.ActivityFriendSearchBinding;
import com.example.kata.onlab.network.Friends;
import com.example.kata.onlab.network.FriendsComp;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.friendDetails.FriendDetalsActivity;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FriendSearchActivity extends AppCompatActivity implements FriendSearchScreen,SearchView.OnQueryTextListener, SortedListAdapter.Callback {
    Context mContext;
    private static final Comparator<FriendsComp> COMPARATOR = new SortedListAdapter.ComparatorBuilder<FriendsComp>()
            .setOrderForModel(FriendsComp.class, new Comparator<FriendsComp>() {
                @Override
                public int compare(FriendsComp a, FriendsComp b) {
                    if (a.name==null || b.name==null) return 0;
                    return (a.name.compareTo(b.name));
                }
            })
            .build();

    private ExampleAdapter mAdapter;
    private ActivityFriendSearchBinding mBinding;
    private Animator mAnimator;
    RealmResults<Friends> results;

    private List<FriendsComp> mModels;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_friend_search);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "users").build();
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(Friends.class).findAll();

        setSupportActionBar(mBinding.toolbars.toolbar);
        setTitle("Find friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mBinding.toolbars.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        NetworkManager.getInstance().getusers();

        mAdapter = new ExampleAdapter(this, COMPARATOR, new ExampleAdapter.Listener() {
            @Override
            public void onExampleModelClicked(FriendsComp model) {
                //Snackbar.make(mBinding.getRoot(), Integer.toString(model.id), Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, FriendDetalsActivity.class);
                intent.putExtra(FriendDetalsActivity.ID, model.id);
                intent.putExtra(FriendDetalsActivity.NAME, model.name);
                mContext.startActivity(intent);
            }
        });

        mAdapter.addCallback(this);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        mModels = new ArrayList<>();
        /*final String[] words = getResources().getStringArray(R.array.words);
        for (int i = 0; i < words.length; i++) {
            mModels.add(new Friends(i, i + 1, words[i]));
        }*/

        List<Friends> list=new ArrayList<>(results);

        for (Friends f :list) {
            FriendsComp fc=new FriendsComp(f.id,f.name,f.image);
            mModels.add(fc);
        }
        mAdapter.edit()
                .replaceAll(mModels)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<FriendsComp> filteredModelList = filter(mModels, query);
        mAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private static List<FriendsComp> filter(List<FriendsComp> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<FriendsComp> filteredModelList = new ArrayList<>();
        for (FriendsComp model : models) {
            final String text = model.name.toLowerCase();
            if (text.contains(lowerCaseQuery) ) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onEditStarted() {
        if (mBinding.editProgressBar.getVisibility() != View.VISIBLE) {
            mBinding.editProgressBar.setVisibility(View.VISIBLE);
            mBinding.editProgressBar.setAlpha(0.0f);
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.editProgressBar, View.ALPHA, 1.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();

        mBinding.recyclerView.animate().alpha(0.5f);
    }

    @Override
    public void onEditFinished() {
        mBinding.recyclerView.scrollToPosition(0);
        mBinding.recyclerView.animate().alpha(1.0f);

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.editProgressBar, View.ALPHA, 0.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {

            private boolean mCanceled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!mCanceled) {
                    mBinding.editProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mAnimator.start();
    }

    @Override
    public void updateUserCallback(List<Friends> data) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();
        results = realm.where(Friends.class).findAll();
        List<Friends> list=new ArrayList<>(results);

        for (Friends f :list) {
            FriendsComp fc=new FriendsComp(f.id,f.name,f.image);
            mModels.add(fc);
        }
        mAdapter.edit()
                .replaceAll(mModels)
                .commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FriendSearchPresenter.getInstance().attachScreen(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FriendSearchPresenter.getInstance().detachScreen();
    }
}


