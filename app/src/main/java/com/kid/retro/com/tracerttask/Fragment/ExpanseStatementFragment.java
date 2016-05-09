package com.kid.retro.com.tracerttask.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kid.retro.com.tracerttask.R;

/**
 * Created by Kid on 1/8/2016.
 */
public class ExpanseStatementFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_expanse_statement, container, false);
        return rootview;
    }
}
