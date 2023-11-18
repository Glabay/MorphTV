package net.lingala.zip4j.crypto.PBKDF2;

import net.lingala.zip4j.util.Raw;

public class PBKDF2Engine {
    protected PBKDF2Parameters parameters;
    protected PRF prf;

    public PBKDF2Engine() {
        this.parameters = null;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters pBKDF2Parameters) {
        this.parameters = pBKDF2Parameters;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters pBKDF2Parameters, PRF prf) {
        this.parameters = pBKDF2Parameters;
        this.prf = prf;
    }

    public byte[] deriveKey(char[] cArr) {
        return deriveKey(cArr, 0);
    }

    public byte[] deriveKey(char[] cArr, int i) {
        byte[] bArr = (byte[]) null;
        if (cArr == null) {
            throw new NullPointerException();
        }
        assertPRF(Raw.convertCharArrayToByteArray(cArr));
        if (i == 0) {
            i = this.prf.getHLen();
        }
        return PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), i);
    }

    public boolean verifyKey(char[] cArr) {
        byte[] derivedKey = getParameters().getDerivedKey();
        if (derivedKey != null) {
            if (derivedKey.length != 0) {
                cArr = deriveKey(cArr, derivedKey.length);
                if (cArr != null) {
                    if (cArr.length == derivedKey.length) {
                        for (int i = 0; i < cArr.length; i++) {
                            if (cArr[i] != derivedKey[i]) {
                                return false;
                            }
                        }
                        return 1;
                    }
                }
                return false;
            }
        }
        return false;
    }

    protected void assertPRF(byte[] bArr) {
        if (this.prf == null) {
            this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
        }
        this.prf.init(bArr);
    }

    public PRF getPseudoRandomFunction() {
        return this.prf;
    }

    protected byte[] PBKDF2(PRF prf, byte[] bArr, int i, int i2) {
        int i3 = i2;
        byte[] bArr2 = bArr == null ? new byte[0] : bArr;
        int hLen = prf.getHLen();
        int ceil = ceil(i3, hLen);
        int i4 = i3 - ((ceil - 1) * hLen);
        Object obj = new byte[(ceil * hLen)];
        int i5 = 0;
        for (int i6 = 1; i6 <= ceil; i6++) {
            _F(obj, i5, prf, bArr2, i, i6);
            i5 += hLen;
        }
        if (i4 >= hLen) {
            return obj;
        }
        Object obj2 = new byte[i3];
        System.arraycopy(obj, 0, obj2, 0, i3);
        return obj2;
    }

    protected int ceil(int i, int i2) {
        return (i / i2) + (i % i2 > 0 ? 1 : 0);
    }

    protected void _F(byte[] bArr, int i, PRF prf, byte[] bArr2, int i2, int i3) {
        int hLen = prf.getHLen();
        Object obj = new byte[hLen];
        byte[] bArr3 = new byte[(bArr2.length + 4)];
        System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
        INT(bArr3, bArr2.length, i3);
        for (bArr2 = null; bArr2 < i2; bArr2++) {
            bArr3 = prf.doFinal(bArr3);
            xor(obj, bArr3);
        }
        System.arraycopy(obj, 0, bArr, i, hLen);
    }

    protected void xor(byte[] bArr, byte[] bArr2) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
        }
    }

    protected void INT(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 / 16777216);
        bArr[i + 1] = (byte) (i2 / 65536);
        bArr[i + 2] = (byte) (i2 / 256);
        bArr[i + 3] = (byte) i2;
    }

    public PBKDF2Parameters getParameters() {
        return this.parameters;
    }

    public void setParameters(PBKDF2Parameters pBKDF2Parameters) {
        this.parameters = pBKDF2Parameters;
    }

    public void setPseudoRandomFunction(PRF prf) {
        this.prf = prf;
    }
}
