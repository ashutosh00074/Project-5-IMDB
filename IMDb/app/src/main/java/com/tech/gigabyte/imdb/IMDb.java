package com.tech.gigabyte.imdb;

/*
 * Created by ASHUTOSH on 18-JULY-2017.
 *
 */

import android.app.Application;

public class IMDb extends Application {

    private static IMDb mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized IMDb getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(Receiver.ConnectivityReceiverListener listener) {
        Receiver.connectivityReceiverListener = listener;
    }
}

