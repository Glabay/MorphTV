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

public final class MinguoDate extends ChronoDateImpl<MinguoDate> implements Serializable {
    private static final long serialVersionUID = 1300372329181994526L;
    private final LocalDate isoDate;

    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static MinguoDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static MinguoDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static MinguoDate now(Clock clock) {
        return new MinguoDate(LocalDate.now(clock));
    }

    public static MinguoDate of(int i, int i2, int i3) {
        return MinguoChronology.INSTANCE.date(i, i2, i3);
    }

    public static MinguoDate from(TemporalAccessor temporalAccessor) {
        return MinguoChronology.INSTANCE.date(temporalAccessor);
    }

    MinguoDate(LocalDate localDate) {
        Jdk8Methods.requireNonNull(localDate, "date");
        this.isoDate = localDate;
    }

    public MinguoChronology getChronology() {
        return MinguoChronology.INSTANCE;
    }

    public MinguoEra getEra() {
        return (MinguoEra) super.getEra();
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
                    return ValueRange.of(1, getProlepticYear() <= 0 ? ((-temporalField.getMinimum()) + 1) + 1911 : temporalField.getMaximum() - 1911);
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
        return this.isoDate.getYear() - 1911;
    }

    public MinguoDate with(TemporalAdjuster temporalAdjuster) {
        return (MinguoDate) super.with(temporalAdjuster);
    }

    public MinguoDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (MinguoDate) temporalField.adjustInto(this, j);
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
                int i = C15501.$SwitchMap$org$threeten$bp$temporal$ChronoField[chronoField.ordinal()];
                if (i != 4) {
                    switch (i) {
                        case 6:
                            return with(this.isoDate.withYear(checkValidIntValue + 1911));
                        case 7:
                            return with(this.isoDate.withYear((1 - getProlepticYear()) + 1911));
                        default:
                            break;
                    }
                }
                return with(this.isoDate.withYear(getProlepticYear() >= 1 ? checkValidIntValue + 1911 : (1 - checkValidIntValue) + 1911));
            case PROLEPTIC_MONTH:
                getChronology().range(chronoField).checkValidValue(j, chronoField);
                return plusMonths(j - getProlepticMonth());
            default:
                break;
        }
        return with(this.isoDate.with(temporalField, j));
    }

    public MinguoDate plus(TemporalAmount temporalAmount) {
        return (MinguoDate) super.plus(temporalAmount);
    }

    public MinguoDate plus(long j, TemporalUnit temporalUnit) {
        return (MinguoDate) super.plus(j, temporalUnit);
    }

    public MinguoDate minus(TemporalAmount temporalAmount) {
        return (MinguoDate) super.minus(temporalAmount);
    }

    public MinguoDate minus(long j, TemporalUnit temporalUnit) {
        return (MinguoDate) super.minus(j, temporalUnit);
    }

    MinguoDate plusYears(long j) {
        return with(this.isoDate.plusYears(j));
    }

    MinguoDate plusMonths(long j) {
        return with(this.isoDate.plusMonths(j));
    }

    MinguoDate plusDays(long j) {
        return with(this.isoDate.plusDays(j));
    }

    private MinguoDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new MinguoDate(localDate);
    }

    public final ChronoLocalDateTime<MinguoDate> atTime(LocalTime localTime) {
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
        if (!(obj instanceof MinguoDate)) {
            return null;
        }
        return this.isoDate.equals(((MinguoDate) obj).isoDate);
    }

    public int hashCode() {
        return getChronology().getId().hashCode() ^ this.isoDate.hashCode();
    }

    private Object writeReplace() {
        return new Ser((byte) 5, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static ChronoLocalDate readExternal(DataInput dataInput) throws IOException {
        return MinguoChronology.INSTANCE.date(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
