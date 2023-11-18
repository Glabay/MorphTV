package com.google.android.gms.internal.cast;

import com.google.android.gms.cast.Cast.MessageReceivedCallback;

final class zzct implements Runnable {
    private final /* synthetic */ String zzad;
    private final /* synthetic */ zzcn zzvw;
    private final /* synthetic */ String zzwa;

    zzct(zzcp zzcp, zzcn zzcn, String str, String str2) {
        this.zzvw = zzcn;
        this.zzad = str;
        this.zzwa = str2;
    }

    public final void run() {
        synchronized (this.zzvw.zzuy) {
            MessageReceivedCallback messageReceivedCallback = (MessageReceivedCallback) this.zzvw.zzuy.get(this.zzad);
        }
        if (messageReceivedCallback != null) {
            messageReceivedCallback.onMessageReceived(this.zzvw.zzhq, this.zzad, this.zzwa);
            return;
        }
        zzcn.zzbd.m25d("Discarded message for unknown namespace '%s'", this.zzad);
    }
}
