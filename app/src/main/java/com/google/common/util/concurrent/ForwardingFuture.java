package com.google.common.util.concurrent;

import com.google.common.collect.ForwardingObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class ForwardingFuture<V> extends ForwardingObject implements Future<V> {
    protected abstract Future<V> delegate();

    protected ForwardingFuture() {
    }

    public boolean cancel(boolean z) {
        return delegate().cancel(z);
    }

    public boolean isCancelled() {
        return delegate().isCancelled();
    }

    public boolean isDone() {
        return delegate().isDone();
    }

    public V get() throws InterruptedException, ExecutionException {
        return delegate().get();
    }

    public V get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate().get(j, timeUnit);
    }
}
