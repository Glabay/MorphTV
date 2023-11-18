package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class NumberUtils {
    public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte) -1);
    public static final Byte BYTE_ONE = Byte.valueOf((byte) 1);
    public static final Byte BYTE_ZERO = Byte.valueOf((byte) 0);
    public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0d);
    public static final Double DOUBLE_ONE = Double.valueOf(1.0d);
    public static final Double DOUBLE_ZERO = Double.valueOf(0.0d);
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0f);
    public static final Float FLOAT_ONE = Float.valueOf(1.0f);
    public static final Float FLOAT_ZERO = Float.valueOf(0.0f);
    public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
    public static final Integer INTEGER_ONE = Integer.valueOf(1);
    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    public static final Long LONG_MINUS_ONE = Long.valueOf(-1);
    public static final Long LONG_ONE = Long.valueOf(1);
    public static final Long LONG_ZERO = Long.valueOf(0);
    public static final Short SHORT_MINUS_ONE = Short.valueOf((short) -1);
    public static final Short SHORT_ONE = Short.valueOf((short) 1);
    public static final Short SHORT_ZERO = Short.valueOf((short) 0);

    public static int compare(byte b, byte b2) {
        return b - b2;
    }

    public static int compare(int i, int i2) {
        if (i == i2) {
            return 0;
        }
        return i < i2 ? -1 : 1;
    }

    public static int compare(long j, long j2) {
        if (j == j2) {
            return 0;
        }
        return j < j2 ? -1 : 1;
    }

    public static int compare(short s, short s2) {
        if (s == s2) {
            return 0;
        }
        return s < s2 ? -1 : 1;
    }

    public static byte max(byte b, byte b2, byte b3) {
        if (b2 > b) {
            b = b2;
        }
        return b3 > b ? b3 : b;
    }

    public static int max(int i, int i2, int i3) {
        if (i2 > i) {
            i = i2;
        }
        return i3 > i ? i3 : i;
    }

    public static long max(long j, long j2, long j3) {
        if (j2 > j) {
            j = j2;
        }
        return j3 > j ? j3 : j;
    }

    public static short max(short s, short s2, short s3) {
        if (s2 > s) {
            s = s2;
        }
        return s3 > s ? s3 : s;
    }

    public static byte min(byte b, byte b2, byte b3) {
        if (b2 < b) {
            b = b2;
        }
        return b3 < b ? b3 : b;
    }

    public static int min(int i, int i2, int i3) {
        if (i2 < i) {
            i = i2;
        }
        return i3 < i ? i3 : i;
    }

    public static long min(long j, long j2, long j3) {
        if (j2 < j) {
            j = j2;
        }
        return j3 < j ? j3 : j;
    }

    public static short min(short s, short s2, short s3) {
        if (s2 < s) {
            s = s2;
        }
        return s3 < s ? s3 : s;
    }

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int toInt(java.lang.String r0, int r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r0 != 0) goto L_0x0003;
    L_0x0002:
        return r1;
    L_0x0003:
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x0008 }
        return r0;
    L_0x0008:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.toInt(java.lang.String, int):int");
    }

    public static long toLong(String str) {
        return toLong(str, 0);
    }

    public static long toLong(java.lang.String r2, long r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r2 != 0) goto L_0x0003;
    L_0x0002:
        return r3;
    L_0x0003:
        r0 = java.lang.Long.parseLong(r2);	 Catch:{ NumberFormatException -> 0x0008 }
        return r0;
    L_0x0008:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.toLong(java.lang.String, long):long");
    }

    public static float toFloat(String str) {
        return toFloat(str, 0.0f);
    }

    public static float toFloat(java.lang.String r0, float r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r0 != 0) goto L_0x0003;
    L_0x0002:
        return r1;
    L_0x0003:
        r0 = java.lang.Float.parseFloat(r0);	 Catch:{ NumberFormatException -> 0x0008 }
        return r0;
    L_0x0008:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.toFloat(java.lang.String, float):float");
    }

    public static double toDouble(String str) {
        return toDouble(str, 0.0d);
    }

    public static double toDouble(java.lang.String r2, double r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r2 != 0) goto L_0x0003;
    L_0x0002:
        return r3;
    L_0x0003:
        r0 = java.lang.Double.parseDouble(r2);	 Catch:{ NumberFormatException -> 0x0008 }
        return r0;
    L_0x0008:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.toDouble(java.lang.String, double):double");
    }

    public static byte toByte(String str) {
        return toByte(str, (byte) 0);
    }

    public static byte toByte(java.lang.String r0, byte r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r0 != 0) goto L_0x0003;
    L_0x0002:
        return r1;
    L_0x0003:
        r0 = java.lang.Byte.parseByte(r0);	 Catch:{ NumberFormatException -> 0x0008 }
        return r0;
    L_0x0008:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.toByte(java.lang.String, byte):byte");
    }

    public static short toShort(String str) {
        return toShort(str, (short) 0);
    }

    public static short toShort(java.lang.String r0, short r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r0 != 0) goto L_0x0003;
    L_0x0002:
        return r1;
    L_0x0003:
        r0 = java.lang.Short.parseShort(r0);	 Catch:{ NumberFormatException -> 0x0008 }
        return r0;
    L_0x0008:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.toShort(java.lang.String, short):short");
    }

    public static java.lang.Number createNumber(java.lang.String r13) throws java.lang.NumberFormatException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r13 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = org.apache.commons.lang3.StringUtils.isBlank(r13);
        if (r1 == 0) goto L_0x0012;
    L_0x000a:
        r13 = new java.lang.NumberFormatException;
        r0 = "A blank string is not a valid number";
        r13.<init>(r0);
        throw r13;
    L_0x0012:
        r1 = "0x";
        r2 = "0X";
        r3 = "-0x";
        r4 = "-0X";
        r5 = "#";
        r6 = "-#";
        r1 = new java.lang.String[]{r1, r2, r3, r4, r5, r6};
        r2 = r1.length;
        r3 = 0;
        r4 = 0;
    L_0x0025:
        if (r4 >= r2) goto L_0x0038;
    L_0x0027:
        r5 = r1[r4];
        r6 = r13.startsWith(r5);
        if (r6 == 0) goto L_0x0035;
    L_0x002f:
        r1 = r5.length();
        r1 = r1 + r3;
        goto L_0x0039;
    L_0x0035:
        r4 = r4 + 1;
        goto L_0x0025;
    L_0x0038:
        r1 = 0;
    L_0x0039:
        if (r1 <= 0) goto L_0x0077;
    L_0x003b:
        r0 = r1;
    L_0x003c:
        r2 = r13.length();
        if (r1 >= r2) goto L_0x004f;
    L_0x0042:
        r3 = r13.charAt(r1);
        r2 = 48;
        if (r3 != r2) goto L_0x004f;
    L_0x004a:
        r0 = r0 + 1;
        r1 = r1 + 1;
        goto L_0x003c;
    L_0x004f:
        r1 = r13.length();
        r1 = r1 - r0;
        r0 = 16;
        if (r1 > r0) goto L_0x0072;
    L_0x0058:
        r2 = 55;
        if (r1 != r0) goto L_0x005f;
    L_0x005c:
        if (r3 <= r2) goto L_0x005f;
    L_0x005e:
        goto L_0x0072;
    L_0x005f:
        r0 = 8;
        if (r1 > r0) goto L_0x006d;
    L_0x0063:
        if (r1 != r0) goto L_0x0068;
    L_0x0065:
        if (r3 <= r2) goto L_0x0068;
    L_0x0067:
        goto L_0x006d;
    L_0x0068:
        r13 = createInteger(r13);
        return r13;
    L_0x006d:
        r13 = createLong(r13);
        return r13;
    L_0x0072:
        r13 = createBigInteger(r13);
        return r13;
    L_0x0077:
        r1 = r13.length();
        r2 = 1;
        r1 = r1 - r2;
        r1 = r13.charAt(r1);
        r4 = 46;
        r5 = r13.indexOf(r4);
        r6 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r6 = r13.indexOf(r6);
        r7 = 69;
        r7 = r13.indexOf(r7);
        r6 = r6 + r7;
        r6 = r6 + r2;
        r7 = -1;
        if (r5 <= r7) goto L_0x00cc;
    L_0x0098:
        if (r6 <= r7) goto L_0x00c1;
    L_0x009a:
        if (r6 < r5) goto L_0x00aa;
    L_0x009c:
        r8 = r13.length();
        if (r6 <= r8) goto L_0x00a3;
    L_0x00a2:
        goto L_0x00aa;
    L_0x00a3:
        r8 = r5 + 1;
        r8 = r13.substring(r8, r6);
        goto L_0x00c7;
    L_0x00aa:
        r0 = new java.lang.NumberFormatException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r13);
        r13 = " is not a valid number.";
        r1.append(r13);
        r13 = r1.toString();
        r0.<init>(r13);
        throw r0;
    L_0x00c1:
        r8 = r5 + 1;
        r8 = r13.substring(r8);
    L_0x00c7:
        r5 = getMantissa(r13, r5);
        goto L_0x00f5;
    L_0x00cc:
        if (r6 <= r7) goto L_0x00f0;
    L_0x00ce:
        r5 = r13.length();
        if (r6 <= r5) goto L_0x00eb;
    L_0x00d4:
        r0 = new java.lang.NumberFormatException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r13);
        r13 = " is not a valid number.";
        r1.append(r13);
        r13 = r1.toString();
        r0.<init>(r13);
        throw r0;
    L_0x00eb:
        r5 = getMantissa(r13, r6);
        goto L_0x00f4;
    L_0x00f0:
        r5 = getMantissa(r13);
    L_0x00f4:
        r8 = r0;
    L_0x00f5:
        r9 = java.lang.Character.isDigit(r1);
        r10 = 0;
        r12 = 0;
        if (r9 != 0) goto L_0x01c8;
    L_0x00fe:
        if (r1 == r4) goto L_0x01c8;
    L_0x0100:
        if (r6 <= r7) goto L_0x0113;
    L_0x0102:
        r4 = r13.length();
        r4 = r4 - r2;
        if (r6 >= r4) goto L_0x0113;
    L_0x0109:
        r6 = r6 + r2;
        r0 = r13.length();
        r0 = r0 - r2;
        r0 = r13.substring(r6, r0);
    L_0x0113:
        r4 = r13.length();
        r4 = r4 - r2;
        r4 = r13.substring(r3, r4);
        r5 = isAllZeros(r5);
        if (r5 == 0) goto L_0x012a;
    L_0x0122:
        r5 = isAllZeros(r0);
        if (r5 == 0) goto L_0x012a;
    L_0x0128:
        r5 = 1;
        goto L_0x012b;
    L_0x012a:
        r5 = 0;
    L_0x012b:
        r6 = 68;
        if (r1 == r6) goto L_0x0196;
    L_0x012f:
        r6 = 70;
        if (r1 == r6) goto L_0x0181;
    L_0x0133:
        r6 = 76;
        if (r1 == r6) goto L_0x0144;
    L_0x0137:
        r6 = 100;
        if (r1 == r6) goto L_0x0196;
    L_0x013b:
        r6 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r1 == r6) goto L_0x0181;
    L_0x013f:
        r5 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        if (r1 == r5) goto L_0x0144;
    L_0x0143:
        goto L_0x01b1;
    L_0x0144:
        if (r8 != 0) goto L_0x016a;
    L_0x0146:
        if (r0 != 0) goto L_0x016a;
    L_0x0148:
        r0 = r4.charAt(r3);
        r1 = 45;
        if (r0 != r1) goto L_0x015a;
    L_0x0150:
        r0 = r4.substring(r2);
        r0 = isDigits(r0);
        if (r0 != 0) goto L_0x0160;
    L_0x015a:
        r0 = isDigits(r4);
        if (r0 == 0) goto L_0x016a;
    L_0x0160:
        r13 = createLong(r4);	 Catch:{ NumberFormatException -> 0x0165 }
        return r13;
    L_0x0165:
        r13 = createBigInteger(r4);
        return r13;
    L_0x016a:
        r0 = new java.lang.NumberFormatException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r13);
        r13 = " is not a valid number.";
        r1.append(r13);
        r13 = r1.toString();
        r0.<init>(r13);
        throw r0;
    L_0x0181:
        r0 = createFloat(r13);	 Catch:{ NumberFormatException -> 0x0196 }
        r1 = r0.isInfinite();	 Catch:{ NumberFormatException -> 0x0196 }
        if (r1 != 0) goto L_0x0196;	 Catch:{ NumberFormatException -> 0x0196 }
    L_0x018b:
        r1 = r0.floatValue();	 Catch:{ NumberFormatException -> 0x0196 }
        r1 = (r1 > r12 ? 1 : (r1 == r12 ? 0 : -1));
        if (r1 != 0) goto L_0x0195;
    L_0x0193:
        if (r5 == 0) goto L_0x0196;
    L_0x0195:
        return r0;
    L_0x0196:
        r0 = createDouble(r13);	 Catch:{ NumberFormatException -> 0x01ac }
        r1 = r0.isInfinite();	 Catch:{ NumberFormatException -> 0x01ac }
        if (r1 != 0) goto L_0x01ac;	 Catch:{ NumberFormatException -> 0x01ac }
    L_0x01a0:
        r1 = r0.floatValue();	 Catch:{ NumberFormatException -> 0x01ac }
        r1 = (double) r1;
        r3 = (r1 > r10 ? 1 : (r1 == r10 ? 0 : -1));
        if (r3 != 0) goto L_0x01ab;
    L_0x01a9:
        if (r5 == 0) goto L_0x01ac;
    L_0x01ab:
        return r0;
    L_0x01ac:
        r0 = createBigDecimal(r4);	 Catch:{ NumberFormatException -> 0x01b1 }
        return r0;
    L_0x01b1:
        r0 = new java.lang.NumberFormatException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r13);
        r13 = " is not a valid number.";
        r1.append(r13);
        r13 = r1.toString();
        r0.<init>(r13);
        throw r0;
    L_0x01c8:
        if (r6 <= r7) goto L_0x01da;
    L_0x01ca:
        r1 = r13.length();
        r1 = r1 - r2;
        if (r6 >= r1) goto L_0x01da;
    L_0x01d1:
        r6 = r6 + r2;
        r0 = r13.length();
        r0 = r13.substring(r6, r0);
    L_0x01da:
        if (r8 != 0) goto L_0x01ed;
    L_0x01dc:
        if (r0 != 0) goto L_0x01ed;
    L_0x01de:
        r0 = createInteger(r13);	 Catch:{ NumberFormatException -> 0x01e3 }
        return r0;
    L_0x01e3:
        r0 = createLong(r13);	 Catch:{ NumberFormatException -> 0x01e8 }
        return r0;
    L_0x01e8:
        r13 = createBigInteger(r13);
        return r13;
    L_0x01ed:
        r1 = isAllZeros(r5);
        if (r1 == 0) goto L_0x01fa;
    L_0x01f3:
        r0 = isAllZeros(r0);
        if (r0 == 0) goto L_0x01fa;
    L_0x01f9:
        goto L_0x01fb;
    L_0x01fa:
        r2 = 0;
    L_0x01fb:
        r0 = createFloat(r13);	 Catch:{ NumberFormatException -> 0x0246 }
        r1 = createDouble(r13);	 Catch:{ NumberFormatException -> 0x0246 }
        r3 = r0.isInfinite();	 Catch:{ NumberFormatException -> 0x0246 }
        if (r3 != 0) goto L_0x0222;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0209:
        r3 = r0.floatValue();	 Catch:{ NumberFormatException -> 0x0246 }
        r3 = (r3 > r12 ? 1 : (r3 == r12 ? 0 : -1));	 Catch:{ NumberFormatException -> 0x0246 }
        if (r3 != 0) goto L_0x0213;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0211:
        if (r2 == 0) goto L_0x0222;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0213:
        r3 = r0.toString();	 Catch:{ NumberFormatException -> 0x0246 }
        r4 = r1.toString();	 Catch:{ NumberFormatException -> 0x0246 }
        r3 = r3.equals(r4);	 Catch:{ NumberFormatException -> 0x0246 }
        if (r3 == 0) goto L_0x0222;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0221:
        return r0;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0222:
        r0 = r1.isInfinite();	 Catch:{ NumberFormatException -> 0x0246 }
        if (r0 != 0) goto L_0x0246;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0228:
        r3 = r1.doubleValue();	 Catch:{ NumberFormatException -> 0x0246 }
        r0 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1));	 Catch:{ NumberFormatException -> 0x0246 }
        if (r0 != 0) goto L_0x0232;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0230:
        if (r2 == 0) goto L_0x0246;	 Catch:{ NumberFormatException -> 0x0246 }
    L_0x0232:
        r0 = createBigDecimal(r13);	 Catch:{ NumberFormatException -> 0x0246 }
        r2 = r1.doubleValue();	 Catch:{ NumberFormatException -> 0x0246 }
        r2 = java.math.BigDecimal.valueOf(r2);	 Catch:{ NumberFormatException -> 0x0246 }
        r2 = r0.compareTo(r2);	 Catch:{ NumberFormatException -> 0x0246 }
        if (r2 != 0) goto L_0x0245;
    L_0x0244:
        return r1;
    L_0x0245:
        return r0;
    L_0x0246:
        r13 = createBigDecimal(r13);
        return r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.createNumber(java.lang.String):java.lang.Number");
    }

    private static String getMantissa(String str) {
        return getMantissa(str, str.length());
    }

    private static String getMantissa(String str, int i) {
        Object obj;
        char charAt = str.charAt(0);
        if (charAt != '-') {
            if (charAt != '+') {
                obj = null;
                return obj == null ? str.substring(1, i) : str.substring(0, i);
            }
        }
        obj = 1;
        if (obj == null) {
        }
    }

    private static boolean isAllZeros(String str) {
        boolean z = true;
        if (str == null) {
            return true;
        }
        for (int length = str.length() - 1; length >= 0; length--) {
            if (str.charAt(length) != '0') {
                return false;
            }
        }
        if (str.length() <= null) {
            z = false;
        }
        return z;
    }

    public static Float createFloat(String str) {
        return str == null ? null : Float.valueOf(str);
    }

    public static Double createDouble(String str) {
        return str == null ? null : Double.valueOf(str);
    }

    public static Integer createInteger(String str) {
        return str == null ? null : Integer.decode(str);
    }

    public static Long createLong(String str) {
        return str == null ? null : Long.decode(str);
    }

    public static BigInteger createBigInteger(String str) {
        if (str == null) {
            return null;
        }
        BigInteger bigInteger;
        int i = 10;
        int i2 = 0;
        Object obj = 1;
        if (str.startsWith("-")) {
            i2 = 1;
        } else {
            obj = null;
        }
        if (!str.startsWith("0x", i2)) {
            if (!str.startsWith("0X", i2)) {
                if (str.startsWith("#", i2)) {
                    i2++;
                    i = 16;
                    bigInteger = new BigInteger(str.substring(i2), i);
                    if (obj != null) {
                        bigInteger = bigInteger.negate();
                    }
                    return bigInteger;
                }
                if (str.startsWith("0", i2)) {
                    int i3 = i2 + 1;
                    if (str.length() > i3) {
                        i = 8;
                        i2 = i3;
                    }
                }
                bigInteger = new BigInteger(str.substring(i2), i);
                if (obj != null) {
                    bigInteger = bigInteger.negate();
                }
                return bigInteger;
            }
        }
        i2 += 2;
        i = 16;
        bigInteger = new BigInteger(str.substring(i2), i);
        if (obj != null) {
            bigInteger = bigInteger.negate();
        }
        return bigInteger;
    }

    public static BigDecimal createBigDecimal(String str) {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        } else if (!str.trim().startsWith("--")) {
            return new BigDecimal(str);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(" is not a valid number.");
            throw new NumberFormatException(stringBuilder.toString());
        }
    }

    public static long min(long... jArr) {
        validateArray(jArr);
        long j = jArr[0];
        for (int i = 1; i < jArr.length; i++) {
            if (jArr[i] < j) {
                j = jArr[i];
            }
        }
        return j;
    }

    public static int min(int... iArr) {
        validateArray(iArr);
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] < i) {
                i = iArr[i2];
            }
        }
        return i;
    }

    public static short min(short... sArr) {
        validateArray(sArr);
        short s = sArr[0];
        for (int i = 1; i < sArr.length; i++) {
            if (sArr[i] < s) {
                s = sArr[i];
            }
        }
        return s;
    }

    public static byte min(byte... bArr) {
        validateArray(bArr);
        byte b = bArr[0];
        for (int i = 1; i < bArr.length; i++) {
            if (bArr[i] < b) {
                b = bArr[i];
            }
        }
        return b;
    }

    public static double min(double... dArr) {
        validateArray(dArr);
        double d = dArr[0];
        for (int i = 1; i < dArr.length; i++) {
            if (Double.isNaN(dArr[i])) {
                return Double.NaN;
            }
            if (dArr[i] < d) {
                d = dArr[i];
            }
        }
        return d;
    }

    public static float min(float... fArr) {
        validateArray(fArr);
        float f = fArr[0];
        for (int i = 1; i < fArr.length; i++) {
            if (Float.isNaN(fArr[i])) {
                return 2143289344;
            }
            if (fArr[i] < f) {
                f = fArr[i];
            }
        }
        return f;
    }

    public static long max(long... jArr) {
        validateArray(jArr);
        long j = jArr[0];
        for (int i = 1; i < jArr.length; i++) {
            if (jArr[i] > j) {
                j = jArr[i];
            }
        }
        return j;
    }

    public static int max(int... iArr) {
        validateArray(iArr);
        int i = iArr[0];
        for (int i2 = 1; i2 < iArr.length; i2++) {
            if (iArr[i2] > i) {
                i = iArr[i2];
            }
        }
        return i;
    }

    public static short max(short... sArr) {
        validateArray(sArr);
        short s = sArr[0];
        for (int i = 1; i < sArr.length; i++) {
            if (sArr[i] > s) {
                s = sArr[i];
            }
        }
        return s;
    }

    public static byte max(byte... bArr) {
        validateArray(bArr);
        byte b = bArr[0];
        for (int i = 1; i < bArr.length; i++) {
            if (bArr[i] > b) {
                b = bArr[i];
            }
        }
        return b;
    }

    public static double max(double... dArr) {
        validateArray(dArr);
        double d = dArr[0];
        for (int i = 1; i < dArr.length; i++) {
            if (Double.isNaN(dArr[i])) {
                return Double.NaN;
            }
            if (dArr[i] > d) {
                d = dArr[i];
            }
        }
        return d;
    }

    public static float max(float... fArr) {
        validateArray(fArr);
        float f = fArr[0];
        for (int i = 1; i < fArr.length; i++) {
            if (Float.isNaN(fArr[i])) {
                return 2143289344;
            }
            if (fArr[i] > f) {
                f = fArr[i];
            }
        }
        return f;
    }

    private static void validateArray(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        Validate.isTrue(Array.getLength(obj) != null ? true : null, "Array cannot be empty.", new Object[0]);
    }

    public static double min(double d, double d2, double d3) {
        return Math.min(Math.min(d, d2), d3);
    }

    public static float min(float f, float f2, float f3) {
        return Math.min(Math.min(f, f2), f3);
    }

    public static double max(double d, double d2, double d3) {
        return Math.max(Math.max(d, d2), d3);
    }

    public static float max(float f, float f2, float f3) {
        return Math.max(Math.max(f, f2), f3);
    }

    public static boolean isDigits(String str) {
        return StringUtils.isNumeric(str);
    }

    @Deprecated
    public static boolean isNumber(String str) {
        return isCreatable(str);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isCreatable(java.lang.String r18) {
        /*
        r0 = org.apache.commons.lang3.StringUtils.isEmpty(r18);
        r1 = 0;
        if (r0 == 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = r18.toCharArray();
        r2 = r0.length;
        r3 = r0[r1];
        r4 = 45;
        r5 = 43;
        r6 = 1;
        if (r3 == r4) goto L_0x001d;
    L_0x0016:
        r3 = r0[r1];
        if (r3 != r5) goto L_0x001b;
    L_0x001a:
        goto L_0x001d;
    L_0x001b:
        r3 = 0;
        goto L_0x001e;
    L_0x001d:
        r3 = 1;
    L_0x001e:
        if (r3 != r6) goto L_0x0026;
    L_0x0020:
        r7 = r0[r1];
        if (r7 != r5) goto L_0x0026;
    L_0x0024:
        r7 = 1;
        goto L_0x0027;
    L_0x0026:
        r7 = 0;
    L_0x0027:
        r8 = r3 + 1;
        r9 = 70;
        r10 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r11 = 57;
        r12 = 48;
        if (r2 <= r8) goto L_0x0089;
    L_0x0033:
        r13 = r0[r3];
        if (r13 != r12) goto L_0x0089;
    L_0x0037:
        r13 = r0[r8];
        r14 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        if (r13 == r14) goto L_0x005f;
    L_0x003d:
        r13 = r0[r8];
        r14 = 88;
        if (r13 != r14) goto L_0x0044;
    L_0x0043:
        goto L_0x005f;
    L_0x0044:
        r13 = r0[r8];
        r13 = java.lang.Character.isDigit(r13);
        if (r13 == 0) goto L_0x0089;
    L_0x004c:
        r2 = r0.length;
        if (r8 >= r2) goto L_0x005e;
    L_0x004f:
        r2 = r0[r8];
        if (r2 < r12) goto L_0x005d;
    L_0x0053:
        r2 = r0[r8];
        r3 = 55;
        if (r2 <= r3) goto L_0x005a;
    L_0x0059:
        goto L_0x005d;
    L_0x005a:
        r8 = r8 + 1;
        goto L_0x004c;
    L_0x005d:
        return r1;
    L_0x005e:
        return r6;
    L_0x005f:
        r3 = r3 + 2;
        if (r3 != r2) goto L_0x0064;
    L_0x0063:
        return r1;
    L_0x0064:
        r2 = r0.length;
        if (r3 >= r2) goto L_0x0088;
    L_0x0067:
        r2 = r0[r3];
        if (r2 < r12) goto L_0x006f;
    L_0x006b:
        r2 = r0[r3];
        if (r2 <= r11) goto L_0x0084;
    L_0x006f:
        r2 = r0[r3];
        r4 = 97;
        if (r2 < r4) goto L_0x0079;
    L_0x0075:
        r2 = r0[r3];
        if (r2 <= r10) goto L_0x0084;
    L_0x0079:
        r2 = r0[r3];
        r4 = 65;
        if (r2 < r4) goto L_0x0087;
    L_0x007f:
        r2 = r0[r3];
        if (r2 <= r9) goto L_0x0084;
    L_0x0083:
        goto L_0x0087;
    L_0x0084:
        r3 = r3 + 1;
        goto L_0x0064;
    L_0x0087:
        return r1;
    L_0x0088:
        return r6;
    L_0x0089:
        r2 = r2 + -1;
        r8 = 0;
        r13 = 0;
        r14 = 0;
        r15 = 0;
    L_0x008f:
        r4 = 46;
        r5 = 69;
        r9 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        if (r3 < r2) goto L_0x0104;
    L_0x0097:
        r10 = r2 + 1;
        if (r3 >= r10) goto L_0x00a3;
    L_0x009b:
        if (r8 == 0) goto L_0x00a3;
    L_0x009d:
        if (r13 != 0) goto L_0x00a3;
    L_0x009f:
        r10 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        goto L_0x0104;
    L_0x00a3:
        r2 = r0.length;
        if (r3 >= r2) goto L_0x00fe;
    L_0x00a6:
        r2 = r0[r3];
        if (r2 < r12) goto L_0x00b8;
    L_0x00aa:
        r2 = r0[r3];
        if (r2 > r11) goto L_0x00b8;
    L_0x00ae:
        r0 = org.apache.commons.lang3.SystemUtils.IS_JAVA_1_6;
        if (r0 == 0) goto L_0x00b7;
    L_0x00b2:
        if (r7 == 0) goto L_0x00b7;
    L_0x00b4:
        if (r14 != 0) goto L_0x00b7;
    L_0x00b6:
        return r1;
    L_0x00b7:
        return r6;
    L_0x00b8:
        r2 = r0[r3];
        if (r2 == r9) goto L_0x00fd;
    L_0x00bc:
        r2 = r0[r3];
        if (r2 != r5) goto L_0x00c1;
    L_0x00c0:
        goto L_0x00fd;
    L_0x00c1:
        r2 = r0[r3];
        if (r2 != r4) goto L_0x00cc;
    L_0x00c5:
        if (r14 != 0) goto L_0x00cb;
    L_0x00c7:
        if (r15 == 0) goto L_0x00ca;
    L_0x00c9:
        goto L_0x00cb;
    L_0x00ca:
        return r13;
    L_0x00cb:
        return r1;
    L_0x00cc:
        if (r8 != 0) goto L_0x00e7;
    L_0x00ce:
        r2 = r0[r3];
        r4 = 100;
        if (r2 == r4) goto L_0x00e6;
    L_0x00d4:
        r2 = r0[r3];
        r4 = 68;
        if (r2 == r4) goto L_0x00e6;
    L_0x00da:
        r2 = r0[r3];
        r10 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r2 == r10) goto L_0x00e6;
    L_0x00e0:
        r2 = r0[r3];
        r4 = 70;
        if (r2 != r4) goto L_0x00e7;
    L_0x00e6:
        return r13;
    L_0x00e7:
        r2 = r0[r3];
        r4 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        if (r2 == r4) goto L_0x00f5;
    L_0x00ed:
        r0 = r0[r3];
        r2 = 76;
        if (r0 != r2) goto L_0x00f4;
    L_0x00f3:
        goto L_0x00f5;
    L_0x00f4:
        return r1;
    L_0x00f5:
        if (r13 == 0) goto L_0x00fc;
    L_0x00f7:
        if (r15 != 0) goto L_0x00fc;
    L_0x00f9:
        if (r14 != 0) goto L_0x00fc;
    L_0x00fb:
        r1 = 1;
    L_0x00fc:
        return r1;
    L_0x00fd:
        return r1;
    L_0x00fe:
        if (r8 != 0) goto L_0x0103;
    L_0x0100:
        if (r13 == 0) goto L_0x0103;
    L_0x0102:
        r1 = 1;
    L_0x0103:
        return r1;
    L_0x0104:
        r16 = 70;
        r6 = r0[r3];
        if (r6 < r12) goto L_0x0115;
    L_0x010a:
        r6 = r0[r3];
        if (r6 > r11) goto L_0x0115;
    L_0x010e:
        r5 = 43;
        r6 = 45;
        r8 = 0;
        r13 = 1;
        goto L_0x0150;
    L_0x0115:
        r6 = r0[r3];
        if (r6 != r4) goto L_0x0125;
    L_0x0119:
        if (r14 != 0) goto L_0x0124;
    L_0x011b:
        if (r15 == 0) goto L_0x011e;
    L_0x011d:
        goto L_0x0124;
    L_0x011e:
        r5 = 43;
        r6 = 45;
        r14 = 1;
        goto L_0x0150;
    L_0x0124:
        return r1;
    L_0x0125:
        r4 = r0[r3];
        if (r4 == r9) goto L_0x0144;
    L_0x0129:
        r4 = r0[r3];
        if (r4 != r5) goto L_0x012e;
    L_0x012d:
        goto L_0x0144;
    L_0x012e:
        r4 = r0[r3];
        r5 = 43;
        if (r4 == r5) goto L_0x013c;
    L_0x0134:
        r4 = r0[r3];
        r6 = 45;
        if (r4 != r6) goto L_0x013b;
    L_0x013a:
        goto L_0x013e;
    L_0x013b:
        return r1;
    L_0x013c:
        r6 = 45;
    L_0x013e:
        if (r8 != 0) goto L_0x0141;
    L_0x0140:
        return r1;
    L_0x0141:
        r8 = 0;
        r13 = 0;
        goto L_0x0150;
    L_0x0144:
        r5 = 43;
        r6 = 45;
        if (r15 == 0) goto L_0x014b;
    L_0x014a:
        return r1;
    L_0x014b:
        if (r13 != 0) goto L_0x014e;
    L_0x014d:
        return r1;
    L_0x014e:
        r8 = 1;
        r15 = 1;
    L_0x0150:
        r3 = r3 + 1;
        r6 = 1;
        r9 = 70;
        goto L_0x008f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.math.NumberUtils.isCreatable(java.lang.String):boolean");
    }

    public static boolean isParsable(String str) {
        if (StringUtils.isEmpty(str) || str.charAt(str.length() - 1) == '.') {
            return false;
        }
        if (str.charAt(0) != '-') {
            return withDecimalsParsing(str, 0);
        }
        if (str.length() == 1) {
            return false;
        }
        return withDecimalsParsing(str, 1);
    }

    private static boolean withDecimalsParsing(String str, int i) {
        int i2 = 0;
        while (i < str.length()) {
            Object obj = str.charAt(i) == '.' ? 1 : null;
            if (obj != null) {
                i2++;
            }
            if (i2 > 1) {
                return false;
            }
            if (obj == null && !Character.isDigit(str.charAt(i))) {
                return false;
            }
            i++;
        }
        return true;
    }
}
