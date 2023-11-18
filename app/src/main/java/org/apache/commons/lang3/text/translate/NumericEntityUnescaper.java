package org.apache.commons.lang3.text.translate;

import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaper extends CharSequenceTranslator {
    private final EnumSet<OPTION> options;

    public enum OPTION {
        semiColonRequired,
        semiColonOptional,
        errorIfNoSemiColon
    }

    public NumericEntityUnescaper(OPTION... optionArr) {
        if (optionArr.length > 0) {
            this.options = EnumSet.copyOf(Arrays.asList(optionArr));
            return;
        }
        this.options = EnumSet.copyOf(Arrays.asList(new OPTION[]{OPTION.semiColonRequired}));
    }

    public boolean isSet(OPTION option) {
        return this.options == null ? null : this.options.contains(option);
    }

    public int translate(java.lang.CharSequence r8, int r9, java.io.Writer r10) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r7 = this;
        r0 = r8.length();
        r1 = r8.charAt(r9);
        r2 = 0;
        r3 = 38;
        if (r1 != r3) goto L_0x00ce;
    L_0x000d:
        r1 = r0 + -2;
        if (r9 >= r1) goto L_0x00ce;
    L_0x0011:
        r1 = r9 + 1;
        r1 = r8.charAt(r1);
        r3 = 35;
        if (r1 != r3) goto L_0x00ce;
    L_0x001b:
        r9 = r9 + 2;
        r1 = r8.charAt(r9);
        r3 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r4 = 1;
        if (r1 == r3) goto L_0x002d;
    L_0x0026:
        r3 = 88;
        if (r1 != r3) goto L_0x002b;
    L_0x002a:
        goto L_0x002d;
    L_0x002b:
        r1 = 0;
        goto L_0x0033;
    L_0x002d:
        r9 = r9 + 1;
        if (r9 != r0) goto L_0x0032;
    L_0x0031:
        return r2;
    L_0x0032:
        r1 = 1;
    L_0x0033:
        r3 = r9;
    L_0x0034:
        if (r3 >= r0) goto L_0x0069;
    L_0x0036:
        r5 = r8.charAt(r3);
        r6 = 48;
        if (r5 < r6) goto L_0x0046;
    L_0x003e:
        r5 = r8.charAt(r3);
        r6 = 57;
        if (r5 <= r6) goto L_0x0066;
    L_0x0046:
        r5 = r8.charAt(r3);
        r6 = 97;
        if (r5 < r6) goto L_0x0056;
    L_0x004e:
        r5 = r8.charAt(r3);
        r6 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r5 <= r6) goto L_0x0066;
    L_0x0056:
        r5 = r8.charAt(r3);
        r6 = 65;
        if (r5 < r6) goto L_0x0069;
    L_0x005e:
        r5 = r8.charAt(r3);
        r6 = 70;
        if (r5 > r6) goto L_0x0069;
    L_0x0066:
        r3 = r3 + 1;
        goto L_0x0034;
    L_0x0069:
        if (r3 == r0) goto L_0x0075;
    L_0x006b:
        r0 = r8.charAt(r3);
        r5 = 59;
        if (r0 != r5) goto L_0x0075;
    L_0x0073:
        r0 = 1;
        goto L_0x0076;
    L_0x0075:
        r0 = 0;
    L_0x0076:
        if (r0 != 0) goto L_0x0091;
    L_0x0078:
        r5 = org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.semiColonRequired;
        r5 = r7.isSet(r5);
        if (r5 == 0) goto L_0x0081;
    L_0x0080:
        return r2;
    L_0x0081:
        r5 = org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.errorIfNoSemiColon;
        r5 = r7.isSet(r5);
        if (r5 == 0) goto L_0x0091;
    L_0x0089:
        r8 = new java.lang.IllegalArgumentException;
        r9 = "Semi-colon required at end of numeric entity";
        r8.<init>(r9);
        throw r8;
    L_0x0091:
        if (r1 == 0) goto L_0x00a2;
    L_0x0093:
        r8 = r8.subSequence(r9, r3);	 Catch:{ NumberFormatException -> 0x00cd }
        r8 = r8.toString();	 Catch:{ NumberFormatException -> 0x00cd }
        r5 = 16;	 Catch:{ NumberFormatException -> 0x00cd }
        r8 = java.lang.Integer.parseInt(r8, r5);	 Catch:{ NumberFormatException -> 0x00cd }
        goto L_0x00b0;	 Catch:{ NumberFormatException -> 0x00cd }
    L_0x00a2:
        r8 = r8.subSequence(r9, r3);	 Catch:{ NumberFormatException -> 0x00cd }
        r8 = r8.toString();	 Catch:{ NumberFormatException -> 0x00cd }
        r5 = 10;	 Catch:{ NumberFormatException -> 0x00cd }
        r8 = java.lang.Integer.parseInt(r8, r5);	 Catch:{ NumberFormatException -> 0x00cd }
    L_0x00b0:
        r5 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r8 <= r5) goto L_0x00c4;
    L_0x00b5:
        r8 = java.lang.Character.toChars(r8);
        r2 = r8[r2];
        r10.write(r2);
        r8 = r8[r4];
        r10.write(r8);
        goto L_0x00c7;
    L_0x00c4:
        r10.write(r8);
    L_0x00c7:
        r3 = r3 + 2;
        r3 = r3 - r9;
        r3 = r3 + r1;
        r3 = r3 + r0;
        return r3;
    L_0x00cd:
        return r2;
    L_0x00ce:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.text.translate.NumericEntityUnescaper.translate(java.lang.CharSequence, int, java.io.Writer):int");
    }
}
