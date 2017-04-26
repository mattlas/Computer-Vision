package com.example.treemapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;

public class MainActivity extends Activity implements View.OnClickListener {

    public FileHandler filehandler;
    private PinView imageView;
    public Pin pin;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

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
        imageView.setImage(ImageSource.resource(R.drawable.tree));
        imageView.setMaxScale(10f);



        //imageView.setImage(ImageSource.uri("/sdcard/DCIM/DSCM00123.JPG"));




        // Event handling
        initialiseEventHandling();

        // Display image in its native orientation
        imageView.setOrientation(ORIENTATION_0);


        // Filehandler - needs permission before starting
        filehandler = new FileHandler();
    }


    protected String getImageData(){
        // Dummy method, replace later with real image data
        return "\"img1.png\", 23, 45, ";
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
        Button cancel = (Button) mView.findViewById(R.id.btn_cancel);

        // show dialog
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        // when save clicked - save info to the file and to the pin list
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin.setInputData(height.getText().toString(), diameter.getText().toString(), species.getText().toString());
                String data = getImageData() + height.getText() + "," + diameter.getText() + "," + species.getText() + "\n";
                if(filehandler.addLine(data))
                    Toast.makeText(getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                else
                   Toast.makeText(getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // when cancel clicked - don't save the info and delete the pin
        cancel.setOnClickListener(new View.OnClickListener() {
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

                    //makePin(e);

                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

                    Pin pin = new Pin("DEBUG",sCoord);
                    /*  TODO replace this with a proper id in the "addPin" method
                     *
                     */
                    imageView.addPin(pin);
                    popUpTreeInput(pin);
                    imageView.invalidate();

                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (imageView.isReady()) {
                    //PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    //Toast.makeText(getApplicationContext(), "Long press: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                    makePin(e);
                } else {
                    Toast.makeText(getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
                /*if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Double tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Double tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;*/
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    private void makePin(MotionEvent e) {
        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

        imageView.addPin(new Pin(sCoord));
        imageView.invalidate();
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



