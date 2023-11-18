package okio;

import kotlin.jvm.internal.CharCompanionObject;

public final class Utf8 {
    private Utf8() {
    }

    public static long size(String str) {
        return size(str, 0, str.length());
    }

    public static long size(String str, int i, int i2) {
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        } else if (i < 0) {
            i2 = new StringBuilder();
            i2.append("beginIndex < 0: ");
            i2.append(i);
            throw new IllegalArgumentException(i2.toString());
        } else if (i2 < i) {
            r0 = new StringBuilder();
            r0.append("endIndex < beginIndex: ");
            r0.append(i2);
            r0.append(" < ");
            r0.append(i);
            throw new IllegalArgumentException(r0.toString());
        } else if (i2 > str.length()) {
            r0 = new StringBuilder();
            r0.append("endIndex > string.length: ");
            r0.append(i2);
            r0.append(" > ");
            r0.append(str.length());
            throw new IllegalArgumentException(r0.toString());
        } else {
            long j = 0;
            while (i < i2) {
                char charAt = str.charAt(i);
                if (charAt < '') {
                    i++;
                    j++;
                } else {
                    long j2;
                    if (charAt < 'ࠀ') {
                        j2 = j + 2;
                        i++;
                    } else {
                        if (charAt >= '?') {
                            if (charAt <= '?') {
                                int i3 = i + 1;
                                char charAt2 = i3 < i2 ? str.charAt(i3) : '\u0000';
                                if (charAt <= CharCompanionObject.MAX_HIGH_SURROGATE && charAt2 >= CharCompanionObject.MIN_LOW_SURROGATE) {
                                    if (charAt2 <= '?') {
                                        j2 = j + 4;
                                        i += 2;
                                    }
                                }
                                j++;
                                i = i3;
                            }
                        }
                        j2 = j + 3;
                        i++;
                    }
                    j = j2;
                }
            }
            return j;
        }
    }
}
