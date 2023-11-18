package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import net.lingala.zip4j.crypto.PBKDF2.BinTools;

@GwtCompatible(emulated = true)
@Beta
public abstract class BaseEncoding {
    private static final BaseEncoding BASE16 = new StandardBaseEncoding("base16()", BinTools.hex, null);
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", Character.valueOf('='));
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", Character.valueOf('='));
    private static final BaseEncoding BASE64 = new StandardBaseEncoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Character.valueOf('='));
    private static final BaseEncoding BASE64_URL = new StandardBaseEncoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", Character.valueOf('='));

    private static final class Alphabet extends CharMatcher {
        final int bitsPerChar;
        final int bytesPerChunk;
        private final char[] chars;
        final int charsPerChunk;
        private final byte[] decodabet;
        final int mask;
        private final String name;
        private final boolean[] validPadding;

        Alphabet(String str, char[] cArr) {
            this.name = (String) Preconditions.checkNotNull(str);
            this.chars = (char[]) Preconditions.checkNotNull(cArr);
            try {
                this.bitsPerChar = IntMath.log2(cArr.length, RoundingMode.UNNECESSARY);
                str = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
                this.charsPerChunk = 8 / str;
                this.bytesPerChunk = this.bitsPerChar / str;
                this.mask = cArr.length - 1;
                str = new byte[128];
                Arrays.fill(str, (byte) -1);
                int i = 0;
                for (int i2 = 0; i2 < cArr.length; i2++) {
                    char c = cArr[i2];
                    Preconditions.checkArgument(CharMatcher.ASCII.matches(c), "Non-ASCII character: %s", Character.valueOf(c));
                    Preconditions.checkArgument(str[c] == (byte) -1, "Duplicate character: %s", Character.valueOf(c));
                    str[c] = (byte) i2;
                }
                this.decodabet = str;
                str = new boolean[this.charsPerChunk];
                while (i < this.bytesPerChunk) {
                    str[IntMath.divide(i * 8, this.bitsPerChar, RoundingMode.CEILING)] = 1;
                    i++;
                }
                this.validPadding = str;
            } catch (String str2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Illegal alphabet length ");
                stringBuilder.append(cArr.length);
                throw new IllegalArgumentException(stringBuilder.toString(), str2);
            }
        }

        char encode(int i) {
            return this.chars[i];
        }

        boolean isValidPaddingStartPosition(int i) {
            return this.validPadding[i % this.charsPerChunk];
        }

        int decode(char c) throws IOException {
            if (c <= Ascii.MAX) {
                if (this.decodabet[c] != (byte) -1) {
                    return this.decodabet[c];
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unrecognized character: ");
            stringBuilder.append(c);
            throw new DecodingException(stringBuilder.toString());
        }

        private boolean hasLowerCase() {
            for (char isLowerCase : this.chars) {
                if (Ascii.isLowerCase(isLowerCase)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasUpperCase() {
            for (char isUpperCase : this.chars) {
                if (Ascii.isUpperCase(isUpperCase)) {
                    return true;
                }
            }
            return false;
        }

        Alphabet upperCase() {
            if (!hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(hasUpperCase() ^ 1, "Cannot call upperCase() on a mixed-case alphabet");
            char[] cArr = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; i++) {
                cArr[i] = Ascii.toUpperCase(this.chars[i]);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.name);
            stringBuilder.append(".upperCase()");
            return new Alphabet(stringBuilder.toString(), cArr);
        }

        Alphabet lowerCase() {
            if (!hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(hasLowerCase() ^ 1, "Cannot call lowerCase() on a mixed-case alphabet");
            char[] cArr = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; i++) {
                cArr[i] = Ascii.toLowerCase(this.chars[i]);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.name);
            stringBuilder.append(".lowerCase()");
            return new Alphabet(stringBuilder.toString(), cArr);
        }

        public boolean matches(char c) {
            return CharMatcher.ASCII.matches(c) && this.decodabet[c] != '￿';
        }

        public String toString() {
            return this.name;
        }
    }

    public static final class DecodingException extends IOException {
        DecodingException(String str) {
            super(str);
        }

        DecodingException(Throwable th) {
            super(th);
        }
    }

    static final class SeparatedBaseEncoding extends BaseEncoding {
        private final int afterEveryChars;
        private final BaseEncoding delegate;
        private final String separator;
        private final CharMatcher separatorChars;

        SeparatedBaseEncoding(BaseEncoding baseEncoding, String str, int i) {
            this.delegate = (BaseEncoding) Preconditions.checkNotNull(baseEncoding);
            this.separator = (String) Preconditions.checkNotNull(str);
            this.afterEveryChars = i;
            Preconditions.checkArgument(i > 0, "Cannot add a separator after every %s chars", Integer.valueOf(i));
            this.separatorChars = CharMatcher.anyOf(str).precomputed();
        }

        CharMatcher padding() {
            return this.delegate.padding();
        }

        int maxEncodedSize(int i) {
            i = this.delegate.maxEncodedSize(i);
            return i + (this.separator.length() * IntMath.divide(Math.max(0, i - 1), this.afterEveryChars, RoundingMode.FLOOR));
        }

        ByteOutput encodingStream(CharOutput charOutput) {
            return this.delegate.encodingStream(BaseEncoding.separatingOutput(charOutput, this.separator, this.afterEveryChars));
        }

        int maxDecodedSize(int i) {
            return this.delegate.maxDecodedSize(i);
        }

        ByteInput decodingStream(CharInput charInput) {
            return this.delegate.decodingStream(BaseEncoding.ignoringInput(charInput, this.separatorChars));
        }

        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding withPadChar(char c) {
            return this.delegate.withPadChar(c).withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding withSeparator(String str, int i) {
            throw new UnsupportedOperationException("Already have a separator");
        }

        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.delegate.toString());
            stringBuilder.append(".withSeparator(\"");
            stringBuilder.append(this.separator);
            stringBuilder.append("\", ");
            stringBuilder.append(this.afterEveryChars);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static final class StandardBaseEncoding extends BaseEncoding {
        private final Alphabet alphabet;
        private transient BaseEncoding lowerCase;
        @Nullable
        private final Character paddingChar;
        private transient BaseEncoding upperCase;

        StandardBaseEncoding(String str, String str2, @Nullable Character ch) {
            this(new Alphabet(str, str2.toCharArray()), ch);
        }

        StandardBaseEncoding(Alphabet alphabet, @Nullable Character ch) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(alphabet);
            if (ch != null) {
                if (alphabet.matches(ch.charValue()) != null) {
                    alphabet = null;
                    Preconditions.checkArgument(alphabet, "Padding character %s was already in alphabet", ch);
                    this.paddingChar = ch;
                }
            }
            alphabet = true;
            Preconditions.checkArgument(alphabet, "Padding character %s was already in alphabet", ch);
            this.paddingChar = ch;
        }

        CharMatcher padding() {
            return this.paddingChar == null ? CharMatcher.NONE : CharMatcher.is(this.paddingChar.charValue());
        }

        int maxEncodedSize(int i) {
            return this.alphabet.charsPerChunk * IntMath.divide(i, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        ByteOutput encodingStream(final CharOutput charOutput) {
            Preconditions.checkNotNull(charOutput);
            return new ByteOutput() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;

                public void write(byte b) throws IOException {
                    this.bitBuffer <<= 8;
                    this.bitBuffer = (b & 255) | this.bitBuffer;
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                        charOutput.write(StandardBaseEncoding.this.alphabet.encode((this.bitBuffer >> (this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar)) & StandardBaseEncoding.this.alphabet.mask));
                        this.writtenChars++;
                        this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar;
                    }
                }

                public void flush() throws IOException {
                    charOutput.flush();
                }

                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        charOutput.write(StandardBaseEncoding.this.alphabet.encode((this.bitBuffer << (StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength)) & StandardBaseEncoding.this.alphabet.mask));
                        this.writtenChars++;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                                charOutput.write(StandardBaseEncoding.this.paddingChar.charValue());
                                this.writtenChars++;
                            }
                        }
                    }
                    charOutput.close();
                }
            };
        }

        int maxDecodedSize(int i) {
            return (int) (((((long) this.alphabet.bitsPerChar) * ((long) i)) + 7) / 8);
        }

        ByteInput decodingStream(final CharInput charInput) {
            Preconditions.checkNotNull(charInput);
            return new ByteInput() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                boolean hitPadding = false;
                final CharMatcher paddingMatcher = StandardBaseEncoding.this.padding();
                int readChars = 0;

                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public int read() throws java.io.IOException {
                    /*
                    r4 = this;
                L_0x0000:
                    r0 = r2;
                    r0 = r0.read();
                    r1 = -1;
                    if (r0 != r1) goto L_0x0035;
                L_0x0009:
                    r0 = r4.hitPadding;
                    if (r0 != 0) goto L_0x0034;
                L_0x000d:
                    r0 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r0 = r0.alphabet;
                    r2 = r4.readChars;
                    r0 = r0.isValidPaddingStartPosition(r2);
                    if (r0 != 0) goto L_0x0034;
                L_0x001b:
                    r0 = new com.google.common.io.BaseEncoding$DecodingException;
                    r1 = new java.lang.StringBuilder;
                    r1.<init>();
                    r2 = "Invalid input length ";
                    r1.append(r2);
                    r2 = r4.readChars;
                    r1.append(r2);
                    r1 = r1.toString();
                    r0.<init>(r1);
                    throw r0;
                L_0x0034:
                    return r1;
                L_0x0035:
                    r1 = r4.readChars;
                    r2 = 1;
                    r1 = r1 + r2;
                    r4.readChars = r1;
                    r0 = (char) r0;
                    r1 = r4.paddingMatcher;
                    r1 = r1.matches(r0);
                    if (r1 == 0) goto L_0x0077;
                L_0x0044:
                    r0 = r4.hitPadding;
                    if (r0 != 0) goto L_0x0074;
                L_0x0048:
                    r0 = r4.readChars;
                    if (r0 == r2) goto L_0x005b;
                L_0x004c:
                    r0 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r0 = r0.alphabet;
                    r1 = r4.readChars;
                    r1 = r1 - r2;
                    r0 = r0.isValidPaddingStartPosition(r1);
                    if (r0 != 0) goto L_0x0074;
                L_0x005b:
                    r0 = new com.google.common.io.BaseEncoding$DecodingException;
                    r1 = new java.lang.StringBuilder;
                    r1.<init>();
                    r2 = "Padding cannot start at index ";
                    r1.append(r2);
                    r2 = r4.readChars;
                    r1.append(r2);
                    r1 = r1.toString();
                    r0.<init>(r1);
                    throw r0;
                L_0x0074:
                    r4.hitPadding = r2;
                    goto L_0x0000;
                L_0x0077:
                    r1 = r4.hitPadding;
                    if (r1 == 0) goto L_0x009c;
                L_0x007b:
                    r1 = new com.google.common.io.BaseEncoding$DecodingException;
                    r2 = new java.lang.StringBuilder;
                    r2.<init>();
                    r3 = "Expected padding character but found '";
                    r2.append(r3);
                    r2.append(r0);
                    r0 = "' at index ";
                    r2.append(r0);
                    r0 = r4.readChars;
                    r2.append(r0);
                    r0 = r2.toString();
                    r1.<init>(r0);
                    throw r1;
                L_0x009c:
                    r1 = r4.bitBuffer;
                    r2 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r2 = r2.alphabet;
                    r2 = r2.bitsPerChar;
                    r1 = r1 << r2;
                    r4.bitBuffer = r1;
                    r1 = r4.bitBuffer;
                    r2 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r2 = r2.alphabet;
                    r0 = r2.decode(r0);
                    r0 = r0 | r1;
                    r4.bitBuffer = r0;
                    r0 = r4.bitBufferLength;
                    r1 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r1 = r1.alphabet;
                    r1 = r1.bitsPerChar;
                    r0 = r0 + r1;
                    r4.bitBufferLength = r0;
                    r0 = r4.bitBufferLength;
                    r1 = 8;
                    if (r0 < r1) goto L_0x0000;
                L_0x00cb:
                    r0 = r4.bitBufferLength;
                    r0 = r0 - r1;
                    r4.bitBufferLength = r0;
                    r0 = r4.bitBuffer;
                    r1 = r4.bitBufferLength;
                    r0 = r0 >> r1;
                    r0 = r0 & 255;
                    return r0;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.BaseEncoding.StandardBaseEncoding.2.read():int");
                }

                public void close() throws IOException {
                    charInput.close();
                }
            };
        }

        public BaseEncoding omitPadding() {
            return this.paddingChar == null ? this : new StandardBaseEncoding(this.alphabet, null);
        }

        public BaseEncoding withPadChar(char c) {
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null || this.paddingChar.charValue() != c) {
                    return new StandardBaseEncoding(this.alphabet, Character.valueOf(c));
                }
            }
            return this;
        }

        public BaseEncoding withSeparator(String str, int i) {
            Preconditions.checkNotNull(str);
            Preconditions.checkArgument(padding().or(this.alphabet).matchesNoneOf(str), "Separator cannot contain alphabet or padding characters");
            return new SeparatedBaseEncoding(this, str, i);
        }

        public BaseEncoding upperCase() {
            BaseEncoding baseEncoding = this.upperCase;
            if (baseEncoding == null) {
                Alphabet upperCase = this.alphabet.upperCase();
                baseEncoding = upperCase == this.alphabet ? this : new StandardBaseEncoding(upperCase, this.paddingChar);
                this.upperCase = baseEncoding;
            }
            return baseEncoding;
        }

        public BaseEncoding lowerCase() {
            BaseEncoding baseEncoding = this.lowerCase;
            if (baseEncoding == null) {
                Alphabet lowerCase = this.alphabet.lowerCase();
                baseEncoding = lowerCase == this.alphabet ? this : new StandardBaseEncoding(lowerCase, this.paddingChar);
                this.lowerCase = baseEncoding;
            }
            return baseEncoding;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("BaseEncoding.");
            stringBuilder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    stringBuilder.append(".omitPadding()");
                } else {
                    stringBuilder.append(".withPadChar(");
                    stringBuilder.append(this.paddingChar);
                    stringBuilder.append(')');
                }
            }
            return stringBuilder.toString();
        }
    }

    abstract ByteInput decodingStream(CharInput charInput);

    abstract ByteOutput encodingStream(CharOutput charOutput);

    @CheckReturnValue
    public abstract BaseEncoding lowerCase();

    abstract int maxDecodedSize(int i);

    abstract int maxEncodedSize(int i);

    @CheckReturnValue
    public abstract BaseEncoding omitPadding();

    abstract CharMatcher padding();

    @CheckReturnValue
    public abstract BaseEncoding upperCase();

    @CheckReturnValue
    public abstract BaseEncoding withPadChar(char c);

    @CheckReturnValue
    public abstract BaseEncoding withSeparator(String str, int i);

    BaseEncoding() {
    }

    public String encode(byte[] bArr) {
        return encode((byte[]) Preconditions.checkNotNull(bArr), 0, bArr.length);
    }

    public final java.lang.String encode(byte[] r5, int r6, int r7) {
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
        r4 = this;
        com.google.common.base.Preconditions.checkNotNull(r5);
        r0 = r6 + r7;
        r1 = r5.length;
        com.google.common.base.Preconditions.checkPositionIndexes(r6, r0, r1);
        r0 = r4.maxEncodedSize(r7);
        r0 = com.google.common.io.GwtWorkarounds.stringBuilderOutput(r0);
        r1 = r4.encodingStream(r0);
        r2 = 0;
    L_0x0016:
        if (r2 >= r7) goto L_0x0022;
    L_0x0018:
        r3 = r6 + r2;
        r3 = r5[r3];	 Catch:{ IOException -> 0x002a }
        r1.write(r3);	 Catch:{ IOException -> 0x002a }
        r2 = r2 + 1;	 Catch:{ IOException -> 0x002a }
        goto L_0x0016;	 Catch:{ IOException -> 0x002a }
    L_0x0022:
        r1.close();	 Catch:{ IOException -> 0x002a }
        r5 = r0.toString();
        return r5;
    L_0x002a:
        r5 = new java.lang.AssertionError;
        r6 = "impossible";
        r5.<init>(r6);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.BaseEncoding.encode(byte[], int, int):java.lang.String");
    }

    @GwtIncompatible("Writer,OutputStream")
    public final OutputStream encodingStream(Writer writer) {
        return GwtWorkarounds.asOutputStream(encodingStream(GwtWorkarounds.asCharOutput(writer)));
    }

    @GwtIncompatible("ByteSink,CharSink")
    public final ByteSink encodingSink(final CharSink charSink) {
        Preconditions.checkNotNull(charSink);
        return new ByteSink() {
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(charSink.openStream());
            }
        };
    }

    private static byte[] extract(byte[] bArr, int i) {
        if (i == bArr.length) {
            return bArr;
        }
        Object obj = new byte[i];
        System.arraycopy(bArr, 0, obj, 0, i);
        return obj;
    }

    public final byte[] decode(CharSequence charSequence) {
        try {
            return decodeChecked(charSequence);
        } catch (CharSequence charSequence2) {
            throw new IllegalArgumentException(charSequence2);
        }
    }

    final byte[] decodeChecked(CharSequence charSequence) throws DecodingException {
        charSequence = padding().trimTrailingFrom(charSequence);
        ByteInput decodingStream = decodingStream(GwtWorkarounds.asCharInput(charSequence));
        charSequence = new byte[maxDecodedSize(charSequence.length())];
        try {
            int read = decodingStream.read();
            int i = 0;
            while (read != -1) {
                int i2 = i + 1;
                charSequence[i] = (byte) read;
                read = decodingStream.read();
                i = i2;
            }
            return extract(charSequence, i);
        } catch (CharSequence charSequence2) {
            throw charSequence2;
        } catch (CharSequence charSequence22) {
            throw new AssertionError(charSequence22);
        }
    }

    @GwtIncompatible("Reader,InputStream")
    public final InputStream decodingStream(Reader reader) {
        return GwtWorkarounds.asInputStream(decodingStream(GwtWorkarounds.asCharInput(reader)));
    }

    @GwtIncompatible("ByteSource,CharSource")
    public final ByteSource decodingSource(final CharSource charSource) {
        Preconditions.checkNotNull(charSource);
        return new ByteSource() {
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(charSource.openStream());
            }
        };
    }

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    static CharInput ignoringInput(final CharInput charInput, final CharMatcher charMatcher) {
        Preconditions.checkNotNull(charInput);
        Preconditions.checkNotNull(charMatcher);
        return new CharInput() {
            public int read() throws IOException {
                int read;
                do {
                    read = charInput.read();
                    if (read == -1) {
                        break;
                    }
                } while (charMatcher.matches((char) read));
                return read;
            }

            public void close() throws IOException {
                charInput.close();
            }
        };
    }

    static CharOutput separatingOutput(final CharOutput charOutput, final String str, final int i) {
        Preconditions.checkNotNull(charOutput);
        Preconditions.checkNotNull(str);
        Preconditions.checkArgument(i > 0);
        return new CharOutput() {
            int charsUntilSeparator = i;

            public void write(char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    for (int i = 0; i < str.length(); i++) {
                        charOutput.write(str.charAt(i));
                    }
                    this.charsUntilSeparator = i;
                }
                charOutput.write(c);
                this.charsUntilSeparator--;
            }

            public void flush() throws IOException {
                charOutput.flush();
            }

            public void close() throws IOException {
                charOutput.close();
            }
        };
    }
}
