package com.example.treemapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by oskar on 2017-05-02.
 * This class (and the OriginalImageView class) were made primarily for debugging purposes
 */

public class OriginalImageActivity extends AppCompatActivity {
    private Button mBtGoBack;
    private static final String TAG = OriginalImageActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.original_image);
        ImageView imageView = (ImageView) findViewById(R.id.originalImageView);
        Intent intent = getIntent();
        float x = intent.getFloatExtra("x", 2);
        float y = intent.getFloatExtra("y", 0);
        String fileName = intent.getStringExtra("fileName");


        File bitmapFile = new File(fileName);
        if (!bitmapFile.exists() || bitmapFile.isDirectory()){

            Log.e(TAG, "Image not found: "+fileName);
            finish();

        } else {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), bmOptions);

            Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);

            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(bitmap, 0, 0, null);
            draw(x, y, fileName, tempCanvas);


            imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));


            mBtGoBack = (Button) findViewById(R.id.btn_original_go_back);

            mBtGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void draw(float x, float y, String fileName, Canvas tempCanvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        tempCanvas.drawCircle(x, y, 20, paint);
        Paint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setShadowLayer(3, 2, 2, Color.BLACK);
        tempCanvas.drawText(fileName, 20, 20, textPaint);
        tempCanvas.drawLine(tempCanvas.getWidth() / 2, 0, tempCanvas.getWidth() / 2, tempCanvas.getHeight(), paint);
        tempCanvas.drawLine(0, tempCanvas.getHeight() / 2, tempCanvas.getWidth(), tempCanvas.getHeight() / 2, paint);
    }
}
