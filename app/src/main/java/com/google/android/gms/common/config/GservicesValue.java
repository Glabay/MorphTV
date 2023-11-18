package com.google.android.gms.common.config;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.stable.zzg;
import com.google.android.gms.internal.stable.zzi;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.annotation.concurrent.GuardedBy;

public abstract class GservicesValue<T> {
    private static final Object sLock = new Object();
    private static zza zzmu = null;
    private static int zzmv = 0;
    private static Context zzmw = null;
    private static String zzmx = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    @GuardedBy("sLock")
    private static HashSet<String> zzmy;
    protected final T mDefaultValue;
    protected final String mKey;
    private T zzmz = null;

    private interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Boolean zza(String str, Boolean bool);

        Double zza(String str, Double d);

        Float zza(String str, Float f);

        Integer zza(String str, Integer num);

        String zzb(String str, String str2);
    }

    private static class zzb implements zza {
        private static final Collection<GservicesValue<?>> zzna = new HashSet();

        private zzb() {
        }

        public final Long getLong(String str, Long l) {
            return l;
        }

        public final String getString(String str, String str2) {
            return str2;
        }

        public final Boolean zza(String str, Boolean bool) {
            return bool;
        }

        public final Double zza(String str, Double d) {
            return d;
        }

        public final Float zza(String str, Float f) {
            return f;
        }

        public final Integer zza(String str, Integer num) {
            return num;
        }

        public final String zzb(String str, String str2) {
            return str2;
        }
    }

    @Deprecated
    private static class zzc implements zza {
        private final Map<String, ?> values;

        public zzc(Map<String, ?> map) {
            this.values = map;
        }

        private final <T> T zza(String str, T t) {
            return this.values.containsKey(str) ? this.values.get(str) : t;
        }

        public final Long getLong(String str, Long l) {
            return (Long) zza(str, (Object) l);
        }

        public final String getString(String str, String str2) {
            return (String) zza(str, (Object) str2);
        }

        public final Boolean zza(String str, Boolean bool) {
            return (Boolean) zza(str, (Object) bool);
        }

        public final Double zza(String str, Double d) {
            return (Double) zza(str, (Object) d);
        }

        public final Float zza(String str, Float f) {
            return (Float) zza(str, (Object) f);
        }

        public final Integer zza(String str, Integer num) {
            return (Integer) zza(str, (Object) num);
        }

        public final String zzb(String str, String str2) {
            return (String) zza(str, (Object) str2);
        }
    }

    private static class zzd implements zza {
        private final ContentResolver mContentResolver;

        public zzd(ContentResolver contentResolver) {
            this.mContentResolver = contentResolver;
        }

        public final Long getLong(String str, Long l) {
            return Long.valueOf(zzi.getLong(this.mContentResolver, str, l.longValue()));
        }

        public final String getString(String str, String str2) {
            return zzi.zza(this.mContentResolver, str, str2);
        }

        public final Boolean zza(String str, Boolean bool) {
            return Boolean.valueOf(zzi.zza(this.mContentResolver, str, bool.booleanValue()));
        }

        public final java.lang.Double zza(java.lang.String r3, java.lang.Double r4) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r2 = this;
            r0 = r2.mContentResolver;
            r1 = 0;
            r3 = com.google.android.gms.internal.stable.zzi.zza(r0, r3, r1);
            if (r3 == 0) goto L_0x0012;
        L_0x0009:
            r0 = java.lang.Double.parseDouble(r3);	 Catch:{ NumberFormatException -> 0x0012 }
            r3 = java.lang.Double.valueOf(r0);	 Catch:{ NumberFormatException -> 0x0012 }
            return r3;
        L_0x0012:
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.config.GservicesValue.zzd.zza(java.lang.String, java.lang.Double):java.lang.Double");
        }

        public final java.lang.Float zza(java.lang.String r3, java.lang.Float r4) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r2 = this;
            r0 = r2.mContentResolver;
            r1 = 0;
            r3 = com.google.android.gms.internal.stable.zzi.zza(r0, r3, r1);
            if (r3 == 0) goto L_0x0012;
        L_0x0009:
            r3 = java.lang.Float.parseFloat(r3);	 Catch:{ NumberFormatException -> 0x0012 }
            r3 = java.lang.Float.valueOf(r3);	 Catch:{ NumberFormatException -> 0x0012 }
            return r3;
        L_0x0012:
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.config.GservicesValue.zzd.zza(java.lang.String, java.lang.Float):java.lang.Float");
        }

        public final Integer zza(String str, Integer num) {
            return Integer.valueOf(zzi.getInt(this.mContentResolver, str, num.intValue()));
        }

        public final String zzb(String str, String str2) {
            return zzg.zza(this.mContentResolver, str, str2);
        }
    }

    protected GservicesValue(String str, T t) {
        this.mKey = str;
        this.mDefaultValue = t;
    }

    @Deprecated
    @VisibleForTesting
    public static void forceInit(Context context) {
        forceInit(context, new HashSet());
    }

    @VisibleForTesting
    public static void forceInit(Context context, @Nullable HashSet<String> hashSet) {
        zza(context, new zzd(context.getContentResolver()), hashSet);
    }

    @TargetApi(24)
    public static SharedPreferences getDirectBootCache(Context context) {
        return context.getApplicationContext().createDeviceProtectedStorageContext().getSharedPreferences("gservices-direboot-cache", 0);
    }

    public static int getSharedUserId() {
        return zzmv;
    }

    @Deprecated
    public static void init(Context context) {
        init(context, zzd(context) ? new HashSet() : null);
    }

    public static void init(Context context, @Nullable HashSet<String> hashSet) {
        synchronized (sLock) {
            if (zzmu == null) {
                zza(context, new zzd(context.getContentResolver()), hashSet);
            }
            if (zzmv == 0) {
                try {
                    zzmv = context.getPackageManager().getApplicationInfo("com.google.android.gms", 0).uid;
                } catch (NameNotFoundException e) {
                    Log.e("GservicesValue", e.toString());
                }
            }
        }
    }

    @Deprecated
    @VisibleForTesting
    public static void initForTests() {
        zza(null, new zzb(), new HashSet());
    }

    @VisibleForTesting
    public static void initForTests(Context context, @Nullable HashSet<String> hashSet) {
        zza(context, new zzb(), hashSet);
    }

    @Deprecated
    @VisibleForTesting
    public static void initForTests(String str, Object obj) {
        Map hashMap = new HashMap(1);
        hashMap.put(str, obj);
        initForTests(hashMap);
    }

    @Deprecated
    @VisibleForTesting
    public static void initForTests(Map<String, ?> map) {
        synchronized (sLock) {
            zzmu = new zzc(map);
        }
    }

    public static boolean isInitialized() {
        boolean z;
        synchronized (sLock) {
            z = zzmu != null;
        }
        return z;
    }

    public static GservicesValue<String> partnerSetting(String str, String str2) {
        return new zzg(str, str2);
    }

    @VisibleForTesting
    public static void resetAllOverrides() {
        synchronized (sLock) {
            if (zzcg()) {
                for (GservicesValue resetOverride : zzb.zzna) {
                    resetOverride.resetOverride();
                }
                zzb.zzna.clear();
            }
        }
    }

    public static GservicesValue<Double> value(String str, Double d) {
        return new zzd(str, d);
    }

    public static GservicesValue<Float> value(String str, Float f) {
        return new zze(str, f);
    }

    public static GservicesValue<Integer> value(String str, Integer num) {
        return new zzc(str, num);
    }

    public static GservicesValue<Long> value(String str, Long l) {
        return new zzb(str, l);
    }

    public static GservicesValue<String> value(String str, String str2) {
        return new zzf(str, str2);
    }

    public static GservicesValue<Boolean> value(String str, boolean z) {
        return new zza(str, Boolean.valueOf(z));
    }

    @TargetApi(24)
    private static void zza(@Nullable Context context, zza zza, @Nullable HashSet<String> hashSet) {
        synchronized (sLock) {
            zzmu = zza;
            zzmy = null;
            zzmw = null;
            if (context != null && zzd(context)) {
                zzmy = hashSet;
                zzmw = context.getApplicationContext().createDeviceProtectedStorageContext();
            }
        }
    }

    private static boolean zzcg() {
        boolean z;
        synchronized (sLock) {
            if (!(zzmu instanceof zzb)) {
                if (!(zzmu instanceof zzc)) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    @TargetApi(24)
    private static boolean zzd(Context context) {
        if (!PlatformVersion.isAtLeastN()) {
            return false;
        }
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        return (userManager.isUserUnlocked() || userManager.isUserRunning(Process.myUserHandle())) ? false : true;
    }

    public final T get() {
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
        r6 = this;
        r0 = r6.zzmz;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r0 = r6.zzmz;
        return r0;
    L_0x0007:
        r0 = android.os.StrictMode.allowThreadDiskReads();
        r1 = sLock;
        monitor-enter(r1);
        r2 = zzmw;	 Catch:{ all -> 0x00b1 }
        if (r2 == 0) goto L_0x001c;	 Catch:{ all -> 0x00b1 }
    L_0x0012:
        r2 = zzmw;	 Catch:{ all -> 0x00b1 }
        r2 = zzd(r2);	 Catch:{ all -> 0x00b1 }
        if (r2 == 0) goto L_0x001c;	 Catch:{ all -> 0x00b1 }
    L_0x001a:
        r2 = 1;	 Catch:{ all -> 0x00b1 }
        goto L_0x001d;	 Catch:{ all -> 0x00b1 }
    L_0x001c:
        r2 = 0;	 Catch:{ all -> 0x00b1 }
    L_0x001d:
        r3 = zzmy;	 Catch:{ all -> 0x00b1 }
        r4 = zzmw;	 Catch:{ all -> 0x00b1 }
        monitor-exit(r1);	 Catch:{ all -> 0x00b1 }
        if (r2 == 0) goto L_0x007f;
    L_0x0024:
        r0 = "GservicesValue";
        r1 = 3;
        r0 = android.util.Log.isLoggable(r0, r1);
        if (r0 == 0) goto L_0x004b;
    L_0x002d:
        r0 = "GservicesValue";
        r1 = "Gservice value accessed during directboot: ";
        r2 = r6.mKey;
        r2 = java.lang.String.valueOf(r2);
        r5 = r2.length();
        if (r5 == 0) goto L_0x0042;
    L_0x003d:
        r1 = r1.concat(r2);
        goto L_0x0048;
    L_0x0042:
        r2 = new java.lang.String;
        r2.<init>(r1);
        r1 = r2;
    L_0x0048:
        android.util.Log.d(r0, r1);
    L_0x004b:
        if (r3 == 0) goto L_0x0076;
    L_0x004d:
        r0 = r6.mKey;
        r0 = r3.contains(r0);
        if (r0 != 0) goto L_0x0076;
    L_0x0055:
        r0 = "GservicesValue";
        r1 = "Gservices key not whitelisted for directboot access: ";
        r2 = r6.mKey;
        r2 = java.lang.String.valueOf(r2);
        r3 = r2.length();
        if (r3 == 0) goto L_0x006a;
    L_0x0065:
        r1 = r1.concat(r2);
        goto L_0x0070;
    L_0x006a:
        r2 = new java.lang.String;
        r2.<init>(r1);
        r1 = r2;
    L_0x0070:
        android.util.Log.e(r0, r1);
        r0 = r6.mDefaultValue;
        return r0;
    L_0x0076:
        r0 = r6.mKey;
        r1 = r6.mDefaultValue;
        r0 = r6.retrieveFromDirectBootCache(r4, r0, r1);
        return r0;
    L_0x007f:
        r2 = sLock;
        monitor-enter(r2);
        r1 = 0;
        zzmy = r1;	 Catch:{ all -> 0x00ae }
        zzmw = r1;	 Catch:{ all -> 0x00ae }
        monitor-exit(r2);	 Catch:{ all -> 0x00ae }
        r1 = r6.mKey;	 Catch:{ SecurityException -> 0x0094 }
        r1 = r6.retrieve(r1);	 Catch:{ SecurityException -> 0x0094 }
        android.os.StrictMode.setThreadPolicy(r0);
        return r1;
    L_0x0092:
        r1 = move-exception;
        goto L_0x00aa;
    L_0x0094:
        r1 = android.os.Binder.clearCallingIdentity();	 Catch:{ all -> 0x0092 }
        r3 = r6.mKey;	 Catch:{ all -> 0x00a5 }
        r3 = r6.retrieve(r3);	 Catch:{ all -> 0x00a5 }
        android.os.Binder.restoreCallingIdentity(r1);	 Catch:{ all -> 0x0092 }
        android.os.StrictMode.setThreadPolicy(r0);
        return r3;
    L_0x00a5:
        r3 = move-exception;
        android.os.Binder.restoreCallingIdentity(r1);	 Catch:{ all -> 0x0092 }
        throw r3;	 Catch:{ all -> 0x0092 }
    L_0x00aa:
        android.os.StrictMode.setThreadPolicy(r0);
        throw r1;
    L_0x00ae:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x00ae }
        throw r0;
    L_0x00b1:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x00b1 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.config.GservicesValue.get():T");
    }

    @Deprecated
    public final T getBinderSafe() {
        return get();
    }

    public String getKey() {
        return this.mKey;
    }

    @VisibleForTesting
    public void override(T t) {
        if (!(zzmu instanceof zzb)) {
            Log.w("GservicesValue", "GservicesValue.override(): test should probably call initForTests() first");
        }
        this.zzmz = t;
        synchronized (sLock) {
            if (zzcg()) {
                zzb.zzna.add(this);
            }
        }
    }

    @VisibleForTesting
    public void resetOverride() {
        this.zzmz = null;
    }

    protected abstract T retrieve(String str);

    @TargetApi(24)
    protected T retrieveFromDirectBootCache(Context context, String str, T t) {
        throw new UnsupportedOperationException("The Gservices classes used does not support direct-boot");
    }
}
