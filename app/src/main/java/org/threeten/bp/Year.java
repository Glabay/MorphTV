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

public final class Year extends DefaultInterfaceTemporalAccessor implements Temporal, TemporalAdjuster, Comparable<Year>, Serializable {
    public static final TemporalQuery<Year> FROM = new C15281();
    public static final int MAX_VALUE = 999999999;
    public static final int MIN_VALUE = -999999999;
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).toFormatter();
    private static final long serialVersionUID = -23038383694477807L;
    private final int year;

    /* renamed from: org.threeten.bp.Year$1 */
    static class C15281 implements TemporalQuery<Year> {
        C15281() {
        }

        public Year queryFrom(TemporalAccessor temporalAccessor) {
            return Year.from(temporalAccessor);
        }
    }

    public static Year now() {
        return now(Clock.systemDefaultZone());
    }

    public static Year now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static Year now(Clock clock) {
        return of(LocalDate.now(clock).getYear());
    }

    public static Year of(int i) {
        ChronoField.YEAR.checkValidValue((long) i);
        return new Year(i);
    }

    public static org.threeten.bp.Year from(org.threeten.bp.temporal.TemporalAccessor r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r3 instanceof org.threeten.bp.Year;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r3 = (org.threeten.bp.Year) r3;
        return r3;
    L_0x0007:
        r0 = org.threeten.bp.chrono.IsoChronology.INSTANCE;	 Catch:{ DateTimeException -> 0x0023 }
        r1 = org.threeten.bp.chrono.Chronology.from(r3);	 Catch:{ DateTimeException -> 0x0023 }
        r0 = r0.equals(r1);	 Catch:{ DateTimeException -> 0x0023 }
        if (r0 != 0) goto L_0x0018;	 Catch:{ DateTimeException -> 0x0023 }
    L_0x0013:
        r0 = org.threeten.bp.LocalDate.from(r3);	 Catch:{ DateTimeException -> 0x0023 }
        r3 = r0;	 Catch:{ DateTimeException -> 0x0023 }
    L_0x0018:
        r0 = org.threeten.bp.temporal.ChronoField.YEAR;	 Catch:{ DateTimeException -> 0x0023 }
        r0 = r3.get(r0);	 Catch:{ DateTimeException -> 0x0023 }
        r0 = of(r0);	 Catch:{ DateTimeException -> 0x0023 }
        return r0;
    L_0x0023:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to obtain Year from TemporalAccessor: ";
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
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.Year.from(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.Year");
    }

    public static Year parse(CharSequence charSequence) {
        return parse(charSequence, PARSER);
    }

    public static Year parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (Year) dateTimeFormatter.parse(charSequence, FROM);
    }

    public static boolean isLeap(long j) {
        return ((j & 3) != 0 || (j % 100 == 0 && j % 400 != 0)) ? 0 : 1;
    }

    private Year(int i) {
        this.year = i;
    }

    public int getValue() {
        return this.year;
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = true;
        if (temporalField instanceof ChronoField) {
            if (!(temporalField == ChronoField.YEAR || temporalField == ChronoField.YEAR_OF_ERA)) {
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
            if (!(temporalUnit == ChronoUnit.YEARS || temporalUnit == ChronoUnit.DECADES || temporalUnit == ChronoUnit.CENTURIES || temporalUnit == ChronoUnit.MILLENNIA)) {
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
        return ValueRange.of(1, this.year <= null ? C0649C.NANOS_PER_SECOND : 999999999);
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

    public boolean isLeap() {
        return isLeap((long) this.year);
    }

    public boolean isValidMonthDay(MonthDay monthDay) {
        return (monthDay == null || monthDay.isValidYear(this.year) == null) ? null : true;
    }

    public int length() {
        return isLeap() ? 366 : 365;
    }

    public Year with(TemporalAdjuster temporalAdjuster) {
        return (Year) temporalAdjuster.adjustInto(this);
    }

    public Year with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (Year) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        chronoField.checkValidValue(j);
        switch (chronoField) {
            case YEAR_OF_ERA:
                if (this.year < 1) {
                    j = 1 - j;
                }
                return of((int) j);
            case YEAR:
                return of((int) j);
            case ERA:
                return getLong(ChronoField.ERA) == j ? this : of(1 - this.year);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public Year plus(TemporalAmount temporalAmount) {
        return (Year) temporalAmount.addTo(this);
    }

    public Year plus(long j, TemporalUnit temporalUnit) {
        if (!(temporalUnit instanceof ChronoUnit)) {
            return (Year) temporalUnit.addTo(this, j);
        }
        switch ((ChronoUnit) temporalUnit) {
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

    public Year plusYears(long j) {
        return j == 0 ? this : of(ChronoField.YEAR.checkValidIntValue(((long) this.year) + j));
    }

    public Year minus(TemporalAmount temporalAmount) {
        return (Year) temporalAmount.subtractFrom(this);
    }

    public Year minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public Year minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.YEARS;
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
            return temporal.with(ChronoField.YEAR, (long) this.year);
        }
        throw new DateTimeException("Adjustment only supported on ISO date-time");
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        long j = ((long) temporal.year) - ((long) this.year);
        switch ((ChronoUnit) temporalUnit) {
            case YEARS:
                return j;
            case DECADES:
                return j / 10;
            case CENTURIES:
                return j / 100;
            case MILLENNIA:
                return j / 1000;
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
        return LocalDate.ofYearDay(this.year, i);
    }

    public YearMonth atMonth(Month month) {
        return YearMonth.of(this.year, month);
    }

    public YearMonth atMonth(int i) {
        return YearMonth.of(this.year, i);
    }

    public LocalDate atMonthDay(MonthDay monthDay) {
        return monthDay.atYear(this.year);
    }

    public int compareTo(Year year) {
        return this.year - year.year;
    }

    public boolean isAfter(Year year) {
        return this.year > year.year ? true : null;
    }

    public boolean isBefore(Year year) {
        return this.year < year.year ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Year)) {
            return false;
        }
        if (this.year != ((Year) obj).year) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.year;
    }

    public String toString() {
        return Integer.toString(this.year);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 67, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
    }

    static Year readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readInt());
    }
}
