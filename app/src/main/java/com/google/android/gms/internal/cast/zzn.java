package com.google.android.gms.internal.cast;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.RelativeLayout;
import com.google.android.gms.cast.framework.C0782R;
import com.google.android.gms.cast.framework.IntroductoryOverlay;
import com.google.android.gms.cast.framework.IntroductoryOverlay.Builder;
import com.google.android.gms.cast.framework.IntroductoryOverlay.OnOverlayDismissedListener;
import com.google.android.gms.cast.framework.internal.featurehighlight.zza;
import com.google.android.gms.cast.framework.internal.featurehighlight.zzi;

public final class zzn extends RelativeLayout implements IntroductoryOverlay {
    private int color;
    private Activity zzhv;
    private View zzhw;
    private String zzhy;
    private OnOverlayDismissedListener zzhz;
    private final boolean zziq;
    private zza zzir;
    private boolean zzis;

    @TargetApi(15)
    public zzn(Builder builder) {
        super(builder.getActivity());
        this.zzhv = builder.getActivity();
        this.zziq = builder.zzae();
        this.zzhz = builder.zzac();
        this.zzhw = builder.zzab();
        this.zzhy = builder.zzaf();
        this.color = builder.zzad();
    }

    private final void reset() {
        removeAllViews();
        this.zzhv = null;
        this.zzhz = null;
        this.zzhw = null;
        this.zzir = null;
        this.zzhy = null;
        this.color = 0;
        this.zzis = false;
    }

    static boolean zzg(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService("accessibility");
        return accessibilityManager != null && accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
    }

    protected final void onDetachedFromWindow() {
        reset();
        super.onDetachedFromWindow();
    }

    public final void remove() {
        if (this.zzis) {
            ((ViewGroup) this.zzhv.getWindow().getDecorView()).removeView(this);
            reset();
        }
    }

    public final void show() {
        if (this.zzhv != null && this.zzhw != null && !this.zzis && !zzg(this.zzhv)) {
            if (this.zziq && IntroductoryOverlay.zza.zze(this.zzhv)) {
                reset();
                return;
            }
            this.zzir = new zza(this.zzhv);
            if (this.color != 0) {
                this.zzir.zzg(this.color);
            }
            addView(this.zzir);
            zzi zzi = (zzi) this.zzhv.getLayoutInflater().inflate(C0782R.layout.cast_help_text, this.zzir, false);
            zzi.setText(this.zzhy, null);
            this.zzir.zza(zzi);
            this.zzir.zza(this.zzhw, null, true, new zzo(this));
            this.zzis = true;
            ((ViewGroup) this.zzhv.getWindow().getDecorView()).addView(this);
            this.zzir.zza(null);
        }
    }
}
