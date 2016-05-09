package com.kid.retro.com.tracerttask.Fragment;

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
import android.widget.Toast;

import com.kid.retro.com.tracerttask.Model.Friends;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;
import com.kid.retro.com.tracerttask.common.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ASUS on 27-02-2016.
 */
public class CustomFrndTotalList extends BaseAdapter {

    private Activity context;
    private ProgressDialog progress;
    private String emailIdTo;
    private String result;
    private ArrayList<Friends> friends_list;
    private String check="";



    public CustomFrndTotalList(Activity activity, ArrayList<Friends> friends_list, String request) {

        this.context = activity;
        this.friends_list = friends_list;
        this.check = request;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return friends_list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return friends_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {



        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_list_frnd, null,
                true);

        TextView title = (TextView) rowView.findViewById(R.id.title);
        Button btn_accept = (Button) rowView.findViewById(R.id.btn_accept);
        Button btn_decline = (Button) rowView.findViewById(R.id.btn_decline);



        if(check.equals("Friends"))
        {
            btn_accept.setVisibility(View.GONE);
            btn_decline.setVisibility(View.GONE);
        }
        else if(check.equals("Request"))
        {
            btn_accept.setVisibility(View.VISIBLE);
            btn_decline.setVisibility(View.VISIBLE);
        }
        title.setText(friends_list.get(position).getFirst_name());

       /* if (arrayList_appointments.get(position).getRequest_stat().equals("pending")) {

            pending.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        } else if (arrayList_appointments.get(position).getRequest_stat().equals("hasroom")) {
            pending.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
        }*/


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isOnline(context)) {
                    new Async_FrndListGet("yes",friends_list.get(position).getTo_email(),friends_list.get(position).getFrom_email(),position).execute();
                }
            }
        });
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isOnline(context)) {
                    new Async_FrndListGet("no",friends_list.get(position).getTo_email(),friends_list.get(position).getFrom_email(),position).execute();
                }
            }
        });
        return rowView;
    }


    public class Async_FrndListGet extends AsyncTask<Void, Void, Boolean> {

        String check="",to_email="",from_email="";
        int position=0;

        public Async_FrndListGet(String s, String to_email, String from_email, int position) {
            this.check = s;
            this.to_email = to_email;
            this.from_email = from_email;
            this.position = position;
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
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            boolean res= friendList(check,to_email,from_email);
            return res;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (progress.isShowing()) {
                progress.dismiss();
            }

            if(result)
            {
                if(check.equals("yes")) {
                    Toast.makeText(context, "Friend request Accepted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Friend request Declined", Toast.LENGTH_SHORT).show();
                }

                friends_list.remove(position);
                notifyDataSetChanged();
                notifyDataSetInvalidated();
            }
            else
            {
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean friendList(String check, String to_email, String from) {

        OkHttpClient client = new OkHttpClient();
//        Log.i("", "++" + AppSettings.getEmail(context));
//        Log.i("", "++" + emailIdTo);
        Request request = new Request.Builder()
                .url("http://tracert.retroinfotech.com/frnd_request_res.php")
                .post(new FormEncodingBuilder()
                        .add("from_email_id", from)
                        .add("to_email_id", to_email)
                        .add("request_res",check)
                        .build()).build();
        //  Request request = new Request.Builder().url("http://tracert.retroinfotech.com/user_location_data_api.php").post(new FormEncodingBuilder().add("email_id", "kishan@retroinfotech.com").add("latlong_types", "Y").add("on_date", "29-01-2016").add("on_time", "08:50:00 PM").add("latitude", "22.60000").add("longitude", "73.30000").build()).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
            if(result!=null)
            {
                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.has("ack"))
                {
                    if(jsonObject.getBoolean("ack"))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            Log.i("","+++"+result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
