package org.apache.commons.lang3;

import java.util.UUID;
import net.lingala.zip4j.util.InternalZipConstants;

public class Conversion {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean[] FFFF = new boolean[]{false, false, false, false};
    private static final boolean[] FFFT = new boolean[]{false, false, false, true};
    private static final boolean[] FFTF = new boolean[]{false, false, true, false};
    private static final boolean[] FFTT = new boolean[]{false, false, true, true};
    private static final boolean[] FTFF = new boolean[]{false, true, false, false};
    private static final boolean[] FTFT = new boolean[]{false, true, false, true};
    private static final boolean[] FTTF = new boolean[]{false, true, true, false};
    private static final boolean[] FTTT = new boolean[]{false, true, true, true};
    private static final boolean[] TFFF = new boolean[]{true, false, false, false};
    private static final boolean[] TFFT = new boolean[]{true, false, false, true};
    private static final boolean[] TFTF = new boolean[]{true, false, true, false};
    private static final boolean[] TFTT = new boolean[]{true, false, true, true};
    private static final boolean[] TTFF = new boolean[]{true, true, false, false};
    private static final boolean[] TTFT = new boolean[]{true, true, false, true};
    private static final boolean[] TTTF = new boolean[]{true, true, true, false};
    private static final boolean[] TTTT = new boolean[]{true, true, true, true};

    public static int hexDigitToInt(char c) {
        int digit = Character.digit(c, 16);
        if (digit >= 0) {
            return digit;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot interpret '");
        stringBuilder.append(c);
        stringBuilder.append("' as a hexadecimal digit");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int hexDigitMsb0ToInt(char r3) {
        /*
        switch(r3) {
            case 48: goto L_0x004b;
            case 49: goto L_0x0048;
            case 50: goto L_0x0046;
            case 51: goto L_0x0043;
            case 52: goto L_0x0041;
            case 53: goto L_0x003e;
            case 54: goto L_0x003c;
            case 55: goto L_0x0039;
            case 56: goto L_0x0037;
            case 57: goto L_0x0034;
            default: goto L_0x0003;
        };
    L_0x0003:
        switch(r3) {
            case 65: goto L_0x0032;
            case 66: goto L_0x002f;
            case 67: goto L_0x002d;
            case 68: goto L_0x002a;
            case 69: goto L_0x0028;
            case 70: goto L_0x0025;
            default: goto L_0x0006;
        };
    L_0x0006:
        switch(r3) {
            case 97: goto L_0x0032;
            case 98: goto L_0x002f;
            case 99: goto L_0x002d;
            case 100: goto L_0x002a;
            case 101: goto L_0x0028;
            case 102: goto L_0x0025;
            default: goto L_0x0009;
        };
    L_0x0009:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Cannot interpret '";
        r1.append(r2);
        r1.append(r3);
        r3 = "' as a hexadecimal digit";
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
    L_0x0025:
        r3 = 15;
        return r3;
    L_0x0028:
        r3 = 7;
        return r3;
    L_0x002a:
        r3 = 11;
        return r3;
    L_0x002d:
        r3 = 3;
        return r3;
    L_0x002f:
        r3 = 13;
        return r3;
    L_0x0032:
        r3 = 5;
        return r3;
    L_0x0034:
        r3 = 9;
        return r3;
    L_0x0037:
        r3 = 1;
        return r3;
    L_0x0039:
        r3 = 14;
        return r3;
    L_0x003c:
        r3 = 6;
        return r3;
    L_0x003e:
        r3 = 10;
        return r3;
    L_0x0041:
        r3 = 2;
        return r3;
    L_0x0043:
        r3 = 12;
        return r3;
    L_0x0046:
        r3 = 4;
        return r3;
    L_0x0048:
        r3 = 8;
        return r3;
    L_0x004b:
        r3 = 0;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.Conversion.hexDigitMsb0ToInt(char):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean[] hexDigitToBinary(char r3) {
        /*
        switch(r3) {
            case 48: goto L_0x00ac;
            case 49: goto L_0x00a3;
            case 50: goto L_0x009a;
            case 51: goto L_0x0091;
            case 52: goto L_0x0088;
            case 53: goto L_0x007f;
            case 54: goto L_0x0076;
            case 55: goto L_0x006d;
            case 56: goto L_0x0064;
            case 57: goto L_0x005b;
            default: goto L_0x0003;
        };
    L_0x0003:
        switch(r3) {
            case 65: goto L_0x0052;
            case 66: goto L_0x0049;
            case 67: goto L_0x0040;
            case 68: goto L_0x0037;
            case 69: goto L_0x002e;
            case 70: goto L_0x0025;
            default: goto L_0x0006;
        };
    L_0x0006:
        switch(r3) {
            case 97: goto L_0x0052;
            case 98: goto L_0x0049;
            case 99: goto L_0x0040;
            case 100: goto L_0x0037;
            case 101: goto L_0x002e;
            case 102: goto L_0x0025;
            default: goto L_0x0009;
        };
    L_0x0009:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Cannot interpret '";
        r1.append(r2);
        r1.append(r3);
        r3 = "' as a hexadecimal digit";
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
    L_0x0025:
        r3 = TTTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x002e:
        r3 = FTTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0037:
        r3 = TFTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0040:
        r3 = FFTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0049:
        r3 = TTFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0052:
        r3 = FTFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x005b:
        r3 = TFFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0064:
        r3 = FFFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x006d:
        r3 = TTTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0076:
        r3 = FTTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x007f:
        r3 = TFTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0088:
        r3 = FFTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0091:
        r3 = TTFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x009a:
        r3 = FTFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x00a3:
        r3 = TFFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x00ac:
        r3 = FFFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.Conversion.hexDigitToBinary(char):boolean[]");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean[] hexDigitMsb0ToBinary(char r3) {
        /*
        switch(r3) {
            case 48: goto L_0x00ac;
            case 49: goto L_0x00a3;
            case 50: goto L_0x009a;
            case 51: goto L_0x0091;
            case 52: goto L_0x0088;
            case 53: goto L_0x007f;
            case 54: goto L_0x0076;
            case 55: goto L_0x006d;
            case 56: goto L_0x0064;
            case 57: goto L_0x005b;
            default: goto L_0x0003;
        };
    L_0x0003:
        switch(r3) {
            case 65: goto L_0x0052;
            case 66: goto L_0x0049;
            case 67: goto L_0x0040;
            case 68: goto L_0x0037;
            case 69: goto L_0x002e;
            case 70: goto L_0x0025;
            default: goto L_0x0006;
        };
    L_0x0006:
        switch(r3) {
            case 97: goto L_0x0052;
            case 98: goto L_0x0049;
            case 99: goto L_0x0040;
            case 100: goto L_0x0037;
            case 101: goto L_0x002e;
            case 102: goto L_0x0025;
            default: goto L_0x0009;
        };
    L_0x0009:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Cannot interpret '";
        r1.append(r2);
        r1.append(r3);
        r3 = "' as a hexadecimal digit";
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
    L_0x0025:
        r3 = TTTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x002e:
        r3 = TTTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0037:
        r3 = TTFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0040:
        r3 = TTFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0049:
        r3 = TFTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0052:
        r3 = TFTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x005b:
        r3 = TFFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0064:
        r3 = TFFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x006d:
        r3 = FTTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0076:
        r3 = FTTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x007f:
        r3 = FTFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0088:
        r3 = FTFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x0091:
        r3 = FFTT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x009a:
        r3 = FFTF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x00a3:
        r3 = FFFT;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
    L_0x00ac:
        r3 = FFFF;
        r3 = r3.clone();
        r3 = (boolean[]) r3;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.Conversion.hexDigitMsb0ToBinary(char):boolean[]");
    }

    public static char binaryToHexDigit(boolean[] zArr) {
        return binaryToHexDigit(zArr, 0);
    }

    public static char binaryToHexDigit(boolean[] zArr, int i) {
        if (zArr.length == 0) {
            throw new IllegalArgumentException("Cannot convert an empty array.");
        }
        int i2 = i + 3;
        if (zArr.length <= i2 || !zArr[i2]) {
            i2 = i + 2;
            if (zArr.length <= i2 || !zArr[i2]) {
                i2 = i + 1;
                if (zArr.length <= i2 || !zArr[i2]) {
                    return zArr[i] != null ? 49 : 48;
                }
                return zArr[i] != null ? 51 : 50;
            }
            i2 = i + 1;
            if (zArr.length <= i2 || !zArr[i2]) {
                return zArr[i] != null ? 53 : 52;
            }
            return zArr[i] != null ? 55 : 54;
        }
        i2 = i + 2;
        if (zArr.length <= i2 || !zArr[i2]) {
            i2 = i + 1;
            if (zArr.length <= i2 || !zArr[i2]) {
                return zArr[i] != null ? 57 : 56;
            }
            return zArr[i] != null ? 98 : 97;
        }
        i2 = i + 1;
        if (zArr.length <= i2 || !zArr[i2]) {
            return zArr[i] != null ? 100 : 99;
        }
        return zArr[i] != null ? 102 : 101;
    }

    public static char binaryToHexDigitMsb0_4bits(boolean[] zArr) {
        return binaryToHexDigitMsb0_4bits(zArr, 0);
    }

    public static char binaryToHexDigitMsb0_4bits(boolean[] zArr, int i) {
        if (zArr.length > 8) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("src.length>8: src.length=");
            stringBuilder.append(zArr.length);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (zArr.length - i < 4) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("src.length-srcPos<4: src.length=");
            stringBuilder2.append(zArr.length);
            stringBuilder2.append(", srcPos=");
            stringBuilder2.append(i);
            throw new IllegalArgumentException(stringBuilder2.toString());
        } else if (zArr[i + 3]) {
            if (zArr[i + 2]) {
                if (zArr[i + 1]) {
                    return zArr[i] != null ? 102 : 55;
                }
                return zArr[i] != null ? 98 : 51;
            } else if (zArr[i + 1]) {
                return zArr[i] != null ? 100 : 53;
            } else {
                return zArr[i] != null ? 57 : 49;
            }
        } else if (zArr[i + 2]) {
            if (zArr[i + 1]) {
                return zArr[i] != null ? 101 : 54;
            }
            return zArr[i] != null ? 97 : 50;
        } else if (zArr[i + 1]) {
            return zArr[i] != null ? 99 : 52;
        } else {
            return zArr[i] != null ? 56 : 48;
        }
    }

    public static char binaryBeMsb0ToHexDigit(boolean[] zArr) {
        return binaryBeMsb0ToHexDigit(zArr, 0);
    }

    public static char binaryBeMsb0ToHexDigit(boolean[] zArr, int i) {
        if (zArr.length == 0) {
            throw new IllegalArgumentException("Cannot convert an empty array.");
        }
        int length = ((zArr.length - 1) - i) + 1;
        int min = Math.min(4, length);
        Object obj = new boolean[4];
        System.arraycopy(zArr, length - min, obj, 4 - min, min);
        if (obj[null] != null) {
            if (obj.length <= 1 || obj[1] == null) {
                if (obj.length <= 2 || obj[2] == null) {
                    zArr = (obj.length <= 3 || obj[3] == null) ? 56 : 57;
                    return zArr;
                }
                zArr = (obj.length <= 3 || obj[3] == null) ? 97 : 98;
                return zArr;
            } else if (obj.length <= 2 || obj[2] == null) {
                zArr = (obj.length <= 3 || obj[3] == null) ? 99 : 100;
                return zArr;
            } else {
                zArr = (obj.length <= 3 || obj[3] == null) ? 101 : 102;
                return zArr;
            }
        } else if (obj.length <= 1 || obj[1] == null) {
            if (obj.length <= 2 || obj[2] == null) {
                zArr = (obj.length <= 3 || obj[3] == null) ? 48 : 49;
                return zArr;
            }
            zArr = (obj.length <= 3 || obj[3] == null) ? 50 : 51;
            return zArr;
        } else if (obj.length <= 2 || obj[2] == null) {
            zArr = (obj.length <= 3 || obj[3] == null) ? 52 : 53;
            return zArr;
        } else {
            zArr = (obj.length <= 3 || obj[3] == null) ? 54 : 55;
            return zArr;
        }
    }

    public static char intToHexDigit(int i) {
        char forDigit = Character.forDigit(i, 16);
        if (forDigit != '\u0000') {
            return forDigit;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("nibble value not between 0 and 15: ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static char intToHexDigitMsb0(int i) {
        switch (i) {
            case 0:
                return '0';
            case 1:
                return '8';
            case 2:
                return '4';
            case 3:
                return 'c';
            case 4:
                return '2';
            case 5:
                return 'a';
            case 6:
                return '6';
            case 7:
                return 'e';
            case 8:
                return '1';
            case 9:
                return '9';
            case 10:
                return '5';
            case 11:
                return 'd';
            case 12:
                return '3';
            case 13:
                return 'b';
            case 14:
                return '7';
            case 15:
                return 'f';
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("nibble value not between 0 and 15: ");
                stringBuilder.append(i);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static long intArrayToLong(int[] iArr, int i, long j, int i2, int i3) {
        if ((iArr.length == 0 && i == 0) || i3 == 0) {
            return j;
        }
        if (((i3 - 1) * 32) + i2 >= 64) {
            throw new IllegalArgumentException("(nInts-1)*32+dstPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 32) + i2;
            j = (j & ((InternalZipConstants.ZIP_64_LIMIT << i5) ^ -1)) | ((((long) iArr[i4 + i]) & InternalZipConstants.ZIP_64_LIMIT) << i5);
        }
        return j;
    }

    public static long shortArrayToLong(short[] sArr, int i, long j, int i2, int i3) {
        if ((sArr.length == 0 && i == 0) || i3 == 0) {
            return j;
        }
        if (((i3 - 1) * 16) + i2 >= 64) {
            throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 16) + i2;
            j = (j & ((65535 << i5) ^ -1)) | ((((long) sArr[i4 + i]) & 65535) << i5);
        }
        return j;
    }

    public static int shortArrayToInt(short[] sArr, int i, int i2, int i3, int i4) {
        if ((sArr.length == 0 && i == 0) || i4 == 0) {
            return i2;
        }
        if (((i4 - 1) * 16) + i3 >= 32) {
            throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 32");
        }
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = (i5 * 16) + i3;
            i2 = (i2 & ((65535 << i6) ^ -1)) | ((sArr[i5 + i] & 65535) << i6);
        }
        return i2;
    }

    public static long byteArrayToLong(byte[] bArr, int i, long j, int i2, int i3) {
        if ((bArr.length == 0 && i == 0) || i3 == 0) {
            return j;
        }
        if (((i3 - 1) * 8) + i2 >= 64) {
            throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 8) + i2;
            j = (j & ((255 << i5) ^ -1)) | ((((long) bArr[i4 + i]) & 255) << i5);
        }
        return j;
    }

    public static int byteArrayToInt(byte[] bArr, int i, int i2, int i3, int i4) {
        if ((bArr.length == 0 && i == 0) || i4 == 0) {
            return i2;
        }
        if (((i4 - 1) * 8) + i3 >= 32) {
            throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 32");
        }
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = (i5 * 8) + i3;
            i2 = (i2 & ((255 << i6) ^ -1)) | ((bArr[i5 + i] & 255) << i6);
        }
        return i2;
    }

    public static short byteArrayToShort(byte[] bArr, int i, short s, int i2, int i3) {
        if ((bArr.length == 0 && i == 0) || i3 == 0) {
            return s;
        }
        if (((i3 - 1) * 8) + i2 >= 16) {
            throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 16");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 8) + i2;
            s = (short) ((s & ((255 << i5) ^ -1)) | ((bArr[i4 + i] & 255) << i5));
        }
        return s;
    }

    public static long hexToLong(String str, int i, long j, int i2, int i3) {
        if (i3 == 0) {
            return j;
        }
        if (((i3 - 1) * 4) + i2 >= 64) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 4) + i2;
            j = (j & ((15 << i5) ^ -1)) | ((((long) hexDigitToInt(str.charAt(i4 + i))) & 15) << i5);
        }
        return j;
    }

    public static int hexToInt(String str, int i, int i2, int i3, int i4) {
        if (i4 == 0) {
            return i2;
        }
        if (((i4 - 1) * 4) + i3 >= 32) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 32");
        }
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = (i5 * 4) + i3;
            i2 = (i2 & ((15 << i6) ^ -1)) | ((hexDigitToInt(str.charAt(i5 + i)) & 15) << i6);
        }
        return i2;
    }

    public static short hexToShort(String str, int i, short s, int i2, int i3) {
        if (i3 == 0) {
            return s;
        }
        if (((i3 - 1) * 4) + i2 >= 16) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 16");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 4) + i2;
            s = (short) ((s & ((15 << i5) ^ -1)) | ((hexDigitToInt(str.charAt(i4 + i)) & 15) << i5));
        }
        return s;
    }

    public static byte hexToByte(String str, int i, byte b, int i2, int i3) {
        if (i3 == 0) {
            return b;
        }
        if (((i3 - 1) * 4) + i2 >= 8) {
            throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 8");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 4) + i2;
            b = (byte) ((b & ((15 << i5) ^ -1)) | ((hexDigitToInt(str.charAt(i4 + i)) & 15) << i5));
        }
        return b;
    }

    public static long binaryToLong(boolean[] zArr, int i, long j, int i2, int i3) {
        if ((zArr.length == 0 && i == 0) || i3 == 0) {
            return j;
        }
        if ((i3 - 1) + i2 >= 64) {
            throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i4 + i2;
            j = (j & ((1 << i5) ^ -1)) | ((zArr[i4 + i] ? 1 : 0) << i5);
        }
        return j;
    }

    public static int binaryToInt(boolean[] zArr, int i, int i2, int i3, int i4) {
        if ((zArr.length == 0 && i == 0) || i4 == 0) {
            return i2;
        }
        if ((i4 - 1) + i3 >= 32) {
            throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 32");
        }
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = i5 + i3;
            i2 = (i2 & ((1 << i6) ^ -1)) | (zArr[i5 + i] << i6);
        }
        return i2;
    }

    public static short binaryToShort(boolean[] zArr, int i, short s, int i2, int i3) {
        if ((zArr.length == 0 && i == 0) || i3 == 0) {
            return s;
        }
        if ((i3 - 1) + i2 >= 16) {
            throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 16");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i4 + i2;
            s = (short) ((s & ((1 << i5) ^ -1)) | (zArr[i4 + i] << i5));
        }
        return s;
    }

    public static byte binaryToByte(boolean[] zArr, int i, byte b, int i2, int i3) {
        if ((zArr.length == 0 && i == 0) || i3 == 0) {
            return b;
        }
        if ((i3 - 1) + i2 >= 8) {
            throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 8");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i4 + i2;
            b = (byte) ((b & ((1 << i5) ^ -1)) | (zArr[i4 + i] << i5));
        }
        return b;
    }

    public static int[] longToIntArray(long j, int i, int[] iArr, int i2, int i3) {
        if (i3 == 0) {
            return iArr;
        }
        if (((i3 - 1) * 32) + i >= 64) {
            throw new IllegalArgumentException("(nInts-1)*32+srcPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            iArr[i2 + i4] = (int) ((j >> ((i4 * 32) + i)) & -1);
        }
        return iArr;
    }

    public static short[] longToShortArray(long j, int i, short[] sArr, int i2, int i3) {
        if (i3 == 0) {
            return sArr;
        }
        if (((i3 - 1) * 16) + i >= 64) {
            throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            sArr[i2 + i4] = (short) ((int) ((j >> ((i4 * 16) + i)) & 65535));
        }
        return sArr;
    }

    public static short[] intToShortArray(int i, int i2, short[] sArr, int i3, int i4) {
        if (i4 == 0) {
            return sArr;
        }
        if (((i4 - 1) * 16) + i2 >= 32) {
            throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 32");
        }
        for (int i5 = 0; i5 < i4; i5++) {
            sArr[i3 + i5] = (short) ((i >> ((i5 * 16) + i2)) & 65535);
        }
        return sArr;
    }

    public static byte[] longToByteArray(long j, int i, byte[] bArr, int i2, int i3) {
        if (i3 == 0) {
            return bArr;
        }
        if (((i3 - 1) * 8) + i >= 64) {
            throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i2 + i4] = (byte) ((int) ((j >> ((i4 * 8) + i)) & 255));
        }
        return bArr;
    }

    public static byte[] intToByteArray(int i, int i2, byte[] bArr, int i3, int i4) {
        if (i4 == 0) {
            return bArr;
        }
        if (((i4 - 1) * 8) + i2 >= 32) {
            throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 32");
        }
        for (int i5 = 0; i5 < i4; i5++) {
            bArr[i3 + i5] = (byte) ((i >> ((i5 * 8) + i2)) & 255);
        }
        return bArr;
    }

    public static byte[] shortToByteArray(short s, int i, byte[] bArr, int i2, int i3) {
        if (i3 == 0) {
            return bArr;
        }
        if (((i3 - 1) * 8) + i >= 16) {
            throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 16");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i2 + i4] = (byte) ((s >> ((i4 * 8) + i)) & 255);
        }
        return bArr;
    }

    public static String longToHex(long j, int i, String str, int i2, int i3) {
        if (i3 == 0) {
            return str;
        }
        if (((i3 - 1) * 4) + i >= 64) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 64");
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        str = stringBuilder.length();
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (int) ((j >> ((i4 * 4) + i)) & 15);
            int i6 = i2 + i4;
            if (i6 == str) {
                str++;
                stringBuilder.append(intToHexDigit(i5));
            } else {
                stringBuilder.setCharAt(i6, intToHexDigit(i5));
            }
        }
        return stringBuilder.toString();
    }

    public static String intToHex(int i, int i2, String str, int i3, int i4) {
        if (i4 == 0) {
            return str;
        }
        if (((i4 - 1) * 4) + i2 >= 32) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 32");
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        str = stringBuilder.length();
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = (i >> ((i5 * 4) + i2)) & 15;
            int i7 = i3 + i5;
            if (i7 == str) {
                str++;
                stringBuilder.append(intToHexDigit(i6));
            } else {
                stringBuilder.setCharAt(i7, intToHexDigit(i6));
            }
        }
        return stringBuilder.toString();
    }

    public static String shortToHex(short s, int i, String str, int i2, int i3) {
        if (i3 == 0) {
            return str;
        }
        if (((i3 - 1) * 4) + i >= 16) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 16");
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        str = stringBuilder.length();
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (s >> ((i4 * 4) + i)) & 15;
            int i6 = i2 + i4;
            if (i6 == str) {
                str++;
                stringBuilder.append(intToHexDigit(i5));
            } else {
                stringBuilder.setCharAt(i6, intToHexDigit(i5));
            }
        }
        return stringBuilder.toString();
    }

    public static String byteToHex(byte b, int i, String str, int i2, int i3) {
        if (i3 == 0) {
            return str;
        }
        if (((i3 - 1) * 4) + i >= 8) {
            throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 8");
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        str = stringBuilder.length();
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (b >> ((i4 * 4) + i)) & 15;
            int i6 = i2 + i4;
            if (i6 == str) {
                str++;
                stringBuilder.append(intToHexDigit(i5));
            } else {
                stringBuilder.setCharAt(i6, intToHexDigit(i5));
            }
        }
        return stringBuilder.toString();
    }

    public static boolean[] longToBinary(long j, int i, boolean[] zArr, int i2, int i3) {
        if (i3 == 0) {
            return zArr;
        }
        if ((i3 - 1) + i >= 64) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 64");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            zArr[i2 + i4] = ((j >> (i4 + i)) & 1) != 0;
        }
        return zArr;
    }

    public static boolean[] intToBinary(int i, int i2, boolean[] zArr, int i3, int i4) {
        if (i4 == 0) {
            return zArr;
        }
        if ((i4 - 1) + i2 >= 32) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 32");
        }
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = i3 + i5;
            boolean z = true;
            if (((i >> (i5 + i2)) & 1) == 0) {
                z = false;
            }
            zArr[i6] = z;
        }
        return zArr;
    }

    public static boolean[] shortToBinary(short s, int i, boolean[] zArr, int i2, int i3) {
        if (i3 == 0) {
            return zArr;
        }
        if ((i3 - 1) + i >= 16) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 16");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i2 + i4;
            boolean z = true;
            if (((s >> (i4 + i)) & 1) == 0) {
                z = false;
            }
            zArr[i5] = z;
        }
        return zArr;
    }

    public static boolean[] byteToBinary(byte b, int i, boolean[] zArr, int i2, int i3) {
        if (i3 == 0) {
            return zArr;
        }
        if ((i3 - 1) + i >= 8) {
            throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 8");
        }
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i2 + i4;
            boolean z = true;
            if (((b >> (i4 + i)) & 1) == 0) {
                z = false;
            }
            zArr[i5] = z;
        }
        return zArr;
    }

    public static byte[] uuidToByteArray(UUID uuid, byte[] bArr, int i, int i2) {
        if (i2 == 0) {
            return bArr;
        }
        if (i2 > 16) {
            throw new IllegalArgumentException("nBytes is greather than 16");
        }
        longToByteArray(uuid.getMostSignificantBits(), 0, bArr, i, i2 > 8 ? 8 : i2);
        if (i2 >= 8) {
            longToByteArray(uuid.getLeastSignificantBits(), 0, bArr, i + 8, i2 - 8);
        }
        return bArr;
    }

    public static UUID byteArrayToUuid(byte[] bArr, int i) {
        if (bArr.length - i < 16) {
            throw new IllegalArgumentException("Need at least 16 bytes for UUID");
        }
        return new UUID(byteArrayToLong(bArr, i, 0, 0, 8), byteArrayToLong(bArr, i + 8, 0, 0, 8));
    }
}
