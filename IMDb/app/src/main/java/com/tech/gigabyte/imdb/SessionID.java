package com.tech.gigabyte.imdb;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 * Session ID
 */


class SessionID extends AsyncTask<String, String, String> {
    private String guest_id, movie_id;
    private float rating;
    private int user_count;
    private BigDecimal vote;

    SessionID(String id, BigDecimal new_vote_average, int new_userCount) {
        this.vote = new_vote_average;
        this.movie_id = id;
        this.user_count = new_userCount;
    }

    @Override
    protected String doInBackground(String... strings) {
        rating = Float.valueOf(vote.toString());
        JSONdata jsoNdata = new JSONdata();
        String guest_session = "http://api.themoviedb.org/3/authentication/guest_session/new?api_key=8496be0b2149805afa458ab8ec27560c";
        JSONObject jsonObject_guest = jsoNdata.getJSONFromURL(guest_session);
        if (jsonObject_guest != null && jsonObject_guest.has("guest_session_id")) {
            try {
                guest_id = String.valueOf(jsonObject_guest.get("guest_session_id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return guest_id;
    }

    @Override
    protected void onPostExecute(String Guest_id) {
        super.onPostExecute(Guest_id);
        Log.e("session id_guest id ", Guest_id);
        Log.e("Things passed", movie_id + ", " + rating + ", " + Guest_id + ", " + user_count);
        PostRating post_rating = new PostRating(movie_id, rating, Guest_id, user_count);
        post_rating.execute();
    }
}
