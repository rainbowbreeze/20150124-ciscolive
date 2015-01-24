package it.rainbowbreeze.libs.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by alfredomorresi on 16/11/14.
 */
public class NetworkUtils {

    /**
     * Checks if a internet connection is available. Note that the device can be connected
     * to a network but not have internet access
     *
     * Requires <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * in the manifest
     *
     * @param appContext
     * @return
     */
    public static boolean isNetworkAvailable(Context appContext) {
        /**
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
         */
        boolean outcome = false;

        if (appContext != null) {
            ConnectivityManager cm = (ConnectivityManager) appContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            for (NetworkInfo tempNetworkInfo : networkInfos) {
                 // Can also check if the user is in roaming
                if (tempNetworkInfo.isConnected()) {
                    outcome = true;
                    break;
                }
            }
        }

        return outcome;
    }
}
