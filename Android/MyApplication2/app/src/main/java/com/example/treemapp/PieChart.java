package com.example.treemapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
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

    private final int[] colorScheme = {
            0xFF50514F,
            0xFFF25F5C,
            0xFFFFE066,
            0xFF247BA0,
            0xFF70C1B3,
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
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(8);


        TextPaint tp = new TextPaint();
        tp.setTextSize(32f);
        tp.setTextAlign(Paint.Align.LEFT);
        int yAlign = -(int) (tp.getFontMetrics().top + tp.getFontMetrics().bottom) / 2;


        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, verts.length, verts, 0, null, 0, colors, 0, null, 0, 0, p);

        int size = 26;
        float yy = height * 0.1f;
        int offsetY;
        int count;

        for (int i = 0; i < speciesCountList.size(); i++) {
            offsetY = i * 50;
            count = speciesCountList.get(i).getAmount();
            p.setColor(colorScheme[i%colorScheme.length]);
            tp.setColor(p.getColor());

            canvas.drawRect(width * 0.65f, yy + (offsetY) - size / 2, width * 0.65f + size, yy + (offsetY) + size / 2, p);
            canvas.drawText(speciesCountList.get(i).getSpecimen() + ": " + count, width * 0.65f + 50, yy + offsetY + yAlign, tp);
        }
    }

    public int[] getColors(){
        int[] colors = new int[length * 6];

        int total=getTotal();

        int lastIndex=0;
        float limit=0;
        float startValue = 0;

        for (int i = 0; i<speciesCountList.size(); i++){
            Statista.SpeciesCount speciesCount = speciesCountList.get(i);

            limit += speciesCount.getAmount() *  (colors.length/12) / (float) total;

            float j;
            for (j = startValue; j < limit && (j + limit) < colors.length; j+=1){
                for (int vertex = 0; vertex < 6; vertex++) {
                    colors[(int) Math.floor(j) * 6 + vertex] = colorScheme[i % colorScheme.length]; //the mod part is only if there are more than 5 colors
                }
            }
            startValue = limit;
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

        float cx = width * .31f;
        float cy = height * .31f;
        float radius = Math.min(width, height) * 0.3f;
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
