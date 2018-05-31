package com.tech.gigabyte.imdb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ASHUTOSH on 18-JULY-2017.
 * Connectivity Receiver
 */


public class Receiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public Receiver() {
        super();
    }

    @Override
    // BroadcastReceiver is receiving an Intent broadcast.
    public void onReceive(Context context, Intent arg1) {

        //State of network connectivity.
        //Indicates whether network connectivity exists or is in the process of being established.
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) IMDb.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
