package org.threeten.bp.chrono;

import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
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

public abstract class ChronoLocalDate extends DefaultInterfaceTemporal implements Temporal, TemporalAdjuster, Comparable<ChronoLocalDate> {
    private static final Comparator<ChronoLocalDate> DATE_COMPARATOR = new C15381();

    /* renamed from: org.threeten.bp.chrono.ChronoLocalDate$1 */
    static class C15381 implements Comparator<ChronoLocalDate> {
        C15381() {
        }

        public int compare(ChronoLocalDate chronoLocalDate, ChronoLocalDate chronoLocalDate2) {
            return Jdk8Methods.compareLongs(chronoLocalDate.toEpochDay(), chronoLocalDate2.toEpochDay());
        }
    }

    public abstract Chronology getChronology();

    public abstract int lengthOfMonth();

    public abstract ChronoLocalDate plus(long j, TemporalUnit temporalUnit);

    public abstract ChronoPeriod until(ChronoLocalDate chronoLocalDate);

    public abstract ChronoLocalDate with(TemporalField temporalField, long j);

    public static Comparator<ChronoLocalDate> timeLineOrder() {
        return DATE_COMPARATOR;
    }

    public static ChronoLocalDate from(TemporalAccessor temporalAccessor) {
        Jdk8Methods.requireNonNull(temporalAccessor, "temporal");
        if (temporalAccessor instanceof ChronoLocalDate) {
            return (ChronoLocalDate) temporalAccessor;
        }
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null) {
            return chronology.date(temporalAccessor);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No Chronology found to create ChronoLocalDate: ");
        stringBuilder.append(temporalAccessor.getClass());
        throw new DateTimeException(stringBuilder.toString());
    }

    public Era getEra() {
        return getChronology().eraOf(get(ChronoField.ERA));
    }

    public boolean isLeapYear() {
        return getChronology().isLeapYear(getLong(ChronoField.YEAR));
    }

    public int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    public boolean isSupported(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isDateBased();
        }
        temporalField = (temporalField == null || temporalField.isSupportedBy(this) == null) ? null : true;
        return temporalField;
    }

    public boolean isSupported(TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isDateBased();
        }
        temporalUnit = (temporalUnit == null || temporalUnit.isSupportedBy(this) == null) ? null : true;
        return temporalUnit;
    }

    public ChronoLocalDate with(TemporalAdjuster temporalAdjuster) {
        return getChronology().ensureChronoLocalDate(super.with(temporalAdjuster));
    }

    public ChronoLocalDate plus(TemporalAmount temporalAmount) {
        return getChronology().ensureChronoLocalDate(super.plus(temporalAmount));
    }

    public ChronoLocalDate minus(TemporalAmount temporalAmount) {
        return getChronology().ensureChronoLocalDate(super.minus(temporalAmount));
    }

    public ChronoLocalDate minus(long j, TemporalUnit temporalUnit) {
        return getChronology().ensureChronoLocalDate(super.minus(j, temporalUnit));
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.DAYS;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            return LocalDate.ofEpochDay(toEpochDay());
        }
        if (!(temporalQuery == TemporalQueries.localTime() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId())) {
            if (temporalQuery != TemporalQueries.offset()) {
                return super.query(temporalQuery);
            }
        }
        return null;
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, toEpochDay());
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public ChronoLocalDateTime<?> atTime(LocalTime localTime) {
        return ChronoLocalDateTimeImpl.of(this, localTime);
    }

    public long toEpochDay() {
        return getLong(ChronoField.EPOCH_DAY);
    }

    public int compareTo(ChronoLocalDate chronoLocalDate) {
        int compareLongs = Jdk8Methods.compareLongs(toEpochDay(), chronoLocalDate.toEpochDay());
        return compareLongs == 0 ? getChronology().compareTo(chronoLocalDate.getChronology()) : compareLongs;
    }

    public boolean isAfter(ChronoLocalDate chronoLocalDate) {
        return toEpochDay() > chronoLocalDate.toEpochDay() ? true : null;
    }

    public boolean isBefore(ChronoLocalDate chronoLocalDate) {
        return toEpochDay() < chronoLocalDate.toEpochDay() ? true : null;
    }

    public boolean isEqual(ChronoLocalDate chronoLocalDate) {
        return toEpochDay() == chronoLocalDate.toEpochDay() ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChronoLocalDate)) {
            return false;
        }
        if (compareTo((ChronoLocalDate) obj) != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        long toEpochDay = toEpochDay();
        return ((int) (toEpochDay ^ (toEpochDay >>> 32))) ^ getChronology().hashCode();
    }

    public String toString() {
        long j = getLong(ChronoField.YEAR_OF_ERA);
        long j2 = getLong(ChronoField.MONTH_OF_YEAR);
        long j3 = getLong(ChronoField.DAY_OF_MONTH);
        StringBuilder stringBuilder = new StringBuilder(30);
        stringBuilder.append(getChronology().toString());
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(getEra());
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(j);
        stringBuilder.append(j2 < 10 ? "-0" : "-");
        stringBuilder.append(j2);
        stringBuilder.append(j3 < 10 ? "-0" : "-");
        stringBuilder.append(j3);
        return stringBuilder.toString();
    }
}
