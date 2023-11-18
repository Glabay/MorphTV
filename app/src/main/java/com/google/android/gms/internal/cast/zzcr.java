package com.google.android.gms.internal.cast;

final class zzcr implements Runnable {
    private final /* synthetic */ zzcn zzvw;
    private final /* synthetic */ zzcv zzvy;

    zzcr(zzcp zzcp, zzcn zzcn, zzcv zzcv) {
        this.zzvw = zzcn;
        this.zzvy = zzcv;
    }

    public final void run() {
        this.zzvw.zza(this.zzvy);
    }
}
