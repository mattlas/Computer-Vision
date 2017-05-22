package com.example.treemapp;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by oskar on 2017-05-11.
 */

public class OriginalGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private OnePinView opw;
    private boolean drag;

    public OriginalGestureDetector(OnePinView onePinView) {
        this.opw = onePinView;
        drag = false;
    }

    public boolean getDrag() {
        return drag;
    }

    public void turnOffDrag() {
        drag = false;
    }

    public void setUpDragPin(MotionEvent e) {
        Pin pin = opw.getPin();

        if (pin != null) {
            if (opw.euclidanViewDistance(pin, e.getX(), e.getY()) < pin.getCollisionRadius()) {

                pin.setDragged(true);
                opw.setZoomEnabled(false);

                /* When you set panEnabled to false, Dave Morrisey (who wrote the image view code).
                * decided that you want to center the image aswell, so we will transform it back */
                float scale = opw.getScale();
                PointF center = opw.getCenter();

                opw.setPanEnabled(false);
                opw.setScaleAndCenter(scale, center);

                opw.invalidate();
            }
            else {
                drag = false;
            }
        }
        else drag = false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent pos) {
        //viewSwitcher.showNext();
        if (opw.isReady()) {
            // If there is no pins we are definitely creating a new one
            Pin pin = opw.getPin();

            if (pin != null) {
                // If tabbed position is inside collision radius of a pin -> edit this pin
                if (opw.euclidanViewDistance(pin, pos.getX(), pos.getY()) < pin.getCollisionRadius()){
                    opw.invalidate();
                    drag = true;
                    setUpDragPin(pos);
                }
            }

        }
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        //nothing now
    }

    @Override /*This zooms in and out*/
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }
}
