package com.south32.oraclecms.floormap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

interface MyTouchHandler {
    void onTouch(JSONObject obj);
}

public class CustomZoom extends ImageView {

    private boolean drawFirstTime = false;

    Matrix matrix;

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 5f;
    float[] m;

    int viewWidth, viewHeight;
    static final int CLICK = 3;
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

    ScaleGestureDetector mScaleDetector;

    Context context;

    MyTouchHandler myTouchHandler;

    public CustomZoom(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public CustomZoom(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    public boolean performClick(MotionEvent event) {
        Log.i("performClick", "CLICKED " + event.getX() + " - " + event.getY());
        super.performClick();
        double coorXTouch = ((event.getX() - m[2]) / m[0]);
        double coorYTouch = ((event.getY() - m[5]) / m[4]);
        JSONObject coor = new JSONObject();
        try{coor.put("x", coorXTouch);}catch(Exception e){Log.i("Assign X", e.getMessage());}
        try{coor.put("y", coorYTouch);}catch(Exception e){Log.i("Assign Y", e.getMessage());}
        Log.i("coor x pos", coorXTouch+"");
        Log.i("coor Y pos", coorYTouch+"");
        this.myTouchHandler.onTouch(coor);
        return true;
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

//                int touchX = (int) event.getX();
//                int touchY = (int) event.getY();
//                Log.i("aaaaa", "x: " + touchX + ", y: " + touchY + ", Action: "+ event.getAction());

                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        last.set(curr);
                        start.set(last);
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth,
                                    origWidth * saveScale);
                            float fixTransY = getFixDragTrans(deltaY, viewHeight,
                                    origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK) {
                            performClick(event);
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }

                setImageMatrix(matrix);
                invalidate();
                return true; // indicate event was handled
            }

        });
    }

    public void setMyTouchHandler(MyTouchHandler handler){
        myTouchHandler = handler;
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth
                    || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2,
                        viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor,
                        detector.getFocusX(), detector.getFocusY());

            fixTrans();


            return true;
        }
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight
                * saveScale);

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            // Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0
                    || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            Log.i("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            float bmScaleWidth = (scale * (float) bmWidth);
            float bmScaleHeight = (scale * (float) bmHeight);
            float bmViewTopLeftXPost = (((float) viewWidth - bmScaleWidth) / 2);

            Log.i("View size", "Width: " + viewWidth + ", Height: " + viewHeight);
            Log.i("Image Scale size", "Width: " + bmScaleWidth + ", Height: " + bmScaleHeight);

            // Center the image
            float redundantYSpace = (float) viewHeight - bmScaleHeight;
            float redundantXSpace = (float) viewWidth - bmScaleWidth;
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

//        if (drawFirstTime == false){
//            drawFirstTime = true;
//
//            Paint paint  = new Paint(Paint.LINEAR_TEXT_FLAG);
//            paint.setColor(Color.RED);
//            paint.setTextSize(12.0F);
//
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sample);
//
//            canvas.drawBitmap(bitmap, 0, 0, paint);
//            canvas.drawLine(0,0,500,500,paint);
//        }


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
            Log.i("a", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
