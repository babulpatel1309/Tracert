package com.kid.retro.com.tracerttask.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kid.retro.com.tracerttask.Adapter.CustomBaseGetAllUser;
import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.Model.GetAllUser;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.Common;
import com.kid.retro.com.tracerttask.common.DataService;
import com.kid.retro.com.tracerttask.common.GPSTracker;
import com.kid.retro.com.tracerttask.common.HorizontalListView;
import com.kid.retro.com.tracerttask.common.ImageLoader;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kid on 1/8/2016.
 */
public class MonitorFragment extends Fragment {

    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;
    private List<Address> addresses;
    private Bundle bundle;
    private TimerTask doAsynchronousTask;
    private Timer timer;
    private LocationManager locationManager;
    private MapView mapView;
    private GoogleMap map;
    private Location location;
    private Geocoder geocoder;
    private TextView mPickedLatLong;
    private Button mSetAddressButton;


    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private String result;

    private double setLat;
    private double setLong;


    private ImageLoader imageLoader;
    private GPSTracker gpsTracker;
    private ImageView mImgSetDestination;
    private ProgressDialog progress;


    private ArrayList<GetAllUser> getAllUsers_ArrayList;

    private ImageView transparent_image;

    private MarkerOptions markerOptionsUser;
    private Marker markerUser;
    private Bitmap bitmap;

    private HorizontalListView horizontalList;

    private CustomBaseGetAllUser customBaseGetAllUser;
    private String datapassed = "";
    private DepartureTimeUpdateReceiver departureTimeUpdateReceiver;

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int w = 125;
        int h = 125;

        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_monitor, container, false);
        //txtDestinationAddress = (TextView) rootview.findViewById(R.id.txtDestinationAddress);


        imageLoader = new ImageLoader(getActivity());


        enableGps();

        init(rootview);


        gpsTracker = new GPSTracker(getActivity());
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
        }
        mapView = (MapView) rootview.findViewById(R.id.mapviewPickLocation);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);


        horizontalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String latitude = getAllUsers_ArrayList.get(position).getLatitude();
                String longitude = getAllUsers_ArrayList.get(position).getLongitude();

                if (!(latitude.equals("") && latitude == null)) {

                    if (!(longitude.equals("") && longitude == null)) {
                        //    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(getAllUsers_ArrayList.get(position).getLatitude()), Double.parseDouble(getAllUsers_ArrayList.get(position).getLongitude())), 14.0f));

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), 14));
                    }
                }
            }
        });
        return rootview;
    }


    private void getAllUser(String datapassed) {

        getAllUsers_ArrayList.clear();
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {

            //   OkHttpClient client = new OkHttpClient();

            // Request request = new Request.Builder().url("http://tracert.retroinfotech.com/get_current_location_apis.php").post(new FormEncodingBuilder().add("email_id", "" + AppSettings.getEmail(getActivity())).build()).build();

            //Response response = client.newCall(request).execute();

//            if (!response.isSuccessful()) {
////                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
//            } else
//            {
            // result = response.body().string();
            {

                JSONObject mainObj = new JSONObject(datapassed);


                if (mainObj.has("location_datas")) {


                    JSONArray jsonArray = mainObj.getJSONArray("location_datas");
                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONArray jsonArray1 = jsonArray.getJSONArray(i);

                        for (int j = 0; j < jsonArray1.length(); j++) {
                            GetAllUser getAllUser = new GetAllUser();

                            JSONObject jsonObject = jsonArray1.getJSONObject(j);


                            if (jsonObject.has("username")) {

                                getAllUser.setUsername(jsonObject.getString("username"));
                            }
                            if (jsonObject.has("email_id")) {

                                getAllUser.setEmail_id(jsonObject.getString("email_id"));
                            }
                            if (jsonObject.has("current_latitude")) {

                                getAllUser.setLatitude(jsonObject.getString("current_latitude"));
                            }
                            if (jsonObject.has("current_longitude")) {
                                getAllUser.setLongitude(jsonObject.getString("current_longitude"));
                            }
                            if (jsonObject.has("img_path")) {


                                if (!jsonObject.getString("img_path").equals("")) {


                                    String imageP = "http://tracert.retroinfotech.com/" + jsonObject.getString("img_path");
                                    Bitmap bitmap = null;
                                    try {

                                        bitmap = imageLoader.getBitmap(imageP);
                                        bitmap = getRoundedCornerBitmap(bitmap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    getAllUser.setBitmap(bitmap);
                                } else {
                                    getAllUser.setBitmap(null);
                                }
                            }

                            getAllUsers_ArrayList.add(getAllUser);
                        }

                    }


                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callingWeb(ArrayList<GetAllUser> getAllUsers_arrayList) {
        if (getAllUsers_ArrayList.size() > 0) {


            customBaseGetAllUser = new CustomBaseGetAllUser(getActivity(), getAllUsers_ArrayList);
            horizontalList.setAdapter(customBaseGetAllUser);


            for (int i = 0; i < getAllUsers_ArrayList.size(); i++) {

                try {

                    // Setting marker on every users..
                    String eMail = getAllUsers_ArrayList.get(i).getEmail_id().toString().substring(0, 1);


                    //    bitmap = getRoundedCornerBitmap(bitmap);

                    if (!getAllUsers_ArrayList.get(i).getLatitude().equals("") && !getAllUsers_ArrayList.get(i).getLongitude().equals("")) {
                        LatLng latLng = new LatLng(Double.parseDouble(getAllUsers_ArrayList.get(i).getLatitude()), Double.parseDouble(getAllUsers_ArrayList.get(i).getLongitude()));
                        //map.addMarker(new MarkerOptions().position(latLng));


                        if (getAllUsers_ArrayList.get(i).getBitmap() != null) {
                            markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(getAllUsers_ArrayList.get(i).getBitmap()));
                            map.addMarker(markerOptionsUser);
                        } else {
                            if ("a".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.a));
                                map.addMarker(markerOptionsUser);
                                //  markerUser=map.addMarker(markerOptionsUser);
                            } else if ("b".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.b));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("c".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.c));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("d".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.d));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("e".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.e));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("f".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.f));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("g".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.g));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("h".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.h));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("i".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.i));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("j".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.j));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("k".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.k));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("l".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.l));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("m".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.m));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("n".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.n));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("o".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.o));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("p".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.p));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("q".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.q));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("r".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.r));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("s".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.s));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("t".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.t));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("u".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.u));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("v".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.v));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("w".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.w));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("x".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.x));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("y".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.y));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else if ("z".equalsIgnoreCase(eMail)) {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.z));
                                markerUser = map.addMarker(markerOptionsUser);
                            } else {
                                markerOptionsUser = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
                                markerUser = map.addMarker(markerOptionsUser);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 15));

                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        }
    }

    private void init(View rootview) {
        departureTimeUpdateReceiver = new DepartureTimeUpdateReceiver();
        transparent_image = (ImageView) rootview.findViewById(R.id.transparent_image);

        horizontalList = (HorizontalListView) rootview.findViewById(R.id.horizontalList);
        getAllUsers_ArrayList = new ArrayList<>();


        transparent_image.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        horizontalList.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        horizontalList.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        horizontalList.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }


        });


    }

    private void enableGps() {

        LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    }
            );
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private int checkSelfPermission(String accessFineLocation) {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DataService.MY_ACTION);
        getActivity().getApplicationContext().registerReceiver(departureTimeUpdateReceiver, intentFilter);
        Intent intent = new Intent(getActivity(),
                DataService.class);
        getActivity().getApplicationContext().startService(intent);
    }

//    void registerReceiver() {
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(DataService.MY_ACTION);
//        registerReceiver(departureTimeUpdateReceiver, intentFilter);
//        Intent intent = new Intent(getActivity(),
//                DataService.class);
//        startService(intent);
//    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        getActivity().getApplicationContext().unregisterReceiver(departureTimeUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private class DepartureTimeUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            datapassed = arg1.getStringExtra("service");
            if (datapassed != null) {


                if (Common.isOnline(getActivity())) {
                    new Async_RegistrationTask(datapassed).execute();
                }
                //  getAllUser(datapassed);


            } else {
                //  Toast.makeText(getActivity(), getResources().getString(R.string.Notification_Data_Server), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        private String newImage;

        public Async_RegistrationTask(String datapassed) {
            newImage = datapassed;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

//            progress = new ProgressDialog(getActivity());
//            progress.setMessage("Loading");
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            getAllUser(newImage);

            return null;
        }


        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            if (getAllUsers_ArrayList.size() > 0) {
                callingWeb(getAllUsers_ArrayList);
            }


        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            MainActivity.changeTitle("Live");
    }

}
