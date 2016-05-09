package com.kid.retro.com.tracerttask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class ResetPassword extends Activity {

    private EditText edtNewPassword;
    private EditText edtConfirmPassword;
    private RelativeLayout relLoginBtn;

    private String mVarificationNumber = "null";

    private Intent intent;

    private String otp;
    private String email;
    private boolean ack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#1cbbb4"));
        }

        setContentView(R.layout.activity_reset_password);

        intent = getIntent();

        if (intent != null) {
            email = intent.getStringExtra("email");
        }

        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);

        relLoginBtn = (RelativeLayout) findViewById(R.id.relLoginBtn);


        relLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        relLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtNewPassword.getText().toString().trim().length() > 0) {

                    if (edtConfirmPassword.getText().toString().trim().length() > 0) {


                        if (edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                            if (Common.isOnline(ResetPassword.this)) {

                                //mProgressDialog.show();
                                new Async_ResetTask().execute();

                            } else {
                                Common.displayToast(ResetPassword.this, "Please check internet connectivity");
                            }
                        } else {

                        }
                    } else {

                    }
                } else {

                }

            }
        });
    }

    public class Async_ResetTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(ResetPassword.this);
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            resetPassword();
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

                Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void resetPassword() {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().url("http://tracert.retroinfotech.com/forgotpass_api.php").post(new FormEncodingBuilder().add("email_id", email).add("password", edtConfirmPassword.getText().toString()).build()).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {

                String result = response.body().string();

                Log.i("","+++"+result);
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.has("ack")) {
                    ack = jsonObject.getBoolean("ack");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
