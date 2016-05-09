
package com.kid.retro.com.tracerttask.common;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.Timer;
import java.util.TimerTask;


public class DataService extends Service {


    public static String MY_ACTION = "MY_ACTION";
    private String response = null;
    private String response2 = null;
    private Double selectedFromLatitude;
    private Double selectedFromLongitude;
    private Timer timer;
    private TimerTask doAsynchronousTask;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {

            @Override
            public void run() {

                if (Common.isOnline(DataService.this)) {
                    new GetJsonResponse().execute();
                }
            }

        };
        timer.schedule(doAsynchronousTask, 0, 60000);
        return super.onStartCommand(intent, flags, startId);
    }

    public class GetJsonResponse extends AsyncTask<String, String, String> {


        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {


                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url("http://tracert.retroinfotech.com/get_current_location_apis.php").post(new FormEncodingBuilder().add("email_id", "" + AppSettings.getEmail(DataService.this)).build()).build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
                } else {
                    result = response.body().string();
                }
//                selectedFromLatitude = 52.2391030000;
//                selectedFromLongitude = 6.8443970000;


                //       https:
//travelguide.moopmobility.nl:443/timemachine/v1/stops?limit=3&near=52.2391030000,6.8443970000&version=CURL&include_shapes=0&include_trains=true&radius=2000&limit_times=3&language=NL


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (result != null) {

                Intent intent = new Intent();
                intent.setAction(MY_ACTION);
                intent.putExtra("service", result);
                sendBroadcast(intent);
            }
        }
    }
}
