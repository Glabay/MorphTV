package com.google.android.gms.common.api.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;

final class zzci implements Runnable {
    private final /* synthetic */ Result zzmk;
    private final /* synthetic */ zzch zzml;

    zzci(zzch zzch, Result result) {
        this.zzml = zzch;
        this.zzmk = result;
    }

    @WorkerThread
    public final void run() {
        GoogleApiClient googleApiClient;
        try {
            BasePendingResult.zzez.set(Boolean.valueOf(true));
            this.zzml.zzmi.sendMessage(this.zzml.zzmi.obtainMessage(0, this.zzml.zzmd.onSuccess(this.zzmk)));
            BasePendingResult.zzez.set(Boolean.valueOf(false));
            zzch.zzb(this.zzmk);
            googleApiClient = (GoogleApiClient) this.zzml.zzfc.get();
            if (googleApiClient != null) {
                googleApiClient.zzb(this.zzml);
            }
        } catch (RuntimeException e) {
            this.zzml.zzmi.sendMessage(this.zzml.zzmi.obtainMessage(1, e));
            BasePendingResult.zzez.set(Boolean.valueOf(false));
            zzch.zzb(this.zzmk);
            googleApiClient = (GoogleApiClient) this.zzml.zzfc.get();
            if (googleApiClient != null) {
                googleApiClient.zzb(this.zzml);
            }
        } catch (Throwable th) {
            BasePendingResult.zzez.set(Boolean.valueOf(false));
            zzch.zzb(this.zzmk);
            GoogleApiClient googleApiClient2 = (GoogleApiClient) this.zzml.zzfc.get();
            if (googleApiClient2 != null) {
                googleApiClient2.zzb(this.zzml);
            }
            throw th;
        }
    }
}
