package com.example.treemapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

/**
 * Dialog that pops up when files are not found, can redirect user to directory selection.
 * Stores a static list of file descriptors for lost files. This list is displayed in the dialog.
 * Created by adam on 5/22/17.
 */

public class FileNotFoundDialog extends DialogFragment {
    private static final String TAG = FileNotFoundDialog.class.getSimpleName();
    public static ArrayList<String> missingFiles = new ArrayList<>();
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static boolean newLostFile=false;
    private static DialogInterface.OnClickListener settingsListener = null;

    /**
     *   Adds a missing file descriptor to the list.
     *   @param file a brief descriptor of the file that cannot be located (filename for example)
     */
    public static void addMissingFiles(String file) {
        if (!missingFiles.contains(file)){
            missingFiles.add(file);
            newLostFile=true;
        }
    }

    /**
     * Removes all missing files from the list
     */
    public static void clearMissingFiles(){
        missingFiles = new ArrayList<>();
        newLostFile=false;
    }

    /**
     * This is being set in mainActivity so the settingsmenu can be opened when
     * "go to settings is clicked"
     * @param settingsListener - the listener set in mainActivity
     */
    public static void setClickListener(DialogInterface.OnClickListener settingsListener) {
        FileNotFoundDialog.settingsListener = settingsListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Log.w(TAG,"Prompting user for clarification.");
            newLostFile = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String message = "One or more essential files seem to be missing";

            if (!missingFiles.isEmpty()) {
                message += ": ";
                for (String file : missingFiles) {
                    message += LINE_SEPARATOR + file;
                }
            } else message+=".";
            message += LINE_SEPARATOR + "Ensure the file structure is correct and try again.";

            builder.setMessage(message);
            if (settingsListener != null)
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing!
                    }
                });

            builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User cancelled dialog. Is this enough to remove it?
                }
            });
            return builder.create();
    }

    /**
     * Makes a dialog asking if the user wants to choose the file location (to be called when the files aren't found).
     * Example format of how this should be used in other classes:                     FileNotFoundDialog.popup(getActivity(), "imageFile");
     * @param file a brief description of the file to be located.
     */
    public static void popup(Activity activity, String file){
        Log.w(TAG, "Essential file not found: "+file+".");
        addMissingFiles(file);
        FileNotFoundDialog d = new FileNotFoundDialog();
        if (newLostFile){
            Log.w(TAG,"Prompting user for clarification");
            d.show(activity.getFragmentManager(), "missing:"+file);
        }
        newLostFile=false;
    }
}
