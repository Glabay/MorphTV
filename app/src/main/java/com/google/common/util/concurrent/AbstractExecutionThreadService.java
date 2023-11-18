package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Beta
public abstract class AbstractExecutionThreadService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
    private final Service delegate = new C11941();

    /* renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$1 */
    class C11941 extends AbstractService {

        /* renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$1$1 */
        class C11921 implements Supplier<String> {
            C11921() {
            }

            public String get() {
                return AbstractExecutionThreadService.this.serviceName();
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$1$2 */
        class C11932 implements Runnable {
            C11932() {
            }

            public void run() {
                try {
                    AbstractExecutionThreadService.this.startUp();
                    C11941.this.notifyStarted();
                    if (C11941.this.isRunning()) {
                        AbstractExecutionThreadService.this.run();
                    }
                    AbstractExecutionThreadService.this.shutDown();
                    C11941.this.notifyStopped();
                } catch (Throwable th) {
                    C11941.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        C11941() {
        }

        protected final void doStart() {
            MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this.executor(), new C11921()).execute(new C11932());
        }

        protected void doStop() {
            AbstractExecutionThreadService.this.triggerShutdown();
        }
    }

    /* renamed from: com.google.common.util.concurrent.AbstractExecutionThreadService$2 */
    class C11952 implements Executor {
        C11952() {
        }

        public void execute(Runnable runnable) {
            MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), runnable).start();
        }
    }

    protected abstract void run() throws Exception;

    protected void shutDown() throws Exception {
    }

    protected void startUp() throws Exception {
    }

    protected void triggerShutdown() {
    }

    protected AbstractExecutionThreadService() {
    }

    protected Executor executor() {
        return new C11952();
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

    protected String serviceName() {
        return getClass().getSimpleName();
    }
}
