package org.jsoup.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;

public final class StringUtil {
    private static final String[] padding = new String[]{"", StringUtils.SPACE, "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          "};

    public static boolean isWhitespace(int i) {
        if (!(i == 32 || i == 9 || i == 10 || i == 12)) {
            if (i != 13) {
                return false;
            }
        }
        return true;
    }

    public static String join(Collection collection, String str) {
        return join(collection.iterator(), str);
    }

    public static String join(Iterator it, String str) {
        if (!it.hasNext()) {
            return "";
        }
        String obj = it.next().toString();
        if (!it.hasNext()) {
            return obj;
        }
        StringBuilder stringBuilder = new StringBuilder(64);
        stringBuilder.append(obj);
        while (it.hasNext()) {
            stringBuilder.append(str);
            stringBuilder.append(it.next());
        }
        return stringBuilder.toString();
    }

    public static String padding(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("width must be > 0");
        } else if (i < padding.length) {
            return padding[i];
        } else {
            char[] cArr = new char[i];
            for (int i2 = 0; i2 < i; i2++) {
                cArr[i2] = ' ';
            }
            return String.valueOf(cArr);
        }
    }

    public static boolean isBlank(String str) {
        if (str != null) {
            if (str.length() != 0) {
                int length = str.length();
                for (int i = 0; i < length; i++) {
                    if (!isWhitespace(str.codePointAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        if (str != null) {
            if (str.length() != 0) {
                int length = str.length();
                for (int i = 0; i < length; i++) {
                    if (!Character.isDigit(str.codePointAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static String normaliseWhitespace(String str) {
        StringBuilder stringBuilder = new StringBuilder(str.length());
        appendNormalisedWhitespace(stringBuilder, str, false);
        return stringBuilder.toString();
    }

    public static void appendNormalisedWhitespace(StringBuilder stringBuilder, String str, boolean z) {
        int length = str.length();
        int i = 0;
        Object obj = null;
        Object obj2 = null;
        while (i < length) {
            int codePointAt = str.codePointAt(i);
            if (!isWhitespace(codePointAt)) {
                stringBuilder.appendCodePoint(codePointAt);
                obj = 1;
                obj2 = null;
            } else if (!z || r3 != null) {
                if (obj2 == null) {
                    stringBuilder.append(' ');
                    obj2 = 1;
                }
            }
            i += Character.charCount(codePointAt);
        }
    }

    public static boolean in(String str, String... strArr) {
        for (String equals : strArr) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inSorted(String str, String[] strArr) {
        return Arrays.binarySearch(strArr, str) >= null ? true : null;
    }

    public static URL resolve(URL url, String str) throws MalformedURLException {
        if (str.startsWith("?")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(url.getPath());
            stringBuilder.append(str);
            str = stringBuilder.toString();
        }
        if (str.indexOf(46) == 0 && url.getFile().indexOf(47) != 0) {
            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("/");
            stringBuilder2.append(url.getFile());
            url = new URL(protocol, host, port, stringBuilder2.toString());
        }
        return new URL(url, str);
    }

    public static java.lang.String resolve(java.lang.String r1, java.lang.String r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x000e }
        r0.<init>(r1);	 Catch:{ MalformedURLException -> 0x000e }
        r1 = resolve(r0, r2);	 Catch:{ MalformedURLException -> 0x0018 }
        r1 = r1.toExternalForm();	 Catch:{ MalformedURLException -> 0x0018 }
        return r1;	 Catch:{ MalformedURLException -> 0x0018 }
    L_0x000e:
        r1 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x0018 }
        r1.<init>(r2);	 Catch:{ MalformedURLException -> 0x0018 }
        r1 = r1.toExternalForm();	 Catch:{ MalformedURLException -> 0x0018 }
        return r1;
    L_0x0018:
        r1 = "";
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.helper.StringUtil.resolve(java.lang.String, java.lang.String):java.lang.String");
    }
}
