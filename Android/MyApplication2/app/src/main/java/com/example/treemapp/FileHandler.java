package com.example.treemapp;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;



import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math3.linear.*;



/**
 * This class handles creating, reading and writing of the treeList CSV file - the final output of the app, containing information for all the trees.
 * @author Adam Kavanagh Coyne
 * @author Karolina Drobotowicz
 *
 * For each line in the CSV file, the format is as follows:
 * id,x,y,height,diameter,species,imagefilename
 *
 * as of recent edits x and y are coordinates in the ORIGINAL IMAGE, not the mosaic
 */


public class FileHandler {

    private String fileName = "treeList.csv";
    private String directory = FileLocation.getSD();
    private String fullFileName= directory+fileName;

    private BufferedReader br;
    private BufferedWriter bw;
    private File file;
    private MainActivity mainActivity;
    private final String TAG = FileHandler.class.getSimpleName();
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
    private double scale;

    public FileHandler(MainActivity mainActivity, double scale) {
        // First initInputOverlay the directory if it doesn't exist
        this.mainActivity=mainActivity;
        this.scale = scale;
        try{
            File dir = new File(directory);

            if (dir.mkdir()){
                Log.i(TAG, "Treelist directory created");
            } else {
                Log.i(TAG, "Opening existing treelist directory " + directory);
            }

        } catch (Exception e){
            Log.e(TAG, "Failed to initInputOverlay/open directory: " + directory+  ": " + e.getLocalizedMessage());

        }
        // Then the file
        try {
            file = new File(fullFileName);

            if (file.createNewFile()) {// if file already exists will do nothing
                Log.i(TAG, "Existing file " + fullFileName + " not found, new file created");
            } else {
                Log.i(TAG, "Existing file " + fullFileName + " found and loaded");
            }

            bw = new BufferedWriter(new FileWriter(file, true));

            br = new BufferedReader(new FileReader(file));

        } catch (IOException e) {
            Log.e(TAG, "Failed to initInputOverlay/open file " + fullFileName + ": " + e.getLocalizedMessage());
        }
    }

    /**
     * Adds a line to the file specified by "fullFileName"
     *
     * @param line the string to append to the file
     * @return true if the operation was a success, false if not
     */
    public boolean addLine(String line) {
        try {

            bw.append(line+LINE_SEPARATOR);
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
    public boolean removeLineId(int id) {
        ArrayList<String[]> lines = this.readContents();

        int line = -1;

        for (int i = 0; i < lines.size(); i++) {
            if (Integer.parseInt(lines.get(i)[0]) == id) {
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
     *
     * @param lineIndex the line number to remove
     * @return true if the line was found and successfully removed, false if not found / not removed
     */
    public boolean removeLine(int lineIndex) {


        try {
            File tempFile = new File(fullFileName +".tmp");

            BufferedWriter bwTemp = new BufferedWriter(new FileWriter(tempFile, true));

            String line;
            boolean success=false;
            // Write each line from the original file to the temp file except for the one to remove
            for (int i=0; (line = br.readLine()) != null; i++) {
                if (i == lineIndex){
                    success=true;
                    Log.d(TAG, "Line "+i+" found and deleted: \""+line+"\"");
                } else {
                    bwTemp.write(line+LINE_SEPARATOR);
                }
            }

            this.close();
            bwTemp.close();

            // Replace the old file with the temp file

            if (!(success &=this.file.delete())){
                Log.e(TAG, "Error deleting file");
            }
            if (!(success &= tempFile.renameTo(new File(this.fileName)))){
                Log.e(TAG, "Error renaming file");
            }
            this.file=new File(this.fileName);

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
            File tempFile = new File(fullFileName+".tmp");

            // Create a writer for the temp file
            BufferedWriter bwTemp = new BufferedWriter(new FileWriter(tempFile, true));

            // Write each line from the original file to the temp file except for the one to edit
            br.reset();

            String line;
            boolean success = false;

            for (int i = 0; (line = br.readLine()) != null; i++) {

                if (i != lineIndex) { // If the line isn't the one to remove, write it to the temp file
                    bwTemp.write(line+LINE_SEPARATOR);
                    Log.d(TAG,"Line "+i+" found and copied: '"+line+"'");

                } else {
                    success = true;
                    bwTemp.write(newLine+LINE_SEPARATOR);
                    Log.d(TAG,"Line "+i+" found and edited: '"+newLine+"'");
                }
            }

            this.close();
            bwTemp.close();

            // Replace the old file with the temp file
            if (!(success &=this.file.delete())){
                Log.e(TAG, "Error deleting file");
            }
            if (!(success &= tempFile.renameTo(new File(fullFileName)))){
                Log.e(TAG, "Error renaming file");
            }
            this.file=new File(fullFileName);

            this.open();
            return success;

        } catch (Exception e) {
            Log.e(TAG, "Error deleting line " + lineIndex + ": " + e.getLocalizedMessage());
            return false;
        }

    }

    /**
     * TODO send content URI instead of file URI (to avoid throwing an exception)     * also some javadoc!!
     * @return
     */
    public Uri getUri(){
        return  Uri.fromFile(file);
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
                String [] line2 = line.split(",");
                if (line2.length == 9)
                    lineList.add(line2);
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
    public ArrayList<Pin> getPinList(ImageInfoListHandler iil) {
        ArrayList<Pin> list = new ArrayList<>();
        float mosaicX;
        float mosaicY;
        String imageFileName;
        ImageInfo info;


        ArrayList<String[]> lineList = this.readContents();
        for (String line[] : lineList) {
            if (line.length == 9){
                imageFileName = line[8];

                info = iil.findImageInfo(imageFileName);

                float origY;
                float origX;

                origX = Float.parseFloat(line[1]) * (float) scale;
                origY = Float.parseFloat(line[2]) * (float) scale;

                if (info != null) {

                    RealMatrix transformMatrix = info.getTransformMatrix();
                    LUDecomposition s = new LUDecomposition(transformMatrix);

                    RealMatrix doubleInversed = s.getSolver().getInverse();

                    double[] coordArr = {origX, origY, 1};
                    RealMatrix coordMatrix = new Array2DRowRealMatrix(coordArr);

                    RealMatrix result = doubleInversed.multiply(coordMatrix);

                    mosaicX = (float) result.getEntry(0, 0) + iil.getICX();
                    mosaicY = (float) result.getEntry(1, 0) + iil.getICY();

                } else {
                    // TODO make sure this is ok in the final build. Maybe improve error handling

                    if (mainActivity != null) {
                        FileNotFoundDialog.popup(mainActivity, "imageInfo");
                    }

                    mosaicX=origX;
                    mosaicY=origY;
                };



                // For each tree on file, initInputOverlay and enter details of the new pin

                Pin p = new Pin(Integer.parseInt(line[0]), mosaicX, mosaicY, origX, origY, imageFileName);
                Boolean dead;
                if (line[6].equals("dead"))
                    dead = true;
                else
                    dead = false;
                p.setInputData(line[3], line[4], line[5], dead, line[7]);
                list.add(p);
            } else {
                Log.e(TAG, "File format invalid");
            }

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
            Log.e(TAG, "Failed to initInputOverlay/open file " + fullFileName + ": " + e.getLocalizedMessage());
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
