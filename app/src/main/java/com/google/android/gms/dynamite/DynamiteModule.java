package com.google.android.gms.dynamite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.IDynamiteLoaderV2.Stub;
import javax.annotation.concurrent.GuardedBy;

public final class DynamiteModule {
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzd();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zze();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzf();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION_NO_FORCE_STAGING = new zzg();
    public static final VersionPolicy PREFER_LOCAL = new zzc();
    public static final VersionPolicy PREFER_REMOTE = new zzb();
    @GuardedBy("DynamiteModule.class")
    private static Boolean zzabr;
    @GuardedBy("DynamiteModule.class")
    private static IDynamiteLoader zzabs;
    @GuardedBy("DynamiteModule.class")
    private static IDynamiteLoaderV2 zzabt;
    @GuardedBy("DynamiteModule.class")
    private static String zzabu;
    private static final ThreadLocal<zza> zzabv = new ThreadLocal();
    private static final IVersions zzabw = new zza();
    private final Context zzabx;

    @DynamiteApi
    public static class DynamiteLoaderClassLoader {
        @GuardedBy("DynamiteLoaderClassLoader.class")
        public static ClassLoader sClassLoader;
    }

    public static class LoadingException extends Exception {
        private LoadingException(String str) {
            super(str);
        }

        private LoadingException(String str, Throwable th) {
            super(str, th);
        }
    }

    public interface VersionPolicy {

        public interface IVersions {
            int getLocalVersion(Context context, String str);

            int getRemoteVersion(Context context, String str, boolean z) throws LoadingException;
        }

        public static class SelectionResult {
            public int localVersion = 0;
            public int remoteVersion = 0;
            public int selection = 0;
        }

        SelectionResult selectModule(Context context, String str, IVersions iVersions) throws LoadingException;
    }

    private static class zza {
        public Cursor zzaby;

        private zza() {
        }
    }

    private static class zzb implements IVersions {
        private final int zzabz;
        private final int zzaca = 0;

        public zzb(int i, int i2) {
            this.zzabz = i;
        }

        public final int getLocalVersion(Context context, String str) {
            return this.zzabz;
        }

        public final int getRemoteVersion(Context context, String str, boolean z) {
            return 0;
        }
    }

    private DynamiteModule(Context context) {
        this.zzabx = (Context) Preconditions.checkNotNull(context);
    }

    public static int getLocalVersion(android.content.Context r4, java.lang.String r5) {
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
        r4 = r4.getApplicationContext();	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r4 = r4.getClassLoader();	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = java.lang.String.valueOf(r5);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = r1.length();	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = r1 + 61;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2.<init>(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = "com.google.android.gms.dynamite.descriptors.";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2.append(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2.append(r5);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = ".ModuleDescriptor";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2.append(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = r2.toString();	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r4 = r4.loadClass(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = "MODULE_ID";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = r4.getDeclaredField(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = "MODULE_VERSION";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r4 = r4.getDeclaredField(r2);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = 0;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3 = r1.get(r2);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3 = r3.equals(r5);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        if (r3 != 0) goto L_0x0083;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
    L_0x0044:
        r4 = "DynamiteModule";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = r1.get(r2);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = java.lang.String.valueOf(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = java.lang.String.valueOf(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = r2.length();	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = r2 + 51;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3 = java.lang.String.valueOf(r5);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3 = r3.length();	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = r2 + r3;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3.<init>(r2);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r2 = "Module descriptor id '";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3.append(r2);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3.append(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = "' didn't match expected id '";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3.append(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3.append(r5);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = "'";	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r3.append(r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        r1 = r3.toString();	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        android.util.Log.e(r4, r1);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        return r0;	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
    L_0x0083:
        r4 = r4.getInt(r2);	 Catch:{ ClassNotFoundException -> 0x00a9, Exception -> 0x0088 }
        return r4;
    L_0x0088:
        r4 = move-exception;
        r5 = "DynamiteModule";
        r1 = "Failed to load module descriptor class: ";
        r4 = r4.getMessage();
        r4 = java.lang.String.valueOf(r4);
        r2 = r4.length();
        if (r2 == 0) goto L_0x00a0;
    L_0x009b:
        r4 = r1.concat(r4);
        goto L_0x00a5;
    L_0x00a0:
        r4 = new java.lang.String;
        r4.<init>(r1);
    L_0x00a5:
        android.util.Log.e(r5, r4);
        return r0;
    L_0x00a9:
        r4 = "DynamiteModule";
        r1 = java.lang.String.valueOf(r5);
        r1 = r1.length();
        r1 = r1 + 45;
        r2 = new java.lang.StringBuilder;
        r2.<init>(r1);
        r1 = "Local module descriptor class for ";
        r2.append(r1);
        r2.append(r5);
        r5 = " not found.";
        r2.append(r5);
        r5 = r2.toString();
        android.util.Log.w(r4, r5);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.getLocalVersion(android.content.Context, java.lang.String):int");
    }

    public static Uri getQueryUri(String str, boolean z) {
        String str2 = z ? ProviderConstants.API_PATH_FORCE_STAGING : ProviderConstants.API_PATH;
        StringBuilder stringBuilder = new StringBuilder((String.valueOf(str2).length() + 42) + String.valueOf(str).length());
        stringBuilder.append("content://com.google.android.gms.chimera/");
        stringBuilder.append(str2);
        stringBuilder.append("/");
        stringBuilder.append(str);
        return Uri.parse(stringBuilder.toString());
    }

    public static int getRemoteVersion(Context context, String str) {
        return getRemoteVersion(context, str, false);
    }

    public static int getRemoteVersion(android.content.Context r8, java.lang.String r9, boolean r10) {
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
        r0 = com.google.android.gms.dynamite.DynamiteModule.class;
        monitor-enter(r0);
        r1 = zzabr;	 Catch:{ all -> 0x00e6 }
        if (r1 != 0) goto L_0x00b3;
    L_0x0007:
        r1 = r8.getApplicationContext();	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        r1 = r1.getClassLoader();	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        r2 = com.google.android.gms.dynamite.DynamiteModule.DynamiteLoaderClassLoader.class;	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        r2 = r2.getName();	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        r1 = r1.loadClass(r2);	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        r2 = "sClassLoader";	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        r2 = r1.getDeclaredField(r2);	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        monitor-enter(r1);	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
        r3 = 0;
        r4 = r2.get(r3);	 Catch:{ all -> 0x0087 }
        r4 = (java.lang.ClassLoader) r4;	 Catch:{ all -> 0x0087 }
        if (r4 == 0) goto L_0x0038;	 Catch:{ all -> 0x0087 }
    L_0x0029:
        r2 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x0087 }
        if (r4 != r2) goto L_0x0032;	 Catch:{ all -> 0x0087 }
    L_0x002f:
        r2 = java.lang.Boolean.FALSE;	 Catch:{ all -> 0x0087 }
        goto L_0x0084;
    L_0x0032:
        zza(r4);	 Catch:{ LoadingException -> 0x0035 }
    L_0x0035:
        r2 = java.lang.Boolean.TRUE;	 Catch:{ all -> 0x0087 }
        goto L_0x0084;	 Catch:{ all -> 0x0087 }
    L_0x0038:
        r4 = "com.google.android.gms";	 Catch:{ all -> 0x0087 }
        r5 = r8.getApplicationContext();	 Catch:{ all -> 0x0087 }
        r5 = r5.getPackageName();	 Catch:{ all -> 0x0087 }
        r4 = r4.equals(r5);	 Catch:{ all -> 0x0087 }
        if (r4 == 0) goto L_0x0050;	 Catch:{ all -> 0x0087 }
    L_0x0048:
        r4 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x0087 }
        r2.set(r3, r4);	 Catch:{ all -> 0x0087 }
        goto L_0x002f;
    L_0x0050:
        r4 = zzb(r8, r9, r10);	 Catch:{ LoadingException -> 0x007c }
        r5 = zzabu;	 Catch:{ LoadingException -> 0x007c }
        if (r5 == 0) goto L_0x0079;	 Catch:{ LoadingException -> 0x007c }
    L_0x0058:
        r5 = zzabu;	 Catch:{ LoadingException -> 0x007c }
        r5 = r5.isEmpty();	 Catch:{ LoadingException -> 0x007c }
        if (r5 == 0) goto L_0x0061;	 Catch:{ LoadingException -> 0x007c }
    L_0x0060:
        goto L_0x0079;	 Catch:{ LoadingException -> 0x007c }
    L_0x0061:
        r5 = new com.google.android.gms.dynamite.zzh;	 Catch:{ LoadingException -> 0x007c }
        r6 = zzabu;	 Catch:{ LoadingException -> 0x007c }
        r7 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ LoadingException -> 0x007c }
        r5.<init>(r6, r7);	 Catch:{ LoadingException -> 0x007c }
        zza(r5);	 Catch:{ LoadingException -> 0x007c }
        r2.set(r3, r5);	 Catch:{ LoadingException -> 0x007c }
        r5 = java.lang.Boolean.TRUE;	 Catch:{ LoadingException -> 0x007c }
        zzabr = r5;	 Catch:{ LoadingException -> 0x007c }
        monitor-exit(r1);	 Catch:{ all -> 0x0087 }
        monitor-exit(r0);	 Catch:{ all -> 0x00e6 }
        return r4;
    L_0x0079:
        monitor-exit(r1);	 Catch:{ all -> 0x0087 }
        monitor-exit(r0);	 Catch:{ all -> 0x00e6 }
        return r4;
    L_0x007c:
        r4 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x0087 }
        r2.set(r3, r4);	 Catch:{ all -> 0x0087 }
        goto L_0x002f;	 Catch:{ all -> 0x0087 }
    L_0x0084:
        monitor-exit(r1);	 Catch:{ all -> 0x0087 }
        r1 = r2;	 Catch:{ all -> 0x0087 }
        goto L_0x00b1;	 Catch:{ all -> 0x0087 }
    L_0x0087:
        r2 = move-exception;	 Catch:{ all -> 0x0087 }
        monitor-exit(r1);	 Catch:{ all -> 0x0087 }
        throw r2;	 Catch:{ ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a, ClassNotFoundException -> 0x008a }
    L_0x008a:
        r1 = move-exception;
        r2 = "DynamiteModule";	 Catch:{ all -> 0x00e6 }
        r1 = java.lang.String.valueOf(r1);	 Catch:{ all -> 0x00e6 }
        r3 = java.lang.String.valueOf(r1);	 Catch:{ all -> 0x00e6 }
        r3 = r3.length();	 Catch:{ all -> 0x00e6 }
        r3 = r3 + 30;	 Catch:{ all -> 0x00e6 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e6 }
        r4.<init>(r3);	 Catch:{ all -> 0x00e6 }
        r3 = "Failed to load module via V2: ";	 Catch:{ all -> 0x00e6 }
        r4.append(r3);	 Catch:{ all -> 0x00e6 }
        r4.append(r1);	 Catch:{ all -> 0x00e6 }
        r1 = r4.toString();	 Catch:{ all -> 0x00e6 }
        android.util.Log.w(r2, r1);	 Catch:{ all -> 0x00e6 }
        r1 = java.lang.Boolean.FALSE;	 Catch:{ all -> 0x00e6 }
    L_0x00b1:
        zzabr = r1;	 Catch:{ all -> 0x00e6 }
    L_0x00b3:
        monitor-exit(r0);	 Catch:{ all -> 0x00e6 }
        r0 = r1.booleanValue();
        if (r0 == 0) goto L_0x00e1;
    L_0x00ba:
        r8 = zzb(r8, r9, r10);	 Catch:{ LoadingException -> 0x00bf }
        return r8;
    L_0x00bf:
        r8 = move-exception;
        r9 = "DynamiteModule";
        r10 = "Failed to retrieve remote module version: ";
        r8 = r8.getMessage();
        r8 = java.lang.String.valueOf(r8);
        r0 = r8.length();
        if (r0 == 0) goto L_0x00d7;
    L_0x00d2:
        r8 = r10.concat(r8);
        goto L_0x00dc;
    L_0x00d7:
        r8 = new java.lang.String;
        r8.<init>(r10);
    L_0x00dc:
        android.util.Log.w(r9, r8);
        r8 = 0;
        return r8;
    L_0x00e1:
        r8 = zza(r8, r9, r10);
        return r8;
    L_0x00e6:
        r8 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00e6 }
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.getRemoteVersion(android.content.Context, java.lang.String, boolean):int");
    }

    @VisibleForTesting
    public static synchronized Boolean getUseV2ForTesting() {
        Boolean bool;
        synchronized (DynamiteModule.class) {
            bool = zzabr;
        }
        return bool;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.dynamite.DynamiteModule load(android.content.Context r10, com.google.android.gms.dynamite.DynamiteModule.VersionPolicy r11, java.lang.String r12) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /*
        r0 = zzabv;
        r0 = r0.get();
        r0 = (com.google.android.gms.dynamite.DynamiteModule.zza) r0;
        r1 = new com.google.android.gms.dynamite.DynamiteModule$zza;
        r2 = 0;
        r1.<init>();
        r3 = zzabv;
        r3.set(r1);
        r3 = zzabw;	 Catch:{ all -> 0x0131 }
        r3 = r11.selectModule(r10, r12, r3);	 Catch:{ all -> 0x0131 }
        r4 = "DynamiteModule";
        r5 = r3.localVersion;	 Catch:{ all -> 0x0131 }
        r6 = r3.remoteVersion;	 Catch:{ all -> 0x0131 }
        r7 = java.lang.String.valueOf(r12);	 Catch:{ all -> 0x0131 }
        r7 = r7.length();	 Catch:{ all -> 0x0131 }
        r7 = r7 + 68;
        r8 = java.lang.String.valueOf(r12);	 Catch:{ all -> 0x0131 }
        r8 = r8.length();	 Catch:{ all -> 0x0131 }
        r7 = r7 + r8;
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0131 }
        r8.<init>(r7);	 Catch:{ all -> 0x0131 }
        r7 = "Considering local module ";
        r8.append(r7);	 Catch:{ all -> 0x0131 }
        r8.append(r12);	 Catch:{ all -> 0x0131 }
        r7 = ":";
        r8.append(r7);	 Catch:{ all -> 0x0131 }
        r8.append(r5);	 Catch:{ all -> 0x0131 }
        r5 = " and remote module ";
        r8.append(r5);	 Catch:{ all -> 0x0131 }
        r8.append(r12);	 Catch:{ all -> 0x0131 }
        r5 = ":";
        r8.append(r5);	 Catch:{ all -> 0x0131 }
        r8.append(r6);	 Catch:{ all -> 0x0131 }
        r5 = r8.toString();	 Catch:{ all -> 0x0131 }
        android.util.Log.i(r4, r5);	 Catch:{ all -> 0x0131 }
        r4 = r3.selection;	 Catch:{ all -> 0x0131 }
        if (r4 == 0) goto L_0x0107;
    L_0x0062:
        r4 = r3.selection;	 Catch:{ all -> 0x0131 }
        r5 = -1;
        if (r4 != r5) goto L_0x006b;
    L_0x0067:
        r4 = r3.localVersion;	 Catch:{ all -> 0x0131 }
        if (r4 == 0) goto L_0x0107;
    L_0x006b:
        r4 = r3.selection;	 Catch:{ all -> 0x0131 }
        r6 = 1;
        if (r4 != r6) goto L_0x0076;
    L_0x0070:
        r4 = r3.remoteVersion;	 Catch:{ all -> 0x0131 }
        if (r4 != 0) goto L_0x0076;
    L_0x0074:
        goto L_0x0107;
    L_0x0076:
        r4 = r3.selection;	 Catch:{ all -> 0x0131 }
        if (r4 != r5) goto L_0x008d;
    L_0x007a:
        r10 = zzd(r10, r12);	 Catch:{ all -> 0x0131 }
        r11 = r1.zzaby;
        if (r11 == 0) goto L_0x0087;
    L_0x0082:
        r11 = r1.zzaby;
        r11.close();
    L_0x0087:
        r11 = zzabv;
        r11.set(r0);
        return r10;
    L_0x008d:
        r4 = r3.selection;	 Catch:{ all -> 0x0131 }
        if (r4 != r6) goto L_0x00ec;
    L_0x0091:
        r4 = r3.remoteVersion;	 Catch:{ LoadingException -> 0x00a6 }
        r4 = zza(r10, r12, r4);	 Catch:{ LoadingException -> 0x00a6 }
        r10 = r1.zzaby;
        if (r10 == 0) goto L_0x00a0;
    L_0x009b:
        r10 = r1.zzaby;
        r10.close();
    L_0x00a0:
        r10 = zzabv;
        r10.set(r0);
        return r4;
    L_0x00a6:
        r4 = move-exception;
        r6 = "DynamiteModule";
        r7 = "Failed to load remote module: ";
        r8 = r4.getMessage();	 Catch:{ all -> 0x0131 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ all -> 0x0131 }
        r9 = r8.length();	 Catch:{ all -> 0x0131 }
        if (r9 == 0) goto L_0x00be;
    L_0x00b9:
        r7 = r7.concat(r8);	 Catch:{ all -> 0x0131 }
        goto L_0x00c4;
    L_0x00be:
        r8 = new java.lang.String;	 Catch:{ all -> 0x0131 }
        r8.<init>(r7);	 Catch:{ all -> 0x0131 }
        r7 = r8;
    L_0x00c4:
        android.util.Log.w(r6, r7);	 Catch:{ all -> 0x0131 }
        r6 = r3.localVersion;	 Catch:{ all -> 0x0131 }
        if (r6 == 0) goto L_0x00e4;
    L_0x00cb:
        r6 = new com.google.android.gms.dynamite.DynamiteModule$zzb;	 Catch:{ all -> 0x0131 }
        r3 = r3.localVersion;	 Catch:{ all -> 0x0131 }
        r7 = 0;
        r6.<init>(r3, r7);	 Catch:{ all -> 0x0131 }
        r11 = r11.selectModule(r10, r12, r6);	 Catch:{ all -> 0x0131 }
        r11 = r11.selection;	 Catch:{ all -> 0x0131 }
        if (r11 != r5) goto L_0x00e4;
    L_0x00db:
        r10 = zzd(r10, r12);	 Catch:{ all -> 0x0131 }
        r11 = r1.zzaby;
        if (r11 == 0) goto L_0x0087;
    L_0x00e3:
        goto L_0x0082;
    L_0x00e4:
        r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException;	 Catch:{ all -> 0x0131 }
        r11 = "Remote load failed. No local fallback found.";
        r10.<init>(r11, r4);	 Catch:{ all -> 0x0131 }
        throw r10;	 Catch:{ all -> 0x0131 }
    L_0x00ec:
        r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException;	 Catch:{ all -> 0x0131 }
        r11 = r3.selection;	 Catch:{ all -> 0x0131 }
        r12 = 47;
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0131 }
        r3.<init>(r12);	 Catch:{ all -> 0x0131 }
        r12 = "VersionPolicy returned invalid code:";
        r3.append(r12);	 Catch:{ all -> 0x0131 }
        r3.append(r11);	 Catch:{ all -> 0x0131 }
        r11 = r3.toString();	 Catch:{ all -> 0x0131 }
        r10.<init>(r11);	 Catch:{ all -> 0x0131 }
        throw r10;	 Catch:{ all -> 0x0131 }
    L_0x0107:
        r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException;	 Catch:{ all -> 0x0131 }
        r11 = r3.localVersion;	 Catch:{ all -> 0x0131 }
        r12 = r3.remoteVersion;	 Catch:{ all -> 0x0131 }
        r3 = 91;
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0131 }
        r4.<init>(r3);	 Catch:{ all -> 0x0131 }
        r3 = "No acceptable module found. Local version is ";
        r4.append(r3);	 Catch:{ all -> 0x0131 }
        r4.append(r11);	 Catch:{ all -> 0x0131 }
        r11 = " and remote version is ";
        r4.append(r11);	 Catch:{ all -> 0x0131 }
        r4.append(r12);	 Catch:{ all -> 0x0131 }
        r11 = ".";
        r4.append(r11);	 Catch:{ all -> 0x0131 }
        r11 = r4.toString();	 Catch:{ all -> 0x0131 }
        r10.<init>(r11);	 Catch:{ all -> 0x0131 }
        throw r10;	 Catch:{ all -> 0x0131 }
    L_0x0131:
        r10 = move-exception;
        r11 = r1.zzaby;
        if (r11 == 0) goto L_0x013b;
    L_0x0136:
        r11 = r1.zzaby;
        r11.close();
    L_0x013b:
        r11 = zzabv;
        r11.set(r0);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.load(android.content.Context, com.google.android.gms.dynamite.DynamiteModule$VersionPolicy, java.lang.String):com.google.android.gms.dynamite.DynamiteModule");
    }

    public static Cursor queryForDynamiteModule(Context context, String str, boolean z) {
        return context.getContentResolver().query(getQueryUri(str, z), null, null, null, null);
    }

    @VisibleForTesting
    public static synchronized void resetInternalStateForTesting() {
        synchronized (DynamiteModule.class) {
            zzabs = null;
            zzabt = null;
            zzabu = null;
            zzabr = null;
            synchronized (DynamiteLoaderClassLoader.class) {
                DynamiteLoaderClassLoader.sClassLoader = null;
            }
        }
    }

    @VisibleForTesting
    public static synchronized void setUseV2ForTesting(Boolean bool) {
        synchronized (DynamiteModule.class) {
            zzabr = bool;
        }
    }

    private static int zza(Context context, String str, boolean z) {
        IDynamiteLoader zzg = zzg(context);
        if (zzg == null) {
            return 0;
        }
        try {
            return zzg.getModuleVersion2(ObjectWrapper.wrap(context), str, z);
        } catch (RemoteException e) {
            str = "DynamiteModule";
            String str2 = "Failed to retrieve remote module version: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.w(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            return 0;
        }
    }

    private static Context zza(Context context, String str, int i, Cursor cursor, IDynamiteLoaderV2 iDynamiteLoaderV2) {
        try {
            return (Context) ObjectWrapper.unwrap(iDynamiteLoaderV2.loadModule2(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(cursor)));
        } catch (Exception e) {
            str = "DynamiteModule";
            String str2 = "Failed to load DynamiteLoader: ";
            String valueOf = String.valueOf(e.toString());
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            return null;
        }
    }

    private static DynamiteModule zza(Context context, String str, int i) throws LoadingException {
        synchronized (DynamiteModule.class) {
            Boolean bool = zzabr;
        }
        if (bool != null) {
            return bool.booleanValue() ? zzc(context, str, i) : zzb(context, str, i);
        } else {
            throw new LoadingException("Failed to determine which loading route to use.");
        }
    }

    @GuardedBy("DynamiteModule.class")
    private static void zza(ClassLoader classLoader) throws LoadingException {
        try {
            zzabt = Stub.asInterface((IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (Throwable e) {
            throw new LoadingException("Failed to instantiate dynamite loader", e);
        }
    }

    private static int zzb(Context context, String str, boolean z) throws LoadingException {
        Throwable e;
        Cursor queryForDynamiteModule;
        try {
            queryForDynamiteModule = queryForDynamiteModule(context, str, z);
            if (queryForDynamiteModule != null) {
                try {
                    if (queryForDynamiteModule.moveToFirst()) {
                        int i = queryForDynamiteModule.getInt(0);
                        if (i > 0) {
                            synchronized (DynamiteModule.class) {
                                zzabu = queryForDynamiteModule.getString(2);
                            }
                            zza zza = (zza) zzabv.get();
                            if (zza != null && zza.zzaby == null) {
                                zza.zzaby = queryForDynamiteModule;
                                queryForDynamiteModule = null;
                            }
                        }
                        if (queryForDynamiteModule != null) {
                            queryForDynamiteModule.close();
                        }
                        return i;
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            }
            Log.w("DynamiteModule", "Failed to retrieve remote module version.");
            throw new LoadingException("Failed to connect to dynamite module ContentResolver.");
        } catch (Exception e3) {
            e = e3;
            queryForDynamiteModule = null;
            try {
                if (e instanceof LoadingException) {
                    throw e;
                }
                throw new LoadingException("V2 version check failed", e);
            } catch (Throwable th) {
                e = th;
                if (queryForDynamiteModule != null) {
                    queryForDynamiteModule.close();
                }
                throw e;
            }
        } catch (Throwable th2) {
            e = th2;
            queryForDynamiteModule = null;
            if (queryForDynamiteModule != null) {
                queryForDynamiteModule.close();
            }
            throw e;
        }
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws LoadingException {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 51);
        stringBuilder.append("Selected remote version of ");
        stringBuilder.append(str);
        stringBuilder.append(", version >= ");
        stringBuilder.append(i);
        Log.i("DynamiteModule", stringBuilder.toString());
        IDynamiteLoader zzg = zzg(context);
        if (zzg == null) {
            throw new LoadingException("Failed to create IDynamiteLoader.");
        }
        try {
            IObjectWrapper createModuleContext = zzg.createModuleContext(ObjectWrapper.wrap(context), str, i);
            if (ObjectWrapper.unwrap(createModuleContext) != null) {
                return new DynamiteModule((Context) ObjectWrapper.unwrap(createModuleContext));
            }
            throw new LoadingException("Failed to load remote module.");
        } catch (Throwable e) {
            throw new LoadingException("Failed to load remote module.", e);
        }
    }

    private static DynamiteModule zzc(Context context, String str, int i) throws LoadingException {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 51);
        stringBuilder.append("Selected remote version of ");
        stringBuilder.append(str);
        stringBuilder.append(", version >= ");
        stringBuilder.append(i);
        Log.i("DynamiteModule", stringBuilder.toString());
        synchronized (DynamiteModule.class) {
            IDynamiteLoaderV2 iDynamiteLoaderV2 = zzabt;
        }
        if (iDynamiteLoaderV2 == null) {
            throw new LoadingException("DynamiteLoaderV2 was not cached.");
        }
        zza zza = (zza) zzabv.get();
        if (zza != null) {
            if (zza.zzaby != null) {
                context = zza(context.getApplicationContext(), str, i, zza.zzaby, iDynamiteLoaderV2);
                if (context != null) {
                    return new DynamiteModule(context);
                }
                throw new LoadingException("Failed to get module context");
            }
        }
        throw new LoadingException("No result cursor");
    }

    private static DynamiteModule zzd(Context context, String str) {
        String str2 = "DynamiteModule";
        String str3 = "Selected local version of ";
        str = String.valueOf(str);
        Log.i(str2, str.length() != 0 ? str3.concat(str) : new String(str3));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static IDynamiteLoader zzg(Context context) {
        synchronized (DynamiteModule.class) {
            IDynamiteLoader iDynamiteLoader;
            if (zzabs != null) {
                iDynamiteLoader = zzabs;
                return iDynamiteLoader;
            } else if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            } else {
                try {
                    iDynamiteLoader = IDynamiteLoader.Stub.asInterface((IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance());
                    if (iDynamiteLoader != null) {
                        zzabs = iDynamiteLoader;
                        return iDynamiteLoader;
                    }
                } catch (Exception e) {
                    String str = "DynamiteModule";
                    String str2 = "Failed to load IDynamiteLoader from GmsCore: ";
                    String valueOf = String.valueOf(e.getMessage());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                }
            }
        }
        return null;
    }

    public final Context getModuleContext() {
        return this.zzabx;
    }

    public final IBinder instantiate(String str) throws LoadingException {
        try {
            return (IBinder) this.zzabx.getClassLoader().loadClass(str).newInstance();
        } catch (Throwable e) {
            String str2 = "Failed to instantiate module class: ";
            str = String.valueOf(str);
            throw new LoadingException(str.length() != 0 ? str2.concat(str) : new String(str2), e);
        }
    }
}
