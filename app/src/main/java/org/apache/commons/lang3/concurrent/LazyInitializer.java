package org.apache.commons.lang3.concurrent;

public abstract class LazyInitializer<T> implements ConcurrentInitializer<T> {
    private volatile T object;

    protected abstract T initialize() throws ConcurrentException;

    public T get() throws ConcurrentException {
        T t = this.object;
        if (t == null) {
            synchronized (this) {
                t = this.object;
                if (t == null) {
                    t = initialize();
                    this.object = t;
                }
            }
        }
        return t;
    }
}
