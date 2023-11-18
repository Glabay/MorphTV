package okio;

import com.google.android.gms.common.util.AndroidUtilsLight;
import java.io.IOException;
import java.security.MessageDigest;
import javax.annotation.Nullable;
import javax.crypto.Mac;

public final class HashingSink extends ForwardingSink {
    @Nullable
    private final Mac mac;
    @Nullable
    private final MessageDigest messageDigest;

    public static HashingSink md5(Sink sink) {
        return new HashingSink(sink, "MD5");
    }

    public static HashingSink sha1(Sink sink) {
        return new HashingSink(sink, "SHA-1");
    }

    public static HashingSink sha256(Sink sink) {
        return new HashingSink(sink, "SHA-256");
    }

    public static HashingSink sha512(Sink sink) {
        return new HashingSink(sink, AndroidUtilsLight.DIGEST_ALGORITHM_SHA512);
    }

    public static HashingSink hmacSha1(Sink sink, ByteString byteString) {
        return new HashingSink(sink, byteString, "HmacSHA1");
    }

    public static HashingSink hmacSha256(Sink sink, ByteString byteString) {
        return new HashingSink(sink, byteString, "HmacSHA256");
    }

    public static HashingSink hmacSha512(Sink sink, ByteString byteString) {
        return new HashingSink(sink, byteString, "HmacSHA512");
    }

    private HashingSink(okio.Sink r1, java.lang.String r2) {
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
        r0 = this;
        r0.<init>(r1);
        r1 = java.security.MessageDigest.getInstance(r2);	 Catch:{ NoSuchAlgorithmException -> 0x000d }
        r0.messageDigest = r1;	 Catch:{ NoSuchAlgorithmException -> 0x000d }
        r1 = 0;	 Catch:{ NoSuchAlgorithmException -> 0x000d }
        r0.mac = r1;	 Catch:{ NoSuchAlgorithmException -> 0x000d }
        return;
    L_0x000d:
        r1 = new java.lang.AssertionError;
        r1.<init>();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.HashingSink.<init>(okio.Sink, java.lang.String):void");
    }

    private HashingSink(okio.Sink r2, okio.ByteString r3, java.lang.String r4) {
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
        r1.<init>(r2);
        r2 = javax.crypto.Mac.getInstance(r4);	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r1.mac = r2;	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r2 = r1.mac;	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r0 = new javax.crypto.spec.SecretKeySpec;	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r3 = r3.toByteArray();	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r0.<init>(r3, r4);	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r2.init(r0);	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r2 = 0;	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        r1.messageDigest = r2;	 Catch:{ NoSuchAlgorithmException -> 0x0022, InvalidKeyException -> 0x001b }
        return;
    L_0x001b:
        r2 = move-exception;
        r3 = new java.lang.IllegalArgumentException;
        r3.<init>(r2);
        throw r3;
    L_0x0022:
        r2 = new java.lang.AssertionError;
        r2.<init>();
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.HashingSink.<init>(okio.Sink, okio.ByteString, java.lang.String):void");
    }

    public void write(Buffer buffer, long j) throws IOException {
        Util.checkOffsetAndCount(buffer.size, 0, j);
        Segment segment = buffer.head;
        long j2 = 0;
        while (j2 < j) {
            int min = (int) Math.min(j - j2, (long) (segment.limit - segment.pos));
            if (this.messageDigest != null) {
                this.messageDigest.update(segment.data, segment.pos, min);
            } else {
                this.mac.update(segment.data, segment.pos, min);
            }
            long j3 = j2 + ((long) min);
            segment = segment.next;
            j2 = j3;
        }
        super.write(buffer, j);
    }

    public ByteString hash() {
        return ByteString.of(this.messageDigest != null ? this.messageDigest.digest() : this.mac.doFinal());
    }
}
