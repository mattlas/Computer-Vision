package com.example.treemapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;


import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;

public class Overlay extends View {
    private static final String TAG = Overlay.class.getSimpleName();
    private final MainActivity mainActivity;

    private final RelativeLayout inputOverlay;
    private final RelativeLayout inputOverlayEdit;

    private final RelativeLayout imagePickerOverlay;

    private Settings settings;

    Canvas canvas;

    // overlay = new Overlay(this, (RelativeLayout) findViewById(R.id.Tree_input_overlayed), (RelativeLayout) findViewById(R.id.Perspective_overlay),
    //(LinearLayout) findViewById(R.id.inp_fake_layer), (LinearLayout) findViewById(R.id.inp_fake_layer_2));
    public Overlay(final MainActivity mainActivity, final RelativeLayout overlayedActivity, final RelativeLayout overLayedActivityEdit, final RelativeLayout imagePickerOverlay, final LinearLayout fakeView, final LinearLayout fakeView2, final Settings settings) {
        super(mainActivity);
        this.mainActivity = mainActivity;

        this.inputOverlay = overlayedActivity;
        this.imagePickerOverlay = imagePickerOverlay;
        this.inputOverlayEdit = overLayedActivityEdit;
        this.settings = settings;

        fakeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //if this is true you can not press on the back layers, we want it to be true of any overlay is visible
                return inputOverlay.getVisibility() == View.VISIBLE || imagePickerOverlay.getVisibility() == View.VISIBLE || inputOverlayEdit.getVisibility() == View.VISIBLE;
            }
        });
    }

    // onDraw for mark of other perspectives...
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * Opens the input for pin data entry. Basically just an invisible view that becomes visible.
     * @param pin the tree/pin to add
     */
    public void initInputOverlay(final Pin pin) {
        inputOverlay.setVisibility(View.VISIBLE);

        final String fileName = pin.getImageFileName();
        final List<String> neighbors = mainActivity.getImageInfoListHandler().loadNeighboringImages(fileName);

        // Components of the input menu
        final ImageButton exitBtn = (ImageButton) mainActivity.findViewById(R.id.btn_Exit);
        final NumberPicker height = (NumberPicker) mainActivity.findViewById(R.id.inp_height);
        final NumberPicker diameter = (NumberPicker) mainActivity.findViewById(R.id.inp_diameter);
        final CarouselPicker carouselPicker = (CarouselPicker) mainActivity.findViewById(R.id.carouselPicker);
        final CheckBox deadTree = (CheckBox) mainActivity.findViewById(R.id.chb_deadtree);
        final EditText notes = (EditText) mainActivity.findViewById(R.id.notes);

        TextView tv = (TextView) mainActivity.findViewById(R.id.overlay_box_txt);
        tv.setText("Save a tree");

        notes.setText(""); //asume no note
        deadTree.setChecked(false); //assume it is not dead

        // Apply the adapter to the spinner
        final List<CarouselPicker.PickerItem> speciesList = getSpeciesList();
        final CarouselListener carouselPickerListener = setUpCarousel(carouselPicker, speciesList);

        // Buttons to save inputs according to a pin/delete a pin
        Button save = (Button) mainActivity.findViewById(R.id.btn_save);

        // When save clicked - save info to the file and to the pin list
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.updateOrigPositionInPin(pin);
                mainActivity.getImageView().addPin(pin);
                mainActivity.getImageView().invalidate();
                if (mainActivity.getImageView().saveNewPin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), carouselPickerListener.getItem(speciesList), deadTree.isChecked(), notes.getText().toString(), (float) mainActivity.getImageInfoListHandler().getScale()))
                    Toast.makeText(mainActivity.getApplicationContext(), R.string.data_saved, Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(mainActivity.getApplicationContext(), R.string.data_save_failed, Toast.LENGTH_SHORT).show();
                }
                // Make overlayed view visible
                inputOverlay.setVisibility(View.INVISIBLE);
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
    }

    /**
     * Really similiar to initInputOverlay but gets the information from an old pin, allowing the user
     * to edit the pin and in so editing the output file
     */
    public void edit(final Pin pin) {
        inputOverlayEdit.setVisibility(View.VISIBLE);

        Log.d(MainActivity.TAG, "Tree detail edit overlay opened");

        final NumberPicker height = (NumberPicker) inputOverlayEdit.findViewById(R.id.inp_height_edit);
        final NumberPicker diameter = (NumberPicker) inputOverlayEdit.findViewById(R.id.inp_diameter_edit);
        final CarouselPicker carouselPicker = (CarouselPicker) mainActivity.findViewById(R.id.carousel_picker_edit);
        final CheckBox deadTree = (CheckBox) mainActivity.findViewById(R.id.chb_deadtree_edit);
        final EditText notes = (EditText) mainActivity.findViewById(R.id.notes_edit);
        final ImageButton closeButton= (ImageButton) mainActivity.findViewById(R.id.btn_Exit_edit);

        height.setValue(Integer.parseInt(pin.getHeight()));
        diameter.setValue(Integer.parseInt(pin.getDiameter()));
        deadTree.setChecked(pin.getIsDead());
        notes.setText(pin.getNotes());

        TextView tv = (TextView) inputOverlayEdit.findViewById(R.id.overlay_box_txt_edit);
        tv.setText(getResources().getText(R.string.edit_text));


        final List<CarouselPicker.PickerItem> speciesList = getSpeciesList();
        final CarouselListener carouselPickerListener = setUpCarousel(carouselPicker, speciesList);

        // Former chosen species
        String formerSpecies = pin.getSpecies();
        // Set picker position to former chosen species
        Toast.makeText(mainActivity.getApplicationContext(), R.string.data_saved + getSpeciesPosition(formerSpecies, speciesList), Toast.LENGTH_SHORT).show();
        carouselPicker.setCurrentItem(getSpeciesPosition(formerSpecies, speciesList));

        Button save = (Button) inputOverlayEdit.findViewById(R.id.btn_save_edit);
        Button delete = (Button) inputOverlayEdit.findViewById(R.id.btn_cancel_edit);

        // when save clicked - save info to the pin list, change the line in the file
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainActivity.getImageView().updatePin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), carouselPickerListener.getItem(speciesList), deadTree.isChecked(), notes.getText().toString())) {
                    Toast.makeText(mainActivity.getApplicationContext(), R.string.data_saved , Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mainActivity.getApplicationContext(), R.string.data_save_failed, Toast.LENGTH_SHORT).show();
                inputOverlayEdit.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        // when delete clicked - don't save the info and delete the pin
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getImageView().deletePin(pin);
                inputOverlayEdit.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        // when close clicked - don't save the info
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputOverlayEdit.setVisibility(View.INVISIBLE);
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

        imagePickerOverlay.setVisibility(View.VISIBLE);

        final String fileName = pin.getImageFileName();
        final List<String> neighbors = mainActivity.getImageInfoListHandler().loadNeighboringImages(fileName);
        final Button continueBtn = (Button) mainActivity.findViewById(R.id.btn_continue_to_input);

        continueBtn.setText(getResources().getText(R.string.continue_add_tree));

        TextView tv = (TextView) mainActivity.findViewById(R.id.overlay_box_txt);
        tv.setText(getResources().getText(R.string.choose_position));

        //this converts from fileName to full path to the file
        String fullFileName = mainActivity.getImageInfoListHandler().loadImage(fileName);

        final OnePinView main = (OnePinView) mainActivity.findViewById(R.id.originalView);
        main.setZoomEnabled(true);
        main.setMaxScale(7f);
        main.setOrientation(ORIENTATION_0);

        main.setScaleAndCenter(1, main.getCenter());

        main.setOnTouchListener(new OriginalOnTouchListener(main));

        main.setPin(pin);
        if (new File(fullFileName).exists()) {
            main.setImage(ImageSource.uri(fullFileName));
        }

        main.setVisibility(View.VISIBLE);

        // Chooose the photos for the buttons (different perspectives)
        final int[] btns = {R.id.btn_perspective_1, R.id.btn_perspective_2, R.id.btn_perspective_3, R.id.btn_perspective_4};
        //ImageButton imgBtn;
        for (int i = -1; i < 3; i++) {
            final int m = i;
            final ImageButton imgBtn = (ImageButton) mainActivity.findViewById(btns[i+1]);
            imgBtn.setSelected(false);

            final String filePath;
            if (neighbors.size() > i) {
                // First ImageButton: original
                if (i == -1) {
                    filePath = fullFileName;
                    imgBtn.setSelected(true);
                    // Rest: it's neighbors
                } else {
                    filePath = neighbors.get(i);
                }

                imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filePath != null) {
                            File file = new File(filePath);
                            if (file.exists()) {
                                imageListenerCode(file, pin, filePath, main);
                                for (int k = -1; k < 3; k++) {
                                    final ImageButton imgBtn2 = (ImageButton) mainActivity.findViewById(btns[k+1]);
                                    imgBtn2.setSelected(false);
                                }
                                imgBtn.setSelected(true);
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
                imgBtn.setVisibility(ImageButton.INVISIBLE);
            }
        }

        ImageButton ib = (ImageButton) imagePickerOverlay.findViewById(R.id.btn_imagepicker_exit);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerOverlay.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float[] mosaic_xy = mainActivity.getImageInfoListHandler().
                                                    transformOrigToMosaic(pin);

                pin.setPosition(mosaic_xy[0], mosaic_xy[1]);

                imagePickerOverlay.setVisibility(View.INVISIBLE);
                initInputOverlay(pin);
            }
        });
    }

    /**
     * Brings up the image picker thingy for an existing pin
     * @param pin the pin we are editing
     */
    public void editImagePickerOverlay(final Pin pin) {
        imagePickerOverlay.setVisibility(View.VISIBLE);

        final String fileName = pin.getImageFileName();
        final List<String> neighbors = mainActivity.getImageInfoListHandler().loadNeighboringImages(fileName);
        final Button continueBtn = (Button) mainActivity.findViewById(R.id.btn_continue_to_input);

        TextView tv = (TextView) mainActivity.findViewById(R.id.overlay_box_txt);
        tv.setText(getResources().getText(R.string.edit_tree_position));

        continueBtn.setText(getResources().getText(R.string.cont_edit_tree));

        //this converts from fileName to full path to the file
        final String fullFileName = mainActivity.getImageInfoListHandler().loadImage(fileName);

        final OnePinView main = (OnePinView) mainActivity.findViewById(R.id.originalView);
        main.setZoomEnabled(true);
        main.setMaxScale(7f);
        main.setOrientation(ORIENTATION_0);

        main.setScaleAndCenter(1, main.getCenter());

        main.setOnTouchListener(new OriginalOnTouchListener(main));

        main.setPin(pin);
        main.setImage(ImageSource.uri(fullFileName));//TODO, do not set if no image
        main.setVisibility(View.VISIBLE);

        // Mosaic Coordinates
        final float[] mosaicCoord = mainActivity.getImageInfoListHandler().transformOrigToMosaic(pin);

        // Chooose the photos for the buttons (different perspectives)
        final int[] btns = {R.id.btn_perspective_1, R.id.btn_perspective_2, R.id.btn_perspective_3, R.id.btn_perspective_4};
        //ImageButton imgBtn;
        for (int i = -1; i < 3; i++) {
            final ImageButton imgBtn = (ImageButton) mainActivity.findViewById(btns[i+1]);
            imgBtn.setSelected(false);

            final String filePath;
            if (neighbors.size() > i) {
                // First ImageButton: original
                if (i == -1) {
                    filePath = fullFileName;
                    imgBtn.setSelected(true);
                    // Rest: it's neighbors
                } else {
                    filePath = neighbors.get(i);
                }

                imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filePath != null) {
                            File file = new File(filePath);
                            if (file.exists()) {
                                // If image is not the pins image -> set mark instead of pin
                                if (!filePath.equals(fullFileName)) {
                                    //main.removePin(pin);
                                    //invalidate();
                                    imageListenerCode(file, pin, filePath, main);

                                    for (int k = -1; k < 3; k++) {
                                        final ImageButton imgBtn2 = (ImageButton) mainActivity.findViewById(btns[k+1]);
                                        imgBtn2.setSelected(false);
                                    }
                                    imgBtn.setSelected(true);

                                    // If image is the pins image -> set pin
                                } else {
                                    //main.setPin(pin);
                                    imageListenerCode(file, pin, filePath, main);
                                }

                                // Change displayed image to clicked perspective
                                main.setImage(ImageSource.uri(filePath));
                                main.setVisibility(View.VISIBLE);
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
                } else {
                    Log.e(TAG, "Image button null (image may not exist)");
                }
            }
        }


        ImageButton ib = (ImageButton) imagePickerOverlay.findViewById(R.id.btn_imagepicker_exit);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add closing event
                imagePickerOverlay.setVisibility(View.INVISIBLE);
                mainActivity.getImageView().invalidate();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float[] mosaic_xy = mainActivity.getImageInfoListHandler().
                        transformOrigToMosaic(pin);

                pin.setPosition(mosaic_xy[0], mosaic_xy[1]);
                imagePickerOverlay.setVisibility(View.INVISIBLE);
                edit(pin);
            }
        });
    }

    /**
     * This code is for the neighboring images you can click on
     * @param file the file that is going to show
     * @param pin the pin involved
     * @param filePath the filePath as well (probably unneccesary)
     * @param onePinView the pinView
     */
    private void imageListenerCode(File file, Pin pin, String filePath, OnePinView onePinView) {
        float[] mos = mainActivity.getImageInfoListHandler().transformOrigToMosaic(pin);
        float[] originalCoord = mainActivity.getImageInfoListHandler().transformMosaicToOrig(mos[0], mos[1], file.getName());

        // Change coordinates of pin
        pin.setOrigCoor(originalCoord[0], originalCoord[1]);
        pin.setImageFileName(new File(filePath).getName());



        // Change displayed image to clicked perspective
        onePinView.setImage(ImageSource.uri(filePath));
        onePinView.setVisibility(View.VISIBLE);
    }

    /**
     * Getter for items in speecies list
     * @return the current species list
     */
    public final List<CarouselPicker.PickerItem> getSpeciesList() {
        List<CarouselPicker.PickerItem> treeList = settings.getTreesSpeciesChosen();
        List<CarouselPicker.PickerItem> newList = new ArrayList<>();

        for (CarouselPicker.PickerItem item : treeList){
            String text = item.getText();
            String newText;
            int id = mainActivity.getResources().getIdentifier(text, "string", mainActivity.getPackageName());
            newText = mainActivity.getResources().getString(id);
            CarouselPicker.PickerItem newItem = new CarouselPicker.TextItem(newText,settings.carouselSize);

            newList.add(newItem);
        }

        return newList;

    }

    @NonNull
    private CarouselListener setUpCarousel(CarouselPicker carouselPicker, List<CarouselPicker.PickerItem> textItems) {
        //Carouse1 Picker with text to display the tree species
        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(mainActivity, textItems, 0);
        carouselPicker.setAdapter(textAdapter);

        final CarouselListener carouselPickerListener = new CarouselListener();

        carouselPicker.addOnPageChangeListener(carouselPickerListener);
        return carouselPickerListener;
    }
}