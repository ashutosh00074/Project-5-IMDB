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


public class Watchlist extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView_Watchlist;
    public View view_Watchlist;
    SwipeRefreshLayout swipeRefreshLayout_Watchlist;

    public Watchlist() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);
        view_Watchlist = inflater.inflate(R.layout.fragment_watchlist, container, false);
        recyclerView_Watchlist = view_Watchlist.findViewById(R.id.recyclerView_watchlist);
        if (checkConnection()) {
            load_WatchlistData();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
        }
        recyclerView_Watchlist.smoothScrollToPosition(0);
        Toast.makeText(getActivity().getApplicationContext(), "Swipe down to refresh", Toast.LENGTH_LONG).show();
        swipeRefreshLayout_Watchlist = view_Watchlist.findViewById(R.id.swipeRefreshLayout_Watchlist);
        swipeRefreshLayout_Watchlist.setOnRefreshListener(this);
        return view_Watchlist;
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

    public void load_WatchlistData() {
        int mode = 2;
        JSONFavorites downloadJSON_watchlist = new JSONFavorites(getActivity().getApplicationContext(), recyclerView_Watchlist, mode);
        downloadJSON_watchlist.execute();
    }

    //On SWIPE-to-REFRESH
    @Override
    public void onRefresh() {
        swipeRefreshLayout_Watchlist.setRefreshing(true);
        Log.d("Swipe", "Refreshing Watchlist");
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout_Watchlist.setRefreshing(false);
                if (checkConnection()) {
                    load_WatchlistData();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }
}
