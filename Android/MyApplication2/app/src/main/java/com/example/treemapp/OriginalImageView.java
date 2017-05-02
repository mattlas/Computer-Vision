package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

/**
 * Created by oskar on 2017-05-02.
 */
public class OriginalImageView extends android.support.v7.widget.AppCompatImageView{

    private Paint paint;

    public OriginalImageView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawCircle(20, 20, 20, paint);
    }
}