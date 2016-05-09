package com.kid.retro.com.tracerttask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.kid.retro.com.tracerttask.common.DatabaseHelper;
import com.kid.retro.com.tracerttask.common.GPSTracker;


public class SplashActivity extends AppCompatActivity {

    private ImageView mImgSplash;
    private static String MyPREFERANCE = "MyPrefs";
    private SharedPreferences sharedpreferences;
    private String username;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;


    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1cbbb4"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#1cbbb4"));
        }

        actionBar.setBackgroundDrawable(colorDrawable);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f54a4a"));
        }

        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(MyPREFERANCE, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username", null);

        gpsTracker = new GPSTracker(SplashActivity.this);

        databaseHelper = new DatabaseHelper(getApplicationContext());


        try {

            databaseHelper.createDataBase();

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (Common.isOnline(SplashActivity.this)) {
//            initParse();
        } else {
            Common.displayToast(SplashActivity.this, "Please check internet connectivity");
        }

        init();
    }

    private void init() {

        mImgSplash = (ImageView) findViewById(R.id.imgSplashTracert);

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 1000) {
                        sleep(100);

                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    if (AppSettings.getisLoggedIn(SplashActivity.this)) {

                     //   if(AppSettings.getisVerified(SplashActivity.this)) {
                            startActivity(new Intent(SplashActivity.this,
                                    MainActivity.class));
                   /*     }else{
                            startActivity(new Intent(SplashActivity.this,
                                    RegiatrationVarification.class));
                        }*/
                    } else {
                        startActivity(new Intent(SplashActivity.this,
                                LoginActivity.class));
                    }
                    finish();
                }
            }
        };
        splashThread.start();
    }

//    private void initParse() {
//        try {
//            // Enable Local Datastore.
//            Parse.enableLocalDatastore(this);
//            // Add your initialization code here
//            Parse.initialize(this);
//            ParseInstallation.getCurrentInstallation().saveInBackground();
//            ParseUser.enableAutomaticUser();
//            ParseACL defaultACL = new ParseACL();
//            // Optionally enable public read access.
//            // defaultACL.setPublicReadAccess(true);
//            ParseACL.setDefaultACL(defaultACL, true);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
