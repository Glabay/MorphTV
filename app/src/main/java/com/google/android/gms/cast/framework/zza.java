package com.google.android.gms.cast.framework;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;

public final class zza extends zzg {
    private final AppVisibilityListener zzgo;

    public zza(AppVisibilityListener appVisibilityListener) {
        this.zzgo = appVisibilityListener;
    }

    public final void onAppEnteredBackground() {
        this.zzgo.onAppEnteredBackground();
    }

    public final void onAppEnteredForeground() {
        this.zzgo.onAppEnteredForeground();
    }

    public final int zzm() {
        return 12451009;
    }

    public final IObjectWrapper zzn() {
        return ObjectWrapper.wrap(this.zzgo);
    }
}
