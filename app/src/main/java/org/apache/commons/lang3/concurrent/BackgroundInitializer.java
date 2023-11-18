package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BackgroundInitializer<T> implements ConcurrentInitializer<T> {
    private ExecutorService executor;
    private ExecutorService externalExecutor;
    private Future<T> future;

    private class InitializationTask implements Callable<T> {
        private final ExecutorService execFinally;

        public InitializationTask(ExecutorService executorService) {
            this.execFinally = executorService;
        }

        public T call() throws Exception {
            try {
                T initialize = BackgroundInitializer.this.initialize();
                return initialize;
            } finally {
                if (this.execFinally != null) {
                    this.execFinally.shutdown();
                }
            }
        }
    }

    protected int getTaskCount() {
        return 1;
    }

    protected abstract T initialize() throws Exception;

    protected BackgroundInitializer() {
        this(null);
    }

    protected BackgroundInitializer(ExecutorService executorService) {
        setExternalExecutor(executorService);
    }

    public final synchronized ExecutorService getExternalExecutor() {
        return this.externalExecutor;
    }

    public synchronized boolean isStarted() {
        return this.future != null;
    }

    public final synchronized void setExternalExecutor(ExecutorService executorService) {
        if (isStarted()) {
            throw new IllegalStateException("Cannot set ExecutorService after start()!");
        }
        this.externalExecutor = executorService;
    }

    public synchronized boolean start() {
        if (isStarted()) {
            return false;
        }
        ExecutorService createExecutor;
        this.executor = getExternalExecutor();
        if (this.executor == null) {
            createExecutor = createExecutor();
            this.executor = createExecutor;
        } else {
            createExecutor = null;
        }
        this.future = this.executor.submit(createTask(createExecutor));
        return true;
    }

    public T get() throws ConcurrentException {
        try {
            return getFuture().get();
        } catch (ExecutionException e) {
            ConcurrentUtils.handleCause(e);
            return null;
        } catch (Throwable e2) {
            Thread.currentThread().interrupt();
            throw new ConcurrentException(e2);
        }
    }

    public synchronized Future<T> getFuture() {
        if (this.future == null) {
            throw new IllegalStateException("start() must be called first!");
        }
        return this.future;
    }

    protected final synchronized ExecutorService getActiveExecutor() {
        return this.executor;
    }

    private Callable<T> createTask(ExecutorService executorService) {
        return new InitializationTask(executorService);
    }

    private ExecutorService createExecutor() {
        return Executors.newFixedThreadPool(getTaskCount());
    }
}
