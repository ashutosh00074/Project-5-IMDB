package com.tech.gigabyte.imdb;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 *
 */

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


class JSONDownload {

    private Context context;
    private RecyclerView currentTabView;
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> arrayMovieList = new ArrayList<>();
    ImageLoaderConfiguration config;

    JSONDownload(Context context, RecyclerView view) {
        this.context = context;
        this.currentTabView = view;
    }

    void getJSON(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        if (result == null || result.length() == 0) {
                            Log.e("Json parsed is :", "null");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                jsonArray = jsonObject.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    jsonObject = jsonArray.getJSONObject(i);
                                    if (jsonObject.has("id")) {
                                        hashMap.put("id", jsonObject.getString("id"));
                                    } else {
                                        hashMap.put("id", "id NA");
                                    }
                                    if (jsonObject.has("original_title")) {
                                        hashMap.put("original_title", jsonObject.getString("original_title"));
                                    } else {
                                        hashMap.put("original_title", "Title NA");
                                    }
                                    if (jsonObject.has("release_date")) {
                                        hashMap.put("release_date", jsonObject.getString("release_date"));
                                    } else {
                                        hashMap.put("release_date", "Date NA");
                                    }

                                    if (jsonObject.has("popularity")) {
                                        hashMap.put("popularity", jsonObject.getString("popularity"));
                                    } else {
                                        hashMap.put("popularity", "Popularity NA");
                                    }

                                    if (jsonObject.has("vote_count")) {
                                        hashMap.put("vote_count", jsonObject.getString("vote_count"));
                                    } else {
                                        hashMap.put("vote_count", "Vote NA");
                                    }

                                    if (jsonObject.has("vote_average")) {
                                        hashMap.put("vote_average", jsonObject.getString("vote_average"));
                                    } else {
                                        hashMap.put("vote_average", "Average Vote NA");
                                    }

                                    if (jsonObject.has("poster_path")) {
                                        hashMap.put("poster_path", "http://image.tmdb.org/t/p/original" +
                                                jsonObject.getString("poster_path"));
                                    } else {
                                        hashMap.put("poster_path", "Poster NA");
                                    }
                                    arrayMovieList.add(hashMap);
                                }
                                MovieAdapter mAdapter = new MovieAdapter(context, arrayMovieList);
                                currentTabView.setAdapter(mAdapter);
                                currentTabView.setLayoutManager(new GridLayoutManager(context,2));
                                currentTabView.smoothScrollToPosition(0);
                            } catch (JSONException e) {
                                Log.e("Error", e.getMessage());
                            }
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}