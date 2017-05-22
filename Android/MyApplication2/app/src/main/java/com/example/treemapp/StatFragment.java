package com.example.treemapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Oskar created this, good job Oskar
 */

public class StatFragment extends Fragment {

    Statista statista = null;
    private HistogramView histogramView;
    private PieChart pieChart;
    private boolean statistaSet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.content_statistics, container, false);

        this.histogramView = (HistogramView) view.findViewById(R.id.histogramView);
        this.pieChart = (PieChart) view.findViewById(R.id.pieChart);

        if (statista != null) setViews();

        return view;
    }

    private void setViews() {
        if (this.histogramView != null) {
            this.histogramView.setHistogram(statista);
        }
        if (this.pieChart != null) {
            this.pieChart.setSpeciesList(statista.getSpeciesList());
        }
    }

    public void init(Statista statista) {
        this.statista = statista;
        setViews();
    }
}