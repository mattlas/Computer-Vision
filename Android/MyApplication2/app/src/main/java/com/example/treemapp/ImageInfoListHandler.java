package com.example.treemapp;

import android.media.Image;
import android.media.ImageReader;
import android.util.Log;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by oskar on 2017-04-26.
 */

public class ImageInfoListHandler {

    private final String TAG = ImageInfoListHandler.class.getSimpleName();
    private String imageFolderName = "/sdcard/mosaic/mosaicImage/";

    private HashMap<String, ImageInfo> imageInfos;

    public ImageInfoListHandler() {

        imageInfos = new HashMap<>();
    }

    public void parseFileToHashMap(BufferedReader bf) {
        if (!imageInfos.isEmpty()) imageInfos.clear();

        String line;
        ImageInfo imageInfo;
        String[] words;
        int lineNumber = 0;

        try {
            while((line = bf.readLine()) != null) {

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

    public String loadImage(ImageInfo im) {
        return imageFolderName + im.getFileName();
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
