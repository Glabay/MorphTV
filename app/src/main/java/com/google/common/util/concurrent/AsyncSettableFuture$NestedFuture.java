package com.google.common.util.concurrent;

final class AsyncSettableFuture$NestedFuture<V> extends AbstractFuture<ListenableFuture<? extends V>> {
    private AsyncSettableFuture$NestedFuture() {
    }

    boolean setFuture(ListenableFuture<? extends V> listenableFuture) {
        boolean z = set(listenableFuture);
        if (isCancelled()) {
            listenableFuture.cancel(wasInterrupted());
        }
        return z;
    }
}
