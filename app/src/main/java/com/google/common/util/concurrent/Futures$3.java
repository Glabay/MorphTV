package com.google.common.util.concurrent;

import com.google.common.base.Function;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class Futures$3 implements Future<O> {
    final /* synthetic */ Function val$function;
    final /* synthetic */ Future val$input;

    Futures$3(Future future, Function function) {
        this.val$input = future;
        this.val$function = function;
    }

    public boolean cancel(boolean z) {
        return this.val$input.cancel(z);
    }

    public boolean isCancelled() {
        return this.val$input.isCancelled();
    }

    public boolean isDone() {
        return this.val$input.isDone();
    }

    public O get() throws InterruptedException, ExecutionException {
        return applyTransformation(this.val$input.get());
    }

    public O get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return applyTransformation(this.val$input.get(j, timeUnit));
    }

    private O applyTransformation(I i) throws ExecutionException {
        try {
            return this.val$function.apply(i);
        } catch (I i2) {
            ExecutionException executionException = new ExecutionException(i2);
        }
    }
}
