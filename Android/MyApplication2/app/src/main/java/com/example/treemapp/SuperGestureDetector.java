package com.example.treemapp;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by oskar on 2017-05-11.
 */

public class SuperGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private MainActivity main;

    public SuperGestureDetector(MainActivity main) {
        this.main = main;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent pos) {
        //viewSwitcher.showNext();
        if (main.getImageView().isReady()) {
            // If there is no pins we are definitely creating a new one
            if (main.getImageView().listIsEmpty()) {
                main.showOriginals(pos); //makes pin, creates menu
            } else {
                // Closest pin to tapped position
                Pin closestPin = main.getImageView().getClosestPin(pos.getX(), pos.getY());

                // If tabbed position is inside collision radius of a pin -> edit this pin
                if (main.getImageView().euclidanViewDistance(closestPin, pos.getX(), pos.getY()) < closestPin.getCollisionRadius()){
                    main.getOverlay().edit(closestPin);
                    main.getImageView().invalidate();
                    // otherwise make new pin
                } else {
                    //If the user's presses not near an existing pin we make a new one
                    main.showOriginals(pos);
                }
            }

        } else {
            Toast.makeText(main.getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        if (main.getImageView().isReady()) {
            main.setUpDragPin(e);
        } else {
            Toast.makeText(main.getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
        }
    }

    @Override /*This zooms in and out*/
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }
}
