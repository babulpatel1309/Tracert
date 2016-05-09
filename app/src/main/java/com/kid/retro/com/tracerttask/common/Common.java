package com.kid.retro.com.tracerttask.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by root on 11/12/15.
 */
public class Common {


    public static boolean isFrom = false;
    public static String latitude;
    public static String longitude;
    public static int id = 0;
    //public static Bitmap bitmapCommon=null;

    public static Void displayToast(Context context, String strToast) {
        Toast.makeText(context, strToast, Toast.LENGTH_SHORT).show();
        return null;
    }

    public static Void displayLog(String strTitle, String strText) {
        Log.d(strTitle, strText);
        return null;
    }

    public static boolean isOnline(Context c) {
        try {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
