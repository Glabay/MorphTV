package com.google.common.util.concurrent;

import com.google.common.base.Function;

class Futures$2 implements AsyncFunction<I, O> {
    final /* synthetic */ Function val$function;

    Futures$2(Function function) {
        this.val$function = function;
    }

    public ListenableFuture<O> apply(I i) {
        return Futures.immediateFuture(this.val$function.apply(i));
    }
}
