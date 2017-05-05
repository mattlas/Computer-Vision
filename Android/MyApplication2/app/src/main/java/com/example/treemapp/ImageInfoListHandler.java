package com.example.treemapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by oskar on 2017-04-26.
 */

public class ImageInfoListHandler {

    private final String TAG = ImageInfoListHandler.class.getSimpleName();
    private String folderName = Environment.getExternalStorageDirectory() + "/mosaic/";
    private String imageFolderName = folderName + "images/";

    private boolean foundEverything;

    private HashMap<String, ImageInfo> imageInfos;
    private float icx;
    private float icy;

    public ImageInfoListHandler() {
        String fileName = "imageList.csv";
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

        Log.d(TAG, "loaded " + imageInfos.size() + " numbers of lines from imageList.csv");
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

        try {
            while((line = bf.readLine()) != null) {
                line = line.replaceAll("\"", "");
                words = line.split(",");
                imageInfo = new ImageInfo(words);

                if (imageInfo.getIsIdentity()) {
                    this.icx = (float) imageInfo.getX() - 300f;
                    this.icy = (float) imageInfo.getY() - 200f;
                }

                this.imageInfos.put(imageInfo.getFileName(), imageInfo);

                lineNumber++;
            }
        } catch (IOException e) {

            Log.e(TAG, "Could not read line: " + Integer.toString(lineNumber) +
                    "(or the line below or above that one) in the imageInfoList file", e);
        }

    }

    public void parseFileToHashMap(Reader reader) {
        parseFileToHashMap(new BufferedReader(reader));
    }

    public ImageInfo findImageInfo(String fileName) {
        return imageInfos.get(fileName);
    }

    /**
     * Returns image files located in mosaic/images, used for viewing a point from different angles
     * @param fileName no path, just the fileName
     * @return an imagesource which can be used to set in an image view
     */
    public String loadImage(String fileName) {
        String location = imageFolderName + fileName;

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

        for (String neighborName: im.getNeighbors()) {
            neighbors.add(loadImage(neighborName));
        }

        return neighbors;
    }

    public List<String> loadNeighboringImages(String fileName) {
        return loadNeighboringImages(imageInfos.get(fileName));
    }

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


    public float[] getResultCoordinates(float x, float y) {
        float[] coor = {x, y};
        coor[0] -= this.icx;
        coor[1] -= this.icy;
        return coor;
    }
}
