package com.kid.retro.com.tracerttask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.soundcloud.android.crop.Crop;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class RegistrationUDetails extends AppCompatActivity {

    static final int DATE_PICKER_ID = 1111;
    private static String MyPREFERANCE = "MyPrefs";
    byte[] byteArrayImage;
    String encodedImage;
    /* private EditText mEdtFName;
     private EditText mEdtLName;*/
    private String mDOB;
    private String mGender;
    private String mEmail;
    private String username;
    private String phonenumber;
    private String password;
    private String fName;
    private String lName;
    private String result;
    private ImageView imgCake;
    private TextView txtSetBDate;
    private RelativeLayout mRelSubmitUDetails;
    private boolean ack = false;
    private String dob;
    private SharedPreferences sharedpreferences = null;
    private SharedPreferences.Editor editor;
    private ProgressDialog progress;
    private String otp;
    private String phoneNumber;
    private String msg;
    private int year;
    private int month;
    private int day;
    private ImageView profilePic, imgSmall;
    private ImageView imgGenderFemale;
    private ImageView imgGenderMale;
    private TextView txtUDetail, txtUPName, txtHowOld;
    private String url;

    ImageView sliding_image;
    Animation anim, anim_back;

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            /*showDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/

            if (day > 10 && (month + 1) > 10) {
                txtSetBDate.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(" "));
            } else if (day < 10 && (month + 1) < 10) {
                txtSetBDate.setText(new StringBuilder().append("0").append(day).append("-").append("0").append(month + 1).append("-").append(year).append(" "));
            } else if (day < 10) {
                txtSetBDate.setText(new StringBuilder().append("0").append(day).append("-").append(month + 1).append("-").append(year).append(" "));
            } else if ((month + 1) < 10) {
                txtSetBDate.setText(new StringBuilder().append(day).append("-").append("0").append(month + 1).append("-").append(year).append(" "));
            }

        }
    };

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f54a4a"));
        }

        setContentView(R.layout.activity_registration2);

        anim = AnimationUtils.loadAnimation(RegistrationUDetails.this, R.anim.slide_out_right);

        anim.setRepeatCount(-1);
        anim.setRepeatMode(2);

        sliding_image = (ImageView) findViewById(R.id.sliding_image);
        sliding_image.startAnimation(anim);


        sharedpreferences = getSharedPreferences(MyPREFERANCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        profilePic = (ImageView) findViewById(R.id.profilePic);

        imgSmall = (ImageView) findViewById(R.id.imgSmall);

        txtUDetail = (TextView) findViewById(R.id.txtUDetail);
        txtUPName = (TextView) findViewById(R.id.txtUPName);
        txtHowOld = (TextView) findViewById(R.id.txtHowOld);

        imgCake = (ImageView) findViewById(R.id.imgCake);
        txtSetBDate = (TextView) findViewById(R.id.txtSetBDate);

        imgGenderFemale = (ImageView) findViewById(R.id.imgGenderFemale);
        imgGenderMale = (ImageView) findViewById(R.id.imgGenderMale);

        /*WebView browser = (WebView) findViewById(R.id.webview);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl("file:///android_asset/carousel/header.html");*/

        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        /*mEdtFName = (EditText) findViewById(R.id.edtFName);
        mEdtLName = (EditText) findViewById(R.id.edtLName);*/
        //mDOB = (EditText) findViewById(R.id.edtDOB);


        Intent intent = getIntent();

        if (intent != null) {
            mEmail = intent.getStringExtra("email");
            //mGender = intent.getStringExtra("gender");
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
            phonenumber = intent.getStringExtra("phonenumber");
            fName = intent.getStringExtra("firstname");
            lName = intent.getStringExtra("lastname");
        }

        txtUDetail.setText("Hello " + fName + " !!");

        txtUPName.setText(fName + " " + lName);

        txtHowOld.setText(fName + ", How old are You?");

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilePic.setImageDrawable(null);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Crop.REQUEST_PICK);
            }
        });

        imgSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilePic.setImageDrawable(null);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Crop.REQUEST_PICK);
            }
        });

        imgGenderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "female";
                imgGenderFemale.setBackgroundResource(R.drawable.girl_selected);
                imgGenderMale.setBackgroundResource(R.drawable.boy);
            }
        });

        imgGenderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "male";
                imgGenderMale.setBackgroundResource(R.drawable.boy_selected);
                imgGenderFemale.setBackgroundResource(R.drawable.girl);
            }
        });

        imgCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DATE_PICKER_ID);

            }
        });

        txtSetBDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DATE_PICKER_ID);

            }
        });

        mRelSubmitUDetails = (RelativeLayout) findViewById(R.id.relSubmitUDetails);
        mRelSubmitUDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*fName = mEdtFName.getText().toString();
                lName = mEdtLName.getText().toString();*/

                mDOB = txtSetBDate.getText().toString();
//                Toast.makeText(RegistrationUDetails.this, "" + mDOB, Toast.LENGTH_SHORT).show();
                //dob = mDOB.getText().toString();

                if (mGender == null) {
                    Toast.makeText(RegistrationUDetails.this, "Please Select your gender", Toast.LENGTH_SHORT).show();
                } else if (mDOB == null) {
                    Toast.makeText(RegistrationUDetails.this, "Please Enter your Birthdate", Toast.LENGTH_SHORT).show();
                } else if (Common.isOnline(RegistrationUDetails.this)) {
                    new Async_RegistrationTask().execute();
                } else {
                    Common.displayToast(RegistrationUDetails.this, "Please check internet connectivity");
                }

            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Crop.REQUEST_PICK && resultCode == RegistrationUDetails.RESULT_OK) {
            beginCrop(data.getData());
        }
        if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RegistrationUDetails.RESULT_OK) {
            profilePic.setImageURI(Crop.getOutput(result));

            Uri uri = Crop.getOutput(result);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(RegistrationUDetails.this.getContentResolver(), uri);


            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byteArrayImage = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            if (Common.isOnline(RegistrationUDetails.this)) {

                //mProgressDialog.show();
                //Common.bitmapCommon=bitmap;
                profilePic.setImageBitmap(bitmap);
                //new Async_PictureUpload(byteArrayImage, encodedImage).execute();

            } else {
                Common.displayToast(RegistrationUDetails.this, "Please check internet connectivity");
            }

            Log.i("uri", "" + uri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(RegistrationUDetails.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(RegistrationUDetails.this.getCacheDir(), "cropped"));
        Log.i("destination", "" + destination);
        Crop.of(source, destination).asSquare().start(this);
    }

    public void Register() {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/registration_api.php").post(new FormEncodingBuilder().add("first_name", "" + fName).add("last_name", "" + lName).add("email_id", mEmail).add("dob", mDOB).add("gender", "" + mGender).add("username", username).add("password", password).add("mobile_no", phonenumber).add("gcm_id", token).build()).build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {

            } else {
                result = response.body().string();
                Log.i("", "++++" + result);

                JSONObject mainObj = new JSONObject(result);
                if (mainObj.has("ack")) {
                    try {
                        ack = mainObj.getBoolean("ack");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (ack) {
                    if (mainObj.has("registration_details")) {
                        JSONArray jsonArray = mainObj.getJSONArray("registration_details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.has("username")) {
                                username = jsonObject.getString("username");
                                AppSettings.setUserName(RegistrationUDetails.this, username);
                            }
                            if (jsonObject.has("email_id")) {
                                mEmail = jsonObject.getString("email_id");
                                AppSettings.setEmail(RegistrationUDetails.this, mEmail);
                            }
                            if (jsonObject.has("otp")) {
                                otp = jsonObject.getString("otp");
                            }
                            if (jsonObject.has("mobile_no")) {
                                phoneNumber = jsonObject.getString("mobile_no");
                            }
                            AppSettings.setContactNumber(RegistrationUDetails.this, phonenumber);
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

    public String postRun(RequestBody formBody) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String str = "";
        Request request = new Request.Builder()
                .url("http://tracert.retroinfotech.com/upload_dp_api.php")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(RegistrationUDetails.this);
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
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

            if (ack) {
                AppSettings.setisLoggedIn(RegistrationUDetails.this, true);
                if (Common.isOnline(RegistrationUDetails.this)) {
                    if (byteArrayImage != null) {
                        new Async_PictureUpload(byteArrayImage, encodedImage).execute();
                    } else {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        Intent intent = new Intent(RegistrationUDetails.this, RegiatrationVarification.class);
                        intent.putExtra("otp", otp);
                        intent.putExtra("email", mEmail);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Common.displayToast(RegistrationUDetails.this, "Please check internet connectivity");
                }

            } else {
                Common.displayToast(RegistrationUDetails.this, msg);
            }

        }
    }

    public class Async_PictureUpload extends AsyncTask<Void, Void, Void> {


        private String imagePath;
        private byte[] byteA;
        private String str;
        private String mainResult;
        private Bitmap resize;

        public Async_PictureUpload(byte[] byteArray, String encodedImage) {
            byteA = byteArray;
            str = encodedImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(RegistrationUDetails.this);
            progress.setMessage("Loading . . .");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"email_id\""),
                            RequestBody.create(null, mEmail))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"photo_data\""),
                            RequestBody.create(null, str))
                    //RequestBody.create(MEDIA_TYPE_JPG, new File("\"profile_picture")))
                    //.addPart(requestBodyImg)
                    .build();


            try {
                mainResult = postRun(requestBody);

                JSONObject jsonObject = new JSONObject(mainResult);

                if (jsonObject.has("img_path")) {
                    imagePath = jsonObject.getString("img_path");

                    AppSettings.setProfileIMG(RegistrationUDetails.this, imagePath);
                }

                url = "http://tracert.retroinfotech.com/" + imagePath;
                resize = getBitmapFromURL(url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (progress.isShowing()) {
                progress.dismiss();

                Intent intent = new Intent(RegistrationUDetails.this, RegiatrationVarification.class);
                intent.putExtra("otp", otp);
                intent.putExtra("email", mEmail);
                startActivity(intent);
                finish();
            }
        }
    }
}
