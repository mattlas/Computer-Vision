package com.example.treemapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by oskar on 2017-05-02.
 * This class (and the OriginalImageView class) were made primarily for debugging purposes
 */

public class OriginalImageActivity extends AppCompatActivity {
    private Button mBtGoBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.original_image);
        OriginalImageView image = (OriginalImageView) findViewById(R.id.originalImageView);
        Intent intent = getIntent();
        float x = intent.getFloatExtra("x", 2);
        float y = intent.getFloatExtra("y", 0);

        image.setXY(x, y);
        image.setFileString(intent.getStringExtra("fileName"));

        mBtGoBack = (Button) findViewById(R.id.btn_original_go_back);

        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
