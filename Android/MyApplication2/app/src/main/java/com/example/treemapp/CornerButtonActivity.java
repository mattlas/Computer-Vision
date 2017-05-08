package com.example.treemapp;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Lena on 05.05.17.
 */

public class CornerButtonActivity extends AppCompatActivity implements View.OnClickListener {
    private PointF latestTouch;
    private MainActivity mainAct;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corners);

        latestTouch = MainActivity.latestTouch;

        mainAct = new MainActivity();

        openPerspective();
    }

    public void openPerspective () {
        ImageButton leftBottomCorner = (ImageButton) findViewById(R.id.btn_LeftBottomCorner);
        ImageButton rightBottomCorner = (ImageButton) findViewById(R.id.btn_RightBottomCorner);
        ImageButton leftTopCorner = (ImageButton) findViewById(R.id.btn_LeftBottomCorner);
        ImageButton rightTopCorner = (ImageButton) findViewById(R.id.btn_RightTopCorner);

        leftBottomCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latestTouch = MainActivity.latestTouch;


            }
        });

        rightBottomCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mainAct.perspectiveViewPopUp(latestTouch.x, latestTouch.y);
                //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();

            }
        });

        leftTopCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        rightTopCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
    }

    @Override
    public void onClick(View v) {
        return;
    }
}
