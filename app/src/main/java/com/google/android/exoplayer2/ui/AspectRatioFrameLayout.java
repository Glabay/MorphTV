package com.google.android.exoplayer2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class AspectRatioFrameLayout extends FrameLayout {
    private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f;
    public static final int RESIZE_MODE_FILL = 3;
    public static final int RESIZE_MODE_FIT = 0;
    public static final int RESIZE_MODE_FIXED_HEIGHT = 2;
    public static final int RESIZE_MODE_FIXED_WIDTH = 1;
    public static final int RESIZE_MODE_ZOOM = 4;
    private int resizeMode;
    private float videoAspectRatio;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResizeMode {
    }

    public AspectRatioFrameLayout(Context context) {
        this(context, null);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.resizeMode = 0;
        if (attributeSet != null) {
            context = context.getTheme().obtainStyledAttributes(attributeSet, C0762R.styleable.AspectRatioFrameLayout, 0, 0);
            try {
                this.resizeMode = context.getInt(C0762R.styleable.AspectRatioFrameLayout_resize_mode, 0);
            } finally {
                context.recycle();
            }
        }
    }

    public void setAspectRatio(float f) {
        if (this.videoAspectRatio != f) {
            this.videoAspectRatio = f;
            requestLayout();
        }
    }

    public int getResizeMode() {
        return this.resizeMode;
    }

    public void setResizeMode(int i) {
        if (this.resizeMode != i) {
            this.resizeMode = i;
            requestLayout();
        }
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.resizeMode != 3) {
            if (this.videoAspectRatio > 0) {
                i = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();
                float f = (float) i;
                float f2 = (float) measuredHeight;
                float f3 = (this.videoAspectRatio / (f / f2)) - 1.0f;
                if (Math.abs(f3) > 0.01f) {
                    int i3 = this.resizeMode;
                    if (i3 != 4) {
                        switch (i3) {
                            case 1:
                                measuredHeight = (int) (f / this.videoAspectRatio);
                                break;
                            case 2:
                                i = (int) (f2 * this.videoAspectRatio);
                                break;
                            default:
                                if (f3 <= 0.0f) {
                                    i = (int) (f2 * this.videoAspectRatio);
                                    break;
                                } else {
                                    measuredHeight = (int) (f / this.videoAspectRatio);
                                    break;
                                }
                        }
                    } else if (f3 > 0.0f) {
                        i = (int) (f2 * this.videoAspectRatio);
                    } else {
                        measuredHeight = (int) (f / this.videoAspectRatio);
                    }
                    super.onMeasure(MeasureSpec.makeMeasureSpec(i, 1073741824), MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824));
                }
            }
        }
    }
}
