package org.threeten.bp;

import com.google.android.exoplayer2.C0649C;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import org.apache.commons.lang3.time.DateUtils;
import org.threeten.bp.chrono.ChronoLocalDateTime;
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

public final class LocalDateTime extends ChronoLocalDateTime<LocalDate> implements Temporal, TemporalAdjuster, Serializable {
    public static final TemporalQuery<LocalDateTime> FROM = new C15151();
    public static final LocalDateTime MAX = of(LocalDate.MAX, LocalTime.MAX);
    public static final LocalDateTime MIN = of(LocalDate.MIN, LocalTime.MIN);
    private static final long serialVersionUID = 6207766400415563566L;
    private final LocalDate date;
    private final LocalTime time;

    /* renamed from: org.threeten.bp.LocalDateTime$1 */
    static class C15151 implements TemporalQuery<LocalDateTime> {
        C15151() {
        }

        public LocalDateTime queryFrom(TemporalAccessor temporalAccessor) {
            return LocalDateTime.from(temporalAccessor);
        }
    }

    public static LocalDateTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static LocalDateTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static LocalDateTime now(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano(), clock.getZone().getRules().getOffset(instant));
    }

    public static LocalDateTime of(int i, Month month, int i2, int i3, int i4) {
        return new LocalDateTime(LocalDate.of(i, month, i2), LocalTime.of(i3, i4));
    }

    public static LocalDateTime of(int i, Month month, int i2, int i3, int i4, int i5) {
        return new LocalDateTime(LocalDate.of(i, month, i2), LocalTime.of(i3, i4, i5));
    }

    public static LocalDateTime of(int i, Month month, int i2, int i3, int i4, int i5, int i6) {
        return new LocalDateTime(LocalDate.of(i, month, i2), LocalTime.of(i3, i4, i5, i6));
    }

    public static LocalDateTime of(int i, int i2, int i3, int i4, int i5) {
        return new LocalDateTime(LocalDate.of(i, i2, i3), LocalTime.of(i4, i5));
    }

    public static LocalDateTime of(int i, int i2, int i3, int i4, int i5, int i6) {
        return new LocalDateTime(LocalDate.of(i, i2, i3), LocalTime.of(i4, i5, i6));
    }

    public static LocalDateTime of(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return new LocalDateTime(LocalDate.of(i, i2, i3), LocalTime.of(i4, i5, i6, i7));
    }

    public static LocalDateTime of(LocalDate localDate, LocalTime localTime) {
        Jdk8Methods.requireNonNull(localDate, "date");
        Jdk8Methods.requireNonNull(localTime, "time");
        return new LocalDateTime(localDate, localTime);
    }

    public static LocalDateTime ofInstant(Instant instant, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(instant, "instant");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano(), zoneId.getRules().getOffset(instant));
    }

    public static LocalDateTime ofEpochSecond(long j, int i, ZoneOffset zoneOffset) {
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        long totalSeconds = j + ((long) zoneOffset.getTotalSeconds());
        return new LocalDateTime(LocalDate.ofEpochDay(Jdk8Methods.floorDiv(totalSeconds, 86400)), LocalTime.ofSecondOfDay((long) Jdk8Methods.floorMod(totalSeconds, 86400), i));
    }

    public static org.threeten.bp.LocalDateTime from(org.threeten.bp.temporal.TemporalAccessor r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r3 instanceof org.threeten.bp.LocalDateTime;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r3 = (org.threeten.bp.LocalDateTime) r3;
        return r3;
    L_0x0007:
        r0 = r3 instanceof org.threeten.bp.ZonedDateTime;
        if (r0 == 0) goto L_0x0012;
    L_0x000b:
        r3 = (org.threeten.bp.ZonedDateTime) r3;
        r3 = r3.toLocalDateTime();
        return r3;
    L_0x0012:
        r0 = org.threeten.bp.LocalDate.from(r3);	 Catch:{ DateTimeException -> 0x0020 }
        r1 = org.threeten.bp.LocalTime.from(r3);	 Catch:{ DateTimeException -> 0x0020 }
        r2 = new org.threeten.bp.LocalDateTime;	 Catch:{ DateTimeException -> 0x0020 }
        r2.<init>(r0, r1);	 Catch:{ DateTimeException -> 0x0020 }
        return r2;
    L_0x0020:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to obtain LocalDateTime from TemporalAccessor: ";
        r1.append(r2);
        r1.append(r3);
        r2 = ", type ";
        r1.append(r2);
        r3 = r3.getClass();
        r3 = r3.getName();
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.LocalDateTime.from(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.LocalDateTime");
    }

    public static LocalDateTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (LocalDateTime) dateTimeFormatter.parse(charSequence, FROM);
    }

    private LocalDateTime(LocalDate localDate, LocalTime localTime) {
        this.date = localDate;
        this.time = localTime;
    }

    private LocalDateTime with(LocalDate localDate, LocalTime localTime) {
        if (this.date == localDate && this.time == localTime) {
            return this;
        }
        return new LocalDateTime(localDate, localTime);
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = true;
        if (temporalField instanceof ChronoField) {
            if (!temporalField.isDateBased()) {
                if (temporalField.isTimeBased() == null) {
                    z = false;
                }
            }
            return z;
        }
        if (temporalField == null || temporalField.isSupportedBy(this) == null) {
            z = false;
        }
        return z;
    }

    public boolean isSupported(TemporalUnit temporalUnit) {
        boolean z = true;
        if (temporalUnit instanceof ChronoUnit) {
            if (!temporalUnit.isDateBased()) {
                if (temporalUnit.isTimeBased() == null) {
                    z = false;
                }
            }
            return z;
        }
        if (temporalUnit == null || temporalUnit.isSupportedBy(this) == null) {
            z = false;
        }
        return z;
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        return temporalField.isTimeBased() ? this.time.range(temporalField) : this.date.range(temporalField);
    }

    public int get(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return super.get(temporalField);
        }
        return temporalField.isTimeBased() ? this.time.get(temporalField) : this.date.get(temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        return temporalField.isTimeBased() ? this.time.getLong(temporalField) : this.date.getLong(temporalField);
    }

    public int getYear() {
        return this.date.getYear();
    }

    public int getMonthValue() {
        return this.date.getMonthValue();
    }

    public Month getMonth() {
        return this.date.getMonth();
    }

    public int getDayOfMonth() {
        return this.date.getDayOfMonth();
    }

    public int getDayOfYear() {
        return this.date.getDayOfYear();
    }

    public DayOfWeek getDayOfWeek() {
        return this.date.getDayOfWeek();
    }

    public int getHour() {
        return this.time.getHour();
    }

    public int getMinute() {
        return this.time.getMinute();
    }

    public int getSecond() {
        return this.time.getSecond();
    }

    public int getNano() {
        return this.time.getNano();
    }

    public LocalDateTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return with((LocalDate) temporalAdjuster, this.time);
        }
        if (temporalAdjuster instanceof LocalTime) {
            return with(this.date, (LocalTime) temporalAdjuster);
        }
        if (temporalAdjuster instanceof LocalDateTime) {
            return (LocalDateTime) temporalAdjuster;
        }
        return (LocalDateTime) temporalAdjuster.adjustInto(this);
    }

    public LocalDateTime with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (LocalDateTime) temporalField.adjustInto(this, j);
        }
        if (temporalField.isTimeBased()) {
            return with(this.date, this.time.with(temporalField, j));
        }
        return with(this.date.with(temporalField, j), this.time);
    }

    public LocalDateTime withYear(int i) {
        return with(this.date.withYear(i), this.time);
    }

    public LocalDateTime withMonth(int i) {
        return with(this.date.withMonth(i), this.time);
    }

    public LocalDateTime withDayOfMonth(int i) {
        return with(this.date.withDayOfMonth(i), this.time);
    }

    public LocalDateTime withDayOfYear(int i) {
        return with(this.date.withDayOfYear(i), this.time);
    }

    public LocalDateTime withHour(int i) {
        return with(this.date, this.time.withHour(i));
    }

    public LocalDateTime withMinute(int i) {
        return with(this.date, this.time.withMinute(i));
    }

    public LocalDateTime withSecond(int i) {
        return with(this.date, this.time.withSecond(i));
    }

    public LocalDateTime withNano(int i) {
        return with(this.date, this.time.withNano(i));
    }

    public LocalDateTime truncatedTo(TemporalUnit temporalUnit) {
        return with(this.date, this.time.truncatedTo(temporalUnit));
    }

    public LocalDateTime plus(TemporalAmount temporalAmount) {
        return (LocalDateTime) temporalAmount.addTo(this);
    }

    public LocalDateTime plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (LocalDateTime) temporalUnit.addTo(this, j);
        }
        switch ((ChronoUnit) temporalUnit) {
            case NANOS:
                return plusNanos(j);
            case MICROS:
                return plusDays(j / 86400000000L).plusNanos((j % 86400000000L) * 1000);
            case MILLIS:
                return plusDays(j / DateUtils.MILLIS_PER_DAY).plusNanos((j % DateUtils.MILLIS_PER_DAY) * C0649C.MICROS_PER_SECOND);
            case SECONDS:
                return plusSeconds(j);
            case MINUTES:
                return plusMinutes(j);
            case HOURS:
                return plusHours(j);
            case HALF_DAYS:
                return plusDays(j / 256).plusHours((j % 256) * 12);
            default:
                return with(this.date.plus(j, temporalUnit), this.time);
        }
    }

    public LocalDateTime plusYears(long j) {
        return with(this.date.plusYears(j), this.time);
    }

    public LocalDateTime plusMonths(long j) {
        return with(this.date.plusMonths(j), this.time);
    }

    public LocalDateTime plusWeeks(long j) {
        return with(this.date.plusWeeks(j), this.time);
    }

    public LocalDateTime plusDays(long j) {
        return with(this.date.plusDays(j), this.time);
    }

    public LocalDateTime plusHours(long j) {
        return plusWithOverflow(this.date, j, 0, 0, 0, 1);
    }

    public LocalDateTime plusMinutes(long j) {
        return plusWithOverflow(this.date, 0, j, 0, 0, 1);
    }

    public LocalDateTime plusSeconds(long j) {
        return plusWithOverflow(this.date, 0, 0, j, 0, 1);
    }

    public LocalDateTime plusNanos(long j) {
        return plusWithOverflow(this.date, 0, 0, 0, j, 1);
    }

    public LocalDateTime minus(TemporalAmount temporalAmount) {
        return (LocalDateTime) temporalAmount.subtractFrom(this);
    }

    public LocalDateTime minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public LocalDateTime minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    public LocalDateTime minusMonths(long j) {
        return j == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-j);
    }

    public LocalDateTime minusWeeks(long j) {
        return j == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-j);
    }

    public LocalDateTime minusDays(long j) {
        return j == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-j);
    }

    public LocalDateTime minusHours(long j) {
        return plusWithOverflow(this.date, j, 0, 0, 0, -1);
    }

    public LocalDateTime minusMinutes(long j) {
        return plusWithOverflow(this.date, 0, j, 0, 0, -1);
    }

    public LocalDateTime minusSeconds(long j) {
        return plusWithOverflow(this.date, 0, 0, j, 0, -1);
    }

    public LocalDateTime minusNanos(long j) {
        return plusWithOverflow(this.date, 0, 0, 0, j, -1);
    }

    private LocalDateTime plusWithOverflow(LocalDate localDate, long j, long j2, long j3, long j4, int i) {
        LocalDateTime localDateTime = this;
        LocalDate localDate2 = localDate;
        if ((((j | j2) | j3) | j4) == 0) {
            return with(localDate2, localDateTime.time);
        }
        long j5 = (long) i;
        long j6 = ((((j4 / 86400000000000L) + (j3 / 86400)) + (j2 / 1440)) + (j / 24)) * j5;
        long j7 = (((j4 % 86400000000000L) + ((j3 % 86400) * C0649C.NANOS_PER_SECOND)) + ((j2 % 1440) * 60000000000L)) + ((j % 24) * 3600000000000L);
        long toNanoOfDay = localDateTime.time.toNanoOfDay();
        long j8 = (j7 * j5) + toNanoOfDay;
        long floorDiv = j6 + Jdk8Methods.floorDiv(j8, 86400000000000L);
        j7 = Jdk8Methods.floorMod(j8, 86400000000000L);
        return with(localDate2.plusDays(floorDiv), j7 == toNanoOfDay ? localDateTime.time : LocalTime.ofNanoOfDay(j7));
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return toLocalDate();
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
        ChronoUnit chronoUnit = (ChronoUnit) temporalUnit;
        if (chronoUnit.isTimeBased()) {
            long j;
            long j2;
            long daysUntil = this.date.daysUntil(temporal.date);
            long toNanoOfDay = temporal.time.toNanoOfDay() - this.time.toNanoOfDay();
            if (daysUntil > 0 && toNanoOfDay < 0) {
                j = daysUntil - 1;
                j2 = toNanoOfDay + 86400000000000L;
            } else if (daysUntil >= 0 || toNanoOfDay <= 0) {
                j = daysUntil;
                j2 = toNanoOfDay;
            } else {
                j = daysUntil + 1;
                j2 = toNanoOfDay - 86400000000000L;
            }
            switch (chronoUnit) {
                case NANOS:
                    return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(j, 86400000000000L), j2);
                case MICROS:
                    return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(j, (long) 500654080), j2 / 1000);
                case MILLIS:
                    return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(j, (long) 86400000), j2 / C0649C.MICROS_PER_SECOND);
                case SECONDS:
                    return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(j, 86400), j2 / C0649C.NANOS_PER_SECOND);
                case MINUTES:
                    return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(j, 1440), j2 / 60000000000L);
                case HOURS:
                    return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(j, 24), j2 / 3600000000000L);
                case HALF_DAYS:
                    return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(j, 2), j2 / 43200000000000L);
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unsupported unit: ");
                    stringBuilder.append(temporalUnit);
                    throw new UnsupportedTemporalTypeException(stringBuilder.toString());
            }
        }
        Temporal temporal2 = temporal.date;
        if (temporal2.isAfter(this.date) && temporal.time.isBefore(this.time)) {
            temporal2 = temporal2.minusDays(1);
        } else if (temporal2.isBefore(this.date) && temporal.time.isAfter(this.time) != null) {
            temporal2 = temporal2.plusDays(1);
        }
        return this.date.until(temporal2, temporalUnit);
    }

    public OffsetDateTime atOffset(ZoneOffset zoneOffset) {
        return OffsetDateTime.of(this, zoneOffset);
    }

    public ZonedDateTime atZone(ZoneId zoneId) {
        return ZonedDateTime.of(this, zoneId);
    }

    public LocalDate toLocalDate() {
        return this.date;
    }

    public LocalTime toLocalTime() {
        return this.time;
    }

    public int compareTo(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return compareTo0((LocalDateTime) chronoLocalDateTime);
        }
        return super.compareTo((ChronoLocalDateTime) chronoLocalDateTime);
    }

    private int compareTo0(LocalDateTime localDateTime) {
        int compareTo0 = this.date.compareTo0(localDateTime.toLocalDate());
        return compareTo0 == 0 ? this.time.compareTo(localDateTime.toLocalTime()) : compareTo0;
    }

    public boolean isAfter(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (!(chronoLocalDateTime instanceof LocalDateTime)) {
            return super.isAfter(chronoLocalDateTime);
        }
        return compareTo0((LocalDateTime) chronoLocalDateTime) > null ? true : null;
    }

    public boolean isBefore(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (!(chronoLocalDateTime instanceof LocalDateTime)) {
            return super.isBefore(chronoLocalDateTime);
        }
        return compareTo0((LocalDateTime) chronoLocalDateTime) < null ? true : null;
    }

    public boolean isEqual(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (!(chronoLocalDateTime instanceof LocalDateTime)) {
            return super.isEqual(chronoLocalDateTime);
        }
        return compareTo0((LocalDateTime) chronoLocalDateTime) == null ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalDateTime)) {
            return false;
        }
        LocalDateTime localDateTime = (LocalDateTime) obj;
        if (!this.date.equals(localDateTime.date) || this.time.equals(localDateTime.time) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.date.hashCode() ^ this.time.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.date.toString());
        stringBuilder.append('T');
        stringBuilder.append(this.time.toString());
        return stringBuilder.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        return super.format(dateTimeFormatter);
    }

    private Object writeReplace() {
        return new Ser((byte) 4, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        this.date.writeExternal(dataOutput);
        this.time.writeExternal(dataOutput);
    }

    static LocalDateTime readExternal(DataInput dataInput) throws IOException {
        return of(LocalDate.readExternal(dataInput), LocalTime.readExternal(dataInput));
    }
}
