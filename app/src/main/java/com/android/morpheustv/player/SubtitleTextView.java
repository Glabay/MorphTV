package com.android.morpheustv.player;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class SubtitleTextView extends AppCompatTextView {
    public SubtitleTextView(Context context) {
        super(context);
    }

    public SubtitleTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SubtitleTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void draw(Canvas canvas) {
        int defaultColor = getTextColors().getDefaultColor();
        setTextColor(ViewCompat.MEASURED_STATE_MASK);
        getPaint().setStrokeWidth(3.0f);
        getPaint().setStyle(Style.STROKE);
        super.draw(canvas);
        setTextColor(defaultColor);
        getPaint().setStrokeWidth(0.0f);
        getPaint().setStyle(Style.FILL);
        super.draw(canvas);
    }
}
