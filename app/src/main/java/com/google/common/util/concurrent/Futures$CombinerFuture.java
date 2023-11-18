package com.google.common.util.concurrent;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.concurrent.Callable;

final class Futures$CombinerFuture<V> extends ListenableFutureTask<V> {
    ImmutableList<ListenableFuture<?>> futures;

    Futures$CombinerFuture(Callable<V> callable, ImmutableList<ListenableFuture<?>> immutableList) {
        super(callable);
        this.futures = immutableList;
    }

    public boolean cancel(boolean z) {
        ImmutableList immutableList = this.futures;
        if (!super.cancel(z)) {
            return false;
        }
        Iterator it = immutableList.iterator();
        while (it.hasNext()) {
            ((ListenableFuture) it.next()).cancel(z);
        }
        return true;
    }

    protected void done() {
        super.done();
        this.futures = null;
    }

    protected void setException(Throwable th) {
        super.setException(th);
    }
}
