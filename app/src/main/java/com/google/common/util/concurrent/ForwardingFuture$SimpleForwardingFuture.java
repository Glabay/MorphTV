package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Future;

public abstract class ForwardingFuture$SimpleForwardingFuture<V> extends ForwardingFuture<V> {
    private final Future<V> delegate;

    protected ForwardingFuture$SimpleForwardingFuture(Future<V> future) {
        this.delegate = (Future) Preconditions.checkNotNull(future);
    }

    protected final Future<V> delegate() {
        return this.delegate;
    }
}
