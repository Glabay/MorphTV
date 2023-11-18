package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.StringUtils;

@Beta
public abstract class AbstractIdleService implements Service {
    private final Service delegate = new C11992();
    private final Supplier<String> threadNameSupplier = new C11961();

    /* renamed from: com.google.common.util.concurrent.AbstractIdleService$1 */
    class C11961 implements Supplier<String> {
        C11961() {
        }

        public String get() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(AbstractIdleService.this.serviceName());
            stringBuilder.append(StringUtils.SPACE);
            stringBuilder.append(AbstractIdleService.this.state());
            return stringBuilder.toString();
        }
    }

    /* renamed from: com.google.common.util.concurrent.AbstractIdleService$2 */
    class C11992 extends AbstractService {

        /* renamed from: com.google.common.util.concurrent.AbstractIdleService$2$1 */
        class C11971 implements Runnable {
            C11971() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.startUp();
                    C11992.this.notifyStarted();
                } catch (Throwable th) {
                    C11992.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        /* renamed from: com.google.common.util.concurrent.AbstractIdleService$2$2 */
        class C11982 implements Runnable {
            C11982() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.shutDown();
                    C11992.this.notifyStopped();
                } catch (Throwable th) {
                    C11992.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        C11992() {
        }

        protected final void doStart() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new C11971());
        }

        protected final void doStop() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new C11982());
        }
    }

    /* renamed from: com.google.common.util.concurrent.AbstractIdleService$3 */
    class C12003 implements Executor {
        C12003() {
        }

        public void execute(Runnable runnable) {
            MoreExecutors.newThread((String) AbstractIdleService.this.threadNameSupplier.get(), runnable).start();
        }
    }

    protected abstract void shutDown() throws Exception;

    protected abstract void startUp() throws Exception;

    protected AbstractIdleService() {
    }

    protected Executor executor() {
        return new C12003();
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
