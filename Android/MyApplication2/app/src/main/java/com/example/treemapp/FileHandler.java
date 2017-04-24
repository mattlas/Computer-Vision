package com.example.treemapp;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by 5dv173 on 4/24/17.
 */

public class FileHandler {

    private final String filename="/sdcard/treeList.csv";
    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    private BufferedWriter bw;
    private File file;

    public FileHandler(){
        try{
            file = new File(filename);
            file.createNewFile(); // if file already exists will do nothing

            fw = new FileWriter(file,true);
            bw = new BufferedWriter(fw);

            fr = new FileReader(file);
            br = new BufferedReader(fr);

        }catch(Exception e){
            e.printStackTrace();
            //System.exit(-1);
        }
    }

    public boolean addLine(String line){
        try{

            bw.append(line);
            return true;

        }catch (Exception e){
            return false;
        }
    }

    public void close(){
        try {
            bw.close();
            fw.close();
            br.close();
            fr.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    // Do this later: reading data
    /*
    public String readContents(){
        try {
            return br.read();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String readLine(){
        try {
            return br.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    */

}
