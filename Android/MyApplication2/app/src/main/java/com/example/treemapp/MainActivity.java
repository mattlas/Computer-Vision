package com.example.treemapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import java.io.File;


import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;


public class MainActivity extends Activity implements View.OnClickListener {

    private Overlay overlay;
    private Vibrator vibrator;

    public FileHandler filehandler;
    private ImageInfoListHandler imageInfoListHandler;
    private PinView imageView;
    private String folderName = Environment.getExternalStorageDirectory() + "/mosaic/";

    private static final int PERMISSION_REQUEST_CODE = 1;
    static final String TAG = MainActivity.class.getSimpleName();
    private Pin dragPin = null;
    public static PointF latestTouch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The activity to create the input
        overlay = new Overlay(this, (RelativeLayout) findViewById(R.id.RelativeLayout_Overlayed), (RelativeLayout) findViewById(R.id.Perspective_overlay),
                 (LinearLayout) findViewById(R.id.inp_fake_layer), (LinearLayout) findViewById(R.id.inp_fake_layer_2));

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 && !checkPermission()) {
            Log.d(TAG, "I doesn't have permission");
            requestPermission(); // Code for permission
            Log.d(TAG, "I do have permission");
        }

        filehandler = new FileHandler();

        // Setting the image to display
        imageViewSetUp();

        // Event handling
        initialiseEventHandling();
    }

    private void imageViewSetUp() {
        imageView = (PinView) findViewById(R.id.imageView);

        imageView.setMaxScale(7f);
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

        // Filehandler - needs permission before starting
        imageView.setFileHandler(filehandler);
        imageView.loadPinsFromFile(imageInfoListHandler);
    }

    public float[] updateOrigPositionInPin(Pin pin) {
        ImageInfo ii = imageInfoListHandler.findImageInfo(pin.getImageFileName());

        float[] origCoor = {pin.getX(), pin.getY()};

        if (ii != null) {
            float[] resultCoor = imageInfoListHandler.getResultCoordinates(pin.getX(), pin.getY());
            origCoor = ii.convertFromIdentityCoordinatesToOriginal(resultCoor[0], resultCoor[1]);
        }
        else {
            Log.e(TAG, "No imageList file or no file to match pin's filename: '" + pin.getImageFileName() + "'");
        }
        pin.setOrigCoor(origCoor[0], origCoor[1]);

        //return is only so its easier to do unit test
        return origCoor;
    }

    private void initialiseEventHandling() {
        final GestureDetector gestureDetector = new GestureDetector(this, new SuperGestureDetector(this));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (imageView.isReady()) {
                    if (dragPin != null) {
                        latestTouch = imageView.viewToSourceCoord(motionEvent.getX(), motionEvent.getY());
                        dragPin.setPosition(latestTouch);

                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            dragPinRelease();
                        }

                        imageView.invalidate();
                    }
                }
                return gestureDetector.onTouchEvent(motionEvent);
            }

            private void dragPinRelease() {
                updateOrigPositionInPin(dragPin);
                imageView.updatePinInFile(dragPin);

                dragPin.setDragged(false);
                dragPin = null;

                imageView.setPanEnabled(true);
                imageView.setZoomEnabled(true);
                imageView.invalidate();
            }
        });
    }

    /* adding the pin to the file and pin list and shows the menu for the pin*/
    void makePin(MotionEvent e) {
        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

        ImageInfo ii = imageInfoListHandler.findImageClosestTo(sCoord.x, sCoord.y);
        String filename = ii.getFileName();

        float[] resultCoor = imageInfoListHandler.getResultCoordinates(sCoord.x, sCoord.y);

        float[] origCoor = ii.convertFromIdentityCoordinatesToOriginal(resultCoor[0], resultCoor[1]);

        Pin pin = new Pin(sCoord, new PointF(origCoor[0], origCoor[1]), filename);

        imageView.addPin(pin);
        overlay.create(pin);

        imageView.invalidate();
    }

    /**
     * Goes to the StatisticsActivity
     */
    public void sendStats(View view){
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(intent);
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

    public void setUpDragPin(MotionEvent e) {
        PointF p = imageView.viewToSourceCoord(e.getX(), e.getY());

        if (!imageView.listIsEmpty()) {

            dragPin = imageView.getClosestPin(e.getX(), e.getY());

            if (imageView.euclidanViewDistance(dragPin, e.getX(), e.getY()) < dragPin.getCollisionRadius()) {

                dragPin.setDragged(true);
                imageView.setZoomEnabled(false);
                vibrator.vibrate(100);

                /* When you set panEnabled to false, Dave Morrisey (who wrote the image view code).
                * decided that you want to center the image aswell, so we will transform it back */
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

    public PinView getImageView() {
        return imageView;
    }

    public ImageInfoListHandler getImageInfoListHandler() {
        return imageInfoListHandler;
    }

    public Overlay getOverlay() {
        return overlay;
    }
}



