package org.threeten.bp.chrono;

import com.google.android.exoplayer2.C0649C;
import com.google.common.base.Ascii;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.ValueRange;

final class ChronoLocalDateTimeImpl<D extends ChronoLocalDate> extends ChronoLocalDateTime<D> implements Temporal, TemporalAdjuster, Serializable {
    private static final int HOURS_PER_DAY = 24;
    private static final long MICROS_PER_DAY = 86400000000L;
    private static final long MILLIS_PER_DAY = 86400000;
    private static final int MINUTES_PER_DAY = 1440;
    private static final int MINUTES_PER_HOUR = 60;
    private static final long NANOS_PER_DAY = 86400000000000L;
    private static final long NANOS_PER_HOUR = 3600000000000L;
    private static final long NANOS_PER_MINUTE = 60000000000L;
    private static final long NANOS_PER_SECOND = 1000000000;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final long serialVersionUID = 4556003607393004514L;
    private final D date;
    private final LocalTime time;

    static <R extends ChronoLocalDate> ChronoLocalDateTimeImpl<R> of(R r, LocalTime localTime) {
        return new ChronoLocalDateTimeImpl(r, localTime);
    }

    private ChronoLocalDateTimeImpl(D d, LocalTime localTime) {
        Jdk8Methods.requireNonNull(d, "date");
        Jdk8Methods.requireNonNull(localTime, "time");
        this.date = d;
        this.time = localTime;
    }

    private ChronoLocalDateTimeImpl<D> with(Temporal temporal, LocalTime localTime) {
        if (this.date == temporal && this.time == localTime) {
            return this;
        }
        return new ChronoLocalDateTimeImpl(this.date.getChronology().ensureChronoLocalDate(temporal), localTime);
    }

    public D toLocalDate() {
        return this.date;
    }

    public LocalTime toLocalTime() {
        return this.time;
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
            return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
        }
        return temporalField.isTimeBased() ? this.time.get(temporalField) : this.date.get(temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        return temporalField.isTimeBased() ? this.time.getLong(temporalField) : this.date.getLong(temporalField);
    }

    public ChronoLocalDateTimeImpl<D> with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof ChronoLocalDate) {
            return with((ChronoLocalDate) temporalAdjuster, this.time);
        }
        if (temporalAdjuster instanceof LocalTime) {
            return with(this.date, (LocalTime) temporalAdjuster);
        }
        if (temporalAdjuster instanceof ChronoLocalDateTimeImpl) {
            return this.date.getChronology().ensureChronoLocalDateTime((ChronoLocalDateTimeImpl) temporalAdjuster);
        }
        return this.date.getChronology().ensureChronoLocalDateTime((ChronoLocalDateTimeImpl) temporalAdjuster.adjustInto(this));
    }

    public ChronoLocalDateTimeImpl<D> with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return this.date.getChronology().ensureChronoLocalDateTime(temporalField.adjustInto(this, j));
        }
        if (temporalField.isTimeBased()) {
            return with(this.date, this.time.with(temporalField, j));
        }
        return with(this.date.with(temporalField, j), this.time);
    }

    public ChronoLocalDateTimeImpl<D> plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return this.date.getChronology().ensureChronoLocalDateTime(temporalUnit.addTo(this, j));
        }
        switch ((ChronoUnit) temporalUnit) {
            case NANOS:
                return plusNanos(j);
            case MICROS:
                return plusDays(j / MICROS_PER_DAY).plusNanos((j % MICROS_PER_DAY) * 1000);
            case MILLIS:
                return plusDays(j / 86400000).plusNanos((j % 86400000) * C0649C.MICROS_PER_SECOND);
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

    private ChronoLocalDateTimeImpl<D> plusDays(long j) {
        return with(this.date.plus(j, ChronoUnit.DAYS), this.time);
    }

    private ChronoLocalDateTimeImpl<D> plusHours(long j) {
        return plusWithOverflow(this.date, j, 0, 0, 0);
    }

    private ChronoLocalDateTimeImpl<D> plusMinutes(long j) {
        return plusWithOverflow(this.date, 0, j, 0, 0);
    }

    ChronoLocalDateTimeImpl<D> plusSeconds(long j) {
        return plusWithOverflow(this.date, 0, 0, j, 0);
    }

    private ChronoLocalDateTimeImpl<D> plusNanos(long j) {
        return plusWithOverflow(this.date, 0, 0, 0, j);
    }

    private ChronoLocalDateTimeImpl<D> plusWithOverflow(D d, long j, long j2, long j3, long j4) {
        ChronoLocalDateTimeImpl chronoLocalDateTimeImpl = this;
        Temporal temporal = d;
        if ((((j | j2) | j3) | j4) == 0) {
            return with(temporal, chronoLocalDateTimeImpl.time);
        }
        long j5 = (((j4 / NANOS_PER_DAY) + (j3 / 86400)) + (j2 / 1440)) + (j / 24);
        long j6 = (((j4 % NANOS_PER_DAY) + ((j3 % 86400) * 1000000000)) + ((j2 % 1440) * NANOS_PER_MINUTE)) + ((j % 24) * NANOS_PER_HOUR);
        long toNanoOfDay = chronoLocalDateTimeImpl.time.toNanoOfDay();
        long j7 = j6 + toNanoOfDay;
        long floorDiv = j5 + Jdk8Methods.floorDiv(j7, (long) NANOS_PER_DAY);
        j6 = Jdk8Methods.floorMod(j7, (long) NANOS_PER_DAY);
        return with(temporal.plus(floorDiv, ChronoUnit.DAYS), j6 == toNanoOfDay ? chronoLocalDateTimeImpl.time : LocalTime.ofNanoOfDay(j6));
    }

    public ChronoZonedDateTime<D> atZone(ZoneId zoneId) {
        return ChronoZonedDateTimeImpl.ofBest(this, zoneId, null);
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = toLocalDate().getChronology().localDateTime(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        ChronoUnit chronoUnit = (ChronoUnit) temporalUnit;
        if (chronoUnit.isTimeBased()) {
            long j = temporal.getLong(ChronoField.EPOCH_DAY) - this.date.getLong(ChronoField.EPOCH_DAY);
            switch (chronoUnit) {
                case NANOS:
                    j = Jdk8Methods.safeMultiply(j, (long) NANOS_PER_DAY);
                    break;
                case MICROS:
                    j = Jdk8Methods.safeMultiply(j, (long) MICROS_PER_DAY);
                    break;
                case MILLIS:
                    j = Jdk8Methods.safeMultiply(j, 86400000);
                    break;
                case SECONDS:
                    j = Jdk8Methods.safeMultiply(j, (int) SECONDS_PER_DAY);
                    break;
                case MINUTES:
                    j = Jdk8Methods.safeMultiply(j, (int) MINUTES_PER_DAY);
                    break;
                case HOURS:
                    j = Jdk8Methods.safeMultiply(j, 24);
                    break;
                case HALF_DAYS:
                    j = Jdk8Methods.safeMultiply(j, 2);
                    break;
                default:
                    break;
            }
            return Jdk8Methods.safeAdd(j, this.time.until(temporal.toLocalTime(), temporalUnit));
        }
        Temporal toLocalDate = temporal.toLocalDate();
        if (temporal.toLocalTime().isBefore(this.time) != null) {
            toLocalDate = toLocalDate.minus(1, ChronoUnit.DAYS);
        }
        return this.date.until(toLocalDate, temporalUnit);
    }

    private Object writeReplace() {
        return new Ser(Ascii.FF, this);
    }

    void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.date);
        objectOutput.writeObject(this.time);
    }

    static ChronoLocalDateTime<?> readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((ChronoLocalDate) objectInput.readObject()).atTime((LocalTime) objectInput.readObject());
    }
}
