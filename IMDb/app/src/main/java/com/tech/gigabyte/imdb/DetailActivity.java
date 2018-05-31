package com.tech.gigabyte.imdb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 * DETAILS of Selected Movie -
 * MOVIE - "NAME","STATUS","Releasing-DATE","BUDGET-REVENUE","OVERVIEW","TRAILER","POSTERS","ACTORS&CREATORS"
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Menu collapsedMenu;
    private boolean appBarExpanded = true;

    TextView votes, crew_label, title, desc, date, budget, revenue,
            status, vote_avg, vote_count, overview, pos_label, trail_label, cast_label;

    ImageView img_moviePoster, img_fav, img_watchlist;
    RatingBar rating;
    HorizontalScrollView HSV_posters, HSV_trail, HSV_cast, HSV_crew;
    LinearLayout linLay_pos, linLay_trail, linLay_cast, linLay_crew;

    String URL_mDetails, URL_mPosters, URL_mTrail, URL_mCastCrew;
    String id, imdb_id;

    ImageLoader imageLoader = ImageLoader.getInstance();
    Model IMDb = new Model();
    Database database = new Database(DetailActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the activity content from a layout resource.
        setContentView(R.layout.activity_detail);

        //A standard toolbar for use within application content.
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        // Wrapper for Toolbar which implements a collapsing app bar.
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.app_name));

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });

        date = (TextView) findViewById(R.id.textView_date);
        title = (TextView) findViewById(R.id.textView_title);
        votes = (TextView) findViewById(R.id.imageView_votes);
        budget = (TextView) findViewById(R.id.textView_budget);
        revenue = (TextView) findViewById(R.id.textView_revenue);
        desc = (TextView) findViewById(R.id.textView_description);
        overview = (TextView) findViewById(R.id.textView_overview);
        status = (TextView) findViewById(R.id.textView_releaseStatus);
        vote_avg = (TextView) findViewById(R.id.textView_vote_average);
        vote_count = (TextView) findViewById(R.id.textView_vote_count);
        cast_label = (TextView) findViewById(R.id.textView_cast_label);
        crew_label = (TextView) findViewById(R.id.textView_crew_label);
        pos_label = (TextView) findViewById(R.id.textView_posters_label);
        trail_label = (TextView) findViewById(R.id.textView_trailers_label);

        img_fav = (ImageView) findViewById(R.id.imageView_favorites);
        img_watchlist = (ImageView) findViewById(R.id.imageView_watchlist);
        img_moviePoster = (ImageView) findViewById(R.id.imageView_moviePoster);

        rating = (RatingBar) findViewById(R.id.ratingBar);

        HSV_cast = (HorizontalScrollView) findViewById(R.id.horizontalScrollView_cast);
        HSV_crew = (HorizontalScrollView) findViewById(R.id.horizontalScrollView_crew);
        HSV_trail = (HorizontalScrollView) findViewById(R.id.horizontalScrollView_trailers);
        HSV_posters = (HorizontalScrollView) findViewById(R.id.horizontalScrollView_posters);

        linLay_cast = (LinearLayout) findViewById(R.id.linearLayout_cast);
        linLay_crew = (LinearLayout) findViewById(R.id.linearLayout_crew);
        linLay_pos = (LinearLayout) findViewById(R.id.linearLayout_posters);
        linLay_trail = (LinearLayout) findViewById(R.id.linearLayout_trailers);



        img_fav.setOnClickListener(this);
        img_watchlist.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.e("id string", id);
        IMDb = database.get_entry(parseInt(id));
        if (IMDb != null) {
            Log.e("ID", String.valueOf(IMDb.getMovie_id()));
            Log.e("is_favorite", String.valueOf(IMDb.getIs_favorite()));
            Log.e("is_watchlist", String.valueOf(IMDb.getIs_watchlist()));
        } else {
            Log.e("Imdb: ", "is null");
        }

        // JSON - MOVIE- Details, Posters, Trailer, Cast & Crew
        URL_mDetails = "http://api.themoviedb.org/3/movie/" + id + "?api_key=8496be0b2149805afa458ab8ec27560c";
        new JSON_movieDetails().execute(); //Executes the task with the specified parameters
        URL_mPosters = "http://api.themoviedb.org/3/movie/" + id + "/images?api_key=8496be0b2149805afa458ab8ec27560c";
        new JSON_moviePosters().execute(); //Executes the task with the specified parameters
        URL_mTrail = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=8496be0b2149805afa458ab8ec27560c";
        new JSON_movieTrailers().execute();
        new JSON_movieCast().execute();
        new JSON_movieCrew().execute();
    }

    // Saving Choice of FAVORITES , Adding to FAVORITES
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageView_favorites) {
            IMDb = database.get_entry(parseInt(id));
            if (database.MovieIDExist(parseInt(id))) {
                if (IMDb.getIs_favorite() == 0) {
                    Log.e("fav added id", id);
                    database.update_favorite(parseInt(id), 1);
                    img_fav.setImageResource(R.drawable.favourite_clicked);
                    Toast.makeText(this, "Successfully added in Favorites", Toast.LENGTH_SHORT).show();
                } else if (IMDb.getIs_favorite() == 1) {
                    database.update_favorite(parseInt(id), 0);
                    img_fav.setImageResource(R.drawable.favourite_unclicked);
                    Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }
            } else {
                database.add_new_entry(parseInt(id), 1, 0);
                img_fav.setImageResource(R.drawable.favourite_clicked);
            }

            // Saving Choice of Watchlist - Adding to WATCHLIST
        } else if (v.getId() == R.id.imageView_watchlist) {
            IMDb = database.get_entry(parseInt(id));
            if (database.MovieIDExist(parseInt(id))) {
                if (IMDb.getIs_watchlist() == 0) {
                    database.update_watchlist(parseInt(id), 1);
                    img_watchlist.setImageResource(R.drawable.watchlist_clicked);
                    Toast.makeText(this, "Successfully added in Watchlist", Toast.LENGTH_SHORT).show();
                } else if (IMDb.getIs_watchlist() == 1) {
                    database.update_watchlist(parseInt(id), 0);
                    img_watchlist.setImageResource(R.drawable.watchlist_unclicked);
                    Toast.makeText(this, "Removed from Watchlist", Toast.LENGTH_SHORT).show();
                }
            } else {
                database.add_new_entry(parseInt(id), 0, 1);
                img_watchlist.setImageResource(R.drawable.watchlist_clicked);
            }

        }
    }

    //AsyncTask enables proper and easy use of the UI thread. This class allows to perform
    // background operations and publish results on the UI thread without having to manipulate threads and/or handlers.
    private class JSON_movieDetails extends AsyncTask<Void, Void, Void> {
        Float new_vote_average;
        int new_vote_count;
        String str_movieImage, str_title, str_description, str_date, str_budget, str_revenue, str_status,
                str_vote_average, str_vote_count, str_overview, users = " users";

        //Runs on the UI thread before doInBackground(Params...).
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //Perform a computation on a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            //A modifiable set of name/value mappings.
            JSONObject jsonObject;
            JSONdata jsondata_movieDetails = new JSONdata();
            jsonObject = jsondata_movieDetails.getJSONFromURL(URL_mDetails);
            try {
                str_movieImage = "http://image.tmdb.org/t/p/original" + jsonObject.getString("poster_path");
                str_title = jsonObject.getString("title");
                str_description = jsonObject.getString("tagline");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (jsonObject.has("budget") && !Objects.equals(jsonObject.getString("budget"), "0")) {
                        str_budget = getResources().getString(R.string.budget) + " $ " +
                                jsonObject.getString("budget");
                    } else {
                        str_budget = getResources().getString(R.string.budget) + " NA";
                    }
                }

                str_date = jsonObject.getString("release_date");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (jsonObject.has("revenue") && !Objects.equals(jsonObject.getString("revenue"), "0")) {
                        str_revenue = getResources().getString(R.string.revenue) + " $ " +
                                jsonObject.getString("revenue");
                    } else {
                        str_revenue = getResources().getString(R.string.revenue) + " NA";
                    }
                }

                str_status = getResources().getString(R.string.status) + " " +
                        jsonObject.getString("status");
                str_vote_average = jsonObject.getString("vote_average");
                str_vote_count = jsonObject.getString("vote_count");
                str_overview = jsonObject.getString("overview");
                imdb_id = jsonObject.getString("imdb_id");
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        //Runs on the UI thread after doInBackground(Params...).
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            title.setText(str_title);
            imageLoader.displayImage(str_movieImage, img_moviePoster);
            title.setText(str_title);
            overview.setText(str_overview);
            desc.setText(str_description);
            date.setText(format_date(str_date));
            budget.setText(str_budget);
            revenue.setText(str_revenue);
            status.setText(str_status);
            vote_avg.setText("(" + str_vote_average + "/10) ");
            vote_count.setText(str_vote_count);
            vote_count.append(users);
            rating.setRating(Float.parseFloat(str_vote_average) / 2);

            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    new_vote_average = v + (Float.parseFloat(str_vote_average)) / 2;
                    new_vote_count = parseInt(str_vote_count) + 1;
                    BigDecimal result_vote_avg;
                    result_vote_avg = round(new_vote_average, 1);
                    Log.e("Det act Rating passed ", String.valueOf(v));
                    SessionID sessionID = new SessionID(id, result_vote_avg, new_vote_count);
                    sessionID.execute();

                    vote_avg.setText("(" + result_vote_avg + "/10) ");
                    vote_count.setText(String.valueOf(new_vote_count));
                    vote_count.append(users);
                }
            });

            if (IMDb != null) {
                if (IMDb.getIs_favorite() == 1) {
                    img_fav.setImageResource(R.drawable.favourite_clicked);
                } else if (IMDb.getIs_favorite() == 0) {
                    img_fav.setImageResource(R.drawable.favourite_unclicked);
                }

                if (IMDb.getIs_watchlist() == 1) {
                    img_watchlist.setImageResource(R.drawable.watchlist_clicked);
                } else if (IMDb.getIs_watchlist() == 0) {
                    img_watchlist.setImageResource(R.drawable.watchlist_unclicked);
                }
            } else {
                img_fav.setImageResource(R.drawable.favourite_unclicked);
                img_watchlist.setImageResource(R.drawable.watchlist_unclicked);
            }
        }
    }

    //rounding mode.
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private class JSON_moviePosters extends AsyncTask<Void, Void, Void> {
        String str_moviePoster = "";
        ArrayList<String> arrayList_moviePoster = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray;
            JSONObject jsonObject;
            JSONdata jsondata_moviePosters = new JSONdata();
            jsonObject = jsondata_moviePosters.getJSONFromURL(URL_mPosters);
            try {
                jsonArray = jsonObject.getJSONArray("posters");
                arrayList_moviePoster = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    str_moviePoster = "http://image.tmdb.org/t/p/w500" + jsonObject.getString("file_path");
                    arrayList_moviePoster.add(str_moviePoster);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < arrayList_moviePoster.size(); i++) {
                ImageView imageView_posters = new ImageView(getApplicationContext());
                imageView_posters.setId(i);
                imageView_posters.setAdjustViewBounds(true);
                imageView_posters.setPadding(1, 1, 1, 1);
                imageLoader.displayImage(arrayList_moviePoster.get(i), imageView_posters);
                linLay_pos.addView(imageView_posters);
            }
        }
    }

    private class JSON_movieTrailers extends AsyncTask<Void, Void, Void> {
        String str_movieTrailer = "";
        ArrayList<HashMap<String, String>> arrayList_movieTrailer = null;
        HashMap<String, String> hashMap_movieTrailer;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONArray jsonArray;
            JSONObject jsonObject;
            JSONdata jsondata_moviePosters = new JSONdata();
            jsonObject = jsondata_moviePosters.getJSONFromURL(URL_mTrail);
            try {
                jsonArray = jsonObject.getJSONArray("results");
                arrayList_movieTrailer = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    hashMap_movieTrailer = new HashMap<>();
                    jsonObject = jsonArray.getJSONObject(i);
                    str_movieTrailer = "https://www.youtube.com/watch?v=" + jsonObject.getString("key");
                    hashMap_movieTrailer.put("name", jsonObject.getString("name"));
                    hashMap_movieTrailer.put("link", str_movieTrailer);
                    arrayList_movieTrailer.add(hashMap_movieTrailer);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < arrayList_movieTrailer.size(); i++) {
                hashMap_movieTrailer = arrayList_movieTrailer.get(i);
                TextView textView_trailers = new TextView(getApplicationContext());
                textView_trailers.setId(i);
                textView_trailers.setTextColor(Color.BLUE);
                textView_trailers.setTextSize(15);
                textView_trailers.setPadding(10, 0, 10, 0);
                textView_trailers.setClickable(true);

                textView_trailers.setText(hashMap_movieTrailer.get("name"));
                textView_trailers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri trailer_web_address = Uri.parse(hashMap_movieTrailer.get("link"));
                        Intent browser = new Intent(Intent.ACTION_VIEW, trailer_web_address);
                        startActivity(browser);
                    }
                });

                TextView textView_trailers_image = new TextView(getApplicationContext());
                textView_trailers_image.setTextColor(Color.RED);
                textView_trailers_image.setTextSize(20);
                textView_trailers_image.setPadding(10, 0, 10, 0);
                textView_trailers_image.setText("*");
                linLay_trail.addView(textView_trailers);
                linLay_trail.addView(textView_trailers_image);
            }
        }
    }

    private class JSON_movieCast extends AsyncTask<Void, Void, Void> {
        String str_movieCast = "";
        ArrayList<HashMap<String, String>> arrayList_movieCast = null;
        HashMap<String, String> hashMap_movieCast;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL_mCastCrew = "http://api.themoviedb.org/3/movie/" + imdb_id + "/credits?api_key=8496be0b2149805afa458ab8ec27560c";
            JSONArray jsonArray;
            JSONObject jsonObject;
            JSONdata jsondata_movieCast = new JSONdata();
            jsonObject = jsondata_movieCast.getJSONFromURL(URL_mCastCrew);
            try {
                jsonArray = jsonObject.getJSONArray("cast");
                arrayList_movieCast = new ArrayList<>();
                Log.e("jsonArray Length", String.valueOf(jsonArray.length()));
                for (int i = 0; i < jsonArray.length(); i++) {
                    hashMap_movieCast = new HashMap<>();
                    jsonObject = jsonArray.getJSONObject(i);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(jsonObject.getString("profile_path"), "null") || jsonObject.getString("profile_path").length() == 0) {
                            str_movieCast = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYCcMi07il3EcGH5UCaaGe5eourhr7TBvOJK0FqK2ToHkEcjNy1_PbSprA";
                        } else {
                            str_movieCast = "http://image.tmdb.org/t/p/w500" + jsonObject.getString("profile_path");
                        }
                    }
                    hashMap_movieCast.put("cast", str_movieCast);
                    hashMap_movieCast.put("name", jsonObject.getString("name"));
                    hashMap_movieCast.put("character", jsonObject.getString("character"));
                    arrayList_movieCast.add(hashMap_movieCast);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < arrayList_movieCast.size(); i++) {
                hashMap_movieCast = arrayList_movieCast.get(i);

                LinearLayout cast_crew_list = (LinearLayout) layoutInflater.inflate(R.layout.cast_crew_list, null);
                ImageView imageView_cast_crew = cast_crew_list.findViewById(R.id.imageView_cast_crew);
                TextView textView_name = cast_crew_list.findViewById(R.id.textView_name);
                TextView textView_role = cast_crew_list.findViewById(R.id.textView_role);
                imageView_cast_crew.setAdjustViewBounds(true);
                imageLoader.displayImage(hashMap_movieCast.get("cast"), imageView_cast_crew);
                textView_name.setText(hashMap_movieCast.get("name"));
                textView_role.setText(hashMap_movieCast.get("character"));
                linLay_cast.addView(cast_crew_list);
            }
        }
    }

    private class JSON_movieCrew extends AsyncTask<Void, Void, Void> {
        String str_movieCrew = "";
        ArrayList<HashMap<String, String>> arrayList_movieCrew = null;
        HashMap<String, String> hashMap_movieCrew;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL_mCastCrew = "http://api.themoviedb.org/3/movie/" + imdb_id + "/credits?api_key=8496be0b2149805afa458ab8ec27560c";
            JSONArray jsonArray;
            JSONObject jsonObject;
            JSONdata jsondata_movieCrew = new JSONdata();
            jsonObject = jsondata_movieCrew.getJSONFromURL(URL_mCastCrew);
            try {
                jsonArray = jsonObject.getJSONArray("crew");
                arrayList_movieCrew = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    hashMap_movieCrew = new HashMap<>();
                    jsonObject = jsonArray.getJSONObject(i);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(jsonObject.getString("profile_path"), "null") || jsonObject.getString("profile_path").length() == 0) {
                            str_movieCrew = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYCcMi07il3EcGH5UCaaGe5eourhr7TBvOJK0FqK2ToHkEcjNy1_PbSprA";
                        } else {
                            str_movieCrew = "http://image.tmdb.org/t/p/w500" + jsonObject.getString("profile_path");
                        }
                    }
                    hashMap_movieCrew.put("crew", str_movieCrew);
                    hashMap_movieCrew.put("name", jsonObject.getString("name"));
                    hashMap_movieCrew.put("job", jsonObject.getString("job"));
                    arrayList_movieCrew.add(hashMap_movieCrew);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < arrayList_movieCrew.size(); i++) {
                hashMap_movieCrew = arrayList_movieCrew.get(i);

                LinearLayout cast_crew_list = (LinearLayout) layoutInflater.inflate(R.layout.cast_crew_list, null);
                ImageView imageView_cast_crew = cast_crew_list.findViewById(R.id.imageView_cast_crew);
                TextView textView_name = cast_crew_list.findViewById(R.id.textView_name);
                TextView textView_role = cast_crew_list.findViewById(R.id.textView_role);

                imageLoader.displayImage(hashMap_movieCrew.get("crew"), imageView_cast_crew);
                textView_name.setText(hashMap_movieCrew.get("name"));
                textView_role.setText(hashMap_movieCrew.get("job"));
                linLay_crew.addView(cast_crew_list);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (collapsedMenu != null
                && (!appBarExpanded || collapsedMenu.size() != 1)) {
            //collapsed
            collapsedMenu.add("Favorites")
                    .setIcon(R.drawable.ic_toolbar_favorites)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            //expanded
        }
        return super.onPrepareOptionsMenu(collapsedMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        collapsedMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.WatchList:
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.putExtra("viewpager_position", 4);
                startActivity(intent);
                return true;
        }
        if (item.getTitle() == "Favorites") {
            Intent i = new Intent(DetailActivity.this, MainActivity.class);
            i.putExtra("viewpager_position", 3);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private String format_date(String input_date) {
        if (input_date.isEmpty()) {
            return null;
        }
        String str_date_array[] = input_date.split("-");
        int get_year = parseInt(str_date_array[0]);
        int get_month = parseInt(str_date_array[1]);
        int get_day = parseInt(str_date_array[2]);
        Calendar calendar_temp = Calendar.getInstance();
        calendar_temp.set(get_year, get_month - 1, get_day);
        String format = "dd MMMM yyyy";
        SimpleDateFormat date_ui_format = new SimpleDateFormat(format, Locale.US);
        return (date_ui_format.format(calendar_temp.getTime()));
    }
}