package net.lingala.zip4j.crypto;

import java.util.Random;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Raw;
import org.apache.commons.lang3.CharEncoding;

public class AESEncrpyter implements IEncrypter {
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private final int PASSWORD_VERIFIER_LENGTH = 2;
    private int SALT_LENGTH;
    private AESEngine aesEngine;
    private byte[] aesKey;
    private byte[] counterBlock;
    private byte[] derivedPasswordVerifier;
    private boolean finished;
    private byte[] iv;
    private int keyStrength;
    private int loopCount = 0;
    private MacBasedPRF mac;
    private byte[] macKey;
    private int nonce = 1;
    private char[] password;
    private byte[] saltBytes;

    public int getPasswordVeriifierLength() {
        return 2;
    }

    public AESEncrpyter(char[] cArr, int i) throws ZipException {
        if (cArr != null) {
            if (cArr.length != 0) {
                if (i == 1 || i == 3) {
                    this.password = cArr;
                    this.keyStrength = i;
                    this.finished = false;
                    this.counterBlock = new byte[16];
                    this.iv = new byte[16];
                    init();
                    return;
                }
                throw new ZipException("Invalid key strength in AES encrypter constructor");
            }
        }
        throw new ZipException("input password is empty or null in AES encrypter constructor");
    }

    private void init() throws ZipException {
        int i = this.keyStrength;
        if (i == 1) {
            this.KEY_LENGTH = 16;
            this.MAC_LENGTH = 16;
            this.SALT_LENGTH = 8;
        } else if (i != 3) {
            throw new ZipException("invalid aes key strength, cannot determine key sizes");
        } else {
            this.KEY_LENGTH = 32;
            this.MAC_LENGTH = 32;
            this.SALT_LENGTH = 16;
        }
        this.saltBytes = generateSalt(this.SALT_LENGTH);
        Object deriveKey = deriveKey(this.saltBytes, this.password);
        if (deriveKey != null) {
            if (deriveKey.length == (this.KEY_LENGTH + this.MAC_LENGTH) + 2) {
                this.aesKey = new byte[this.KEY_LENGTH];
                this.macKey = new byte[this.MAC_LENGTH];
                this.derivedPasswordVerifier = new byte[2];
                System.arraycopy(deriveKey, 0, this.aesKey, 0, this.KEY_LENGTH);
                System.arraycopy(deriveKey, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
                System.arraycopy(deriveKey, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
                this.aesEngine = new AESEngine(this.aesKey);
                this.mac = new MacBasedPRF("HmacSHA1");
                this.mac.init(this.macKey);
                return;
            }
        }
        throw new ZipException("invalid key generated, cannot decrypt file");
    }

    private byte[] deriveKey(byte[] bArr, char[] cArr) throws ZipException {
        try {
            return new PBKDF2Engine(new PBKDF2Parameters("HmacSHA1", CharEncoding.ISO_8859_1, bArr, 1000)).deriveKey(cArr, (this.KEY_LENGTH + this.MAC_LENGTH) + 2);
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    public int encryptData(byte[] bArr) throws ZipException {
        if (bArr != null) {
            return encryptData(bArr, 0, bArr.length);
        }
        throw new ZipException("input bytes are null, cannot perform AES encrpytion");
    }

    public int encryptData(byte[] bArr, int i, int i2) throws ZipException {
        if (this.finished) {
            throw new ZipException("AES Encrypter is in finished state (A non 16 byte block has already been passed to encrypter)");
        }
        if (i2 % 16 != 0) {
            this.finished = true;
        }
        int i3 = i;
        while (true) {
            int i4 = i + i2;
            if (i3 >= i4) {
                return i2;
            }
            int i5 = i3 + 16;
            this.loopCount = i5 <= i4 ? 16 : i4 - i3;
            Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
            this.aesEngine.processBlock(this.iv, this.counterBlock);
            for (i4 = 0; i4 < this.loopCount; i4++) {
                int i6 = i3 + i4;
                bArr[i6] = (byte) (bArr[i6] ^ this.counterBlock[i4]);
            }
            this.mac.update(bArr, i3, this.loopCount);
            this.nonce++;
            i3 = i5;
        }
    }

    private static byte[] generateSalt(int i) throws ZipException {
        if (i == 8 || i == 16) {
            int i2 = i == 8 ? 2 : 0;
            if (i == 16) {
                i2 = 4;
            }
            i = new byte[i];
            for (int i3 = 0; i3 < i2; i3++) {
                int nextInt = new Random().nextInt();
                int i4 = i3 * 4;
                i[i4 + 0] = (byte) (nextInt >> 24);
                i[i4 + 1] = (byte) (nextInt >> 16);
                i[i4 + 2] = (byte) (nextInt >> 8);
                i[i4 + 3] = (byte) nextInt;
            }
            return i;
        }
        throw new ZipException("invalid salt size, cannot generate salt");
    }

    public byte[] getFinalMac() {
        Object obj = new byte[10];
        System.arraycopy(this.mac.doFinal(), 0, obj, 0, 10);
        return obj;
    }

    public byte[] getDerivedPasswordVerifier() {
        return this.derivedPasswordVerifier;
    }

    public void setDerivedPasswordVerifier(byte[] bArr) {
        this.derivedPasswordVerifier = bArr;
    }

    public byte[] getSaltBytes() {
        return this.saltBytes;
    }

    public void setSaltBytes(byte[] bArr) {
        this.saltBytes = bArr;
    }

    public int getSaltLength() {
        return this.SALT_LENGTH;
    }
}
