package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oskar on 2017-04-24.
 */

public class PinView extends SubsamplingScaleImageView {

    private final String TAG = PinView.class.getSimpleName();
    private Paint paint;
    private List<Pin> pins;

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
    }

    public void addPin(Pin pin) {
        pins.add(pin);
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
            canvas.drawCircle((int) point.x, (int) point.y, 30, paint);
            Log.e(TAG, "Here");
        }
    }

}
