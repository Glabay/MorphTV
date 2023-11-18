package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;

public class ConcurrentUtils {

    static final class ConstantFuture<T> implements Future<T> {
        private final T value;

        public boolean cancel(boolean z) {
            return false;
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }

        ConstantFuture(T t) {
            this.value = t;
        }

        public T get() {
            return this.value;
        }

        public T get(long j, TimeUnit timeUnit) {
            return this.value;
        }
    }

    private ConcurrentUtils() {
    }

    public static ConcurrentException extractCause(ExecutionException executionException) {
        if (executionException != null) {
            if (executionException.getCause() != null) {
                throwCause(executionException);
                return new ConcurrentException(executionException.getMessage(), executionException.getCause());
            }
        }
        return null;
    }

    public static ConcurrentRuntimeException extractCauseUnchecked(ExecutionException executionException) {
        if (executionException != null) {
            if (executionException.getCause() != null) {
                throwCause(executionException);
                return new ConcurrentRuntimeException(executionException.getMessage(), executionException.getCause());
            }
        }
        return null;
    }

    public static void handleCause(ExecutionException executionException) throws ConcurrentException {
        executionException = extractCause(executionException);
        if (executionException != null) {
            throw executionException;
        }
    }

    public static void handleCauseUnchecked(ExecutionException executionException) {
        executionException = extractCauseUnchecked(executionException);
        if (executionException != null) {
            throw executionException;
        }
    }

    static Throwable checkedException(Throwable th) {
        boolean z = (th == null || (th instanceof RuntimeException) || (th instanceof Error)) ? false : true;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Not a checked exception: ");
        stringBuilder.append(th);
        Validate.isTrue(z, stringBuilder.toString(), new Object[0]);
        return th;
    }

    private static void throwCause(ExecutionException executionException) {
        if (executionException.getCause() instanceof RuntimeException) {
            throw ((RuntimeException) executionException.getCause());
        } else if (executionException.getCause() instanceof Error) {
            throw ((Error) executionException.getCause());
        }
    }

    public static <T> T initialize(ConcurrentInitializer<T> concurrentInitializer) throws ConcurrentException {
        return concurrentInitializer != null ? concurrentInitializer.get() : null;
    }

    public static <T> T initializeUnchecked(ConcurrentInitializer<T> concurrentInitializer) {
        try {
            return initialize(concurrentInitializer);
        } catch (ConcurrentInitializer<T> concurrentInitializer2) {
            throw new ConcurrentRuntimeException(concurrentInitializer2.getCause());
        }
    }

    public static <K, V> V putIfAbsent(ConcurrentMap<K, V> concurrentMap, K k, V v) {
        if (concurrentMap == null) {
            return null;
        }
        concurrentMap = concurrentMap.putIfAbsent(k, v);
        if (concurrentMap == null) {
            concurrentMap = v;
        }
        return concurrentMap;
    }

    public static <K, V> V createIfAbsent(ConcurrentMap<K, V> concurrentMap, K k, ConcurrentInitializer<V> concurrentInitializer) throws ConcurrentException {
        if (concurrentMap != null) {
            if (concurrentInitializer != null) {
                V v = concurrentMap.get(k);
                return v == null ? putIfAbsent(concurrentMap, k, concurrentInitializer.get()) : v;
            }
        }
        return null;
    }

    public static <K, V> V createIfAbsentUnchecked(ConcurrentMap<K, V> concurrentMap, K k, ConcurrentInitializer<V> concurrentInitializer) {
        try {
            return createIfAbsent(concurrentMap, k, concurrentInitializer);
        } catch (ConcurrentMap<K, V> concurrentMap2) {
            throw new ConcurrentRuntimeException(concurrentMap2.getCause());
        }
    }

    public static <T> Future<T> constantFuture(T t) {
        return new ConstantFuture(t);
    }
}
