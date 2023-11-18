package org.threeten.bp.format;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.threeten.bp.jdk8.Jdk8Methods;

public final class DecimalStyle {
    private static final ConcurrentMap<Locale, DecimalStyle> CACHE = new ConcurrentHashMap(16, 0.75f, 2);
    public static final DecimalStyle STANDARD = new DecimalStyle('0', '+', '-', '.');
    private final char decimalSeparator;
    private final char negativeSign;
    private final char positiveSign;
    private final char zeroDigit;

    public static Set<Locale> getAvailableLocales() {
        return new HashSet(Arrays.asList(DecimalFormatSymbols.getAvailableLocales()));
    }

    public static DecimalStyle ofDefaultLocale() {
        return of(Locale.getDefault());
    }

    public static DecimalStyle of(Locale locale) {
        Jdk8Methods.requireNonNull(locale, "locale");
        DecimalStyle decimalStyle = (DecimalStyle) CACHE.get(locale);
        if (decimalStyle != null) {
            return decimalStyle;
        }
        CACHE.putIfAbsent(locale, create(locale));
        return (DecimalStyle) CACHE.get(locale);
    }

    private static DecimalStyle create(Locale locale) {
        locale = DecimalFormatSymbols.getInstance(locale);
        char zeroDigit = locale.getZeroDigit();
        char minusSign = locale.getMinusSign();
        locale = locale.getDecimalSeparator();
        if (zeroDigit == '0' && minusSign == '-' && locale == 46) {
            return STANDARD;
        }
        return new DecimalStyle(zeroDigit, '+', minusSign, locale);
    }

    private DecimalStyle(char c, char c2, char c3, char c4) {
        this.zeroDigit = c;
        this.positiveSign = c2;
        this.negativeSign = c3;
        this.decimalSeparator = c4;
    }

    public char getZeroDigit() {
        return this.zeroDigit;
    }

    public DecimalStyle withZeroDigit(char c) {
        if (c == this.zeroDigit) {
            return this;
        }
        return new DecimalStyle(c, this.positiveSign, this.negativeSign, this.decimalSeparator);
    }

    public char getPositiveSign() {
        return this.positiveSign;
    }

    public DecimalStyle withPositiveSign(char c) {
        if (c == this.positiveSign) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, c, this.negativeSign, this.decimalSeparator);
    }

    public char getNegativeSign() {
        return this.negativeSign;
    }

    public DecimalStyle withNegativeSign(char c) {
        if (c == this.negativeSign) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, this.positiveSign, c, this.decimalSeparator);
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public DecimalStyle withDecimalSeparator(char c) {
        if (c == this.decimalSeparator) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, this.positiveSign, this.negativeSign, c);
    }

    int convertToDigit(char c) {
        c -= this.zeroDigit;
        return (c < '\u0000' || c > '\t') ? '￿' : c;
    }

    String convertNumberToI18N(String str) {
        if (this.zeroDigit == '0') {
            return str;
        }
        int i = this.zeroDigit - 48;
        str = str.toCharArray();
        for (int i2 = 0; i2 < str.length; i2++) {
            str[i2] = (char) (str[i2] + i);
        }
        return new String(str);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DecimalStyle)) {
            return false;
        }
        DecimalStyle decimalStyle = (DecimalStyle) obj;
        if (this.zeroDigit != decimalStyle.zeroDigit || this.positiveSign != decimalStyle.positiveSign || this.negativeSign != decimalStyle.negativeSign || this.decimalSeparator != decimalStyle.decimalSeparator) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((this.zeroDigit + this.positiveSign) + this.negativeSign) + this.decimalSeparator;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DecimalStyle[");
        stringBuilder.append(this.zeroDigit);
        stringBuilder.append(this.positiveSign);
        stringBuilder.append(this.negativeSign);
        stringBuilder.append(this.decimalSeparator);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
