package com.google.android.gms.cast.framework;

import android.support.annotation.NonNull;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.internal.cast.zzch;
import com.google.android.gms.internal.cast.zzdg;

public class PrecacheManager {
    private final zzdg zzbd = new zzdg("PrecacheManager");
    private final SessionManager zzgu;
    private final CastOptions zzgy;
    private final zzch zzid;

    public PrecacheManager(@NonNull CastOptions castOptions, @NonNull SessionManager sessionManager, @NonNull zzch zzch) {
        this.zzgy = castOptions;
        this.zzgu = sessionManager;
        this.zzid = zzch;
    }

    public void precache(@NonNull String str) {
        Session currentSession = this.zzgu.getCurrentSession();
        if (str == null) {
            throw new IllegalArgumentException("No precache data found to be precached");
        } else if (currentSession == null) {
            this.zzid.zza(new String[]{this.zzgy.getReceiverApplicationId()}, str, null);
        } else if (currentSession instanceof CastSession) {
            RemoteMediaClient remoteMediaClient = ((CastSession) currentSession).getRemoteMediaClient();
            if (remoteMediaClient != null) {
                remoteMediaClient.zza(str, null);
            } else {
                this.zzbd.m26e("Failed to get RemoteMediaClient from current cast session.", new Object[0]);
            }
        } else {
            this.zzbd.m26e("Current session is not a CastSession. Precache is not supported.", new Object[0]);
        }
    }
}
