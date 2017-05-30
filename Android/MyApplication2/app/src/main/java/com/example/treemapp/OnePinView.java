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

    //getter
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

    /**
     * Sets the original image
     * @param x originalX
     * @param y originalY
     * @return
     */
    public PointF setPinXandY(float x, float y) {
        if (pin != null) {
            pin.setOrigCoor(x, y); // we can update this as we are on the original image
            new PointF(pin.getOrigX(), pin.getOrigY());
        }
        return null;
    }

    /**
     * sets up the colors
     */
    private void init() {
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

    /**
     * Draws things on the image (only the pin for now)
     * @param canvas - what we are drawing to, given to us by android
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (pin != null && isReady()) {
            PointF point = sourceToViewCoord(pin.getOrigX(), pin.getOrigY());

            if (pin.isDragged()) { //some feedback when you drag it
                canvas.drawCircle((int) point.x, (int) point.y, pin.getCollisionRadius(), unfilled);
                filled.setAlpha(128);
            } else filled.setAlpha(255);

            drawPin(canvas, pin);
        }
    }

    /**
     * Draws the pin on the pinview
     * @param canvas - what we are drawing to, given to us by android
     * @param pin - the pin to draw
     */
    public void drawPin(Canvas canvas, Pin pin){
        // First see if the species exists as a pin

        PointF point = sourceToViewCoord(pin.getOrigX(), pin.getOrigY());

        String species="Other";

        if (pin.getSpecies() != null) {
            species=pin.getSpecies();
        }

        int drawableName = getResources().getIdentifier(species.replaceAll(" ","_").toLowerCase(),"drawable",getContext().getPackageName());

        if (drawableName == 0) {
            drawableName=R.drawable.empty;
        }


        Drawable d = ResourcesCompat.getDrawable(getResources(), drawableName, null);

        if (d != null) {
            float prettyScale = 1.5f;
            int w = (int) (pin.getRadius() * prettyScale);
            int h = (int) (prettyScale * d.getIntrinsicHeight() * pin.getRadius() / d.getIntrinsicWidth());

            int left = (int) point.x - (w / 2);
            int top = (int) point.y - h;
            int right = left + w;
            int bottom = (int) point.y;

            d.setBounds(left, top, right, bottom);
            d.draw(canvas);
        }
    }

    /**
     * Swaps the pin used
     * @param pin
     */
    public void setPin(Pin pin) {
        this.pin = pin;
    }
}
