package ir.mahdi.mzip.rar.util;

public class VolumeHelper {
    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private VolumeHelper() {
    }

    public static String nextVolumeName(String str, boolean z) {
        StringBuilder stringBuilder;
        int i;
        if (z) {
            z = str.length();
            if (z > true) {
                if (str.charAt(z - 4) == '.') {
                    stringBuilder = new StringBuilder();
                    i = z - 3;
                    stringBuilder.append(str, 0, i);
                    if (isDigit(str.charAt(i + 1))) {
                        if (isDigit(str.charAt(i + 2))) {
                            char[] cArr = new char[3];
                            str.getChars(i, z, cArr, 0);
                            str = cArr.length - 1;
                            while (true) {
                                z = (char) (cArr[str] + 1);
                                cArr[str] = z;
                                if (!z) {
                                    break;
                                }
                                cArr[str] = '0';
                                str--;
                            }
                            stringBuilder.append(cArr);
                            return stringBuilder.toString();
                        }
                    }
                    stringBuilder.append("r00");
                    return stringBuilder.toString();
                }
            }
            return null;
        }
        z = str.length();
        i = z - 1;
        while (i >= 0 && !isDigit(str.charAt(i))) {
            i--;
        }
        int i2 = i + 1;
        int i3 = i - 1;
        while (i3 >= 0 && isDigit(str.charAt(i3))) {
            i3--;
        }
        if (i3 < 0) {
            return null;
        }
        i3++;
        stringBuilder = new StringBuilder(z);
        stringBuilder.append(str, 0, i3);
        char[] cArr2 = new char[((i - i3) + 1)];
        str.getChars(i3, i2, cArr2, 0);
        int length = cArr2.length - 1;
        while (length >= 0) {
            char c = (char) (cArr2[length] + 1);
            cArr2[length] = c;
            if (c != ':') {
                break;
            }
            cArr2[length] = '0';
            length--;
        }
        if (length < 0) {
            stringBuilder.append('1');
        }
        stringBuilder.append(cArr2);
        stringBuilder.append(str, i2, z);
        return stringBuilder.toString();
    }
}
