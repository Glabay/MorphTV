package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.cast.CastRemoteDisplay.CastRemoteDisplaySessionResult;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.util.VisibleForTesting;

@VisibleForTesting
class zzdv extends ApiMethodImpl<CastRemoteDisplaySessionResult, zzea> {
    final /* synthetic */ zzdq zzxu;

    public zzdv(zzdq zzdq, GoogleApiClient googleApiClient) {
        this.zzxu = zzdq;
        super(zzdq.zzxs, googleApiClient);
    }

    protected /* synthetic */ Result createFailedResult(Status status) {
        return new zzdy(status);
    }

    @VisibleForTesting
    public /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzea) anyClient);
    }

    @VisibleForTesting
    public void zza(zzea zzea) throws RemoteException {
    }
}
