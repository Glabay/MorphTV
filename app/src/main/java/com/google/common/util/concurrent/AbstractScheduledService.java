package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
import org.apache.commons.lang3.StringUtils;

@Beta
public abstract class AbstractScheduledService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
    private final AbstractService delegate = new C12051();

    /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1 */
    class C12051 extends AbstractService {
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock = new ReentrantLock();
        private volatile Future<?> runningTask;
        private final Runnable task = new C12011();

        /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1$1 */
        class C12011 implements Runnable {
            C12011() {
            }

            public void run() {
                C12051.this.lock.lock();
                try {
                    AbstractScheduledService.this.runOneIteration();
                    C12051.this.lock.unlock();
                } catch (Throwable th) {
                    C12051.this.lock.unlock();
                }
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1$2 */
        class C12022 implements Supplier<String> {
            C12022() {
            }

            public String get() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(AbstractScheduledService.this.serviceName());
                stringBuilder.append(StringUtils.SPACE);
                stringBuilder.append(C12051.this.state());
                return stringBuilder.toString();
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1$3 */
        class C12033 implements Runnable {
            C12033() {
            }

            public void run() {
                C12051.this.lock.lock();
                try {
                    AbstractScheduledService.this.startUp();
                    C12051.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, C12051.this.executorService, C12051.this.task);
                    C12051.this.notifyStarted();
                    C12051.this.lock.unlock();
                } catch (Throwable th) {
                    C12051.this.lock.unlock();
                }
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$1$4 */
        class C12044 implements Runnable {
            C12044() {
            }

            public void run() {
                try {
                    C12051.this.lock.lock();
                    if (C12051.this.state() != State.STOPPING) {
                        C12051.this.lock.unlock();
                        return;
                    }
                    AbstractScheduledService.this.shutDown();
                    C12051.this.lock.unlock();
                    C12051.this.notifyStopped();
                } catch (Throwable th) {
                    C12051.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        C12051() {
        }

        protected final void doStart() {
            this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new C12022());
            this.executorService.execute(new C12033());
        }

        protected final void doStop() {
            this.runningTask.cancel(false);
            this.executorService.execute(new C12044());
        }
    }

    /* renamed from: com.google.common.util.concurrent.AbstractScheduledService$2 */
    class C12062 implements ThreadFactory {
        C12062() {
        }

        public Thread newThread(Runnable runnable) {
            return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
        }
    }

    public static abstract class Scheduler {
        abstract Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable);

        public static Scheduler newFixedDelaySchedule(long j, long j2, TimeUnit timeUnit) {
            final long j3 = j;
            final long j4 = j2;
            final TimeUnit timeUnit2 = timeUnit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
                    return scheduledExecutorService.scheduleWithFixedDelay(runnable, j3, j4, timeUnit2);
                }
            };
        }

        public static Scheduler newFixedRateSchedule(long j, long j2, TimeUnit timeUnit) {
            final long j3 = j;
            final long j4 = j2;
            final TimeUnit timeUnit2 = timeUnit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
                    return scheduledExecutorService.scheduleAtFixedRate(runnable, j3, j4, timeUnit2);
                }
            };
        }

        private Scheduler() {
        }
    }

    @Beta
    public static abstract class CustomScheduler extends Scheduler {

        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
            @GuardedBy("lock")
            private Future<Void> currentFuture;
            private final ScheduledExecutorService executor;
            private final ReentrantLock lock = new ReentrantLock();
            private final AbstractService service;
            private final Runnable wrappedRunnable;

            ReschedulableCallable(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
                this.wrappedRunnable = runnable;
                this.executor = scheduledExecutorService;
                this.service = abstractService;
            }

            public Void call() throws Exception {
                this.wrappedRunnable.run();
                reschedule();
                return null;
            }

            public void reschedule() {
                this.lock.lock();
                try {
                    if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
                        Schedule nextSchedule = CustomScheduler.this.getNextSchedule();
                        this.currentFuture = this.executor.schedule(this, nextSchedule.delay, nextSchedule.unit);
                    }
                } catch (Throwable th) {
                    this.lock.unlock();
                }
                this.lock.unlock();
            }

            public boolean cancel(boolean z) {
                this.lock.lock();
                try {
                    z = this.currentFuture.cancel(z);
                    return z;
                } finally {
                    this.lock.unlock();
                }
            }

            protected Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel is supported by this future");
            }
        }

        @Beta
        protected static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            public Schedule(long j, TimeUnit timeUnit) {
                this.delay = j;
                this.unit = (TimeUnit) Preconditions.checkNotNull(timeUnit);
            }
        }

        protected abstract Schedule getNextSchedule() throws Exception;

        public CustomScheduler() {
            super();
        }

        final Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
            Future reschedulableCallable = new ReschedulableCallable(abstractService, scheduledExecutorService, runnable);
            reschedulableCallable.reschedule();
            return reschedulableCallable;
        }
    }

    protected abstract void runOneIteration() throws Exception;

    protected abstract Scheduler scheduler();

    protected void shutDown() throws Exception {
    }

    protected void startUp() throws Exception {
    }

    protected AbstractScheduledService() {
    }

    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor(new C12062());
        addListener(new Listener() {
            public void terminated(State state) {
                newSingleThreadScheduledExecutor.shutdown();
            }

            public void failed(State state, Throwable th) {
                newSingleThreadScheduledExecutor.shutdown();
            }
        }, MoreExecutors.directExecutor());
        return newSingleThreadScheduledExecutor;
    }

    protected String serviceName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(serviceName());
        stringBuilder.append(" [");
        stringBuilder.append(state());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    public final State state() {
        return this.delegate.state();
    }

    public final void addListener(Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    public final void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException {
        this.delegate.awaitRunning(j, timeUnit);
    }

    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    public final void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException {
        this.delegate.awaitTerminated(j, timeUnit);
    }
}
