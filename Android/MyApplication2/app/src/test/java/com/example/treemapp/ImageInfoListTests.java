package com.example.treemapp;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ImageInfoListTests {

    String content;
    String fileName;
    BufferedReader reader;

    @Before
    public void initialize() {
        fileName = "DSC01104_geotag.JPG";
        String[] matrices = {"1,0,0,0,1,0,0,0,1,", "1,0,74,0,1,-12,0,0,1,", "1,0,-12,0,1,-80,0,0,1,"};
        String[] fileNames = {fileName, "DSC01105_geotag.JPG", "DSC01106_geotag.JPG"};
        String[] coordinates = {",3,3,", ",1,2,", ",9,7,"};
        String randomNeighbors = "0,1,2,";

        content = ".15, 20, 21,\n";

        for (int i = 0; i < 3; i++) {
            content += fileNames[i] + coordinates[i] + matrices[i] + randomNeighbors + '\n';
        }

        reader = new BufferedReader(new StringReader(content));
    }

    @Test
    public void thereAndBackAgain() {
        ImageInfoListHandler iilh = new ImageInfoListHandler();
        iilh.parseFileToHashMap(reader);
        Pin pin = new Pin(0, 5, 5, 10, 10, fileName);

        float[] xy = iilh.transformOrigToMosaic(pin);
        xy = iilh.transformMosaicToOrig(xy[0], xy[1], pin.getImageFileName());

        System.out.println(pin.getOrigX() + ", " + xy[0] + "\n" + pin.getOrigY() + ", " + xy[1]);
        assertTrue(pin.getOrigX() == xy[0]);
        assertTrue(pin.getOrigY() == xy[1]);
    }

    @Test
    public void readingFromList_parsesCorrect() throws RuntimeException {
        ImageInfoListHandler iilh = new ImageInfoListHandler();
        iilh.parseFileToHashMap(reader);

        boolean condition = (iilh.findImageInfo(fileName).euclidianDistance(4, 3) == 1);

        if(BuildConfig.DEBUG && !(condition))
            throw new RuntimeException();
    }

    @Test
    public void checkClosest_givesClosest() throws RuntimeException {
        ImageInfoListHandler iilh = new ImageInfoListHandler();
        iilh.parseFileToHashMap(reader);

        assertEquals("DSC01104_geotag.JPG", iilh.findImageClosestTo(2, 3).getFileName());
        assertEquals("DSC01105_geotag.JPG", iilh.findImageClosestTo(-1, -1).getFileName());
        assertEquals("DSC01106_geotag.JPG", iilh.findImageClosestTo(8, 8).getFileName());
    }



}