package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingListMultimap<K, V> extends ForwardingMultimap<K, V> implements ListMultimap<K, V> {
    protected abstract ListMultimap<K, V> delegate();

    protected ForwardingListMultimap() {
    }

    public List<V> get(@Nullable K k) {
        return delegate().get(k);
    }

    public List<V> removeAll(@Nullable Object obj) {
        return delegate().removeAll(obj);
    }

    public List<V> replaceValues(K k, Iterable<? extends V> iterable) {
        return delegate().replaceValues(k, iterable);
    }
}
