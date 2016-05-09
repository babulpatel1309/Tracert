package com.kid.retro.com.tracerttask.chat;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kid.retro.com.tracerttask.Adapter.CustomListLocationHistory;
import com.kid.retro.com.tracerttask.Model.GetAllUser;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserFragment extends Fragment {
    ListView list;
    ArrayList<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
    Button refresh;
    List<NameValuePair> params;
    SharedPreferences prefs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        prefs = getActivity().getSharedPreferences("Chat", 0);

        list = (ListView) view.findViewById(R.id.listView);
        refresh = (Button) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();
                Fragment reg = new UserFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, reg);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        new Load().execute();
        new Async_RegistrationTask().execute();

        ((MainActivity_Chat) getActivity()).
                mToolbar.setTitle("Friends List");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity_Chat) getActivity()).
                        mToolbar.setTitle("Friends List");
            }
        },1000);
        return view;
    }

    private class Load extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("no", prefs.getString("REG_FROM","")));
            params.add(new BasicNameValuePair("email_id", AppSettings.getEmail(getActivity())));

            JSONArray jAry = json.getJSONArray("http://tracert.retroinfotech.com/get_current_location_apis.php"
                    , params);

            return jAry;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    JSONObject c = null;
                    try {
                        c = json.getJSONObject(i);
                        String name = c.getString("username");
                        String mobno = c.getString("mobile_no");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("username", name);
                        map.put("mobile_no", mobno);
                        users.add(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ListAdapter adapter = new SimpleAdapter(getActivity(), users,
                        R.layout.user_list_single,
                        new String[]{"username", "mobile_no"}, new int[]{
                        R.id.name, R.id.mob});
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Bundle args = new Bundle();
                        args.putString("mobile_no", users.get(position).get("mobile_no"));
                        Intent chat = new Intent(getActivity(), ChatActivity.class);
                        chat.putExtra("INFO", args);
                        startActivity(chat);
                    }
                });
            }

        }
    }


    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

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


            if (users.size() > 0) {
                CustomAdapter adapter1 = new CustomAdapter(getActivity(), users);
                list.setAdapter(adapter1);

            }
        }
    }

    private String result;
    ArrayList<String> AllEmails = new ArrayList<>();

    private void getAllUser() {
        AllEmails.clear();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/get_current_location_apis.php").post(new FormEncodingBuilder().add("email_id", "" + AppSettings.getEmail(getActivity())).build()).build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
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

                            JSONObject c = jsonObject;
                            try {
                                String name = c.getString("username");
                                String mobno = c.getString("mobile_no");
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("username", name);
                                map.put("mobile_no", mobno);
                                users.add(map);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (jsonObject.has("email_id")) {
                                AllEmails.add(jsonObject.getString("email_id"));
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

    private ArrayList<GetAllUser> getAllUsers_ArrayList = new ArrayList<>();


    public class CustomAdapter extends BaseAdapter {

        ArrayList<HashMap<String, String>> users = new ArrayList<>();
        Context mContext;
        private LayoutInflater inflater = null;

        public class Holder {
            TextView tv;
        }

        public CustomAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
            this.mContext = context;
            this.users = arrayList;
            inflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            convertView = inflater.inflate(R.layout.user_list_single, null);
            holder.tv = (TextView) convertView.findViewById(R.id.email);

            holder.tv.setText(users.get(position).get("username"));

            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppSettings.req_user_email = AllEmails.get(position
                    );
                    AppSettings.req_user_phone = users.get(position).get("mobile_no");

                    Bundle args = new Bundle();
                    args.putString("mobile_no", users.get(position).get("mobile_no"));
                    Intent chat = new Intent(getActivity(), ChatActivity.class);
                    chat.putExtra("INFO", args);
                    startActivity(chat);
                }
            });

            return convertView;
        }
    }

}