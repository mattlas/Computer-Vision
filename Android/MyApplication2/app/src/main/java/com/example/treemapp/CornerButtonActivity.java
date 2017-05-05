package com.example.treemapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Lena on 05.05.17.
 */

public class CornerButtonActivity extends AppCompatActivity {

    private ImageButton leftBottomCorner;
    private ImageButton rightBottomCorner;
    private ImageButton leftTopCorner;
    private ImageButton rightTopCorner;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corners);

        leftBottomCorner = (ImageButton) findViewById(R.id.btn_LeftBottomCorner);
        rightBottomCorner = (ImageButton) findViewById(R.id.btn_RightBottomCorner);
        leftTopCorner = (ImageButton) findViewById(R.id.btn_LeftBottomCorner);
        rightTopCorner = (ImageButton) findViewById(R.id.btn_RightTopCorner);
    }

}
