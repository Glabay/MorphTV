package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Arrays;
import java.util.BitSet;
import javax.annotation.CheckReturnValue;
import net.lingala.zip4j.crypto.PBKDF2.BinTools;
import org.apache.commons.io.IOUtils;

@GwtCompatible(emulated = true)
@Beta
public abstract class CharMatcher implements Predicate<Character> {
    public static final CharMatcher ANY = new NamedFastMatcher("CharMatcher.ANY") {
        public boolean matches(char c) {
            return true;
        }

        public int indexIn(CharSequence charSequence) {
            return charSequence.length() == null ? -1 : null;
        }

        public int indexIn(CharSequence charSequence, int i) {
            charSequence = charSequence.length();
            Preconditions.checkPositionIndex(i, charSequence);
            return i == charSequence ? -1 : i;
        }

        public int lastIndexIn(CharSequence charSequence) {
            return charSequence.length() - 1;
        }

        public boolean matchesAllOf(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return true;
        }

        public boolean matchesNoneOf(CharSequence charSequence) {
            return charSequence.length() == null ? true : null;
        }

        public String removeFrom(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return "";
        }

        public String replaceFrom(CharSequence charSequence, char c) {
            charSequence = new char[charSequence.length()];
            Arrays.fill(charSequence, c);
            return new String(charSequence);
        }

        public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
            StringBuilder stringBuilder = new StringBuilder(charSequence.length() * charSequence2.length());
            for (int i = 0; i < charSequence.length(); i++) {
                stringBuilder.append(charSequence2);
            }
            return stringBuilder.toString();
        }

        public String collapseFrom(CharSequence charSequence, char c) {
            return charSequence.length() == null ? "" : String.valueOf(c);
        }

        public String trimFrom(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return "";
        }

        public int countIn(CharSequence charSequence) {
            return charSequence.length();
        }

        public CharMatcher and(CharMatcher charMatcher) {
            return (CharMatcher) Preconditions.checkNotNull(charMatcher);
        }

        public CharMatcher or(CharMatcher charMatcher) {
            Preconditions.checkNotNull(charMatcher);
            return this;
        }

        public CharMatcher negate() {
            return NONE;
        }
    };
    public static final CharMatcher ASCII = new NamedFastMatcher("CharMatcher.ASCII") {
        public boolean matches(char c) {
            return c <= Ascii.MAX;
        }
    };
    public static final CharMatcher BREAKING_WHITESPACE = new C07921();
    public static final CharMatcher DIGIT;
    private static final int DISTINCT_CHARS = 65536;
    public static final CharMatcher INVISIBLE = new RangesMatcher("CharMatcher.INVISIBLE", "\u0000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺".toCharArray(), "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻".toCharArray());
    public static final CharMatcher JAVA_DIGIT = new C07943();
    public static final CharMatcher JAVA_ISO_CONTROL = new NamedFastMatcher("CharMatcher.JAVA_ISO_CONTROL") {
        public boolean matches(char c) {
            if (c > '\u001f') {
                if (c < Ascii.MAX || c > '') {
                    return false;
                }
            }
            return true;
        }
    };
    public static final CharMatcher JAVA_LETTER = new C07954();
    public static final CharMatcher JAVA_LETTER_OR_DIGIT = new C07965();
    public static final CharMatcher JAVA_LOWER_CASE = new C07987();
    public static final CharMatcher JAVA_UPPER_CASE = new C07976();
    public static final CharMatcher NONE = new NamedFastMatcher("CharMatcher.NONE") {
        public boolean matches(char c) {
            return false;
        }

        public int indexIn(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return -1;
        }

        public int indexIn(CharSequence charSequence, int i) {
            Preconditions.checkPositionIndex(i, charSequence.length());
            return -1;
        }

        public int lastIndexIn(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return -1;
        }

        public boolean matchesAllOf(CharSequence charSequence) {
            return charSequence.length() == null ? true : null;
        }

        public boolean matchesNoneOf(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return true;
        }

        public String removeFrom(CharSequence charSequence) {
            return charSequence.toString();
        }

        public String replaceFrom(CharSequence charSequence, char c) {
            return charSequence.toString();
        }

        public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
            Preconditions.checkNotNull(charSequence2);
            return charSequence.toString();
        }

        public String collapseFrom(CharSequence charSequence, char c) {
            return charSequence.toString();
        }

        public String trimFrom(CharSequence charSequence) {
            return charSequence.toString();
        }

        public String trimLeadingFrom(CharSequence charSequence) {
            return charSequence.toString();
        }

        public String trimTrailingFrom(CharSequence charSequence) {
            return charSequence.toString();
        }

        public int countIn(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return null;
        }

        public CharMatcher and(CharMatcher charMatcher) {
            Preconditions.checkNotNull(charMatcher);
            return this;
        }

        public CharMatcher or(CharMatcher charMatcher) {
            return (CharMatcher) Preconditions.checkNotNull(charMatcher);
        }

        public CharMatcher negate() {
            return ANY;
        }
    };
    public static final CharMatcher SINGLE_WIDTH = new RangesMatcher("CharMatcher.SINGLE_WIDTH", "\u0000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ".toCharArray());
    public static final CharMatcher WHITESPACE = new NamedFastMatcher("WHITESPACE") {
        public boolean matches(char c) {
            return CharMatcher.WHITESPACE_TABLE.charAt((CharMatcher.WHITESPACE_MULTIPLIER * c) >>> WHITESPACE_SHIFT) == c;
        }

        @GwtIncompatible("java.util.BitSet")
        void setBits(BitSet bitSet) {
            for (int i = 0; i < CharMatcher.WHITESPACE_TABLE.length(); i++) {
                bitSet.set(CharMatcher.WHITESPACE_TABLE.charAt(i));
            }
        }
    };
    static final int WHITESPACE_MULTIPLIER = 1682554634;
    static final int WHITESPACE_SHIFT = Integer.numberOfLeadingZeros(WHITESPACE_TABLE.length() - 1);
    static final String WHITESPACE_TABLE = " 　\r   　 \u000b　   　 \t     \f 　 　　 \n 　";
    private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";

    static abstract class FastMatcher extends CharMatcher {
        public final CharMatcher precomputed() {
            return this;
        }

        FastMatcher() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public CharMatcher negate() {
            return new NegatedFastMatcher(this);
        }
    }

    static abstract class NamedFastMatcher extends FastMatcher {
        private final String description;

        NamedFastMatcher(String str) {
            this.description = (String) Preconditions.checkNotNull(str);
        }

        public final String toString() {
            return this.description;
        }
    }

    private static class NegatedMatcher extends CharMatcher {
        final CharMatcher original;

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        NegatedMatcher(CharMatcher charMatcher) {
            this.original = (CharMatcher) Preconditions.checkNotNull(charMatcher);
        }

        public boolean matches(char c) {
            return this.original.matches(c) ^ 1;
        }

        public boolean matchesAllOf(CharSequence charSequence) {
            return this.original.matchesNoneOf(charSequence);
        }

        public boolean matchesNoneOf(CharSequence charSequence) {
            return this.original.matchesAllOf(charSequence);
        }

        public int countIn(CharSequence charSequence) {
            return charSequence.length() - this.original.countIn(charSequence);
        }

        @GwtIncompatible("java.util.BitSet")
        void setBits(BitSet bitSet) {
            BitSet bitSet2 = new BitSet();
            this.original.setBits(bitSet2);
            bitSet2.flip(0, 65536);
            bitSet.or(bitSet2);
        }

        public CharMatcher negate() {
            return this.original;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.original);
            stringBuilder.append(".negate()");
            return stringBuilder.toString();
        }
    }

    static class NegatedFastMatcher extends NegatedMatcher {
        public final CharMatcher precomputed() {
            return this;
        }

        NegatedFastMatcher(CharMatcher charMatcher) {
            super(charMatcher);
        }
    }

    /* renamed from: com.google.common.base.CharMatcher$1 */
    static class C07921 extends CharMatcher {
        public boolean matches(char c) {
            boolean z = true;
            if (!(c == ' ' || c == '' || c == ' ')) {
                if (c == ' ') {
                    return false;
                }
                if (!(c == ' ' || c == '　')) {
                    switch (c) {
                        case '\t':
                        case '\n':
                        case '\u000b':
                        case '\f':
                        case '\r':
                            break;
                        default:
                            switch (c) {
                                case ' ':
                                case ' ':
                                    break;
                                default:
                                    if (c < ' ' || c > ' ') {
                                        z = false;
                                    }
                                    return z;
                            }
                    }
                }
            }
            return true;
        }

        public String toString() {
            return "CharMatcher.BREAKING_WHITESPACE";
        }

        C07921() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }
    }

    /* renamed from: com.google.common.base.CharMatcher$3 */
    static class C07943 extends CharMatcher {
        public String toString() {
            return "CharMatcher.JAVA_DIGIT";
        }

        C07943() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isDigit(c);
        }
    }

    /* renamed from: com.google.common.base.CharMatcher$4 */
    static class C07954 extends CharMatcher {
        public String toString() {
            return "CharMatcher.JAVA_LETTER";
        }

        C07954() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isLetter(c);
        }
    }

    /* renamed from: com.google.common.base.CharMatcher$5 */
    static class C07965 extends CharMatcher {
        public String toString() {
            return "CharMatcher.JAVA_LETTER_OR_DIGIT";
        }

        C07965() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isLetterOrDigit(c);
        }
    }

    /* renamed from: com.google.common.base.CharMatcher$6 */
    static class C07976 extends CharMatcher {
        public String toString() {
            return "CharMatcher.JAVA_UPPER_CASE";
        }

        C07976() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isUpperCase(c);
        }
    }

    /* renamed from: com.google.common.base.CharMatcher$7 */
    static class C07987 extends CharMatcher {
        public String toString() {
            return "CharMatcher.JAVA_LOWER_CASE";
        }

        C07987() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isLowerCase(c);
        }
    }

    private static class And extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        And(CharMatcher charMatcher, CharMatcher charMatcher2) {
            this.first = (CharMatcher) Preconditions.checkNotNull(charMatcher);
            this.second = (CharMatcher) Preconditions.checkNotNull(charMatcher2);
        }

        public boolean matches(char c) {
            return this.first.matches(c) && this.second.matches(c) != '\u0000';
        }

        @GwtIncompatible("java.util.BitSet")
        void setBits(BitSet bitSet) {
            BitSet bitSet2 = new BitSet();
            this.first.setBits(bitSet2);
            BitSet bitSet3 = new BitSet();
            this.second.setBits(bitSet3);
            bitSet2.and(bitSet3);
            bitSet.or(bitSet2);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CharMatcher.and(");
            stringBuilder.append(this.first);
            stringBuilder.append(", ");
            stringBuilder.append(this.second);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    @GwtIncompatible("java.util.BitSet")
    private static class BitSetMatcher extends NamedFastMatcher {
        private final BitSet table;

        private BitSetMatcher(BitSet bitSet, String str) {
            super(str);
            if (bitSet.length() + 64 < bitSet.size()) {
                bitSet = (BitSet) bitSet.clone();
            }
            this.table = bitSet;
        }

        public boolean matches(char c) {
            return this.table.get(c);
        }

        void setBits(BitSet bitSet) {
            bitSet.or(this.table);
        }
    }

    private static class Or extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        Or(CharMatcher charMatcher, CharMatcher charMatcher2) {
            this.first = (CharMatcher) Preconditions.checkNotNull(charMatcher);
            this.second = (CharMatcher) Preconditions.checkNotNull(charMatcher2);
        }

        @GwtIncompatible("java.util.BitSet")
        void setBits(BitSet bitSet) {
            this.first.setBits(bitSet);
            this.second.setBits(bitSet);
        }

        public boolean matches(char c) {
            if (!this.first.matches(c)) {
                if (this.second.matches(c) == '\u0000') {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CharMatcher.or(");
            stringBuilder.append(this.first);
            stringBuilder.append(", ");
            stringBuilder.append(this.second);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class RangesMatcher extends CharMatcher {
        private final String description;
        private final char[] rangeEnds;
        private final char[] rangeStarts;

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        RangesMatcher(String str, char[] cArr, char[] cArr2) {
            this.description = str;
            this.rangeStarts = cArr;
            this.rangeEnds = cArr2;
            Preconditions.checkArgument(cArr.length == cArr2.length ? true : null);
            str = null;
            while (str < cArr.length) {
                Preconditions.checkArgument(cArr[str] <= cArr2[str]);
                int i = str + 1;
                if (i < cArr.length) {
                    Preconditions.checkArgument(cArr2[str] < cArr[i] ? true : null);
                }
                str = i;
            }
        }

        public boolean matches(char c) {
            int binarySearch = Arrays.binarySearch(this.rangeStarts, c);
            boolean z = true;
            if (binarySearch >= 0) {
                return true;
            }
            binarySearch = (binarySearch ^ -1) - 1;
            if (binarySearch < 0 || c > this.rangeEnds[binarySearch]) {
                z = false;
            }
            return z;
        }

        public String toString() {
            return this.description;
        }
    }

    @GwtIncompatible("SmallCharMatcher")
    private static boolean isSmall(int i, int i2) {
        return i <= 1023 && i2 > (i * 4) * 16;
    }

    public abstract boolean matches(char c);

    static {
        char[] toCharArray = ZEROES.toCharArray();
        char[] cArr = new char[toCharArray.length];
        for (int i = 0; i < toCharArray.length; i++) {
            cArr[i] = (char) (toCharArray[i] + 9);
        }
        DIGIT = new RangesMatcher("CharMatcher.DIGIT", toCharArray, cArr);
    }

    private static String showCharacter(char c) {
        String str = BinTools.hex;
        char[] cArr = new char[]{IOUtils.DIR_SEPARATOR_WINDOWS, 'u', '\u0000', '\u0000', '\u0000', '\u0000'};
        for (int i = 0; i < 4; i++) {
            cArr[5 - i] = str.charAt(c & 15);
            c = (char) (c >> '\u0004');
        }
        return String.copyValueOf(cArr);
    }

    public static CharMatcher is(final char c) {
        return new FastMatcher() {
            public boolean matches(char c) {
                return c == c;
            }

            public String replaceFrom(CharSequence charSequence, char c) {
                return charSequence.toString().replace(c, c);
            }

            public CharMatcher and(CharMatcher charMatcher) {
                return charMatcher.matches(c) != null ? this : NONE;
            }

            public CharMatcher or(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? charMatcher : super.or(charMatcher);
            }

            public CharMatcher negate() {
                return CharMatcher.isNot(c);
            }

            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet bitSet) {
                bitSet.set(c);
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CharMatcher.is('");
                stringBuilder.append(CharMatcher.showCharacter(c));
                stringBuilder.append("')");
                return stringBuilder.toString();
            }
        };
    }

    public static CharMatcher isNot(final char c) {
        return new FastMatcher() {
            public boolean matches(char c) {
                return c != c;
            }

            public CharMatcher and(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? super.and(charMatcher) : charMatcher;
            }

            public CharMatcher or(CharMatcher charMatcher) {
                return charMatcher.matches(c) != null ? ANY : this;
            }

            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet bitSet) {
                bitSet.set(0, c);
                bitSet.set(c + 1, 65536);
            }

            public CharMatcher negate() {
                return CharMatcher.is(c);
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CharMatcher.isNot('");
                stringBuilder.append(CharMatcher.showCharacter(c));
                stringBuilder.append("')");
                return stringBuilder.toString();
            }
        };
    }

    public static CharMatcher anyOf(CharSequence charSequence) {
        switch (charSequence.length()) {
            case 0:
                return NONE;
            case 1:
                return is(charSequence.charAt(0));
            case 2:
                return isEither(charSequence.charAt(0), charSequence.charAt(1));
            default:
                charSequence = charSequence.toString().toCharArray();
                Arrays.sort(charSequence);
                return new CharMatcher() {
                    public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                        return super.apply((Character) obj);
                    }

                    public boolean matches(char c) {
                        return Arrays.binarySearch(charSequence, c) >= '\u0000';
                    }

                    @GwtIncompatible("java.util.BitSet")
                    void setBits(BitSet bitSet) {
                        for (char c : charSequence) {
                            bitSet.set(c);
                        }
                    }

                    public String toString() {
                        StringBuilder stringBuilder = new StringBuilder("CharMatcher.anyOf(\"");
                        for (char access$000 : charSequence) {
                            stringBuilder.append(CharMatcher.showCharacter(access$000));
                        }
                        stringBuilder.append("\")");
                        return stringBuilder.toString();
                    }
                };
        }
    }

    private static CharMatcher isEither(final char c, final char c2) {
        return new FastMatcher() {
            public boolean matches(char c) {
                if (c != c) {
                    if (c != c2) {
                        return false;
                    }
                }
                return true;
            }

            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet bitSet) {
                bitSet.set(c);
                bitSet.set(c2);
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CharMatcher.anyOf(\"");
                stringBuilder.append(CharMatcher.showCharacter(c));
                stringBuilder.append(CharMatcher.showCharacter(c2));
                stringBuilder.append("\")");
                return stringBuilder.toString();
            }
        };
    }

    public static CharMatcher noneOf(CharSequence charSequence) {
        return anyOf(charSequence).negate();
    }

    public static CharMatcher inRange(final char c, final char c2) {
        Preconditions.checkArgument(c2 >= c);
        return new FastMatcher() {
            public boolean matches(char c) {
                return c <= c && c <= c2;
            }

            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet bitSet) {
                bitSet.set(c, c2 + 1);
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CharMatcher.inRange('");
                stringBuilder.append(CharMatcher.showCharacter(c));
                stringBuilder.append("', '");
                stringBuilder.append(CharMatcher.showCharacter(c2));
                stringBuilder.append("')");
                return stringBuilder.toString();
            }
        };
    }

    public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
        Preconditions.checkNotNull(predicate);
        if (predicate instanceof CharMatcher) {
            return (CharMatcher) predicate;
        }
        return new CharMatcher() {
            public boolean matches(char c) {
                return predicate.apply(Character.valueOf(c));
            }

            public boolean apply(Character ch) {
                return predicate.apply(Preconditions.checkNotNull(ch));
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CharMatcher.forPredicate(");
                stringBuilder.append(predicate);
                stringBuilder.append(")");
                return stringBuilder.toString();
            }
        };
    }

    protected CharMatcher() {
    }

    public CharMatcher negate() {
        return new NegatedMatcher(this);
    }

    public CharMatcher and(CharMatcher charMatcher) {
        return new And(this, charMatcher);
    }

    public CharMatcher or(CharMatcher charMatcher) {
        return new Or(this, charMatcher);
    }

    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }

    @GwtIncompatible("java.util.BitSet")
    CharMatcher precomputedInternal() {
        BitSet bitSet = new BitSet();
        setBits(bitSet);
        int cardinality = bitSet.cardinality();
        if (cardinality * 2 <= 65536) {
            return precomputedPositive(cardinality, bitSet, toString());
        }
        bitSet.flip(0, 65536);
        int i = 65536 - cardinality;
        String str = ".negate()";
        final String charMatcher = toString();
        if (charMatcher.endsWith(str)) {
            str = charMatcher.substring(0, charMatcher.length() - str.length());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(charMatcher);
            stringBuilder.append(str);
            str = stringBuilder.toString();
        }
        return new NegatedFastMatcher(precomputedPositive(i, bitSet, str)) {
            public String toString() {
                return charMatcher;
            }
        };
    }

    @GwtIncompatible("java.util.BitSet")
    private static CharMatcher precomputedPositive(int i, BitSet bitSet, String str) {
        switch (i) {
            case 0:
                return NONE;
            case 1:
                return is((char) bitSet.nextSetBit(0));
            case 2:
                i = (char) bitSet.nextSetBit(0);
                return isEither(i, (char) bitSet.nextSetBit(i + 1));
            default:
                return isSmall(i, bitSet.length()) != 0 ? SmallCharMatcher.from(bitSet, str) : new BitSetMatcher(bitSet, str);
        }
    }

    @GwtIncompatible("java.util.BitSet")
    void setBits(BitSet bitSet) {
        for (int i = 65535; i >= 0; i--) {
            if (matches((char) i)) {
                bitSet.set(i);
            }
        }
    }

    public boolean matchesAnyOf(CharSequence charSequence) {
        return matchesNoneOf(charSequence) ^ 1;
    }

    public boolean matchesAllOf(CharSequence charSequence) {
        for (int length = charSequence.length() - 1; length >= 0; length--) {
            if (!matches(charSequence.charAt(length))) {
                return null;
            }
        }
        return true;
    }

    public boolean matchesNoneOf(CharSequence charSequence) {
        return indexIn(charSequence) == -1 ? true : null;
    }

    public int indexIn(CharSequence charSequence) {
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (matches(charSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int indexIn(CharSequence charSequence, int i) {
        int length = charSequence.length();
        Preconditions.checkPositionIndex(i, length);
        while (i < length) {
            if (matches(charSequence.charAt(i))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int lastIndexIn(CharSequence charSequence) {
        for (int length = charSequence.length() - 1; length >= 0; length--) {
            if (matches(charSequence.charAt(length))) {
                return length;
            }
        }
        return -1;
    }

    public int countIn(CharSequence charSequence) {
        int i = 0;
        for (int i2 = 0; i2 < charSequence.length(); i2++) {
            if (matches(charSequence.charAt(i2))) {
                i++;
            }
        }
        return i;
    }

    @CheckReturnValue
    public String removeFrom(CharSequence charSequence) {
        charSequence = charSequence.toString();
        int indexIn = indexIn(charSequence);
        if (indexIn == -1) {
            return charSequence;
        }
        charSequence = charSequence.toCharArray();
        int i = 1;
        while (true) {
            indexIn++;
            while (indexIn != charSequence.length) {
                if (matches(charSequence[indexIn])) {
                    i++;
                } else {
                    charSequence[indexIn - i] = charSequence[indexIn];
                    indexIn++;
                }
            }
            return new String(charSequence, 0, indexIn - i);
        }
    }

    @CheckReturnValue
    public String retainFrom(CharSequence charSequence) {
        return negate().removeFrom(charSequence);
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence charSequence, char c) {
        charSequence = charSequence.toString();
        int indexIn = indexIn(charSequence);
        if (indexIn == -1) {
            return charSequence;
        }
        charSequence = charSequence.toCharArray();
        charSequence[indexIn] = c;
        while (true) {
            indexIn++;
            if (indexIn >= charSequence.length) {
                return new String(charSequence);
            }
            if (matches(charSequence[indexIn])) {
                charSequence[indexIn] = c;
            }
        }
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
        int length = charSequence2.length();
        if (length == 0) {
            return removeFrom(charSequence);
        }
        int i = 0;
        if (length == 1) {
            return replaceFrom(charSequence, charSequence2.charAt(0));
        }
        charSequence = charSequence.toString();
        length = indexIn(charSequence);
        if (length == -1) {
            return charSequence;
        }
        int length2 = charSequence.length();
        StringBuilder stringBuilder = new StringBuilder(((length2 * 3) / 2) + 16);
        do {
            stringBuilder.append(charSequence, i, length);
            stringBuilder.append(charSequence2);
            i = length + 1;
            length = indexIn(charSequence, i);
        } while (length != -1);
        stringBuilder.append(charSequence, i, length2);
        return stringBuilder.toString();
    }

    @CheckReturnValue
    public String trimFrom(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            if (!matches(charSequence.charAt(i))) {
                break;
            }
            i++;
        }
        length--;
        while (length > i) {
            if (!matches(charSequence.charAt(length))) {
                break;
            }
            length--;
        }
        return charSequence.subSequence(i, length + 1).toString();
    }

    @CheckReturnValue
    public String trimLeadingFrom(CharSequence charSequence) {
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!matches(charSequence.charAt(i))) {
                return charSequence.subSequence(i, length).toString();
            }
        }
        return "";
    }

    @CheckReturnValue
    public String trimTrailingFrom(CharSequence charSequence) {
        for (int length = charSequence.length() - 1; length >= 0; length--) {
            if (!matches(charSequence.charAt(length))) {
                return charSequence.subSequence(0, length + 1).toString();
            }
        }
        return "";
    }

    @CheckReturnValue
    public String collapseFrom(CharSequence charSequence, char c) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            char charAt = charSequence.charAt(i);
            if (matches(charAt)) {
                if (charAt != c || (i != length - 1 && matches(charSequence.charAt(i + 1)))) {
                    StringBuilder stringBuilder = new StringBuilder(length);
                    stringBuilder.append(charSequence.subSequence(0, i));
                    stringBuilder.append(c);
                    return finishCollapseFrom(charSequence, i + 1, length, c, stringBuilder, true);
                }
                i++;
            }
            i++;
        }
        return charSequence.toString();
    }

    @CheckReturnValue
    public String trimAndCollapseFrom(CharSequence charSequence, char c) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && matches(charSequence.charAt(i))) {
            i++;
        }
        length--;
        int i2 = length;
        while (i2 > i && matches(charSequence.charAt(i2))) {
            i2--;
        }
        if (i == 0 && i2 == length) {
            return collapseFrom(charSequence, c);
        }
        int i3 = i2 + 1;
        return finishCollapseFrom(charSequence, i, i3, c, new StringBuilder(i3 - i), false);
    }

    private String finishCollapseFrom(CharSequence charSequence, int i, int i2, char c, StringBuilder stringBuilder, boolean z) {
        while (i < i2) {
            char charAt = charSequence.charAt(i);
            if (!matches(charAt)) {
                stringBuilder.append(charAt);
                z = false;
            } else if (!z) {
                stringBuilder.append(c);
                z = true;
            }
            i++;
        }
        return stringBuilder.toString();
    }

    @Deprecated
    public boolean apply(Character ch) {
        return matches(ch.charValue());
    }

    public String toString() {
        return super.toString();
    }
}
