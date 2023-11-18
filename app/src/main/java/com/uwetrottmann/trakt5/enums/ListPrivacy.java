package com.uwetrottmann.trakt5.enums;

import java.util.HashMap;
import java.util.Map;

public enum ListPrivacy implements TraktEnum {
    PRIVATE("private"),
    FRIENDS("friends"),
    PUBLIC("public");
    
    private static final Map<String, ListPrivacy> STRING_MAPPING = null;
    public final String value;

    static {
        STRING_MAPPING = new HashMap();
        ListPrivacy[] values = values();
        int length = values.length;
        int i;
        while (i < length) {
            ListPrivacy listPrivacy = values[i];
            STRING_MAPPING.put(listPrivacy.toString(), listPrivacy);
            i++;
        }
    }

    private ListPrivacy(String str) {
        this.value = str;
    }

    public static ListPrivacy fromValue(String str) {
        return (ListPrivacy) STRING_MAPPING.get(str);
    }

    public String toString() {
        return this.value;
    }
}
