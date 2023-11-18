package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class SingletonImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    transient ImmutableBiMap<V, K> inverse;
    final transient K singleKey;
    final transient V singleValue;

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return 1;
    }

    SingletonImmutableBiMap(K k, V v) {
        CollectPreconditions.checkEntryNotNull(k, v);
        this.singleKey = k;
        this.singleValue = v;
    }

    private SingletonImmutableBiMap(K k, V v, ImmutableBiMap<V, K> immutableBiMap) {
        this.singleKey = k;
        this.singleValue = v;
        this.inverse = immutableBiMap;
    }

    public V get(@Nullable Object obj) {
        return this.singleKey.equals(obj) != null ? this.singleValue : null;
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.singleKey.equals(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        return this.singleValue.equals(obj);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }

    ImmutableSet<K> createKeySet() {
        return ImmutableSet.of(this.singleKey);
    }

    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap<V, K> immutableBiMap = this.inverse;
        if (immutableBiMap != null) {
            return immutableBiMap;
        }
        immutableBiMap = new SingletonImmutableBiMap(this.singleValue, this.singleKey, this);
        this.inverse = immutableBiMap;
        return immutableBiMap;
    }
}
