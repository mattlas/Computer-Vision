package com.example.treemapp;

import android.graphics.PointF;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HistogramTest {
    @Test
    public void createsHistogramCorrect() {
        ArrayList<Pin> pins = new ArrayList<>();

        Pin p;

        int[][] pos = {{6,3,4,5}, {9,1,2,0}, {64,63,287,9}, {9093, 32, 23, 43}};
        String[] widths = {"12", "18", "17", "3"};
        String[] heights = {"33", "98", "2", "17"};

        for (int i = 0; i < 4; i++) {
            p = new Pin(new PointF(pos[i][0], pos[i][1]), new PointF(pos[i][2], pos[i][3]), "blabla");
            p.setInputData(widths[i], heights[i], "birch");
            pins.add(p);
        }

        Statista stat = new Statista(pins);
        Statista.Histogram histogram = stat.generateHistogram(8);

        assertTrue(histogram.getValues()[0] != 0);
    }

}