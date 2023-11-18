package org.jsoup.nodes;

import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import kotlin.text.Typography;
import org.jsoup.SerializationException;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;

public class Entities {
    static final int codepointRadix = 36;
    static final int empty = -1;
    static final String emptyName = "";
    private static Pattern entityPattern = Pattern.compile("^(\\w+)=(\\w+)(?:,(\\w+))?;(\\w+)$");
    private static final HashMap<String, String> multipoints = new HashMap();

    private enum CoreCharset {
        ascii,
        utf,
        fallback;

        private static CoreCharset byName(String str) {
            if (str.equals("US-ASCII")) {
                return ascii;
            }
            if (str.startsWith("UTF-") != null) {
                return utf;
            }
            return fallback;
        }
    }

    public enum EscapeMode {
        xhtml("entities-xhtml.properties", 4),
        base("entities-base.properties", 106),
        extended("entities-full.properties", 2125);
        
        private int[] codeKeys;
        private int[] codeVals;
        private String[] nameKeys;
        private String[] nameVals;

        private EscapeMode(String str, int i) {
            Entities.load(this, str, i);
        }

        int codepointForName(String str) {
            str = Arrays.binarySearch(this.nameKeys, str);
            return str >= null ? this.codeVals[str] : -1;
        }

        String nameForCodepoint(int i) {
            int binarySearch = Arrays.binarySearch(this.codeKeys, i);
            if (binarySearch < 0) {
                return "";
            }
            if (binarySearch < this.nameVals.length - 1) {
                int i2 = binarySearch + 1;
                if (this.codeKeys[i2] == i) {
                    i = this.nameVals[i2];
                    return i;
                }
            }
            i = this.nameVals[binarySearch];
            return i;
        }

        private int size() {
            return this.nameKeys.length;
        }
    }

    private Entities() {
    }

    public static boolean isNamedEntity(String str) {
        return EscapeMode.extended.codepointForName(str) != -1 ? true : null;
    }

    public static boolean isBaseNamedEntity(String str) {
        return EscapeMode.base.codepointForName(str) != -1 ? true : null;
    }

    public static Character getCharacterByName(String str) {
        return Character.valueOf((char) EscapeMode.extended.codepointForName(str));
    }

    public static String getByName(String str) {
        String str2 = (String) multipoints.get(str);
        if (str2 != null) {
            return str2;
        }
        if (EscapeMode.extended.codepointForName(str) == -1) {
            return "";
        }
        return new String(new int[]{EscapeMode.extended.codepointForName(str)}, 0, 1);
    }

    public static int codepointsForName(String str, int[] iArr) {
        String str2 = (String) multipoints.get(str);
        if (str2 != null) {
            iArr[0] = str2.codePointAt(0);
            iArr[1] = str2.codePointAt(1);
            return 2;
        }
        str = EscapeMode.extended.codepointForName(str);
        if (str == -1) {
            return 0;
        }
        iArr[0] = str;
        return 1;
    }

    static String escape(String str, OutputSettings outputSettings) {
        Appendable stringBuilder = new StringBuilder(str.length() * 2);
        try {
            escape(stringBuilder, str, outputSettings, false, false, false);
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new SerializationException(e);
        }
    }

    static void escape(Appendable appendable, String str, OutputSettings outputSettings, boolean z, boolean z2, boolean z3) throws IOException {
        EscapeMode escapeMode = outputSettings.escapeMode();
        outputSettings = outputSettings.encoder();
        CoreCharset access$100 = CoreCharset.byName(outputSettings.charset().name());
        int length = str.length();
        int i = 0;
        Object obj = null;
        Object obj2 = null;
        while (i < length) {
            int codePointAt = str.codePointAt(i);
            Object obj3 = 1;
            if (!z2) {
                obj3 = obj;
            } else if (!StringUtil.isWhitespace(codePointAt)) {
                obj2 = null;
            } else if (z3 && obj == null) {
                i += Character.charCount(codePointAt);
            } else {
                if (obj2 == null) {
                    appendable.append(' ');
                    obj2 = 1;
                }
                i += Character.charCount(codePointAt);
            }
            if (codePointAt < 65536) {
                char c = (char) codePointAt;
                if (c != Typography.quote) {
                    if (c == Typography.amp) {
                        appendable.append("&amp;");
                    } else if (c == Typography.less) {
                        if (z) {
                            if (escapeMode != EscapeMode.xhtml) {
                                appendable.append(c);
                            }
                        }
                        appendable.append("&lt;");
                    } else if (c != Typography.greater) {
                        if (c != Typography.nbsp) {
                            if (canEncode(access$100, c, outputSettings)) {
                                appendable.append(c);
                            } else {
                                appendEncoded(appendable, escapeMode, codePointAt);
                            }
                        } else if (escapeMode != EscapeMode.xhtml) {
                            appendable.append("&nbsp;");
                        } else {
                            appendable.append("&#xa0;");
                        }
                    } else if (z) {
                        appendable.append(c);
                    } else {
                        appendable.append("&gt;");
                    }
                } else if (z) {
                    appendable.append("&quot;");
                } else {
                    appendable.append(c);
                }
            } else {
                CharSequence str2 = new String(Character.toChars(codePointAt));
                if (outputSettings.canEncode(str2)) {
                    appendable.append(str2);
                } else {
                    appendEncoded(appendable, escapeMode, codePointAt);
                }
            }
            obj = obj3;
            i += Character.charCount(codePointAt);
        }
    }

    private static void appendEncoded(Appendable appendable, EscapeMode escapeMode, int i) throws IOException {
        escapeMode = escapeMode.nameForCodepoint(i);
        if (escapeMode != "") {
            appendable.append(38).append(escapeMode).append(';');
        } else {
            appendable.append("&#x").append(Integer.toHexString(i)).append(';');
        }
    }

    static String unescape(String str) {
        return unescape(str, false);
    }

    static String unescape(String str, boolean z) {
        return Parser.unescapeEntities(str, z);
    }

    private static boolean canEncode(CoreCharset coreCharset, char c, CharsetEncoder charsetEncoder) {
        boolean z = true;
        switch (coreCharset) {
            case ascii:
                if (c >= '') {
                    z = false;
                }
                return z;
            case utf:
                return true;
            default:
                return charsetEncoder.canEncode(c);
        }
    }

    private static void load(org.jsoup.nodes.Entities.EscapeMode r11, java.lang.String r12, int r13) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new java.lang.String[r13];
        r11.nameKeys = r0;
        r0 = new int[r13];
        r11.codeVals = r0;
        r0 = new int[r13];
        r11.codeKeys = r0;
        r13 = new java.lang.String[r13];
        r11.nameVals = r13;
        r13 = org.jsoup.nodes.Entities.class;
        r13 = r13.getResourceAsStream(r12);
        if (r13 != 0) goto L_0x0041;
    L_0x001c:
        r11 = new java.lang.IllegalStateException;
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r0 = "Could not read resource ";
        r13.append(r0);
        r13.append(r12);
        r12 = ". Make sure you copy resources for ";
        r13.append(r12);
        r12 = org.jsoup.nodes.Entities.class;
        r12 = r12.getCanonicalName();
        r13.append(r12);
        r12 = r13.toString();
        r11.<init>(r12);
        throw r11;
    L_0x0041:
        r0 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r1.<init>(r13);
        r0.<init>(r1);
        r13 = 0;
        r1 = 0;
    L_0x004d:
        r2 = r0.readLine();	 Catch:{ IOException -> 0x00bb }
        if (r2 == 0) goto L_0x00b7;	 Catch:{ IOException -> 0x00bb }
    L_0x0053:
        r3 = entityPattern;	 Catch:{ IOException -> 0x00bb }
        r2 = r3.matcher(r2);	 Catch:{ IOException -> 0x00bb }
        r3 = r2.find();	 Catch:{ IOException -> 0x00bb }
        if (r3 == 0) goto L_0x004d;	 Catch:{ IOException -> 0x00bb }
    L_0x005f:
        r3 = 1;	 Catch:{ IOException -> 0x00bb }
        r4 = r2.group(r3);	 Catch:{ IOException -> 0x00bb }
        r5 = 2;	 Catch:{ IOException -> 0x00bb }
        r6 = r2.group(r5);	 Catch:{ IOException -> 0x00bb }
        r7 = 36;	 Catch:{ IOException -> 0x00bb }
        r6 = java.lang.Integer.parseInt(r6, r7);	 Catch:{ IOException -> 0x00bb }
        r8 = 3;	 Catch:{ IOException -> 0x00bb }
        r9 = r2.group(r8);	 Catch:{ IOException -> 0x00bb }
        r10 = -1;	 Catch:{ IOException -> 0x00bb }
        if (r9 == 0) goto L_0x0080;	 Catch:{ IOException -> 0x00bb }
    L_0x0077:
        r8 = r2.group(r8);	 Catch:{ IOException -> 0x00bb }
        r8 = java.lang.Integer.parseInt(r8, r7);	 Catch:{ IOException -> 0x00bb }
        goto L_0x0081;	 Catch:{ IOException -> 0x00bb }
    L_0x0080:
        r8 = -1;	 Catch:{ IOException -> 0x00bb }
    L_0x0081:
        r9 = 4;	 Catch:{ IOException -> 0x00bb }
        r2 = r2.group(r9);	 Catch:{ IOException -> 0x00bb }
        r2 = java.lang.Integer.parseInt(r2, r7);	 Catch:{ IOException -> 0x00bb }
        r7 = r11.nameKeys;	 Catch:{ IOException -> 0x00bb }
        r7[r1] = r4;	 Catch:{ IOException -> 0x00bb }
        r7 = r11.codeVals;	 Catch:{ IOException -> 0x00bb }
        r7[r1] = r6;	 Catch:{ IOException -> 0x00bb }
        r7 = r11.codeKeys;	 Catch:{ IOException -> 0x00bb }
        r7[r2] = r6;	 Catch:{ IOException -> 0x00bb }
        r7 = r11.nameVals;	 Catch:{ IOException -> 0x00bb }
        r7[r2] = r4;	 Catch:{ IOException -> 0x00bb }
        if (r8 == r10) goto L_0x00b4;	 Catch:{ IOException -> 0x00bb }
    L_0x00a4:
        r2 = multipoints;	 Catch:{ IOException -> 0x00bb }
        r7 = new java.lang.String;	 Catch:{ IOException -> 0x00bb }
        r9 = new int[r5];	 Catch:{ IOException -> 0x00bb }
        r9[r13] = r6;	 Catch:{ IOException -> 0x00bb }
        r9[r3] = r8;	 Catch:{ IOException -> 0x00bb }
        r7.<init>(r9, r13, r5);	 Catch:{ IOException -> 0x00bb }
        r2.put(r4, r7);	 Catch:{ IOException -> 0x00bb }
    L_0x00b4:
        r1 = r1 + 1;	 Catch:{ IOException -> 0x00bb }
        goto L_0x004d;	 Catch:{ IOException -> 0x00bb }
    L_0x00b7:
        r0.close();	 Catch:{ IOException -> 0x00bb }
        return;
    L_0x00bb:
        r11 = new java.lang.IllegalStateException;
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r0 = "Error reading resource ";
        r13.append(r0);
        r13.append(r12);
        r12 = r13.toString();
        r11.<init>(r12);
        throw r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.nodes.Entities.load(org.jsoup.nodes.Entities$EscapeMode, java.lang.String, int):void");
    }
}
