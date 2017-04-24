package com.example.treemapp;

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
    }


}
