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
    private GestureDetector gs;

    public OriginalOnTouchListener(OnePinView opw, GestureDetector gs) {
        this.opw = opw;
        this.gs = gs;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (opw.isReady()) {
            PointF newPosition = opw.viewToSourceCoord(motionEvent.getX(), motionEvent.getY());
            opw.setPinXandY(newPosition.x, newPosition.y);
            opw.invalidate();
        }

        return gs.onTouchEvent(motionEvent);
    }

    private void dragPinRelease() {

        //opw.updatePinInFile(opw.getPin()); TODO update this

        opw.setPanEnabled(true);
        opw.setZoomEnabled(true);
        opw.invalidate();
    }

}
