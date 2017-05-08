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

public class PerspectiveButtonActivity extends AppCompatActivity implements View.OnClickListener {
    private PointF latestTouch;
    private MainActivity mainAct;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perspective);

        latestTouch = MainActivity.latestTouch;

        mainAct = new MainActivity();

        openPerspective();
    }

    public void openPerspective () {

    }

    @Override
    public void onClick(View v) {
        return;
    }
}
