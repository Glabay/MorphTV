package org.threeten.bp.chrono;

import java.io.Serializable;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalUnit;

abstract class ChronoDateImpl<D extends ChronoLocalDate> extends ChronoLocalDate implements Temporal, TemporalAdjuster, Serializable {
    private static final long serialVersionUID = 6282433883239719096L;

    abstract ChronoDateImpl<D> plusDays(long j);

    abstract ChronoDateImpl<D> plusMonths(long j);

    abstract ChronoDateImpl<D> plusYears(long j);

    ChronoDateImpl() {
    }

    public ChronoDateImpl<D> plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (ChronoDateImpl) getChronology().ensureChronoLocalDate(temporalUnit.addTo(this, j));
        }
        switch ((ChronoUnit) temporalUnit) {
            case DAYS:
                return plusDays(j);
            case WEEKS:
                return plusDays(Jdk8Methods.safeMultiply(j, 7));
            case MONTHS:
                return plusMonths(j);
            case YEARS:
                return plusYears(j);
            case DECADES:
                return plusYears(Jdk8Methods.safeMultiply(j, 10));
            case CENTURIES:
                return plusYears(Jdk8Methods.safeMultiply(j, 100));
            case MILLENNIA:
                return plusYears(Jdk8Methods.safeMultiply(j, 1000));
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(temporalUnit);
                stringBuilder.append(" not valid for chronology ");
                stringBuilder.append(getChronology().getId());
                throw new DateTimeException(stringBuilder.toString());
        }
    }

    ChronoDateImpl<D> plusWeeks(long j) {
        return plusDays(Jdk8Methods.safeMultiply(j, 7));
    }

    ChronoDateImpl<D> minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    ChronoDateImpl<D> minusMonths(long j) {
        return j == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-j);
    }

    ChronoDateImpl<D> minusWeeks(long j) {
        return j == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-j);
    }

    ChronoDateImpl<D> minusDays(long j) {
        return j == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-j);
    }

    public ChronoLocalDateTime<?> atTime(LocalTime localTime) {
        return ChronoLocalDateTimeImpl.of(this, localTime);
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = getChronology().date(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            return LocalDate.from(this).until(temporal, temporalUnit);
        }
        return temporalUnit.between(this, temporal);
    }

    public ChronoPeriod until(ChronoLocalDate chronoLocalDate) {
        throw new UnsupportedOperationException("Not supported in ThreeTen backport");
    }
}
