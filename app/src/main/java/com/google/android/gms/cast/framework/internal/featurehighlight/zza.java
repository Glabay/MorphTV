package com.google.android.gms.cast.framework.internal.featurehighlight;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.internal.cast.zzej;
import com.google.android.gms.internal.cast.zzes;
import com.google.android.gms.internal.cast.zzev;

public final class zza extends ViewGroup {
    private View targetView;
    private final int[] zzjd = new int[2];
    private final Rect zzje = new Rect();
    private final Rect zzjf = new Rect();
    private final OuterHighlightDrawable zzjg;
    private final InnerZoneDrawable zzjh;
    private zzi zzji;
    @Nullable
    private View zzjj;
    @Nullable
    private Animator zzjk;
    private final zzj zzjl;
    private final GestureDetectorCompat zzjm;
    @Nullable
    private GestureDetectorCompat zzjn;
    private zzh zzjo;
    private boolean zzjp;

    public zza(Context context) {
        super(context);
        setId(C0782R.id.cast_featurehighlight_view);
        setWillNotDraw(false);
        this.zzjh = new InnerZoneDrawable(context);
        this.zzjh.setCallback(this);
        this.zzjg = new OuterHighlightDrawable(context);
        this.zzjg.setCallback(this);
        this.zzjl = new zzj(this);
        this.zzjm = new GestureDetectorCompat(context, new zzb(this));
        this.zzjm.setIsLongpressEnabled(false);
        setVisibility(8);
    }

    private final void zza(Animator animator) {
        if (this.zzjk != null) {
            this.zzjk.cancel();
        }
        this.zzjk = animator;
        this.zzjk.start();
    }

    private final boolean zza(float f, float f2) {
        return this.zzjf.contains(Math.round(f), Math.round(f2));
    }

    private final Animator zzat() {
        InnerZoneDrawable innerZoneDrawable = this.zzjh;
        Animator animatorSet = new AnimatorSet();
        Animator duration = ObjectAnimator.ofFloat(innerZoneDrawable, "scale", new float[]{1.0f, 1.1f}).setDuration(500);
        Animator duration2 = ObjectAnimator.ofFloat(innerZoneDrawable, "scale", new float[]{1.1f, 1.0f}).setDuration(500);
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("pulseScale", new float[]{1.1f, 2.0f});
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("pulseAlpha", new float[]{1.0f, 0.0f});
        Animator duration3 = ObjectAnimator.ofPropertyValuesHolder(innerZoneDrawable, new PropertyValuesHolder[]{ofFloat, ofFloat2}).setDuration(500);
        animatorSet.play(duration);
        animatorSet.play(duration2).with(duration3).after(duration);
        animatorSet.setInterpolator(zzes.zzdk());
        animatorSet.setStartDelay(500);
        zzej.zza(animatorSet, -1, null);
        return animatorSet;
    }

    protected final boolean checkLayoutParams(LayoutParams layoutParams) {
        return layoutParams instanceof MarginLayoutParams;
    }

    protected final LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-2, -2);
    }

    public final LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new MarginLayoutParams(getContext(), attributeSet);
    }

    protected final LayoutParams generateLayoutParams(LayoutParams layoutParams) {
        return new MarginLayoutParams(layoutParams);
    }

    protected final void onDraw(Canvas canvas) {
        canvas.save();
        this.zzjg.draw(canvas);
        this.zzjh.draw(canvas);
        if (this.targetView != null) {
            if (this.targetView.getParent() != null) {
                Bitmap createBitmap = Bitmap.createBitmap(this.targetView.getWidth(), this.targetView.getHeight(), Config.ARGB_8888);
                this.targetView.draw(new Canvas(createBitmap));
                int color = this.zzjg.getColor();
                int red = Color.red(color);
                int green = Color.green(color);
                color = Color.blue(color);
                for (int i = 0; i < createBitmap.getHeight(); i++) {
                    for (int i2 = 0; i2 < createBitmap.getWidth(); i2++) {
                        int pixel = createBitmap.getPixel(i2, i);
                        if (Color.alpha(pixel) != 0) {
                            createBitmap.setPixel(i2, i, Color.argb(Color.alpha(pixel), red, green, color));
                        }
                    }
                }
                canvas.drawBitmap(createBitmap, (float) this.zzje.left, (float) this.zzje.top, null);
            }
            canvas.restore();
            return;
        }
        throw new IllegalStateException("Neither target view nor drawable was set");
    }

    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.targetView == null) {
            throw new IllegalStateException("Target view must be set before layout");
        }
        if (this.targetView.getParent() != null) {
            int[] iArr = this.zzjd;
            View view = this.targetView;
            getLocationInWindow(iArr);
            int i5 = iArr[0];
            int i6 = iArr[1];
            view.getLocationInWindow(iArr);
            iArr[0] = iArr[0] - i5;
            iArr[1] = iArr[1] - i6;
        }
        this.zzje.set(this.zzjd[0], this.zzjd[1], this.zzjd[0] + this.targetView.getWidth(), this.zzjd[1] + this.targetView.getHeight());
        this.zzjf.set(i, i2, i3, i4);
        this.zzjg.setBounds(this.zzjf);
        this.zzjh.setBounds(this.zzjf);
        this.zzjl.zza(this.zzje, this.zzjf);
    }

    protected final void onMeasure(int i, int i2) {
        setMeasuredDimension(resolveSize(MeasureSpec.getSize(i), i), resolveSize(MeasureSpec.getSize(i2), i2));
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.zzjp = this.zzje.contains((int) motionEvent.getX(), (int) motionEvent.getY());
        }
        if (this.zzjp) {
            if (this.zzjn != null) {
                this.zzjn.onTouchEvent(motionEvent);
                if (actionMasked == 1) {
                    motionEvent = MotionEvent.obtain(motionEvent);
                    motionEvent.setAction(3);
                }
            }
            if (this.targetView.getParent() != null) {
                this.targetView.onTouchEvent(motionEvent);
                return true;
            }
        }
        this.zzjm.onTouchEvent(motionEvent);
        return true;
    }

    protected final boolean verifyDrawable(Drawable drawable) {
        if (!(super.verifyDrawable(drawable) || drawable == this.zzjg || drawable == this.zzjh)) {
            if (drawable != null) {
                return false;
            }
        }
        return true;
    }

    public final void zza(View view, @Nullable View view2, boolean z, zzh zzh) {
        this.targetView = (View) zzev.checkNotNull(view);
        this.zzjj = null;
        this.zzjo = (zzh) zzev.checkNotNull(zzh);
        this.zzjn = new GestureDetectorCompat(getContext(), new zzc(this, view, true, zzh));
        this.zzjn.setIsLongpressEnabled(false);
        setVisibility(4);
    }

    public final void zza(zzi zzi) {
        this.zzji = (zzi) zzev.checkNotNull(zzi);
        addView(zzi.asView(), 0);
    }

    public final void zza(@Nullable Runnable runnable) {
        addOnLayoutChangeListener(new zzd(this, null));
    }

    public final void zzap() {
        if (this.targetView == null) {
            throw new IllegalStateException("Target view must be set before animation");
        }
        setVisibility(0);
        ObjectAnimator.ofFloat(this.zzji.asView(), "alpha", new float[]{0.0f, 1.0f}).setDuration(350).setInterpolator(zzes.zzdi());
        Animator zzc = this.zzjg.zzc(this.zzje.exactCenterX() - this.zzjg.getCenterX(), this.zzje.exactCenterY() - this.zzjg.getCenterY());
        InnerZoneDrawable innerZoneDrawable = this.zzjh;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("scale", new float[]{0.0f, 1.0f});
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{0, 255});
        Animator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(innerZoneDrawable, new PropertyValuesHolder[]{ofFloat, ofInt});
        ofPropertyValuesHolder.setInterpolator(zzes.zzdi());
        Animator duration = ofPropertyValuesHolder.setDuration(350);
        Animator animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{r1, zzc, duration});
        animatorSet.addListener(new zze(this));
        zza(animatorSet);
    }

    final View zzaq() {
        return this.zzji.asView();
    }

    final OuterHighlightDrawable zzar() {
        return this.zzjg;
    }

    final InnerZoneDrawable zzas() {
        return this.zzjh;
    }

    public final void zzb(@Nullable Runnable runnable) {
        ObjectAnimator.ofFloat(this.zzji.asView(), "alpha", new float[]{0.0f}).setDuration(200).setInterpolator(zzes.zzdj());
        float exactCenterX = this.zzje.exactCenterX() - this.zzjg.getCenterX();
        float exactCenterY = this.zzje.exactCenterY() - this.zzjg.getCenterY();
        OuterHighlightDrawable outerHighlightDrawable = this.zzjg;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("scale", new float[]{0.0f});
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{0});
        PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat("translationX", new float[]{0.0f, exactCenterX});
        PropertyValuesHolder ofFloat3 = PropertyValuesHolder.ofFloat("translationY", new float[]{0.0f, exactCenterY});
        Animator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(outerHighlightDrawable, new PropertyValuesHolder[]{ofFloat, ofFloat2, ofFloat3, ofInt});
        ofPropertyValuesHolder.setInterpolator(zzes.zzdj());
        ofPropertyValuesHolder = ofPropertyValuesHolder.setDuration(200);
        Animator zzau = this.zzjh.zzau();
        Animator animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{r0, ofPropertyValuesHolder, zzau});
        animatorSet.addListener(new zzg(this, runnable));
        zza(animatorSet);
    }

    public final void zzc(@Nullable Runnable runnable) {
        ObjectAnimator.ofFloat(this.zzji.asView(), "alpha", new float[]{0.0f}).setDuration(200).setInterpolator(zzes.zzdj());
        OuterHighlightDrawable outerHighlightDrawable = this.zzjg;
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("scale", new float[]{1.125f});
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("alpha", new int[]{0});
        Animator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(outerHighlightDrawable, new PropertyValuesHolder[]{ofFloat, ofInt});
        ofPropertyValuesHolder.setInterpolator(zzes.zzdj());
        ofPropertyValuesHolder = ofPropertyValuesHolder.setDuration(200);
        Animator zzau = this.zzjh.zzau();
        Animator animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{r0, ofPropertyValuesHolder, zzau});
        animatorSet.addListener(new zzf(this, runnable));
        zza(animatorSet);
    }

    public final void zzg(@ColorInt int i) {
        this.zzjg.setColor(i);
    }
}
