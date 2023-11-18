package org.apache.commons.io.filefilter;

import java.io.Serializable;
import java.nio.charset.Charset;

public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -547733176983104172L;
    private final long byteOffset;
    private final byte[] magicNumbers;

    public MagicNumberFileFilter(byte[] bArr) {
        this(bArr, 0);
    }

    public MagicNumberFileFilter(String str) {
        this(str, 0);
    }

    public MagicNumberFileFilter(String str, long j) {
        if (str == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        } else if (str.isEmpty()) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        } else if (j < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        } else {
            this.magicNumbers = str.getBytes(Charset.defaultCharset());
            this.byteOffset = j;
        }
    }

    public MagicNumberFileFilter(byte[] bArr, long j) {
        if (bArr == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        } else if (bArr.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        } else if (j < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        } else {
            this.magicNumbers = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.magicNumbers, 0, bArr.length);
            this.byteOffset = j;
        }
    }

    public boolean accept(java.io.File r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r5 = this;
        r0 = 0;
        if (r6 == 0) goto L_0x0051;
    L_0x0003:
        r1 = r6.isFile();
        if (r1 == 0) goto L_0x0051;
    L_0x0009:
        r1 = r6.canRead();
        if (r1 == 0) goto L_0x0051;
    L_0x000f:
        r1 = new java.io.RandomAccessFile;	 Catch:{ IOException -> 0x0051 }
        r2 = "r";	 Catch:{ IOException -> 0x0051 }
        r1.<init>(r6, r2);	 Catch:{ IOException -> 0x0051 }
        r6 = 0;
        r2 = r5.magicNumbers;	 Catch:{ Throwable -> 0x003e }
        r2 = r2.length;	 Catch:{ Throwable -> 0x003e }
        r2 = new byte[r2];	 Catch:{ Throwable -> 0x003e }
        r3 = r5.byteOffset;	 Catch:{ Throwable -> 0x003e }
        r1.seek(r3);	 Catch:{ Throwable -> 0x003e }
        r3 = r1.read(r2);	 Catch:{ Throwable -> 0x003e }
        r4 = r5.magicNumbers;	 Catch:{ Throwable -> 0x003e }
        r4 = r4.length;	 Catch:{ Throwable -> 0x003e }
        if (r3 == r4) goto L_0x0030;
    L_0x002a:
        if (r1 == 0) goto L_0x002f;
    L_0x002c:
        r1.close();	 Catch:{ IOException -> 0x0051 }
    L_0x002f:
        return r0;
    L_0x0030:
        r3 = r5.magicNumbers;	 Catch:{ Throwable -> 0x003e }
        r2 = java.util.Arrays.equals(r3, r2);	 Catch:{ Throwable -> 0x003e }
        if (r1 == 0) goto L_0x003b;
    L_0x0038:
        r1.close();	 Catch:{ IOException -> 0x0051 }
    L_0x003b:
        return r2;
    L_0x003c:
        r2 = move-exception;
        goto L_0x0040;
    L_0x003e:
        r6 = move-exception;
        throw r6;	 Catch:{ all -> 0x003c }
    L_0x0040:
        if (r1 == 0) goto L_0x0050;
    L_0x0042:
        if (r6 == 0) goto L_0x004d;
    L_0x0044:
        r1.close();	 Catch:{ Throwable -> 0x0048 }
        goto L_0x0050;
    L_0x0048:
        r1 = move-exception;
        r6.addSuppressed(r1);	 Catch:{ IOException -> 0x0051 }
        goto L_0x0050;	 Catch:{ IOException -> 0x0051 }
    L_0x004d:
        r1.close();	 Catch:{ IOException -> 0x0051 }
    L_0x0050:
        throw r2;	 Catch:{ IOException -> 0x0051 }
    L_0x0051:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.filefilter.MagicNumberFileFilter.accept(java.io.File):boolean");
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.append("(");
        stringBuilder.append(new String(this.magicNumbers, Charset.defaultCharset()));
        stringBuilder.append(",");
        stringBuilder.append(this.byteOffset);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
