package org.threeten.bp;

import com.google.android.exoplayer2.C0649C;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.SignStyle;
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

public final class YearMonth extends DefaultInterfaceTemporalAccessor implements Temporal, TemporalAdjuster, Comparable<YearMonth>, Serializable {
    public static final TemporalQuery<YearMonth> FROM = new C15301();
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 2).toFormatter();
    private static final long serialVersionUID = 4183400860270640070L;
    private final int month;
    private final int year;

    /* renamed from: org.threeten.bp.YearMonth$1 */
    static class C15301 implements TemporalQuery<YearMonth> {
        C15301() {
        }

        public YearMonth queryFrom(TemporalAccessor temporalAccessor) {
            return YearMonth.from(temporalAccessor);
        }
    }

    public static YearMonth now() {
        return now(Clock.systemDefaultZone());
    }

    public static YearMonth now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static YearMonth now(Clock clock) {
        clock = LocalDate.now(clock);
        return of(clock.getYear(), clock.getMonth());
    }

    public static YearMonth of(int i, Month month) {
        Jdk8Methods.requireNonNull(month, "month");
        return of(i, month.getValue());
    }

    public static YearMonth of(int i, int i2) {
        ChronoField.YEAR.checkValidValue((long) i);
        ChronoField.MONTH_OF_YEAR.checkValidValue((long) i2);
        return new YearMonth(i, i2);
    }

    public static org.threeten.bp.YearMonth from(org.threeten.bp.temporal.TemporalAccessor r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r3 instanceof org.threeten.bp.YearMonth;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r3 = (org.threeten.bp.YearMonth) r3;
        return r3;
    L_0x0007:
        r0 = org.threeten.bp.chrono.IsoChronology.INSTANCE;	 Catch:{ DateTimeException -> 0x0029 }
        r1 = org.threeten.bp.chrono.Chronology.from(r3);	 Catch:{ DateTimeException -> 0x0029 }
        r0 = r0.equals(r1);	 Catch:{ DateTimeException -> 0x0029 }
        if (r0 != 0) goto L_0x0018;	 Catch:{ DateTimeException -> 0x0029 }
    L_0x0013:
        r0 = org.threeten.bp.LocalDate.from(r3);	 Catch:{ DateTimeException -> 0x0029 }
        r3 = r0;	 Catch:{ DateTimeException -> 0x0029 }
    L_0x0018:
        r0 = org.threeten.bp.temporal.ChronoField.YEAR;	 Catch:{ DateTimeException -> 0x0029 }
        r0 = r3.get(r0);	 Catch:{ DateTimeException -> 0x0029 }
        r1 = org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;	 Catch:{ DateTimeException -> 0x0029 }
        r1 = r3.get(r1);	 Catch:{ DateTimeException -> 0x0029 }
        r0 = of(r0, r1);	 Catch:{ DateTimeException -> 0x0029 }
        return r0;
    L_0x0029:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to obtain YearMonth from TemporalAccessor: ";
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
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.YearMonth.from(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.YearMonth");
    }

    public static YearMonth parse(CharSequence charSequence) {
        return parse(charSequence, PARSER);
    }

    public static YearMonth parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (YearMonth) dateTimeFormatter.parse(charSequence, FROM);
    }

    private YearMonth(int i, int i2) {
        this.year = i;
        this.month = i2;
    }

    private YearMonth with(int i, int i2) {
        if (this.year == i && this.month == i2) {
            return this;
        }
        return new YearMonth(i, i2);
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = true;
        if (temporalField instanceof ChronoField) {
            if (!(temporalField == ChronoField.YEAR || temporalField == ChronoField.MONTH_OF_YEAR || temporalField == ChronoField.PROLEPTIC_MONTH || temporalField == ChronoField.YEAR_OF_ERA)) {
                if (temporalField != ChronoField.ERA) {
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
            if (!(temporalUnit == ChronoUnit.MONTHS || temporalUnit == ChronoUnit.YEARS || temporalUnit == ChronoUnit.DECADES || temporalUnit == ChronoUnit.CENTURIES || temporalUnit == ChronoUnit.MILLENNIA)) {
                if (temporalUnit != ChronoUnit.ERAS) {
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
        if (temporalField != ChronoField.YEAR_OF_ERA) {
            return super.range(temporalField);
        }
        return ValueRange.of(1, getYear() <= null ? C0649C.NANOS_PER_SECOND : 999999999);
    }

    public int get(TemporalField temporalField) {
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        int i = 1;
        switch ((ChronoField) temporalField) {
            case MONTH_OF_YEAR:
                return (long) this.month;
            case PROLEPTIC_MONTH:
                return getProlepticMonth();
            case YEAR_OF_ERA:
                return (long) (this.year < 1 ? 1 - this.year : this.year);
            case YEAR:
                return (long) this.year;
            case ERA:
                if (this.year < 1) {
                    i = 0;
                }
                return (long) i;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    private long getProlepticMonth() {
        return (((long) this.year) * 12) + ((long) (this.month - 1));
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

    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear((long) this.year);
    }

    public boolean isValidDay(int i) {
        return i >= 1 && i <= lengthOfMonth();
    }

    public int lengthOfMonth() {
        return getMonth().length(isLeapYear());
    }

    public int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    public YearMonth with(TemporalAdjuster temporalAdjuster) {
        return (YearMonth) temporalAdjuster.adjustInto(this);
    }

    public YearMonth with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (YearMonth) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        chronoField.checkValidValue(j);
        switch (chronoField) {
            case MONTH_OF_YEAR:
                return withMonth((int) j);
            case PROLEPTIC_MONTH:
                return plusMonths(j - getLong(ChronoField.PROLEPTIC_MONTH));
            case YEAR_OF_ERA:
                if (this.year < 1) {
                    j = 1 - j;
                }
                return withYear((int) j);
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

    public YearMonth withYear(int i) {
        ChronoField.YEAR.checkValidValue((long) i);
        return with(i, this.month);
    }

    public YearMonth withMonth(int i) {
        ChronoField.MONTH_OF_YEAR.checkValidValue((long) i);
        return with(this.year, i);
    }

    public YearMonth plus(TemporalAmount temporalAmount) {
        return (YearMonth) temporalAmount.addTo(this);
    }

    public YearMonth plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (YearMonth) temporalUnit.addTo(this, j);
        }
        switch ((ChronoUnit) temporalUnit) {
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

    public YearMonth plusYears(long j) {
        if (j == 0) {
            return this;
        }
        return with(ChronoField.YEAR.checkValidIntValue(((long) this.year) + j), this.month);
    }

    public YearMonth plusMonths(long j) {
        if (j == 0) {
            return this;
        }
        long j2 = ((((long) this.year) * 12) + ((long) (this.month - 1))) + j;
        return with(ChronoField.YEAR.checkValidIntValue(Jdk8Methods.floorDiv(j2, 12)), Jdk8Methods.floorMod(j2, 12) + 1);
    }

    public YearMonth minus(TemporalAmount temporalAmount) {
        return (YearMonth) temporalAmount.subtractFrom(this);
    }

    public YearMonth minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public YearMonth minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    public YearMonth minusMonths(long j) {
        return j == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-j);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.MONTHS;
        }
        if (!(temporalQuery == TemporalQueries.localDate() || temporalQuery == TemporalQueries.localTime() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId())) {
            if (temporalQuery != TemporalQueries.offset()) {
                return super.query(temporalQuery);
            }
        }
        return null;
    }

    public Temporal adjustInto(Temporal temporal) {
        if (Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            return temporal.with(ChronoField.PROLEPTIC_MONTH, getProlepticMonth());
        }
        throw new DateTimeException("Adjustment only supported on ISO date-time");
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        long prolepticMonth = temporal.getProlepticMonth() - getProlepticMonth();
        switch ((ChronoUnit) temporalUnit) {
            case MONTHS:
                return prolepticMonth;
            case YEARS:
                return prolepticMonth / 12;
            case DECADES:
                return prolepticMonth / 120;
            case CENTURIES:
                return prolepticMonth / 1200;
            case MILLENNIA:
                return prolepticMonth / 12000;
            case ERAS:
                return temporal.getLong(ChronoField.ERA) - getLong(ChronoField.ERA);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported unit: ");
                stringBuilder.append(temporalUnit);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public LocalDate atDay(int i) {
        return LocalDate.of(this.year, this.month, i);
    }

    public LocalDate atEndOfMonth() {
        return LocalDate.of(this.year, this.month, lengthOfMonth());
    }

    public int compareTo(YearMonth yearMonth) {
        int i = this.year - yearMonth.year;
        return i == 0 ? this.month - yearMonth.month : i;
    }

    public boolean isAfter(YearMonth yearMonth) {
        return compareTo(yearMonth) > null ? true : null;
    }

    public boolean isBefore(YearMonth yearMonth) {
        return compareTo(yearMonth) < null ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof YearMonth)) {
            return false;
        }
        YearMonth yearMonth = (YearMonth) obj;
        if (this.year != yearMonth.year || this.month != yearMonth.month) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.year ^ (this.month << 27);
    }

    public String toString() {
        int abs = Math.abs(this.year);
        StringBuilder stringBuilder = new StringBuilder(9);
        if (abs >= 1000) {
            stringBuilder.append(this.year);
        } else if (this.year < 0) {
            stringBuilder.append(this.year - 10000);
            stringBuilder.deleteCharAt(1);
        } else {
            stringBuilder.append(this.year + 10000);
            stringBuilder.deleteCharAt(0);
        }
        stringBuilder.append(this.month < 10 ? "-0" : "-");
        stringBuilder.append(this.month);
        return stringBuilder.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 68, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeByte(this.month);
    }

    static YearMonth readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readByte());
    }
}
