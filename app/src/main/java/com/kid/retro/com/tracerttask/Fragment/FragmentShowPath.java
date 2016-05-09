package com.kid.retro.com.tracerttask.Fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kid.retro.com.tracerttask.HttpConnection;
import com.kid.retro.com.tracerttask.PathJSONParser;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.Common;
import com.kid.retro.com.tracerttask.common.GPSTracker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kid on 1/16/2016.
 */
public class FragmentShowPath extends Fragment {

    private LocationManager locationManager;
    private MapView mapView;
    private GoogleMap map;
    private Location location;
    private View rootview;
    private Geocoder geocoder;
    private List<Address> addresses;
    private GPSTracker gpsTracker;

    String myAddress;
    String myCity;
    String myState;
    String myPostalCode;
    String myKnownLocation;
    String latitude;
    String longitude;

    double lat;
    double lng;

    private TextView mTxtAddress;

    private Button share;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_showpath, container, false);



        Common.id = 0;
        mTxtAddress = (TextView) rootview.findViewById(R.id.getAddress);

        share = (Button) rootview.findViewById(R.id.share);
        mapView = (MapView) rootview.findViewById(R.id.mapviewPathLocation);
        mapView.onCreate(savedInstanceState);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        gpsTracker = new GPSTracker(getActivity());

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map = mapView.getMap();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        try {
            if (gpsTracker.canGetLocation()) {


                lat = gpsTracker.getLatitude();
                lng = gpsTracker.getLongitude();

                LatLng latLng = new LatLng(lat, lng);

                map.addMarker(new MarkerOptions().position(latLng));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1);

                    Bundle bundle = getArguments();
                    if (bundle == null) {
                        //Toast.makeText(getActivity(), "welcome to ", Toast.LENGTH_LONG).show();
                    } else {
                        myAddress = bundle.getString("setAddress", "Empty");
                      /*  myCity = bundle.getString("setCity", "Empty");
                        Log.d(">>>City::", myCity);
                        myState = bundle.getString("setState", "Empty");
                        Log.d(">>>State::", myState);
                        myPostalCode = bundle.getString("setPostalcode", "Empty");
                        Log.d(">>>Postal::", myPostalCode);
                        myKnownLocation = bundle.getString("setKnownname", "Empty");
                        Log.d(">>>KnownLocation::", myKnownLocation);
                      */
                        latitude = bundle.getString("setLati", "Emplty");
                        Log.d(">>>Latitude::", latitude);
                        longitude = bundle.getString("setLongi", "Emplty");
                        Log.d(">>>Longitude::", longitude);

                        LatLng latLng1 = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                        map.addMarker(new MarkerOptions().position(latLng1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                        String url = getMapsApiDirectionsUrl();
                        ReadTask downloadTask = new ReadTask();
                        downloadTask.execute(url);

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15));
                        //map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                        // mTxtAddress.setText(myAddress+", "+myCity+", "+myState+", "+myPostalCode+", "+myKnownLocation+", "+latitude+", "+longitude);

                        mTxtAddress.setText(myAddress);

                        //Toast.makeText(getActivity(), "" + myAddress, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                gpsTracker.showSettingsAlert();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment locationShareFragment = new LocationShareFragment();


                bundle = new Bundle();
                bundle.putString("setLati", String.valueOf(latitude));
                bundle.putString("setLongi", String.valueOf(longitude));
                locationShareFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, locationShareFragment);
                transaction.commit();

            }
        });

        return rootview;
    }

    private String getMapsApiDirectionsUrl() {


        String output = "json";

        String str_origin = "origin=" + lat + ","
                + lng;

        // Destination of route
        String str_dest = "destination=" + latitude + ","
                + longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";

        waypoints = "waypoints=";
        // waypoints += SURAT.latitude + "," + SURAT.longitude + "|";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
                + waypoints;
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;


        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes

            if (routes.size() > 0) {
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(6);
                    polyLineOptions.color(Color.GREEN);
                }

                map.addPolyline(polyLineOptions);
            }
        }
    }

    private int checkSelfPermission(String accessFineLocation) {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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


}
