package com.google.android.gms.internal.cast;

import android.os.Build.VERSION;
import android.os.Looper;

final class zzem extends ThreadLocal<zzel> {
    zzem() {
    }

    protected final /* synthetic */ Object initialValue() {
        if (VERSION.SDK_INT >= 16) {
            return new zzer();
        }
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            return new zzeq(myLooper);
        }
        throw new IllegalStateException("The current thread must have a looper!");
    }
}
