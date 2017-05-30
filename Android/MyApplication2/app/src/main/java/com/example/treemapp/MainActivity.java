package com.example.treemapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import java.io.File;
import java.util.ArrayList;


import static android.support.v4.content.FileProvider.getUriForFile;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Overlay overlay;
    private Vibrator vibrator;

    private Settings settings;

    public FileHandler filehandler;
    private ImageInfoListHandler imageInfoListHandler;
    private PinView imageView;
    private String folderName;
    private boolean foundMosaic = false;

    public boolean isMosaicIsFound() {
        return foundMosaic;
    }

    private static final int PERMISSION_REQUEST_CODE = 1;
    private float yPinOffset;
    static final String TAG = MainActivity.class.getSimpleName();
    private Pin dragPin = null;
    public static PointF latestTouch = null;

    // TODO: do we need that?
    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23 && !checkPermission()) {
            Log.d(TAG, "I don't have permission");
            requestPermission(); // Code for permission
        }
        if(checkPermission())
            Log.d(TAG, "I do have permission");


        this.yPinOffset = getResources().getDimension(R.dimen.pin_selection_offset);

        folderName = FileLocation.getFileSystemSDCardName(getApplicationContext());
        FileLocation.setPathForList(getApplicationContext());

        settings = new Settings();

        // The activity to initInputOverlay the input
        overlay = new Overlay(this, (RelativeLayout) findViewById(R.id.Tree_input_overlayed), (RelativeLayout) findViewById(R.id.Tree_input_overlayed_edit), (RelativeLayout) findViewById(R.id.Image_picker_overlayed),
                (LinearLayout) findViewById(R.id.inp_fake_layer), (LinearLayout) findViewById(R.id.inp_fake_layer_2), settings);

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        initMenu();
        imageInfoListHandler = new ImageInfoListHandler();
        filehandler = new FileHandler(this, imageInfoListHandler.getScale());

        // Setting the image to display
        imageViewSetUp();

        // Event handling
        initialiseEventHandling();
    }


    private void initMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //for lena
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void imageViewSetUp() {
        imageView = (PinView) findViewById(R.id.imageView);

        imageView.setMaxScale(7f);
        imageView.setOrientation(ORIENTATION_0);

        String path = folderName + "mosaic.jpg";
        File file = new File(path);

        if (file.exists()) {
            imageView.setImage(ImageSource.uri(path));
            foundMosaic = true;
        }
        else  { //if it is a png
            path = folderName + "mosaic.png";
            file = new File(path);

            if (file.exists()) {
                Log.d(TAG, "Found png");
                imageView.setImage(ImageSource.uri(path));
                foundMosaic = true;
            }
            else {
                Log.e(TAG,"Mosaic image file not found");
                imageView.setImage(ImageSource.resource(R.drawable.could_not_find_file)); //default if we can't find mosaic
            }
        }

        imageView.setFileHandler(filehandler);
        imageView.loadPinsFromFile(imageInfoListHandler);
    }


    /**
     * Goes to the default android share activity - share "treeList.csv"
     */
    private void export(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        File listFile = new File(this.getFilesDir(), filehandler.getFileName());
        Uri csv = getUriForFile(getApplicationContext(), "com.example.treemapp.fileprovider", listFile);
        File file = new File(csv.getPath());
        if (file.length()!=0)
            Log.d(TAG, "file to export exists");
        else
            Log.d(TAG, "file to export does NOT exist");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, csv);
        sharingIntent.setType("text/html");
        if (file.length()!=0)
            Log.d(TAG, "file to export exists");
        else
            Log.d(TAG, "file to export does NOT exist");
        startActivity(sharingIntent);
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
                        latestTouch = imageView.viewToSourceCoord(motionEvent.getX(), motionEvent.getY()+yPinOffset);
                        dragPin.setPosition(latestTouch);

                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            Pin pin = dragPin;
                            dragPinRelease();
                            editOriginals(pin);
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


    /**
     *  adding the pin to the file and pin list and shows the menu for the pin
     */
    void showOriginals(MotionEvent e) {
        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

        ImageInfo ii = imageInfoListHandler.findImageClosestTo(sCoord.x, sCoord.y);
        String filename = ii.getFileName();

        float[] origCoor = imageInfoListHandler.transformMosaicToOrig(sCoord.x, sCoord.y, ii);

        Pin pin = new Pin(sCoord, new PointF(origCoor[0], origCoor[1]), filename);

        overlay.initImagePickerOverlay(pin);

        imageView.invalidate();
    }

    /**
     *  edits the pin in the file and pin list and shows the menu for the pin
     */
    void editOriginals(Pin pin) {
        overlay.initImagePickerOverlay(pin);
        imageView.invalidate();
    }

    /**
     * Checks if the app has permission to read and write from external storage. Required for Android 5 and up with requestPermission()
     * @return true if app has permission, false if not
     */
    private boolean checkPermission(){
        int result= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * This askes the user to allow the user to use the storage
     */
    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(getApplicationContext(), R.string.write_permission_explanation, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * Sets up the information for when the user is dragging the pin
     */
    public void setUpDragPin(MotionEvent e) {
        if (!imageView.listIsEmpty()) {

            dragPin = imageView.getClosestPin(e.getX(), e.getY() + yPinOffset);
            if (imageView.euclidanViewDistance(dragPin, e.getX(), e.getY() + yPinOffset) < dragPin.getCollisionRadius()) {

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
                dragPin = null; //should never come here
            }
        }
        else dragPin = null; //should never come here
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i(TAG,"Permission Granted");
                } else {
                    Log.w(TAG,"Permission Denied, no access to local drive");
                }
        }
    }

    /**
     * Gets the information which item from the menu is selected (clicked)
     * @param item the clicked option
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            export();
        }
        else if (id == R.id.nav_statistics) {

            StatFragment sf = new StatFragment();
            sf.init(new Statista(imageView.getPins()));

            FragmentManager fm = getFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.map_fragment, sf)
                    .addToBackStack(null)
                    .commit();
        }else if (id == R.id.nav_home) {
            getFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }else if (id == R.id.nav_manage){
            startSettings();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * starts the SettingsFragment object, (opens the settings menu)
     */
    private void startSettings() {
        SettingsFragment sf = new SettingsFragment();
        sf.init(settings,filehandler, this.getPackageName());

        FragmentManager fm = getFragmentManager();

        fm.beginTransaction()
                .replace(R.id.map_fragment, sf)
                .addToBackStack(null)
                .commit();
    }

    //getters
    public PinView getImageView() {
        return imageView;
    }

    public ImageInfoListHandler getImageInfoListHandler() {
        return imageInfoListHandler;
    }

    public Overlay getOverlay() {
        return overlay;
    }

    /**
     * Deletes all pins in the active pin list. Does not remove the treeList.csv file.
     */
    public void deleteAllPinsFromMemory(){
        imageView.deleteAllPins();
    }

}



