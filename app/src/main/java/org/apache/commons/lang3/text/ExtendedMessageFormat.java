package org.apache.commons.lang3.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

public class ExtendedMessageFormat extends MessageFormat {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String DUMMY_PATTERN = "";
    private static final char END_FE = '}';
    private static final int HASH_SEED = 31;
    private static final char QUOTE = '\'';
    private static final char START_FE = '{';
    private static final char START_FMT = ',';
    private static final long serialVersionUID = -2362048321261811743L;
    private final Map<String, ? extends FormatFactory> registry;
    private String toPattern;

    public ExtendedMessageFormat(String str) {
        this(str, Locale.getDefault());
    }

    public ExtendedMessageFormat(String str, Locale locale) {
        this(str, locale, null);
    }

    public ExtendedMessageFormat(String str, Map<String, ? extends FormatFactory> map) {
        this(str, Locale.getDefault(), map);
    }

    public ExtendedMessageFormat(String str, Locale locale, Map<String, ? extends FormatFactory> map) {
        super("");
        setLocale(locale);
        this.registry = map;
        applyPattern(str);
    }

    public String toPattern() {
        return this.toPattern;
    }

    public final void applyPattern(String str) {
        if (this.registry == null) {
            super.applyPattern(str);
            this.toPattern = super.toPattern();
            return;
        }
        Object arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder(str.length());
        int i = 0;
        ParsePosition parsePosition = new ParsePosition(0);
        char[] toCharArray = str.toCharArray();
        int i2 = 0;
        while (parsePosition.getIndex() < str.length()) {
            char c = toCharArray[parsePosition.getIndex()];
            if (c != QUOTE) {
                if (c == START_FE) {
                    Object parseFormatDescription;
                    Object format;
                    i2++;
                    seekNonWs(str, parsePosition);
                    int index = parsePosition.getIndex();
                    int readArgumentIndex = readArgumentIndex(str, next(parsePosition));
                    stringBuilder.append(START_FE);
                    stringBuilder.append(readArgumentIndex);
                    seekNonWs(str, parsePosition);
                    if (toCharArray[parsePosition.getIndex()] == START_FMT) {
                        parseFormatDescription = parseFormatDescription(str, next(parsePosition));
                        format = getFormat(parseFormatDescription);
                        if (format == null) {
                            stringBuilder.append(START_FMT);
                            stringBuilder.append(parseFormatDescription);
                        }
                    } else {
                        parseFormatDescription = null;
                        format = parseFormatDescription;
                    }
                    arrayList.add(format);
                    if (format == null) {
                        parseFormatDescription = null;
                    }
                    arrayList2.add(parseFormatDescription);
                    boolean z = true;
                    Validate.isTrue(arrayList.size() == i2);
                    if (arrayList2.size() != i2) {
                        z = false;
                    }
                    Validate.isTrue(z);
                    if (toCharArray[parsePosition.getIndex()] != END_FE) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Unreadable format element at position ");
                        stringBuilder2.append(index);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                }
                stringBuilder.append(toCharArray[parsePosition.getIndex()]);
                next(parsePosition);
            } else {
                appendQuotedString(str, parsePosition, stringBuilder);
            }
        }
        super.applyPattern(stringBuilder.toString());
        this.toPattern = insertFormats(super.toPattern(), arrayList2);
        if (containsElements(arrayList) != null) {
            str = getFormats();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Format format2 = (Format) it.next();
                if (format2 != null) {
                    str[i] = format2;
                }
                i++;
            }
            super.setFormats(str);
        }
    }

    public void setFormat(int i, Format format) {
        throw new UnsupportedOperationException();
    }

    public void setFormatByArgumentIndex(int i, Format format) {
        throw new UnsupportedOperationException();
    }

    public void setFormats(Format[] formatArr) {
        throw new UnsupportedOperationException();
    }

    public void setFormatsByArgumentIndex(Format[] formatArr) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !super.equals(obj) || ObjectUtils.notEqual(getClass(), obj.getClass())) {
            return false;
        }
        ExtendedMessageFormat extendedMessageFormat = (ExtendedMessageFormat) obj;
        return !ObjectUtils.notEqual(this.toPattern, extendedMessageFormat.toPattern) && ObjectUtils.notEqual(this.registry, extendedMessageFormat.registry) == null;
    }

    public int hashCode() {
        return (((super.hashCode() * 31) + ObjectUtils.hashCode(this.registry)) * 31) + ObjectUtils.hashCode(this.toPattern);
    }

    private Format getFormat(String str) {
        if (this.registry != null) {
            String trim;
            int indexOf = str.indexOf(44);
            if (indexOf > 0) {
                String trim2 = str.substring(0, indexOf).trim();
                trim = str.substring(indexOf + 1).trim();
                str = trim2;
            } else {
                trim = null;
            }
            FormatFactory formatFactory = (FormatFactory) this.registry.get(str);
            if (formatFactory != null) {
                return formatFactory.getFormat(str, trim, getLocale());
            }
        }
        return null;
    }

    private int readArgumentIndex(java.lang.String r8, java.text.ParsePosition r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r7 = this;
        r0 = r9.getIndex();
        r7.seekNonWs(r8, r9);
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = 0;
    L_0x000d:
        if (r2 != 0) goto L_0x005d;
    L_0x000f:
        r3 = r9.getIndex();
        r4 = r8.length();
        if (r3 >= r4) goto L_0x005d;
    L_0x0019:
        r2 = r9.getIndex();
        r2 = r8.charAt(r2);
        r3 = java.lang.Character.isWhitespace(r2);
        r4 = 1;
        r5 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r6 = 44;
        if (r3 == 0) goto L_0x003d;
    L_0x002c:
        r7.seekNonWs(r8, r9);
        r2 = r9.getIndex();
        r2 = r8.charAt(r2);
        if (r2 == r6) goto L_0x003d;
    L_0x0039:
        if (r2 == r5) goto L_0x003d;
    L_0x003b:
        r2 = 1;
        goto L_0x0059;
    L_0x003d:
        if (r2 == r6) goto L_0x0041;
    L_0x003f:
        if (r2 != r5) goto L_0x0050;
    L_0x0041:
        r3 = r1.length();
        if (r3 <= 0) goto L_0x0050;
    L_0x0047:
        r3 = r1.toString();	 Catch:{ NumberFormatException -> 0x0050 }
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x0050 }
        return r3;
    L_0x0050:
        r3 = java.lang.Character.isDigit(r2);
        r3 = r3 ^ r4;
        r1.append(r2);
        r2 = r3;
    L_0x0059:
        r7.next(r9);
        goto L_0x000d;
    L_0x005d:
        if (r2 == 0) goto L_0x0086;
    L_0x005f:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Invalid format argument index at position ";
        r2.append(r3);
        r2.append(r0);
        r3 = ": ";
        r2.append(r3);
        r9 = r9.getIndex();
        r8 = r8.substring(r0, r9);
        r2.append(r8);
        r8 = r2.toString();
        r1.<init>(r8);
        throw r1;
    L_0x0086:
        r8 = new java.lang.IllegalArgumentException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r1 = "Unterminated format element at position ";
        r9.append(r1);
        r9.append(r0);
        r9 = r9.toString();
        r8.<init>(r9);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.text.ExtendedMessageFormat.readArgumentIndex(java.lang.String, java.text.ParsePosition):int");
    }

    private String parseFormatDescription(String str, ParsePosition parsePosition) {
        int index = parsePosition.getIndex();
        seekNonWs(str, parsePosition);
        int index2 = parsePosition.getIndex();
        int i = 1;
        while (parsePosition.getIndex() < str.length()) {
            char charAt = str.charAt(parsePosition.getIndex());
            if (charAt == QUOTE) {
                getQuotedString(str, parsePosition);
            } else if (charAt == START_FE) {
                i++;
            } else if (charAt == END_FE) {
                i--;
                if (i == 0) {
                    return str.substring(index2, parsePosition.getIndex());
                }
            }
            next(parsePosition);
        }
        parsePosition = new StringBuilder();
        parsePosition.append("Unterminated format element at position ");
        parsePosition.append(index);
        throw new IllegalArgumentException(parsePosition.toString());
    }

    private String insertFormats(String str, ArrayList<String> arrayList) {
        if (!containsElements(arrayList)) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(str.length() * 2);
        int i = 0;
        ParsePosition parsePosition = new ParsePosition(0);
        int i2 = -1;
        while (parsePosition.getIndex() < str.length()) {
            char charAt = str.charAt(parsePosition.getIndex());
            if (charAt == QUOTE) {
                appendQuotedString(str, parsePosition, stringBuilder);
            } else if (charAt != START_FE) {
                if (charAt == END_FE) {
                    i--;
                }
                stringBuilder.append(charAt);
                next(parsePosition);
            } else {
                i++;
                stringBuilder.append(START_FE);
                stringBuilder.append(readArgumentIndex(str, next(parsePosition)));
                if (i == 1) {
                    i2++;
                    String str2 = (String) arrayList.get(i2);
                    if (str2 != null) {
                        stringBuilder.append(START_FMT);
                        stringBuilder.append(str2);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    private void seekNonWs(String str, ParsePosition parsePosition) {
        char[] toCharArray = str.toCharArray();
        do {
            int isMatch = StrMatcher.splitMatcher().isMatch(toCharArray, parsePosition.getIndex());
            parsePosition.setIndex(parsePosition.getIndex() + isMatch);
            if (isMatch <= 0) {
                return;
            }
        } while (parsePosition.getIndex() < str.length());
    }

    private ParsePosition next(ParsePosition parsePosition) {
        parsePosition.setIndex(parsePosition.getIndex() + 1);
        return parsePosition;
    }

    private StringBuilder appendQuotedString(String str, ParsePosition parsePosition, StringBuilder stringBuilder) {
        if (stringBuilder != null) {
            stringBuilder.append(QUOTE);
        }
        next(parsePosition);
        int index = parsePosition.getIndex();
        char[] toCharArray = str.toCharArray();
        int index2 = parsePosition.getIndex();
        while (index2 < str.length()) {
            if (toCharArray[parsePosition.getIndex()] != QUOTE) {
                next(parsePosition);
                index2++;
            } else {
                next(parsePosition);
                if (stringBuilder == null) {
                    return null;
                }
                stringBuilder.append(toCharArray, index, parsePosition.getIndex() - index);
                return stringBuilder;
            }
        }
        parsePosition = new StringBuilder();
        parsePosition.append("Unterminated quoted string at position ");
        parsePosition.append(index);
        throw new IllegalArgumentException(parsePosition.toString());
    }

    private void getQuotedString(String str, ParsePosition parsePosition) {
        appendQuotedString(str, parsePosition, null);
    }

    private boolean containsElements(Collection<?> collection) {
        if (collection != null) {
            if (!collection.isEmpty()) {
                for (Object obj : collection) {
                    if (obj != null) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
}
