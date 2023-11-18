package org.threeten.bp;

import com.google.android.exoplayer2.C0649C;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.Era;
import org.threeten.bp.chrono.HijrahDate;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeFormatter;
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
import org.threeten.bp.zone.ZoneOffsetTransition;

public final class LocalDate extends ChronoLocalDate implements Temporal, TemporalAdjuster, Serializable {
    static final long DAYS_0000_TO_1970 = 719528;
    private static final int DAYS_PER_CYCLE = 146097;
    public static final TemporalQuery<LocalDate> FROM = new C15131();
    public static final LocalDate MAX = of((int) Year.MAX_VALUE, 12, 31);
    public static final LocalDate MIN = of((int) Year.MIN_VALUE, 1, 1);
    private static final long serialVersionUID = 2942565459149668126L;
    private final short day;
    private final short month;
    private final int year;

    /* renamed from: org.threeten.bp.LocalDate$1 */
    static class C15131 implements TemporalQuery<LocalDate> {
        C15131() {
        }

        public LocalDate queryFrom(TemporalAccessor temporalAccessor) {
            return LocalDate.from(temporalAccessor);
        }
    }

    public static LocalDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static LocalDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static LocalDate now(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofEpochDay(Jdk8Methods.floorDiv(instant.getEpochSecond() + ((long) clock.getZone().getRules().getOffset(instant).getTotalSeconds()), 86400));
    }

    public static LocalDate of(int i, Month month, int i2) {
        ChronoField.YEAR.checkValidValue((long) i);
        Jdk8Methods.requireNonNull(month, "month");
        ChronoField.DAY_OF_MONTH.checkValidValue((long) i2);
        return create(i, month, i2);
    }

    public static LocalDate of(int i, int i2, int i3) {
        ChronoField.YEAR.checkValidValue((long) i);
        ChronoField.MONTH_OF_YEAR.checkValidValue((long) i2);
        ChronoField.DAY_OF_MONTH.checkValidValue((long) i3);
        return create(i, Month.of(i2), i3);
    }

    public static LocalDate ofYearDay(int i, int i2) {
        long j = (long) i;
        ChronoField.YEAR.checkValidValue(j);
        ChronoField.DAY_OF_YEAR.checkValidValue((long) i2);
        boolean isLeapYear = IsoChronology.INSTANCE.isLeapYear(j);
        if (i2 != 366 || isLeapYear) {
            Month of = Month.of(((i2 - 1) / 31) + 1);
            if (i2 > (of.firstDayOfYear(isLeapYear) + of.length(isLeapYear)) - 1) {
                of = of.plus(1);
            }
            return create(i, of, (i2 - of.firstDayOfYear(isLeapYear)) + 1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid date 'DayOfYear 366' as '");
        stringBuilder.append(i);
        stringBuilder.append("' is not a leap year");
        throw new DateTimeException(stringBuilder.toString());
    }

    public static LocalDate ofEpochDay(long j) {
        long j2;
        long j3;
        long j4 = j;
        ChronoField.EPOCH_DAY.checkValidValue(j4);
        long j5 = (j4 + DAYS_0000_TO_1970) - 60;
        if (j5 < 0) {
            long j6 = ((j5 + 1) / 146097) - 1;
            j2 = j6 * 400;
            j3 = j5 + ((-j6) * 146097);
        } else {
            j2 = 0;
            j3 = j5;
        }
        long j7 = ((j3 * 400) + 591) / 146097;
        long j8 = j3 - ((((j7 * 365) + (j7 / 4)) - (j7 / 100)) + (j7 / 400));
        if (j8 < 0) {
            j4 = j7 - 1;
            j8 = j3 - ((((365 * j4) + (j4 / 4)) - (j4 / 100)) + (j4 / 400));
        } else {
            j4 = j7;
        }
        long j9 = j4 + j2;
        int i = (int) j8;
        int i2 = ((i * 5) + 2) / 153;
        return new LocalDate(ChronoField.YEAR.checkValidIntValue(j9 + ((long) (i2 / 10))), ((i2 + 2) % 12) + 1, (i - (((i2 * 306) + 5) / 10)) + 1);
    }

    public static LocalDate from(TemporalAccessor temporalAccessor) {
        LocalDate localDate = (LocalDate) temporalAccessor.query(TemporalQueries.localDate());
        if (localDate != null) {
            return localDate;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to obtain LocalDate from TemporalAccessor: ");
        stringBuilder.append(temporalAccessor);
        stringBuilder.append(", type ");
        stringBuilder.append(temporalAccessor.getClass().getName());
        throw new DateTimeException(stringBuilder.toString());
    }

    public static LocalDate parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDate parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (LocalDate) dateTimeFormatter.parse(charSequence, FROM);
    }

    private static LocalDate create(int i, Month month, int i2) {
        if (i2 <= 28 || i2 <= month.length(IsoChronology.INSTANCE.isLeapYear((long) i))) {
            return new LocalDate(i, month.getValue(), i2);
        }
        if (i2 == 29) {
            i2 = new StringBuilder();
            i2.append("Invalid date 'February 29' as '");
            i2.append(i);
            i2.append("' is not a leap year");
            throw new DateTimeException(i2.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid date '");
        stringBuilder.append(month.name());
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(i2);
        stringBuilder.append("'");
        throw new DateTimeException(stringBuilder.toString());
    }

    private static LocalDate resolvePreviousValid(int i, int i2, int i3) {
        if (i2 == 2) {
            i3 = Math.min(i3, IsoChronology.INSTANCE.isLeapYear((long) i) ? 29 : 28);
        } else if (i2 == 4 || i2 == 6 || i2 == 9 || i2 == 11) {
            i3 = Math.min(i3, 30);
        }
        return of(i, i2, i3);
    }

    private LocalDate(int i, int i2, int i3) {
        this.year = i;
        this.month = (short) i2;
        this.day = (short) i3;
    }

    public boolean isSupported(TemporalField temporalField) {
        return super.isSupported(temporalField);
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        if (chronoField.isDateBased()) {
            switch (chronoField) {
                case DAY_OF_MONTH:
                    return ValueRange.of(1, (long) lengthOfMonth());
                case DAY_OF_YEAR:
                    return ValueRange.of(1, (long) lengthOfYear());
                case ALIGNED_WEEK_OF_MONTH:
                    long j = (getMonth() == Month.FEBRUARY && isLeapYear() == null) ? 4 : 5;
                    return ValueRange.of(1, j);
                case YEAR_OF_ERA:
                    return ValueRange.of(1, getYear() <= null ? C0649C.NANOS_PER_SECOND : 999999999);
                default:
                    return temporalField.range();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public int get(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return get0(temporalField);
        }
        return super.get(temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        if (temporalField == ChronoField.EPOCH_DAY) {
            return toEpochDay();
        }
        if (temporalField == ChronoField.PROLEPTIC_MONTH) {
            return getProlepticMonth();
        }
        return (long) get0(temporalField);
    }

    private int get0(TemporalField temporalField) {
        int i = 1;
        StringBuilder stringBuilder;
        switch ((ChronoField) temporalField) {
            case DAY_OF_MONTH:
                return this.day;
            case DAY_OF_YEAR:
                return getDayOfYear();
            case ALIGNED_WEEK_OF_MONTH:
                return ((this.day - 1) / 7) + 1;
            case YEAR_OF_ERA:
                return this.year >= 1 ? this.year : 1 - this.year;
            case DAY_OF_WEEK:
                return getDayOfWeek().getValue();
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                return ((this.day - 1) % 7) + 1;
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                return ((getDayOfYear() - 1) % 7) + 1;
            case EPOCH_DAY:
                stringBuilder = new StringBuilder();
                stringBuilder.append("Field too large for an int: ");
                stringBuilder.append(temporalField);
                throw new DateTimeException(stringBuilder.toString());
            case ALIGNED_WEEK_OF_YEAR:
                return ((getDayOfYear() - 1) / 7) + 1;
            case MONTH_OF_YEAR:
                return this.month;
            case PROLEPTIC_MONTH:
                stringBuilder = new StringBuilder();
                stringBuilder.append("Field too large for an int: ");
                stringBuilder.append(temporalField);
                throw new DateTimeException(stringBuilder.toString());
            case YEAR:
                return this.year;
            case ERA:
                if (this.year < 1) {
                    i = 0;
                }
                return i;
            default:
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    private long getProlepticMonth() {
        return (((long) this.year) * 12) + ((long) (this.month - 1));
    }

    public IsoChronology getChronology() {
        return IsoChronology.INSTANCE;
    }

    public Era getEra() {
        return super.getEra();
    }

    public int getYear() {
        return this.year;
    }

    public int getMonthValue() {
        return this.month;
    }

    public Month getMonth() {
        return Month.of(this.month);
    }

    public int getDayOfMonth() {
        return this.day;
    }

    public int getDayOfYear() {
        return (getMonth().firstDayOfYear(isLeapYear()) + this.day) - 1;
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.of(Jdk8Methods.floorMod(toEpochDay() + 3, 7) + 1);
    }

    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear((long) this.year);
    }

    public int lengthOfMonth() {
        short s = this.month;
        if (s != (short) 2) {
            return (s == (short) 4 || s == (short) 6 || s == (short) 9 || s == (short) 11) ? 30 : 31;
        } else {
            return isLeapYear() ? 29 : 28;
        }
    }

    public int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    public LocalDate with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return (LocalDate) temporalAdjuster;
        }
        return (LocalDate) temporalAdjuster.adjustInto(this);
    }

    public LocalDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (LocalDate) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        chronoField.checkValidValue(j);
        switch (chronoField) {
            case DAY_OF_MONTH:
                return withDayOfMonth((int) j);
            case DAY_OF_YEAR:
                return withDayOfYear((int) j);
            case ALIGNED_WEEK_OF_MONTH:
                return plusWeeks(j - getLong(ChronoField.ALIGNED_WEEK_OF_MONTH));
            case YEAR_OF_ERA:
                if (this.year < 1) {
                    j = 1 - j;
                }
                return withYear((int) j);
            case DAY_OF_WEEK:
                return plusDays(j - ((long) getDayOfWeek().getValue()));
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            case EPOCH_DAY:
                return ofEpochDay(j);
            case ALIGNED_WEEK_OF_YEAR:
                return plusWeeks(j - getLong(ChronoField.ALIGNED_WEEK_OF_YEAR));
            case MONTH_OF_YEAR:
                return withMonth((int) j);
            case PROLEPTIC_MONTH:
                return plusMonths(j - getLong(ChronoField.PROLEPTIC_MONTH));
            case YEAR:
                return withYear((int) j);
            case ERA:
                return getLong(ChronoField.ERA) == j ? this : withYear(1 - this.year);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public LocalDate withYear(int i) {
        if (this.year == i) {
            return this;
        }
        ChronoField.YEAR.checkValidValue((long) i);
        return resolvePreviousValid(i, this.month, this.day);
    }

    public LocalDate withMonth(int i) {
        if (this.month == i) {
            return this;
        }
        ChronoField.MONTH_OF_YEAR.checkValidValue((long) i);
        return resolvePreviousValid(this.year, i, this.day);
    }

    public LocalDate withDayOfMonth(int i) {
        if (this.day == i) {
            return this;
        }
        return of(this.year, this.month, i);
    }

    public LocalDate withDayOfYear(int i) {
        if (getDayOfYear() == i) {
            return this;
        }
        return ofYearDay(this.year, i);
    }

    public LocalDate plus(TemporalAmount temporalAmount) {
        return (LocalDate) temporalAmount.addTo(this);
    }

    public LocalDate plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (LocalDate) temporalUnit.addTo(this, j);
        }
        switch ((ChronoUnit) temporalUnit) {
            case DAYS:
                return plusDays(j);
            case WEEKS:
                return plusWeeks(j);
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
            case ERAS:
                return with(ChronoField.ERA, Jdk8Methods.safeAdd(getLong(ChronoField.ERA), j));
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public LocalDate plusYears(long j) {
        if (j == 0) {
            return this;
        }
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(((long) this.year) + j), this.month, this.day);
    }

    public LocalDate plusMonths(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = ((((long) this.year) * 12) + ((long) (this.month - 1))) + j;
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(Jdk8Methods.floorDiv(j2, 12)), Jdk8Methods.floorMod(j2, 12) + 1, this.day);
    }

    public LocalDate plusWeeks(long j) {
        return plusDays(Jdk8Methods.safeMultiply(j, 7));
    }

    public LocalDate plusDays(long j) {
        if (j == 0) {
            return this;
        }
        return ofEpochDay(Jdk8Methods.safeAdd(toEpochDay(), j));
    }

    public LocalDate minus(TemporalAmount temporalAmount) {
        return (LocalDate) temporalAmount.subtractFrom(this);
    }

    public LocalDate minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public LocalDate minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    public LocalDate minusMonths(long j) {
        return j == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-j);
    }

    public LocalDate minusWeeks(long j) {
        return j == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-j);
    }

    public LocalDate minusDays(long j) {
        return j == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-j);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return this;
        }
        return super.query(temporalQuery);
    }

    public Temporal adjustInto(Temporal temporal) {
        return super.adjustInto(temporal);
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        switch ((ChronoUnit) temporalUnit) {
            case DAYS:
                return daysUntil(temporal);
            case WEEKS:
                return daysUntil(temporal) / 7;
            case MONTHS:
                return monthsUntil(temporal);
            case YEARS:
                return monthsUntil(temporal) / 12;
            case DECADES:
                return monthsUntil(temporal) / 120;
            case CENTURIES:
                return monthsUntil(temporal) / 1200;
            case MILLENNIA:
                return monthsUntil(temporal) / 12000;
            case ERAS:
                return temporal.getLong(ChronoField.ERA) - getLong(ChronoField.ERA);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    long daysUntil(LocalDate localDate) {
        return localDate.toEpochDay() - toEpochDay();
    }

    private long monthsUntil(LocalDate localDate) {
        return (((localDate.getProlepticMonth() * 32) + ((long) localDate.getDayOfMonth())) - ((getProlepticMonth() * 32) + ((long) getDayOfMonth()))) / 32;
    }

    public Period until(ChronoLocalDate chronoLocalDate) {
        long j;
        chronoLocalDate = from(chronoLocalDate);
        long prolepticMonth = chronoLocalDate.getProlepticMonth() - getProlepticMonth();
        int i = chronoLocalDate.day - this.day;
        if (prolepticMonth > 0 && i < 0) {
            j = prolepticMonth - 1;
            chronoLocalDate = (int) (chronoLocalDate.toEpochDay() - plusMonths(j).toEpochDay());
        } else if (prolepticMonth >= 0 || i <= 0) {
            chronoLocalDate = i;
            j = prolepticMonth;
        } else {
            chronoLocalDate = i - chronoLocalDate.lengthOfMonth();
            j = prolepticMonth + 1;
        }
        prolepticMonth = j / 12;
        return Period.of(Jdk8Methods.safeToInt(prolepticMonth), (int) (j % 12), chronoLocalDate);
    }

    public LocalDateTime atTime(LocalTime localTime) {
        return LocalDateTime.of(this, localTime);
    }

    public LocalDateTime atTime(int i, int i2) {
        return atTime(LocalTime.of(i, i2));
    }

    public LocalDateTime atTime(int i, int i2, int i3) {
        return atTime(LocalTime.of(i, i2, i3));
    }

    public LocalDateTime atTime(int i, int i2, int i3, int i4) {
        return atTime(LocalTime.of(i, i2, i3, i4));
    }

    public OffsetDateTime atTime(OffsetTime offsetTime) {
        return OffsetDateTime.of(LocalDateTime.of(this, offsetTime.toLocalTime()), offsetTime.getOffset());
    }

    public LocalDateTime atStartOfDay() {
        return LocalDateTime.of(this, LocalTime.MIDNIGHT);
    }

    public ZonedDateTime atStartOfDay(ZoneId zoneId) {
        Jdk8Methods.requireNonNull(zoneId, "zone");
        LocalDateTime atTime = atTime(LocalTime.MIDNIGHT);
        if (!(zoneId instanceof ZoneOffset)) {
            ZoneOffsetTransition transition = zoneId.getRules().getTransition(atTime);
            if (transition != null && transition.isGap()) {
                atTime = transition.getDateTimeAfter();
            }
        }
        return ZonedDateTime.of(atTime, zoneId);
    }

    public long toEpochDay() {
        long j = (long) this.year;
        long j2 = (long) this.month;
        long j3 = (365 * j) + 0;
        if (j >= 0) {
            j = j3 + ((((j + 3) / 4) - ((j + 99) / 100)) + ((j + 399) / 400));
        } else {
            j = j3 - (((j / -4) - (j / -100)) + (j / -400));
        }
        long j4 = (j + (((367 * j2) - 362) / 12)) + ((long) (this.day - 1));
        if (j2 > 2) {
            j2 = j4 - 1;
            j4 = !isLeapYear() ? j2 - 1 : j2;
        }
        return j4 - DAYS_0000_TO_1970;
    }

    public int compareTo(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate);
        }
        return super.compareTo(chronoLocalDate);
    }

    int compareTo0(LocalDate localDate) {
        int i = this.year - localDate.year;
        if (i != 0) {
            return i;
        }
        i = this.month - localDate.month;
        return i == 0 ? this.day - localDate.day : i;
    }

    public boolean isAfter(ChronoLocalDate chronoLocalDate) {
        if (!(chronoLocalDate instanceof LocalDate)) {
            return super.isAfter(chronoLocalDate);
        }
        return compareTo0((LocalDate) chronoLocalDate) > null ? true : null;
    }

    public boolean isBefore(ChronoLocalDate chronoLocalDate) {
        if (!(chronoLocalDate instanceof LocalDate)) {
            return super.isBefore(chronoLocalDate);
        }
        return compareTo0((LocalDate) chronoLocalDate) < null ? true : null;
    }

    public boolean isEqual(ChronoLocalDate chronoLocalDate) {
        if (!(chronoLocalDate instanceof LocalDate)) {
            return super.isEqual(chronoLocalDate);
        }
        return compareTo0((LocalDate) chronoLocalDate) == null ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalDate)) {
            return false;
        }
        if (compareTo0((LocalDate) obj) != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i = this.year;
        return (((i << 11) + (this.month << 6)) + this.day) ^ (i & -2048);
    }

    public String toString() {
        int i = this.year;
        short s = this.month;
        short s2 = this.day;
        int abs = Math.abs(i);
        StringBuilder stringBuilder = new StringBuilder(10);
        if (abs >= 1000) {
            if (i > HijrahDate.MAX_VALUE_OF_ERA) {
                stringBuilder.append('+');
            }
            stringBuilder.append(i);
        } else if (i < 0) {
            stringBuilder.append(i - 10000);
            stringBuilder.deleteCharAt(1);
        } else {
            stringBuilder.append(i + 10000);
            stringBuilder.deleteCharAt(0);
        }
        stringBuilder.append(s < (short) 10 ? "-0" : "-");
        stringBuilder.append(s);
        stringBuilder.append(s2 < (short) 10 ? "-0" : "-");
        stringBuilder.append(s2);
        return stringBuilder.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        return super.format(dateTimeFormatter);
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeByte(this.month);
        dataOutput.writeByte(this.day);
    }

    static LocalDate readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
