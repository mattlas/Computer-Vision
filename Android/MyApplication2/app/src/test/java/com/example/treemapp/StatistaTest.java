package com.example.treemapp;

import android.graphics.PointF;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
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

public class StatistaTest {

    private Statista stat;

    @Before
    public void setup() {
        ArrayList<Pin> pins = new ArrayList<>();

        Pin p;

        int[][] pos = {{6,3,4,5}, {9,1,2,0}, {64,63,287,9}, {9093, 32, 23, 43}};
        String[] widths = {"12", "18", "17", "3"};
        String[] heights = {"33", "98", "2", "17"};
        String[] species = {"birch", "spruce", "birch", "pine"};

        for (int i = 0; i < 4; i++) {
            p = new Pin(new PointF(pos[i][0], pos[i][1]), new PointF(pos[i][2], pos[i][3]), "blabla");

            //p.setInputData(widths[i], heights[i], species[i]);
            //pins.add(p);
        }

        stat = new Statista(pins);
    }

    @Test
    public void createsHistogramCorrect() {
        Statista.Histogram histogram = stat.generateHistogram(8);

        assertTrue(histogram.getValues()[0] != 0);
    }

    @Test
    public void createsSpeciesCorrect() {
        List<Statista.SpeciesCount> list = stat.getSpeciesList();
        assertTrue(list.get(0).getAmount() == 2);
        assertTrue(list.get(1).getAmount() == 1);
        assertTrue(list.get(2).getAmount() == 1);
    }

}