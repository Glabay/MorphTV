package com.google.android.gms.common.wrappers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Process;
import android.support.v4.content.PermissionChecker;
import android.support.v4.util.Pair;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;

public class PackageManagerWrapper {
    private final Context zzjp;

    public PackageManagerWrapper(Context context) {
        this.zzjp = context;
    }

    public boolean allowApiAccess(String str, int i) {
        return true;
    }

    public void checkCallerIsNotInstantApp() {
        if (isCallerInstantApp()) {
            throw new SecurityException("This operation is not supported for instant apps.");
        }
    }

    public int checkCallingOrSelfPermission(String str) {
        return this.zzjp.checkCallingOrSelfPermission(str);
    }

    public int checkCallingOrSelfPermissionAndAppOps(String str) {
        return PermissionChecker.checkCallingOrSelfPermission(this.zzjp, str);
    }

    public int checkCallingPermission(String str) {
        return this.zzjp.checkCallingPermission(str);
    }

    @Deprecated
    public int checkCallingPermission(String str, String str2) {
        return checkPermission(str, str2);
    }

    public int checkCallingPermissionAndAppOps(String str, String str2) {
        return PermissionChecker.checkCallingPermission(this.zzjp, str, str2);
    }

    public void checkPackage(int i, String str) {
        if (!uidHasPackageName(i, str)) {
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 39);
            stringBuilder.append("Package ");
            stringBuilder.append(str);
            stringBuilder.append(" does not belong to ");
            stringBuilder.append(i);
            throw new SecurityException(stringBuilder.toString());
        }
    }

    public int checkPermission(String str, int i, int i2) {
        return this.zzjp.checkPermission(str, i, i2);
    }

    @Deprecated
    public int checkPermission(String str, int i, int i2, String str2) {
        return checkPermission(str, i, i2);
    }

    public int checkPermission(String str, String str2) {
        return this.zzjp.getPackageManager().checkPermission(str, str2);
    }

    public int checkPermissionAndAppOps(String str, int i, int i2, String str2) {
        return PermissionChecker.checkPermission(this.zzjp, str, i, i2, str2);
    }

    public ApplicationInfo getApplicationInfo(String str, int i) throws NameNotFoundException {
        return this.zzjp.getPackageManager().getApplicationInfo(str, i);
    }

    public CharSequence getApplicationLabel(String str) throws NameNotFoundException {
        return this.zzjp.getPackageManager().getApplicationLabel(this.zzjp.getPackageManager().getApplicationInfo(str, 0));
    }

    public Pair<CharSequence, Drawable> getApplicationLabelAndIcon(String str) throws NameNotFoundException {
        ApplicationInfo applicationInfo = this.zzjp.getPackageManager().getApplicationInfo(str, 0);
        return Pair.create(this.zzjp.getPackageManager().getApplicationLabel(applicationInfo), this.zzjp.getPackageManager().getApplicationIcon(applicationInfo));
    }

    public ComponentName getCallingActivity(Activity activity) {
        return activity.getCallingActivity();
    }

    public String getCallingPackage(Activity activity) {
        ComponentName callingActivity = activity.getCallingActivity();
        if (callingActivity == null) {
            return null;
        }
        String packageName = callingActivity.getPackageName();
        if (packageName != null) {
            return packageName;
        }
        String valueOf = String.valueOf(callingActivity);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 54);
        stringBuilder.append("getCallingPackage(): Couldn't get a package name from ");
        stringBuilder.append(valueOf);
        Log.e("PackageManagerWrapper", stringBuilder.toString());
        return null;
    }

    protected Context getContext() {
        return this.zzjp;
    }

    public PackageInfo getPackageInfo(String str, int i) throws NameNotFoundException {
        return this.zzjp.getPackageManager().getPackageInfo(str, i);
    }

    public String[] getPackagesForUid(int i) {
        return this.zzjp.getPackageManager().getPackagesForUid(i);
    }

    public boolean isCallerInstantApp() {
        if (Binder.getCallingUid() == Process.myUid()) {
            return InstantApps.isInstantApp(this.zzjp);
        }
        if (PlatformVersion.isAtLeastO()) {
            String nameForUid = this.zzjp.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (nameForUid != null) {
                return this.zzjp.getPackageManager().isInstantApp(nameForUid);
            }
        }
        return false;
    }

    public boolean isInstantAppUid(int i) {
        return false;
    }

    @android.annotation.TargetApi(19)
    public boolean uidHasPackageName(int r5, java.lang.String r6) {
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
        r4 = this;
        r0 = com.google.android.gms.common.util.PlatformVersion.isAtLeastKitKat();
        r1 = 0;
        r2 = 1;
        if (r0 == 0) goto L_0x0017;
    L_0x0008:
        r0 = r4.zzjp;	 Catch:{ SecurityException -> 0x0016 }
        r3 = "appops";	 Catch:{ SecurityException -> 0x0016 }
        r0 = r0.getSystemService(r3);	 Catch:{ SecurityException -> 0x0016 }
        r0 = (android.app.AppOpsManager) r0;	 Catch:{ SecurityException -> 0x0016 }
        r0.checkPackage(r5, r6);	 Catch:{ SecurityException -> 0x0016 }
        return r2;
    L_0x0016:
        return r1;
    L_0x0017:
        r0 = r4.zzjp;
        r0 = r0.getPackageManager();
        r5 = r0.getPackagesForUid(r5);
        if (r6 == 0) goto L_0x0035;
    L_0x0023:
        if (r5 == 0) goto L_0x0035;
    L_0x0025:
        r0 = 0;
    L_0x0026:
        r3 = r5.length;
        if (r0 >= r3) goto L_0x0035;
    L_0x0029:
        r3 = r5[r0];
        r3 = r6.equals(r3);
        if (r3 == 0) goto L_0x0032;
    L_0x0031:
        return r2;
    L_0x0032:
        r0 = r0 + 1;
        goto L_0x0026;
    L_0x0035:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.wrappers.PackageManagerWrapper.uidHasPackageName(int, java.lang.String):boolean");
    }
}
