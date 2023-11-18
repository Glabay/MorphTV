package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@GwtCompatible
@Beta
public abstract class CharEscaper extends Escaper {
    private static final int DEST_PAD_MULTIPLIER = 2;

    protected abstract char[] escape(char c);

    protected CharEscaper() {
    }

    public String escape(String str) {
        Preconditions.checkNotNull(str);
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (escape(str.charAt(i)) != null) {
                return escapeSlow(str, i);
            }
        }
        return str;
    }

    protected final String escapeSlow(String str, int i) {
        int length = str.length();
        char[] charBufferFromThreadLocal = Platform.charBufferFromThreadLocal();
        char[] cArr = charBufferFromThreadLocal;
        int length2 = charBufferFromThreadLocal.length;
        int i2 = 0;
        int i3 = 0;
        while (i < length) {
            Object escape = escape(str.charAt(i));
            if (escape != null) {
                int length3 = escape.length;
                int i4 = i - i2;
                int i5 = i3 + i4;
                int i6 = i5 + length3;
                if (length2 < i6) {
                    length2 = ((length - i) * 2) + i6;
                    cArr = growBuffer(cArr, i3, length2);
                }
                if (i4 > 0) {
                    str.getChars(i2, i, cArr, i3);
                    i3 = i5;
                }
                if (length3 > 0) {
                    System.arraycopy(escape, 0, cArr, i3, length3);
                    i3 += length3;
                }
                i2 = i + 1;
            }
            i++;
        }
        i = length - i2;
        if (i > 0) {
            i += i3;
            if (length2 < i) {
                cArr = growBuffer(cArr, i3, i);
            }
            str.getChars(i2, length, cArr, i3);
        } else {
            i = i3;
        }
        return new String(cArr, 0, i);
    }

    private static char[] growBuffer(char[] cArr, int i, int i2) {
        i2 = new char[i2];
        if (i > 0) {
            System.arraycopy(cArr, 0, i2, 0, i);
        }
        return i2;
    }
}
