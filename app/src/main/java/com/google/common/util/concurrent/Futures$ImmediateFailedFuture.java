package com.google.common.util.concurrent;

import com.google.common.util.concurrent.Futures.ImmediateFuture;
import java.util.concurrent.ExecutionException;

class Futures$ImmediateFailedFuture<V> extends ImmediateFuture<V> {
    private final Throwable thrown;

    Futures$ImmediateFailedFuture(Throwable th) {
        super(null);
        this.thrown = th;
    }

    public V get() throws ExecutionException {
        throw new ExecutionException(this.thrown);
    }
}
