package org.threeten.bp.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.jdk8.DefaultInterfaceTemporalAccessor;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;

final class DateTimeParseContext {
    private boolean caseSensitive = true;
    private Locale locale;
    private Chronology overrideChronology;
    private ZoneId overrideZone;
    private final ArrayList<Parsed> parsed = new ArrayList();
    private boolean strict = true;
    private DecimalStyle symbols;

    final class Parsed extends DefaultInterfaceTemporalAccessor {
        List<Object[]> callbacks;
        Chronology chrono;
        Period excessDays;
        final Map<TemporalField, Long> fieldValues;
        boolean leapSecond;
        ZoneId zone;

        private Parsed() {
            this.chrono = null;
            this.zone = null;
            this.fieldValues = new HashMap();
            this.excessDays = Period.ZERO;
        }

        protected Parsed copy() {
            Parsed parsed = new Parsed();
            parsed.chrono = this.chrono;
            parsed.zone = this.zone;
            parsed.fieldValues.putAll(this.fieldValues);
            parsed.leapSecond = this.leapSecond;
            return parsed;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.fieldValues.toString());
            stringBuilder.append(",");
            stringBuilder.append(this.chrono);
            stringBuilder.append(",");
            stringBuilder.append(this.zone);
            return stringBuilder.toString();
        }

        public boolean isSupported(TemporalField temporalField) {
            return this.fieldValues.containsKey(temporalField);
        }

        public int get(TemporalField temporalField) {
            if (this.fieldValues.containsKey(temporalField)) {
                return Jdk8Methods.safeToInt(((Long) this.fieldValues.get(temporalField)).longValue());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported field: ");
            stringBuilder.append(temporalField);
            throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }

        public long getLong(TemporalField temporalField) {
            if (this.fieldValues.containsKey(temporalField)) {
                return ((Long) this.fieldValues.get(temporalField)).longValue();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported field: ");
            stringBuilder.append(temporalField);
            throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }

        public <R> R query(TemporalQuery<R> temporalQuery) {
            if (temporalQuery == TemporalQueries.chronology()) {
                return this.chrono;
            }
            if (temporalQuery != TemporalQueries.zoneId()) {
                if (temporalQuery != TemporalQueries.zone()) {
                    return super.query(temporalQuery);
                }
            }
            return this.zone;
        }

        DateTimeBuilder toBuilder() {
            DateTimeBuilder dateTimeBuilder = new DateTimeBuilder();
            dateTimeBuilder.fieldValues.putAll(this.fieldValues);
            dateTimeBuilder.chrono = DateTimeParseContext.this.getEffectiveChronology();
            if (this.zone != null) {
                dateTimeBuilder.zone = this.zone;
            } else {
                dateTimeBuilder.zone = DateTimeParseContext.this.overrideZone;
            }
            dateTimeBuilder.leapSecond = this.leapSecond;
            dateTimeBuilder.excessDays = this.excessDays;
            return dateTimeBuilder;
        }
    }

    DateTimeParseContext(DateTimeFormatter dateTimeFormatter) {
        this.locale = dateTimeFormatter.getLocale();
        this.symbols = dateTimeFormatter.getDecimalStyle();
        this.overrideChronology = dateTimeFormatter.getChronology();
        this.overrideZone = dateTimeFormatter.getZone();
        this.parsed.add(new Parsed());
    }

    DateTimeParseContext(Locale locale, DecimalStyle decimalStyle, Chronology chronology) {
        this.locale = locale;
        this.symbols = decimalStyle;
        this.overrideChronology = chronology;
        this.overrideZone = null;
        this.parsed.add(new Parsed());
    }

    DateTimeParseContext(DateTimeParseContext dateTimeParseContext) {
        this.locale = dateTimeParseContext.locale;
        this.symbols = dateTimeParseContext.symbols;
        this.overrideChronology = dateTimeParseContext.overrideChronology;
        this.overrideZone = dateTimeParseContext.overrideZone;
        this.caseSensitive = dateTimeParseContext.caseSensitive;
        this.strict = dateTimeParseContext.strict;
        this.parsed.add(new Parsed());
    }

    DateTimeParseContext copy() {
        return new DateTimeParseContext(this);
    }

    Locale getLocale() {
        return this.locale;
    }

    DecimalStyle getSymbols() {
        return this.symbols;
    }

    Chronology getEffectiveChronology() {
        Chronology chronology = currentParsed().chrono;
        if (chronology != null) {
            return chronology;
        }
        chronology = this.overrideChronology;
        return chronology == null ? IsoChronology.INSTANCE : chronology;
    }

    boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    void setCaseSensitive(boolean z) {
        this.caseSensitive = z;
    }

    boolean subSequenceEquals(CharSequence charSequence, int i, CharSequence charSequence2, int i2, int i3) {
        if (i + i3 <= charSequence.length()) {
            if (i2 + i3 <= charSequence2.length()) {
                int i4;
                if (isCaseSensitive()) {
                    for (i4 = 0; i4 < i3; i4++) {
                        if (charSequence.charAt(i + i4) != charSequence2.charAt(i2 + i4)) {
                            return false;
                        }
                    }
                } else {
                    for (i4 = 0; i4 < i3; i4++) {
                        char charAt = charSequence.charAt(i + i4);
                        char charAt2 = charSequence2.charAt(i2 + i4);
                        if (charAt != charAt2 && Character.toUpperCase(charAt) != Character.toUpperCase(charAt2) && Character.toLowerCase(charAt) != Character.toLowerCase(charAt2)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    boolean charEquals(char c, char c2) {
        if (!isCaseSensitive()) {
            return charEqualsIgnoreCase(c, c2);
        }
        return c == c2 ? '\u0001' : '\u0000';
    }

    static boolean charEqualsIgnoreCase(char c, char c2) {
        if (!(c == c2 || Character.toUpperCase(c) == Character.toUpperCase(c2))) {
            if (Character.toLowerCase(c) != Character.toLowerCase(c2)) {
                return false;
            }
        }
        return true;
    }

    boolean isStrict() {
        return this.strict;
    }

    void setStrict(boolean z) {
        this.strict = z;
    }

    void startOptional() {
        this.parsed.add(currentParsed().copy());
    }

    void endOptional(boolean z) {
        if (z) {
            this.parsed.remove(this.parsed.size() - 2);
        } else {
            this.parsed.remove(this.parsed.size() - 1);
        }
    }

    private Parsed currentParsed() {
        return (Parsed) this.parsed.get(this.parsed.size() - 1);
    }

    Long getParsed(TemporalField temporalField) {
        return (Long) currentParsed().fieldValues.get(temporalField);
    }

    int setParsedField(TemporalField temporalField, long j, int i, int i2) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        Long l = (Long) currentParsed().fieldValues.put(temporalField, Long.valueOf(j));
        return (l == null || l.longValue() == j) ? i2 : i ^ -1;
    }

    void setParsed(Chronology chronology) {
        Jdk8Methods.requireNonNull(chronology, "chrono");
        Parsed currentParsed = currentParsed();
        currentParsed.chrono = chronology;
        if (currentParsed.callbacks != null) {
            Chronology<Object[]> arrayList = new ArrayList(currentParsed.callbacks);
            currentParsed.callbacks.clear();
            for (Object[] objArr : arrayList) {
                ((ReducedPrinterParser) objArr[0]).setValue(this, ((Long) objArr[1]).longValue(), ((Integer) objArr[2]).intValue(), ((Integer) objArr[3]).intValue());
            }
        }
    }

    void addChronologyChangedParser(ReducedPrinterParser reducedPrinterParser, long j, int i, int i2) {
        Parsed currentParsed = currentParsed();
        if (currentParsed.callbacks == null) {
            currentParsed.callbacks = new ArrayList(2);
        }
        currentParsed.callbacks.add(new Object[]{reducedPrinterParser, Long.valueOf(j), Integer.valueOf(i), Integer.valueOf(i2)});
    }

    void setParsed(ZoneId zoneId) {
        Jdk8Methods.requireNonNull(zoneId, "zone");
        currentParsed().zone = zoneId;
    }

    void setParsedLeapSecond() {
        currentParsed().leapSecond = true;
    }

    Parsed toParsed() {
        return currentParsed();
    }

    public String toString() {
        return currentParsed().toString();
    }

    void setLocale(Locale locale) {
        Jdk8Methods.requireNonNull(locale, "locale");
        this.locale = locale;
    }
}
