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

/*    private final int[] colorScheme = {
            0x50514F,
            0xF25F5C,
            0xFFE066,
            0x247BA0,
            0x70C1B3,
    };*/

private int[] colorScheme = {
        Color.MAGENTA,
        Color.CYAN,
        Color.YELLOW,
        Color.GREEN,
        Color.RED


};

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
        p.setColor(Color.MAGENTA);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(8);


        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, verts.length, verts, 0, null, 0, colors, 0, null, 0, 0, p);

        int size = 26;
        float yy = height * 0.1f;
        canvas.drawRect(width * 0.75f, yy - size/2, width * 0.75f + size, yy + size/2, p);
        canvas.drawText("Birch", width *0.75f + 50, yy, p);

    }

    public int[] getColors(){
        int color1 = Color.CYAN;
        int color2 = Color.MAGENTA;

        int[] colors = new int[length * 6];


        int total=getTotal();

        int lastIndex=0;

        for (int i = 0; i<speciesCountList.size(); i++){
            Statista.SpeciesCount speciesCount = speciesCountList.get(i);
            float triangles = speciesCount.getAmount() *colors.length/ total;
            for (int j=lastIndex;j<triangles; j++){
                colors[j]=colorScheme[i%5];
                lastIndex=j;
            }
        }

        return colors;

    }
    public int getTotal(){
        int total=0;
        for (Statista.SpeciesCount speciesCount : speciesCountList){
            total+=speciesCount.getAmount();
        }
        return total;
    }

    public float[] getVerts() {
        verts = new float[length * 6];

        float cx = width / 2;
        float cy = height / 2;
        float radius = Math.min(width, height) * 0.25f;
        float angle;
        float angle2;

        for (int i = 0; i < verts.length; i += 6) {
            angle = (i / (float) verts.length) * (float) Math.PI * 2;
            angle2 = ((i + 6) / (float) verts.length) * (float) Math.PI * 2;

            verts[i] = cx;
            verts[i+1] = cy;


            //first x and y
            verts[i+2] = cx + (float) Math.cos(angle) * radius;
            verts[i+3] = cy + (float) Math.sin(angle) * radius;

            //second x and y
            verts[i+4] = cx + (float) Math.cos(angle2) * radius;
            verts[i+5] = cy + (float) Math.sin(angle2) * radius;
        }

        return verts;
    }
}
