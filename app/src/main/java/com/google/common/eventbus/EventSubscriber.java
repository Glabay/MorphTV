package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

class EventSubscriber {
    private final Method method;
    private final Object target;

    EventSubscriber(Object obj, Method method) {
        Preconditions.checkNotNull(obj, "EventSubscriber target cannot be null.");
        Preconditions.checkNotNull(method, "EventSubscriber method cannot be null.");
        this.target = obj;
        this.method = method;
        method.setAccessible(true);
    }

    public void handleEvent(Object obj) throws InvocationTargetException {
        StringBuilder stringBuilder;
        Preconditions.checkNotNull(obj);
        try {
            this.method.invoke(this.target, new Object[]{obj});
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

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[wrapper ");
        stringBuilder.append(this.method);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int hashCode() {
        return ((this.method.hashCode() + 31) * 31) + System.identityHashCode(this.target);
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof EventSubscriber)) {
            return false;
        }
        EventSubscriber eventSubscriber = (EventSubscriber) obj;
        if (this.target == eventSubscriber.target && this.method.equals(eventSubscriber.method) != null) {
            z = true;
        }
        return z;
    }

    public Object getSubscriber() {
        return this.target;
    }

    public Method getMethod() {
        return this.method;
    }
}
