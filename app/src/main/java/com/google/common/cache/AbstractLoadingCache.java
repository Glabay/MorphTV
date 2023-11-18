package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Beta
public abstract class AbstractLoadingCache<K, V> extends AbstractCache<K, V> implements LoadingCache<K, V> {
    protected AbstractLoadingCache() {
    }

    public V getUnchecked(K k) {
        try {
            return get(k);
        } catch (K k2) {
            throw new UncheckedExecutionException(k2.getCause());
        }
    }

    public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
        Map newLinkedHashMap = Maps.newLinkedHashMap();
        for (Object next : iterable) {
            if (!newLinkedHashMap.containsKey(next)) {
                newLinkedHashMap.put(next, get(next));
            }
        }
        return ImmutableMap.copyOf(newLinkedHashMap);
    }

    public final V apply(K k) {
        return getUnchecked(k);
    }

    public void refresh(K k) {
        throw new UnsupportedOperationException();
    }
}
