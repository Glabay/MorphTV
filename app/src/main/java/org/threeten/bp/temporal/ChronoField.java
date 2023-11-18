package org.threeten.bp.temporal;

import com.google.android.exoplayer2.C0649C;
import java.util.Locale;
import java.util.Map;
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.jdk8.Jdk8Methods;

public enum ChronoField implements TemporalField {
    NANO_OF_SECOND("NanoOfSecond", ChronoUnit.NANOS, ChronoUnit.SECONDS, ValueRange.of(0, 999999999)),
    NANO_OF_DAY("NanoOfDay", ChronoUnit.NANOS, ChronoUnit.DAYS, ValueRange.of(0, 86399999999999L)),
    MICRO_OF_SECOND("MicroOfSecond", ChronoUnit.MICROS, ChronoUnit.SECONDS, ValueRange.of(0, 999999)),
    MICRO_OF_DAY("MicroOfDay", ChronoUnit.MICROS, ChronoUnit.DAYS, ValueRange.of(0, 86399999999L)),
    MILLI_OF_SECOND("MilliOfSecond", ChronoUnit.MILLIS, ChronoUnit.SECONDS, ValueRange.of(0, 999)),
    MILLI_OF_DAY("MilliOfDay", ChronoUnit.MILLIS, ChronoUnit.DAYS, ValueRange.of(0, 86399999)),
    SECOND_OF_MINUTE("SecondOfMinute", ChronoUnit.SECONDS, ChronoUnit.MINUTES, ValueRange.of(0, 59)),
    SECOND_OF_DAY("SecondOfDay", ChronoUnit.SECONDS, ChronoUnit.DAYS, ValueRange.of(0, 86399)),
    MINUTE_OF_HOUR("MinuteOfHour", ChronoUnit.MINUTES, ChronoUnit.HOURS, ValueRange.of(0, 59)),
    MINUTE_OF_DAY("MinuteOfDay", ChronoUnit.MINUTES, ChronoUnit.DAYS, ValueRange.of(0, 1439)),
    HOUR_OF_AMPM("HourOfAmPm", ChronoUnit.HOURS, ChronoUnit.HALF_DAYS, ValueRange.of(0, 11)),
    CLOCK_HOUR_OF_AMPM("ClockHourOfAmPm", ChronoUnit.HOURS, ChronoUnit.HALF_DAYS, ValueRange.of(1, 12)),
    HOUR_OF_DAY("HourOfDay", ChronoUnit.HOURS, ChronoUnit.DAYS, ValueRange.of(0, 23)),
    CLOCK_HOUR_OF_DAY("ClockHourOfDay", ChronoUnit.HOURS, ChronoUnit.DAYS, ValueRange.of(1, 24)),
    AMPM_OF_DAY("AmPmOfDay", ChronoUnit.HALF_DAYS, ChronoUnit.DAYS, ValueRange.of(0, 1)),
    DAY_OF_WEEK("DayOfWeek", ChronoUnit.DAYS, ChronoUnit.WEEKS, ValueRange.of(1, 7)),
    ALIGNED_DAY_OF_WEEK_IN_MONTH("AlignedDayOfWeekInMonth", ChronoUnit.DAYS, ChronoUnit.WEEKS, ValueRange.of(1, 7)),
    ALIGNED_DAY_OF_WEEK_IN_YEAR("AlignedDayOfWeekInYear", ChronoUnit.DAYS, ChronoUnit.WEEKS, ValueRange.of(1, 7)),
    DAY_OF_MONTH("DayOfMonth", ChronoUnit.DAYS, ChronoUnit.MONTHS, ValueRange.of(1, 28, 31)),
    DAY_OF_YEAR("DayOfYear", ChronoUnit.DAYS, ChronoUnit.YEARS, ValueRange.of(1, 365, 366)),
    EPOCH_DAY("EpochDay", ChronoUnit.DAYS, ChronoUnit.FOREVER, ValueRange.of(-365243219162L, 365241780471L)),
    ALIGNED_WEEK_OF_MONTH("AlignedWeekOfMonth", ChronoUnit.WEEKS, ChronoUnit.MONTHS, ValueRange.of(1, 4, 5)),
    ALIGNED_WEEK_OF_YEAR("AlignedWeekOfYear", ChronoUnit.WEEKS, ChronoUnit.YEARS, ValueRange.of(1, 53)),
    MONTH_OF_YEAR("MonthOfYear", ChronoUnit.MONTHS, ChronoUnit.YEARS, ValueRange.of(1, 12)),
    PROLEPTIC_MONTH("ProlepticMonth", ChronoUnit.MONTHS, ChronoUnit.FOREVER, ValueRange.of(-11999999988L, 11999999999L)),
    YEAR_OF_ERA("YearOfEra", ChronoUnit.YEARS, ChronoUnit.FOREVER, ValueRange.of(1, 999999999, C0649C.NANOS_PER_SECOND)),
    YEAR("Year", ChronoUnit.YEARS, ChronoUnit.FOREVER, ValueRange.of(-999999999, 999999999)),
    ERA("Era", ChronoUnit.ERAS, ChronoUnit.FOREVER, ValueRange.of(0, 1)),
    INSTANT_SECONDS("InstantSeconds", ChronoUnit.SECONDS, ChronoUnit.FOREVER, ValueRange.of(Long.MIN_VALUE, Long.MAX_VALUE)),
    OFFSET_SECONDS("OffsetSeconds", ChronoUnit.SECONDS, ChronoUnit.FOREVER, ValueRange.of(-64800, 64800));
    
    private final TemporalUnit baseUnit;
    private final String name;
    private final ValueRange range;
    private final TemporalUnit rangeUnit;

    public TemporalAccessor resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
        return null;
    }

    private ChronoField(String str, TemporalUnit temporalUnit, TemporalUnit temporalUnit2, ValueRange valueRange) {
        this.name = str;
        this.baseUnit = temporalUnit;
        this.rangeUnit = temporalUnit2;
        this.range = valueRange;
    }

    public TemporalUnit getBaseUnit() {
        return this.baseUnit;
    }

    public TemporalUnit getRangeUnit() {
        return this.rangeUnit;
    }

    public ValueRange range() {
        return this.range;
    }

    public boolean isDateBased() {
        return ordinal() >= DAY_OF_WEEK.ordinal() && ordinal() <= ERA.ordinal();
    }

    public boolean isTimeBased() {
        return ordinal() < DAY_OF_WEEK.ordinal();
    }

    public long checkValidValue(long j) {
        return range().checkValidValue(j, this);
    }

    public int checkValidIntValue(long j) {
        return range().checkValidIntValue(j, this);
    }

    public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
        return temporalAccessor.isSupported(this);
    }

    public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
        return temporalAccessor.range(this);
    }

    public long getFrom(TemporalAccessor temporalAccessor) {
        return temporalAccessor.getLong(this);
    }

    public <R extends Temporal> R adjustInto(R r, long j) {
        return r.with(this, j);
    }

    public String getDisplayName(Locale locale) {
        Jdk8Methods.requireNonNull(locale, "locale");
        return toString();
    }

    public String toString() {
        return this.name;
    }
}
