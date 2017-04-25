package com.example.treemapp;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class handles creating, reading and writing of the treelist CSV file - the final output of the app, containing information for all the trees.
 * @author Adam Kavanagh Coyne
 * @author Karolina Drobotowicz
 */

public class FileHandler {

    private String filename="/sdcard/treeList_";
    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    private BufferedWriter bw;
    private File file;
    private final String TAG = FileHandler.class.getSimpleName();

    public FileHandler(){
        try{
            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
            filename = filename + dateFormat.format(date) + ".csv" ;
            file = new File(filename);

            Log.d(TAG,"Attempting to create/open file: "+filename);
            if (file.createNewFile()){// if file already exists will do nothing
                Log.d(TAG, "Existing file not found, new file created");
            } else {
                Log.d(TAG, "Existing file found and loaded");
            }

            fw = new FileWriter(file,true);
            bw = new BufferedWriter(fw);

            fr = new FileReader(file);
            br = new BufferedReader(fr);

        }catch(IOException e){
            Log.e(TAG, "Failed to create/open file "+filename+": "+e.getLocalizedMessage());
            //System.exit(-1);
        }
    }

    /**
     * Adds a line to the file specified by "filename"
     * @param line the string to append to the file
     * @return true if the operation was a success, false if not
     */
    public boolean addLine(String line){
        try{

            bw.append(line);
            return true;

        }catch (Exception e){
            return false;
        }
    }



    /**
     * Closes the file. Only to be used on destruction of this class.
     */
    public void close(){
        try {
            bw.close();
            fw.close();
            br.close();
            fr.close();
        } catch (Exception e){
            Log.getStackTraceString(e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }


    /**
     * Returns the entire file in list format.
     * @return an ArrayList of String Arrays, one array for each line in the file, one String for each element
     */
    public ArrayList<String[]> readContents(){
        ArrayList<String[]> lineList = new ArrayList<>();
        try {
            String line;
            br.reset();
            while ((line = br.readLine()) != null){
                lineList.add(line.split(","));
            }
            br.reset();
        } catch (Exception e){
            Log.getStackTraceString(e);
        }
        Log.d(TAG,lineList.toString());
        return lineList;
    }


    // Do we really need this one?
//
//    public String readLine(){
//        try {
//            return br.readLine();
//        } catch (Exception e){
//            Log.getStackTraceString(e);
//            return null;
//        }
//    }


}
