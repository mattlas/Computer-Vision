package com.example.treemapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import in.goodiebag.carouselpicker.CarouselPicker;

/**
 * Created by 5dv115 on 5/22/17.
 */

public class SettingsFragment extends Fragment {


    private static final int REQUEST_CHOOSER = 1234;
    private static final String TAG = SettingsFragment.class.getSimpleName();
    Settings settings = null;
    public FileHandler fileHandler;
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

    /**
     * Sets the settings view, signs listeners to checkboxes
     * @param view
     */
    private void setViews(View view) {

        //lv = (ListView) view.findViewById(R.id.chb_species_list);
        //lv.setAdapter(new SetChbAdapter(view.getContext(), settings.getTreesSpeciesChb()));


        Button deleteListButton = (Button) view.findViewById(R.id.btn_delete_treelist);
        deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteListDialog();



            }
        });

        CompoundButton.OnCheckedChangeListener list = new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String tree = (String) buttonView.getTag();
                        if (isChecked)
                            settings.addToChecked(tree);
                        else
                            settings.removeFromChecked(tree);
                    }
                };

        for(String tree:settings.getTreesSpeciesAll()){
            int id =  getResources().getIdentifier("chb_" + tree.toLowerCase(), "id", packageName);

            CheckBox cb = (CheckBox) view.findViewById(id);
            if (cb != null) {
                cb.setOnCheckedChangeListener(list);
            }
            else {
                Log.e(TAG, "no checkbox found called for tag: " + "chb_" + tree.toLowerCase());
            }
        }

        for(CarouselPicker.PickerItem pi:settings.getTreesSpeciesChosen()) {
            String tree = pi.getText();
            int id =  getResources().getIdentifier("chb_" + tree, "id", packageName);
            CheckBox cb = (CheckBox) view.findViewById(id);
            cb.setChecked(true);

        }

        TextView tv = (TextView) view.findViewById(R.id.directory_textview);

        tv.setText(FileLocation.getSD());


    }

    private void deleteListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.delete_treelist_message);
        builder.setTitle(R.string.delete_treelist_title);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fileHandler.deleteAllTrees();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     * gets objects needed for init
     * @param settings the Settings instance
     * @param packageName the package name of an app
     */
    public void init(Settings settings, FileHandler fileHandler, String packageName) {
        this.settings = settings;
        this.packageName = packageName;
        this.fileHandler = fileHandler;
    }






}
