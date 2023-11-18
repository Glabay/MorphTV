package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
public final class Escapers {
    private static final Escaper NULL_ESCAPER = new C11171();

    /* renamed from: com.google.common.escape.Escapers$1 */
    static class C11171 extends CharEscaper {
        protected char[] escape(char c) {
            return null;
        }

        C11171() {
        }

        public String escape(String str) {
            return (String) Preconditions.checkNotNull(str);
        }
    }

    @Beta
    public static final class Builder {
        private final Map<Character, String> replacementMap;
        private char safeMax;
        private char safeMin;
        private String unsafeReplacement;

        private Builder() {
            this.replacementMap = new HashMap();
            this.safeMin = '\u0000';
            this.safeMax = '￿';
            this.unsafeReplacement = null;
        }

        public Builder setSafeRange(char c, char c2) {
            this.safeMin = c;
            this.safeMax = c2;
            return this;
        }

        public Builder setUnsafeReplacement(@Nullable String str) {
            this.unsafeReplacement = str;
            return this;
        }

        public Builder addEscape(char c, String str) {
            Preconditions.checkNotNull(str);
            this.replacementMap.put(Character.valueOf(c), str);
            return this;
        }

        public Escaper build() {
            return new ArrayBasedCharEscaper(this.replacementMap, this.safeMin, this.safeMax) {
                private final char[] replacementChars;

                protected char[] escapeUnsafe(char c) {
                    return this.replacementChars;
                }
            };
        }
    }

    private Escapers() {
    }

    public static Escaper nullEscaper() {
        return NULL_ESCAPER;
    }

    public static Builder builder() {
        return new Builder();
    }

    static UnicodeEscaper asUnicodeEscaper(Escaper escaper) {
        Preconditions.checkNotNull(escaper);
        if (escaper instanceof UnicodeEscaper) {
            return (UnicodeEscaper) escaper;
        }
        if (escaper instanceof CharEscaper) {
            return wrap((CharEscaper) escaper);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot create a UnicodeEscaper from: ");
        stringBuilder.append(escaper.getClass().getName());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static String computeReplacement(CharEscaper charEscaper, char c) {
        return stringOrNull(charEscaper.escape(c));
    }

    public static String computeReplacement(UnicodeEscaper unicodeEscaper, int i) {
        return stringOrNull(unicodeEscaper.escape(i));
    }

    private static String stringOrNull(char[] cArr) {
        return cArr == null ? null : new String(cArr);
    }

    private static UnicodeEscaper wrap(final CharEscaper charEscaper) {
        return new UnicodeEscaper() {
            protected char[] escape(int i) {
                if (i < 65536) {
                    return charEscaper.escape((char) i);
                }
                char[] cArr = new char[2];
                int i2 = 0;
                Character.toChars(i, cArr, 0);
                i = charEscaper.escape(cArr[0]);
                char[] escape = charEscaper.escape(cArr[1]);
                if (i == 0 && escape == null) {
                    return 0;
                }
                int length = i != 0 ? i.length : 1;
                char[] cArr2 = new char[((escape != null ? escape.length : 1) + length)];
                if (i != 0) {
                    for (int i3 = 0; i3 < i.length; i3++) {
                        cArr2[i3] = i[i3];
                    }
                } else {
                    cArr2[0] = cArr[0];
                }
                if (escape != null) {
                    while (i2 < escape.length) {
                        cArr2[length + i2] = escape[i2];
                        i2++;
                    }
                } else {
                    cArr2[length] = cArr[1];
                }
                return cArr2;
            }
        };
    }
}
