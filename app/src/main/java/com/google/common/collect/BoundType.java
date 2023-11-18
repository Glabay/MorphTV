package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum BoundType {
    OPEN {
        BoundType flip() {
            return CLOSED;
        }
    },
    CLOSED {
        BoundType flip() {
            return OPEN;
        }
    };

    abstract BoundType flip();

    static BoundType forBoolean(boolean z) {
        return z ? CLOSED : OPEN;
    }
}
