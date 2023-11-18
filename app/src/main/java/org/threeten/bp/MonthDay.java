package org.threeten.bp;

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
import org.threeten.bp.jdk8.DefaultInterfaceTemporalAccessor;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;

public final class MonthDay extends DefaultInterfaceTemporalAccessor implements TemporalAccessor, TemporalAdjuster, Comparable<MonthDay>, Serializable {
    public static final TemporalQuery<MonthDay> FROM = new C15211();
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendLiteral("--").appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter();
    private static final long serialVersionUID = -939150713474957432L;
    private final int day;
    private final int month;

    /* renamed from: org.threeten.bp.MonthDay$1 */
    static class C15211 implements TemporalQuery<MonthDay> {
        C15211() {
        }

        public MonthDay queryFrom(TemporalAccessor temporalAccessor) {
            return MonthDay.from(temporalAccessor);
        }
    }

    public static MonthDay now() {
        return now(Clock.systemDefaultZone());
    }

    public static MonthDay now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static MonthDay now(Clock clock) {
        clock = LocalDate.now(clock);
        return of(clock.getMonth(), clock.getDayOfMonth());
    }

    public static MonthDay of(Month month, int i) {
        Jdk8Methods.requireNonNull(month, "month");
        ChronoField.DAY_OF_MONTH.checkValidValue((long) i);
        if (i <= month.maxLength()) {
            return new MonthDay(month.getValue(), i);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Illegal value for DayOfMonth field, value ");
        stringBuilder.append(i);
        stringBuilder.append(" is not valid for month ");
        stringBuilder.append(month.name());
        throw new DateTimeException(stringBuilder.toString());
    }

    public static MonthDay of(int i, int i2) {
        return of(Month.of(i), i2);
    }

    public static org.threeten.bp.MonthDay from(org.threeten.bp.temporal.TemporalAccessor r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r3 instanceof org.threeten.bp.MonthDay;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r3 = (org.threeten.bp.MonthDay) r3;
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
        r0 = org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;	 Catch:{ DateTimeException -> 0x0029 }
        r0 = r3.get(r0);	 Catch:{ DateTimeException -> 0x0029 }
        r1 = org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH;	 Catch:{ DateTimeException -> 0x0029 }
        r1 = r3.get(r1);	 Catch:{ DateTimeException -> 0x0029 }
        r0 = of(r0, r1);	 Catch:{ DateTimeException -> 0x0029 }
        return r0;
    L_0x0029:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to obtain MonthDay from TemporalAccessor: ";
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
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.MonthDay.from(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.MonthDay");
    }

    public static MonthDay parse(CharSequence charSequence) {
        return parse(charSequence, PARSER);
    }

    public static MonthDay parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (MonthDay) dateTimeFormatter.parse(charSequence, FROM);
    }

    private MonthDay(int i, int i2) {
        this.month = i;
        this.day = i2;
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = true;
        if (temporalField instanceof ChronoField) {
            if (temporalField != ChronoField.MONTH_OF_YEAR) {
                if (temporalField != ChronoField.DAY_OF_MONTH) {
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

    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return temporalField.range();
        }
        if (temporalField == ChronoField.DAY_OF_MONTH) {
            return ValueRange.of(1, (long) getMonth().minLength(), (long) getMonth().maxLength());
        }
        return super.range(temporalField);
    }

    public int get(TemporalField temporalField) {
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField) temporalField) {
            case DAY_OF_MONTH:
                return (long) this.day;
            case MONTH_OF_YEAR:
                return (long) this.month;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public int getMonthValue() {
        return this.month;
    }

    public Month getMonth() {
        return Month.of(this.month);
    }

    public int getDayOfMonth() {
        return this.day;
    }

    public boolean isValidYear(int i) {
        i = (this.day == 29 && this.month == 2 && Year.isLeap((long) i) == 0) ? 1 : 0;
        return i ^ 1;
    }

    public MonthDay withMonth(int i) {
        return with(Month.of(i));
    }

    public MonthDay with(Month month) {
        Jdk8Methods.requireNonNull(month, "month");
        if (month.getValue() == this.month) {
            return this;
        }
        return new MonthDay(month.getValue(), Math.min(this.day, month.maxLength()));
    }

    public MonthDay withDayOfMonth(int i) {
        if (i == this.day) {
            return this;
        }
        return of(this.month, i);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return IsoChronology.INSTANCE;
        }
        return super.query(temporalQuery);
    }

    public Temporal adjustInto(Temporal temporal) {
        if (Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            temporal = temporal.with(ChronoField.MONTH_OF_YEAR, (long) this.month);
            return temporal.with(ChronoField.DAY_OF_MONTH, Math.min(temporal.range(ChronoField.DAY_OF_MONTH).getMaximum(), (long) this.day));
        }
        throw new DateTimeException("Adjustment only supported on ISO date-time");
    }

    public LocalDate atYear(int i) {
        return LocalDate.of(i, this.month, isValidYear(i) ? this.day : 28);
    }

    public int compareTo(MonthDay monthDay) {
        int i = this.month - monthDay.month;
        return i == 0 ? this.day - monthDay.day : i;
    }

    public boolean isAfter(MonthDay monthDay) {
        return compareTo(monthDay) > null ? true : null;
    }

    public boolean isBefore(MonthDay monthDay) {
        return compareTo(monthDay) < null ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MonthDay)) {
            return false;
        }
        MonthDay monthDay = (MonthDay) obj;
        if (this.month != monthDay.month || this.day != monthDay.day) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (this.month << 6) + this.day;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(10);
        stringBuilder.append("--");
        stringBuilder.append(this.month < 10 ? "0" : "");
        stringBuilder.append(this.month);
        stringBuilder.append(this.day < 10 ? "-0" : "-");
        stringBuilder.append(this.day);
        return stringBuilder.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 64, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(this.month);
        dataOutput.writeByte(this.day);
    }

    static MonthDay readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readByte(), dataInput.readByte());
    }
}
