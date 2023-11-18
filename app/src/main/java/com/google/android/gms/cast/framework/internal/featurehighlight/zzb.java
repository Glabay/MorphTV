package com.google.android.gms.cast.framework.internal.featurehighlight;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

final class zzb extends SimpleOnGestureListener {
    private final /* synthetic */ zza zzjq;

    zzb(zza zza) {
        this.zzjq = zza;
    }

    public final boolean onSingleTapUp(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (!(this.zzjq.zza(x, y) && this.zzjq.zzjg.zzb(x, y))) {
            this.zzjq.zzjo.dismiss();
        }
        return true;
    }
}
