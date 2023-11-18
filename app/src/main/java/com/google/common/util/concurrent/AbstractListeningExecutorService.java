package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractListeningExecutorService extends AbstractExecutorService implements ListeningExecutorService {
    protected final <T> ListenableFutureTask<T> newTaskFor(Runnable runnable, T t) {
        return ListenableFutureTask.create(runnable, t);
    }

    protected final <T> ListenableFutureTask<T> newTaskFor(Callable<T> callable) {
        return ListenableFutureTask.create(callable);
    }

    public ListenableFuture<?> submit(Runnable runnable) {
        return (ListenableFuture) super.submit(runnable);
    }

    public <T> ListenableFuture<T> submit(Runnable runnable, @Nullable T t) {
        return (ListenableFuture) super.submit(runnable, t);
    }

    public <T> ListenableFuture<T> submit(Callable<T> callable) {
        return (ListenableFuture) super.submit(callable);
    }
}
