package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class ParseRequest {
    final int radix;
    final String rawValue;

    private ParseRequest(String str, int i) {
        this.rawValue = str;
        this.radix = i;
    }

    static ParseRequest fromString(String str) {
        if (str.length() == 0) {
            throw new NumberFormatException("empty string");
        }
        char charAt = str.charAt(0);
        int i = 16;
        if (!str.startsWith("0x")) {
            if (!str.startsWith("0X")) {
                if (charAt == '#') {
                    str = str.substring(1);
                } else if (charAt != '0' || str.length() <= 1) {
                    i = 10;
                } else {
                    str = str.substring(1);
                    i = 8;
                }
                return new ParseRequest(str, i);
            }
        }
        str = str.substring(2);
        return new ParseRequest(str, i);
    }
}
