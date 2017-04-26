package com.example.treemapp;

import android.graphics.PointF;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

/**
 * Class representing both a saved tree and its representation as a pin on the map. To be used with PinView
 */
public class Pin {

    private PointF sPin;
    private Bitmap pin;
    private String id;
    private String height;
    private String diameter;
    private String species;

    /*
    * Make sure you are passing in image coordinates here
    * */

    public Pin(String id, PointF sPin) {
        this.sPin = sPin;
        this.id = id;
    }

    public Pin(String id, float x, float y) {
        this(id, new PointF(x, y));

    }

    public Pin(PointF sPin){
        this("",sPin);
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

    public void setInputData(String height, String diameter, String species) {
        this.diameter = diameter;
        this.height = height;
        this.species = species;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pin pin = (Pin) o;

        return id.equals(pin.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Gives part of the CSV format for the pin/tree (to be saved in the file)
     * @return String representing the CSV line for the tree - "x,y,height,diameter,species"
     */
    public String getCSV(){
        String s = id+","+sPin.x + "," + sPin.y + "," + height + "," + diameter + "," + species;
        return s;
    }


}