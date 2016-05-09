package com.kid.retro.com.tracerttask.Fragment;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kid.retro.com.tracerttask.Adapter.CustomBaseGetAllUser;
import com.kid.retro.com.tracerttask.Adapter.CustomListLocationHistory;
import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.Model.GetAllUser;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.kid.retro.com.tracerttask.common.GPSTracker;
import com.kid.retro.com.tracerttask.common.HorizontalListView;
import com.kid.retro.com.tracerttask.common.ImageLoader;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
public class ShowLocation extends Fragment {

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


    private MarkerOptions markerOptionsUser;
    private Marker markerUser;
    private Bitmap bitmap;

    private HorizontalListView horizontalList;

    private CustomBaseGetAllUser customBaseGetAllUser;
    private ListView listLocatin;

    private CustomListLocationHistory customListLocationHistory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_location_history, container, false);
        //txtDestinationAddress = (TextView) rootview.findViewById(R.id.txtDestinationAddress);


        init(rootview);


        listLocatin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment PickedLocation = new LocationHistoryFragment();
                bundle = new Bundle();

                bundle.putString("email", getAllUsers_ArrayList.get(position).getEmail_id());
                PickedLocation.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, PickedLocation);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return rootview;
    }

    private void getAllUser() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/get_current_location_apis.php").post(new FormEncodingBuilder().add("email_id", "" + AppSettings.getEmail(getActivity())).build()).build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
            } else {
                result = response.body().string();


                JSONObject mainObj = new JSONObject(result);


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

                            getAllUsers_ArrayList.add(getAllUser);
                        }

                    }

                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(View rootview) {

        listLocatin = (ListView) rootview.findViewById(R.id.listLocatin);
        getAllUsers_ArrayList = new ArrayList<>();

        if (Common.isOnline(getActivity())) {
            new Async_RegistrationTask().execute();
        }

    }


    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

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
            getAllUser();

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


            if (getAllUsers_ArrayList.size() > 0) {

                customListLocationHistory = new CustomListLocationHistory(getActivity(), getAllUsers_ArrayList);
                listLocatin.setAdapter(customListLocationHistory);
            }

        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            MainActivity.changeTitle("Location History");
    }


}
