package com.google.android.gms.common.util;

import android.os.Binder;
import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.Nullable;

public class ProcessUtils {
    private static String zzaai;
    private static int zzaaj;

    public static class SystemGroupsNotAvailableException extends Exception {
        SystemGroupsNotAvailableException(String str) {
            super(str);
        }

        SystemGroupsNotAvailableException(String str, Throwable th) {
            super(str, th);
        }
    }

    private ProcessUtils() {
    }

    @Nullable
    public static String getCallingProcessName() {
        int callingPid = Binder.getCallingPid();
        return callingPid == zzde() ? getMyProcessName() : zzl(callingPid);
    }

    @Nullable
    public static String getMyProcessName() {
        if (zzaai == null) {
            zzaai = zzl(zzde());
        }
        return zzaai;
    }

    public static boolean hasSystemGroups() throws SystemGroupsNotAvailableException {
        Closeable closeable;
        Throwable th;
        Closeable closeable2 = null;
        try {
            int zzde = zzde();
            StringBuilder stringBuilder = new StringBuilder(24);
            stringBuilder.append("/proc/");
            stringBuilder.append(zzde);
            stringBuilder.append("/status");
            Closeable zzm = zzm(stringBuilder.toString());
            try {
                boolean zzk = zzk(zzm);
                IOUtils.closeQuietly(zzm);
                return zzk;
            } catch (Throwable e) {
                closeable = zzm;
                th = e;
                closeable2 = closeable;
                try {
                    throw new SystemGroupsNotAvailableException("Unable to access /proc/pid/status.", th);
                } catch (Throwable th2) {
                    th = th2;
                    IOUtils.closeQuietly(closeable2);
                    throw th;
                }
            } catch (Throwable e2) {
                closeable = zzm;
                th = e2;
                closeable2 = closeable;
                IOUtils.closeQuietly(closeable2);
                throw th;
            }
        } catch (IOException e3) {
            th = e3;
            throw new SystemGroupsNotAvailableException("Unable to access /proc/pid/status.", th);
        }
    }

    private static int zzde() {
        if (zzaaj == 0) {
            zzaaj = Process.myPid();
        }
        return zzaaj;
    }

    private static boolean zzk(java.io.BufferedReader r8) throws java.io.IOException, com.google.android.gms.common.util.ProcessUtils.SystemGroupsNotAvailableException {
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
    L_0x0000:
        r0 = r8.readLine();
        if (r0 == 0) goto L_0x003f;
    L_0x0006:
        r0 = r0.trim();
        r1 = "Groups:";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x0000;
    L_0x0012:
        r8 = 7;
        r8 = r0.substring(r8);
        r8 = r8.trim();
        r0 = "\\s";
        r1 = -1;
        r8 = r8.split(r0, r1);
        r0 = r8.length;
        r1 = 0;
        r2 = 0;
    L_0x0025:
        if (r2 >= r0) goto L_0x003e;
    L_0x0027:
        r3 = r8[r2];
        r3 = java.lang.Long.parseLong(r3);	 Catch:{ NumberFormatException -> 0x003b }
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 < 0) goto L_0x003b;
    L_0x0033:
        r5 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 >= 0) goto L_0x003b;
    L_0x0039:
        r8 = 1;
        return r8;
    L_0x003b:
        r2 = r2 + 1;
        goto L_0x0025;
    L_0x003e:
        return r1;
    L_0x003f:
        r8 = new com.google.android.gms.common.util.ProcessUtils$SystemGroupsNotAvailableException;
        r0 = "Missing Groups entry from proc/pid/status.";
        r8.<init>(r0);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.ProcessUtils.zzk(java.io.BufferedReader):boolean");
    }

    @javax.annotation.Nullable
    private static java.lang.String zzl(int r4) {
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
        r0 = 0;
        if (r4 > 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 25;
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r2.<init>(r1);	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r1 = "/proc/";	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r2.append(r1);	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r2.append(r4);	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r4 = "/cmdline";	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r2.append(r4);	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r4 = r2.toString();	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r4 = zzm(r4);	 Catch:{ IOException -> 0x0037, all -> 0x0032 }
        r1 = r4.readLine();	 Catch:{ IOException -> 0x0038, all -> 0x002d }
        r1 = r1.trim();	 Catch:{ IOException -> 0x0038, all -> 0x002d }
        com.google.android.gms.common.util.IOUtils.closeQuietly(r4);
        r0 = r1;
        return r0;
    L_0x002d:
        r0 = move-exception;
        r3 = r0;
        r0 = r4;
        r4 = r3;
        goto L_0x0033;
    L_0x0032:
        r4 = move-exception;
    L_0x0033:
        com.google.android.gms.common.util.IOUtils.closeQuietly(r0);
        throw r4;
    L_0x0037:
        r4 = r0;
    L_0x0038:
        com.google.android.gms.common.util.IOUtils.closeQuietly(r4);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.ProcessUtils.zzl(int):java.lang.String");
    }

    private static BufferedReader zzm(String str) throws IOException {
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(str));
            return bufferedReader;
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }
}
