package com.google.common.eventbus;

import com.android.morpheustv.service.BackgroundService;
import com.google.common.annotations.Beta;
import java.lang.annotation.Annotation;
import java.util.concurrent.Executor;

@Beta
public class AsyncEventBus extends EventBus {
    public AsyncEventBus(String str, Executor executor, Class<? extends Annotation> cls) {
        super(str, executor, Dispatcher.legacyAsync(), LoggingHandler.INSTANCE, cls);
    }

    public AsyncEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler, Class<? extends Annotation> cls) {
        super(BackgroundService.PRIMARY_NOTIFICATION_CHANNEL, executor, Dispatcher.legacyAsync(), subscriberExceptionHandler, cls);
    }

    public AsyncEventBus(Executor executor, Class<? extends Annotation> cls) {
        super(BackgroundService.PRIMARY_NOTIFICATION_CHANNEL, executor, Dispatcher.legacyAsync(), LoggingHandler.INSTANCE, cls);
    }
}
