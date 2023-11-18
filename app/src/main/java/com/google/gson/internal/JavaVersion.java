package com.google.gson.internal;

public final class JavaVersion {
    private static final int majorJavaVersion = determineMajorJavaVersion();

    private static int determineMajorJavaVersion() {
        return getMajorJavaVersion(System.getProperty("java.version"));
    }

    static int getMajorJavaVersion(String str) {
        int parseDotted = parseDotted(str);
        if (parseDotted == -1) {
            parseDotted = extractBeginningInt(str);
        }
        return parseDotted == -1 ? 6 : parseDotted;
    }

    private static int parseDotted(java.lang.String r3) {
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
        r0 = "[._]";	 Catch:{ NumberFormatException -> 0x001b }
        r3 = r3.split(r0);	 Catch:{ NumberFormatException -> 0x001b }
        r0 = 0;	 Catch:{ NumberFormatException -> 0x001b }
        r0 = r3[r0];	 Catch:{ NumberFormatException -> 0x001b }
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x001b }
        r1 = 1;	 Catch:{ NumberFormatException -> 0x001b }
        if (r0 != r1) goto L_0x001a;	 Catch:{ NumberFormatException -> 0x001b }
    L_0x0010:
        r2 = r3.length;	 Catch:{ NumberFormatException -> 0x001b }
        if (r2 <= r1) goto L_0x001a;	 Catch:{ NumberFormatException -> 0x001b }
    L_0x0013:
        r3 = r3[r1];	 Catch:{ NumberFormatException -> 0x001b }
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x001b }
        return r3;
    L_0x001a:
        return r0;
    L_0x001b:
        r3 = -1;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.JavaVersion.parseDotted(java.lang.String):int");
    }

    private static int extractBeginningInt(java.lang.String r4) {
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
        r0 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x0025 }
        r0.<init>();	 Catch:{ NumberFormatException -> 0x0025 }
        r1 = 0;	 Catch:{ NumberFormatException -> 0x0025 }
    L_0x0006:
        r2 = r4.length();	 Catch:{ NumberFormatException -> 0x0025 }
        if (r1 >= r2) goto L_0x001c;	 Catch:{ NumberFormatException -> 0x0025 }
    L_0x000c:
        r2 = r4.charAt(r1);	 Catch:{ NumberFormatException -> 0x0025 }
        r3 = java.lang.Character.isDigit(r2);	 Catch:{ NumberFormatException -> 0x0025 }
        if (r3 == 0) goto L_0x001c;	 Catch:{ NumberFormatException -> 0x0025 }
    L_0x0016:
        r0.append(r2);	 Catch:{ NumberFormatException -> 0x0025 }
        r1 = r1 + 1;	 Catch:{ NumberFormatException -> 0x0025 }
        goto L_0x0006;	 Catch:{ NumberFormatException -> 0x0025 }
    L_0x001c:
        r4 = r0.toString();	 Catch:{ NumberFormatException -> 0x0025 }
        r4 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x0025 }
        return r4;
    L_0x0025:
        r4 = -1;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.JavaVersion.extractBeginningInt(java.lang.String):int");
    }

    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }

    public static boolean isJava9OrLater() {
        return majorJavaVersion >= 9;
    }

    private JavaVersion() {
    }
}
