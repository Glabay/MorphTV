package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractSetMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements SetMultimap<K, V> {
    private static final long serialVersionUID = 7431625294878419160L;

    abstract Set<V> createCollection();

    protected AbstractSetMultimap(Map<K, Collection<V>> map) {
        super(map);
    }

    Set<V> createUnmodifiableEmptyCollection() {
        return ImmutableSet.of();
    }

    public Set<V> get(@Nullable K k) {
        return (Set) super.get(k);
    }

    public Set<Entry<K, V>> entries() {
        return (Set) super.entries();
    }

    public Set<V> removeAll(@Nullable Object obj) {
        return (Set) super.removeAll(obj);
    }

    public Set<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        return (Set) super.replaceValues(k, iterable);
    }

    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }

    public boolean put(@Nullable K k, @Nullable V v) {
        return super.put(k, v);
    }

    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
