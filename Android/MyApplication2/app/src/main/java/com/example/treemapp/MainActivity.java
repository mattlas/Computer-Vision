package com.example.treemapp;


import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_0;


public class MainActivity extends AppCompatActivity {


    public FileHandler filehandler;


    protected String getImageData(){
        // Dummy method, replace later with real image data
        return "\"img1.png\", 23, 45, ";
    }

    private SubsamplingScaleImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Setting the image to display
        imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.drawable.tree));
        //imageView.setImage(ImageSource.uri("/sdcard/DCIM/DSCM00123.JPG"));

        // Display image in its native orientation
        imageView.setOrientation(ORIENTATION_0);

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Toast error = Toast.makeText(MainActivity.this, "Error 1!", Toast.LENGTH_SHORT);
                error.show();

                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    // ...

                    Toast errorToast = Toast.makeText(MainActivity.this, "Error, pls chech your internet connection and try again!", Toast.LENGTH_SHORT);
                    errorToast.show();
                }
                return true;
            }
        });

        // inputting and saving the data
        filehandler=new FileHandler();

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

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = getImageData() + "," + height.getText() + "," + diameter.getText() + "," + species.getText();
                        filehandler.addLine(data);
                    }
                });
            };
        });

    }

}



