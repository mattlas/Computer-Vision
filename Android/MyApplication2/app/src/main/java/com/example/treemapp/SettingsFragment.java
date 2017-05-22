package com.example.treemapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 5dv115 on 5/22/17.
 */

public class SettingsFragment extends Fragment {

    Settings settings = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO problem - some checkboxes under the top bar
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_view, container, false);

        if (settings != null) setViews();

        return view;
    }

    private void setViews() {
        //TODO check the objects that are on the list
    }

    public void init(Settings settings) {
        this.settings = settings;
        setViews();
    }
}
