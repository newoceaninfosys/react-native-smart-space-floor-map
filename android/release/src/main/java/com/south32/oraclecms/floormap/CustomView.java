package com.south32.oraclecms.floormap;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

public class CustomView extends LinearLayout {
    public CustomView(Context context) {
        super(context);
    }

    public void init() {
        inflate(getContext(), R.layout.custom_layout, this);
    }
}
