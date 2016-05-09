package com.kid.retro.com.tracerttask.chat;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kid.retro.com.tracerttask.R;

import java.io.IOException;


public class MainActivity_Chat extends AppCompatActivity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "681732706172";


    static final String TAG = "L2C";

    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    String regid;

    public static Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        prefs = getSharedPreferences("Chat", 0);
        context = getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Friends List");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        mActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
//        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //What to do on back clicked
//            }
//        });

        Fragment user = new UserFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, user);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

//        if(!prefs.getString("REG_FROM","").isEmpty()){
//            Fragment user = new UserFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, user);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(null);
//            ft.commit();
//        }else  if(!prefs.getString("REG_ID", "").isEmpty()){
//            Fragment reg = new LoginFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, reg);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(null);
//            ft.commit();
//
//        }else if(checkPlayServices()){
//
//            new Register().execute();
//
//        }else{
//            Toast.makeText(getApplicationContext(),"This device is not supported",Toast.LENGTH_SHORT).show();
//        }
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle("Friends List");
    }

    private class Register extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(SENDER_ID);
                    Log.e("RegId",regid);

                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_ID", regid);
                    edit.commit();

                }

                return  regid;

            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
                return "Fails";

            }
        }
        @Override
        protected void onPostExecute(String json) {
            Fragment reg = new LoginFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, reg);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
