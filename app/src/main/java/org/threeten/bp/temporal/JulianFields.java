package org.threeten.bp.temporal;

import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.jdk8.Jdk8Methods;

public final class JulianFields {
    public static final TemporalField JULIAN_DAY = Field.JULIAN_DAY;
    public static final TemporalField MODIFIED_JULIAN_DAY = Field.MODIFIED_JULIAN_DAY;
    public static final TemporalField RATA_DIE = Field.RATA_DIE;

    private enum Field implements TemporalField {
        JULIAN_DAY("JulianDay", ChronoUnit.DAYS, ChronoUnit.FOREVER, 2440588),
        MODIFIED_JULIAN_DAY("ModifiedJulianDay", ChronoUnit.DAYS, ChronoUnit.FOREVER, 40587),
        RATA_DIE("RataDie", ChronoUnit.DAYS, ChronoUnit.FOREVER, 719163);
        
        private final TemporalUnit baseUnit;
        private final String name;
        private final long offset;
        private final ValueRange range;
        private final TemporalUnit rangeUnit;

        public boolean isDateBased() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        private Field(String str, TemporalUnit temporalUnit, TemporalUnit temporalUnit2, long j) {
            this.name = str;
            this.baseUnit = temporalUnit;
            this.rangeUnit = temporalUnit2;
            this.range = ValueRange.of(j - 170999002, j + 169560311);
            this.offset = j;
        }

        public TemporalUnit getBaseUnit() {
            return this.baseUnit;
        }

        public TemporalUnit getRangeUnit() {
            return this.rangeUnit;
        }

        public ValueRange range() {
            return this.range;
        }

        public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
            return temporalAccessor.isSupported(ChronoField.EPOCH_DAY);
        }

        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            if (isSupportedBy(temporalAccessor) != null) {
                return range();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported field: ");
            stringBuilder.append(this);
            throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }

        public long getFrom(TemporalAccessor temporalAccessor) {
            return temporalAccessor.getLong(ChronoField.EPOCH_DAY) + this.offset;
        }

        public <R extends Temporal> R adjustInto(R r, long j) {
            if (range().isValidValue(j)) {
                return r.with(ChronoField.EPOCH_DAY, Jdk8Methods.safeSubtract(j, this.offset));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid value: ");
            stringBuilder.append(this.name);
            stringBuilder.append(StringUtils.SPACE);
            stringBuilder.append(j);
            throw new DateTimeException(stringBuilder.toString());
        }

        public String getDisplayName(Locale locale) {
            Jdk8Methods.requireNonNull(locale, "locale");
            return toString();
        }

        public TemporalAccessor resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            return Chronology.from(temporalAccessor).dateEpochDay(Jdk8Methods.safeSubtract(((Long) map.remove(this)).longValue(), this.offset));
        }

        public String toString() {
            return this.name;
        }
    }
}
