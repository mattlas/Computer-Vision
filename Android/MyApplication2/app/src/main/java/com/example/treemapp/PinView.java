package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.List;

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
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setStrokeWidth(50);

        canvas.drawCircle(300, 300, 30, paint);
    }
}
