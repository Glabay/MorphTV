package de.timroes.base64;

import java.util.HashMap;

public class Base64 {
    private static final char[] code = "=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final HashMap<Character, Byte> map = new HashMap();

    static {
        for (int i = 0; i < code.length; i++) {
            map.put(Character.valueOf(code[i]), Byte.valueOf((byte) i));
        }
    }

    public static byte[] decode(String str) {
        str = str.replaceAll("\\r|\\n", "");
        if (str.length() % 4 != 0) {
            throw new IllegalArgumentException("The length of the input string must be a multiple of four.");
        } else if (str.matches("^[A-Za-z0-9+/]*[=]{0,3}$")) {
            Object obj = new byte[((str.length() * 3) / 4)];
            char[] toCharArray = str.toCharArray();
            int i = 0;
            int i2 = 0;
            while (i < toCharArray.length) {
                int byteValue = ((Byte) map.get(Character.valueOf(toCharArray[i + 1]))).byteValue() - 1;
                int byteValue2 = ((Byte) map.get(Character.valueOf(toCharArray[i + 2]))).byteValue() - 1;
                int byteValue3 = ((Byte) map.get(Character.valueOf(toCharArray[i + 3]))).byteValue() - 1;
                int i3 = i2 + 1;
                obj[i2] = (byte) (((((Byte) map.get(Character.valueOf(toCharArray[i]))).byteValue() - 1) << 2) | (byteValue >>> 4));
                i2 = i3 + 1;
                obj[i3] = (byte) (((byteValue & 15) << 4) | (byteValue2 >>> 2));
                int i4 = i2 + 1;
                obj[i2] = (byte) (((byteValue2 & 3) << 6) | (byteValue3 & 63));
                i += 4;
                i2 = i4;
            }
            if (!str.endsWith("=")) {
                return obj;
            }
            str = new byte[(obj.length - (str.length() - str.indexOf("=")))];
            System.arraycopy(obj, 0, str, 0, str.length);
            return str;
        } else {
            throw new IllegalArgumentException("The argument contains illegal characters.");
        }
    }

    public static String decodeAsString(String str) {
        return new String(decode(str));
    }

    public static String encode(String str) {
        return encode(str.getBytes());
    }

    public static String encode(Byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr2.length; i++) {
            bArr2[i] = bArr[i].byteValue();
        }
        return encode(bArr2);
    }

    public static String encode(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(((bArr.length + 2) / 3) * 4);
        bArr = encodeAsBytes(bArr);
        for (int i = 0; i < bArr.length; i++) {
            stringBuilder.append(code[bArr[i] + 1]);
            if (i % 72 == 71) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public static byte[] encodeAsBytes(String str) {
        return encodeAsBytes(str.getBytes());
    }

    public static byte[] encodeAsBytes(byte[] bArr) {
        byte[] bArr2 = new byte[(((bArr.length + 2) / 3) * 4)];
        Object obj = new byte[(((bArr.length + 2) / 3) * 3)];
        int i = 0;
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        int i2 = 0;
        while (i < obj.length) {
            int i3 = i2 + 1;
            bArr2[i2] = (byte) ((obj[i] & 255) >>> 2);
            i2 = i3 + 1;
            int i4 = i + 1;
            bArr2[i3] = (byte) (((obj[i] & 3) << 4) | ((obj[i4] & 255) >>> 4));
            i3 = i2 + 1;
            i4 = i + 2;
            bArr2[i2] = (byte) (((obj[i4] & 15) << 2) | ((obj[i4] & 255) >>> 6));
            i2 = i3 + 1;
            bArr2[i3] = (byte) (obj[i4] & 63);
            i += 3;
        }
        for (int length = obj.length - bArr.length; length > 0; length--) {
            bArr2[bArr2.length - length] = (byte) -1;
        }
        return bArr2;
    }
}
