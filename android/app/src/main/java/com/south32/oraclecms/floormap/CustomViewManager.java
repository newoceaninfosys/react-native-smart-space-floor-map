package com.south32.oraclecms.floormap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.Object;

public class CustomViewManager extends SimpleViewManager<CustomZoom> {

    private String _uri;
    private Uri _URI;
    private JSONArray _desks = new JSONArray();
    private JSONObject _activeDesk = null;
    private CustomZoom _zoom;
    private Bitmap _buffer;
    private Bitmap _copyBm;
    private Paint _paint;
    private Canvas _canvas;
    private String _activeColor = "#ff0000";
    private int _radius = 1;
    private int _strokeWidth = 5;

    private void _drawDesks(CustomZoom zoom) {
        _copyBm = _buffer.copy(Bitmap.Config.ARGB_8888, true);
        _canvas = new Canvas(_copyBm);

        float activeDeskX = 0.0f, activeDeskY = 0.0f;
        if (_activeDesk != null) {
            try {
                activeDeskX = (float) _activeDesk.getDouble("x");
            } catch (Exception e) {
                Log.i("Parse X Error", e.getMessage());
            }
            try {
                activeDeskY = (float) _activeDesk.getDouble("y");
            } catch (Exception e) {
                Log.i("Parse Y Error", e.getMessage());
            }
        }
        for (int i = 0; i < _desks.length(); i++) {
            JSONObject o = null;
            try {
                o = _desks.getJSONObject(i);
            } catch (Exception e) {
                Log.i("Get Json Object Error", e.getMessage());
            }
            if (o != null) {
                float x = 0.0f, y = 0.0f;
                String color = "#cccccc";
                try {
                    x = (float) o.getDouble("x");
                } catch (Exception e) {
                    Log.i("Parse X Error", e.getMessage());
                }
                try {
                    y = (float) o.getDouble("y");
                } catch (Exception e) {
                    Log.i("Parse Y Error", e.getMessage());
                }
                try {
                    color = o.getString("color");
                } catch (Exception e) {
                    Log.i("Parse color Error", e.getMessage());
                }

                if (_activeDesk != null && activeDeskX == x && activeDeskY == y) {
                    // Draw the stroke
                    Paint strokeP = new Paint();
                    strokeP.setStyle(Paint.Style.STROKE);
                    strokeP.setColor(Color.parseColor(_activeColor));
                    strokeP.setStrokeWidth(_strokeWidth);
                    strokeP.setStrokeCap(Paint.Cap.ROUND);
                    this._canvas.drawCircle(x, y, this._radius, strokeP);
                }

                this._paint.setColor(Color.parseColor(color));

                this._canvas.drawCircle(x, y, this._radius, this._paint);
            }
        }
        zoom.setImageBitmap(_copyBm);
    }

    @Override
    public String getName() {
        return "FloorMap";
    }

    @Override
    protected CustomZoom createViewInstance(final ThemedReactContext context) {
        _zoom = new CustomZoom(context);
        _zoom.setMyTouchHandler(new MyTouchHandler() {
            @Override
            public void onTouch(JSONObject obj) {
                // Callbacks
                Double touchX = 0.0, touchY = 0.0;
                try {
                    touchX = obj.getDouble("x");
                } catch (Exception e) {
                    Log.i("Get X", e.getMessage());
                }
                try {
                    touchY = obj.getDouble("y");
                } catch (Exception e) {
                    Log.i("Get Y", e.getMessage());
                }
                Log.i("MyTouchHandler onTouch", touchX.toString());
                Log.i("MyTouchHandler onTouch", touchY.toString());

                for (int i = 0; i < _desks.length(); i++) {
                    JSONObject o = null;
                    try {
                        o = _desks.getJSONObject(i);
                    } catch (Exception e) {
                        Log.i("Get Json Object Error", e.getMessage());
                    }
                    if (o != null) {
                        float deskX = 0.0f, deskY = 0.0f;
                        try {
                            deskX = (float) o.getDouble("x");
                        } catch (Exception e) {
                            Log.i("Parse X Error", e.getMessage());
                        }
                        try {
                            deskY = (float) o.getDouble("y");
                        } catch (Exception e) {
                            Log.i("Parse Y Error", e.getMessage());
                        }

                        boolean found = Math.sqrt(Math.pow(deskX - touchX, 2) + Math.pow(deskY - touchY, 2)) < _radius;

                        if (found) {
                            Log.i("FOUND", "X: " + deskX + " - Y " + deskY);
                            _activeDesk = o;
                            _drawDesks(_zoom);

                            // send event to React-Native
                            WritableMap map = Arguments.createMap();
                            try {
                                map = JsonConvert.jsonToReact(o);
                            }catch(org.json.JSONException ex){
                                System.out.println("Exception: " + ex);
                            }
                            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                    .emit("FloorMapOnSelect", map);
                            break;
                        }
                    }
                }
            }
        });
        _paint = new Paint();
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(Color.GRAY);
        return _zoom;
    }

    @ReactProp(name = "uri")
    public void setUri(CustomZoom zoom, String uri) {
        _uri = uri;
        _URI = Uri.fromFile(new File(_uri.substring(7)));
        _buffer = BitmapFactory.decodeFile(_uri.substring(7));
        this._drawDesks(zoom);
    }

    @ReactProp(name = "maxZoom")
    public void setMaxZoom(CustomZoom zoom, int maxZoom) {
        zoom.setMaxZoom(maxZoom);
    }

    @ReactProp(name = "radius")
    public void setRadius(CustomZoom zoom, int radius) {
        _radius = radius;
        this._drawDesks(zoom);
    }

    @ReactProp(name = "strokeWidth")
    public void setStrokeWidth(CustomZoom zoom, int strokeWidth) {
        _strokeWidth = strokeWidth;
        this._drawDesks(zoom);
    }

    @ReactProp(name = "activeColor")
    public void setActiveColor(CustomZoom zoom, String color) {
        _activeColor = color;
        this._drawDesks(zoom);
    }

    @ReactProp(name = "desks")
    public void setDesks(CustomZoom zoom, ReadableArray desks) {
        _desks = new JSONArray();

        try {
            _desks = ArrayUtil.toJSONArray(desks);
        } catch (Exception e) {
            Log.i("Parse Desk Array Error", e.getMessage());
        }

        this._drawDesks(zoom);
    }

    @ReactProp(name = "activeDesk")
    public void setActiveDesk(CustomZoom zoom, ReadableMap desk) {
        try {
            _activeDesk = MapUtil.toJSONObject(desk);
        } catch (Exception e) {
            Log.i("Parse ActiveDesk Error", e.getMessage());
        }
        this._drawDesks(zoom);
    }
}
