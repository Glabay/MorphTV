package org.apache.commons.lang3.concurrent;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class EventCountCircuitBreaker extends AbstractCircuitBreaker<Integer> {
    private static final Map<State, StateStrategy> STRATEGY_MAP = createStrategyMap();
    private final AtomicReference<CheckIntervalData> checkIntervalData;
    private final long closingInterval;
    private final int closingThreshold;
    private final long openingInterval;
    private final int openingThreshold;

    private static class CheckIntervalData {
        private final long checkIntervalStart;
        private final int eventCount;

        public CheckIntervalData(int i, long j) {
            this.eventCount = i;
            this.checkIntervalStart = j;
        }

        public int getEventCount() {
            return this.eventCount;
        }

        public long getCheckIntervalStart() {
            return this.checkIntervalStart;
        }

        public CheckIntervalData increment(int i) {
            return i != 0 ? new CheckIntervalData(getEventCount() + i, getCheckIntervalStart()) : this;
        }
    }

    private static abstract class StateStrategy {
        protected abstract long fetchCheckInterval(EventCountCircuitBreaker eventCountCircuitBreaker);

        public abstract boolean isStateTransition(EventCountCircuitBreaker eventCountCircuitBreaker, CheckIntervalData checkIntervalData, CheckIntervalData checkIntervalData2);

        private StateStrategy() {
        }

        public boolean isCheckIntervalFinished(EventCountCircuitBreaker eventCountCircuitBreaker, CheckIntervalData checkIntervalData, long j) {
            return j - checkIntervalData.getCheckIntervalStart() > fetchCheckInterval(eventCountCircuitBreaker) ? true : null;
        }
    }

    private static class StateStrategyClosed extends StateStrategy {
        private StateStrategyClosed() {
            super();
        }

        public boolean isStateTransition(EventCountCircuitBreaker eventCountCircuitBreaker, CheckIntervalData checkIntervalData, CheckIntervalData checkIntervalData2) {
            return checkIntervalData2.getEventCount() > eventCountCircuitBreaker.getOpeningThreshold() ? true : null;
        }

        protected long fetchCheckInterval(EventCountCircuitBreaker eventCountCircuitBreaker) {
            return eventCountCircuitBreaker.getOpeningInterval();
        }
    }

    private static class StateStrategyOpen extends StateStrategy {
        private StateStrategyOpen() {
            super();
        }

        public boolean isStateTransition(EventCountCircuitBreaker eventCountCircuitBreaker, CheckIntervalData checkIntervalData, CheckIntervalData checkIntervalData2) {
            return (checkIntervalData2.getCheckIntervalStart() == checkIntervalData.getCheckIntervalStart() || checkIntervalData.getEventCount() >= eventCountCircuitBreaker.getClosingThreshold()) ? null : true;
        }

        protected long fetchCheckInterval(EventCountCircuitBreaker eventCountCircuitBreaker) {
            return eventCountCircuitBreaker.getClosingInterval();
        }
    }

    public EventCountCircuitBreaker(int i, long j, TimeUnit timeUnit, int i2, long j2, TimeUnit timeUnit2) {
        this.checkIntervalData = new AtomicReference(new CheckIntervalData(0, 0));
        this.openingThreshold = i;
        this.openingInterval = timeUnit.toNanos(j);
        this.closingThreshold = i2;
        this.closingInterval = timeUnit2.toNanos(j2);
    }

    public EventCountCircuitBreaker(int i, long j, TimeUnit timeUnit, int i2) {
        this(i, j, timeUnit, i2, j, timeUnit);
    }

    public EventCountCircuitBreaker(int i, long j, TimeUnit timeUnit) {
        this(i, j, timeUnit, i);
    }

    public int getOpeningThreshold() {
        return this.openingThreshold;
    }

    public long getOpeningInterval() {
        return this.openingInterval;
    }

    public int getClosingThreshold() {
        return this.closingThreshold;
    }

    public long getClosingInterval() {
        return this.closingInterval;
    }

    public boolean checkState() {
        return performStateCheck(0);
    }

    public boolean incrementAndCheckState(Integer num) throws CircuitBreakingException {
        return performStateCheck(1);
    }

    public boolean incrementAndCheckState() {
        return incrementAndCheckState(Integer.valueOf(1));
    }

    public void open() {
        super.open();
        this.checkIntervalData.set(new CheckIntervalData(0, now()));
    }

    public void close() {
        super.close();
        this.checkIntervalData.set(new CheckIntervalData(0, now()));
    }

    private boolean performStateCheck(int i) {
        CheckIntervalData checkIntervalData;
        CheckIntervalData nextCheckIntervalData;
        State state;
        do {
            state = (State) this.state.get();
            checkIntervalData = (CheckIntervalData) this.checkIntervalData.get();
            nextCheckIntervalData = nextCheckIntervalData(i, checkIntervalData, state, now());
        } while (!updateCheckIntervalData(checkIntervalData, nextCheckIntervalData));
        if (stateStrategy(state).isStateTransition(this, checkIntervalData, nextCheckIntervalData) != 0) {
            state = state.oppositeState();
            changeStateAndStartNewCheckInterval(state);
        }
        return AbstractCircuitBreaker.isOpen(state) ^ 1;
    }

    private boolean updateCheckIntervalData(CheckIntervalData checkIntervalData, CheckIntervalData checkIntervalData2) {
        if (checkIntervalData != checkIntervalData2) {
            if (this.checkIntervalData.compareAndSet(checkIntervalData, checkIntervalData2) == null) {
                return null;
            }
        }
        return true;
    }

    private void changeStateAndStartNewCheckInterval(State state) {
        changeState(state);
        this.checkIntervalData.set(new CheckIntervalData(0, now()));
    }

    private CheckIntervalData nextCheckIntervalData(int i, CheckIntervalData checkIntervalData, State state, long j) {
        if (stateStrategy(state).isCheckIntervalFinished(this, checkIntervalData, j) != null) {
            return new CheckIntervalData(i, j);
        }
        return checkIntervalData.increment(i);
    }

    long now() {
        return System.nanoTime();
    }

    private static StateStrategy stateStrategy(State state) {
        return (StateStrategy) STRATEGY_MAP.get(state);
    }

    private static Map<State, StateStrategy> createStrategyMap() {
        Map<State, StateStrategy> enumMap = new EnumMap(State.class);
        enumMap.put(State.CLOSED, new StateStrategyClosed());
        enumMap.put(State.OPEN, new StateStrategyOpen());
        return enumMap;
    }
}
