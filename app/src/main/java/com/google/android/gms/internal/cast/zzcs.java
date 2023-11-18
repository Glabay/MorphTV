package com.google.android.gms.internal.cast;

final class zzcs implements Runnable {
    private final /* synthetic */ zzcn zzvw;
    private final /* synthetic */ zzcd zzvz;

    zzcs(zzcp zzcp, zzcn zzcn, zzcd zzcd) {
        this.zzvw = zzcn;
        this.zzvz = zzcd;
    }

    public final void run() {
        this.zzvw.zza(this.zzvz);
    }
}
