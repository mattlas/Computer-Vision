package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adam on 5/15/17.
 */

public class HistogramView extends AppCompatImageView {

    private static final String TAG=HistogramView.class.getSimpleName();

    private static final int MAX_STAPLES = 20;

    private static final int marginY = 100;
    private static final int marginX = 100;

    private static final int axesColor=Color.GRAY;


    private int width;
    private int height;


    private final int[] colorScheme = {
            0xFF50514F,
            0xFFF25F5C,
            0xFFFFE066,
            0xFF247BA0,
            0xFF70C1B3,
    };


    public Statista.Histogram histogram;
    private boolean ready = false;


    public HistogramView(Context context){
        super(context);

    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HistogramView(Context context, Statista stats){
        this(context);
        this.histogram=stats.generateHistogram(MAX_STAPLES);
    }

    public void setHistogram(Statista statista){
        this.histogram=statista.generateHistogram(MAX_STAPLES);
        ready = true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        drawHistogram(canvas);
    }


    private void drawHistogram(Canvas canvas){


        if (!ready){
            return;
        }

        Paint axesPaint=new Paint();
        axesPaint.setColor(axesColor);
        axesPaint.setStyle(Paint.Style.STROKE);
        axesPaint.setStrokeWidth(8);

        // - Then the labels

        Paint textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(35);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int xAxisLength=width-2*marginX;
        int yAxisLength=height-2*marginY;

        int yText=height-marginY/2;

        //TODO uncomment once Histogram data is correct
        int size=histogram.size();

        double staple=histogram.widthOfStaple();
        double min=histogram.getMin();
        double max=histogram.getMax();

        int[] values=histogram.getValues();
        int i;

        int maxStapleSize=(int)staple;//histogram.mostPopulatedStapleSize();

        for (i=0; i<size; i++){

            int scaledValue = values[i] * yAxisLength/maxStapleSize;
            int xText=marginX+i*xAxisLength/size;
            canvas.drawText(String.format("%.3g%n", Math.min(min+staple*i,max)),xText,yText,textPaint);

                Rect r = new Rect(xText, height - marginY - scaledValue,xText+xAxisLength/size,height-marginY);
                Paint rectPaint = new Paint();
                //fill
                rectPaint.setColor(colorScheme[i%colorScheme.length]);
                canvas.drawRect(r,rectPaint);
                //border
                rectPaint.setColor(Color.WHITE);
                rectPaint.setStyle(Paint.Style.STROKE);
                rectPaint.setStrokeWidth(3);
                canvas.drawRect(r,rectPaint);

        }

        canvas.drawText(String.format("%.3g%n", Math.min(min+staple*i,max)),width-marginX,yText,textPaint);

        // Axes

        canvas.drawLine(marginX,marginY,marginX,height-marginY,axesPaint);

        // X axis
        canvas.drawLine(marginX,height-marginY,width-marginX,height-marginY,axesPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }



}
