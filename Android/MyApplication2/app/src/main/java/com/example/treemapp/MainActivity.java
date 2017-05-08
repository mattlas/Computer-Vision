package com.example.treemapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;


import android.os.Vibrator;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import android.widget.Toast;

import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.File;


import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;


public class MainActivity extends Activity implements View.OnClickListener {

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Vibrator v;


    public FileHandler filehandler;
    private ImageInfoListHandler imageInfoListHandler;
    private PinView imageView;
    private String folderName = Environment.getExternalStorageDirectory() + "/mosaic/";

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Pin dragPin = null;
    public static PointF latestTouch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());*/

        v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);


        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device here if we need any
                Log.d(TAG, "I have permission");
            } else {
                Log.d(TAG, "I doesn't have permission");
                requestPermission(); // Code for permission
                Log.d(TAG, "I do have permission");
            }
        }
        else
        {
            // Code for Below 23 API Oriented Device if we need any
        }

        // Setting the image to display
        imageView = (PinView) findViewById(R.id.imageView);

        imageView.setMaxScale(8f);
        imageView.setOrientation(ORIENTATION_0);

        imageInfoListHandler = new ImageInfoListHandler();

        String path = folderName + "mosaic.jpg";
        File file = new File(path);

        if (file.exists()) {
            imageView.setImage(ImageSource.uri(path));
        }
        else  { //if it is a png
            path = folderName + "mosaic.png";
            file = new File(path);

            if (file.exists()) {
                Log.d(TAG, "Found png");
                imageView.setImage(ImageSource.uri(path));
            }
            else imageView.setImage(ImageSource.resource(R.drawable.tree)); //default if we can't find mosaic
        }

        // Event handling
        initialiseEventHandling();

        // Filehandler - needs permission before starting
        filehandler = new FileHandler();
        imageView.setFileHandler(filehandler);
        imageView.loadPinsFromFile();
    }


    //opens up the perspective for a certain point
    public void perspectiveViewPopUp(double x, double y) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.perspective, null);

        Log.d(TAG,"Perspective popup opened");

        final ImageView perspective1 = (ImageView) mView.findViewById(R.id.Perspective1);

        ImageInfo im = imageInfoListHandler.findImageClosestTo(x, y);

        String fileLocation = imageInfoListHandler.getImageFileName(im);
        imageInfoListHandler.loadNeighboringImages(im);


        File f = new File(fileLocation);

        if (f.exists()) {
            try {
                Bitmap bmp = BitmapFactory.decodeFile(fileLocation);
                Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, 200, 200, true);
                perspective1.setImageBitmap(bmp2);
            }
            catch (Exception e) {
                Log.e(TAG, "could not set image to imageview", e);
            }
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "could not find file at:\'" +
                    fileLocation + "'", Toast.LENGTH_LONG);
            toast.show();
        }

        final Button cancel = (Button) mView.findViewById(R.id.btn_perspective_cancel);

        // show perspectivePopUp
        mBuilder.setView(mView);
        final AlertDialog perspectivePopUp = mBuilder.create();

        perspectivePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        perspectivePopUp.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        perspectivePopUp.show();

        // when cancel clicked - don't save the info and delete the pin
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perspectivePopUp.dismiss();
            }
        });
    }


    /*New version*/
    private void popUpTreeInput(final Pin pin) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.tree_input, null);

        Log.d(TAG,"Tree detail input popup opened");

        final NumberPicker height = (NumberPicker) mView.findViewById(R.id.inp_height);
        final NumberPicker diameter = (NumberPicker) mView.findViewById(R.id.inp_diameter);
        final Spinner species = (Spinner) mView.findViewById(R.id.inp_species);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.trees_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (species == null){
            Log.e(TAG,"Species object is null");
        }
        else {
            Log.d(TAG,"Species object exists");
        }

        species.setAdapter(adapter);
        Button save = (Button) mView.findViewById(R.id.btn_save);
        Button delete = (Button) mView.findViewById(R.id.btn_cancel);
        Button preview = (Button) mView.findViewById(R.id.btn_preview_original);
        ImageButton perspectiveButton1 = (ImageButton) mView.findViewById(R.id.btn_perspective_1);
        ImageButton perspectiveButton2 = (ImageButton) mView.findViewById(R.id.btn_perspective_2);
        ImageButton perspectiveButton3 = (ImageButton) mView.findViewById(R.id.btn_perspective_3);
        ImageButton perspectiveButton4 = (ImageButton) mView.findViewById(R.id.btn_perspective_4);


        // show dialog
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


        dialog.show();

        // when save clicked - save info to the file and to the pin list
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageView.saveNewPin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), species.getSelectedItem().toString()))
                    Toast.makeText(getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                else
                   Toast.makeText(getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // when delete clicked - don't save the info and delete the pin
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.removePinFromList(pin);
                dialog.dismiss();
                imageView.invalidate();
            }
        });

        dialog.setCanceledOnTouchOutside(false);

        // when preview clicked - open preview activity
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(pin);
            }
        });

        perspectiveButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                // Open new activity
                Intent intent = new Intent(MainActivity.this, PerspectiveButtonActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        perspectiveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                // Open new activity
                Intent intent = new Intent(MainActivity.this, PerspectiveButtonActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });


        perspectiveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                // Open new activity
                Intent intent = new Intent(MainActivity.this, PerspectiveButtonActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        perspectiveButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                // Open new activity
                Intent intent = new Intent(MainActivity.this, PerspectiveButtonActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });
    }

    /* editting the tree entry */
    private void popUpTreeEdit(final Pin pin) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.tree_input, null);

        Log.d(TAG,"Tree detail input popup opened");

        final NumberPicker height = (NumberPicker) mView.findViewById(R.id.inp_height);
        final NumberPicker diameter = (NumberPicker) mView.findViewById(R.id.inp_diameter);
        final Spinner species = (Spinner) findViewById(R.id.inp_species);
        Button save = (Button) mView.findViewById(R.id.btn_save);
        Button delete = (Button) mView.findViewById(R.id.btn_cancel);
        Button preview = (Button) mView.findViewById(R.id.btn_preview_original);


        /*if (!pin.getHeight().isEmpty()) height.setText(pin.getHeight());
        else height.setHint("Height");

        if (!pin.getDiameter().isEmpty()) diameter.setText(pin.getDiameter());
        else diameter.setHint("Diameter");

        if (!pin.getSpecies().isEmpty()) species.setText(pin.getSpecies());
        else species.setHint("Species");*/

        // show dialog
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        // when save clicked - save info to the pin list, change the line in the file
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView.updatePin(pin, Integer.toString(height.getValue()), Integer.toString(diameter.getValue()), species.getSelectedItem().toString()))
                    Toast.makeText(getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // when delete clicked - don't save the info and delete the pin
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.deletePin(pin);
                dialog.dismiss();
            }
        });

        // when preview clicked - open preview activity
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(pin);
            }
        });
    }

    /* launching the original image and preview activity*/
    private void launchActivity(Pin pin) {
        Intent intent = new Intent(this, OriginalImageActivity.class);
        //x and y are mosaic coordinates, we want mosaic-coordinates
        PointF mosaicCoor = pin.getPoint();

        if (imageInfoListHandler.didFindEverything()) {
            ImageInfo ii = imageInfoListHandler.findImageClosestTo(mosaicCoor.x, mosaicCoor.y);


            float[] resultCoor = imageInfoListHandler.getResultCoordinates(mosaicCoor.x, mosaicCoor.y);

            float[] origCoor = ii.convertFromMosaicCoordinateToOriginal(resultCoor[0], resultCoor[1]);

            float x = origCoor[0];
            float y = origCoor[1];

            intent.putExtra("x", x);
            intent.putExtra("y", y);
            intent.putExtra("mx", mosaicCoor.x);
            intent.putExtra("my", mosaicCoor.y);
            intent.putExtra("rx", resultCoor[0]);
            intent.putExtra("ry", resultCoor[1]);
            intent.putExtra("fileName", imageInfoListHandler.getImageFileName(ii));
        }
        else {
            intent.putExtra("fileName", "noImageFound.JPG");
            Toast toast = Toast.makeText(getApplicationContext(), "ImageInfoListHandler could not find the image", Toast.LENGTH_LONG);
            toast.show();
        }
        startActivity(intent);
    }

    /* getting the coordinates of the pin on the original image*/
    private int [] getOriginalImageCo(Pin pin){
        int [] coordinates = {1,1};
        return coordinates;
    }

    /*TODO: comment needed*/
    private void initialiseEventHandling() {
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (imageView.isReady()) {
                    // Tapped position
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

                    // If there is no pins we are definitely creating a new one
                    if (imageView.listIsEmpty()) {
                        makePin(e); //makes pin, creates menu
                    } else {
                        // Closest pin to tapped position
                        Pin closestPin = imageView.getClosestPin(e.getX(), e.getY());

                        // If tabbed position is inside collision radius of a pin -> edit this pin
                        if (imageView.euclidanViewDistance(closestPin, e.getX(), e.getY()) < closestPin.getCollisionRadius()){
                            // User should get notification!!!

                            popUpTreeEdit(closestPin);
                            imageView.invalidate();
                            // otherwise make new pin
                        } else {
                            //If the user's presses not near an existing pin we make a new one
                            makePin(e);
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (imageView.isReady()) {
                    setUpDragPin(e);
                } else {
                    Toast.makeText(getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (imageView.isReady()) {
                    if (dragPin != null) {
                        latestTouch = imageView.viewToSourceCoord(motionEvent.getX(), motionEvent.getY());
                        dragPin.setPosition(latestTouch);

                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            imageView.updatePinInFile(dragPin);
                            dragPin.setDragged(false);
                            dragPin = null;
                            imageView.setPanEnabled(true);
                            imageView.setZoomEnabled(true);
                            imageView.invalidate();
                        }

                        imageView.invalidate();
                    }
                }
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    /* adding the pin to the file and pin list*/
    private void makePin(MotionEvent e) {
        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
        String filename = imageInfoListHandler.findImageClosestTo(sCoord.x,sCoord.y).getFileName();

        Pin pin = new Pin(sCoord, filename);

        imageView.addPin(pin);
        popUpTreeInput(pin);
        imageView.invalidate();
    }


    /* function for dragging the pin*/
    private void setUpDragPin(MotionEvent e) {
        PointF p = imageView.viewToSourceCoord(e.getX(), e.getY());
        Toast t = Toast.makeText(getApplicationContext(),"Screen coordinates: " + Double.toString(e.getX()) +", " +  Double.toString(e.getY()) +
                "\nMosaic Coordinates: " + Double.toString(p.x) + ", " + Double.toString(p.y) + imageInfoListHandler.getImageFileName(imageInfoListHandler.findImageClosestTo(p.x, p.y)), Toast.LENGTH_LONG);
        t.show();

        if (!imageView.listIsEmpty()) {

            dragPin = imageView.getClosestPin(e.getX(), e.getY());

            if (imageView.euclidanViewDistance(dragPin, e.getX(), e.getY()) < dragPin.getCollisionRadius()) {

                dragPin.setDragged(true);
                imageView.setZoomEnabled(false);
                v.vibrate(100);

                /* When you set panEnabled to false, Dave Morrisey (who wrote the image view code).
                * deciced that you want to center the image aswell, so we will transform it back */
                float scale = imageView.getScale();
                PointF center = imageView.getCenter();

                imageView.setPanEnabled(false);
                imageView.setScaleAndCenter(scale, center);

                imageView.invalidate();
            }
            else {
                dragPin = null;
            }
        }
        else dragPin = null;
    }


    /**
     * Checks if the app has permission to read and write from external storage. Required for Android 5 and up with requestPermission()
     * @return true if app has permission, false if not
     */
    private boolean checkPermission(){

        int result= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(getApplicationContext(), "Write External Storage permission allows us to store the tree data. Please allow this permission in App Settings", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i(TAG,"Permission Granted");
                } else {
                    Log.w(TAG,"Permission Denied, no access to local drive");
                }
        }
    }

    @Override
    public void onClick(View v) {
        return;
    }

}



