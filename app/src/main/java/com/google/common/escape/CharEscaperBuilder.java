package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@GwtCompatible
@Beta
public final class CharEscaperBuilder {
    private final Map<Character, String> map = new HashMap();
    private int max = -1;

    private static class CharArrayDecorator extends CharEscaper {
        private final int replaceLength;
        private final char[][] replacements;

        CharArrayDecorator(char[][] cArr) {
            this.replacements = cArr;
            this.replaceLength = cArr.length;
        }

        public String escape(String str) {
            int length = str.length();
            for (int i = 0; i < length; i++) {
                char charAt = str.charAt(i);
                if (charAt < this.replacements.length && this.replacements[charAt] != null) {
                    return escapeSlow(str, i);
                }
            }
            return str;
        }

        protected char[] escape(char c) {
            return c < this.replaceLength ? this.replacements[c] : '\u0000';
        }
    }

    public CharEscaperBuilder addEscape(char c, String str) {
        this.map.put(Character.valueOf(c), Preconditions.checkNotNull(str));
        if (c > this.max) {
            this.max = c;
        }
        return this;
    }

    public CharEscaperBuilder addEscapes(char[] cArr, String str) {
        Preconditions.checkNotNull(str);
        for (char addEscape : cArr) {
            addEscape(addEscape, str);
        }
        return this;
    }

    public char[][] toArray() {
        char[][] cArr = new char[(this.max + 1)][];
        for (Entry entry : this.map.entrySet()) {
            cArr[((Character) entry.getKey()).charValue()] = ((String) entry.getValue()).toCharArray();
        }
        return cArr;
    }

    public Escaper toEscaper() {
        return new CharArrayDecorator(toArray());
    }
}
