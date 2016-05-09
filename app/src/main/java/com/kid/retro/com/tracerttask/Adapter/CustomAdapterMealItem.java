package com.kid.retro.com.tracerttask.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kid.retro.com.tracerttask.Model.LocationHistory;
import com.kid.retro.com.tracerttask.R;

import java.util.ArrayList;

/**
 * Created by dhaval on 28/12/15.
 */
public class CustomAdapterMealItem extends BaseAdapter {
    Activity context;
    ArrayList<LocationHistory> list;

    public CustomAdapterMealItem(Activity context, ArrayList<LocationHistory> mealItemModelArrayList) {
        this.context = context;
        this.list = mealItemModelArrayList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.indexOf(i);
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.raw_view_meal_item, null);
            holder = new ViewHolder();
            holder.txtMealName = (TextView) convertView.findViewById(R.id.txtMealItemName);
            holder.txtdate = (TextView) convertView.findViewById(R.id.txtdate);
            holder.txtdate.setText(list.get(i).getOn_date().toString());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtMealName;
        TextView txtdate;
    }
}
