package com.google.android.gms.internal.cast;

import android.annotation.TargetApi;
import android.view.Choreographer;

@TargetApi(16)
final class zzer extends zzel {
    private Choreographer zzym = Choreographer.getInstance();

    public final void zza(zzen zzen) {
        this.zzym.postFrameCallback(zzen.zzdg());
    }
}
