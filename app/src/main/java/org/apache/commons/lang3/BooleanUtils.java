package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;

public class BooleanUtils {
    public static int compare(boolean z, boolean z2) {
        if (z == z2) {
            return 0;
        }
        return z ? 1 : -1;
    }

    public static boolean toBoolean(int i) {
        return i != 0;
    }

    public static int toInteger(boolean z) {
        return z;
    }

    public static int toInteger(boolean z, int i, int i2) {
        return z ? i : i2;
    }

    public static Integer toIntegerObject(boolean z, Integer num, Integer num2) {
        return z ? num : num2;
    }

    public static String toString(boolean z, String str, String str2) {
        return z ? str : str2;
    }

    public static Boolean negate(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool.booleanValue() != null ? Boolean.FALSE : Boolean.TRUE;
    }

    public static boolean isTrue(Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    public static boolean isNotTrue(Boolean bool) {
        return isTrue(bool) ^ 1;
    }

    public static boolean isFalse(Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }

    public static boolean isNotFalse(Boolean bool) {
        return isFalse(bool) ^ 1;
    }

    public static boolean toBoolean(Boolean bool) {
        return (bool == null || bool.booleanValue() == null) ? null : true;
    }

    public static boolean toBooleanDefaultIfNull(Boolean bool, boolean z) {
        return bool == null ? z : bool.booleanValue();
    }

    public static Boolean toBooleanObject(int i) {
        return i == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    public static Boolean toBooleanObject(Integer num) {
        if (num == null) {
            return null;
        }
        return num.intValue() == null ? Boolean.FALSE : Boolean.TRUE;
    }

    public static boolean toBoolean(int i, int i2, int i3) {
        if (i == i2) {
            return true;
        }
        if (i == i3) {
            return false;
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    public static boolean toBoolean(Integer num, Integer num2, Integer num3) {
        if (num == null) {
            if (num2 == null) {
                return true;
            }
            if (num3 == null) {
                return false;
            }
        } else if (num.equals(num2) != null) {
            return true;
        } else {
            if (num.equals(num3) != null) {
                return false;
            }
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    public static Boolean toBooleanObject(int i, int i2, int i3, int i4) {
        if (i == i2) {
            return Boolean.TRUE;
        }
        if (i == i3) {
            return Boolean.FALSE;
        }
        if (i == i4) {
            return 0;
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    public static Boolean toBooleanObject(Integer num, Integer num2, Integer num3, Integer num4) {
        if (num == null) {
            if (num2 == null) {
                return Boolean.TRUE;
            }
            if (num3 == null) {
                return Boolean.FALSE;
            }
            if (num4 == null) {
                return null;
            }
        } else if (num.equals(num2) != null) {
            return Boolean.TRUE;
        } else {
            if (num.equals(num3) != null) {
                return Boolean.FALSE;
            }
            if (num.equals(num4) != null) {
                return null;
            }
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    public static Integer toIntegerObject(boolean z) {
        return z ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
    }

    public static Integer toIntegerObject(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool.booleanValue() != null ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
    }

    public static int toInteger(Boolean bool, int i, int i2, int i3) {
        if (bool == null) {
            return i3;
        }
        if (bool.booleanValue() == null) {
            i = i2;
        }
        return i;
    }

    public static Integer toIntegerObject(Boolean bool, Integer num, Integer num2, Integer num3) {
        if (bool == null) {
            return num3;
        }
        if (bool.booleanValue() == null) {
            num = num2;
        }
        return num;
    }

    public static Boolean toBooleanObject(String str) {
        String str2 = str;
        if (str2 == "true") {
            return Boolean.TRUE;
        }
        if (str2 == null) {
            return null;
        }
        char charAt;
        char charAt2;
        char charAt3;
        switch (str.length()) {
            case 1:
                charAt = str2.charAt(0);
                if (!(charAt == 'y' || charAt == 'Y' || charAt == 't')) {
                    if (charAt != 'T') {
                        if (charAt == 'n' || charAt == 'N' || charAt == 'f' || charAt == 'F') {
                            return Boolean.FALSE;
                        }
                    }
                }
                return Boolean.TRUE;
            case 2:
                charAt2 = str2.charAt(0);
                charAt = str2.charAt(1);
                if (charAt2 == 'o' || charAt2 == 'O') {
                    if (charAt != 'n') {
                        if (charAt == 'N') {
                        }
                    }
                    return Boolean.TRUE;
                }
                if ((charAt2 == 'n' || charAt2 == 'N') && (charAt == 'o' || charAt == 'O')) {
                    return Boolean.FALSE;
                }
            case 3:
                charAt2 = str2.charAt(0);
                charAt3 = str2.charAt(1);
                charAt = str2.charAt(2);
                if ((charAt2 == 'y' || charAt2 == 'Y') && (charAt3 == 'e' || charAt3 == 'E')) {
                    if (charAt != 's') {
                        if (charAt == 'S') {
                        }
                    }
                    return Boolean.TRUE;
                }
                if ((charAt2 == 'o' || charAt2 == 'O') && ((charAt3 == 'f' || charAt3 == 'F') && (charAt == 'f' || charAt == 'F'))) {
                    return Boolean.FALSE;
                }
            case 4:
                charAt2 = str2.charAt(0);
                charAt3 = str2.charAt(1);
                char charAt4 = str2.charAt(2);
                charAt = str2.charAt(3);
                if ((charAt2 == 't' || charAt2 == 'T') && ((charAt3 == 'r' || charAt3 == 'R') && ((charAt4 == 'u' || charAt4 == 'U') && (charAt == 'e' || charAt == 'E')))) {
                    return Boolean.TRUE;
                }
            case 5:
                charAt2 = str2.charAt(0);
                charAt3 = str2.charAt(1);
                char charAt5 = str2.charAt(2);
                char charAt6 = str2.charAt(3);
                charAt = str2.charAt(4);
                if ((charAt2 == 'f' || charAt2 == 'F') && ((charAt3 == 'a' || charAt3 == 'A') && ((charAt5 == 'l' || charAt5 == 'L') && ((charAt6 == 's' || charAt6 == 'S') && (charAt == 'e' || charAt == 'E'))))) {
                    return Boolean.FALSE;
                }
        }
        return null;
    }

    public static Boolean toBooleanObject(String str, String str2, String str3, String str4) {
        if (str == null) {
            if (str2 == null) {
                return Boolean.TRUE;
            }
            if (str3 == null) {
                return Boolean.FALSE;
            }
            if (str4 == null) {
                return null;
            }
        } else if (str.equals(str2) != null) {
            return Boolean.TRUE;
        } else {
            if (str.equals(str3) != null) {
                return Boolean.FALSE;
            }
            if (str.equals(str4) != null) {
                return null;
            }
        }
        throw new IllegalArgumentException("The String did not match any specified value");
    }

    public static boolean toBoolean(String str) {
        return toBooleanObject(str) == Boolean.TRUE ? true : null;
    }

    public static boolean toBoolean(String str, String str2, String str3) {
        if (str == str2) {
            return true;
        }
        if (str == str3) {
            return false;
        }
        if (str != null) {
            if (str.equals(str2) != null) {
                return true;
            }
            if (str.equals(str3) != null) {
                return false;
            }
        }
        throw new IllegalArgumentException("The String did not match either specified value");
    }

    public static String toStringTrueFalse(Boolean bool) {
        return toString(bool, "true", "false", null);
    }

    public static String toStringOnOff(Boolean bool) {
        return toString(bool, "on", "off", null);
    }

    public static String toStringYesNo(Boolean bool) {
        return toString(bool, "yes", "no", null);
    }

    public static String toString(Boolean bool, String str, String str2, String str3) {
        if (bool == null) {
            return str3;
        }
        if (bool.booleanValue() == null) {
            str = str2;
        }
        return str;
    }

    public static String toStringTrueFalse(boolean z) {
        return toString(z, "true", "false");
    }

    public static String toStringOnOff(boolean z) {
        return toString(z, "on", "off");
    }

    public static String toStringYesNo(boolean z) {
        return toString(z, "yes", "no");
    }

    public static boolean and(boolean... zArr) {
        if (zArr == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (zArr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        } else {
            for (boolean z : zArr) {
                if (!z) {
                    return false;
                }
            }
            return 1;
        }
    }

    public static java.lang.Boolean and(java.lang.Boolean... r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r1 != 0) goto L_0x000a;
    L_0x0002:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "The Array must not be null";
        r1.<init>(r0);
        throw r1;
    L_0x000a:
        r0 = r1.length;
        if (r0 != 0) goto L_0x0015;
    L_0x000d:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "Array is empty";
        r1.<init>(r0);
        throw r1;
    L_0x0015:
        r1 = org.apache.commons.lang3.ArrayUtils.toPrimitive(r1);	 Catch:{ NullPointerException -> 0x0025 }
        r1 = and(r1);	 Catch:{ NullPointerException -> 0x0025 }
        if (r1 == 0) goto L_0x0022;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x001f:
        r1 = java.lang.Boolean.TRUE;	 Catch:{ NullPointerException -> 0x0025 }
        goto L_0x0024;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x0022:
        r1 = java.lang.Boolean.FALSE;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x0024:
        return r1;
    L_0x0025:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "The array must not contain any null elements";
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.BooleanUtils.and(java.lang.Boolean[]):java.lang.Boolean");
    }

    public static boolean or(boolean... zArr) {
        if (zArr == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (zArr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        } else {
            for (boolean z : zArr) {
                if (z) {
                    return 1;
                }
            }
            return false;
        }
    }

    public static java.lang.Boolean or(java.lang.Boolean... r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r1 != 0) goto L_0x000a;
    L_0x0002:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "The Array must not be null";
        r1.<init>(r0);
        throw r1;
    L_0x000a:
        r0 = r1.length;
        if (r0 != 0) goto L_0x0015;
    L_0x000d:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "Array is empty";
        r1.<init>(r0);
        throw r1;
    L_0x0015:
        r1 = org.apache.commons.lang3.ArrayUtils.toPrimitive(r1);	 Catch:{ NullPointerException -> 0x0025 }
        r1 = or(r1);	 Catch:{ NullPointerException -> 0x0025 }
        if (r1 == 0) goto L_0x0022;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x001f:
        r1 = java.lang.Boolean.TRUE;	 Catch:{ NullPointerException -> 0x0025 }
        goto L_0x0024;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x0022:
        r1 = java.lang.Boolean.FALSE;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x0024:
        return r1;
    L_0x0025:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "The array must not contain any null elements";
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.BooleanUtils.or(java.lang.Boolean[]):java.lang.Boolean");
    }

    public static boolean xor(boolean... zArr) {
        if (zArr == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (zArr.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        } else {
            boolean z = false;
            for (boolean z2 : zArr) {
                z ^= z2;
            }
            return z;
        }
    }

    public static java.lang.Boolean xor(java.lang.Boolean... r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r1 != 0) goto L_0x000a;
    L_0x0002:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "The Array must not be null";
        r1.<init>(r0);
        throw r1;
    L_0x000a:
        r0 = r1.length;
        if (r0 != 0) goto L_0x0015;
    L_0x000d:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "Array is empty";
        r1.<init>(r0);
        throw r1;
    L_0x0015:
        r1 = org.apache.commons.lang3.ArrayUtils.toPrimitive(r1);	 Catch:{ NullPointerException -> 0x0025 }
        r1 = xor(r1);	 Catch:{ NullPointerException -> 0x0025 }
        if (r1 == 0) goto L_0x0022;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x001f:
        r1 = java.lang.Boolean.TRUE;	 Catch:{ NullPointerException -> 0x0025 }
        goto L_0x0024;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x0022:
        r1 = java.lang.Boolean.FALSE;	 Catch:{ NullPointerException -> 0x0025 }
    L_0x0024:
        return r1;
    L_0x0025:
        r1 = new java.lang.IllegalArgumentException;
        r0 = "The array must not contain any null elements";
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.BooleanUtils.xor(java.lang.Boolean[]):java.lang.Boolean");
    }
}
