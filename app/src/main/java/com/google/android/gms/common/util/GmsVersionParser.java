package com.google.android.gms.common.util;

import com.google.android.gms.cast.framework.media.NotificationOptions;
import java.util.regex.Pattern;

public final class GmsVersionParser {
    public static final int UNKNOWN = -1;
    private static Pattern zzzy;

    private GmsVersionParser() {
    }

    public static int parseBuildMajorVersion(int i) {
        return i == -1 ? -1 : i / DefaultOggSeeker.MATCH_BYTE_RANGE;
    }

    public static long parseBuildNumber(java.lang.String r4) {
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
        r0 = -1;
        if (r4 != 0) goto L_0x0005;
    L_0x0004:
        return r0;
    L_0x0005:
        r2 = zzdc();
        r4 = r2.matcher(r4);
        r2 = r4.find();
        if (r2 == 0) goto L_0x001d;
    L_0x0013:
        r2 = 2;
        r4 = r4.group(r2);
        r2 = java.lang.Long.parseLong(r4);	 Catch:{ NumberFormatException -> 0x001d }
        return r2;
    L_0x001d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.GmsVersionParser.parseBuildNumber(java.lang.String):long");
    }

    public static int parseBuildType(String str) {
        long parseVariantCode = parseVariantCode(str);
        return parseVariantCode == -1 ? -1 : (int) (parseVariantCode / NotificationOptions.SKIP_STEP_TEN_SECONDS_IN_MS);
    }

    public static int parseBuildVersion(int i) {
        return i == -1 ? -1 : i / 1000;
    }

    public static int parseScreenDensity(String str) {
        long parseVariantCode = parseVariantCode(str);
        return parseVariantCode == -1 ? -1 : (int) (parseVariantCode % 100);
    }

    public static int parseTargetArchitecture(String str) {
        long parseVariantCode = parseVariantCode(str);
        return parseVariantCode == -1 ? -1 : (int) ((parseVariantCode / 100) % 100);
    }

    public static long parseVariantCode(java.lang.String r4) {
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
        r0 = -1;
        if (r4 != 0) goto L_0x0005;
    L_0x0004:
        return r0;
    L_0x0005:
        r2 = zzdc();
        r4 = r2.matcher(r4);
        r2 = r4.find();
        if (r2 == 0) goto L_0x001d;
    L_0x0013:
        r2 = 1;
        r4 = r4.group(r2);
        r2 = java.lang.Long.parseLong(r4);	 Catch:{ NumberFormatException -> 0x001d }
        return r2;
    L_0x001d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.GmsVersionParser.parseVariantCode(java.lang.String):long");
    }

    private static Pattern zzdc() {
        if (zzzy == null) {
            zzzy = Pattern.compile("\\((?:eng-)?(\\d+)-(.+?)[-)$]");
        }
        return zzzy;
    }
}
