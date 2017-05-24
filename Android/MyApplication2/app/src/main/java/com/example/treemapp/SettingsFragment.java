package com.example.treemapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import in.goodiebag.carouselpicker.CarouselPicker;

/**
 * Created by 5dv115 on 5/22/17.
 */

public class SettingsFragment extends Fragment {

    Settings settings = null;
    private String packageName;
    private ListView lv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_view, container, false);

        if (settings != null) setViews(view);

        return view;
    }

    private void setViews(View view) {

        //lv = (ListView) view.findViewById(R.id.chb_species_list);
        //lv.setAdapter(new SetChbAdapter(view.getContext(), settings.getTreesSpeciesChb()));

        CompoundButton.OnCheckedChangeListener list = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                String tree = (String) buttonView.getTag();
                if(isChecked)
                    settings.addToChecked(tree);
                else
                    settings.removeFromChecked(tree);
            }
        };

        for(String tree:settings.getTreesSpeciesAll()){
            int id =  getResources().getIdentifier("chb_" + tree, "id", packageName);
            CheckBox cb = (CheckBox) view.findViewById(id);
            cb.setOnCheckedChangeListener(list);
        }

        for(CarouselPicker.PickerItem pi:settings.getTreesSpeciesChosen()) {
            String tree = pi.getText();
            int id =  getResources().getIdentifier("chb_" + tree, "id", packageName);
            CheckBox cb = (CheckBox) view.findViewById(id);
            cb.setChecked(true);
        }
    }

    public void init(Settings settings, String packageName) {
        this.settings = settings;
        this.packageName = packageName;
    }

}
