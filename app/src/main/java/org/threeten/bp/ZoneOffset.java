package org.threeten.bp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;
import org.threeten.bp.temporal.ValueRange;
import org.threeten.bp.zone.ZoneRules;

public final class ZoneOffset extends ZoneId implements TemporalAccessor, TemporalAdjuster, Comparable<ZoneOffset>, Serializable {
    public static final TemporalQuery<ZoneOffset> FROM = new C15341();
    private static final ConcurrentMap<String, ZoneOffset> ID_CACHE = new ConcurrentHashMap(16, 0.75f, 4);
    public static final ZoneOffset MAX = ofTotalSeconds(MAX_SECONDS);
    private static final int MAX_SECONDS = 64800;
    public static final ZoneOffset MIN = ofTotalSeconds(-64800);
    private static final int MINUTES_PER_HOUR = 60;
    private static final ConcurrentMap<Integer, ZoneOffset> SECONDS_CACHE = new ConcurrentHashMap(16, 0.75f, 4);
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    public static final ZoneOffset UTC = ofTotalSeconds(0);
    private static final long serialVersionUID = 2357656521762053153L;
    private final transient String id;
    private final int totalSeconds;

    /* renamed from: org.threeten.bp.ZoneOffset$1 */
    static class C15341 implements TemporalQuery<ZoneOffset> {
        C15341() {
        }

        public ZoneOffset queryFrom(TemporalAccessor temporalAccessor) {
            return ZoneOffset.from(temporalAccessor);
        }
    }

    private static int totalSeconds(int i, int i2, int i3) {
        return ((i * SECONDS_PER_HOUR) + (i2 * 60)) + i3;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.threeten.bp.ZoneOffset of(java.lang.String r6) {
        /*
        r0 = "offsetId";
        org.threeten.bp.jdk8.Jdk8Methods.requireNonNull(r6, r0);
        r0 = ID_CACHE;
        r0 = r0.get(r6);
        r0 = (org.threeten.bp.ZoneOffset) r0;
        if (r0 == 0) goto L_0x0010;
    L_0x000f:
        return r0;
    L_0x0010:
        r0 = r6.length();
        r1 = 3;
        r2 = 4;
        r3 = 1;
        r4 = 0;
        switch(r0) {
            case 2: goto L_0x0060;
            case 3: goto L_0x007c;
            case 4: goto L_0x001b;
            case 5: goto L_0x0057;
            case 6: goto L_0x004e;
            case 7: goto L_0x0040;
            case 8: goto L_0x001b;
            case 9: goto L_0x0032;
            default: goto L_0x001b;
        };
    L_0x001b:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid ID for ZoneOffset, invalid format: ";
        r1.append(r2);
        r1.append(r6);
        r6 = r1.toString();
        r0.<init>(r6);
        throw r0;
    L_0x0032:
        r0 = parseNumber(r6, r3, r4);
        r1 = parseNumber(r6, r2, r3);
        r2 = 7;
        r2 = parseNumber(r6, r2, r3);
        goto L_0x0082;
    L_0x0040:
        r0 = parseNumber(r6, r3, r4);
        r1 = parseNumber(r6, r1, r4);
        r2 = 5;
        r2 = parseNumber(r6, r2, r4);
        goto L_0x0082;
    L_0x004e:
        r0 = parseNumber(r6, r3, r4);
        r1 = parseNumber(r6, r2, r3);
        goto L_0x0081;
    L_0x0057:
        r0 = parseNumber(r6, r3, r4);
        r1 = parseNumber(r6, r1, r4);
        goto L_0x0081;
    L_0x0060:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = r6.charAt(r4);
        r0.append(r1);
        r1 = "0";
        r0.append(r1);
        r6 = r6.charAt(r3);
        r0.append(r6);
        r6 = r0.toString();
    L_0x007c:
        r0 = parseNumber(r6, r3, r4);
        r1 = 0;
    L_0x0081:
        r2 = 0;
    L_0x0082:
        r3 = r6.charAt(r4);
        r4 = 43;
        r5 = 45;
        if (r3 == r4) goto L_0x00a5;
    L_0x008c:
        if (r3 == r5) goto L_0x00a5;
    L_0x008e:
        r0 = new org.threeten.bp.DateTimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid ID for ZoneOffset, plus/minus not found when expected: ";
        r1.append(r2);
        r1.append(r6);
        r6 = r1.toString();
        r0.<init>(r6);
        throw r0;
    L_0x00a5:
        if (r3 != r5) goto L_0x00af;
    L_0x00a7:
        r6 = -r0;
        r0 = -r1;
        r1 = -r2;
        r6 = ofHoursMinutesSeconds(r6, r0, r1);
        return r6;
    L_0x00af:
        r6 = ofHoursMinutesSeconds(r0, r1, r2);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.ZoneOffset.of(java.lang.String):org.threeten.bp.ZoneOffset");
    }

    private static int parseNumber(CharSequence charSequence, int i, boolean z) {
        if (!z || charSequence.charAt(i - 1)) {
            z = charSequence.charAt(i);
            i = charSequence.charAt(i + 1);
            if (z >= true && z <= true && i >= 48) {
                if (i <= 57) {
                    return ((z - true) * 10) + (i - 48);
                }
            }
            z = new StringBuilder();
            z.append("Invalid ID for ZoneOffset, non numeric characters found: ");
            z.append(charSequence);
            throw new DateTimeException(z.toString());
        }
        z = new StringBuilder();
        z.append("Invalid ID for ZoneOffset, colon not found when expected: ");
        z.append(charSequence);
        throw new DateTimeException(z.toString());
    }

    public static ZoneOffset ofHours(int i) {
        return ofHoursMinutesSeconds(i, 0, 0);
    }

    public static ZoneOffset ofHoursMinutes(int i, int i2) {
        return ofHoursMinutesSeconds(i, i2, 0);
    }

    public static ZoneOffset ofHoursMinutesSeconds(int i, int i2, int i3) {
        validate(i, i2, i3);
        return ofTotalSeconds(totalSeconds(i, i2, i3));
    }

    public static ZoneOffset from(TemporalAccessor temporalAccessor) {
        ZoneOffset zoneOffset = (ZoneOffset) temporalAccessor.query(TemporalQueries.offset());
        if (zoneOffset != null) {
            return zoneOffset;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to obtain ZoneOffset from TemporalAccessor: ");
        stringBuilder.append(temporalAccessor);
        stringBuilder.append(", type ");
        stringBuilder.append(temporalAccessor.getClass().getName());
        throw new DateTimeException(stringBuilder.toString());
    }

    private static void validate(int i, int i2, int i3) {
        if (i >= -18) {
            if (i <= 18) {
                if (i > 0) {
                    if (i2 < 0 || i3 < 0) {
                        throw new DateTimeException("Zone offset minutes and seconds must be positive because hours is positive");
                    }
                } else if (i < 0) {
                    if (i2 > 0 || i3 > 0) {
                        throw new DateTimeException("Zone offset minutes and seconds must be negative because hours is negative");
                    }
                } else if ((i2 > 0 && i3 < 0) || (i2 < 0 && i3 > 0)) {
                    throw new DateTimeException("Zone offset minutes and seconds must have the same sign");
                }
                if (Math.abs(i2) > 59) {
                    i3 = new StringBuilder();
                    i3.append("Zone offset minutes not in valid range: abs(value) ");
                    i3.append(Math.abs(i2));
                    i3.append(" is not in the range 0 to 59");
                    throw new DateTimeException(i3.toString());
                } else if (Math.abs(i3) > 59) {
                    i2 = new StringBuilder();
                    i2.append("Zone offset seconds not in valid range: abs(value) ");
                    i2.append(Math.abs(i3));
                    i2.append(" is not in the range 0 to 59");
                    throw new DateTimeException(i2.toString());
                } else if (Math.abs(i) != 18) {
                    return;
                } else {
                    if (Math.abs(i2) > 0 || Math.abs(i3) > 0) {
                        throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
                    }
                    return;
                }
            }
        }
        i3 = new StringBuilder();
        i3.append("Zone offset hours not in valid range: value ");
        i3.append(i);
        i3.append(" is not in the range -18 to 18");
        throw new DateTimeException(i3.toString());
    }

    public static ZoneOffset ofTotalSeconds(int i) {
        if (Math.abs(i) > MAX_SECONDS) {
            throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00");
        } else if (i % 900 != 0) {
            return new ZoneOffset(i);
        } else {
            Integer valueOf = Integer.valueOf(i);
            ZoneOffset zoneOffset = (ZoneOffset) SECONDS_CACHE.get(valueOf);
            if (zoneOffset == null) {
                SECONDS_CACHE.putIfAbsent(valueOf, new ZoneOffset(i));
                zoneOffset = (ZoneOffset) SECONDS_CACHE.get(valueOf);
                ID_CACHE.putIfAbsent(zoneOffset.getId(), zoneOffset);
            }
            return zoneOffset;
        }
    }

    private ZoneOffset(int i) {
        this.totalSeconds = i;
        this.id = buildId(i);
    }

    private static String buildId(int i) {
        if (i == 0) {
            return "Z";
        }
        int abs = Math.abs(i);
        StringBuilder stringBuilder = new StringBuilder();
        int i2 = abs / SECONDS_PER_HOUR;
        int i3 = (abs / 60) % 60;
        stringBuilder.append(i < 0 ? "-" : "+");
        stringBuilder.append(i2 < 10 ? "0" : "");
        stringBuilder.append(i2);
        stringBuilder.append(i3 < 10 ? ":0" : ":");
        stringBuilder.append(i3);
        abs %= 60;
        if (abs != 0) {
            stringBuilder.append(abs < 10 ? ":0" : ":");
            stringBuilder.append(abs);
        }
        return stringBuilder.toString();
    }

    public int getTotalSeconds() {
        return this.totalSeconds;
    }

    public String getId() {
        return this.id;
    }

    public ZoneRules getRules() {
        return ZoneRules.of(this);
    }

    public boolean isSupported(TemporalField temporalField) {
        boolean z = false;
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.OFFSET_SECONDS) {
                z = true;
            }
            return z;
        }
        if (!(temporalField == null || temporalField.isSupportedBy(this) == null)) {
            z = true;
        }
        return z;
    }

    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return temporalField.range();
        }
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.rangeRefinedBy(this);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public int get(TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return this.totalSeconds;
        }
        if (!(temporalField instanceof ChronoField)) {
            return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public long getLong(TemporalField temporalField) {
        if (temporalField == ChronoField.OFFSET_SECONDS) {
            return (long) this.totalSeconds;
        }
        if (!(temporalField instanceof ChronoField)) {
            return temporalField.getFrom(this);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported field: ");
        stringBuilder.append(temporalField);
        throw new DateTimeException(stringBuilder.toString());
    }

    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery != TemporalQueries.offset()) {
            if (temporalQuery != TemporalQueries.zone()) {
                if (!(temporalQuery == TemporalQueries.localDate() || temporalQuery == TemporalQueries.localTime() || temporalQuery == TemporalQueries.precision() || temporalQuery == TemporalQueries.chronology())) {
                    if (temporalQuery != TemporalQueries.zoneId()) {
                        return temporalQuery.queryFrom(this);
                    }
                }
                return null;
            }
        }
        return this;
    }

    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.OFFSET_SECONDS, (long) this.totalSeconds);
    }

    public int compareTo(ZoneOffset zoneOffset) {
        return zoneOffset.totalSeconds - this.totalSeconds;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ZoneOffset)) {
            return false;
        }
        if (this.totalSeconds != ((ZoneOffset) obj).totalSeconds) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.totalSeconds;
    }

    public String toString() {
        return this.id;
    }

    private Object writeReplace() {
        return new Ser((byte) 8, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(8);
        writeExternal(dataOutput);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        int i = this.totalSeconds;
        int i2 = i % 900 == 0 ? i / 900 : 127;
        dataOutput.writeByte(i2);
        if (i2 == 127) {
            dataOutput.writeInt(i);
        }
    }

    static ZoneOffset readExternal(DataInput dataInput) throws IOException {
        byte readByte = dataInput.readByte();
        return readByte == Byte.MAX_VALUE ? ofTotalSeconds(dataInput.readInt()) : ofTotalSeconds(readByte * 900);
    }
}
