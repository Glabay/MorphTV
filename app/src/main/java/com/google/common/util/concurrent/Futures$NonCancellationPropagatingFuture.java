package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;

class Futures$NonCancellationPropagatingFuture<V> extends AbstractFuture<V> {
    Futures$NonCancellationPropagatingFuture(final ListenableFuture<V> listenableFuture) {
        Preconditions.checkNotNull(listenableFuture);
        Futures.addCallback(listenableFuture, new FutureCallback<V>() {
            public void onSuccess(V v) {
                Futures$NonCancellationPropagatingFuture.this.set(v);
            }

            public void onFailure(Throwable th) {
                if (listenableFuture.isCancelled()) {
                    Futures$NonCancellationPropagatingFuture.this.cancel(false);
                } else {
                    Futures$NonCancellationPropagatingFuture.this.setException(th);
                }
            }
        }, MoreExecutors.directExecutor());
    }
}
