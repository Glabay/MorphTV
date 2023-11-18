package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;

public abstract class DowngradeableSafeParcel extends AbstractSafeParcelable implements ReflectedParcelable {
    private static final Object zzsj = new Object();
    private static ClassLoader zzsk;
    private static Integer zzsl;
    private boolean zzsm = false;

    public static final class DowngradeableSafeParcelHelper {
        public static Bundle getExtras(Intent intent, Context context, Integer num) {
            Bundle extras;
            synchronized (DowngradeableSafeParcel.zzsj) {
                extras = DowngradeableSafeParcel.getExtras(intent, context, num);
            }
            return extras;
        }

        public static <T extends Parcelable> T getParcelable(Intent intent, String str, Context context, Integer num) {
            T parcelable;
            synchronized (DowngradeableSafeParcel.zzsj) {
                parcelable = DowngradeableSafeParcel.getParcelable(intent, str, context, num);
            }
            return parcelable;
        }

        public static <T extends Parcelable> T getParcelable(Bundle bundle, String str, Context context, Integer num) {
            T parcelable;
            synchronized (DowngradeableSafeParcel.zzsj) {
                parcelable = DowngradeableSafeParcel.getParcelable(bundle, str, context, num);
            }
            return parcelable;
        }

        public static boolean putParcelable(Bundle bundle, String str, DowngradeableSafeParcel downgradeableSafeParcel, Context context, Integer num) {
            return DowngradeableSafeParcel.putParcelable(bundle, str, downgradeableSafeParcel, context, num);
        }
    }

    protected static boolean canUnparcelSafely(java.lang.String r1) {
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
        r0 = getUnparcelClassLoader();
        if (r0 != 0) goto L_0x0008;
    L_0x0006:
        r1 = 1;
        return r1;
    L_0x0008:
        r1 = r0.loadClass(r1);	 Catch:{ Exception -> 0x0011 }
        r1 = zza(r1);	 Catch:{ Exception -> 0x0011 }
        return r1;
    L_0x0011:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.DowngradeableSafeParcel.canUnparcelSafely(java.lang.String):boolean");
    }

    static Bundle getExtras(Intent intent, Context context, Integer num) {
        Bundle bundle;
        if (context != null) {
            try {
                zza(context.getClassLoader(), num);
                if (intent.getExtras() != null) {
                    bundle = new Bundle();
                    bundle.putAll(intent.getExtras());
                    zza(null, null);
                    return bundle;
                }
            } catch (Throwable th) {
                zza(null, null);
            }
        }
        bundle = null;
        zza(null, null);
        return bundle;
    }

    static <T extends Parcelable> T getParcelable(Intent intent, String str, Context context, Integer num) {
        T parcelableExtra;
        if (context != null) {
            try {
                zza(context.getClassLoader(), num);
                parcelableExtra = intent.getParcelableExtra(str);
            } catch (Throwable th) {
                zza(null, null);
            }
        } else {
            parcelableExtra = null;
        }
        zza(null, null);
        return parcelableExtra;
    }

    static <T extends Parcelable> T getParcelable(Bundle bundle, String str, Context context, Integer num) {
        T parcelable;
        if (context != null) {
            try {
                zza(context.getClassLoader(), num);
                parcelable = bundle.getParcelable(str);
            } catch (Throwable th) {
                zza(null, null);
            }
        } else {
            parcelable = null;
        }
        zza(null, null);
        return parcelable;
    }

    protected static ClassLoader getUnparcelClassLoader() {
        ClassLoader classLoader;
        synchronized (zzsj) {
            classLoader = zzsk;
        }
        return classLoader;
    }

    protected static Integer getUnparcelClientVersion() {
        Integer num;
        synchronized (zzsj) {
            num = zzsl;
        }
        return num;
    }

    static boolean putParcelable(Bundle bundle, String str, DowngradeableSafeParcel downgradeableSafeParcel, Context context, Integer num) {
        boolean z = false;
        if (context == null && num == null) {
            return false;
        }
        if (downgradeableSafeParcel.zza(context, num)) {
            bundle.putParcelable(str, downgradeableSafeParcel);
            z = true;
        }
        return z;
    }

    private static void zza(ClassLoader classLoader, Integer num) {
        synchronized (zzsj) {
            zzsk = classLoader;
            zzsl = num;
        }
    }

    private final boolean zza(android.content.Context r1, java.lang.Integer r2) {
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
        r0 = this;
        if (r2 == 0) goto L_0x000b;
    L_0x0002:
        r1 = r2.intValue();
        r1 = r0.prepareForClientVersion(r1);
        return r1;
    L_0x000b:
        r1 = r1.getClassLoader();	 Catch:{ Exception -> 0x0025 }
        r2 = r0.getClass();	 Catch:{ Exception -> 0x0025 }
        r2 = r2.getCanonicalName();	 Catch:{ Exception -> 0x0025 }
        r1 = r1.loadClass(r2);	 Catch:{ Exception -> 0x0025 }
        r1 = zza(r1);	 Catch:{ Exception -> 0x0025 }
        r2 = 1;	 Catch:{ Exception -> 0x0025 }
        r1 = r1 ^ r2;	 Catch:{ Exception -> 0x0025 }
        r0.setShouldDowngrade(r1);	 Catch:{ Exception -> 0x0025 }
        return r2;
    L_0x0025:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.DowngradeableSafeParcel.zza(android.content.Context, java.lang.Integer):boolean");
    }

    private static boolean zza(java.lang.Class<?> r3) {
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
        r0 = 0;
        r1 = "NULL";	 Catch:{ NoSuchFieldException -> 0x0013, NoSuchFieldException -> 0x0013 }
        r3 = r3.getField(r1);	 Catch:{ NoSuchFieldException -> 0x0013, NoSuchFieldException -> 0x0013 }
        r1 = "SAFE_PARCELABLE_NULL_STRING";	 Catch:{ NoSuchFieldException -> 0x0013, NoSuchFieldException -> 0x0013 }
        r2 = 0;	 Catch:{ NoSuchFieldException -> 0x0013, NoSuchFieldException -> 0x0013 }
        r3 = r3.get(r2);	 Catch:{ NoSuchFieldException -> 0x0013, NoSuchFieldException -> 0x0013 }
        r3 = r1.equals(r3);	 Catch:{ NoSuchFieldException -> 0x0013, NoSuchFieldException -> 0x0013 }
        return r3;
    L_0x0013:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.DowngradeableSafeParcel.zza(java.lang.Class):boolean");
    }

    protected abstract boolean prepareForClientVersion(int i);

    public void setShouldDowngrade(boolean z) {
        this.zzsm = z;
    }

    protected boolean shouldDowngrade() {
        return this.zzsm;
    }
}
