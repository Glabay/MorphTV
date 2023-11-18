package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.ClientLibraryUtils;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import java.util.concurrent.atomic.AtomicBoolean;

public class GooglePlayServicesUtilLight {
    public static final String FEATURE_SIDEWINDER = "cn.google";
    public static final String GOOGLE_PLAY_GAMES_PACKAGE = "com.google.android.play.games";
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = 12451000;
    public static final String GOOGLE_PLAY_STORE_GAMES_URI_STRING = "http://play.google.com/store/apps/category/GAME";
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    public static final String GOOGLE_PLAY_STORE_URI_STRING = "http://play.google.com/store/apps/";
    public static final boolean HONOR_DEBUG_CERTIFICATES = false;
    public static final String KEY_METADATA_GOOGLE_PLAY_SERVICES_VERSION = "com.google.android.gms.version";
    public static final int MAX_ATTEMPTS_NO_SUCH_ALGORITHM = 2;
    @VisibleForTesting
    public static boolean sIsTestMode;
    @VisibleForTesting
    public static boolean sTestIsUserBuild;
    private static boolean zzbr;
    @VisibleForTesting
    private static boolean zzbs;
    @VisibleForTesting
    static final AtomicBoolean zzbt = new AtomicBoolean();
    private static final AtomicBoolean zzbu = new AtomicBoolean();

    GooglePlayServicesUtilLight() {
    }

    @java.lang.Deprecated
    public static void cancelAvailabilityErrorNotifications(android.content.Context r2) {
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
        r0 = zzbt;
        r1 = 1;
        r0 = r0.getAndSet(r1);
        if (r0 == 0) goto L_0x000a;
    L_0x0009:
        return;
    L_0x000a:
        r0 = "notification";	 Catch:{ SecurityException -> 0x0019 }
        r2 = r2.getSystemService(r0);	 Catch:{ SecurityException -> 0x0019 }
        r2 = (android.app.NotificationManager) r2;	 Catch:{ SecurityException -> 0x0019 }
        if (r2 == 0) goto L_0x0019;	 Catch:{ SecurityException -> 0x0019 }
    L_0x0014:
        r0 = 10436; // 0x28c4 float:1.4624E-41 double:5.156E-320;	 Catch:{ SecurityException -> 0x0019 }
        r2.cancel(r0);	 Catch:{ SecurityException -> 0x0019 }
    L_0x0019:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.cancelAvailabilityErrorNotifications(android.content.Context):void");
    }

    public static void enableUsingApkIndependentContext() {
        zzbu.set(true);
    }

    @Deprecated
    public static void ensurePlayServicesAvailable(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        ensurePlayServicesAvailable(context, GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    @Deprecated
    public static void ensurePlayServicesAvailable(Context context, int i) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        i = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context, i);
        if (i != 0) {
            Intent errorResolutionIntent = GoogleApiAvailabilityLight.getInstance().getErrorResolutionIntent(context, i, "e");
            StringBuilder stringBuilder = new StringBuilder(57);
            stringBuilder.append("GooglePlayServices not available due to error ");
            stringBuilder.append(i);
            Log.e("GooglePlayServicesUtil", stringBuilder.toString());
            if (errorResolutionIntent == null) {
                throw new GooglePlayServicesNotAvailableException(i);
            }
            throw new GooglePlayServicesRepairableException(i, "Google Play Services not available", errorResolutionIntent);
        }
    }

    @java.lang.Deprecated
    public static int getApkVersion(android.content.Context r2) {
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
        r2 = r2.getPackageManager();	 Catch:{ NameNotFoundException -> 0x000e }
        r1 = "com.google.android.gms";	 Catch:{ NameNotFoundException -> 0x000e }
        r2 = r2.getPackageInfo(r1, r0);	 Catch:{ NameNotFoundException -> 0x000e }
        r2 = r2.versionCode;
        return r2;
    L_0x000e:
        r2 = "GooglePlayServicesUtil";
        r1 = "Google Play services is missing.";
        android.util.Log.w(r2, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.getApkVersion(android.content.Context):int");
    }

    @Deprecated
    public static int getClientVersion(Context context) {
        Preconditions.checkState(true);
        return ClientLibraryUtils.getClientVersion(context, context.getPackageName());
    }

    @Deprecated
    public static PendingIntent getErrorPendingIntent(int i, Context context, int i2) {
        return GoogleApiAvailabilityLight.getInstance().getErrorResolutionPendingIntent(context, i, i2);
    }

    @Deprecated
    @VisibleForTesting
    public static String getErrorString(int i) {
        return ConnectionResult.zza(i);
    }

    @Deprecated
    public static Intent getGooglePlayServicesAvailabilityRecoveryIntent(int i) {
        return GoogleApiAvailabilityLight.getInstance().getErrorResolutionIntent(null, i, null);
    }

    @java.lang.Deprecated
    public static java.lang.String getOpenSourceSoftwareLicenseInfo(android.content.Context r3) {
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
        r0 = new android.net.Uri$Builder;
        r0.<init>();
        r1 = "android.resource";
        r0 = r0.scheme(r1);
        r1 = "com.google.android.gms";
        r0 = r0.authority(r1);
        r1 = "raw";
        r0 = r0.appendPath(r1);
        r1 = "oss_notice";
        r0 = r0.appendPath(r1);
        r0 = r0.build();
        r1 = 0;
        r3 = r3.getContentResolver();	 Catch:{ Exception -> 0x004b }
        r3 = r3.openInputStream(r0);	 Catch:{ Exception -> 0x004b }
        r0 = new java.util.Scanner;	 Catch:{ NoSuchElementException -> 0x0046, all -> 0x003f }
        r0.<init>(r3);	 Catch:{ NoSuchElementException -> 0x0046, all -> 0x003f }
        r2 = "\\A";	 Catch:{ NoSuchElementException -> 0x0046, all -> 0x003f }
        r0 = r0.useDelimiter(r2);	 Catch:{ NoSuchElementException -> 0x0046, all -> 0x003f }
        r0 = r0.next();	 Catch:{ NoSuchElementException -> 0x0046, all -> 0x003f }
        if (r3 == 0) goto L_0x003e;
    L_0x003b:
        r3.close();	 Catch:{ Exception -> 0x004b }
    L_0x003e:
        return r0;	 Catch:{ Exception -> 0x004b }
    L_0x003f:
        r0 = move-exception;	 Catch:{ Exception -> 0x004b }
        if (r3 == 0) goto L_0x0045;	 Catch:{ Exception -> 0x004b }
    L_0x0042:
        r3.close();	 Catch:{ Exception -> 0x004b }
    L_0x0045:
        throw r0;	 Catch:{ Exception -> 0x004b }
    L_0x0046:
        if (r3 == 0) goto L_0x004b;	 Catch:{ Exception -> 0x004b }
    L_0x0048:
        r3.close();	 Catch:{ Exception -> 0x004b }
    L_0x004b:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.getOpenSourceSoftwareLicenseInfo(android.content.Context):java.lang.String");
    }

    public static android.content.Context getRemoteContext(android.content.Context r2) {
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
        r0 = "com.google.android.gms";	 Catch:{ NameNotFoundException -> 0x0008 }
        r1 = 3;	 Catch:{ NameNotFoundException -> 0x0008 }
        r2 = r2.createPackageContext(r0, r1);	 Catch:{ NameNotFoundException -> 0x0008 }
        return r2;
    L_0x0008:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.getRemoteContext(android.content.Context):android.content.Context");
    }

    public static android.content.res.Resources getRemoteResource(android.content.Context r1) {
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
        r1 = r1.getPackageManager();	 Catch:{ NameNotFoundException -> 0x000b }
        r0 = "com.google.android.gms";	 Catch:{ NameNotFoundException -> 0x000b }
        r1 = r1.getResourcesForApplication(r0);	 Catch:{ NameNotFoundException -> 0x000b }
        return r1;
    L_0x000b:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.getRemoteResource(android.content.Context):android.content.res.Resources");
    }

    public static boolean honorsDebugCertificates(Context context) {
        if (!isTestKeysBuild(context)) {
            if (isUserBuildDevice()) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    public static int isGooglePlayServicesAvailable(Context context) {
        return isGooglePlayServicesAvailable(context, GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    @java.lang.Deprecated
    public static int isGooglePlayServicesAvailable(android.content.Context r3, int r4) {
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
        r0 = r3.getResources();	 Catch:{ Throwable -> 0x000a }
        r1 = com.google.android.gms.common.C0783R.string.common_google_play_services_unknown_issue;	 Catch:{ Throwable -> 0x000a }
        r0.getString(r1);	 Catch:{ Throwable -> 0x000a }
        goto L_0x0011;
    L_0x000a:
        r0 = "GooglePlayServicesUtil";
        r1 = "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included.";
        android.util.Log.e(r0, r1);
    L_0x0011:
        r0 = "com.google.android.gms";
        r1 = r3.getPackageName();
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x005f;
    L_0x001d:
        r0 = zzbu;
        r0 = r0.get();
        if (r0 != 0) goto L_0x005f;
    L_0x0025:
        r0 = com.google.android.gms.common.internal.MetadataValueReader.getGooglePlayServicesVersion(r3);
        if (r0 != 0) goto L_0x0033;
    L_0x002b:
        r3 = new java.lang.IllegalStateException;
        r4 = "A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />";
        r3.<init>(r4);
        throw r3;
    L_0x0033:
        r1 = GOOGLE_PLAY_SERVICES_VERSION_CODE;
        if (r0 == r1) goto L_0x005f;
    L_0x0037:
        r3 = new java.lang.IllegalStateException;
        r4 = GOOGLE_PLAY_SERVICES_VERSION_CODE;
        r1 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
        r2 = new java.lang.StringBuilder;
        r2.<init>(r1);
        r1 = "The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected ";
        r2.append(r1);
        r2.append(r4);
        r4 = " but found ";
        r2.append(r4);
        r2.append(r0);
        r4 = ".  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />";
        r2.append(r4);
        r4 = r2.toString();
        r3.<init>(r4);
        throw r3;
    L_0x005f:
        r0 = com.google.android.gms.common.util.DeviceProperties.isWearableWithoutPlayStore(r3);
        if (r0 != 0) goto L_0x006d;
    L_0x0065:
        r0 = com.google.android.gms.common.util.DeviceProperties.isIoT(r3);
        if (r0 != 0) goto L_0x006d;
    L_0x006b:
        r0 = 1;
        goto L_0x006e;
    L_0x006d:
        r0 = 0;
    L_0x006e:
        r3 = zza(r3, r0, r4);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.isGooglePlayServicesAvailable(android.content.Context, int):int");
    }

    @Deprecated
    public static boolean isGooglePlayServicesUid(Context context, int i) {
        return UidVerifier.isGooglePlayServicesUid(context, i);
    }

    @Deprecated
    public static boolean isPlayServicesPossiblyUpdating(Context context, int i) {
        return i == 18 ? true : i == 1 ? isUninstalledAppPossiblyUpdating(context, "com.google.android.gms") : false;
    }

    @Deprecated
    public static boolean isPlayStorePossiblyUpdating(Context context, int i) {
        return i == 9 ? isUninstalledAppPossiblyUpdating(context, "com.android.vending") : false;
    }

    @TargetApi(18)
    public static boolean isRestrictedUserProfile(Context context) {
        if (PlatformVersion.isAtLeastJellyBeanMR2()) {
            Bundle applicationRestrictions = ((UserManager) context.getSystemService("user")).getApplicationRestrictions(context.getPackageName());
            if (applicationRestrictions != null && "true".equals(applicationRestrictions.getString("restricted_profile"))) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @VisibleForTesting
    public static boolean isSidewinderDevice(Context context) {
        return DeviceProperties.isSidewinder(context);
    }

    public static boolean isTestKeysBuild(Context context) {
        if (!zzbs) {
            try {
                PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo("com.google.android.gms", 64);
                GoogleSignatureVerifier instance = GoogleSignatureVerifier.getInstance(context);
                if (packageInfo == null || instance.isGooglePublicSignedPackage(packageInfo, false) || !instance.isGooglePublicSignedPackage(packageInfo, true)) {
                    zzbr = false;
                    zzbs = true;
                } else {
                    zzbr = true;
                    zzbs = true;
                }
            } catch (Throwable e) {
                Log.w("GooglePlayServicesUtil", "Cannot find Google Play services package name.", e);
            } catch (Throwable th) {
                zzbs = true;
            }
        }
        return zzbr;
    }

    @android.annotation.TargetApi(21)
    static boolean isUninstalledAppPossiblyUpdating(android.content.Context r5, java.lang.String r6) {
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
        r0 = r6.equals(r0);
        r1 = com.google.android.gms.common.util.PlatformVersion.isAtLeastLollipop();
        r2 = 1;
        r3 = 0;
        if (r1 == 0) goto L_0x0036;
    L_0x000e:
        r1 = r5.getPackageManager();	 Catch:{ Exception -> 0x0035 }
        r1 = r1.getPackageInstaller();	 Catch:{ Exception -> 0x0035 }
        r1 = r1.getAllSessions();	 Catch:{ Exception -> 0x0035 }
        r1 = r1.iterator();
    L_0x001e:
        r4 = r1.hasNext();
        if (r4 == 0) goto L_0x0036;
    L_0x0024:
        r4 = r1.next();
        r4 = (android.content.pm.PackageInstaller.SessionInfo) r4;
        r4 = r4.getAppPackageName();
        r4 = r6.equals(r4);
        if (r4 == 0) goto L_0x001e;
    L_0x0034:
        return r2;
    L_0x0035:
        return r3;
    L_0x0036:
        r1 = r5.getPackageManager();
        r4 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r6 = r1.getApplicationInfo(r6, r4);	 Catch:{ NameNotFoundException -> 0x0050 }
        if (r0 == 0) goto L_0x0045;	 Catch:{ NameNotFoundException -> 0x0050 }
    L_0x0042:
        r5 = r6.enabled;	 Catch:{ NameNotFoundException -> 0x0050 }
        return r5;	 Catch:{ NameNotFoundException -> 0x0050 }
    L_0x0045:
        r6 = r6.enabled;	 Catch:{ NameNotFoundException -> 0x0050 }
        if (r6 == 0) goto L_0x0050;	 Catch:{ NameNotFoundException -> 0x0050 }
    L_0x0049:
        r5 = isRestrictedUserProfile(r5);	 Catch:{ NameNotFoundException -> 0x0050 }
        if (r5 != 0) goto L_0x0050;
    L_0x004f:
        return r2;
    L_0x0050:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.isUninstalledAppPossiblyUpdating(android.content.Context, java.lang.String):boolean");
    }

    @Deprecated
    public static boolean isUserBuildDevice() {
        return DeviceProperties.isUserBuild();
    }

    @Deprecated
    public static boolean isUserRecoverableError(int i) {
        if (i != 9) {
            switch (i) {
                case 1:
                case 2:
                case 3:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @TargetApi(19)
    @Deprecated
    public static boolean uidHasPackageName(Context context, int i, String str) {
        return UidVerifier.uidHasPackageName(context, i, str);
    }

    @com.google.android.gms.common.util.VisibleForTesting
    private static int zza(android.content.Context r7, boolean r8, int r9) {
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
        r1 = 1;
        if (r9 < 0) goto L_0x0006;
    L_0x0004:
        r2 = 1;
        goto L_0x0007;
    L_0x0006:
        r2 = 0;
    L_0x0007:
        com.google.android.gms.common.internal.Preconditions.checkArgument(r2);
        r2 = r7.getPackageManager();
        r3 = 0;
        r4 = 9;
        if (r8 == 0) goto L_0x0024;
    L_0x0013:
        r3 = "com.android.vending";	 Catch:{ NameNotFoundException -> 0x001c }
        r5 = 8256; // 0x2040 float:1.1569E-41 double:4.079E-320;	 Catch:{ NameNotFoundException -> 0x001c }
        r3 = r2.getPackageInfo(r3, r5);	 Catch:{ NameNotFoundException -> 0x001c }
        goto L_0x0024;
    L_0x001c:
        r7 = "GooglePlayServicesUtil";
        r8 = "Google Play Store is missing.";
    L_0x0020:
        android.util.Log.w(r7, r8);
        return r4;
    L_0x0024:
        r5 = "com.google.android.gms";	 Catch:{ NameNotFoundException -> 0x00a1 }
        r6 = 64;	 Catch:{ NameNotFoundException -> 0x00a1 }
        r5 = r2.getPackageInfo(r5, r6);	 Catch:{ NameNotFoundException -> 0x00a1 }
        r7 = com.google.android.gms.common.GoogleSignatureVerifier.getInstance(r7);
        r6 = r7.isGooglePublicSignedPackage(r5, r1);
        if (r6 != 0) goto L_0x003b;
    L_0x0036:
        r7 = "GooglePlayServicesUtil";
        r8 = "Google Play services signature invalid.";
        goto L_0x0020;
    L_0x003b:
        if (r8 == 0) goto L_0x0056;
    L_0x003d:
        r7 = r7.isGooglePublicSignedPackage(r3, r1);
        if (r7 == 0) goto L_0x0051;
    L_0x0043:
        r7 = r3.signatures;
        r7 = r7[r0];
        r8 = r5.signatures;
        r8 = r8[r0];
        r7 = r7.equals(r8);
        if (r7 != 0) goto L_0x0056;
    L_0x0051:
        r7 = "GooglePlayServicesUtil";
        r8 = "Google Play Store signature invalid.";
        goto L_0x0020;
    L_0x0056:
        r7 = r5.versionCode;
        r7 = com.google.android.gms.common.util.GmsVersionParser.parseBuildVersion(r7);
        r8 = com.google.android.gms.common.util.GmsVersionParser.parseBuildVersion(r9);
        if (r7 >= r8) goto L_0x0086;
    L_0x0062:
        r7 = "GooglePlayServicesUtil";
        r8 = r5.versionCode;
        r0 = 77;
        r1 = new java.lang.StringBuilder;
        r1.<init>(r0);
        r0 = "Google Play services out of date.  Requires ";
        r1.append(r0);
        r1.append(r9);
        r9 = " but found ";
        r1.append(r9);
        r1.append(r8);
        r8 = r1.toString();
        android.util.Log.w(r7, r8);
        r7 = 2;
        return r7;
    L_0x0086:
        r7 = r5.applicationInfo;
        if (r7 != 0) goto L_0x009a;
    L_0x008a:
        r7 = "com.google.android.gms";	 Catch:{ NameNotFoundException -> 0x0091 }
        r7 = r2.getApplicationInfo(r7, r0);	 Catch:{ NameNotFoundException -> 0x0091 }
        goto L_0x009a;
    L_0x0091:
        r7 = move-exception;
        r8 = "GooglePlayServicesUtil";
        r9 = "Google Play services missing when getting application info.";
        android.util.Log.wtf(r8, r9, r7);
        return r1;
    L_0x009a:
        r7 = r7.enabled;
        if (r7 != 0) goto L_0x00a0;
    L_0x009e:
        r7 = 3;
        return r7;
    L_0x00a0:
        return r0;
    L_0x00a1:
        r7 = "GooglePlayServicesUtil";
        r8 = "Google Play services is missing.";
        android.util.Log.w(r7, r8);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtilLight.zza(android.content.Context, boolean, int):int");
    }
}
