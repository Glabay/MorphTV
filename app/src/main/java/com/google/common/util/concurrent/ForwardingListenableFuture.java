package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

public abstract class ForwardingListenableFuture<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
    protected abstract ListenableFuture<V> delegate();

    protected ForwardingListenableFuture() {
    }

    public void addListener(Runnable runnable, Executor executor) {
        delegate().addListener(runnable, executor);
    }
}
