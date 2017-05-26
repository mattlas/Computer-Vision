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
 * This view is an extention of the SubsamplingScaleImageView where you can add a pin.
 * Pops up when you want to add a new pin.
 * Created by oskar on 2017-04-24.
 */

public class OnePinView extends SubsamplingScaleImageView {

    private final String TAG = OnePinView.class.getSimpleName();
    private Paint filled;
    private Pin pin;
    private Paint unfilled;

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


    /*
    * Compute distances in view coordinates (screen distance)
    * */
    public double euclidanViewDistance(Pin pin, float x, float y) {
        PointF p = sourceToViewCoord(pin.getOrigX(), pin.getOrigY());
        return Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
    }


    public PointF setPinXandY(float x, float y) {
        if (pin != null) {
            pin.setOrigCoor(x, y); // we can update this as we are on the original image
            new PointF(pin.getOrigX(), pin.getOrigY());
        }
        return null;
    }

    public void init() {
        filled = new Paint();
        filled.setColor(Color.WHITE);
        filled.setAlpha(255);
        filled.setStrokeWidth(1);

        unfilled = new Paint();
        unfilled.setColor(Color.WHITE);
        unfilled.setAlpha(255);
        unfilled.setStrokeWidth(5);
        unfilled.setStyle(Paint.Style.STROKE);

        pin = null;
    }

    public void updatePin(Pin pin, String height, String diameter, String species, Boolean isDead, String notes){
        pin.setInputData(height, diameter, species, isDead, notes);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (pin != null && isReady()) {
            PointF point = sourceToViewCoord(pin.getOrigX(), pin.getOrigY());

            if (pin.isDragged()) {
                canvas.drawCircle((int) point.x, (int) point.y, pin.getCollisionRadius(), unfilled);
                filled.setAlpha(128);
            } else filled.setAlpha(255);

            drawPin(canvas, pin);
        }
    }

    public void drawPin(Canvas canvas, Pin pin){
        // First see if the species exists as a pin

        //PointF point = sourceToViewCoord(pin.getPoint());
        PointF point = sourceToViewCoord(pin.getOrigX(), pin.getOrigY());

        //TODO, convert from mosaic to original

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

    public void setPin(Pin pin) {
        this.pin = pin;
    }
}
