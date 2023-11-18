package okhttp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;
import okhttp3.internal.http.HttpDate;

public final class Headers {
    private final String[] namesAndValues;

    Headers(Headers$Builder headers$Builder) {
        this.namesAndValues = (String[]) headers$Builder.namesAndValues.toArray(new String[headers$Builder.namesAndValues.size()]);
    }

    private Headers(String[] strArr) {
        this.namesAndValues = strArr;
    }

    @Nullable
    public String get(String str) {
        return get(this.namesAndValues, str);
    }

    @Nullable
    public Date getDate(String str) {
        str = get(str);
        return str != null ? HttpDate.parse(str) : null;
    }

    public int size() {
        return this.namesAndValues.length / 2;
    }

    public String name(int i) {
        return this.namesAndValues[i * 2];
    }

    public String value(int i) {
        return this.namesAndValues[(i * 2) + 1];
    }

    public Set<String> names() {
        Set treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        int size = size();
        for (int i = 0; i < size; i++) {
            treeSet.add(name(i));
        }
        return Collections.unmodifiableSet(treeSet);
    }

    public List<String> values(String str) {
        int size = size();
        List list = null;
        for (int i = 0; i < size; i++) {
            if (str.equalsIgnoreCase(name(i))) {
                if (list == null) {
                    list = new ArrayList(2);
                }
                list.add(value(i));
            }
        }
        if (list != null) {
            return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

    public Headers$Builder newBuilder() {
        Headers$Builder headers$Builder = new Headers$Builder();
        Collections.addAll(headers$Builder.namesAndValues, this.namesAndValues);
        return headers$Builder;
    }

    public boolean equals(@Nullable Object obj) {
        return (!(obj instanceof Headers) || Arrays.equals(((Headers) obj).namesAndValues, this.namesAndValues) == null) ? null : true;
    }

    public int hashCode() {
        return Arrays.hashCode(this.namesAndValues);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int size = size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(name(i));
            stringBuilder.append(": ");
            stringBuilder.append(value(i));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Map<String, List<String>> toMultimap() {
        Map<String, List<String>> treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        int size = size();
        for (int i = 0; i < size; i++) {
            String toLowerCase = name(i).toLowerCase(Locale.US);
            List list = (List) treeMap.get(toLowerCase);
            if (list == null) {
                list = new ArrayList(2);
                treeMap.put(toLowerCase, list);
            }
            list.add(value(i));
        }
        return treeMap;
    }

    private static String get(String[] strArr, String str) {
        for (int length = strArr.length - 2; length >= 0; length -= 2) {
            if (str.equalsIgnoreCase(strArr[length])) {
                return strArr[length + 1];
            }
        }
        return null;
    }

    public static Headers of(String... strArr) {
        if (strArr == null) {
            throw new NullPointerException("namesAndValues == null");
        } else if (strArr.length % 2 != 0) {
            throw new IllegalArgumentException("Expected alternating header names and values");
        } else {
            int i;
            strArr = (String[]) strArr.clone();
            for (i = 0; i < strArr.length; i++) {
                if (strArr[i] == null) {
                    throw new IllegalArgumentException("Headers cannot be null");
                }
                strArr[i] = strArr[i].trim();
            }
            i = 0;
            while (i < strArr.length) {
                String str = strArr[i];
                String str2 = strArr[i + 1];
                if (str.length() != 0 && str.indexOf(0) == -1) {
                    if (str2.indexOf(0) == -1) {
                        i += 2;
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected header: ");
                stringBuilder.append(str);
                stringBuilder.append(": ");
                stringBuilder.append(str2);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            return new Headers(strArr);
        }
    }

    public static Headers of(Map<String, String> map) {
        if (map == null) {
            throw new NullPointerException("headers == null");
        }
        String[] strArr = new String[(map.size() * 2)];
        map = map.entrySet().iterator();
        int i = 0;
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            if (entry.getKey() != null) {
                if (entry.getValue() != null) {
                    String trim = ((String) entry.getKey()).trim();
                    String trim2 = ((String) entry.getValue()).trim();
                    if (trim.length() != 0 && trim.indexOf(0) == -1) {
                        if (trim2.indexOf(0) == -1) {
                            strArr[i] = trim;
                            strArr[i + 1] = trim2;
                            i += 2;
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected header: ");
                    stringBuilder.append(trim);
                    stringBuilder.append(": ");
                    stringBuilder.append(trim2);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            throw new IllegalArgumentException("Headers cannot be null");
        }
        return new Headers(strArr);
    }
}
