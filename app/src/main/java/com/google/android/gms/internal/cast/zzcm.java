package com.google.android.gms.internal.cast;

import android.support.annotation.NonNull;
import android.text.TextUtils;

public class zzcm {
    private final String namespace;
    protected final zzdg zzuv;
    private zzdl zzuw;

    protected zzcm(String str, String str2, String str3) {
        zzcu.zzo(str);
        this.namespace = str;
        this.zzuv = new zzdg(str2);
        setSessionLabel(str3);
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final void setSessionLabel(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.zzuv.zzt(str);
        }
    }

    public void zza(long j, int i) {
    }

    public final void zza(zzdl zzdl) {
        this.zzuw = zzdl;
        if (this.zzuw == null) {
            zzcm();
        }
    }

    protected final void zza(String str, long j, String str2) throws IllegalStateException {
        Object[] objArr = new Object[]{str, null};
        this.zzuw.zza(this.namespace, str, j, null);
    }

    public void zzcm() {
    }

    protected final long zzco() {
        return this.zzuw.zzl();
    }

    public void zzn(@NonNull String str) {
    }
}
