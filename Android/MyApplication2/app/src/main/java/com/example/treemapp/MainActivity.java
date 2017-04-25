package com.example.treemapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

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

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting the image to display

        imageView = (PinView) findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.drawable.tree));

        //imageView.setImage(ImageSource.uri("/sdcard/DCIM/DSCM00123.JPG"));

        // Event handling
        initialiseEventHandling();

        // Display image in its native orientation
        imageView.setOrientation(ORIENTATION_0);

        // Filehandler
        filehandler = new FileHandler();
        initialiseTreeDataSaving();
    }


    protected String getImageData(){
        // Dummy method, replace later with real image data
        return "\"img1.png\", 23, 45, ";
    }


    public void initialiseTreeDataSaving() {
        // inputting and saving the data
        Button mShowDialog = (Button) findViewById(R.id.showInput);
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.tree_input, null);
                
                Log.d(TAG,"Tree detail input popup opened");
                // Just for debugging! it triggers a Log.d()
                filehandler.readContents();
                /*
                 * TODO GUYS! I need your help in debugging this. I need to test the read from SD card but i never have permission to edit files.
                 * Take a look at this: http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
                 * It worked for Karolina though... I hope i didnt fuck up the code :S
                 * -A
                 *
                 */


                final EditText height = (EditText) mView.findViewById(R.id.inp_height);
                final EditText diameter = (EditText) mView.findViewById(R.id.inp_diameter);
                final EditText species = (EditText) mView.findViewById(R.id.inp_species);
                Button save = (Button) mView.findViewById(R.id.btn_save);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = getImageData() + "," + height.getText() + "," + diameter.getText() + "," + species.getText();
                        filehandler.addLine(data);
                    }
                });
            }
        });
    }


    private void initialiseEventHandling() {
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

                    imageView.addPin(new Pin(sCoord));
                    imageView.invalidate();

                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Long press: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {
        return;
    }
}



