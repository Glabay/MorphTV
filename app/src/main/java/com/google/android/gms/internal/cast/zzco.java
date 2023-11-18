package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast.ApplicationConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.VisibleForTesting;

@VisibleForTesting
final class zzco implements ApplicationConnectionResult {
    private final Status zzge;
    private final ApplicationMetadata zzvr;
    private final String zzvs;
    private final String zzvt;
    private final boolean zzvu;

    public zzco(Status status) {
        this(status, null, null, null, false);
    }

    public zzco(Status status, ApplicationMetadata applicationMetadata, String str, String str2, boolean z) {
        this.zzge = status;
        this.zzvr = applicationMetadata;
        this.zzvs = str;
        this.zzvt = str2;
        this.zzvu = z;
    }

    public final ApplicationMetadata getApplicationMetadata() {
        return this.zzvr;
    }

    public final String getApplicationStatus() {
        return this.zzvs;
    }

    public final String getSessionId() {
        return this.zzvt;
    }

    public final Status getStatus() {
        return this.zzge;
    }

    public final boolean getWasLaunched() {
        return this.zzvu;
    }
}
