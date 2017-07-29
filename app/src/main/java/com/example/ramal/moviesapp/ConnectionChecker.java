package com.example.ramal.moviesapp;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ramal on 7/29/2017.
 */

public class ConnectionChecker {
    Context context;
    public ConnectionChecker(Context context){
            this.context=context;
    }
    public boolean isConnected()
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if(connectivity !=null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null)
            {
                if(info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }
}

