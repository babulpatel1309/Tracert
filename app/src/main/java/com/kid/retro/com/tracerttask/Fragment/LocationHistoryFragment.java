package com.kid.retro.com.tracerttask.Fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.Adapter.CustomListAdapterTask;
import com.kid.retro.com.tracerttask.Model.LocationHistory;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.kid.retro.com.tracerttask.common.DatabaseHelper;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kid on 1/8/2016.
 */
public class LocationHistoryFragment extends Fragment {


    private ListView listLocatin;
    private String resultF;
    private ArrayList<LocationHistory> locationHistories;

    private DatabaseHelper databaseHelper;
    private CustomListAdapterTask customAdapterMealItem;
    private ProgressDialog progress;
    private String dateInString;
    private String email;
    private String result2;
    private String addressMain;
    private Bundle bundle;
    private String myAddress;
    private String latitude;
    private String longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_location_history, container, false);

        init(rootview);

        return rootview;
    }

    private void init(View rootview) {

        dateInString = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


        listLocatin = (ListView) rootview.findViewById(R.id.listLocatin);
        locationHistories = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getActivity());


        bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("email", "");
        }

        if (Common.isOnline(getActivity())) {

            //mProgressDialog.show();
            new Async_RegistrationTask().execute();

        } else {
            Common.displayToast(getActivity(), "Please check internet connectivity");
        }



       /* try {
            locationHistories = databaseHelper.getparentItem();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(locationHistories.size() > 0){

            customAdapterMealItem = new CustomListAdapterTask(getActivity() , locationHistories);
            listLocatin.setAdapter(customAdapterMealItem);
        }*/


    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub


            super.onPreExecute();

            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading ....");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            getLocation();
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
            if (locationHistories.size() > 0) {
                customAdapterMealItem = new CustomListAdapterTask(getActivity(), locationHistories);
                listLocatin.setAdapter(customAdapterMealItem);
            } else {
                Toast.makeText(getActivity(), "No history available for this date", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void getLocation() {

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", email).add("latlong_types", "N").add("on_date", dateInString).build()).build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
            } else {
                resultF = response.body().string();

                Log.i("", "++++" + resultF);

                if (resultF != null) {


                    JSONObject jsonObject = new JSONObject(resultF);

                    if (jsonObject.has("location_datas")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("location_datas");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            LocationHistory locationHistory = new LocationHistory();
                            JSONObject jsonObjectInner = jsonArray.getJSONObject(i);

                            if (jsonObjectInner.has(dateInString)) {
                                JSONObject jsonObjectInnerI = jsonObjectInner.getJSONObject(dateInString);

                              /*  if(jsonObjectInnerI.has("location_id")) {
                                    locationHistory.setLocation_id(jsonObjectInner.getString("location_id"));
                                }*/
                                /*if (jsonObjectInnerI.has("latitude")) {
                                    locationHistory.setLatitude(jsonObjectInnerI.getString("latitude"));
                                }
                                if (jsonObjectInnerI.has("longitude")) {
                                    locationHistory.setLongitude(jsonObjectInnerI.getString("longitude"));
                                }*/


                                if (jsonObjectInnerI.has("latitude")) {
                                    if (jsonObjectInnerI.has("longitude")) {


                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                        try {

                                            OkHttpClient client2 = new OkHttpClient();


                                            Request request2 = new Request.Builder().url("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + jsonObjectInnerI.getString("latitude") + "," + jsonObjectInnerI.getString("longitude") + "&sensor=true_or_false\"").build();

                                            Response response2 = client2.newCall(request2).execute();

                                            if (!response2.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
                                            } else {
                                                result2 = response2.body().string();

                                                JSONObject jsonObject2 = new JSONObject(result2);

                                                JSONArray jsonArray2 = jsonObject2.getJSONArray("results");


                                                for (int j = 0; j < jsonArray2.length(); j++) {


                                                    if (j == 0) {
                                                        JSONObject jsonObject1 = jsonArray2.getJSONObject(j);

                                                        if (jsonObject1.has("formatted_address")) {

                                                            addressMain = jsonObject1.getString("formatted_address");
                                                            locationHistory.setAddress(addressMain);
                                                        }
                                                    }
                                                }


                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                     /*   List<Address> addresses;
                                        Button mSetAddressButton;
                                        String address;
                                        String city;
                                        String state;
                                        String country;
                                        String postalCode;
                                        String knownName;
                                        Geocoder geocoder;
                                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                                        String finalTxt = null;
                                        try {
                                            addresses = geocoder.getFromLocation(Double.parseDouble(jsonObjectInnerI.getString("latitude")), Double.parseDouble(jsonObjectInnerI.getString("longitude")), 1);

                                            address = addresses.get(0).getAddressLine(0);
                                            city = addresses.get(0).getLocality();
                                            state = addresses.get(0).getAdminArea();

                                            finalTxt = address + "," + city + "," + state;*/


                                        /*} catch (Exception e) {
                                            e.printStackTrace();
                                        }*/
                                    }

                                }
                                if (jsonObjectInnerI.has("on_date")) {
                                    locationHistory.setOn_date(jsonObjectInnerI.getString("on_date"));
                                }
                                if (jsonObjectInnerI.has("on_time")) {
                                    locationHistory.setOn_time(jsonObjectInnerI.getString("on_time"));

                                }
                                if (jsonObjectInnerI.has("email_id")) {
                                    locationHistory.setEmail_id(jsonObjectInnerI.getString("email_id"));

                                }
                                locationHistories.add(locationHistory);
                            }
                        }

                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
