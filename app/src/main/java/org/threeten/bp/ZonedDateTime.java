package org.threeten.bp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;
import org.threeten.bp.chrono.ChronoZonedDateTime;
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
import org.threeten.bp.temporal.ValueRange;
import org.threeten.bp.zone.ZoneOffsetTransition;
import org.threeten.bp.zone.ZoneRules;

public final class ZonedDateTime extends ChronoZonedDateTime<LocalDate> implements Temporal, Serializable {
    public static final TemporalQuery<ZonedDateTime> FROM = new C15351();
    private static final long serialVersionUID = -6260982410461394882L;
    private final LocalDateTime dateTime;
    private final ZoneOffset offset;
    private final ZoneId zone;

    /* renamed from: org.threeten.bp.ZonedDateTime$1 */
    static class C15351 implements TemporalQuery<ZonedDateTime> {
        C15351() {
        }

        public ZonedDateTime queryFrom(TemporalAccessor temporalAccessor) {
            return ZonedDateTime.from(temporalAccessor);
        }
    }

    public static ZonedDateTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static ZonedDateTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static ZonedDateTime now(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        return ofInstant(clock.instant(), clock.getZone());
    }

    public static ZonedDateTime of(LocalDate localDate, LocalTime localTime, ZoneId zoneId) {
        return of(LocalDateTime.of(localDate, localTime), zoneId);
    }

    public static ZonedDateTime of(LocalDateTime localDateTime, ZoneId zoneId) {
        return ofLocal(localDateTime, zoneId, null);
    }

    public static ZonedDateTime of(int i, int i2, int i3, int i4, int i5, int i6, int i7, ZoneId zoneId) {
        return ofLocal(LocalDateTime.of(i, i2, i3, i4, i5, i6, i7), zoneId, 0);
    }

    public static ZonedDateTime ofLocal(LocalDateTime localDateTime, ZoneId zoneId, ZoneOffset zoneOffset) {
        Jdk8Methods.requireNonNull(localDateTime, "localDateTime");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        if (zoneId instanceof ZoneOffset) {
            return new ZonedDateTime(localDateTime, (ZoneOffset) zoneId, zoneId);
        }
        ZoneRules rules = zoneId.getRules();
        List validOffsets = rules.getValidOffsets(localDateTime);
        if (validOffsets.size() == 1) {
            zoneOffset = (ZoneOffset) validOffsets.get(0);
        } else if (validOffsets.size() == 0) {
            zoneOffset = rules.getTransition(localDateTime);
            localDateTime = localDateTime.plusSeconds(zoneOffset.getDuration().getSeconds());
            zoneOffset = zoneOffset.getOffsetAfter();
        } else if (zoneOffset == null || !validOffsets.contains(zoneOffset)) {
            zoneOffset = (ZoneOffset) Jdk8Methods.requireNonNull(validOffsets.get(0), "offset");
        }
        return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
    }

    public static ZonedDateTime ofInstant(Instant instant, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(instant, "instant");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return create(instant.getEpochSecond(), instant.getNano(), zoneId);
    }

    public static ZonedDateTime ofInstant(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(localDateTime, "localDateTime");
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return create(localDateTime.toEpochSecond(zoneOffset), localDateTime.getNano(), zoneId);
    }

    private static ZonedDateTime create(long j, int i, ZoneId zoneId) {
        ZoneOffset offset = zoneId.getRules().getOffset(Instant.ofEpochSecond(j, (long) i));
        return new ZonedDateTime(LocalDateTime.ofEpochSecond(j, i, offset), offset, zoneId);
    }

    public static ZonedDateTime ofStrict(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(localDateTime, "localDateTime");
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        ZoneRules rules = zoneId.getRules();
        if (rules.isValidOffset(localDateTime, zoneOffset)) {
            return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
        }
        ZoneOffsetTransition transition = rules.getTransition(localDateTime);
        if (transition == null || !transition.isGap()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ZoneOffset '");
            stringBuilder.append(zoneOffset);
            stringBuilder.append("' is not valid for LocalDateTime '");
            stringBuilder.append(localDateTime);
            stringBuilder.append("' in zone '");
            stringBuilder.append(zoneId);
            stringBuilder.append("'");
            throw new DateTimeException(stringBuilder.toString());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("LocalDateTime '");
        stringBuilder2.append(localDateTime);
        stringBuilder2.append("' does not exist in zone '");
        stringBuilder2.append(zoneId);
        stringBuilder2.append("' due to a gap in the local time-line, typically caused by daylight savings");
        throw new DateTimeException(stringBuilder2.toString());
    }

    private static ZonedDateTime ofLenient(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(localDateTime, "localDateTime");
        Jdk8Methods.requireNonNull(zoneOffset, "offset");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        if (!(zoneId instanceof ZoneOffset) || zoneOffset.equals(zoneId)) {
            return new ZonedDateTime(localDateTime, zoneOffset, zoneId);
        }
        throw new IllegalArgumentException("ZoneId must match ZoneOffset");
    }

    public static org.threeten.bp.ZonedDateTime from(org.threeten.bp.temporal.TemporalAccessor r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r4 instanceof org.threeten.bp.ZonedDateTime;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r4 = (org.threeten.bp.ZonedDateTime) r4;
        return r4;
    L_0x0007:
        r0 = org.threeten.bp.ZoneId.from(r4);	 Catch:{ DateTimeException -> 0x002d }
        r1 = org.threeten.bp.temporal.ChronoField.INSTANT_SECONDS;	 Catch:{ DateTimeException -> 0x002d }
        r1 = r4.isSupported(r1);	 Catch:{ DateTimeException -> 0x002d }
        if (r1 == 0) goto L_0x0024;
    L_0x0013:
        r1 = org.threeten.bp.temporal.ChronoField.INSTANT_SECONDS;	 Catch:{ DateTimeException -> 0x0024 }
        r1 = r4.getLong(r1);	 Catch:{ DateTimeException -> 0x0024 }
        r3 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;	 Catch:{ DateTimeException -> 0x0024 }
        r3 = r4.get(r3);	 Catch:{ DateTimeException -> 0x0024 }
        r1 = create(r1, r3, r0);	 Catch:{ DateTimeException -> 0x0024 }
        return r1;
    L_0x0024:
        r1 = org.threeten.bp.LocalDateTime.from(r4);	 Catch:{ DateTimeException -> 0x002d }
        r0 = of(r1, r0);	 Catch:{ DateTimeException -> 0x002d }
        return r0;
    L_0x002d:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to obtain ZonedDateTime from TemporalAccessor: ";
        r1.append(r2);
        r1.append(r4);
        r2 = ", type ";
        r1.append(r2);
        r4 = r4.getClass();
        r4 = r4.getName();
        r1.append(r4);
        r4 = r1.toString();
        r0.<init>(r4);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.ZonedDateTime.from(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.ZonedDateTime");
    }

    public static ZonedDateTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public static ZonedDateTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (ZonedDateTime) dateTimeFormatter.parse(charSequence, FROM);
    }

    private ZonedDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset, ZoneId zoneId) {
        this.dateTime = localDateTime;
        this.offset = zoneOffset;
        this.zone = zoneId;
    }

    private ZonedDateTime resolveLocal(LocalDateTime localDateTime) {
        return ofLocal(localDateTime, this.zone, this.offset);
    }

    private ZonedDateTime resolveInstant(LocalDateTime localDateTime) {
        return ofInstant(localDateTime, this.offset, this.zone);
    }

    private ZonedDateTime resolveOffset(ZoneOffset zoneOffset) {
        return (zoneOffset.equals(this.offset) || !this.zone.getRules().isValidOffset(this.dateTime, zoneOffset)) ? this : new ZonedDateTime(this.dateTime, zoneOffset, this.zone);
    }

    public boolean isSupported(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            if (temporalField == null || temporalField.isSupportedBy(this) == null) {
                return null;
            }
        }
        return true;
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
        if (temporalField != ChronoField.INSTANT_SECONDS) {
            if (temporalField != ChronoField.OFFSET_SECONDS) {
                return this.dateTime.range(temporalField);
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
                throw new DateTimeException(stringBuilder.toString());
            case OFFSET_SECONDS:
                return getOffset().getTotalSeconds();
            default:
                return this.dateTime.get(temporalField);
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
                return this.dateTime.getLong(temporalField);
        }
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public ZonedDateTime withEarlierOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(this.dateTime);
        if (transition != null && transition.isOverlap()) {
            ZoneOffset offsetBefore = transition.getOffsetBefore();
            if (!offsetBefore.equals(this.offset)) {
                return new ZonedDateTime(this.dateTime, offsetBefore, this.zone);
            }
        }
        return this;
    }

    public ZonedDateTime withLaterOffsetAtOverlap() {
        ZoneOffsetTransition transition = getZone().getRules().getTransition(toLocalDateTime());
        if (transition != null) {
            ZoneOffset offsetAfter = transition.getOffsetAfter();
            if (!offsetAfter.equals(this.offset)) {
                return new ZonedDateTime(this.dateTime, offsetAfter, this.zone);
            }
        }
        return this;
    }

    public ZoneId getZone() {
        return this.zone;
    }

    public ZonedDateTime withZoneSameLocal(ZoneId zoneId) {
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : ofLocal(this.dateTime, zoneId, this.offset);
    }

    public ZonedDateTime withZoneSameInstant(ZoneId zoneId) {
        Jdk8Methods.requireNonNull(zoneId, "zone");
        return this.zone.equals(zoneId) ? this : create(this.dateTime.toEpochSecond(this.offset), this.dateTime.getNano(), zoneId);
    }

    public ZonedDateTime withFixedOffsetZone() {
        return this.zone.equals(this.offset) ? this : new ZonedDateTime(this.dateTime, this.offset, this.offset);
    }

    public int getYear() {
        return this.dateTime.getYear();
    }

    public int getMonthValue() {
        return this.dateTime.getMonthValue();
    }

    public Month getMonth() {
        return this.dateTime.getMonth();
    }

    public int getDayOfMonth() {
        return this.dateTime.getDayOfMonth();
    }

    public int getDayOfYear() {
        return this.dateTime.getDayOfYear();
    }

    public DayOfWeek getDayOfWeek() {
        return this.dateTime.getDayOfWeek();
    }

    public int getHour() {
        return this.dateTime.getHour();
    }

    public int getMinute() {
        return this.dateTime.getMinute();
    }

    public int getSecond() {
        return this.dateTime.getSecond();
    }

    public int getNano() {
        return this.dateTime.getNano();
    }

    public ZonedDateTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return resolveLocal(LocalDateTime.of((LocalDate) temporalAdjuster, this.dateTime.toLocalTime()));
        }
        if (temporalAdjuster instanceof LocalTime) {
            return resolveLocal(LocalDateTime.of(this.dateTime.toLocalDate(), (LocalTime) temporalAdjuster));
        }
        if (temporalAdjuster instanceof LocalDateTime) {
            return resolveLocal((LocalDateTime) temporalAdjuster);
        }
        if (temporalAdjuster instanceof Instant) {
            Instant instant = (Instant) temporalAdjuster;
            return create(instant.getEpochSecond(), instant.getNano(), this.zone);
        } else if (temporalAdjuster instanceof ZoneOffset) {
            return resolveOffset((ZoneOffset) temporalAdjuster);
        } else {
            return (ZonedDateTime) temporalAdjuster.adjustInto(this);
        }
    }

    public ZonedDateTime with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (ZonedDateTime) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        switch (chronoField) {
            case INSTANT_SECONDS:
                return create(j, getNano(), this.zone);
            case OFFSET_SECONDS:
                return resolveOffset(ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(j)));
            default:
                return resolveLocal(this.dateTime.with(temporalField, j));
        }
    }

    public ZonedDateTime withYear(int i) {
        return resolveLocal(this.dateTime.withYear(i));
    }

    public ZonedDateTime withMonth(int i) {
        return resolveLocal(this.dateTime.withMonth(i));
    }

    public ZonedDateTime withDayOfMonth(int i) {
        return resolveLocal(this.dateTime.withDayOfMonth(i));
    }

    public ZonedDateTime withDayOfYear(int i) {
        return resolveLocal(this.dateTime.withDayOfYear(i));
    }

    public ZonedDateTime withHour(int i) {
        return resolveLocal(this.dateTime.withHour(i));
    }

    public ZonedDateTime withMinute(int i) {
        return resolveLocal(this.dateTime.withMinute(i));
    }

    public ZonedDateTime withSecond(int i) {
        return resolveLocal(this.dateTime.withSecond(i));
    }

    public ZonedDateTime withNano(int i) {
        return resolveLocal(this.dateTime.withNano(i));
    }

    public ZonedDateTime truncatedTo(TemporalUnit temporalUnit) {
        return resolveLocal(this.dateTime.truncatedTo(temporalUnit));
    }

    public ZonedDateTime plus(TemporalAmount temporalAmount) {
        return (ZonedDateTime) temporalAmount.addTo(this);
    }

    public ZonedDateTime plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (ZonedDateTime) temporalUnit.addTo(this, j);
        }
        if (temporalUnit.isDateBased()) {
            return resolveLocal(this.dateTime.plus(j, temporalUnit));
        }
        return resolveInstant(this.dateTime.plus(j, temporalUnit));
    }

    public ZonedDateTime plusYears(long j) {
        return resolveLocal(this.dateTime.plusYears(j));
    }

    public ZonedDateTime plusMonths(long j) {
        return resolveLocal(this.dateTime.plusMonths(j));
    }

    public ZonedDateTime plusWeeks(long j) {
        return resolveLocal(this.dateTime.plusWeeks(j));
    }

    public ZonedDateTime plusDays(long j) {
        return resolveLocal(this.dateTime.plusDays(j));
    }

    public ZonedDateTime plusHours(long j) {
        return resolveInstant(this.dateTime.plusHours(j));
    }

    public ZonedDateTime plusMinutes(long j) {
        return resolveInstant(this.dateTime.plusMinutes(j));
    }

    public ZonedDateTime plusSeconds(long j) {
        return resolveInstant(this.dateTime.plusSeconds(j));
    }

    public ZonedDateTime plusNanos(long j) {
        return resolveInstant(this.dateTime.plusNanos(j));
    }

    public ZonedDateTime minus(TemporalAmount temporalAmount) {
        return (ZonedDateTime) temporalAmount.subtractFrom(this);
    }

    public ZonedDateTime minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public ZonedDateTime minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    public ZonedDateTime minusMonths(long j) {
        return j == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-j);
    }

    public ZonedDateTime minusWeeks(long j) {
        return j == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-j);
    }

    public ZonedDateTime minusDays(long j) {
        return j == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-j);
    }

    public ZonedDateTime minusHours(long j) {
        return j == Long.MIN_VALUE ? plusHours(Long.MAX_VALUE).plusHours(1) : plusHours(-j);
    }

    public ZonedDateTime minusMinutes(long j) {
        return j == Long.MIN_VALUE ? plusMinutes(Long.MAX_VALUE).plusMinutes(1) : plusMinutes(-j);
    }

    public ZonedDateTime minusSeconds(long j) {
        return j == Long.MIN_VALUE ? plusSeconds(Long.MAX_VALUE).plusSeconds(1) : plusSeconds(-j);
    }

    public ZonedDateTime minusNanos(long j) {
        return j == Long.MIN_VALUE ? plusNanos(Long.MAX_VALUE).plusNanos(1) : plusNanos(-j);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return toLocalDate();
        }
        return super.query(temporalQuery);
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        temporal = temporal.withZoneSameInstant(this.zone);
        if (temporalUnit.isDateBased()) {
            return this.dateTime.until(temporal.dateTime, temporalUnit);
        }
        return toOffsetDateTime().until(temporal.toOffsetDateTime(), temporalUnit);
    }

    public LocalDateTime toLocalDateTime() {
        return this.dateTime;
    }

    public LocalDate toLocalDate() {
        return this.dateTime.toLocalDate();
    }

    public LocalTime toLocalTime() {
        return this.dateTime.toLocalTime();
    }

    public OffsetDateTime toOffsetDateTime() {
        return OffsetDateTime.of(this.dateTime, this.offset);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZonedDateTime)) {
            return false;
        }
        ZonedDateTime zonedDateTime = (ZonedDateTime) obj;
        if (!this.dateTime.equals(zonedDateTime.dateTime) || !this.offset.equals(zonedDateTime.offset) || this.zone.equals(zonedDateTime.zone) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (this.dateTime.hashCode() ^ this.offset.hashCode()) ^ Integer.rotateLeft(this.zone.hashCode(), 3);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.dateTime.toString());
        stringBuilder.append(this.offset.toString());
        String stringBuilder2 = stringBuilder.toString();
        if (this.offset == this.zone) {
            return stringBuilder2;
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(stringBuilder2);
        stringBuilder3.append('[');
        stringBuilder3.append(this.zone.toString());
        stringBuilder3.append(']');
        return stringBuilder3.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        return super.format(dateTimeFormatter);
    }

    private Object writeReplace() {
        return new Ser((byte) 6, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        this.dateTime.writeExternal(dataOutput);
        this.offset.writeExternal(dataOutput);
        this.zone.write(dataOutput);
    }

    static ZonedDateTime readExternal(DataInput dataInput) throws IOException {
        return ofLenient(LocalDateTime.readExternal(dataInput), ZoneOffset.readExternal(dataInput), (ZoneId) Ser.read(dataInput));
    }
}
