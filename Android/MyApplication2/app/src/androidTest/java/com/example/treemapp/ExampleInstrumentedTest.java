package com.example.treemapp;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
<<<<<<< HEAD
=======
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> a65dc693b060dd0c3d43948ab2a81e6b2024621c
>>>>>>> 1459ed5ff6ef813ed662cfe96bcc6082736c73ee

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

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.treemapp", appContext.getPackageName());
    }

    @Test
    public void checkIfMosaicFolderExists() {
        File f = new File(Environment.getExternalStorageDirectory().toString() + "/mosaic");
        assertTrue(f.isDirectory());
    }

    @Test
    public void checkIfMosaicImageExists() {
        File f = new File(Environment.getExternalStorageDirectory() + "/mosaic/mosiac.jpg");

        assertTrue(f.exists() && !f.isDirectory());
    }

    @Test
    public void checkIfImagesExists() {
        ImageInfoListHandler iilh = new ImageInfoListHandler();
<<<<<<< HEAD
        iilh.getImageFileName(iilh.findImageClosestTo(0, 0));
=======
<<<<<<< HEAD
        iilh.loadImage(iilh.findImageClosestTo(0, 0));
=======
        iilh.getImageFileName(iilh.findImageClosestTo(0, 0));
>>>>>>> a65dc693b060dd0c3d43948ab2a81e6b2024621c
>>>>>>> 1459ed5ff6ef813ed662cfe96bcc6082736c73ee
    }

}
