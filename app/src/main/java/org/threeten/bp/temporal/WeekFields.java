package org.threeten.bp.temporal;

import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Year;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.jdk8.Jdk8Methods;

public final class WeekFields implements Serializable {
    private static final ConcurrentMap<String, WeekFields> CACHE = new ConcurrentHashMap(4, 0.75f, 2);
    public static final WeekFields ISO = new WeekFields(DayOfWeek.MONDAY, 4);
    public static final WeekFields SUNDAY_START = of(DayOfWeek.SUNDAY, 1);
    private static final long serialVersionUID = -1177360819670808121L;
    private final transient TemporalField dayOfWeek = ComputedDayOfField.ofDayOfWeekField(this);
    private final DayOfWeek firstDayOfWeek;
    private final int minimalDays;
    private final transient TemporalField weekBasedYear = ComputedDayOfField.ofWeekBasedYearField(this);
    private final transient TemporalField weekOfMonth = ComputedDayOfField.ofWeekOfMonthField(this);
    private final transient TemporalField weekOfWeekBasedYear = ComputedDayOfField.ofWeekOfWeekBasedYearField(this);
    private final transient TemporalField weekOfYear = ComputedDayOfField.ofWeekOfYearField(this);

    static class ComputedDayOfField implements TemporalField {
        private static final ValueRange DAY_OF_WEEK_RANGE = ValueRange.of(1, 7);
        private static final ValueRange WEEK_BASED_YEAR_RANGE = ChronoField.YEAR.range();
        private static final ValueRange WEEK_OF_MONTH_RANGE = ValueRange.of(0, 1, 4, 6);
        private static final ValueRange WEEK_OF_WEEK_BASED_YEAR_RANGE = ValueRange.of(1, 52, 53);
        private static final ValueRange WEEK_OF_YEAR_RANGE = ValueRange.of(0, 1, 52, 54);
        private final TemporalUnit baseUnit;
        private final String name;
        private final ValueRange range;
        private final TemporalUnit rangeUnit;
        private final WeekFields weekDef;

        public boolean isDateBased() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        static ComputedDayOfField ofDayOfWeekField(WeekFields weekFields) {
            return new ComputedDayOfField("DayOfWeek", weekFields, ChronoUnit.DAYS, ChronoUnit.WEEKS, DAY_OF_WEEK_RANGE);
        }

        static ComputedDayOfField ofWeekOfMonthField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfMonth", weekFields, ChronoUnit.WEEKS, ChronoUnit.MONTHS, WEEK_OF_MONTH_RANGE);
        }

        static ComputedDayOfField ofWeekOfYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfYear", weekFields, ChronoUnit.WEEKS, ChronoUnit.YEARS, WEEK_OF_YEAR_RANGE);
        }

        static ComputedDayOfField ofWeekOfWeekBasedYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfWeekBasedYear", weekFields, ChronoUnit.WEEKS, IsoFields.WEEK_BASED_YEARS, WEEK_OF_WEEK_BASED_YEAR_RANGE);
        }

        static ComputedDayOfField ofWeekBasedYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekBasedYear", weekFields, IsoFields.WEEK_BASED_YEARS, ChronoUnit.FOREVER, WEEK_BASED_YEAR_RANGE);
        }

        private ComputedDayOfField(String str, WeekFields weekFields, TemporalUnit temporalUnit, TemporalUnit temporalUnit2, ValueRange valueRange) {
            this.name = str;
            this.weekDef = weekFields;
            this.baseUnit = temporalUnit;
            this.rangeUnit = temporalUnit2;
            this.range = valueRange;
        }

        public long getFrom(TemporalAccessor temporalAccessor) {
            int floorMod = Jdk8Methods.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return (long) floorMod;
            }
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                temporalAccessor = temporalAccessor.get(ChronoField.DAY_OF_MONTH);
                return (long) computeWeek(startOfWeekOffset(temporalAccessor, floorMod), temporalAccessor);
            } else if (this.rangeUnit == ChronoUnit.YEARS) {
                temporalAccessor = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
                return (long) computeWeek(startOfWeekOffset(temporalAccessor, floorMod), temporalAccessor);
            } else if (this.rangeUnit == IsoFields.WEEK_BASED_YEARS) {
                return (long) localizedWOWBY(temporalAccessor);
            } else {
                if (this.rangeUnit == ChronoUnit.FOREVER) {
                    return (long) localizedWBY(temporalAccessor);
                }
                throw new IllegalStateException("unreachable");
            }
        }

        private int localizedDayOfWeek(TemporalAccessor temporalAccessor, int i) {
            return Jdk8Methods.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - i, 7) + 1;
        }

        private long localizedWeekOfMonth(TemporalAccessor temporalAccessor, int i) {
            temporalAccessor = temporalAccessor.get(ChronoField.DAY_OF_MONTH);
            return (long) computeWeek(startOfWeekOffset(temporalAccessor, i), temporalAccessor);
        }

        private long localizedWeekOfYear(TemporalAccessor temporalAccessor, int i) {
            temporalAccessor = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            return (long) computeWeek(startOfWeekOffset(temporalAccessor, i), temporalAccessor);
        }

        private int localizedWOWBY(TemporalAccessor temporalAccessor) {
            int floorMod = Jdk8Methods.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
            long localizedWeekOfYear = localizedWeekOfYear(temporalAccessor, floorMod);
            if (localizedWeekOfYear == 0) {
                return ((int) localizedWeekOfYear(Chronology.from(temporalAccessor).date(temporalAccessor).minus(1, ChronoUnit.WEEKS), floorMod)) + 1;
            }
            if (localizedWeekOfYear >= 53) {
                temporalAccessor = computeWeek(startOfWeekOffset(temporalAccessor.get(ChronoField.DAY_OF_YEAR), floorMod), (Year.isLeap((long) temporalAccessor.get(ChronoField.YEAR)) != null ? 366 : 365) + this.weekDef.getMinimalDaysInFirstWeek());
                if (localizedWeekOfYear >= ((long) temporalAccessor)) {
                    return (int) (localizedWeekOfYear - ((long) (temporalAccessor - 1)));
                }
            }
            return (int) localizedWeekOfYear;
        }

        private int localizedWBY(TemporalAccessor temporalAccessor) {
            int floorMod = Jdk8Methods.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
            int i = temporalAccessor.get(ChronoField.YEAR);
            long localizedWeekOfYear = localizedWeekOfYear(temporalAccessor, floorMod);
            if (localizedWeekOfYear == 0) {
                return i - 1;
            }
            if (localizedWeekOfYear < 53) {
                return i;
            }
            return localizedWeekOfYear >= ((long) computeWeek(startOfWeekOffset(temporalAccessor.get(ChronoField.DAY_OF_YEAR), floorMod), (Year.isLeap((long) i) ? 366 : 365) + this.weekDef.getMinimalDaysInFirstWeek())) ? i + 1 : i;
        }

        private int startOfWeekOffset(int i, int i2) {
            i = Jdk8Methods.floorMod(i - i2, 7);
            return i + 1 > this.weekDef.getMinimalDaysInFirstWeek() ? 7 - i : -i;
        }

        private int computeWeek(int i, int i2) {
            return ((i + 7) + (i2 - 1)) / 7;
        }

        public <R extends Temporal> R adjustInto(R r, long j) {
            int checkValidIntValue = this.range.checkValidIntValue(j, this);
            int i = r.get(this);
            if (checkValidIntValue == i) {
                return r;
            }
            if (this.rangeUnit != ChronoUnit.FOREVER) {
                return r.plus((long) (checkValidIntValue - i), this.baseUnit);
            }
            int i2 = r.get(this.weekDef.weekOfWeekBasedYear);
            r = r.plus((long) (((double) (j - ((long) i))) * 4632540147608159519L), ChronoUnit.WEEKS);
            if (r.get(this) > checkValidIntValue) {
                r = r.minus((long) r.get(this.weekDef.weekOfWeekBasedYear), ChronoUnit.WEEKS);
            } else {
                if (r.get(this) < checkValidIntValue) {
                    r = r.plus(2, ChronoUnit.WEEKS);
                }
                r = r.plus((long) (i2 - r.get(this.weekDef.weekOfWeekBasedYear)), ChronoUnit.WEEKS);
                if (r.get(this) > checkValidIntValue) {
                    r = r.minus(1, ChronoUnit.WEEKS);
                }
            }
            return r;
        }

        public TemporalAccessor resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            Map<TemporalField, Long> map2 = map;
            ResolverStyle resolverStyle2 = resolverStyle;
            int value = this.weekDef.getFirstDayOfWeek().getValue();
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                map2.put(ChronoField.DAY_OF_WEEK, Long.valueOf((long) (Jdk8Methods.floorMod((value - 1) + (r0.range.checkValidIntValue(((Long) map2.remove(r0)).longValue(), r0) - 1), 7) + 1)));
                return null;
            } else if (!map2.containsKey(ChronoField.DAY_OF_WEEK)) {
                return null;
            } else {
                int floorMod;
                long longValue;
                TemporalAccessor plus;
                if (r0.rangeUnit == ChronoUnit.FOREVER) {
                    if (!map2.containsKey(r0.weekDef.weekOfWeekBasedYear)) {
                        return null;
                    }
                    ChronoLocalDate date;
                    long localizedWeekOfYear;
                    Chronology from = Chronology.from(temporalAccessor);
                    floorMod = Jdk8Methods.floorMod(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map2.get(ChronoField.DAY_OF_WEEK)).longValue()) - value, 7) + 1;
                    int checkValidIntValue = range().checkValidIntValue(((Long) map2.get(r0)).longValue(), r0);
                    if (resolverStyle2 == ResolverStyle.LENIENT) {
                        date = from.date(checkValidIntValue, 1, r0.weekDef.getMinimalDaysInFirstWeek());
                        longValue = ((Long) map2.get(r0.weekDef.weekOfWeekBasedYear)).longValue();
                        value = localizedDayOfWeek(date, value);
                        localizedWeekOfYear = ((longValue - localizedWeekOfYear(date, value)) * 7) + ((long) (floorMod - value));
                    } else {
                        date = from.date(checkValidIntValue, 1, r0.weekDef.getMinimalDaysInFirstWeek());
                        longValue = (long) r0.weekDef.weekOfWeekBasedYear.range().checkValidIntValue(((Long) map2.get(r0.weekDef.weekOfWeekBasedYear)).longValue(), r0.weekDef.weekOfWeekBasedYear);
                        value = localizedDayOfWeek(date, value);
                        localizedWeekOfYear = ((longValue - localizedWeekOfYear(date, value)) * 7) + ((long) (floorMod - value));
                    }
                    plus = date.plus(localizedWeekOfYear, ChronoUnit.DAYS);
                    if (resolverStyle2 != ResolverStyle.STRICT || plus.getLong(r0) == ((Long) map2.get(r0)).longValue()) {
                        map2.remove(r0);
                        map2.remove(r0.weekDef.weekOfWeekBasedYear);
                        map2.remove(ChronoField.DAY_OF_WEEK);
                        return plus;
                    }
                    throw new DateTimeException("Strict mode rejected date parsed to a different year");
                } else if (!map2.containsKey(ChronoField.YEAR)) {
                    return null;
                } else {
                    int floorMod2 = Jdk8Methods.floorMod(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map2.get(ChronoField.DAY_OF_WEEK)).longValue()) - value, 7) + 1;
                    floorMod = ChronoField.YEAR.checkValidIntValue(((Long) map2.get(ChronoField.YEAR)).longValue());
                    Chronology from2 = Chronology.from(temporalAccessor);
                    if (r0.rangeUnit == ChronoUnit.MONTHS) {
                        if (!map2.containsKey(ChronoField.MONTH_OF_YEAR)) {
                            return null;
                        }
                        ChronoLocalDate plus2;
                        long longValue2 = ((Long) map2.remove(r0)).longValue();
                        if (resolverStyle2 == ResolverStyle.LENIENT) {
                            plus2 = from2.date(floorMod, 1, 1).plus(((Long) map2.get(ChronoField.MONTH_OF_YEAR)).longValue() - 1, ChronoUnit.MONTHS);
                            value = localizedDayOfWeek(plus2, value);
                            longValue = ((longValue2 - localizedWeekOfMonth(plus2, value)) * 7) + ((long) (floorMod2 - value));
                        } else {
                            plus2 = from2.date(floorMod, ChronoField.MONTH_OF_YEAR.checkValidIntValue(((Long) map2.get(ChronoField.MONTH_OF_YEAR)).longValue()), 8);
                            value = localizedDayOfWeek(plus2, value);
                            longValue = ((((long) r0.range.checkValidIntValue(longValue2, r0)) - localizedWeekOfMonth(plus2, value)) * 7) + ((long) (floorMod2 - value));
                        }
                        plus = plus2.plus(longValue, ChronoUnit.DAYS);
                        if (resolverStyle2 != ResolverStyle.STRICT || plus.getLong(ChronoField.MONTH_OF_YEAR) == ((Long) map2.get(ChronoField.MONTH_OF_YEAR)).longValue()) {
                            map2.remove(r0);
                            map2.remove(ChronoField.YEAR);
                            map2.remove(ChronoField.MONTH_OF_YEAR);
                            map2.remove(ChronoField.DAY_OF_WEEK);
                            return plus;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    } else if (r0.rangeUnit == ChronoUnit.YEARS) {
                        long longValue3 = ((Long) map2.remove(r0)).longValue();
                        Object date2 = from2.date(floorMod, 1, 1);
                        if (resolverStyle2 == ResolverStyle.LENIENT) {
                            value = localizedDayOfWeek(date2, value);
                            longValue = ((longValue3 - localizedWeekOfYear(date2, value)) * 7) + ((long) (floorMod2 - value));
                        } else {
                            value = localizedDayOfWeek(date2, value);
                            longValue = ((((long) r0.range.checkValidIntValue(longValue3, r0)) - localizedWeekOfYear(date2, value)) * 7) + ((long) (floorMod2 - value));
                        }
                        plus = date2.plus(longValue, ChronoUnit.DAYS);
                        if (resolverStyle2 != ResolverStyle.STRICT || plus.getLong(ChronoField.YEAR) == ((Long) map2.get(ChronoField.YEAR)).longValue()) {
                            map2.remove(r0);
                            map2.remove(ChronoField.YEAR);
                            map2.remove(ChronoField.DAY_OF_WEEK);
                            return plus;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different year");
                    } else {
                        throw new IllegalStateException("unreachable");
                    }
                }
            }
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
            if (temporalAccessor.isSupported(ChronoField.DAY_OF_WEEK)) {
                if (this.rangeUnit == ChronoUnit.WEEKS) {
                    return true;
                }
                if (this.rangeUnit == ChronoUnit.MONTHS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH);
                }
                if (this.rangeUnit == ChronoUnit.YEARS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR);
                }
                if (this.rangeUnit == IsoFields.WEEK_BASED_YEARS) {
                    return temporalAccessor.isSupported(ChronoField.EPOCH_DAY);
                }
                if (this.rangeUnit == ChronoUnit.FOREVER) {
                    return temporalAccessor.isSupported(ChronoField.EPOCH_DAY);
                }
            }
            return null;
        }

        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return this.range;
            }
            TemporalField temporalField;
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                temporalField = ChronoField.DAY_OF_MONTH;
            } else if (this.rangeUnit == ChronoUnit.YEARS) {
                temporalField = ChronoField.DAY_OF_YEAR;
            } else if (this.rangeUnit == IsoFields.WEEK_BASED_YEARS) {
                return rangeWOWBY(temporalAccessor);
            } else {
                if (this.rangeUnit == ChronoUnit.FOREVER) {
                    return temporalAccessor.range(ChronoField.YEAR);
                }
                throw new IllegalStateException("unreachable");
            }
            int startOfWeekOffset = startOfWeekOffset(temporalAccessor.get(temporalField), Jdk8Methods.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1);
            temporalAccessor = temporalAccessor.range(temporalField);
            return ValueRange.of((long) computeWeek(startOfWeekOffset, (int) temporalAccessor.getMinimum()), (long) computeWeek(startOfWeekOffset, (int) temporalAccessor.getMaximum()));
        }

        private ValueRange rangeWOWBY(TemporalAccessor temporalAccessor) {
            int floorMod = Jdk8Methods.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
            long localizedWeekOfYear = localizedWeekOfYear(temporalAccessor, floorMod);
            if (localizedWeekOfYear == 0) {
                return rangeWOWBY(Chronology.from(temporalAccessor).date(temporalAccessor).minus(2, ChronoUnit.WEEKS));
            }
            floorMod = computeWeek(startOfWeekOffset(temporalAccessor.get(ChronoField.DAY_OF_YEAR), floorMod), (Year.isLeap((long) temporalAccessor.get(ChronoField.YEAR)) ? 366 : 365) + this.weekDef.getMinimalDaysInFirstWeek());
            if (localizedWeekOfYear >= ((long) floorMod)) {
                return rangeWOWBY(Chronology.from(temporalAccessor).date(temporalAccessor).plus(2, ChronoUnit.WEEKS));
            }
            return ValueRange.of(1, (long) (floorMod - 1));
        }

        public String getDisplayName(Locale locale) {
            Jdk8Methods.requireNonNull(locale, "locale");
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return "Week";
            }
            return toString();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.name);
            stringBuilder.append("[");
            stringBuilder.append(this.weekDef.toString());
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    public static WeekFields of(Locale locale) {
        Jdk8Methods.requireNonNull(locale, "locale");
        locale = new GregorianCalendar(new Locale(locale.getLanguage(), locale.getCountry()));
        return of(DayOfWeek.SUNDAY.plus((long) (locale.getFirstDayOfWeek() - 1)), locale.getMinimalDaysInFirstWeek());
    }

    public static WeekFields of(DayOfWeek dayOfWeek, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dayOfWeek.toString());
        stringBuilder.append(i);
        String stringBuilder2 = stringBuilder.toString();
        WeekFields weekFields = (WeekFields) CACHE.get(stringBuilder2);
        if (weekFields != null) {
            return weekFields;
        }
        CACHE.putIfAbsent(stringBuilder2, new WeekFields(dayOfWeek, i));
        return (WeekFields) CACHE.get(stringBuilder2);
    }

    private WeekFields(DayOfWeek dayOfWeek, int i) {
        Jdk8Methods.requireNonNull(dayOfWeek, "firstDayOfWeek");
        if (i >= 1) {
            if (i <= 7) {
                this.firstDayOfWeek = dayOfWeek;
                this.minimalDays = i;
                return;
            }
        }
        throw new IllegalArgumentException("Minimal number of days is invalid");
    }

    private Object readResolve() throws InvalidObjectException {
        try {
            return of(this.firstDayOfWeek, this.minimalDays);
        } catch (IllegalArgumentException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid WeekFields");
            stringBuilder.append(e.getMessage());
            throw new InvalidObjectException(stringBuilder.toString());
        }
    }

    public DayOfWeek getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDays;
    }

    public TemporalField dayOfWeek() {
        return this.dayOfWeek;
    }

    public TemporalField weekOfMonth() {
        return this.weekOfMonth;
    }

    public TemporalField weekOfYear() {
        return this.weekOfYear;
    }

    public TemporalField weekOfWeekBasedYear() {
        return this.weekOfWeekBasedYear;
    }

    public TemporalField weekBasedYear() {
        return this.weekBasedYear;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WeekFields)) {
            return false;
        }
        if (hashCode() != obj.hashCode()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (this.firstDayOfWeek.ordinal() * 7) + this.minimalDays;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WeekFields[");
        stringBuilder.append(this.firstDayOfWeek);
        stringBuilder.append(',');
        stringBuilder.append(this.minimalDays);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
