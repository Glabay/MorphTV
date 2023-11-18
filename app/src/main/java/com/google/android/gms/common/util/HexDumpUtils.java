package com.google.android.gms.common.util;

public final class HexDumpUtils {
    public static byte[] bytesFromString(java.lang.String r3) {
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
        r0 = new java.util.StringTokenizer;
        r1 = " \t\n";
        r0.<init>(r3, r1);
        r3 = new java.io.ByteArrayOutputStream;
        r3.<init>();
    L_0x000c:
        r1 = r0.hasMoreTokens();
        if (r1 == 0) goto L_0x0024;
    L_0x0012:
        r1 = r0.nextToken();	 Catch:{ NumberFormatException -> 0x0022 }
        r2 = 16;	 Catch:{ NumberFormatException -> 0x0022 }
        r1 = java.lang.Integer.parseInt(r1, r2);	 Catch:{ NumberFormatException -> 0x0022 }
        r1 = r1 & 255;	 Catch:{ NumberFormatException -> 0x0022 }
        r3.write(r1);	 Catch:{ NumberFormatException -> 0x0022 }
        goto L_0x000c;
    L_0x0022:
        r3 = 0;
        return r3;
    L_0x0024:
        r3 = r3.toByteArray();
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.HexDumpUtils.bytesFromString(java.lang.String):byte[]");
    }

    public static String dump(byte[] bArr, int i, int i2, boolean z) {
        if (bArr != null && bArr.length != 0 && i >= 0 && i2 > 0) {
            if (i + i2 <= bArr.length) {
                int i3 = 57;
                if (z) {
                    i3 = 75;
                }
                StringBuilder stringBuilder = new StringBuilder(i3 * (((i2 + 16) - 1) / 16));
                int i4 = i;
                i = i2;
                int i5 = 0;
                int i6 = 0;
                while (i > 0) {
                    if (i5 == 0) {
                        String str;
                        Object[] objArr;
                        if (i2 < 65536) {
                            str = "%04X:";
                            objArr = new Object[]{Integer.valueOf(i4)};
                        } else {
                            str = "%08X:";
                            objArr = new Object[]{Integer.valueOf(i4)};
                        }
                        stringBuilder.append(String.format(str, objArr));
                        i6 = i4;
                    } else if (i5 == 8) {
                        stringBuilder.append(" -");
                    }
                    stringBuilder.append(String.format(" %02X", new Object[]{Integer.valueOf(bArr[i4] & 255)}));
                    i--;
                    i5++;
                    if (z && (i5 == 16 || i == 0)) {
                        int i7 = 16 - i5;
                        if (i7 > 0) {
                            for (int i8 = 0; i8 < i7; i8++) {
                                stringBuilder.append("   ");
                            }
                        }
                        if (i7 >= 8) {
                            stringBuilder.append("  ");
                        }
                        stringBuilder.append("  ");
                        for (int i9 = 0; i9 < i5; i9++) {
                            char c = (char) bArr[i6 + i9];
                            if (c < ' ' || c > '~') {
                                c = '.';
                            }
                            stringBuilder.append(c);
                        }
                    }
                    if (i5 == 16 || i == 0) {
                        stringBuilder.append('\n');
                        i5 = 0;
                    }
                    i4++;
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }
}
