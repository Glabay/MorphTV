package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerClient.GameManagerInstanceResult;
import com.google.android.gms.common.api.Status;

final class zzbw implements GameManagerInstanceResult {
    private final Status zzge;
    private final GameManagerClient zztq;

    zzbw(Status status, GameManagerClient gameManagerClient) {
        this.zzge = status;
        this.zztq = gameManagerClient;
    }

    public final GameManagerClient getGameManagerClient() {
        return this.zztq;
    }

    public final Status getStatus() {
        return this.zzge;
    }
}
