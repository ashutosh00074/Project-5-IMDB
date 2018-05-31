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
 * TopRated Fragment for showing movies
 * Swipe to Refresh
 */

public class TopRated extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public TopRated() {
    }

    SwipeRefreshLayout swipeRefreshLayout_TopRated;
    View view_topRated;
    RecyclerView lv_topRated;
    String URL_TR = "http://api.themoviedb.org/3/movie/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";

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
        view_topRated = inflater.inflate(R.layout.fragment_top_rated, container, false);
        lv_topRated = view_topRated.findViewById(R.id.recyclerView_topRated);
        if (checkConnection()) {
            loadTopRatedData();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
        }
        lv_topRated.smoothScrollToPosition(0);
        swipeRefreshLayout_TopRated = view_topRated.findViewById(R.id.swipeRefreshLayout_TopRated);
        swipeRefreshLayout_TopRated.setOnRefreshListener(this);
        return view_topRated;
    }

    //Network-Connection Check
    private boolean checkConnection() {
        return Receiver.isConnected();
    }

    //Load JSONData
    private void loadTopRatedData() {
        JSONDownload JSONDownload = new JSONDownload(getActivity().getApplication().getApplicationContext(), lv_topRated);
        JSONDownload.getJSON(URL_TR);
    }

    //On SWIPE-to-REFRESH
    @Override
    public void onRefresh() {
        swipeRefreshLayout_TopRated.setRefreshing(true);
        Log.d("Swipe", "Refreshing TopRated");
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout_TopRated.setRefreshing(false);
                if (checkConnection()) {
                    loadTopRatedData();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }
}


