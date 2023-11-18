package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Map.Entry;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements BiMap<K, V> {

    public static final class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        public Builder<K, V> put(K k, V v) {
            super.put(k, v);
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll((Map) map);
            return this;
        }

        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
            super.putAll((Iterable) iterable);
            return this;
        }

        public ImmutableBiMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableBiMap.of();
                case 1:
                    return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    return new RegularImmutableBiMap(this.size, this.entries);
            }
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;

        SerializedForm(ImmutableBiMap<?, ?> immutableBiMap) {
            super(immutableBiMap);
        }

        Object readResolve() {
            return createMap(new Builder());
        }
    }

    public abstract ImmutableBiMap<V, K> inverse();

    public static <K, V> ImmutableBiMap<K, V> of() {
        return EmptyImmutableBiMap.INSTANCE;
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v) {
        return new SingletonImmutableBiMap(k, v);
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2) {
        return new RegularImmutableBiMap(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return new RegularImmutableBiMap(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new RegularImmutableBiMap(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new RegularImmutableBiMap(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableBiMap) {
            ImmutableBiMap<K, V> immutableBiMap = (ImmutableBiMap) map;
            if (!immutableBiMap.isPartialView()) {
                return immutableBiMap;
            }
        }
        return copyOf(map.entrySet());
    }

    @Beta
    public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        Entry[] entryArr = (Entry[]) Iterables.toArray((Iterable) iterable, EMPTY_ENTRY_ARRAY);
        switch (entryArr.length) {
            case 0:
                return of();
            case 1:
                iterable = entryArr[0];
                return of(iterable.getKey(), iterable.getValue());
            default:
                return new RegularImmutableBiMap(entryArr);
        }
    }

    ImmutableBiMap() {
    }

    public ImmutableSet<V> values() {
        return inverse().keySet();
    }

    @Deprecated
    public V forcePut(K k, V v) {
        throw new UnsupportedOperationException();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
