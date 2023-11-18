package com.google.common.util.concurrent;

import java.util.concurrent.Callable;

class Callables$1 implements Callable<T> {
    final /* synthetic */ Object val$value;

    Callables$1(Object obj) {
        this.val$value = obj;
    }

    public T call() {
        return this.val$value;
    }
}
