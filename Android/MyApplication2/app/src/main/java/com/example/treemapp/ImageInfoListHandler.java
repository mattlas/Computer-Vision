package com.example.treemapp;

import android.app.Activity;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.ImageSource;

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
    private String folderName = Environment.getExternalStorageDirectory() + "/mosaic/";
    private String imageFolderName = folderName + "images/";

    private HashMap<String, ImageInfo> imageInfos;

    public ImageInfoListHandler() {
        String fileName = "imageList.csv";
        File file = new File(folderName + fileName);
        FileReader fileReader;
        imageInfos = new HashMap<>();

        try {
            fileReader = new FileReader(file);
            parseFileToHashMap(fileReader);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Could not find file: '"+ fileName + "' in folder '" + folderName + "'");
        }

        Log.d(TAG, "loaded " + imageInfos.size() + " numbers of lines from imageList.csv");
    }

    public void parseFileToHashMap(BufferedReader bf) {
        if (!imageInfos.isEmpty()) imageInfos.clear();

        String line;
        ImageInfo imageInfo;
        String[] words;
        int lineNumber = 0;

        try {
            while((line = bf.readLine()) != null) {
                line = line.replaceAll("\"", "");
                words = line.split(",");
                imageInfo = new ImageInfo(words);
                imageInfos.put(imageInfo.getFileName(), imageInfo);

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
    public ImageSource loadImage(String fileName) {
        String location = imageFolderName + fileName;
        return ImageSource.uri(location);
    }

    public ImageSource loadImage(ImageInfo im) {
        return loadImage(im.getFileName());
    }

    /*These two returns all neighbors to that image as ImageSources*/
    public List<ImageSource> loadNeighboringImages(ImageInfo im) {
        List<ImageSource> neighbors = new ArrayList<>();

        for (String neighborName: im.getNeighbors()) {
            neighbors.add(loadImage(neighborName));
        }

        return neighbors;
    }

    public List<ImageSource> loadNeighboringImages(String fileName) {
        return loadNeighboringImages(imageInfos.get(fileName));
    }

    public ImageInfo findImageClosestTo(int x, int y) {
        if (imageInfos.isEmpty()) return null;
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

            it.remove(); // avoids a ConcurrentModificationException
        }

        return closest;
    }


}
