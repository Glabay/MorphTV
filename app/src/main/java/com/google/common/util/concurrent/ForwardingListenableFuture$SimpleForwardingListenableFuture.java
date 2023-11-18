package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;

public abstract class ForwardingListenableFuture$SimpleForwardingListenableFuture<V> extends ForwardingListenableFuture<V> {
    private final ListenableFuture<V> delegate;

    protected ForwardingListenableFuture$SimpleForwardingListenableFuture(ListenableFuture<V> listenableFuture) {
        this.delegate = (ListenableFuture) Preconditions.checkNotNull(listenableFuture);
    }

    protected final ListenableFuture<V> delegate() {
        return this.delegate;
    }
}
