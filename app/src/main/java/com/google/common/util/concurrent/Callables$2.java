package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import java.util.concurrent.Callable;

class Callables$2 implements Callable<T> {
    final /* synthetic */ Callable val$callable;
    final /* synthetic */ Supplier val$nameSupplier;

    Callables$2(Supplier supplier, Callable callable) {
        this.val$nameSupplier = supplier;
        this.val$callable = callable;
    }

    public T call() throws Exception {
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        boolean access$000 = Callables.access$000((String) this.val$nameSupplier.get(), currentThread);
        try {
            T call = this.val$callable.call();
            return call;
        } finally {
            if (access$000) {
                Callables.access$000(name, currentThread);
            }
        }
    }
}
