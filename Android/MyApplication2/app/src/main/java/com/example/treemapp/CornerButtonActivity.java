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

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corners);

        ImageButton leftBottomCorner = (ImageButton) findViewById(R.id.btn_LeftBottomCorner);
        ImageButton rightBottomCorner = (ImageButton) findViewById(R.id.btn_RightBottomCorner);
        ImageButton leftTopCorner = (ImageButton) findViewById(R.id.btn_LeftBottomCorner);
        ImageButton rightTopCorner = (ImageButton) findViewById(R.id.btn_RightTopCorner);
    }

}
