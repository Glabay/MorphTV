package com.google.android.gms.cast.framework.media;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

final class zzau implements ResultCallback<Status> {
    private final long zzga;
    private final /* synthetic */ zza zzob;

    zzau(zza zza, long j) {
        this.zzob = zza;
        this.zzga = j;
    }

    public final /* synthetic */ void onResult(@NonNull Result result) {
        Status status = (Status) result;
        if (!status.isSuccess()) {
            this.zzob.zzns.zzeu.zza(this.zzga, status.getStatusCode());
        }
    }
}
