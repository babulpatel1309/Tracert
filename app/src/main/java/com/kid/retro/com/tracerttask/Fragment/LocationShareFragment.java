package com.kid.retro.com.tracerttask.Fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kid.retro.com.tracerttask.Adapter.CustomLocationShareList;
import com.kid.retro.com.tracerttask.Model.Friends;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kid on 1/8/2016.
 */
public class LocationShareFragment extends Fragment {

    private ListView shareLocation;
    private ArrayList<Friends> friends_list;
    private ProgressDialog progress;
    private String result;

    private CustomLocationShareList customLocationShareList;
    private String latitude;
    private String longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_location_share, container, false);

        init(rootview);
        onClick();

        return rootview;
    }

    private void onClick() {

    }

    private void init(View rootview) {
        shareLocation = (ListView) rootview.findViewById(R.id.shareLocation);
        friends_list = new ArrayList<>();


        Bundle bundle = getArguments();
        if (bundle != null) {
            latitude = bundle.getString("setLati", "Emplty");
            longitude = bundle.getString("setLongi", "Emplty");
        }

        if (Common.isOnline(getActivity())) {
            new Async_FrndListGet("Friends").execute();
        }
    }


    public class Async_FrndListGet extends AsyncTask<Void, Void, Void> {

        String check = "";

        public Async_FrndListGet(String check) {
            this.check = check;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading");
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            friendList();
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

            if (check.equals("Friends")) {
                if (friends_list != null && friends_list.size() > 0) {

                    customLocationShareList = new CustomLocationShareList(getActivity(), friends_list, latitude, longitude);
                    shareLocation.setAdapter(customLocationShareList);
                }
            }


        }
    }

    private void friendList() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("http://tracert.retroinfotech.com/frnd_request_get.php")
                .post(new FormEncodingBuilder()
                        .add("email_id", AppSettings.getEmail(getActivity())).build()).build();


        Response response = null;
        try {
            response = client.newCall(request).execute();

            result = response.body().string();

            System.out.print("RESPO = " + result);
            JSONObject jsonObject = new JSONObject(result);

            friends_list.clear();

            if (jsonObject.has("request_frnd")) {


                JSONArray jsonArray = jsonObject.getJSONArray("request_frnd");
                for (int i = 0; i < jsonArray.length(); i++) {

                    Friends friends = new Friends();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (jsonObject1.has("first_name")) {
                        friends.setFirst_name(jsonObject1.getString("first_name"));
                    }
                    if (jsonObject1.has("last_name")) {
                        friends.setLast_name(jsonObject1.getString("last_name"));
                    }
                    if (jsonObject1.has("req_id")) {
                        friends.setReq_id(jsonObject1.getString("req_id"));
                    }
                    if (jsonObject1.has("from_email")) {

                        if (jsonObject1.getString("from_email").equals(AppSettings.getEmail(getActivity()))) {
                            if (jsonObject1.has("to_email")) {
                                friends.setFrom_email(jsonObject1.getString("to_email"));
                            }
                        } else {
                            friends.setFrom_email(jsonObject1.getString("from_email"));
                        }
                    }
                    if (jsonObject1.has("to_email")) {
                        friends.setTo_email(jsonObject1.getString("to_email"));
                    }
                    if (jsonObject1.has("request_status")) {
                        friends.setRequest_status(jsonObject1.getString("request_status"));
                    }

                    friends_list.add(friends);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
