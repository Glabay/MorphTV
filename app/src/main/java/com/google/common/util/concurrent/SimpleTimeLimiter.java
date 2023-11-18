package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Beta
public final class SimpleTimeLimiter implements TimeLimiter {
    private final ExecutorService executor;

    public SimpleTimeLimiter(ExecutorService executorService) {
        this.executor = (ExecutorService) Preconditions.checkNotNull(executorService);
    }

    public SimpleTimeLimiter() {
        this(Executors.newCachedThreadPool());
    }

    public <T> T newProxy(T t, Class<T> cls, long j, TimeUnit timeUnit) {
        Preconditions.checkNotNull(t);
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(timeUnit);
        Preconditions.checkArgument(j > 0, "bad timeout: %s", new Object[]{Long.valueOf(j)});
        Preconditions.checkArgument(cls.isInterface(), "interfaceType must be an interface type");
        final Set findInterruptibleMethods = findInterruptibleMethods(cls);
        final T t2 = t;
        final long j2 = j;
        final TimeUnit timeUnit2 = timeUnit;
        return newProxy(cls, new InvocationHandler() {
            public Object invoke(Object obj, final Method method, final Object[] objArr) throws Throwable {
                return SimpleTimeLimiter.this.callWithTimeout(new Callable<Object>() {
                    public Object call() throws Exception {
                        try {
                            return method.invoke(t2, objArr);
                        } catch (Exception e) {
                            SimpleTimeLimiter.throwCause(e, false);
                            throw new AssertionError("can't get here");
                        }
                    }
                }, j2, timeUnit2, findInterruptibleMethods.contains(method));
            }
        });
    }

    public <T> T callWithTimeout(Callable<T> callable, long j, TimeUnit timeUnit, boolean z) throws Exception {
        Preconditions.checkNotNull(callable);
        Preconditions.checkNotNull(timeUnit);
        Preconditions.checkArgument(j > 0, "timeout must be positive: %s", new Object[]{Long.valueOf(j)});
        callable = this.executor.submit(callable);
        if (!z) {
            return Uninterruptibles.getUninterruptibly(callable, j, timeUnit);
        }
        try {
            return callable.get(j, timeUnit);
        } catch (long j2) {
            callable.cancel(true);
            throw j2;
        } catch (Callable<T> callable2) {
            throw throwCause(callable2, true);
        } catch (long j22) {
            callable2.cancel(true);
            throw new UncheckedTimeoutException(j22);
        }
    }

    private static Exception throwCause(Exception exception, boolean z) throws Exception {
        Throwable cause = exception.getCause();
        if (cause == null) {
            throw exception;
        }
        if (z) {
            cause.setStackTrace((StackTraceElement[]) ObjectArrays.concat(cause.getStackTrace(), exception.getStackTrace(), StackTraceElement.class));
        }
        if (cause instanceof Exception) {
            throw ((Exception) cause);
        } else if (cause instanceof Error) {
            throw ((Error) cause);
        } else {
            throw exception;
        }
    }

    private static Set<Method> findInterruptibleMethods(Class<?> cls) {
        Set<Method> newHashSet = Sets.newHashSet();
        for (Method method : cls.getMethods()) {
            if (declaresInterruptedEx(method)) {
                newHashSet.add(method);
            }
        }
        return newHashSet;
    }

    private static boolean declaresInterruptedEx(Method method) {
        for (Class cls : method.getExceptionTypes()) {
            if (cls == InterruptedException.class) {
                return true;
            }
        }
        return false;
    }

    private static <T> T newProxy(Class<T> cls, InvocationHandler invocationHandler) {
        return cls.cast(Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, invocationHandler));
    }
}
