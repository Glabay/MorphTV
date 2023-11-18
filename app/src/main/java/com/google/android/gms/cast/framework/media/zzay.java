package com.google.android.gms.cast.framework.media;

import java.util.TimerTask;

final class zzay extends TimerTask {
    private final /* synthetic */ RemoteMediaClient zzod;
    private final /* synthetic */ zze zzoj;

    zzay(zze zze, RemoteMediaClient remoteMediaClient) {
        this.zzoj = zze;
        this.zzod = remoteMediaClient;
    }

    public final void run() {
        this.zzoj.zzns.zza(this.zzoj.zzof);
        this.zzoj.zzns.handler.postDelayed(this, this.zzoj.zzog);
    }
}
