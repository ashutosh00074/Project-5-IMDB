package com.tech.gigabyte.imdb;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 * JSON for Rating
 */

class JSONPost {
    private float new_rating;
    private int user_count;

    JSONPost(float rating, int user_count) {
        this.new_rating = rating;
        this.user_count = user_count;
    }

    void postJSON_toURL(String url) {

        try {//A URLConnection with support for HTTP-specific features.
            URL mUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) mUrl.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.addRequestProperty("Accept", "application/json");
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod("POST");

            //An output stream accepts output bytes and sends them to some sink.
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("vote_average", new_rating);
            jsonParam.put("vote_count", user_count);

            //Writes text to a character-output stream,
            bufferedWriter.write(jsonParam.toString());
            bufferedWriter.close();

            //Reads text from a character-input stream,
            BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = serverAnswer.readLine()) != null) {

                System.out.println("LINE: " + line); //<--If any response from server
            }

            os.close();
            serverAnswer.close();

        } catch (Exception e) {
            Log.e("Oops", "Something went wrong");
        }
    }
}
