package org.threeten.bp.chrono;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Calendar;
import org.threeten.bp.Clock;
import org.threeten.bp.DateTimeException;
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

public final class JapaneseDate extends ChronoDateImpl<JapaneseDate> implements Serializable {
    static final LocalDate MIN_DATE = LocalDate.of(1873, 1, 1);
    private static final long serialVersionUID = -305327627230580483L;
    private transient JapaneseEra era;
    private final LocalDate isoDate;
    private transient int yearOfEra;

    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static JapaneseDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static JapaneseDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static JapaneseDate now(Clock clock) {
        return new JapaneseDate(LocalDate.now(clock));
    }

    public static JapaneseDate of(JapaneseEra japaneseEra, int i, int i2, int i3) {
        Jdk8Methods.requireNonNull(japaneseEra, "era");
        if (i < 1) {
            i2 = new StringBuilder();
            i2.append("Invalid YearOfEra: ");
            i2.append(i);
            throw new DateTimeException(i2.toString());
        }
        ChronoLocalDate startDate = japaneseEra.startDate();
        ChronoLocalDate endDate = japaneseEra.endDate();
        i2 = LocalDate.of((startDate.getYear() - 1) + i, i2, i3);
        if (i2.isBefore(startDate) == 0) {
            if (i2.isAfter(endDate) == 0) {
                return new JapaneseDate(japaneseEra, i, i2);
            }
        }
        i2 = new StringBuilder();
        i2.append("Requested date is outside bounds of era ");
        i2.append(japaneseEra);
        throw new DateTimeException(i2.toString());
    }

    static JapaneseDate ofYearDay(JapaneseEra japaneseEra, int i, int i2) {
        Jdk8Methods.requireNonNull(japaneseEra, "era");
        if (i < 1) {
            i2 = new StringBuilder();
            i2.append("Invalid YearOfEra: ");
            i2.append(i);
            throw new DateTimeException(i2.toString());
        }
        ChronoLocalDate startDate = japaneseEra.startDate();
        ChronoLocalDate endDate = japaneseEra.endDate();
        if (i == 1) {
            i2 += startDate.getDayOfYear() - 1;
            if (i2 > startDate.lengthOfYear()) {
                i2 = new StringBuilder();
                i2.append("DayOfYear exceeds maximum allowed in the first year of era ");
                i2.append(japaneseEra);
                throw new DateTimeException(i2.toString());
            }
        }
        i2 = LocalDate.ofYearDay((startDate.getYear() - 1) + i, i2);
        if (!i2.isBefore(startDate)) {
            if (!i2.isAfter(endDate)) {
                return new JapaneseDate(japaneseEra, i, i2);
            }
        }
        i2 = new StringBuilder();
        i2.append("Requested date is outside bounds of era ");
        i2.append(japaneseEra);
        throw new DateTimeException(i2.toString());
    }

    public static JapaneseDate of(int i, int i2, int i3) {
        return new JapaneseDate(LocalDate.of(i, i2, i3));
    }

    public static JapaneseDate from(TemporalAccessor temporalAccessor) {
        return JapaneseChronology.INSTANCE.date(temporalAccessor);
    }

    JapaneseDate(LocalDate localDate) {
        if (localDate.isBefore(MIN_DATE)) {
            throw new DateTimeException("Minimum supported date is January 1st Meiji 6");
        }
        this.era = JapaneseEra.from(localDate);
        this.yearOfEra = localDate.getYear() - (this.era.startDate().getYear() - 1);
        this.isoDate = localDate;
    }

    JapaneseDate(JapaneseEra japaneseEra, int i, LocalDate localDate) {
        if (localDate.isBefore(MIN_DATE)) {
            throw new DateTimeException("Minimum supported date is January 1st Meiji 6");
        }
        this.era = japaneseEra;
        this.yearOfEra = i;
        this.isoDate = localDate;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.era = JapaneseEra.from(this.isoDate);
        this.yearOfEra = this.isoDate.getYear() - (this.era.startDate().getYear() - 1);
    }

    public JapaneseChronology getChronology() {
        return JapaneseChronology.INSTANCE;
    }

    public JapaneseEra getEra() {
        return this.era;
    }

    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
    }

    public int lengthOfYear() {
        Calendar instance = Calendar.getInstance(JapaneseChronology.LOCALE);
        instance.set(0, this.era.getValue() + 2);
        instance.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
        return instance.getActualMaximum(6);
    }

    public boolean isSupported(TemporalField temporalField) {
        if (!(temporalField == ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH || temporalField == ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR || temporalField == ChronoField.ALIGNED_WEEK_OF_MONTH)) {
            if (temporalField != ChronoField.ALIGNED_WEEK_OF_YEAR) {
                return super.isSupported(temporalField);
            }
        }
        return null;
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (isSupported(temporalField)) {
            ChronoField chronoField = (ChronoField) temporalField;
            switch (chronoField) {
                case DAY_OF_YEAR:
                    return actualRange(6);
                case YEAR_OF_ERA:
                    return actualRange(1);
                default:
                    return getChronology().range(chronoField);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    private ValueRange actualRange(int i) {
        Calendar instance = Calendar.getInstance(JapaneseChronology.LOCALE);
        instance.set(0, this.era.getValue() + 2);
        instance.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
        return ValueRange.of((long) instance.getActualMinimum(i), (long) instance.getActualMaximum(i));
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField) temporalField) {
            case DAY_OF_YEAR:
                return getDayOfYear();
            case YEAR_OF_ERA:
                return (long) this.yearOfEra;
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
            case ALIGNED_WEEK_OF_MONTH:
            case ALIGNED_WEEK_OF_YEAR:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
            case ERA:
                return (long) this.era.getValue();
            default:
                return this.isoDate.getLong(temporalField);
        }
    }

    private long getDayOfYear() {
        if (this.yearOfEra == 1) {
            return (long) ((this.isoDate.getDayOfYear() - this.era.startDate().getDayOfYear()) + 1);
        }
        return (long) this.isoDate.getDayOfYear();
    }

    public JapaneseDate with(TemporalAdjuster temporalAdjuster) {
        return (JapaneseDate) super.with(temporalAdjuster);
    }

    public JapaneseDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (JapaneseDate) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        if (getLong(chronoField) == j) {
            return this;
        }
        int i = C15481.$SwitchMap$org$threeten$bp$temporal$ChronoField[chronoField.ordinal()];
        if (i != 7) {
            switch (i) {
                case 1:
                case 2:
                    break;
                default:
                    break;
            }
        }
        i = getChronology().range(chronoField).checkValidIntValue(j, chronoField);
        int i2 = C15481.$SwitchMap$org$threeten$bp$temporal$ChronoField[chronoField.ordinal()];
        if (i2 == 7) {
            return withYear(JapaneseEra.of(i), this.yearOfEra);
        }
        switch (i2) {
            case 1:
                return with(this.isoDate.plusDays(((long) i) - getDayOfYear()));
            case 2:
                return withYear(i);
        }
        return with(this.isoDate.with(temporalField, j));
    }

    public JapaneseDate plus(TemporalAmount temporalAmount) {
        return (JapaneseDate) super.plus(temporalAmount);
    }

    public JapaneseDate plus(long j, TemporalUnit temporalUnit) {
        return (JapaneseDate) super.plus(j, temporalUnit);
    }

    public JapaneseDate minus(TemporalAmount temporalAmount) {
        return (JapaneseDate) super.minus(temporalAmount);
    }

    public JapaneseDate minus(long j, TemporalUnit temporalUnit) {
        return (JapaneseDate) super.minus(j, temporalUnit);
    }

    private JapaneseDate withYear(JapaneseEra japaneseEra, int i) {
        return with(this.isoDate.withYear(JapaneseChronology.INSTANCE.prolepticYear(japaneseEra, i)));
    }

    private JapaneseDate withYear(int i) {
        return withYear(getEra(), i);
    }

    JapaneseDate plusYears(long j) {
        return with(this.isoDate.plusYears(j));
    }

    JapaneseDate plusMonths(long j) {
        return with(this.isoDate.plusMonths(j));
    }

    JapaneseDate plusDays(long j) {
        return with(this.isoDate.plusDays(j));
    }

    private JapaneseDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new JapaneseDate(localDate);
    }

    public final ChronoLocalDateTime<JapaneseDate> atTime(LocalTime localTime) {
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
        if (!(obj instanceof JapaneseDate)) {
            return null;
        }
        return this.isoDate.equals(((JapaneseDate) obj).isoDate);
    }

    public int hashCode() {
        return getChronology().getId().hashCode() ^ this.isoDate.hashCode();
    }

    private Object writeReplace() {
        return new Ser((byte) 1, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static ChronoLocalDate readExternal(DataInput dataInput) throws IOException {
        return JapaneseChronology.INSTANCE.date(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
