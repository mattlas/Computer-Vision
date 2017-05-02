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

    private final int collisionRadius;
    private PointF sPin;
    private Bitmap pin;
    private String id;
    private String height;
    private String diameter;
    private String species;
    private int radius;
    private boolean dragged = false;

    /*
    * Make sure you are passing in image coordinates here
    * */

    public Pin(String id, PointF sPin) {
        this.sPin = sPin;
        this.id = id;
        this.radius = 20;
        this.collisionRadius = 30;
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

    /*Two different ways of setting position of the pin (mosaic-coordinates)*/
    public void setPosition(double x, double y) {
        sPin.set((float) x, (float) y);
    }

    public void setPosition(PointF position) {
        setPosition(position.x, position.y);
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
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

    /*How far away the user can touch the screen for the pin to consider itself touched*/
    public int getCollisionRadius() {
        return collisionRadius;
    }

    /*How big the drawn circle should be*/
    public int getRadius() {
        return radius;
    }

    public void updatePositionInFile() {
        //TODO, here we can update the file maybe
    }

    public boolean isDragged() {
        return dragged;
    }
}