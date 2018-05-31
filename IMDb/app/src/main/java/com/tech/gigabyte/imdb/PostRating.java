package com.tech.gigabyte.imdb;

import android.os.AsyncTask;
import android.util.Log;

import static android.R.attr.id;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 * Guest Rating
 */


class PostRating extends AsyncTask<Object, Object, String> {
    private String movie_id;
    private Float mrating;
    private String Guest_id;
    private int user_Count;

    PostRating(String movie_id, float rating, String guest_id, int user_count) {
        this.movie_id = movie_id;
        this.mrating = rating;
        this.Guest_id = guest_id;
        this.user_Count = user_count;
    }

    @Override
    protected String doInBackground(Object... strings) {
        String url_post = "http://api.themoviedb.org/3/movie/" + id + "/rating?api_key=8496be0b2149805afa458ab8ec27560c&guest_session_id=" + Guest_id;
        JSONPost JSON_post = new JSONPost(mrating, user_Count);
        Log.e("Post rating", mrating + ", " + user_Count);
        JSON_post.postJSON_toURL(url_post);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }
}
