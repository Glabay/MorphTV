package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class FilenameUtils {
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final int NOT_FOUND = -1;
    private static final char OTHER_SEPARATOR;
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

    private static boolean isSeparator(char c) {
        if (c != '/') {
            if (c != '\\') {
                return false;
            }
        }
        return true;
    }

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        } else {
            OTHER_SEPARATOR = '\\';
        }
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    public static String normalize(String str) {
        return doNormalize(str, SYSTEM_SEPARATOR, true);
    }

    public static String normalize(String str, boolean z) {
        return doNormalize(str, z ? true : true, true);
    }

    public static String normalizeNoEndSeparator(String str) {
        return doNormalize(str, SYSTEM_SEPARATOR, false);
    }

    public static String normalizeNoEndSeparator(String str, boolean z) {
        return doNormalize(str, z ? true : true, false);
    }

    private static String doNormalize(String str, char c, boolean z) {
        String str2 = str;
        char c2 = c;
        if (str2 == null) {
            return null;
        }
        failIfNullBytePresent(str);
        int length = str.length();
        if (length == 0) {
            return str2;
        }
        int prefixLength = getPrefixLength(str);
        if (prefixLength < 0) {
            return null;
        }
        int i;
        Object obj;
        Object obj2 = new char[(length + 2)];
        str2.getChars(0, str.length(), obj2, 0);
        char c3 = c2 == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
        for (int i2 = 0; i2 < obj2.length; i2++) {
            if (obj2[i2] == c3) {
                obj2[i2] = c2;
            }
        }
        if (obj2[length - 1] != c2) {
            i = length + 1;
            obj2[length] = c2;
            obj = null;
        } else {
            i = length;
            obj = 1;
        }
        int i3 = prefixLength + 1;
        int i4 = i;
        i = i3;
        while (i < i4) {
            if (obj2[i] == c2) {
                int i5 = i - 1;
                if (obj2[i5] == c2) {
                    System.arraycopy(obj2, i, obj2, i5, i4 - i);
                    i4--;
                    i--;
                }
            }
            i++;
        }
        i = i3;
        while (i < i4) {
            if (obj2[i] == c2) {
                int i6 = i - 1;
                if (obj2[i6] == '.' && (i == i3 || obj2[i - 2] == c2)) {
                    if (i == i4 - 1) {
                        obj = 1;
                    }
                    System.arraycopy(obj2, i + 1, obj2, i6, i4 - i);
                    i4 -= 2;
                    i--;
                }
            }
            i++;
        }
        i = prefixLength + 2;
        Object obj3 = obj;
        length = i;
        while (length < i4) {
            int i7;
            if (obj2[length] != c2 || obj2[length - 1] != '.' || obj2[length - 2] != '.' || (length != i && obj2[length - 3] != c2)) {
                i7 = length;
            } else if (length == i) {
                return null;
            } else {
                int i8;
                if (length == i4 - 1) {
                    obj3 = 1;
                }
                for (i8 = length - 4; i8 >= prefixLength; i8--) {
                    if (obj2[i8] == c2) {
                        i7 = i8 + 1;
                        System.arraycopy(obj2, length + 1, obj2, i7, i4 - length);
                        i4 -= length - i8;
                        break;
                    }
                }
                i8 = length + 1;
                System.arraycopy(obj2, i8, obj2, prefixLength, i4 - length);
                i4 -= i8 - prefixLength;
                i7 = i3;
            }
            length = i7 + 1;
        }
        if (i4 <= 0) {
            return "";
        }
        if (i4 <= prefixLength) {
            return new String(obj2, 0, i4);
        }
        if (obj3 == null || !z) {
            return new String(obj2, 0, i4 - 1);
        }
        return new String(obj2, 0, i4);
    }

    public static String concat(String str, String str2) {
        int prefixLength = getPrefixLength(str2);
        if (prefixLength < 0) {
            return null;
        }
        if (prefixLength > 0) {
            return normalize(str2);
        }
        if (str == null) {
            return null;
        }
        prefixLength = str.length();
        if (prefixLength == 0) {
            return normalize(str2);
        }
        if (isSeparator(str.charAt(prefixLength - 1))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(str2);
            return normalize(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append('/');
        stringBuilder.append(str2);
        return normalize(stringBuilder.toString());
    }

    public static boolean directoryContains(String str, String str2) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("Directory must not be null");
        } else if (str2 == null || IOCase.SYSTEM.checkEquals(str, str2)) {
            return false;
        } else {
            return IOCase.SYSTEM.checkStartsWith(str2, str);
        }
    }

    public static String separatorsToUnix(String str) {
        if (str != null) {
            if (str.indexOf(92) != -1) {
                return str.replace('\\', '/');
            }
        }
        return str;
    }

    public static String separatorsToWindows(String str) {
        if (str != null) {
            if (str.indexOf(47) != -1) {
                return str.replace('/', '\\');
            }
        }
        return str;
    }

    public static String separatorsToSystem(String str) {
        if (str == null) {
            return null;
        }
        if (isSystemWindows()) {
            return separatorsToWindows(str);
        }
        return separatorsToUnix(str);
    }

    public static int getPrefixLength(String str) {
        if (str == null) {
            return -1;
        }
        int length = str.length();
        if (length == 0) {
            return 0;
        }
        char charAt = str.charAt(0);
        if (charAt == ':') {
            return -1;
        }
        if (length == 1) {
            if (charAt == '~') {
                return 2;
            }
            return isSeparator(charAt);
        } else if (charAt == '~') {
            int indexOf = str.indexOf(47, 1);
            str = str.indexOf(92, 1);
            if (indexOf == -1 && str == -1) {
                return length + 1;
            }
            if (indexOf == -1) {
                indexOf = str;
            }
            if (str == -1) {
                str = indexOf;
            }
            return Math.min(indexOf, str) + 1;
        } else {
            char charAt2 = str.charAt(1);
            if (charAt2 == ':') {
                charAt = Character.toUpperCase(charAt);
                if (charAt >= 'A' && charAt <= 'Z') {
                    if (length != 2) {
                        if (isSeparator(str.charAt(2)) != null) {
                            return 3;
                        }
                    }
                    return 2;
                } else if (charAt == '/') {
                    return 1;
                } else {
                    return -1;
                }
            } else if (!isSeparator(charAt) || !isSeparator(charAt2)) {
                return isSeparator(charAt);
            } else {
                length = str.indexOf(47, 2);
                str = str.indexOf(92, 2);
                if (!((length == -1 && str == -1) || length == 2)) {
                    if (str != 2) {
                        if (length == -1) {
                            length = str;
                        }
                        if (str == -1) {
                            str = length;
                        }
                        return Math.min(length, str) + 1;
                    }
                }
                return -1;
            }
        }
    }

    public static int indexOfLastSeparator(String str) {
        if (str == null) {
            return -1;
        }
        return Math.max(str.lastIndexOf(47), str.lastIndexOf(92));
    }

    public static int indexOfExtension(String str) {
        int i = -1;
        if (str == null) {
            return -1;
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (indexOfLastSeparator(str) <= lastIndexOf) {
            i = lastIndexOf;
        }
        return i;
    }

    public static String getPrefix(String str) {
        if (str == null) {
            return null;
        }
        int prefixLength = getPrefixLength(str);
        if (prefixLength < 0) {
            return null;
        }
        if (prefixLength > str.length()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append('/');
            failIfNullBytePresent(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append('/');
            return stringBuilder.toString();
        }
        str = str.substring(0, prefixLength);
        failIfNullBytePresent(str);
        return str;
    }

    public static String getPath(String str) {
        return doGetPath(str, 1);
    }

    public static String getPathNoEndSeparator(String str) {
        return doGetPath(str, 0);
    }

    private static String doGetPath(String str, int i) {
        if (str == null) {
            return null;
        }
        int prefixLength = getPrefixLength(str);
        if (prefixLength < 0) {
            return null;
        }
        int indexOfLastSeparator = indexOfLastSeparator(str);
        i += indexOfLastSeparator;
        if (prefixLength < str.length() && indexOfLastSeparator >= 0) {
            if (prefixLength < i) {
                str = str.substring(prefixLength, i);
                failIfNullBytePresent(str);
                return str;
            }
        }
        return "";
    }

    public static String getFullPath(String str) {
        return doGetFullPath(str, true);
    }

    public static String getFullPathNoEndSeparator(String str) {
        return doGetFullPath(str, false);
    }

    private static String doGetFullPath(String str, boolean z) {
        if (str == null) {
            return null;
        }
        int prefixLength = getPrefixLength(str);
        if (prefixLength < 0) {
            return null;
        }
        if (prefixLength >= str.length()) {
            return z ? getPrefix(str) : str;
        } else {
            int indexOfLastSeparator = indexOfLastSeparator(str);
            if (indexOfLastSeparator < 0) {
                return str.substring(0, prefixLength);
            }
            indexOfLastSeparator += z;
            if (indexOfLastSeparator == 0) {
                indexOfLastSeparator++;
            }
            return str.substring(0, indexOfLastSeparator);
        }
    }

    public static String getName(String str) {
        if (str == null) {
            return null;
        }
        failIfNullBytePresent(str);
        return str.substring(indexOfLastSeparator(str) + 1);
    }

    private static void failIfNullBytePresent(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) == '\u0000') {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }

    public static String getBaseName(String str) {
        return removeExtension(getName(str));
    }

    public static String getExtension(String str) {
        if (str == null) {
            return null;
        }
        int indexOfExtension = indexOfExtension(str);
        if (indexOfExtension == -1) {
            return "";
        }
        return str.substring(indexOfExtension + 1);
    }

    public static String removeExtension(String str) {
        if (str == null) {
            return null;
        }
        failIfNullBytePresent(str);
        int indexOfExtension = indexOfExtension(str);
        if (indexOfExtension == -1) {
            return str;
        }
        return str.substring(0, indexOfExtension);
    }

    public static boolean equals(String str, String str2) {
        return equals(str, str2, false, IOCase.SENSITIVE);
    }

    public static boolean equalsOnSystem(String str, String str2) {
        return equals(str, str2, false, IOCase.SYSTEM);
    }

    public static boolean equalsNormalized(String str, String str2) {
        return equals(str, str2, true, IOCase.SENSITIVE);
    }

    public static boolean equalsNormalizedOnSystem(String str, String str2) {
        return equals(str, str2, true, IOCase.SYSTEM);
    }

    public static boolean equals(String str, String str2, boolean z, IOCase iOCase) {
        if (str != null) {
            if (str2 != null) {
                if (z) {
                    str = normalize(str);
                    str2 = normalize(str2);
                    if (str == null || str2 == null) {
                        throw new NullPointerException("Error normalizing one or both of the file names");
                    }
                }
                if (iOCase == null) {
                    iOCase = IOCase.SENSITIVE;
                }
                return iOCase.checkEquals(str, str2);
            }
        }
        str = (str == null && str2 == null) ? true : null;
        return str;
    }

    public static boolean isExtension(String str, String str2) {
        boolean z = false;
        if (str == null) {
            return false;
        }
        failIfNullBytePresent(str);
        if (str2 != null) {
            if (!str2.isEmpty()) {
                return getExtension(str).equals(str2);
            }
        }
        if (indexOfExtension(str) == -1) {
            z = true;
        }
        return z;
    }

    public static boolean isExtension(String str, String[] strArr) {
        boolean z = false;
        if (str == null) {
            return false;
        }
        failIfNullBytePresent(str);
        if (strArr != null) {
            if (strArr.length != 0) {
                str = getExtension(str);
                for (Object equals : strArr) {
                    if (str.equals(equals)) {
                        return true;
                    }
                }
                return false;
            }
        }
        if (indexOfExtension(str) == -1) {
            z = true;
        }
        return z;
    }

    public static boolean isExtension(String str, Collection<String> collection) {
        boolean z = false;
        if (str == null) {
            return false;
        }
        failIfNullBytePresent(str);
        if (collection != null) {
            if (!collection.isEmpty()) {
                str = getExtension(str);
                for (String equals : collection) {
                    if (str.equals(equals)) {
                        return true;
                    }
                }
                return false;
            }
        }
        if (indexOfExtension(str) == -1) {
            z = true;
        }
        return z;
    }

    public static boolean wildcardMatch(String str, String str2) {
        return wildcardMatch(str, str2, IOCase.SENSITIVE);
    }

    public static boolean wildcardMatchOnSystem(String str, String str2) {
        return wildcardMatch(str, str2, IOCase.SYSTEM);
    }

    public static boolean wildcardMatch(String str, String str2, IOCase iOCase) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str != null) {
            if (str2 != null) {
                if (iOCase == null) {
                    iOCase = IOCase.SENSITIVE;
                }
                str2 = splitOnTokens(str2);
                Stack stack = new Stack();
                Object obj = null;
                int i = 0;
                int i2 = 0;
                while (true) {
                    int i3;
                    Object obj2;
                    if (stack.size() > 0) {
                        int[] iArr = (int[]) stack.pop();
                        i = iArr[0];
                        i3 = iArr[1];
                        i2 = i;
                        obj2 = 1;
                    } else {
                        int i4 = i;
                        obj2 = obj;
                        i3 = i4;
                    }
                    while (i2 < str2.length) {
                        if (str2[i2].equals("?")) {
                            i3++;
                            if (i3 > str.length()) {
                                break;
                            }
                        } else if (str2[i2].equals("*")) {
                            if (i2 == str2.length - 1) {
                                i3 = str.length();
                            }
                            obj2 = 1;
                            i2++;
                        } else {
                            if (obj2 == null) {
                                if (!iOCase.checkRegionMatches(str, i3, str2[i2])) {
                                    break;
                                }
                            }
                            i3 = iOCase.checkIndexOf(str, i3, str2[i2]);
                            if (i3 == -1) {
                                break;
                            }
                            if (iOCase.checkIndexOf(str, i3 + 1, str2[i2]) >= 0) {
                                stack.push(new int[]{i2, iOCase.checkIndexOf(str, i3 + 1, str2[i2])});
                            }
                            i3 += str2[i2].length();
                        }
                        obj2 = null;
                        i2++;
                    }
                    if (i2 == str2.length && i3 == str.length()) {
                        return true;
                    }
                    if (stack.size() <= 0) {
                        return false;
                    }
                    Object obj3 = obj2;
                    i = i3;
                    obj = obj3;
                }
            }
        }
        return false;
    }

    static String[] splitOnTokens(String str) {
        if (str.indexOf(63) == -1 && str.indexOf(42) == -1) {
            return new String[]{str};
        }
        str = str.toCharArray();
        ArrayList arrayList = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        int length = str.length;
        int i = 0;
        char c = '\u0000';
        while (i < length) {
            char c2 = str[i];
            if (c2 != '?') {
                if (c2 != '*') {
                    stringBuilder.append(c2);
                    i++;
                    c = c2;
                }
            }
            if (stringBuilder.length() != 0) {
                arrayList.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
            if (c2 == '?') {
                arrayList.add("?");
            } else if (c != '*') {
                arrayList.add("*");
            }
            i++;
            c = c2;
        }
        if (stringBuilder.length() != null) {
            arrayList.add(stringBuilder.toString());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }
}
