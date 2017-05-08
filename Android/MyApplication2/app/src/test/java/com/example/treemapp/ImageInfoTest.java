package com.example.treemapp;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ImageInfoTest {
    @Test
    public void mosaicToImage_isCorrect() {

        float x = 5;
        float y = 3;

        ImageInfo im = new ImageInfo(2, 0, 0, 0, 1, 0, 0, 0, 1);
        float[] point = im.convertFromMosaicCoordinateToOriginal(x, y);

        /*
        System.out.println("Converted from: " + Float.toString(x) + ", " + Float.toString(y) +
                " to " + Float.toString(point[0]) + ", " + Float.toString(point[1]));
        */

        assertTrue(x * 2 == point[0]);
        assertTrue(y == point[1]);
    }

    @Test
    public void checkIfIdentity() {

        float x = 5;
        float y = 3;

        ImageInfo im = new ImageInfo(1, 0, 0, 0, 1, 0, 0, 0, 1);

        assert(im.getIsIdentity());
    }

    @Test
    public void checkIfValuesCorrect() {
        String[] words = {"img2.jpg", "19","22",  "1","0","72","0","2.5","0","1.01","2","9.3"  , "img1.jpg", "img3.jpg"};

        double[][] arr = {{1, 0, 72}, {0, 2.5, 0}, {1.01, 2, 9.3}};

        List<String> neighbors = new ArrayList<>();
        neighbors.add("img1.jpg");
        neighbors.add("img3.jpg");
        RealMatrix rm = new Array2DRowRealMatrix(arr);

        ImageInfo im = new ImageInfo(words);
        assertTrue(im.getX() == 19);
        assertTrue(im.getY() == 22);
        assertEquals(rm, im.getInverseTransformMatrix());
        assertEquals(neighbors, im.getNeighbors());
    }
}