package com.example.treemapp;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Oskar created this, good job Oskar
 */

public class StatFragment extends Fragment {

    Statista statista = null;
    private HistogramView histogramView;
    private PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_statistics, container, false);

        this.histogramView = (HistogramView) view.findViewById(R.id.histogramView);
        this.pieChart = (PieChart) view.findViewById(R.id.pieChart);

        this.histogramView.setHistogram(statista);
        this.pieChart.setSpeciesList(statista.getSpeciesList());

        return view;
    }

    public void init(Statista statista) {
        this.statista = statista;
    }
}