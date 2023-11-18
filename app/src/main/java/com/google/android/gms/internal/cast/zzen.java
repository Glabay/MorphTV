package com.google.android.gms.internal.cast;

import android.annotation.TargetApi;
import android.view.Choreographer.FrameCallback;

public abstract class zzen {
    private Runnable zzyj;
    private FrameCallback zzyk;

    public abstract void doFrame(long j);

    @TargetApi(16)
    final FrameCallback zzdg() {
        if (this.zzyk == null) {
            this.zzyk = new zzeo(this);
        }
        return this.zzyk;
    }

    final Runnable zzdh() {
        if (this.zzyj == null) {
            this.zzyj = new zzep(this);
        }
        return this.zzyj;
    }
}
