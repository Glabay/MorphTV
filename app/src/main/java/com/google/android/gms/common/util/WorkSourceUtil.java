package com.google.android.gms.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Process;
import android.os.WorkSource;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.wrappers.Wrappers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkSourceUtil {
    public static final String TAG = "WorkSourceUtil";
    private static final int zzaam = Process.myUid();
    private static final Method zzaan = zzdf();
    private static final Method zzaao = zzdg();
    private static final Method zzaap = zzdh();
    private static final Method zzaaq = zzdi();
    private static final Method zzaar = zzdj();
    private static final Method zzaas = zzdk();
    private static final Method zzaat = zzdl();

    private WorkSourceUtil() {
    }

    public static void add(WorkSource workSource, int i, @Nullable String str) {
        if (zzaao != null) {
            if (str == null) {
                str = "";
            }
            try {
                zzaao.invoke(workSource, new Object[]{Integer.valueOf(i), str});
                return;
            } catch (Throwable e) {
                Log.wtf(TAG, "Unable to assign blame through WorkSource", e);
                return;
            }
        }
        if (zzaan != null) {
            try {
                zzaan.invoke(workSource, new Object[]{Integer.valueOf(i)});
            } catch (Throwable e2) {
                Log.wtf(TAG, "Unable to assign blame through WorkSource", e2);
            }
        }
    }

    @android.support.annotation.Nullable
    public static android.os.WorkSource fromPackage(android.content.Context r3, @android.support.annotation.Nullable java.lang.String r4) {
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
        if (r3 == 0) goto L_0x0055;
    L_0x0003:
        r1 = r3.getPackageManager();
        if (r1 == 0) goto L_0x0055;
    L_0x0009:
        if (r4 != 0) goto L_0x000c;
    L_0x000b:
        return r0;
    L_0x000c:
        r3 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r3);	 Catch:{ NameNotFoundException -> 0x003a }
        r1 = 0;	 Catch:{ NameNotFoundException -> 0x003a }
        r3 = r3.getApplicationInfo(r4, r1);	 Catch:{ NameNotFoundException -> 0x003a }
        if (r3 != 0) goto L_0x0033;
    L_0x0017:
        r3 = "WorkSourceUtil";
        r1 = "Could not get applicationInfo from package: ";
        r4 = java.lang.String.valueOf(r4);
        r2 = r4.length();
        if (r2 == 0) goto L_0x002a;
    L_0x0025:
        r4 = r1.concat(r4);
        goto L_0x002f;
    L_0x002a:
        r4 = new java.lang.String;
        r4.<init>(r1);
    L_0x002f:
        android.util.Log.e(r3, r4);
        return r0;
    L_0x0033:
        r3 = r3.uid;
        r3 = fromUidAndPackage(r3, r4);
        return r3;
    L_0x003a:
        r3 = "WorkSourceUtil";
        r1 = "Could not find package: ";
        r4 = java.lang.String.valueOf(r4);
        r2 = r4.length();
        if (r2 == 0) goto L_0x004d;
    L_0x0048:
        r4 = r1.concat(r4);
        goto L_0x0052;
    L_0x004d:
        r4 = new java.lang.String;
        r4.<init>(r1);
    L_0x0052:
        android.util.Log.e(r3, r4);
    L_0x0055:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.WorkSourceUtil.fromPackage(android.content.Context, java.lang.String):android.os.WorkSource");
    }

    public static WorkSource fromPackageAndModuleExperimentalPi(Context context, String str, String str2) {
        if (!(context == null || context.getPackageManager() == null || str2 == null)) {
            if (str != null) {
                int zzc = zzc(context, str);
                if (zzc < 0) {
                    return null;
                }
                WorkSource workSource = new WorkSource();
                if (zzaas != null) {
                    if (zzaat != null) {
                        try {
                            Object invoke = zzaas.invoke(workSource, new Object[0]);
                            if (zzc != zzaam) {
                                zzaat.invoke(invoke, new Object[]{Integer.valueOf(zzc), str});
                            }
                            zzaat.invoke(invoke, new Object[]{Integer.valueOf(zzaam), str2});
                            return workSource;
                        } catch (Throwable e) {
                            Log.w(TAG, "Unable to assign chained blame through WorkSource", e);
                            return workSource;
                        }
                    }
                }
                add(workSource, zzc, str);
                return workSource;
            }
        }
        Log.w(TAG, "Unexpected null arguments");
        return null;
    }

    public static WorkSource fromUidAndPackage(int i, String str) {
        WorkSource workSource = new WorkSource();
        add(workSource, i, str);
        return workSource;
    }

    public static int get(WorkSource workSource, int i) {
        if (zzaaq != null) {
            try {
                return ((Integer) zzaaq.invoke(workSource, new Object[]{Integer.valueOf(i)})).intValue();
            } catch (Throwable e) {
                Log.wtf(TAG, "Unable to assign blame through WorkSource", e);
            }
        }
        return 0;
    }

    @Nullable
    public static String getName(WorkSource workSource, int i) {
        if (zzaar != null) {
            try {
                return (String) zzaar.invoke(workSource, new Object[]{Integer.valueOf(i)});
            } catch (Throwable e) {
                Log.wtf(TAG, "Unable to assign blame through WorkSource", e);
            }
        }
        return null;
    }

    public static List<String> getNames(@Nullable WorkSource workSource) {
        int size = workSource == null ? 0 : size(workSource);
        if (size == 0) {
            return Collections.emptyList();
        }
        List<String> arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            String name = getName(workSource, i);
            if (!Strings.isEmptyOrWhitespace(name)) {
                arrayList.add(name);
            }
        }
        return arrayList;
    }

    public static boolean hasWorkSourcePermission(Context context) {
        return (context == null || context.getPackageManager() == null || Wrappers.packageManager(context).checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) != 0) ? false : true;
    }

    public static int size(WorkSource workSource) {
        if (zzaap != null) {
            try {
                return ((Integer) zzaap.invoke(workSource, new Object[0])).intValue();
            } catch (Throwable e) {
                Log.wtf(TAG, "Unable to assign blame through WorkSource", e);
            }
        }
        return 0;
    }

    private static int zzc(android.content.Context r3, java.lang.String r4) {
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
        r0 = -1;
        r3 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r3);	 Catch:{ NameNotFoundException -> 0x002b }
        r1 = 0;	 Catch:{ NameNotFoundException -> 0x002b }
        r3 = r3.getApplicationInfo(r4, r1);	 Catch:{ NameNotFoundException -> 0x002b }
        if (r3 != 0) goto L_0x0028;
    L_0x000c:
        r3 = "WorkSourceUtil";
        r1 = "Could not get applicationInfo from package: ";
        r4 = java.lang.String.valueOf(r4);
        r2 = r4.length();
        if (r2 == 0) goto L_0x001f;
    L_0x001a:
        r4 = r1.concat(r4);
        goto L_0x0024;
    L_0x001f:
        r4 = new java.lang.String;
        r4.<init>(r1);
    L_0x0024:
        android.util.Log.e(r3, r4);
        return r0;
    L_0x0028:
        r3 = r3.uid;
        return r3;
    L_0x002b:
        r3 = "WorkSourceUtil";
        r1 = "Could not find package: ";
        r4 = java.lang.String.valueOf(r4);
        r2 = r4.length();
        if (r2 == 0) goto L_0x003e;
    L_0x0039:
        r4 = r1.concat(r4);
        goto L_0x0043;
    L_0x003e:
        r4 = new java.lang.String;
        r4.<init>(r1);
    L_0x0043:
        android.util.Log.e(r3, r4);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.WorkSourceUtil.zzc(android.content.Context, java.lang.String):int");
    }

    private static java.lang.reflect.Method zzdf() {
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
        r0 = android.os.WorkSource.class;	 Catch:{ Exception -> 0x0011 }
        r1 = "add";	 Catch:{ Exception -> 0x0011 }
        r2 = 1;	 Catch:{ Exception -> 0x0011 }
        r2 = new java.lang.Class[r2];	 Catch:{ Exception -> 0x0011 }
        r3 = 0;	 Catch:{ Exception -> 0x0011 }
        r4 = java.lang.Integer.TYPE;	 Catch:{ Exception -> 0x0011 }
        r2[r3] = r4;	 Catch:{ Exception -> 0x0011 }
        r0 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x0011 }
        return r0;
    L_0x0011:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.WorkSourceUtil.zzdf():java.lang.reflect.Method");
    }

    private static java.lang.reflect.Method zzdg() {
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
        r0 = com.google.android.gms.common.util.PlatformVersion.isAtLeastJellyBeanMR2();
        if (r0 == 0) goto L_0x001c;
    L_0x0006:
        r0 = android.os.WorkSource.class;	 Catch:{ Exception -> 0x001c }
        r1 = "add";	 Catch:{ Exception -> 0x001c }
        r2 = 2;	 Catch:{ Exception -> 0x001c }
        r2 = new java.lang.Class[r2];	 Catch:{ Exception -> 0x001c }
        r3 = 0;	 Catch:{ Exception -> 0x001c }
        r4 = java.lang.Integer.TYPE;	 Catch:{ Exception -> 0x001c }
        r2[r3] = r4;	 Catch:{ Exception -> 0x001c }
        r3 = 1;	 Catch:{ Exception -> 0x001c }
        r4 = java.lang.String.class;	 Catch:{ Exception -> 0x001c }
        r2[r3] = r4;	 Catch:{ Exception -> 0x001c }
        r0 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x001c }
        return r0;
    L_0x001c:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.WorkSourceUtil.zzdg():java.lang.reflect.Method");
    }

    private static java.lang.reflect.Method zzdh() {
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
        r0 = android.os.WorkSource.class;	 Catch:{ Exception -> 0x000c }
        r1 = "size";	 Catch:{ Exception -> 0x000c }
        r2 = 0;	 Catch:{ Exception -> 0x000c }
        r2 = new java.lang.Class[r2];	 Catch:{ Exception -> 0x000c }
        r0 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x000c }
        return r0;
    L_0x000c:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.WorkSourceUtil.zzdh():java.lang.reflect.Method");
    }

    private static java.lang.reflect.Method zzdi() {
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
        r0 = android.os.WorkSource.class;	 Catch:{ Exception -> 0x0011 }
        r1 = "get";	 Catch:{ Exception -> 0x0011 }
        r2 = 1;	 Catch:{ Exception -> 0x0011 }
        r2 = new java.lang.Class[r2];	 Catch:{ Exception -> 0x0011 }
        r3 = 0;	 Catch:{ Exception -> 0x0011 }
        r4 = java.lang.Integer.TYPE;	 Catch:{ Exception -> 0x0011 }
        r2[r3] = r4;	 Catch:{ Exception -> 0x0011 }
        r0 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x0011 }
        return r0;
    L_0x0011:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.WorkSourceUtil.zzdi():java.lang.reflect.Method");
    }

    private static java.lang.reflect.Method zzdj() {
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
        r0 = com.google.android.gms.common.util.PlatformVersion.isAtLeastJellyBeanMR2();
        if (r0 == 0) goto L_0x0017;
    L_0x0006:
        r0 = android.os.WorkSource.class;	 Catch:{ Exception -> 0x0017 }
        r1 = "getName";	 Catch:{ Exception -> 0x0017 }
        r2 = 1;	 Catch:{ Exception -> 0x0017 }
        r2 = new java.lang.Class[r2];	 Catch:{ Exception -> 0x0017 }
        r3 = 0;	 Catch:{ Exception -> 0x0017 }
        r4 = java.lang.Integer.TYPE;	 Catch:{ Exception -> 0x0017 }
        r2[r3] = r4;	 Catch:{ Exception -> 0x0017 }
        r0 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x0017 }
        return r0;
    L_0x0017:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.WorkSourceUtil.zzdj():java.lang.reflect.Method");
    }

    private static final Method zzdk() {
        if (PlatformVersion.isAtLeastP()) {
            try {
                return WorkSource.class.getMethod("createWorkChain", new Class[0]);
            } catch (Throwable e) {
                Log.w(TAG, "Missing WorkChain API createWorkChain", e);
            }
        }
        return null;
    }

    @SuppressLint({"PrivateApi"})
    private static final Method zzdl() {
        if (PlatformVersion.isAtLeastP()) {
            try {
                return Class.forName("android.os.WorkSource$WorkChain").getMethod("addNode", new Class[]{Integer.TYPE, String.class});
            } catch (Throwable e) {
                Log.w(TAG, "Missing WorkChain class", e);
            }
        }
        return null;
    }
}
