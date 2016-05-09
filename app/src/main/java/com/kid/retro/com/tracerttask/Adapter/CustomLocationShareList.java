package com.kid.retro.com.tracerttask.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.Model.Friends;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class CustomLocationShareList extends BaseAdapter {
    private Activity context;
    private ArrayList<Friends> arrayList_appointments;

    private String emailIdTo;
    private String result;

    private Button add;
    private Button pending;

    private String lat = "";
    private String longi = "";

    public CustomLocationShareList(Activity activity,
                                   ArrayList<Friends> arrayList_appointments, String latitude, String longitude) {
        // TODO Auto-generated constructor stub
        this.context = activity;
        this.arrayList_appointments = arrayList_appointments;
        this.lat = latitude;
        this.longi = longitude;
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
        View rowView = inflater.inflate(R.layout.list_inner_share, null,
                true);

        TextView title = (TextView) rowView.findViewById(R.id.title_main);
        add = (Button) rowView.findViewById(R.id.share);

        title.setText(arrayList_appointments.get(position).getFirst_name());


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.displayToast(context , arrayList_appointments.get(position).getFrom_email());


                if (Common.isOnline(context)) {
                    new Async_FrndListGet(arrayList_appointments.get(position).getFrom_email()).execute();
                }
            }
        });
        return rowView;
    }


    public class Async_FrndListGet extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progress;
        private String emailID;

        public Async_FrndListGet(String from_email) {
            this.emailID = from_email;
        }

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
            friendList(emailID);
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

    private void friendList(String emailID) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("http://tracert.retroinfotech.com/share_location.php").post(new FormEncodingBuilder().add("user_email_id", AppSettings.getEmail(context)).add("req_email_id", emailID).add("latitude", lat).add("longitude", longi).build()).build();

        Response response;
        try {
            response = client.newCall(request).execute();

            result = response.body().string();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}