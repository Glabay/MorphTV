package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

public class LookupTranslator extends CharSequenceTranslator {
    private final int longest;
    private final HashMap<String, String> lookupMap = new HashMap();
    private final HashSet<Character> prefixSet = new HashSet();
    private final int shortest;

    public LookupTranslator(CharSequence[]... charSequenceArr) {
        int i = 0;
        int i2 = Integer.MAX_VALUE;
        if (charSequenceArr != null) {
            int i3 = Integer.MAX_VALUE;
            int i4 = 0;
            for (CharSequence[] charSequenceArr2 : charSequenceArr) {
                this.lookupMap.put(charSequenceArr2[0].toString(), charSequenceArr2[1].toString());
                this.prefixSet.add(Character.valueOf(charSequenceArr2[0].charAt(0)));
                int length = charSequenceArr2[0].length();
                if (length < i3) {
                    i3 = length;
                }
                if (length > i4) {
                    i4 = length;
                }
            }
            i2 = i3;
            i = i4;
        }
        this.shortest = i2;
        this.longest = i;
    }

    public int translate(CharSequence charSequence, int i, Writer writer) throws IOException {
        if (this.prefixSet.contains(Character.valueOf(charSequence.charAt(i)))) {
            int i2 = this.longest;
            if (this.longest + i > charSequence.length()) {
                i2 = charSequence.length() - i;
            }
            while (i2 >= this.shortest) {
                String str = (String) this.lookupMap.get(charSequence.subSequence(i, i + i2).toString());
                if (str != null) {
                    writer.write(str);
                    return i2;
                }
                i2--;
            }
        }
        return null;
    }
}
