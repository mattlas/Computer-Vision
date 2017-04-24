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

    public FileHandler(){
        try{
            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
            filename = filename + dateFormat.format(date) + ".csv" ;
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
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    /* Do this later: reading data
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
