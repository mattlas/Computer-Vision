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

        float x = 5;
        float y = 3;

        ImageInfo im = new ImageInfo(2, 0, 0, 0, 1, 0, 0, 0, 1);
        float[] point = im.convertFromMosaicCoordinateToOriginal(x, y);

        System.out.println("Converted from: " + Float.toString(x) + ", " + Float.toString(y) +
                " to " + Float.toString(point[0]) + ", " + Float.toString(point[1]));

        assertTrue(x * 2 == point[0]);
        assertTrue(y == point[1]);
    }
}