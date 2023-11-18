package ir.mahdi.mzip.rar.rarfile;

public class FileNameDecoder {
    public static int getChar(byte[] bArr, int i) {
        return bArr[i] & 255;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String decode(byte[] r9, int r10) {
        /*
        r0 = r10 + 1;
        r10 = getChar(r9, r10);
        r1 = new java.lang.StringBuffer;
        r1.<init>();
        r2 = 0;
        r3 = 0;
        r4 = 0;
    L_0x000e:
        r5 = r9.length;
        if (r0 >= r5) goto L_0x00a2;
    L_0x0011:
        r5 = 8;
        if (r2 != 0) goto L_0x001e;
    L_0x0015:
        r2 = r0 + 1;
        r3 = getChar(r9, r0);
        r0 = r2;
        r2 = 8;
    L_0x001e:
        r6 = r3 >> 6;
        switch(r6) {
            case 0: goto L_0x008d;
            case 1: goto L_0x007d;
            case 2: goto L_0x0067;
            case 3: goto L_0x0025;
            default: goto L_0x0023;
        };
    L_0x0023:
        goto L_0x009a;
    L_0x0025:
        r5 = r0 + 1;
        r0 = getChar(r9, r0);
        r6 = r0 & 128;
        if (r6 == 0) goto L_0x0053;
    L_0x002f:
        r6 = r5 + 1;
        r5 = getChar(r9, r5);
        r0 = r0 & 127;
        r0 = r0 + 2;
    L_0x0039:
        if (r0 <= 0) goto L_0x0051;
    L_0x003b:
        r7 = r9.length;
        if (r4 >= r7) goto L_0x0051;
    L_0x003e:
        r7 = getChar(r9, r4);
        r7 = r7 + r5;
        r7 = r7 & 255;
        r8 = r10 << 8;
        r8 = r8 + r7;
        r7 = (char) r8;
        r1.append(r7);
        r0 = r0 + -1;
        r4 = r4 + 1;
        goto L_0x0039;
    L_0x0051:
        r0 = r6;
        goto L_0x009a;
    L_0x0053:
        r0 = r0 + 2;
    L_0x0055:
        if (r0 <= 0) goto L_0x0099;
    L_0x0057:
        r6 = r9.length;
        if (r4 >= r6) goto L_0x0099;
    L_0x005a:
        r6 = getChar(r9, r4);
        r6 = (char) r6;
        r1.append(r6);
        r0 = r0 + -1;
        r4 = r4 + 1;
        goto L_0x0055;
    L_0x0067:
        r6 = getChar(r9, r0);
        r7 = r0 + 1;
        r7 = getChar(r9, r7);
        r5 = r7 << 8;
        r5 = r5 + r6;
        r5 = (char) r5;
        r1.append(r5);
        r4 = r4 + 1;
        r0 = r0 + 2;
        goto L_0x009a;
    L_0x007d:
        r5 = r0 + 1;
        r0 = getChar(r9, r0);
        r6 = r10 << 8;
        r0 = r0 + r6;
        r0 = (char) r0;
        r1.append(r0);
        r4 = r4 + 1;
        goto L_0x0099;
    L_0x008d:
        r5 = r0 + 1;
        r0 = getChar(r9, r0);
        r0 = (char) r0;
        r1.append(r0);
        r4 = r4 + 1;
    L_0x0099:
        r0 = r5;
    L_0x009a:
        r3 = r3 << 2;
        r3 = r3 & 255;
        r2 = r2 + -2;
        goto L_0x000e;
    L_0x00a2:
        r9 = r1.toString();
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.rarfile.FileNameDecoder.decode(byte[], int):java.lang.String");
    }
}
