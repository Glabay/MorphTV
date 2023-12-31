package org.apache.commons.io.serialization;

import org.apache.commons.io.FilenameUtils;

final class WildcardClassNameMatcher implements ClassNameMatcher {
    private final String pattern;

    public WildcardClassNameMatcher(String str) {
        this.pattern = str;
    }

    public boolean matches(String str) {
        return FilenameUtils.wildcardMatch(str, this.pattern);
    }
}
