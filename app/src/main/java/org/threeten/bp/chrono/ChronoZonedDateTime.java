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
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;

public abstract class ChronoZonedDateTime<D extends ChronoLocalDate> extends DefaultInterfaceTemporal implements Temporal, Comparable<ChronoZonedDateTime<?>> {
    private static Comparator<ChronoZonedDateTime<?>> INSTANT_COMPARATOR = new C15411();

    /* renamed from: org.threeten.bp.chrono.ChronoZonedDateTime$1 */
    static class C15411 implements Comparator<ChronoZonedDateTime<?>> {
        C15411() {
        }

        public int compare(ChronoZonedDateTime<?> chronoZonedDateTime, ChronoZonedDateTime<?> chronoZonedDateTime2) {
            int compareLongs = Jdk8Methods.compareLongs(chronoZonedDateTime.toEpochSecond(), chronoZonedDateTime2.toEpochSecond());
            return compareLongs == 0 ? Jdk8Methods.compareLongs(chronoZonedDateTime.toLocalTime().toNanoOfDay(), chronoZonedDateTime2.toLocalTime().toNanoOfDay()) : compareLongs;
        }
    }

    public abstract ZoneOffset getOffset();

    public abstract ZoneId getZone();

    public abstract ChronoZonedDateTime<D> plus(long j, TemporalUnit temporalUnit);

    public abstract ChronoLocalDateTime<D> toLocalDateTime();

    public abstract ChronoZonedDateTime<D> with(TemporalField temporalField, long j);

    public abstract ChronoZonedDateTime<D> withEarlierOffsetAtOverlap();

    public abstract ChronoZonedDateTime<D> withLaterOffsetAtOverlap();

    public abstract ChronoZonedDateTime<D> withZoneSameInstant(ZoneId zoneId);

    public abstract ChronoZonedDateTime<D> withZoneSameLocal(ZoneId zoneId);

    public static Comparator<ChronoZonedDateTime<?>> timeLineOrder() {
        return INSTANT_COMPARATOR;
    }

    public static ChronoZonedDateTime<?> from(TemporalAccessor temporalAccessor) {
        Jdk8Methods.requireNonNull(temporalAccessor, "temporal");
        if (temporalAccessor instanceof ChronoZonedDateTime) {
            return (ChronoZonedDateTime) temporalAccessor;
        }
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null) {
            return chronology.zonedDateTime(temporalAccessor);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No Chronology found to create ChronoZonedDateTime: ");
        stringBuilder.append(temporalAccessor.getClass());
        throw new DateTimeException(stringBuilder.toString());
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField != ChronoField.INSTANT_SECONDS) {
            if (temporalField != ChronoField.OFFSET_SECONDS) {
                return toLocalDateTime().range(temporalField);
            }
        }
        return temporalField.range();
    }

    public int get(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return super.get(temporalField);
        }
        switch ((ChronoField) temporalField) {
            case INSTANT_SECONDS:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Field too large for an int: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
            case OFFSET_SECONDS:
                return getOffset().getTotalSeconds();
            default:
                return toLocalDateTime().get(temporalField);
        }
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField) temporalField) {
            case INSTANT_SECONDS:
                return toEpochSecond();
            case OFFSET_SECONDS:
                return (long) getOffset().getTotalSeconds();
            default:
                return toLocalDateTime().getLong(temporalField);
        }
    }

    public D toLocalDate() {
        return toLocalDateTime().toLocalDate();
    }

    public LocalTime toLocalTime() {
        return toLocalDateTime().toLocalTime();
    }

    public Chronology getChronology() {
        return toLocalDate().getChronology();
    }

    public ChronoZonedDateTime<D> with(TemporalAdjuster temporalAdjuster) {
        return toLocalDate().getChronology().ensureChronoZonedDateTime(super.with(temporalAdjuster));
    }

    public ChronoZonedDateTime<D> plus(TemporalAmount temporalAmount) {
        return toLocalDate().getChronology().ensureChronoZonedDateTime(super.plus(temporalAmount));
    }

    public ChronoZonedDateTime<D> minus(TemporalAmount temporalAmount) {
        return toLocalDate().getChronology().ensureChronoZonedDateTime(super.minus(temporalAmount));
    }

    public ChronoZonedDateTime<D> minus(long j, TemporalUnit temporalUnit) {
        return toLocalDate().getChronology().ensureChronoZonedDateTime(super.minus(j, temporalUnit));
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery != TemporalQueries.zoneId()) {
            if (temporalQuery != TemporalQueries.zone()) {
                if (temporalQuery == TemporalQueries.chronology()) {
                    return toLocalDate().getChronology();
                }
                if (temporalQuery == TemporalQueries.precision()) {
                    return ChronoUnit.NANOS;
                }
                if (temporalQuery == TemporalQueries.offset()) {
                    return getOffset();
                }
                if (temporalQuery == TemporalQueries.localDate()) {
                    return LocalDate.ofEpochDay(toLocalDate().toEpochDay());
                }
                if (temporalQuery == TemporalQueries.localTime()) {
                    return toLocalTime();
                }
                return super.query(temporalQuery);
            }
        }
        return getZone();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public Instant toInstant() {
        return Instant.ofEpochSecond(toEpochSecond(), (long) toLocalTime().getNano());
    }

    public long toEpochSecond() {
        return ((toLocalDate().toEpochDay() * 86400) + ((long) toLocalTime().toSecondOfDay())) - ((long) getOffset().getTotalSeconds());
    }

    public int compareTo(ChronoZonedDateTime<?> chronoZonedDateTime) {
        int compareLongs = Jdk8Methods.compareLongs(toEpochSecond(), chronoZonedDateTime.toEpochSecond());
        if (compareLongs != 0) {
            return compareLongs;
        }
        compareLongs = toLocalTime().getNano() - chronoZonedDateTime.toLocalTime().getNano();
        if (compareLongs != 0) {
            return compareLongs;
        }
        compareLongs = toLocalDateTime().compareTo(chronoZonedDateTime.toLocalDateTime());
        if (compareLongs != 0) {
            return compareLongs;
        }
        compareLongs = getZone().getId().compareTo(chronoZonedDateTime.getZone().getId());
        return compareLongs == 0 ? toLocalDate().getChronology().compareTo(chronoZonedDateTime.toLocalDate().getChronology()) : compareLongs;
    }

    public boolean isAfter(ChronoZonedDateTime<?> chronoZonedDateTime) {
        long toEpochSecond = toEpochSecond();
        long toEpochSecond2 = chronoZonedDateTime.toEpochSecond();
        if (toEpochSecond <= toEpochSecond2) {
            if (toEpochSecond != toEpochSecond2 || toLocalTime().getNano() <= chronoZonedDateTime.toLocalTime().getNano()) {
                return null;
            }
        }
        return true;
    }

    public boolean isBefore(ChronoZonedDateTime<?> chronoZonedDateTime) {
        long toEpochSecond = toEpochSecond();
        long toEpochSecond2 = chronoZonedDateTime.toEpochSecond();
        if (toEpochSecond >= toEpochSecond2) {
            if (toEpochSecond != toEpochSecond2 || toLocalTime().getNano() >= chronoZonedDateTime.toLocalTime().getNano()) {
                return null;
            }
        }
        return true;
    }

    public boolean isEqual(ChronoZonedDateTime<?> chronoZonedDateTime) {
        return (toEpochSecond() == chronoZonedDateTime.toEpochSecond() && toLocalTime().getNano() == chronoZonedDateTime.toLocalTime().getNano()) ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChronoZonedDateTime)) {
            return false;
        }
        if (compareTo((ChronoZonedDateTime) obj) != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (toLocalDateTime().hashCode() ^ getOffset().hashCode()) ^ Integer.rotateLeft(getZone().hashCode(), 3);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toLocalDateTime().toString());
        stringBuilder.append(getOffset().toString());
        String stringBuilder2 = stringBuilder.toString();
        if (getOffset() == getZone()) {
            return stringBuilder2;
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append('[');
        stringBuilder3.append(getZone().toString());
        stringBuilder3.append(']');
        return stringBuilder3.toString();
    }
}
