package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures.ImmediateFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class Futures$ImmediateFailedCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
    private final X thrown;

    Futures$ImmediateFailedCheckedFuture(X x) {
        super(null);
        this.thrown = x;
    }

    public V get() throws ExecutionException {
        throw new ExecutionException(this.thrown);
    }

    public V checkedGet() throws Exception {
        throw this.thrown;
    }

    public V checkedGet(long j, TimeUnit timeUnit) throws Exception {
        Preconditions.checkNotNull(timeUnit);
        throw this.thrown;
    }
}
