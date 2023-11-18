package org.threeten.bp.chrono;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import org.threeten.bp.Clock;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;

public final class ThaiBuddhistDate extends ChronoDateImpl<ThaiBuddhistDate> implements Serializable {
    private static final long serialVersionUID = -8722293800195731463L;
    private final LocalDate isoDate;

    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static ThaiBuddhistDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static ThaiBuddhistDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static ThaiBuddhistDate now(Clock clock) {
        return new ThaiBuddhistDate(LocalDate.now(clock));
    }

    public static ThaiBuddhistDate of(int i, int i2, int i3) {
        return ThaiBuddhistChronology.INSTANCE.date(i, i2, i3);
    }

    public static ThaiBuddhistDate from(TemporalAccessor temporalAccessor) {
        return ThaiBuddhistChronology.INSTANCE.date(temporalAccessor);
    }

    ThaiBuddhistDate(LocalDate localDate) {
        Jdk8Methods.requireNonNull(localDate, "date");
        this.isoDate = localDate;
    }

    public ThaiBuddhistChronology getChronology() {
        return ThaiBuddhistChronology.INSTANCE;
    }

    public ThaiBuddhistEra getEra() {
        return (ThaiBuddhistEra) super.getEra();
    }

    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (isSupported(temporalField)) {
            ChronoField chronoField = (ChronoField) temporalField;
            switch (chronoField) {
                case DAY_OF_MONTH:
                case DAY_OF_YEAR:
                case ALIGNED_WEEK_OF_MONTH:
                    return this.isoDate.range(temporalField);
                case YEAR_OF_ERA:
                    temporalField = ChronoField.YEAR.range();
                    return ValueRange.of(1, getProlepticYear() <= 0 ? (-(temporalField.getMinimum() + 543)) + 1 : temporalField.getMaximum() + 543);
                default:
                    return getChronology().range(chronoField);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        int i = 1;
        switch ((ChronoField) temporalField) {
            case YEAR_OF_ERA:
                temporalField = getProlepticYear();
                if (temporalField < 1) {
                    temporalField = 1 - temporalField;
                }
                return (long) temporalField;
            case PROLEPTIC_MONTH:
                return getProlepticMonth();
            case YEAR:
                return (long) getProlepticYear();
            case ERA:
                if (getProlepticYear() < 1) {
                    i = 0;
                }
                return (long) i;
            default:
                return this.isoDate.getLong(temporalField);
        }
    }

    private long getProlepticMonth() {
        return ((((long) getProlepticYear()) * 12) + ((long) this.isoDate.getMonthValue())) - 1;
    }

    private int getProlepticYear() {
        return this.isoDate.getYear() + 543;
    }

    public ThaiBuddhistDate with(TemporalAdjuster temporalAdjuster) {
        return (ThaiBuddhistDate) super.with(temporalAdjuster);
    }

    public ThaiBuddhistDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (ThaiBuddhistDate) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        if (getLong(chronoField) == j) {
            return this;
        }
        switch (chronoField) {
            case YEAR_OF_ERA:
            case YEAR:
            case ERA:
                int checkValidIntValue = getChronology().range(chronoField).checkValidIntValue(j, chronoField);
                int i = C15521.$SwitchMap$org$threeten$bp$temporal$ChronoField[chronoField.ordinal()];
                if (i != 4) {
                    switch (i) {
                        case 6:
                            return with(this.isoDate.withYear(checkValidIntValue - 543));
                        case 7:
                            return with(this.isoDate.withYear((1 - getProlepticYear()) - 543));
                        default:
                            break;
                    }
                }
                temporalField = this.isoDate;
                if (getProlepticYear() < 1) {
                    checkValidIntValue = 1 - checkValidIntValue;
                }
                return with(temporalField.withYear(checkValidIntValue - 543));
            case PROLEPTIC_MONTH:
                getChronology().range(chronoField).checkValidValue(j, chronoField);
                return plusMonths(j - getProlepticMonth());
            default:
                break;
        }
        return with(this.isoDate.with(temporalField, j));
    }

    public ThaiBuddhistDate plus(TemporalAmount temporalAmount) {
        return (ThaiBuddhistDate) super.plus(temporalAmount);
    }

    public ThaiBuddhistDate plus(long j, TemporalUnit temporalUnit) {
        return (ThaiBuddhistDate) super.plus(j, temporalUnit);
    }

    public ThaiBuddhistDate minus(TemporalAmount temporalAmount) {
        return (ThaiBuddhistDate) super.minus(temporalAmount);
    }

    public ThaiBuddhistDate minus(long j, TemporalUnit temporalUnit) {
        return (ThaiBuddhistDate) super.minus(j, temporalUnit);
    }

    ThaiBuddhistDate plusYears(long j) {
        return with(this.isoDate.plusYears(j));
    }

    ThaiBuddhistDate plusMonths(long j) {
        return with(this.isoDate.plusMonths(j));
    }

    ThaiBuddhistDate plusDays(long j) {
        return with(this.isoDate.plusDays(j));
    }

    private ThaiBuddhistDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new ThaiBuddhistDate(localDate);
    }

    public final ChronoLocalDateTime<ThaiBuddhistDate> atTime(LocalTime localTime) {
        return super.atTime(localTime);
    }

    public ChronoPeriod until(ChronoLocalDate chronoLocalDate) {
        chronoLocalDate = this.isoDate.until(chronoLocalDate);
        return getChronology().period(chronoLocalDate.getYears(), chronoLocalDate.getMonths(), chronoLocalDate.getDays());
    }

    public long toEpochDay() {
        return this.isoDate.toEpochDay();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ThaiBuddhistDate)) {
            return null;
        }
        return this.isoDate.equals(((ThaiBuddhistDate) obj).isoDate);
    }

    public int hashCode() {
        return getChronology().getId().hashCode() ^ this.isoDate.hashCode();
    }

    private Object writeReplace() {
        return new Ser((byte) 7, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static ChronoLocalDate readExternal(DataInput dataInput) throws IOException {
        return ThaiBuddhistChronology.INSTANCE.date(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
