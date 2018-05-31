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
 * Up-Coming Fragment for showing movies
 * Swipe to Refresh
 */

public class Upcoming extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Upcoming() {}

    SwipeRefreshLayout swipeRefreshLayout_Upcoming;
    View view_upComing;
    RecyclerView lv_upcoming;
    String URL_UC = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    //When View Created , Setting layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view_upComing = inflater.inflate(R.layout.fragment_upcoming, container, false);
        lv_upcoming = view_upComing.findViewById(R.id.recyclerView_upComing);
        if (checkConnection()) {
            loadUpcomingData();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout_Upcoming = view_upComing.findViewById(R.id.swipeRefreshLayout_upComing);
        swipeRefreshLayout_Upcoming.setOnRefreshListener(this);
        lv_upcoming.smoothScrollToPosition(0);
        return view_upComing;
    }

    //Network-Connection Check
    private boolean checkConnection() {
        return Receiver.isConnected();
    }

    //Load JSONData
    private void loadUpcomingData() {
        JSONDownload JSONDownload = new JSONDownload(getActivity().getApplication().getApplicationContext(), lv_upcoming);
        JSONDownload.getJSON(URL_UC);
    }

    //On SWIPE-to-REFRESH
    @Override
    public void onRefresh() {
        swipeRefreshLayout_Upcoming.setRefreshing(true);
        Log.d("Swipe", "Refreshing Upcoming");
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout_Upcoming.setRefreshing(false);
                if (checkConnection()) {
                    loadUpcomingData();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }
}


