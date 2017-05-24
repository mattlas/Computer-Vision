package com.example.treemapp;

import android.graphics.PointF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by oskar on 2017-05-11.
 */

public class OriginalOnTouchListener implements View.OnTouchListener {

    private static final String TAG = OriginalOnTouchListener.class.getSimpleName();
    private OnePinView opw;
    private boolean drag;
    private float scale = 1;
    private PointF center = null;

    public OriginalOnTouchListener(OnePinView opw) {
        this.opw = opw;
        drag = false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean used = false;

        if (opw.isReady()) {
            if (!drag) {
                if (opw.getPin() != null) {
                    if (opw.euclidanViewDistance(opw.getPin(), motionEvent.getX(), motionEvent.getY()) < opw.getPin().getCollisionRadius()) {
                        setUpDragPin(motionEvent); //disables the touch to pan and zoom
                        used = true;
                    }
                }
                else drag = false;
            }

            if (drag) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    dragPinRelease();
                } else {
                    //view to source
                    PointF newPosition = opw.viewToSourceCoord(motionEvent.getX(), motionEvent.getY());
                    //newPosition.set(newPosition.x - opw.getX(), newPosition.y - opw.getY());
                    Log.d(TAG, Integer.toString((int) motionEvent.getY()) + ", " + Integer.toString((int) motionEvent.getY()) + "\t\t\t\t" + Integer.toString((int) newPosition.x)  + ", " +  Integer.toString((int) newPosition.y));

                    opw.setPinXandY(newPosition.x, newPosition.y);
                    opw.invalidate();
                }
                used = true;
            }
        }

        return used;
    }

    private void dragPinRelease() {
        opw.getPin().setDragged(false);
        drag = false;
        opw.setPanEnabled(true);
        opw.setZoomEnabled(true);
        opw.invalidate();
    }

    public void setUpDragPin(MotionEvent e) {
        drag = true;
        opw.setZoomEnabled(false);
        /* When you set panEnabled to false, Dave Morrisey (who wrote the image view code).
        * decided that you want to center the image aswell, so we will transform it back */
        scale = opw.getScale();
        center = opw.getCenter();
        
        opw.setPanEnabled(false);
        opw.setScaleAndCenter(scale, center);

        opw.invalidate();
    }


}
