package net.lingala.zip4j.crypto.PBKDF2;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacBasedPRF implements PRF {
    protected int hLen;
    protected Mac mac;
    protected String macAlgorithm;

    public MacBasedPRF(String str) {
        this.macAlgorithm = str;
        try {
            this.mac = Mac.getInstance(str);
            this.hLen = this.mac.getMacLength();
        } catch (String str2) {
            throw new RuntimeException(str2);
        }
    }

    public MacBasedPRF(String str, String str2) {
        this.macAlgorithm = str;
        try {
            this.mac = Mac.getInstance(str, str2);
            this.hLen = this.mac.getMacLength();
        } catch (String str3) {
            throw new RuntimeException(str3);
        } catch (String str32) {
            throw new RuntimeException(str32);
        }
    }

    public byte[] doFinal(byte[] bArr) {
        return this.mac.doFinal(bArr);
    }

    public byte[] doFinal() {
        return this.mac.doFinal();
    }

    public int getHLen() {
        return this.hLen;
    }

    public void init(byte[] bArr) {
        try {
            this.mac.init(new SecretKeySpec(bArr, this.macAlgorithm));
        } catch (byte[] bArr2) {
            throw new RuntimeException(bArr2);
        }
    }

    public void update(byte[] bArr) {
        try {
            this.mac.update(bArr);
        } catch (byte[] bArr2) {
            throw new RuntimeException(bArr2);
        }
    }

    public void update(byte[] bArr, int i, int i2) {
        try {
            this.mac.update(bArr, i, i2);
        } catch (byte[] bArr2) {
            throw new RuntimeException(bArr2);
        }
    }
}
