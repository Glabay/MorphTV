package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryBuilder {
    private ThreadFactory backingThreadFactory = null;
    private Boolean daemon = null;
    private String nameFormat = null;
    private Integer priority = null;
    private UncaughtExceptionHandler uncaughtExceptionHandler = null;

    public ThreadFactoryBuilder setNameFormat(String str) {
        String.format(str, new Object[]{Integer.valueOf(0)});
        this.nameFormat = str;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean z) {
        this.daemon = Boolean.valueOf(z);
        return this;
    }

    public ThreadFactoryBuilder setPriority(int i) {
        Preconditions.checkArgument(i >= 1, "Thread priority (%s) must be >= %s", new Object[]{Integer.valueOf(i), Integer.valueOf(1)});
        Preconditions.checkArgument(i <= 10, "Thread priority (%s) must be <= %s", new Object[]{Integer.valueOf(i), Integer.valueOf(10)});
        this.priority = Integer.valueOf(i);
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = (UncaughtExceptionHandler) Preconditions.checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory threadFactory) {
        this.backingThreadFactory = (ThreadFactory) Preconditions.checkNotNull(threadFactory);
        return this;
    }

    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder threadFactoryBuilder) {
        final String str = threadFactoryBuilder.nameFormat;
        final Boolean bool = threadFactoryBuilder.daemon;
        final Integer num = threadFactoryBuilder.priority;
        final UncaughtExceptionHandler uncaughtExceptionHandler = threadFactoryBuilder.uncaughtExceptionHandler;
        final ThreadFactoryBuilder defaultThreadFactory = threadFactoryBuilder.backingThreadFactory != null ? threadFactoryBuilder.backingThreadFactory : Executors.defaultThreadFactory();
        final ThreadFactoryBuilder atomicLong = str != null ? new AtomicLong(0) : null;
        return new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                runnable = defaultThreadFactory.newThread(runnable);
                if (str != null) {
                    runnable.setName(String.format(str, new Object[]{Long.valueOf(atomicLong.getAndIncrement())}));
                }
                if (bool != null) {
                    runnable.setDaemon(bool.booleanValue());
                }
                if (num != null) {
                    runnable.setPriority(num.intValue());
                }
                if (uncaughtExceptionHandler != null) {
                    runnable.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }
                return runnable;
            }
        };
    }
}
