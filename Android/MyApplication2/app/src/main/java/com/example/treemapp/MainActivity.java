package com.example.treemapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;

public class MainActivity extends Activity implements View.OnClickListener {

    public FileHandler filehandler;
    private SubsamplingScaleImageView imageView;
    public Mark mark;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting the image to display
        imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.drawable.tree));
        //imageView.setImage(ImageSource.uri("/sdcard/DCIM/DSCM00123.JPG"));

        // Event handling
        initialiseEventHandling();

        // Display image in its native orientation
        imageView.setOrientation(ORIENTATION_0);

        // Filehandler
        filehandler = new FileHandler();
        createFileHandler();




        /*LinearLayout mLinearLayout;

        super.onCreate(savedInstanceState);

        // Create a LinearLayout in which to add the ImageView
        mLinearLayout = new LinearLayout(this);

        // Instantiate an ImageView and define its properties
        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.pin);
        i.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
        i.setLayoutParams(new Gallery.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Add the ImageView to the layout and set the layout as the content view
        mLinearLayout.addView(i);
        setContentView(mLinearLayout);*/
    }


    protected String getImageData(){
        // Dummy method, replace later with real image data
        return "\"img1.png\", 23, 45, ";
    }


    public void createFileHandler() {
        // inputting and saving the data
        Button mShowDialog = (Button) findViewById(R.id.showInput);
        mShowDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.tree_input, null);

                final EditText height = (EditText) mView.findViewById(R.id.inp_height);
                final EditText diameter = (EditText) mView.findViewById(R.id.inp_diameter);
                final EditText species = (EditText) mView.findViewById(R.id.inp_species);
                Button save = (Button) mView.findViewById(R.id.btn_save);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = getImageData() + height.getText() + "," + diameter.getText() + "," + species.getText() + "\n";
                        if(filehandler.addLine(data))
                            Toast.makeText(getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Failed to save the data.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
                    Toast.makeText(getApplicationContext(), "Single tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();

                    // Mark a tree
                    /*Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                    Paint paint = new Paint();
                    Canvas canvas = new Canvas(b);

                    canvas.drawCircle((int)sCoord.x, (int)sCoord.y, 5, paint);
                    imageView.onDrawForeground(canvas);*/

                    //mark = new Mark(getApplicationContext());
                    //mark.setPin(sCoord);


                    // Pop up menu


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



