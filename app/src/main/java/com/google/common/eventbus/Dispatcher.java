package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract class Dispatcher {

    private static final class ImmediateDispatcher extends Dispatcher {
        private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();

        private ImmediateDispatcher() {
        }

        void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            while (it.hasNext()) {
                ((Subscriber) it.next()).dispatchEvent(obj);
            }
        }
    }

    private static final class LegacyAsyncDispatcher extends Dispatcher {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;

        private static final class EventWithSubscriber {
            private final Object event;
            private final Subscriber subscriber;

            private EventWithSubscriber(Object obj, Subscriber subscriber) {
                this.event = obj;
                this.subscriber = subscriber;
            }
        }

        private LegacyAsyncDispatcher() {
            this.queue = Queues.newConcurrentLinkedQueue();
        }

        void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            while (it.hasNext()) {
                this.queue.add(new EventWithSubscriber(obj, (Subscriber) it.next()));
            }
            while (true) {
                EventWithSubscriber eventWithSubscriber = (EventWithSubscriber) this.queue.poll();
                if (eventWithSubscriber != null) {
                    eventWithSubscriber.subscriber.dispatchEvent(eventWithSubscriber.event);
                } else {
                    return;
                }
            }
        }
    }

    private static final class PerThreadQueuedDispatcher extends Dispatcher {
        private final ThreadLocal<Boolean> dispatching;
        private final ThreadLocal<Queue<Event>> queue;

        /* renamed from: com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$1 */
        class C11221 extends ThreadLocal<Queue<Event>> {
            C11221() {
            }

            protected Queue<Event> initialValue() {
                return Queues.newArrayDeque();
            }
        }

        /* renamed from: com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$2 */
        class C11232 extends ThreadLocal<Boolean> {
            C11232() {
            }

            protected Boolean initialValue() {
                return Boolean.valueOf(false);
            }
        }

        private static final class Event {
            private final Object event;
            private final Iterator<Subscriber> subscribers;

            private Event(Object obj, Iterator<Subscriber> it) {
                this.event = obj;
                this.subscribers = it;
            }
        }

        private PerThreadQueuedDispatcher() {
            this.queue = new C11221();
            this.dispatching = new C11232();
        }

        void dispatch(java.lang.Object r4, java.util.Iterator<com.google.common.eventbus.Subscriber> r5) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            com.google.common.base.Preconditions.checkNotNull(r4);
            com.google.common.base.Preconditions.checkNotNull(r5);
            r0 = r3.queue;
            r0 = r0.get();
            r0 = (java.util.Queue) r0;
            r1 = new com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event;
            r2 = 0;
            r1.<init>(r4, r5);
            r0.offer(r1);
            r4 = r3.dispatching;
            r4 = r4.get();
            r4 = (java.lang.Boolean) r4;
            r4 = r4.booleanValue();
            if (r4 != 0) goto L_0x006a;
        L_0x0025:
            r4 = r3.dispatching;
            r5 = 1;
            r5 = java.lang.Boolean.valueOf(r5);
            r4.set(r5);
        L_0x002f:
            r4 = r0.poll();	 Catch:{ all -> 0x005e }
            r4 = (com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.Event) r4;	 Catch:{ all -> 0x005e }
            if (r4 == 0) goto L_0x0053;	 Catch:{ all -> 0x005e }
        L_0x0037:
            r5 = r4.subscribers;	 Catch:{ all -> 0x005e }
            r5 = r5.hasNext();	 Catch:{ all -> 0x005e }
            if (r5 == 0) goto L_0x002f;	 Catch:{ all -> 0x005e }
        L_0x0041:
            r5 = r4.subscribers;	 Catch:{ all -> 0x005e }
            r5 = r5.next();	 Catch:{ all -> 0x005e }
            r5 = (com.google.common.eventbus.Subscriber) r5;	 Catch:{ all -> 0x005e }
            r1 = r4.event;	 Catch:{ all -> 0x005e }
            r5.dispatchEvent(r1);	 Catch:{ all -> 0x005e }
            goto L_0x0037;
        L_0x0053:
            r4 = r3.dispatching;
            r4.remove();
            r4 = r3.queue;
            r4.remove();
            goto L_0x006a;
        L_0x005e:
            r4 = move-exception;
            r5 = r3.dispatching;
            r5.remove();
            r5 = r3.queue;
            r5.remove();
            throw r4;
        L_0x006a:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.dispatch(java.lang.Object, java.util.Iterator):void");
        }
    }

    abstract void dispatch(Object obj, Iterator<Subscriber> it);

    Dispatcher() {
    }

    static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }

    static Dispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }
}
