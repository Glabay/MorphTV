package com.google.android.gms.cast.framework;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.cast.zzdg;

public final class zze {
    private static final zzdg zzbd = new zzdg("DiscoveryManager");
    private final zzp zzhu;

    zze(zzp zzp) {
        this.zzhu = zzp;
    }

    public final IObjectWrapper zzr() {
        try {
            return this.zzhu.zzx();
        } catch (Throwable e) {
            zzbd.zza(e, "Unable to call %s on %s.", "getWrappedThis", zzp.class.getSimpleName());
            return null;
        }
    }
}
