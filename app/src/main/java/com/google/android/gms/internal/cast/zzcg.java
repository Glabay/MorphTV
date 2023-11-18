package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@VisibleForTesting
public class zzcg extends zzcm {
    private final List<zzdn> zzus = Collections.synchronizedList(new ArrayList());

    public zzcg(String str, String str2, String str3) {
        super(str, str2, null);
    }

    protected final void zza(zzdn zzdn) {
        this.zzus.add(zzdn);
    }

    public void zzcm() {
        synchronized (this.zzus) {
            for (zzdn zzq : this.zzus) {
                zzq.zzq(CastStatusCodes.CANCELED);
            }
        }
    }

    protected final List<zzdn> zzcn() {
        return this.zzus;
    }
}
