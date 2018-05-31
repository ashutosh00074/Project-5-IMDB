package com.tech.gigabyte.imdb;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 *
 */


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

class JSONFavorites extends AsyncTask<Object, Object, ArrayList<HashMap<String, String>>> {

    private Context context;
    private Database database;
    private RecyclerView currentView;
    private int mode = 0;

    JSONFavorites(Context applicationContext, RecyclerView view, int mode) {
        this.context = applicationContext;
        this.currentView = view;
        this.mode = mode;
        database = new Database(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(Object... params) {
        ArrayList<Model> arrayList_imdb = new ArrayList<>();
        arrayList_imdb.clear();

        if (mode == 1) {
            Log.e("DJSON get_fav", "Started");
            arrayList_imdb = database.get_favorite_entries();
            Log.e("Length of arraylist", String.valueOf(arrayList_imdb.size()));
        } else if (mode == 2) {
            arrayList_imdb = database.get_watchlist_entries();
        }

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        JSONdata jsoNdata = new JSONdata();

        try {
            for (int i = 0; i < arrayList_imdb.size(); i++) {
                String url_fav = "http://api.themoviedb.org/3/movie/" + arrayList_imdb.get(i).getMovie_id()
                        + "?api_key=8496be0b2149805afa458ab8ec27560c";
                JSONObject jsonObject = jsoNdata.getJSONFromURL(url_fav);
                if (jsonObject != null && jsonObject.length() != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("id", jsonObject.getString("id"));
                    hashMap.put("original_title", jsonObject.getString("original_title"));
                    hashMap.put("release_date", jsonObject.getString("release_date"));
                    hashMap.put("popularity", jsonObject.getString("popularity"));
                    hashMap.put("vote_count", jsonObject.getString("vote_count"));
                    hashMap.put("vote_average", jsonObject.getString("vote_average"));
                    hashMap.put("poster_path", "http://image.tmdb.org/t/p/original" +
                            jsonObject.getString("poster_path"));
                    arrayList.add(hashMap);
                    Log.e("Arraylist fav: size", String.valueOf(arrayList.size()));
                } else {
                    Toast.makeText(context, "Please check Internet connection", Toast.LENGTH_LONG).show();
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        Log.e("Arraylist fav", arrayList.toString());
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
        super.onPostExecute(result);
        MovieAdapter mAdapter = new MovieAdapter(context, result);
        currentView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        currentView.setLayoutManager(new GridLayoutManager(context,2));
        currentView.smoothScrollToPosition(0);
    }
}
