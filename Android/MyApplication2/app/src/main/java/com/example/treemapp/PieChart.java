package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 5dv115 on 5/15/17.
 */

public class PieChart extends AppCompatImageView {

    private static final int length = 64;
    private int width;
    private int height;
    private float[] verts;
    private ArrayList<Statista.SpeciesCount> speciesCountList;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSpeciesList(ArrayList<Statista.SpeciesCount> speciesCountList){
        this.speciesCountList=speciesCountList;
    }

    @Override
    public void onDraw(Canvas canvas){
        drawPieChart(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void drawPieChart(Canvas canvas) {

        float[] verts = getVerts();
        int[] colors = getColors();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //float[] verts = {this.width / 2, this.height / 2, 500, 20, 10, 500};
        //int colors[] = {Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY};

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(8);


        canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, verts.length, verts, 0, null, 0, colors, 0, null, 0, 0, p);

    }

    public int[] getColors(){
        int[] colors = new int[length * 4 + 2];
        colors[0]=Color.LTGRAY;
        colors[1]=Color.LTGRAY;
        for (int i = 2; i < colors.length; i += 4){
            colors[i]=Color.YELLOW;
            colors[i+1]=Color.BLUE;
            colors[i+2]=Color.YELLOW;
            colors[i+3]=Color.YELLOW;
        }
        return colors;

    }

    public float[] getVerts() {
        verts = new float[length * 4 + 2];

        float cx = width / 2;
        float cy = height / 2;
        float radius = Math.min(width, height) * 0.375f;
        float angle;
        float angle2;

        verts[0] = cx;
        verts[1] = cy;

        for (int i = 2; i < verts.length; i += 4) {
            angle = (i / (float) length) * (float) Math.PI * 2;
            angle2 = ((i + 2) / (float) length) * (float) Math.PI * 2;

            //first x and y
            verts[i] = cx + (float) Math.cos(angle) * radius;
            verts[i+1] = cy + (float) Math.sin(angle) * radius;

            //second x and y
            verts[i+2] = cx + (float) Math.cos(angle2) * radius;
            verts[i+3] = cy + (float) Math.sin(angle2) * radius;
        }

        return verts;
    }
}
