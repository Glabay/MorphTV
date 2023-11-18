package org.jsoup.parser;

import java.util.Arrays;
import java.util.Locale;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharUtils;
import org.jsoup.helper.Validate;

final class CharacterReader {
    static final char EOF = '￿';
    private static final int maxCacheLen = 12;
    private final char[] input;
    private final int length;
    private int mark = 0;
    private int pos = 0;
    private final String[] stringCache = new String[512];

    CharacterReader(String str) {
        Validate.notNull(str);
        this.input = str.toCharArray();
        this.length = this.input.length;
    }

    int pos() {
        return this.pos;
    }

    boolean isEmpty() {
        return this.pos >= this.length;
    }

    char current() {
        return this.pos >= this.length ? EOF : this.input[this.pos];
    }

    char consume() {
        char c = this.pos >= this.length ? EOF : this.input[this.pos];
        this.pos++;
        return c;
    }

    void unconsume() {
        this.pos--;
    }

    void advance() {
        this.pos++;
    }

    void mark() {
        this.mark = this.pos;
    }

    void rewindToMark() {
        this.pos = this.mark;
    }

    String consumeAsString() {
        char[] cArr = this.input;
        int i = this.pos;
        this.pos = i + 1;
        return new String(cArr, i, 1);
    }

    int nextIndexOf(char c) {
        for (int i = this.pos; i < this.length; i++) {
            if (c == this.input[i]) {
                return i - this.pos;
            }
        }
        return EOF;
    }

    int nextIndexOf(CharSequence charSequence) {
        char charAt = charSequence.charAt(0);
        int i = this.pos;
        while (i < this.length) {
            if (charAt != this.input[i]) {
                while (true) {
                    i++;
                    if (i >= this.length || charAt == this.input[i]) {
                        break;
                    }
                }
            }
            int i2 = i + 1;
            int length = (charSequence.length() + i2) - 1;
            if (i < this.length && length <= this.length) {
                int i3 = i2;
                int i4 = 1;
                while (i3 < length && charSequence.charAt(i4) == this.input[i3]) {
                    i3++;
                    i4++;
                }
                if (i3 == length) {
                    return i - this.pos;
                }
            }
            i = i2;
        }
        return -1;
    }

    String consumeTo(char c) {
        c = nextIndexOf(c);
        if (c == EOF) {
            return consumeToEnd();
        }
        String cacheString = cacheString(this.pos, c);
        this.pos += c;
        return cacheString;
    }

    String consumeTo(String str) {
        str = nextIndexOf((CharSequence) str);
        if (str == -1) {
            return consumeToEnd();
        }
        String cacheString = cacheString(this.pos, str);
        this.pos += str;
        return cacheString;
    }

    String consumeToAny(char... cArr) {
        int i = this.pos;
        int i2 = this.length;
        char[] cArr2 = this.input;
        loop0:
        while (this.pos < i2) {
            for (char c : cArr) {
                if (cArr2[this.pos] == c) {
                    break loop0;
                }
            }
            this.pos++;
        }
        return this.pos > i ? cacheString(i, this.pos - i) : "";
    }

    String consumeToAnySorted(char... cArr) {
        int i = this.pos;
        int i2 = this.length;
        char[] cArr2 = this.input;
        while (this.pos < i2) {
            if (Arrays.binarySearch(cArr, cArr2[this.pos]) >= 0) {
                break;
            }
            this.pos++;
        }
        return this.pos > i ? cacheString(i, this.pos - i) : "";
    }

    String consumeData() {
        int i = this.pos;
        int i2 = this.length;
        char[] cArr = this.input;
        while (this.pos < i2) {
            char c = cArr[this.pos];
            if (c == Typography.amp || c == Typography.less) {
                break;
            } else if (c == '\u0000') {
                break;
            } else {
                this.pos++;
            }
        }
        return this.pos > i ? cacheString(i, this.pos - i) : "";
    }

    String consumeTagName() {
        int i = this.pos;
        int i2 = this.length;
        char[] cArr = this.input;
        while (this.pos < i2) {
            char c = cArr[this.pos];
            if (c == '\t' || c == '\n' || c == CharUtils.CR || c == '\f' || c == ' ' || c == IOUtils.DIR_SEPARATOR_UNIX || c == Typography.greater) {
                break;
            } else if (c == '\u0000') {
                break;
            } else {
                this.pos++;
            }
        }
        return this.pos > i ? cacheString(i, this.pos - i) : "";
    }

    String consumeToEnd() {
        String cacheString = cacheString(this.pos, this.length - this.pos);
        this.pos = this.length;
        return cacheString;
    }

    String consumeLetterSequence() {
        int i = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if ((c < 'A' || c > 'Z') && ((c < 'a' || c > 'z') && !Character.isLetter(c))) {
                break;
            }
            this.pos++;
        }
        return cacheString(i, this.pos - i);
    }

    String consumeLetterThenDigitSequence() {
        int i = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if ((c < 'A' || c > 'Z') && ((c < 'a' || c > 'z') && !Character.isLetter(c))) {
                break;
            }
            this.pos++;
        }
        while (!isEmpty()) {
            c = this.input[this.pos];
            if (c < '0' || c > '9') {
                break;
            }
            this.pos++;
        }
        return cacheString(i, this.pos - i);
    }

    String consumeHexSequence() {
        int i = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if ((c < '0' || c > '9') && ((c < 'A' || c > 'F') && (c < 'a' || c > 'f'))) {
                break;
            }
            this.pos++;
        }
        return cacheString(i, this.pos - i);
    }

    String consumeDigitSequence() {
        int i = this.pos;
        while (this.pos < this.length) {
            char c = this.input[this.pos];
            if (c < '0' || c > '9') {
                break;
            }
            this.pos++;
        }
        return cacheString(i, this.pos - i);
    }

    boolean matches(char c) {
        return !isEmpty() && this.input[this.pos] == c;
    }

    boolean matches(String str) {
        int length = str.length();
        if (length > this.length - this.pos) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) != this.input[this.pos + i]) {
                return false;
            }
        }
        return true;
    }

    boolean matchesIgnoreCase(String str) {
        int length = str.length();
        if (length > this.length - this.pos) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (Character.toUpperCase(str.charAt(i)) != Character.toUpperCase(this.input[this.pos + i])) {
                return false;
            }
        }
        return true;
    }

    boolean matchesAny(char... cArr) {
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        for (char c2 : cArr) {
            if (c2 == c) {
                return 1;
            }
        }
        return false;
    }

    boolean matchesAnySorted(char[] cArr) {
        return (isEmpty() || Arrays.binarySearch(cArr, this.input[this.pos]) < null) ? null : 1;
    }

    boolean matchesLetter() {
        boolean z = false;
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        if ((c >= 'A' && c <= 'Z') || ((c >= 'a' && c <= 'z') || Character.isLetter(c))) {
            z = true;
        }
        return z;
    }

    boolean matchesDigit() {
        boolean z = false;
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        if (c >= '0' && c <= '9') {
            z = true;
        }
        return z;
    }

    boolean matchConsume(String str) {
        if (!matches(str)) {
            return null;
        }
        this.pos += str.length();
        return true;
    }

    boolean matchConsumeIgnoreCase(String str) {
        if (!matchesIgnoreCase(str)) {
            return null;
        }
        this.pos += str.length();
        return true;
    }

    boolean containsIgnoreCase(String str) {
        CharSequence toLowerCase = str.toLowerCase(Locale.ENGLISH);
        CharSequence toUpperCase = str.toUpperCase(Locale.ENGLISH);
        if (nextIndexOf(toLowerCase) <= -1) {
            if (nextIndexOf(toUpperCase) <= -1) {
                return null;
            }
        }
        return true;
    }

    public String toString() {
        return new String(this.input, this.pos, this.length - this.pos);
    }

    private String cacheString(int i, int i2) {
        char[] cArr = this.input;
        String[] strArr = this.stringCache;
        if (i2 > 12) {
            return new String(cArr, i, i2);
        }
        int i3 = 0;
        int i4 = i;
        int i5 = 0;
        while (i3 < i2) {
            i5 = (i5 * 31) + cArr[i4];
            i3++;
            i4++;
        }
        i3 = (strArr.length - 1) & i5;
        String str = strArr[i3];
        if (str == null) {
            str = new String(cArr, i, i2);
            strArr[i3] = str;
        } else if (rangeEquals(i, i2, str)) {
            return str;
        } else {
            str = new String(cArr, i, i2);
            strArr[i3] = str;
        }
        return str;
    }

    boolean rangeEquals(int i, int i2, String str) {
        if (i2 != str.length()) {
            return false;
        }
        char[] cArr = this.input;
        int i3 = 0;
        while (true) {
            int i4 = i2 - 1;
            if (i2 == 0) {
                return true;
            }
            i2 = i + 1;
            int i5 = i3 + 1;
            if (cArr[i] != str.charAt(i3)) {
                return false;
            }
            i = i2;
            i2 = i4;
            i3 = i5;
        }
    }
}
