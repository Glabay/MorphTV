package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

final class AsyncSettableFuture<V> extends ForwardingListenableFuture<V> {
    private final ListenableFuture<V> dereferenced = Futures.dereference(this.nested);
    private final AsyncSettableFuture$NestedFuture<V> nested = new AsyncSettableFuture$NestedFuture(null);

    public static <V> AsyncSettableFuture<V> create() {
        return new AsyncSettableFuture();
    }

    private AsyncSettableFuture() {
    }

    protected ListenableFuture<V> delegate() {
        return this.dereferenced;
    }

    public boolean setFuture(ListenableFuture<? extends V> listenableFuture) {
        return this.nested.setFuture((ListenableFuture) Preconditions.checkNotNull(listenableFuture));
    }

    public boolean setValue(@Nullable V v) {
        return setFuture(Futures.immediateFuture(v));
    }

    public boolean setException(Throwable th) {
        return setFuture(Futures.immediateFailedFuture(th));
    }

    public boolean isSet() {
        return this.nested.isDone();
    }
}
