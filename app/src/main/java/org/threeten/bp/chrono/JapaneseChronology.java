package org.threeten.bp.chrono;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.Clock;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjusters;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.ValueRange;

public final class JapaneseChronology extends Chronology implements Serializable {
    private static final Map<String, String[]> ERA_FULL_NAMES = new HashMap();
    private static final Map<String, String[]> ERA_NARROW_NAMES = new HashMap();
    private static final Map<String, String[]> ERA_SHORT_NAMES = new HashMap();
    private static final String FALLBACK_LANGUAGE = "en";
    public static final JapaneseChronology INSTANCE = new JapaneseChronology();
    static final Locale LOCALE = new Locale(TARGET_LANGUAGE, "JP", "JP");
    private static final String TARGET_LANGUAGE = "ja";
    private static final long serialVersionUID = 459996390165777884L;

    public String getCalendarType() {
        return "japanese";
    }

    public String getId() {
        return "Japanese";
    }

    static {
        ERA_NARROW_NAMES.put(FALLBACK_LANGUAGE, new String[]{"Unknown", "K", "M", "T", "S", "H"});
        ERA_NARROW_NAMES.put(TARGET_LANGUAGE, new String[]{"Unknown", "K", "M", "T", "S", "H"});
        ERA_SHORT_NAMES.put(FALLBACK_LANGUAGE, new String[]{"Unknown", "K", "M", "T", "S", "H"});
        ERA_SHORT_NAMES.put(TARGET_LANGUAGE, new String[]{"Unknown", "慶", "明", "大", "昭", "平"});
        ERA_FULL_NAMES.put(FALLBACK_LANGUAGE, new String[]{"Unknown", "Keio", "Meiji", "Taisho", "Showa", "Heisei"});
        ERA_FULL_NAMES.put(TARGET_LANGUAGE, new String[]{"Unknown", "慶応", "明治", "大正", "昭和", "平成"});
    }

    private JapaneseChronology() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public JapaneseDate date(Era era, int i, int i2, int i3) {
        if (era instanceof JapaneseEra) {
            return JapaneseDate.of((JapaneseEra) era, i, i2, i3);
        }
        throw new ClassCastException("Era must be JapaneseEra");
    }

    public JapaneseDate date(int i, int i2, int i3) {
        return new JapaneseDate(LocalDate.of(i, i2, i3));
    }

    public JapaneseDate dateYearDay(Era era, int i, int i2) {
        if (era instanceof JapaneseEra) {
            return JapaneseDate.ofYearDay((JapaneseEra) era, i, i2);
        }
        throw new ClassCastException("Era must be JapaneseEra");
    }

    public JapaneseDate dateYearDay(int i, int i2) {
        i2 = LocalDate.ofYearDay(i, i2);
        return date(i, i2.getMonthValue(), i2.getDayOfMonth());
    }

    public JapaneseDate dateEpochDay(long j) {
        return new JapaneseDate(LocalDate.ofEpochDay(j));
    }

    public JapaneseDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof JapaneseDate) {
            return (JapaneseDate) temporalAccessor;
        }
        return new JapaneseDate(LocalDate.from(temporalAccessor));
    }

    public ChronoLocalDateTime<JapaneseDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<JapaneseDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<JapaneseDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    public JapaneseDate dateNow() {
        return (JapaneseDate) super.dateNow();
    }

    public JapaneseDate dateNow(ZoneId zoneId) {
        return (JapaneseDate) super.dateNow(zoneId);
    }

    public JapaneseDate dateNow(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        return (JapaneseDate) super.dateNow(clock);
    }

    public boolean isLeapYear(long j) {
        return IsoChronology.INSTANCE.isLeapYear(j);
    }

    public int prolepticYear(Era era, int i) {
        if (era instanceof JapaneseEra) {
            JapaneseEra japaneseEra = (JapaneseEra) era;
            int year = (japaneseEra.startDate().getYear() + i) - 1;
            ValueRange.of(1, (long) ((japaneseEra.endDate().getYear() - japaneseEra.startDate().getYear()) + 1)).checkValidValue((long) i, ChronoField.YEAR_OF_ERA);
            return year;
        }
        throw new ClassCastException("Era must be JapaneseEra");
    }

    public JapaneseEra eraOf(int i) {
        return JapaneseEra.of(i);
    }

    public List<Era> eras() {
        return Arrays.asList(JapaneseEra.values());
    }

    public ValueRange range(ChronoField chronoField) {
        switch (chronoField) {
            case DAY_OF_MONTH:
            case DAY_OF_WEEK:
            case MICRO_OF_DAY:
            case MICRO_OF_SECOND:
            case HOUR_OF_DAY:
            case HOUR_OF_AMPM:
            case MINUTE_OF_DAY:
            case MINUTE_OF_HOUR:
            case SECOND_OF_DAY:
            case SECOND_OF_MINUTE:
            case MILLI_OF_DAY:
            case MILLI_OF_SECOND:
            case NANO_OF_DAY:
            case NANO_OF_SECOND:
            case CLOCK_HOUR_OF_DAY:
            case CLOCK_HOUR_OF_AMPM:
            case EPOCH_DAY:
            case PROLEPTIC_MONTH:
                return chronoField.range();
            default:
                Calendar instance = Calendar.getInstance(LOCALE);
                int i = 0;
                int year;
                switch (chronoField) {
                    case ERA:
                        chronoField = JapaneseEra.values();
                        return ValueRange.of((long) chronoField[0].getValue(), (long) chronoField[chronoField.length - 1].getValue());
                    case YEAR:
                        chronoField = JapaneseEra.values();
                        return ValueRange.of((long) JapaneseDate.MIN_DATE.getYear(), (long) chronoField[chronoField.length - 1].endDate().getYear());
                    case YEAR_OF_ERA:
                        chronoField = JapaneseEra.values();
                        year = (chronoField[chronoField.length - 1].endDate().getYear() - chronoField[chronoField.length - 1].startDate().getYear()) + 1;
                        int i2 = Integer.MAX_VALUE;
                        while (i < chronoField.length) {
                            i2 = Math.min(i2, (chronoField[i].endDate().getYear() - chronoField[i].startDate().getYear()) + 1);
                            i++;
                        }
                        return ValueRange.of(1, 6, (long) i2, (long) year);
                    case MONTH_OF_YEAR:
                        return ValueRange.of((long) (instance.getMinimum(2) + 1), (long) (instance.getGreatestMinimum(2) + 1), (long) (instance.getLeastMaximum(2) + 1), (long) (instance.getMaximum(2) + 1));
                    case DAY_OF_YEAR:
                        chronoField = JapaneseEra.values();
                        year = 366;
                        while (i < chronoField.length) {
                            year = Math.min(year, (chronoField[i].startDate().lengthOfYear() - chronoField[i].startDate().getDayOfYear()) + 1);
                            i++;
                        }
                        return ValueRange.of(1, (long) year, 366);
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unimplementable field: ");
                        stringBuilder.append(chronoField);
                        throw new UnsupportedOperationException(stringBuilder.toString());
                }
        }
    }

    public JapaneseDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        if (map.containsKey(ChronoField.EPOCH_DAY)) {
            return dateEpochDay(((Long) map.remove(ChronoField.EPOCH_DAY)).longValue());
        }
        Long l = (Long) map.remove(ChronoField.PROLEPTIC_MONTH);
        if (l != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.PROLEPTIC_MONTH.checkValidValue(l.longValue());
            }
            updateResolveMap(map, ChronoField.MONTH_OF_YEAR, (long) (Jdk8Methods.floorMod(l.longValue(), 12) + 1));
            updateResolveMap(map, ChronoField.YEAR, Jdk8Methods.floorDiv(l.longValue(), 12));
        }
        l = (Long) map.get(ChronoField.ERA);
        JapaneseEra eraOf = l != null ? eraOf(range(ChronoField.ERA).checkValidIntValue(l.longValue(), ChronoField.ERA)) : null;
        Long l2 = (Long) map.get(ChronoField.YEAR_OF_ERA);
        if (l2 != null) {
            int checkValidIntValue = range(ChronoField.YEAR_OF_ERA).checkValidIntValue(l2.longValue(), ChronoField.YEAR_OF_ERA);
            if (!(eraOf != null || resolverStyle == ResolverStyle.STRICT || map.containsKey(ChronoField.YEAR))) {
                List eras = eras();
                eraOf = (JapaneseEra) eras.get(eras.size() - 1);
            }
            if (eraOf != null && map.containsKey(ChronoField.MONTH_OF_YEAR) && map.containsKey(ChronoField.DAY_OF_MONTH)) {
                map.remove(ChronoField.ERA);
                map.remove(ChronoField.YEAR_OF_ERA);
                return resolveEYMD(map, resolverStyle, eraOf, checkValidIntValue);
            } else if (eraOf != null && map.containsKey(ChronoField.DAY_OF_YEAR)) {
                map.remove(ChronoField.ERA);
                map.remove(ChronoField.YEAR_OF_ERA);
                return resolveEYD(map, resolverStyle, eraOf, checkValidIntValue);
            }
        }
        if (map.containsKey(ChronoField.YEAR)) {
            int checkValidIntValue2;
            if (map.containsKey(ChronoField.MONTH_OF_YEAR)) {
                int checkValidIntValue3;
                if (map.containsKey(ChronoField.DAY_OF_MONTH)) {
                    checkValidIntValue2 = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return date(checkValidIntValue2, 1, 1).plusMonths(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1)).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_MONTH)).longValue(), 1));
                    }
                    checkValidIntValue3 = range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), ChronoField.MONTH_OF_YEAR);
                    int checkValidIntValue4 = range(ChronoField.DAY_OF_MONTH).checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_MONTH)).longValue(), ChronoField.DAY_OF_MONTH);
                    if (resolverStyle == ResolverStyle.SMART && checkValidIntValue4 > 28) {
                        checkValidIntValue4 = Math.min(checkValidIntValue4, date(checkValidIntValue2, checkValidIntValue3, 1).lengthOfMonth());
                    }
                    return date(checkValidIntValue2, checkValidIntValue3, checkValidIntValue4);
                } else if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_MONTH)) {
                    long safeSubtract;
                    if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)) {
                        checkValidIntValue2 = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            safeSubtract = Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1);
                            return date(checkValidIntValue2, 1, 1).plus(safeSubtract, ChronoUnit.MONTHS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).longValue(), 1), ChronoUnit.DAYS);
                        }
                        checkValidIntValue3 = ChronoField.MONTH_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue());
                        map = date(checkValidIntValue2, checkValidIntValue3, 1).plus((long) (((ChronoField.ALIGNED_WEEK_OF_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue()) - 1) * 7) + (ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).longValue()) - 1)), ChronoUnit.DAYS);
                        if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.MONTH_OF_YEAR) == checkValidIntValue3) {
                            return map;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    } else if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                        checkValidIntValue2 = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            safeSubtract = Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1);
                            return date(checkValidIntValue2, 1, 1).plus(safeSubtract, ChronoUnit.MONTHS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue(), 1), ChronoUnit.DAYS);
                        }
                        checkValidIntValue3 = ChronoField.MONTH_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue());
                        map = date(checkValidIntValue2, checkValidIntValue3, 1).plus((long) (ChronoField.ALIGNED_WEEK_OF_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue()) - 1), ChronoUnit.WEEKS).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue()))));
                        if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.MONTH_OF_YEAR) == checkValidIntValue3) {
                            return map;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    }
                }
            }
            if (map.containsKey(ChronoField.DAY_OF_YEAR)) {
                checkValidIntValue2 = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                if (resolverStyle != ResolverStyle.LENIENT) {
                    return dateYearDay(checkValidIntValue2, ChronoField.DAY_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue()));
                }
                return dateYearDay(checkValidIntValue2, 1).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue(), 1));
            } else if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
                if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)) {
                    checkValidIntValue2 = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return date(checkValidIntValue2, 1, 1).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).longValue(), 1), ChronoUnit.DAYS);
                    }
                    map = date(checkValidIntValue2, 1, 1).plusDays((long) (((ChronoField.ALIGNED_WEEK_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue()) - 1) * 7) + (ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).longValue()) - 1)));
                    if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.YEAR) == checkValidIntValue2) {
                        return map;
                    }
                    throw new DateTimeException("Strict mode rejected date parsed to a different year");
                } else if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                    checkValidIntValue2 = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return date(checkValidIntValue2, 1, 1).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue(), 1), ChronoUnit.DAYS);
                    }
                    map = date(checkValidIntValue2, 1, 1).plus((long) (ChronoField.ALIGNED_WEEK_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue()) - 1), ChronoUnit.WEEKS).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue()))));
                    if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.YEAR) == checkValidIntValue2) {
                        return map;
                    }
                    throw new DateTimeException("Strict mode rejected date parsed to a different month");
                }
            }
        }
        return null;
    }

    private JapaneseDate resolveEYMD(Map<TemporalField, Long> map, ResolverStyle resolverStyle, JapaneseEra japaneseEra, int i) {
        if (resolverStyle == ResolverStyle.LENIENT) {
            int year = (japaneseEra.startDate().getYear() + i) - 1;
            long safeSubtract = Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1);
            return date(year, 1, 1).plus(safeSubtract, ChronoUnit.MONTHS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_MONTH)).longValue(), 1), ChronoUnit.DAYS);
        }
        int checkValidIntValue = range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), ChronoField.MONTH_OF_YEAR);
        int checkValidIntValue2 = range(ChronoField.DAY_OF_MONTH).checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_MONTH)).longValue(), ChronoField.DAY_OF_MONTH);
        if (resolverStyle != ResolverStyle.SMART) {
            return date((Era) japaneseEra, i, checkValidIntValue, checkValidIntValue2);
        }
        if (i < 1) {
            resolverStyle = new StringBuilder();
            resolverStyle.append("Invalid YearOfEra: ");
            resolverStyle.append(i);
            throw new DateTimeException(resolverStyle.toString());
        }
        year = (japaneseEra.startDate().getYear() + i) - 1;
        if (checkValidIntValue2 > 28) {
            checkValidIntValue2 = Math.min(checkValidIntValue2, date(year, checkValidIntValue, 1).lengthOfMonth());
        }
        map = date(year, checkValidIntValue, checkValidIntValue2);
        if (map.getEra() != japaneseEra) {
            if (Math.abs(map.getEra().getValue() - japaneseEra.getValue()) > 1) {
                resolverStyle = new StringBuilder();
                resolverStyle.append("Invalid Era/YearOfEra: ");
                resolverStyle.append(japaneseEra);
                resolverStyle.append(StringUtils.SPACE);
                resolverStyle.append(i);
                throw new DateTimeException(resolverStyle.toString());
            } else if (!(map.get(ChronoField.YEAR_OF_ERA) == 1 || i == 1)) {
                resolverStyle = new StringBuilder();
                resolverStyle.append("Invalid Era/YearOfEra: ");
                resolverStyle.append(japaneseEra);
                resolverStyle.append(StringUtils.SPACE);
                resolverStyle.append(i);
                throw new DateTimeException(resolverStyle.toString());
            }
        }
        return map;
    }

    private JapaneseDate resolveEYD(Map<TemporalField, Long> map, ResolverStyle resolverStyle, JapaneseEra japaneseEra, int i) {
        if (resolverStyle != ResolverStyle.LENIENT) {
            return dateYearDay((Era) japaneseEra, i, range(ChronoField.DAY_OF_YEAR).checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue(), ChronoField.DAY_OF_YEAR));
        }
        int year = (japaneseEra.startDate().getYear() + i) - 1;
        return dateYearDay(year, 1).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue(), 1), ChronoUnit.DAYS);
    }
}
