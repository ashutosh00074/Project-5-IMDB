package com.tech.gigabyte.imdb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 *
 */

class PageAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new NowPlaying();
            case 1:
                return new TopRated();
            case 2:
                return new Upcoming();
            case 3:
                return new Favorites();
            case 4:
                return new Watchlist();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Now Playing";
            case 1:
                return "Top Rated";
            case 2:
                return "Up Coming";
            case 3:
                return "Favorite";
            case 4:
                return "Watch List";
        }
        return null;
    }
}

