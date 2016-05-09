package com.kid.retro.com.tracerttask.Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kid.retro.com.tracerttask.Model.FriendCheckList;
import com.kid.retro.com.tracerttask.Model.Friends;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class CustomFrndTotalList extends BaseAdapter {
    private Activity context;
    private ArrayList<FriendCheckList> arrayList_appointments;
    private ProgressDialog progress;
    private String emailIdTo;
    private String result;



    public CustomFrndTotalList(Activity activity, ArrayList<Friends> friends_list) {

        context = activity;
        this.arrayList_appointments = arrayList_appointments;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList_appointments.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayList_appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.drawer_list_inner, null,
                true);

        TextView title = (TextView) rowView.findViewById(R.id.title);
        Button add = (Button) rowView.findViewById(R.id.add);
        Button pending = (Button) rowView.findViewById(R.id.pending);

        title.setText(arrayList_appointments.get(position).getFirst_name());


       /* if (arrayList_appointments.get(position).getRequest_stat().equals("pending")) {

            pending.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        } else if (arrayList_appointments.get(position).getRequest_stat().equals("hasroom")) {
            pending.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
        }*/


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isOnline(context)) {
                    new Async_FrndListGet().execute();
                }
            }
        });
        return rowView;
    }


    public class Async_FrndListGet extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progress = new ProgressDialog(context);
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


        }
    }

    private void friendList() {

        OkHttpClient client = new OkHttpClient();


        Log.i("","++"+AppSettings.getEmail(context));
        Log.i("","++"+emailIdTo);
        Request request = new Request.Builder().url("http://tracert.retroinfotech.com/frnd_request.php").post(new FormEncodingBuilder().add("from_email_id", AppSettings.getEmail(context)).add("to_email_id", emailIdTo).build()).build();


        //  Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", "kishan@retroinfotech.com").add("latlong_types", "Y").add("on_date", "29-01-2016").add("on_time", "08:50:00 PM").add("latitude", "22.60000").add("longitude", "73.30000").build()).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            result = response.body().string();

            Log.i("","+++"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}