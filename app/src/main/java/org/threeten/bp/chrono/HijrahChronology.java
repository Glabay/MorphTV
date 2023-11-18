package org.threeten.bp.chrono;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public final class HijrahChronology extends Chronology implements Serializable {
    private static final HashMap<String, String[]> ERA_FULL_NAMES = new HashMap();
    private static final HashMap<String, String[]> ERA_NARROW_NAMES = new HashMap();
    private static final HashMap<String, String[]> ERA_SHORT_NAMES = new HashMap();
    private static final String FALLBACK_LANGUAGE = "en";
    public static final HijrahChronology INSTANCE = new HijrahChronology();
    private static final long serialVersionUID = 3127340209035924785L;

    public String getCalendarType() {
        return "islamic-umalqura";
    }

    public String getId() {
        return "Hijrah-umalqura";
    }

    static {
        ERA_NARROW_NAMES.put(FALLBACK_LANGUAGE, new String[]{"BH", "HE"});
        ERA_SHORT_NAMES.put(FALLBACK_LANGUAGE, new String[]{"B.H.", "H.E."});
        ERA_FULL_NAMES.put(FALLBACK_LANGUAGE, new String[]{"Before Hijrah", "Hijrah Era"});
    }

    private HijrahChronology() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public HijrahDate date(Era era, int i, int i2, int i3) {
        return (HijrahDate) super.date(era, i, i2, i3);
    }

    public HijrahDate date(int i, int i2, int i3) {
        return HijrahDate.of(i, i2, i3);
    }

    public HijrahDate dateYearDay(Era era, int i, int i2) {
        return (HijrahDate) super.dateYearDay(era, i, i2);
    }

    public HijrahDate dateYearDay(int i, int i2) {
        return HijrahDate.of(i, 1, 1).plusDays((long) (i2 - 1));
    }

    public HijrahDate dateEpochDay(long j) {
        return HijrahDate.of(LocalDate.ofEpochDay(j));
    }

    public HijrahDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof HijrahDate) {
            return (HijrahDate) temporalAccessor;
        }
        return HijrahDate.ofEpochDay(temporalAccessor.getLong(ChronoField.EPOCH_DAY));
    }

    public ChronoLocalDateTime<HijrahDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<HijrahDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    public ChronoZonedDateTime<HijrahDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    public HijrahDate dateNow() {
        return (HijrahDate) super.dateNow();
    }

    public HijrahDate dateNow(ZoneId zoneId) {
        return (HijrahDate) super.dateNow(zoneId);
    }

    public HijrahDate dateNow(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        return (HijrahDate) super.dateNow(clock);
    }

    public boolean isLeapYear(long j) {
        return HijrahDate.isLeapYear(j);
    }

    public int prolepticYear(Era era, int i) {
        if (era instanceof HijrahEra) {
            return era == HijrahEra.AH ? i : 1 - i;
        } else {
            throw new ClassCastException("Era must be HijrahEra");
        }
    }

    public HijrahEra eraOf(int i) {
        switch (i) {
            case 0:
                return HijrahEra.BEFORE_AH;
            case 1:
                return HijrahEra.AH;
            default:
                throw new DateTimeException("invalid Hijrah era");
        }
    }

    public List<Era> eras() {
        return Arrays.asList(HijrahEra.values());
    }

    public ValueRange range(ChronoField chronoField) {
        return chronoField.range();
    }

    public HijrahDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        if (map.containsKey(ChronoField.EPOCH_DAY)) {
            return dateEpochDay(((Long) map.remove(ChronoField.EPOCH_DAY)).longValue());
        }
        long safeSubtract;
        Long l = (Long) map.remove(ChronoField.PROLEPTIC_MONTH);
        if (l != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.PROLEPTIC_MONTH.checkValidValue(l.longValue());
            }
            updateResolveMap(map, ChronoField.MONTH_OF_YEAR, (long) (Jdk8Methods.floorMod(l.longValue(), 12) + 1));
            updateResolveMap(map, ChronoField.YEAR, Jdk8Methods.floorDiv(l.longValue(), 12));
        }
        l = (Long) map.remove(ChronoField.YEAR_OF_ERA);
        if (l != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.YEAR_OF_ERA.checkValidValue(l.longValue());
            }
            Long l2 = (Long) map.remove(ChronoField.ERA);
            if (l2 == null) {
                l2 = (Long) map.get(ChronoField.YEAR);
                if (resolverStyle != ResolverStyle.STRICT) {
                    ChronoField chronoField = ChronoField.YEAR;
                    if (l2 != null) {
                        if (l2.longValue() <= 0) {
                            safeSubtract = Jdk8Methods.safeSubtract(1, l.longValue());
                            updateResolveMap(map, chronoField, safeSubtract);
                        }
                    }
                    safeSubtract = l.longValue();
                    updateResolveMap(map, chronoField, safeSubtract);
                } else if (l2 != null) {
                    updateResolveMap(map, ChronoField.YEAR, l2.longValue() > 0 ? l.longValue() : Jdk8Methods.safeSubtract(1, l.longValue()));
                } else {
                    map.put(ChronoField.YEAR_OF_ERA, l);
                }
            } else if (l2.longValue() == 1) {
                updateResolveMap(map, ChronoField.YEAR, l.longValue());
            } else if (l2.longValue() == 0) {
                updateResolveMap(map, ChronoField.YEAR, Jdk8Methods.safeSubtract(1, l.longValue()));
            } else {
                resolverStyle = new StringBuilder();
                resolverStyle.append("Invalid value for era: ");
                resolverStyle.append(l2);
                throw new DateTimeException(resolverStyle.toString());
            }
        } else if (map.containsKey(ChronoField.ERA)) {
            ChronoField.ERA.checkValidValue(((Long) map.get(ChronoField.ERA)).longValue());
        }
        if (map.containsKey(ChronoField.YEAR)) {
            int checkValidIntValue;
            if (map.containsKey(ChronoField.MONTH_OF_YEAR)) {
                int checkValidIntValue2;
                if (map.containsKey(ChronoField.DAY_OF_MONTH)) {
                    checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return date(checkValidIntValue, 1, 1).plusMonths(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1)).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_MONTH)).longValue(), 1));
                    }
                    checkValidIntValue2 = range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), ChronoField.MONTH_OF_YEAR);
                    int checkValidIntValue3 = range(ChronoField.DAY_OF_MONTH).checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_MONTH)).longValue(), ChronoField.DAY_OF_MONTH);
                    if (resolverStyle == ResolverStyle.SMART && checkValidIntValue3 > 28) {
                        checkValidIntValue3 = Math.min(checkValidIntValue3, date(checkValidIntValue, checkValidIntValue2, 1).lengthOfMonth());
                    }
                    return date(checkValidIntValue, checkValidIntValue2, checkValidIntValue3);
                } else if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_MONTH)) {
                    if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)) {
                        checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            safeSubtract = Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1);
                            return date(checkValidIntValue, 1, 1).plus(safeSubtract, ChronoUnit.MONTHS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).longValue(), 1), ChronoUnit.DAYS);
                        }
                        checkValidIntValue2 = ChronoField.MONTH_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue());
                        map = date(checkValidIntValue, checkValidIntValue2, 1).plus((long) (((ChronoField.ALIGNED_WEEK_OF_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue()) - 1) * 7) + (ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).longValue()) - 1)), ChronoUnit.DAYS);
                        if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.MONTH_OF_YEAR) == checkValidIntValue2) {
                            return map;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    } else if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                        checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            safeSubtract = Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1);
                            return date(checkValidIntValue, 1, 1).plus(safeSubtract, ChronoUnit.MONTHS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue(), 1), ChronoUnit.DAYS);
                        }
                        checkValidIntValue2 = ChronoField.MONTH_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue());
                        map = date(checkValidIntValue, checkValidIntValue2, 1).plus((long) (ChronoField.ALIGNED_WEEK_OF_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue()) - 1), ChronoUnit.WEEKS).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue()))));
                        if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.MONTH_OF_YEAR) == checkValidIntValue2) {
                            return map;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    }
                }
            }
            if (map.containsKey(ChronoField.DAY_OF_YEAR)) {
                checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                if (resolverStyle != ResolverStyle.LENIENT) {
                    return dateYearDay(checkValidIntValue, ChronoField.DAY_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue()));
                }
                return dateYearDay(checkValidIntValue, 1).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue(), 1));
            } else if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
                if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)) {
                    checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return date(checkValidIntValue, 1, 1).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).longValue(), 1), ChronoUnit.DAYS);
                    }
                    map = date(checkValidIntValue, 1, 1).plusDays((long) (((ChronoField.ALIGNED_WEEK_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue()) - 1) * 7) + (ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).longValue()) - 1)));
                    if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.YEAR) == checkValidIntValue) {
                        return map;
                    }
                    throw new DateTimeException("Strict mode rejected date parsed to a different year");
                } else if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                    checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return date(checkValidIntValue, 1, 1).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue(), 1), ChronoUnit.WEEKS).plus(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue(), 1), ChronoUnit.DAYS);
                    }
                    map = date(checkValidIntValue, 1, 1).plus((long) (ChronoField.ALIGNED_WEEK_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue()) - 1), ChronoUnit.WEEKS).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue()))));
                    if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.YEAR) == checkValidIntValue) {
                        return map;
                    }
                    throw new DateTimeException("Strict mode rejected date parsed to a different month");
                }
            }
        }
        return null;
    }
}
