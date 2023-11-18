package com.google.android.gms.internal.cast;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Locale;

public final class zzdn {
    private static final zzdg zzbd = new zzdg("RequestTracker");
    private static final Object zzxo = new Object();
    private final Handler handler = new Handler(Looper.getMainLooper());
    @VisibleForTesting
    private Runnable zzoh;
    @VisibleForTesting
    private long zztt = -1;
    private long zzxm;
    @VisibleForTesting
    private zzdm zzxn;

    public zzdn(long j) {
        this.zzxm = j;
    }

    private final void zza(int i, Object obj, String str) {
        zzbd.m25d(str, new Object[0]);
        synchronized (zzxo) {
            if (this.zzxn != null) {
                this.zzxn.zza(this.zztt, i, obj);
            }
            this.zztt = -1;
            this.zzxn = null;
            synchronized (zzxo) {
                if (this.zzoh != null) {
                    this.handler.removeCallbacks(this.zzoh);
                    this.zzoh = null;
                }
            }
        }
    }

    private final boolean zza(int i, Object obj) {
        synchronized (zzxo) {
            if (this.zztt != -1) {
                zza(i, null, String.format(Locale.ROOT, "clearing request %d", new Object[]{Long.valueOf(this.zztt)}));
                return true;
            }
            return false;
        }
    }

    public final boolean test(long j) {
        boolean z;
        synchronized (zzxo) {
            z = this.zztt != -1 && this.zztt == j;
        }
        return z;
    }

    public final void zza(long j, zzdm zzdm) {
        synchronized (zzxo) {
            zzdm zzdm2 = this.zzxn;
            long j2 = this.zztt;
            this.zztt = j;
            this.zzxn = zzdm;
        }
        if (zzdm2 != null) {
            zzdm2.zzb(j2);
        }
        synchronized (zzxo) {
            if (this.zzoh != null) {
                this.handler.removeCallbacks(this.zzoh);
            }
            this.zzoh = new zzdo(this);
            this.handler.postDelayed(this.zzoh, this.zzxm);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zzc(long r7, int r9, java.lang.Object r10) {
        /*
        r6 = this;
        r0 = zzxo;
        monitor-enter(r0);
        r1 = r6.zztt;	 Catch:{ all -> 0x002a }
        r3 = -1;
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        r1 = 0;
        if (r5 == 0) goto L_0x0028;
    L_0x000c:
        r2 = r6.zztt;	 Catch:{ all -> 0x002a }
        r4 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r4 != 0) goto L_0x0028;
    L_0x0012:
        r2 = java.util.Locale.ROOT;	 Catch:{ all -> 0x002a }
        r3 = "request %d completed";
        r4 = 1;
        r5 = new java.lang.Object[r4];	 Catch:{ all -> 0x002a }
        r7 = java.lang.Long.valueOf(r7);	 Catch:{ all -> 0x002a }
        r5[r1] = r7;	 Catch:{ all -> 0x002a }
        r7 = java.lang.String.format(r2, r3, r5);	 Catch:{ all -> 0x002a }
        r6.zza(r9, r10, r7);	 Catch:{ all -> 0x002a }
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        return r4;
    L_0x0028:
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        return r1;
    L_0x002a:
        r7 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cast.zzdn.zzc(long, int, java.lang.Object):boolean");
    }

    public final boolean zzdb() {
        boolean z;
        synchronized (zzxo) {
            z = this.zztt != -1;
        }
        return z;
    }

    final /* synthetic */ void zzdc() {
        synchronized (zzxo) {
            if (this.zztt == -1) {
                return;
            }
            zza(15, null);
        }
    }

    public final boolean zzq(int i) {
        return zza((int) CastStatusCodes.CANCELED, null);
    }
}
