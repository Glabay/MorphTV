package org.apache.commons.lang3;

public class CharSequenceUtils {
    private static final int NOT_FOUND = -1;

    public static CharSequence subSequence(CharSequence charSequence, int i) {
        return charSequence == null ? null : charSequence.subSequence(i, charSequence.length());
    }

    static int indexOf(CharSequence charSequence, int i, int i2) {
        if (charSequence instanceof String) {
            return ((String) charSequence).indexOf(i, i2);
        }
        int length = charSequence.length();
        if (i2 < 0) {
            i2 = 0;
        }
        while (i2 < length) {
            if (charSequence.charAt(i2) == i) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    static int indexOf(CharSequence charSequence, CharSequence charSequence2, int i) {
        return charSequence.toString().indexOf(charSequence2.toString(), i);
    }

    static int lastIndexOf(CharSequence charSequence, int i, int i2) {
        if (charSequence instanceof String) {
            return ((String) charSequence).lastIndexOf(i, i2);
        }
        int length = charSequence.length();
        if (i2 < 0) {
            return -1;
        }
        if (i2 >= length) {
            i2 = length - 1;
        }
        while (i2 >= 0) {
            if (charSequence.charAt(i2) == i) {
                return i2;
            }
            i2--;
        }
        return -1;
    }

    static int lastIndexOf(CharSequence charSequence, CharSequence charSequence2, int i) {
        return charSequence.toString().lastIndexOf(charSequence2.toString(), i);
    }

    static char[] toCharArray(CharSequence charSequence) {
        if (charSequence instanceof String) {
            return ((String) charSequence).toCharArray();
        }
        int length = charSequence.length();
        char[] cArr = new char[charSequence.length()];
        for (int i = 0; i < length; i++) {
            cArr[i] = charSequence.charAt(i);
        }
        return cArr;
    }

    static boolean regionMatches(CharSequence charSequence, boolean z, int i, CharSequence charSequence2, int i2, int i3) {
        if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
            return ((String) charSequence).regionMatches(z, i, (String) charSequence2, i2, i3);
        }
        int length = charSequence.length() - i;
        int length2 = charSequence2.length() - i2;
        if (i >= 0 && i2 >= 0) {
            if (i3 >= 0) {
                if (length >= i3) {
                    if (length2 >= i3) {
                        while (true) {
                            length = i3 - 1;
                            if (i3 <= 0) {
                                return true;
                            }
                            i3 = i + 1;
                            i = charSequence.charAt(i);
                            length2 = i2 + 1;
                            i2 = charSequence2.charAt(i2);
                            if (i != i2) {
                                if (!z) {
                                    return false;
                                }
                                if (!(Character.toUpperCase(i) == Character.toUpperCase(i2) || Character.toLowerCase(i) == Character.toLowerCase(i2))) {
                                    return false;
                                }
                            }
                            i = i3;
                            i3 = length;
                            i2 = length2;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }
}
