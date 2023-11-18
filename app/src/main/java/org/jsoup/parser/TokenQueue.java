package org.jsoup.parser;

import kotlin.text.Typography;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class TokenQueue {
    private static final char ESC = '\\';
    private int pos = 0;
    private String queue;

    public TokenQueue(String str) {
        Validate.notNull(str);
        this.queue = str;
    }

    public boolean isEmpty() {
        return remainingLength() == 0;
    }

    private int remainingLength() {
        return this.queue.length() - this.pos;
    }

    public char peek() {
        return isEmpty() ? '\u0000' : this.queue.charAt(this.pos);
    }

    public void addFirst(Character ch) {
        addFirst(ch.toString());
    }

    public void addFirst(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(this.queue.substring(this.pos));
        this.queue = stringBuilder.toString();
        this.pos = null;
    }

    public boolean matches(String str) {
        return this.queue.regionMatches(true, this.pos, str, 0, str.length());
    }

    public boolean matchesCS(String str) {
        return this.queue.startsWith(str, this.pos);
    }

    public boolean matchesAny(String... strArr) {
        for (String matches : strArr) {
            if (matches(matches)) {
                return 1;
            }
        }
        return false;
    }

    public boolean matchesAny(char... cArr) {
        if (isEmpty()) {
            return false;
        }
        for (char c : cArr) {
            if (this.queue.charAt(this.pos) == c) {
                return 1;
            }
        }
        return false;
    }

    public boolean matchesStartTag() {
        return remainingLength() >= 2 && this.queue.charAt(this.pos) == Typography.less && Character.isLetter(this.queue.charAt(this.pos + 1));
    }

    public boolean matchChomp(String str) {
        if (!matches(str)) {
            return null;
        }
        this.pos += str.length();
        return true;
    }

    public boolean matchesWhitespace() {
        return !isEmpty() && StringUtil.isWhitespace(this.queue.charAt(this.pos));
    }

    public boolean matchesWord() {
        return !isEmpty() && Character.isLetterOrDigit(this.queue.charAt(this.pos));
    }

    public void advance() {
        if (!isEmpty()) {
            this.pos++;
        }
    }

    public char consume() {
        String str = this.queue;
        int i = this.pos;
        this.pos = i + 1;
        return str.charAt(i);
    }

    public void consume(String str) {
        if (matches(str)) {
            str = str.length();
            if (str > remainingLength()) {
                throw new IllegalStateException("Queue not long enough to consume sequence");
            }
            this.pos += str;
            return;
        }
        throw new IllegalStateException("Queue did not match expected sequence");
    }

    public String consumeTo(String str) {
        str = this.queue.indexOf(str, this.pos);
        if (str == -1) {
            return remainder();
        }
        str = this.queue.substring(this.pos, str);
        this.pos += str.length();
        return str;
    }

    public String consumeToIgnoreCase(String str) {
        int i = this.pos;
        String substring = str.substring(0, 1);
        boolean equals = substring.toLowerCase().equals(substring.toUpperCase());
        while (!isEmpty()) {
            if (matches(str)) {
                break;
            } else if (equals) {
                int indexOf = this.queue.indexOf(substring, this.pos) - this.pos;
                if (indexOf == 0) {
                    this.pos++;
                } else if (indexOf < 0) {
                    this.pos = this.queue.length();
                } else {
                    this.pos += indexOf;
                }
            } else {
                this.pos++;
            }
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeToAny(String... strArr) {
        int i = this.pos;
        while (!isEmpty() && !matchesAny(strArr)) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String chompTo(String str) {
        String consumeTo = consumeTo(str);
        matchChomp(str);
        return consumeTo;
    }

    public String chompToIgnoreCase(String str) {
        String consumeToIgnoreCase = consumeToIgnoreCase(str);
        matchChomp(str);
        return consumeToIgnoreCase;
    }

    public String chompBalanced(char c, char c2) {
        char c3 = '\u0000';
        int i = 0;
        int i2 = -1;
        int i3 = -1;
        int i4 = 0;
        while (!isEmpty()) {
            Character valueOf = Character.valueOf(consume());
            if (c3 == '\u0000' || c3 != '\\') {
                if ((valueOf.equals(Character.valueOf('\'')) || valueOf.equals(Character.valueOf(Typography.quote))) && valueOf.charValue() != c) {
                    i ^= 1;
                }
                if (i != 0) {
                    continue;
                    if (i4 <= 0) {
                        break;
                    }
                } else if (valueOf.equals(Character.valueOf(c))) {
                    i4++;
                    if (i2 == -1) {
                        i2 = this.pos;
                    }
                } else if (valueOf.equals(Character.valueOf(c2))) {
                    i4--;
                }
            }
            if (i4 > 0 && c3 != '\u0000') {
                i3 = this.pos;
            }
            c3 = valueOf.charValue();
            continue;
            if (i4 <= 0) {
                break;
            }
        }
        return i3 >= 0 ? this.queue.substring(i2, i3) : "";
    }

    public static String unescape(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        str = str.toCharArray();
        int length = str.length;
        int i = 0;
        char c = '\u0000';
        while (i < length) {
            char c2 = str[i];
            if (c2 != '\\') {
                stringBuilder.append(c2);
            } else if (c != '\u0000' && c == '\\') {
                stringBuilder.append(c2);
            }
            i++;
            c = c2;
        }
        return stringBuilder.toString();
    }

    public boolean consumeWhitespace() {
        boolean z = false;
        while (matchesWhitespace()) {
            this.pos++;
            z = true;
        }
        return z;
    }

    public String consumeWord() {
        int i = this.pos;
        while (matchesWord()) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeTagName() {
        int i = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny(':', '_', '-'))) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeElementSelector() {
        int i = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny("*|", "|", "_", "-"))) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeCssIdentifier() {
        int i = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_'))) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String consumeAttributeKey() {
        int i = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_', ':'))) {
            this.pos++;
        }
        return this.queue.substring(i, this.pos);
    }

    public String remainder() {
        String substring = this.queue.substring(this.pos, this.queue.length());
        this.pos = this.queue.length();
        return substring;
    }

    public String toString() {
        return this.queue.substring(this.pos);
    }
}
