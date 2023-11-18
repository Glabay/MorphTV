package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@GwtCompatible
@Beta
public abstract class UnicodeEscaper extends Escaper {
    private static final int DEST_PAD = 32;

    protected abstract char[] escape(int i);

    protected UnicodeEscaper() {
    }

    protected int nextEscapeIndex(CharSequence charSequence, int i, int i2) {
        while (i < i2) {
            int codePointAt = codePointAt(charSequence, i, i2);
            if (codePointAt < 0) {
                break;
            } else if (escape(codePointAt) != null) {
                break;
            } else {
                i += Character.isSupplementaryCodePoint(codePointAt) ? 2 : 1;
            }
        }
        return i;
    }

    public String escape(String str) {
        Preconditions.checkNotNull(str);
        int length = str.length();
        int nextEscapeIndex = nextEscapeIndex(str, 0, length);
        return nextEscapeIndex == length ? str : escapeSlow(str, nextEscapeIndex);
    }

    protected final String escapeSlow(String str, int i) {
        int length = str.length();
        char[] charBufferFromThreadLocal = Platform.charBufferFromThreadLocal();
        int i2 = 0;
        int i3 = 0;
        while (i < length) {
            int codePointAt = codePointAt(str, i, length);
            if (codePointAt < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            Object escape = escape(codePointAt);
            codePointAt = (Character.isSupplementaryCodePoint(codePointAt) ? 2 : 1) + i;
            if (escape != null) {
                int i4 = i - i2;
                int i5 = i3 + i4;
                int length2 = escape.length + i5;
                if (charBufferFromThreadLocal.length < length2) {
                    charBufferFromThreadLocal = growBuffer(charBufferFromThreadLocal, i3, (length2 + (length - i)) + 32);
                }
                if (i4 > 0) {
                    str.getChars(i2, i, charBufferFromThreadLocal, i3);
                    i3 = i5;
                }
                if (escape.length > 0) {
                    System.arraycopy(escape, 0, charBufferFromThreadLocal, i3, escape.length);
                    i3 += escape.length;
                }
                i2 = codePointAt;
            }
            i = nextEscapeIndex(str, codePointAt, length);
        }
        i = length - i2;
        if (i > 0) {
            i += i3;
            if (charBufferFromThreadLocal.length < i) {
                charBufferFromThreadLocal = growBuffer(charBufferFromThreadLocal, i3, i);
            }
            str.getChars(i2, length, charBufferFromThreadLocal, i3);
        } else {
            i = i3;
        }
        return new String(charBufferFromThreadLocal, 0, i);
    }

    protected static int codePointAt(CharSequence charSequence, int i, int i2) {
        Preconditions.checkNotNull(charSequence);
        if (i < i2) {
            int i3 = i + 1;
            i = charSequence.charAt(i);
            if (i >= 55296) {
                if (i <= 57343) {
                    StringBuilder stringBuilder;
                    if (i > 56319) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected low surrogate character '");
                        stringBuilder.append(i);
                        stringBuilder.append("' with value ");
                        stringBuilder.append(i);
                        stringBuilder.append(" at index ");
                        stringBuilder.append(i3 - 1);
                        stringBuilder.append(" in '");
                        stringBuilder.append(charSequence);
                        stringBuilder.append("'");
                        throw new IllegalArgumentException(stringBuilder.toString());
                    } else if (i3 == i2) {
                        return -i;
                    } else {
                        i2 = charSequence.charAt(i3);
                        if (Character.isLowSurrogate(i2)) {
                            return Character.toCodePoint(i, i2);
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Expected low surrogate but got char '");
                        stringBuilder.append(i2);
                        stringBuilder.append("' with value ");
                        stringBuilder.append(i2);
                        stringBuilder.append(" at index ");
                        stringBuilder.append(i3);
                        stringBuilder.append(" in '");
                        stringBuilder.append(charSequence);
                        stringBuilder.append("'");
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
            }
            return i;
        }
        throw new IndexOutOfBoundsException("Index exceeds specified range");
    }

    private static char[] growBuffer(char[] cArr, int i, int i2) {
        i2 = new char[i2];
        if (i > 0) {
            System.arraycopy(cArr, 0, i2, 0, i);
        }
        return i2;
    }
}
