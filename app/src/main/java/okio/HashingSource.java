package okio;

import java.io.IOException;
import java.security.MessageDigest;
import javax.crypto.Mac;

public final class HashingSource extends ForwardingSource {
    private final Mac mac;
    private final MessageDigest messageDigest;

    public static HashingSource md5(Source source) {
        return new HashingSource(source, "MD5");
    }

    public static HashingSource sha1(Source source) {
        return new HashingSource(source, "SHA-1");
    }

    public static HashingSource sha256(Source source) {
        return new HashingSource(source, "SHA-256");
    }

    public static HashingSource hmacSha1(Source source, ByteString byteString) {
        return new HashingSource(source, byteString, "HmacSHA1");
    }

    public static HashingSource hmacSha256(Source source, ByteString byteString) {
        return new HashingSource(source, byteString, "HmacSHA256");
    }

    private HashingSource(okio.Source r1, java.lang.String r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
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
        throw new UnsupportedOperationException("Method not decompiled: okio.HashingSource.<init>(okio.Source, java.lang.String):void");
    }

    private HashingSource(okio.Source r2, okio.ByteString r3, java.lang.String r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
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
        throw new UnsupportedOperationException("Method not decompiled: okio.HashingSource.<init>(okio.Source, okio.ByteString, java.lang.String):void");
    }

    public long read(Buffer buffer, long j) throws IOException {
        j = super.read(buffer, j);
        if (j != -1) {
            long j2 = buffer.size - j;
            long j3 = buffer.size;
            Segment segment = buffer.head;
            while (j3 > j2) {
                segment = segment.prev;
                j3 -= (long) (segment.limit - segment.pos);
            }
            while (j3 < buffer.size) {
                int i = (int) ((((long) segment.pos) + j2) - j3);
                if (this.messageDigest != null) {
                    this.messageDigest.update(segment.data, i, segment.limit - i);
                } else {
                    this.mac.update(segment.data, i, segment.limit - i);
                }
                long j4 = j3 + ((long) (segment.limit - segment.pos));
                segment = segment.next;
                j3 = j4;
                j2 = j3;
            }
        }
        return j;
    }

    public ByteString hash() {
        return ByteString.of(this.messageDigest != null ? this.messageDigest.digest() : this.mac.doFinal());
    }
}
