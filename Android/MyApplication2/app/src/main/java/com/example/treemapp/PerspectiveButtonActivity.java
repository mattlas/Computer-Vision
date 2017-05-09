package com.example.treemapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Lena on 05.05.17.
 */

public class PerspectiveButtonActivity extends AppCompatActivity implements View.OnClickListener {
    private PointF latestTouch;
    private MainActivity mainAct;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perspective);
        final String TAG = PerspectiveButtonActivity.class.getSimpleName();

        latestTouch = MainActivity.latestTouch;

        mainAct = new MainActivity(); //TODO, does this need to be here

        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName");

        ImageView imageView = (ImageView) findViewById(R.id.perspective_image);

        File bitmapFile = new File(fileName);
        if (!bitmapFile.exists() || bitmapFile.isDirectory()) {
            Log.e(TAG, "Image not found: '" + fileName + "'");
            finish();

        } else {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), bmOptions);
            Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);

            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(bitmap, 0, 0, null);
            draw(0, 0, fileName,tempCanvas);
            imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        }

        Button mBtGoBack = (Button) findViewById(R.id.btn_perspective_cancel);

        mBtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        openPerspective();
    }

    private void draw(float x, float y, String fileName, Canvas tempCanvas) {

        /*
        Change this to draw on the image
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        Paint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setShadowLayer(3, 2, 2, Color.BLACK);
        tempCanvas.drawText(fileName, 20, 20, textPaint);

        tempCanvas.drawLine(x, 0, x, tempCanvas.getHeight(), paint);
        tempCanvas.drawLine(0, y, tempCanvas.getWidth(), y, paint);
        */
    }

    public void openPerspective () {

    }

    @Override
    public void onClick(View v) {
        return;
    }
}
