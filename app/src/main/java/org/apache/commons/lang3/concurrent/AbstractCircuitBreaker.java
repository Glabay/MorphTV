package org.apache.commons.lang3.concurrent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCircuitBreaker<T> implements CircuitBreaker<T> {
    public static final String PROPERTY_NAME = "open";
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    protected final AtomicReference<State> state = new AtomicReference(State.CLOSED);

    protected enum State {
        CLOSED {
            public State oppositeState() {
                return OPEN;
            }
        },
        OPEN {
            public State oppositeState() {
                return CLOSED;
            }
        };

        public abstract State oppositeState();
    }

    public abstract boolean checkState();

    public abstract boolean incrementAndCheckState(T t);

    public boolean isOpen() {
        return isOpen((State) this.state.get());
    }

    public boolean isClosed() {
        return isOpen() ^ 1;
    }

    public void close() {
        changeState(State.CLOSED);
    }

    public void open() {
        changeState(State.OPEN);
    }

    protected static boolean isOpen(State state) {
        return state == State.OPEN ? true : null;
    }

    protected void changeState(State state) {
        if (this.state.compareAndSet(state.oppositeState(), state)) {
            this.changeSupport.firePropertyChange(PROPERTY_NAME, isOpen(state) ^ 1, isOpen(state));
        }
    }

    public void addChangeListener(PropertyChangeListener propertyChangeListener) {
        this.changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public void removeChangeListener(PropertyChangeListener propertyChangeListener) {
        this.changeSupport.removePropertyChangeListener(propertyChangeListener);
    }
}
