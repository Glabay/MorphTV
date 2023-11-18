package com.google.android.gms.cast;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.cast.MediaLoadOptions.Builder;
import com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.cast.zzcn;
import org.json.JSONObject;

final class zzba extends zzb {
    private final /* synthetic */ RemoteMediaPlayer zzfa;
    private final /* synthetic */ GoogleApiClient zzfb;
    private final /* synthetic */ long zzfh;
    private final /* synthetic */ JSONObject zzfi;
    private final /* synthetic */ boolean zzfp;
    private final /* synthetic */ long[] zzfq;
    private final /* synthetic */ MediaInfo zzfr;

    zzba(RemoteMediaPlayer remoteMediaPlayer, GoogleApiClient googleApiClient, GoogleApiClient googleApiClient2, boolean z, long j, long[] jArr, JSONObject jSONObject, MediaInfo mediaInfo) {
        this.zzfa = remoteMediaPlayer;
        this.zzfb = googleApiClient2;
        this.zzfp = z;
        this.zzfh = j;
        this.zzfq = jArr;
        this.zzfi = jSONObject;
        this.zzfr = mediaInfo;
        super(googleApiClient);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    protected final void zza(zzcn zzcn) {
        synchronized (this.zzfa.lock) {
            zza zzf;
            this.zzfa.zzev.zza(this.zzfb);
            try {
                this.zzfa.zzeu.zza(this.zzgc, this.zzfr, new Builder().setAutoplay(this.zzfp).setPlayPosition(this.zzfh).setActiveTrackIds(this.zzfq).setCustomData(this.zzfi).build());
                zzf = this.zzfa.zzev;
            } catch (Throwable e) {
                try {
                    Log.e("RemoteMediaPlayer", "load - channel error", e);
                    setResult((MediaChannelResult) createFailedResult(new Status(2100)));
                    zzf = this.zzfa.zzev;
                } catch (Throwable th) {
                    this.zzfa.zzev.zza(null);
                }
            }
            zzf.zza(null);
        }
    }
}
