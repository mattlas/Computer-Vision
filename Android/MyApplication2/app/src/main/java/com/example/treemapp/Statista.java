package com.example.treemapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oskar on 2017-05-15.
 * Computes a histogram for the withs of the trees in the pinList
 */

public class Statista implements Serializable{
    private List<Pin> pins; //not allowed to modify in this class
    private SmallToLarge smallToLarge;
    private ArrayList<SpeciesCount> species = new ArrayList<>();

    public Statista(ArrayList<Pin> list) {
        this.pins = list;
        smallToLarge = new SmallToLarge();

        for (Pin p: pins) {
            if (species.contains(p.getSpecies())) {
                SpeciesCount s = species.get(species.indexOf(new SpeciesCount(p.getSpecies())));
                s.addTree();
            }
            else {
                species.add(new SpeciesCount(p.getSpecies()));
            }
        }
    }
    public ArrayList<SpeciesCount> getSpeciesList(){
        return species;
    }





    @SuppressWarnings("Since15")
    /**
     * Creates a histogram object with the values of the height of trees marked,
     * maxStaple is how many staples the histogram will have at most
     */
    public Histogram generateHistogram(int maxStaples) {

        if (pins.isEmpty()) {
            int[] arr = {0};
            return new Histogram(arr, 0, 0, 0);
        }

        List<Double> heights = new ArrayList<>();
        double height;
        double minHeight = 10000;
        double maxHeight = 0;

        for (Pin p : pins) {
            height = Double.parseDouble(p.getHeight());
            heights.add(height);

            if (height > maxHeight) {
                maxHeight = height;
            }
            else if (height < minHeight) {
                minHeight = height;
            }
        }

        int numberOfStaples = Math.min(heights.size(), maxStaples);

        heights.sort(smallToLarge);

        int currentStaple = 0;
        double widthOfOneStaple = (heights.get(heights.size()-1) - heights.get(0)) / numberOfStaples;
        double currentStaplesTop = heights.get(0) + widthOfOneStaple;
        double value;

        int[] amounts = new int[numberOfStaples];

        for (int i = 0; i < numberOfStaples || currentStaple >= numberOfStaples; ++i) {
            value = heights.get(i);

            while(value > currentStaplesTop) {
                currentStaplesTop += widthOfOneStaple;
                if (currentStaple >= amounts.length-1) break;
                else currentStaple++;
            }

            amounts[currentStaple]++;
        }


        return new Histogram(amounts, heights.get(0), heights.get(heights.size()-1), widthOfOneStaple);
    }

    private class SmallToLarge implements Comparator<Double>, Serializable{
        public int compare(Double d, Double d2) {
            if (d < d2) return -1;
            if (d > d2) return 1;
            return 0;
        }
    }

    /**
     * Created by oskar on 2017-05-15.
     * Holds the information about a histogram, min, max, widths and amounts for each staple
     */
    public class Histogram implements Serializable{
        private final int[] values;
        private final double min;
        private final double max;
        private final double width;
        private final int mostPopulated;

        public Histogram(int[] values, double min, double max, double width) {
            this.values = values;
            this.min = min;
            this.max = max;
            this.width = width;

            int mostPopulated = 0;
            for (int i = 0; i < values.length; i++) {
                if (this.values[i] > mostPopulated) {
                    mostPopulated = this.values[i];
                }
            }
            this.mostPopulated = mostPopulated;
        }

        /*Sorted with the lowest ranges to the left and the heighest to the right*/
        public int[] getValues() {
            return values;
        }

        public double widthOfStaple() {
            return this.width;
        }

        public int size() {
            return this.values.length;
        }

        public int mostPopulatedStapleSize() {
            return mostPopulated;
        }

        /**
         * Use this to get the range of the histogram
         * @return the heightest value of the trees heights
         */
        public double getMax() {
            return max;
        }

        public double getMin() {
            return min;
        }
    }

    public class SpeciesCount implements Serializable{
        private final String species;
        private int count;

        public SpeciesCount(String species) {
            this.species = species;
            this.count = 1;
        }

        public void addTree() {
            count++;
        }

        public int getAmount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            return o.equals(species);
        }


    }
}
