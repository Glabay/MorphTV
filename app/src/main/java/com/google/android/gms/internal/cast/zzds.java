package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;

final class zzds extends zzdv {
    private final /* synthetic */ zzdq zzxu;
    private final /* synthetic */ String zzxv;

    zzds(zzdq zzdq, GoogleApiClient googleApiClient, String str) {
        this.zzxu = zzdq;
        this.zzxv = str;
        super(zzdq, googleApiClient);
    }

    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzea) anyClient);
    }

    public final void zza(zzea zzea) throws RemoteException {
        zzea.zza(new zzdw(this, zzea), this.zzxu.zzxt, this.zzxv);
    }
}
