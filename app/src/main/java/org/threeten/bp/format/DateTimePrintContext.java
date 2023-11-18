package org.threeten.bp.format;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.jdk8.DefaultInterfaceTemporalAccessor;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.ValueRange;

final class DateTimePrintContext {
    private Locale locale;
    private int optional;
    private DecimalStyle symbols;
    private TemporalAccessor temporal;

    DateTimePrintContext(TemporalAccessor temporalAccessor, DateTimeFormatter dateTimeFormatter) {
        this.temporal = adjust(temporalAccessor, dateTimeFormatter);
        this.locale = dateTimeFormatter.getLocale();
        this.symbols = dateTimeFormatter.getDecimalStyle();
    }

    DateTimePrintContext(TemporalAccessor temporalAccessor, Locale locale, DecimalStyle decimalStyle) {
        this.temporal = temporalAccessor;
        this.locale = locale;
        this.symbols = decimalStyle;
    }

    private static TemporalAccessor adjust(final TemporalAccessor temporalAccessor, DateTimeFormatter dateTimeFormatter) {
        Chronology chronology = dateTimeFormatter.getChronology();
        dateTimeFormatter = dateTimeFormatter.getZone();
        if (chronology == null && dateTimeFormatter == null) {
            return temporalAccessor;
        }
        Chronology chronology2 = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zoneId());
        ChronoLocalDate chronoLocalDate = null;
        if (Jdk8Methods.equals(chronology2, chronology)) {
            chronology = null;
        }
        if (Jdk8Methods.equals(zoneId, dateTimeFormatter)) {
            dateTimeFormatter = null;
        }
        if (chronology == null && dateTimeFormatter == null) {
            return temporalAccessor;
        }
        final Chronology chronology3 = chronology != null ? chronology : chronology2;
        if (dateTimeFormatter != null) {
            zoneId = dateTimeFormatter;
        }
        if (dateTimeFormatter != null) {
            if (temporalAccessor.isSupported(ChronoField.INSTANT_SECONDS)) {
                if (chronology3 == null) {
                    chronology3 = IsoChronology.INSTANCE;
                }
                return chronology3.zonedDateTime(Instant.from(temporalAccessor), dateTimeFormatter);
            }
            ZoneId normalized = dateTimeFormatter.normalized();
            ZoneOffset zoneOffset = (ZoneOffset) temporalAccessor.query(TemporalQueries.offset());
            if (!(!(normalized instanceof ZoneOffset) || zoneOffset == null || normalized.equals(zoneOffset))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid override zone for temporal: ");
                stringBuilder.append(dateTimeFormatter);
                stringBuilder.append(StringUtils.SPACE);
                stringBuilder.append(temporalAccessor);
                throw new DateTimeException(stringBuilder.toString());
            }
        }
        if (chronology != null) {
            if (temporalAccessor.isSupported(ChronoField.EPOCH_DAY) != null) {
                chronoLocalDate = chronology3.date(temporalAccessor);
            } else if (!(chronology == IsoChronology.INSTANCE && chronology2 == null)) {
                for (TemporalField temporalField : ChronoField.values()) {
                    if (temporalField.isDateBased() && temporalAccessor.isSupported(temporalField)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid override chronology for temporal: ");
                        stringBuilder.append(chronology);
                        stringBuilder.append(StringUtils.SPACE);
                        stringBuilder.append(temporalAccessor);
                        throw new DateTimeException(stringBuilder.toString());
                    }
                }
            }
        }
        return new DefaultInterfaceTemporalAccessor() {
            public boolean isSupported(TemporalField temporalField) {
                if (chronoLocalDate == null || !temporalField.isDateBased()) {
                    return temporalAccessor.isSupported(temporalField);
                }
                return chronoLocalDate.isSupported(temporalField);
            }

            public ValueRange range(TemporalField temporalField) {
                if (chronoLocalDate == null || !temporalField.isDateBased()) {
                    return temporalAccessor.range(temporalField);
                }
                return chronoLocalDate.range(temporalField);
            }

            public long getLong(TemporalField temporalField) {
                if (chronoLocalDate == null || !temporalField.isDateBased()) {
                    return temporalAccessor.getLong(temporalField);
                }
                return chronoLocalDate.getLong(temporalField);
            }

            public <R> R query(TemporalQuery<R> temporalQuery) {
                if (temporalQuery == TemporalQueries.chronology()) {
                    return chronology3;
                }
                if (temporalQuery == TemporalQueries.zoneId()) {
                    return zoneId;
                }
                if (temporalQuery == TemporalQueries.precision()) {
                    return temporalAccessor.query(temporalQuery);
                }
                return temporalQuery.queryFrom(this);
            }
        };
    }

    TemporalAccessor getTemporal() {
        return this.temporal;
    }

    Locale getLocale() {
        return this.locale;
    }

    DecimalStyle getSymbols() {
        return this.symbols;
    }

    void startOptional() {
        this.optional++;
    }

    void endOptional() {
        this.optional--;
    }

    <R> R getValue(TemporalQuery<R> temporalQuery) {
        temporalQuery = this.temporal.query(temporalQuery);
        if (temporalQuery != null || this.optional != 0) {
            return temporalQuery;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to extract value: ");
        stringBuilder.append(this.temporal.getClass());
        throw new DateTimeException(stringBuilder.toString());
    }

    Long getValue(TemporalField temporalField) {
        try {
            return Long.valueOf(this.temporal.getLong(temporalField));
        } catch (TemporalField temporalField2) {
            if (this.optional > 0) {
                return null;
            }
            throw temporalField2;
        }
    }

    public String toString() {
        return this.temporal.toString();
    }

    void setDateTime(TemporalAccessor temporalAccessor) {
        Jdk8Methods.requireNonNull(temporalAccessor, "temporal");
        this.temporal = temporalAccessor;
    }

    void setLocale(Locale locale) {
        Jdk8Methods.requireNonNull(locale, "locale");
        this.locale = locale;
    }
}
