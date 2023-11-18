package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;

@GwtCompatible
@Beta
public abstract class ArrayBasedCharEscaper extends CharEscaper {
    private final char[][] replacements;
    private final int replacementsLength;
    private final char safeMax;
    private final char safeMin;

    protected abstract char[] escapeUnsafe(char c);

    protected ArrayBasedCharEscaper(Map<Character, String> map, char c, char c2) {
        this(ArrayBasedEscaperMap.create(map), c, c2);
    }

    protected ArrayBasedCharEscaper(ArrayBasedEscaperMap arrayBasedEscaperMap, char c, char c2) {
        Preconditions.checkNotNull(arrayBasedEscaperMap);
        this.replacements = arrayBasedEscaperMap.getReplacementArray();
        this.replacementsLength = this.replacements.length;
        if (c2 < c) {
            c2 = '\u0000';
            c = '￿';
        }
        this.safeMin = c;
        this.safeMax = c2;
    }

    public final String escape(String str) {
        Preconditions.checkNotNull(str);
        int i = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if ((charAt >= this.replacementsLength || this.replacements[charAt] == null) && charAt <= this.safeMax) {
                if (charAt >= this.safeMin) {
                    i++;
                }
            }
            return escapeSlow(str, i);
        }
        return str;
    }

    protected final char[] escape(char c) {
        if (c < this.replacementsLength) {
            char[] cArr = this.replacements[c];
            if (cArr != null) {
                return cArr;
            }
        }
        if (c < this.safeMin || c > this.safeMax) {
            return escapeUnsafe(c);
        }
        return '\u0000';
    }
}
