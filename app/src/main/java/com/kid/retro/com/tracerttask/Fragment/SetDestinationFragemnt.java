package com.kid.retro.com.tracerttask.Fragment;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.Common;
import com.kid.retro.com.tracerttask.common.GPSTracker;
import com.kid.retro.com.tracerttask.common.PlaceJSONParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kid on 1/8/2016.
 */
public class SetDestinationFragemnt extends Fragment implements View.OnClickListener, GoogleMap.OnMarkerDragListener {

    private LocationManager locationManager;
    private MapView mapView;
    private GoogleMap map;
    private double setLat;
    private double setLong;

    Bundle bundle;

    private GPSTracker gpsTracker;

    PlacesTask placesTask;
    ParserTask parserTask;

    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;

    private Button mImgSetDestination;
    private ProgressDialog progress;
    private String result;
    private String latitude;
    private String longitude;
    private String addressMain;

    private AutoCompleteTextView destination;
    private Button go;
    private String url;
    private String lati;
    private String longi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_set_destination, container, false);
        //txtDestinationAddress = (TextView) rootview.findViewById(R.id.txtDestinationAddress);
        gpsTracker = new GPSTracker(getActivity());
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
        }

        mImgSetDestination = (Button) rootview.findViewById(R.id.setDestination);
        destination = (AutoCompleteTextView) rootview.findViewById(R.id.destination);
        go = (Button) rootview.findViewById(R.id.go);


        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                new Async_LatLng(destination.getText().toString()).execute();
            }
        });


        mImgSetDestination.setOnClickListener(this);
        mapView = (MapView) rootview.findViewById(R.id.mapviewPickLocation);

        mapView.onCreate(savedInstanceState);
//        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        //   final Marker marker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target));
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (gpsTracker.canGetLocation()) {
            lati = String.valueOf(gpsTracker.getLatitude());
            longi = String.valueOf(gpsTracker.getLongitude());
            //latLng = new LatLng(lat, lng);
            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

            map.addMarker(new MarkerOptions().position(latLng).draggable(true));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            map.setOnMarkerDragListener(this);
        }else{
            Toast.makeText(getActivity(),"Please turn on Location from Setting.",Toast.LENGTH_SHORT).show();
        }


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        destination.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        if (Common.id == 1) {
            if (Common.isOnline(getActivity())) {

                //mProgressDialog.show();
                new Async_RegistrationTask(Double.parseDouble(Common.latitude), Double.parseDouble(Common.longitude)).execute();

            } else {
                Common.displayToast(getActivity(), "Please check internet connectivity");
            }
        }
        return rootview;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setDestination:

                if (Common.isOnline(getActivity())) {

                    //mProgressDialog.show();
                    if(lati!=null && longi!=null &&
                            lati.trim().length()>0 && longi.trim().length()>0)
                        new Async_RegistrationTask(Double.parseDouble(lati), Double.parseDouble(longi)).execute();

                } else {
                    Common.displayToast(getActivity(), "Please check internet connectivity");
                }

             /*   if (bundle == null) {
                    Toast.makeText(getActivity(), "First Drag the marker then set Destination...", Toast.LENGTH_LONG).show();
                } else {*/


                //    }

               /* Fragment PickedLocation = new FragmentShowPath();
                Fragment PickedLocation1 = new SetDestinationFragemnt();
                bundle = new Bundle();

                if (bundle != null) {

                    bundle.putString("setAddress", addressMain);
                    bundle.putString("setLati", String.valueOf(setLat));
                    bundle.putString("setLongi", String.valueOf(setLong));
                    PickedLocation.setArguments(bundle);

                    *//*Fragment AccountDetail = new AccountDetailFragment();
                    FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                    transaction1.replace(R.id.frameAccount, AccountDetail).addToBackStack(null);
                    transaction1.commit();*//*

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, PickedLocation);
                    getFragmentManager().popBackStack();
                    transaction.commit();

                } else if (bundle == null) {

                    Toast.makeText(getActivity(), "First Drag the marker then set Destination...", Toast.LENGTH_SHORT).show();

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, PickedLocation1);
                    getFragmentManager().popBackStack();
                    transaction.commit();

                } else {

                }*/
                break;
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

        fromPosition = marker.getPosition();
        Log.d("From Position", marker.getPosition().latitude + "----" + marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker2) {
        toPosition = marker2.getPosition();

        lati = String.valueOf(marker2.getPosition().latitude);
        longi = String.valueOf(marker2.getPosition().longitude);


    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        private double lat;
        private double lng;

        public Async_RegistrationTask(double latitude, double longitude) {
            lat = latitude;
            lng = longitude;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // Login(lat, lng);
            Login(Double.parseDouble(lati), Double.parseDouble(longi));
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


            if (Common.id == 1) {
                setLat = Double.parseDouble(Common.latitude);
                setLong = Double.parseDouble(Common.longitude);
            } else {
                setLat = Double.parseDouble(lati);
                setLong = Double.parseDouble(longi);
            }
            Fragment PickedLocation = new FragmentShowPath();
            bundle = new Bundle();
            bundle.putString("setAddress", addressMain);
            bundle.putString("setLati", String.valueOf(setLat));
            bundle.putString("setLongi", String.valueOf(setLong));
            PickedLocation.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, PickedLocation);
            transaction.addToBackStack(null);
            transaction.commit();


        }
    }


    public class Async_LatLng extends AsyncTask<Void, Void, Void> {

        private String address;

        public Async_LatLng(String s) {
            address = s;
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
            Toast.makeText(getActivity(), "++" + address, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub


            String TAG_RESULTS = "results";
            String TAG_GEOMETRY = "geometry";
            String TAG_VIEWPORT = "viewport";
            String TAG_NORTHEAST = "northeast";
            String TAG_LAT = "lat";
            String TAG_LNG = "lng";


            url = "http://maps.googleapis.com/maps/api/geocode/json?address="
                    + address + "&sensor=true";


            try {


                String respo = getRun(url);

                JSONObject json = new JSONObject(respo);

                JSONArray results = json.getJSONArray(TAG_RESULTS);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject r = results.getJSONObject(i);

                    JSONObject geometry = r.getJSONObject(TAG_GEOMETRY);

                    JSONObject viewport = geometry.getJSONObject(TAG_VIEWPORT);

                    JSONObject northest = viewport.getJSONObject(TAG_NORTHEAST);

                    lati = northest.getString(TAG_LAT);
                    longi = northest.getString(TAG_LNG);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

      /*  private void setMarker(double lati,double longi) {

            LatLng latLng = new LatLng(lati, longi);

            map.addMarker(new MarkerOptions().position(new LatLng(lati, longi)));

        }*/


        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            setMarker(Double.valueOf(lati), Double.valueOf(longi));

            if (progress.isShowing()) {
                progress.dismiss();
            }

            Common.displayToast(getActivity(), "" + lati + "+++" + longi);


        }
    }

    private void setMarker(Double aDouble, Double aDouble1) {


        map.clear();
        LatLng latLng = new LatLng(aDouble, aDouble1);

        map.addMarker(new MarkerOptions().position(new LatLng(aDouble, aDouble1)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aDouble, aDouble1), 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        map.setOnMarkerDragListener(this);
    }

    public String getRun(String url) throws IOException {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void Login(double lat, double lng) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {

            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder().url("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true_or_false\"").build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
            } else {
                result = response.body().string();

                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = jsonObject.getJSONArray("results");


                for (int i = 0; i < jsonArray.length(); i++) {


                    if (i == 0) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        if (jsonObject1.has("formatted_address")) {

                            addressMain = jsonObject1.getString("formatted_address");

                        }
                    }
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyATMR-6gNsCjk6lxkz5X84-01DC3QHUQAw";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from web service in background
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            destination.setAdapter(adapter);
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            MainActivity.changeTitle("SetDestination");
    }

}




