package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import java.util.List;
import kotlin.text.Typography;

@GwtCompatible
class TrieParser {
    private static final Joiner PREFIX_JOINER = Joiner.on("");

    TrieParser() {
    }

    static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence charSequence) {
        Builder builder = ImmutableMap.builder();
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            i += doParseTrieToBuilder(Lists.newLinkedList(), charSequence.subSequence(i, length), builder);
        }
        return builder.build();
    }

    private static int doParseTrieToBuilder(List<CharSequence> list, CharSequence charSequence, Builder<String, PublicSuffixType> builder) {
        int length = charSequence.length();
        int i = 0;
        char c = '\u0000';
        while (i < length) {
            c = charSequence.charAt(i);
            if (c == Typography.amp || c == '?' || c == '!' || c == ':') {
                break;
            } else if (c == ',') {
                break;
            } else {
                i++;
            }
        }
        list.add(0, reverse(charSequence.subSequence(0, i)));
        if (c == '!' || c == '?' || c == ':' || c == ',') {
            String join = PREFIX_JOINER.join(list);
            if (join.length() > 0) {
                builder.put(join, PublicSuffixType.fromCode(c));
            }
        }
        i++;
        if (c != '?' && c != ',') {
            while (i < length) {
                i += doParseTrieToBuilder(list, charSequence.subSequence(i, length), builder);
                if (charSequence.charAt(i) != '?') {
                    if (charSequence.charAt(i) == ',') {
                    }
                }
                i++;
                break;
            }
        }
        list.remove(0);
        return i;
    }

    private static CharSequence reverse(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 1;
        if (length <= 1) {
            return charSequence;
        }
        char[] cArr = new char[length];
        int i2 = length - 1;
        cArr[0] = charSequence.charAt(i2);
        while (i < length) {
            cArr[i] = charSequence.charAt(i2 - i);
            int i3 = i - 1;
            if (Character.isSurrogatePair(cArr[i], cArr[i3])) {
                swap(cArr, i3, i);
            }
            i++;
        }
        return new String(cArr);
    }

    private static void swap(char[] cArr, int i, int i2) {
        char c = cArr[i];
        cArr[i] = cArr[i2];
        cArr[i2] = c;
    }
}
