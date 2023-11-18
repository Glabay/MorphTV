package org.threeten.bp.temporal;

public interface TemporalAdjuster {
    Temporal adjustInto(Temporal temporal);
}
