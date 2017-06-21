package com.south32.oraclecms.floormap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

public class MainActivity extends Activity {
    private CustomZoom imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);

        imageView = (CustomZoom) findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.sample);
        
//        Bitmap bitmap = drawOnImage(R.mipmap.sample);
//        imageView.setImageBitmap(bitmap);
    }

    private Bitmap drawOnImage(int drawableId) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);


        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        Canvas canvas = new Canvas(bm);
//        canvas.drawLine(0,0,200,200,paint);
        canvas.drawCircle(100, 100, 20, paint);
        canvas.drawCircle(200, 200, 20, paint);
        canvas.drawCircle(300, 300, 20, paint);
//        return new BitmapDrawable(getResources(), bm);
        return bm;
    }
}
