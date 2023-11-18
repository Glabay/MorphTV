package net.lingala.zip4j.crypto.engine;

public class ZipCryptoEngine {
    private static final int[] CRC_TABLE = new int[256];
    private final int[] keys = new int[3];

    static {
        for (int i = 0; i < 256; i++) {
            int i2 = i;
            for (int i3 = 0; i3 < 8; i3++) {
                i2 = (i2 & 1) == 1 ? (i2 >>> 1) ^ -306674912 : i2 >>> 1;
            }
            CRC_TABLE[i] = i2;
        }
    }

    public void initKeys(char[] cArr) {
        int i = 0;
        this.keys[0] = 305419896;
        this.keys[1] = 591751049;
        this.keys[2] = 878082192;
        while (i < cArr.length) {
            updateKeys((byte) (cArr[i] & 255));
            i++;
        }
    }

    public void updateKeys(byte b) {
        this.keys[0] = crc32(this.keys[0], b);
        b = this.keys;
        b[1] = b[1] + (this.keys[0] & 255);
        this.keys[1] = (this.keys[1] * 134775813) + 1;
        this.keys[2] = crc32(this.keys[2], (byte) (this.keys[1] >> 24));
    }

    private int crc32(int i, byte b) {
        return CRC_TABLE[(i ^ b) & 255] ^ (i >>> 8);
    }

    public byte decryptByte() {
        int i = this.keys[2] | 2;
        return (byte) ((i * (i ^ 1)) >>> 8);
    }
}
