package com.kid.retro.com.tracerttask.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.R;

/**
 * Created by ASUS on 28-04-2016.
 */
public class AboutTrracertFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.about_trracert, container, false);

        return rootview;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            MainActivity.changeTitle("About Us");
    }
}
