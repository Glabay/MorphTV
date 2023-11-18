package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public final class Functions {

    private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
        private static final long serialVersionUID = 0;
        private final E value;

        public ConstantFunction(@Nullable E e) {
            this.value = e;
        }

        public E apply(@Nullable Object obj) {
            return this.value;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ConstantFunction)) {
                return null;
            }
            return Objects.equal(this.value, ((ConstantFunction) obj).value);
        }

        public int hashCode() {
            return this.value == null ? 0 : this.value.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("constant(");
            stringBuilder.append(this.value);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final V defaultValue;
        final Map<K, ? extends V> map;

        ForMapWithDefault(Map<K, ? extends V> map, @Nullable V v) {
            this.map = (Map) Preconditions.checkNotNull(map);
            this.defaultValue = v;
        }

        public V apply(@Nullable K k) {
            V v = this.map.get(k);
            if (v == null) {
                return this.map.containsKey(k) != null ? v : this.defaultValue;
            } else {
                return v;
            }
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof ForMapWithDefault)) {
                return false;
            }
            ForMapWithDefault forMapWithDefault = (ForMapWithDefault) obj;
            if (this.map.equals(forMapWithDefault.map) && Objects.equal(this.defaultValue, forMapWithDefault.defaultValue) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hashCode(this.map, this.defaultValue);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("forMap(");
            stringBuilder.append(this.map);
            stringBuilder.append(", defaultValue=");
            stringBuilder.append(this.defaultValue);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
        private static final long serialVersionUID = 0;
        /* renamed from: f */
        private final Function<A, ? extends B> f42f;
        /* renamed from: g */
        private final Function<B, C> f43g;

        public FunctionComposition(Function<B, C> function, Function<A, ? extends B> function2) {
            this.f43g = (Function) Preconditions.checkNotNull(function);
            this.f42f = (Function) Preconditions.checkNotNull(function2);
        }

        public C apply(@Nullable A a) {
            return this.f43g.apply(this.f42f.apply(a));
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof FunctionComposition)) {
                return false;
            }
            FunctionComposition functionComposition = (FunctionComposition) obj;
            if (this.f42f.equals(functionComposition.f42f) && this.f43g.equals(functionComposition.f43g) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.f42f.hashCode() ^ this.f43g.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.f43g);
            stringBuilder.append("(");
            stringBuilder.append(this.f42f);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final Map<K, V> map;

        FunctionForMapNoDefault(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        public V apply(@Nullable K k) {
            boolean z;
            V v = this.map.get(k);
            if (v == null) {
                if (!this.map.containsKey(k)) {
                    z = false;
                    Preconditions.checkArgument(z, "Key '%s' not present in map", k);
                    return v;
                }
            }
            z = true;
            Preconditions.checkArgument(z, "Key '%s' not present in map", k);
            return v;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof FunctionForMapNoDefault)) {
                return null;
            }
            return this.map.equals(((FunctionForMapNoDefault) obj).map);
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("forMap(");
            stringBuilder.append(this.map);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private enum IdentityFunction implements Function<Object, Object> {
        INSTANCE;

        @Nullable
        public Object apply(@Nullable Object obj) {
            return obj;
        }

        public String toString() {
            return "identity";
        }
    }

    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
        private static final long serialVersionUID = 0;
        private final Predicate<T> predicate;

        private PredicateFunction(Predicate<T> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        public Boolean apply(@Nullable T t) {
            return Boolean.valueOf(this.predicate.apply(t));
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof PredicateFunction)) {
                return null;
            }
            return this.predicate.equals(((PredicateFunction) obj).predicate);
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("forPredicate(");
            stringBuilder.append(this.predicate);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static class SupplierFunction<T> implements Function<Object, T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> supplier) {
            this.supplier = (Supplier) Preconditions.checkNotNull(supplier);
        }

        public T apply(@Nullable Object obj) {
            return this.supplier.get();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof SupplierFunction)) {
                return null;
            }
            return this.supplier.equals(((SupplierFunction) obj).supplier);
        }

        public int hashCode() {
            return this.supplier.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("forSupplier(");
            stringBuilder.append(this.supplier);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private enum ToStringFunction implements Function<Object, String> {
        INSTANCE;

        public String toString() {
            return "toString";
        }

        public String apply(Object obj) {
            Preconditions.checkNotNull(obj);
            return obj.toString();
        }
    }

    private Functions() {
    }

    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }

    public static <E> Function<E, E> identity() {
        return IdentityFunction.INSTANCE;
    }

    public static <K, V> Function<K, V> forMap(Map<K, V> map) {
        return new FunctionForMapNoDefault(map);
    }

    public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V v) {
        return new ForMapWithDefault(map, v);
    }

    public static <A, B, C> Function<A, C> compose(Function<B, C> function, Function<A, ? extends B> function2) {
        return new FunctionComposition(function, function2);
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
        return new PredicateFunction(predicate);
    }

    public static <E> Function<Object, E> constant(@Nullable E e) {
        return new ConstantFunction(e);
    }

    @Beta
    public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
        return new SupplierFunction(supplier);
    }
}
