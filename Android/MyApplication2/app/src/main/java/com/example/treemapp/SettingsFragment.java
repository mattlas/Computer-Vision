package com.example.treemapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;

import org.w3c.dom.Text;

import in.goodiebag.carouselpicker.CarouselPicker;

/**
 * Created by 5dv115 on 5/22/17.
 */

public class SettingsFragment extends Fragment {

    private static final int REQUEST_CHOOSER = 1234;
    private static final String TAG = SettingsFragment.class.getSimpleName();

    Settings settings = null;
    private String packageName;
    private ListView lv;
    private TextView tv;


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

        Button changeDirectory = (Button) view.findViewById(R.id.btn_directory_change);
        changeDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDirectory();
            }
        });

        TextView tv = (TextView) view.findViewById(R.id.directory_textview);

        tv.setText(FileLocation.getSD());


    }

    public void init(Settings settings, String packageName) {
        this.settings = settings;
        this.packageName = packageName;
    }

    /**
     * Uses aFileChooser to pick a URI
     * @return the chosen uri
     */
    private void changeDirectory() {

        Log.i(TAG, "Changing directory");
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a new directory");

        startActivityForResult(intent, REQUEST_CHOOSER);




        Intent data = new Intent();
        onActivityResult(REQUEST_CHOOSER, 0, data);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CHOOSER) {
            final Uri uri = data.getData();
            if (uri != null) {
                Log.d(TAG, "Data: "+uri.toString());
                String path = uri.getPath();
                Log.d(TAG, "Data as string: "+path);
                FileLocation.changeSDLocation(path);
                FileNotFoundDialog.clearMissingFiles();

                tv.setText(path);

            } else {
                Log.e(TAG, "Null location object - directory not changed");
            }
        }
    }



}
