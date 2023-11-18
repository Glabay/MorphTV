package com.google.android.gms.cast;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Display;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.internal.cast.zzdz;
import com.google.android.gms.internal.cast.zzee;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzq extends TaskApiCall<zzdz, Display> {
    private final /* synthetic */ String zzaf;
    private final /* synthetic */ int zzbf;
    private final /* synthetic */ PendingIntent zzbg;
    private final /* synthetic */ CastDevice zzbh;
    final /* synthetic */ CastRemoteDisplayClient zzbi;

    zzq(CastRemoteDisplayClient castRemoteDisplayClient, int i, PendingIntent pendingIntent, CastDevice castDevice, String str) {
        this.zzbi = castRemoteDisplayClient;
        this.zzbf = i;
        this.zzbg = pendingIntent;
        this.zzbh = castDevice;
        this.zzaf = str;
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzdz zzdz = (zzdz) anyClient;
        Bundle bundle = new Bundle();
        bundle.putInt("configuration", this.zzbf);
        ((zzee) zzdz.getService()).zza(new zzr(this, taskCompletionSource, zzdz), this.zzbg, this.zzbh.getDeviceId(), this.zzaf, bundle);
    }
}
