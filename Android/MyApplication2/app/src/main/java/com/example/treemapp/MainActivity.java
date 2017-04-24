package com.example.treemapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);

        imageView.setImage(ImageSource.resource(R.drawable.tree));
        //imageView.setImage(ImageSource.uri("/sdcard/DCIM/DSCM00123.JPG"));


    }

}
