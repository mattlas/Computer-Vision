package com.example.treemapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by oskar on 2017-05-02.
 */
public class OriginalImageView extends android.support.v7.widget.AppCompatImageView{

    private static final String TAG = OriginalImageView.class.getSimpleName();

    private Paint paint;
    private float x = 20; //default
    private float y = 20; //default
    private String fileString;

    public OriginalImageView(Context context) {
        super(context);
        init();
    }

    public OriginalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OriginalImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
<<<<<<< HEAD
        paint.setColor(Color.WHITE);
=======
        paint.setColor(Color.BLUE);
>>>>>>> a65dc693b060dd0c3d43948ab2a81e6b2024621c
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawCircle(x, y, 20, paint);
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
        File f = new File(fileString);
        if (f.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(fileString);
            //Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, 200, 200, true);
            setImageBitmap(bmp);
        }
        else {
            Log.e(TAG, "Can't find file: " + this.fileString);
        }
    }
}
