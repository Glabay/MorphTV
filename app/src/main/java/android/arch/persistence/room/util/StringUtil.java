package android.arch.persistence.room.util;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@RestrictTo({Scope.LIBRARY_GROUP})
public class StringUtil {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static StringBuilder newStringBuilder() {
        return new StringBuilder();
    }

    public static void appendPlaceholders(StringBuilder stringBuilder, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append("?");
            if (i2 < i - 1) {
                stringBuilder.append(",");
            }
        }
    }

    @Nullable
    public static List<Integer> splitToIntList(@Nullable String str) {
        if (str == null) {
            return null;
        }
        List<Integer> arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        while (stringTokenizer.hasMoreElements() != null) {
            try {
                arrayList.add(Integer.valueOf(Integer.parseInt(stringTokenizer.nextToken())));
            } catch (String str2) {
                Log.e("ROOM", "Malformed integer list", str2);
            }
        }
        return arrayList;
    }

    @Nullable
    public static String joinIntoString(@Nullable List<Integer> list) {
        if (list == null) {
            return null;
        }
        int size = list.size();
        if (size == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(Integer.toString(((Integer) list.get(i)).intValue()));
            if (i < size - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}
