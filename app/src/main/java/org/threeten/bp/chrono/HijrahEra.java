package org.threeten.bp.chrono;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Locale;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;

public enum HijrahEra implements Era {
    BEFORE_AH,
    AH;

    public static HijrahEra of(int i) {
        switch (i) {
            case 0:
                return BEFORE_AH;
            case 1:
                return AH;
            default:
                throw new DateTimeException("HijrahEra not valid");
        }
    }

    public int getValue() {
        return ordinal();
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = false;
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.ERA) {
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
        if (temporalField == ChronoField.ERA) {
            return ValueRange.of(1, 1);
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
        if (temporalField == ChronoField.ERA) {
            return getValue();
        }
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    public long getLong(TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
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

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.ERA, (long) getValue());
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.ERAS;
        }
        if (!(temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.localDate())) {
            if (temporalQuery != TemporalQueries.localTime()) {
                return temporalQuery.queryFrom(this);
            }
        }
        return null;
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendText(ChronoField.ERA, textStyle).toFormatter(locale).format(this);
    }

    int prolepticYear(int i) {
        return this == AH ? i : 1 - i;
    }

    private Object writeReplace() {
        return new Ser((byte) 4, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(getValue());
    }

    static HijrahEra readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readByte());
    }
}
