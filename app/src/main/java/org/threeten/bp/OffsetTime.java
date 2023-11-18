package org.threeten.bp;

import com.google.android.exoplayer2.C0649C;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.jdk8.DefaultInterfaceTemporalAccessor;
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

public final class OffsetTime extends DefaultInterfaceTemporalAccessor implements Temporal, TemporalAdjuster, Comparable<OffsetTime>, Serializable {
    public static final TemporalQuery<OffsetTime> FROM = new C15261();
    public static final OffsetTime MAX = LocalTime.MAX.atOffset(ZoneOffset.MIN);
    public static final OffsetTime MIN = LocalTime.MIN.atOffset(ZoneOffset.MAX);
    private static final long serialVersionUID = 7264499704384272492L;
    private final ZoneOffset offset;
    private final LocalTime time;

    /* renamed from: org.threeten.bp.OffsetTime$1 */
    static class C15261 implements TemporalQuery<OffsetTime> {
        C15261() {
        }

        public OffsetTime queryFrom(TemporalAccessor temporalAccessor) {
            return OffsetTime.from(temporalAccessor);
        }
    }

    public static OffsetTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static OffsetTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static OffsetTime now(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofInstant(instant, clock.getZone().getRules().getOffset(instant));
    }

    public static OffsetTime of(LocalTime localTime, ZoneOffset zoneOffset) {
        return new OffsetTime(localTime, zoneOffset);
    }

    public static OffsetTime of(int i, int i2, int i3, int i4, ZoneOffset zoneOffset) {
        return new OffsetTime(LocalTime.of(i, i2, i3, i4), zoneOffset);
    }

    public static OffsetTime ofInstant(Instant instant, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(instant, "instant");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        zoneId = zoneId.getRules().getOffset(instant);
        long epochSecond = ((instant.getEpochSecond() % 86400) + ((long) zoneId.getTotalSeconds())) % 86400;
        return new OffsetTime(LocalTime.ofSecondOfDay(epochSecond < 0 ? epochSecond + 86400 : epochSecond, instant.getNano()), zoneId);
    }

    public static org.threeten.bp.OffsetTime from(org.threeten.bp.temporal.TemporalAccessor r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r3 instanceof org.threeten.bp.OffsetTime;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r3 = (org.threeten.bp.OffsetTime) r3;
        return r3;
    L_0x0007:
        r0 = org.threeten.bp.LocalTime.from(r3);	 Catch:{ DateTimeException -> 0x0015 }
        r1 = org.threeten.bp.ZoneOffset.from(r3);	 Catch:{ DateTimeException -> 0x0015 }
        r2 = new org.threeten.bp.OffsetTime;	 Catch:{ DateTimeException -> 0x0015 }
        r2.<init>(r0, r1);	 Catch:{ DateTimeException -> 0x0015 }
        return r2;
    L_0x0015:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to obtain OffsetTime from TemporalAccessor: ";
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
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.OffsetTime.from(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.OffsetTime");
    }

    public static OffsetTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_OFFSET_TIME);
    }

    public static OffsetTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (OffsetTime) dateTimeFormatter.parse(charSequence, FROM);
    }

    private OffsetTime(LocalTime localTime, ZoneOffset zoneOffset) {
        this.time = (LocalTime) Jdk8Methods.requireNonNull(localTime, "time");
        this.offset = (ZoneOffset) Jdk8Methods.requireNonNull(zoneOffset, "offset");
    }

    private OffsetTime with(LocalTime localTime, ZoneOffset zoneOffset) {
        if (this.time == localTime && this.offset.equals(zoneOffset)) {
            return this;
        }
        return new OffsetTime(localTime, zoneOffset);
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = true;
        if (temporalField instanceof ChronoField) {
            if (!temporalField.isTimeBased()) {
                if (temporalField != ChronoField.OFFSET_SECONDS) {
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
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isTimeBased();
        }
        temporalUnit = (temporalUnit == null || temporalUnit.isSupportedBy(this) == null) ? null : true;
        return temporalUnit;
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return temporalField.range();
        }
        return this.time.range(temporalField);
    }

    public int get(TemporalField temporalField) {
        return super.get(temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return (long) getOffset().getTotalSeconds();
        }
        return this.time.getLong(temporalField);
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public OffsetTime withOffsetSameLocal(ZoneOffset zoneOffset) {
        return (zoneOffset == null || !zoneOffset.equals(this.offset)) ? new OffsetTime(this.time, zoneOffset) : this;
    }

    public OffsetTime withOffsetSameInstant(ZoneOffset zoneOffset) {
        if (zoneOffset.equals(this.offset)) {
            return this;
        }
        return new OffsetTime(this.time.plusSeconds((long) (zoneOffset.getTotalSeconds() - this.offset.getTotalSeconds())), zoneOffset);
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

    public OffsetTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalTime) {
            return with((LocalTime) temporalAdjuster, this.offset);
        }
        if (temporalAdjuster instanceof ZoneOffset) {
            return with(this.time, (ZoneOffset) temporalAdjuster);
        }
        if (temporalAdjuster instanceof OffsetTime) {
            return (OffsetTime) temporalAdjuster;
        }
        return (OffsetTime) temporalAdjuster.adjustInto(this);
    }

    public OffsetTime with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (OffsetTime) temporalField.adjustInto(this, j);
        }
        if (temporalField != ChronoField.OFFSET_SECONDS) {
            return with(this.time.with(temporalField, j), this.offset);
        }
        return with(this.time, ZoneOffset.ofTotalSeconds(((ChronoField) temporalField).checkValidIntValue(j)));
    }

    public OffsetTime withHour(int i) {
        return with(this.time.withHour(i), this.offset);
    }

    public OffsetTime withMinute(int i) {
        return with(this.time.withMinute(i), this.offset);
    }

    public OffsetTime withSecond(int i) {
        return with(this.time.withSecond(i), this.offset);
    }

    public OffsetTime withNano(int i) {
        return with(this.time.withNano(i), this.offset);
    }

    public OffsetTime truncatedTo(TemporalUnit temporalUnit) {
        return with(this.time.truncatedTo(temporalUnit), this.offset);
    }

    public OffsetTime plus(TemporalAmount temporalAmount) {
        return (OffsetTime) temporalAmount.addTo(this);
    }

    public OffsetTime plus(long j, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return with(this.time.plus(j, temporalUnit), this.offset);
        }
        return (OffsetTime) temporalUnit.addTo(this, j);
    }

    public OffsetTime plusHours(long j) {
        return with(this.time.plusHours(j), this.offset);
    }

    public OffsetTime plusMinutes(long j) {
        return with(this.time.plusMinutes(j), this.offset);
    }

    public OffsetTime plusSeconds(long j) {
        return with(this.time.plusSeconds(j), this.offset);
    }

    public OffsetTime plusNanos(long j) {
        return with(this.time.plusNanos(j), this.offset);
    }

    public OffsetTime minus(TemporalAmount temporalAmount) {
        return (OffsetTime) temporalAmount.subtractFrom(this);
    }

    public OffsetTime minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public OffsetTime minusHours(long j) {
        return with(this.time.minusHours(j), this.offset);
    }

    public OffsetTime minusMinutes(long j) {
        return with(this.time.minusMinutes(j), this.offset);
    }

    public OffsetTime minusSeconds(long j) {
        return with(this.time.minusSeconds(j), this.offset);
    }

    public OffsetTime minusNanos(long j) {
        return with(this.time.minusNanos(j), this.offset);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.NANOS;
        }
        if (temporalQuery != TemporalQueries.offset()) {
            if (temporalQuery != TemporalQueries.zone()) {
                if (temporalQuery == TemporalQueries.localTime()) {
                    return this.time;
                }
                if (!(temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.localDate())) {
                    if (temporalQuery != TemporalQueries.zoneId()) {
                        return super.query(temporalQuery);
                    }
                }
                return null;
            }
        }
        return getOffset();
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.NANO_OF_DAY, this.time.toNanoOfDay()).with(ChronoField.OFFSET_SECONDS, (long) getOffset().getTotalSeconds());
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        long toEpochNano = temporal.toEpochNano() - toEpochNano();
        switch ((ChronoUnit) temporalUnit) {
            case NANOS:
                return toEpochNano;
            case MICROS:
                return toEpochNano / 1000;
            case MILLIS:
                return toEpochNano / 1000000;
            case SECONDS:
                return toEpochNano / 1000000000;
            case MINUTES:
                return toEpochNano / -129542144;
            case HOURS:
                return toEpochNano / 817405952;
            case HALF_DAYS:
                return toEpochNano / 1218936832;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public OffsetDateTime atDate(LocalDate localDate) {
        return OffsetDateTime.of(localDate, this.time, this.offset);
    }

    public LocalTime toLocalTime() {
        return this.time;
    }

    private long toEpochNano() {
        return this.time.toNanoOfDay() - (((long) this.offset.getTotalSeconds()) * C0649C.NANOS_PER_SECOND);
    }

    public int compareTo(OffsetTime offsetTime) {
        if (this.offset.equals(offsetTime.offset)) {
            return this.time.compareTo(offsetTime.time);
        }
        int compareLongs = Jdk8Methods.compareLongs(toEpochNano(), offsetTime.toEpochNano());
        if (compareLongs == 0) {
            compareLongs = this.time.compareTo(offsetTime.time);
        }
        return compareLongs;
    }

    public boolean isAfter(OffsetTime offsetTime) {
        return toEpochNano() > offsetTime.toEpochNano() ? true : null;
    }

    public boolean isBefore(OffsetTime offsetTime) {
        return toEpochNano() < offsetTime.toEpochNano() ? true : null;
    }

    public boolean isEqual(OffsetTime offsetTime) {
        return toEpochNano() == offsetTime.toEpochNano() ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OffsetTime)) {
            return false;
        }
        OffsetTime offsetTime = (OffsetTime) obj;
        if (!this.time.equals(offsetTime.time) || this.offset.equals(offsetTime.offset) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.time.hashCode() ^ this.offset.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.time.toString());
        stringBuilder.append(this.offset.toString());
        return stringBuilder.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 66, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        this.time.writeExternal(dataOutput);
        this.offset.writeExternal(dataOutput);
    }

    static OffsetTime readExternal(DataInput dataInput) throws IOException {
        return of(LocalTime.readExternal(dataInput), ZoneOffset.readExternal(dataInput));
    }
}
