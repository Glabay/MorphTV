package com.google.android.gms.internal.cast;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

final class zzbq implements ResultCallback<Status> {
    private final /* synthetic */ zzbl zzth;
    private final /* synthetic */ long zztm;

    zzbq(zzbl zzbl, long j) {
        this.zzth = zzbl;
        this.zztm = j;
    }

    public final /* synthetic */ void onResult(Result result) {
        Status status = (Status) result;
        if (!status.isSuccess()) {
            this.zzth.zza(this.zztm, status.getStatusCode());
        }
    }
}
