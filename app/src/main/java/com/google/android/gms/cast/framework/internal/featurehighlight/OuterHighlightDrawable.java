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
import android.support.annotation.ColorInt;
import android.support.annotation.Keep;
import android.support.v4.graphics.ColorUtils;
import android.util.TypedValue;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.internal.cast.zzes;
import com.google.android.gms.internal.cast.zzeu;
import org.mozilla.universalchardet.prober.HebrewProber;

class OuterHighlightDrawable extends Drawable {
    private float centerX;
    private float centerY;
    private final Paint zziz = new Paint();
    private float zzja;
    private final Rect zzje = new Rect();
    private float zzkc = 1.0f;
    private final Rect zzkf = new Rect();
    private final int zzkl;
    private final int zzkm;
    private final int zzkn;
    private float zzko = 0.0f;
    private float zzkp = 0.0f;
    private int zzkq = HebrewProber.NORMAL_PE;

    public OuterHighlightDrawable(Context context) {
        int alphaComponent;
        if (PlatformVersion.isAtLeastLollipop()) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16843827, typedValue, true);
            alphaComponent = ColorUtils.setAlphaComponent(typedValue.data, HebrewProber.NORMAL_PE);
        } else {
            alphaComponent = context.getResources().getColor(C0782R.color.f36xfbd34f47);
        }
        setColor(alphaComponent);
        this.zziz.setAntiAlias(true);
        this.zziz.setStyle(Style.FILL);
        Resources resources = context.getResources();
        this.zzkl = resources.getDimensionPixelSize(C0782R.dimen.cast_libraries_material_featurehighlight_center_threshold);
        this.zzkm = resources.getDimensionPixelSize(C0782R.dimen.f38xd1678993);
        this.zzkn = resources.getDimensionPixelSize(C0782R.dimen.cast_libraries_material_featurehighlight_outer_padding);
    }

    private static float zza(float f, float f2, Rect rect) {
        float f3 = (float) rect.left;
        float f4 = (float) rect.top;
        float f5 = (float) rect.right;
        float f6 = (float) rect.bottom;
        float zza = zzeu.zza(f, f2, f3, f4);
        f4 = zzeu.zza(f, f2, f5, f4);
        f5 = zzeu.zza(f, f2, f5, f6);
        f = zzeu.zza(f, f2, f3, f6);
        if (zza > f4 && zza > f5 && zza > f) {
            f = zza;
        } else if (f4 > f5 && f4 > f) {
            f = f4;
        } else if (f5 > f) {
            f = f5;
        }
        return (float) Math.ceil((double) f);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(this.centerX + this.zzko, this.centerY + this.zzkp, this.zzja * this.zzkc, this.zziz);
    }

    public int getAlpha() {
        return this.zziz.getAlpha();
    }

    public final float getCenterX() {
        return this.centerX;
    }

    public final float getCenterY() {
        return this.centerY;
    }

    @ColorInt
    public final int getColor() {
        return this.zziz.getColor();
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
        this.zziz.setAlpha(i);
        invalidateSelf();
    }

    public final void setColor(@ColorInt int i) {
        this.zziz.setColor(i);
        this.zzkq = this.zziz.getAlpha();
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.zziz.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Keep
    public void setScale(float f) {
        this.zzkc = f;
        invalidateSelf();
    }

    @Keep
    public void setTranslationX(float f) {
        this.zzko = f;
        invalidateSelf();
    }

    @Keep
    public void setTranslationY(float f) {
        this.zzkp = f;
        invalidateSelf();
    }

    public final void zzb(Rect rect, Rect rect2) {
        this.zzje.set(rect);
        this.zzkf.set(rect2);
        float exactCenterX = rect.exactCenterX();
        float exactCenterY = rect.exactCenterY();
        Rect bounds = getBounds();
        if (Math.min(exactCenterY - ((float) bounds.top), ((float) bounds.bottom) - exactCenterY) < ((float) this.zzkl)) {
            this.centerX = exactCenterX;
            this.centerY = exactCenterY;
        } else {
            this.centerX = ((exactCenterX > bounds.exactCenterX() ? 1 : (exactCenterX == bounds.exactCenterX() ? 0 : -1)) <= 0 ? 1 : null) != null ? rect2.exactCenterX() + ((float) this.zzkm) : rect2.exactCenterX() - ((float) this.zzkm);
            this.centerY = rect2.exactCenterY();
        }
        this.zzja = ((float) this.zzkn) + Math.max(zza(this.centerX, this.centerY, rect), zza(this.centerX, this.centerY, rect2));
        invalidateSelf();
    }

    public final boolean zzb(float f, float f2) {
        return zzeu.zza(f, f2, this.centerX, this.centerY) < this.zzja;
    }

    public final Animator zzc(float f, float f2) {
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("scale", new float[]{0.0f, 1.0f});
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("translationX", new float[]{f, 0.0f});
        PropertyValuesHolder ofFloat3 = PropertyValuesHolder.ofFloat("translationY", new float[]{f2, 0.0f});
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{0, this.zzkq});
        Animator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{ofFloat, ofFloat2, ofFloat3, ofInt});
        ofPropertyValuesHolder.setInterpolator(zzes.zzdi());
        return ofPropertyValuesHolder.setDuration(350);
    }
}
