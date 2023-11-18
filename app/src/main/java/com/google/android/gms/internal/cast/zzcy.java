package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

public class zzcy extends zzcf<Status> {
    public zzcy(GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    public /* synthetic */ Result createFailedResult(Status status) {
        return status;
    }

    public /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzcn) anyClient);
    }

    public void zza(zzcn zzcn) throws RemoteException {
    }
}
