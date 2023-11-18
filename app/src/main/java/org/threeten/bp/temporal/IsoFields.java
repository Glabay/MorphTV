package org.threeten.bp.temporal;

import java.util.Locale;
import java.util.Map;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.jdk8.Jdk8Methods;

public final class IsoFields {
    public static final TemporalField DAY_OF_QUARTER = Field.DAY_OF_QUARTER;
    public static final TemporalField QUARTER_OF_YEAR = Field.QUARTER_OF_YEAR;
    public static final TemporalUnit QUARTER_YEARS = Unit.QUARTER_YEARS;
    public static final TemporalField WEEK_BASED_YEAR = Field.WEEK_BASED_YEAR;
    public static final TemporalUnit WEEK_BASED_YEARS = Unit.WEEK_BASED_YEARS;
    public static final TemporalField WEEK_OF_WEEK_BASED_YEAR = Field.WEEK_OF_WEEK_BASED_YEAR;

    private enum Field implements TemporalField {
        DAY_OF_QUARTER {
            public String toString() {
                return "DayOfQuarter";
            }

            public TemporalUnit getBaseUnit() {
                return ChronoUnit.DAYS;
            }

            public TemporalUnit getRangeUnit() {
                return IsoFields.QUARTER_YEARS;
            }

            public ValueRange range() {
                return ValueRange.of(1, 90, 92);
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return (temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR) && temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) && temporalAccessor.isSupported(ChronoField.YEAR) && Field.isIso(temporalAccessor) != null) ? true : null;
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (temporalAccessor.isSupported(this)) {
                    long j = temporalAccessor.getLong(QUARTER_OF_YEAR);
                    if (j == 1) {
                        return IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) != null ? ValueRange.of(1, 91) : ValueRange.of(1, 90);
                    } else if (j == 2) {
                        return ValueRange.of(1, 91);
                    } else {
                        if (j != 3) {
                            if (j != 4) {
                                return range();
                            }
                        }
                        return ValueRange.of(1, 92);
                    }
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (temporalAccessor.isSupported(this)) {
                    return (long) (temporalAccessor.get(ChronoField.DAY_OF_YEAR) - Field.QUARTER_DAYS[((temporalAccessor.get(ChronoField.MONTH_OF_YEAR) - 1) / 3) + (IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) ? 4 : 0)]);
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                long from = getFrom(r);
                range().checkValidValue(j, this);
                return r.with(ChronoField.DAY_OF_YEAR, r.getLong(ChronoField.DAY_OF_YEAR) + (j - from));
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public org.threeten.bp.temporal.TemporalAccessor resolve(java.util.Map<org.threeten.bp.temporal.TemporalField, java.lang.Long> r11, org.threeten.bp.temporal.TemporalAccessor r12, org.threeten.bp.format.ResolverStyle r13) {
                /*
                r10 = this;
                r12 = org.threeten.bp.temporal.ChronoField.YEAR;
                r12 = r11.get(r12);
                r12 = (java.lang.Long) r12;
                r0 = QUARTER_OF_YEAR;
                r0 = r11.get(r0);
                r0 = (java.lang.Long) r0;
                if (r12 == 0) goto L_0x00a9;
            L_0x0012:
                if (r0 != 0) goto L_0x0016;
            L_0x0014:
                goto L_0x00a9;
            L_0x0016:
                r1 = org.threeten.bp.temporal.ChronoField.YEAR;
                r2 = r12.longValue();
                r12 = r1.checkValidIntValue(r2);
                r1 = DAY_OF_QUARTER;
                r1 = r11.get(r1);
                r1 = (java.lang.Long) r1;
                r1 = r1.longValue();
                r3 = org.threeten.bp.format.ResolverStyle.LENIENT;
                r4 = 3;
                r5 = 1;
                r7 = 1;
                if (r13 != r3) goto L_0x0051;
            L_0x0034:
                r8 = r0.longValue();
                r12 = org.threeten.bp.LocalDate.of(r12, r7, r7);
                r7 = org.threeten.bp.jdk8.Jdk8Methods.safeSubtract(r8, r5);
                r3 = org.threeten.bp.jdk8.Jdk8Methods.safeMultiply(r7, r4);
                r12 = r12.plusMonths(r3);
                r0 = org.threeten.bp.jdk8.Jdk8Methods.safeSubtract(r1, r5);
                r12 = r12.plusDays(r0);
                goto L_0x009b;
            L_0x0051:
                r3 = QUARTER_OF_YEAR;
                r3 = r3.range();
                r8 = r0.longValue();
                r0 = QUARTER_OF_YEAR;
                r0 = r3.checkValidIntValue(r8, r0);
                r3 = org.threeten.bp.format.ResolverStyle.STRICT;
                if (r13 != r3) goto L_0x0086;
            L_0x0065:
                r13 = 92;
                r3 = 91;
                if (r0 != r7) goto L_0x0078;
            L_0x006b:
                r13 = org.threeten.bp.chrono.IsoChronology.INSTANCE;
                r8 = (long) r12;
                r13 = r13.isLeapYear(r8);
                if (r13 == 0) goto L_0x0075;
            L_0x0074:
                goto L_0x007b;
            L_0x0075:
                r13 = 90;
                goto L_0x007d;
            L_0x0078:
                r8 = 2;
                if (r0 != r8) goto L_0x007d;
            L_0x007b:
                r13 = 91;
            L_0x007d:
                r8 = (long) r13;
                r13 = org.threeten.bp.temporal.ValueRange.of(r5, r8);
                r13.checkValidValue(r1, r10);
                goto L_0x008d;
            L_0x0086:
                r13 = r10.range();
                r13.checkValidValue(r1, r10);
            L_0x008d:
                r0 = r0 - r7;
                r0 = r0 * 3;
                r0 = r0 + r7;
                r12 = org.threeten.bp.LocalDate.of(r12, r0, r7);
                r3 = r1 - r5;
                r12 = r12.plusDays(r3);
            L_0x009b:
                r11.remove(r10);
                r13 = org.threeten.bp.temporal.ChronoField.YEAR;
                r11.remove(r13);
                r13 = QUARTER_OF_YEAR;
                r11.remove(r13);
                return r12;
            L_0x00a9:
                r11 = 0;
                return r11;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.temporal.IsoFields.Field.1.resolve(java.util.Map, org.threeten.bp.temporal.TemporalAccessor, org.threeten.bp.format.ResolverStyle):org.threeten.bp.temporal.TemporalAccessor");
            }
        },
        QUARTER_OF_YEAR {
            public String toString() {
                return "QuarterOfYear";
            }

            public TemporalUnit getBaseUnit() {
                return IsoFields.QUARTER_YEARS;
            }

            public TemporalUnit getRangeUnit() {
                return ChronoUnit.YEARS;
            }

            public ValueRange range() {
                return ValueRange.of(1, 4);
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return (!temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) || Field.isIso(temporalAccessor) == null) ? null : true;
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                return range();
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (temporalAccessor.isSupported(this)) {
                    return (temporalAccessor.getLong(ChronoField.MONTH_OF_YEAR) + 2) / 3;
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: QuarterOfYear");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                long from = getFrom(r);
                range().checkValidValue(j, this);
                return r.with(ChronoField.MONTH_OF_YEAR, r.getLong(ChronoField.MONTH_OF_YEAR) + ((j - from) * 3));
            }
        },
        WEEK_OF_WEEK_BASED_YEAR {
            public String toString() {
                return "WeekOfWeekBasedYear";
            }

            public TemporalUnit getBaseUnit() {
                return ChronoUnit.WEEKS;
            }

            public TemporalUnit getRangeUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }

            public String getDisplayName(Locale locale) {
                Jdk8Methods.requireNonNull(locale, "locale");
                return "Week";
            }

            public ValueRange range() {
                return ValueRange.of(1, 52, 53);
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return (!temporalAccessor.isSupported(ChronoField.EPOCH_DAY) || Field.isIso(temporalAccessor) == null) ? null : true;
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (temporalAccessor.isSupported(this)) {
                    return Field.getWeekRange((LocalDate) LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (temporalAccessor.isSupported(this)) {
                    return (long) Field.getWeek(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                range().checkValidValue(j, this);
                return r.plus(Jdk8Methods.safeSubtract(j, getFrom(r)), ChronoUnit.WEEKS);
            }

            public TemporalAccessor resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                C15663 c15663 = this;
                Map<TemporalField, Long> map2 = map;
                ResolverStyle resolverStyle2 = resolverStyle;
                Long l = (Long) map2.get(WEEK_BASED_YEAR);
                Long l2 = (Long) map2.get(ChronoField.DAY_OF_WEEK);
                if (l != null) {
                    if (l2 != null) {
                        TemporalAccessor with;
                        int checkValidIntValue = WEEK_BASED_YEAR.range().checkValidIntValue(l.longValue(), WEEK_BASED_YEAR);
                        long longValue = ((Long) map2.get(WEEK_OF_WEEK_BASED_YEAR)).longValue();
                        if (resolverStyle2 == ResolverStyle.LENIENT) {
                            long j;
                            long longValue2 = l2.longValue();
                            if (longValue2 > 7) {
                                j = longValue2 - 1;
                                longValue2 = j / 7;
                                j = (j % 7) + 1;
                            } else if (longValue2 < 1) {
                                j = (longValue2 % 7) + 7;
                                longValue2 = (longValue2 / 7) - 1;
                            } else {
                                long j2 = longValue2;
                                longValue2 = 0;
                                j = j2;
                            }
                            with = LocalDate.of(checkValidIntValue, 1, 4).plusWeeks(longValue - 1).plusWeeks(longValue2).with(ChronoField.DAY_OF_WEEK, j);
                        } else {
                            int checkValidIntValue2 = ChronoField.DAY_OF_WEEK.checkValidIntValue(l2.longValue());
                            if (resolverStyle2 == ResolverStyle.STRICT) {
                                Field.getWeekRange(LocalDate.of(checkValidIntValue, 1, 4)).checkValidValue(longValue, c15663);
                            } else {
                                range().checkValidValue(longValue, c15663);
                            }
                            with = LocalDate.of(checkValidIntValue, 1, 4).plusWeeks(longValue - 1).with(ChronoField.DAY_OF_WEEK, (long) checkValidIntValue2);
                        }
                        map2.remove(c15663);
                        map2.remove(WEEK_BASED_YEAR);
                        map2.remove(ChronoField.DAY_OF_WEEK);
                        return with;
                    }
                }
                return null;
            }
        },
        WEEK_BASED_YEAR {
            public String toString() {
                return "WeekBasedYear";
            }

            public TemporalUnit getBaseUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }

            public TemporalUnit getRangeUnit() {
                return ChronoUnit.FOREVER;
            }

            public ValueRange range() {
                return ChronoField.YEAR.range();
            }

            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return (!temporalAccessor.isSupported(ChronoField.EPOCH_DAY) || Field.isIso(temporalAccessor) == null) ? null : true;
            }

            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                return ChronoField.YEAR.range();
            }

            public long getFrom(TemporalAccessor temporalAccessor) {
                if (temporalAccessor.isSupported(this)) {
                    return (long) Field.getWeekBasedYear(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
            }

            public <R extends Temporal> R adjustInto(R r, long j) {
                if (isSupportedBy(r)) {
                    int checkValidIntValue = range().checkValidIntValue(j, WEEK_BASED_YEAR);
                    LocalDate from = LocalDate.from(r);
                    int i = from.get(ChronoField.DAY_OF_WEEK);
                    int access$400 = Field.getWeek(from);
                    if (access$400 == 53 && Field.getWeekRange(checkValidIntValue) == 52) {
                        access$400 = 52;
                    }
                    j = LocalDate.of(checkValidIntValue, 1, 4);
                    return r.with(j.plusDays((long) ((i - j.get(ChronoField.DAY_OF_WEEK)) + ((access$400 - 1) * 7))));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
            }
        };
        
        private static final int[] QUARTER_DAYS = null;

        public boolean isDateBased() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        public TemporalAccessor resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            return null;
        }

        static {
            QUARTER_DAYS = new int[]{0, 90, 181, 273, 0, 91, 182, 274};
        }

        public String getDisplayName(Locale locale) {
            Jdk8Methods.requireNonNull(locale, "locale");
            return toString();
        }

        private static boolean isIso(TemporalAccessor temporalAccessor) {
            return Chronology.from(temporalAccessor).equals(IsoChronology.INSTANCE);
        }

        private static ValueRange getWeekRange(LocalDate localDate) {
            return ValueRange.of(1, (long) getWeekRange(getWeekBasedYear(localDate)));
        }

        private static int getWeekRange(int i) {
            i = LocalDate.of(i, 1, 1);
            if (i.getDayOfWeek() != DayOfWeek.THURSDAY) {
                if (i.getDayOfWeek() != DayOfWeek.WEDNESDAY || i.isLeapYear() == 0) {
                    return 52;
                }
            }
            return 53;
        }

        private static int getWeek(LocalDate localDate) {
            int dayOfYear = localDate.getDayOfYear() - 1;
            int ordinal = (3 - localDate.getDayOfWeek().ordinal()) + dayOfYear;
            ordinal = (ordinal - ((ordinal / 7) * 7)) - 3;
            if (ordinal < -3) {
                ordinal += 7;
            }
            if (dayOfYear < ordinal) {
                return (int) getWeekRange(localDate.withDayOfYear(180).minusYears(1)).getMaximum();
            }
            dayOfYear = ((dayOfYear - ordinal) / 7) + 1;
            if (dayOfYear == 53) {
                if (ordinal != -3) {
                    if (ordinal != -2 || localDate.isLeapYear() == null) {
                        localDate = null;
                        if (localDate == null) {
                            dayOfYear = 1;
                        }
                    }
                }
                localDate = true;
                if (localDate == null) {
                    dayOfYear = 1;
                }
            }
            return dayOfYear;
        }

        private static int getWeekBasedYear(LocalDate localDate) {
            int year = localDate.getYear();
            int dayOfYear = localDate.getDayOfYear();
            if (dayOfYear <= 3) {
                if (dayOfYear - localDate.getDayOfWeek().ordinal() < -2) {
                    return year - 1;
                }
                return year;
            } else if (dayOfYear < 363) {
                return year;
            } else {
                return ((dayOfYear - 363) - localDate.isLeapYear()) - localDate.getDayOfWeek().ordinal() >= 0 ? year + 1 : year;
            }
        }
    }

    private enum Unit implements TemporalUnit {
        WEEK_BASED_YEARS("WeekBasedYears", Duration.ofSeconds(31556952)),
        QUARTER_YEARS("QuarterYears", Duration.ofSeconds(7889238));
        
        private final Duration duration;
        private final String name;

        public boolean isDateBased() {
            return true;
        }

        public boolean isDurationEstimated() {
            return true;
        }

        public boolean isTimeBased() {
            return false;
        }

        private Unit(String str, Duration duration) {
            this.name = str;
            this.duration = duration;
        }

        public Duration getDuration() {
            return this.duration;
        }

        public boolean isSupportedBy(Temporal temporal) {
            return temporal.isSupported(ChronoField.EPOCH_DAY);
        }

        public <R extends Temporal> R addTo(R r, long j) {
            switch (this) {
                case WEEK_BASED_YEARS:
                    return r.with(IsoFields.WEEK_BASED_YEAR, Jdk8Methods.safeAdd((long) r.get(IsoFields.WEEK_BASED_YEAR), j));
                case QUARTER_YEARS:
                    return r.plus(j / 256, ChronoUnit.YEARS).plus((j % 256) * 3, ChronoUnit.MONTHS);
                default:
                    throw new IllegalStateException("Unreachable");
            }
        }

        public long between(Temporal temporal, Temporal temporal2) {
            switch (this) {
                case WEEK_BASED_YEARS:
                    return Jdk8Methods.safeSubtract(temporal2.getLong(IsoFields.WEEK_BASED_YEAR), temporal.getLong(IsoFields.WEEK_BASED_YEAR));
                case QUARTER_YEARS:
                    return temporal.until(temporal2, ChronoUnit.MONTHS) / 3;
                default:
                    throw new IllegalStateException("Unreachable");
            }
        }

        public String toString() {
            return this.name;
        }
    }

    private IsoFields() {
        throw new AssertionError("Not instantiable");
    }
}
