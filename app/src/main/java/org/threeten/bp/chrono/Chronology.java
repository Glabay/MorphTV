package org.threeten.bp.chrono;

import com.google.common.base.Ascii;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.Clock;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.ResolverStyle;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.jdk8.DefaultInterfaceTemporalAccessor;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;

public abstract class Chronology implements Comparable<Chronology> {
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_ID = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_TYPE = new ConcurrentHashMap();
    public static final TemporalQuery<Chronology> FROM = new C15441();
    private static final Method LOCALE_METHOD;

    /* renamed from: org.threeten.bp.chrono.Chronology$1 */
    static class C15441 implements TemporalQuery<Chronology> {
        C15441() {
        }

        public Chronology queryFrom(TemporalAccessor temporalAccessor) {
            return Chronology.from(temporalAccessor);
        }
    }

    /* renamed from: org.threeten.bp.chrono.Chronology$2 */
    class C15452 extends DefaultInterfaceTemporalAccessor {
        public boolean isSupported(TemporalField temporalField) {
            return false;
        }

        C15452() {
        }

        public long getLong(TemporalField temporalField) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unsupported field: ");
            stringBuilder.append(temporalField);
            throw new UnsupportedTemporalTypeException(stringBuilder.toString());
        }

        public <R> R query(TemporalQuery<R> temporalQuery) {
            if (temporalQuery == TemporalQueries.chronology()) {
                return Chronology.this;
            }
            return super.query(temporalQuery);
        }
    }

    public abstract ChronoLocalDate date(int i, int i2, int i3);

    public abstract ChronoLocalDate date(TemporalAccessor temporalAccessor);

    public abstract ChronoLocalDate dateEpochDay(long j);

    public abstract ChronoLocalDate dateYearDay(int i, int i2);

    public abstract Era eraOf(int i);

    public abstract List<Era> eras();

    public abstract String getCalendarType();

    public abstract String getId();

    public abstract boolean isLeapYear(long j);

    public abstract int prolepticYear(Era era, int i);

    public abstract ValueRange range(ChronoField chronoField);

    public abstract ChronoLocalDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle);

    static {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new org.threeten.bp.chrono.Chronology$1;
        r0.<init>();
        FROM = r0;
        r0 = new java.util.concurrent.ConcurrentHashMap;
        r0.<init>();
        CHRONOS_BY_ID = r0;
        r0 = new java.util.concurrent.ConcurrentHashMap;
        r0.<init>();
        CHRONOS_BY_TYPE = r0;
        r0 = java.util.Locale.class;	 Catch:{ Throwable -> 0x0026 }
        r1 = "getUnicodeLocaleType";	 Catch:{ Throwable -> 0x0026 }
        r2 = 1;	 Catch:{ Throwable -> 0x0026 }
        r2 = new java.lang.Class[r2];	 Catch:{ Throwable -> 0x0026 }
        r3 = 0;	 Catch:{ Throwable -> 0x0026 }
        r4 = java.lang.String.class;	 Catch:{ Throwable -> 0x0026 }
        r2[r3] = r4;	 Catch:{ Throwable -> 0x0026 }
        r0 = r0.getMethod(r1, r2);	 Catch:{ Throwable -> 0x0026 }
        goto L_0x0027;
    L_0x0026:
        r0 = 0;
    L_0x0027:
        LOCALE_METHOD = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.Chronology.<clinit>():void");
    }

    public static Chronology from(TemporalAccessor temporalAccessor) {
        Jdk8Methods.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        return chronology != null ? chronology : IsoChronology.INSTANCE;
    }

    public static org.threeten.bp.chrono.Chronology ofLocale(java.util.Locale r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        init();
        r0 = "locale";
        org.threeten.bp.jdk8.Jdk8Methods.requireNonNull(r5, r0);
        r0 = "iso";
        r1 = LOCALE_METHOD;
        if (r1 == 0) goto L_0x0020;
    L_0x000e:
        r1 = LOCALE_METHOD;	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r2 = 1;	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r2 = new java.lang.Object[r2];	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r3 = 0;	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r4 = "ca";	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r2[r3] = r4;	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r5 = r1.invoke(r5, r2);	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r5 = (java.lang.String) r5;	 Catch:{ IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a, IllegalArgumentException -> 0x002a }
        r0 = r5;
        goto L_0x002a;
    L_0x0020:
        r1 = org.threeten.bp.chrono.JapaneseChronology.LOCALE;
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x002a;
    L_0x0028:
        r0 = "japanese";
    L_0x002a:
        if (r0 == 0) goto L_0x005f;
    L_0x002c:
        r5 = "iso";
        r5 = r5.equals(r0);
        if (r5 != 0) goto L_0x005f;
    L_0x0034:
        r5 = "iso8601";
        r5 = r5.equals(r0);
        if (r5 == 0) goto L_0x003d;
    L_0x003c:
        goto L_0x005f;
    L_0x003d:
        r5 = CHRONOS_BY_TYPE;
        r5 = r5.get(r0);
        r5 = (org.threeten.bp.chrono.Chronology) r5;
        if (r5 != 0) goto L_0x005e;
    L_0x0047:
        r5 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unknown calendar system: ";
        r1.append(r2);
        r1.append(r0);
        r0 = r1.toString();
        r5.<init>(r0);
        throw r5;
    L_0x005e:
        return r5;
    L_0x005f:
        r5 = org.threeten.bp.chrono.IsoChronology.INSTANCE;
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.Chronology.ofLocale(java.util.Locale):org.threeten.bp.chrono.Chronology");
    }

    public static Chronology of(String str) {
        init();
        Chronology chronology = (Chronology) CHRONOS_BY_ID.get(str);
        if (chronology != null) {
            return chronology;
        }
        chronology = (Chronology) CHRONOS_BY_TYPE.get(str);
        if (chronology != null) {
            return chronology;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown chronology: ");
        stringBuilder.append(str);
        throw new DateTimeException(stringBuilder.toString());
    }

    public static Set<Chronology> getAvailableChronologies() {
        init();
        return new HashSet(CHRONOS_BY_ID.values());
    }

    private static void init() {
        if (CHRONOS_BY_ID.isEmpty()) {
            register(IsoChronology.INSTANCE);
            register(ThaiBuddhistChronology.INSTANCE);
            register(MinguoChronology.INSTANCE);
            register(JapaneseChronology.INSTANCE);
            register(HijrahChronology.INSTANCE);
            CHRONOS_BY_ID.putIfAbsent("Hijrah", HijrahChronology.INSTANCE);
            CHRONOS_BY_TYPE.putIfAbsent("islamic", HijrahChronology.INSTANCE);
            Iterator it = ServiceLoader.load(Chronology.class, Chronology.class.getClassLoader()).iterator();
            while (it.hasNext()) {
                Chronology chronology = (Chronology) it.next();
                CHRONOS_BY_ID.putIfAbsent(chronology.getId(), chronology);
                String calendarType = chronology.getCalendarType();
                if (calendarType != null) {
                    CHRONOS_BY_TYPE.putIfAbsent(calendarType, chronology);
                }
            }
        }
    }

    private static void register(Chronology chronology) {
        CHRONOS_BY_ID.putIfAbsent(chronology.getId(), chronology);
        String calendarType = chronology.getCalendarType();
        if (calendarType != null) {
            CHRONOS_BY_TYPE.putIfAbsent(calendarType, chronology);
        }
    }

    protected Chronology() {
    }

    <D extends ChronoLocalDate> D ensureChronoLocalDate(Temporal temporal) {
        ChronoLocalDate chronoLocalDate = (ChronoLocalDate) temporal;
        if (equals(chronoLocalDate.getChronology())) {
            return chronoLocalDate;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Chrono mismatch, expected: ");
        stringBuilder.append(getId());
        stringBuilder.append(", actual: ");
        stringBuilder.append(chronoLocalDate.getChronology().getId());
        throw new ClassCastException(stringBuilder.toString());
    }

    <D extends ChronoLocalDate> ChronoLocalDateTimeImpl<D> ensureChronoLocalDateTime(Temporal temporal) {
        ChronoLocalDateTimeImpl chronoLocalDateTimeImpl = (ChronoLocalDateTimeImpl) temporal;
        if (equals(chronoLocalDateTimeImpl.toLocalDate().getChronology())) {
            return chronoLocalDateTimeImpl;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Chrono mismatch, required: ");
        stringBuilder.append(getId());
        stringBuilder.append(", supplied: ");
        stringBuilder.append(chronoLocalDateTimeImpl.toLocalDate().getChronology().getId());
        throw new ClassCastException(stringBuilder.toString());
    }

    <D extends ChronoLocalDate> ChronoZonedDateTimeImpl<D> ensureChronoZonedDateTime(Temporal temporal) {
        ChronoZonedDateTimeImpl chronoZonedDateTimeImpl = (ChronoZonedDateTimeImpl) temporal;
        if (equals(chronoZonedDateTimeImpl.toLocalDate().getChronology())) {
            return chronoZonedDateTimeImpl;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Chrono mismatch, required: ");
        stringBuilder.append(getId());
        stringBuilder.append(", supplied: ");
        stringBuilder.append(chronoZonedDateTimeImpl.toLocalDate().getChronology().getId());
        throw new ClassCastException(stringBuilder.toString());
    }

    public ChronoLocalDate date(Era era, int i, int i2, int i3) {
        return date(prolepticYear(era, i), i2, i3);
    }

    public ChronoLocalDate dateYearDay(Era era, int i, int i2) {
        return dateYearDay(prolepticYear(era, i), i2);
    }

    public ChronoLocalDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    public ChronoLocalDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    public ChronoLocalDate dateNow(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        return date(LocalDate.now(clock));
    }

    public ChronoLocalDateTime<?> localDateTime(TemporalAccessor temporalAccessor) {
        try {
            return date(temporalAccessor).atTime(LocalTime.from(temporalAccessor));
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to obtain ChronoLocalDateTime from TemporalAccessor: ");
            stringBuilder.append(temporalAccessor.getClass());
            throw new DateTimeException(stringBuilder.toString(), e);
        }
    }

    public org.threeten.bp.chrono.ChronoZonedDateTime<?> zonedDateTime(org.threeten.bp.temporal.TemporalAccessor r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = org.threeten.bp.ZoneId.from(r5);	 Catch:{ DateTimeException -> 0x001b }
        r1 = org.threeten.bp.Instant.from(r5);	 Catch:{ DateTimeException -> 0x000d }
        r1 = r4.zonedDateTime(r1, r0);	 Catch:{ DateTimeException -> 0x000d }
        return r1;
    L_0x000d:
        r1 = r4.localDateTime(r5);	 Catch:{ DateTimeException -> 0x001b }
        r1 = r4.ensureChronoLocalDateTime(r1);	 Catch:{ DateTimeException -> 0x001b }
        r2 = 0;	 Catch:{ DateTimeException -> 0x001b }
        r0 = org.threeten.bp.chrono.ChronoZonedDateTimeImpl.ofBest(r1, r0, r2);	 Catch:{ DateTimeException -> 0x001b }
        return r0;
    L_0x001b:
        r0 = move-exception;
        r1 = new org.threeten.bp.DateTimeException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unable to obtain ChronoZonedDateTime from TemporalAccessor: ";
        r2.append(r3);
        r5 = r5.getClass();
        r2.append(r5);
        r5 = r2.toString();
        r1.<init>(r5, r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.chrono.Chronology.zonedDateTime(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.chrono.ChronoZonedDateTime<?>");
    }

    public ChronoZonedDateTime<?> zonedDateTime(Instant instant, ZoneId zoneId) {
        return ChronoZonedDateTimeImpl.ofInstant(this, instant, zoneId);
    }

    public ChronoPeriod period(int i, int i2, int i3) {
        return new ChronoPeriodImpl(this, i, i2, i3);
    }

    public String getDisplayName(TextStyle textStyle, Locale locale) {
        return new DateTimeFormatterBuilder().appendChronologyText(textStyle).toFormatter(locale).format(new C15452());
    }

    void updateResolveMap(Map<TemporalField, Long> map, ChronoField chronoField, long j) {
        Long l = (Long) map.get(chronoField);
        if (l == null || l.longValue() == j) {
            map.put(chronoField, Long.valueOf(j));
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid state, field: ");
        stringBuilder.append(chronoField);
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(l);
        stringBuilder.append(" conflicts with ");
        stringBuilder.append(chronoField);
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(j);
        throw new DateTimeException(stringBuilder.toString());
    }

    public int compareTo(Chronology chronology) {
        return getId().compareTo(chronology.getId());
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Chronology)) {
            return false;
        }
        if (compareTo((Chronology) obj) != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return getClass().hashCode() ^ getId().hashCode();
    }

    public String toString() {
        return getId();
    }

    private Object writeReplace() {
        return new Ser(Ascii.VT, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(getId());
    }

    static Chronology readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readUTF());
    }
}
