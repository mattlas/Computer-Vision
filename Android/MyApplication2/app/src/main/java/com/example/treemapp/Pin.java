package com.example.treemapp;

import android.graphics.PointF;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;


public class Pin {

    private PointF sPin;
    private Bitmap pin;

    /*
    * Make sure you are passing in image coordinates here
    * */

    public Pin(PointF sPin) {
        this.sPin = sPin;
    }

    public Pin(float x, float y) {
        this.sPin = new PointF(x, y);
    }

    public PointF getPoint() {
        return sPin;
    }

    public int getX() {
        return (int) sPin.x;
    }

    public int getY() {
        return (int) sPin.y;
    }

}