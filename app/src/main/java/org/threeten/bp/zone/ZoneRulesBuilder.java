package org.threeten.bp.zone;

import com.google.android.gms.cast.CastStatusCodes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.Year;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.TemporalAdjusters;
import org.threeten.bp.zone.ZoneOffsetTransitionRule.TimeDefinition;

class ZoneRulesBuilder {
    private Map<Object, Object> deduplicateMap;
    private List<TZWindow> windowList = new ArrayList();

    class TZRule implements Comparable<TZRule> {
        private int dayOfMonthIndicator;
        private DayOfWeek dayOfWeek;
        private Month month;
        private int savingAmountSecs;
        private LocalTime time;
        private TimeDefinition timeDefinition;
        private boolean timeEndOfDay;
        private int year;

        TZRule(int i, Month month, int i2, DayOfWeek dayOfWeek, LocalTime localTime, boolean z, TimeDefinition timeDefinition, int i3) {
            this.year = i;
            this.month = month;
            this.dayOfMonthIndicator = i2;
            this.dayOfWeek = dayOfWeek;
            this.time = localTime;
            this.timeEndOfDay = z;
            this.timeDefinition = timeDefinition;
            this.savingAmountSecs = i3;
        }

        ZoneOffsetTransition toTransition(ZoneOffset zoneOffset, int i) {
            ZoneOffset zoneOffset2 = (ZoneOffset) ZoneRulesBuilder.this.deduplicate(ZoneOffset.ofTotalSeconds(zoneOffset.getTotalSeconds() + i));
            return new ZoneOffsetTransition((LocalDateTime) ZoneRulesBuilder.this.deduplicate(this.timeDefinition.createDateTime((LocalDateTime) ZoneRulesBuilder.this.deduplicate(LocalDateTime.of((LocalDate) ZoneRulesBuilder.this.deduplicate(toLocalDate()), this.time)), zoneOffset, zoneOffset2)), zoneOffset2, (ZoneOffset) ZoneRulesBuilder.this.deduplicate(ZoneOffset.ofTotalSeconds(zoneOffset.getTotalSeconds() + this.savingAmountSecs)));
        }

        ZoneOffsetTransitionRule toTransitionRule(ZoneOffset zoneOffset, int i) {
            if (this.dayOfMonthIndicator < 0 && this.month != Month.FEBRUARY) {
                this.dayOfMonthIndicator = this.month.maxLength() - 6;
            }
            if (this.timeEndOfDay && this.dayOfMonthIndicator > 0) {
                Object obj = (this.dayOfMonthIndicator == 28 && this.month == Month.FEBRUARY) ? 1 : null;
                if (obj == null) {
                    LocalDate plusDays = LocalDate.of((int) CastStatusCodes.APPLICATION_NOT_FOUND, this.month, this.dayOfMonthIndicator).plusDays(1);
                    this.month = plusDays.getMonth();
                    this.dayOfMonthIndicator = plusDays.getDayOfMonth();
                    if (this.dayOfWeek != null) {
                        this.dayOfWeek = this.dayOfWeek.plus(1);
                    }
                    this.timeEndOfDay = false;
                }
            }
            i = toTransition(zoneOffset, i);
            return new ZoneOffsetTransitionRule(this.month, this.dayOfMonthIndicator, this.dayOfWeek, this.time, this.timeEndOfDay, this.timeDefinition, zoneOffset, i.getOffsetBefore(), i.getOffsetAfter());
        }

        public int compareTo(TZRule tZRule) {
            int i = this.year - tZRule.year;
            if (i == 0) {
                i = this.month.compareTo(tZRule.month);
            }
            if (i == 0) {
                i = toLocalDate().compareTo(tZRule.toLocalDate());
            }
            return i == 0 ? this.time.compareTo(tZRule.time) : i;
        }

        private LocalDate toLocalDate() {
            LocalDate of;
            if (this.dayOfMonthIndicator < 0) {
                of = LocalDate.of(this.year, this.month, (this.month.length(IsoChronology.INSTANCE.isLeapYear((long) this.year)) + 1) + this.dayOfMonthIndicator);
                if (this.dayOfWeek != null) {
                    of = of.with(TemporalAdjusters.previousOrSame(this.dayOfWeek));
                }
            } else {
                of = LocalDate.of(this.year, this.month, this.dayOfMonthIndicator);
                if (this.dayOfWeek != null) {
                    of = of.with(TemporalAdjusters.nextOrSame(this.dayOfWeek));
                }
            }
            return this.timeEndOfDay ? of.plusDays(1) : of;
        }
    }

    class TZWindow {
        private Integer fixedSavingAmountSecs;
        private List<TZRule> lastRuleList = new ArrayList();
        private int maxLastRuleStartYear = Year.MIN_VALUE;
        private List<TZRule> ruleList = new ArrayList();
        private final ZoneOffset standardOffset;
        private final TimeDefinition timeDefinition;
        private final LocalDateTime windowEnd;

        TZWindow(ZoneOffset zoneOffset, LocalDateTime localDateTime, TimeDefinition timeDefinition) {
            this.windowEnd = localDateTime;
            this.timeDefinition = timeDefinition;
            this.standardOffset = zoneOffset;
        }

        void setFixedSavings(int i) {
            if (this.ruleList.size() <= 0) {
                if (this.lastRuleList.size() <= 0) {
                    this.fixedSavingAmountSecs = Integer.valueOf(i);
                    return;
                }
            }
            throw new IllegalStateException("Window has DST rules, so cannot have fixed savings");
        }

        void addRule(int i, int i2, Month month, int i3, DayOfWeek dayOfWeek, LocalTime localTime, boolean z, TimeDefinition timeDefinition, int i4) {
            if (this.fixedSavingAmountSecs != null) {
                throw new IllegalStateException("Window has a fixed DST saving, so cannot have DST rules");
            } else if (r0.ruleList.size() >= 2000) {
                throw new IllegalStateException("Window has reached the maximum number of allowed rules");
            } else {
                Object obj = null;
                int i5 = i2;
                if (i5 == 999999999) {
                    obj = 1;
                    i5 = i;
                }
                for (int i6 = i; i6 <= i5; i6++) {
                    TZRule tZRule = new TZRule(i6, month, i3, dayOfWeek, localTime, z, timeDefinition, i4);
                    if (obj != null) {
                        r0.lastRuleList.add(tZRule);
                        r0.maxLastRuleStartYear = Math.max(i, r0.maxLastRuleStartYear);
                    } else {
                        int i7 = i;
                        r0.ruleList.add(tZRule);
                    }
                }
            }
        }

        void validateWindowOrder(TZWindow tZWindow) {
            if (this.windowEnd.isBefore(tZWindow.windowEnd)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Windows must be added in date-time order: ");
                stringBuilder.append(this.windowEnd);
                stringBuilder.append(" < ");
                stringBuilder.append(tZWindow.windowEnd);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }

        void tidy(int i) {
            if (this.lastRuleList.size() == 1) {
                throw new IllegalStateException("Cannot have only one rule defined as being forever");
            }
            if (this.windowEnd.equals(LocalDateTime.MAX)) {
                this.maxLastRuleStartYear = Math.max(this.maxLastRuleStartYear, i) + 1;
                for (TZRule tZRule : this.lastRuleList) {
                    addRule(tZRule.year, this.maxLastRuleStartYear, tZRule.month, tZRule.dayOfMonthIndicator, tZRule.dayOfWeek, tZRule.time, tZRule.timeEndOfDay, tZRule.timeDefinition, tZRule.savingAmountSecs);
                    tZRule.year = this.maxLastRuleStartYear + 1;
                }
                if (this.maxLastRuleStartYear == Year.MAX_VALUE) {
                    this.lastRuleList.clear();
                } else {
                    this.maxLastRuleStartYear++;
                }
            } else {
                i = this.windowEnd.getYear();
                for (TZRule tZRule2 : this.lastRuleList) {
                    addRule(tZRule2.year, i + 1, tZRule2.month, tZRule2.dayOfMonthIndicator, tZRule2.dayOfWeek, tZRule2.time, tZRule2.timeEndOfDay, tZRule2.timeDefinition, tZRule2.savingAmountSecs);
                }
                this.lastRuleList.clear();
                this.maxLastRuleStartYear = Year.MAX_VALUE;
            }
            Collections.sort(this.ruleList);
            Collections.sort(this.lastRuleList);
            if (this.ruleList.size() == 0 && this.fixedSavingAmountSecs == 0) {
                this.fixedSavingAmountSecs = Integer.valueOf(0);
            }
        }

        boolean isSingleWindowStandardOffset() {
            return this.windowEnd.equals(LocalDateTime.MAX) && this.timeDefinition == TimeDefinition.WALL && this.fixedSavingAmountSecs == null && this.lastRuleList.isEmpty() && this.ruleList.isEmpty();
        }

        ZoneOffset createWallOffset(int i) {
            return ZoneOffset.ofTotalSeconds(this.standardOffset.getTotalSeconds() + i);
        }

        long createDateTimeEpochSecond(int i) {
            i = createWallOffset(i);
            return this.timeDefinition.createDateTime(this.windowEnd, this.standardOffset, i).toEpochSecond(i);
        }
    }

    public ZoneRulesBuilder addWindow(ZoneOffset zoneOffset, LocalDateTime localDateTime, TimeDefinition timeDefinition) {
        Jdk8Methods.requireNonNull(zoneOffset, "standardOffset");
        Jdk8Methods.requireNonNull(localDateTime, "until");
        Jdk8Methods.requireNonNull(timeDefinition, "untilDefinition");
        TZWindow tZWindow = new TZWindow(zoneOffset, localDateTime, timeDefinition);
        if (this.windowList.size() > null) {
            tZWindow.validateWindowOrder((TZWindow) this.windowList.get(this.windowList.size() - 1));
        }
        this.windowList.add(tZWindow);
        return this;
    }

    public ZoneRulesBuilder addWindowForever(ZoneOffset zoneOffset) {
        return addWindow(zoneOffset, LocalDateTime.MAX, TimeDefinition.WALL);
    }

    public ZoneRulesBuilder setFixedSavingsToWindow(int i) {
        if (this.windowList.isEmpty()) {
            throw new IllegalStateException("Must add a window before setting the fixed savings");
        }
        ((TZWindow) this.windowList.get(this.windowList.size() - 1)).setFixedSavings(i);
        return this;
    }

    public ZoneRulesBuilder addRuleToWindow(LocalDateTime localDateTime, TimeDefinition timeDefinition, int i) {
        Jdk8Methods.requireNonNull(localDateTime, "transitionDateTime");
        return addRuleToWindow(localDateTime.getYear(), localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), null, localDateTime.toLocalTime(), false, timeDefinition, i);
    }

    public ZoneRulesBuilder addRuleToWindow(int i, Month month, int i2, LocalTime localTime, boolean z, TimeDefinition timeDefinition, int i3) {
        return addRuleToWindow(i, i, month, i2, null, localTime, z, timeDefinition, i3);
    }

    public ZoneRulesBuilder addRuleToWindow(int i, int i2, Month month, int i3, DayOfWeek dayOfWeek, LocalTime localTime, boolean z, TimeDefinition timeDefinition, int i4) {
        ZoneRulesBuilder zoneRulesBuilder = this;
        int i5 = i3;
        LocalTime localTime2 = localTime;
        Month month2 = month;
        Jdk8Methods.requireNonNull(month2, "month");
        Jdk8Methods.requireNonNull(localTime2, "time");
        TimeDefinition timeDefinition2 = timeDefinition;
        Jdk8Methods.requireNonNull(timeDefinition2, "timeDefinition");
        int i6 = i;
        ChronoField.YEAR.checkValidValue((long) i6);
        int i7 = i2;
        ChronoField.YEAR.checkValidValue((long) i7);
        if (i5 >= -28 && i5 <= 31) {
            if (i5 != 0) {
                if (z && !localTime2.equals(LocalTime.MIDNIGHT)) {
                    throw new IllegalArgumentException("Time must be midnight when end of day flag is true");
                } else if (zoneRulesBuilder.windowList.isEmpty()) {
                    throw new IllegalStateException("Must add a window before adding a rule");
                } else {
                    ((TZWindow) zoneRulesBuilder.windowList.get(zoneRulesBuilder.windowList.size() - 1)).addRule(i6, i7, month2, i5, dayOfWeek, localTime2, z, timeDefinition2, i4);
                    return zoneRulesBuilder;
                }
            }
        }
        throw new IllegalArgumentException("Day of month indicator must be between -28 and 31 inclusive excluding zero");
    }

    public ZoneRules toRules(String str) {
        return toRules(str, new HashMap());
    }

    ZoneRules toRules(String str, Map<Object, Object> map) {
        Jdk8Methods.requireNonNull(str, "zoneId");
        this.deduplicateMap = map;
        if (this.windowList.isEmpty()) {
            throw new IllegalStateException("No windows have been added to the builder");
        }
        List arrayList = new ArrayList(4);
        List arrayList2 = new ArrayList(256);
        List arrayList3 = new ArrayList(2);
        int i = 0;
        TZWindow tZWindow = (TZWindow) r0.windowList.get(0);
        ZoneOffset access$000 = tZWindow.standardOffset;
        int intValue = tZWindow.fixedSavingAmountSecs != null ? tZWindow.fixedSavingAmountSecs.intValue() : 0;
        ZoneOffset zoneOffset = (ZoneOffset) deduplicate(ZoneOffset.ofTotalSeconds(access$000.getTotalSeconds() + intValue));
        LocalDateTime localDateTime = (LocalDateTime) deduplicate(LocalDateTime.of((int) Year.MIN_VALUE, 1, 1, 0, 0));
        Iterator it = r0.windowList.iterator();
        int i2 = intValue;
        ZoneOffset zoneOffset2 = zoneOffset;
        while (it.hasNext()) {
            Iterator it2;
            TZWindow tZWindow2 = (TZWindow) it.next();
            tZWindow2.tidy(localDateTime.getYear());
            Integer access$100 = tZWindow2.fixedSavingAmountSecs;
            if (access$100 == null) {
                access$100 = Integer.valueOf(i);
                for (TZRule tZRule : tZWindow2.ruleList) {
                    if (tZRule.toTransition(access$000, i2).toEpochSecond() > localDateTime.toEpochSecond(zoneOffset2)) {
                        break;
                    }
                    access$100 = Integer.valueOf(tZRule.savingAmountSecs);
                }
            }
            if (access$000.equals(tZWindow2.standardOffset)) {
                it2 = it;
            } else {
                it2 = it;
                arrayList.add(deduplicate(new ZoneOffsetTransition(LocalDateTime.ofEpochSecond(localDateTime.toEpochSecond(zoneOffset2), 0, access$000), access$000, tZWindow2.standardOffset)));
                access$000 = (ZoneOffset) deduplicate(tZWindow2.standardOffset);
            }
            ZoneOffset zoneOffset3 = (ZoneOffset) deduplicate(ZoneOffset.ofTotalSeconds(access$000.getTotalSeconds() + access$100.intValue()));
            if (!zoneOffset2.equals(zoneOffset3)) {
                arrayList2.add((ZoneOffsetTransition) deduplicate(new ZoneOffsetTransition(localDateTime, zoneOffset2, zoneOffset3)));
            }
            i = access$100.intValue();
            for (TZRule tZRule2 : tZWindow2.ruleList) {
                ZoneOffsetTransition zoneOffsetTransition = (ZoneOffsetTransition) deduplicate(tZRule2.toTransition(access$000, i));
                if ((zoneOffsetTransition.toEpochSecond() < localDateTime.toEpochSecond(zoneOffset2) ? 1 : null) == null && zoneOffsetTransition.toEpochSecond() < tZWindow2.createDateTimeEpochSecond(i) && !zoneOffsetTransition.getOffsetBefore().equals(zoneOffsetTransition.getOffsetAfter())) {
                    arrayList2.add(zoneOffsetTransition);
                    i = tZRule2.savingAmountSecs;
                }
            }
            i2 = i;
            for (TZRule tZRule3 : tZWindow2.lastRuleList) {
                arrayList3.add((ZoneOffsetTransitionRule) deduplicate(tZRule3.toTransitionRule(access$000, i2)));
                i2 = tZRule3.savingAmountSecs;
            }
            zoneOffset2 = (ZoneOffset) deduplicate(tZWindow2.createWallOffset(i2));
            i = 0;
            localDateTime = (LocalDateTime) deduplicate(LocalDateTime.ofEpochSecond(tZWindow2.createDateTimeEpochSecond(i2), 0, zoneOffset2));
            it = it2;
        }
        return new StandardZoneRules(tZWindow.standardOffset, zoneOffset, arrayList, arrayList2, arrayList3);
    }

    <T> T deduplicate(T t) {
        if (!this.deduplicateMap.containsKey(t)) {
            this.deduplicateMap.put(t, t);
        }
        return this.deduplicateMap.get(t);
    }
}
