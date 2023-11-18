package org.threeten.bp.chrono;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.threeten.bp.Clock;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;
import org.threeten.bp.Year;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjusters;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.ValueRange;

public final class IsoChronology extends Chronology implements Serializable {
    public static final IsoChronology INSTANCE = new IsoChronology();
    private static final long serialVersionUID = -1440403870442975015L;

    public String getCalendarType() {
        return "iso8601";
    }

    public String getId() {
        return "ISO";
    }

    private IsoChronology() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public LocalDate date(Era era, int i, int i2, int i3) {
        return date(prolepticYear(era, i), i2, i3);
    }

    public LocalDate date(int i, int i2, int i3) {
        return LocalDate.of(i, i2, i3);
    }

    public LocalDate dateYearDay(Era era, int i, int i2) {
        return dateYearDay(prolepticYear(era, i), i2);
    }

    public LocalDate dateYearDay(int i, int i2) {
        return LocalDate.ofYearDay(i, i2);
    }

    public LocalDate dateEpochDay(long j) {
        return LocalDate.ofEpochDay(j);
    }

    public LocalDate date(TemporalAccessor temporalAccessor) {
        return LocalDate.from(temporalAccessor);
    }

    public LocalDateTime localDateTime(TemporalAccessor temporalAccessor) {
        return LocalDateTime.from(temporalAccessor);
    }

    public ZonedDateTime zonedDateTime(TemporalAccessor temporalAccessor) {
        return ZonedDateTime.from(temporalAccessor);
    }

    public ZonedDateTime zonedDateTime(Instant instant, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    public LocalDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    public LocalDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    public LocalDate dateNow(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        return date(LocalDate.now(clock));
    }

    public boolean isLeapYear(long j) {
        return ((j & 3) != 0 || (j % 100 == 0 && j % 400 != 0)) ? 0 : 1;
    }

    public int prolepticYear(Era era, int i) {
        if (era instanceof IsoEra) {
            return era == IsoEra.CE ? i : 1 - i;
        } else {
            throw new ClassCastException("Era must be IsoEra");
        }
    }

    public IsoEra eraOf(int i) {
        return IsoEra.of(i);
    }

    public List<Era> eras() {
        return Arrays.asList(IsoEra.values());
    }

    public ValueRange range(ChronoField chronoField) {
        return chronoField.range();
    }

    public LocalDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        if (map.containsKey(ChronoField.EPOCH_DAY)) {
            return LocalDate.ofEpochDay(((Long) map.remove(ChronoField.EPOCH_DAY)).longValue());
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
                int safeToInt;
                if (map.containsKey(ChronoField.DAY_OF_MONTH)) {
                    checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    safeToInt = Jdk8Methods.safeToInt(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue());
                    int safeToInt2 = Jdk8Methods.safeToInt(((Long) map.remove(ChronoField.DAY_OF_MONTH)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return LocalDate.of(checkValidIntValue, 1, 1).plusMonths((long) Jdk8Methods.safeSubtract(safeToInt, 1)).plusDays((long) Jdk8Methods.safeSubtract(safeToInt2, 1));
                    } else if (resolverStyle != ResolverStyle.SMART) {
                        return LocalDate.of(checkValidIntValue, safeToInt, safeToInt2);
                    } else {
                        ChronoField.DAY_OF_MONTH.checkValidValue((long) safeToInt2);
                        if (!(safeToInt == 4 || safeToInt == 6 || safeToInt == 9)) {
                            if (safeToInt != 11) {
                                if (safeToInt == 2) {
                                    safeToInt2 = Math.min(safeToInt2, Month.FEBRUARY.length(Year.isLeap((long) checkValidIntValue)));
                                }
                                return LocalDate.of(checkValidIntValue, safeToInt, safeToInt2);
                            }
                        }
                        safeToInt2 = Math.min(safeToInt2, 30);
                        return LocalDate.of(checkValidIntValue, safeToInt, safeToInt2);
                    }
                } else if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_MONTH)) {
                    if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)) {
                        checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            safeSubtract = Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1);
                            return LocalDate.of(checkValidIntValue, 1, 1).plusMonths(safeSubtract).plusWeeks(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue(), 1)).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).longValue(), 1));
                        }
                        safeToInt = ChronoField.MONTH_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue());
                        map = LocalDate.of(checkValidIntValue, safeToInt, 1).plusDays((long) (((ChronoField.ALIGNED_WEEK_OF_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue()) - 1) * 7) + (ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).longValue()) - 1)));
                        if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.MONTH_OF_YEAR) == safeToInt) {
                            return map;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    } else if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                        checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                        if (resolverStyle == ResolverStyle.LENIENT) {
                            safeSubtract = Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue(), 1);
                            return LocalDate.of(checkValidIntValue, 1, 1).plusMonths(safeSubtract).plusWeeks(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue(), 1)).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue(), 1));
                        }
                        safeToInt = ChronoField.MONTH_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.MONTH_OF_YEAR)).longValue());
                        map = LocalDate.of(checkValidIntValue, safeToInt, 1).plusWeeks((long) (ChronoField.ALIGNED_WEEK_OF_MONTH.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_MONTH)).longValue()) - 1)).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue()))));
                        if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.MONTH_OF_YEAR) == safeToInt) {
                            return map;
                        }
                        throw new DateTimeException("Strict mode rejected date parsed to a different month");
                    }
                }
            }
            if (map.containsKey(ChronoField.DAY_OF_YEAR)) {
                checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                if (resolverStyle != ResolverStyle.LENIENT) {
                    return LocalDate.ofYearDay(checkValidIntValue, ChronoField.DAY_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue()));
                }
                return LocalDate.ofYearDay(checkValidIntValue, 1).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_YEAR)).longValue(), 1));
            } else if (map.containsKey(ChronoField.ALIGNED_WEEK_OF_YEAR)) {
                if (map.containsKey(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)) {
                    checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return LocalDate.of(checkValidIntValue, 1, 1).plusWeeks(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue(), 1)).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).longValue(), 1));
                    }
                    map = LocalDate.of(checkValidIntValue, 1, 1).plusDays((long) (((ChronoField.ALIGNED_WEEK_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue()) - 1) * 7) + (ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).longValue()) - 1)));
                    if (resolverStyle != ResolverStyle.STRICT || map.get(ChronoField.YEAR) == checkValidIntValue) {
                        return map;
                    }
                    throw new DateTimeException("Strict mode rejected date parsed to a different year");
                } else if (map.containsKey(ChronoField.DAY_OF_WEEK)) {
                    checkValidIntValue = ChronoField.YEAR.checkValidIntValue(((Long) map.remove(ChronoField.YEAR)).longValue());
                    if (resolverStyle == ResolverStyle.LENIENT) {
                        return LocalDate.of(checkValidIntValue, 1, 1).plusWeeks(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue(), 1)).plusDays(Jdk8Methods.safeSubtract(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue(), 1));
                    }
                    map = LocalDate.of(checkValidIntValue, 1, 1).plusWeeks((long) (ChronoField.ALIGNED_WEEK_OF_YEAR.checkValidIntValue(((Long) map.remove(ChronoField.ALIGNED_WEEK_OF_YEAR)).longValue()) - 1)).with(TemporalAdjusters.nextOrSame(DayOfWeek.of(ChronoField.DAY_OF_WEEK.checkValidIntValue(((Long) map.remove(ChronoField.DAY_OF_WEEK)).longValue()))));
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
