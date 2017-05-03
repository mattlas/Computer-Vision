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
    private int intId;
    private String height;
    private String diameter;
    private String species;
    private int radius;
    private String imageFileName;
    private boolean dragged = false;

    /*
    * Make sure you are passing in image coordinates here
    * */

    public Pin(String id, PointF sPin, String imageFileName) {
        this.sPin = sPin;
        this.id = id;
        this.radius = 20;
        this.collisionRadius = 30;
        this.imageFileName = imageFileName;
    }

    public Pin(String id, float x, float y, String imageFileName) {
        this(id, new PointF(x, y), imageFileName);

    }

    public Pin(PointF sPin, String imageFileName){
        this("",sPin,imageFileName);
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

    public String getHeight() {return this.height;}

    public String getDiameter() {return this.diameter;}

    public String getSpecies() {return this.species;}

    public int getIntId() {return this.intId;}

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

    public void setIntId(int id) { this.intId = id;}

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
<<<<<<< HEAD
     * @return String representing the CSV line for the tree - "x,y,height,diameter,species"
=======
     * @return String representing the CSV line for the tree - "id,x,y,height,diameter,species,imageFileName"
>>>>>>> a65dc693b060dd0c3d43948ab2a81e6b2024621c
     */
    public String getCSV(){
        return id+","+sPin.x + "," + sPin.y + "," + height + "," + diameter + "," + species + "," + imageFileName + "\n";
    }

    /*How far away the user can touch the screen for the pin to consider itself touched*/
    public int getCollisionRadius() {
        return collisionRadius;
    }

    /*How big the drawn circle should be*/
    public int getRadius() {
        return radius;
    }



    public boolean isDragged() {
        return dragged;
    }

    public String getImageFileName() {
        return this.imageFileName;
    }
}