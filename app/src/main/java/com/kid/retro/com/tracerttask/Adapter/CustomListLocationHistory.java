package com.kid.retro.com.tracerttask.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kid.retro.com.tracerttask.Model.GetAllUser;
import com.kid.retro.com.tracerttask.Model.LocationHistory;
import com.kid.retro.com.tracerttask.R;

import java.util.ArrayList;

public class CustomListLocationHistory extends BaseAdapter {
    private Activity context;
    private ArrayList<GetAllUser> arrayList_appointments;

    public CustomListLocationHistory(Activity activity,
                                     ArrayList<GetAllUser> arrayList_appointments) {
        // TODO Auto-generated constructor stub
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
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_locationrow, null,
                true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        txtTitle.setText(arrayList_appointments.get(position).getUsername());
        return rowView;
    }

}