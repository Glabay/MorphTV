package org.apache.commons.lang3.text;

import java.util.Arrays;
import kotlin.text.Typography;
import org.apache.commons.lang3.StringUtils;

public abstract class StrMatcher {
    private static final StrMatcher COMMA_MATCHER = new CharMatcher(',');
    private static final StrMatcher DOUBLE_QUOTE_MATCHER = new CharMatcher(Typography.quote);
    private static final StrMatcher NONE_MATCHER = new NoMatcher();
    private static final StrMatcher QUOTE_MATCHER = new CharSetMatcher("'\"".toCharArray());
    private static final StrMatcher SINGLE_QUOTE_MATCHER = new CharMatcher('\'');
    private static final StrMatcher SPACE_MATCHER = new CharMatcher(' ');
    private static final StrMatcher SPLIT_MATCHER = new CharSetMatcher(" \t\n\r\f".toCharArray());
    private static final StrMatcher TAB_MATCHER = new CharMatcher('\t');
    private static final StrMatcher TRIM_MATCHER = new TrimMatcher();

    static final class CharMatcher extends StrMatcher {
        private final char ch;

        CharMatcher(char c) {
            this.ch = c;
        }

        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return this.ch == cArr[i] ? 1 : null;
        }
    }

    static final class CharSetMatcher extends StrMatcher {
        private final char[] chars;

        CharSetMatcher(char[] cArr) {
            this.chars = (char[]) cArr.clone();
            Arrays.sort(this.chars);
        }

        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return Arrays.binarySearch(this.chars, cArr[i]) >= null ? 1 : null;
        }
    }

    static final class NoMatcher extends StrMatcher {
        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return 0;
        }

        NoMatcher() {
        }
    }

    static final class StringMatcher extends StrMatcher {
        private final char[] chars;

        StringMatcher(String str) {
            this.chars = str.toCharArray();
        }

        public int isMatch(char[] cArr, int i, int i2, int i3) {
            i2 = this.chars.length;
            if (i + i2 > i3) {
                return 0;
            }
            i3 = i;
            i = 0;
            while (i < this.chars.length) {
                if (this.chars[i] != cArr[i3]) {
                    return 0;
                }
                i++;
                i3++;
            }
            return i2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(super.toString());
            stringBuilder.append(' ');
            stringBuilder.append(Arrays.toString(this.chars));
            return stringBuilder.toString();
        }
    }

    static final class TrimMatcher extends StrMatcher {
        TrimMatcher() {
        }

        public int isMatch(char[] cArr, int i, int i2, int i3) {
            return cArr[i] <= 32 ? 1 : null;
        }
    }

    public abstract int isMatch(char[] cArr, int i, int i2, int i3);

    public static StrMatcher commaMatcher() {
        return COMMA_MATCHER;
    }

    public static StrMatcher tabMatcher() {
        return TAB_MATCHER;
    }

    public static StrMatcher spaceMatcher() {
        return SPACE_MATCHER;
    }

    public static StrMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    public static StrMatcher trimMatcher() {
        return TRIM_MATCHER;
    }

    public static StrMatcher singleQuoteMatcher() {
        return SINGLE_QUOTE_MATCHER;
    }

    public static StrMatcher doubleQuoteMatcher() {
        return DOUBLE_QUOTE_MATCHER;
    }

    public static StrMatcher quoteMatcher() {
        return QUOTE_MATCHER;
    }

    public static StrMatcher noneMatcher() {
        return NONE_MATCHER;
    }

    public static StrMatcher charMatcher(char c) {
        return new CharMatcher(c);
    }

    public static StrMatcher charSetMatcher(char... cArr) {
        if (cArr != null) {
            if (cArr.length != 0) {
                if (cArr.length == 1) {
                    return new CharMatcher(cArr[0]);
                }
                return new CharSetMatcher(cArr);
            }
        }
        return NONE_MATCHER;
    }

    public static StrMatcher charSetMatcher(String str) {
        if (StringUtils.isEmpty(str)) {
            return NONE_MATCHER;
        }
        if (str.length() == 1) {
            return new CharMatcher(str.charAt(0));
        }
        return new CharSetMatcher(str.toCharArray());
    }

    public static StrMatcher stringMatcher(String str) {
        if (StringUtils.isEmpty(str)) {
            return NONE_MATCHER;
        }
        return new StringMatcher(str);
    }

    protected StrMatcher() {
    }

    public int isMatch(char[] cArr, int i) {
        return isMatch(cArr, i, 0, cArr.length);
    }
}
