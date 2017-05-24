package com.example.treemapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ipaulpro.afilechooser.utils.FileUtils;

import in.goodiebag.carouselpicker.CarouselPicker;

/**
 * Created by 5dv115 on 5/22/17.
 */

public class SettingsFragment extends Fragment {

    private static final int REQUEST_CHOOSER = 1234;

    Settings settings = null;
    private String packageName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_view, container, false);

        if (settings != null) setViews(view);

        return view;
    }

    private void setViews(View view) {

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

        Button changeDirectory = (Button) getActivity().findViewById(R.id.btn_directory_change);
        changeDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDirectory(pickDirectory());
            }
        });
        
    }

    public void init(Settings settings, String packageName) {
        this.settings = settings;
        this.packageName = packageName;
    }

    /**
     * Uses aFileChooser to pick a URI
     */
    private String pickDirectory() {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a new directory");

        startActivityForResult(intent, REQUEST_CHOOSER);

        Intent data = new Intent();

        onActivityResult(REQUEST_CHOOSER, 0, data);

        final Uri uri = data.getData();

        return FileUtils.getPath(getActivity(), uri);
    }

    /**
     * Sets the overall directory of the files to use.
     * @param directory the URI to input to FileLocation.
     */
    private void setDirectory(String directory){

    }


}
