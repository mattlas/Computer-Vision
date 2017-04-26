package com.example.treemapp;

import android.graphics.PointF;
import android.nfc.Tag;
import android.util.Log;

import junit.framework.Assert;

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
    private List<String> neighbours;
    private RealMatrix inverseTransform;

    public ImageInfo(String[] words) {
        parseInfo(words);
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

        this.inverseTransform = new Array2DRowRealMatrix(entries2d);
    }

    public ImageInfo(double[][] inverseTransform) {
        this.inverseTransform = new Array2DRowRealMatrix(inverseTransform);
    }

    private void parseInfo(String[] words) {
        this.fileName = words[0];
        this.x = Double.parseDouble(words[1]);
        this.y = Double.parseDouble(words[2]);

        double[][] entries = new double[3][3];
        int count = 3;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                entries[x][y] = Double.parseDouble(words[count++]);
            }
        }

        inverseTransform = new Array2DRowRealMatrix(entries);
    }

    public double euclidianDistance(double x, double y) {
        return Math.sqrt((Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2)));
    }

    public PointF convertFromMosaicCoordinateToOriginal(float x, float y) {
        double[] mc = new double[3];
        mc[0] = x;
        mc[1] = y;
        //the z-coordinate but we don't have that information so 1 is apparently correct
        mc[2] = 1;

        RealMatrix mosaicCordinates = new Array2DRowRealMatrix(mc);

        RealMatrix resultingMatrix = mosaicCordinates.multiply(inverseTransform);

        float imageX, imageY;
        imageX = (float) resultingMatrix.getEntry(0, 0);
        imageY = (float) resultingMatrix.getEntry(1, 0);
        //ignoring the z-coordinate since we don't need it

        return new PointF(imageX, imageY);
    }
}