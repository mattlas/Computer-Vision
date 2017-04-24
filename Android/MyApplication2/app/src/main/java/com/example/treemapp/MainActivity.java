package com.example.treemapp;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    public FileHandler filehandler;


    protected String getImageData(){
        // Dummy method, replace later with real image data
        return "\"img1.png\", 23, 45, ";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filehandler=new FileHandler();

        Button mShowDialog = (Button) findViewById(R.id.showInput);
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.tree_input, null);
                final EditText height = (EditText) mView.findViewById(R.id.inp_height);
                final EditText diameter = (EditText) mView.findViewById(R.id.inp_diameter);
                final EditText species = (EditText) mView.findViewById(R.id.inp_species);
                Button save = (Button) mView.findViewById(R.id.btn_save);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = getImageData() + "," + height.getText() + "," + diameter.getText() + "," + species.getText();
                        filehandler.addLine(data);
                    }
                });
                };
                });
            }

    }
}
