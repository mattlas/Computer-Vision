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

    /**
     * TODO, check that this works as expected
     * To see how close a point is to a point
     * @param x, the x position on the mosaic
     * @param y, the y position on the mosaic
     * @return the length from the point to this in screen distance
     */
    public double euclidianDistance(double x, double y) {
        return Math.sqrt((Math.pow(this.sPin.x - x, 2) + Math.pow(this.sPin.y - y, 2)));
    }

}