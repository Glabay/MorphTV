package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import com.google.android.gms.common.GooglePlayServicesUtilLight;

public class ClientLibraryUtils {
    public static final int GMS_CLIENT_VERSION_UNKNOWN = -1;

    private ClientLibraryUtils() {
    }

    public static int getClientVersion(Context context, String str) {
        return getClientVersion(getPackageInfo(context, str));
    }

    public static int getClientVersion(PackageInfo packageInfo) {
        if (packageInfo == null || packageInfo.applicationInfo == null) {
            return -1;
        }
        Bundle bundle = packageInfo.applicationInfo.metaData;
        return bundle == null ? -1 : bundle.getInt(GooglePlayServicesUtilLight.KEY_METADATA_GOOGLE_PLAY_SERVICES_VERSION, -1);
    }

    @android.support.annotation.Nullable
    public static android.content.pm.PackageInfo getPackageInfo(android.content.Context r1, java.lang.String r2) {
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
        r1 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r1);	 Catch:{ NameNotFoundException -> 0x000b }
        r0 = 128; // 0x80 float:1.794E-43 double:6.32E-322;	 Catch:{ NameNotFoundException -> 0x000b }
        r1 = r1.getPackageInfo(r2, r0);	 Catch:{ NameNotFoundException -> 0x000b }
        return r1;
    L_0x000b:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.ClientLibraryUtils.getPackageInfo(android.content.Context, java.lang.String):android.content.pm.PackageInfo");
    }

    public static boolean isPackageSide() {
        return false;
    }

    public static boolean isPackageStopped(android.content.Context r1, java.lang.String r2) {
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
        r0 = "com.google.android.gms";
        r0.equals(r2);
        r0 = 0;
        r1 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r1);	 Catch:{ NameNotFoundException -> 0x0017 }
        r1 = r1.getApplicationInfo(r2, r0);	 Catch:{ NameNotFoundException -> 0x0017 }
        r1 = r1.flags;	 Catch:{ NameNotFoundException -> 0x0017 }
        r2 = 2097152; // 0x200000 float:2.938736E-39 double:1.0361308E-317;
        r1 = r1 & r2;
        if (r1 == 0) goto L_0x0017;
    L_0x0015:
        r1 = 1;
        return r1;
    L_0x0017:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.ClientLibraryUtils.isPackageStopped(android.content.Context, java.lang.String):boolean");
    }
}
