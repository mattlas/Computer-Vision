package com.example.treemapp;

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
import java.util.List;
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

    private String filename = Environment.getExternalStorageDirectory() + "/mosaic/treeList.csv";
    private BufferedReader br;
    private BufferedWriter bw;
    private File file;
    private final String TAG = FileHandler.class.getSimpleName();
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    public FileHandler() {
        // First create the directory if it doesn't exist
        try{
            File dir = new File(Environment.getExternalStorageDirectory()+"/mosaic");
            if (dir.mkdir()){
                Log.i(TAG, "Treelist directory created");
            } else {
                Log.i(TAG, "Opening existing treelist directory");
            }

        } catch (Exception e){
            Log.e(TAG, "Failed to create/open directory: " + Environment.getExternalStorageDirectory()+"/mosaic: " + e.getLocalizedMessage());
        }
        // Then the file
        try {
            file = new File(filename);

            if (file.createNewFile()) {// if file already exists will do nothing
                Log.i(TAG, "Existing file " + filename + " not found, new file created");
            } else {
                Log.i(TAG, "Existing file " + filename + " found and loaded");
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
            File tempFile = new File(this.filename+".tmp");

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
            File tempFile = new File(this.filename+".tmp");

            // Create a writer for the temp file
            BufferedWriter bwTemp = new BufferedWriter(new FileWriter(tempFile, true));

            // Write each line from the original file to the temp file except for the one to edit
            br.reset();

            String line;
            boolean success = false;

            Log.d(TAG, this.file.getName());

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
    public List<Pin> getPinList(ImageInfoListHandler iil) {
        List<Pin> list = new ArrayList<>();
        float mosaicX;
        float mosaicY;
        String fileName;
        ImageInfo info;


        ArrayList<String[]> lineList = this.readContents();
        for (String line[] : lineList) {
            if (line.length == 7){
                fileName = line[6];

                info = iil.findImageInfo(fileName);

                float origY;
                float origX;

                origX = Float.parseFloat(line[1]);
                origY = Float.parseFloat(line[2]);

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

                    Log.e(TAG,"Image info not found (does the image file exist?)");

                    mosaicX=origX;
                    mosaicY=origY;
                };



                // For each tree on file, create and enter details of the new pin

                Pin p = new Pin(Integer.parseInt(line[0]), mosaicX, mosaicY, origX, origY, fileName);
                p.setInputData(line[3], line[4], line[5]);
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
