package com.github.angads25.filepicker.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.github.angads25.filepicker.C0619R;

public class MaterialCheckbox extends View {
    private RectF bounds;
    private boolean checked;
    private Context context;
    private int minDim;
    private OnCheckedChangeListener onCheckedChangeListener;
    private Paint paint;
    private Path tick;

    /* renamed from: com.github.angads25.filepicker.widget.MaterialCheckbox$1 */
    class C06251 implements OnClickListener {
        C06251() {
        }

        public void onClick(View view) {
            MaterialCheckbox.this.setChecked(MaterialCheckbox.this.checked ^ 1);
            MaterialCheckbox.this.onCheckedChangeListener.onCheckedChanged(MaterialCheckbox.this, MaterialCheckbox.this.isChecked());
        }
    }

    public MaterialCheckbox(Context context) {
        super(context);
        initView(context);
    }

    public MaterialCheckbox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public MaterialCheckbox(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        this.checked = null;
        this.tick = new Path();
        this.paint = new Paint();
        this.bounds = new RectF();
        setOnClickListener(new C06251());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isChecked()) {
            this.paint.reset();
            this.paint.setAntiAlias(true);
            this.bounds.set((float) (this.minDim / 10), (float) (this.minDim / 10), (float) (this.minDim - (this.minDim / 10)), (float) (this.minDim - (this.minDim / 10)));
            if (VERSION.SDK_INT >= 23) {
                this.paint.setColor(getResources().getColor(C0619R.color.colorAccent, this.context.getTheme()));
            } else {
                this.paint.setColor(getResources().getColor(C0619R.color.colorAccent));
            }
            canvas.drawRoundRect(this.bounds, (float) (this.minDim / 8), (float) (this.minDim / 8), this.paint);
            this.paint.setColor(Color.parseColor("#FFFFFF"));
            this.paint.setStrokeWidth((float) (this.minDim / 10));
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeJoin(Join.BEVEL);
            canvas.drawPath(this.tick, this.paint);
            return;
        }
        this.paint.reset();
        this.paint.setAntiAlias(true);
        this.bounds.set((float) (this.minDim / 10), (float) (this.minDim / 10), (float) (this.minDim - (this.minDim / 10)), (float) (this.minDim - (this.minDim / 10)));
        this.paint.setColor(Color.parseColor("#C1C1C1"));
        canvas.drawRoundRect(this.bounds, (float) (this.minDim / 8), (float) (this.minDim / 8), this.paint);
        this.bounds.set((float) (this.minDim / 5), (float) (this.minDim / 5), (float) (this.minDim - (this.minDim / 5)), (float) (this.minDim - (this.minDim / 5)));
        this.paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawRect(this.bounds, this.paint);
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        i = getMeasuredHeight();
        i2 = getMeasuredWidth();
        this.minDim = Math.min(i2, i);
        this.bounds.set((float) (this.minDim / 10), (float) (this.minDim / 10), (float) (this.minDim - (this.minDim / 10)), (float) (this.minDim - (this.minDim / 10)));
        this.tick.moveTo((float) (this.minDim / 4), (float) (this.minDim / 2));
        this.tick.lineTo(((float) this.minDim) / 2.5f, (float) (this.minDim - (this.minDim / 3)));
        this.tick.moveTo(((float) this.minDim) / 2.75f, ((float) this.minDim) - (((float) this.minDim) / 3.25f));
        this.tick.lineTo((float) (this.minDim - (this.minDim / 4)), (float) (this.minDim / 3));
        setMeasuredDimension(i2, i);
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean z) {
        this.checked = z;
        invalidate();
    }

    public void setOnCheckedChangedListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
}
