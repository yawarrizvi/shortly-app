package com.shortly.shortlyapp.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by yarizvi on 08/06/2017.
 */

public class NetworkManager {
    /*
    * Check internet connection
    */
    public static boolean isConnected(Context context) {
        boolean isConnectedToMobileNetwork = isConnectedToMobileData(context);
        boolean isConnectedToAnyMobileData = isConnectedToAnyMobileData(context);
        boolean isConnectedToWIFI = isConnectedToWifi(context);

        boolean _isConnected = (isConnectedToMobileNetwork || isConnectedToWIFI);

        return _isConnected;
    }

    public static boolean isConnectedToWifi(Context context) {
        boolean _isConnectedToWifi = false;
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
                _isConnectedToWifi = true;
            }
        }
        return _isConnectedToWifi;
    }

    public static boolean isConnectedToMobileData(Context context) {
        boolean _isConnectedToMobileData = false;
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() != ConnectivityManager.TYPE_WIFI) {
            if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
                _isConnectedToMobileData = true;
            }
        }
        return _isConnectedToMobileData;
    }

    public static boolean isConnectedToAnyMobileData(Context context) {
        boolean _isConnectedToMobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo != null && networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        _isConnectedToMobileData = true;
                    }
                }
            }
        } else {
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo networkInfo : info) {
                        if (networkInfo != null && networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
                            if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                                _isConnectedToMobileData = true;
                            }
                        }
                    }
                }
            }
        }
        return _isConnectedToMobileData;
    }
}
