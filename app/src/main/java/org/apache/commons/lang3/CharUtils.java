package org.apache.commons.lang3;

import com.google.common.base.Ascii;

public class CharUtils {
    private static final String[] CHAR_STRING_ARRAY = new String[128];
    public static final char CR = '\r';
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final char LF = '\n';

    public static int compare(char c, char c2) {
        return c - c2;
    }

    public static boolean isAscii(char c) {
        return c < '';
    }

    public static boolean isAsciiAlphaLower(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static boolean isAsciiAlphaUpper(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static boolean isAsciiControl(char c) {
        if (c >= ' ') {
            if (c != Ascii.MAX) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAsciiNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isAsciiPrintable(char c) {
        return c >= ' ' && c < Ascii.MAX;
    }

    static {
        for (char c = '\u0000'; c < CHAR_STRING_ARRAY.length; c = (char) (c + 1)) {
            CHAR_STRING_ARRAY[c] = String.valueOf(c);
        }
    }

    @Deprecated
    public static Character toCharacterObject(char c) {
        return Character.valueOf(c);
    }

    public static Character toCharacterObject(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return Character.valueOf(str.charAt(0));
    }

    public static char toChar(Character ch) {
        if (ch != null) {
            return ch.charValue();
        }
        throw new IllegalArgumentException("The Character must not be null");
    }

    public static char toChar(Character ch, char c) {
        return ch == null ? c : ch.charValue();
    }

    public static char toChar(String str) {
        if (!StringUtils.isEmpty(str)) {
            return str.charAt(0);
        }
        throw new IllegalArgumentException("The String must not be empty");
    }

    public static char toChar(String str, char c) {
        if (StringUtils.isEmpty(str)) {
            return c;
        }
        return str.charAt('\u0000');
    }

    public static int toIntValue(char c) {
        if (isAsciiNumeric(c)) {
            return c - 48;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The character ");
        stringBuilder.append(c);
        stringBuilder.append(" is not in the range '0' - '9'");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static int toIntValue(char c, int i) {
        return !isAsciiNumeric(c) ? i : c - 48;
    }

    public static int toIntValue(Character ch) {
        if (ch != null) {
            return toIntValue(ch.charValue());
        }
        throw new IllegalArgumentException("The character must not be null");
    }

    public static int toIntValue(Character ch, int i) {
        return ch == null ? i : toIntValue(ch.charValue(), i);
    }

    public static String toString(char c) {
        if (c < '') {
            return CHAR_STRING_ARRAY[c];
        }
        return new String(new char[]{c});
    }

    public static String toString(Character ch) {
        return ch == null ? null : toString(ch.charValue());
    }

    public static String unicodeEscaped(char c) {
        StringBuilder stringBuilder = new StringBuilder(6);
        stringBuilder.append("\\u");
        stringBuilder.append(HEX_DIGITS[(c >> 12) & 15]);
        stringBuilder.append(HEX_DIGITS[(c >> 8) & 15]);
        stringBuilder.append(HEX_DIGITS[(c >> 4) & 15]);
        stringBuilder.append(HEX_DIGITS[c & 15]);
        return stringBuilder.toString();
    }

    public static String unicodeEscaped(Character ch) {
        return ch == null ? null : unicodeEscaped(ch.charValue());
    }

    public static boolean isAsciiAlpha(char c) {
        if (!isAsciiAlphaUpper(c)) {
            if (isAsciiAlphaLower(c) == '\u0000') {
                return false;
            }
        }
        return true;
    }

    public static boolean isAsciiAlphanumeric(char c) {
        if (!isAsciiAlpha(c)) {
            if (isAsciiNumeric(c) == '\u0000') {
                return false;
            }
        }
        return true;
    }
}
