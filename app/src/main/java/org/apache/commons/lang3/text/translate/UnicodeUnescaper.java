package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.io.IOUtils;

public class UnicodeUnescaper extends CharSequenceTranslator {
    public int translate(CharSequence charSequence, int i, Writer writer) throws IOException {
        StringBuilder stringBuilder;
        if (charSequence.charAt(i) == IOUtils.DIR_SEPARATOR_WINDOWS) {
            int i2 = i + 1;
            if (i2 < charSequence.length() && charSequence.charAt(i2) == 'u') {
                int i3;
                i2 = 2;
                while (true) {
                    i3 = i + i2;
                    if (i3 < charSequence.length() && charSequence.charAt(i3) == 'u') {
                        i2++;
                    }
                }
                if (i3 < charSequence.length() && charSequence.charAt(i3) == '+') {
                    i2++;
                }
                int i4 = i + i2;
                i3 = i4 + 4;
                if (i3 <= charSequence.length()) {
                    charSequence = charSequence.subSequence(i4, i3);
                    try {
                        writer.write((char) Integer.parseInt(charSequence.toString(), 16));
                        return i2 + 4;
                    } catch (int i5) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unable to parse unicode value: ");
                        stringBuilder.append(charSequence);
                        throw new IllegalArgumentException(stringBuilder.toString(), i5);
                    }
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Less than 4 hex digits in unicode value: '");
                stringBuilder.append(charSequence.subSequence(i5, charSequence.length()));
                stringBuilder.append("' due to end of CharSequence");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        return null;
    }
}
