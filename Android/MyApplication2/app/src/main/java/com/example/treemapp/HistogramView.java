package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.graphics.Rect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adam on 5/15/17.
 */

public class HistogramView extends AppCompatImageView {

    private HashMap<Rect,Paint> barList;

    public HistogramView(Context context){
        super(context);
        init();

    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    
    @Override
    protected void onDraw(Canvas canvas){
        for (Map.Entry<Rect, Paint> bar : barList.entrySet()){
            //canvas.drawColor(Color.MAGENTA);
            canvas.drawRect(bar.getKey(), bar.getValue());
        }
    }

    private void init(){
        barList=new HashMap<>();
        Paint p = new Paint();
        p.setColor(Color.MAGENTA);
        Rect r = new Rect(10,10,100,50);
        barList.put(new Rect(10,10,100,50),new Paint());
    }

}
