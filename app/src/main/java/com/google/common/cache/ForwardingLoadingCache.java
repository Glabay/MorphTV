package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;

@Beta
public abstract class ForwardingLoadingCache<K, V> extends ForwardingCache<K, V> implements LoadingCache<K, V> {

    @Beta
    public static abstract class SimpleForwardingLoadingCache<K, V> extends ForwardingLoadingCache<K, V> {
        private final LoadingCache<K, V> delegate;

        protected SimpleForwardingLoadingCache(LoadingCache<K, V> loadingCache) {
            this.delegate = (LoadingCache) Preconditions.checkNotNull(loadingCache);
        }

        protected final LoadingCache<K, V> delegate() {
            return this.delegate;
        }
    }

    protected abstract LoadingCache<K, V> delegate();

    protected ForwardingLoadingCache() {
    }

    public V get(K k) throws ExecutionException {
        return delegate().get(k);
    }

    public V getUnchecked(K k) {
        return delegate().getUnchecked(k);
    }

    public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
        return delegate().getAll(iterable);
    }

    public V apply(K k) {
        return delegate().apply(k);
    }

    public void refresh(K k) {
        delegate().refresh(k);
    }
}
