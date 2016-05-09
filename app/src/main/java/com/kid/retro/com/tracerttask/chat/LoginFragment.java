package com.kid.retro.com.tracerttask.chat;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.common.AppSettings;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginFragment extends Fragment {
    SharedPreferences prefs;
    EditText name, mobno;
    String uname, no;
    Button login;
    List<NameValuePair> params;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        prefs = getActivity().getSharedPreferences("Chat", 0);

        name = (EditText) view.findViewById(R.id.name);
        uname = name.getText().toString();
        mobno = (EditText) view.findViewById(R.id.mob);
        no = mobno.getText().toString();
        login = (Button) view.findViewById(R.id.log_btn);
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Registering ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        name.setText(AppSettings.getUserName(getActivity()));
        mobno.setText(AppSettings.getContactNumber(getActivity()));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                new Login().execute();
            }
        });

        new Login().execute();

        return view;
    }

    private class Login extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("email_id", AppSettings.getEmail(getActivity())));
            params.add(new BasicNameValuePair("password", AppSettings.getPassword(getActivity())));
            params.add(new BasicNameValuePair("gcm_id", AppSettings.getGcm_id(getActivity())));

            JSONObject jObj = json.getJSONFromUrl("http://tracert.retroinfotech.com/login_api.php", params);
            return jObj;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            progress.dismiss();
            try {
                String res = json.getString("msg");
                if (res.equals("success")) {
                    Fragment reg = new UserFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, reg);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}