package com.tech.gigabyte.imdb;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 * Using TabLayout for Showing 'Now Playing','Upcoming','Top Rated'
 */

public class MainActivity extends AppCompatActivity implements Receiver.ConnectivityReceiverListener {
    ViewPager mViewPager;
    static String MOVIE_ID = "id";
    static String MOVIE_TITLE = "original_title";
    static String RELEASE_DATE = "release_date";
    static String VOTE_AVERAGE = "vote_average";
    static String MOVIE_IMAGE = "poster_path";
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int position = 0;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            position = extras.getInt("viewpager_position");}

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_empty_image)
                .showImageOnFail(R.drawable.ic_error_red)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        //Presents configuration for ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        //A standard toolbar for use within application content.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.np));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.rat));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.up));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.fav));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.wl));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(pageAdapter);
        pageAdapter.notifyDataSetChanged();
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(position);

        //Check Internet Connection and set SnackBar
        checkConnection();

        //Set TabLayout with view pager
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(getApplicationContext(), R.color.tab_unselected), ContextCompat.getColor(getApplicationContext(), R.color.tab_selected));
        tabLayout.setupWithViewPager(mViewPager);
    }

    // Method to check Internet connection status
    private void checkConnection() {
        boolean isConnected = Receiver.isConnected();
        showSnack(isConnected);
    }
    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Internet Connection : Active";
            color = Color.WHITE;
        } else {
            message = "Internet connection : Disconnected";
            color = Color.RED;
        }

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.snackbar_main), message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    //Callback will be triggered when there is change in network connection
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IMDb.getInstance().setConnectivityListener(this);
    }
}


