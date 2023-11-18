package com.google.common.eventbus;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

class Subscriber {
    private EventBus bus;
    private final Executor executor;
    private final Method method;
    @VisibleForTesting
    final Object target;

    @VisibleForTesting
    static final class SynchronizedSubscriber extends Subscriber {
        private SynchronizedSubscriber(EventBus eventBus, Object obj, Method method) {
            super(eventBus, obj, method);
        }

        void invokeSubscriberMethod(Object obj) throws InvocationTargetException {
            synchronized (this) {
                super.invokeSubscriberMethod(obj);
            }
        }
    }

    static Subscriber create(EventBus eventBus, Object obj, Method method) {
        return isDeclaredThreadSafe(method) ? new Subscriber(eventBus, obj, method) : new SynchronizedSubscriber(eventBus, obj, method);
    }

    private Subscriber(EventBus eventBus, Object obj, Method method) {
        this.bus = eventBus;
        this.target = Preconditions.checkNotNull(obj);
        this.method = method;
        method.setAccessible(true);
        this.executor = eventBus.executor();
    }

    final void dispatchEvent(final Object obj) {
        this.executor.execute(new Runnable() {
            public void run() {
                try {
                    Subscriber.this.invokeSubscriberMethod(obj);
                } catch (InvocationTargetException e) {
                    Subscriber.this.bus.handleSubscriberException(e.getCause(), Subscriber.this.context(obj));
                }
            }
        });
    }

    @VisibleForTesting
    void invokeSubscriberMethod(Object obj) throws InvocationTargetException {
        StringBuilder stringBuilder;
        try {
            this.method.invoke(this.target, new Object[]{Preconditions.checkNotNull(obj)});
        } catch (Throwable e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Method rejected target/argument: ");
            stringBuilder.append(obj);
            throw new Error(stringBuilder.toString(), e);
        } catch (Throwable e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Method became inaccessible: ");
            stringBuilder.append(obj);
            throw new Error(stringBuilder.toString(), e2);
        } catch (Object obj2) {
            if (obj2.getCause() instanceof Error) {
                throw ((Error) obj2.getCause());
            }
            throw obj2;
        }
    }

    private SubscriberExceptionContext context(Object obj) {
        return new SubscriberExceptionContext(this.bus, obj, this.target, this.method);
    }

    public final int hashCode() {
        return ((this.method.hashCode() + 31) * 31) + System.identityHashCode(this.target);
    }

    public final boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof Subscriber)) {
            return false;
        }
        Subscriber subscriber = (Subscriber) obj;
        if (this.target == subscriber.target && this.method.equals(subscriber.method) != null) {
            z = true;
        }
        return z;
    }

    private static boolean isDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null ? true : null;
    }
}
