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
 * This class handles creating, reading and writing of the treelist CSV file - the final output of the app, containing information for all the trees.
 * @author Adam Kavanagh Coyne
 * @author Karolina Drobotowicz
 */
// TODO test out the new line deletion functionality via PinView.deletePin()


public class FileHandler {

    private String filename = Environment.getExternalStorageDirectory() + "/treelists/treeList_";
    private BufferedReader br;
    private BufferedWriter bw;
    private File file;
    private final String TAG = FileHandler.class.getSimpleName();

    public FileHandler() {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            filename = filename + dateFormat.format(date) + ".csv";
            file = new File(filename);

            Log.d(TAG, "Attempting to create/open file: " + filename);
            if (file.createNewFile()) {// if file already exists will do nothing
                Log.d(TAG, "Existing file not found, new file created");
            } else {
                Log.d(TAG, "Existing file found and loaded");
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

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i)[0].equals(id)) {
                return this.removeLine(i);
            }
        }

        Log.w(TAG, "Line not found in file for ID: '" + id + "'");
        return false;

    }

    /**
     * Removes a line from the CSV file of a certain line number
     * @param lineIndex the line number to remove
     * @return true if the line was found and successfully removed, false if not found / not removed
     */
    public boolean removeLine(int lineIndex) {


        try {
            File tempFile = File.createTempFile("tmp", "");

            // Create a writer for the temp file
            BufferedWriter bwTemp = new BufferedWriter(new FileWriter(tempFile, true));

            // Write each line from the original file to the temp file except for the one to remove
            br.reset();

            String line;
            boolean success = false;

            for (int i = 0; (line = br.readLine()) != null; i++) {

                if (i != lineIndex) { // If the line isn't the one to remove, write it to the temp file
                    bwTemp.write(line);
                } else {
                    success = true;
                }
            }

            this.close();
            bwTemp.close();

            // Replace the old file with the temp file
            success &= tempFile.renameTo(this.file);

            br = new BufferedReader(new FileReader(this.file));
            bw = new BufferedWriter(new FileWriter(this.file, true));

            return success;

        } catch (Exception e) {
            Log.e(TAG, "Error deleting line " + lineIndex + ": " + e.getLocalizedMessage());
            return false;
        }

    }


    /**
     * Closes the file. To be used mainly on destruction of this class.
     */
    public void close() {
        try {
            bw.close();
            br.close();
        } catch (Exception e) {
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
     *
     * @return an ArrayList of String Arrays, one array for each line in the file, one String for each element
     */
    public ArrayList<String[]> readContents() {
        ArrayList<String[]> lineList = new ArrayList<>();
        try {
            String line;
            br.reset();
            while ((line = br.readLine()) != null) {
                lineList.add(line.split(","));
            }
            br.reset();
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
    public List<Pin> getPinList() {
        ArrayList<String[]> lineList = this.readContents();
        List<Pin> list = new ArrayList<>();

        for (String line[] : lineList) {
            // For each tree on file, create and enter details of the new pin
            Pin p = new Pin(line[0], Float.parseFloat(line[1]), Float.parseFloat(line[2]));
            p.setInputData(line[3], line[4], line[5]);
            list.add(p);
        }

        return list;

    }

}
