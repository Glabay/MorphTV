package com.google.android.gms.cast;

final class zzx implements Runnable {
    private final /* synthetic */ CastRemoteDisplayLocalService zzcg;
    private final /* synthetic */ boolean zzcm;

    zzx(CastRemoteDisplayLocalService castRemoteDisplayLocalService, boolean z) {
        this.zzcg = castRemoteDisplayLocalService;
        this.zzcm = z;
    }

    public final void run() {
        this.zzcg.zza(this.zzcm);
    }
}
