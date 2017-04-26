package com.example.treemapp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 2017-04-26.
 */

public class ImageInfoListHandler {

    private final String TAG = ImageInfoListHandler.class.getSimpleName();

    private List<ImageInfo> imageInfoList;

    public ImageInfoListHandler() {
        imageInfoList = new ArrayList<>();
    }

    public void parseFile(BufferedReader bf) {
        if (!imageInfoList.isEmpty()) imageInfoList.clear();

        String line;
        ImageInfo imageInfo;
        String[] words;
        int lineNumber = 0;

        try {
            while((line = bf.readLine()) != null) {

                words = line.split(",");
                imageInfo = new ImageInfo(words);

                lineNumber++;
            }
        } catch (IOException e) {

            Log.e(TAG, "Could not read line: " + Integer.toString(lineNumber) +
                    "(or the line below or above that one) in the imageInfoList file", e);
        }

    }

    public ImageInfo findImageClosestTo(int x, int y) {
        if (imageInfoList.isEmpty()) return null;
        double dis;

        ImageInfo closest = imageInfoList.get(0);
        double currentMinDistance = imageInfoList.get(0).euclidianDistance(x, y);

        for(ImageInfo imi : imageInfoList) {
            dis = imi.euclidianDistance(x, y);
            if (dis < currentMinDistance) {
                closest = imi;
                currentMinDistance = dis;
            }
        }

        return closest;
    }


}
