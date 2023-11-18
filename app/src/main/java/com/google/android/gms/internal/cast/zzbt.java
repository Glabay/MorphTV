package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.util.VisibleForTesting;

@VisibleForTesting
public abstract class zzbt<R extends Result> extends zzcf<R> {
    protected zzdm zztp;

    public zzbt(zzbl zzbl) {
        super(zzbl.zznm);
    }

    protected /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        execute();
    }

    public abstract void execute();
}
