package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * This view is an extention of the SubsamplingScaleImageView where you can add
 * Created by oskar on 2017-04-24.
 */

public class PinView extends SubsamplingScaleImageView {

    private final String TAG = PinView.class.getSimpleName();
    private Paint filled;
    private List<Pin> pins;
    private int pinIndex;
    private FileHandler fileHandler;
    private Paint unfilled;

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
        filled = new Paint();
        filled.setColor(Color.WHITE);
        filled.setAlpha(255);
        filled.setStrokeWidth(1);

        unfilled = new Paint();
        unfilled.setColor(Color.WHITE);
        unfilled.setAlpha(255);
        unfilled.setStrokeWidth(5);
        unfilled.setStyle(Paint.Style.STROKE);

        pins = new LinkedList<Pin>();
        pinIndex=0;
    }

    public void setFileHandler(FileHandler f){
        this.fileHandler=f;
    }

    /**
     * Adds a pin to the active pin list (DOESN'T save to the file)
     * @param pin the pin to add to the list
     */
    public void addPin(Pin pin) {
        pin.setId("tree-"+String.format("%03d",pinIndex)); // Padding the number with zeros (ie 003,012,123)
        pins.add(pin);
        pinIndex++;
    }


    public void deletePin(Pin pin)
    {
        pins.remove(pin);

        fileHandler.removeLine(pin.getId());
    }

    public boolean listIsEmpty () {
        return pins == null || pins.isEmpty();
    }

    /**
     * Used when editing pins
     * @param x - mosaic coordinates
     * @param y - mosaic coordinates
     * @return the closest pin
     */
    public Pin getClosestPin (double x, double y) {
        // Coordinates of tapped position: x, y
        PointF pointF = new PointF();
        pointF.set((float)x, (float)y);

        // Coordinates of pins
        PointF point;

        double minimalDistance = 10000000;
        Pin pin = new Pin(pointF,"");

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

            if (p.isDragged()) {
                canvas.drawCircle((int) point.x, (int) point.y, p.getCollisionRadius(), unfilled);
                filled.setAlpha(128);
            }
            else filled.setAlpha(255);

            canvas.drawCircle((int) point.x, (int) point.y, p.getRadius(), filled);
        }
    }

    public boolean updatePositionInFile(Pin pin) {

        // First find the pin in the file
        List<Pin> list = fileHandler.getPinList();
        int lineToUpdate = -1;
        for (int i = 0; lineToUpdate==-1 && i<list.size(); i++){
            if (list.get(i).getId() == pin.getId()) {
                lineToUpdate=i;
            }
        }

        // then remove the line and put a new one back in
        if (lineToUpdate != -1){
            fileHandler.removeLine(lineToUpdate);
            fileHandler.addLine(pin.getCSV());
            return true;
        }
        else return false;
    }
}
