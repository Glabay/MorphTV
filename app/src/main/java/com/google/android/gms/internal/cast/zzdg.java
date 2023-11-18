package com.google.android.gms.internal.cast;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Locale;

public final class zzdg {
    private static boolean zzwk;
    private final String mTag;
    private final boolean zzwl;
    private boolean zzwm;
    private boolean zzwn;
    private String zzwo;

    public zzdg(String str) {
        this(str, false);
    }

    private zzdg(String str, boolean z) {
        Preconditions.checkNotEmpty(str, "The log tag cannot be null or empty.");
        this.mTag = str;
        this.zzwl = str.length() <= 23;
        this.zzwm = false;
        this.zzwn = false;
    }

    private final String zza(String str, Object... objArr) {
        if (objArr.length != 0) {
            str = String.format(Locale.ROOT, str, objArr);
        }
        if (!TextUtils.isEmpty(this.zzwo)) {
            String valueOf = String.valueOf(this.zzwo);
            str = String.valueOf(str);
            if (str.length() != 0) {
                return valueOf.concat(str);
            }
            str = new String(valueOf);
        }
        return str;
    }

    private final boolean zzcy() {
        if (!this.zzwm) {
            if (!this.zzwl || !Log.isLoggable(this.mTag, 3)) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: d */
    public final void m25d(String str, Object... objArr) {
        if (zzcy()) {
            Log.d(this.mTag, zza(str, objArr));
        }
    }

    /* renamed from: e */
    public final void m26e(String str, Object... objArr) {
        Log.e(this.mTag, zza(str, objArr));
    }

    /* renamed from: i */
    public final void m27i(String str, Object... objArr) {
        Log.i(this.mTag, zza(str, objArr));
    }

    /* renamed from: w */
    public final void m28w(String str, Object... objArr) {
        Log.w(this.mTag, zza(str, objArr));
    }

    public final void zza(Throwable th, String str, Object... objArr) {
        if (zzcy()) {
            Log.d(this.mTag, zza(str, objArr), th);
        }
    }

    public final void zzb(Throwable th, String str, Object... objArr) {
        Log.w(this.mTag, zza(str, objArr), th);
    }

    public final void zzc(Throwable th, String str, Object... objArr) {
        Log.e(this.mTag, zza(str, objArr), th);
    }

    public final void zzk(boolean z) {
        this.zzwm = true;
    }

    public final void zzt(String str) {
        if (TextUtils.isEmpty(str)) {
            str = null;
        } else {
            str = String.format("[%s] ", new Object[]{str});
        }
        this.zzwo = str;
    }
}
