package com.google.android.gms.cast.framework.media;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.Callback;
import com.google.android.gms.cast.framework.media.RemoteMediaClient.Listener;
import com.google.android.gms.internal.cast.zzdj;
import java.util.List;

final class zzr implements zzdj {
    private final /* synthetic */ RemoteMediaClient zzns;

    zzr(RemoteMediaClient remoteMediaClient) {
        this.zzns = remoteMediaClient;
    }

    private final void zzbp() {
        if (this.zzns.zznr != null) {
            MediaStatus mediaStatus = this.zzns.getMediaStatus();
            if (mediaStatus != null) {
                mediaStatus.zzf(this.zzns.zznr.parseIsPlayingAdFromMediaStatus(mediaStatus));
                List parseAdBreaksFromMediaStatus = this.zzns.zznr.parseAdBreaksFromMediaStatus(mediaStatus);
                MediaInfo mediaInfo = this.zzns.getMediaInfo();
                if (mediaInfo != null) {
                    mediaInfo.zzb(parseAdBreaksFromMediaStatus);
                }
            }
        }
    }

    public final void onAdBreakStatusUpdated() {
        for (Listener onAdBreakStatusUpdated : this.zzns.zznn) {
            onAdBreakStatusUpdated.onAdBreakStatusUpdated();
        }
        for (Callback onAdBreakStatusUpdated2 : this.zzns.zzno) {
            onAdBreakStatusUpdated2.onAdBreakStatusUpdated();
        }
    }

    public final void onMetadataUpdated() {
        zzbp();
        for (Listener onMetadataUpdated : this.zzns.zznn) {
            onMetadataUpdated.onMetadataUpdated();
        }
        for (Callback onMetadataUpdated2 : this.zzns.zzno) {
            onMetadataUpdated2.onMetadataUpdated();
        }
    }

    public final void onPreloadStatusUpdated() {
        for (Listener onPreloadStatusUpdated : this.zzns.zznn) {
            onPreloadStatusUpdated.onPreloadStatusUpdated();
        }
        for (Callback onPreloadStatusUpdated2 : this.zzns.zzno) {
            onPreloadStatusUpdated2.onPreloadStatusUpdated();
        }
    }

    public final void onQueueStatusUpdated() {
        for (Listener onQueueStatusUpdated : this.zzns.zznn) {
            onQueueStatusUpdated.onQueueStatusUpdated();
        }
        for (Callback onQueueStatusUpdated2 : this.zzns.zzno) {
            onQueueStatusUpdated2.onQueueStatusUpdated();
        }
    }

    public final void onStatusUpdated() {
        zzbp();
        this.zzns.zzbo();
        for (Listener onStatusUpdated : this.zzns.zznn) {
            onStatusUpdated.onStatusUpdated();
        }
        for (Callback onStatusUpdated2 : this.zzns.zzno) {
            onStatusUpdated2.onStatusUpdated();
        }
    }

    public final void zza(int[] iArr) {
        for (Callback zza : this.zzns.zzno) {
            zza.zza(iArr);
        }
    }

    public final void zza(int[] iArr, int i) {
        for (Callback zza : this.zzns.zzno) {
            zza.zza(iArr, i);
        }
    }

    public final void zzb(int[] iArr) {
        for (Callback zzb : this.zzns.zzno) {
            zzb.zzb(iArr);
        }
    }

    public final void zzb(MediaQueueItem[] mediaQueueItemArr) {
        for (Callback zzb : this.zzns.zzno) {
            zzb.zzb(mediaQueueItemArr);
        }
    }

    public final void zzc(int[] iArr) {
        for (Callback zzc : this.zzns.zzno) {
            zzc.zzc(iArr);
        }
    }
}
