package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract class AbstractCheckedFuture<V, X extends Exception> extends ForwardingListenableFuture$SimpleForwardingListenableFuture<V> implements CheckedFuture<V, X> {
    protected abstract X mapException(Exception exception);

    protected AbstractCheckedFuture(ListenableFuture<V> listenableFuture) {
        super(listenableFuture);
    }

    public V checkedGet() throws Exception {
        try {
            return get();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw mapException(e);
        } catch (Exception e2) {
            throw mapException(e2);
        } catch (Exception e22) {
            throw mapException(e22);
        }
    }

    public V checkedGet(long j, TimeUnit timeUnit) throws TimeoutException, Exception {
        try {
            return get(j, timeUnit);
        } catch (long j2) {
            Thread.currentThread().interrupt();
            throw mapException(j2);
        } catch (long j22) {
            throw mapException(j22);
        } catch (long j222) {
            throw mapException(j222);
        }
    }
}
