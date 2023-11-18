package okhttp3.internal.publicsuffix;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.IDN;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okio.GzipSource;
import okio.Okio;

public final class PublicSuffixDatabase {
    private static final String[] EMPTY_RULE = new String[0];
    private static final byte EXCEPTION_MARKER = (byte) 33;
    private static final String[] PREVAILING_RULE = new String[]{"*"};
    public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
    private static final byte[] WILDCARD_LABEL = new byte[]{(byte) 42};
    private static final PublicSuffixDatabase instance = new PublicSuffixDatabase();
    private final AtomicBoolean listRead = new AtomicBoolean(false);
    private byte[] publicSuffixExceptionListBytes;
    private byte[] publicSuffixListBytes;
    private final CountDownLatch readCompleteLatch = new CountDownLatch(1);

    public static PublicSuffixDatabase get() {
        return instance;
    }

    public String getEffectiveTldPlusOne(String str) {
        if (str == null) {
            throw new NullPointerException("domain == null");
        }
        String[] split = IDN.toUnicode(str).split("\\.");
        String[] findMatchingRule = findMatchingRule(split);
        if (split.length == findMatchingRule.length && findMatchingRule[0].charAt(0) != '!') {
            return null;
        }
        int length;
        if (findMatchingRule[0].charAt(0) == '!') {
            length = split.length - findMatchingRule.length;
        } else {
            length = split.length - (findMatchingRule.length + 1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        str = str.split("\\.");
        for (length = 
/*
Method generation error in method: okhttp3.internal.publicsuffix.PublicSuffixDatabase.getEffectiveTldPlusOne(java.lang.String):java.lang.String, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r0_7 'length' int) = (r0_4 'length' int), (r0_6 'length' int) binds: {(r0_4 'length' int)=B:11:0x0031, (r0_6 'length' int)=B:12:0x0035} in method: okhttp3.internal.publicsuffix.PublicSuffixDatabase.getEffectiveTldPlusOne(java.lang.String):java.lang.String, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 24 more

*/

        private java.lang.String[] findMatchingRule(java.lang.String[] r8) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r7 = this;
            r0 = r7.listRead;
            r0 = r0.get();
            r1 = 0;
            r2 = 1;
            if (r0 != 0) goto L_0x0016;
        L_0x000a:
            r0 = r7.listRead;
            r0 = r0.compareAndSet(r1, r2);
            if (r0 == 0) goto L_0x0016;
        L_0x0012:
            r7.readTheList();
            goto L_0x001b;
        L_0x0016:
            r0 = r7.readCompleteLatch;	 Catch:{ InterruptedException -> 0x001b }
            r0.await();	 Catch:{ InterruptedException -> 0x001b }
        L_0x001b:
            monitor-enter(r7);
            r0 = r7.publicSuffixListBytes;	 Catch:{ all -> 0x00bf }
            if (r0 != 0) goto L_0x0028;	 Catch:{ all -> 0x00bf }
        L_0x0020:
            r8 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00bf }
            r0 = "Unable to load publicsuffixes.gz resource from the classpath.";	 Catch:{ all -> 0x00bf }
            r8.<init>(r0);	 Catch:{ all -> 0x00bf }
            throw r8;	 Catch:{ all -> 0x00bf }
        L_0x0028:
            monitor-exit(r7);	 Catch:{ all -> 0x00bf }
            r0 = r8.length;
            r0 = new byte[r0][];
            r3 = 0;
        L_0x002d:
            r4 = r8.length;
            if (r3 >= r4) goto L_0x003d;
        L_0x0030:
            r4 = r8[r3];
            r5 = okhttp3.internal.Util.UTF_8;
            r4 = r4.getBytes(r5);
            r0[r3] = r4;
            r3 = r3 + 1;
            goto L_0x002d;
        L_0x003d:
            r8 = 0;
        L_0x003e:
            r3 = r0.length;
            r4 = 0;
            if (r8 >= r3) goto L_0x004e;
        L_0x0042:
            r3 = r7.publicSuffixListBytes;
            r3 = binarySearchBytes(r3, r0, r8);
            if (r3 == 0) goto L_0x004b;
        L_0x004a:
            goto L_0x004f;
        L_0x004b:
            r8 = r8 + 1;
            goto L_0x003e;
        L_0x004e:
            r3 = r4;
        L_0x004f:
            r8 = r0.length;
            if (r8 <= r2) goto L_0x006d;
        L_0x0052:
            r8 = r0.clone();
            r8 = (byte[][]) r8;
            r5 = 0;
        L_0x0059:
            r6 = r8.length;
            r6 = r6 - r2;
            if (r5 >= r6) goto L_0x006d;
        L_0x005d:
            r6 = WILDCARD_LABEL;
            r8[r5] = r6;
            r6 = r7.publicSuffixListBytes;
            r6 = binarySearchBytes(r6, r8, r5);
            if (r6 == 0) goto L_0x006a;
        L_0x0069:
            goto L_0x006e;
        L_0x006a:
            r5 = r5 + 1;
            goto L_0x0059;
        L_0x006d:
            r6 = r4;
        L_0x006e:
            if (r6 == 0) goto L_0x0080;
        L_0x0070:
            r8 = r0.length;
            r8 = r8 - r2;
            if (r1 >= r8) goto L_0x0080;
        L_0x0074:
            r8 = r7.publicSuffixExceptionListBytes;
            r8 = binarySearchBytes(r8, r0, r1);
            if (r8 == 0) goto L_0x007d;
        L_0x007c:
            goto L_0x0081;
        L_0x007d:
            r1 = r1 + 1;
            goto L_0x0070;
        L_0x0080:
            r8 = r4;
        L_0x0081:
            if (r8 == 0) goto L_0x009b;
        L_0x0083:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "!";
            r0.append(r1);
            r0.append(r8);
            r8 = r0.toString();
            r0 = "\\.";
            r8 = r8.split(r0);
            return r8;
        L_0x009b:
            if (r3 != 0) goto L_0x00a2;
        L_0x009d:
            if (r6 != 0) goto L_0x00a2;
        L_0x009f:
            r8 = PREVAILING_RULE;
            return r8;
        L_0x00a2:
            if (r3 == 0) goto L_0x00ab;
        L_0x00a4:
            r8 = "\\.";
            r8 = r3.split(r8);
            goto L_0x00ad;
        L_0x00ab:
            r8 = EMPTY_RULE;
        L_0x00ad:
            if (r6 == 0) goto L_0x00b6;
        L_0x00af:
            r0 = "\\.";
            r0 = r6.split(r0);
            goto L_0x00b8;
        L_0x00b6:
            r0 = EMPTY_RULE;
        L_0x00b8:
            r1 = r8.length;
            r2 = r0.length;
            if (r1 <= r2) goto L_0x00bd;
        L_0x00bc:
            goto L_0x00be;
        L_0x00bd:
            r8 = r0;
        L_0x00be:
            return r8;
        L_0x00bf:
            r8 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x00bf }
            throw r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.publicsuffix.PublicSuffixDatabase.findMatchingRule(java.lang.String[]):java.lang.String[]");
        }

        private static String binarySearchBytes(byte[] bArr, byte[][] bArr2, int i) {
            byte[] bArr3 = bArr;
            byte[][] bArr4 = bArr2;
            int length = bArr3.length;
            int i2 = 0;
            while (i2 < length) {
                int i3;
                int i4;
                int i5 = (i2 + length) / 2;
                while (i5 > -1 && bArr3[i5] != (byte) 10) {
                    i5--;
                }
                i5++;
                int i6 = 1;
                while (true) {
                    i3 = i5 + i6;
                    if (bArr3[i3] == (byte) 10) {
                        break;
                    }
                    i6++;
                }
                int i7 = i3 - i5;
                int i8 = i;
                Object obj = null;
                int i9 = 0;
                int i10 = 0;
                while (true) {
                    if (obj != null) {
                        obj = null;
                        i4 = 46;
                    } else {
                        i4 = bArr4[i8][i9] & 255;
                    }
                    i4 -= bArr3[i5 + i10] & 255;
                    if (i4 != 0) {
                        break;
                    }
                    i10++;
                    i9++;
                    if (i10 == i7) {
                        break;
                    } else if (bArr4[i8].length == i9) {
                        if (i8 == bArr4.length - 1) {
                            break;
                        }
                        i8++;
                        obj = 1;
                        i9 = -1;
                    }
                }
                if (i4 < 0) {
                    i5--;
                } else {
                    if (i4 > 0) {
                        i3++;
                    } else {
                        int i11 = i7 - i10;
                        int length2 = bArr4[i8].length - i9;
                        while (true) {
                            i8++;
                            if (i8 >= bArr4.length) {
                                break;
                            }
                            length2 += bArr4[i8].length;
                        }
                        if (length2 < i11) {
                            i5--;
                        } else if (length2 <= i11) {
                            return new String(bArr3, i5, i7, Util.UTF_8);
                        } else {
                            i3++;
                        }
                    }
                    i2 = i3;
                }
                length = i5;
            }
            return null;
        }

        private void readTheList() {
            byte[] bArr;
            InputStream resourceAsStream = PublicSuffixDatabase.class.getClassLoader().getResourceAsStream(PUBLIC_SUFFIX_RESOURCE);
            byte[] bArr2 = null;
            if (resourceAsStream != null) {
                Closeable buffer = Okio.buffer(new GzipSource(Okio.source(resourceAsStream)));
                byte[] bArr3;
                try {
                    bArr3 = new byte[buffer.readInt()];
                    buffer.readFully(bArr3);
                    bArr = new byte[buffer.readInt()];
                    buffer.readFully(bArr);
                    bArr2 = bArr3;
                } catch (IOException e) {
                    bArr3 = e;
                    bArr = Platform.get();
                    bArr.log(5, "Failed to read public suffix list", bArr3);
                } finally {
                    Util.closeQuietly(buffer);
                }
                synchronized (this) {
                    this.publicSuffixListBytes = bArr2;
                    this.publicSuffixExceptionListBytes = bArr;
                }
                this.readCompleteLatch.countDown();
            }
            bArr = null;
            synchronized (this) {
                this.publicSuffixListBytes = bArr2;
                this.publicSuffixExceptionListBytes = bArr;
            }
            this.readCompleteLatch.countDown();
        }

        void setListBytes(byte[] bArr, byte[] bArr2) {
            this.publicSuffixListBytes = bArr;
            this.publicSuffixExceptionListBytes = bArr2;
            this.listRead.set(1);
            this.readCompleteLatch.countDown();
        }
    }
