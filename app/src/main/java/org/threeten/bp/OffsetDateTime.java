package org.threeten.bp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Comparator;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.jdk8.DefaultInterfaceTemporal;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.ValueRange;

public final class OffsetDateTime extends DefaultInterfaceTemporal implements Temporal, TemporalAdjuster, Comparable<OffsetDateTime>, Serializable {
    public static final TemporalQuery<OffsetDateTime> FROM = new C15231();
    private static final Comparator<OffsetDateTime> INSTANT_COMPARATOR = new C15242();
    public static final OffsetDateTime MAX = LocalDateTime.MAX.atOffset(ZoneOffset.MIN);
    public static final OffsetDateTime MIN = LocalDateTime.MIN.atOffset(ZoneOffset.MAX);
    private static final long serialVersionUID = 2287754244819255394L;
    private final LocalDateTime dateTime;
    private final ZoneOffset offset;

    /* renamed from: org.threeten.bp.OffsetDateTime$1 */
    static class C15231 implements TemporalQuery<OffsetDateTime> {
        C15231() {
        }

        public OffsetDateTime queryFrom(TemporalAccessor temporalAccessor) {
            return OffsetDateTime.from(temporalAccessor);
        }
    }

    /* renamed from: org.threeten.bp.OffsetDateTime$2 */
    static class C15242 implements Comparator<OffsetDateTime> {
        C15242() {
        }

        public int compare(OffsetDateTime offsetDateTime, OffsetDateTime offsetDateTime2) {
            int compareLongs = Jdk8Methods.compareLongs(offsetDateTime.toEpochSecond(), offsetDateTime2.toEpochSecond());
            return compareLongs == 0 ? Jdk8Methods.compareLongs((long) offsetDateTime.getNano(), (long) offsetDateTime2.getNano()) : compareLongs;
        }
    }

    public static Comparator<OffsetDateTime> timeLineOrder() {
        return INSTANT_COMPARATOR;
    }

    public static OffsetDateTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static OffsetDateTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static OffsetDateTime now(Clock clock) {
        Jdk8Methods.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofInstant(instant, clock.getZone().getRules().getOffset(instant));
    }

    public static OffsetDateTime of(LocalDate localDate, LocalTime localTime, ZoneOffset zoneOffset) {
        return new OffsetDateTime(LocalDateTime.of(localDate, localTime), zoneOffset);
    }

    public static OffsetDateTime of(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        return new OffsetDateTime(localDateTime, zoneOffset);
    }

    public static OffsetDateTime of(int i, int i2, int i3, int i4, int i5, int i6, int i7, ZoneOffset zoneOffset) {
        return new OffsetDateTime(LocalDateTime.of(i, i2, i3, i4, i5, i6, i7), zoneOffset);
    }

    public static OffsetDateTime ofInstant(Instant instant, ZoneId zoneId) {
        Jdk8Methods.requireNonNull(instant, "instant");
        Jdk8Methods.requireNonNull(zoneId, "zone");
        zoneId = zoneId.getRules().getOffset(instant);
        return new OffsetDateTime(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), zoneId), zoneId);
    }

    public static org.threeten.bp.OffsetDateTime from(org.threeten.bp.temporal.TemporalAccessor r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = r3 instanceof org.threeten.bp.OffsetDateTime;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r3 = (org.threeten.bp.OffsetDateTime) r3;
        return r3;
    L_0x0007:
        r0 = org.threeten.bp.ZoneOffset.from(r3);	 Catch:{ DateTimeException -> 0x001d }
        r1 = org.threeten.bp.LocalDateTime.from(r3);	 Catch:{ DateTimeException -> 0x0014 }
        r1 = of(r1, r0);	 Catch:{ DateTimeException -> 0x0014 }
        return r1;
    L_0x0014:
        r1 = org.threeten.bp.Instant.from(r3);	 Catch:{ DateTimeException -> 0x001d }
        r0 = ofInstant(r1, r0);	 Catch:{ DateTimeException -> 0x001d }
        return r0;
    L_0x001d:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to obtain OffsetDateTime from TemporalAccessor: ";
        r1.append(r2);
        r1.append(r3);
        r2 = ", type ";
        r1.append(r2);
        r3 = r3.getClass();
        r3 = r3.getName();
        r1.append(r3);
        r3 = r1.toString();
        r0.<init>(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.OffsetDateTime.from(org.threeten.bp.temporal.TemporalAccessor):org.threeten.bp.OffsetDateTime");
    }

    public static OffsetDateTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static OffsetDateTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return (OffsetDateTime) dateTimeFormatter.parse(charSequence, FROM);
    }

    private OffsetDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        this.dateTime = (LocalDateTime) Jdk8Methods.requireNonNull(localDateTime, "dateTime");
        this.offset = (ZoneOffset) Jdk8Methods.requireNonNull(zoneOffset, "offset");
    }

    private OffsetDateTime with(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        if (this.dateTime == localDateTime && this.offset.equals(zoneOffset)) {
            return this;
        }
        return new OffsetDateTime(localDateTime, zoneOffset);
    }

    public boolean isSupported(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            if (temporalField == null || temporalField.isSupportedBy(this) == null) {
                return null;
            }
        }
        return true;
    }

    public boolean isSupported(TemporalUnit temporalUnit) {
        boolean z = true;
        if (temporalUnit instanceof ChronoUnit) {
            if (!temporalUnit.isDateBased()) {
                if (temporalUnit.isTimeBased() == null) {
                    z = false;
                }
            }
            return z;
        }
        if (temporalUnit == null || temporalUnit.isSupportedBy(this) == null) {
            z = false;
        }
        return z;
    }

    public ValueRange range(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        if (temporalField != ChronoField.INSTANT_SECONDS) {
            if (temporalField != ChronoField.OFFSET_SECONDS) {
                return this.dateTime.range(temporalField);
            }
        }
        return temporalField.range();
    }

    public int get(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return super.get(temporalField);
        }
        switch ((ChronoField) temporalField) {
            case INSTANT_SECONDS:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Field too large for an int: ");
                stringBuilder.append(temporalField);
                throw new DateTimeException(stringBuilder.toString());
            case OFFSET_SECONDS:
                return getOffset().getTotalSeconds();
            default:
                return this.dateTime.get(temporalField);
        }
    }

    public long getLong(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        switch ((ChronoField) temporalField) {
            case INSTANT_SECONDS:
                return toEpochSecond();
            case OFFSET_SECONDS:
                return (long) getOffset().getTotalSeconds();
            default:
                return this.dateTime.getLong(temporalField);
        }
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public OffsetDateTime withOffsetSameLocal(ZoneOffset zoneOffset) {
        return with(this.dateTime, zoneOffset);
    }

    public OffsetDateTime withOffsetSameInstant(ZoneOffset zoneOffset) {
        if (zoneOffset.equals(this.offset)) {
            return this;
        }
        return new OffsetDateTime(this.dateTime.plusSeconds((long) (zoneOffset.getTotalSeconds() - this.offset.getTotalSeconds())), zoneOffset);
    }

    public int getYear() {
        return this.dateTime.getYear();
    }

    public int getMonthValue() {
        return this.dateTime.getMonthValue();
    }

    public Month getMonth() {
        return this.dateTime.getMonth();
    }

    public int getDayOfMonth() {
        return this.dateTime.getDayOfMonth();
    }

    public int getDayOfYear() {
        return this.dateTime.getDayOfYear();
    }

    public DayOfWeek getDayOfWeek() {
        return this.dateTime.getDayOfWeek();
    }

    public int getHour() {
        return this.dateTime.getHour();
    }

    public int getMinute() {
        return this.dateTime.getMinute();
    }

    public int getSecond() {
        return this.dateTime.getSecond();
    }

    public int getNano() {
        return this.dateTime.getNano();
    }

    public OffsetDateTime with(TemporalAdjuster temporalAdjuster) {
        if (!((temporalAdjuster instanceof LocalDate) || (temporalAdjuster instanceof LocalTime))) {
            if (!(temporalAdjuster instanceof LocalDateTime)) {
                if (temporalAdjuster instanceof Instant) {
                    return ofInstant((Instant) temporalAdjuster, this.offset);
                }
                if (temporalAdjuster instanceof ZoneOffset) {
                    return with(this.dateTime, (ZoneOffset) temporalAdjuster);
                }
                if (temporalAdjuster instanceof OffsetDateTime) {
                    return (OffsetDateTime) temporalAdjuster;
                }
                return (OffsetDateTime) temporalAdjuster.adjustInto(this);
            }
        }
        return with(this.dateTime.with(temporalAdjuster), this.offset);
    }

    public OffsetDateTime with(TemporalField temporalField, long j) {
        if (!(temporalField instanceof ChronoField)) {
            return (OffsetDateTime) temporalField.adjustInto(this, j);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        switch (chronoField) {
            case INSTANT_SECONDS:
                return ofInstant(Instant.ofEpochSecond(j, (long) getNano()), this.offset);
            case OFFSET_SECONDS:
                return with(this.dateTime, ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(j)));
            default:
                return with(this.dateTime.with(temporalField, j), this.offset);
        }
    }

    public OffsetDateTime withYear(int i) {
        return with(this.dateTime.withYear(i), this.offset);
    }

    public OffsetDateTime withMonth(int i) {
        return with(this.dateTime.withMonth(i), this.offset);
    }

    public OffsetDateTime withDayOfMonth(int i) {
        return with(this.dateTime.withDayOfMonth(i), this.offset);
    }

    public OffsetDateTime withDayOfYear(int i) {
        return with(this.dateTime.withDayOfYear(i), this.offset);
    }

    public OffsetDateTime withHour(int i) {
        return with(this.dateTime.withHour(i), this.offset);
    }

    public OffsetDateTime withMinute(int i) {
        return with(this.dateTime.withMinute(i), this.offset);
    }

    public OffsetDateTime withSecond(int i) {
        return with(this.dateTime.withSecond(i), this.offset);
    }

    public OffsetDateTime withNano(int i) {
        return with(this.dateTime.withNano(i), this.offset);
    }

    public OffsetDateTime truncatedTo(TemporalUnit temporalUnit) {
        return with(this.dateTime.truncatedTo(temporalUnit), this.offset);
    }

    public OffsetDateTime plus(TemporalAmount temporalAmount) {
        return (OffsetDateTime) temporalAmount.addTo(this);
    }

    public OffsetDateTime plus(long j, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return with(this.dateTime.plus(j, temporalUnit), this.offset);
        }
        return (OffsetDateTime) temporalUnit.addTo(this, j);
    }

    public OffsetDateTime plusYears(long j) {
        return with(this.dateTime.plusYears(j), this.offset);
    }

    public OffsetDateTime plusMonths(long j) {
        return with(this.dateTime.plusMonths(j), this.offset);
    }

    public OffsetDateTime plusWeeks(long j) {
        return with(this.dateTime.plusWeeks(j), this.offset);
    }

    public OffsetDateTime plusDays(long j) {
        return with(this.dateTime.plusDays(j), this.offset);
    }

    public OffsetDateTime plusHours(long j) {
        return with(this.dateTime.plusHours(j), this.offset);
    }

    public OffsetDateTime plusMinutes(long j) {
        return with(this.dateTime.plusMinutes(j), this.offset);
    }

    public OffsetDateTime plusSeconds(long j) {
        return with(this.dateTime.plusSeconds(j), this.offset);
    }

    public OffsetDateTime plusNanos(long j) {
        return with(this.dateTime.plusNanos(j), this.offset);
    }

    public OffsetDateTime minus(TemporalAmount temporalAmount) {
        return (OffsetDateTime) temporalAmount.subtractFrom(this);
    }

    public OffsetDateTime minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public OffsetDateTime minusYears(long j) {
        return j == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-j);
    }

    public OffsetDateTime minusMonths(long j) {
        return j == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-j);
    }

    public OffsetDateTime minusWeeks(long j) {
        return j == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-j);
    }

    public OffsetDateTime minusDays(long j) {
        return j == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-j);
    }

    public OffsetDateTime minusHours(long j) {
        return j == Long.MIN_VALUE ? plusHours(Long.MAX_VALUE).plusHours(1) : plusHours(-j);
    }

    public OffsetDateTime minusMinutes(long j) {
        return j == Long.MIN_VALUE ? plusMinutes(Long.MAX_VALUE).plusMinutes(1) : plusMinutes(-j);
    }

    public OffsetDateTime minusSeconds(long j) {
        return j == Long.MIN_VALUE ? plusSeconds(Long.MAX_VALUE).plusSeconds(1) : plusSeconds(-j);
    }

    public OffsetDateTime minusNanos(long j) {
        return j == Long.MIN_VALUE ? plusNanos(Long.MAX_VALUE).plusNanos(1) : plusNanos(-j);
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return ChronoUnit.NANOS;
        }
        if (temporalQuery != TemporalQueries.offset()) {
            if (temporalQuery != TemporalQueries.zone()) {
                if (temporalQuery == TemporalQueries.localDate()) {
                    return toLocalDate();
                }
                if (temporalQuery == TemporalQueries.localTime()) {
                    return toLocalTime();
                }
                if (temporalQuery == TemporalQueries.zoneId()) {
                    return null;
                }
                return super.query(temporalQuery);
            }
        }
        return getOffset();
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, toLocalDate().toEpochDay()).with(ChronoField.NANO_OF_DAY, toLocalTime().toNanoOfDay()).with(ChronoField.OFFSET_SECONDS, (long) getOffset().getTotalSeconds());
    }

    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        temporal = from(temporal);
        if (!(temporalUnit instanceof ChronoUnit)) {
            return temporalUnit.between(this, temporal);
        }
        return this.dateTime.until(temporal.withOffsetSameInstant(this.offset).dateTime, temporalUnit);
    }

    public ZonedDateTime atZoneSameInstant(ZoneId zoneId) {
        return ZonedDateTime.ofInstant(this.dateTime, this.offset, zoneId);
    }

    public ZonedDateTime atZoneSimilarLocal(ZoneId zoneId) {
        return ZonedDateTime.ofLocal(this.dateTime, zoneId, this.offset);
    }

    public LocalDateTime toLocalDateTime() {
        return this.dateTime;
    }

    public LocalDate toLocalDate() {
        return this.dateTime.toLocalDate();
    }

    public LocalTime toLocalTime() {
        return this.dateTime.toLocalTime();
    }

    public OffsetTime toOffsetTime() {
        return OffsetTime.of(this.dateTime.toLocalTime(), this.offset);
    }

    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.of(this.dateTime, this.offset);
    }

    public Instant toInstant() {
        return this.dateTime.toInstant(this.offset);
    }

    public long toEpochSecond() {
        return this.dateTime.toEpochSecond(this.offset);
    }

    public int compareTo(OffsetDateTime offsetDateTime) {
        if (getOffset().equals(offsetDateTime.getOffset())) {
            return toLocalDateTime().compareTo(offsetDateTime.toLocalDateTime());
        }
        int compareLongs = Jdk8Methods.compareLongs(toEpochSecond(), offsetDateTime.toEpochSecond());
        if (compareLongs == 0) {
            compareLongs = toLocalTime().getNano() - offsetDateTime.toLocalTime().getNano();
            if (compareLongs == 0) {
                compareLongs = toLocalDateTime().compareTo(offsetDateTime.toLocalDateTime());
            }
        }
        return compareLongs;
    }

    public boolean isAfter(OffsetDateTime offsetDateTime) {
        long toEpochSecond = toEpochSecond();
        long toEpochSecond2 = offsetDateTime.toEpochSecond();
        if (toEpochSecond <= toEpochSecond2) {
            if (toEpochSecond != toEpochSecond2 || toLocalTime().getNano() <= offsetDateTime.toLocalTime().getNano()) {
                return null;
            }
        }
        return true;
    }

    public boolean isBefore(OffsetDateTime offsetDateTime) {
        long toEpochSecond = toEpochSecond();
        long toEpochSecond2 = offsetDateTime.toEpochSecond();
        if (toEpochSecond >= toEpochSecond2) {
            if (toEpochSecond != toEpochSecond2 || toLocalTime().getNano() >= offsetDateTime.toLocalTime().getNano()) {
                return null;
            }
        }
        return true;
    }

    public boolean isEqual(OffsetDateTime offsetDateTime) {
        return (toEpochSecond() == offsetDateTime.toEpochSecond() && toLocalTime().getNano() == offsetDateTime.toLocalTime().getNano()) ? true : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OffsetDateTime)) {
            return false;
        }
        OffsetDateTime offsetDateTime = (OffsetDateTime) obj;
        if (!this.dateTime.equals(offsetDateTime.dateTime) || this.offset.equals(offsetDateTime.offset) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.dateTime.hashCode() ^ this.offset.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.dateTime.toString());
        stringBuilder.append(this.offset.toString());
        return stringBuilder.toString();
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 69, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        this.dateTime.writeExternal(dataOutput);
        this.offset.writeExternal(dataOutput);
    }

    static OffsetDateTime readExternal(DataInput dataInput) throws IOException {
        return of(LocalDateTime.readExternal(dataInput), ZoneOffset.readExternal(dataInput));
    }
}
