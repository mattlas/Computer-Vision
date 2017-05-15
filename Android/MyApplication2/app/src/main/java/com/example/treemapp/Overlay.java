package com.example.treemapp;

import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import java.io.File;
import java.util.List;

public class Overlay {
    private final MainActivity mainActivity;

    private final RelativeLayout overlayedActivity;
    private final LinearLayout fakeView;

    private final RelativeLayout perspectiveOverlay;
    private final LinearLayout fakeView2;

    public Overlay(final MainActivity mainActivity, final RelativeLayout overlayedActivity, final RelativeLayout perspectiveOverlay, final LinearLayout fakeView, final LinearLayout fakeView2) {

        this.mainActivity = mainActivity;
        this.overlayedActivity = overlayedActivity;
        this.perspectiveOverlay = perspectiveOverlay;
        this.fakeView = fakeView;
        this.fakeView2 = fakeView2;

        this.fakeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return inputIsVisible();
            }
        });

        this.fakeView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (perspectiveOverlay.getVisibility() == View.VISIBLE);
            }
        });
    }

    /*Checks if the input is currently invisble*/
    public boolean inputIsVisible(){
        return overlayedActivity.getVisibility() == View.VISIBLE;
    }

    /**
     * Opens the input for pin data entry. Basically just an invisible view that becomes visible.
     *
     * @param pin the tree/pin to add
     */
    public void create(final Pin pin) {
        overlayedActivity.setVisibility(View.VISIBLE);


        final String fileName = pin.getImageFileName();
        final List<String> neighbors = mainActivity.getImageInfoListHandler().loadNeighboringImages(fileName);
        
        /*Components*/
        final Button exitBtn = (Button) mainActivity.findViewById(R.id.btn_Exit);
        final NumberPicker height = (NumberPicker) mainActivity.findViewById(R.id.inp_height);
        final NumberPicker diameter = (NumberPicker) mainActivity.findViewById(R.id.inp_diameter);
        final Spinner species = (Spinner) mainActivity.findViewById(R.id.inp_species);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mainActivity,
                R.array.trees_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner

        TextView tv = (TextView) mainActivity.findViewById(R.id.overlay_box_txt);
        tv.setText("Add tree");

        Button perspExitBtn = (Button) mainActivity.findViewById(R.id.btn_perspective_cancel);

        species.setAdapter(adapter);

        Button save = (Button) mainActivity.findViewById(R.id.btn_save);
        Button delete = (Button) mainActivity.findViewById(R.id.btn_cancel);

        // chooose the photos for the buttons (diffrent perspectives)
        int[] btns = {R.id.btn_perspective_1, R.id.btn_perspective_2, R.id.btn_perspective_3, R.id.btn_perspective_4};
        ImageButton[] imgBtns = new ImageButton[4];
        for (int i = 0; i < 4; i++) {
            imgBtns[i] = (ImageButton) mainActivity.findViewById(btns[i]);

            final int finalIndex = i;

            if (neighbors.size() > i) {

                imgBtns[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filePath;
                        filePath = neighbors.get(finalIndex);

                        if (filePath != null) {
                            File file = new File(filePath);
                            if (file.exists()) {
                                ImageView im = (ImageView) perspectiveOverlay.findViewById(R.id.perspective_image);
                                im.setImageURI(Uri.fromFile(file));
                                perspectiveOverlay.setVisibility(View.VISIBLE);
                            }
                        } else Log.e(MainActivity.TAG, "Filename does not exist: " + filePath);
                    }
                });

                File file = new File(neighbors.get(i));
                if (file.exists()) {
                    imgBtns[i].setImageURI(Uri.fromFile(file));
                    imgBtns[i].setVisibility(ImageButton.VISIBLE);
                } else {
                    Log.e(MainActivity.TAG, "Could not find file: '" + file.toString() + "'");
                    imgBtns[i].setVisibility(ImageButton.INVISIBLE);
                }
            } else {
                imgBtns[i].setVisibility(ImageButton.INVISIBLE);
            }
        }

        // when save clicked - save info to the file and to the pin list
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.updateOrigPositionInPin(pin);
                if (mainActivity.getImageView().saveNewPin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), species.getSelectedItem().toString()))
                    Toast.makeText(mainActivity.getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mainActivity.getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                // Make overlayed view visible
                overlayedActivity.setVisibility(View.INVISIBLE);
            }
        });

        // when delete clicked - don't save the info and delete the pin
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getImageView().removePinFromList(pin);
                overlayedActivity.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        // when X is clicked - the same as delete button
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getImageView().removePinFromList(pin);
                overlayedActivity.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        perspExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perspectiveOverlay.setVisibility(View.INVISIBLE);
                //imageView.invalidate();
            }
        });
    }

    /**
     * Really similiar to create but gets the information from an old pin, allowing the user
     * to edit the pin and in so editing the output file
     */
    public void edit(final Pin pin) {
        overlayedActivity.setVisibility(View.VISIBLE);

        Log.d(mainActivity.TAG,"Tree detail edit overlay opened");

        //TODO, put values here

        final NumberPicker height = (NumberPicker) overlayedActivity.findViewById(R.id.inp_height);
        final NumberPicker diameter = (NumberPicker) overlayedActivity.findViewById(R.id.inp_diameter);
        final Spinner species = (Spinner) overlayedActivity.findViewById(R.id.inp_species);

        height.setValue(Integer.parseInt(pin.getHeight()));
        diameter.setValue(Integer.parseInt(pin.getDiameter()));

        TextView tv = (TextView) overlayedActivity.findViewById(R.id.overlay_box_txt);
        tv.setText("Edit tree");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mainActivity,
                R.array.trees_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        species.setAdapter(adapter);

        species.setSelection(adapter.getPosition(pin.getSpecies()));

        Button save = (Button) overlayedActivity.findViewById(R.id.btn_save);
        Button delete = (Button) overlayedActivity.findViewById(R.id.btn_cancel);

        // when save clicked - save info to the pin list, change the line in the file
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainActivity.getImageView().updatePin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), species.getSelectedItem().toString())){
                    Toast.makeText(mainActivity.getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();}
                else
                    Toast.makeText(mainActivity.getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                overlayedActivity.setVisibility(View.INVISIBLE);
            }
        });

        // when delete clicked - don't save the info and delete the pin
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getImageView().deletePin(pin);
                overlayedActivity.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });
    }
}