package org.threeten.bp;

import java.util.Locale;
import org.mozilla.universalchardet.prober.HebrewProber;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;

public enum Month implements TemporalAccessor, TemporalAdjuster {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;
    
    private static final Month[] ENUMS = null;
    public static final TemporalQuery<Month> FROM = null;

    /* renamed from: org.threeten.bp.Month$1 */
    static class C15191 implements TemporalQuery<Month> {
        C15191() {
        }

        public Month queryFrom(TemporalAccessor temporalAccessor) {
            return Month.from(temporalAccessor);
        }
    }

    static {
        FROM = new C15191();
        ENUMS = values();
    }

    public static Month of(int i) {
        if (i >= 1) {
            if (i <= 12) {
                return ENUMS[i - 1];
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid value for MonthOfYear: ");
        stringBuilder.append(i);
        throw new DateTimeException(stringBuilder.toString());
    }

    public static Month from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Month) {
            return (Month) temporalAccessor;
        }
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(temporalAccessor))) {
                temporalAccessor = LocalDate.from(temporalAccessor);
            }
            return of(temporalAccessor.get(ChronoField.MONTH_OF_YEAR));
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to obtain Month from TemporalAccessor: ");
            stringBuilder.append(temporalAccessor);
            stringBuilder.append(", type ");
            stringBuilder.append(temporalAccessor.getClass().getName());
            throw new DateTimeException(stringBuilder.toString(), e);
        }
    }

    public int getValue() {
        return ordinal() + 1;
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendText(ChronoField.MONTH_OF_YEAR, textStyle).toFormatter(locale).format(this);
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = false;
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.MONTH_OF_YEAR) {
                z = true;
            }
            return z;
        }
        if (!(temporalField == null || temporalField.isSupportedBy(this) == null)) {
            z = true;
        }
        return z;
    }

    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return temporalField.range();
        }
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public int get(TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return getValue();
        }
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (temporalField == ChronoField.MONTH_OF_YEAR) {
            return (long) getValue();
        }
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public Month plus(long j) {
        return ENUMS[(ordinal() + (((int) (j % 12)) + 12)) % 12];
    }

    public Month minus(long j) {
        return plus(-(j % 12));
    }

    public int length(boolean z) {
        switch (this) {
            case FEBRUARY:
                return z ? true : true;
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return true;
            default:
                return true;
        }
    }

    public int minLength() {
        switch (this) {
            case FEBRUARY:
                return 28;
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return 30;
            default:
                return 31;
        }
    }

    public int maxLength() {
        switch (this) {
            case FEBRUARY:
                return 29;
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return 30;
            default:
                return 31;
        }
    }

    public int firstDayOfYear(boolean z) {
        switch (this) {
            case FEBRUARY:
                return true;
            case APRIL:
                return z + 91;
            case JUNE:
                return z + 152;
            case SEPTEMBER:
                return z + HebrewProber.NORMAL_PE;
            case NOVEMBER:
                return z + 305;
            case JANUARY:
                return true;
            case MARCH:
                return z + 60;
            case MAY:
                return z + 121;
            case JULY:
                return z + 182;
            case AUGUST:
                return z + 213;
            case OCTOBER:
                return z + 274;
            default:
                return z + 335;
        }
    }

    public Month firstMonthOfQuarter() {
        return ENUMS[(ordinal() / 3) * 3];
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
                return temporalQuery.queryFrom(this);
            }
        }
        return null;
    }

    public Temporal adjustInto(Temporal temporal) {
        if (Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            return temporal.with(ChronoField.MONTH_OF_YEAR, (long) getValue());
        }
        throw new DateTimeException("Adjustment only supported on ISO date-time");
    }
}
