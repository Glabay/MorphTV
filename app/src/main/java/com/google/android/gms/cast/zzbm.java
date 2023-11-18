package com.google.android.gms.cast;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

final class zzbm implements ResultCallback<Status> {
    private final long zzga;
    private final /* synthetic */ zza zzgb;

    zzbm(zza zza, long j) {
        this.zzgb = zza;
        this.zzga = j;
    }

    public final /* synthetic */ void onResult(@NonNull Result result) {
        Status status = (Status) result;
        if (!status.isSuccess()) {
            this.zzgb.zzfa.zzeu.zza(this.zzga, status.getStatusCode());
        }
    }
}
