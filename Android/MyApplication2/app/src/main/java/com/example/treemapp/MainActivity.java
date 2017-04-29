package com.example.treemapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;


import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.io.File;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;

public class MainActivity extends Activity implements View.OnClickListener {

    public FileHandler filehandler;
    private ImageInfoListHandler imageInfoListHandler;
    private PinView imageView;
    private String folderName = Environment.getExternalStorageDirectory() + "/mosaic/";

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Pin dragPin = null;
    private PointF latestTouch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device here if we need any
            } else {
                requestPermission(); // Code for permission
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
        else imageView.setImage(ImageSource.resource(R.drawable.tree)); //default if we can't find mosaic

        // Event handling
        initialiseEventHandling();

        // Filehandler - needs permission before starting
        filehandler = new FileHandler();
        imageView.setFileHandler(filehandler);
        imageView.loadPinsFromFile();
    }


    protected String getImageData(){
        // TODO replace with real image data
        return "\"img1.png\"";
    }


    //opens up the perspective for a certain point
    private void perspectiveViewPopUp(double x, double y) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.perspective, null);

        Log.d(TAG,"Perspective popup opened");

        final ImageView perspective1 = (ImageView) mView.findViewById(R.id.Perspective1);

        ImageInfo im = imageInfoListHandler.findImageClosestTo(x, y);

        String fileLocation = imageInfoListHandler.loadImage(im);

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

        final EditText height = (EditText) mView.findViewById(R.id.inp_height);
        final EditText diameter = (EditText) mView.findViewById(R.id.inp_diameter);
        final EditText species = (EditText) mView.findViewById(R.id.inp_species);
        Button save = (Button) mView.findViewById(R.id.btn_save);
        Button delete = (Button) mView.findViewById(R.id.btn_cancel);

        // show dialog
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        // when save clicked - save info to the file and to the pin list
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin.setInputData(height.getText().toString(), diameter.getText().toString(), species.getText().toString());
                String data = pin.getCSV() + "," + getImageData() + "\n";
                if(filehandler.addLine(data))
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
    }


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
                        Pin closestPin = imageView.getClosestPin(sCoord.x, sCoord.y);

                        // Distance between closest pin and tapped position
                        double distance = closestPin.euclidianDistance(sCoord.x, sCoord.y);

                        // If tabbed position is inside collision radius of a pin -> edit this pin
                        if (distance < closestPin.getCollisionRadius()){
                            // User should get notification!!!

                            popUpTreeInput(closestPin);
                            imageView.invalidate();
                            // otherwise make new pin
                        } else {
                            //If the user's presses not near an existing pin we make a new one
                            makePin(e);
                        }
                    }

                    // TODO, talk about everything thats happening here, when exactly the pin is saved to the file!
                    // I'm trying to make it save when it adds it with addPin, but maybe you guys have other plans?

                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (imageView.isReady()) {
                    //perspectiveViewPopUp(0, 0);
                    // Drag Pin
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
                            dragPin.updatePositionInFile();
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

    private void makePin(MotionEvent e) {
        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

        Pin pin = new Pin(sCoord);

        imageView.addPin(pin);
        popUpTreeInput(pin);
        imageView.invalidate();
    }

    private void setUpDragPin(MotionEvent e) {

        if (!imageView.listIsEmpty()) {

            PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
            dragPin = imageView.getClosestPin(sCoord.x, sCoord.y);
            dragPin.setDragged(true);
            imageView.setPanEnabled(false);
            imageView.setZoomEnabled(false);

            imageView.invalidate();
        }
    }

    /**
     * Checks if the app has permission to read and write from external storage. Required for Android 5 and up with requestPermission()
     * @return true if app has permission, false if not
     */
    private boolean checkPermission(){

        int result= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }

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



