package com.example.treemapp;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * This class handles creating, reading and writing of the treelist CSV file - the final output of the app, containing information for all the trees.
 * @author Adam Kavanagh Coyne
 * @author Karolina Drobotowicz
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
     * Returns the entire file in list format. Each element requires further parsing.
     * @return an ArrayList of Strings, one for each line in the file.
     */
    public ArrayList<String> readContents(){
        ArrayList<String> lineList = new ArrayList<>();
        try {
            String line;
            br.reset();
            while ((line = br.readLine()) != null){
                lineList.add(line);
            }
            br.reset();
        } catch (Exception e){
            Log.getStackTraceString(e);
        }
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
