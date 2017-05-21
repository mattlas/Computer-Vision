package com.example.treemapp;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class Overlay {
    private final MainActivity mainActivity;

    private final RelativeLayout inputOverlay;
    private final LinearLayout fakeView;

    private final RelativeLayout imagePickerOverlay;
    private final LinearLayout fakeView2;
    private PinView originalView;

    // overlay = new Overlay(this, (RelativeLayout) findViewById(R.id.Tree_input_overlayed), (RelativeLayout) findViewById(R.id.Perspective_overlay),
    //(LinearLayout) findViewById(R.id.inp_fake_layer), (LinearLayout) findViewById(R.id.inp_fake_layer_2));
    public Overlay(final MainActivity mainActivity, final RelativeLayout overlayedActivity, final RelativeLayout imagePickerOverlay, final LinearLayout fakeView, final LinearLayout fakeView2) {

        this.mainActivity = mainActivity;
        this.inputOverlay = overlayedActivity;
        this.imagePickerOverlay = imagePickerOverlay;
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
                return (imagePickerOverlay.getVisibility() == View.VISIBLE);
            }
        });
    }

    /*Checks if the input is currently invisble*/
    public boolean inputIsVisible() {
        return inputOverlay.getVisibility() == View.VISIBLE;
    }

    /**
     * Opens the input for pin data entry. Basically just an invisible view that becomes visible.
     *
     * @param pin the tree/pin to add
     */
    public void initInputOverlay(final Pin pin) {
        inputOverlay.setVisibility(View.VISIBLE);

        final String fileName = pin.getImageFileName();
        final List<String> neighbors = mainActivity.getImageInfoListHandler().loadNeighboringImages(fileName);

        // Components of the input menu
        final Button exitBtn = (Button) mainActivity.findViewById(R.id.btn_Exit);
        final NumberPicker height = (NumberPicker) mainActivity.findViewById(R.id.inp_height);
        final NumberPicker diameter = (NumberPicker) mainActivity.findViewById(R.id.inp_diameter);
        final CarouselPicker carouselPicker = (CarouselPicker) mainActivity.findViewById(R.id.carouselPicker);

        TextView tv = (TextView) mainActivity.findViewById(R.id.overlay_box_txt);
        tv.setText("Add tree");

        // Button to close the input menu
        Button perspExitBtn = (Button) mainActivity.findViewById(R.id.btn_perspective_cancel);

        // Apply the adapter to the spinner
        final List<CarouselPicker.PickerItem> speciesList = getSpeciesList();
        final CarouselListener carouselPickerListener = setUpCarousel(carouselPicker, speciesList);

        // Buttons to save inputs according to a pin/delete a pin
        Button save = (Button) mainActivity.findViewById(R.id.btn_save);
        Button delete = (Button) mainActivity.findViewById(R.id.btn_cancel);

        // When save clicked - save info to the file and to the pin list
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.updateOrigPositionInPin(pin);
                if (mainActivity.getImageView().saveNewPin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), carouselPickerListener.getItem(speciesList)))
                    Toast.makeText(mainActivity.getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mainActivity.getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                // Make overlayed view visible
                inputOverlay.setVisibility(View.INVISIBLE);
            }
        });

        // When delete clicked - don't save the info and delete the pin
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getImageView().removePinFromList(pin);
                inputOverlay.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        // when X is clicked - the same as delete button
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getImageView().removePinFromList(pin);
                inputOverlay.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        perspExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickerOverlay.setVisibility(View.INVISIBLE);
                //imageView.invalidate();
            }
        });
    }

    /**
     * Really similiar to initInputOverlay but gets the information from an old pin, allowing the user
     * to edit the pin and in so editing the output file
     */
    public void edit(final Pin pin) {
        inputOverlay.setVisibility(View.VISIBLE);

        Log.d(mainActivity.TAG, "Tree detail edit overlay opened");

        //TODO, put values here

        final NumberPicker height = (NumberPicker) inputOverlay.findViewById(R.id.inp_height);
        final NumberPicker diameter = (NumberPicker) inputOverlay.findViewById(R.id.inp_diameter);
        final CarouselPicker carouselPicker = (CarouselPicker) mainActivity.findViewById(R.id.carouselPicker);

        height.setValue(Integer.parseInt(pin.getHeight()));
        diameter.setValue(Integer.parseInt(pin.getDiameter()));

        TextView tv = (TextView) inputOverlay.findViewById(R.id.overlay_box_txt);
        tv.setText("Edit tree");


        final List<CarouselPicker.PickerItem> speciesList = getSpeciesList();
        final CarouselListener carouselPickerListener = setUpCarousel(carouselPicker, speciesList);

        // Former chosen species
        String formerSpecies = pin.getSpecies();
        // Set picker position to former chosen species
        Toast.makeText(mainActivity.getApplicationContext(), "Data saved." + getSpeciesPosition(formerSpecies, speciesList), Toast.LENGTH_SHORT).show();
        carouselPicker.setCurrentItem(getSpeciesPosition(formerSpecies, speciesList));

        Button save = (Button) inputOverlay.findViewById(R.id.btn_save);
        Button delete = (Button) inputOverlay.findViewById(R.id.btn_cancel);

        // when save clicked - save info to the pin list, change the line in the file
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainActivity.getImageView().updatePin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), carouselPickerListener.getItem(speciesList))) {
                    Toast.makeText(mainActivity.getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mainActivity.getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                inputOverlay.setVisibility(View.INVISIBLE);
            }
        });

        // when delete clicked - don't save the info and delete the pin
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getImageView().deletePin(pin);
                inputOverlay.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });
    }

    private class CarouselListener implements ViewPager.OnPageChangeListener {

        private int position;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //don't think we need this
        }

        @Override
        public void onPageSelected(int position) {
            //position of the selected item
            this.position = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //don't think we need this
        }

        public int getPosition() {
            return position;
        }

        public String getItem(List<CarouselPicker.PickerItem> species) {
            return species.get(position).getText();
        }
    }

    public int getSpeciesPosition(String species, List<CarouselPicker.PickerItem> textItems) {
        int position = 0;
        for (int i = 0; i < textItems.size(); i++) {
            if (textItems.get(i).getText().equals(species)) {
                position = i;
            }
        }
        return position;
    }

    public void initImagePickerOverlay(final Pin pin) {

        final String fileName = pin.getImageFileName();
        final List<String> neighbors = mainActivity.getImageInfoListHandler().loadNeighboringImages(fileName);

        OnePinView main = (OnePinView) mainActivity.findViewById(R.id.originalView);
        main.setImage(ImageSource.uri(pin.getImageFileName()));
        main.setVisibility(View.VISIBLE);

        // Chooose the photos for the buttons (different perspectives)
        int[] btns = {R.id.btn_perspective_1, R.id.btn_perspective_2, R.id.btn_perspective_3, R.id.btn_perspective_4};
        ImageButton imgBtn;
        for (int i = 0; i < 4; i++) {
            imgBtn = (ImageButton) mainActivity.findViewById(btns[i]);


            if (neighbors.size() > i) {
                final String filePath = neighbors.get(i);
                imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filePath != null) {
                            File file = new File(filePath);
                            if (file.exists()) {
                                ImageView im = (ImageView) imagePickerOverlay.findViewById(R.id.perspective_image);
                                im.setImageURI(Uri.fromFile(file));
                                //imagePickerOverlay.setVisibility(View.VISIBLE); do not think we need this line
                            }
                        } else Log.e(MainActivity.TAG, "Filename is null");
                    }
                });

                File file = new File(filePath);
                if (file.exists()) {
                    imgBtn.setImageURI(Uri.fromFile(file));
                    imgBtn.setVisibility(ImageButton.VISIBLE);
                } else {
                    Log.e(MainActivity.TAG, "Could not find file: '" + file.toString() + "'");
                    imgBtn.setVisibility(ImageButton.INVISIBLE);
                }

            } else {
                if (imgBtn != null) {
                    imgBtn.setVisibility(ImageButton.INVISIBLE);
                }
            }

        }

        imagePickerOverlay.setVisibility(View.VISIBLE);
    }


    public final List<CarouselPicker.PickerItem> getSpeciesList() {
        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new CarouselPicker.TextItem("Spruce", 12));
        textItems.add(new CarouselPicker.TextItem("Pine", 12));
        textItems.add(new CarouselPicker.TextItem("Birch", 12));
        textItems.add(new CarouselPicker.TextItem("Oak", 12));
        textItems.add(new CarouselPicker.TextItem("Other", 12));
        return textItems;
    }

    @NonNull
    private CarouselListener setUpCarousel(CarouselPicker carouselPicker, List<CarouselPicker.PickerItem> textItems) {
        //Carousse1 Picker with text to display the tree species

        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(mainActivity, textItems, 0);
        carouselPicker.setAdapter(textAdapter);

        final CarouselListener carouselPickerListener = new CarouselListener();

        carouselPicker.addOnPageChangeListener(carouselPickerListener);
        return carouselPickerListener;
    }
}