package com.google.android.gms.internal.stable;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class zzi {
    private static final Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    private static HashMap<String, String> zzagq;
    private static final Uri zzagv = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    private static final Pattern zzagw = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    private static final Pattern zzagx = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    private static final AtomicBoolean zzagy = new AtomicBoolean();
    private static final HashMap<String, Boolean> zzagz = new HashMap();
    private static final HashMap<String, Integer> zzaha = new HashMap();
    private static final HashMap<String, Long> zzahb = new HashMap();
    private static final HashMap<String, Float> zzahc = new HashMap();
    private static Object zzahd;
    private static boolean zzahe;
    private static String[] zzahf = new String[0];

    public static int getInt(android.content.ContentResolver r3, java.lang.String r4, int r5) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = zzb(r3);
        r1 = zzaha;
        r2 = java.lang.Integer.valueOf(r5);
        r1 = zza(r1, r4, r2);
        r1 = (java.lang.Integer) r1;
        if (r1 == 0) goto L_0x0017;
    L_0x0012:
        r3 = r1.intValue();
        return r3;
    L_0x0017:
        r2 = 0;
        r3 = zza(r3, r4, r2);
        if (r3 != 0) goto L_0x001f;
    L_0x001e:
        goto L_0x0029;
    L_0x001f:
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x0029 }
        r2 = java.lang.Integer.valueOf(r3);	 Catch:{ NumberFormatException -> 0x0029 }
        r1 = r2;
        goto L_0x002a;
    L_0x0029:
        r3 = r5;
    L_0x002a:
        r5 = zzaha;
        zza(r0, r5, r4, r1);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zzi.getInt(android.content.ContentResolver, java.lang.String, int):int");
    }

    public static long getLong(android.content.ContentResolver r4, java.lang.String r5, long r6) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = zzb(r4);
        r1 = zzahb;
        r2 = java.lang.Long.valueOf(r6);
        r1 = zza(r1, r5, r2);
        r1 = (java.lang.Long) r1;
        if (r1 == 0) goto L_0x0017;
    L_0x0012:
        r4 = r1.longValue();
        return r4;
    L_0x0017:
        r2 = 0;
        r4 = zza(r4, r5, r2);
        if (r4 != 0) goto L_0x001f;
    L_0x001e:
        goto L_0x0029;
    L_0x001f:
        r2 = java.lang.Long.parseLong(r4);	 Catch:{ NumberFormatException -> 0x0029 }
        r4 = java.lang.Long.valueOf(r2);	 Catch:{ NumberFormatException -> 0x0029 }
        r6 = r2;
        goto L_0x002a;
    L_0x0029:
        r4 = r1;
    L_0x002a:
        r1 = zzahb;
        zza(r0, r1, r5, r4);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zzi.getLong(android.content.ContentResolver, java.lang.String, long):long");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> T zza(java.util.HashMap<java.lang.String, T> r2, java.lang.String r3, T r4) {
        /*
        r0 = com.google.android.gms.internal.stable.zzi.class;
        monitor-enter(r0);
        r1 = r2.containsKey(r3);	 Catch:{ all -> 0x0016 }
        if (r1 == 0) goto L_0x0013;
    L_0x0009:
        r2 = r2.get(r3);	 Catch:{ all -> 0x0016 }
        if (r2 == 0) goto L_0x0010;
    L_0x000f:
        goto L_0x0011;
    L_0x0010:
        r2 = r4;
    L_0x0011:
        monitor-exit(r0);	 Catch:{ all -> 0x0016 }
        return r2;
    L_0x0013:
        monitor-exit(r0);	 Catch:{ all -> 0x0016 }
        r2 = 0;
        return r2;
    L_0x0016:
        r2 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0016 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zzi.zza(java.util.HashMap, java.lang.String, java.lang.Object):T");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String zza(android.content.ContentResolver r13, java.lang.String r14, java.lang.String r15) {
        /*
        r0 = com.google.android.gms.internal.stable.zzi.class;
        monitor-enter(r0);
        zza(r13);	 Catch:{ all -> 0x00a5 }
        r1 = zzahd;	 Catch:{ all -> 0x00a5 }
        r2 = zzagq;	 Catch:{ all -> 0x00a5 }
        r2 = r2.containsKey(r14);	 Catch:{ all -> 0x00a5 }
        if (r2 == 0) goto L_0x001e;
    L_0x0010:
        r13 = zzagq;	 Catch:{ all -> 0x00a5 }
        r13 = r13.get(r14);	 Catch:{ all -> 0x00a5 }
        r13 = (java.lang.String) r13;	 Catch:{ all -> 0x00a5 }
        if (r13 == 0) goto L_0x001b;
    L_0x001a:
        goto L_0x001c;
    L_0x001b:
        r13 = r15;
    L_0x001c:
        monitor-exit(r0);	 Catch:{ all -> 0x00a5 }
        return r13;
    L_0x001e:
        r2 = zzahf;	 Catch:{ all -> 0x00a5 }
        r3 = r2.length;	 Catch:{ all -> 0x00a5 }
        r4 = 0;
        r5 = 0;
    L_0x0023:
        r6 = 1;
        if (r5 >= r3) goto L_0x0062;
    L_0x0026:
        r7 = r2[r5];	 Catch:{ all -> 0x00a5 }
        r7 = r14.startsWith(r7);	 Catch:{ all -> 0x00a5 }
        if (r7 == 0) goto L_0x005f;
    L_0x002e:
        r1 = zzahe;	 Catch:{ all -> 0x00a5 }
        if (r1 == 0) goto L_0x003a;
    L_0x0032:
        r1 = zzagq;	 Catch:{ all -> 0x00a5 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x00a5 }
        if (r1 == 0) goto L_0x005d;
    L_0x003a:
        r1 = zzahf;	 Catch:{ all -> 0x00a5 }
        r2 = zzagq;	 Catch:{ all -> 0x00a5 }
        r13 = zza(r13, r1);	 Catch:{ all -> 0x00a5 }
        r2.putAll(r13);	 Catch:{ all -> 0x00a5 }
        zzahe = r6;	 Catch:{ all -> 0x00a5 }
        r13 = zzagq;	 Catch:{ all -> 0x00a5 }
        r13 = r13.containsKey(r14);	 Catch:{ all -> 0x00a5 }
        if (r13 == 0) goto L_0x005d;
    L_0x004f:
        r13 = zzagq;	 Catch:{ all -> 0x00a5 }
        r13 = r13.get(r14);	 Catch:{ all -> 0x00a5 }
        r13 = (java.lang.String) r13;	 Catch:{ all -> 0x00a5 }
        if (r13 == 0) goto L_0x005a;
    L_0x0059:
        goto L_0x005b;
    L_0x005a:
        r13 = r15;
    L_0x005b:
        monitor-exit(r0);	 Catch:{ all -> 0x00a5 }
        return r13;
    L_0x005d:
        monitor-exit(r0);	 Catch:{ all -> 0x00a5 }
        return r15;
    L_0x005f:
        r5 = r5 + 1;
        goto L_0x0023;
    L_0x0062:
        monitor-exit(r0);	 Catch:{ all -> 0x00a5 }
        r8 = CONTENT_URI;
        r9 = 0;
        r10 = 0;
        r11 = new java.lang.String[r6];
        r11[r4] = r14;
        r12 = 0;
        r7 = r13;
        r13 = r7.query(r8, r9, r10, r11, r12);
        if (r13 == 0) goto L_0x0095;
    L_0x0073:
        r0 = r13.moveToFirst();	 Catch:{ all -> 0x0093 }
        if (r0 != 0) goto L_0x007a;
    L_0x0079:
        goto L_0x0095;
    L_0x007a:
        r0 = r13.getString(r6);	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x0087;
    L_0x0080:
        r2 = r0.equals(r15);	 Catch:{ all -> 0x0093 }
        if (r2 == 0) goto L_0x0087;
    L_0x0086:
        r0 = r15;
    L_0x0087:
        zza(r1, r14, r0);	 Catch:{ all -> 0x0093 }
        if (r0 == 0) goto L_0x008d;
    L_0x008c:
        r15 = r0;
    L_0x008d:
        if (r13 == 0) goto L_0x0092;
    L_0x008f:
        r13.close();
    L_0x0092:
        return r15;
    L_0x0093:
        r14 = move-exception;
        goto L_0x009f;
    L_0x0095:
        r0 = 0;
        zza(r1, r14, r0);	 Catch:{ all -> 0x0093 }
        if (r13 == 0) goto L_0x009e;
    L_0x009b:
        r13.close();
    L_0x009e:
        return r15;
    L_0x009f:
        if (r13 == 0) goto L_0x00a4;
    L_0x00a1:
        r13.close();
    L_0x00a4:
        throw r14;
    L_0x00a5:
        r13 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00a5 }
        throw r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zzi.zza(android.content.ContentResolver, java.lang.String, java.lang.String):java.lang.String");
    }

    private static Map<String, String> zza(ContentResolver contentResolver, String... strArr) {
        Cursor query = contentResolver.query(zzagv, null, null, strArr, null);
        Map<String, String> treeMap = new TreeMap();
        if (query == null) {
            return treeMap;
        }
        while (query.moveToNext()) {
            try {
                treeMap.put(query.getString(0), query.getString(1));
            } finally {
                query.close();
            }
        }
        return treeMap;
    }

    private static void zza(ContentResolver contentResolver) {
        if (zzagq == null) {
            zzagy.set(false);
            zzagq = new HashMap();
            zzahd = new Object();
            zzahe = false;
            contentResolver.registerContentObserver(CONTENT_URI, true, new zzj(null));
            return;
        }
        if (zzagy.getAndSet(false)) {
            zzagq.clear();
            zzagz.clear();
            zzaha.clear();
            zzahb.clear();
            zzahc.clear();
            zzahd = new Object();
            zzahe = false;
        }
    }

    private static void zza(Object obj, String str, String str2) {
        synchronized (zzi.class) {
            if (obj == zzahd) {
                zzagq.put(str, str2);
            }
        }
    }

    private static <T> void zza(Object obj, HashMap<String, T> hashMap, String str, T t) {
        synchronized (zzi.class) {
            if (obj == zzahd) {
                hashMap.put(str, t);
                zzagq.remove(str);
            }
        }
    }

    public static boolean zza(ContentResolver contentResolver, String str, boolean z) {
        Object zzb = zzb(contentResolver);
        Object obj = (Boolean) zza(zzagz, str, Boolean.valueOf(z));
        if (obj != null) {
            return obj.booleanValue();
        }
        Object zza = zza(contentResolver, str, null);
        if (zza != null) {
            if (!zza.equals("")) {
                if (zzagw.matcher(zza).matches()) {
                    obj = Boolean.valueOf(true);
                    z = true;
                } else if (zzagx.matcher(zza).matches()) {
                    obj = Boolean.valueOf(false);
                    z = false;
                } else {
                    StringBuilder stringBuilder = new StringBuilder("attempt to read gservices key ");
                    stringBuilder.append(str);
                    stringBuilder.append(" (value \"");
                    stringBuilder.append(zza);
                    stringBuilder.append("\") as boolean");
                    Log.w("Gservices", stringBuilder.toString());
                }
            }
        }
        zza(zzb, zzagz, str, obj);
        return z;
    }

    private static Object zzb(ContentResolver contentResolver) {
        Object obj;
        synchronized (zzi.class) {
            zza(contentResolver);
            obj = zzahd;
        }
        return obj;
    }
}
