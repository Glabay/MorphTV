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

public final class Instant extends DefaultInterfaceTemporalAccessor implements Temporal, TemporalAdjuster, Comparable<Instant>, Serializable {
    public static final Instant EPOCH = new Instant(0, 0);
    public static final TemporalQuery<Instant> FROM = new C15111();
    public static final Instant MAX = ofEpochSecond(MAX_SECOND, 999999999);
    private static final long MAX_SECOND = 31556889864403199L;
    private static final long MILLIS_PER_SEC = 1000;
    public static final Instant MIN = ofEpochSecond(MIN_SECOND, 0);
    private static final long MIN_SECOND = -31557014167219200L;
    private static final int NANOS_PER_MILLI = 1000000;
    private static final int NANOS_PER_SECOND = 1000000000;
    private static final long serialVersionUID = -665713676816604388L;
    private final int nanos;
    private final long seconds;

    /* renamed from: org.threeten.bp.Instant$1 */
    static class C15111 implements TemporalQuery<Instant> {
        C15111() {
        }

        public Instant queryFrom(TemporalAccessor temporalAccessor) {
            return Instant.from(temporalAccessor);
        }
    }

    public static Instant now() {
        return Clock.systemUTC().instant();
    }

    public static Instant now(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        return clock.instant();
    }

    public static Instant ofEpochSecond(long j) {
        return create(j, 0);
    }

    public static Instant ofEpochSecond(long j, long j2) {
        return create(Jdk8Methods.safeAdd(j, Jdk8Methods.floorDiv(j2, (long) C0649C.NANOS_PER_SECOND)), Jdk8Methods.floorMod(j2, (int) NANOS_PER_SECOND));
    }

    public static Instant ofEpochMilli(long j) {
        return create(Jdk8Methods.floorDiv(j, 1000), Jdk8Methods.floorMod(j, 1000) * NANOS_PER_MILLI);
    }

    public static Instant from(TemporalAccessor temporalAccessor) {
        try {
            return ofEpochSecond(temporalAccessor.getLong(ChronoField.INSTANT_SECONDS), (long) temporalAccessor.get(ChronoField.NANO_OF_SECOND));
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to obtain Instant from TemporalAccessor: ");
            stringBuilder.append(temporalAccessor);
            stringBuilder.append(", type ");
            stringBuilder.append(temporalAccessor.getClass().getName());
            throw new DateTimeException(stringBuilder.toString(), e);
        }
    }

    public static Instant parse(CharSequence charSequence) {
        return (Instant) DateTimeFormatter.ISO_INSTANT.parse(charSequence, FROM);
    }

    private static Instant create(long j, int i) {
        if ((j | ((long) i)) == 0) {
            return EPOCH;
        }
        if (j >= MIN_SECOND) {
            if (j <= MAX_SECOND) {
                return new Instant(j, i);
            }
        }
        throw new DateTimeException("Instant exceeds minimum or maximum instant");
    }

    private Instant(long j, int i) {
        this.seconds = j;
        this.nanos = i;
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = true;
        if (temporalField instanceof ChronoField) {
            if (!(temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.NANO_OF_SECOND || temporalField == ChronoField.MICRO_OF_SECOND)) {
                if (temporalField != ChronoField.MILLI_OF_SECOND) {
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
            if (!temporalUnit.isTimeBased()) {
                if (temporalUnit != ChronoUnit.DAYS) {
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
        return super.range(temporalField);
    }

    public int get(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return range(temporalField).checkValidIntValue(temporalField.getFrom(this), temporalField);
        }
        switch ((ChronoField) temporalField) {
            case NANO_OF_SECOND:
                return this.nanos;
            case MICRO_OF_SECOND:
                return this.nanos / 1000;
            case MILLI_OF_SECOND:
                return this.nanos / NANOS_PER_MILLI;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField) temporalField) {
            case NANO_OF_SECOND:
                return (long) this.nanos;
            case MICRO_OF_SECOND:
                return (long) (this.nanos / 1000);
            case MILLI_OF_SECOND:
                return (long) (this.nanos / NANOS_PER_MILLI);
            case INSTANT_SECONDS:
                return this.seconds;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public long getEpochSecond() {
        return this.seconds;
    }

    public int getNano() {
        return this.nanos;
    }

    public Instant with(TemporalAdjuster temporalAdjuster) {
        return (Instant) temporalAdjuster.adjustInto(this);
    }

    public Instant with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (Instant) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        chronoField.checkValidValue(j);
        switch (chronoField) {
            case NANO_OF_SECOND:
                return j != ((long) this.nanos) ? create(this.seconds, (int) j) : this;
            case MICRO_OF_SECOND:
                temporalField = ((int) j) * 1000;
                return temporalField != this.nanos ? create(this.seconds, temporalField) : this;
            case MILLI_OF_SECOND:
                temporalField = ((int) j) * C0649C.MICROS_PER_SECOND;
                return temporalField != this.nanos ? create(this.seconds, temporalField) : this;
            case INSTANT_SECONDS:
                return j != this.seconds ? create(j, this.nanos) : this;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public Instant truncatedTo(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.NANOS) {
            return this;
        }
        temporalUnit = temporalUnit.getDuration();
        if (temporalUnit.getSeconds() > 86400) {
            throw new DateTimeException("Unit is too large to be used for truncation");
        }
        long toNanos = temporalUnit.toNanos();
        if (86400000000000L % toNanos != 0) {
            throw new DateTimeException("Unit must divide into a standard day without remainder");
        }
        long j = ((this.seconds % 86400) * C0649C.NANOS_PER_SECOND) + ((long) this.nanos);
        return plusNanos((Jdk8Methods.floorDiv(j, toNanos) * toNanos) - j);
    }

    public Instant plus(TemporalAmount temporalAmount) {
        return (Instant) temporalAmount.addTo(this);
    }

    public Instant plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (Instant) temporalUnit.addTo(this, j);
        }
        switch ((ChronoUnit) temporalUnit) {
            case NANOS:
                return plusNanos(j);
            case MICROS:
                return plus(j / C0649C.MICROS_PER_SECOND, (j % C0649C.MICROS_PER_SECOND) * 1000);
            case MILLIS:
                return plusMillis(j);
            case SECONDS:
                return plusSeconds(j);
            case MINUTES:
                return plusSeconds(Jdk8Methods.safeMultiply(j, 60));
            case HOURS:
                return plusSeconds(Jdk8Methods.safeMultiply(j, 3600));
            case HALF_DAYS:
                return plusSeconds(Jdk8Methods.safeMultiply(j, 43200));
            case DAYS:
                return plusSeconds(Jdk8Methods.safeMultiply(j, 86400));
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public Instant plusSeconds(long j) {
        return plus(j, 0);
    }

    public Instant plusMillis(long j) {
        return plus(j / 1000, (j % 1000) * C0649C.MICROS_PER_SECOND);
    }

    public Instant plusNanos(long j) {
        return plus(0, j);
    }

    private Instant plus(long j, long j2) {
        if ((j | j2) == 0) {
            return this;
        }
        return ofEpochSecond(Jdk8Methods.safeAdd(Jdk8Methods.safeAdd(this.seconds, j), j2 / C0649C.NANOS_PER_SECOND), ((long) this.nanos) + (j2 % C0649C.NANOS_PER_SECOND));
    }

    public Instant minus(TemporalAmount temporalAmount) {
        return (Instant) temporalAmount.subtractFrom(this);
    }

    public Instant minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public Instant minusSeconds(long j) {
        if (j == Long.MIN_VALUE) {
            return plusSeconds(Long.MAX_VALUE).plusSeconds(1);
        }
        return plusSeconds(-j);
    }

    public Instant minusMillis(long j) {
        if (j == Long.MIN_VALUE) {
            return plusMillis(Long.MAX_VALUE).plusMillis(1);
        }
        return plusMillis(-j);
    }

    public Instant minusNanos(long j) {
        if (j == Long.MIN_VALUE) {
            return plusNanos(Long.MAX_VALUE).plusNanos(1);
        }
        return plusNanos(-j);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.NANOS;
        }
        if (!(temporalQuery == TemporalQueries.localDate() || temporalQuery == TemporalQueries.localTime() || temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone())) {
            if (temporalQuery != TemporalQueries.offset()) {
                return temporalQuery.queryFrom(this);
            }
        }
        return null;
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.INSTANT_SECONDS, this.seconds).with(ChronoField.NANO_OF_SECOND, (long) this.nanos);
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        switch ((ChronoUnit) temporalUnit) {
            case NANOS:
                return nanosUntil(temporal);
            case MICROS:
                return nanosUntil(temporal) / 1000;
            case MILLIS:
                return Jdk8Methods.safeSubtract(temporal.toEpochMilli(), toEpochMilli());
            case SECONDS:
                return secondsUntil(temporal);
            case MINUTES:
                return secondsUntil(temporal) / 60;
            case HOURS:
                return secondsUntil(temporal) / 3600;
            case HALF_DAYS:
                return secondsUntil(temporal) / 43200;
            case DAYS:
                return secondsUntil(temporal) / 86400;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    private long nanosUntil(Instant instant) {
        return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(Jdk8Methods.safeSubtract(instant.seconds, this.seconds), (int) NANOS_PER_SECOND), (long) (instant.nanos - this.nanos));
    }

    private long secondsUntil(Instant instant) {
        long safeSubtract = Jdk8Methods.safeSubtract(instant.seconds, this.seconds);
        long j = (long) (instant.nanos - this.nanos);
        if (safeSubtract <= 0 || j >= 0) {
            return (safeSubtract >= 0 || j <= 0) ? safeSubtract : safeSubtract + 1;
        } else {
            return safeSubtract - 1;
        }
    }

    public OffsetDateTime atOffset(ZoneOffset zoneOffset) {
        return OffsetDateTime.ofInstant(this, zoneOffset);
    }

    public ZonedDateTime atZone(ZoneId zoneId) {
        return ZonedDateTime.ofInstant(this, zoneId);
    }

    public long toEpochMilli() {
        if (this.seconds >= 0) {
            return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(this.seconds, 1000), (long) (this.nanos / NANOS_PER_MILLI));
        }
        return Jdk8Methods.safeSubtract(Jdk8Methods.safeMultiply(this.seconds + 1, 1000), 1000 - ((long) (this.nanos / NANOS_PER_MILLI)));
    }

    public int compareTo(Instant instant) {
        int compareLongs = Jdk8Methods.compareLongs(this.seconds, instant.seconds);
        if (compareLongs != 0) {
            return compareLongs;
        }
        return this.nanos - instant.nanos;
    }

    public boolean isAfter(Instant instant) {
        return compareTo(instant) > null ? true : null;
    }

    public boolean isBefore(Instant instant) {
        return compareTo(instant) < null ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Instant)) {
            return false;
        }
        Instant instant = (Instant) obj;
        if (this.seconds != instant.seconds || this.nanos != instant.nanos) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((int) (this.seconds ^ (this.seconds >>> 32))) + (this.nanos * 51);
    }

    public String toString() {
        return DateTimeFormatter.ISO_INSTANT.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 2, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.seconds);
        dataOutput.writeInt(this.nanos);
    }

    static Instant readExternal(DataInput dataInput) throws IOException {
        return ofEpochSecond(dataInput.readLong(), (long) dataInput.readInt());
    }
}
