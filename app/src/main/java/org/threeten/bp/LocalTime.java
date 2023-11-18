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

public final class LocalTime extends DefaultInterfaceTemporalAccessor implements Temporal, TemporalAdjuster, Comparable<LocalTime>, Serializable {
    public static final TemporalQuery<LocalTime> FROM = new C15171();
    private static final LocalTime[] HOURS = new LocalTime[24];
    static final int HOURS_PER_DAY = 24;
    public static final LocalTime MAX = new LocalTime(23, 59, 59, Year.MAX_VALUE);
    static final long MICROS_PER_DAY = 86400000000L;
    public static final LocalTime MIDNIGHT = HOURS[0];
    static final long MILLIS_PER_DAY = 86400000;
    public static final LocalTime MIN = HOURS[0];
    static final int MINUTES_PER_DAY = 1440;
    static final int MINUTES_PER_HOUR = 60;
    static final long NANOS_PER_DAY = 86400000000000L;
    static final long NANOS_PER_HOUR = 3600000000000L;
    static final long NANOS_PER_MINUTE = 60000000000L;
    static final long NANOS_PER_SECOND = 1000000000;
    public static final LocalTime NOON = HOURS[12];
    static final int SECONDS_PER_DAY = 86400;
    static final int SECONDS_PER_HOUR = 3600;
    static final int SECONDS_PER_MINUTE = 60;
    private static final long serialVersionUID = 6414437269572265201L;
    private final byte hour;
    private final byte minute;
    private final int nano;
    private final byte second;

    /* renamed from: org.threeten.bp.LocalTime$1 */
    static class C15171 implements TemporalQuery<LocalTime> {
        C15171() {
        }

        public LocalTime queryFrom(TemporalAccessor temporalAccessor) {
            return LocalTime.from(temporalAccessor);
        }
    }

    static {
        for (int i = 0; i < HOURS.length; i++) {
            HOURS[i] = new LocalTime(i, 0, 0, 0);
        }
    }

    public static LocalTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static LocalTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static LocalTime now(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        long epochSecond = ((instant.getEpochSecond() % 86400) + ((long) clock.getZone().getRules().getOffset(instant).getTotalSeconds())) % 86400;
        return ofSecondOfDay(epochSecond < 0 ? epochSecond + 86400 : epochSecond, instant.getNano());
    }

    public static LocalTime of(int i, int i2) {
        ChronoField.HOUR_OF_DAY.checkValidValue((long) i);
        if (i2 == 0) {
            return HOURS[i];
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue((long) i2);
        return new LocalTime(i, i2, 0, 0);
    }

    public static LocalTime of(int i, int i2, int i3) {
        ChronoField.HOUR_OF_DAY.checkValidValue((long) i);
        if ((i2 | i3) == 0) {
            return HOURS[i];
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue((long) i2);
        ChronoField.SECOND_OF_MINUTE.checkValidValue((long) i3);
        return new LocalTime(i, i2, i3, 0);
    }

    public static LocalTime of(int i, int i2, int i3, int i4) {
        ChronoField.HOUR_OF_DAY.checkValidValue((long) i);
        ChronoField.MINUTE_OF_HOUR.checkValidValue((long) i2);
        ChronoField.SECOND_OF_MINUTE.checkValidValue((long) i3);
        ChronoField.NANO_OF_SECOND.checkValidValue((long) i4);
        return create(i, i2, i3, i4);
    }

    public static LocalTime ofSecondOfDay(long j) {
        ChronoField.SECOND_OF_DAY.checkValidValue(j);
        int i = (int) (j / 3600);
        long j2 = j - ((long) (i * SECONDS_PER_HOUR));
        j = (int) (j2 / 60);
        return create(i, j, (int) (j2 - ((long) (j * 60))), 0);
    }

    static LocalTime ofSecondOfDay(long j, int i) {
        ChronoField.SECOND_OF_DAY.checkValidValue(j);
        ChronoField.NANO_OF_SECOND.checkValidValue((long) i);
        int i2 = (int) (j / 3600);
        long j2 = j - ((long) (i2 * SECONDS_PER_HOUR));
        j = (int) (j2 / 60);
        return create(i2, j, (int) (j2 - ((long) (j * 60))), i);
    }

    public static LocalTime ofNanoOfDay(long j) {
        ChronoField.NANO_OF_DAY.checkValidValue(j);
        int i = (int) (j / NANOS_PER_HOUR);
        long j2 = j - (((long) i) * NANOS_PER_HOUR);
        int i2 = (int) (j2 / NANOS_PER_MINUTE);
        j = j2 - (((long) i2) * NANOS_PER_MINUTE);
        int i3 = (int) (j / 1000000000);
        return create(i, i2, i3, (int) (j - (((long) i3) * 1000000000)));
    }

    public static LocalTime from(TemporalAccessor temporalAccessor) {
        LocalTime localTime = (LocalTime) temporalAccessor.query(TemporalQueries.localTime());
        if (localTime != null) {
            return localTime;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to obtain LocalTime from TemporalAccessor: ");
        stringBuilder.append(temporalAccessor);
        stringBuilder.append(", type ");
        stringBuilder.append(temporalAccessor.getClass().getName());
        throw new DateTimeException(stringBuilder.toString());
    }

    public static LocalTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static LocalTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (LocalTime) dateTimeFormatter.parse(charSequence, FROM);
    }

    private static LocalTime create(int i, int i2, int i3, int i4) {
        if (((i2 | i3) | i4) == 0) {
            return HOURS[i];
        }
        return new LocalTime(i, i2, i3, i4);
    }

    private LocalTime(int i, int i2, int i3, int i4) {
        this.hour = (byte) i;
        this.minute = (byte) i2;
        this.second = (byte) i3;
        this.nano = i4;
    }

    public boolean isSupported(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isTimeBased();
        }
        temporalField = (temporalField == null || temporalField.isSupportedBy(this) == null) ? null : true;
        return temporalField;
    }

    public boolean isSupported(TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isTimeBased();
        }
        temporalUnit = (temporalUnit == null || temporalUnit.isSupportedBy(this) == null) ? null : true;
        return temporalUnit;
    }

    public ValueRange range(TemporalField temporalField) {
        return super.range(temporalField);
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
        if (temporalField == ChronoField.NANO_OF_DAY) {
            return toNanoOfDay();
        }
        if (temporalField == ChronoField.MICRO_OF_DAY) {
            return toNanoOfDay() / 1000;
        }
        return (long) get0(temporalField);
    }

    private int get0(TemporalField temporalField) {
        StringBuilder stringBuilder;
        switch ((ChronoField) temporalField) {
            case NANO_OF_SECOND:
                return this.nano;
            case NANO_OF_DAY:
                stringBuilder = new StringBuilder();
                stringBuilder.append("Field too large for an int: ");
                stringBuilder.append(temporalField);
                throw new DateTimeException(stringBuilder.toString());
            case MICRO_OF_SECOND:
                return this.nano / 1000;
            case MICRO_OF_DAY:
                stringBuilder = new StringBuilder();
                stringBuilder.append("Field too large for an int: ");
                stringBuilder.append(temporalField);
                throw new DateTimeException(stringBuilder.toString());
            case MILLI_OF_SECOND:
                return this.nano / 1000000;
            case MILLI_OF_DAY:
                return (int) (toNanoOfDay() / C0649C.MICROS_PER_SECOND);
            case SECOND_OF_MINUTE:
                return this.second;
            case SECOND_OF_DAY:
                return toSecondOfDay();
            case MINUTE_OF_HOUR:
                return this.minute;
            case MINUTE_OF_DAY:
                return (this.hour * 60) + this.minute;
            case HOUR_OF_AMPM:
                return this.hour % 12;
            case CLOCK_HOUR_OF_AMPM:
                temporalField = this.hour % 12;
                if (temporalField % 12 == 0) {
                    temporalField = 12;
                }
                return temporalField;
            case HOUR_OF_DAY:
                return this.hour;
            case CLOCK_HOUR_OF_DAY:
                return this.hour == null ? 24 : this.hour;
            case AMPM_OF_DAY:
                return this.hour / 12;
            default:
                stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSecond() {
        return this.second;
    }

    public int getNano() {
        return this.nano;
    }

    public LocalTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalTime) {
            return (LocalTime) temporalAdjuster;
        }
        return (LocalTime) temporalAdjuster.adjustInto(this);
    }

    public LocalTime with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (LocalTime) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        chronoField.checkValidValue(j);
        switch (chronoField) {
            case NANO_OF_SECOND:
                return withNano((int) j);
            case NANO_OF_DAY:
                return ofNanoOfDay(j);
            case MICRO_OF_SECOND:
                return withNano(((int) j) * 1000);
            case MICRO_OF_DAY:
                return ofNanoOfDay(j * 1000);
            case MILLI_OF_SECOND:
                return withNano(((int) j) * C0649C.MICROS_PER_SECOND);
            case MILLI_OF_DAY:
                return ofNanoOfDay(j * C0649C.MICROS_PER_SECOND);
            case SECOND_OF_MINUTE:
                return withSecond((int) j);
            case SECOND_OF_DAY:
                return plusSeconds(j - ((long) toSecondOfDay()));
            case MINUTE_OF_HOUR:
                return withMinute((int) j);
            case MINUTE_OF_DAY:
                return plusMinutes(j - ((long) ((this.hour * 60) + this.minute)));
            case HOUR_OF_AMPM:
                return plusHours(j - ((long) (this.hour % 12)));
            case CLOCK_HOUR_OF_AMPM:
                if (j == 12) {
                    j = 0;
                }
                return plusHours(j - ((long) (this.hour % 12)));
            case HOUR_OF_DAY:
                return withHour((int) j);
            case CLOCK_HOUR_OF_DAY:
                if (j == 24) {
                    j = 0;
                }
                return withHour((int) j);
            case AMPM_OF_DAY:
                return plusHours((j - ((long) (this.hour / 12))) * 12);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public LocalTime withHour(int i) {
        if (this.hour == i) {
            return this;
        }
        ChronoField.HOUR_OF_DAY.checkValidValue((long) i);
        return create(i, this.minute, this.second, this.nano);
    }

    public LocalTime withMinute(int i) {
        if (this.minute == i) {
            return this;
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue((long) i);
        return create(this.hour, i, this.second, this.nano);
    }

    public LocalTime withSecond(int i) {
        if (this.second == i) {
            return this;
        }
        ChronoField.SECOND_OF_MINUTE.checkValidValue((long) i);
        return create(this.hour, this.minute, i, this.nano);
    }

    public LocalTime withNano(int i) {
        if (this.nano == i) {
            return this;
        }
        ChronoField.NANO_OF_SECOND.checkValidValue((long) i);
        return create(this.hour, this.minute, this.second, i);
    }

    public LocalTime truncatedTo(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.NANOS) {
            return this;
        }
        temporalUnit = temporalUnit.getDuration();
        if (temporalUnit.getSeconds() > 86400) {
            throw new DateTimeException("Unit is too large to be used for truncation");
        }
        long toNanos = temporalUnit.toNanos();
        if (NANOS_PER_DAY % toNanos == 0) {
            return ofNanoOfDay((toNanoOfDay() / toNanos) * toNanos);
        }
        throw new DateTimeException("Unit must divide into a standard day without remainder");
    }

    public LocalTime plus(TemporalAmount temporalAmount) {
        return (LocalTime) temporalAmount.addTo(this);
    }

    public LocalTime plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (LocalTime) temporalUnit.addTo(this, j);
        }
        switch ((ChronoUnit) temporalUnit) {
            case NANOS:
                return plusNanos(j);
            case MICROS:
                return plusNanos((j % MICROS_PER_DAY) * 1000);
            case MILLIS:
                return plusNanos((j % 86400000) * C0649C.MICROS_PER_SECOND);
            case SECONDS:
                return plusSeconds(j);
            case MINUTES:
                return plusMinutes(j);
            case HOURS:
                return plusHours(j);
            case HALF_DAYS:
                return plusHours((j % 2) * 12);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public LocalTime plusHours(long j) {
        if (j == 0) {
            return this;
        }
        return create(((((int) (j % 24)) + this.hour) + 24) % 24, this.minute, this.second, this.nano);
    }

    public LocalTime plusMinutes(long j) {
        if (j == 0) {
            return this;
        }
        int i = (this.hour * 60) + this.minute;
        j = ((((int) (j % 1440)) + i) + MINUTES_PER_DAY) % MINUTES_PER_DAY;
        if (i == j) {
            return this;
        }
        return create(j / 60, j % 60, this.second, this.nano);
    }

    public LocalTime plusSeconds(long j) {
        if (j == 0) {
            return this;
        }
        int i = ((this.hour * SECONDS_PER_HOUR) + (this.minute * 60)) + this.second;
        j = ((((int) (j % 86400)) + i) + 86400) % 86400;
        if (i == j) {
            return this;
        }
        return create(j / SECONDS_PER_HOUR, (j / 60) % 60, j % 60, this.nano);
    }

    public LocalTime plusNanos(long j) {
        if (j == 0) {
            return this;
        }
        long toNanoOfDay = toNanoOfDay();
        j = (((j % NANOS_PER_DAY) + toNanoOfDay) + NANOS_PER_DAY) % NANOS_PER_DAY;
        if (toNanoOfDay == j) {
            return this;
        }
        return create((int) (j / NANOS_PER_HOUR), (int) ((j / NANOS_PER_MINUTE) % 60), (int) ((j / 1000000000) % 60), (int) (j % 1000000000));
    }

    public LocalTime minus(TemporalAmount temporalAmount) {
        return (LocalTime) temporalAmount.subtractFrom(this);
    }

    public LocalTime minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public LocalTime minusHours(long j) {
        return plusHours(-(j % 24));
    }

    public LocalTime minusMinutes(long j) {
        return plusMinutes(-(j % 1440));
    }

    public LocalTime minusSeconds(long j) {
        return plusSeconds(-(j % 86400));
    }

    public LocalTime minusNanos(long j) {
        return plusNanos(-(j % NANOS_PER_DAY));
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.NANOS;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return this;
        }
        if (!(temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset())) {
            if (temporalQuery != TemporalQueries.localDate()) {
                return temporalQuery.queryFrom(this);
            }
        }
        return null;
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.NANO_OF_DAY, toNanoOfDay());
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        long toNanoOfDay = temporal.toNanoOfDay() - toNanoOfDay();
        switch ((ChronoUnit) temporalUnit) {
            case NANOS:
                return toNanoOfDay;
            case MICROS:
                return toNanoOfDay / 1000;
            case MILLIS:
                return toNanoOfDay / 1000000;
            case SECONDS:
                return toNanoOfDay / 1000000000;
            case MINUTES:
                return toNanoOfDay / -129542144;
            case HOURS:
                return toNanoOfDay / 817405952;
            case HALF_DAYS:
                return toNanoOfDay / 1218936832;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public LocalDateTime atDate(LocalDate localDate) {
        return LocalDateTime.of(localDate, this);
    }

    public OffsetTime atOffset(ZoneOffset zoneOffset) {
        return OffsetTime.of(this, zoneOffset);
    }

    public int toSecondOfDay() {
        return ((this.hour * SECONDS_PER_HOUR) + (this.minute * 60)) + this.second;
    }

    public long toNanoOfDay() {
        return (((((long) this.hour) * NANOS_PER_HOUR) + (((long) this.minute) * NANOS_PER_MINUTE)) + (((long) this.second) * 1000000000)) + ((long) this.nano);
    }

    public int compareTo(LocalTime localTime) {
        int compareInts = Jdk8Methods.compareInts(this.hour, localTime.hour);
        if (compareInts != 0) {
            return compareInts;
        }
        compareInts = Jdk8Methods.compareInts(this.minute, localTime.minute);
        if (compareInts != 0) {
            return compareInts;
        }
        compareInts = Jdk8Methods.compareInts(this.second, localTime.second);
        return compareInts == 0 ? Jdk8Methods.compareInts(this.nano, localTime.nano) : compareInts;
    }

    public boolean isAfter(LocalTime localTime) {
        return compareTo(localTime) > null ? true : null;
    }

    public boolean isBefore(LocalTime localTime) {
        return compareTo(localTime) < null ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalTime)) {
            return false;
        }
        LocalTime localTime = (LocalTime) obj;
        if (this.hour != localTime.hour || this.minute != localTime.minute || this.second != localTime.second || this.nano != localTime.nano) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        long toNanoOfDay = toNanoOfDay();
        return (int) (toNanoOfDay ^ (toNanoOfDay >>> 32));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(18);
        byte b = this.hour;
        byte b2 = this.minute;
        byte b3 = this.second;
        int i = this.nano;
        stringBuilder.append(b < (byte) 10 ? "0" : "");
        stringBuilder.append(b);
        stringBuilder.append(b2 < (byte) 10 ? ":0" : ":");
        stringBuilder.append(b2);
        if (b3 > (byte) 0 || i > 0) {
            stringBuilder.append(b3 < (byte) 10 ? ":0" : ":");
            stringBuilder.append(b3);
            if (i > 0) {
                stringBuilder.append('.');
                if (i % 1000000 == 0) {
                    stringBuilder.append(Integer.toString((i / 1000000) + 1000).substring(1));
                } else if (i % 1000 == 0) {
                    stringBuilder.append(Integer.toString((i / 1000) + 1000000).substring(1));
                } else {
                    stringBuilder.append(Integer.toString(i + 1000000000).substring(1));
                }
            }
        }
        return stringBuilder.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 5, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        if (this.nano != 0) {
            dataOutput.writeByte(this.hour);
            dataOutput.writeByte(this.minute);
            dataOutput.writeByte(this.second);
            dataOutput.writeInt(this.nano);
        } else if (this.second != (byte) 0) {
            dataOutput.writeByte(this.hour);
            dataOutput.writeByte(this.minute);
            dataOutput.writeByte(this.second ^ -1);
        } else if (this.minute == (byte) 0) {
            dataOutput.writeByte(this.hour ^ -1);
        } else {
            dataOutput.writeByte(this.hour);
            dataOutput.writeByte(this.minute ^ -1);
        }
    }

    static LocalTime readExternal(DataInput dataInput) throws IOException {
        int i;
        int readByte = dataInput.readByte();
        int i2 = 0;
        if (readByte < 0) {
            readByte ^= -1;
            dataInput = null;
            i = 0;
        } else {
            i = dataInput.readByte();
            if (i < 0) {
                i ^= -1;
            } else {
                byte readByte2 = dataInput.readByte();
                if (readByte2 < (byte) 0) {
                    i2 = readByte2 ^ -1;
                } else {
                    dataInput = dataInput.readInt();
                    i2 = readByte2;
                }
            }
            dataInput = null;
        }
        return of(readByte, i, i2, dataInput);
    }
}
