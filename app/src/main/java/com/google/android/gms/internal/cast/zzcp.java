package com.google.android.gms.internal.cast;

import android.os.Handler;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.concurrent.atomic.AtomicReference;

@VisibleForTesting
final class zzcp extends zzdc {
    private final Handler handler;
    private final AtomicReference<zzcn> zzvv;

    public zzcp(zzcn zzcn) {
        this.zzvv = new AtomicReference(zzcn);
        this.handler = new Handler(zzcn.getLooper());
    }

    public final boolean isDisposed() {
        return this.zzvv.get() == null;
    }

    public final void onApplicationDisconnected(int i) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzvj = null;
            zzcn.zzvk = null;
            zzcn.zzm(i);
            if (zzcn.zzaj != null) {
                this.handler.post(new zzcq(this, zzcn, i));
            }
        }
    }

    public final void zza(ApplicationMetadata applicationMetadata, String str, String str2, boolean z) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzux = applicationMetadata;
            zzcn.zzvj = applicationMetadata.getApplicationId();
            zzcn.zzvk = str2;
            zzcn.zzvb = str;
            synchronized (zzcn.zzvp) {
                if (zzcn.zzvn != null) {
                    zzcn.zzvn.setResult(new zzco(new Status(0), applicationMetadata, str, str2, z));
                    zzcn.zzvn = null;
                }
            }
        }
    }

    public final void zza(String str, double d, boolean z) {
        zzcn.zzbd.m25d("Deprecated callback: \"onStatusreceived\"", new Object[0]);
    }

    public final void zza(String str, long j) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzb(j, 0);
        }
    }

    public final void zza(String str, long j, int i) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzb(j, i);
        }
    }

    public final void zza(String str, byte[] bArr) {
        if (((zzcn) this.zzvv.get()) != null) {
            zzcn.zzbd.m25d("IGNORING: Receive (type=binary, ns=%s) <%d bytes>", str, Integer.valueOf(bArr.length));
        }
    }

    public final void zzb(zzcd zzcd) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzbd.m25d("onApplicationStatusChanged", new Object[0]);
            this.handler.post(new zzcs(this, zzcn, zzcd));
        }
    }

    public final void zzb(zzcv zzcv) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzbd.m25d("onDeviceStatusChanged", new Object[0]);
            this.handler.post(new zzcr(this, zzcn, zzcv));
        }
    }

    public final void zzb(String str, String str2) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzbd.m25d("Receive (type=text, ns=%s) %s", str, str2);
            this.handler.post(new zzct(this, zzcn, str, str2));
        }
    }

    public final zzcn zzcu() {
        zzcn zzcn = (zzcn) this.zzvv.getAndSet(null);
        if (zzcn == null) {
            return null;
        }
        zzcn.zzcp();
        return zzcn;
    }

    public final void zzf(int i) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzl(i);
        }
    }

    public final void zzn(int i) {
        zzcn zzcu = zzcu();
        if (zzcu != null) {
            zzcn.zzbd.m25d("ICastDeviceControllerListener.onDisconnected: %d", Integer.valueOf(i));
            if (i != 0) {
                zzcu.triggerConnectionSuspended(2);
            }
        }
    }

    public final void zzo(int i) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzm(i);
        }
    }

    public final void zzp(int i) {
        zzcn zzcn = (zzcn) this.zzvv.get();
        if (zzcn != null) {
            zzcn.zzm(i);
        }
    }
}
