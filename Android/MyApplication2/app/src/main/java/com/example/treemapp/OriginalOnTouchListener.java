package com.example.treemapp;

import android.graphics.PointF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by oskar on 2017-05-11.
 * This code handles dragging the pin
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

    /**
     * Registers when the user touches the screen
     * @param view - the view touched (our view)
     * @param motionEvent - the motionevent gotten from the user (has x and y)
     * @return if this event was used by the code
     *  (if not, other codes can use it, like pan and zoom)
     */
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

    /**
     * Code that runs when we release a dragged pin
     */
    private void dragPinRelease() {
        opw.getPin().setDragged(false);
        drag = false;
        opw.setPanEnabled(true);
        opw.setZoomEnabled(true);
        opw.invalidate();
    }

    /**
     * Code that runs when the user is starting to drag a pin
     * @param e the motion event we get from the tap event
     */
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
