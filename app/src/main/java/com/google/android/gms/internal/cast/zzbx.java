package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.games.GameManagerClient.GameManagerResult;
import com.google.android.gms.common.api.Status;
import org.json.JSONObject;

final class zzbx implements GameManagerResult {
    private final Status zzge;
    private final String zzts;
    private final long zztt;
    private final JSONObject zztu;

    zzbx(Status status, String str, long j, JSONObject jSONObject) {
        this.zzge = status;
        this.zzts = str;
        this.zztt = j;
        this.zztu = jSONObject;
    }

    public final JSONObject getExtraMessageData() {
        return this.zztu;
    }

    public final String getPlayerId() {
        return this.zzts;
    }

    public final long getRequestId() {
        return this.zztt;
    }

    public final Status getStatus() {
        return this.zzge;
    }
}
