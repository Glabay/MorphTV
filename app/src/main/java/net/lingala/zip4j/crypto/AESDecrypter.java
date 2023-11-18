package net.lingala.zip4j.crypto;

import java.util.Arrays;
import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
import net.lingala.zip4j.crypto.engine.AESEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.util.Raw;
import org.apache.commons.lang3.CharEncoding;

public class AESDecrypter implements IDecrypter {
    private int KEY_LENGTH;
    private int MAC_LENGTH;
    private final int PASSWORD_VERIFIER_LENGTH = 2;
    private int SALT_LENGTH;
    private AESEngine aesEngine;
    private byte[] aesKey;
    private byte[] counterBlock;
    private byte[] derivedPasswordVerifier;
    private byte[] iv;
    private LocalFileHeader localFileHeader;
    private int loopCount = 0;
    private MacBasedPRF mac;
    private byte[] macKey;
    private int nonce = 1;
    private byte[] storedMac;

    public int getPasswordVerifierLength() {
        return 2;
    }

    public AESDecrypter(LocalFileHeader localFileHeader, byte[] bArr, byte[] bArr2) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("one of the input parameters is null in AESDecryptor Constructor");
        }
        this.localFileHeader = localFileHeader;
        this.storedMac = null;
        this.iv = new byte[16];
        this.counterBlock = new byte[16];
        init(bArr, bArr2);
    }

    private void init(byte[] bArr, byte[] bArr2) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("invalid file header in init method of AESDecryptor");
        }
        AESExtraDataRecord aesExtraDataRecord = this.localFileHeader.getAesExtraDataRecord();
        if (aesExtraDataRecord == null) {
            throw new ZipException("invalid aes extra data record - in init method of AESDecryptor");
        }
        switch (aesExtraDataRecord.getAesStrength()) {
            case 1:
                this.KEY_LENGTH = 16;
                this.MAC_LENGTH = 16;
                this.SALT_LENGTH = 8;
                break;
            case 2:
                this.KEY_LENGTH = 24;
                this.MAC_LENGTH = 24;
                this.SALT_LENGTH = 12;
                break;
            case 3:
                this.KEY_LENGTH = 32;
                this.MAC_LENGTH = 32;
                this.SALT_LENGTH = 16;
                break;
            default:
                bArr2 = new StringBuffer("invalid aes key strength for file: ");
                bArr2.append(this.localFileHeader.getFileName());
                throw new ZipException(bArr2.toString());
        }
        if (this.localFileHeader.getPassword() != null) {
            if (this.localFileHeader.getPassword().length > 0) {
                bArr = deriveKey(bArr, this.localFileHeader.getPassword());
                if (bArr != null) {
                    if (bArr.length == (this.KEY_LENGTH + this.MAC_LENGTH) + 2) {
                        this.aesKey = new byte[this.KEY_LENGTH];
                        this.macKey = new byte[this.MAC_LENGTH];
                        this.derivedPasswordVerifier = new byte[2];
                        System.arraycopy(bArr, 0, this.aesKey, 0, this.KEY_LENGTH);
                        System.arraycopy(bArr, this.KEY_LENGTH, this.macKey, 0, this.MAC_LENGTH);
                        System.arraycopy(bArr, this.KEY_LENGTH + this.MAC_LENGTH, this.derivedPasswordVerifier, 0, 2);
                        if (this.derivedPasswordVerifier == null) {
                            throw new ZipException("invalid derived password verifier for AES");
                        } else if (Arrays.equals(bArr2, this.derivedPasswordVerifier) == null) {
                            bArr2 = new StringBuffer("Wrong Password for file: ");
                            bArr2.append(this.localFileHeader.getFileName());
                            throw new ZipException(bArr2.toString(), 5);
                        } else {
                            this.aesEngine = new AESEngine(this.aesKey);
                            this.mac = new MacBasedPRF("HmacSHA1");
                            this.mac.init(this.macKey);
                            return;
                        }
                    }
                }
                throw new ZipException("invalid derived key");
            }
        }
        throw new ZipException("empty or null password provided for AES Decryptor");
    }

    public int decryptData(byte[] bArr, int i, int i2) throws ZipException {
        if (this.aesEngine == null) {
            throw new ZipException("AES not initialized properly");
        }
        int i3 = i;
        while (true) {
            int i4 = i + i2;
            if (i3 >= i4) {
                return i2;
            }
            int i5 = i3 + 16;
            try {
                this.loopCount = i5 <= i4 ? 16 : i4 - i3;
                this.mac.update(bArr, i3, this.loopCount);
                Raw.prepareBuffAESIVBytes(this.iv, this.nonce, 16);
                this.aesEngine.processBlock(this.iv, this.counterBlock);
                for (i4 = 0; i4 < this.loopCount; i4++) {
                    int i6 = i3 + i4;
                    bArr[i6] = (byte) (bArr[i6] ^ this.counterBlock[i4]);
                }
                this.nonce++;
                i3 = i5;
            } catch (byte[] bArr2) {
                throw bArr2;
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        }
    }

    public int decryptData(byte[] bArr) throws ZipException {
        return decryptData(bArr, 0, bArr.length);
    }

    private byte[] deriveKey(byte[] bArr, char[] cArr) throws ZipException {
        try {
            return new PBKDF2Engine(new PBKDF2Parameters("HmacSHA1", CharEncoding.ISO_8859_1, bArr, 1000)).deriveKey(cArr, (this.KEY_LENGTH + this.MAC_LENGTH) + 2);
        } catch (Throwable e) {
            throw new ZipException(e);
        }
    }

    public int getSaltLength() {
        return this.SALT_LENGTH;
    }

    public byte[] getCalculatedAuthenticationBytes() {
        return this.mac.doFinal();
    }

    public void setStoredMac(byte[] bArr) {
        this.storedMac = bArr;
    }

    public byte[] getStoredMac() {
        return this.storedMac;
    }
}
