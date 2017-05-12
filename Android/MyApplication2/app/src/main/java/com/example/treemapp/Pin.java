package com.example.treemapp;

import android.graphics.PointF;

import android.graphics.*;

/**
 * Class representing both a saved tree and its representation as a pin on the map. To be used with PinView
 */
public class Pin {

    private final int collisionRadius;
    private PointF sPin;
    private int id;
    private String height;
    private String diameter;
    private String species;
    private int radius;
    private String imageFileName;
    private boolean dragged = false;
    private PointF origCoor;

    /*
    * Make sure you are passing in image coordinates here
    * */

    public Pin(int id, PointF sPin, PointF origImage, String imageFileName) {
        this.sPin = sPin;
        this.id = id;
        this.radius = 20;
        this.collisionRadius = 30;
        this.imageFileName = imageFileName;
        this.origCoor = origImage;
    }

    public Pin(int id, float x, float y, float ox, float oy, String imageFileName) {
        this(id, new PointF(x, y), new PointF(ox, oy), imageFileName);

    }

    public Pin(PointF sCoor, PointF oCoor, String imageFileName){
        this(-1,sCoor, oCoor, imageFileName);
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

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pin pin = (Pin) o;

        return id == pin.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Gives part of the CSV format for the pin/tree (to be saved in the file)
     * @return String representing the CSV line for the tree - "id,x,y,height,diameter,species,imageFileName"
     */
    public String getCSV(){
        return id+","+ origCoor.x + "," + origCoor.y + "," + height + "," + diameter + "," + species + "," + imageFileName;
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

    @Override
    public String toString() {
        return "Pin{" +
                "sPin=" + sPin.toString() +
                ", id=" + id +
                ", height='" + height + '\'' +
                ", diameter='" + diameter + '\'' +
                ", species='" + species + '\'' +
                '}';
    }

    public void setOrigCoor(float x, float y) {
        origCoor = new PointF(x, y);
    }
}