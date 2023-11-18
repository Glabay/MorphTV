package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

final class zzba extends Handler {
    private final /* synthetic */ zzav zzit;

    zzba(zzav zzav, Looper looper) {
        this.zzit = zzav;
        super(looper);
    }

    public final void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                this.zzit.zzay();
                return;
            case 2:
                this.zzit.resume();
                return;
            default:
                int i = message.what;
                StringBuilder stringBuilder = new StringBuilder(31);
                stringBuilder.append("Unknown message id: ");
                stringBuilder.append(i);
                Log.w("GoogleApiClientImpl", stringBuilder.toString());
                return;
        }
    }
}
