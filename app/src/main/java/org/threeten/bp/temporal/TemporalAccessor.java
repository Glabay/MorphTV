package org.threeten.bp.temporal;

public interface TemporalAccessor {
    int get(TemporalField temporalField);

    long getLong(TemporalField temporalField);

    boolean isSupported(TemporalField temporalField);

    <R> R query(TemporalQuery<R> temporalQuery);

    ValueRange range(TemporalField temporalField);
}
