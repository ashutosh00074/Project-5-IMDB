package com.tech.gigabyte.imdb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tech.gigabyte.imdb.MainActivity.MOVIE_ID;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 * RecyclerView Adapter - Movies Adapter
 * Movies List Items
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> MovieList;
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    MovieAdapter(Context context, ArrayList<HashMap<String, String>> MovieList) {
        inflater = LayoutInflater.from(context);// Instantiates a layout XML file into its corresponding View objects.
        this.MovieList = MovieList;
        this.context = context;
    }

    //ViewHolder describes an item view and metadata about its place within the RecyclerView.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = R.layout.list_item;
        View view_movieList = inflater.inflate(resource, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view_movieList);
        view_movieList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMapIntent;
                hashMapIntent = MovieList.get(viewHolder.getAdapterPosition());
                Log.e("before intent id ", hashMapIntent.get(MOVIE_ID));
                Log.e("before intent position ", String.valueOf(viewHolder.getAdapterPosition()));
                Intent intent = new Intent(context, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", hashMapIntent.get(MOVIE_ID));
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    //Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> currentMovie = MovieList.get(position);

        //Singletone for image loading and displaying at ImageViews
        imageLoader.displayImage(currentMovie.get(MainActivity.MOVIE_IMAGE), holder.mImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.pBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.pBar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.pBar.setVisibility(View.GONE);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.pBar.setVisibility(View.GONE);
            }
        });
        holder.mTitle.setText(currentMovie.get(MainActivity.MOVIE_TITLE));
        holder.rDate.setText(currentMovie.get(MainActivity.RELEASE_DATE));
        holder.ratingBar.setRating(Float.parseFloat(currentMovie.get(MainActivity.VOTE_AVERAGE)) / 2);
        holder.vCount.setText(currentMovie.get(MainActivity.VOTE_AVERAGE));
    }

    //Returns the total number of items in the data set held by the adapter.
    @Override
    public int getItemCount() {
        return MovieList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        RatingBar ratingBar;
        ProgressBar pBar;
        TextView mTitle;
        TextView rDate;
        TextView vCount;

        ViewHolder(final View itemView) {
            super(itemView);
            pBar = itemView.findViewById(R.id.progressbar);
            mImage = itemView.findViewById(R.id.imageView_mImage);
            mTitle = itemView.findViewById(R.id.textView_movieTitle);
            rDate = itemView.findViewById(R.id.textView_releaseDate);
            ratingBar = itemView.findViewById(R.id.ratingBar_popularity);
            vCount = itemView.findViewById(R.id.textView_voteCount);
        }
    }
}