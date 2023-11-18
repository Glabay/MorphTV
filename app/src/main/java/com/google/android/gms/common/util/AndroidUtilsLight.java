package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.android.gms.common.wrappers.Wrappers;
import java.security.MessageDigest;

public class AndroidUtilsLight {
    public static final String DIGEST_ALGORITHM_SHA1 = "SHA1";
    public static final String DIGEST_ALGORITHM_SHA512 = "SHA-512";

    public static java.security.MessageDigest getMessageDigest(java.lang.String r2) {
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
    L_0x0001:
        r1 = 2;
        if (r0 >= r1) goto L_0x000e;
    L_0x0004:
        r1 = java.security.MessageDigest.getInstance(r2);	 Catch:{ NoSuchAlgorithmException -> 0x000b }
        if (r1 == 0) goto L_0x000b;
    L_0x000a:
        return r1;
    L_0x000b:
        r0 = r0 + 1;
        goto L_0x0001;
    L_0x000e:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.AndroidUtilsLight.getMessageDigest(java.lang.String):java.security.MessageDigest");
    }

    public static byte[] getPackageCertificateHashBytes(Context context, String str) throws NameNotFoundException {
        return getPackageCertificateHashBytes(context, str, DIGEST_ALGORITHM_SHA1);
    }

    public static byte[] getPackageCertificateHashBytes(Context context, String str, String str2) throws NameNotFoundException {
        PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 64);
        if (packageInfo.signatures != null && packageInfo.signatures.length == 1) {
            MessageDigest messageDigest = getMessageDigest(str2);
            if (messageDigest != null) {
                return messageDigest.digest(packageInfo.signatures[0].toByteArray());
            }
        }
        return null;
    }
}
