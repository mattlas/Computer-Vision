package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * This view is an extention of the SubsamplingScaleImageView where you can add
 * Created by oskar on 2017-04-24.
 */

public class PinView extends SubsamplingScaleImageView {

    private final String TAG = PinView.class.getSimpleName();
    private Paint paint;
    private List<Pin> pins;
    private int pinIndex;
    private FileHandler fileHandler;

    // I'm keeping the filehandler as an attribute to PinView to make interfacing between them easier.

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public PinView(Context context) {
        super(context);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setStrokeWidth(50);

        pins = new LinkedList<Pin>();
        pinIndex=0;
    }

    public void setFileHandler(FileHandler f){
        this.fileHandler=f;
    }

    public void addPin(Pin pin) {
        pin.setId("tree-"+String.format("%03d",pinIndex)); // Padding the number with zeros (ie 003,012,123)
        pins.add(pin);
        pinIndex++;

        fileHandler.addLine(pin.getCSV());
    }


    public void deletePin(Pin pin)
    {
        pins.remove(pin);

        fileHandler.removeLine(pin.getId());

        pin = null;

    }

    public boolean listIsEmpty () {
        if (pins == null || pins.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Pin getClosestPin (double x, double y) {
        // Coordinates of tapped position: x, y
        PointF pointF = new PointF();
        pointF.set((float)x, (float)y);

        // Coordinates of pins
        PointF point;

        double minimalDistance = 10000000;
        Pin pin = new Pin(pointF);

        // Check all pins in list, find pin with minimal distance to tabbed point
        for(Pin p : pins) {
            point = sourceToViewCoord(p.getPoint());
            // Distance
            double distance = Math.sqrt((Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2)));
            if (distance < minimalDistance) {
                minimalDistance = distance;
                pin = p;
            }
        }
        return pin;
    }

    public void changePinLocation (Pin pin, PointF point) {


    }

    /**
     * Loads the pins from the tree list into memory
     */
    public void loadPinsFromFile(){
        pins = fileHandler.getPinList();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        PointF point;

        for(Pin p : pins) {
            point = sourceToViewCoord(p.getPoint());
            canvas.drawCircle((int) point.x, (int) point.y, p.getRadius(), paint);
        }
    }
}
