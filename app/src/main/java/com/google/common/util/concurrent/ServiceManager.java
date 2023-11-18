package com.google.common.util.concurrent;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSetMultimap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.util.concurrent.Monitor.Guard;
import com.google.common.util.concurrent.Service.State;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class ServiceManager {
    private static final Callback<Listener> HEALTHY_CALLBACK = new Callback<Listener>("healthy()") {
        void call(Listener listener) {
            listener.healthy();
        }
    };
    private static final Callback<Listener> STOPPED_CALLBACK = new Callback<Listener>("stopped()") {
        void call(Listener listener) {
            listener.stopped();
        }
    };
    private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
    private final ImmutableList<Service> services;
    private final ServiceManagerState state;

    private static final class EmptyServiceManagerWarning extends Throwable {
        private EmptyServiceManagerWarning() {
        }
    }

    @Beta
    public static abstract class Listener {
        public void failure(Service service) {
        }

        public void healthy() {
        }

        public void stopped() {
        }
    }

    private static final class NoOpService extends AbstractService {
        private NoOpService() {
        }

        protected void doStart() {
            notifyStarted();
        }

        protected void doStop() {
            notifyStopped();
        }
    }

    private static final class ServiceListener extends com.google.common.util.concurrent.Service.Listener {
        final Service service;
        final WeakReference<ServiceManagerState> state;

        ServiceListener(Service service, WeakReference<ServiceManagerState> weakReference) {
            this.service = service;
            this.state = weakReference;
        }

        public void starting() {
            ServiceManagerState serviceManagerState = (ServiceManagerState) this.state.get();
            if (serviceManagerState != null) {
                serviceManagerState.transitionService(this.service, State.NEW, State.STARTING);
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
                }
            }
        }

        public void running() {
            ServiceManagerState serviceManagerState = (ServiceManagerState) this.state.get();
            if (serviceManagerState != null) {
                serviceManagerState.transitionService(this.service, State.STARTING, State.RUNNING);
            }
        }

        public void stopping(State state) {
            ServiceManagerState serviceManagerState = (ServiceManagerState) this.state.get();
            if (serviceManagerState != null) {
                serviceManagerState.transitionService(this.service, state, State.STOPPING);
            }
        }

        public void terminated(State state) {
            ServiceManagerState serviceManagerState = (ServiceManagerState) this.state.get();
            if (serviceManagerState != null) {
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[]{this.service, state});
                }
                serviceManagerState.transitionService(this.service, state, State.TERMINATED);
            }
        }

        public void failed(State state, Throwable th) {
            ServiceManagerState serviceManagerState = (ServiceManagerState) this.state.get();
            if (serviceManagerState != null) {
                if (!(this.service instanceof NoOpService)) {
                    Logger access$200 = ServiceManager.logger;
                    Level level = Level.SEVERE;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Service ");
                    stringBuilder.append(this.service);
                    stringBuilder.append(" has failed in the ");
                    stringBuilder.append(state);
                    stringBuilder.append(" state.");
                    access$200.log(level, stringBuilder.toString(), th);
                }
                serviceManagerState.transitionService(this.service, state, State.FAILED);
            }
        }
    }

    private static final class ServiceManagerState {
        final Guard awaitHealthGuard = new Guard(this.monitor) {
            public boolean isSatisfied() {
                if (!(ServiceManagerState.this.states.count(State.RUNNING) == ServiceManagerState.this.numberOfServices || ServiceManagerState.this.states.contains(State.STOPPING) || ServiceManagerState.this.states.contains(State.TERMINATED))) {
                    if (!ServiceManagerState.this.states.contains(State.FAILED)) {
                        return false;
                    }
                }
                return true;
            }
        };
        @GuardedBy("monitor")
        final List<ListenerCallQueue<Listener>> listeners = Collections.synchronizedList(new ArrayList());
        final Monitor monitor = new Monitor();
        final int numberOfServices;
        @GuardedBy("monitor")
        boolean ready;
        @GuardedBy("monitor")
        final SetMultimap<State, Service> servicesByState = MultimapBuilder.enumKeys(State.class).linkedHashSetValues().build();
        @GuardedBy("monitor")
        final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
        @GuardedBy("monitor")
        final Multiset<State> states = this.servicesByState.keys();
        final Guard stoppedGuard = new Guard(this.monitor) {
            public boolean isSatisfied() {
                return ServiceManagerState.this.states.count(State.TERMINATED) + ServiceManagerState.this.states.count(State.FAILED) == ServiceManagerState.this.numberOfServices;
            }
        };
        @GuardedBy("monitor")
        boolean transitioned;

        /* renamed from: com.google.common.util.concurrent.ServiceManager$ServiceManagerState$3 */
        class C12503 implements Function<Entry<Service, Long>, Long> {
            C12503() {
            }

            public Long apply(Entry<Service, Long> entry) {
                return (Long) entry.getValue();
            }
        }

        ServiceManagerState(ImmutableCollection<Service> immutableCollection) {
            this.numberOfServices = immutableCollection.size();
            this.servicesByState.putAll(State.NEW, immutableCollection);
        }

        void tryStartTiming(Service service) {
            this.monitor.enter();
            try {
                if (((Stopwatch) this.startupTimers.get(service)) == null) {
                    this.startupTimers.put(service, Stopwatch.createStarted());
                }
                this.monitor.leave();
            } catch (Throwable th) {
                this.monitor.leave();
            }
        }

        void markReady() {
            this.monitor.enter();
            try {
                if (this.transitioned) {
                    List newArrayList = Lists.newArrayList();
                    Iterator it = servicesByState().values().iterator();
                    while (it.hasNext()) {
                        Service service = (Service) it.next();
                        if (service.state() != State.NEW) {
                            newArrayList.add(service);
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Services started transitioning asynchronously before the ServiceManager was constructed: ");
                    stringBuilder.append(newArrayList);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                this.ready = true;
            } finally {
                this.monitor.leave();
            }
        }

        void addListener(Listener listener, Executor executor) {
            Preconditions.checkNotNull(listener, CastExtraArgs.LISTENER);
            Preconditions.checkNotNull(executor, "executor");
            this.monitor.enter();
            try {
                if (!this.stoppedGuard.isSatisfied()) {
                    this.listeners.add(new ListenerCallQueue(listener, executor));
                }
                this.monitor.leave();
            } catch (Throwable th) {
                this.monitor.leave();
            }
        }

        void awaitHealthy() {
            this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
            try {
                checkHealthy();
            } finally {
                this.monitor.leave();
            }
        }

        void awaitHealthy(long j, TimeUnit timeUnit) throws TimeoutException {
            this.monitor.enter();
            try {
                if (this.monitor.waitForUninterruptibly(this.awaitHealthGuard, j, timeUnit) == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Timeout waiting for the services to become healthy. The following services have not started: ");
                    stringBuilder.append(Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(State.NEW, State.STARTING))));
                    throw new TimeoutException(stringBuilder.toString());
                }
                checkHealthy();
            } finally {
                this.monitor.leave();
            }
        }

        void awaitStopped() {
            this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
            this.monitor.leave();
        }

        void awaitStopped(long j, TimeUnit timeUnit) throws TimeoutException {
            this.monitor.enter();
            try {
                if (this.monitor.waitForUninterruptibly(this.stoppedGuard, j, timeUnit) == null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Timeout waiting for the services to stop. The following services have not stopped: ");
                    stringBuilder.append(Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(EnumSet.of(State.TERMINATED, State.FAILED)))));
                    throw new TimeoutException(stringBuilder.toString());
                }
            } finally {
                this.monitor.leave();
            }
        }

        ImmutableMultimap<State, Service> servicesByState() {
            Builder builder = ImmutableSetMultimap.builder();
            this.monitor.enter();
            try {
                for (Entry entry : this.servicesByState.entries()) {
                    if (!(entry.getValue() instanceof NoOpService)) {
                        builder.put(entry.getKey(), entry.getValue());
                    }
                }
                return builder.build();
            } finally {
                this.monitor.leave();
            }
        }

        ImmutableMap<Service, Long> startupTimes() {
            this.monitor.enter();
            try {
                List<Entry> newArrayListWithCapacity = Lists.newArrayListWithCapacity(this.startupTimers.size());
                for (Entry entry : this.startupTimers.entrySet()) {
                    Service service = (Service) entry.getKey();
                    Stopwatch stopwatch = (Stopwatch) entry.getValue();
                    if (!(stopwatch.isRunning() || (service instanceof NoOpService))) {
                        newArrayListWithCapacity.add(Maps.immutableEntry(service, Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS))));
                    }
                }
                Collections.sort(newArrayListWithCapacity, Ordering.natural().onResultOf(new C12503()));
                ImmutableMap.Builder builder = ImmutableMap.builder();
                for (Entry entry2 : newArrayListWithCapacity) {
                    builder.put(entry2);
                }
                return builder.build();
            } finally {
                this.monitor.leave();
            }
        }

        void transitionService(Service service, State state, State state2) {
            Preconditions.checkNotNull(service);
            Preconditions.checkArgument(state != state2);
            this.monitor.enter();
            try {
                this.transitioned = true;
                if (this.ready) {
                    Preconditions.checkState(this.servicesByState.remove(state, service), "Service %s not at the expected location in the state map %s", new Object[]{service, state});
                    Preconditions.checkState(this.servicesByState.put(state2, service), "Service %s in the state map unexpectedly at %s", new Object[]{service, state2});
                    state = (Stopwatch) this.startupTimers.get(service);
                    if (state == null) {
                        state = Stopwatch.createStarted();
                        this.startupTimers.put(service, state);
                    }
                    if (state2.compareTo(State.RUNNING) >= 0 && state.isRunning()) {
                        state.stop();
                        if (!(service instanceof NoOpService)) {
                            ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[]{service, state});
                        }
                    }
                    if (state2 == State.FAILED) {
                        fireFailedListeners(service);
                    }
                    if (this.states.count(State.RUNNING) == this.numberOfServices) {
                        fireHealthyListeners();
                    } else if (this.states.count(State.TERMINATED) + this.states.count(State.FAILED) == this.numberOfServices) {
                        fireStoppedListeners();
                    }
                    this.monitor.leave();
                    executeListeners();
                }
            } finally {
                this.monitor.leave();
                executeListeners();
            }
        }

        @GuardedBy("monitor")
        void fireStoppedListeners() {
            ServiceManager.STOPPED_CALLBACK.enqueueOn(this.listeners);
        }

        @GuardedBy("monitor")
        void fireHealthyListeners() {
            ServiceManager.HEALTHY_CALLBACK.enqueueOn(this.listeners);
        }

        @GuardedBy("monitor")
        void fireFailedListeners(final Service service) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("failed({service=");
            stringBuilder.append(service);
            stringBuilder.append("})");
            new Callback<Listener>(stringBuilder.toString()) {
                void call(Listener listener) {
                    listener.failure(service);
                }
            }.enqueueOn(this.listeners);
        }

        void executeListeners() {
            Preconditions.checkState(this.monitor.isOccupiedByCurrentThread() ^ 1, "It is incorrect to execute listeners with the monitor held.");
            for (int i = 0; i < this.listeners.size(); i++) {
                ((ListenerCallQueue) this.listeners.get(i)).execute();
            }
        }

        @GuardedBy("monitor")
        void checkHealthy() {
            if (this.states.count(State.RUNNING) != this.numberOfServices) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expected to be healthy after starting. The following services are not running: ");
                stringBuilder.append(Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(State.RUNNING))));
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    public ServiceManager(Iterable<? extends Service> iterable) {
        iterable = ImmutableList.copyOf(iterable);
        if (iterable.isEmpty()) {
            logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning());
            iterable = ImmutableList.of(new NoOpService());
        }
        this.state = new ServiceManagerState(iterable);
        this.services = iterable;
        WeakReference weakReference = new WeakReference(this.state);
        iterable = iterable.iterator();
        while (iterable.hasNext()) {
            Service service = (Service) iterable.next();
            service.addListener(new ServiceListener(service, weakReference), MoreExecutors.directExecutor());
            Preconditions.checkArgument(service.state() == State.NEW, "Can only manage NEW services, %s", new Object[]{service});
        }
        this.state.markReady();
    }

    public void addListener(Listener listener, Executor executor) {
        this.state.addListener(listener, executor);
    }

    public void addListener(Listener listener) {
        this.state.addListener(listener, MoreExecutors.directExecutor());
    }

    public ServiceManager startAsync() {
        Iterator it = this.services.iterator();
        while (it.hasNext()) {
            Preconditions.checkState(((Service) it.next()).state() == State.NEW, "Service %s is %s, cannot start it.", new Object[]{(Service) it.next(), ((Service) it.next()).state()});
        }
        it = this.services.iterator();
        while (it.hasNext()) {
            Service service = (Service) it.next();
            try {
                this.state.tryStartTiming(service);
                service.startAsync();
            } catch (Throwable e) {
                Logger logger = logger;
                Level level = Level.WARNING;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to start Service ");
                stringBuilder.append(service);
                logger.log(level, stringBuilder.toString(), e);
            }
        }
        return this;
    }

    public void awaitHealthy() {
        this.state.awaitHealthy();
    }

    public void awaitHealthy(long j, TimeUnit timeUnit) throws TimeoutException {
        this.state.awaitHealthy(j, timeUnit);
    }

    public ServiceManager stopAsync() {
        Iterator it = this.services.iterator();
        while (it.hasNext()) {
            ((Service) it.next()).stopAsync();
        }
        return this;
    }

    public void awaitStopped() {
        this.state.awaitStopped();
    }

    public void awaitStopped(long j, TimeUnit timeUnit) throws TimeoutException {
        this.state.awaitStopped(j, timeUnit);
    }

    public boolean isHealthy() {
        Iterator it = this.services.iterator();
        while (it.hasNext()) {
            if (!((Service) it.next()).isRunning()) {
                return false;
            }
        }
        return true;
    }

    public ImmutableMultimap<State, Service> servicesByState() {
        return this.state.servicesByState();
    }

    public ImmutableMap<Service, Long> startupTimes() {
        return this.state.startupTimes();
    }

    public String toString() {
        return MoreObjects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(this.services, Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString();
    }
}
