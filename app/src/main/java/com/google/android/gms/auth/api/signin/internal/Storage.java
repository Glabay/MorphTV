package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

public class Storage {
    private static final Lock zzaf = new ReentrantLock();
    @GuardedBy("sLk")
    private static Storage zzag;
    private final Lock zzah = new ReentrantLock();
    @GuardedBy("mLk")
    private final SharedPreferences zzai;

    @VisibleForTesting
    private Storage(Context context) {
        this.zzai = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static Storage getInstance(Context context) {
        Preconditions.checkNotNull(context);
        zzaf.lock();
        try {
            if (zzag == null) {
                zzag = new Storage(context.getApplicationContext());
            }
            Storage storage = zzag;
            return storage;
        } finally {
            zzaf.unlock();
        }
    }

    @VisibleForTesting
    public static void setInstance(@Nullable Storage storage) {
        zzaf.lock();
        try {
            zzag = storage;
        } finally {
            zzaf.unlock();
        }
    }

    private static String zza(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder((String.valueOf(str).length() + 1) + String.valueOf(str2).length());
        stringBuilder.append(str);
        stringBuilder.append(":");
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }

    @javax.annotation.Nullable
    @com.google.android.gms.common.util.VisibleForTesting
    private final com.google.android.gms.auth.api.signin.GoogleSignInAccount zzb(java.lang.String r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = android.text.TextUtils.isEmpty(r3);
        r1 = 0;
        if (r0 == 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = "googleSignInAccount";
        r3 = zza(r0, r3);
        r3 = r2.getFromStore(r3);
        if (r3 == 0) goto L_0x0019;
    L_0x0014:
        r3 = com.google.android.gms.auth.api.signin.GoogleSignInAccount.fromJsonString(r3);	 Catch:{ JSONException -> 0x0019 }
        return r3;
    L_0x0019:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.internal.Storage.zzb(java.lang.String):com.google.android.gms.auth.api.signin.GoogleSignInAccount");
    }

    @javax.annotation.Nullable
    @com.google.android.gms.common.util.VisibleForTesting
    private final com.google.android.gms.auth.api.signin.GoogleSignInOptions zzc(java.lang.String r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = android.text.TextUtils.isEmpty(r3);
        r1 = 0;
        if (r0 == 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = "googleSignInOptions";
        r3 = zza(r0, r3);
        r3 = r2.getFromStore(r3);
        if (r3 == 0) goto L_0x0019;
    L_0x0014:
        r3 = com.google.android.gms.auth.api.signin.GoogleSignInOptions.fromJsonString(r3);	 Catch:{ JSONException -> 0x0019 }
        return r3;
    L_0x0019:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.internal.Storage.zzc(java.lang.String):com.google.android.gms.auth.api.signin.GoogleSignInOptions");
    }

    public void clear() {
        this.zzah.lock();
        try {
            this.zzai.edit().clear().apply();
        } finally {
            this.zzah.unlock();
        }
    }

    @Nullable
    protected String getFromStore(String str) {
        this.zzah.lock();
        try {
            str = this.zzai.getString(str, null);
            return str;
        } finally {
            this.zzah.unlock();
        }
    }

    @Nullable
    public GoogleSignInAccount getSavedDefaultGoogleSignInAccount() {
        return zzb(getFromStore("defaultGoogleSignInAccount"));
    }

    @Nullable
    public GoogleSignInOptions getSavedDefaultGoogleSignInOptions() {
        return zzc(getFromStore("defaultGoogleSignInAccount"));
    }

    @Nullable
    public String getSavedRefreshToken() {
        return getFromStore("refreshToken");
    }

    protected void removeFromStore(String str) {
        this.zzah.lock();
        try {
            this.zzai.edit().remove(str).apply();
        } finally {
            this.zzah.unlock();
        }
    }

    public void removeSavedDefaultGoogleSignInAccount() {
        String fromStore = getFromStore("defaultGoogleSignInAccount");
        removeFromStore("defaultGoogleSignInAccount");
        if (!TextUtils.isEmpty(fromStore)) {
            removeFromStore(zza("googleSignInAccount", fromStore));
            removeFromStore(zza("googleSignInOptions", fromStore));
        }
    }

    public void saveDefaultGoogleSignInAccount(GoogleSignInAccount googleSignInAccount, GoogleSignInOptions googleSignInOptions) {
        Preconditions.checkNotNull(googleSignInAccount);
        Preconditions.checkNotNull(googleSignInOptions);
        saveToStore("defaultGoogleSignInAccount", googleSignInAccount.getObfuscatedIdentifier());
        Preconditions.checkNotNull(googleSignInAccount);
        Preconditions.checkNotNull(googleSignInOptions);
        String obfuscatedIdentifier = googleSignInAccount.getObfuscatedIdentifier();
        saveToStore(zza("googleSignInAccount", obfuscatedIdentifier), googleSignInAccount.toJsonForStorage());
        saveToStore(zza("googleSignInOptions", obfuscatedIdentifier), googleSignInOptions.toJson());
    }

    public void saveRefreshToken(String str) {
        if (!TextUtils.isEmpty(str)) {
            saveToStore("refreshToken", str);
        }
    }

    protected void saveToStore(String str, String str2) {
        this.zzah.lock();
        try {
            this.zzai.edit().putString(str, str2).apply();
        } finally {
            this.zzah.unlock();
        }
    }
}
