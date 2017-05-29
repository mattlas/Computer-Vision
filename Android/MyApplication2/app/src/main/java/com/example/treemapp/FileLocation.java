package com.example.treemapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

/**
 * Created by oskar on 2017-05-22.
 */

public class FileLocation {

    private static String fileLocation = null;

    /**
     * This can only be run after getFileSystemSDCard name has been run
     * @return
     */
    public static String getSD() {
        return fileLocation;
    }

    /**
     * This line of code finds the mosaic, has to be run before FileLocation is usable,
     * run from main activity in onCreate before any file things
     */
    @NonNull
    public static String getFileSystemSDCardName(Context context) {
        File[] paths = context.getExternalFilesDirs(null);
        String sd = "ERROR";

        if (paths.length > 1) {
            sd = paths[1].getAbsolutePath().split("Android")[0];
        }

        File sub = new File(sd + "TMS/mosaic");

        if (sub.exists()) {
            sd = sub.getAbsolutePath() + "/";

            if (new File(sd + "mosaic.png").exists()) {
                Log.d("Hello ", "mosaic.png exists");
            }

        }

        fileLocation = sd + "/";

        return fileLocation;
    }
}
