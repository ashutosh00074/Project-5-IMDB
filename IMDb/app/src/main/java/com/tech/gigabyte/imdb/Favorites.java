package com.tech.gigabyte.imdb;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 *
 */

public class Favorites extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView_Favorites;
    public View view_Favorites;
    SwipeRefreshLayout swipeRefreshLayout_Favorites;

    public Favorites() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);
        view_Favorites = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView_Favorites = view_Favorites.findViewById(R.id.recyclerView_favorites);
        if (checkConnection()) {
            load_FavoritesData();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
        }
        recyclerView_Favorites.smoothScrollToPosition(0);
        Toast.makeText(getActivity().getApplicationContext(), "Swipe down to refresh", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout_Favorites = view_Favorites.findViewById(R.id.swipeRefreshLayout_Favorites);
        swipeRefreshLayout_Favorites.setOnRefreshListener(this);

        return view_Favorites;
    }

    private boolean checkConnection() {
        return Receiver.isConnected();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    public void load_FavoritesData() {
        int mode = 1;
        JSONFavorites downloadJSON_favorites = new JSONFavorites(getActivity().getApplicationContext(), recyclerView_Favorites, mode);
        downloadJSON_favorites.execute();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout_Favorites.setRefreshing(true);
        Log.d("Swipe", "Refreshing favorites");
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout_Favorites.setRefreshing(false);
                if (checkConnection()) {
                    load_FavoritesData();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }
}
