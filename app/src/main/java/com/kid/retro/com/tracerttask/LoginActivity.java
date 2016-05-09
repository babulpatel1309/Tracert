package com.kid.retro.com.tracerttask;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.viewpagerindicator.CirclePageIndicator;
import com.vistrav.ask.Ask;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText edtEmail;
    private EditText edtPassword;

    private RelativeLayout mBtnLogin;
    private TextView mTxtSignUp;
    private String result;
    private ProgressDialog progress;
    private boolean ack;
    private String log_id;
    private String email_id;
    private String otp;
    private String status;
    private String msg;

    private TextView txtForgot;
    private String imagePath;

    ImageView sliding_image, eye_icon;
    Animation anim, anim_back;
    RelativeLayout middle_Lay;
    LinearLayout bottom_lay;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f54a4a"));
        }

        setContentView(R.layout.activity_login1);
        anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_out_right);

        anim.setRepeatCount(-1);
        anim.setRepeatMode(2);

        sliding_image = (ImageView) findViewById(R.id.sliding_image);
        sliding_image.startAnimation(anim);
        bottom_lay = (LinearLayout) findViewById(R.id.bottom_lay);
        middle_Lay = (RelativeLayout) findViewById(R.id.middle_Lay);
        eye_icon = (ImageView) findViewById(R.id.eye_icon);

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);
        CirclePageIndicator titleIndicator = (CirclePageIndicator) findViewById(R.id.titles);

        titleIndicator.setViewPager(viewPager);
        titleIndicator.setCurrentItem(0);
        titleIndicator.setSnap(true);

        /*WebView browser = (WebView) findViewById(R.id.webview);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl("file:///android_asset/carousel/header1.html");*/
        findView();
        onClick();

        if (bottom_lay != null &&
                middle_Lay != null
                && eye_icon != null) {
            final PropertyAction title_lay = PropertyAction.newPropertyAction(bottom_lay).
                    translationY(1000).
                    alpha(0f).
                    duration(500).
                    build();

            final PropertyAction fabAction = PropertyAction.newPropertyAction(middle_Lay).
                    translationY(1000).
                    alpha(0f).
                    duration(500).
                    build();

            final PropertyAction eye = PropertyAction.newPropertyAction(eye_icon).
                    translationY(-1000).
                    alpha(0f).
                    duration(500).
                    build();


            Player.init()
                    .animate(fabAction)
                    .animate(title_lay)
                    .animate(eye)
                    .play();
        } else {
            findView();
            onClick();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Ask.on(LoginActivity.this)
                        .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION
                                , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.VIBRATE
                                , Manifest.permission.RECEIVE_BOOT_COMPLETED
                                , Manifest.permission.WAKE_LOCK
                                , Manifest.permission.READ_CONTACTS) //one or more permissions
                        .withRationales("Location permission need for map to work properly",
                                "In order to save file you will need to grant storage permission",
                                "For invitation we need a permission for contacts.") //optional
                        .when(new Ask.Permission() {
                            @Override
                            public void granted(List<String> permissions) {
                                Log.i(TAG, "granted :: " + permissions);
                                enableGps();
                            }

                            @Override
                            public void denied(List<String> permissions) {
                                Log.i(TAG, "denied :: " + permissions);
                                enableGps();
                            }
                        }).go();
            }
        }, 500);
    }

    Dialog alertDialog = null;

    private void enableGps() {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 1);
                }
            });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            finish();
                        }
                    }
            );
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            enableGps();
        } else {
            enableGps();
        }
    }

    private void onClick() {
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mTxtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();

            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtEmail.getText().toString().length() > 0) {

                    if (edtPassword.getText().toString().length() > 0) {

                        if (Common.isOnline(LoginActivity.this)) {

                            //mProgressDialog.show();
                            new Async_RegistrationTask().execute();

                        } else {
                            Common.displayToast(LoginActivity.this, "Please check internet connectivity");
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Please , insert Password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please , insert EmailId", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Login();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            if (progress.isShowing()) {
                progress.dismiss();
            }


            if (ack) {


                AppSettings.setisLoggedIn(LoginActivity.this, true);

                AppSettings.setEmail(LoginActivity.this, email_id);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Emaild id and password invalid", Toast.LENGTH_SHORT).show();

            }


        }
    }

    public void Login() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/login_api.php").
                    post(new FormEncodingBuilder().add("email_id", edtEmail.getText().toString()).
                            add("password", edtPassword.getText().toString()).add("gcm_id", token).build()).build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
            } else {
                result = response.body().string();

                AppSettings.email_id = edtEmail.getText().toString();
                AppSettings.password = edtPassword.getText().toString();
                AppSettings.gcm_id = token;

                AppSettings.setEmail(context,AppSettings.email_id);
                AppSettings.setPassword(context,AppSettings.password);
                AppSettings.setGcm_id(context,AppSettings.gcm_id);

                JSONObject mainObj = new JSONObject(result);

                if (mainObj.has("ack")) {
                    ack = mainObj.getBoolean("ack");
                }

                if (mainObj.has("msg")) {
                    msg = mainObj.getString("msg");
                }
                if (ack) {
                    if (mainObj.has("login_details")) {
                        JSONArray jsonArray = mainObj.getJSONArray("login_details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.has("log_id")) {
                                log_id = jsonObject.getString("log_id");
                            }

                            if (jsonObject.has("email_id")) {
                                email_id = jsonObject.getString("email_id");
                                AppSettings.setEmail(LoginActivity.this, email_id);
                            }

                            if (jsonObject.has("img_path")) {
                                imagePath = jsonObject.getString("img_path");

                                AppSettings.setProfileIMG(LoginActivity.this, imagePath);
                            }

                            if (jsonObject.has("otp")) {
                                otp = jsonObject.getString("otp");
                            }

                            if (jsonObject.has("status")) {
                                status = jsonObject.getString("status");
                            }

                            if (jsonObject.has("username")) {

                                AppSettings.setUserName(LoginActivity.this, jsonObject.getString("username"));
                            }
                            if (jsonObject.has("mobile_no")) {

                                AppSettings.setContactNumber(LoginActivity.this, jsonObject.getString("mobile_no"));
                            }

                        }


                    }
                } else {
                    if (mainObj.has("msg")) {

                        msg = mainObj.getString("msg");

                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findView() {

        edtEmail = (EditText) findViewById(R.id.edtEntEmail);
        edtPassword = (EditText) findViewById(R.id.edtEntPassword);
        mBtnLogin = (RelativeLayout) findViewById(R.id.relLoginBtn);
        mTxtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtForgot = (TextView) findViewById(R.id.txtForgot);
    }

    /*
    * Harsh Patel
    * This code is for Auto Sliding Panel ViewPager
    * With Images and Content.
    * */
    public static class SwipeFragment extends Fragment {
        public String[] TXT_IDS = {
                "1.Finding your loved once made easy with Trracert !!!",
                "2.Finding your loved once made easy with Trracert !!!",
                "3.Finding your loved once made easy with Trracert !!!",
                "4.Finding your loved once made easy with Trracert !!!"
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);

            Bundle bundle = getArguments();
            int position = bundle.getInt("position");

            TextView txtCarousel = (TextView) swipeView.findViewById(R.id.txtCarousel);
            txtCarousel.setText(TXT_IDS[position]);

            return swipeView;
        }

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }

    static final int NUM_ITEMS = 4;

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }

}