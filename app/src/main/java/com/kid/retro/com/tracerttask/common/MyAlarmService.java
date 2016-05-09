package com.kid.retro.com.tracerttask.common;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyAlarmService extends Service


{
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100000; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final String TAG = "Service";
    // Declaring a Location Manager
    protected LocationManager locationManager;
    GPSTracker gpsTracker;
    TimerTask doAsynchronousTask;
    Timer timer = new Timer();
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String time;
    String dateInString;
    private String email;
    private DatabaseHelper databaseHelper;
    Context context = this;


    private Location locOrigin, locNewPt;
    private double distKM = 0;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        Date now = new Date();


        gpsTracker = new GPSTracker(MyAlarmService.this);

        if (gpsTracker.canGetLocation()) {
            locOrigin = new Location("");
            locOrigin.setLatitude(gpsTracker.getLatitude());

            locOrigin.setLongitude((gpsTracker.getLongitude()));
        }
        super.onCreate();
    }


    @SuppressWarnings({"static-access", "deprecation", "deprecation"})
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        onstartFunction();
//        Ask.on(this)
//                .forPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION
//                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        , Manifest.permission.READ_CONTACTS) //one or more permissions
//                .withRationales("Location permission need for map to work properly",
//                        "In order to save file you will need to grant storage permission",
//                        "For invitation we need a permission for contacts.") //optional
//                .when(new Ask.Permission() {
//                    @Override
//                    public void granted(List<String> permissions) {
//                        Log.i(TAG, "granted :: " + permissions);
//
//                    }
//
//                    @Override
//                    public void denied(List<String> permissions) {
//                        Log.i(TAG, "denied :: " + permissions);
//                        Toast.makeText(context,"Permission not granted",Toast.LENGTH_LONG).show();
//                    }
//                }).go();

    }

    public void onstartFunction(){
        dateInString = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        time = sdf.format(new Date());

        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    // First get location from Network Provider
                    if (isNetworkEnabled) {

                        if (locationManager != null) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {

                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            }
                        }
                    }


                    if (location != null) {
                        try {
                            locNewPt = new Location("");
                            locNewPt.setLatitude(location.getLatitude());
                            locNewPt.setLongitude(location.getLongitude());

                            distKM = distKM + (locOrigin.distanceTo(locNewPt) / 1000);
                            Log.e("&&&", "TOTAL DISTACE TRAVELLED" + distKM);
                            locOrigin = locNewPt;

                            AppSettings.setKm(MyAlarmService.this, String.valueOf(distKM));

                            if (Common.isOnline(MyAlarmService.this)) {
                                email = AppSettings.getEmail(MyAlarmService.this);

                                new Async_RegistrationTask(location.getLatitude(), location.getLongitude(), dateInString.toString(), email, time).execute();
                            } else {
                                Common.displayToast(MyAlarmService.this, "Please check internet connectivity");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        timer.schedule(doAsynchronousTask, 0, 10000);
    }


    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        private String lat;
        private String lng;
        private String format;
        private String emailC;
        private String resultF;
        private String timeC;

        public Async_RegistrationTask(double latitude, double longitude, String date, String email, String time) {
            lat = "" + latitude;
            lng = "" + longitude;
            emailC = email;
            format = date;
            timeC = time;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {

                OkHttpClient client = new OkHttpClient();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String formatted = dateFormat.format(new Date());
                Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", emailC).add("latlong_types", "Y").add("on_date", format).add("on_time", formatted).add("latitude", lat).add("longitude", lng).build()).build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                } else {
                    resultF = response.body().string();


                }

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

        }
    }


}