package org.apache.commons.lang3;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import kotlin.text.Typography;

public class StringUtils {
    public static final String CR = "\r";
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    public static final String LF = "\n";
    private static final int PAD_LIMIT = 8192;
    public static final String SPACE = " ";

    public static String defaultString(String str) {
        return str == null ? "" : str;
    }

    public static String defaultString(String str, String str2) {
        return str == null ? str2 : str;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        if (charSequence != null) {
            if (charSequence.length() != null) {
                return null;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return isEmpty(charSequence) ^ 1;
    }

    public static boolean isAnyEmpty(CharSequence... charSequenceArr) {
        if (ArrayUtils.isEmpty((Object[]) charSequenceArr)) {
            return true;
        }
        for (CharSequence isEmpty : charSequenceArr) {
            if (isEmpty(isEmpty)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoneEmpty(CharSequence... charSequenceArr) {
        return isAnyEmpty(charSequenceArr) ^ 1;
    }

    public static boolean isBlank(CharSequence charSequence) {
        if (charSequence != null) {
            int length = charSequence.length();
            if (length != 0) {
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(charSequence.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return isBlank(charSequence) ^ 1;
    }

    public static boolean isAnyBlank(CharSequence... charSequenceArr) {
        if (ArrayUtils.isEmpty((Object[]) charSequenceArr)) {
            return true;
        }
        for (CharSequence isBlank : charSequenceArr) {
            if (isBlank(isBlank)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNoneBlank(CharSequence... charSequenceArr) {
        return isAnyBlank(charSequenceArr) ^ 1;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String trimToNull(String str) {
        str = trim(str);
        return isEmpty(str) ? null : str;
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static String truncate(String str, int i) {
        return truncate(str, 0, i);
    }

    public static String truncate(String str, int i, int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        } else if (i2 < 0) {
            throw new IllegalArgumentException("maxWith cannot be negative");
        } else if (str == null) {
            return null;
        } else {
            if (i > str.length()) {
                return "";
            }
            if (str.length() <= i2) {
                return str.substring(i);
            }
            i2 += i;
            if (i2 > str.length()) {
                i2 = str.length();
            }
            return str.substring(i, i2);
        }
    }

    public static String strip(String str) {
        return strip(str, null);
    }

    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        str = strip(str, null);
        if (str.isEmpty()) {
            str = null;
        }
        return str;
    }

    public static String stripToEmpty(String str) {
        return str == null ? "" : strip(str, null);
    }

    public static String strip(String str, String str2) {
        if (isEmpty(str)) {
            return str;
        }
        return stripEnd(stripStart(str, str2), str2);
    }

    public static String stripStart(String str, String str2) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                int i = 0;
                if (str2 == null) {
                    while (i != length && Character.isWhitespace(str.charAt(i)) != null) {
                        i++;
                    }
                } else if (str2.isEmpty()) {
                    return str;
                } else {
                    while (i != length && str2.indexOf(str.charAt(i)) != -1) {
                        i++;
                    }
                }
                return str.substring(i);
            }
        }
        return str;
    }

    public static String stripEnd(String str, String str2) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                if (str2 == null) {
                    while (length != 0 && Character.isWhitespace(str.charAt(length - 1)) != null) {
                        length--;
                    }
                } else if (str2.isEmpty()) {
                    return str;
                } else {
                    while (length != 0 && str2.indexOf(str.charAt(length - 1)) != -1) {
                        length--;
                    }
                }
                return str.substring(null, length);
            }
        }
        return str;
    }

    public static String[] stripAll(String... strArr) {
        return stripAll(strArr, null);
    }

    public static String[] stripAll(String[] strArr, String str) {
        if (strArr != null) {
            int length = strArr.length;
            if (length != 0) {
                String[] strArr2 = new String[length];
                for (int i = 0; i < length; i++) {
                    strArr2[i] = strip(strArr[i], str);
                }
                return strArr2;
            }
        }
        return strArr;
    }

    public static String stripAccents(String str) {
        if (str == null) {
            return null;
        }
        Pattern compile = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        CharSequence stringBuilder = new StringBuilder(Normalizer.normalize(str, Form.NFD));
        convertRemainingAccentCharacters(stringBuilder);
        return compile.matcher(stringBuilder).replaceAll("");
    }

    private static void convertRemainingAccentCharacters(StringBuilder stringBuilder) {
        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == 'Ł') {
                stringBuilder.deleteCharAt(i);
                stringBuilder.insert(i, 'L');
            } else if (stringBuilder.charAt(i) == 'ł') {
                stringBuilder.deleteCharAt(i);
                stringBuilder.insert(i, 'l');
            }
        }
    }

    public static boolean equals(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (charSequence.length() != charSequence2.length()) {
                    return false;
                }
                if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
                    return charSequence.equals(charSequence2);
                }
                return CharSequenceUtils.regionMatches(charSequence, false, 0, charSequence2, 0, charSequence.length());
            }
        }
        return false;
    }

    public static boolean equalsIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        boolean z = false;
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (charSequence == charSequence2) {
                    return true;
                }
                if (charSequence.length() != charSequence2.length()) {
                    return false;
                }
                return CharSequenceUtils.regionMatches(charSequence, true, 0, charSequence2, 0, charSequence.length());
            }
        }
        if (charSequence == charSequence2) {
            z = true;
        }
        return z;
    }

    public static int compare(String str, String str2) {
        return compare(str, str2, true);
    }

    public static int compare(String str, String str2, boolean z) {
        if (str == str2) {
            return null;
        }
        int i = 1;
        if (str == null) {
            if (z) {
                i = -1;
            }
            return i;
        } else if (str2 != null) {
            return str.compareTo(str2);
        } else {
            if (!z) {
                i = -1;
            }
            return i;
        }
    }

    public static int compareIgnoreCase(String str, String str2) {
        return compareIgnoreCase(str, str2, true);
    }

    public static int compareIgnoreCase(String str, String str2, boolean z) {
        if (str == str2) {
            return null;
        }
        int i = 1;
        if (str == null) {
            if (z) {
                i = -1;
            }
            return i;
        } else if (str2 != null) {
            return str.compareToIgnoreCase(str2);
        } else {
            if (!z) {
                i = -1;
            }
            return i;
        }
    }

    public static boolean equalsAny(CharSequence charSequence, CharSequence... charSequenceArr) {
        if (ArrayUtils.isNotEmpty((Object[]) charSequenceArr)) {
            for (CharSequence equals : charSequenceArr) {
                if (equals(charSequence, equals)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean equalsAnyIgnoreCase(CharSequence charSequence, CharSequence... charSequenceArr) {
        if (ArrayUtils.isNotEmpty((Object[]) charSequenceArr)) {
            for (CharSequence equalsIgnoreCase : charSequenceArr) {
                if (equalsIgnoreCase(charSequence, equalsIgnoreCase)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int indexOf(CharSequence charSequence, int i) {
        if (isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(charSequence, i, 0);
    }

    public static int indexOf(CharSequence charSequence, int i, int i2) {
        if (isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(charSequence, i, i2);
    }

    public static int indexOf(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                return CharSequenceUtils.indexOf(charSequence, charSequence2, 0);
            }
        }
        return -1;
    }

    public static int indexOf(CharSequence charSequence, CharSequence charSequence2, int i) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                return CharSequenceUtils.indexOf(charSequence, charSequence2, i);
            }
        }
        return -1;
    }

    public static int ordinalIndexOf(CharSequence charSequence, CharSequence charSequence2, int i) {
        return ordinalIndexOf(charSequence, charSequence2, i, false);
    }

    private static int ordinalIndexOf(CharSequence charSequence, CharSequence charSequence2, int i, boolean z) {
        int i2 = -1;
        if (!(charSequence == null || charSequence2 == null)) {
            if (i > 0) {
                int i3 = 0;
                if (charSequence2.length() == 0) {
                    if (z) {
                        i3 = charSequence.length();
                    }
                    return i3;
                }
                if (z) {
                    i2 = charSequence.length();
                }
                do {
                    if (z) {
                        i2 = CharSequenceUtils.lastIndexOf(charSequence, charSequence2, i2 - 1);
                    } else {
                        i2 = CharSequenceUtils.indexOf(charSequence, charSequence2, i2 + 1);
                    }
                    if (i2 < 0) {
                        return i2;
                    }
                    i3++;
                } while (i3 < i);
                return i2;
            }
        }
        return -1;
    }

    public static int indexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        return indexOfIgnoreCase(charSequence, charSequence2, 0);
    }

    public static int indexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2, int i) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (i < 0) {
                    i = 0;
                }
                int length = (charSequence.length() - charSequence2.length()) + 1;
                if (i > length) {
                    return -1;
                }
                if (charSequence2.length() == 0) {
                    return i;
                }
                while (i < length) {
                    if (CharSequenceUtils.regionMatches(charSequence, true, i, charSequence2, 0, charSequence2.length())) {
                        return i;
                    }
                    i++;
                }
                return -1;
            }
        }
        return -1;
    }

    public static int lastIndexOf(CharSequence charSequence, int i) {
        if (isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(charSequence, i, charSequence.length());
    }

    public static int lastIndexOf(CharSequence charSequence, int i, int i2) {
        if (isEmpty(charSequence)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(charSequence, i, i2);
    }

    public static int lastIndexOf(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                return CharSequenceUtils.lastIndexOf(charSequence, charSequence2, charSequence.length());
            }
        }
        return -1;
    }

    public static int lastOrdinalIndexOf(CharSequence charSequence, CharSequence charSequence2, int i) {
        return ordinalIndexOf(charSequence, charSequence2, i, true);
    }

    public static int lastIndexOf(CharSequence charSequence, CharSequence charSequence2, int i) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                return CharSequenceUtils.lastIndexOf(charSequence, charSequence2, i);
            }
        }
        return -1;
    }

    public static int lastIndexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                return lastIndexOfIgnoreCase(charSequence, charSequence2, charSequence.length());
            }
        }
        return -1;
    }

    public static int lastIndexOfIgnoreCase(CharSequence charSequence, CharSequence charSequence2, int i) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (i > charSequence.length() - charSequence2.length()) {
                    i = charSequence.length() - charSequence2.length();
                }
                if (i < 0) {
                    return -1;
                }
                if (charSequence2.length() == 0) {
                    return i;
                }
                while (i >= 0) {
                    if (CharSequenceUtils.regionMatches(charSequence, true, i, charSequence2, 0, charSequence2.length())) {
                        return i;
                    }
                    i--;
                }
                return -1;
            }
        }
        return -1;
    }

    public static boolean contains(CharSequence charSequence, int i) {
        boolean z = false;
        if (isEmpty(charSequence)) {
            return false;
        }
        if (CharSequenceUtils.indexOf(charSequence, i, 0) >= null) {
            z = true;
        }
        return z;
    }

    public static boolean contains(CharSequence charSequence, CharSequence charSequence2) {
        boolean z = false;
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (CharSequenceUtils.indexOf(charSequence, charSequence2, 0) >= null) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public static boolean containsIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                int length = charSequence2.length();
                int length2 = charSequence.length() - length;
                for (int i = 0; i <= length2; i++) {
                    if (CharSequenceUtils.regionMatches(charSequence, true, i, charSequence2, 0, length)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static boolean containsWhitespace(CharSequence charSequence) {
        if (isEmpty(charSequence)) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (Character.isWhitespace(charSequence.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int indexOfAny(java.lang.CharSequence r11, char... r12) {
        /*
        r0 = isEmpty(r11);
        r1 = -1;
        if (r0 != 0) goto L_0x0045;
    L_0x0007:
        r0 = org.apache.commons.lang3.ArrayUtils.isEmpty(r12);
        if (r0 == 0) goto L_0x000e;
    L_0x000d:
        goto L_0x0045;
    L_0x000e:
        r0 = r11.length();
        r2 = r0 + -1;
        r3 = r12.length;
        r4 = r3 + -1;
        r5 = 0;
        r6 = 0;
    L_0x0019:
        if (r6 >= r0) goto L_0x0044;
    L_0x001b:
        r7 = r11.charAt(r6);
        r8 = 0;
    L_0x0020:
        if (r8 >= r3) goto L_0x0041;
    L_0x0022:
        r9 = r12[r8];
        if (r9 != r7) goto L_0x003e;
    L_0x0026:
        if (r6 >= r2) goto L_0x003d;
    L_0x0028:
        if (r8 >= r4) goto L_0x003d;
    L_0x002a:
        r9 = java.lang.Character.isHighSurrogate(r7);
        if (r9 == 0) goto L_0x003d;
    L_0x0030:
        r9 = r8 + 1;
        r9 = r12[r9];
        r10 = r6 + 1;
        r10 = r11.charAt(r10);
        if (r9 != r10) goto L_0x003e;
    L_0x003c:
        return r6;
    L_0x003d:
        return r6;
    L_0x003e:
        r8 = r8 + 1;
        goto L_0x0020;
    L_0x0041:
        r6 = r6 + 1;
        goto L_0x0019;
    L_0x0044:
        return r1;
    L_0x0045:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.StringUtils.indexOfAny(java.lang.CharSequence, char[]):int");
    }

    public static int indexOfAny(CharSequence charSequence, String str) {
        if (!isEmpty(charSequence)) {
            if (!isEmpty(str)) {
                return indexOfAny(charSequence, str.toCharArray());
            }
        }
        return -1;
    }

    public static boolean containsAny(CharSequence charSequence, char... cArr) {
        if (!isEmpty(charSequence)) {
            if (!ArrayUtils.isEmpty(cArr)) {
                int length = charSequence.length();
                int length2 = cArr.length;
                int i = length - 1;
                int i2 = length2 - 1;
                int i3 = 0;
                while (i3 < length) {
                    char charAt = charSequence.charAt(i3);
                    int i4 = 0;
                    while (i4 < length2) {
                        if (cArr[i4] == charAt) {
                            if (!Character.isHighSurrogate(charAt) || i4 == i2) {
                                return true;
                            }
                            if (i3 < i && cArr[i4 + 1] == charSequence.charAt(i3 + 1)) {
                                return true;
                            }
                        }
                        i4++;
                    }
                    i3++;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean containsAny(CharSequence charSequence, CharSequence charSequence2) {
        return charSequence2 == null ? null : containsAny(charSequence, CharSequenceUtils.toCharArray(charSequence2));
    }

    public static boolean containsAny(CharSequence charSequence, CharSequence... charSequenceArr) {
        if (!isEmpty(charSequence)) {
            if (!ArrayUtils.isEmpty((Object[]) charSequenceArr)) {
                for (CharSequence contains : charSequenceArr) {
                    if (contains(charSequence, contains)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static int indexOfAnyBut(CharSequence charSequence, char... cArr) {
        if (!isEmpty(charSequence)) {
            if (!ArrayUtils.isEmpty(cArr)) {
                int length = charSequence.length();
                int i = length - 1;
                int length2 = cArr.length;
                int i2 = length2 - 1;
                int i3 = 0;
                while (i3 < length) {
                    char charAt = charSequence.charAt(i3);
                    int i4 = 0;
                    while (i4 < length2) {
                        if (cArr[i4] != charAt || (i3 < i && i4 < i2 && Character.isHighSurrogate(charAt) && cArr[i4 + 1] != charSequence.charAt(i3 + 1))) {
                            i4++;
                        } else {
                            i3++;
                        }
                    }
                    return i3;
                }
                return -1;
            }
        }
        return -1;
    }

    public static int indexOfAnyBut(CharSequence charSequence, CharSequence charSequence2) {
        if (!isEmpty(charSequence)) {
            if (!isEmpty(charSequence2)) {
                int length = charSequence.length();
                int i = 0;
                while (i < length) {
                    int charAt = charSequence.charAt(i);
                    Object obj = CharSequenceUtils.indexOf(charSequence2, charAt, 0) >= 0 ? 1 : null;
                    int i2 = i + 1;
                    if (i2 < length && Character.isHighSurrogate(charAt)) {
                        charAt = charSequence.charAt(i2);
                        if (obj != null && CharSequenceUtils.indexOf(charSequence2, charAt, 0) < 0) {
                            return i;
                        }
                    } else if (obj == null) {
                        return i;
                    }
                    i = i2;
                }
                return -1;
            }
        }
        return -1;
    }

    public static boolean containsOnly(CharSequence charSequence, char... cArr) {
        boolean z = false;
        if (cArr != null) {
            if (charSequence != null) {
                if (charSequence.length() == 0) {
                    return true;
                }
                if (cArr.length == 0) {
                    return false;
                }
                if (indexOfAnyBut(charSequence, cArr) == -1) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    public static boolean containsOnly(CharSequence charSequence, String str) {
        if (charSequence != null) {
            if (str != null) {
                return containsOnly(charSequence, str.toCharArray());
            }
        }
        return null;
    }

    public static boolean containsNone(CharSequence charSequence, char... cArr) {
        if (charSequence != null) {
            if (cArr != null) {
                int length = charSequence.length();
                int i = length - 1;
                int length2 = cArr.length;
                int i2 = length2 - 1;
                int i3 = 0;
                while (i3 < length) {
                    char charAt = charSequence.charAt(i3);
                    int i4 = 0;
                    while (i4 < length2) {
                        if (cArr[i4] == charAt) {
                            if (!Character.isHighSurrogate(charAt) || i4 == i2) {
                                return false;
                            }
                            if (i3 < i && cArr[i4 + 1] == charSequence.charAt(i3 + 1)) {
                                return false;
                            }
                        }
                        i4++;
                    }
                    i3++;
                }
                return true;
            }
        }
        return true;
    }

    public static boolean containsNone(CharSequence charSequence, String str) {
        if (charSequence != null) {
            if (str != null) {
                return containsNone(charSequence, str.toCharArray());
            }
        }
        return true;
    }

    public static int indexOfAny(CharSequence charSequence, CharSequence... charSequenceArr) {
        int i = -1;
        if (charSequence != null) {
            if (charSequenceArr != null) {
                int i2 = Integer.MAX_VALUE;
                for (CharSequence charSequence2 : charSequenceArr) {
                    if (charSequence2 != null) {
                        int indexOf = CharSequenceUtils.indexOf(charSequence, charSequence2, 0);
                        if (indexOf != -1) {
                            if (indexOf < i2) {
                                i2 = indexOf;
                            }
                        }
                    }
                }
                if (i2 != Integer.MAX_VALUE) {
                    i = i2;
                }
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfAny(CharSequence charSequence, CharSequence... charSequenceArr) {
        int i = -1;
        if (charSequence != null) {
            if (charSequenceArr != null) {
                for (CharSequence charSequence2 : charSequenceArr) {
                    if (charSequence2 != null) {
                        int lastIndexOf = CharSequenceUtils.lastIndexOf(charSequence, charSequence2, charSequence.length());
                        if (lastIndexOf > i) {
                            i = lastIndexOf;
                        }
                    }
                }
                return i;
            }
        }
        return -1;
    }

    public static String substring(String str, int i) {
        if (str == null) {
            return null;
        }
        if (i < 0) {
            i += str.length();
        }
        if (i < 0) {
            i = 0;
        }
        if (i > str.length()) {
            return "";
        }
        return str.substring(i);
    }

    public static String substring(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (i2 < 0) {
            i2 += str.length();
        }
        if (i < 0) {
            i += str.length();
        }
        if (i2 > str.length()) {
            i2 = str.length();
        }
        if (i > i2) {
            return "";
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        return str.substring(i, i2);
    }

    public static String left(String str, int i) {
        if (str == null) {
            return null;
        }
        if (i < 0) {
            return "";
        }
        if (str.length() <= i) {
            return str;
        }
        return str.substring(0, i);
    }

    public static String right(String str, int i) {
        if (str == null) {
            return null;
        }
        if (i < 0) {
            return "";
        }
        if (str.length() <= i) {
            return str;
        }
        return str.substring(str.length() - i);
    }

    public static String mid(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (i2 >= 0) {
            if (i <= str.length()) {
                if (i < 0) {
                    i = 0;
                }
                i2 += i;
                if (str.length() <= i2) {
                    return str.substring(i);
                }
                return str.substring(i, i2);
            }
        }
        return "";
    }

    public static String substringBefore(String str, String str2) {
        if (!isEmpty(str)) {
            if (str2 != null) {
                if (str2.isEmpty()) {
                    return "";
                }
                str2 = str.indexOf(str2);
                if (str2 == -1) {
                    return str;
                }
                return str.substring(0, str2);
            }
        }
        return str;
    }

    public static String substringAfter(String str, String str2) {
        if (isEmpty(str)) {
            return str;
        }
        if (str2 == null) {
            return "";
        }
        int indexOf = str.indexOf(str2);
        if (indexOf == -1) {
            return "";
        }
        return str.substring(indexOf + str2.length());
    }

    public static String substringBeforeLast(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                str2 = str.lastIndexOf(str2);
                if (str2 == -1) {
                    return str;
                }
                return str.substring(0, str2);
            }
        }
        return str;
    }

    public static String substringAfterLast(String str, String str2) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(str2)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(str2);
        if (lastIndexOf != -1) {
            if (lastIndexOf != str.length() - str2.length()) {
                return str.substring(lastIndexOf + str2.length());
            }
        }
        return "";
    }

    public static String substringBetween(String str, String str2) {
        return substringBetween(str, str2, str2);
    }

    public static String substringBetween(String str, String str2, String str3) {
        if (!(str == null || str2 == null)) {
            if (str3 != null) {
                int indexOf = str.indexOf(str2);
                if (indexOf != -1) {
                    str3 = str.indexOf(str3, str2.length() + indexOf);
                    if (str3 != -1) {
                        return str.substring(indexOf + str2.length(), str3);
                    }
                }
                return null;
            }
        }
        return null;
    }

    public static String[] substringsBetween(String str, String str2, String str3) {
        if (!(str == null || isEmpty(str2))) {
            if (!isEmpty(str3)) {
                int length = str.length();
                if (length == 0) {
                    return ArrayUtils.EMPTY_STRING_ARRAY;
                }
                int length2 = str3.length();
                int length3 = str2.length();
                List arrayList = new ArrayList();
                int i = 0;
                while (i < length - length2) {
                    i = str.indexOf(str2, i);
                    if (i < 0) {
                        break;
                    }
                    i += length3;
                    int indexOf = str.indexOf(str3, i);
                    if (indexOf < 0) {
                        break;
                    }
                    arrayList.add(str.substring(i, indexOf));
                    i = indexOf + length2;
                }
                if (arrayList.isEmpty() != null) {
                    return null;
                }
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
        }
        return null;
    }

    public static String[] split(String str) {
        return split(str, null, -1);
    }

    public static String[] split(String str, char c) {
        return splitWorker(str, c, false);
    }

    public static String[] split(String str, String str2) {
        return splitWorker(str, str2, -1, false);
    }

    public static String[] split(String str, String str2, int i) {
        return splitWorker(str, str2, i, false);
    }

    public static String[] splitByWholeSeparator(String str, String str2) {
        return splitByWholeSeparatorWorker(str, str2, -1, false);
    }

    public static String[] splitByWholeSeparator(String str, String str2, int i) {
        return splitByWholeSeparatorWorker(str, str2, i, false);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String str2) {
        return splitByWholeSeparatorWorker(str, str2, -1, true);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String str2, int i) {
        return splitByWholeSeparatorWorker(str, str2, i, true);
    }

    private static String[] splitByWholeSeparatorWorker(String str, String str2, int i, boolean z) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (str2 != null) {
            if (!"".equals(str2)) {
                int length2 = str2.length();
                ArrayList arrayList = new ArrayList();
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                while (i2 < length) {
                    i2 = str.indexOf(str2, i3);
                    if (i2 <= -1) {
                        arrayList.add(str.substring(i3));
                    } else if (i2 > i3) {
                        i4++;
                        if (i4 == i) {
                            arrayList.add(str.substring(i3));
                        } else {
                            arrayList.add(str.substring(i3, i2));
                            i3 = i2 + length2;
                        }
                    } else {
                        if (z) {
                            i4++;
                            if (i4 == i) {
                                arrayList.add(str.substring(i3));
                                i2 = length;
                            } else {
                                arrayList.add("");
                            }
                        }
                        i3 = i2 + length2;
                    }
                    i2 = length;
                }
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
        }
        return splitWorker(str, null, i, z);
    }

    public static String[] splitPreserveAllTokens(String str) {
        return splitWorker(str, null, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, char c) {
        return splitWorker(str, c, true);
    }

    private static String[] splitWorker(String str, char c, boolean z) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List arrayList = new ArrayList();
        int i = 0;
        Object obj = null;
        int i2 = 0;
        loop0:
        while (true) {
            Object obj2 = null;
            while (i < length) {
                if (str.charAt(i) == c) {
                    if (obj != null || z) {
                        arrayList.add(str.substring(i2, i));
                        obj = null;
                        obj2 = 1;
                    }
                    i2 = i + 1;
                    i = i2;
                } else {
                    i++;
                    obj = 1;
                }
            }
            break loop0;
        }
        if (obj != null || (z && r7 != null)) {
            arrayList.add(str.substring(i2, i));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String[] splitPreserveAllTokens(String str, String str2) {
        return splitWorker(str, str2, -1, true);
    }

    public static String[] splitPreserveAllTokens(String str, String str2, int i) {
        return splitWorker(str, str2, i, true);
    }

    private static String[] splitWorker(String str, String str2, int i, boolean z) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        int i2;
        int i3;
        Object obj;
        List arrayList = new ArrayList();
        int i4;
        Object obj2;
        if (str2 == null) {
            str2 = null;
            Object obj3 = null;
            i2 = 1;
            i4 = 0;
            loop0:
            while (true) {
                obj2 = null;
                while (str2 < length) {
                    if (Character.isWhitespace(str.charAt(str2))) {
                        if (obj3 != null || z) {
                            Object obj4;
                            i3 = i2 + 1;
                            if (i2 == i) {
                                str2 = length;
                                obj4 = null;
                            } else {
                                obj4 = 1;
                            }
                            arrayList.add(str.substring(i4, str2));
                            obj2 = obj4;
                            i2 = i3;
                            obj3 = null;
                        }
                        i4 = str2 + 1;
                        str2 = i4;
                    } else {
                        str2++;
                        obj3 = 1;
                    }
                }
                break loop0;
            }
            i2 = i4;
            obj = obj3;
            i3 = str2;
        } else if (str2.length() == 1) {
            str2 = str2.charAt(0);
            i3 = 0;
            i2 = 0;
            obj = null;
            obj2 = null;
            r8 = 1;
            while (i3 < length) {
                if (str.charAt(i3) == str2) {
                    if (obj != null || z) {
                        i4 = r8 + 1;
                        if (r8 == i) {
                            i3 = length;
                            obj2 = null;
                        } else {
                            obj2 = 1;
                        }
                        arrayList.add(str.substring(i2, i3));
                        r8 = i4;
                        obj = null;
                    }
                    i2 = i3 + 1;
                    i3 = i2;
                } else {
                    i3++;
                    obj = 1;
                    obj2 = null;
                }
            }
        } else {
            i3 = 0;
            i2 = 0;
            obj = null;
            obj2 = null;
            r8 = 1;
            while (i3 < length) {
                if (str2.indexOf(str.charAt(i3)) >= 0) {
                    if (obj != null || z) {
                        i4 = r8 + 1;
                        if (r8 == i) {
                            i3 = length;
                            obj2 = null;
                        } else {
                            obj2 = 1;
                        }
                        arrayList.add(str.substring(i2, i3));
                        r8 = i4;
                        obj = null;
                    }
                    i2 = i3 + 1;
                    i3 = i2;
                } else {
                    i3++;
                    obj = 1;
                    obj2 = null;
                }
            }
        }
        if (obj != null || (z && r7 != null)) {
            arrayList.add(str.substring(i2, i3));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String[] splitByCharacterType(String str) {
        return splitByCharacterType(str, false);
    }

    public static String[] splitByCharacterTypeCamelCase(String str) {
        return splitByCharacterType(str, true);
    }

    private static String[] splitByCharacterType(String str, boolean z) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        str = str.toCharArray();
        List arrayList = new ArrayList();
        int type = Character.getType(str[0]);
        int i = 0;
        for (int i2 = 1; i2 < str.length; i2++) {
            int type2 = Character.getType(str[i2]);
            if (type2 != type) {
                if (z && type2 == 2 && type == 1) {
                    type = i2 - 1;
                    if (type != i) {
                        arrayList.add(new String(str, i, type - i));
                    } else {
                        type = i;
                    }
                } else {
                    arrayList.add(new String(str, i, i2 - i));
                    type = i2;
                }
                i = type;
                type = type2;
            }
        }
        arrayList.add(new String(str, i, str.length - i));
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static <T> String join(T... tArr) {
        return join((Object[]) tArr, null);
    }

    public static String join(Object[] objArr, char c) {
        return objArr == null ? null : join(objArr, c, 0, objArr.length);
    }

    public static String join(long[] jArr, char c) {
        return jArr == null ? null : join(jArr, c, 0, jArr.length);
    }

    public static String join(int[] iArr, char c) {
        return iArr == null ? null : join(iArr, c, 0, iArr.length);
    }

    public static String join(short[] sArr, char c) {
        return sArr == null ? null : join(sArr, c, 0, sArr.length);
    }

    public static String join(byte[] bArr, char c) {
        return bArr == null ? null : join(bArr, c, 0, bArr.length);
    }

    public static String join(char[] cArr, char c) {
        return cArr == null ? null : join(cArr, c, 0, cArr.length);
    }

    public static String join(float[] fArr, char c) {
        return fArr == null ? null : join(fArr, c, 0, fArr.length);
    }

    public static String join(double[] dArr, char c) {
        return dArr == null ? null : join(dArr, c, 0, dArr.length);
    }

    public static String join(Object[] objArr, char c, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            if (objArr[i3] != null) {
                stringBuilder.append(objArr[i3]);
            }
        }
        return stringBuilder.toString();
    }

    public static String join(long[] jArr, char c, int i, int i2) {
        if (jArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            stringBuilder.append(jArr[i3]);
        }
        return stringBuilder.toString();
    }

    public static String join(int[] iArr, char c, int i, int i2) {
        if (iArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            stringBuilder.append(iArr[i3]);
        }
        return stringBuilder.toString();
    }

    public static String join(byte[] bArr, char c, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            stringBuilder.append(bArr[i3]);
        }
        return stringBuilder.toString();
    }

    public static String join(short[] sArr, char c, int i, int i2) {
        if (sArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            stringBuilder.append(sArr[i3]);
        }
        return stringBuilder.toString();
    }

    public static String join(char[] cArr, char c, int i, int i2) {
        if (cArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            stringBuilder.append(cArr[i3]);
        }
        return stringBuilder.toString();
    }

    public static String join(double[] dArr, char c, int i, int i2) {
        if (dArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            stringBuilder.append(dArr[i3]);
        }
        return stringBuilder.toString();
    }

    public static String join(float[] fArr, char c, int i, int i2) {
        if (fArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(c);
            }
            stringBuilder.append(fArr[i3]);
        }
        return stringBuilder.toString();
    }

    public static String join(Object[] objArr, String str) {
        return objArr == null ? null : join(objArr, str, 0, objArr.length);
    }

    public static String join(Object[] objArr, String str, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        if (str == null) {
            str = "";
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(i3 * 16);
        for (i3 = i; i3 < i2; i3++) {
            if (i3 > i) {
                stringBuilder.append(str);
            }
            if (objArr[i3] != null) {
                stringBuilder.append(objArr[i3]);
            }
        }
        return stringBuilder.toString();
    }

    public static String join(Iterator<?> it, char c) {
        if (it == null) {
            return null;
        }
        if (!it.hasNext()) {
            return "";
        }
        Object next = it.next();
        if (!it.hasNext()) {
            return ObjectUtils.toString(next);
        }
        StringBuilder stringBuilder = new StringBuilder(256);
        if (next != null) {
            stringBuilder.append(next);
        }
        while (it.hasNext()) {
            stringBuilder.append(c);
            next = it.next();
            if (next != null) {
                stringBuilder.append(next);
            }
        }
        return stringBuilder.toString();
    }

    public static String join(Iterator<?> it, String str) {
        if (it == null) {
            return null;
        }
        if (!it.hasNext()) {
            return "";
        }
        Object next = it.next();
        if (!it.hasNext()) {
            return ObjectUtils.toString(next);
        }
        StringBuilder stringBuilder = new StringBuilder(256);
        if (next != null) {
            stringBuilder.append(next);
        }
        while (it.hasNext()) {
            if (str != null) {
                stringBuilder.append(str);
            }
            next = it.next();
            if (next != null) {
                stringBuilder.append(next);
            }
        }
        return stringBuilder.toString();
    }

    public static String join(Iterable<?> iterable, char c) {
        return iterable == null ? null : join(iterable.iterator(), c);
    }

    public static String join(Iterable<?> iterable, String str) {
        return iterable == null ? null : join(iterable.iterator(), str);
    }

    public static String joinWith(String str, Object... objArr) {
        if (objArr == null) {
            throw new IllegalArgumentException("Object varargs must not be null");
        }
        str = defaultString(str, "");
        StringBuilder stringBuilder = new StringBuilder();
        objArr = Arrays.asList(objArr).iterator();
        while (objArr.hasNext()) {
            stringBuilder.append(ObjectUtils.toString(objArr.next()));
            if (objArr.hasNext()) {
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }

    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int length = str.length();
        char[] cArr = new char[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (!Character.isWhitespace(str.charAt(i2))) {
                int i3 = i + 1;
                cArr[i] = str.charAt(i2);
                i = i3;
            }
        }
        if (i == length) {
            return str;
        }
        return new String(cArr, 0, i);
    }

    public static String removeStart(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                return str.startsWith(str2) ? str.substring(str2.length()) : str;
            }
        }
        return str;
    }

    public static String removeStartIgnoreCase(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                return startsWithIgnoreCase(str, str2) ? str.substring(str2.length()) : str;
            }
        }
        return str;
    }

    public static String removeEnd(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                return str.endsWith(str2) ? str.substring(0, str.length() - str2.length()) : str;
            }
        }
        return str;
    }

    public static String removeEndIgnoreCase(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                return endsWithIgnoreCase(str, str2) ? str.substring(0, str.length() - str2.length()) : str;
            }
        }
        return str;
    }

    public static String remove(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                return replace(str, str2, "", -1);
            }
        }
        return str;
    }

    public static String removeIgnoreCase(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                return replaceIgnoreCase(str, str2, "", -1);
            }
        }
        return str;
    }

    public static String remove(String str, char c) {
        if (!isEmpty(str)) {
            if (str.indexOf(c) != -1) {
                str = str.toCharArray();
                int i = 0;
                for (int i2 = 0; i2 < str.length; i2++) {
                    if (str[i2] != c) {
                        int i3 = i + 1;
                        str[i] = str[i2];
                        i = i3;
                    }
                }
                return new String(str, 0, i);
            }
        }
        return str;
    }

    public static String removeAll(String str, String str2) {
        return replaceAll(str, str2, "");
    }

    public static String removeFirst(String str, String str2) {
        return replaceFirst(str, str2, "");
    }

    public static String replaceOnce(String str, String str2, String str3) {
        return replace(str, str2, str3, 1);
    }

    public static String replaceOnceIgnoreCase(String str, String str2, String str3) {
        return replaceIgnoreCase(str, str2, str3, 1);
    }

    public static String replacePattern(String str, String str2, String str3) {
        if (!(str == null || str2 == null)) {
            if (str3 != null) {
                return Pattern.compile(str2, 32).matcher(str).replaceAll(str3);
            }
        }
        return str;
    }

    public static String removePattern(String str, String str2) {
        return replacePattern(str, str2, "");
    }

    public static String replaceAll(String str, String str2, String str3) {
        if (!(str == null || str2 == null)) {
            if (str3 != null) {
                return str.replaceAll(str2, str3);
            }
        }
        return str;
    }

    public static String replaceFirst(String str, String str2, String str3) {
        if (!(str == null || str2 == null)) {
            if (str3 != null) {
                return str.replaceFirst(str2, str3);
            }
        }
        return str;
    }

    public static String replace(String str, String str2, String str3) {
        return replace(str, str2, str3, -1);
    }

    public static String replaceIgnoreCase(String str, String str2, String str3) {
        return replaceIgnoreCase(str, str2, str3, -1);
    }

    public static String replace(String str, String str2, String str3, int i) {
        return replace(str, str2, str3, i, false);
    }

    private static String replace(String str, String str2, String str3, int i, boolean z) {
        if (!(isEmpty(str) || isEmpty(str2) || str3 == null)) {
            if (i != 0) {
                if (z) {
                    z = str.toLowerCase();
                    str2 = str2.toLowerCase();
                } else {
                    z = str;
                }
                int i2 = 0;
                int indexOf = z.indexOf(str2, 0);
                if (indexOf == -1) {
                    return str;
                }
                int length = str2.length();
                int length2 = str3.length() - length;
                if (length2 < 0) {
                    length2 = 0;
                }
                int i3 = 64;
                if (i < 0) {
                    i3 = 16;
                } else if (i <= 64) {
                    i3 = i;
                }
                StringBuilder stringBuilder = new StringBuilder(str.length() + (length2 * i3));
                while (indexOf != -1) {
                    stringBuilder.append(str.substring(i2, indexOf));
                    stringBuilder.append(str3);
                    i2 = indexOf + length;
                    i--;
                    if (i == 0) {
                        break;
                    }
                    indexOf = z.indexOf(str2, i2);
                }
                stringBuilder.append(str.substring(i2));
                return stringBuilder.toString();
            }
        }
        return str;
    }

    public static String replaceIgnoreCase(String str, String str2, String str3, int i) {
        return replace(str, str2, str3, i, true);
    }

    public static String replaceEach(String str, String[] strArr, String[] strArr2) {
        return replaceEach(str, strArr, strArr2, false, 0);
    }

    public static String replaceEachRepeatedly(String str, String[] strArr, String[] strArr2) {
        return replaceEach(str, strArr, strArr2, true, strArr == null ? 0 : strArr.length);
    }

    private static String replaceEach(String str, String[] strArr, String[] strArr2, boolean z, int i) {
        if (!(str == null || str.isEmpty() || strArr == null || strArr.length == 0 || strArr2 == null)) {
            if (strArr2.length != 0) {
                if (i < 0) {
                    throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
                }
                int length = strArr.length;
                int length2 = strArr2.length;
                if (length != length2) {
                    strArr = new StringBuilder();
                    strArr.append("Search and Replace array lengths don't match: ");
                    strArr.append(length);
                    strArr.append(" vs ");
                    strArr.append(length2);
                    throw new IllegalArgumentException(strArr.toString());
                }
                int indexOf;
                boolean[] zArr = new boolean[length];
                int i2 = 0;
                int i3 = -1;
                int i4 = -1;
                while (i2 < length) {
                    if (!(zArr[i2] || strArr[i2] == null || strArr[i2].isEmpty())) {
                        if (strArr2[i2] != null) {
                            indexOf = str.indexOf(strArr[i2]);
                            if (indexOf == -1) {
                                zArr[i2] = true;
                            } else if (i3 == -1 || indexOf < i3) {
                                i4 = i2;
                                i3 = indexOf;
                            }
                        }
                    }
                    i2++;
                }
                if (i3 == -1) {
                    return str;
                }
                int length3;
                indexOf = 0;
                for (i2 = 0; i2 < strArr.length; i2++) {
                    if (strArr[i2] != null) {
                        if (strArr2[i2] != null) {
                            length3 = strArr2[i2].length() - strArr[i2].length();
                            if (length3 > 0) {
                                indexOf += length3 * 3;
                            }
                        }
                    }
                }
                StringBuilder stringBuilder = new StringBuilder(str.length() + Math.min(indexOf, str.length() / 5));
                i2 = 0;
                while (i3 != -1) {
                    while (i2 < i3) {
                        stringBuilder.append(str.charAt(i2));
                        i2++;
                    }
                    stringBuilder.append(strArr2[i4]);
                    i2 = strArr[i4].length() + i3;
                    i3 = 0;
                    i4 = -1;
                    length3 = -1;
                    while (i3 < length) {
                        if (!(zArr[i3] || strArr[i3] == null || strArr[i3].isEmpty())) {
                            if (strArr2[i3] != null) {
                                int indexOf2 = str.indexOf(strArr[i3], i2);
                                if (indexOf2 == -1) {
                                    zArr[i3] = true;
                                } else if (i4 == -1 || indexOf2 < i4) {
                                    length3 = i3;
                                    i4 = indexOf2;
                                }
                            }
                        }
                        i3++;
                    }
                    i3 = i4;
                    i4 = length3;
                }
                length = str.length();
                while (i2 < length) {
                    stringBuilder.append(str.charAt(i2));
                    i2++;
                }
                str = stringBuilder.toString();
                if (z) {
                    return replaceEach(str, strArr, strArr2, z, i - 1);
                }
                return str;
            }
        }
        return str;
    }

    public static String replaceChars(String str, char c, char c2) {
        return str == null ? null : str.replace(c, c2);
    }

    public static String replaceChars(String str, String str2, String str3) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                if (str3 == null) {
                    str3 = "";
                }
                int length = str3.length();
                int length2 = str.length();
                StringBuilder stringBuilder = new StringBuilder(length2);
                Object obj = null;
                for (int i = 0; i < length2; i++) {
                    char charAt = str.charAt(i);
                    int indexOf = str2.indexOf(charAt);
                    if (indexOf >= 0) {
                        if (indexOf < length) {
                            stringBuilder.append(str3.charAt(indexOf));
                        }
                        obj = 1;
                    } else {
                        stringBuilder.append(charAt);
                    }
                }
                return obj != null ? stringBuilder.toString() : str;
            }
        }
        return str;
    }

    public static String overlay(String str, String str2, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = "";
        }
        int length = str.length();
        if (i < 0) {
            i = 0;
        }
        if (i > length) {
            i = length;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 > length) {
            i2 = length;
        }
        if (i > i2) {
            int i3 = i2;
            i2 = i;
            i = i3;
        }
        StringBuilder stringBuilder = new StringBuilder((((length + i) - i2) + str2.length()) + 1);
        stringBuilder.append(str.substring(0, i));
        stringBuilder.append(str2);
        stringBuilder.append(str.substring(i2));
        return stringBuilder.toString();
    }

    public static String chomp(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            char charAt = str.charAt(0);
            if (charAt != CharUtils.CR) {
                if (charAt != '\n') {
                    return str;
                }
            }
            return "";
        }
        int length = str.length() - 1;
        char charAt2 = str.charAt(length);
        if (charAt2 == '\n') {
            if (str.charAt(length - 1) == CharUtils.CR) {
                length--;
            }
        } else if (charAt2 != CharUtils.CR) {
            length++;
        }
        return str.substring(0, length);
    }

    @Deprecated
    public static String chomp(String str, String str2) {
        return removeEnd(str, str2);
    }

    public static String chop(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length < 2) {
            return "";
        }
        length--;
        String substring = str.substring(0, length);
        if (str.charAt(length) == 10) {
            length--;
            if (substring.charAt(length) == 13) {
                return substring.substring(0, length);
            }
        }
        return substring;
    }

    public static String repeat(String str, int i) {
        if (str == null) {
            return null;
        }
        if (i <= 0) {
            return "";
        }
        int length = str.length();
        if (i != 1) {
            if (length != 0) {
                int i2 = 0;
                if (length == 1 && i <= 8192) {
                    return repeat(str.charAt(0), i);
                }
                int i3 = length * i;
                switch (length) {
                    case 1:
                        return repeat(str.charAt(0), i);
                    case 2:
                        char charAt = str.charAt(0);
                        str = str.charAt(1);
                        char[] cArr = new char[i3];
                        for (i = (i * 2) - 2; i >= 0; i = (i - 1) - 1) {
                            cArr[i] = charAt;
                            cArr[i + 1] = str;
                        }
                        return new String(cArr);
                    default:
                        StringBuilder stringBuilder = new StringBuilder(i3);
                        while (i2 < i) {
                            stringBuilder.append(str);
                            i2++;
                        }
                        return stringBuilder.toString();
                }
            }
        }
        return str;
    }

    public static String repeat(String str, String str2, int i) {
        if (str != null) {
            if (str2 != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(str2);
                return removeEnd(repeat(stringBuilder.toString(), i), str2);
            }
        }
        return repeat(str, i);
    }

    public static String repeat(char c, int i) {
        if (i <= 0) {
            return "";
        }
        char[] cArr = new char[i];
        for (i--; i >= 0; i--) {
            cArr[i] = c;
        }
        return new String(cArr);
    }

    public static String rightPad(String str, int i) {
        return rightPad(str, i, ' ');
    }

    public static String rightPad(String str, int i, char c) {
        if (str == null) {
            return null;
        }
        int length = i - str.length();
        if (length <= 0) {
            return str;
        }
        if (length > 8192) {
            return rightPad(str, i, String.valueOf(c));
        }
        return str.concat(repeat(c, length));
    }

    public static String rightPad(String str, int i, String str2) {
        if (str == null) {
            return null;
        }
        if (isEmpty(str2)) {
            str2 = SPACE;
        }
        int length = str2.length();
        int length2 = i - str.length();
        if (length2 <= 0) {
            return str;
        }
        int i2 = 0;
        if (length == 1 && length2 <= 8192) {
            return rightPad(str, i, str2.charAt(0));
        }
        if (length2 == length) {
            return str.concat(str2);
        }
        if (length2 < length) {
            return str.concat(str2.substring(0, length2));
        }
        i = new char[length2];
        str2 = str2.toCharArray();
        while (i2 < length2) {
            i[i2] = str2[i2 % length];
            i2++;
        }
        return str.concat(new String(i));
    }

    public static String leftPad(String str, int i) {
        return leftPad(str, i, ' ');
    }

    public static String leftPad(String str, int i, char c) {
        if (str == null) {
            return null;
        }
        int length = i - str.length();
        if (length <= 0) {
            return str;
        }
        if (length > 8192) {
            return leftPad(str, i, String.valueOf(c));
        }
        return repeat(c, length).concat(str);
    }

    public static String leftPad(String str, int i, String str2) {
        if (str == null) {
            return null;
        }
        if (isEmpty(str2)) {
            str2 = SPACE;
        }
        int length = str2.length();
        int length2 = i - str.length();
        if (length2 <= 0) {
            return str;
        }
        int i2 = 0;
        if (length == 1 && length2 <= 8192) {
            return leftPad(str, i, str2.charAt(0));
        }
        if (length2 == length) {
            return str2.concat(str);
        }
        if (length2 < length) {
            return str2.substring(0, length2).concat(str);
        }
        i = new char[length2];
        str2 = str2.toCharArray();
        while (i2 < length2) {
            i[i2] = str2[i2 % length];
            i2++;
        }
        return new String(i).concat(str);
    }

    public static int length(CharSequence charSequence) {
        return charSequence == null ? null : charSequence.length();
    }

    public static String center(String str, int i) {
        return center(str, i, ' ');
    }

    public static String center(String str, int i, char c) {
        if (str != null) {
            if (i > 0) {
                int length = str.length();
                int i2 = i - length;
                if (i2 <= 0) {
                    return str;
                }
                return rightPad(leftPad(str, length + (i2 / 2), c), i, c);
            }
        }
        return str;
    }

    public static String center(String str, int i, String str2) {
        if (str != null) {
            if (i > 0) {
                if (isEmpty(str2)) {
                    str2 = SPACE;
                }
                int length = str.length();
                int i2 = i - length;
                if (i2 <= 0) {
                    return str;
                }
                return rightPad(leftPad(str, length + (i2 / 2), str2), i, str2);
            }
        }
        return str;
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String upperCase(String str, Locale locale) {
        return str == null ? null : str.toUpperCase(locale);
    }

    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String lowerCase(String str, Locale locale) {
        return str == null ? null : str.toLowerCase(locale);
    }

    public static String capitalize(String str) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                char charAt = str.charAt(0);
                char toTitleCase = Character.toTitleCase(charAt);
                if (charAt == toTitleCase) {
                    return str;
                }
                char[] cArr = new char[length];
                cArr[0] = toTitleCase;
                str.getChars(1, length, cArr, 1);
                return String.valueOf(cArr);
            }
        }
        return str;
    }

    public static String uncapitalize(String str) {
        if (str != null) {
            int length = str.length();
            if (length != 0) {
                char charAt = str.charAt(0);
                char toLowerCase = Character.toLowerCase(charAt);
                if (charAt == toLowerCase) {
                    return str;
                }
                char[] cArr = new char[length];
                cArr[0] = toLowerCase;
                str.getChars(1, length, cArr, 1);
                return String.valueOf(cArr);
            }
        }
        return str;
    }

    public static String swapCase(String str) {
        if (isEmpty(str)) {
            return str;
        }
        str = str.toCharArray();
        for (int i = 0; i < str.length; i++) {
            char c = str[i];
            if (Character.isUpperCase(c)) {
                str[i] = Character.toLowerCase(c);
            } else if (Character.isTitleCase(c)) {
                str[i] = Character.toLowerCase(c);
            } else if (Character.isLowerCase(c)) {
                str[i] = Character.toUpperCase(c);
            }
        }
        return new String(str);
    }

    public static int countMatches(CharSequence charSequence, CharSequence charSequence2) {
        int i = 0;
        if (!isEmpty(charSequence)) {
            if (!isEmpty(charSequence2)) {
                int i2 = 0;
                while (true) {
                    i = CharSequenceUtils.indexOf(charSequence, charSequence2, i);
                    if (i == -1) {
                        return i2;
                    }
                    i2++;
                    i += charSequence2.length();
                }
            }
        }
        return 0;
    }

    public static int countMatches(CharSequence charSequence, char c) {
        int i = 0;
        if (isEmpty(charSequence)) {
            return 0;
        }
        int i2 = 0;
        while (i < charSequence.length()) {
            if (c == charSequence.charAt(i)) {
                i2++;
            }
            i++;
        }
        return i2;
    }

    public static boolean isAlpha(CharSequence charSequence) {
        if (isEmpty(charSequence)) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaSpace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            if (!Character.isLetter(charSequence.charAt(i)) && charSequence.charAt(i) != ' ') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isAlphanumeric(CharSequence charSequence) {
        if (isEmpty(charSequence)) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumericSpace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            if (!Character.isLetterOrDigit(charSequence.charAt(i)) && charSequence.charAt(i) != ' ') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isAsciiPrintable(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!CharUtils.isAsciiPrintable(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(CharSequence charSequence) {
        if (isEmpty(charSequence)) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericSpace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            if (!Character.isDigit(charSequence.charAt(i)) && charSequence.charAt(i) != ' ') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isWhitespace(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllLowerCase(CharSequence charSequence) {
        if (charSequence != null) {
            if (!isEmpty(charSequence)) {
                int length = charSequence.length();
                for (int i = 0; i < length; i++) {
                    if (!Character.isLowerCase(charSequence.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isAllUpperCase(CharSequence charSequence) {
        if (charSequence != null) {
            if (!isEmpty(charSequence)) {
                int length = charSequence.length();
                for (int i = 0; i < length; i++) {
                    if (!Character.isUpperCase(charSequence.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static <T extends CharSequence> T defaultIfBlank(T t, T t2) {
        return isBlank(t) ? t2 : t;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T t, T t2) {
        return isEmpty(t) ? t2 : t;
    }

    public static String rotate(String str, int i) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (!(i == 0 || length == 0)) {
            i %= length;
            if (i != 0) {
                StringBuilder stringBuilder = new StringBuilder(length);
                i = -i;
                stringBuilder.append(substring(str, i));
                stringBuilder.append(substring(str, 0, i));
                return stringBuilder.toString();
            }
        }
        return str;
    }

    public static String reverse(String str) {
        return str == null ? null : new StringBuilder(str).reverse().toString();
    }

    public static String reverseDelimited(String str, char c) {
        if (str == null) {
            return null;
        }
        Object[] split = split(str, c);
        ArrayUtils.reverse(split);
        return join(split, c);
    }

    public static String abbreviate(String str, int i) {
        return abbreviate(str, 0, i);
    }

    public static String abbreviate(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (i2 < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        } else if (str.length() <= i2) {
            return str;
        } else {
            if (i > str.length()) {
                i = str.length();
            }
            int i3 = i2 - 3;
            if (str.length() - i < i3) {
                i = str.length() - i3;
            }
            if (i <= 4) {
                i = new StringBuilder();
                i.append(str.substring(0, i3));
                i.append("...");
                return i.toString();
            } else if (i2 < 7) {
                throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
            } else if ((i2 + i) - 3 < str.length()) {
                i2 = new StringBuilder();
                i2.append("...");
                i2.append(abbreviate(str.substring(i), i3));
                return i2.toString();
            } else {
                i = new StringBuilder();
                i.append("...");
                i.append(str.substring(str.length() - i3));
                return i.toString();
            }
        }
    }

    public static String abbreviateMiddle(String str, String str2, int i) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                if (i < str.length()) {
                    if (i >= str2.length() + 2) {
                        int length = i - str2.length();
                        int i2 = length / 2;
                        length = (length % 2) + i2;
                        int length2 = str.length() - i2;
                        StringBuilder stringBuilder = new StringBuilder(i);
                        stringBuilder.append(str.substring(0, length));
                        stringBuilder.append(str2);
                        stringBuilder.append(str.substring(length2));
                        return stringBuilder.toString();
                    }
                }
                return str;
            }
        }
        return str;
    }

    public static String difference(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        str = indexOfDifference(str, str2);
        if (str == -1) {
            return "";
        }
        return str2.substring(str);
    }

    public static int indexOfDifference(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return -1;
        }
        int i = 0;
        if (charSequence != null) {
            if (charSequence2 != null) {
                while (i < charSequence.length() && i < charSequence2.length()) {
                    if (charSequence.charAt(i) != charSequence2.charAt(i)) {
                        break;
                    }
                    i++;
                }
                if (i >= charSequence2.length()) {
                    if (i >= charSequence.length()) {
                        return -1;
                    }
                }
                return i;
            }
        }
        return 0;
    }

    public static int indexOfDifference(CharSequence... charSequenceArr) {
        if (charSequenceArr != null) {
            if (charSequenceArr.length > 1) {
                int i;
                int min;
                int length = charSequenceArr.length;
                Object obj = 1;
                int i2 = Integer.MAX_VALUE;
                int i3 = 0;
                Object obj2 = null;
                for (i = 0; i < length; i++) {
                    if (charSequenceArr[i] == null) {
                        i2 = 0;
                        obj2 = 1;
                    } else {
                        min = Math.min(charSequenceArr[i].length(), i2);
                        i3 = Math.max(charSequenceArr[i].length(), i3);
                        i2 = min;
                        obj = null;
                    }
                }
                if (obj == null) {
                    if (i3 != 0 || r8 != null) {
                        if (i2 == 0) {
                            return 0;
                        }
                        min = -1;
                        for (i = 0; i < i2; i++) {
                            char charAt = charSequenceArr[0].charAt(i);
                            for (int i4 = 1; i4 < length; i4++) {
                                if (charSequenceArr[i4].charAt(i) != charAt) {
                                    min = i;
                                    break;
                                }
                            }
                            if (min != -1) {
                                break;
                            }
                        }
                        return (min != -1 || i2 == i3) ? min : i2;
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public static String getCommonPrefix(String... strArr) {
        if (strArr != null) {
            if (strArr.length != 0) {
                int indexOfDifference = indexOfDifference(strArr);
                if (indexOfDifference == -1) {
                    if (strArr[0] == null) {
                        return "";
                    }
                    return strArr[0];
                } else if (indexOfDifference == 0) {
                    return "";
                } else {
                    return strArr[0].substring(0, indexOfDifference);
                }
            }
        }
        return "";
    }

    public static int getLevenshteinDistance(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                int length = charSequence.length();
                int length2 = charSequence2.length();
                if (length == 0) {
                    return length2;
                }
                if (length2 == 0) {
                    return length;
                }
                if (length > length2) {
                    int i = length2;
                    length2 = charSequence.length();
                    length = i;
                } else {
                    CharSequence charSequence3 = charSequence2;
                    charSequence2 = charSequence;
                    charSequence = charSequence3;
                }
                int i2 = length + 1;
                int[] iArr = new int[i2];
                int[] iArr2 = new int[i2];
                for (int i3 = 0; i3 <= length; i3++) {
                    iArr[i3] = i3;
                }
                int[] iArr3 = iArr;
                iArr = iArr2;
                i2 = 1;
                while (i2 <= length2) {
                    char charAt = charSequence.charAt(i2 - 1);
                    iArr[0] = i2;
                    for (int i4 = 1; i4 <= length; i4++) {
                        int i5 = i4 - 1;
                        iArr[i4] = Math.min(Math.min(iArr[i5] + 1, iArr3[i4] + 1), iArr3[i5] + (charSequence2.charAt(i5) == charAt ? 0 : 1));
                    }
                    i2++;
                    int[] iArr4 = iArr3;
                    iArr3 = iArr;
                    iArr = iArr4;
                }
                return iArr3[length];
            }
        }
        throw new IllegalArgumentException("Strings must not be null");
    }

    public static int getLevenshteinDistance(CharSequence charSequence, CharSequence charSequence2, int i) {
        int i2 = i;
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (i2 < 0) {
                    throw new IllegalArgumentException("Threshold must not be negative");
                }
                int length = charSequence.length();
                int length2 = charSequence2.length();
                if (length == 0) {
                    if (length2 > i2) {
                        length2 = -1;
                    }
                    return length2;
                } else if (length2 == 0) {
                    if (length > i2) {
                        length = -1;
                    }
                    return length;
                } else if (Math.abs(length - length2) > i2) {
                    return -1;
                } else {
                    CharSequence charSequence3;
                    CharSequence charSequence4;
                    if (length > length2) {
                        charSequence3 = charSequence;
                        charSequence4 = charSequence2;
                        int i3 = length2;
                        length2 = charSequence.length();
                        length = i3;
                    } else {
                        charSequence4 = charSequence;
                        charSequence3 = charSequence2;
                    }
                    int i4 = length + 1;
                    int[] iArr = new int[i4];
                    int[] iArr2 = new int[i4];
                    int min = Math.min(length, i2) + 1;
                    int i5 = 0;
                    for (int i6 = 0; i6 < min; i6++) {
                        iArr[i6] = i6;
                    }
                    int i7 = Integer.MAX_VALUE;
                    Arrays.fill(iArr, min, iArr.length, Integer.MAX_VALUE);
                    Arrays.fill(iArr2, Integer.MAX_VALUE);
                    int[] iArr3 = iArr;
                    iArr = iArr2;
                    i4 = 1;
                    while (i4 <= length2) {
                        int i8;
                        char charAt = charSequence3.charAt(i4 - 1);
                        iArr[i5] = i4;
                        int max = Math.max(1, i4 - i2);
                        if (i4 > i7 - i2) {
                            i8 = length;
                        } else {
                            i8 = Math.min(length, i4 + i2);
                        }
                        if (max > i8) {
                            return -1;
                        }
                        if (max > 1) {
                            iArr[max - 1] = i7;
                        }
                        while (max <= i8) {
                            int i9 = max - 1;
                            if (charSequence4.charAt(i9) == charAt) {
                                iArr[max] = iArr3[i9];
                            } else {
                                iArr[max] = Math.min(Math.min(iArr[i9], iArr3[max]), iArr3[i9]) + 1;
                            }
                            max++;
                        }
                        i4++;
                        i5 = 0;
                        i7 = Integer.MAX_VALUE;
                        int[] iArr4 = iArr3;
                        iArr3 = iArr;
                        iArr = iArr4;
                    }
                    if (iArr3[length] <= i2) {
                        return iArr3[length];
                    }
                    return -1;
                }
            }
        }
        throw new IllegalArgumentException("Strings must not be null");
    }

    public static double getJaroWinklerDistance(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                int[] matches = matches(charSequence, charSequence2);
                double d = (double) matches[0];
                if (d == 0.0d) {
                    return 0.0d;
                }
                double length = (((d / ((double) charSequence.length())) + (d / ((double) charSequence2.length()))) + ((d - ((double) matches[1])) / d)) / 0;
                if (length >= 1717986918) {
                    length += (Math.min(-1717986918, 1.0d / ((double) matches[3])) * ((double) matches[2])) * (1.0d - length);
                }
                return ((double) Math.round(length * 100.0d)) / 100.0d;
            }
        }
        throw new IllegalArgumentException("Strings must not be null");
    }

    private static int[] matches(CharSequence charSequence, CharSequence charSequence2) {
        CharSequence charSequence3;
        CharSequence charSequence4;
        int max;
        int i;
        if (charSequence.length() > charSequence2.length()) {
            charSequence3 = charSequence;
            charSequence4 = charSequence2;
        } else {
            charSequence4 = charSequence;
            charSequence3 = charSequence2;
        }
        int max2 = Math.max((charSequence3.length() / 2) - 1, 0);
        int[] iArr = new int[charSequence4.length()];
        Arrays.fill(iArr, -1);
        boolean[] zArr = new boolean[charSequence3.length()];
        int i2 = 0;
        for (int i3 = 0; i3 < charSequence4.length(); i3++) {
            char charAt = charSequence4.charAt(i3);
            max = Math.max(i3 - max2, 0);
            int min = Math.min((i3 + max2) + 1, charSequence3.length());
            while (max < min) {
                if (!zArr[max] && charAt == charSequence3.charAt(max)) {
                    iArr[i3] = max;
                    zArr[max] = true;
                    i2++;
                    break;
                }
                max++;
            }
        }
        char[] cArr = new char[i2];
        char[] cArr2 = new char[i2];
        max = 0;
        for (int i4 = 0; i4 < charSequence4.length(); i4++) {
            if (iArr[i4] != -1) {
                cArr[max] = charSequence4.charAt(i4);
                max++;
            }
        }
        int i5 = 0;
        for (i = 0; i < charSequence3.length(); i++) {
            if (zArr[i]) {
                cArr2[i5] = charSequence3.charAt(i);
                i5++;
            }
        }
        i5 = 0;
        for (i = 0; i < cArr.length; i++) {
            if (cArr[i] != cArr2[i]) {
                i5++;
            }
        }
        max2 = 0;
        i = 0;
        while (max2 < charSequence4.length() && charSequence.charAt(max2) == charSequence2.charAt(max2)) {
            i++;
            max2++;
        }
        return new int[]{i2, i5 / 2, i, charSequence3.length()};
    }

    public static int getFuzzyDistance(CharSequence charSequence, CharSequence charSequence2, Locale locale) {
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (locale == null) {
                    throw new IllegalArgumentException("Locale must not be null");
                }
                charSequence = charSequence.toString().toLowerCase(locale);
                charSequence2 = charSequence2.toString().toLowerCase(locale);
                locale = null;
                int i = 0;
                int i2 = 0;
                int i3 = Integer.MIN_VALUE;
                while (locale < charSequence2.length()) {
                    char charAt = charSequence2.charAt(locale);
                    int i4 = i3;
                    i3 = i;
                    Object obj = null;
                    while (i2 < charSequence.length() && r1 == null) {
                        if (charAt == charSequence.charAt(i2)) {
                            i3++;
                            if (i4 + 1 == i2) {
                                i3 += 2;
                            }
                            obj = 1;
                            i4 = i2;
                        }
                        i2++;
                    }
                    locale++;
                    i = i3;
                    i3 = i4;
                }
                return i;
            }
        }
        throw new IllegalArgumentException("Strings must not be null");
    }

    public static boolean startsWith(CharSequence charSequence, CharSequence charSequence2) {
        return startsWith(charSequence, charSequence2, false);
    }

    public static boolean startsWithIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        return startsWith(charSequence, charSequence2, true);
    }

    private static boolean startsWith(CharSequence charSequence, CharSequence charSequence2, boolean z) {
        boolean z2 = false;
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (charSequence2.length() > charSequence.length()) {
                    return false;
                }
                return CharSequenceUtils.regionMatches(charSequence, z, 0, charSequence2, 0, charSequence2.length());
            }
        }
        if (charSequence == null && charSequence2 == null) {
            z2 = true;
        }
        return z2;
    }

    public static boolean startsWithAny(CharSequence charSequence, CharSequence... charSequenceArr) {
        if (!isEmpty(charSequence)) {
            if (!ArrayUtils.isEmpty((Object[]) charSequenceArr)) {
                for (CharSequence startsWith : charSequenceArr) {
                    if (startsWith(charSequence, startsWith)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static boolean endsWith(CharSequence charSequence, CharSequence charSequence2) {
        return endsWith(charSequence, charSequence2, false);
    }

    public static boolean endsWithIgnoreCase(CharSequence charSequence, CharSequence charSequence2) {
        return endsWith(charSequence, charSequence2, true);
    }

    private static boolean endsWith(CharSequence charSequence, CharSequence charSequence2, boolean z) {
        boolean z2 = false;
        if (charSequence != null) {
            if (charSequence2 != null) {
                if (charSequence2.length() > charSequence.length()) {
                    return false;
                }
                return CharSequenceUtils.regionMatches(charSequence, z, charSequence.length() - charSequence2.length(), charSequence2, 0, charSequence2.length());
            }
        }
        if (charSequence == null && charSequence2 == null) {
            z2 = true;
        }
        return z2;
    }

    public static String normalizeSpace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int length = str.length();
        char[] cArr = new char[length];
        int i = 1;
        Object obj = 1;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            char charAt = str.charAt(i4);
            if (Character.isWhitespace(charAt)) {
                if (i3 == 0 && obj == null) {
                    int i5 = i2 + 1;
                    cArr[i2] = SPACE.charAt(0);
                    i2 = i5;
                }
                i3++;
            } else {
                int i6 = i2 + 1;
                if (charAt == Typography.nbsp) {
                    charAt = ' ';
                }
                cArr[i2] = charAt;
                i2 = i6;
                obj = null;
                i3 = 0;
            }
        }
        if (obj != null) {
            return "";
        }
        if (i3 <= 0) {
            i = 0;
        }
        return new String(cArr, 0, i2 - i).trim();
    }

    public static boolean endsWithAny(CharSequence charSequence, CharSequence... charSequenceArr) {
        if (!isEmpty(charSequence)) {
            if (!ArrayUtils.isEmpty((Object[]) charSequenceArr)) {
                for (CharSequence endsWith : charSequenceArr) {
                    if (endsWith(charSequence, endsWith)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private static String appendIfMissing(String str, CharSequence charSequence, boolean z, CharSequence... charSequenceArr) {
        if (!(str == null || isEmpty(charSequence))) {
            if (!endsWith(str, charSequence, z)) {
                if (charSequenceArr != null && charSequenceArr.length > 0) {
                    for (CharSequence endsWith : charSequenceArr) {
                        if (endsWith(str, endsWith, z)) {
                            return str;
                        }
                    }
                }
                z = new StringBuilder();
                z.append(str);
                z.append(charSequence.toString());
                return z.toString();
            }
        }
        return str;
    }

    public static String appendIfMissing(String str, CharSequence charSequence, CharSequence... charSequenceArr) {
        return appendIfMissing(str, charSequence, false, charSequenceArr);
    }

    public static String appendIfMissingIgnoreCase(String str, CharSequence charSequence, CharSequence... charSequenceArr) {
        return appendIfMissing(str, charSequence, true, charSequenceArr);
    }

    private static String prependIfMissing(String str, CharSequence charSequence, boolean z, CharSequence... charSequenceArr) {
        if (!(str == null || isEmpty(charSequence))) {
            if (!startsWith(str, charSequence, z)) {
                if (charSequenceArr != null && charSequenceArr.length > 0) {
                    for (CharSequence startsWith : charSequenceArr) {
                        if (startsWith(str, startsWith, z)) {
                            return str;
                        }
                    }
                }
                z = new StringBuilder();
                z.append(charSequence.toString());
                z.append(str);
                return z.toString();
            }
        }
        return str;
    }

    public static String prependIfMissing(String str, CharSequence charSequence, CharSequence... charSequenceArr) {
        return prependIfMissing(str, charSequence, false, charSequenceArr);
    }

    public static String prependIfMissingIgnoreCase(String str, CharSequence charSequence, CharSequence... charSequenceArr) {
        return prependIfMissing(str, charSequence, true, charSequenceArr);
    }

    @Deprecated
    public static String toString(byte[] bArr, String str) throws UnsupportedEncodingException {
        return str != null ? new String(bArr, str) : new String(bArr, Charset.defaultCharset());
    }

    public static String toEncodedString(byte[] bArr, Charset charset) {
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        return new String(bArr, charset);
    }

    public static String wrap(String str, char c) {
        if (!isEmpty(str)) {
            if (c != '\u0000') {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(c);
                stringBuilder.append(str);
                stringBuilder.append(c);
                return stringBuilder.toString();
            }
        }
        return str;
    }

    public static String wrap(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                return str2.concat(str).concat(str2);
            }
        }
        return str;
    }

    public static String wrapIfMissing(String str, char c) {
        if (!isEmpty(str)) {
            if (c != '\u0000') {
                StringBuilder stringBuilder = new StringBuilder(str.length() + 2);
                if (str.charAt(0) != c) {
                    stringBuilder.append(c);
                }
                stringBuilder.append(str);
                if (str.charAt(str.length() - 1) != c) {
                    stringBuilder.append(c);
                }
                return stringBuilder.toString();
            }
        }
        return str;
    }

    public static String wrapIfMissing(String str, String str2) {
        if (!isEmpty(str)) {
            if (!isEmpty(str2)) {
                StringBuilder stringBuilder = new StringBuilder((str.length() + str2.length()) + str2.length());
                if (!str.startsWith(str2)) {
                    stringBuilder.append(str2);
                }
                stringBuilder.append(str);
                if (str.endsWith(str2) == null) {
                    stringBuilder.append(str2);
                }
                return stringBuilder.toString();
            }
        }
        return str;
    }
}
