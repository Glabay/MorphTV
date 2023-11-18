package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.escape.UnicodeEscaper;
import org.apache.commons.lang3.StringUtils;

@GwtCompatible
@Beta
public final class PercentEscaper extends UnicodeEscaper {
    private static final char[] PLUS_SIGN = new char[]{'+'};
    private static final char[] UPPER_HEX_DIGITS = BinTools.hex.toCharArray();
    private final boolean plusForSpace;
    private final boolean[] safeOctets;

    public PercentEscaper(String str, boolean z) {
        Preconditions.checkNotNull(str);
        if (str.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        str = stringBuilder.toString();
        if (z && str.contains(StringUtils.SPACE)) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        }
        this.plusForSpace = z;
        this.safeOctets = createSafeOctets(str);
    }

    private static boolean[] createSafeOctets(String str) {
        str = str.toCharArray();
        int i = -1;
        for (char max : str) {
            i = Math.max(max, i);
        }
        boolean[] zArr = new boolean[(i + 1)];
        for (char max2 : str) {
            zArr[max2] = true;
        }
        return zArr;
    }

    protected int nextEscapeIndex(CharSequence charSequence, int i, int i2) {
        Preconditions.checkNotNull(charSequence);
        while (i < i2) {
            char charAt = charSequence.charAt(i);
            if (charAt >= this.safeOctets.length) {
                break;
            } else if (!this.safeOctets[charAt]) {
                break;
            } else {
                i++;
            }
        }
        return i;
    }

    public String escape(String str) {
        Preconditions.checkNotNull(str);
        int length = str.length();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt < this.safeOctets.length) {
                if (this.safeOctets[charAt]) {
                    i++;
                }
            }
            return escapeSlow(str, i);
        }
        return str;
    }

    protected char[] escape(int i) {
        if (i < this.safeOctets.length && this.safeOctets[i]) {
            return 0;
        }
        if (i == 32 && this.plusForSpace) {
            return PLUS_SIGN;
        }
        if (i <= 127) {
            return new char[]{'%', UPPER_HEX_DIGITS[i & 15], UPPER_HEX_DIGITS[i >>> 4]};
        } else if (i <= 2047) {
            r0 = new char[6];
            r0[0] = '%';
            r0[3] = '%';
            r0[5] = UPPER_HEX_DIGITS[i & 15];
            i >>>= 4;
            r0[4] = UPPER_HEX_DIGITS[(i & 3) | 8];
            i >>>= 2;
            r0[2] = UPPER_HEX_DIGITS[i & 15];
            r0[1] = UPPER_HEX_DIGITS[(i >>> 4) | 12];
            return r0;
        } else if (i <= 65535) {
            r0 = new char[9];
            i >>>= 4;
            r0[7] = UPPER_HEX_DIGITS[(i & 3) | 8];
            i >>>= 2;
            r0[5] = UPPER_HEX_DIGITS[i & 15];
            i >>>= 4;
            r0[4] = UPPER_HEX_DIGITS[(i & 3) | 8];
            r0[2] = UPPER_HEX_DIGITS[i >>> 2];
            return r0;
        } else if (i <= 1114111) {
            r0 = new char[12];
            i >>>= 4;
            r0[10] = UPPER_HEX_DIGITS[(i & 3) | 8];
            i >>>= 2;
            r0[8] = UPPER_HEX_DIGITS[i & 15];
            i >>>= 4;
            r0[7] = UPPER_HEX_DIGITS[(i & 3) | 8];
            i >>>= 2;
            r0[5] = UPPER_HEX_DIGITS[i & 15];
            i >>>= 4;
            r0[4] = UPPER_HEX_DIGITS[(i & 3) | 8];
            r0[2] = UPPER_HEX_DIGITS[(i >>> 2) & 7];
            return r0;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid unicode character value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
