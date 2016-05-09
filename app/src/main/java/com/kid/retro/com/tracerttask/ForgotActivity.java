package com.kid.retro.com.tracerttask;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

/**
 * Created by Kid on 1/8/2016.
 */
public class ForgotActivity extends Activity {

    private RelativeLayout btnForgot;
    private EditText edtEntEmail;
    private ProgressDialog progress;
    private String otp;
    private String msg;
    private boolean ack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_new);


        findView();

        onClick();

    }

    private void onClick() {

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtEntEmail.getText().toString().trim().length() > 0) {
                    if (Common.isOnline(ForgotActivity.this)) {

                        //mProgressDialog.show();
                        new Async_ForgotTask().execute();

                    } else {
                        Common.displayToast(ForgotActivity.this, "Please check internet connectivity");
                    }

                } else {

                }

            }
        });

    }

    private void findView() {


        btnForgot = (RelativeLayout) findViewById(R.id.relLoginBtn);
        edtEntEmail = (EditText) findViewById(R.id.edtEntEmail);
    }

    public class Async_ForgotTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(ForgotActivity.this);
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            forgotActivity();
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


                Intent intent = new Intent(ForgotActivity.this, CodeVarification.class);
                intent.putExtra("email", edtEntEmail.getText().toString());
                intent.putExtra("otp", otp);
                startActivity(intent);
            }
        }
    }

    private void forgotActivity() {


        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().url("http://tracert.retroinfotech.com/forgotpass_api.php").post(new FormEncodingBuilder().add("email_id", edtEntEmail.getText().toString()).build()).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            String result = response.body().string();


            JSONObject jsonObject = new JSONObject(result);


            if (jsonObject.has("ack")) {


                ack = jsonObject.getBoolean("ack");
            }

            if (jsonObject.has("msg")) {

                msg = jsonObject.getString("msg");
            }

            if (jsonObject.has("otp")) {

                otp = jsonObject.getString("otp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotActivity.this,LoginActivity.class));
        finish();
    }
}
