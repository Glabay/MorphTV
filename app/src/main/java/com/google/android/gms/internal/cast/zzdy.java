package com.google.android.gms.internal.cast;

import android.view.Display;
import com.google.android.gms.cast.CastRemoteDisplay.CastRemoteDisplaySessionResult;
import com.google.android.gms.common.api.Status;

final class zzdy implements CastRemoteDisplaySessionResult {
    private final Display zzbx;
    private final Status zzge;

    public zzdy(Display display) {
        this.zzge = Status.RESULT_SUCCESS;
        this.zzbx = display;
    }

    public zzdy(Status status) {
        this.zzge = status;
        this.zzbx = null;
    }

    public final Display getPresentationDisplay() {
        return this.zzbx;
    }

    public final Status getStatus() {
        return this.zzge;
    }
}
