package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzc<T> extends zzb {
    protected final TaskCompletionSource<T> zzdu;

    public zzc(int i, TaskCompletionSource<T> taskCompletionSource) {
        super(i);
        this.zzdu = taskCompletionSource;
    }

    public void zza(@NonNull Status status) {
        this.zzdu.trySetException(new ApiException(status));
    }

    public final void zza(zza<?> zza) throws DeadObjectException {
        try {
            zzb(zza);
        } catch (RemoteException e) {
            zza(zzb.zza(e));
            throw e;
        } catch (RemoteException e2) {
            zza(zzb.zza(e2));
        } catch (RuntimeException e3) {
            zza(e3);
        }
    }

    public void zza(@NonNull zzaa zzaa, boolean z) {
    }

    public void zza(@NonNull RuntimeException runtimeException) {
        this.zzdu.trySetException(runtimeException);
    }

    protected abstract void zzb(zza<?> zza) throws RemoteException;
}
