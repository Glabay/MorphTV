package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public abstract class CharSequenceTranslator {
    static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public abstract int translate(CharSequence charSequence, int i, Writer writer) throws IOException;

    public final String translate(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        try {
            Writer stringWriter = new StringWriter(charSequence.length() * 2);
            translate(charSequence, stringWriter);
            return stringWriter.toString();
        } catch (CharSequence charSequence2) {
            throw new RuntimeException(charSequence2);
        }
    }

    public final void translate(CharSequence charSequence, Writer writer) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (charSequence != null) {
            int length = charSequence.length();
            int i = 0;
            while (i < length) {
                int translate = translate(charSequence, i, writer);
                if (translate == 0) {
                    char charAt = charSequence.charAt(i);
                    writer.write(charAt);
                    i++;
                    if (Character.isHighSurrogate(charAt) && i < length) {
                        charAt = charSequence.charAt(i);
                        if (Character.isLowSurrogate(charAt)) {
                            writer.write(charAt);
                            i++;
                        }
                    }
                } else {
                    int i2 = i;
                    for (i = 0; i < translate; i++) {
                        i2 += Character.charCount(Character.codePointAt(charSequence, i2));
                    }
                    i = i2;
                }
            }
        }
    }

    public final CharSequenceTranslator with(CharSequenceTranslator... charSequenceTranslatorArr) {
        Object obj = new CharSequenceTranslator[(charSequenceTranslatorArr.length + 1)];
        obj[0] = this;
        System.arraycopy(charSequenceTranslatorArr, 0, obj, 1, charSequenceTranslatorArr.length);
        return new AggregateTranslator(obj);
    }

    public static String hex(int i) {
        return Integer.toHexString(i).toUpperCase(Locale.ENGLISH);
    }
}
