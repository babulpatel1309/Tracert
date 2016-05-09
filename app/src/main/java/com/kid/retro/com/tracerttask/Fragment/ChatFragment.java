package com.kid.retro.com.tracerttask.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kid.retro.com.tracerttask.Adapter.CustomBaseAdp;
import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.Model.ModelContacts;
import com.kid.retro.com.tracerttask.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kid on 1/8/2016.
 */
public class ChatFragment extends Fragment {

    private ArrayList<ModelContacts> alContacts;
    private ListView listContacts;

    private CustomBaseAdp customBaseAdp;

    private ModelContacts modelContact;
    private ProgressDialog progress;
    private String resultF;
    private boolean ack = false;
    private String frnd_state;
    private String req_emai;
    private String req_number;
    private String result;
    private boolean ackFrnd = false;
    EditText contact_search;

    RelativeLayout progress_lay;
    LinearLayout contact_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_chat, container, false);

        listContacts = (ListView) rootview.findViewById(R.id.listContacts);
        contact_search = (EditText)rootview.findViewById(R.id.contact_search);
        alContacts = new ArrayList<ModelContacts>();
        progress_lay = (RelativeLayout) rootview.findViewById(R.id.progress);
        contact_view = (LinearLayout) rootview.findViewById(R.id.contact_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                getContactList();

                new GetContacts().execute();

            }
        }, 500);

        contact_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(customBaseAdp!=null && alContacts.size()>0){
                    if(s.toString().trim().length()>0){
                        customBaseAdp.getFilter().filter(s);
                    }else{
                        customBaseAdp = new CustomBaseAdp(getActivity(), alContacts);
                        listContacts.setAdapter(customBaseAdp);
                        customBaseAdp.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootview;
    }


    public class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getContactList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(visible){
                if (alContacts.size() > 0) {
                    Collections.sort(alContacts, new Comparator<ModelContacts>() {
                        @Override
                        public int compare(ModelContacts s1, ModelContacts s2) {
                            return s1.getContactName().compareToIgnoreCase(s2.getContactName());
                        }
                    });

                    customBaseAdp = new CustomBaseAdp(getActivity(), alContacts);
                    listContacts.setAdapter(customBaseAdp);
                }


                listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });

                progress_lay
                        .setVisibility(View.GONE);
                contact_view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getContactList() {
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            alContacts = new ArrayList<ModelContacts>();
            do {
                modelContact = new ModelContacts();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        modelContact.setContactNumber(contactNumber);
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        modelContact.setContactName(contactName);
                        alContacts.add(modelContact);
                        break;
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }
    }

    boolean visible=true;
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible){
            visible=true;
            MainActivity.changeTitle("Contacts");
        }else{
            visible=false;
        }
    }

}
