package com.bustracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Premission {

    private static String [] premissions = {
            android.Manifest.permission.ACCESS_FINE_LOCATION ,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};

    public static boolean checkLocationPremission(Context context){

        return ActivityCompat.checkSelfPermission(
                context,premissions[0])
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                premissions[1])==PackageManager.PERMISSION_GRANTED;
    }


    public static void requestPremission(Context context){

    }
}
