package com.example.treemapp;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by oskar on 2017-05-11.
 */

public class OriginalGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private OnePinView opw;

    public OriginalGestureDetector(OnePinView main) {
        this.opw = main;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent pos) {
        //viewSwitcher.showNext();
        if (opw..isReady()) {
            // If there is no pins we are definitely creating a new one
            Pin pin = opw.getPin();

            if (pin != null) {

                // If tabbed position is inside collision radius of a pin -> edit this pin
                if (opw.euclidanViewDistance(pin, pos.getX(), pos.getY()) < pin.getCollisionRadius()){
                    opw.invalidate();
                    // otherwise make new pin
                } else {
                    //If the user's presses not near an existing pin we make a new one
                    opw.showOriginals(pos);
                }
            }

        } else {
            Toast.makeText(opw.getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        if (opw.getImageView().isReady()) {
            opw.setUpDragPin(e);
        } else {
            Toast.makeText(opw.getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
        }
    }

    @Override /*This zooms in and out*/
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }
}
