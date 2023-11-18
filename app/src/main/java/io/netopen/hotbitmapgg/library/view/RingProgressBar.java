package io.netopen.hotbitmapgg.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import io.netopen.hotbitmapgg.library.C1394R;

public class RingProgressBar extends View {
    public static final int FILL = 1;
    public static final int STROKE = 0;
    private int centre;
    private int height;
    private OnProgressListener mOnProgressListener;
    private int max;
    private int padding;
    private Paint paint;
    private int progress;
    private int radius;
    private int result;
    private int ringColor;
    private int ringProgressColor;
    private float ringWidth;
    private int style;
    private int textColor;
    private boolean textIsShow;
    private float textSize;
    private int width;

    public interface OnProgressListener {
        void progressToComplete();
    }

    public RingProgressBar(Context context) {
        this(context, null);
    }

    public RingProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RingProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.result = 0;
        this.padding = 0;
        this.paint = new Paint();
        this.result = dp2px(100);
        context = context.obtainStyledAttributes(attributeSet, C1394R.styleable.RingProgressBar);
        this.ringColor = context.getColor(C1394R.styleable.RingProgressBar_ringColor, ViewCompat.MEASURED_STATE_MASK);
        this.ringProgressColor = context.getColor(C1394R.styleable.RingProgressBar_ringProgressColor, -1);
        this.textColor = context.getColor(C1394R.styleable.RingProgressBar_textColor, ViewCompat.MEASURED_STATE_MASK);
        this.textSize = context.getDimension(C1394R.styleable.RingProgressBar_textSize, 16.0f);
        this.ringWidth = context.getDimension(C1394R.styleable.RingProgressBar_ringWidth, 5.0f);
        this.max = context.getInteger(C1394R.styleable.RingProgressBar_max, 100);
        this.textIsShow = context.getBoolean(C1394R.styleable.RingProgressBar_textIsShow, true);
        this.style = context.getInt(C1394R.styleable.RingProgressBar_style, 0);
        context.recycle();
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.centre = getWidth() / 2;
        this.radius = (int) (((float) this.centre) - (this.ringWidth / 2.0f));
        drawCircle(canvas);
        drawTextContent(canvas);
        drawProgress(canvas);
    }

    private void drawCircle(Canvas canvas) {
        this.paint.setColor(this.ringColor);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(this.ringWidth);
        this.paint.setAntiAlias(true);
        canvas.drawCircle((float) this.centre, (float) this.centre, (float) this.radius, this.paint);
    }

    private void drawTextContent(Canvas canvas) {
        this.paint.setStrokeWidth(0.0f);
        this.paint.setColor(this.textColor);
        this.paint.setTextSize(this.textSize);
        this.paint.setTypeface(Typeface.DEFAULT);
        int i = (int) ((((float) this.progress) / ((float) this.max)) * 100.0f);
        Paint paint = this.paint;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        stringBuilder.append("%");
        float measureText = paint.measureText(stringBuilder.toString());
        if (this.textIsShow && i != 0 && this.style == 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(i);
            stringBuilder.append("%");
            canvas.drawText(stringBuilder.toString(), ((float) this.centre) - (measureText / 2.0f), ((float) this.centre) + (this.textSize / 2.0f), this.paint);
        }
    }

    private void drawProgress(Canvas canvas) {
        this.paint.setStrokeWidth(this.ringWidth);
        this.paint.setColor(this.ringProgressColor);
        RectF rectF = new RectF((float) (this.centre - this.radius), (float) (this.centre - this.radius), (float) (this.centre + this.radius), (float) (this.centre + this.radius));
        RectF rectF2 = new RectF((((float) (this.centre - this.radius)) + this.ringWidth) + ((float) this.padding), (((float) (this.centre - this.radius)) + this.ringWidth) + ((float) this.padding), (((float) (this.centre + this.radius)) - this.ringWidth) - ((float) this.padding), (((float) (this.centre + this.radius)) - this.ringWidth) - ((float) this.padding));
        switch (this.style) {
            case 0:
                this.paint.setStyle(Style.STROKE);
                this.paint.setStrokeCap(Cap.ROUND);
                canvas.drawArc(rectF, -90.0f, (float) ((this.progress * 360) / this.max), false, this.paint);
                return;
            case 1:
                this.paint.setStyle(Style.FILL_AND_STROKE);
                this.paint.setStrokeCap(Cap.ROUND);
                if (this.progress != 0) {
                    canvas.drawArc(rectF2, -90.0f, (float) ((this.progress * 360) / this.max), true, this.paint);
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int mode = MeasureSpec.getMode(i);
        i = MeasureSpec.getSize(i);
        int mode2 = MeasureSpec.getMode(i2);
        i2 = MeasureSpec.getSize(i2);
        if (mode == Integer.MIN_VALUE) {
            this.width = this.result;
        } else {
            this.width = i;
        }
        if (mode2 == Integer.MIN_VALUE) {
            this.height = this.result;
        } else {
            this.height = i2;
        }
        setMeasuredDimension(this.width, this.height);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.width = i;
        this.height = i2;
        this.padding = dp2px(5);
    }

    public synchronized int getMax() {
        return this.max;
    }

    public synchronized void setMax(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("The max progress of 0");
        }
        this.max = i;
    }

    public synchronized int getProgress() {
        return this.progress;
    }

    public synchronized void setProgress(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("The progress of 0");
        }
        if (i > this.max) {
            i = this.max;
        }
        if (i <= this.max) {
            this.progress = i;
            postInvalidate();
        }
        if (i == this.max && this.mOnProgressListener != 0) {
            this.mOnProgressListener.progressToComplete();
        }
    }

    public int getRingColor() {
        return this.ringColor;
    }

    public void setRingColor(int i) {
        this.ringColor = i;
    }

    public int getRingProgressColor() {
        return this.ringProgressColor;
    }

    public void setRingProgressColor(int i) {
        this.ringProgressColor = i;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float f) {
        this.textSize = f;
    }

    public float getRingWidth() {
        return this.ringWidth;
    }

    public void setRingWidth(float f) {
        this.ringWidth = f;
    }

    public int dp2px(int i) {
        return (int) ((((float) i) * getContext().getResources().getDisplayMetrics().density) + 1056964608);
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.mOnProgressListener = onProgressListener;
    }
}
