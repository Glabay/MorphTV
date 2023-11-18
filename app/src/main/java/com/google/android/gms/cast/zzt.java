package com.google.android.gms.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzt extends zza {
    private final /* synthetic */ TaskCompletionSource zzbj;
    private final /* synthetic */ zzs zzbm;

    zzt(zzs zzs, TaskCompletionSource taskCompletionSource) {
        this.zzbm = zzs;
        this.zzbj = taskCompletionSource;
        super();
    }

    public final void onDisconnected() throws RemoteException {
        this.zzbm.zzbi.zzbd.m25d("onDisconnected", new Object[0]);
        this.zzbm.zzbi.a_();
        TaskUtil.setResultOrApiException(Status.RESULT_SUCCESS, this.zzbj);
    }

    public final void onError(int i) throws RemoteException {
        this.zzbm.zzbi.zzbd.m25d("onError: %d", Integer.valueOf(i));
        this.zzbm.zzbi.a_();
        TaskUtil.setResultOrApiException(Status.RESULT_INTERNAL_ERROR, this.zzbj);
    }
}
