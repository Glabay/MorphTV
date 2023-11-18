package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract class ForwardingCheckedFuture<V, X extends Exception> extends ForwardingListenableFuture<V> implements CheckedFuture<V, X> {

    @Beta
    public static abstract class SimpleForwardingCheckedFuture<V, X extends Exception> extends ForwardingCheckedFuture<V, X> {
        private final CheckedFuture<V, X> delegate;

        protected SimpleForwardingCheckedFuture(CheckedFuture<V, X> checkedFuture) {
            this.delegate = (CheckedFuture) Preconditions.checkNotNull(checkedFuture);
        }

        protected final CheckedFuture<V, X> delegate() {
            return this.delegate;
        }
    }

    protected abstract CheckedFuture<V, X> delegate();

    public V checkedGet() throws Exception {
        return delegate().checkedGet();
    }

    public V checkedGet(long j, TimeUnit timeUnit) throws TimeoutException, Exception {
        return delegate().checkedGet(j, timeUnit);
    }
}
