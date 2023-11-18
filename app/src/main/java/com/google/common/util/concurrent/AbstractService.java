package com.google.common.util.concurrent;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor.Guard;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.concurrent.GuardedBy;

@Beta
public abstract class AbstractService implements Service {
    private static final Callback<Listener> RUNNING_CALLBACK = new Callback<Listener>("running()") {
        void call(Listener listener) {
            listener.running();
        }
    };
    private static final Callback<Listener> STARTING_CALLBACK = new Callback<Listener>("starting()") {
        void call(Listener listener) {
            listener.starting();
        }
    };
    private static final Callback<Listener> STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(State.RUNNING);
    private static final Callback<Listener> STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(State.STARTING);
    private static final Callback<Listener> TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(State.NEW);
    private static final Callback<Listener> TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(State.RUNNING);
    private static final Callback<Listener> TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(State.STOPPING);
    private final Guard hasReachedRunning = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) >= 0;
        }
    };
    private final Guard isStartable = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state() == State.NEW;
        }
    };
    private final Guard isStoppable = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) <= 0;
        }
    };
    private final Guard isStopped = new Guard(this.monitor) {
        public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
        }
    };
    @GuardedBy("monitor")
    private final List<ListenerCallQueue<Listener>> listeners = Collections.synchronizedList(new ArrayList());
    private final Monitor monitor = new Monitor();
    @GuardedBy("monitor")
    private volatile AbstractService$StateSnapshot snapshot = new AbstractService$StateSnapshot(State.NEW);

    protected abstract void doStart();

    protected abstract void doStop();

    private static Callback<Listener> terminatedCallback(final State state) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("terminated({from = ");
        stringBuilder.append(state);
        stringBuilder.append("})");
        return new Callback<Listener>(stringBuilder.toString()) {
            void call(Listener listener) {
                listener.terminated(state);
            }
        };
    }

    private static Callback<Listener> stoppingCallback(final State state) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stopping({from = ");
        stringBuilder.append(state);
        stringBuilder.append("})");
        return new Callback<Listener>(stringBuilder.toString()) {
            void call(Listener listener) {
                listener.stopping(state);
            }
        };
    }

    protected AbstractService() {
    }

    public final Service startAsync() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new AbstractService$StateSnapshot(State.STARTING);
                starting();
                doStart();
            } catch (Throwable th) {
                this.monitor.leave();
                executeListeners();
            }
            this.monitor.leave();
            executeListeners();
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Service ");
        stringBuilder.append(this);
        stringBuilder.append(" has already been started");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public final Service stopAsync() {
        if (this.monitor.enterIf(this.isStoppable)) {
            try {
                State state = state();
                StringBuilder stringBuilder;
                switch (state) {
                    case NEW:
                        this.snapshot = new AbstractService$StateSnapshot(State.TERMINATED);
                        terminated(State.NEW);
                        break;
                    case STARTING:
                        this.snapshot = new AbstractService$StateSnapshot(State.STARTING, true, null);
                        stopping(State.STARTING);
                        break;
                    case RUNNING:
                        this.snapshot = new AbstractService$StateSnapshot(State.STOPPING);
                        stopping(State.RUNNING);
                        doStop();
                        break;
                    case STOPPING:
                    case TERMINATED:
                    case FAILED:
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("isStoppable is incorrectly implemented, saw: ");
                        stringBuilder.append(state);
                        throw new AssertionError(stringBuilder.toString());
                    default:
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected state: ");
                        stringBuilder.append(state);
                        throw new AssertionError(stringBuilder.toString());
                }
            } catch (Throwable th) {
                this.monitor.leave();
                executeListeners();
            }
            this.monitor.leave();
            executeListeners();
        }
        return this;
    }

    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            checkCurrentState(State.RUNNING);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, j, timeUnit) != null) {
            try {
                checkCurrentState(State.RUNNING);
            } finally {
                this.monitor.leave();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Timed out waiting for ");
            stringBuilder.append(this);
            stringBuilder.append(" to reach the RUNNING state. ");
            stringBuilder.append("Current state: ");
            stringBuilder.append(state());
            throw new TimeoutException(stringBuilder.toString());
        }
    }

    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            checkCurrentState(State.TERMINATED);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, j, timeUnit) != null) {
            try {
                checkCurrentState(State.TERMINATED);
            } finally {
                this.monitor.leave();
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Timed out waiting for ");
            stringBuilder.append(this);
            stringBuilder.append(" to reach a terminal state. ");
            stringBuilder.append("Current state: ");
            stringBuilder.append(state());
            throw new TimeoutException(stringBuilder.toString());
        }
    }

    @GuardedBy("monitor")
    private void checkCurrentState(State state) {
        State state2 = state();
        if (state2 == state) {
            return;
        }
        if (state2 == State.FAILED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected the service to be ");
            stringBuilder.append(state);
            stringBuilder.append(", but the service has FAILED");
            throw new IllegalStateException(stringBuilder.toString(), failureCause());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Expected the service to be ");
        stringBuilder2.append(state);
        stringBuilder2.append(", but was ");
        stringBuilder2.append(state2);
        throw new IllegalStateException(stringBuilder2.toString());
    }

    protected final void notifyStarted() {
        this.monitor.enter();
        try {
            if (this.snapshot.state != State.STARTING) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot notifyStarted() when the service is ");
                stringBuilder.append(this.snapshot.state);
                Throwable illegalStateException = new IllegalStateException(stringBuilder.toString());
                notifyFailed(illegalStateException);
                throw illegalStateException;
            }
            if (this.snapshot.shutdownWhenStartupFinishes) {
                this.snapshot = new AbstractService$StateSnapshot(State.STOPPING);
                doStop();
            } else {
                this.snapshot = new AbstractService$StateSnapshot(State.RUNNING);
                running();
            }
            this.monitor.leave();
            executeListeners();
        } catch (Throwable th) {
            this.monitor.leave();
            executeListeners();
        }
    }

    protected final void notifyStopped() {
        this.monitor.enter();
        try {
            State state = this.snapshot.state;
            if (state == State.STOPPING || state == State.RUNNING) {
                this.snapshot = new AbstractService$StateSnapshot(State.TERMINATED);
                terminated(state);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot notifyStopped() when the service is ");
            stringBuilder.append(state);
            Throwable illegalStateException = new IllegalStateException(stringBuilder.toString());
            notifyFailed(illegalStateException);
            throw illegalStateException;
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    protected final void notifyFailed(Throwable th) {
        Preconditions.checkNotNull(th);
        this.monitor.enter();
        try {
            State state = state();
            switch (state) {
                case NEW:
                case TERMINATED:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed while in state:");
                    stringBuilder.append(state);
                    throw new IllegalStateException(stringBuilder.toString(), th);
                case STARTING:
                case RUNNING:
                case STOPPING:
                    this.snapshot = new AbstractService$StateSnapshot(State.FAILED, false, th);
                    failed(state, th);
                    break;
                case FAILED:
                    break;
                default:
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unexpected state: ");
                    stringBuilder2.append(state);
                    throw new AssertionError(stringBuilder2.toString());
            }
            this.monitor.leave();
            executeListeners();
        } catch (Throwable th2) {
            this.monitor.leave();
            executeListeners();
        }
    }

    public final boolean isRunning() {
        return state() == State.RUNNING;
    }

    public final State state() {
        return this.snapshot.externalState();
    }

    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }

    public final void addListener(Listener listener, Executor executor) {
        Preconditions.checkNotNull(listener, CastExtraArgs.LISTENER);
        Preconditions.checkNotNull(executor, "executor");
        this.monitor.enter();
        try {
            if (!state().isTerminal()) {
                this.listeners.add(new ListenerCallQueue(listener, executor));
            }
            this.monitor.leave();
        } catch (Throwable th) {
            this.monitor.leave();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(" [");
        stringBuilder.append(state());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private void executeListeners() {
        if (!this.monitor.isOccupiedByCurrentThread()) {
            for (int i = 0; i < this.listeners.size(); i++) {
                ((ListenerCallQueue) this.listeners.get(i)).execute();
            }
        }
    }

    @GuardedBy("monitor")
    private void starting() {
        STARTING_CALLBACK.enqueueOn(this.listeners);
    }

    @GuardedBy("monitor")
    private void running() {
        RUNNING_CALLBACK.enqueueOn(this.listeners);
    }

    @GuardedBy("monitor")
    private void stopping(State state) {
        if (state == State.STARTING) {
            STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
        } else if (state == State.RUNNING) {
            STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
        } else {
            throw new AssertionError();
        }
    }

    @GuardedBy("monitor")
    private void terminated(State state) {
        state = AnonymousClass10.$SwitchMap$com$google$common$util$concurrent$Service$State[state.ordinal()];
        if (state != 1) {
            switch (state) {
                case 3:
                    TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
                    return;
                case 4:
                    TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
                    return;
                default:
                    throw new AssertionError();
            }
        }
        TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
    }

    @GuardedBy("monitor")
    private void failed(final State state, final Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("failed({from = ");
        stringBuilder.append(state);
        stringBuilder.append(", cause = ");
        stringBuilder.append(th);
        stringBuilder.append("})");
        new Callback<Listener>(stringBuilder.toString()) {
            void call(Listener listener) {
                listener.failed(state, th);
            }
        }.enqueueOn(this.listeners);
    }
}
