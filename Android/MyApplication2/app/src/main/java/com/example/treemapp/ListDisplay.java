package com.example.treemapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by 5dv115 on 5/8/17.
 */

public class ListDisplay extends Activity {
    // The array of strings
    String[] mobileArray = {"Android", "iPhone", "Blackberry"};
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_input); //rätt id?

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.tree_input, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }*/
}
