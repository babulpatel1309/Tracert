package com.kid.retro.com.tracerttask.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.Model.GetAllUser;
import com.kid.retro.com.tracerttask.Model.ModelContacts;
import com.kid.retro.com.tracerttask.R;

import java.util.ArrayList;

/**
 * Created by root on 25/3/16.
 */
public class CustomBaseGetAllUser extends BaseAdapter {


    Context context;
    ArrayList<GetAllUser> arrayList;

    public CustomBaseGetAllUser(Context mainActivity, ArrayList<GetAllUser> alContacts) {
        this.arrayList = alContacts;
        this.context = mainActivity;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayList.indexOf(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.drawer_list_inner_frnd, null);

        Holder holder = new Holder();
        holder.txtContact = (TextView) convertView.findViewById(R.id.title);
        holder.btnInvite = (ImageView) convertView.findViewById(R.id.profilePic);
       // holder.txtContact.setText(arrayList.get(position).getContactName());
        // holder.txtContact.setText(arrayList.get(position));
        //holder.btnInvite.setText(arrayList.get(position));
       /* holder.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", arrayList.get(position).getContactNumber().toString(), null)));

                Toast.makeText(context,"Number is:"+arrayList.get(position).getContactNumber().toString(),Toast.LENGTH_LONG).show();
            }
        });*/

        holder.txtContact.setText(arrayList.get(position).getUsername());


        if(arrayList.get(position).getBitmap() != null) {
            holder.btnInvite.setImageBitmap(arrayList.get(position).getBitmap());
        }else{
            holder.btnInvite.setImageResource(R.drawable.ic_launcher);

        }
        return convertView;
    }


    private class Holder {
        TextView txtContact;
        ImageView btnInvite;
    }
}
