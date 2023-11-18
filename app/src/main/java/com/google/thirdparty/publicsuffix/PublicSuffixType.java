package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
enum PublicSuffixType {
    PRIVATE(':', ','),
    ICANN('!', '?');
    
    private final char innerNodeCode;
    private final char leafNodeCode;

    private PublicSuffixType(char c, char c2) {
        this.innerNodeCode = c;
        this.leafNodeCode = c2;
    }

    char getLeafNodeCode() {
        return this.leafNodeCode;
    }

    char getInnerNodeCode() {
        return this.innerNodeCode;
    }

    static PublicSuffixType fromCode(char c) {
        PublicSuffixType[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            PublicSuffixType publicSuffixType = values[i];
            if (publicSuffixType.getInnerNodeCode() != c) {
                if (publicSuffixType.getLeafNodeCode() != c) {
                    i++;
                }
            }
            return publicSuffixType;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No enum corresponding to given code: ");
        stringBuilder.append(c);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    static PublicSuffixType fromIsPrivate(boolean z) {
        return z ? PRIVATE : ICANN;
    }
}
