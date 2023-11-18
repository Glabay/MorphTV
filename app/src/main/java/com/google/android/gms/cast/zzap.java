package com.google.android.gms.cast;

import com.google.android.gms.internal.cast.zzdj;

final class zzap implements zzdj {
    private final /* synthetic */ RemoteMediaPlayer zzfa;

    zzap(RemoteMediaPlayer remoteMediaPlayer) {
        this.zzfa = remoteMediaPlayer;
    }

    public final void onAdBreakStatusUpdated() {
    }

    public final void onMetadataUpdated() {
        this.zzfa.onMetadataUpdated();
    }

    public final void onPreloadStatusUpdated() {
        this.zzfa.onPreloadStatusUpdated();
    }

    public final void onQueueStatusUpdated() {
        this.zzfa.onQueueStatusUpdated();
    }

    public final void onStatusUpdated() {
        this.zzfa.onStatusUpdated();
    }

    public final void zza(int[] iArr) {
    }

    public final void zza(int[] iArr, int i) {
    }

    public final void zzb(int[] iArr) {
    }

    public final void zzb(MediaQueueItem[] mediaQueueItemArr) {
    }

    public final void zzc(int[] iArr) {
    }
}
