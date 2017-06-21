package com.south32.oraclecms.floormap;

import android.net.Uri;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.io.File;

public class CustomViewManager extends SimpleViewManager<CustomZoom> {
    @Override
    public String getName() {
        return "FloorMap";
    }

    @Override
    protected CustomZoom createViewInstance(ThemedReactContext context) {
        CustomZoom zoom = new CustomZoom(context);
        zoom.setImageResource(R.mipmap.sample);
        return zoom;
    }

    @ReactProp(name = "uri")
    public void setUri(CustomZoom zoom, String src) {
        zoom.setImageURI(Uri.fromFile(new File(src)));
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
