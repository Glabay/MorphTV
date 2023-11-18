package com.google.android.gms.common.api.internal;

import android.support.annotation.WorkerThread;

abstract class zzat implements Runnable {
    private final /* synthetic */ zzaj zzhv;

    private zzat(zzaj zzaj) {
        this.zzhv = zzaj;
    }

    @WorkerThread
    public void run() {
        this.zzhv.zzga.lock();
        try {
            if (!Thread.interrupted()) {
                zzaq();
            }
        } catch (RuntimeException e) {
            this.zzhv.zzhf.zzb(e);
        } catch (Throwable th) {
            this.zzhv.zzga.unlock();
        }
        this.zzhv.zzga.unlock();
    }

    @WorkerThread
    protected abstract void zzaq();
}
