package com.google.android.gms.cast.framework.internal.featurehighlight;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.internal.cast.zzes;

class InnerZoneDrawable extends Drawable {
    private float centerX;
    private float centerY;
    private final Paint zziz = new Paint();
    private float zzja;
    private final Rect zzje = new Rect();
    private final Paint zzjz = new Paint();
    private final int zzka;
    private final int zzkb;
    private float zzkc = 1.0f;
    private float zzkd;
    private float zzke;

    public InnerZoneDrawable(Context context) {
        Resources resources = context.getResources();
        this.zzka = resources.getDimensionPixelSize(C0782R.dimen.cast_libraries_material_featurehighlight_inner_radius);
        this.zzkb = resources.getInteger(C0782R.integer.cast_libraries_material_featurehighlight_pulse_base_alpha);
        this.zziz.setAntiAlias(true);
        this.zziz.setStyle(Style.FILL);
        this.zzjz.setAntiAlias(true);
        this.zzjz.setStyle(Style.FILL);
        this.zziz.setColor(-1);
        this.zzjz.setColor(-1);
        invalidateSelf();
    }

    public void draw(Canvas canvas) {
        if (this.zzke > 0.0f) {
            float f = this.zzja * this.zzkd;
            this.zzjz.setAlpha((int) (((float) this.zzkb) * this.zzke));
            canvas.drawCircle(this.centerX, this.centerY, f, this.zzjz);
        }
        canvas.drawCircle(this.centerX, this.centerY, this.zzja * this.zzkc, this.zziz);
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
        this.zziz.setAlpha(i);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.zziz.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Keep
    public void setPulseAlpha(float f) {
        this.zzke = f;
        invalidateSelf();
    }

    @Keep
    public void setPulseScale(float f) {
        this.zzkd = f;
        invalidateSelf();
    }

    @Keep
    public void setScale(float f) {
        this.zzkc = f;
        invalidateSelf();
    }

    public final void zza(Rect rect) {
        this.zzje.set(rect);
        this.centerX = this.zzje.exactCenterX();
        this.centerY = this.zzje.exactCenterY();
        this.zzja = Math.max((float) this.zzka, Math.max(((float) this.zzje.width()) / 2.0f, ((float) this.zzje.height()) / 2.0f));
        invalidateSelf();
    }

    public final Animator zzau() {
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("scale", new float[]{0.0f});
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{0});
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("pulseScale", new float[]{0.0f});
        PropertyValuesHolder ofFloat3 = PropertyValuesHolder.ofFloat("pulseAlpha", new float[]{0.0f});
        Animator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{ofFloat, ofInt, ofFloat2, ofFloat3});
        ofPropertyValuesHolder.setInterpolator(zzes.zzdj());
        return ofPropertyValuesHolder.setDuration(200);
    }
}
