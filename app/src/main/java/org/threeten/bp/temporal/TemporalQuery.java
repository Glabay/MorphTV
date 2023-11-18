package org.threeten.bp.temporal;

public interface TemporalQuery<R> {
    R queryFrom(TemporalAccessor temporalAccessor);
}
