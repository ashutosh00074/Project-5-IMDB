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
 * NOW Playing Fragment for showing movies
 * Swipe to Refresh
 */


public class NowPlaying extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public NowPlaying() {}

    SwipeRefreshLayout swipeRefreshLayout_NowPlaying;
    RecyclerView lv_nowPlaying;
    public View view_nowPlaying;
    MovieAdapter adapter;
    String URL_NP = "http://api.themoviedb.org/3/movie/now_playing?api_key=8496be0b2149805afa458ab8ec27560c";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        view_nowPlaying = inflater.inflate(R.layout.fragment_now_playing, container, false);
        lv_nowPlaying = view_nowPlaying.findViewById(R.id.recyclerView_nowPlaying);

        if (checkConnection()) {
            load_NowplayingData();

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
        }

        lv_nowPlaying.smoothScrollToPosition(0);
        lv_nowPlaying.setAdapter(adapter);
        swipeRefreshLayout_NowPlaying = view_nowPlaying.findViewById(R.id.swipeRefreshLayout_NowPlaying);
        swipeRefreshLayout_NowPlaying.setOnRefreshListener(this);
        return view_nowPlaying;
    }

    //Network-Connection Check
    private boolean checkConnection() {
        return Receiver.isConnected();
    }

    //Called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //Load JSONData
    private void load_NowplayingData() {
        JSONDownload JSONDownload = new JSONDownload(getActivity().getApplication().getApplicationContext(), lv_nowPlaying);
        JSONDownload.getJSON(URL_NP);
    }

    //On SWIPE-to-REFRESH
    @Override
    public void onRefresh() {
        swipeRefreshLayout_NowPlaying.setRefreshing(true);
        Log.d("Swipe", "Refreshing NowPlaying");
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout_NowPlaying.setRefreshing(false);
                if (checkConnection()) {
                    load_NowplayingData();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }
}






