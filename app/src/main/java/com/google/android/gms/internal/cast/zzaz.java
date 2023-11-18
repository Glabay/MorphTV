package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.Cast.Listener;

final class zzaz extends Listener {
    private final /* synthetic */ zzay zzqj;

    zzaz(zzay zzay) {
        this.zzqj = zzay;
    }

    public final void onVolumeChanged() {
        this.zzqj.zzby();
    }
}
