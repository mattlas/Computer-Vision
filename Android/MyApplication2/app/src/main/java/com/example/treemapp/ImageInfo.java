package com.example.treemapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.*;

import static java.lang.System.exit;

/**
 * Created by oskar on 2017-04-26.
 * Holds information of one of the original images and its neighbours
 */

class ImageInfo {

    private String TAG = ImageInfo.class.getSimpleName();
    private double x;
    private double y;
    private String fileName;
    private List<String> neighbors;
    private List<Integer> neighborIndexes = new ArrayList<>();
    private RealMatrix matrix;
    private boolean isIdentity;

    public ImageInfo(String[] words) {
            parseInfo(words);

    }
    public ImageInfo(){
        this.fileName = "DEBUG_NO_IMAGE";
        this.x=-1;
        this.y=-1;
        this.neighbors=new ArrayList<>();
        this.neighborIndexes = new ArrayList<>();
        this.matrix = null;
    }

    public ImageInfo(double... entries) {
        if (entries.length != 9) {
            Log.e(TAG, "There are supposed to be exactly 9 entries, you have: " + Integer.toString(entries.length));
            exit(27);
        }

        double[][] entries2d = new double[3][3];

        int count = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                entries2d[x][y] = entries[count++];
            }
        }

        this.matrix = new Array2DRowRealMatrix(entries2d);
        setIsIdentity();
    }

    public void setIsIdentity() {
        double[][] arr = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        Array2DRowRealMatrix identity = new Array2DRowRealMatrix(arr);
        isIdentity = identity.equals(matrix);
    }

    public ImageInfo(double[][] matrix) {
        this.matrix = new Array2DRowRealMatrix(matrix);
        setIsIdentity();
    }

    private void parseInfo(String[] words) {
        neighbors = new ArrayList<String>();
        this.fileName = words[0];
        this.x = Double.parseDouble(words[1]);
        this.y = Double.parseDouble(words[2]);

        double[][] entries = new double[3][3];
        int count = 3;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                entries[x][y] = Double.parseDouble(words[count++]);
            }
        }

        matrix = new Array2DRowRealMatrix(entries);



        while ( count < words.length ) {
            String word = words[count];
            Integer number = Integer.parseInt(word);
            neighborIndexes.add(number);
            count++;
        }

        setIsIdentity();
    }

    public String getFileName() {
        return this.fileName;
    }

    public double euclidianDistance(double x, double y) {
        return Math.sqrt((Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2)));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    //TODO, this is missing the translation and the rescaling
    public float[] convertFromIdentityCoordinatesToOriginal(double x, double y) {
        double[] mc = new double[3];
        mc[0] = x;
        mc[1] = y;
        //the z-coordinate but we don't have that information so 1 is apparently correct
        mc[2] = 1f;

        RealMatrix mosaicCordinates = new Array2DRowRealMatrix(mc);

        float imageX, imageY;

        if (matrix != null && mosaicCordinates != null) {

            RealMatrix resultingMatrix = matrix.multiply(mosaicCordinates);

            imageX = (float) resultingMatrix.getEntry(0, 0);
            imageY = (float) resultingMatrix.getEntry(1, 0);
            //ignoring the z-coordinate since we don't need it

        } else {
            // TODO improve error handling here
            Log.e(TAG, "Matrix data or mosaic coordinates do not exist (does the image exist?)-Incorrect coordinates used");
            imageX=(float) x;
            imageY=(float) y;

        }

        float[] originalCoordinate = {imageX, imageY};

        return originalCoordinate;
    }

    public RealMatrix getTransformMatrix() {
        return matrix;
    }

    public List<String> getNeighbors() {
        if (neighbors==null)
        {
            return new ArrayList<>();
        }
        return neighbors;
    }

    public boolean getIsIdentity() {
        return isIdentity;
    }

    public List<Integer> getNeigborIndexes() {
        return this.neighborIndexes;
    }

    public void addNeighborName(String s) {
        this.neighbors.add(s);
    }
}
