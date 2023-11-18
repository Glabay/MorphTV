package com.google.android.gms.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.GmsIntents;
import com.google.android.gms.common.util.DeviceProperties;

public class GoogleApiAvailabilityLight {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    private static final GoogleApiAvailabilityLight zzaw = new GoogleApiAvailabilityLight();

    GoogleApiAvailabilityLight() {
    }

    public static GoogleApiAvailabilityLight getInstance() {
        return zzaw;
    }

    @com.google.android.gms.common.util.VisibleForTesting
    private static java.lang.String zza(@android.support.annotation.Nullable android.content.Context r2, @android.support.annotation.Nullable java.lang.String r3) {
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
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "gcore_";
        r0.append(r1);
        r1 = GOOGLE_PLAY_SERVICES_VERSION_CODE;
        r0.append(r1);
        r1 = "-";
        r0.append(r1);
        r1 = android.text.TextUtils.isEmpty(r3);
        if (r1 != 0) goto L_0x001d;
    L_0x001a:
        r0.append(r3);
    L_0x001d:
        r3 = "-";
        r0.append(r3);
        if (r2 == 0) goto L_0x002b;
    L_0x0024:
        r3 = r2.getPackageName();
        r0.append(r3);
    L_0x002b:
        r3 = "-";
        r0.append(r3);
        if (r2 == 0) goto L_0x0044;
    L_0x0032:
        r3 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r2);	 Catch:{ NameNotFoundException -> 0x0044 }
        r2 = r2.getPackageName();	 Catch:{ NameNotFoundException -> 0x0044 }
        r1 = 0;	 Catch:{ NameNotFoundException -> 0x0044 }
        r2 = r3.getPackageInfo(r2, r1);	 Catch:{ NameNotFoundException -> 0x0044 }
        r2 = r2.versionCode;	 Catch:{ NameNotFoundException -> 0x0044 }
        r0.append(r2);	 Catch:{ NameNotFoundException -> 0x0044 }
    L_0x0044:
        r2 = r0.toString();
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GoogleApiAvailabilityLight.zza(android.content.Context, java.lang.String):java.lang.String");
    }

    public void cancelAvailabilityErrorNotifications(Context context) {
        GooglePlayServicesUtilLight.cancelAvailabilityErrorNotifications(context);
    }

    public int getApkVersion(Context context) {
        return GooglePlayServicesUtilLight.getApkVersion(context);
    }

    public int getClientVersion(Context context) {
        return GooglePlayServicesUtilLight.getClientVersion(context);
    }

    @Nullable
    @Deprecated
    public Intent getErrorResolutionIntent(int i) {
        return getErrorResolutionIntent(null, i, null);
    }

    @Nullable
    public Intent getErrorResolutionIntent(Context context, int i, @Nullable String str) {
        switch (i) {
            case 1:
            case 2:
                return (context == null || !DeviceProperties.isWearableWithoutPlayStore(context)) ? GmsIntents.createPlayStoreIntent("com.google.android.gms", zza(context, str)) : GmsIntents.createAndroidWearUpdateIntent();
            case 3:
                return GmsIntents.createSettingsIntent("com.google.android.gms");
            default:
                return null;
        }
    }

    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2) {
        return getErrorResolutionPendingIntent(context, i, i2, null);
    }

    @Nullable
    public PendingIntent getErrorResolutionPendingIntent(Context context, int i, int i2, @Nullable String str) {
        Intent errorResolutionIntent = getErrorResolutionIntent(context, i, str);
        return errorResolutionIntent == null ? null : PendingIntent.getActivity(context, i2, errorResolutionIntent, 134217728);
    }

    public String getErrorString(int i) {
        return GooglePlayServicesUtilLight.getErrorString(i);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        return isGooglePlayServicesAvailable(context, GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    public int isGooglePlayServicesAvailable(Context context, int i) {
        i = GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(context, i);
        return GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, i) ? 18 : i;
    }

    public boolean isPlayServicesPossiblyUpdating(Context context, int i) {
        return GooglePlayServicesUtilLight.isPlayServicesPossiblyUpdating(context, i);
    }

    public boolean isPlayStorePossiblyUpdating(Context context, int i) {
        return GooglePlayServicesUtilLight.isPlayStorePossiblyUpdating(context, i);
    }

    public boolean isUninstalledAppPossiblyUpdating(Context context, String str) {
        return GooglePlayServicesUtilLight.isUninstalledAppPossiblyUpdating(context, str);
    }

    public boolean isUserResolvableError(int i) {
        return GooglePlayServicesUtilLight.isUserRecoverableError(i);
    }

    public void verifyGooglePlayServicesIsAvailable(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtilLight.ensurePlayServicesAvailable(context);
    }

    public void verifyGooglePlayServicesIsAvailable(Context context, int i) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        GooglePlayServicesUtilLight.ensurePlayServicesAvailable(context, i);
    }
}
