package com.google.android.gms.internal.cast;

import android.annotation.TargetApi;
import android.hardware.display.VirtualDisplay;
import com.google.android.gms.cast.CastRemoteDisplay.CastRemoteDisplaySessionResult;
import com.google.android.gms.cast.CastRemoteDisplayApi;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;

@Deprecated
public final class zzdq implements CastRemoteDisplayApi {
    private static final zzdg zzbd = new zzdg("CastRemoteDisplayApiImpl");
    private VirtualDisplay zzbe;
    private Api<?> zzxs;
    private final zzeg zzxt = new zzdr(this);

    public zzdq(Api api) {
        this.zzxs = api;
    }

    @TargetApi(19)
    private final void a_() {
        if (this.zzbe != null) {
            if (this.zzbe.getDisplay() != null) {
                zzdg zzdg = zzbd;
                int displayId = this.zzbe.getDisplay().getDisplayId();
                StringBuilder stringBuilder = new StringBuilder(38);
                stringBuilder.append("releasing virtual display: ");
                stringBuilder.append(displayId);
                zzdg.m25d(stringBuilder.toString(), new Object[0]);
            }
            this.zzbe.release();
            this.zzbe = null;
        }
    }

    public final PendingResult<CastRemoteDisplaySessionResult> startRemoteDisplay(GoogleApiClient googleApiClient, String str) {
        zzbd.m25d("startRemoteDisplay", new Object[0]);
        return googleApiClient.execute(new zzds(this, googleApiClient, str));
    }

    public final PendingResult<CastRemoteDisplaySessionResult> stopRemoteDisplay(GoogleApiClient googleApiClient) {
        zzbd.m25d("stopRemoteDisplay", new Object[0]);
        return googleApiClient.execute(new zzdt(this, googleApiClient));
    }
}
