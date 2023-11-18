package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

class Futures$MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X> {
    final Function<? super Exception, X> mapper;

    Futures$MappingCheckedFuture(ListenableFuture<V> listenableFuture, Function<? super Exception, X> function) {
        super(listenableFuture);
        this.mapper = (Function) Preconditions.checkNotNull(function);
    }

    protected X mapException(Exception exception) {
        return (Exception) this.mapper.apply(exception);
    }
}
