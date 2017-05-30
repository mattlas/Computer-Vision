package com.example.treemapp;

import android.util.Log;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oskar on 2017-04-26.
 */

public class ImageInfoListHandler {

    private final String TAG = ImageInfoListHandler.class.getSimpleName();
    private String folderName = FileLocation.getSD();

    private boolean foundEverything;

    private HashMap<String, ImageInfo> imageInfos;
    private float icx;
    private float icy;
    private double scale;

    public ImageInfoListHandler() {

        String fileName = "androidData.csv";
        File file = new File(folderName + fileName);
        FileReader fileReader;
        imageInfos = new HashMap<>();
        foundEverything = false;


        try {
            fileReader = new FileReader(file);
            parseFileToHashMap(fileReader);
            foundEverything = true;

        } catch (FileNotFoundException e) {
            Log.e(TAG, "Could not find file: '"+ fileName + "' in folder '" + folderName + "'");
        }

        Log.d(TAG, "loaded " + imageInfos.size() + " numbers of lines from androidDate.csv");
    }

    public boolean didFindEverything() {
        return foundEverything;
    }

    public void parseFileToHashMap(BufferedReader bf) {
        if (!this.imageInfos.isEmpty()) this.imageInfos.clear();

        String line;
        ImageInfo imageInfo;
        String[] words;
        int lineNumber = 0;
        List <String> names = new ArrayList<>();
        names.add(null);
        try {
            String firstLine = bf.readLine();
            words = firstLine.split(",");
            this.scale = Double.parseDouble(words[0]);
            this.icx = Float.parseFloat(words[1]);
            this.icy = Float.parseFloat(words[2]);
            while((line = bf.readLine()) != null) {
                line = line.replaceAll("\"", "");
                words = line.split(",");
                imageInfo = new ImageInfo(words);
                names.add(words[0]);

                this.imageInfos.put(imageInfo.getFileName(), imageInfo);

                lineNumber++;
            }
        } catch (IOException e) {

            Log.e(TAG, "Could not read line: " + Integer.toString(lineNumber) +
                    "(or the line below or above that one) in the imageInfoList file", e);
        }

        for (String fileName:names){
            if (fileName != null) {
                ImageInfo ii = this.findImageInfo(fileName);
                List<Integer> neighborsIndexes = ii.getNeigborIndexes();
                for (Integer neighbor : neighborsIndexes) {
                    if (neighbor < names.size()) ii.addNeighborName(names.get(neighbor));
                }
            }
        }

    }

    public float[] transformOrigToMosaic(Pin pin) {

        ImageInfo ii = findImageInfo(pin.getImageFileName());

        if (ii == null) {
            Log.e(TAG, "Could not find the correct imageInfo for filename: '" + pin.getImageFileName() +"' so I am returning orig");
            return new float[]{pin.getOrigY(), pin.getOrigY()};
        }

        RealMatrix transformMatrix = findImageInfo(pin.getImageFileName()).getTransformMatrix();
        LUDecomposition s = new LUDecomposition(transformMatrix);

        RealMatrix doubleInversed = s.getSolver().getInverse();

        double[] coordArr = {pin.getOrigX(), pin.getOrigY(), 1};
        RealMatrix coordMatrix = new Array2DRowRealMatrix(coordArr);

        RealMatrix result = doubleInversed.multiply(coordMatrix);
        float[] coor = new float[2];

        coor[0] = (float) result.getEntry(0, 0) + getICX();
        coor[1] = (float) result.getEntry(1, 0) + getICY();
        return coor;
    }

    public float[] transformMosaicToOrig(float x, float y, ImageInfo im) {
        float[] f = getResultCoordinates(x, y);
        return im.convertFromIdentityCoordinatesToOriginal(f[0], f[1]);
    }

    public float[] transformMosaicToOrig(float x, float y, String fileName) {
        ImageInfo im = findImageInfo(fileName);
        return transformMosaicToOrig(x, y, im);
    }


    public void parseFileToHashMap(Reader reader) {
        parseFileToHashMap(new BufferedReader(reader));
    }

    public ImageInfo findImageInfo(String fileName) {

        if (imageInfos.containsKey(fileName)) {
            return imageInfos.get(fileName);
        }
        else {
            return null;
        }
    }

    /**
     * Returns image files located in mosaic/images, used for viewing a point from different angles
     * @param fileName no path, just the fileName
     * @return an imagesource which can be used to set in an image view
     */
    public String loadImage(String fileName) {
        String location = folderName + fileName;

        File file = new File(location);

        if (!file.exists()) {
            Log.e(TAG, "could not find file: '" + location +"'");
        }

        return location;
    }

    public String getImageFileName(ImageInfo im) {
        return loadImage(im.getFileName());
    }

    /*These two returns all neighbors to that image as ImageSources*/
    public List<String> loadNeighboringImages(ImageInfo im) {
        List<String> neighbors = new ArrayList<>();

        if (im != null) {
            for (String neighborName : im.getNeighbors()) {
                neighbors.add(loadImage(neighborName));
            }
        }

        return neighbors;
    }

    public List<String> loadNeighboringImages(String fileName) {
        return loadNeighboringImages(imageInfos.get(fileName));
    }


    /**
     * For a given position it returns the image closest to it (as an imageInfo)
     * @param x mosaic coordinates
     * @param y mosaic coordinates
     * @return an ImageInfo which contains information about the file
     */
    public ImageInfo findImageClosestTo(double x, double y) {
        if (imageInfos.isEmpty()) return new ImageInfo();
        double dis;

        Iterator<ImageInfo> it = imageInfos.values().iterator();

        ImageInfo imageInfo = it.next();

        ImageInfo closest = imageInfo;
        double currentMinDistance = closest.euclidianDistance(x, y);

        while (it.hasNext()) {
            imageInfo = it.next();

            dis = imageInfo.euclidianDistance(x, y);

            if (dis < currentMinDistance) {
                currentMinDistance = dis;
                closest = imageInfo;
            }
        }

        return closest;
    }


    private float[] getResultCoordinates(float x, float y) {
        float[] coor = {x, y};
        coor[0] -= this.icx;
        coor[1] -= this.icy;
        return coor;
    }

    public float getICX() {
        return icx;
    }

    public float getICY() {
        return icy;
    }

    public double getScale() {
        return scale;
    }
}
