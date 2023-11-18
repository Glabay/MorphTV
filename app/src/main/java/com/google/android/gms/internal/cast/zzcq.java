package com.google.android.gms.internal.cast;

final class zzcq implements Runnable {
    private final /* synthetic */ zzcn zzvw;
    private final /* synthetic */ int zzvx;

    zzcq(zzcp zzcp, zzcn zzcn, int i) {
        this.zzvw = zzcn;
        this.zzvx = i;
    }

    public final void run() {
        this.zzvw.zzaj.onApplicationDisconnected(this.zzvx);
    }
}
