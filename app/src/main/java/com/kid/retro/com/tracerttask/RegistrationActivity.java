package com.kid.retro.com.tracerttask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static String MyPREFERANCE = "MyPrefs";
    private SharedPreferences sharedpreferences = null;
    private SharedPreferences.Editor editor;

    private EditText mUserName;
    private EditText mEmailId;
    private EditText mPassword;
    private EditText mFName;
    private EditText mLName;
    private EditText mConfirmPassword;
    private EditText mContactNumeber;
    private RelativeLayout mSignUpBtn;

    private TextView mTxtLogin;

    private ProgressDialog mProgressDialog;
    private String result;

    private String ack = "";

    private String username = "";
    private String phoneNumber = "";
    private String email = "";
    private String FName = "";
    private String LName = "";
    private String otp = "";
    private String mStrEmail;

    private String msg;

    ImageView sliding_image;
    Animation anim, anim_back;

    public static String name;
    public static String usernameMain;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f54a4a"));
        }

        setContentView(R.layout.activity_registration);

        anim = AnimationUtils.loadAnimation(RegistrationActivity.this, R.anim.slide_out_right);

        anim.setRepeatCount(-1);
        anim.setRepeatMode(2);

        sliding_image = (ImageView) findViewById(R.id.sliding_image);
        sliding_image.startAnimation(anim);

        /*WebView browser = (WebView) findViewById(R.id.webview);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl("file:///android_asset/carousel/header.html");*/

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);

        findView();

    }


    private void findView() {

        mUserName = (EditText) findViewById(R.id.edtUserName);
        mEmailId = (EditText) findViewById(R.id.edtEmail);
        mFName = (EditText) findViewById(R.id.edtFName);
        mLName = (EditText) findViewById(R.id.edtLName);
        mPassword = (EditText) findViewById(R.id.edtPassword);
        mConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        mContactNumeber = (EditText) findViewById(R.id.edtContact);
        mSignUpBtn = (RelativeLayout) findViewById(R.id.relSignUpBtn);

        mSignUpBtn.setOnClickListener(this);

        mTxtLogin = (TextView) findViewById(R.id.txtLogin);
        mTxtLogin.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERANCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.relSignUpBtn:

                name = mUserName.getText().toString();

                if (name.trim().length() > 0)
                    Toast.makeText(RegistrationActivity.this, "" + name, Toast.LENGTH_LONG).show();

                String cPass = mConfirmPassword.getText().toString();
                mStrEmail = mEmailId.getText().toString();

                if (mUserName.getText().toString().trim().length() < 1) {
                    mUserName.requestFocus();
                    mUserName.setError(Html.fromHtml("<font color='red'>Enter Username....</font>"));

                } else if (!mUserName.getText().toString().matches("[a-zA-Z]+")) {
                    mUserName.requestFocus();
                    mUserName.setError(Html.fromHtml("<font color='red'>Enter only characters</font>"));
                } else if (!mEmailId.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    mEmailId.requestFocus();
                    mEmailId.setError(Html.fromHtml("<font color='red'>Please enter correct E-mail id</font>"));
                } else if (mFName.getText().toString().trim().length() < 1) {
                    mFName.requestFocus();
                    mFName.setError(Html.fromHtml("<font color='red'>Enter FirstName</font>"));
                } else if (!mFName.getText().toString().matches("[a-zA-Z]+")) {
                    mFName.requestFocus();
                    mFName.setError(Html.fromHtml("<font color='red'>Enter only characters</font>"));
                } else if (mLName.getText().toString().trim().length() < 1) {
                    mLName.requestFocus();
                    mLName.setError(Html.fromHtml("<font color='red'>Enter LastName</font>"));
                } else if (!mLName.getText().toString().matches("[a-zA-Z]+")) {
                    mLName.requestFocus();
                    mLName.setError(Html.fromHtml("<font color='red'>Enter only characters</font>"));
                } else if (!mContactNumeber.getText().toString().matches("[0-9]+")) {
                    mContactNumeber.requestFocus();
                    mContactNumeber.setError(Html.fromHtml("<font color='red'>Enter only digits in mobile number field..</font>"));
                } else if (!mPassword.getText().toString().equals(cPass.toString())) {
                    mPassword.requestFocus();
                    mPassword.setError(Html.fromHtml("<font color='red'>Password and Confirm Password do not match</font>"));
                } else {
                    if (Common.isOnline(RegistrationActivity.this)) {

                        //mProgressDialog.show();
                        usernameMain = mUserName.getText().toString();

                        Intent intent = new Intent(RegistrationActivity.this, RegistrationUDetails.class);
                        intent.putExtra("username", mUserName.getText().toString());
                        intent.putExtra("password", mPassword.getText().toString());
                        intent.putExtra("email", mEmailId.getText().toString());
                        intent.putExtra("phonenumber", mContactNumeber.getText().toString());
                        intent.putExtra("firstname", mFName.getText().toString());
                        intent.putExtra("lastname", mLName.getText().toString());
                        startActivity(intent);
                      /*  new Async_RegistrationTask().execute();*/

                    } else {
                        Common.displayToast(RegistrationActivity.this, "Please check internet connectivity");
                    }
                }
                Log.e("Registration", editor.toString());
                break;

            case R.id.txtLogin:

                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;


        }
    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(RegistrationActivity.this);
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Register();
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

            if (ack.equals("true")) {

                Intent intent = new Intent(RegistrationActivity.this, RegiatrationVarification.class);
                intent.putExtra("email", mStrEmail);
                intent.putExtra("otp", otp);
                Log.d("OTP REG", "" + otp);
                startActivity(intent);
                finish();

            } else {
                Common.displayToast(RegistrationActivity.this, "Email id is already existing..!");
            }

        }
    }

    public void Register() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/registration_api.php").post(new FormEncodingBuilder().add("username", mUserName.getText().toString().trim()).add("password", mPassword.getText().toString()).add("email_id", mEmailId.getText().toString()).add("mobile_no", mContactNumeber.getText().toString()).build()).build();


            //  Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", "kishan@retroinfotech.com").add("latlong_types", "Y").add("on_date", "29-01-2016").add("on_time", "08:50:00 PM").add("latitude", "22.60000").add("longitude", "73.30000").build()).build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
            } else {
                result = response.body().string();

                JSONObject mainObj = new JSONObject(result);

                if (mainObj.has("ack")) {

                    ack = mainObj.getString("ack");

                }

                if (ack.equals("true")) {

                    if (mainObj.has("registration_details")) {

                        JSONArray jsonArray = mainObj.getJSONArray("registration_details");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.has("username")) {
                                username = jsonObject.getString("username");
                            }

                            if (jsonObject.has("email_id")) {
                                email = jsonObject.getString("email_id");
                            }

                            if (jsonObject.has("otp")) {
                                otp = jsonObject.getString("otp");
                            }

                            if (jsonObject.has("mobile_no")) {
                                phoneNumber = jsonObject.getString("mobile_no");
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
}