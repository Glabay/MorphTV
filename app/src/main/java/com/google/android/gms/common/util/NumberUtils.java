package com.google.android.gms.common.util;

@VisibleForTesting
public class NumberUtils {
    private NumberUtils() {
    }

    public static int compare(int i, int i2) {
        return i < i2 ? -1 : i == i2 ? 0 : 1;
    }

    public static int compare(long j, long j2) {
        return j < j2 ? -1 : j == j2 ? 0 : 1;
    }

    public static boolean isNumeric(java.lang.String r0) {
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
        java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x0005 }
        r0 = 1;
        return r0;
    L_0x0005:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.NumberUtils.isNumeric(java.lang.String):boolean");
    }

    public static long parseHexLong(String str) {
        if (str.length() <= 16) {
            return str.length() == 16 ? Long.parseLong(str.substring(8), 16) | (Long.parseLong(str.substring(0, 8), 16) << 32) : Long.parseLong(str, 16);
        } else {
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(str).length() + 37);
            stringBuilder.append("Invalid input: ");
            stringBuilder.append(str);
            stringBuilder.append(" exceeds 16 characters");
            throw new NumberFormatException(stringBuilder.toString());
        }
    }
}
