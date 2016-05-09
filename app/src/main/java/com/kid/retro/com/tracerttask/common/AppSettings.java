

package com.kid.retro.com.tracerttask.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class AppSettings {

    public static SharedPreferences mPrefs;
    public static Editor prefsEditor;

    public static String email_id = "";
    public static String password = "";
    public static String gcm_id = "";
    public static String req_user_email = "";
    public static String req_user_phone = "";

    public static void setPref(Context context, String key, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static String getPref(Context context, String key) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(key, "");
    }

    public static void setisVerified(Context context, boolean value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean("setisVerified", value);
        prefsEditor.commit();
    }

    public static boolean getisVerified(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean("setisVerified", false);

    }

    public static void setisLoggedIn(Context context, boolean value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean("isloggedin", value);
        prefsEditor.commit();
    }

    public static boolean getisLoggedIn(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean("isloggedin", false);

    }

    public static void setEmail(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("Email", value);
        prefsEditor.commit();
    }

    public static void setPassword(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("Password", value);
        prefsEditor.commit();
    }

    public static String getPassword(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("Password", null);
    }

    public static void setGcm_id(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("GCMID", value);
        prefsEditor.commit();
    }

    public static String getGcm_id(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("GCMID", null);
    }


    public static String getEmail(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("Email", null);
    }

    public static void setKm(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("km", value);
        prefsEditor.commit();
    }

    public static String getKm(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("km", null);
    }

    public static void setUserName(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("UserName", value);
        prefsEditor.commit();
    }

    public static String getUserName(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("UserName", null);
    }

    public static void setProfileIMG(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("profileIMG", value);
        prefsEditor.commit();
    }

    public static String getProfileIMG(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("profileIMG", null);
    }

    public static void setContactNumber(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("contactno", value);
        prefsEditor.commit();
    }

    public static String getContactNumber(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("contactno", null);
    }

    public static void setNumberRequest(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("noRequest", value);
        prefsEditor.commit();
    }

    public static String getNumberRequest(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("requestno", null);
    }

    public static void setNumberFriends(Context context, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("noFriends", value);
        prefsEditor.commit();
    }

    public static String getNumberFriends(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("noFriends", null);
    }


    private void enableGps(final Context context) {

        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    }
            );
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

}