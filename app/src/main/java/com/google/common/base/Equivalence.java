package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Equivalence<T> {

    static final class Equals extends Equivalence<Object> implements Serializable {
        static final Equals INSTANCE = new Equals();
        private static final long serialVersionUID = 1;

        Equals() {
        }

        protected boolean doEquivalent(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        protected int doHash(Object obj) {
            return obj.hashCode();
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Equivalence<T> equivalence;
        @Nullable
        private final T target;

        EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T t) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
            this.target = t;
        }

        public boolean apply(@Nullable T t) {
            return this.equivalence.equivalent(t, this.target);
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EquivalentToPredicate)) {
                return false;
            }
            EquivalentToPredicate equivalentToPredicate = (EquivalentToPredicate) obj;
            if (!this.equivalence.equals(equivalentToPredicate.equivalence) || Objects.equal(this.target, equivalentToPredicate.target) == null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hashCode(this.equivalence, this.target);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.equivalence);
            stringBuilder.append(".equivalentTo(");
            stringBuilder.append(this.target);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static final class Identity extends Equivalence<Object> implements Serializable {
        static final Identity INSTANCE = new Identity();
        private static final long serialVersionUID = 1;

        protected boolean doEquivalent(Object obj, Object obj2) {
            return false;
        }

        Identity() {
        }

        protected int doHash(Object obj) {
            return System.identityHashCode(obj);
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    public static final class Wrapper<T> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Equivalence<? super T> equivalence;
        @Nullable
        private final T reference;

        private Wrapper(Equivalence<? super T> equivalence, @Nullable T t) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
            this.reference = t;
        }

        @Nullable
        public T get() {
            return this.reference;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Wrapper) {
                Wrapper wrapper = (Wrapper) obj;
                if (this.equivalence.equals(wrapper.equivalence)) {
                    return this.equivalence.equivalent(this.reference, wrapper.reference);
                }
            }
            return null;
        }

        public int hashCode() {
            return this.equivalence.hash(this.reference);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.equivalence);
            stringBuilder.append(".wrap(");
            stringBuilder.append(this.reference);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    protected abstract boolean doEquivalent(T t, T t2);

    protected abstract int doHash(T t);

    protected Equivalence() {
    }

    public final boolean equivalent(@Nullable T t, @Nullable T t2) {
        if (t == t2) {
            return true;
        }
        if (t != null) {
            if (t2 != null) {
                return doEquivalent(t, t2);
            }
        }
        return null;
    }

    public final int hash(@Nullable T t) {
        return t == null ? null : doHash(t);
    }

    public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
        return new FunctionalEquivalence(function, this);
    }

    public final <S extends T> Wrapper<S> wrap(@Nullable S s) {
        return new Wrapper(s);
    }

    @GwtCompatible(serializable = true)
    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return new PairwiseEquivalence(this);
    }

    @Beta
    public final Predicate<T> equivalentTo(@Nullable T t) {
        return new EquivalentToPredicate(this, t);
    }

    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }
}
