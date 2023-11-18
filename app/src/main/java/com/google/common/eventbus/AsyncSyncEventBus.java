package com.google.common.eventbus;

import com.android.morpheustv.service.BackgroundService;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.concurrent.Executor;

public class AsyncSyncEventBus extends EventBus {
    private Dispatcher syncDispatcher = Dispatcher.perThreadDispatchQueue();

    public AsyncSyncEventBus(String str, Executor executor, Class<? extends Annotation> cls) {
        super(str, executor, Dispatcher.legacyAsync(), LoggingHandler.INSTANCE, cls);
    }

    public AsyncSyncEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler, Class<? extends Annotation> cls) {
        super(BackgroundService.PRIMARY_NOTIFICATION_CHANNEL, executor, Dispatcher.legacyAsync(), subscriberExceptionHandler, cls);
    }

    public AsyncSyncEventBus(Executor executor, Class<? extends Annotation> cls) {
        super(BackgroundService.PRIMARY_NOTIFICATION_CHANNEL, executor, Dispatcher.legacyAsync(), LoggingHandler.INSTANCE, cls);
    }

    public void fire(Object obj) {
        Iterator subscribers = getSubscribers().getSubscribers(obj);
        if (subscribers.hasNext()) {
            this.syncDispatcher.dispatch(obj, subscribers);
        } else if (!(obj instanceof DeadEvent)) {
            fire(new DeadEvent(this, obj));
        }
    }
}
