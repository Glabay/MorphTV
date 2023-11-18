package com.google.android.gms.internal.stable;

import android.content.ContentResolver;
import android.net.Uri;
import com.google.android.gms.internal.stable.zze.zza;

public final class zzg extends zza {
    private static final Uri CONTENT_URI = Uri.parse("content://com.google.settings/partner");

    public static int getInt(android.content.ContentResolver r0, java.lang.String r1, int r2) {
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
        r0 = getString(r0, r1);
        r1 = -1;
        if (r0 == 0) goto L_0x000c;
    L_0x0007:
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x000c }
        r1 = r0;
    L_0x000c:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zzg.getInt(android.content.ContentResolver, java.lang.String, int):int");
    }

    private static String getString(ContentResolver contentResolver, String str) {
        return zza.zza(contentResolver, CONTENT_URI, str);
    }

    public static String zza(ContentResolver contentResolver, String str, String str2) {
        String string = getString(contentResolver, str);
        return string == null ? str2 : string;
    }
}
