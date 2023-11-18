package net.lingala.zip4j.crypto;

import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class StandardDecrypter implements IDecrypter {
    private byte[] crc = new byte[4];
    private FileHeader fileHeader;
    private ZipCryptoEngine zipCryptoEngine;

    public StandardDecrypter(FileHeader fileHeader, byte[] bArr) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("one of more of the input parameters were null in StandardDecryptor");
        }
        this.fileHeader = fileHeader;
        this.zipCryptoEngine = new ZipCryptoEngine();
        init(bArr);
    }

    public int decryptData(byte[] bArr) throws ZipException {
        return decryptData(bArr, 0, bArr.length);
    }

    public int decryptData(byte[] bArr, int i, int i2) throws ZipException {
        if (i >= 0) {
            if (i2 >= 0) {
                int i3 = i;
                while (i3 < i + i2) {
                    try {
                        byte decryptByte = (byte) (((bArr[i3] & 255) ^ this.zipCryptoEngine.decryptByte()) & 255);
                        this.zipCryptoEngine.updateKeys(decryptByte);
                        bArr[i3] = decryptByte;
                        i3++;
                    } catch (Throwable e) {
                        throw new ZipException(e);
                    }
                }
                return i2;
            }
        }
        throw new ZipException("one of the input parameters were null in standard decrpyt data");
    }

    public void init(byte[] bArr) throws ZipException {
        byte[] crcBuff = this.fileHeader.getCrcBuff();
        this.crc[3] = (byte) (crcBuff[3] & 255);
        this.crc[2] = (byte) ((crcBuff[3] >> 8) & 255);
        this.crc[1] = (byte) ((crcBuff[3] >> 16) & 255);
        int i = 0;
        this.crc[0] = (byte) ((crcBuff[3] >> 24) & 255);
        if (this.crc[2] <= (byte) 0 && this.crc[1] <= (byte) 0) {
            if (this.crc[0] <= (byte) 0) {
                if (this.fileHeader.getPassword() != null) {
                    if (this.fileHeader.getPassword().length > 0) {
                        this.zipCryptoEngine.initKeys(this.fileHeader.getPassword());
                        try {
                            int i2 = bArr[0];
                            while (i < 12) {
                                this.zipCryptoEngine.updateKeys((byte) (this.zipCryptoEngine.decryptByte() ^ i2));
                                i++;
                                if (i != 12) {
                                    i2 = bArr[i];
                                }
                            }
                            return;
                        } catch (Throwable e) {
                            throw new ZipException(e);
                        }
                    }
                }
                throw new ZipException("Wrong password!", 5);
            }
        }
        throw new IllegalStateException("Invalid CRC in File Header");
    }
}
