package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;

final class zzdt extends zzdv {
    zzdt(zzdq zzdq, GoogleApiClient googleApiClient) {
        super(zzdq, googleApiClient);
    }

    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzea) anyClient);
    }

    public final void zza(zzea zzea) throws RemoteException {
        zzea.zza(new zzdx(this));
    }
}
