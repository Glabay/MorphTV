package com.google.android.gms.cast;

import com.google.android.gms.cast.RemoteMediaPlayer.MediaChannelResult;
import com.google.android.gms.common.api.Status;
import org.json.JSONObject;

final class zzbo implements MediaChannelResult {
    private final /* synthetic */ Status zzal;

    zzbo(zzb zzb, Status status) {
        this.zzal = status;
    }

    public final JSONObject getCustomData() {
        return null;
    }

    public final Status getStatus() {
        return this.zzal;
    }
}
