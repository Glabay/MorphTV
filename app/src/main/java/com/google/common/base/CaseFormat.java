package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
public enum CaseFormat {
    LOWER_HYPHEN(CharMatcher.is('-'), "-") {
        String normalizeWord(String str) {
            return Ascii.toLowerCase(str);
        }

        String convert(CaseFormat caseFormat, String str) {
            if (caseFormat == LOWER_UNDERSCORE) {
                return str.replace('-', '_');
            }
            if (caseFormat == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(str.replace('-', '_'));
            }
            return super.convert(caseFormat, str);
        }
    },
    LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
        String normalizeWord(String str) {
            return Ascii.toLowerCase(str);
        }

        String convert(CaseFormat caseFormat, String str) {
            if (caseFormat == LOWER_HYPHEN) {
                return str.replace('_', '-');
            }
            if (caseFormat == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(str);
            }
            return super.convert(caseFormat, str);
        }
    },
    LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        String normalizeWord(String str) {
            return CaseFormat.firstCharOnlyToUpper(str);
        }
    },
    UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        String normalizeWord(String str) {
            return CaseFormat.firstCharOnlyToUpper(str);
        }
    },
    UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
        String normalizeWord(String str) {
            return Ascii.toUpperCase(str);
        }

        String convert(CaseFormat caseFormat, String str) {
            if (caseFormat == LOWER_HYPHEN) {
                return Ascii.toLowerCase(str.replace('_', '-'));
            }
            if (caseFormat == LOWER_UNDERSCORE) {
                return Ascii.toLowerCase(str);
            }
            return super.convert(caseFormat, str);
        }
    };
    
    private final CharMatcher wordBoundary;
    private final String wordSeparator;

    private static final class StringConverter extends Converter<String, String> implements Serializable {
        private static final long serialVersionUID = 0;
        private final CaseFormat sourceFormat;
        private final CaseFormat targetFormat;

        StringConverter(CaseFormat caseFormat, CaseFormat caseFormat2) {
            this.sourceFormat = (CaseFormat) Preconditions.checkNotNull(caseFormat);
            this.targetFormat = (CaseFormat) Preconditions.checkNotNull(caseFormat2);
        }

        protected String doForward(String str) {
            return str == null ? null : this.sourceFormat.to(this.targetFormat, str);
        }

        protected String doBackward(String str) {
            return str == null ? null : this.targetFormat.to(this.sourceFormat, str);
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof StringConverter)) {
                return false;
            }
            StringConverter stringConverter = (StringConverter) obj;
            if (this.sourceFormat.equals(stringConverter.sourceFormat) && this.targetFormat.equals(stringConverter.targetFormat) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.sourceFormat);
            stringBuilder.append(".converterTo(");
            stringBuilder.append(this.targetFormat);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    abstract String normalizeWord(String str);

    private CaseFormat(CharMatcher charMatcher, String str) {
        this.wordBoundary = charMatcher;
        this.wordSeparator = str;
    }

    public final String to(CaseFormat caseFormat, String str) {
        Preconditions.checkNotNull(caseFormat);
        Preconditions.checkNotNull(str);
        return caseFormat == this ? str : convert(caseFormat, str);
    }

    String convert(CaseFormat caseFormat, String str) {
        int i = 0;
        StringBuilder stringBuilder = null;
        int i2 = -1;
        while (true) {
            i2 = this.wordBoundary.indexIn(str, i2 + 1);
            if (i2 == -1) {
                break;
            }
            if (i == 0) {
                stringBuilder = new StringBuilder(str.length() + (this.wordSeparator.length() * 4));
                stringBuilder.append(caseFormat.normalizeFirstWord(str.substring(i, i2)));
            } else {
                stringBuilder.append(caseFormat.normalizeWord(str.substring(i, i2)));
            }
            stringBuilder.append(caseFormat.wordSeparator);
            i = this.wordSeparator.length() + i2;
        }
        if (i == 0) {
            return caseFormat.normalizeFirstWord(str);
        }
        stringBuilder.append(caseFormat.normalizeWord(str.substring(i)));
        return stringBuilder.toString();
    }

    @Beta
    public Converter<String, String> converterTo(CaseFormat caseFormat) {
        return new StringConverter(this, caseFormat);
    }

    private String normalizeFirstWord(String str) {
        return this == LOWER_CAMEL ? Ascii.toLowerCase(str) : normalizeWord(str);
    }

    private static String firstCharOnlyToUpper(String str) {
        if (str.isEmpty()) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(str.length());
        stringBuilder.append(Ascii.toUpperCase(str.charAt(0)));
        stringBuilder.append(Ascii.toLowerCase(str.substring(1)));
        return stringBuilder.toString();
    }
}
