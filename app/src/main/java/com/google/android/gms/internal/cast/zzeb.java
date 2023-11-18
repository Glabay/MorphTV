package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;

final class zzeb extends zzeh {
    private final /* synthetic */ zzeg zzya;
    private final /* synthetic */ zzea zzyb;

    zzeb(zzea zzea, zzeg zzeg) {
        this.zzyb = zzea;
        this.zzya = zzeg;
    }

    public final void zzr(int i) throws RemoteException {
        zzea.zzbd.m25d("onRemoteDisplayEnded", new Object[0]);
        if (this.zzya != null) {
            this.zzya.zzr(i);
        }
        if (this.zzyb.zzxy != null) {
            this.zzyb.zzxy.onRemoteDisplayEnded(new Status(i));
        }
    }
}
