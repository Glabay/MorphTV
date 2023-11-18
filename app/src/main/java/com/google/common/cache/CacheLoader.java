package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

@GwtCompatible(emulated = true)
public abstract class CacheLoader<K, V> {

    private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Function<K, V> computingFunction;

        public FunctionToCacheLoader(Function<K, V> function) {
            this.computingFunction = (Function) Preconditions.checkNotNull(function);
        }

        public V load(K k) {
            return this.computingFunction.apply(Preconditions.checkNotNull(k));
        }
    }

    public static final class InvalidCacheLoadException extends RuntimeException {
        public InvalidCacheLoadException(String str) {
            super(str);
        }
    }

    private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<V> computingSupplier;

        public SupplierToCacheLoader(Supplier<V> supplier) {
            this.computingSupplier = (Supplier) Preconditions.checkNotNull(supplier);
        }

        public V load(Object obj) {
            Preconditions.checkNotNull(obj);
            return this.computingSupplier.get();
        }
    }

    static final class UnsupportedLoadingOperationException extends UnsupportedOperationException {
        UnsupportedLoadingOperationException() {
        }
    }

    public abstract V load(K k) throws Exception;

    protected CacheLoader() {
    }

    @GwtIncompatible("Futures")
    public ListenableFuture<V> reload(K k, V v) throws Exception {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(v);
        return Futures.immediateFuture(load(k));
    }

    public Map<K, V> loadAll(Iterable<? extends K> iterable) throws Exception {
        throw new UnsupportedLoadingOperationException();
    }

    @Beta
    public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
        return new FunctionToCacheLoader(function);
    }

    @Beta
    public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) {
        return new SupplierToCacheLoader(supplier);
    }

    @GwtIncompatible("Executor + Futures")
    @Beta
    public static <K, V> CacheLoader<K, V> asyncReloading(final CacheLoader<K, V> cacheLoader, final Executor executor) {
        Preconditions.checkNotNull(cacheLoader);
        Preconditions.checkNotNull(executor);
        return new CacheLoader<K, V>() {
            public V load(K k) throws Exception {
                return cacheLoader.load(k);
            }

            public ListenableFuture<V> reload(final K k, final V v) throws Exception {
                k = ListenableFutureTask.create(new Callable<V>() {
                    public V call() throws Exception {
                        return cacheLoader.reload(k, v).get();
                    }
                });
                executor.execute(k);
                return k;
            }

            public Map<K, V> loadAll(Iterable<? extends K> iterable) throws Exception {
                return cacheLoader.loadAll(iterable);
            }
        };
    }
}
