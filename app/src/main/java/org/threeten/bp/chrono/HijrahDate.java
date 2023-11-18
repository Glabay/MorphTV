package org.threeten.bp.chrono;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import org.threeten.bp.Clock;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;

public final class HijrahDate extends ChronoDateImpl<HijrahDate> implements Serializable {
    private static final Long[] ADJUSTED_CYCLES = new Long[MAX_ADJUSTED_CYCLE];
    private static final HashMap<Integer, Integer[]> ADJUSTED_CYCLE_YEARS = new HashMap();
    private static final Integer[] ADJUSTED_LEAST_MAX_VALUES = new Integer[LEAST_MAX_VALUES.length];
    private static final Integer[] ADJUSTED_MAX_VALUES = new Integer[MAX_VALUES.length];
    private static final Integer[] ADJUSTED_MIN_VALUES = new Integer[MIN_VALUES.length];
    private static final HashMap<Integer, Integer[]> ADJUSTED_MONTH_DAYS = new HashMap();
    private static final HashMap<Integer, Integer[]> ADJUSTED_MONTH_LENGTHS = new HashMap();
    private static final int[] CYCLEYEAR_START_DATE = new int[]{0, 354, 709, 1063, 1417, 1772, 2126, 2481, 2835, 3189, 3544, 3898, 4252, 4607, 4961, 5315, 5670, 6024, 6379, 6733, 7087, 7442, 7796, 8150, 8505, 8859, 9214, 9568, 9922, 10277};
    private static final String DEFAULT_CONFIG_FILENAME = "hijrah_deviation.cfg";
    private static final String DEFAULT_CONFIG_PATH;
    private static final Integer[] DEFAULT_CYCLE_YEARS = new Integer[CYCLEYEAR_START_DATE.length];
    private static final Integer[] DEFAULT_LEAP_MONTH_DAYS = new Integer[LEAP_NUM_DAYS.length];
    private static final Integer[] DEFAULT_LEAP_MONTH_LENGTHS = new Integer[LEAP_MONTH_LENGTH.length];
    private static final Integer[] DEFAULT_MONTH_DAYS = new Integer[NUM_DAYS.length];
    private static final Integer[] DEFAULT_MONTH_LENGTHS = new Integer[MONTH_LENGTH.length];
    private static final char FILE_SEP = File.separatorChar;
    private static final int HIJRAH_JAN_1_1_GREGORIAN_DAY = -492148;
    private static final int[] LEAP_MONTH_LENGTH = new int[]{30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 30};
    private static final int[] LEAP_NUM_DAYS = new int[]{0, 30, 59, 89, 118, 148, 177, 207, 236, 266, 295, 325};
    private static final int[] LEAST_MAX_VALUES = new int[]{1, MAX_VALUE_OF_ERA, 11, 51, 5, 29, 354};
    private static final int MAX_ADJUSTED_CYCLE = 334;
    private static final int[] MAX_VALUES = new int[]{1, MAX_VALUE_OF_ERA, 11, 52, 6, 30, 355};
    public static final int MAX_VALUE_OF_ERA = 9999;
    private static final int[] MIN_VALUES = new int[]{0, 1, 0, 1, 0, 1, 1};
    public static final int MIN_VALUE_OF_ERA = 1;
    private static final int[] MONTH_LENGTH = new int[]{30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29};
    private static final int[] NUM_DAYS = new int[]{0, 30, 59, 89, 118, 148, 177, 207, 236, 266, 295, 325};
    private static final String PATH_SEP = File.pathSeparator;
    private static final int POSITION_DAY_OF_MONTH = 5;
    private static final int POSITION_DAY_OF_YEAR = 6;
    private static final long serialVersionUID = -5207853542612002020L;
    private final transient int dayOfMonth;
    private final transient DayOfWeek dayOfWeek;
    private final transient int dayOfYear;
    private final transient HijrahEra era;
    private final long gregorianEpochDay;
    private final transient boolean isLeapYear = isLeapYear((long) this.yearOfEra);
    private final transient int monthOfYear;
    private final transient int yearOfEra;

    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public /* bridge */ /* synthetic */ ChronoPeriod until(ChronoLocalDate chronoLocalDate) {
        return super.until(chronoLocalDate);
    }

    static {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 12;
        r1 = new int[r0];
        r1 = {0, 30, 59, 89, 118, 148, 177, 207, 236, 266, 295, 325};
        NUM_DAYS = r1;
        r1 = new int[r0];
        r1 = {0, 30, 59, 89, 118, 148, 177, 207, 236, 266, 295, 325};
        LEAP_NUM_DAYS = r1;
        r1 = new int[r0];
        r1 = {30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29};
        MONTH_LENGTH = r1;
        r0 = new int[r0];
        r0 = {30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 30};
        LEAP_MONTH_LENGTH = r0;
        r0 = 7;
        r1 = new int[r0];
        r1 = {0, 1, 0, 1, 0, 1, 1};
        MIN_VALUES = r1;
        r1 = new int[r0];
        r1 = {1, 9999, 11, 51, 5, 29, 354};
        LEAST_MAX_VALUES = r1;
        r0 = new int[r0];
        r0 = {1, 9999, 11, 52, 6, 30, 355};
        MAX_VALUES = r0;
        r0 = 30;
        r0 = new int[r0];
        r0 = {0, 354, 709, 1063, 1417, 1772, 2126, 2481, 2835, 3189, 3544, 3898, 4252, 4607, 4961, 5315, 5670, 6024, 6379, 6733, 7087, 7442, 7796, 8150, 8505, 8859, 9214, 9568, 9922, 10277};
        CYCLEYEAR_START_DATE = r0;
        r0 = java.io.File.separatorChar;
        FILE_SEP = r0;
        r0 = java.io.File.pathSeparator;
        PATH_SEP = r0;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "org";
        r0.append(r1);
        r1 = FILE_SEP;
        r0.append(r1);
        r1 = "threeten";
        r0.append(r1);
        r1 = FILE_SEP;
        r0.append(r1);
        r1 = "bp";
        r0.append(r1);
        r1 = FILE_SEP;
        r0.append(r1);
        r1 = "chrono";
        r0.append(r1);
        r0 = r0.toString();
        DEFAULT_CONFIG_PATH = r0;
        r0 = new java.util.HashMap;
        r0.<init>();
        ADJUSTED_MONTH_DAYS = r0;
        r0 = new java.util.HashMap;
        r0.<init>();
        ADJUSTED_MONTH_LENGTHS = r0;
        r0 = new java.util.HashMap;
        r0.<init>();
        ADJUSTED_CYCLE_YEARS = r0;
        r0 = NUM_DAYS;
        r0 = r0.length;
        r0 = new java.lang.Integer[r0];
        DEFAULT_MONTH_DAYS = r0;
        r0 = 0;
        r1 = 0;
    L_0x0091:
        r2 = NUM_DAYS;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x00a6;
    L_0x0096:
        r2 = DEFAULT_MONTH_DAYS;
        r3 = new java.lang.Integer;
        r4 = NUM_DAYS;
        r4 = r4[r1];
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x0091;
    L_0x00a6:
        r1 = LEAP_NUM_DAYS;
        r1 = r1.length;
        r1 = new java.lang.Integer[r1];
        DEFAULT_LEAP_MONTH_DAYS = r1;
        r1 = 0;
    L_0x00ae:
        r2 = LEAP_NUM_DAYS;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x00c3;
    L_0x00b3:
        r2 = DEFAULT_LEAP_MONTH_DAYS;
        r3 = new java.lang.Integer;
        r4 = LEAP_NUM_DAYS;
        r4 = r4[r1];
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x00ae;
    L_0x00c3:
        r1 = MONTH_LENGTH;
        r1 = r1.length;
        r1 = new java.lang.Integer[r1];
        DEFAULT_MONTH_LENGTHS = r1;
        r1 = 0;
    L_0x00cb:
        r2 = MONTH_LENGTH;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x00e0;
    L_0x00d0:
        r2 = DEFAULT_MONTH_LENGTHS;
        r3 = new java.lang.Integer;
        r4 = MONTH_LENGTH;
        r4 = r4[r1];
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x00cb;
    L_0x00e0:
        r1 = LEAP_MONTH_LENGTH;
        r1 = r1.length;
        r1 = new java.lang.Integer[r1];
        DEFAULT_LEAP_MONTH_LENGTHS = r1;
        r1 = 0;
    L_0x00e8:
        r2 = LEAP_MONTH_LENGTH;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x00fd;
    L_0x00ed:
        r2 = DEFAULT_LEAP_MONTH_LENGTHS;
        r3 = new java.lang.Integer;
        r4 = LEAP_MONTH_LENGTH;
        r4 = r4[r1];
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x00e8;
    L_0x00fd:
        r1 = CYCLEYEAR_START_DATE;
        r1 = r1.length;
        r1 = new java.lang.Integer[r1];
        DEFAULT_CYCLE_YEARS = r1;
        r1 = 0;
    L_0x0105:
        r2 = CYCLEYEAR_START_DATE;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x011a;
    L_0x010a:
        r2 = DEFAULT_CYCLE_YEARS;
        r3 = new java.lang.Integer;
        r4 = CYCLEYEAR_START_DATE;
        r4 = r4[r1];
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x0105;
    L_0x011a:
        r1 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        r1 = new java.lang.Long[r1];
        ADJUSTED_CYCLES = r1;
        r1 = 0;
    L_0x0121:
        r2 = ADJUSTED_CYCLES;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x0135;
    L_0x0126:
        r2 = ADJUSTED_CYCLES;
        r3 = new java.lang.Long;
        r4 = r1 * 10631;
        r4 = (long) r4;
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x0121;
    L_0x0135:
        r1 = MIN_VALUES;
        r1 = r1.length;
        r1 = new java.lang.Integer[r1];
        ADJUSTED_MIN_VALUES = r1;
        r1 = 0;
    L_0x013d:
        r2 = MIN_VALUES;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x0152;
    L_0x0142:
        r2 = ADJUSTED_MIN_VALUES;
        r3 = new java.lang.Integer;
        r4 = MIN_VALUES;
        r4 = r4[r1];
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x013d;
    L_0x0152:
        r1 = LEAST_MAX_VALUES;
        r1 = r1.length;
        r1 = new java.lang.Integer[r1];
        ADJUSTED_LEAST_MAX_VALUES = r1;
        r1 = 0;
    L_0x015a:
        r2 = LEAST_MAX_VALUES;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x016f;
    L_0x015f:
        r2 = ADJUSTED_LEAST_MAX_VALUES;
        r3 = new java.lang.Integer;
        r4 = LEAST_MAX_VALUES;
        r4 = r4[r1];
        r3.<init>(r4);
        r2[r1] = r3;
        r1 = r1 + 1;
        goto L_0x015a;
    L_0x016f:
        r1 = MAX_VALUES;
        r1 = r1.length;
        r1 = new java.lang.Integer[r1];
        ADJUSTED_MAX_VALUES = r1;
    L_0x0176:
        r1 = MAX_VALUES;
        r1 = r1.length;
        if (r0 >= r1) goto L_0x018b;
    L_0x017b:
        r1 = ADJUSTED_MAX_VALUES;
        r2 = new java.lang.Integer;
        r3 = MAX_VALUES;
        r3 = r3[r0];
        r2.<init>(r3);
        r1[r0] = r2;
        r0 = r0 + 1;
        goto L_0x0176;
    L_0x018b:
        readDeviationConfig();	 Catch:{ IOException -> 0x018e, IOException -> 0x018e }
    L_0x018e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.<clinit>():void");
    }

    public static HijrahDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static HijrahDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static HijrahDate now(Clock clock) {
        return HijrahChronology.INSTANCE.dateNow(clock);
    }

    public static HijrahDate of(int i, int i2, int i3) {
        return i >= 1 ? of(HijrahEra.AH, i, i2, i3) : of(HijrahEra.BEFORE_AH, 1 - i, i2, i3);
    }

    static HijrahDate of(HijrahEra hijrahEra, int i, int i2, int i3) {
        Jdk8Methods.requireNonNull(hijrahEra, "era");
        checkValidYearOfEra(i);
        checkValidMonth(i2);
        checkValidDayOfMonth(i3);
        return new HijrahDate(getGregorianEpochDay(hijrahEra.prolepticYear(i), i2, i3));
    }

    private static void checkValidYearOfEra(int i) {
        if (i >= 1) {
            if (i <= MAX_VALUE_OF_ERA) {
                return;
            }
        }
        throw new DateTimeException("Invalid year of Hijrah Era");
    }

    private static void checkValidDayOfYear(int i) {
        if (i >= 1) {
            if (i <= getMaximumDayOfYear()) {
                return;
            }
        }
        throw new DateTimeException("Invalid day of year of Hijrah date");
    }

    private static void checkValidMonth(int i) {
        if (i >= 1) {
            if (i <= 12) {
                return;
            }
        }
        throw new DateTimeException("Invalid month of Hijrah date");
    }

    private static void checkValidDayOfMonth(int i) {
        if (i >= 1) {
            if (i <= getMaximumDayOfMonth()) {
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid day of month of Hijrah date, day ");
        stringBuilder.append(i);
        stringBuilder.append(" greater than ");
        stringBuilder.append(getMaximumDayOfMonth());
        stringBuilder.append(" or less than 1");
        throw new DateTimeException(stringBuilder.toString());
    }

    static HijrahDate of(LocalDate localDate) {
        return new HijrahDate(localDate.toEpochDay());
    }

    static HijrahDate ofEpochDay(long j) {
        return new HijrahDate(j);
    }

    public static HijrahDate from(TemporalAccessor temporalAccessor) {
        return HijrahChronology.INSTANCE.date(temporalAccessor);
    }

    private HijrahDate(long j) {
        int[] hijrahDateInfo = getHijrahDateInfo(j);
        checkValidYearOfEra(hijrahDateInfo[1]);
        checkValidMonth(hijrahDateInfo[2]);
        checkValidDayOfMonth(hijrahDateInfo[3]);
        checkValidDayOfYear(hijrahDateInfo[4]);
        this.era = HijrahEra.of(hijrahDateInfo[0]);
        this.yearOfEra = hijrahDateInfo[1];
        this.monthOfYear = hijrahDateInfo[2];
        this.dayOfMonth = hijrahDateInfo[3];
        this.dayOfYear = hijrahDateInfo[4];
        this.dayOfWeek = DayOfWeek.of(hijrahDateInfo[5]);
        this.gregorianEpochDay = j;
    }

    private Object readResolve() {
        return new HijrahDate(this.gregorianEpochDay);
    }

    public HijrahChronology getChronology() {
        return HijrahChronology.INSTANCE;
    }

    public HijrahEra getEra() {
        return this.era;
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (isSupported(temporalField)) {
            ChronoField chronoField = (ChronoField) temporalField;
            switch (chronoField) {
                case DAY_OF_MONTH:
                    return ValueRange.of(1, (long) lengthOfMonth());
                case DAY_OF_YEAR:
                    return ValueRange.of(1, (long) lengthOfYear());
                case ALIGNED_WEEK_OF_MONTH:
                    return ValueRange.of(1, 5);
                case YEAR_OF_ERA:
                    return ValueRange.of(1, 1000);
                default:
                    return getChronology().range(chronoField);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField) temporalField) {
            case DAY_OF_MONTH:
                return (long) this.dayOfMonth;
            case DAY_OF_YEAR:
                return (long) this.dayOfYear;
            case ALIGNED_WEEK_OF_MONTH:
                return (long) (((this.dayOfMonth - 1) / 7) + 1);
            case YEAR_OF_ERA:
                return (long) this.yearOfEra;
            case DAY_OF_WEEK:
                return (long) this.dayOfWeek.getValue();
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                return (long) (((this.dayOfMonth - 1) % 7) + 1);
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                return (long) (((this.dayOfYear - 1) % 7) + 1);
            case EPOCH_DAY:
                return toEpochDay();
            case ALIGNED_WEEK_OF_YEAR:
                return (long) (((this.dayOfYear - 1) / 7) + 1);
            case MONTH_OF_YEAR:
                return (long) this.monthOfYear;
            case YEAR:
                return (long) this.yearOfEra;
            case ERA:
                return (long) this.era.getValue();
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    public HijrahDate with(TemporalAdjuster temporalAdjuster) {
        return (HijrahDate) super.with(temporalAdjuster);
    }

    public HijrahDate with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (HijrahDate) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        chronoField.checkValidValue(j);
        int i = (int) j;
        switch (chronoField) {
            case DAY_OF_MONTH:
                return resolvePreviousValid(this.yearOfEra, this.monthOfYear, i);
            case DAY_OF_YEAR:
                i--;
                return resolvePreviousValid(this.yearOfEra, (i / 30) + 1, (i % 30) + 1);
            case ALIGNED_WEEK_OF_MONTH:
                return plusDays((j - getLong(ChronoField.ALIGNED_WEEK_OF_MONTH)) * 7);
            case YEAR_OF_ERA:
                if (this.yearOfEra < 1) {
                    i = 1 - i;
                }
                return resolvePreviousValid(i, this.monthOfYear, this.dayOfMonth);
            case DAY_OF_WEEK:
                return plusDays(j - ((long) this.dayOfWeek.getValue()));
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                return plusDays(j - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
            case EPOCH_DAY:
                return new HijrahDate((long) i);
            case ALIGNED_WEEK_OF_YEAR:
                return plusDays((j - getLong(ChronoField.ALIGNED_WEEK_OF_YEAR)) * 7);
            case MONTH_OF_YEAR:
                return resolvePreviousValid(this.yearOfEra, i, this.dayOfMonth);
            case YEAR:
                return resolvePreviousValid(i, this.monthOfYear, this.dayOfMonth);
            case ERA:
                return resolvePreviousValid(1 - this.yearOfEra, this.monthOfYear, this.dayOfMonth);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported field: ");
                stringBuilder.append(temporalField);
                throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }
    }

    private static HijrahDate resolvePreviousValid(int i, int i2, int i3) {
        int monthDays = getMonthDays(i2 - 1, i);
        if (i3 > monthDays) {
            i3 = monthDays;
        }
        return of(i, i2, i3);
    }

    public HijrahDate plus(TemporalAmount temporalAmount) {
        return (HijrahDate) super.plus(temporalAmount);
    }

    public HijrahDate plus(long j, TemporalUnit temporalUnit) {
        return (HijrahDate) super.plus(j, temporalUnit);
    }

    public HijrahDate minus(TemporalAmount temporalAmount) {
        return (HijrahDate) super.minus(temporalAmount);
    }

    public HijrahDate minus(long j, TemporalUnit temporalUnit) {
        return (HijrahDate) super.minus(j, temporalUnit);
    }

    public final ChronoLocalDateTime<HijrahDate> atTime(LocalTime localTime) {
        return super.atTime(localTime);
    }

    public long toEpochDay() {
        return getGregorianEpochDay(this.yearOfEra, this.monthOfYear, this.dayOfMonth);
    }

    public boolean isLeapYear() {
        return this.isLeapYear;
    }

    HijrahDate plusYears(long j) {
        if (j == 0) {
            return this;
        }
        return of(this.era, Jdk8Methods.safeAdd(this.yearOfEra, (int) j), this.monthOfYear, this.dayOfMonth);
    }

    HijrahDate plusMonths(long j) {
        if (j == 0) {
            return this;
        }
        int i = (this.monthOfYear - 1) + ((int) j);
        int i2 = i / 12;
        i %= 12;
        while (i < 0) {
            i += 12;
            i2 = Jdk8Methods.safeSubtract(i2, 1);
        }
        return of(this.era, Jdk8Methods.safeAdd(this.yearOfEra, i2), i + 1, this.dayOfMonth);
    }

    HijrahDate plusDays(long j) {
        return new HijrahDate(this.gregorianEpochDay + j);
    }

    private static int[] getHijrahDateInfo(long j) {
        int cycleNumber;
        int dayOfCycle;
        int yearInCycle;
        int dayOfMonth;
        int value;
        long j2 = j - -492148;
        if (j2 >= 0) {
            cycleNumber = getCycleNumber(j2);
            dayOfCycle = getDayOfCycle(j2, cycleNumber);
            yearInCycle = getYearInCycle(cycleNumber, (long) dayOfCycle);
            dayOfCycle = getDayOfYear(cycleNumber, dayOfCycle, yearInCycle);
            cycleNumber = ((cycleNumber * 30) + yearInCycle) + 1;
            yearInCycle = getMonthOfYear(dayOfCycle, cycleNumber);
            dayOfMonth = getDayOfMonth(dayOfCycle, yearInCycle, cycleNumber) + 1;
            value = HijrahEra.AH.getValue();
        } else {
            cycleNumber = (int) j2;
            dayOfCycle = cycleNumber / 10631;
            cycleNumber %= 10631;
            if (cycleNumber == 0) {
                cycleNumber = -10631;
                dayOfCycle++;
            }
            yearInCycle = getYearInCycle(dayOfCycle, (long) cycleNumber);
            cycleNumber = getDayOfYear(dayOfCycle, cycleNumber, yearInCycle);
            dayOfCycle = 1 - ((dayOfCycle * 30) - yearInCycle);
            cycleNumber = isLeapYear((long) dayOfCycle) ? cycleNumber + 355 : cycleNumber + 354;
            yearInCycle = getMonthOfYear(cycleNumber, dayOfCycle);
            dayOfMonth = getDayOfMonth(cycleNumber, yearInCycle, dayOfCycle) + 1;
            value = HijrahEra.BEFORE_AH.getValue();
            int i = dayOfCycle;
            dayOfCycle = cycleNumber;
            cycleNumber = i;
        }
        int i2 = (int) ((j2 + 5) % 7);
        i2 += i2 <= 0 ? 7 : 0;
        return new int[]{value, cycleNumber, yearInCycle + 1, dayOfMonth, dayOfCycle + 1, i2};
    }

    private static long getGregorianEpochDay(int i, int i2, int i3) {
        return (yearToGregorianEpochDay(i) + ((long) getMonthDays(i2 - 1, i))) + ((long) i3);
    }

    private static long yearToGregorianEpochDay(int r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r6 = r6 + -1;
        r0 = r6 / 30;
        r6 = r6 % 30;
        r1 = getAdjustedCycle(r0);
        r2 = java.lang.Math.abs(r6);
        r1 = r1[r2];
        r1 = r1.intValue();
        if (r6 >= 0) goto L_0x0017;
    L_0x0016:
        r1 = -r1;
    L_0x0017:
        r6 = ADJUSTED_CYCLES;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001c }
        r6 = r6[r0];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001c }
        goto L_0x001d;
    L_0x001c:
        r6 = 0;
    L_0x001d:
        if (r6 != 0) goto L_0x0027;
    L_0x001f:
        r6 = new java.lang.Long;
        r0 = r0 * 10631;
        r2 = (long) r0;
        r6.<init>(r2);
    L_0x0027:
        r2 = r6.longValue();
        r0 = (long) r1;
        r4 = r2 + r0;
        r0 = -492148; // 0xfffffffffff87d8c float:NaN double:NaN;
        r2 = r4 + r0;
        r0 = 1;
        r4 = r2 - r0;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.yearToGregorianEpochDay(int):long");
    }

    private static int getCycleNumber(long r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = ADJUSTED_CYCLES;
        r1 = 0;
    L_0x0003:
        r2 = r0.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        if (r1 >= r2) goto L_0x0016;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
    L_0x0006:
        r2 = r0[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        r2 = r2.longValue();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        r4 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1));	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        if (r4 >= 0) goto L_0x0013;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
    L_0x0010:
        r1 = r1 + -1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        return r1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
    L_0x0013:
        r1 = r1 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        goto L_0x0003;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
    L_0x0016:
        r0 = (int) r5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        r0 = r0 / 10631;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x001a }
        goto L_0x001d;
    L_0x001a:
        r5 = (int) r5;
        r0 = r5 / 10631;
    L_0x001d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.getCycleNumber(long):int");
    }

    private static int getDayOfCycle(long r4, int r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = ADJUSTED_CYCLES;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0005 }
        r0 = r0[r6];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0005 }
        goto L_0x0006;
    L_0x0005:
        r0 = 0;
    L_0x0006:
        if (r0 != 0) goto L_0x0010;
    L_0x0008:
        r0 = new java.lang.Long;
        r6 = r6 * 10631;
        r1 = (long) r6;
        r0.<init>(r1);
    L_0x0010:
        r0 = r0.longValue();
        r2 = r4 - r0;
        r4 = (int) r2;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.getDayOfCycle(long, int):int");
    }

    private static int getYearInCycle(int i, long j) {
        i = getAdjustedCycle(i);
        int i2 = 0;
        if (j == 0) {
            return 0;
        }
        if (j > 0) {
            while (i2 < i.length) {
                if (j < ((long) i[i2].intValue())) {
                    return i2 - 1;
                }
                i2++;
            }
            return 29;
        }
        j = -j;
        while (i2 < i.length) {
            if (j <= ((long) i[i2].intValue())) {
                return i2 - 1;
            }
            i2++;
        }
        return 29;
    }

    private static java.lang.Integer[] getAdjustedCycle(int r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = ADJUSTED_CYCLE_YEARS;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r1 = new java.lang.Integer;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r1.<init>(r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r2 = r0.get(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r2 = (java.lang.Integer[]) r2;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        goto L_0x000f;
    L_0x000e:
        r2 = 0;
    L_0x000f:
        if (r2 != 0) goto L_0x0013;
    L_0x0011:
        r2 = DEFAULT_CYCLE_YEARS;
    L_0x0013:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.getAdjustedCycle(int):java.lang.Integer[]");
    }

    private static java.lang.Integer[] getAdjustedMonthDays(int r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = ADJUSTED_MONTH_DAYS;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r1 = new java.lang.Integer;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r1.<init>(r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r0 = r0.get(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r0 = (java.lang.Integer[]) r0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        goto L_0x000f;
    L_0x000e:
        r0 = 0;
    L_0x000f:
        if (r0 != 0) goto L_0x001d;
    L_0x0011:
        r0 = (long) r2;
        r2 = isLeapYear(r0);
        if (r2 == 0) goto L_0x001b;
    L_0x0018:
        r0 = DEFAULT_LEAP_MONTH_DAYS;
        goto L_0x001d;
    L_0x001b:
        r0 = DEFAULT_MONTH_DAYS;
    L_0x001d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.getAdjustedMonthDays(int):java.lang.Integer[]");
    }

    private static java.lang.Integer[] getAdjustedMonthLength(int r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = ADJUSTED_MONTH_LENGTHS;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r1 = new java.lang.Integer;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r1.<init>(r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r0 = r0.get(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        r0 = (java.lang.Integer[]) r0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x000e }
        goto L_0x000f;
    L_0x000e:
        r0 = 0;
    L_0x000f:
        if (r0 != 0) goto L_0x001d;
    L_0x0011:
        r0 = (long) r2;
        r2 = isLeapYear(r0);
        if (r2 == 0) goto L_0x001b;
    L_0x0018:
        r0 = DEFAULT_LEAP_MONTH_LENGTHS;
        goto L_0x001d;
    L_0x001b:
        r0 = DEFAULT_MONTH_LENGTHS;
    L_0x001d:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.getAdjustedMonthLength(int):java.lang.Integer[]");
    }

    private static int getDayOfYear(int i, int i2, int i3) {
        i = getAdjustedCycle(i);
        if (i2 > 0) {
            return i2 - i[i3].intValue();
        }
        return i[i3].intValue() + i2;
    }

    private static int getMonthOfYear(int i, int i2) {
        Integer[] adjustedMonthDays = getAdjustedMonthDays(i2);
        int i3 = 0;
        if (i >= 0) {
            while (i3 < adjustedMonthDays.length) {
                if (i < adjustedMonthDays[i3].intValue()) {
                    return i3 - 1;
                }
                i3++;
            }
            return 11;
        }
        i = isLeapYear((long) i2) != 0 ? i + 355 : i + 354;
        while (i3 < adjustedMonthDays.length) {
            if (i < adjustedMonthDays[i3].intValue()) {
                return i3 - 1;
            }
            i3++;
        }
        return 11;
    }

    private static int getDayOfMonth(int i, int i2, int i3) {
        Integer[] adjustedMonthDays = getAdjustedMonthDays(i3);
        if (i >= 0) {
            return i2 > 0 ? i - adjustedMonthDays[i2].intValue() : i;
        } else {
            i = isLeapYear((long) i3) != 0 ? i + 355 : i + 354;
            return i2 > 0 ? i - adjustedMonthDays[i2].intValue() : i;
        }
    }

    static boolean isLeapYear(long j) {
        if (j <= 0) {
            j = -j;
        }
        return ((j * 11) + 14) % 30 < 11 ? 1 : 0;
    }

    private static int getMonthDays(int i, int i2) {
        return getAdjustedMonthDays(i2)[i].intValue();
    }

    static int getMonthLength(int i, int i2) {
        return getAdjustedMonthLength(i2)[i].intValue();
    }

    public int lengthOfMonth() {
        return getMonthLength(this.monthOfYear - 1, this.yearOfEra);
    }

    static int getYearLength(int r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r4 + -1;
        r1 = r0 / 30;
        r2 = ADJUSTED_CYCLE_YEARS;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0011 }
        r3 = java.lang.Integer.valueOf(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0011 }
        r2 = r2.get(r3);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0011 }
        r2 = (java.lang.Integer[]) r2;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0011 }
        goto L_0x0012;
    L_0x0011:
        r2 = 0;
    L_0x0012:
        if (r2 == 0) goto L_0x0045;
    L_0x0014:
        r0 = r0 % 30;
        r4 = 29;
        if (r0 != r4) goto L_0x0035;
    L_0x001a:
        r4 = ADJUSTED_CYCLES;
        r3 = r1 + 1;
        r4 = r4[r3];
        r4 = r4.intValue();
        r3 = ADJUSTED_CYCLES;
        r1 = r3[r1];
        r1 = r1.intValue();
        r4 = r4 - r1;
        r0 = r2[r0];
        r0 = r0.intValue();
        r4 = r4 - r0;
        return r4;
    L_0x0035:
        r4 = r0 + 1;
        r4 = r2[r4];
        r4 = r4.intValue();
        r0 = r2[r0];
        r0 = r0.intValue();
        r4 = r4 - r0;
        return r4;
    L_0x0045:
        r0 = (long) r4;
        r4 = isLeapYear(r0);
        if (r4 == 0) goto L_0x004f;
    L_0x004c:
        r4 = 355; // 0x163 float:4.97E-43 double:1.754E-321;
        goto L_0x0051;
    L_0x004f:
        r4 = 354; // 0x162 float:4.96E-43 double:1.75E-321;
    L_0x0051:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.getYearLength(int):int");
    }

    public int lengthOfYear() {
        return getYearLength(this.yearOfEra);
    }

    static int getMaximumDayOfMonth() {
        return ADJUSTED_MAX_VALUES[5].intValue();
    }

    static int getSmallestMaximumDayOfMonth() {
        return ADJUSTED_LEAST_MAX_VALUES[5].intValue();
    }

    static int getMaximumDayOfYear() {
        return ADJUSTED_MAX_VALUES[6].intValue();
    }

    static int getSmallestMaximumDayOfYear() {
        return ADJUSTED_LEAST_MAX_VALUES[6].intValue();
    }

    private static void addDeviationAsHijrah(int i, int i2, int i3, int i4, int i5) {
        int i6 = i;
        int i7 = i2;
        int i8 = i3;
        int i9 = i4;
        int i10 = i5;
        if (i6 < 1) {
            throw new IllegalArgumentException("startYear < 1");
        } else if (i8 < 1) {
            throw new IllegalArgumentException("endYear < 1");
        } else {
            if (i7 >= 0) {
                if (i7 <= 11) {
                    if (i9 >= 0) {
                        if (i9 <= 11) {
                            if (i8 > MAX_VALUE_OF_ERA) {
                                throw new IllegalArgumentException("endYear > 9999");
                            } else if (i8 < i6) {
                                throw new IllegalArgumentException("startYear > endYear");
                            } else if (i8 != i6 || i9 >= i7) {
                                int i11;
                                int i12;
                                int i13;
                                int i14;
                                Integer[] numArr;
                                int i15;
                                boolean isLeapYear = isLeapYear((long) i6);
                                Integer[] numArr2 = (Integer[]) ADJUSTED_MONTH_DAYS.get(new Integer(i6));
                                if (numArr2 == null) {
                                    if (isLeapYear) {
                                        numArr2 = new Integer[LEAP_NUM_DAYS.length];
                                        for (i11 = 0; i11 < LEAP_NUM_DAYS.length; i11++) {
                                            numArr2[i11] = new Integer(LEAP_NUM_DAYS[i11]);
                                        }
                                    } else {
                                        numArr2 = new Integer[NUM_DAYS.length];
                                        for (i11 = 0; i11 < NUM_DAYS.length; i11++) {
                                            numArr2[i11] = new Integer(NUM_DAYS[i11]);
                                        }
                                    }
                                }
                                Object obj = new Integer[numArr2.length];
                                for (i12 = 0; i12 < 12; i12++) {
                                    if (i12 > i7) {
                                        obj[i12] = new Integer(numArr2[i12].intValue() - i10);
                                    } else {
                                        obj[i12] = new Integer(numArr2[i12].intValue());
                                    }
                                }
                                ADJUSTED_MONTH_DAYS.put(new Integer(i6), obj);
                                numArr2 = (Integer[]) ADJUSTED_MONTH_LENGTHS.get(new Integer(i6));
                                if (numArr2 == null) {
                                    if (isLeapYear) {
                                        numArr2 = new Integer[LEAP_MONTH_LENGTH.length];
                                        for (i13 = 0; i13 < LEAP_MONTH_LENGTH.length; i13++) {
                                            numArr2[i13] = new Integer(LEAP_MONTH_LENGTH[i13]);
                                        }
                                    } else {
                                        numArr2 = new Integer[MONTH_LENGTH.length];
                                        for (i13 = 0; i13 < MONTH_LENGTH.length; i13++) {
                                            numArr2[i13] = new Integer(MONTH_LENGTH[i13]);
                                        }
                                    }
                                }
                                Object obj2 = new Integer[numArr2.length];
                                for (i11 = 0; i11 < 12; i11++) {
                                    if (i11 == i7) {
                                        obj2[i11] = new Integer(numArr2[i11].intValue() - i10);
                                    } else {
                                        obj2[i11] = new Integer(numArr2[i11].intValue());
                                    }
                                }
                                ADJUSTED_MONTH_LENGTHS.put(new Integer(i6), obj2);
                                if (i6 != i8) {
                                    int i16;
                                    i13 = i6 - 1;
                                    int i17 = i13 / 30;
                                    i13 %= 30;
                                    Integer[] numArr3 = (Integer[]) ADJUSTED_CYCLE_YEARS.get(new Integer(i17));
                                    if (numArr3 == null) {
                                        numArr3 = new Integer[CYCLEYEAR_START_DATE.length];
                                        for (i12 = 0; i12 < numArr3.length; i12++) {
                                            numArr3[i12] = new Integer(CYCLEYEAR_START_DATE[i12]);
                                        }
                                    }
                                    for (i13++; i13 < CYCLEYEAR_START_DATE.length; i13++) {
                                        numArr3[i13] = new Integer(numArr3[i13].intValue() - i10);
                                    }
                                    ADJUSTED_CYCLE_YEARS.put(new Integer(i17), numArr3);
                                    i13 = i8 - 1;
                                    i11 = i13 / 30;
                                    if (i17 != i11) {
                                        i17++;
                                        while (i17 < ADJUSTED_CYCLES.length) {
                                            i16 = i11;
                                            ADJUSTED_CYCLES[i17] = new Long(ADJUSTED_CYCLES[i17].longValue() - ((long) i10));
                                            i17++;
                                            i11 = i16;
                                        }
                                        i16 = i11;
                                        i11 = i16 + 1;
                                        while (i11 < ADJUSTED_CYCLES.length) {
                                            ADJUSTED_CYCLES[i11] = new Long(ADJUSTED_CYCLES[i11].longValue() + ((long) i10));
                                            i11++;
                                            i6 = i;
                                            i7 = i2;
                                        }
                                    } else {
                                        i16 = i11;
                                    }
                                    i13 %= 30;
                                    i14 = i16;
                                    numArr = (Integer[]) ADJUSTED_CYCLE_YEARS.get(new Integer(i14));
                                    if (numArr == null) {
                                        numArr = new Integer[CYCLEYEAR_START_DATE.length];
                                        for (i7 = 0; i7 < numArr.length; i7++) {
                                            numArr[i7] = new Integer(CYCLEYEAR_START_DATE[i7]);
                                        }
                                    }
                                    for (i13++; i13 < CYCLEYEAR_START_DATE.length; i13++) {
                                        numArr[i13] = new Integer(numArr[i13].intValue() + i10);
                                    }
                                    ADJUSTED_CYCLE_YEARS.put(new Integer(i14), numArr);
                                }
                                boolean isLeapYear2 = isLeapYear((long) i8);
                                Integer[] numArr4 = (Integer[]) ADJUSTED_MONTH_DAYS.get(new Integer(i8));
                                if (numArr4 == null) {
                                    if (isLeapYear2) {
                                        numArr4 = new Integer[LEAP_NUM_DAYS.length];
                                        for (i14 = 0; i14 < LEAP_NUM_DAYS.length; i14++) {
                                            numArr4[i14] = new Integer(LEAP_NUM_DAYS[i14]);
                                        }
                                    } else {
                                        numArr4 = new Integer[NUM_DAYS.length];
                                        for (i14 = 0; i14 < NUM_DAYS.length; i14++) {
                                            numArr4[i14] = new Integer(NUM_DAYS[i14]);
                                        }
                                    }
                                }
                                Object obj3 = new Integer[numArr4.length];
                                for (i15 = 0; i15 < 12; i15++) {
                                    if (i15 > i9) {
                                        obj3[i15] = new Integer(numArr4[i15].intValue() + i10);
                                    } else {
                                        obj3[i15] = new Integer(numArr4[i15].intValue());
                                    }
                                }
                                ADJUSTED_MONTH_DAYS.put(new Integer(i8), obj3);
                                numArr4 = (Integer[]) ADJUSTED_MONTH_LENGTHS.get(new Integer(i8));
                                if (numArr4 == null) {
                                    if (isLeapYear2) {
                                        numArr4 = new Integer[LEAP_MONTH_LENGTH.length];
                                        for (i6 = 0; i6 < LEAP_MONTH_LENGTH.length; i6++) {
                                            numArr4[i6] = new Integer(LEAP_MONTH_LENGTH[i6]);
                                        }
                                    } else {
                                        numArr4 = new Integer[MONTH_LENGTH.length];
                                        for (i6 = 0; i6 < MONTH_LENGTH.length; i6++) {
                                            numArr4[i6] = new Integer(MONTH_LENGTH[i6]);
                                        }
                                    }
                                }
                                Object obj4 = new Integer[numArr4.length];
                                for (i14 = 0; i14 < 12; i14++) {
                                    if (i14 == i9) {
                                        obj4[i14] = new Integer(numArr4[i14].intValue() + i10);
                                    } else {
                                        obj4[i14] = new Integer(numArr4[i14].intValue());
                                    }
                                }
                                ADJUSTED_MONTH_LENGTHS.put(new Integer(i8), obj4);
                                i10 = i;
                                numArr = (Integer[]) ADJUSTED_MONTH_LENGTHS.get(new Integer(i10));
                                numArr4 = (Integer[]) ADJUSTED_MONTH_LENGTHS.get(new Integer(i8));
                                Integer[] numArr5 = (Integer[]) ADJUSTED_MONTH_DAYS.get(new Integer(i10));
                                Integer[] numArr6 = (Integer[]) ADJUSTED_MONTH_DAYS.get(new Integer(i8));
                                i14 = numArr[i2].intValue();
                                i9 = numArr4[i9].intValue();
                                i10 = numArr5[11].intValue() + numArr[11].intValue();
                                i6 = numArr6[11].intValue() + numArr4[11].intValue();
                                i7 = ADJUSTED_MAX_VALUES[5].intValue();
                                i15 = ADJUSTED_LEAST_MAX_VALUES[5].intValue();
                                if (i7 < i14) {
                                    i7 = i14;
                                }
                                if (i7 < i9) {
                                    i7 = i9;
                                }
                                ADJUSTED_MAX_VALUES[5] = new Integer(i7);
                                if (i15 <= i14) {
                                    i14 = i15;
                                }
                                if (i14 <= i9) {
                                    i9 = i14;
                                }
                                ADJUSTED_LEAST_MAX_VALUES[5] = new Integer(i9);
                                i7 = ADJUSTED_MAX_VALUES[6].intValue();
                                i9 = ADJUSTED_LEAST_MAX_VALUES[6].intValue();
                                if (i7 < i10) {
                                    i7 = i10;
                                }
                                if (i7 < i6) {
                                    i7 = i6;
                                }
                                ADJUSTED_MAX_VALUES[6] = new Integer(i7);
                                if (i9 > i10) {
                                    i9 = i10;
                                }
                                if (i9 <= i6) {
                                    i6 = i9;
                                }
                                ADJUSTED_LEAST_MAX_VALUES[6] = new Integer(i6);
                                return;
                            } else {
                                throw new IllegalArgumentException("startYear == endYear && endMonth < startMonth");
                            }
                        }
                    }
                    throw new IllegalArgumentException("endMonth < 0 || endMonth > 11");
                }
            }
            throw new IllegalArgumentException("startMonth < 0 || startMonth > 11");
        }
    }

    private static void readDeviationConfig() throws IOException, ParseException {
        Throwable th;
        InputStream configFileInputStream = getConfigFileInputStream();
        if (configFileInputStream != null) {
            BufferedReader bufferedReader = null;
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(configFileInputStream));
                int i = 0;
                while (true) {
                    try {
                        String readLine = bufferedReader2.readLine();
                        if (readLine == null) {
                            break;
                        }
                        i++;
                        parseLine(readLine.trim(), i);
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader = bufferedReader2;
                    }
                }
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        }
    }

    private static void parseLine(java.lang.String r9, int r10) throws java.text.ParseException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new java.util.StringTokenizer;
        r1 = ";";
        r0.<init>(r9, r1);
    L_0x0007:
        r9 = r0.hasMoreTokens();
        if (r9 == 0) goto L_0x0199;
    L_0x000d:
        r9 = r0.nextToken();
        r1 = 58;
        r1 = r9.indexOf(r1);
        r2 = -1;
        if (r1 == r2) goto L_0x017d;
    L_0x001a:
        r3 = r1 + 1;
        r4 = r9.length();
        r3 = r9.substring(r3, r4);
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x0161 }
        r4 = 45;
        r4 = r9.indexOf(r4);
        if (r4 == r2) goto L_0x0145;
    L_0x0030:
        r5 = 0;
        r6 = r9.substring(r5, r4);
        r4 = r4 + 1;
        r9 = r9.substring(r4, r1);
        r1 = 47;
        r4 = r6.indexOf(r1);
        r1 = r9.indexOf(r1);
        if (r4 == r2) goto L_0x0129;
    L_0x0047:
        r7 = r6.substring(r5, r4);
        r4 = r4 + 1;
        r8 = r6.length();
        r4 = r6.substring(r4, r8);
        r6 = java.lang.Integer.parseInt(r7);	 Catch:{ NumberFormatException -> 0x010d }
        r4 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x00f1 }
        if (r1 == r2) goto L_0x00d5;
    L_0x005f:
        r5 = r9.substring(r5, r1);
        r1 = r1 + 1;
        r7 = r9.length();
        r9 = r9.substring(r1, r7);
        r1 = java.lang.Integer.parseInt(r5);	 Catch:{ NumberFormatException -> 0x00b9 }
        r9 = java.lang.Integer.parseInt(r9);	 Catch:{ NumberFormatException -> 0x009d }
        if (r6 == r2) goto L_0x0081;
    L_0x0077:
        if (r4 == r2) goto L_0x0081;
    L_0x0079:
        if (r1 == r2) goto L_0x0081;
    L_0x007b:
        if (r9 == r2) goto L_0x0081;
    L_0x007d:
        addDeviationAsHijrah(r6, r4, r1, r9, r3);
        goto L_0x0007;
    L_0x0081:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Unknown error at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x009d:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "End month is not properly set at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x00b9:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "End year is not properly set at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x00d5:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "End year/month has incorrect format at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x00f1:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Start month is not properly set at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x010d:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Start year is not properly set at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x0129:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Start year/month has incorrect format at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x0145:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Start and end year/month has incorrect format at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x0161:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Offset is not properly set at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x017d:
        r9 = new java.text.ParseException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Offset has incorrect format at line ";
        r0.append(r1);
        r0.append(r10);
        r1 = ".";
        r0.append(r1);
        r0 = r0.toString();
        r9.<init>(r0, r10);
        throw r9;
    L_0x0199:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.parseLine(java.lang.String, int):void");
    }

    private static java.io.InputStream getConfigFileInputStream() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = "org.threeten.bp.i18n.HijrahDate.deviationConfigFile";
        r0 = java.lang.System.getProperty(r0);
        if (r0 != 0) goto L_0x000a;
    L_0x0008:
        r0 = "hijrah_deviation.cfg";
    L_0x000a:
        r1 = "org.threeten.bp.i18n.HijrahDate.deviationConfigDir";
        r1 = java.lang.System.getProperty(r1);
        r2 = 0;
        if (r1 == 0) goto L_0x0062;
    L_0x0013:
        r3 = r1.length();
        if (r3 != 0) goto L_0x0025;
    L_0x0019:
        r3 = "file.separator";
        r3 = java.lang.System.getProperty(r3);
        r3 = r1.endsWith(r3);
        if (r3 != 0) goto L_0x003a;
    L_0x0025:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r1);
        r1 = "file.separator";
        r1 = java.lang.System.getProperty(r1);
        r3.append(r1);
        r1 = r3.toString();
    L_0x003a:
        r3 = new java.io.File;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r1);
        r1 = FILE_SEP;
        r4.append(r1);
        r4.append(r0);
        r0 = r4.toString();
        r3.<init>(r0);
        r0 = r3.exists();
        if (r0 == 0) goto L_0x0061;
    L_0x0059:
        r0 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x005f }
        r0.<init>(r3);	 Catch:{ IOException -> 0x005f }
        return r0;
    L_0x005f:
        r0 = move-exception;
        throw r0;
    L_0x0061:
        return r2;
    L_0x0062:
        r1 = "java.class.path";
        r1 = java.lang.System.getProperty(r1);
        r3 = new java.util.StringTokenizer;
        r4 = PATH_SEP;
        r3.<init>(r1, r4);
    L_0x006f:
        r1 = r3.hasMoreTokens();
        if (r1 == 0) goto L_0x0118;
    L_0x0075:
        r1 = r3.nextToken();
        r4 = new java.io.File;
        r4.<init>(r1);
        r5 = r4.exists();
        if (r5 == 0) goto L_0x006f;
    L_0x0084:
        r5 = r4.isDirectory();
        if (r5 == 0) goto L_0x00d1;
    L_0x008a:
        r4 = new java.io.File;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r1);
        r6 = FILE_SEP;
        r5.append(r6);
        r6 = DEFAULT_CONFIG_PATH;
        r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5, r0);
        r4 = r4.exists();
        if (r4 == 0) goto L_0x006f;
    L_0x00ab:
        r2 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x00cf }
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00cf }
        r3.<init>();	 Catch:{ IOException -> 0x00cf }
        r3.append(r1);	 Catch:{ IOException -> 0x00cf }
        r1 = FILE_SEP;	 Catch:{ IOException -> 0x00cf }
        r3.append(r1);	 Catch:{ IOException -> 0x00cf }
        r1 = DEFAULT_CONFIG_PATH;	 Catch:{ IOException -> 0x00cf }
        r3.append(r1);	 Catch:{ IOException -> 0x00cf }
        r1 = FILE_SEP;	 Catch:{ IOException -> 0x00cf }
        r3.append(r1);	 Catch:{ IOException -> 0x00cf }
        r3.append(r0);	 Catch:{ IOException -> 0x00cf }
        r0 = r3.toString();	 Catch:{ IOException -> 0x00cf }
        r2.<init>(r0);	 Catch:{ IOException -> 0x00cf }
        return r2;
    L_0x00cf:
        r0 = move-exception;
        throw r0;
    L_0x00d1:
        r1 = new java.util.zip.ZipFile;	 Catch:{ IOException -> 0x00d7 }
        r1.<init>(r4);	 Catch:{ IOException -> 0x00d7 }
        goto L_0x00d8;
    L_0x00d7:
        r1 = r2;
    L_0x00d8:
        if (r1 == 0) goto L_0x006f;
    L_0x00da:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = DEFAULT_CONFIG_PATH;
        r4.append(r5);
        r5 = FILE_SEP;
        r4.append(r5);
        r4.append(r0);
        r4 = r4.toString();
        r5 = r1.getEntry(r4);
        if (r5 != 0) goto L_0x010f;
    L_0x00f6:
        r5 = FILE_SEP;
        r6 = 92;
        r7 = 47;
        if (r5 != r7) goto L_0x0103;
    L_0x00fe:
        r4 = r4.replace(r7, r6);
        goto L_0x010b;
    L_0x0103:
        r5 = FILE_SEP;
        if (r5 != r6) goto L_0x010b;
    L_0x0107:
        r4 = r4.replace(r6, r7);
    L_0x010b:
        r5 = r1.getEntry(r4);
    L_0x010f:
        if (r5 == 0) goto L_0x006f;
    L_0x0111:
        r0 = r1.getInputStream(r5);	 Catch:{ IOException -> 0x0116 }
        return r0;
    L_0x0116:
        r0 = move-exception;
        throw r0;
    L_0x0118:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.HijrahDate.getConfigFileInputStream():java.io.InputStream");
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static ChronoLocalDate readExternal(DataInput dataInput) throws IOException {
        return HijrahChronology.INSTANCE.date(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
