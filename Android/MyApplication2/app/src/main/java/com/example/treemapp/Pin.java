package com.example.treemapp;

import android.graphics.PointF;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;

/**
 * Class representing both a saved tree and its representation as a pin on the map. To be used with PinView
 */
public class Pin implements Serializable{

    private final int collisionRadius;
    private float sx;
    private float sy;
    private int id;
    private String height;
    private String diameter;
    private String species;
    private String notes;
    private Boolean isDead;
    private int radius;
    private String imageFileName;
    private boolean dragged = false;
    private float ox;
    private float oy;

    /*
    * Make sure you are passing in image coordinates here
    * */

    public Pin(int id, @NonNull PointF sPin, @NonNull PointF origImage, String imageFileName) {
        this.sx = sPin.x;
        this.sy = sPin.y;

        this.id = id;
        this.radius = 30;
        this.collisionRadius = 30;
        this.imageFileName = imageFileName;
        this.ox = origImage.x;
        this.oy = origImage.y;
    }

    public Pin(int id, float x, float y, float ox, float oy, String imageFileName) {
        this(id, new PointF(x, y), new PointF(ox, oy), imageFileName);

    }

    public Pin(@NonNull PointF sCoor, @NonNull PointF oCoor, String imageFileName){
        this(-1,sCoor, oCoor, imageFileName);
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public PointF getPoint() {
        return new PointF(sx, sy);
    }

    public int getX() {
        return (int) sx;
    }

    public int getY() {
        return (int) sy;
    }

    public String getHeight() {return this.height;}

    public String getDiameter() {return this.diameter;}

    public String getSpecies() {return this.species;}

    public String getImageFileName() {
        return this.imageFileName;
    }

    /*Two different ways of setting position of the pin (mosaic-coordinates)*/
    public void setPosition(double x, double y) {
            sx = (float) x;
            sy = (float) y;
    }

    public void setPosition(PointF position) {
        setPosition(position.x, position.y);
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    public void setInputData(String height, String diameter, String species, Boolean isDead, String notes) {
        this.diameter = diameter;
        this.height = height;
        this.species = species;
        this.isDead = isDead;
        this.notes = notes;
    }

    /**
     * Setter for parameters.
     * @param diameter
     * @param height
     * @param species
     */
    public void setInputData(String diameter, String height, String species){
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

    public String getNotes() {
        return notes;
    }

    public Boolean getIsDead() {
        return isDead;
    }

    /**
     * checks if a pin is equal to other pin - not used for now
     */
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
    public String getCSV(float scale){
        String dead;
        if(isDead)
            dead = "dead";
        else
            dead = "alive";
        notes = notes.replaceAll(",", "¸");
        String line = id+","+ Float.toString(ox/scale) + "," + Float.toString(oy/scale) + "," + height + "," + diameter + "," + species + "," + dead + "," + notes + "," + imageFileName;
        Log.d("pin", line);
        return line;
    }

    /**
     * How far away the user can touch the screen for the pin to consider itself touched
     */
    public int getCollisionRadius() {
        return collisionRadius;
    }

    /**
     * How big the drawn circle should be
     */
    public int getRadius() {
        return radius;
    }

    public boolean isDragged() {
        return dragged;
    }

    @Override
    public String toString() {
        return "Pin{" +
                "sPin=" + sx +", " + sy +
                ", id=" + id +
                ", height='" + height + '\'' +
                ", diameter='" + diameter + '\'' +
                ", species='" + species + '\'' +
                '}';
    }

    public void setOrigCoor(float x, float y) {
        ox = x;
        oy = y;
    }

    public float getOrigX() {
        return ox;
    }

    public float getOrigY() {
        return oy;
    }
}