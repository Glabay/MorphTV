package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
public final class InternetDomainName {
    private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
    private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
    private static final Joiner DOT_JOINER = Joiner.on('.');
    private static final String DOT_REGEX = "\\.";
    private static final Splitter DOT_SPLITTER = Splitter.on('.');
    private static final int MAX_DOMAIN_PART_LENGTH = 63;
    private static final int MAX_LENGTH = 253;
    private static final int MAX_PARTS = 127;
    private static final int NO_PUBLIC_SUFFIX_FOUND = -1;
    private static final CharMatcher PART_CHAR_MATCHER = CharMatcher.JAVA_LETTER_OR_DIGIT.or(DASH_MATCHER);
    private final String name;
    private final ImmutableList<String> parts;
    private final int publicSuffixIndex;

    InternetDomainName(String str) {
        str = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom((CharSequence) str, '.'));
        if (str.endsWith(".")) {
            str = str.substring(0, str.length() - 1);
        }
        Preconditions.checkArgument(str.length() <= MAX_LENGTH, "Domain name too long: '%s':", str);
        this.name = str;
        this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(str));
        Preconditions.checkArgument(this.parts.size() <= MAX_PARTS, "Domain has too many parts: '%s'", str);
        Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", str);
        this.publicSuffixIndex = findPublicSuffix();
    }

    private int findPublicSuffix() {
        int size = this.parts.size();
        for (int i = 0; i < size; i++) {
            String join = DOT_JOINER.join(this.parts.subList(i, size));
            if (PublicSuffixPatterns.EXACT.containsKey(join)) {
                return i;
            }
            if (PublicSuffixPatterns.EXCLUDED.containsKey(join)) {
                return i + 1;
            }
            if (matchesWildcardPublicSuffix(join)) {
                return i;
            }
        }
        return -1;
    }

    public static InternetDomainName from(String str) {
        return new InternetDomainName((String) Preconditions.checkNotNull(str));
    }

    private static boolean validateSyntax(List<String> list) {
        int size = list.size() - 1;
        if (!validatePart((String) list.get(size), true)) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!validatePart((String) list.get(i), false)) {
                return false;
            }
        }
        return true;
    }

    private static boolean validatePart(String str, boolean z) {
        if (str.length() >= 1) {
            if (str.length() <= 63) {
                if (PART_CHAR_MATCHER.matchesAllOf(CharMatcher.ASCII.retainFrom(str)) && !DASH_MATCHER.matches(str.charAt(0))) {
                    if (!DASH_MATCHER.matches(str.charAt(str.length() - 1))) {
                        return !z || CharMatcher.DIGIT.matches(str.charAt(0)) == null;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public ImmutableList<String> parts() {
        return this.parts;
    }

    public boolean isPublicSuffix() {
        return this.publicSuffixIndex == 0;
    }

    public boolean hasPublicSuffix() {
        return this.publicSuffixIndex != -1;
    }

    public InternetDomainName publicSuffix() {
        return hasPublicSuffix() ? ancestor(this.publicSuffixIndex) : null;
    }

    public boolean isUnderPublicSuffix() {
        return this.publicSuffixIndex > 0;
    }

    public boolean isTopPrivateDomain() {
        return this.publicSuffixIndex == 1;
    }

    public InternetDomainName topPrivateDomain() {
        if (isTopPrivateDomain()) {
            return this;
        }
        Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
        return ancestor(this.publicSuffixIndex - 1);
    }

    public boolean hasParent() {
        return this.parts.size() > 1;
    }

    public InternetDomainName parent() {
        Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
        return ancestor(1);
    }

    private InternetDomainName ancestor(int i) {
        return from(DOT_JOINER.join(this.parts.subList(i, this.parts.size())));
    }

    public InternetDomainName child(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String) Preconditions.checkNotNull(str));
        stringBuilder.append(".");
        stringBuilder.append(this.name);
        return from(stringBuilder.toString());
    }

    public static boolean isValid(java.lang.String r0) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        from(r0);	 Catch:{ IllegalArgumentException -> 0x0005 }
        r0 = 1;
        return r0;
    L_0x0005:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.InternetDomainName.isValid(java.lang.String):boolean");
    }

    private static boolean matchesWildcardPublicSuffix(String str) {
        str = str.split(DOT_REGEX, 2);
        return str.length == 2 && PublicSuffixPatterns.UNDER.containsKey(str[1]) != null;
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof InternetDomainName)) {
            return null;
        }
        return this.name.equals(((InternetDomainName) obj).name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
