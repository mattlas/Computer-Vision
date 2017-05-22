package com.example.treemapp;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by oskar on 2017-05-11.
 */

public class OriginalOnTouchListener implements View.OnTouchListener {

    private OnePinView opw;

    public OriginalOnTouchListener(OnePinView opw) {
        this.opw = opw;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (opw.isReady()) {
            PointF newPosition = opw.viewToSourceCoord(motionEvent.getX(), motionEvent.getY());
            opw.setPinXandY(newPosition.x, newPosition.y);
            opw.invalidate();
        }
        //return gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private void dragPinRelease() {

        //opw.updatePinInFile(opw.getPin()); TODO update this

        opw.setPanEnabled(true);
        opw.setZoomEnabled(true);
        opw.invalidate();
    }

}
