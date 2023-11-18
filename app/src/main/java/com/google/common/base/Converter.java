package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
public abstract class Converter<A, B> implements Function<A, B> {
    private final boolean handleNullAutomatically;
    private transient Converter<B, A> reverse;

    private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> first;
        final Converter<B, C> second;

        ConverterComposition(Converter<A, B> converter, Converter<B, C> converter2) {
            this.first = converter;
            this.second = converter2;
        }

        protected C doForward(A a) {
            throw new AssertionError();
        }

        protected A doBackward(C c) {
            throw new AssertionError();
        }

        @Nullable
        C correctedDoForward(@Nullable A a) {
            return this.second.correctedDoForward(this.first.correctedDoForward(a));
        }

        @Nullable
        A correctedDoBackward(@Nullable C c) {
            return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof ConverterComposition)) {
                return false;
            }
            ConverterComposition converterComposition = (ConverterComposition) obj;
            if (this.first.equals(converterComposition.first) && this.second.equals(converterComposition.second) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return (this.first.hashCode() * 31) + this.second.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.first);
            stringBuilder.append(".andThen(");
            stringBuilder.append(this.second);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable {
        private final Function<? super B, ? extends A> backwardFunction;
        private final Function<? super A, ? extends B> forwardFunction;

        private FunctionBasedConverter(Function<? super A, ? extends B> function, Function<? super B, ? extends A> function2) {
            this.forwardFunction = (Function) Preconditions.checkNotNull(function);
            this.backwardFunction = (Function) Preconditions.checkNotNull(function2);
        }

        protected B doForward(A a) {
            return this.forwardFunction.apply(a);
        }

        protected A doBackward(B b) {
            return this.backwardFunction.apply(b);
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof FunctionBasedConverter)) {
                return false;
            }
            FunctionBasedConverter functionBasedConverter = (FunctionBasedConverter) obj;
            if (this.forwardFunction.equals(functionBasedConverter.forwardFunction) && this.backwardFunction.equals(functionBasedConverter.backwardFunction) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return (this.forwardFunction.hashCode() * 31) + this.backwardFunction.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Converter.from(");
            stringBuilder.append(this.forwardFunction);
            stringBuilder.append(", ");
            stringBuilder.append(this.backwardFunction);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static final class IdentityConverter<T> extends Converter<T, T> implements Serializable {
        static final IdentityConverter INSTANCE = new IdentityConverter();
        private static final long serialVersionUID = 0;

        protected T doBackward(T t) {
            return t;
        }

        protected T doForward(T t) {
            return t;
        }

        public IdentityConverter<T> reverse() {
            return this;
        }

        public String toString() {
            return "Converter.identity()";
        }

        private IdentityConverter() {
        }

        <S> Converter<T, S> doAndThen(Converter<T, S> converter) {
            return (Converter) Preconditions.checkNotNull(converter, "otherConverter");
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> original;

        ReverseConverter(Converter<A, B> converter) {
            this.original = converter;
        }

        protected A doForward(B b) {
            throw new AssertionError();
        }

        protected B doBackward(A a) {
            throw new AssertionError();
        }

        @Nullable
        A correctedDoForward(@Nullable B b) {
            return this.original.correctedDoBackward(b);
        }

        @Nullable
        B correctedDoBackward(@Nullable A a) {
            return this.original.correctedDoForward(a);
        }

        public Converter<A, B> reverse() {
            return this.original;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ReverseConverter)) {
                return null;
            }
            return this.original.equals(((ReverseConverter) obj).original);
        }

        public int hashCode() {
            return this.original.hashCode() ^ -1;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.original);
            stringBuilder.append(".reverse()");
            return stringBuilder.toString();
        }
    }

    protected abstract A doBackward(B b);

    protected abstract B doForward(A a);

    protected Converter() {
        this(true);
    }

    Converter(boolean z) {
        this.handleNullAutomatically = z;
    }

    @Nullable
    public final B convert(@Nullable A a) {
        return correctedDoForward(a);
    }

    @Nullable
    B correctedDoForward(@Nullable A a) {
        if (!this.handleNullAutomatically) {
            return doForward(a);
        }
        if (a == null) {
            a = null;
        } else {
            a = Preconditions.checkNotNull(doForward(a));
        }
        return a;
    }

    @Nullable
    A correctedDoBackward(@Nullable B b) {
        if (!this.handleNullAutomatically) {
            return doBackward(b);
        }
        if (b == null) {
            b = null;
        } else {
            b = Preconditions.checkNotNull(doBackward(b));
        }
        return b;
    }

    public Iterable<B> convertAll(final Iterable<? extends A> iterable) {
        Preconditions.checkNotNull(iterable, "fromIterable");
        return new Iterable<B>() {

            /* renamed from: com.google.common.base.Converter$1$1 */
            class C08011 implements Iterator<B> {
                private final Iterator<? extends A> fromIterator = iterable.iterator();

                C08011() {
                }

                public boolean hasNext() {
                    return this.fromIterator.hasNext();
                }

                public B next() {
                    return Converter.this.convert(this.fromIterator.next());
                }

                public void remove() {
                    this.fromIterator.remove();
                }
            }

            public Iterator<B> iterator() {
                return new C08011();
            }
        };
    }

    public Converter<B, A> reverse() {
        Converter<B, A> converter = this.reverse;
        if (converter != null) {
            return converter;
        }
        converter = new ReverseConverter(this);
        this.reverse = converter;
        return converter;
    }

    public final <C> Converter<A, C> andThen(Converter<B, C> converter) {
        return doAndThen(converter);
    }

    <C> Converter<A, C> doAndThen(Converter<B, C> converter) {
        return new ConverterComposition(this, (Converter) Preconditions.checkNotNull(converter));
    }

    @Deprecated
    @Nullable
    public final B apply(@Nullable A a) {
        return convert(a);
    }

    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> function, Function<? super B, ? extends A> function2) {
        return new FunctionBasedConverter(function, function2);
    }

    public static <T> Converter<T, T> identity() {
        return IdentityConverter.INSTANCE;
    }
}
