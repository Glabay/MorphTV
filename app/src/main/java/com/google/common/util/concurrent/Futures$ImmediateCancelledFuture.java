package com.google.common.util.concurrent;

import com.google.common.util.concurrent.Futures.ImmediateFuture;
import java.util.concurrent.CancellationException;

class Futures$ImmediateCancelledFuture<V> extends ImmediateFuture<V> {
    private final CancellationException thrown = new CancellationException("Immediate cancelled future.");

    public boolean isCancelled() {
        return true;
    }

    Futures$ImmediateCancelledFuture() {
        super(null);
    }

    public V get() {
        throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
    }
}
