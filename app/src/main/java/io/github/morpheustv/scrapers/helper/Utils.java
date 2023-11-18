package io.github.morpheustv.scrapers.helper;

import android.content.Context;
import android.text.format.Formatter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

public class Utils {
    public static double stringSimilarity(String str, String str2) {
        if (str.length() < str2.length()) {
            String str3 = str2;
            str2 = str;
            str = str3;
        }
        int length = str.length();
        if (length == 0) {
            return 0;
        }
        return ((double) (length - editDistance(str, str2))) / ((double) length);
    }

    private static int editDistance(String str, String str2) {
        str = str.toLowerCase();
        str2 = str2.toLowerCase();
        int[] iArr = new int[(str2.length() + 1)];
        for (int i = 0; i <= str.length(); i++) {
            int i2 = i;
            for (int i3 = 0; i3 <= str2.length(); i3++) {
                if (i == 0) {
                    iArr[i3] = i3;
                } else if (i3 > 0) {
                    int i4 = i3 - 1;
                    int i5 = iArr[i4];
                    if (str.charAt(i - 1) != str2.charAt(i4)) {
                        i5 = Math.min(Math.min(i5, i2), iArr[i3]) + 1;
                    }
                    iArr[i4] = i2;
                    i2 = i5;
                }
            }
            if (i > 0) {
                iArr[str2.length()] = i2;
            }
        }
        return iArr[str2.length()];
    }

    public static String getFilenameFromUrl(String str, String str2) {
        try {
            if (!str.startsWith("http")) {
                return str2;
            }
            str = URLDecoder.decode(FilenameUtils.getBaseName(new URL(str).getPath()), "UTF-8");
            return (str == null || str.isEmpty() || str.length() <= 15) ? str2 : str;
        } catch (String str3) {
            str3.printStackTrace();
            return str2;
        }
    }

    public static Map<String, String> splitQuery(URL url) {
        Map<String, String> linkedHashMap = new LinkedHashMap();
        try {
            for (String str : url.getQuery().split("&")) {
                int indexOf = str.indexOf("=");
                linkedHashMap.put(URLDecoder.decode(str.substring(0, indexOf), "UTF-8"), URLDecoder.decode(str.substring(indexOf + 1), "UTF-8"));
            }
        } catch (URL url2) {
            url2.printStackTrace();
        }
        return linkedHashMap;
    }

    public static String formatSize(Context context, long j) {
        return Formatter.formatShortFileSize(context, j);
    }

    public static String LoadAsset(Context context, String str) {
        String str2 = "";
        try {
            context = context.getAssets().open(str);
            str = new byte[context.available()];
            context.read(str);
            context.close();
            return new String(str);
        } catch (Context context2) {
            context2.printStackTrace();
            return str2;
        }
    }
}
