package com.south32.oraclecms.floormap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class CustomViewManager extends SimpleViewManager<CustomZoom> {
    @Override
    public String getName() {
        return "FloorMap";
    }

    @Override
    protected CustomZoom createViewInstance(ThemedReactContext context){
        CustomZoom zoom = new CustomZoom(context);
//        Bitmap bitmap = drawOnImage(R.mipmap.sample);
        zoom.setImageResource(R.mipmap.sample);
        return zoom;
    }

//    private Bitmap drawOnImage(int drawableId) {
//
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
//                .copy(Bitmap.Config.ARGB_8888, true);
//
//
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.RED);
//
//        Canvas canvas = new Canvas(bm);
////        canvas.drawLine(0,0,200,200,paint);
//        canvas.drawCircle(100, 100, 20, paint);
//        canvas.drawCircle(200, 200, 20, paint);
//        canvas.drawCircle(300, 300, 20, paint);
////        return new BitmapDrawable(getResources(), bm);
//        return bm;
//    }
}
