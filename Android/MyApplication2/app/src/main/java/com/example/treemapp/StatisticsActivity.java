package com.example.treemapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    public HistogramView histogramView;
    public PieChart pieChart;
    public Statista statista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statista = new Statista((ArrayList<Pin>) getIntent().getSerializableExtra("pinList"));

        histogramView = (HistogramView) findViewById(R.id.histogramView);
        histogramView.setHistogram(statista);

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setSpeciesList(statista.getSpeciesList());


    }

}