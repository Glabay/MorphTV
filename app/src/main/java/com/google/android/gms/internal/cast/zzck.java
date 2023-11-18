package com.google.android.gms.internal.cast;

import android.os.RemoteException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.IStatusCallback.Stub;
import com.google.android.gms.common.api.internal.TaskUtil;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzck extends Stub {
    private final /* synthetic */ TaskCompletionSource zzbj;

    zzck(zzcj zzcj, TaskCompletionSource taskCompletionSource) {
        this.zzbj = taskCompletionSource;
    }

    public final void onResult(Status status) throws RemoteException {
        TaskUtil.setResultOrApiException(status, this.zzbj);
    }
}
