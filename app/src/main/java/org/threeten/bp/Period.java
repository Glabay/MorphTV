package org.threeten.bp;

import com.google.android.exoplayer2.util.MimeTypes;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.ChronoPeriod;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;

public final class Period extends ChronoPeriod implements Serializable {
    private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?", 2);
    public static final Period ZERO = new Period(0, 0, 0);
    private static final long serialVersionUID = -8290556941213247973L;
    private final int days;
    private final int months;
    private final int years;

    public static Period ofYears(int i) {
        return create(i, 0, 0);
    }

    public static Period ofMonths(int i) {
        return create(0, i, 0);
    }

    public static Period ofWeeks(int i) {
        return create(0, 0, Jdk8Methods.safeMultiply(i, 7));
    }

    public static Period ofDays(int i) {
        return create(0, 0, i);
    }

    public static Period of(int i, int i2, int i3) {
        return create(i, i2, i3);
    }

    public static Period from(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return (Period) temporalAmount;
        }
        if (!(temporalAmount instanceof ChronoPeriod) || IsoChronology.INSTANCE.equals(((ChronoPeriod) temporalAmount).getChronology())) {
            Jdk8Methods.requireNonNull(temporalAmount, "amount");
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            for (TemporalUnit temporalUnit : temporalAmount.getUnits()) {
                long j = temporalAmount.get(temporalUnit);
                if (temporalUnit == ChronoUnit.YEARS) {
                    i = Jdk8Methods.safeToInt(j);
                } else if (temporalUnit == ChronoUnit.MONTHS) {
                    i2 = Jdk8Methods.safeToInt(j);
                } else if (temporalUnit == ChronoUnit.DAYS) {
                    i3 = Jdk8Methods.safeToInt(j);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unit must be Years, Months or Days, but was ");
                    stringBuilder.append(temporalUnit);
                    throw new DateTimeException(stringBuilder.toString());
                }
            }
            return create(i, i2, i3);
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Period requires ISO chronology: ");
        stringBuilder2.append(temporalAmount);
        throw new DateTimeException(stringBuilder2.toString());
    }

    public static Period between(LocalDate localDate, LocalDate localDate2) {
        return localDate.until((ChronoLocalDate) localDate2);
    }

    public static Period parse(CharSequence charSequence) {
        Jdk8Methods.requireNonNull(charSequence, MimeTypes.BASE_TYPE_TEXT);
        Matcher matcher = PATTERN.matcher(charSequence);
        if (matcher.matches()) {
            int i = 1;
            if ("-".equals(matcher.group(1))) {
                i = -1;
            }
            String group = matcher.group(2);
            String group2 = matcher.group(3);
            String group3 = matcher.group(4);
            String group4 = matcher.group(5);
            if (!(group == null && group2 == null && group3 == null && group4 == null)) {
                try {
                    return create(parseNumber(charSequence, group, i), parseNumber(charSequence, group2, i), Jdk8Methods.safeAdd(parseNumber(charSequence, group4, i), Jdk8Methods.safeMultiply(parseNumber(charSequence, group3, i), 7)));
                } catch (Throwable e) {
                    throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0).initCause(e));
                }
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0);
    }

    private static int parseNumber(CharSequence charSequence, String str, int i) {
        if (str == null) {
            return 0;
        }
        try {
            return Jdk8Methods.safeMultiply(Integer.parseInt(str), i);
        } catch (String str2) {
            throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0).initCause(str2));
        }
    }

    private static Period create(int i, int i2, int i3) {
        if (((i | i2) | i3) == 0) {
            return ZERO;
        }
        return new Period(i, i2, i3);
    }

    private Period(int i, int i2, int i3) {
        this.years = i;
        this.months = i2;
        this.days = i3;
    }

    private Object readResolve() {
        return ((this.years | this.months) | this.days) == 0 ? ZERO : this;
    }

    public List<TemporalUnit> getUnits() {
        return Collections.unmodifiableList(Arrays.asList(new ChronoUnit[]{ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS}));
    }

    public Chronology getChronology() {
        return IsoChronology.INSTANCE;
    }

    public long get(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.YEARS) {
            return (long) this.years;
        }
        if (temporalUnit == ChronoUnit.MONTHS) {
            return (long) this.months;
        }
        if (temporalUnit == ChronoUnit.DAYS) {
            return (long) this.days;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported unit: ");
        stringBuilder.append(temporalUnit);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public boolean isZero() {
        return this == ZERO;
    }

    public boolean isNegative() {
        if (this.years >= 0 && this.months >= 0) {
            if (this.days >= 0) {
                return false;
            }
        }
        return true;
    }

    public int getYears() {
        return this.years;
    }

    public int getMonths() {
        return this.months;
    }

    public int getDays() {
        return this.days;
    }

    public Period withYears(int i) {
        if (i == this.years) {
            return this;
        }
        return create(i, this.months, this.days);
    }

    public Period withMonths(int i) {
        if (i == this.months) {
            return this;
        }
        return create(this.years, i, this.days);
    }

    public Period withDays(int i) {
        if (i == this.days) {
            return this;
        }
        return create(this.years, this.months, i);
    }

    public Period plus(TemporalAmount temporalAmount) {
        temporalAmount = from(temporalAmount);
        return create(Jdk8Methods.safeAdd(this.years, temporalAmount.years), Jdk8Methods.safeAdd(this.months, temporalAmount.months), Jdk8Methods.safeAdd(this.days, temporalAmount.days));
    }

    public Period plusYears(long j) {
        return j == 0 ? this : create(Jdk8Methods.safeToInt(Jdk8Methods.safeAdd((long) this.years, j)), this.months, this.days);
    }

    public Period plusMonths(long j) {
        return j == 0 ? this : create(this.years, Jdk8Methods.safeToInt(Jdk8Methods.safeAdd((long) this.months, j)), this.days);
    }

    public Period plusDays(long j) {
        return j == 0 ? this : create(this.years, this.months, Jdk8Methods.safeToInt(Jdk8Methods.safeAdd((long) this.days, j)));
    }

    public Period minus(TemporalAmount temporalAmount) {
        temporalAmount = from(temporalAmount);
        return create(Jdk8Methods.safeSubtract(this.years, temporalAmount.years), Jdk8Methods.safeSubtract(this.months, temporalAmount.months), Jdk8Methods.safeSubtract(this.days, temporalAmount.days));
    }

    public Period minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    public Period minusMonths(long j) {
        return j == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-j);
    }

    public Period minusDays(long j) {
        return j == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-j);
    }

    public Period multipliedBy(int i) {
        if (this != ZERO) {
            if (i != 1) {
                return create(Jdk8Methods.safeMultiply(this.years, i), Jdk8Methods.safeMultiply(this.months, i), Jdk8Methods.safeMultiply(this.days, i));
            }
        }
        return this;
    }

    public Period negated() {
        return multipliedBy(-1);
    }

    public Period normalized() {
        long toTotalMonths = toTotalMonths();
        long j = toTotalMonths / 12;
        int i = (int) (toTotalMonths % 12);
        if (j == ((long) this.years) && i == this.months) {
            return this;
        }
        return create(Jdk8Methods.safeToInt(j), i, this.days);
    }

    public long toTotalMonths() {
        return (((long) this.years) * 12) + ((long) this.months);
    }

    public Temporal addTo(Temporal temporal) {
        Jdk8Methods.requireNonNull(temporal, "temporal");
        if (this.years != 0) {
            if (this.months != 0) {
                temporal = temporal.plus(toTotalMonths(), ChronoUnit.MONTHS);
            } else {
                temporal = temporal.plus((long) this.years, ChronoUnit.YEARS);
            }
        } else if (this.months != 0) {
            temporal = temporal.plus((long) this.months, ChronoUnit.MONTHS);
        }
        return this.days != 0 ? temporal.plus((long) this.days, ChronoUnit.DAYS) : temporal;
    }

    public Temporal subtractFrom(Temporal temporal) {
        Jdk8Methods.requireNonNull(temporal, "temporal");
        if (this.years != 0) {
            if (this.months != 0) {
                temporal = temporal.minus(toTotalMonths(), ChronoUnit.MONTHS);
            } else {
                temporal = temporal.minus((long) this.years, ChronoUnit.YEARS);
            }
        } else if (this.months != 0) {
            temporal = temporal.minus((long) this.months, ChronoUnit.MONTHS);
        }
        return this.days != 0 ? temporal.minus((long) this.days, ChronoUnit.DAYS) : temporal;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Period)) {
            return false;
        }
        Period period = (Period) obj;
        if (this.years != period.years || this.months != period.months || this.days != period.days) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (this.years + Integer.rotateLeft(this.months, 8)) + Integer.rotateLeft(this.days, 16);
    }

    public String toString() {
        if (this == ZERO) {
            return "P0D";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('P');
        if (this.years != 0) {
            stringBuilder.append(this.years);
            stringBuilder.append('Y');
        }
        if (this.months != 0) {
            stringBuilder.append(this.months);
            stringBuilder.append('M');
        }
        if (this.days != 0) {
            stringBuilder.append(this.days);
            stringBuilder.append('D');
        }
        return stringBuilder.toString();
    }
}
