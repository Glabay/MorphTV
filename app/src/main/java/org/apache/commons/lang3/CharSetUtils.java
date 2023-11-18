package org.apache.commons.lang3;

public class CharSetUtils {
    public static String squeeze(String str, String... strArr) {
        if (!StringUtils.isEmpty(str)) {
            if (!deepEmpty(strArr)) {
                strArr = CharSet.getInstance(strArr);
                StringBuilder stringBuilder = new StringBuilder(str.length());
                str = str.toCharArray();
                int length = str.length;
                char c = str[0];
                stringBuilder.append(c);
                Character ch = null;
                Character ch2 = null;
                for (int i = 1; i < length; i++) {
                    char c2 = str[i];
                    if (c2 == c) {
                        if (ch != null && c2 == ch.charValue()) {
                        } else if (ch2 == null || c2 != ch2.charValue()) {
                            if (strArr.contains(c2)) {
                                ch = Character.valueOf(c2);
                            } else {
                                ch2 = Character.valueOf(c2);
                            }
                        }
                    }
                    stringBuilder.append(c2);
                    c = c2;
                }
                return stringBuilder.toString();
            }
        }
        return str;
    }

    public static boolean containsAny(String str, String... strArr) {
        if (!StringUtils.isEmpty(str)) {
            if (!deepEmpty(strArr)) {
                strArr = CharSet.getInstance(strArr);
                for (char contains : str.toCharArray()) {
                    if (strArr.contains(contains)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static int count(String str, String... strArr) {
        int i = 0;
        if (!StringUtils.isEmpty(str)) {
            if (!deepEmpty(strArr)) {
                strArr = CharSet.getInstance(strArr);
                str = str.toCharArray();
                int length = str.length;
                int i2 = 0;
                while (i < length) {
                    if (strArr.contains(str[i])) {
                        i2++;
                    }
                    i++;
                }
                return i2;
            }
        }
        return 0;
    }

    public static String keep(String str, String... strArr) {
        if (str == null) {
            return null;
        }
        if (!str.isEmpty()) {
            if (!deepEmpty(strArr)) {
                return modify(str, strArr, true);
            }
        }
        return "";
    }

    public static String delete(String str, String... strArr) {
        if (!StringUtils.isEmpty(str)) {
            if (!deepEmpty(strArr)) {
                return modify(str, strArr, false);
            }
        }
        return str;
    }

    private static String modify(String str, String[] strArr, boolean z) {
        strArr = CharSet.getInstance(strArr);
        StringBuilder stringBuilder = new StringBuilder(str.length());
        str = str.toCharArray();
        int length = str.length;
        for (int i = 0; i < length; i++) {
            if (strArr.contains(str[i]) == z) {
                stringBuilder.append(str[i]);
            }
        }
        return stringBuilder.toString();
    }

    private static boolean deepEmpty(String[] strArr) {
        if (strArr != null) {
            for (CharSequence isNotEmpty : strArr) {
                if (StringUtils.isNotEmpty(isNotEmpty)) {
                    return false;
                }
            }
        }
        return 1;
    }
}
