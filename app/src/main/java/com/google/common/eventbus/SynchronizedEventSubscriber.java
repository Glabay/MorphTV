package com.google.common.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class SynchronizedEventSubscriber extends EventSubscriber {
    public SynchronizedEventSubscriber(Object obj, Method method) {
        super(obj, method);
    }

    public void handleEvent(Object obj) throws InvocationTargetException {
        synchronized (this) {
            super.handleEvent(obj);
        }
    }
}
