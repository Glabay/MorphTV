package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerClient.GameManagerInstanceResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.VisibleForTesting;

@VisibleForTesting
public abstract class zzbu extends zzbt<GameManagerInstanceResult> {
    final /* synthetic */ zzbl zzth;
    private GameManagerClient zztq;

    public zzbu(zzbl zzbl, GameManagerClient gameManagerClient) {
        this.zzth = zzbl;
        super(zzbl);
        this.zztq = gameManagerClient;
        this.zztp = new zzbv(this, zzbl);
    }

    public static GameManagerInstanceResult zzc(Status status) {
        return new zzbw(status, null);
    }

    public /* synthetic */ Result createFailedResult(Status status) {
        return zzc(status);
    }
}
