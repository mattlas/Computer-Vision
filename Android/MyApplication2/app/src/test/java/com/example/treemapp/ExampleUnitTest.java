package com.example.treemapp;

import android.graphics.PointF;
import android.provider.Settings;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void mosaicToImage_isCorrect() {

        int x = 0;
        int y = 0;

        ImageInfo im = new ImageInfo(1, 2, 3, 1, 2, 4, 1, 2, 5);
        PointF point = im.convertFromMosaicCoordinateToOriginal(0, 0);

        System.out.println("Converted from: " + Float.toString(x) + "," + Float.toString(y) + "to" + Float.toString(point.x) + ", " + Float.toString(point.y));
    }
}