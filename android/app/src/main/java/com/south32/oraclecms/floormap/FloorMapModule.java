package com.south32.oraclecms.floormap;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;

public class FloorMapModule extends ReactContextBaseJavaModule {
    public FloorMapModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "FloorMap";
    }

    // Public API
    @ReactMethod
    public void show(String message, int duration) {

    }
}