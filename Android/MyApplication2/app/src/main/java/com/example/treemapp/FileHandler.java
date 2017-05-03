package com.example.treemapp;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


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
import java.util.List;


/**
 * This class handles creating, reading and writing of the treeList CSV file - the final output of the app, containing information for all the trees.
 * @author Adam Kavanagh Coyne
 * @author Karolina Drobotowicz
 */
// TODO test out the new line deletion functionality via PinView.deletePin()
// TODO Debug this more once the tablet is accessible. Saving and editing is still complicated, we need to keep a close eye on the csv file


public class FileHandler {

    private String filename = Environment.getExternalStorageDirectory() + "/mosaic/treeList.csv";
    private BufferedReader br;
    private BufferedWriter bw;
    private File file;
    private final String TAG = FileHandler.class.getSimpleName();
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    public FileHandler() {
        // First create the directory if it doesn't exist
        try{
            File dir=new File(Environment.getExternalStorageDirectory()+"/mosaic");
            if (dir.mkdir()){
                Log.d(TAG, "Treelist directory created");
            } else {
                Log.d(TAG, "Opening existing treelist directory");
            }

        } catch (Exception e){
            Log.e(TAG, "Failed to create/open directory: " + Environment.getExternalStorageDirectory()+"/mosaic: " + e.getLocalizedMessage());
        }
        try {
            file = new File(filename);

            if (file.createNewFile()) {// if file already exists will do nothing
                Log.d(TAG, "Existing file " + filename + " not found, new file created");
            } else {
                Log.d(TAG, "Existing file " + filename + " found and loaded");
            }

            bw = new BufferedWriter(new FileWriter(file, true));

            br = new BufferedReader(new FileReader(file));

        } catch (IOException e) {
            Log.e(TAG, "Failed to create/open file " + filename + ": " + e.getLocalizedMessage());
            //System.exit(-1);
        }
    }

    /**
     * Adds a line to the file specified by "filename"
     *
     * @param line the string to append to the file
     * @return true if the operation was a success, false if not
     */
    public boolean addLine(String line) {
        try {

            bw.append(line);
            this.save();
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error adding line to file: " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Removes a line from the CSV file that has a certain ID
     * @param id the ID of the line to be removed
     * @return true if the line was found and successfully removed, false if not found.
     */
    public boolean removeLine(String id) {
        ArrayList<String[]> lines = this.readContents();

        int line = -1;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i)[0].equals(id)) {
                line=i;
            }
        }

        if (line != -1){
            this.removeLine(line);
            return true;
        } else {
            Log.w(TAG, "Line not found in file for ID: '" + id + "'");
            return false;
        }

    }

    /**
     * Removes a line from the CSV file of a certain line number
     * TODO fix this. For some reason once a second pin is deleted, all the others dissapear.
     * (My theory is that the temporary file gets reloaded even though it doesnt exist anymore)
     *
     * @param lineIndex the line number to remove
     * @return true if the line was found and successfully removed, false if not found / not removed
     */
    public boolean removeLine(int lineIndex) {


        try {
            File tempFile = File.createTempFile("csvtransfer","tmp");

            // Create a writer for the temp file
            BufferedWriter bwTemp = new BufferedWriter(new FileWriter(tempFile, true));

            // Write each line from the original file to the temp file except for the one to remove
            br.reset();

            String line;
            boolean success = false;

            for (int i = 0; (line = br.readLine()) != null; i++) {

                if (i != lineIndex) { // If the line isn't the one to remove, write it to the temp file
                    bwTemp.write(line+LINE_SEPARATOR);
                    Log.d(TAG,"Line "+i+" found and copied: '"+line+"'");

                } else {
                    success = true;
                    Log.d(TAG,"Line "+i+" found and deleted: '"+line+"'");
                }
            }

            this.close();
            bwTemp.close();

            // Replace the old file with the temp file

            if (!(success &=this.file.delete())){
                Log.e(TAG, "Error deleting file");
            }
            if (!(success &= tempFile.renameTo(new File(this.filename)))){
                Log.e(TAG, "Error renaming file");
            }
            this.file=new File(this.filename);

            this.open();
            return success;

        } catch (Exception e) {
            Log.e(TAG, "Error deleting line " + lineIndex + ": " + e.getLocalizedMessage());
            return false;
        }

    }

    /**
     * Changes the line at lineIndex in csv file into the line
     * @param lineIndex the index of the line to change
     * @param newLine the new version of the line
     * @return
     */
    public boolean editLine(int lineIndex, String newLine) {

        try {
            File tempFile = File.createTempFile("csvtransfer","tmp");

            // Create a writer for the temp file
            BufferedWriter bwTemp = new BufferedWriter(new FileWriter(tempFile, true));

            // Write each line from the original file to the temp file except for the one to edit
            br.reset();

            Log.d(TAG, "edit function");
            String line;
            boolean success = false;

            for (int i = 0; (line = br.readLine()) != null; i++) {

                if (i != lineIndex) { // If the line isn't the one to remove, write it to the temp file
                    bwTemp.write(line+LINE_SEPARATOR);
                    Log.d(TAG,"Line "+i+" found and copied: '"+line+"'");

                } else {
                    success = true;
                    bwTemp.write(newLine + LINE_SEPARATOR);
                    Log.d(TAG,"Line "+i+" found and edited: '"+newLine+"'");
                }
            }

            this.close();
            bwTemp.close();

            // Replace the old file with the temp file

            // Replace the old file with the temp file

            if (!(success &=this.file.delete())){
                Log.e(TAG, "Error deleting file");
            }
            if (!(success &= tempFile.renameTo(new File(this.filename)))){
                Log.e(TAG, "Error renaming file");
            }
            this.file=new File(this.filename);

            this.open();
            return success;

        } catch (Exception e) {
            Log.e(TAG, "Error deleting line " + lineIndex + ": " + e.getLocalizedMessage());
            return false;
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
    public ArrayList<String[]> readContents() {
        ArrayList<String[]> lineList = new ArrayList<>();
        try {
            String line;

            while ((line = br.readLine()) != null) {
                lineList.add(line.split(","));
            }
            this.save();
        } catch (Exception e) {
            Log.getStackTraceString(e);
        }
        Log.d(TAG, "Trees in file: " + lineList.size());
        return lineList;
    }

    /**
     * Reads all existing trees on file and formats them for using on app startup
     *
     * @return a List of Pins that can be used for a PinView
     */
    // TODO: debug the function
    public List<Pin> getPinList() {
        List<Pin> list = new ArrayList<>();


        ArrayList<String[]> lineList = this.readContents();
        for (String line[] : lineList) {
            // For each tree on file, create and enter details of the new pin
            Pin p = new Pin(line[0], Float.parseFloat(line[1]), Float.parseFloat(line[2]), line[6]);
            p.setInputData(line[3], line[4], line[5]);
            list.add(p);
        }

        return list;

    }

    /**
     * Closes the BufferedReader / writer
     */
    protected void close() {
        try {
            bw.close();
            br.close();
        } catch (Exception e) {
            Log.getStackTraceString(e);
        }
    }

    /**
     * Opens the BufferedReader / writer
     */
    protected void open(){
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
            br = new BufferedReader(new FileReader(file));
            br.mark(0);

        } catch (IOException e) {
            Log.e(TAG, "Failed to create/open file " + filename + ": " + e.getLocalizedMessage());
            //System.exit(-1);
        }
    }

    /**
     * Closes and reopens the file
     */
    protected void save(){
        this.close();
        this.open();
    }

}
