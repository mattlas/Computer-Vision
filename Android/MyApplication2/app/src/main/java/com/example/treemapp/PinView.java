package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by oskar on 2017-04-24.
 */

public class PinView extends SubsamplingScaleImageView {
    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public PinView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAlpha(255);
        p.setColor(Color.BLUE);
        canvas.drawCircle(300, 300, 50, p);
    }
}
