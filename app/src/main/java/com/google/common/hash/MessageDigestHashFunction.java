package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Arrays;

final class MessageDigestHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private final int bytes;
    private final MessageDigest prototype;
    private final boolean supportsClone;
    private final String toString;

    private static final class MessageDigestHasher extends AbstractByteHasher {
        private final int bytes;
        private final MessageDigest digest;
        private boolean done;

        private MessageDigestHasher(MessageDigest messageDigest, int i) {
            this.digest = messageDigest;
            this.bytes = i;
        }

        protected void update(byte b) {
            checkNotDone();
            this.digest.update(b);
        }

        protected void update(byte[] bArr) {
            checkNotDone();
            this.digest.update(bArr);
        }

        protected void update(byte[] bArr, int i, int i2) {
            checkNotDone();
            this.digest.update(bArr, i, i2);
        }

        private void checkNotDone() {
            Preconditions.checkState(this.done ^ 1, "Cannot re-use a Hasher after calling hash() on it");
        }

        public HashCode hash() {
            checkNotDone();
            this.done = true;
            return HashCode.fromBytesNoCopy(this.bytes == this.digest.getDigestLength() ? this.digest.digest() : Arrays.copyOf(this.digest.digest(), this.bytes));
        }
    }

    private static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final String algorithmName;
        private final int bytes;
        private final String toString;

        private SerializedForm(String str, int i, String str2) {
            this.algorithmName = str;
            this.bytes = i;
            this.toString = str2;
        }

        private Object readResolve() {
            return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
        }
    }

    MessageDigestHashFunction(String str, String str2) {
        this.prototype = getMessageDigest(str);
        this.bytes = this.prototype.getDigestLength();
        this.toString = (String) Preconditions.checkNotNull(str2);
        this.supportsClone = supportsClone();
    }

    MessageDigestHashFunction(String str, int i, String str2) {
        this.toString = (String) Preconditions.checkNotNull(str2);
        this.prototype = getMessageDigest(str);
        boolean z = i >= 4 && i <= this.prototype.getDigestLength();
        Preconditions.checkArgument(z, "bytes (%s) must be >= 4 and < %s", Integer.valueOf(i), Integer.valueOf(str));
        this.bytes = i;
        this.supportsClone = supportsClone();
    }

    private boolean supportsClone() {
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
        r1 = this;
        r0 = r1.prototype;	 Catch:{ CloneNotSupportedException -> 0x0007 }
        r0.clone();	 Catch:{ CloneNotSupportedException -> 0x0007 }
        r0 = 1;
        return r0;
    L_0x0007:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.MessageDigestHashFunction.supportsClone():boolean");
    }

    public int bits() {
        return this.bytes * 8;
    }

    public String toString() {
        return this.toString;
    }

    private static MessageDigest getMessageDigest(String str) {
        try {
            return MessageDigest.getInstance(str);
        } catch (String str2) {
            throw new AssertionError(str2);
        }
    }

    public com.google.common.hash.Hasher newHasher() {
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
        r4 = this;
        r0 = r4.supportsClone;
        r1 = 0;
        if (r0 == 0) goto L_0x0015;
    L_0x0005:
        r0 = new com.google.common.hash.MessageDigestHashFunction$MessageDigestHasher;	 Catch:{ CloneNotSupportedException -> 0x0015 }
        r2 = r4.prototype;	 Catch:{ CloneNotSupportedException -> 0x0015 }
        r2 = r2.clone();	 Catch:{ CloneNotSupportedException -> 0x0015 }
        r2 = (java.security.MessageDigest) r2;	 Catch:{ CloneNotSupportedException -> 0x0015 }
        r3 = r4.bytes;	 Catch:{ CloneNotSupportedException -> 0x0015 }
        r0.<init>(r2, r3);	 Catch:{ CloneNotSupportedException -> 0x0015 }
        return r0;
    L_0x0015:
        r0 = new com.google.common.hash.MessageDigestHashFunction$MessageDigestHasher;
        r2 = r4.prototype;
        r2 = r2.getAlgorithm();
        r2 = getMessageDigest(r2);
        r3 = r4.bytes;
        r0.<init>(r2, r3);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.MessageDigestHashFunction.newHasher():com.google.common.hash.Hasher");
    }

    Object writeReplace() {
        return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
    }
}
