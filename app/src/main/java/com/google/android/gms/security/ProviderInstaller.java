package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.reflect.Method;

public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static final Object sLock = new Object();
    private static final GoogleApiAvailabilityLight zzacw = GoogleApiAvailabilityLight.getInstance();
    private static Method zzacx;

    public interface ProviderInstallListener {
        void onProviderInstallFailed(int i, Intent intent);

        void onProviderInstalled();
    }

    public static void installIfNeeded(android.content.Context r8) throws com.google.android.gms.common.GooglePlayServicesRepairableException, com.google.android.gms.common.GooglePlayServicesNotAvailableException {
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
        r0 = "Context must not be null";
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r8, r0);
        r0 = zzacw;
        r1 = 11925000; // 0xb5f608 float:1.6710484E-38 double:5.891733E-317;
        r0.verifyGooglePlayServicesIsAvailable(r8, r1);
        r0 = 8;
        r1 = 6;
        r8 = com.google.android.gms.common.GooglePlayServicesUtilLight.getRemoteContext(r8);	 Catch:{ NotFoundException -> 0x0095 }
        if (r8 != 0) goto L_0x002b;
    L_0x0016:
        r8 = "ProviderInstaller";
        r8 = android.util.Log.isLoggable(r8, r1);
        if (r8 == 0) goto L_0x0025;
    L_0x001e:
        r8 = "ProviderInstaller";
        r1 = "Failed to get remote context";
        android.util.Log.e(r8, r1);
    L_0x0025:
        r8 = new com.google.android.gms.common.GooglePlayServicesNotAvailableException;
        r8.<init>(r0);
        throw r8;
    L_0x002b:
        r2 = sLock;
        monitor-enter(r2);
        r3 = zzacx;	 Catch:{ Exception -> 0x005a }
        r4 = 0;	 Catch:{ Exception -> 0x005a }
        r5 = 1;	 Catch:{ Exception -> 0x005a }
        if (r3 != 0) goto L_0x004c;	 Catch:{ Exception -> 0x005a }
    L_0x0034:
        r3 = r8.getClassLoader();	 Catch:{ Exception -> 0x005a }
        r6 = "com.google.android.gms.common.security.ProviderInstallerImpl";	 Catch:{ Exception -> 0x005a }
        r3 = r3.loadClass(r6);	 Catch:{ Exception -> 0x005a }
        r6 = new java.lang.Class[r5];	 Catch:{ Exception -> 0x005a }
        r7 = android.content.Context.class;	 Catch:{ Exception -> 0x005a }
        r6[r4] = r7;	 Catch:{ Exception -> 0x005a }
        r7 = "insertProvider";	 Catch:{ Exception -> 0x005a }
        r3 = r3.getMethod(r7, r6);	 Catch:{ Exception -> 0x005a }
        zzacx = r3;	 Catch:{ Exception -> 0x005a }
    L_0x004c:
        r3 = zzacx;	 Catch:{ Exception -> 0x005a }
        r6 = 0;	 Catch:{ Exception -> 0x005a }
        r5 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x005a }
        r5[r4] = r8;	 Catch:{ Exception -> 0x005a }
        r3.invoke(r6, r5);	 Catch:{ Exception -> 0x005a }
        monitor-exit(r2);	 Catch:{ all -> 0x0058 }
        return;	 Catch:{ all -> 0x0058 }
    L_0x0058:
        r8 = move-exception;	 Catch:{ all -> 0x0058 }
        goto L_0x0093;	 Catch:{ all -> 0x0058 }
    L_0x005a:
        r8 = move-exception;	 Catch:{ all -> 0x0058 }
        r3 = r8.getCause();	 Catch:{ all -> 0x0058 }
        r4 = "ProviderInstaller";	 Catch:{ all -> 0x0058 }
        r1 = android.util.Log.isLoggable(r4, r1);	 Catch:{ all -> 0x0058 }
        if (r1 == 0) goto L_0x008d;	 Catch:{ all -> 0x0058 }
    L_0x0067:
        if (r3 != 0) goto L_0x006e;	 Catch:{ all -> 0x0058 }
    L_0x0069:
        r8 = r8.getMessage();	 Catch:{ all -> 0x0058 }
        goto L_0x0072;	 Catch:{ all -> 0x0058 }
    L_0x006e:
        r8 = r3.getMessage();	 Catch:{ all -> 0x0058 }
    L_0x0072:
        r1 = "ProviderInstaller";	 Catch:{ all -> 0x0058 }
        r3 = "Failed to install provider: ";	 Catch:{ all -> 0x0058 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ all -> 0x0058 }
        r4 = r8.length();	 Catch:{ all -> 0x0058 }
        if (r4 == 0) goto L_0x0085;	 Catch:{ all -> 0x0058 }
    L_0x0080:
        r8 = r3.concat(r8);	 Catch:{ all -> 0x0058 }
        goto L_0x008a;	 Catch:{ all -> 0x0058 }
    L_0x0085:
        r8 = new java.lang.String;	 Catch:{ all -> 0x0058 }
        r8.<init>(r3);	 Catch:{ all -> 0x0058 }
    L_0x008a:
        android.util.Log.e(r1, r8);	 Catch:{ all -> 0x0058 }
    L_0x008d:
        r8 = new com.google.android.gms.common.GooglePlayServicesNotAvailableException;	 Catch:{ all -> 0x0058 }
        r8.<init>(r0);	 Catch:{ all -> 0x0058 }
        throw r8;	 Catch:{ all -> 0x0058 }
    L_0x0093:
        monitor-exit(r2);	 Catch:{ all -> 0x0058 }
        throw r8;
    L_0x0095:
        r8 = "ProviderInstaller";
        r8 = android.util.Log.isLoggable(r8, r1);
        if (r8 == 0) goto L_0x00a4;
    L_0x009d:
        r8 = "ProviderInstaller";
        r1 = "Failed to get remote context - resource not found";
        android.util.Log.e(r8, r1);
    L_0x00a4:
        r8 = new com.google.android.gms.common.GooglePlayServicesNotAvailableException;
        r8.<init>(r0);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.security.ProviderInstaller.installIfNeeded(android.content.Context):void");
    }

    public static void installIfNeededAsync(Context context, ProviderInstallListener providerInstallListener) {
        Preconditions.checkNotNull(context, "Context must not be null");
        Preconditions.checkNotNull(providerInstallListener, "Listener must not be null");
        Preconditions.checkMainThread("Must be called on the UI thread");
        new zza(context, providerInstallListener).execute(new Void[0]);
    }
}
