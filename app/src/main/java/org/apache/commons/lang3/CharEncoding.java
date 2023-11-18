package org.apache.commons.lang3;

public class CharEncoding {
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String US_ASCII = "US-ASCII";
    public static final String UTF_16 = "UTF-16";
    public static final String UTF_16BE = "UTF-16BE";
    public static final String UTF_16LE = "UTF-16LE";
    public static final String UTF_8 = "UTF-8";

    public static boolean isSupported(java.lang.String r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r1 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = java.nio.charset.Charset.isSupported(r1);	 Catch:{ IllegalCharsetNameException -> 0x0009 }
        return r1;
    L_0x0009:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.CharEncoding.isSupported(java.lang.String):boolean");
    }
}
