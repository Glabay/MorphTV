package com.google.common.util.concurrent;

import com.google.common.base.Supplier;

class Callables$3 implements Runnable {
    final /* synthetic */ Supplier val$nameSupplier;
    final /* synthetic */ Runnable val$task;

    Callables$3(Supplier supplier, Runnable runnable) {
        this.val$nameSupplier = supplier;
        this.val$task = runnable;
    }

    public void run() {
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        boolean access$000 = Callables.access$000((String) this.val$nameSupplier.get(), currentThread);
        try {
            this.val$task.run();
        } finally {
            if (access$000) {
                Callables.access$000(name, currentThread);
            }
        }
    }
}
