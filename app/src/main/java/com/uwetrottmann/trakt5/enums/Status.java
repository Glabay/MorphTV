package com.uwetrottmann.trakt5.enums;

import java.util.HashMap;
import java.util.Map;

public enum Status implements TraktEnum {
    ENDED("ended"),
    RETURNING("returning series"),
    CANCELED("canceled"),
    IN_PRODUCTION("in production");
    
    private static final Map<String, Status> STRING_MAPPING = null;
    private final String value;

    static {
        STRING_MAPPING = new HashMap();
        Status[] values = values();
        int length = values.length;
        int i;
        while (i < length) {
            Status status = values[i];
            STRING_MAPPING.put(status.toString().toUpperCase(), status);
            i++;
        }
    }

    private Status(String str) {
        this.value = str;
    }

    public static Status fromValue(String str) {
        return (Status) STRING_MAPPING.get(str.toUpperCase());
    }

    public String toString() {
        return this.value;
    }
}
