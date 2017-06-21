package com.south32.oraclecms.floormap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CustomImageView extends ImageView {

    /**
     * @param context
     */
    public CustomImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setBackgroundColor(0xFFFFFF);
    }


    /**
     * @param context
     * @param attrs
     */
    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        System.out.println("Painting content");
        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(12.0F);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sample);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawLine(0, 0, 500, 500, paint);

//        saveBitmap();
    }

    private void saveBitmap() {
        this.setDrawingCacheEnabled(true);
        Bitmap b = this.getDrawingCache();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("myphoto.png");
            b.compress(Bitmap.CompressFormat.PNG, 95, fos);
        } catch (Exception e) {
            Log.d("a", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.d("Hello Android", "Got a touch event: " + event.getAction());
        return super.onTouchEvent(event);

    }
}
