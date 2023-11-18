package org.threeten.bp;

import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.UnsupportedTemporalTypeException;

public final class Duration implements TemporalAmount, Comparable<Duration>, Serializable {
    private static final BigInteger BI_NANOS_PER_SECOND = BigInteger.valueOf(C0649C.NANOS_PER_SECOND);
    private static final int NANOS_PER_MILLI = 1000000;
    private static final int NANOS_PER_SECOND = 1000000000;
    private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", 2);
    public static final Duration ZERO = new Duration(0, 0);
    private static final long serialVersionUID = 3078945930695997490L;
    private final int nanos;
    private final long seconds;

    public static Duration ofDays(long j) {
        return create(Jdk8Methods.safeMultiply(j, 86400), 0);
    }

    public static Duration ofHours(long j) {
        return create(Jdk8Methods.safeMultiply(j, 3600), 0);
    }

    public static Duration ofMinutes(long j) {
        return create(Jdk8Methods.safeMultiply(j, 60), 0);
    }

    public static Duration ofSeconds(long j) {
        return create(j, 0);
    }

    public static Duration ofSeconds(long j, long j2) {
        return create(Jdk8Methods.safeAdd(j, Jdk8Methods.floorDiv(j2, (long) C0649C.NANOS_PER_SECOND)), Jdk8Methods.floorMod(j2, (int) NANOS_PER_SECOND));
    }

    public static Duration ofMillis(long j) {
        long j2 = j / 1000;
        j = (int) (j % 1000);
        if (j < null) {
            j += 1000;
            j2--;
        }
        return create(j2, j * NANOS_PER_MILLI);
    }

    public static Duration ofNanos(long j) {
        long j2 = j / C0649C.NANOS_PER_SECOND;
        j = (int) (j % C0649C.NANOS_PER_SECOND);
        if (j < null) {
            j += NANOS_PER_SECOND;
            j2--;
        }
        return create(j2, j);
    }

    public static Duration of(long j, TemporalUnit temporalUnit) {
        return ZERO.plus(j, temporalUnit);
    }

    public static Duration from(TemporalAmount temporalAmount) {
        Jdk8Methods.requireNonNull(temporalAmount, "amount");
        Duration duration = ZERO;
        for (TemporalUnit temporalUnit : temporalAmount.getUnits()) {
            duration = duration.plus(temporalAmount.get(temporalUnit), temporalUnit);
        }
        return duration;
    }

    public static org.threeten.bp.Duration between(org.threeten.bp.temporal.Temporal r11, org.threeten.bp.temporal.Temporal r12) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = org.threeten.bp.temporal.ChronoUnit.SECONDS;
        r0 = r11.until(r12, r0);
        r2 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;
        r2 = r11.isSupported(r2);
        r3 = 0;
        if (r2 == 0) goto L_0x0057;
    L_0x0010:
        r2 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;
        r2 = r12.isSupported(r2);
        if (r2 == 0) goto L_0x0057;
    L_0x0018:
        r2 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;	 Catch:{ DateTimeException -> 0x0057, DateTimeException -> 0x0057 }
        r5 = r11.getLong(r2);	 Catch:{ DateTimeException -> 0x0057, DateTimeException -> 0x0057 }
        r2 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;	 Catch:{ DateTimeException -> 0x0057, DateTimeException -> 0x0057 }
        r7 = r12.getLong(r2);	 Catch:{ DateTimeException -> 0x0057, DateTimeException -> 0x0057 }
        r2 = 0;
        r9 = r7 - r5;
        r2 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        r7 = 1000000000; // 0x3b9aca00 float:0.0047237873 double:4.94065646E-315;
        if (r2 <= 0) goto L_0x0036;
    L_0x002e:
        r2 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r2 >= 0) goto L_0x0036;
    L_0x0032:
        r11 = r9 + r7;
    L_0x0034:
        r3 = r11;
        goto L_0x0057;
    L_0x0036:
        r2 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r2 >= 0) goto L_0x0041;
    L_0x003a:
        r2 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x0041;
    L_0x003e:
        r11 = r9 - r7;
        goto L_0x0034;
    L_0x0041:
        r2 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r2 != 0) goto L_0x0056;
    L_0x0045:
        r2 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r2 == 0) goto L_0x0056;
    L_0x0049:
        r2 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;	 Catch:{ DateTimeException -> 0x0056, DateTimeException -> 0x0056 }
        r12 = r12.with(r2, r5);	 Catch:{ DateTimeException -> 0x0056, DateTimeException -> 0x0056 }
        r2 = org.threeten.bp.temporal.ChronoUnit.SECONDS;	 Catch:{ DateTimeException -> 0x0056, DateTimeException -> 0x0056 }
        r11 = r11.until(r12, r2);	 Catch:{ DateTimeException -> 0x0056, DateTimeException -> 0x0056 }
        r0 = r11;
    L_0x0056:
        r3 = r9;
    L_0x0057:
        r11 = ofSeconds(r0, r3);
        return r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.Duration.between(org.threeten.bp.temporal.Temporal, org.threeten.bp.temporal.Temporal):org.threeten.bp.Duration");
    }

    public static Duration parse(CharSequence charSequence) {
        CharSequence charSequence2 = charSequence;
        Jdk8Methods.requireNonNull(charSequence2, MimeTypes.BASE_TYPE_TEXT);
        Matcher matcher = PATTERN.matcher(charSequence2);
        if (matcher.matches() && !"T".equals(matcher.group(3))) {
            int i = 1;
            boolean equals = "-".equals(matcher.group(1));
            String group = matcher.group(2);
            String group2 = matcher.group(4);
            String group3 = matcher.group(5);
            String group4 = matcher.group(6);
            String group5 = matcher.group(7);
            if (!(group == null && group2 == null && group3 == null && group4 == null)) {
                long parseNumber = parseNumber(charSequence2, group, 86400, "days");
                long parseNumber2 = parseNumber(charSequence2, group2, 3600, "hours");
                long parseNumber3 = parseNumber(charSequence2, group3, 60, "minutes");
                long parseNumber4 = parseNumber(charSequence2, group4, 1, "seconds");
                Object obj = (group4 == null || group4.charAt(0) != '-') ? null : 1;
                if (obj != null) {
                    i = -1;
                }
                try {
                    return create(equals, parseNumber, parseNumber2, parseNumber3, parseNumber4, parseFraction(charSequence2, group5, i));
                } catch (Throwable e) {
                    throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: overflow", charSequence2, 0).initCause(e));
                }
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Duration", charSequence2, 0);
    }

    private static long parseNumber(CharSequence charSequence, String str, int i, String str2) {
        StringBuilder stringBuilder;
        if (str == null) {
            return null;
        }
        try {
            if (str.startsWith("+")) {
                str = str.substring(1);
            }
            return Jdk8Methods.safeMultiply(Long.parseLong(str), i);
        } catch (String str3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Text cannot be parsed to a Duration: ");
            stringBuilder.append(str2);
            throw ((DateTimeParseException) new DateTimeParseException(stringBuilder.toString(), charSequence, 0).initCause(str3));
        } catch (String str32) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Text cannot be parsed to a Duration: ");
            stringBuilder.append(str2);
            throw ((DateTimeParseException) new DateTimeParseException(stringBuilder.toString(), charSequence, 0).initCause(str32));
        }
    }

    private static int parseFraction(CharSequence charSequence, String str, int i) {
        if (str != null) {
            if (str.length() != 0) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append("000000000");
                    return Integer.parseInt(stringBuilder.toString().substring(0, 9)) * i;
                } catch (String str2) {
                    throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: fraction", charSequence, 0).initCause(str2));
                } catch (String str22) {
                    throw ((DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: fraction", charSequence, 0).initCause(str22));
                }
            }
        }
        return 0;
    }

    private static Duration create(boolean z, long j, long j2, long j3, long j4, int i) {
        j = Jdk8Methods.safeAdd(j, Jdk8Methods.safeAdd(j2, Jdk8Methods.safeAdd(j3, j4)));
        if (z) {
            return ofSeconds(j, (long) i).negated();
        }
        return ofSeconds(j, (long) i);
    }

    private static Duration create(long j, int i) {
        if ((j | ((long) i)) == 0) {
            return ZERO;
        }
        return new Duration(j, i);
    }

    private Duration(long j, int i) {
        this.seconds = j;
        this.nanos = i;
    }

    public List<TemporalUnit> getUnits() {
        return Collections.unmodifiableList(Arrays.asList(new ChronoUnit[]{ChronoUnit.SECONDS, ChronoUnit.NANOS}));
    }

    public long get(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.SECONDS) {
            return this.seconds;
        }
        if (temporalUnit == ChronoUnit.NANOS) {
            return (long) this.nanos;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unsupported unit: ");
        stringBuilder.append(temporalUnit);
        throw new UnsupportedTemporalTypeException(stringBuilder.toString());
    }

    public boolean isZero() {
        return (this.seconds | ((long) this.nanos)) == 0;
    }

    public boolean isNegative() {
        return this.seconds < 0;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public int getNano() {
        return this.nanos;
    }

    public Duration withSeconds(long j) {
        return create(j, this.nanos);
    }

    public Duration withNanos(int i) {
        ChronoField.NANO_OF_SECOND.checkValidIntValue((long) i);
        return create(this.seconds, i);
    }

    public Duration plus(Duration duration) {
        return plus(duration.getSeconds(), (long) duration.getNano());
    }

    public Duration plus(long j, TemporalUnit temporalUnit) {
        Jdk8Methods.requireNonNull(temporalUnit, "unit");
        if (temporalUnit == ChronoUnit.DAYS) {
            return plus(Jdk8Methods.safeMultiply(j, 86400), 0);
        }
        if (temporalUnit.isDurationEstimated()) {
            throw new DateTimeException("Unit must not have an estimated duration");
        } else if (j == 0) {
            return this;
        } else {
            if (temporalUnit instanceof ChronoUnit) {
                switch ((ChronoUnit) temporalUnit) {
                    case NANOS:
                        return plusNanos(j);
                    case MICROS:
                        return plusSeconds((j / C0649C.NANOS_PER_SECOND) * 1000).plusNanos((j % C0649C.NANOS_PER_SECOND) * 1000);
                    case MILLIS:
                        return plusMillis(j);
                    case SECONDS:
                        return plusSeconds(j);
                    default:
                        return plusSeconds(Jdk8Methods.safeMultiply(temporalUnit.getDuration().seconds, j));
                }
            }
            j = temporalUnit.getDuration().multipliedBy(j);
            return plusSeconds(j.getSeconds()).plusNanos((long) j.getNano());
        }
    }

    public Duration plusDays(long j) {
        return plus(Jdk8Methods.safeMultiply(j, 86400), 0);
    }

    public Duration plusHours(long j) {
        return plus(Jdk8Methods.safeMultiply(j, 3600), 0);
    }

    public Duration plusMinutes(long j) {
        return plus(Jdk8Methods.safeMultiply(j, 60), 0);
    }

    public Duration plusSeconds(long j) {
        return plus(j, 0);
    }

    public Duration plusMillis(long j) {
        return plus(j / 1000, (j % 1000) * C0649C.MICROS_PER_SECOND);
    }

    public Duration plusNanos(long j) {
        return plus(0, j);
    }

    private Duration plus(long j, long j2) {
        if ((j | j2) == 0) {
            return this;
        }
        return ofSeconds(Jdk8Methods.safeAdd(Jdk8Methods.safeAdd(this.seconds, j), j2 / C0649C.NANOS_PER_SECOND), ((long) this.nanos) + (j2 % C0649C.NANOS_PER_SECOND));
    }

    public Duration minus(Duration duration) {
        long seconds = duration.getSeconds();
        duration = duration.getNano();
        if (seconds == Long.MIN_VALUE) {
            return plus(Long.MAX_VALUE, (long) (-duration)).plus(1, 0);
        }
        return plus(-seconds, (long) (-duration));
    }

    public Duration minus(long j, TemporalUnit temporalUnit) {
        return j == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1, temporalUnit) : plus(-j, temporalUnit);
    }

    public Duration minusDays(long j) {
        return j == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-j);
    }

    public Duration minusHours(long j) {
        return j == Long.MIN_VALUE ? plusHours(Long.MAX_VALUE).plusHours(1) : plusHours(-j);
    }

    public Duration minusMinutes(long j) {
        return j == Long.MIN_VALUE ? plusMinutes(Long.MAX_VALUE).plusMinutes(1) : plusMinutes(-j);
    }

    public Duration minusSeconds(long j) {
        return j == Long.MIN_VALUE ? plusSeconds(Long.MAX_VALUE).plusSeconds(1) : plusSeconds(-j);
    }

    public Duration minusMillis(long j) {
        return j == Long.MIN_VALUE ? plusMillis(Long.MAX_VALUE).plusMillis(1) : plusMillis(-j);
    }

    public Duration minusNanos(long j) {
        return j == Long.MIN_VALUE ? plusNanos(Long.MAX_VALUE).plusNanos(1) : plusNanos(-j);
    }

    public Duration multipliedBy(long j) {
        if (j == 0) {
            return ZERO;
        }
        if (j == 1) {
            return this;
        }
        return create(toSeconds().multiply(BigDecimal.valueOf(j)));
    }

    public Duration dividedBy(long j) {
        if (j == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        } else if (j == 1) {
            return this;
        } else {
            return create(toSeconds().divide(BigDecimal.valueOf(j), RoundingMode.DOWN));
        }
    }

    private BigDecimal toSeconds() {
        return BigDecimal.valueOf(this.seconds).add(BigDecimal.valueOf((long) this.nanos, 9));
    }

    private static Duration create(BigDecimal bigDecimal) {
        bigDecimal = bigDecimal.movePointRight(9).toBigIntegerExact();
        BigInteger[] divideAndRemainder = bigDecimal.divideAndRemainder(BI_NANOS_PER_SECOND);
        if (divideAndRemainder[0].bitLength() <= 63) {
            return ofSeconds(divideAndRemainder[0].longValue(), (long) divideAndRemainder[1].intValue());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Exceeds capacity of Duration: ");
        stringBuilder.append(bigDecimal);
        throw new ArithmeticException(stringBuilder.toString());
    }

    public Duration negated() {
        return multipliedBy(-1);
    }

    public Duration abs() {
        return isNegative() ? negated() : this;
    }

    public Temporal addTo(Temporal temporal) {
        if (this.seconds != 0) {
            temporal = temporal.plus(this.seconds, ChronoUnit.SECONDS);
        }
        return this.nanos != 0 ? temporal.plus((long) this.nanos, ChronoUnit.NANOS) : temporal;
    }

    public Temporal subtractFrom(Temporal temporal) {
        if (this.seconds != 0) {
            temporal = temporal.minus(this.seconds, ChronoUnit.SECONDS);
        }
        return this.nanos != 0 ? temporal.minus((long) this.nanos, ChronoUnit.NANOS) : temporal;
    }

    public long toDays() {
        return this.seconds / 86400;
    }

    public long toHours() {
        return this.seconds / 3600;
    }

    public long toMinutes() {
        return this.seconds / 60;
    }

    public long toMillis() {
        return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(this.seconds, 1000), (long) (this.nanos / NANOS_PER_MILLI));
    }

    public long toNanos() {
        return Jdk8Methods.safeAdd(Jdk8Methods.safeMultiply(this.seconds, (int) NANOS_PER_SECOND), (long) this.nanos);
    }

    public int compareTo(Duration duration) {
        int compareLongs = Jdk8Methods.compareLongs(this.seconds, duration.seconds);
        if (compareLongs != 0) {
            return compareLongs;
        }
        return this.nanos - duration.nanos;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Duration)) {
            return false;
        }
        Duration duration = (Duration) obj;
        if (this.seconds != duration.seconds || this.nanos != duration.nanos) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((int) (this.seconds ^ (this.seconds >>> 32))) + (this.nanos * 51);
    }

    public String toString() {
        if (this == ZERO) {
            return "PT0S";
        }
        long j = this.seconds / 3600;
        int i = (int) ((this.seconds % 3600) / 60);
        int i2 = (int) (this.seconds % 60);
        StringBuilder stringBuilder = new StringBuilder(24);
        stringBuilder.append("PT");
        if (j != 0) {
            stringBuilder.append(j);
            stringBuilder.append('H');
        }
        if (i != 0) {
            stringBuilder.append(i);
            stringBuilder.append('M');
        }
        if (i2 == 0 && this.nanos == 0 && stringBuilder.length() > 2) {
            return stringBuilder.toString();
        }
        if (i2 >= 0 || this.nanos <= 0) {
            stringBuilder.append(i2);
        } else if (i2 == -1) {
            stringBuilder.append("-0");
        } else {
            stringBuilder.append(i2 + 1);
        }
        if (this.nanos > 0) {
            int length = stringBuilder.length();
            if (i2 < 0) {
                stringBuilder.append(2000000000 - this.nanos);
            } else {
                stringBuilder.append(this.nanos + NANOS_PER_SECOND);
            }
            while (stringBuilder.charAt(stringBuilder.length() - 1) == '0') {
                stringBuilder.setLength(stringBuilder.length() - 1);
            }
            stringBuilder.setCharAt(length, '.');
        }
        stringBuilder.append('S');
        return stringBuilder.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 1, this);
    }

    private Object readResolve() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.seconds);
        dataOutput.writeInt(this.nanos);
    }

    static Duration readExternal(DataInput dataInput) throws IOException {
        return ofSeconds(dataInput.readLong(), (long) dataInput.readInt());
    }
}
