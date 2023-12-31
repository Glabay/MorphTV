package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

@Beta
public final class Funnels {

    private enum ByteArrayFunnel implements Funnel<byte[]> {
        INSTANCE;

        public String toString() {
            return "Funnels.byteArrayFunnel()";
        }

        public void funnel(byte[] bArr, PrimitiveSink primitiveSink) {
            primitiveSink.putBytes(bArr);
        }
    }

    private enum IntegerFunnel implements Funnel<Integer> {
        INSTANCE;

        public String toString() {
            return "Funnels.integerFunnel()";
        }

        public void funnel(Integer num, PrimitiveSink primitiveSink) {
            primitiveSink.putInt(num.intValue());
        }
    }

    private enum LongFunnel implements Funnel<Long> {
        INSTANCE;

        public String toString() {
            return "Funnels.longFunnel()";
        }

        public void funnel(Long l, PrimitiveSink primitiveSink) {
            primitiveSink.putLong(l.longValue());
        }
    }

    private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
        private final Funnel<E> elementFunnel;

        SequentialFunnel(Funnel<E> funnel) {
            this.elementFunnel = (Funnel) Preconditions.checkNotNull(funnel);
        }

        public void funnel(Iterable<? extends E> iterable, PrimitiveSink primitiveSink) {
            for (Object funnel : iterable) {
                this.elementFunnel.funnel(funnel, primitiveSink);
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Funnels.sequentialFunnel(");
            stringBuilder.append(this.elementFunnel);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof SequentialFunnel)) {
                return null;
            }
            return this.elementFunnel.equals(((SequentialFunnel) obj).elementFunnel);
        }

        public int hashCode() {
            return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
        }
    }

    private static class SinkAsStream extends OutputStream {
        final PrimitiveSink sink;

        SinkAsStream(PrimitiveSink primitiveSink) {
            this.sink = (PrimitiveSink) Preconditions.checkNotNull(primitiveSink);
        }

        public void write(int i) {
            this.sink.putByte((byte) i);
        }

        public void write(byte[] bArr) {
            this.sink.putBytes(bArr);
        }

        public void write(byte[] bArr, int i, int i2) {
            this.sink.putBytes(bArr, i, i2);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Funnels.asOutputStream(");
            stringBuilder.append(this.sink);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
        private final Charset charset;

        private static class SerializedForm implements Serializable {
            private static final long serialVersionUID = 0;
            private final String charsetCanonicalName;

            SerializedForm(Charset charset) {
                this.charsetCanonicalName = charset.name();
            }

            private Object readResolve() {
                return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
            }
        }

        StringCharsetFunnel(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        public void funnel(CharSequence charSequence, PrimitiveSink primitiveSink) {
            primitiveSink.putString(charSequence, this.charset);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Funnels.stringFunnel(");
            stringBuilder.append(this.charset.name());
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof StringCharsetFunnel)) {
                return null;
            }
            return this.charset.equals(((StringCharsetFunnel) obj).charset);
        }

        public int hashCode() {
            return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
        }

        Object writeReplace() {
            return new SerializedForm(this.charset);
        }
    }

    private enum UnencodedCharsFunnel implements Funnel<CharSequence> {
        INSTANCE;

        public String toString() {
            return "Funnels.unencodedCharsFunnel()";
        }

        public void funnel(CharSequence charSequence, PrimitiveSink primitiveSink) {
            primitiveSink.putUnencodedChars(charSequence);
        }
    }

    private Funnels() {
    }

    public static Funnel<byte[]> byteArrayFunnel() {
        return ByteArrayFunnel.INSTANCE;
    }

    public static Funnel<CharSequence> unencodedCharsFunnel() {
        return UnencodedCharsFunnel.INSTANCE;
    }

    public static Funnel<CharSequence> stringFunnel(Charset charset) {
        return new StringCharsetFunnel(charset);
    }

    public static Funnel<Integer> integerFunnel() {
        return IntegerFunnel.INSTANCE;
    }

    public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> funnel) {
        return new SequentialFunnel(funnel);
    }

    public static Funnel<Long> longFunnel() {
        return LongFunnel.INSTANCE;
    }

    public static OutputStream asOutputStream(PrimitiveSink primitiveSink) {
        return new SinkAsStream(primitiveSink);
    }
}
