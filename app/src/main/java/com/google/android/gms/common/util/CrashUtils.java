package com.google.android.gms.common.util;

import android.content.Context;
import android.os.DropBoxManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.annotation.concurrent.GuardedBy;

public final class CrashUtils {
    private static final String[] zzzc = new String[]{"android.", "com.android.", "dalvik.", "java.", "javax."};
    private static DropBoxManager zzzd = null;
    private static boolean zzze = false;
    private static boolean zzzf = false;
    private static boolean zzzg = false;
    private static int zzzh = -1;
    @GuardedBy("CrashUtils.class")
    private static int zzzi;
    @GuardedBy("CrashUtils.class")
    private static int zzzj;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorDialogData {
        public static final int AVG_CRASH_FREQ = 2;
        public static final int BINDER_CRASH = 268435456;
        public static final int DYNAMITE_CRASH = 536870912;
        public static final int FORCED_SHUSHED_BY_WRAPPER = 4;
        public static final int NONE = 0;
        public static final int POPUP_FREQ = 1;
        public static final int SUPPRESSED = 1073741824;
    }

    public static boolean addDynamiteErrorToDropBox(Context context, Throwable th) {
        return addErrorToDropBoxInternal(context, th, ErrorDialogData.DYNAMITE_CRASH);
    }

    @Deprecated
    public static boolean addErrorToDropBox(Context context, Throwable th) {
        return addDynamiteErrorToDropBox(context, th);
    }

    public static boolean addErrorToDropBoxInternal(Context context, String str, String str2, int i) {
        return zza(context, str, str2, i, null);
    }

    public static boolean addErrorToDropBoxInternal(Context context, Throwable th, int i) {
        boolean zzdb;
        try {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(th);
            if (!isPackageSide()) {
                return false;
            }
            if (!zzdb()) {
                th = zza(th);
                if (th == null) {
                    return false;
                }
            }
            return zza(context, Log.getStackTraceString(th), ProcessUtils.getMyProcessName(), i, th);
        } catch (Throwable e) {
            try {
                zzdb = zzdb();
            } catch (Throwable th2) {
                Log.e("CrashUtils", "Error determining which process we're running in!", th2);
                zzdb = false;
            }
            if (zzdb) {
                throw e;
            }
            Log.e("CrashUtils", "Error adding exception to DropBox!", e);
            return false;
        }
    }

    private static boolean isPackageSide() {
        return zzze ? zzzf : false;
    }

    public static boolean isSystemClassPrefixInternal(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String startsWith : zzzc) {
            if (str.startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    public static synchronized void setTestVariables(DropBoxManager dropBoxManager, boolean z, boolean z2, int i) {
        synchronized (CrashUtils.class) {
            zzze = true;
            zzzd = dropBoxManager;
            zzzg = z;
            zzzf = z2;
            zzzh = i;
            zzzi = 0;
            zzzj = 0;
        }
    }

    @com.google.android.gms.common.util.VisibleForTesting
    private static synchronized java.lang.String zza(android.content.Context r7, java.lang.String r8, java.lang.String r9, int r10) {
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
        r0 = com.google.android.gms.common.util.CrashUtils.class;
        monitor-enter(r0);
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0196 }
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;	 Catch:{ all -> 0x0196 }
        r1.<init>(r2);	 Catch:{ all -> 0x0196 }
        r2 = "Process: ";	 Catch:{ all -> 0x0196 }
        r1.append(r2);	 Catch:{ all -> 0x0196 }
        r9 = com.google.android.gms.common.util.Strings.nullToEmpty(r9);	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = "\n";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = "Package: com.google.android.gms";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = 12451009; // 0xbdfcc1 float:1.744758E-38 double:6.151616E-317;	 Catch:{ all -> 0x0196 }
        r2 = "12.4.51 (020308-{{cl}})";	 Catch:{ all -> 0x0196 }
        r3 = zzdb();	 Catch:{ all -> 0x0196 }
        r4 = 0;
        if (r3 == 0) goto L_0x004f;
    L_0x002c:
        r3 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r7);	 Catch:{ Exception -> 0x0047 }
        r5 = r7.getPackageName();	 Catch:{ Exception -> 0x0047 }
        r3 = r3.getPackageInfo(r5, r4);	 Catch:{ Exception -> 0x0047 }
        r5 = r3.versionCode;	 Catch:{ Exception -> 0x0047 }
        r9 = r3.versionName;	 Catch:{ Exception -> 0x0043 }
        if (r9 == 0) goto L_0x0041;	 Catch:{ Exception -> 0x0043 }
    L_0x003e:
        r9 = r3.versionName;	 Catch:{ Exception -> 0x0043 }
        r2 = r9;
    L_0x0041:
        r9 = r5;
        goto L_0x004f;
    L_0x0043:
        r9 = move-exception;
        r3 = r9;
        r9 = r5;
        goto L_0x0048;
    L_0x0047:
        r3 = move-exception;
    L_0x0048:
        r5 = "CrashUtils";	 Catch:{ all -> 0x0196 }
        r6 = "Error while trying to get the package information! Using static version.";	 Catch:{ all -> 0x0196 }
        android.util.Log.w(r5, r6, r3);	 Catch:{ all -> 0x0196 }
    L_0x004f:
        r3 = " v";	 Catch:{ all -> 0x0196 }
        r1.append(r3);	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x0196 }
        if (r9 != 0) goto L_0x0096;	 Catch:{ all -> 0x0196 }
    L_0x005d:
        r9 = "(";	 Catch:{ all -> 0x0196 }
        r9 = r2.contains(r9);	 Catch:{ all -> 0x0196 }
        if (r9 == 0) goto L_0x0089;	 Catch:{ all -> 0x0196 }
    L_0x0065:
        r9 = ")";	 Catch:{ all -> 0x0196 }
        r9 = r2.contains(r9);	 Catch:{ all -> 0x0196 }
        if (r9 != 0) goto L_0x0089;	 Catch:{ all -> 0x0196 }
    L_0x006d:
        r9 = "-";	 Catch:{ all -> 0x0196 }
        r9 = r2.endsWith(r9);	 Catch:{ all -> 0x0196 }
        if (r9 == 0) goto L_0x007f;	 Catch:{ all -> 0x0196 }
    L_0x0075:
        r9 = java.lang.String.valueOf(r2);	 Catch:{ all -> 0x0196 }
        r2 = "111111111";	 Catch:{ all -> 0x0196 }
        r2 = r9.concat(r2);	 Catch:{ all -> 0x0196 }
    L_0x007f:
        r9 = java.lang.String.valueOf(r2);	 Catch:{ all -> 0x0196 }
        r2 = ")";	 Catch:{ all -> 0x0196 }
        r2 = r9.concat(r2);	 Catch:{ all -> 0x0196 }
    L_0x0089:
        r9 = " (";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r1.append(r2);	 Catch:{ all -> 0x0196 }
        r9 = ")";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
    L_0x0096:
        r9 = "\n";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = "Build: ";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = android.os.Build.FINGERPRINT;	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = "\n";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = android.os.Debug.isDebuggerConnected();	 Catch:{ all -> 0x0196 }
        if (r9 == 0) goto L_0x00b5;	 Catch:{ all -> 0x0196 }
    L_0x00b0:
        r9 = "Debugger: Connected\n";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
    L_0x00b5:
        if (r10 == 0) goto L_0x00c4;	 Catch:{ all -> 0x0196 }
    L_0x00b7:
        r9 = "DD-EDD: ";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r1.append(r10);	 Catch:{ all -> 0x0196 }
        r9 = "\n";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
    L_0x00c4:
        r9 = "\n";	 Catch:{ all -> 0x0196 }
        r1.append(r9);	 Catch:{ all -> 0x0196 }
        r9 = android.text.TextUtils.isEmpty(r8);	 Catch:{ all -> 0x0196 }
        if (r9 != 0) goto L_0x00d2;	 Catch:{ all -> 0x0196 }
    L_0x00cf:
        r1.append(r8);	 Catch:{ all -> 0x0196 }
    L_0x00d2:
        r8 = zzdb();	 Catch:{ all -> 0x0196 }
        if (r8 == 0) goto L_0x00ea;	 Catch:{ all -> 0x0196 }
    L_0x00d8:
        r8 = "logcat_for_system_app_crash";	 Catch:{ all -> 0x0196 }
        r9 = zzzh;	 Catch:{ all -> 0x0196 }
        if (r9 < 0) goto L_0x00e1;	 Catch:{ all -> 0x0196 }
    L_0x00de:
        r7 = zzzh;	 Catch:{ all -> 0x0196 }
        goto L_0x00eb;	 Catch:{ all -> 0x0196 }
    L_0x00e1:
        r7 = r7.getContentResolver();	 Catch:{ all -> 0x0196 }
        r7 = android.provider.Settings.Secure.getInt(r7, r8, r4);	 Catch:{ all -> 0x0196 }
        goto L_0x00eb;	 Catch:{ all -> 0x0196 }
    L_0x00ea:
        r7 = 0;	 Catch:{ all -> 0x0196 }
    L_0x00eb:
        if (r7 <= 0) goto L_0x0190;	 Catch:{ all -> 0x0196 }
    L_0x00ed:
        r8 = "\n";	 Catch:{ all -> 0x0196 }
        r1.append(r8);	 Catch:{ all -> 0x0196 }
        r8 = 0;
        r9 = new java.lang.ProcessBuilder;	 Catch:{ IOException -> 0x017c }
        r10 = 13;	 Catch:{ IOException -> 0x017c }
        r10 = new java.lang.String[r10];	 Catch:{ IOException -> 0x017c }
        r2 = "/system/bin/logcat";	 Catch:{ IOException -> 0x017c }
        r10[r4] = r2;	 Catch:{ IOException -> 0x017c }
        r2 = "-v";	 Catch:{ IOException -> 0x017c }
        r3 = 1;	 Catch:{ IOException -> 0x017c }
        r10[r3] = r2;	 Catch:{ IOException -> 0x017c }
        r2 = 2;	 Catch:{ IOException -> 0x017c }
        r5 = "time";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 3;	 Catch:{ IOException -> 0x017c }
        r5 = "-b";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 4;	 Catch:{ IOException -> 0x017c }
        r5 = "events";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 5;	 Catch:{ IOException -> 0x017c }
        r5 = "-b";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 6;	 Catch:{ IOException -> 0x017c }
        r5 = "system";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 7;	 Catch:{ IOException -> 0x017c }
        r5 = "-b";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 8;	 Catch:{ IOException -> 0x017c }
        r5 = "main";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 9;	 Catch:{ IOException -> 0x017c }
        r5 = "-b";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 10;	 Catch:{ IOException -> 0x017c }
        r5 = "crash";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 11;	 Catch:{ IOException -> 0x017c }
        r5 = "-t";	 Catch:{ IOException -> 0x017c }
        r10[r2] = r5;	 Catch:{ IOException -> 0x017c }
        r2 = 12;	 Catch:{ IOException -> 0x017c }
        r7 = java.lang.String.valueOf(r7);	 Catch:{ IOException -> 0x017c }
        r10[r2] = r7;	 Catch:{ IOException -> 0x017c }
        r9.<init>(r10);	 Catch:{ IOException -> 0x017c }
        r7 = r9.redirectErrorStream(r3);	 Catch:{ IOException -> 0x017c }
        r7 = r7.start();	 Catch:{ IOException -> 0x017c }
        r9 = r7.getOutputStream();	 Catch:{ IOException -> 0x0152 }
        r9.close();	 Catch:{ IOException -> 0x0152 }
    L_0x0152:
        r9 = r7.getErrorStream();	 Catch:{ IOException -> 0x0159 }
        r9.close();	 Catch:{ IOException -> 0x0159 }
    L_0x0159:
        r9 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x017c }
        r7 = r7.getInputStream();	 Catch:{ IOException -> 0x017c }
        r9.<init>(r7);	 Catch:{ IOException -> 0x017c }
        r7 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r7 = new char[r7];	 Catch:{ IOException -> 0x0177, all -> 0x0174 }
    L_0x0166:
        r8 = r9.read(r7);	 Catch:{ IOException -> 0x0177, all -> 0x0174 }
        if (r8 <= 0) goto L_0x0170;	 Catch:{ IOException -> 0x0177, all -> 0x0174 }
    L_0x016c:
        r1.append(r7, r4, r8);	 Catch:{ IOException -> 0x0177, all -> 0x0174 }
        goto L_0x0166;
    L_0x0170:
        r9.close();	 Catch:{ IOException -> 0x0190 }
        goto L_0x0190;
    L_0x0174:
        r7 = move-exception;
        r8 = r9;
        goto L_0x018a;
    L_0x0177:
        r7 = move-exception;
        r8 = r9;
        goto L_0x017d;
    L_0x017a:
        r7 = move-exception;
        goto L_0x018a;
    L_0x017c:
        r7 = move-exception;
    L_0x017d:
        r9 = "CrashUtils";	 Catch:{ all -> 0x017a }
        r10 = "Error running logcat";	 Catch:{ all -> 0x017a }
        android.util.Log.e(r9, r10, r7);	 Catch:{ all -> 0x017a }
        if (r8 == 0) goto L_0x0190;
    L_0x0186:
        r8.close();	 Catch:{ IOException -> 0x0190 }
        goto L_0x0190;
    L_0x018a:
        if (r8 == 0) goto L_0x018f;
    L_0x018c:
        r8.close();	 Catch:{ IOException -> 0x018f }
    L_0x018f:
        throw r7;	 Catch:{ all -> 0x0196 }
    L_0x0190:
        r7 = r1.toString();	 Catch:{ all -> 0x0196 }
        monitor-exit(r0);
        return r7;
    L_0x0196:
        r7 = move-exception;
        monitor-exit(r0);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.CrashUtils.zza(android.content.Context, java.lang.String, java.lang.String, int):java.lang.String");
    }

    @VisibleForTesting
    private static synchronized Throwable zza(Throwable th) {
        synchronized (CrashUtils.class) {
            LinkedList linkedList = new LinkedList();
            while (th != null) {
                linkedList.push(th);
                th = th.getCause();
            }
            Throwable th2 = null;
            int i = 0;
            while (!linkedList.isEmpty()) {
                Throwable th3 = (Throwable) linkedList.pop();
                StackTraceElement[] stackTrace = th3.getStackTrace();
                ArrayList arrayList = new ArrayList();
                arrayList.add(new StackTraceElement(th3.getClass().getName(), "<filtered>", "<filtered>", 1));
                int i2 = i;
                for (Object obj : stackTrace) {
                    Object obj2;
                    String className = obj2.getClassName();
                    Object fileName = obj2.getFileName();
                    int i3 = (TextUtils.isEmpty(fileName) || !fileName.startsWith(":com.google.android.gms")) ? 0 : 1;
                    i2 |= i3;
                    if (i3 == 0 && !isSystemClassPrefixInternal(className)) {
                        obj2 = new StackTraceElement("<filtered>", "<filtered>", "<filtered>", 1);
                    }
                    arrayList.add(obj2);
                }
                th2 = th2 == null ? new Throwable("<filtered>") : new Throwable("<filtered>", th2);
                th2.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[0]));
                i = i2;
            }
            if (i == 0) {
                return null;
            }
            return th2;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized boolean zza(android.content.Context r4, java.lang.String r5, java.lang.String r6, int r7, java.lang.Throwable r8) {
        /*
        r0 = com.google.android.gms.common.util.CrashUtils.class;
        monitor-enter(r0);
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r4);	 Catch:{ all -> 0x0059 }
        r1 = isPackageSide();	 Catch:{ all -> 0x0059 }
        r2 = 0;
        if (r1 == 0) goto L_0x0057;
    L_0x000d:
        r1 = com.google.android.gms.common.util.Strings.isEmptyOrWhitespace(r5);	 Catch:{ all -> 0x0059 }
        if (r1 == 0) goto L_0x0014;
    L_0x0013:
        goto L_0x0057;
    L_0x0014:
        r1 = r5.hashCode();	 Catch:{ all -> 0x0059 }
        if (r8 != 0) goto L_0x001d;
    L_0x001a:
        r8 = zzzj;	 Catch:{ all -> 0x0059 }
        goto L_0x0021;
    L_0x001d:
        r8 = r8.hashCode();	 Catch:{ all -> 0x0059 }
    L_0x0021:
        r3 = zzzi;	 Catch:{ all -> 0x0059 }
        if (r3 != r1) goto L_0x002b;
    L_0x0025:
        r3 = zzzj;	 Catch:{ all -> 0x0059 }
        if (r3 != r8) goto L_0x002b;
    L_0x0029:
        monitor-exit(r0);
        return r2;
    L_0x002b:
        zzzi = r1;	 Catch:{ all -> 0x0059 }
        zzzj = r8;	 Catch:{ all -> 0x0059 }
        r8 = zzzd;	 Catch:{ all -> 0x0059 }
        if (r8 == 0) goto L_0x0036;
    L_0x0033:
        r8 = zzzd;	 Catch:{ all -> 0x0059 }
        goto L_0x003e;
    L_0x0036:
        r8 = "dropbox";
        r8 = r4.getSystemService(r8);	 Catch:{ all -> 0x0059 }
        r8 = (android.os.DropBoxManager) r8;	 Catch:{ all -> 0x0059 }
    L_0x003e:
        if (r8 == 0) goto L_0x0055;
    L_0x0040:
        r1 = "system_app_crash";
        r1 = r8.isTagEnabled(r1);	 Catch:{ all -> 0x0059 }
        if (r1 != 0) goto L_0x0049;
    L_0x0048:
        goto L_0x0055;
    L_0x0049:
        r4 = zza(r4, r5, r6, r7);	 Catch:{ all -> 0x0059 }
        r5 = "system_app_crash";
        r8.addText(r5, r4);	 Catch:{ all -> 0x0059 }
        r4 = 1;
        monitor-exit(r0);
        return r4;
    L_0x0055:
        monitor-exit(r0);
        return r2;
    L_0x0057:
        monitor-exit(r0);
        return r2;
    L_0x0059:
        r4 = move-exception;
        monitor-exit(r0);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.CrashUtils.zza(android.content.Context, java.lang.String, java.lang.String, int, java.lang.Throwable):boolean");
    }

    private static boolean zzdb() {
        return zzze ? zzzg : false;
    }
}
