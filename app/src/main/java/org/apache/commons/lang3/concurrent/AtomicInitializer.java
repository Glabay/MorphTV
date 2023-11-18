package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicInitializer<T> implements ConcurrentInitializer<T> {
    private final AtomicReference<T> reference = new AtomicReference();

    protected abstract T initialize() throws ConcurrentException;

    public T get() throws ConcurrentException {
        T t = this.reference.get();
        if (t != null) {
            return t;
        }
        t = initialize();
        return !this.reference.compareAndSet(null, t) ? this.reference.get() : t;
    }
}
