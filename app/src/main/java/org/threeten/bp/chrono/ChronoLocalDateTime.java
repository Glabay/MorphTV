package org.threeten.bp.chrono;

import java.util.Comparator;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.jdk8.DefaultInterfaceTemporal;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.TemporalUnit;

public abstract class ChronoLocalDateTime<D extends ChronoLocalDate> extends DefaultInterfaceTemporal implements Temporal, TemporalAdjuster, Comparable<ChronoLocalDateTime<?>> {
    private static final Comparator<ChronoLocalDateTime<?>> DATE_TIME_COMPARATOR = new C15391();

    /* renamed from: org.threeten.bp.chrono.ChronoLocalDateTime$1 */
    static class C15391 implements Comparator<ChronoLocalDateTime<?>> {
        C15391() {
        }

        public int compare(ChronoLocalDateTime<?> chronoLocalDateTime, ChronoLocalDateTime<?> chronoLocalDateTime2) {
            int compareLongs = Jdk8Methods.compareLongs(chronoLocalDateTime.toLocalDate().toEpochDay(), chronoLocalDateTime2.toLocalDate().toEpochDay());
            return compareLongs == 0 ? Jdk8Methods.compareLongs(chronoLocalDateTime.toLocalTime().toNanoOfDay(), chronoLocalDateTime2.toLocalTime().toNanoOfDay()) : compareLongs;
        }
    }

    public abstract ChronoZonedDateTime<D> atZone(ZoneId zoneId);

    public abstract ChronoLocalDateTime<D> plus(long j, TemporalUnit temporalUnit);

    public abstract D toLocalDate();

    public abstract LocalTime toLocalTime();

    public abstract ChronoLocalDateTime<D> with(TemporalField temporalField, long j);

    public static Comparator<ChronoLocalDateTime<?>> timeLineOrder() {
        return DATE_TIME_COMPARATOR;
    }

    public static ChronoLocalDateTime<?> from(TemporalAccessor temporalAccessor) {
        Jdk8Methods.requireNonNull(temporalAccessor, "temporal");
        if (temporalAccessor instanceof ChronoLocalDateTime) {
            return (ChronoLocalDateTime) temporalAccessor;
        }
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null) {
            return chronology.localDateTime(temporalAccessor);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No Chronology found to create ChronoLocalDateTime: ");
        stringBuilder.append(temporalAccessor.getClass());
        throw new DateTimeException(stringBuilder.toString());
    }

    public Chronology getChronology() {
        return toLocalDate().getChronology();
    }

    public ChronoLocalDateTime<D> with(TemporalAdjuster temporalAdjuster) {
        return toLocalDate().getChronology().ensureChronoLocalDateTime(super.with(temporalAdjuster));
    }

    public ChronoLocalDateTime<D> plus(TemporalAmount temporalAmount) {
        return toLocalDate().getChronology().ensureChronoLocalDateTime(super.plus(temporalAmount));
    }

    public ChronoLocalDateTime<D> minus(TemporalAmount temporalAmount) {
        return toLocalDate().getChronology().ensureChronoLocalDateTime(super.minus(temporalAmount));
    }

    public ChronoLocalDateTime<D> minus(long j, TemporalUnit temporalUnit) {
        return toLocalDate().getChronology().ensureChronoLocalDateTime(super.minus(j, temporalUnit));
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.NANOS;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            return LocalDate.ofEpochDay(toLocalDate().toEpochDay());
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return toLocalTime();
        }
        if (!(temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId())) {
            if (temporalQuery != TemporalQueries.offset()) {
                return super.query(temporalQuery);
            }
        }
        return null;
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, toLocalDate().toEpochDay()).with(ChronoField.NANO_OF_DAY, toLocalTime().toNanoOfDay());
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public Instant toInstant(ZoneOffset zoneOffset) {
        return Instant.ofEpochSecond(toEpochSecond(zoneOffset), (long) toLocalTime().getNano());
    }

    public long toEpochSecond(ZoneOffset zoneOffset) {
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        return ((toLocalDate().toEpochDay() * 86400) + ((long) toLocalTime().toSecondOfDay())) - ((long) zoneOffset.getTotalSeconds());
    }

    public int compareTo(ChronoLocalDateTime<?> chronoLocalDateTime) {
        int compareTo = toLocalDate().compareTo(chronoLocalDateTime.toLocalDate());
        if (compareTo != 0) {
            return compareTo;
        }
        compareTo = toLocalTime().compareTo(chronoLocalDateTime.toLocalTime());
        return compareTo == 0 ? getChronology().compareTo(chronoLocalDateTime.getChronology()) : compareTo;
    }

    public boolean isAfter(ChronoLocalDateTime<?> chronoLocalDateTime) {
        long toEpochDay = toLocalDate().toEpochDay();
        long toEpochDay2 = chronoLocalDateTime.toLocalDate().toEpochDay();
        if (toEpochDay <= toEpochDay2) {
            if (toEpochDay != toEpochDay2 || toLocalTime().toNanoOfDay() <= chronoLocalDateTime.toLocalTime().toNanoOfDay()) {
                return null;
            }
        }
        return true;
    }

    public boolean isBefore(ChronoLocalDateTime<?> chronoLocalDateTime) {
        long toEpochDay = toLocalDate().toEpochDay();
        long toEpochDay2 = chronoLocalDateTime.toLocalDate().toEpochDay();
        if (toEpochDay >= toEpochDay2) {
            if (toEpochDay != toEpochDay2 || toLocalTime().toNanoOfDay() >= chronoLocalDateTime.toLocalTime().toNanoOfDay()) {
                return null;
            }
        }
        return true;
    }

    public boolean isEqual(ChronoLocalDateTime<?> chronoLocalDateTime) {
        return (toLocalTime().toNanoOfDay() == chronoLocalDateTime.toLocalTime().toNanoOfDay() && toLocalDate().toEpochDay() == chronoLocalDateTime.toLocalDate().toEpochDay()) ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChronoLocalDateTime)) {
            return false;
        }
        if (compareTo((ChronoLocalDateTime) obj) != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return toLocalDate().hashCode() ^ toLocalTime().hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toLocalDate().toString());
        stringBuilder.append('T');
        stringBuilder.append(toLocalTime().toString());
        return stringBuilder.toString();
    }
}
