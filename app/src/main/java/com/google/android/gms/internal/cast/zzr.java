package com.google.android.gms.internal.cast;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.IntroductoryOverlay.Builder;
import com.google.android.gms.cast.framework.IntroductoryOverlay.OnOverlayDismissedListener;
import com.google.android.gms.cast.framework.IntroductoryOverlay.zza;

public final class zzr extends RelativeLayout implements IntroductoryOverlay {
    private Activity zzhv;
    private OnOverlayDismissedListener zzhz;
    private final boolean zziq;
    private boolean zzis;
    private int zziv;
    private final zzu zziw;

    public zzr(Builder builder) {
        this(builder, null, C0782R.attr.castIntroOverlayStyle);
    }

    private zzr(Builder builder, AttributeSet attributeSet, int i) {
        super(builder.getActivity(), null, i);
        this.zzhv = builder.getActivity();
        this.zziq = builder.zzae();
        this.zzhz = builder.zzac();
        TypedArray obtainStyledAttributes = this.zzhv.getTheme().obtainStyledAttributes(null, C0782R.styleable.CastIntroOverlay, i, C0782R.style.CastIntroOverlay);
        if (builder.zzab() != null) {
            Rect rect = new Rect();
            builder.zzab().getGlobalVisibleRect(rect);
            this.zziw = new zzu();
            this.zziw.f40x = rect.centerX();
            this.zziw.f41y = rect.centerY();
            zzu zzu = this.zziw;
            Xfermode porterDuffXfermode = new PorterDuffXfermode(Mode.MULTIPLY);
            Paint paint = new Paint();
            paint.setColor(-1);
            paint.setAlpha(0);
            paint.setXfermode(porterDuffXfermode);
            paint.setAntiAlias(true);
            zzu.zziz = paint;
            this.zziw.zzja = builder.zzah();
            if (this.zziw.zzja == 0.0f) {
                this.zziw.zzja = obtainStyledAttributes.getDimension(C0782R.styleable.CastIntroOverlay_castFocusRadius, 0.0f);
            }
        } else {
            this.zziw = null;
        }
        LayoutInflater.from(this.zzhv).inflate(C0782R.layout.cast_intro_overlay, this);
        this.zziv = builder.zzad();
        if (this.zziv == 0) {
            this.zziv = obtainStyledAttributes.getColor(C0782R.styleable.CastIntroOverlay_castBackgroundColor, Color.argb(0, 0, 0, 0));
        }
        TextView textView = (TextView) findViewById(C0782R.id.textTitle);
        if (!TextUtils.isEmpty(builder.zzaf())) {
            textView.setText(builder.zzaf());
            int resourceId = obtainStyledAttributes.getResourceId(C0782R.styleable.CastIntroOverlay_castTitleTextAppearance, 0);
            if (resourceId != 0) {
                textView.setTextAppearance(this.zzhv, resourceId);
            }
        }
        CharSequence zzag = builder.zzag();
        if (TextUtils.isEmpty(zzag)) {
            zzag = obtainStyledAttributes.getString(C0782R.styleable.CastIntroOverlay_castButtonText);
        }
        i = obtainStyledAttributes.getColor(C0782R.styleable.CastIntroOverlay_castButtonBackgroundColor, Color.argb(0, 0, 0, 0));
        Button button = (Button) findViewById(C0782R.id.button);
        button.setText(zzag);
        button.getBackground().setColorFilter(i, Mode.MULTIPLY);
        int resourceId2 = obtainStyledAttributes.getResourceId(C0782R.styleable.CastIntroOverlay_castButtonTextAppearance, 0);
        if (resourceId2 != 0) {
            button.setTextAppearance(this.zzhv, resourceId2);
        }
        button.setOnClickListener(new zzs(this));
        obtainStyledAttributes.recycle();
        setFitsSystemWindows(true);
    }

    private final void zzao() {
        zza.zzd(this.zzhv);
        if (this.zzhz != null) {
            this.zzhz.onOverlayDismissed();
            this.zzhz = null;
        }
        remove();
    }

    protected final void dispatchDraw(Canvas canvas) {
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap);
        canvas2.drawColor(this.zziv);
        if (this.zziw != null) {
            canvas2.drawCircle((float) this.zziw.f40x, (float) this.zziw.f41y, this.zziw.zzja, this.zziw.zziz);
        }
        canvas.drawBitmap(createBitmap, 0.0f, 0.0f, null);
        createBitmap.recycle();
        super.dispatchDraw(canvas);
    }

    protected final void onDetachedFromWindow() {
        if (this.zzhv != null) {
            this.zzhv = null;
        }
        super.onDetachedFromWindow();
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public final void remove() {
        if (this.zzhv != null) {
            ((ViewGroup) this.zzhv.getWindow().getDecorView()).removeView(this);
            this.zzhv = null;
        }
        this.zzhz = null;
    }

    public final void show() {
        if (this.zzhv != null && !zzn.zzg(this.zzhv)) {
            if (this.zziq && zza.zze(this.zzhv)) {
                this.zzhv = null;
                this.zzhz = null;
                return;
            }
            if (!this.zzis) {
                this.zzis = true;
                ((ViewGroup) this.zzhv.getWindow().getDecorView()).addView(this);
            }
        }
    }
}
