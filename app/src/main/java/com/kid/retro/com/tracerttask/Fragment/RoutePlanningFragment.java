package com.kid.retro.com.tracerttask.Fragment;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kid on 1/8/2016.
 */
public class RoutePlanningFragment extends Fragment implements GoogleMap.OnMarkerDragListener, View.OnClickListener{

    private LocationManager locationManager;
    private MapView mapView;
    private GoogleMap map;
    private Location location;
    private Geocoder geocoder;
    private TextView mPickedLatLong;
    List<Address> addresses;
    private Button mSetAddressButton;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private double setLat;
    private double setLong;

    Bundle bundle;

    private GPSTracker gpsTracker;

    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;

    private Button mImgSetDestination;
    private ProgressDialog progress;
    private String result;
    private String latitude;
    private String longitude;
    private String addressMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_route_planning, container, false);

        gpsTracker = new GPSTracker(getActivity());
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
        }

        mImgSetDestination = (Button) rootview.findViewById(R.id.setDestination);
        mImgSetDestination.setOnClickListener(this);
        mapView = (MapView) rootview.findViewById(R.id.mapviewPickLocation);

        mapView.onCreate(savedInstanceState);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        //   final Marker marker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target));
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (gpsTracker.canGetLocation()) {

            setLat = gpsTracker.getLatitude();
            setLong = gpsTracker.getLongitude();
            //latLng = new LatLng(lat, lng);
            LatLng latLng = new LatLng(setLat, setLong);

            map.addMarker(new MarkerOptions().position(latLng).draggable(true));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            map.setOnMarkerDragListener(this);

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
                    new Async_RegistrationTask(setLat, setLong).execute();

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
    public void onMarkerDragEnd(Marker marker) {
        toPosition = marker.getPosition();

        setLat = marker.getPosition().latitude;
        setLong = marker.getPosition().longitude;


        /*Toast.makeText(getActivity(),"Marker " + marker.getTitle() + " dragged from " + marker.getPosition().latitude
                + " to " + marker.getPosition().longitude, Toast.LENGTH_LONG).show();*/

      /*  try {
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            setLat = marker.getPosition().latitude;
            setLong = marker.getPosition().longitude;

            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();

            //mPickedLatLong.setText(address + "," + city);

            Toast.makeText(getActivity(), "Address is :" + address + ", " + city, Toast.LENGTH_SHORT).show();

            //Toast.makeText(getActivity(), "Your full Address is : "+knownName+" ,"+address+" ,"+city+" ,"+state+" ,"+country+" ,"+postalCode,Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Can't getting address", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/

        //mPickedLatLong.setText(marker.getPosition().latitude + "," + marker.getPosition().longitude);
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
            Login(lat, lng);
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

            Fragment PickedLocation = new FragmentShowPath();
            bundle = new Bundle();
            bundle.putString("setAddress", addressMain);
            bundle.putString("setLati", String.valueOf(setLat));
            bundle.putString("setLongi", String.valueOf(setLong));
            PickedLocation.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, PickedLocation);
            getFragmentManager().popBackStack();
            transaction.commit();


        }
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


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            MainActivity.changeTitle("Route Planning");
    }

}
