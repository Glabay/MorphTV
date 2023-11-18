package ir.mahdi.mzip.rar.crc;

public class RarCRC {
    private static final int[] crcTab = new int[256];

    static {
        for (int i = 0; i < 256; i++) {
            int i2 = i;
            for (int i3 = 0; i3 < 8; i3++) {
                i2 = (i2 & 1) != 0 ? (i2 >>> 1) ^ -306674912 : i2 >>> 1;
            }
            crcTab[i] = i2;
        }
    }

    private RarCRC() {
    }

    public static int checkCrc(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = 0; i4 < Math.min(bArr.length - i2, i3); i4++) {
            i = (i >>> 8) ^ crcTab[(bArr[i2 + i4] ^ i) & 255];
        }
        return i;
    }

    public static short checkOldCrc(short s, byte[] bArr, int i) {
        for (int i2 = 0; i2 < Math.min(bArr.length, i); i2++) {
            s = (short) (((short) (s + ((short) (bArr[i2] & 255)))) & -1);
            s = (short) (((s >>> 15) | (s << 1)) & -1);
        }
        return s;
    }
}
