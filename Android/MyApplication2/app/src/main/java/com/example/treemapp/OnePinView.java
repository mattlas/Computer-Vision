package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

/**
 * This view is an extention of the SubsamplingScaleImageView where you can add
 * Created by oskar on 2017-04-24.
 */

public class OnePinView extends SubsamplingScaleImageView {

    private final String TAG = OnePinView.class.getSimpleName();
    private Paint filled;
    private Pin pin;
    private Paint unfilled;
    private Paint smaller;

    public OnePinView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public OnePinView(Context context) {
        super(context);
        init();
    }

    public Pin getPin() {
        return pin;
    }

    public void setPinXandY(float x, float y) {
        if (pin != null) {
            pin.setPosition(x, y);
        }
    }

    public void init() {
        filled = new Paint();
        filled.setColor(Color.WHITE);
        filled.setAlpha(255);
        filled.setStrokeWidth(1);

        smaller = new Paint();
        smaller.setColor(Color.BLACK);
        smaller.setAlpha(150);
        smaller.setStrokeWidth(1);

        pin = null;
    }

    public void updatePin(Pin pin, String height, String diameter, String species){
        pin.setInputData(height, diameter, species);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        PointF point;

        point = sourceToViewCoord(pin.getPoint());

        if (pin.isDragged()) {
            canvas.drawCircle((int) point.x, (int) point.y, pin.getCollisionRadius(), unfilled);
            filled.setAlpha(128);
        }
        else filled.setAlpha(255);

        drawPin(canvas, pin);
    }

    public void drawPin(Canvas canvas, Pin pin){
        // First see if the species exists as a pin

        PointF point = sourceToViewCoord(pin.getPoint());

        int drawableName=R.drawable.alder;

        String species="Spruce";

        if (pin.getSpecies() != null) {
            species=pin.getSpecies();
        }

        switch (species){
            case "Spruce": drawableName=R.drawable.spruce;
                break;
            case "Pine": drawableName=R.drawable.pine;
                break;
            case "Alder": drawableName=R.drawable.alder;
                break;
            case "Aspen": drawableName=R.drawable.aspen;
                break;
            case "Rowan": drawableName=R.drawable.rowan;
                break;
            case "Birch": drawableName=R.drawable.birch;
                break;
        }

        Drawable d = ResourcesCompat.getDrawable(getResources(), drawableName, null);

        if (d != null) {
            int w = pin.getRadius();
            int h = d.getIntrinsicHeight() * pin.getRadius() / d.getIntrinsicWidth();

            int left = (int) point.x - (w / 2);
            int top = (int) point.y - h;
            int right = left + w;
            int bottom = (int) point.y;

            d.setBounds(left, top, right, bottom);
            d.draw(canvas);
        }

        /*
        else{
            // if not just draw a circle
            filled.setAlpha(255);
            canvas.drawCircle((int) point.x, (int) point.y, pin.getRadius(), filled);
            canvas.drawCircle((int) point.x, (int) point.y, (int) (pin.getRadius() * 0.75), smaller);
        }
        */

    }
}
