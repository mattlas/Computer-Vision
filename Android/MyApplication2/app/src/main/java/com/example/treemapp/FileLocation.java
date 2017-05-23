package com.example.treemapp;

import android.util.Log;

import java.io.File;

/**
 * Created by oskar on 2017-05-22.
 */

public class FileLocation {
    private static final String TAG = FileLocation.class.getSimpleName();
    private static String fileLocation = getFileSystemSDcardName();

    public static String getSD() {
        return fileLocation;
    }

    public static boolean changeSDLocation(String location) {
        if(new File(location).exists()) {
            fileLocation = location;
            return true;
        }
        else {
            Log.e(TAG, "that folder does not exist... sooo I am not going to change. Folder you sent was '" + location + "'");
            return false;
        }
    }

    private static String getFileSystemSDcardName() {
        String folder = "ERROR";

        if(new File("/storage/extSdCard/").exists()) {
            folder="/storage/extSdCard/";
            Log.i("Sd Cardext Path",folder);
        }
        else if(new File("/storage/sdcard1/").exists()) {
            folder="/storage/sdcard1/";
            Log.i("Sd Card1 Path",folder);
        }
        else if(new File("/storage/usbcard1/").exists()) {
            folder="/storage/usbcard1/";
            Log.i("USB Path",folder);
        }
        else if(new File("/storage/sdcard0/").exists()) {
            folder="/storage/sdcard0/";
            Log.i("Sd Card0 Path",folder);
        }
        else { // For use with emulator
            folder="/storage/emulated/0/";
            Log.i("Emulated SD path", folder);
        }

        return folder;
    }
}
