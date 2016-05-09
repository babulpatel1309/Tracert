package com.kid.retro.com.tracerttask.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.R;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends Activity {
    SharedPreferences prefs;
    List<NameValuePair> params;
    EditText chat_msg;
    String chatmsg;
    Button send_btn;
    Bundle bundle;
    TableLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tab = (TableLayout) findViewById(R.id.tab);

        prefs = getSharedPreferences("Chat", 0);
        bundle = getIntent().getBundleExtra("INFO");
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("CURRENT_ACTIVE", bundle.getString("mobile_no"));
        edit.commit();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


        if (bundle.getString("username") != null) {
            TableRow tr1 = new TableRow(getApplicationContext());
            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml("<b>" + bundle.getString("username") + " : </b>" + bundle.getString("msg")));
            tr1.addView(textview);
            tab.addView(tr1);

        }

        chat_msg = (EditText) findViewById(R.id.chat_msg);
        chatmsg = chat_msg.getText().toString();
        send_btn = (Button) findViewById(R.id.sendbtn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tr2 = new TableRow(getApplicationContext());
                tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#A901DB"));
                textview.setText(Html.fromHtml("<b>You : </b>" + chat_msg.getText().toString()));
                tr2.addView(textview);
                tab.addView(tr2);
                new Send().execute();
            }
        });


    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("msg");
            String str1 = intent.getStringExtra("fromname");
            String str2 = intent.getStringExtra("fromu");
            if (str2.equals(bundle.getString("mobile_no"))) {

                TableRow tr1 = new TableRow(getApplicationContext());
                tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#0B0719"));
                textview.setText(Html.fromHtml("<b>" + str1 + " : </b>" + str));
                tr1.addView(textview);
                tab.addView(tr1);
            }
        }
    };

    Context context = this;

    private class Send extends AsyncTask<String, String, JSONObject> {

        String chats = chat_msg.getText().toString();
        Response response = null;
        String result = "";

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();

//            params.add(new BasicNameValuePair("user_email_id", AppSettings.getEmail(context)));
//            params.add(new BasicNameValuePair("user_mobile_no", AppSettings.getContactNumber(context)));
//            params.add(new BasicNameValuePair("req_email_id", AppSettings.req_user_email));
//            params.add(new BasicNameValuePair("req_mobile_no", AppSettings.req_user_phone));
            params.add(new BasicNameValuePair("user_email_id", "babulpatel1309@gmail.com"));
            params.add(new BasicNameValuePair("user_mobile_no", "7698646474"));
            params.add(new BasicNameValuePair("req_email_id", "babulpatel1309@gmail.com"));
            params.add(new BasicNameValuePair("req_mobile_no", "7698646474"));
            params.add(new BasicNameValuePair("msg", chats));

//            JSONObject jObj = json.getJSONFromUrl("http://tracert.retroinfotech.com/get_current_location_apis.php",params);
            JSONObject jObj = json.getJSONFromUrl("http://tracert.retroinfotech.com/chat_msg.php", params);
//            JSONObject jObj = json.getJSONFromUrl("http://trracert.com/Matrix/chat_msg.php", params);

//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url("http://trracert.com/Matrix/chat_msg.php").
//                    post(new FormEncodingBuilder().
//                            add("user_email_id", AppSettings.getEmail(context)).
//                            add("user_mobile_no", AppSettings.getContactNumber(context)).
//                            add("req_email_id", AppSettings.req_user_email).
//                            add("req_mobile_no", AppSettings.req_user_phone).
//                            add("msg", chats).build()).build();
//            try {
//                response = client.newCall(request).execute();
//                if (!response.isSuccessful()) {
//                } else {
//                    result = response.body().string();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            //http://trracert.com/Matrix/login_api.php
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            chat_msg.setText("");

            String res = null;
            try {
                res = json.getString("response");
                if (res.equals("Failure")) {
                    Toast.makeText(getApplicationContext(), "The user has logged out. You cant send message anymore !", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}