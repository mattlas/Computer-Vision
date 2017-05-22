package com.example.treemapp;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by oskar on 2017-05-11.
 */

public class OriginalOnTouchListener implements View.OnTouchListener {

    private OnePinView opw;
    private boolean drag;

    public OriginalOnTouchListener(OnePinView opw) {
        this.opw = opw;
        drag = false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean used = false;

        if (opw.isReady()) {
            if (opw.getPin() != null) {
                if (opw.euclidanViewDistance(opw.getPin(), motionEvent.getX(), motionEvent.getY()) < opw.getPin().getCollisionRadius()) {
                    setUpDragPin(motionEvent); //disables the touch to pan and zoom
                    used = true;
                }
            }

            if (drag) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    dragPinRelease();
                } else {
                    PointF newPosition = opw.viewToSourceCoord(motionEvent.getX(), motionEvent.getY());

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
        Pin pin = opw.getPin();
        pin.setDragged(true);
        drag = true;

        opw.setZoomEnabled(false);

        /* When you set panEnabled to false, Dave Morrisey (who wrote the image view code).
        * decided that you want to center the image aswell, so we will transform it back */
        float scale = opw.getScale();
        PointF center = opw.getCenter();

        opw.setPanEnabled(false);
        opw.setScaleAndCenter(scale, center);

        opw.invalidate();
    }


}
