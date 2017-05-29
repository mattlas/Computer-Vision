package com.example.treemapp;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private final String TAG = this.getClass().getSimpleName();
    private Context appContext;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.treemapp", appContext.getPackageName());
    }

    @Test
    public void checkTheSDcard() {

        String path = InstrumentationRegistry.getTargetContext().getExternalFilesDirs(null)[1].getAbsolutePath().split("Android")[0];

        Log.i("SD-path " ,path);
    }

    @Test
    public void checkIfMosaicFolderExists() {
        File f = new File(FileLocation.getSD());
        assertTrue(f.isDirectory());
    }

    @Test
    public void checkIfMosaicImageExists() {
        File f = new File(FileLocation.getSD() + "mosaic.jpg");

        assertTrue(f.exists() && !f.isDirectory());
    }

    @Test
    public void checkIfImagesExists() {
        ImageInfoListHandler iilh = new ImageInfoListHandler();
        iilh.getImageFileName(iilh.findImageClosestTo(0, 0));
    }

    @Test
    public void checkIfWritePermission() throws Exception{
        new FileHandler(null, 1);
    }

}
