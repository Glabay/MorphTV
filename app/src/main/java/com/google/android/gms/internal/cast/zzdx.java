package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;

public final class zzdx extends zzdu {
    private final /* synthetic */ zzdv zzxx;

    protected zzdx(zzdv zzdv) {
        this.zzxx = zzdv;
    }

    public final void onDisconnected() throws RemoteException {
        zzdq.zzbd.m25d("onDisconnected", new Object[0]);
        this.zzxx.zzxu.a_();
        this.zzxx.setResult(new zzdy(Status.RESULT_SUCCESS));
    }

    public final void onError(int i) throws RemoteException {
        zzdq.zzbd.m25d("onError: %d", Integer.valueOf(i));
        this.zzxx.zzxu.a_();
        this.zzxx.setResult(new zzdy(Status.RESULT_INTERNAL_ERROR));
    }
}
