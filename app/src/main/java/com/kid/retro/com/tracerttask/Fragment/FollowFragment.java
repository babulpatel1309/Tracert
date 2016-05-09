package com.kid.retro.com.tracerttask.Fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.Adapter.CustomFrndList;
import com.kid.retro.com.tracerttask.Model.FriendCheckList;
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
public class FollowFragment extends Fragment {

    private EditText frndEmail;
    private Button go;
    private ProgressDialog progress;
    private String result;
    private boolean ack = true;
    private String msg;

    private Spinner spinner;


    private ListView frndList;

    private Button add;
    private Button pending;

    private CustomFrndList customFrndList;

    private CustomFrndTotalList customFrndTotalList;

    private ArrayList<FriendCheckList> friendCheckLists;

    private ArrayList<Friends> friends_list;
    private ArrayList<Friends> friend_Req;
    private ArrayList<Friends> friend_pending;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_follow, container, false);


        Common.id = 0;
        init(rootview);
        onClick();
        return rootview;
    }

    private void onClick() {
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (frndEmail.getText().toString().trim().length() > 0) {

                    if (Common.isOnline(getActivity())) {
                        new Async_RegistrationTask().execute();
                    }

                } else {
                    Common.displayToast(getActivity(), "Please ,enter email Id");
                }
            }
        });
    }


    private void init(View rootview) {
        mContext = getActivity();

        frndEmail = (EditText) rootview.findViewById(R.id.frndEmail);

        frndList = (ListView) rootview.findViewById(R.id.frndList);

        go = (Button) rootview.findViewById(R.id.go);

        add = (Button) rootview.findViewById(R.id.add);

        pending = (Button) rootview.findViewById(R.id.pending);

        spinner = (Spinner) rootview.findViewById(R.id.frndSpinner);
        friendCheckLists = new ArrayList<>();
        friends_list = new ArrayList<>();
        friend_Req = new ArrayList<>();

        friend_pending = new ArrayList<>();
//        if (Common.isOnline(getActivity())) {
//            Log.e("Hi..","Async_FrndListGet..");
//            new Async_FrndListGet().execute();
//        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinner.getItemAtPosition(position).equals("Friends")) {
                    if (Common.isOnline(getActivity())) {
                        new Async_FrndListGet("Friends").execute();
                    }
                }
                if (spinner.getItemAtPosition(position).equals("Request")) {
                    if (Common.isOnline(getActivity())) {
                        new Async_FrndListGet("Request").execute();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


       /* spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(spinner.getItemAtPosition(position).equals("Friends")){
                    customFrndTotalList = new CustomFrndTotalList(getActivity() , friends_list);
                    frndList.setAdapter(customFrndTotalList);
                }

                if(spinner.getItemAtPosition(position).equals("Request")){
                    customFrndTotalList = new CustomFrndTotalList(getActivity() , friend_Req);
                    frndList.setAdapter(customFrndTotalList);
                }


            }
        });
*/

       /* if (Common.isOnline(getActivity())) {
            new Async_FrndListGet().execute();
        }*/
    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            try {


                if (progress != null) {
                    progress = new ProgressDialog(getActivity());
                    progress.setMessage("Loading");
                    progress.setIndeterminate(true);
                    progress.show();
                    friendCheckLists.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            friendRequest();
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


            try {

                if (progress.isShowing()) {
                    progress.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (!ack) {

                Common.displayToast(getActivity(), "Sorry , user not found");
            }
            if (friendCheckLists != null && friendCheckLists.size() > 0) {
                customFrndList = new CustomFrndList(getActivity(), friendCheckLists, frndEmail.getText().toString());
                frndList.setAdapter(customFrndList);
            }
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


            try {


                if (progress != null) {
                    progress = new ProgressDialog(getActivity());
                    progress.setMessage("Loading");
                    progress.setIndeterminate(true);
                    progress.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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


            try {


                if (progress.isShowing()) {
                    progress.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (check.equals("Friends")) {
                if (friends_list != null && friends_list.size() > 0) {
                    AppSettings.setNumberFriends(getActivity(), ""+friends_list.size());

                    customFrndTotalList = new CustomFrndTotalList(getActivity(), friends_list, "Friends");
                    frndList.setAdapter(customFrndTotalList);
                } else {
                    Toast.makeText(mContext, "No Friends Found", Toast.LENGTH_SHORT).show();
                    friends_list = new ArrayList<Friends>();
                    customFrndTotalList = new CustomFrndTotalList(getActivity(), friends_list, "Friends");
                    frndList.setAdapter(customFrndTotalList);
                }
            } else {
                if (friend_Req != null && friend_Req.size() > 0) {
                    AppSettings.setNumberRequest(getActivity(),""+friend_Req.size());
                    customFrndTotalList = new CustomFrndTotalList(getActivity(), friend_Req, "Request");
                    frndList.setAdapter(customFrndTotalList);
                } else {
                    Toast.makeText(mContext, "No Friends Found", Toast.LENGTH_SHORT).show();
                    friend_Req = new ArrayList<Friends>();
                    customFrndTotalList = new CustomFrndTotalList(getActivity(), friend_Req, "Request");
                    frndList.setAdapter(customFrndTotalList);

                }
            }
//            if (friends_list.size() > 0) {
//                CustomFrndTotalList customFrndList = new CustomFrndTotalList(getActivity() , friends_list, "Friends");
//                frndList.setAdapter(customFrndList);
//            }


        }
    }

    private void friendList() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("http://tracert.retroinfotech.com/frnd_request_get.php")
                .post(new FormEncodingBuilder()
                        .add("email_id", AppSettings.getEmail(getActivity())).build()).build();


        //  Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", "kishan@retroinfotech.com").add("latlong_types", "Y").add("on_date", "29-01-2016").add("on_time", "08:50:00 PM").add("latitude", "22.60000").add("longitude", "73.30000").build()).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            result = response.body().string();

            System.out.print("RESPO = " + result);
            JSONObject jsonObject = new JSONObject(result);

            friend_pending.clear();
            friend_Req.clear();
            friends_list.clear();

            if (jsonObject.has("request_sent")) {
                JSONArray jsonArray = jsonObject.getJSONArray("request_sent");
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
                        friends.setFrom_email(jsonObject1.getString("from_email"));
                    }
                    if (jsonObject1.has("to_email")) {
                        friends.setTo_email(jsonObject1.getString("to_email"));
                    }
                    if (jsonObject1.has("request_status")) {
                        friends.setRequest_status(jsonObject1.getString("request_status"));
                    }
                    friend_pending.add(friends);
                }
            }
            if (jsonObject.has("request_recv")) {
                JSONArray jsonArray = jsonObject.getJSONArray("request_recv");
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
                        friends.setFrom_email(jsonObject1.getString("from_email"));
                    }
                    if (jsonObject1.has("to_email")) {
                        friends.setTo_email(jsonObject1.getString("to_email"));
                    }
                    if (jsonObject1.has("request_status")) {
                        friends.setRequest_status(jsonObject1.getString("request_status"));
                    }

                    friend_Req.add(friends);
                }
            }

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
                        friends.setFrom_email(jsonObject1.getString("from_email"));
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

    private void friendRequest() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://tracert.retroinfotech.com/frnd_request_chk.php")
                .post(new FormEncodingBuilder()
                        .add("user_email_id", AppSettings.getEmail(getActivity()))
                        .add("user_mobile_no", AppSettings.getContactNumber(getActivity()))
                        .add("req_param", frndEmail.getText().toString()).build()).build();


        //  Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", "kishan@retroinfotech.com").add("latlong_types", "Y").add("on_date", "29-01-2016").add("on_time", "08:50:00 PM").add("latitude", "22.60000").add("longitude", "73.30000").build()).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!response.isSuccessful()) {
//                Toast.makeText(RegistrationActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();


        } else {
            try {
                result = response.body().string();

                Log.i("", "+++" + result);

                JSONObject mainObj = new JSONObject(result);


                if (mainObj.has("ack")) {

                    ack = mainObj.getBoolean("ack");
                } else {

                    if (mainObj.has("request_chk")) {

                        JSONArray jsonArray = mainObj.getJSONArray("request_chk");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            FriendCheckList friendCheckList = new FriendCheckList();
                            JSONObject jsonObjectInner = jsonArray.getJSONObject(i);

                            if (jsonObjectInner.has("first_name")) {

                                friendCheckList.setFirst_name(jsonObjectInner.getString("first_name"));
                            }
                            if (jsonObjectInner.has("last_name")) {

                                friendCheckList.setLast_name(jsonObjectInner.getString("last_name"));
                            }
                            if (jsonObjectInner.has("request_status")) {

                                friendCheckList.setRequest_status(jsonObjectInner.getString("request_status"));
                            }
                            if (jsonObjectInner.has("request_stat")) {

                                friendCheckList.setRequest_stat(jsonObjectInner.getString("request_stat"));
                            }

                            friendCheckLists.add(friendCheckList);
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
