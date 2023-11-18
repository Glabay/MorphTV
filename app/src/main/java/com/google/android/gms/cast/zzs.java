package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.internal.cast.zzdz;
import com.google.android.gms.internal.cast.zzee;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzs extends TaskApiCall<zzdz, Void> {
    final /* synthetic */ CastRemoteDisplayClient zzbi;

    zzs(CastRemoteDisplayClient castRemoteDisplayClient) {
        this.zzbi = castRemoteDisplayClient;
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzdz zzdz = (zzdz) anyClient;
        ((zzee) zzdz.getService()).zza(new zzt(this, taskCompletionSource));
    }
}
