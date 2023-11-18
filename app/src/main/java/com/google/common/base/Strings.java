package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;

@GwtCompatible
public final class Strings {
    public static String nullToEmpty(@Nullable String str) {
        return str == null ? "" : str;
    }

    private Strings() {
    }

    @Nullable
    public static String emptyToNull(@Nullable String str) {
        return isNullOrEmpty(str) ? null : str;
    }

    public static boolean isNullOrEmpty(@Nullable String str) {
        if (str != null) {
            if (str.length() != null) {
                return null;
            }
        }
        return true;
    }

    public static String padStart(String str, int i, char c) {
        Preconditions.checkNotNull(str);
        if (str.length() >= i) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(i);
        for (int length = str.length(); length < i; length++) {
            stringBuilder.append(c);
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static String padEnd(String str, int i, char c) {
        Preconditions.checkNotNull(str);
        if (str.length() >= i) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(i);
        stringBuilder.append(str);
        for (str = str.length(); str < i; str++) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String repeat(String str, int i) {
        Preconditions.checkNotNull(str);
        if (i <= 1) {
            Preconditions.checkArgument(i >= 0, "invalid count: %s", Integer.valueOf(i));
            if (i == 0) {
                str = "";
            }
            return str;
        }
        int length = str.length();
        long j = ((long) length) * ((long) i);
        i = (int) j;
        if (((long) i) != j) {
            i = new StringBuilder();
            i.append("Required array size too large: ");
            i.append(j);
            throw new ArrayIndexOutOfBoundsException(i.toString());
        }
        Object obj = new char[i];
        str.getChars(0, length, obj, 0);
        while (true) {
            str = i - length;
            if (length < str) {
                System.arraycopy(obj, 0, obj, length, length);
                length <<= 1;
            } else {
                System.arraycopy(obj, 0, obj, length, str);
                return new String(obj);
            }
        }
    }

    public static String commonPrefix(CharSequence charSequence, CharSequence charSequence2) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(charSequence2);
        int min = Math.min(charSequence.length(), charSequence2.length());
        int i = 0;
        while (i < min && charSequence.charAt(i) == charSequence2.charAt(i)) {
            i++;
        }
        min = i - 1;
        if (validSurrogatePairAt(charSequence, min) || validSurrogatePairAt(charSequence2, min) != null) {
            i--;
        }
        return charSequence.subSequence(0, i).toString();
    }

    public static String commonSuffix(CharSequence charSequence, CharSequence charSequence2) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(charSequence2);
        int min = Math.min(charSequence.length(), charSequence2.length());
        int i = 0;
        while (i < min && charSequence.charAt((charSequence.length() - i) - 1) == charSequence2.charAt((charSequence2.length() - i) - 1)) {
            i++;
        }
        if (validSurrogatePairAt(charSequence, (charSequence.length() - i) - 1) || validSurrogatePairAt(charSequence2, (charSequence2.length() - i) - 1) != null) {
            i--;
        }
        return charSequence.subSequence(charSequence.length() - i, charSequence.length()).toString();
    }

    @VisibleForTesting
    static boolean validSurrogatePairAt(CharSequence charSequence, int i) {
        return i >= 0 && i <= charSequence.length() - 2 && Character.isHighSurrogate(charSequence.charAt(i)) && Character.isLowSurrogate(charSequence.charAt(i + 1)) != null;
    }
}
