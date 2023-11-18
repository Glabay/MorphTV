package com.google.common.util.concurrent;

import java.util.concurrent.Executor;

class Futures$FallbackFuture<V> extends AbstractFuture<V> {
    private volatile ListenableFuture<? extends V> running;

    Futures$FallbackFuture(ListenableFuture<? extends V> listenableFuture, final FutureFallback<? extends V> futureFallback, Executor executor) {
        this.running = listenableFuture;
        Futures.addCallback(this.running, new FutureCallback<V>() {

            /* renamed from: com.google.common.util.concurrent.Futures$FallbackFuture$1$1 */
            class C12271 implements FutureCallback<V> {
                C12271() {
                }

                public void onSuccess(V v) {
                    Futures$FallbackFuture.this.set(v);
                }

                public void onFailure(Throwable th) {
                    if (Futures$FallbackFuture.this.running.isCancelled()) {
                        Futures$FallbackFuture.this.cancel(false);
                    } else {
                        Futures$FallbackFuture.this.setException(th);
                    }
                }
            }

            public void onSuccess(V v) {
                Futures$FallbackFuture.this.set(v);
            }

            public void onFailure(Throwable th) {
                if (!Futures$FallbackFuture.this.isCancelled()) {
                    try {
                        Futures$FallbackFuture.this.running = futureFallback.create(th);
                        if (Futures$FallbackFuture.this.isCancelled() != null) {
                            Futures$FallbackFuture.this.running.cancel(Futures$FallbackFuture.this.wasInterrupted());
                        } else {
                            Futures.addCallback(Futures$FallbackFuture.this.running, new C12271(), MoreExecutors.directExecutor());
                        }
                    } catch (Throwable th2) {
                        Futures$FallbackFuture.this.setException(th2);
                    }
                }
            }
        }, executor);
    }

    public boolean cancel(boolean z) {
        if (!super.cancel(z)) {
            return false;
        }
        this.running.cancel(z);
        return true;
    }
}
