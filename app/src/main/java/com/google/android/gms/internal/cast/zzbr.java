package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.games.GameManagerClient.GameManagerResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.VisibleForTesting;

@VisibleForTesting
public abstract class zzbr extends zzbt<GameManagerResult> {
    final /* synthetic */ zzbl zzth;

    public zzbr(zzbl zzbl) {
        this.zzth = zzbl;
        super(zzbl);
        this.zztp = new zzbs(this, zzbl);
    }

    public static GameManagerResult zzb(Status status) {
        return new zzbx(status, null, -1, null);
    }

    public /* synthetic */ Result createFailedResult(Status status) {
        return zzb(status);
    }
}
