package org.apache.commons.lang3.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class WordUtils {
    public static String wrap(String str, int i) {
        return wrap(str, i, null, false);
    }

    public static String wrap(String str, int i, String str2, boolean z) {
        return wrap(str, i, str2, z, StringUtils.SPACE);
    }

    public static String wrap(String str, int i, String str2, boolean z, String str3) {
        if (str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = SystemUtils.LINE_SEPARATOR;
        }
        if (i < 1) {
            i = 1;
        }
        if (StringUtils.isBlank(str3)) {
            str3 = StringUtils.SPACE;
        }
        str3 = Pattern.compile(str3);
        int length = str.length();
        int i2 = 0;
        StringBuilder stringBuilder = new StringBuilder(length + 32);
        while (i2 < length) {
            int i3 = -1;
            int i4 = i2 + i;
            Matcher matcher = str3.matcher(str.substring(i2, Math.min(i4 + 1, length)));
            if (matcher.find()) {
                if (matcher.start() == 0) {
                    i2 += matcher.end();
                } else {
                    i3 = matcher.start();
                }
            }
            if (length - i2 <= i) {
                break;
            }
            while (matcher.find()) {
                i3 = matcher.start() + i2;
            }
            if (i3 >= i2) {
                stringBuilder.append(str.substring(i2, i3));
                stringBuilder.append(str2);
                i2 = i3 + 1;
            } else if (z) {
                stringBuilder.append(str.substring(i2, i4));
                stringBuilder.append(str2);
                i2 = i4;
            } else {
                Matcher matcher2 = str3.matcher(str.substring(i4));
                if (matcher2.find()) {
                    i3 = (matcher2.start() + i2) + i;
                }
                if (i3 >= 0) {
                    stringBuilder.append(str.substring(i2, i3));
                    stringBuilder.append(str2);
                    i2 = i3 + 1;
                } else {
                    stringBuilder.append(str.substring(i2));
                    i2 = length;
                }
            }
        }
        stringBuilder.append(str.substring(i2));
        return stringBuilder.toString();
    }

    public static String capitalize(String str) {
        return capitalize(str, null);
    }

    public static String capitalize(String str, char... cArr) {
        int length = cArr == null ? -1 : cArr.length;
        if (!StringUtils.isEmpty(str)) {
            if (length != 0) {
                str = str.toCharArray();
                Object obj = 1;
                for (int i = 0; i < str.length; i++) {
                    char c = str[i];
                    if (isDelimiter(c, cArr)) {
                        obj = 1;
                    } else if (obj != null) {
                        str[i] = Character.toTitleCase(c);
                        obj = null;
                    }
                }
                return new String(str);
            }
        }
        return str;
    }

    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    public static String capitalizeFully(String str, char... cArr) {
        int length = cArr == null ? -1 : cArr.length;
        if (!StringUtils.isEmpty(str)) {
            if (length != 0) {
                return capitalize(str.toLowerCase(), cArr);
            }
        }
        return str;
    }

    public static String uncapitalize(String str) {
        return uncapitalize(str, null);
    }

    public static String uncapitalize(String str, char... cArr) {
        int length = cArr == null ? -1 : cArr.length;
        if (!StringUtils.isEmpty(str)) {
            if (length != 0) {
                str = str.toCharArray();
                Object obj = 1;
                for (int i = 0; i < str.length; i++) {
                    char c = str[i];
                    if (isDelimiter(c, cArr)) {
                        obj = 1;
                    } else if (obj != null) {
                        str[i] = Character.toLowerCase(c);
                        obj = null;
                    }
                }
                return new String(str);
            }
        }
        return str;
    }

    public static String swapCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        str = str.toCharArray();
        boolean z = true;
        for (int i = 0; i < str.length; i++) {
            char c = str[i];
            if (Character.isUpperCase(c)) {
                str[i] = Character.toLowerCase(c);
            } else if (Character.isTitleCase(c)) {
                str[i] = Character.toLowerCase(c);
            } else {
                if (!Character.isLowerCase(c)) {
                    z = Character.isWhitespace(c);
                } else if (z) {
                    str[i] = Character.toTitleCase(c);
                } else {
                    str[i] = Character.toUpperCase(c);
                }
            }
            z = false;
        }
        return new String(str);
    }

    public static String initials(String str) {
        return initials(str, null);
    }

    public static String initials(String str, char... cArr) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (cArr != null && cArr.length == 0) {
            return "";
        }
        int length = str.length();
        char[] cArr2 = new char[((length / 2) + 1)];
        int i = 0;
        Object obj = 1;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (isDelimiter(charAt, cArr)) {
                obj = 1;
            } else if (obj != null) {
                int i3 = i + 1;
                cArr2[i] = charAt;
                i = i3;
                obj = null;
            }
        }
        return new String(cArr2, 0, i);
    }

    public static boolean containsAllWords(CharSequence charSequence, CharSequence... charSequenceArr) {
        if (!StringUtils.isEmpty(charSequence)) {
            if (!ArrayUtils.isEmpty((Object[]) charSequenceArr)) {
                for (CharSequence charSequence2 : charSequenceArr) {
                    if (StringUtils.isBlank(charSequence2)) {
                        return false;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(".*\\b");
                    stringBuilder.append(charSequence2);
                    stringBuilder.append("\\b.*");
                    if (!Pattern.compile(stringBuilder.toString()).matcher(charSequence).matches()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isDelimiter(char c, char[] cArr) {
        if (cArr == null) {
            return Character.isWhitespace(c);
        }
        for (char c2 : cArr) {
            if (c == c2) {
                return true;
            }
        }
        return false;
    }
}
