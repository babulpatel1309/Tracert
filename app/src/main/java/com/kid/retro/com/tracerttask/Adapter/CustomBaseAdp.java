package com.kid.retro.com.tracerttask.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.Model.ModelContacts;
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
 * Created by root on 25/3/16.
 */
public class CustomBaseAdp extends BaseAdapter implements Filterable {


    Context context;
    ArrayList<ModelContacts> arrayList;
    ArrayList<ModelContacts> arrayList_f;
    private boolean ackFrnd = false;
    private ProgressDialog progress;
    private String resultF;
    private boolean ack = false;
    private String frnd_state;
    private String req_emai;
    private String req_number;
    private String result;


    public CustomBaseAdp(Context mainActivity, ArrayList<ModelContacts> alContacts) {
        this.arrayList = alContacts;
        this.context = mainActivity;
        this.arrayList_f= alContacts;

    }

    @Override
    public int getCount() {
        return arrayList_f.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList_f.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayList_f.indexOf(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.raw_single_contact, null);

        final Holder holder = new Holder();
        holder.txtContact = (TextView) convertView.findViewById(R.id.txtContact);
        holder.btnInvite = (Button) convertView.findViewById(R.id.btnInvite);


        holder.txtContact.setText(arrayList_f.get(position).getContactName());
        holder.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isOnline(context)) {

                    //mProgressDialog.show();
                    new Async_RegistrationTask(arrayList_f.get(position).getContactNumber(), holder.btnInvite).execute();

                } else {
                    Common.displayToast(context, "Please check internet connectivity");
                }
            }
        });

        return convertView;
    }


    private class Holder {
        TextView txtContact;
        Button btnInvite;

    }

    public class Async_RegistrationTask extends AsyncTask<Void, Void, Void> {


        private String cntNumber;
        private Button mainButton;


        public Async_RegistrationTask(String contactNumber, Button btnInvite) {
            this.cntNumber = contactNumber;
            this.mainButton = btnInvite;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub


            super.onPreExecute();

            progress = new ProgressDialog(context);
            progress.setMessage("Loading ....");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            getLocation(cntNumber);
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
            // if (i > 0) {
            //} else {

            if (ack) {

                if (frnd_state.equals("hasroom")) {


                    if (Common.isOnline(context)) {

                        //mProgressDialog.show();
                        new Async_Request(req_emai, mainButton).execute();

                    } else {
                        Common.displayToast(context, "Please check internet connectivity");
                    }
                } else if (frnd_state.equals("friends")) {


                    mainButton.setText("Friends");
                    Common.displayToast(context, "This User is already in your friend list");

                }

                else if (frnd_state.equals("pending")) {
                    Common.displayToast(context, "Request already send.");

                } else {
                    Toast.makeText(context, "This user is already in friend List", Toast.LENGTH_SHORT).show();

                }



            } else {

                //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", cntNumber, null)));
                Toast.makeText(context, "No user available", Toast.LENGTH_SHORT).show();
            }
            //}


        }
    }

    private void getLocation(String cntNumber) {

        try {

            OkHttpClient client = new OkHttpClient();


            Log.i("++number","+++"+AppSettings.getContactNumber(context));
            Log.i("++email","+++"+AppSettings.getEmail(context));
            Log.i("++cntNumber","+++"+cntNumber);


            String cont = cntNumber.replace(" ","");

            String finalMain = cont.replace("+91","");

            Request request = new Request.Builder().url("http://tracert.retroinfotech.com/frnd_request_chk.php").post(new FormEncodingBuilder().add("req_param", finalMain).add("user_email_id", AppSettings.getEmail(context)).add("user_mobile_no", AppSettings.getContactNumber(context)).build()).build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
            } else {
                resultF = response.body().string();

                Log.i("", "" + resultF);

                JSONObject jsonObject = new JSONObject(resultF);


                if (jsonObject.has("ack")) {

                    ack = jsonObject.getBoolean("ack");

                } else {
                    ack = true;

                    if (jsonObject.has("request_chk")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("request_chk");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1.has("request_stat")) {

                                frnd_state = jsonObject1.getString("request_stat");
                            }

                            if (frnd_state.equals("hasroom")) {

                                if (jsonObject1.has("req_email_id")) {
                                    req_emai = jsonObject1.getString("req_email_id");
                                }

                                if (jsonObject1.has("req_mobile_no")) {
                                    req_number = jsonObject1.getString("req_mobile_no");
                                }
                            }
                        }

                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Async_Request extends AsyncTask<Void, Void, Void> {


        private String cntNumber;
        private Button reqButton;

        public Async_Request(String contactNumber, Button mainButton) {
            this.cntNumber = contactNumber;
            this.reqButton = mainButton;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub


            super.onPreExecute();

            progress = new ProgressDialog(context);
            progress.setMessage("Loading ....");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            getLocationFrnd(cntNumber);
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

            if (ackFrnd) {
                reqButton.setText("Pending");
                Toast.makeText(context, "Friend request send successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocationFrnd(String cntNumber) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://tracert.retroinfotech.com/frnd_request.php")
                .post(new FormEncodingBuilder()
                        .add("from_email_id", AppSettings.getEmail(context))
                        .add("to_email_id", cntNumber).build()).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!response.isSuccessful()) {

        } else {
            try {
                result = response.body().string();

                Log.i("",""+result);

                JSONObject mainObj = new JSONObject(result);

                if (mainObj.has("multicast_id")) {
                    ackFrnd = true;
                } else {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ValueFilter valueFilter;

    @Override
    public Filter getFilter() {
        if(valueFilter==null) {
            valueFilter=new ValueFilter();
        }

        return valueFilter;
    }
    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<ModelContacts> filterList=new ArrayList<ModelContacts>();
                for(int i=0;i<arrayList.size();i++){
                    if((arrayList.get(i).getContactName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        ModelContacts contacts = new ModelContacts();
                        contacts.setContactName(arrayList.get(i).getContactName());
                        contacts.setContactNumber(arrayList.get(i).getContactNumber());
                        filterList.add(contacts);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=arrayList.size();
                results.values=arrayList;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            arrayList_f=(ArrayList<ModelContacts>) results.values;
            notifyDataSetChanged();
        }
    }
}
