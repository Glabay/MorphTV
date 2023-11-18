package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
public abstract class ArrayBasedUnicodeEscaper extends UnicodeEscaper {
    private final char[][] replacements;
    private final int replacementsLength;
    private final int safeMax;
    private final char safeMaxChar;
    private final int safeMin;
    private final char safeMinChar;

    protected abstract char[] escapeUnsafe(int i);

    protected ArrayBasedUnicodeEscaper(Map<Character, String> map, int i, int i2, @Nullable String str) {
        this(ArrayBasedEscaperMap.create(map), i, i2, str);
    }

    protected ArrayBasedUnicodeEscaper(ArrayBasedEscaperMap arrayBasedEscaperMap, int i, int i2, @Nullable String str) {
        Preconditions.checkNotNull(arrayBasedEscaperMap);
        this.replacements = arrayBasedEscaperMap.getReplacementArray();
        this.replacementsLength = this.replacements.length;
        if (i2 < i) {
            i2 = -1;
            i = Integer.MAX_VALUE;
        }
        this.safeMin = i;
        this.safeMax = i2;
        if (i >= 55296) {
            this.safeMinChar = '￿';
            this.safeMaxChar = null;
            return;
        }
        this.safeMinChar = (char) i;
        this.safeMaxChar = (char) Math.min(i2, 55295);
    }

    public final String escape(String str) {
        Preconditions.checkNotNull(str);
        int i = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if ((charAt >= this.replacementsLength || this.replacements[charAt] == null) && charAt <= this.safeMaxChar) {
                if (charAt >= this.safeMinChar) {
                    i++;
                }
            }
            return escapeSlow(str, i);
        }
        return str;
    }

    protected final int nextEscapeIndex(CharSequence charSequence, int i, int i2) {
        while (i < i2) {
            char charAt = charSequence.charAt(i);
            if ((charAt < this.replacementsLength && this.replacements[charAt] != null) || charAt > this.safeMaxChar) {
                break;
            } else if (charAt < this.safeMinChar) {
                break;
            } else {
                i++;
            }
        }
        return i;
    }

    protected final char[] escape(int i) {
        if (i < this.replacementsLength) {
            char[] cArr = this.replacements[i];
            if (cArr != null) {
                return cArr;
            }
        }
        if (i < this.safeMin || i > this.safeMax) {
            return escapeUnsafe(i);
        }
        return 0;
    }
}
