package org.apache.commons.lang3.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class DurationFormatUtils {
    /* renamed from: H */
    static final Object f67H = "H";
    public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'";
    /* renamed from: M */
    static final Object f68M = "M";
    /* renamed from: S */
    static final Object f69S = "S";
    /* renamed from: d */
    static final Object f70d = "d";
    /* renamed from: m */
    static final Object f71m = "m";
    /* renamed from: s */
    static final Object f72s = "s";
    /* renamed from: y */
    static final Object f73y = "y";

    static class Token {
        private int count;
        private final Object value;

        static boolean containsTokenWithValue(Token[] tokenArr, Object obj) {
            for (Token value : tokenArr) {
                if (value.getValue() == obj) {
                    return 1;
                }
            }
            return false;
        }

        Token(Object obj) {
            this.value = obj;
            this.count = 1;
        }

        Token(Object obj, int i) {
            this.value = obj;
            this.count = i;
        }

        void increment() {
            this.count++;
        }

        int getCount() {
            return this.count;
        }

        Object getValue() {
            return this.value;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof Token)) {
                return false;
            }
            Token token = (Token) obj;
            if (this.value.getClass() != token.value.getClass() || this.count != token.count) {
                return false;
            }
            if (this.value instanceof StringBuilder) {
                return this.value.toString().equals(token.value.toString());
            }
            if (this.value instanceof Number) {
                return this.value.equals(token.value);
            }
            if (this.value == token.value) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.value.hashCode();
        }

        public String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }
    }

    public static String formatDurationHMS(long j) {
        return formatDuration(j, "HH:mm:ss.SSS");
    }

    public static String formatDurationISO(long j) {
        return formatDuration(j, ISO_EXTENDED_FORMAT_PATTERN, false);
    }

    public static String formatDuration(long j, String str) {
        return formatDuration(j, str, true);
    }

    public static String formatDuration(long j, String str, boolean z) {
        long j2;
        long j3;
        long j4;
        long j5;
        long j6;
        long j7;
        Validate.inclusiveBetween(0, Long.MAX_VALUE, j, "durationMillis must not be negative");
        Token[] lexx = lexx(str);
        if (Token.containsTokenWithValue(lexx, f70d)) {
            long j8 = j / DateUtils.MILLIS_PER_DAY;
            j2 = j - (DateUtils.MILLIS_PER_DAY * j8);
            j3 = j8;
        } else {
            j2 = j;
            j3 = 0;
        }
        if (Token.containsTokenWithValue(lexx, f67H)) {
            long j9 = j2 / DateUtils.MILLIS_PER_HOUR;
            j2 -= DateUtils.MILLIS_PER_HOUR * j9;
            j4 = j9;
        } else {
            j4 = 0;
        }
        if (Token.containsTokenWithValue(lexx, f71m)) {
            j9 = j2 / 60000;
            j2 -= 60000 * j9;
            j5 = j9;
        } else {
            j5 = 0;
        }
        if (Token.containsTokenWithValue(lexx, f72s)) {
            long j10 = j2 / 1000;
            j6 = j10;
            j7 = j2 - (1000 * j10);
        } else {
            j6 = 0;
            j7 = j2;
        }
        return format(lexx, 0, 0, j3, j4, j5, j6, j7, z);
    }

    public static String formatDurationWords(long j, boolean z, boolean z2) {
        StringBuilder stringBuilder;
        String replaceOnce;
        j = formatDuration(j, "d' days 'H' hours 'm' minutes 's' seconds'");
        if (z) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(StringUtils.SPACE);
            stringBuilder.append(j);
            j = stringBuilder.toString();
            replaceOnce = StringUtils.replaceOnce(j, " 0 days", "");
            if (replaceOnce.length() != j.length()) {
                j = StringUtils.replaceOnce(replaceOnce, " 0 hours", "");
                if (j.length() != replaceOnce.length()) {
                    j = StringUtils.replaceOnce(j, " 0 minutes", "");
                    if (j.length() != j.length()) {
                        j = StringUtils.replaceOnce(j, " 0 seconds", "");
                    }
                } else {
                    j = replaceOnce;
                }
            }
            if (j.length() != 0) {
                j = j.substring(1);
            }
        }
        if (z2) {
            replaceOnce = StringUtils.replaceOnce(j, " 0 seconds", "");
            if (replaceOnce.length() != j.length()) {
                j = StringUtils.replaceOnce(replaceOnce, " 0 minutes", "");
                if (j.length() != replaceOnce.length()) {
                    replaceOnce = StringUtils.replaceOnce(j, " 0 hours", "");
                    if (replaceOnce.length() != j.length()) {
                        j = StringUtils.replaceOnce(replaceOnce, " 0 days", "");
                    }
                } else {
                    j = replaceOnce;
                }
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtils.SPACE);
        stringBuilder.append(j);
        return StringUtils.replaceOnce(StringUtils.replaceOnce(StringUtils.replaceOnce(StringUtils.replaceOnce(stringBuilder.toString(), " 1 seconds", " 1 second"), " 1 minutes", " 1 minute"), " 1 hours", " 1 hour"), " 1 days", " 1 day").trim();
    }

    public static String formatPeriodISO(long j, long j2) {
        return formatPeriod(j, j2, ISO_EXTENDED_FORMAT_PATTERN, false, TimeZone.getDefault());
    }

    public static String formatPeriod(long j, long j2, String str) {
        return formatPeriod(j, j2, str, true, TimeZone.getDefault());
    }

    public static String formatPeriod(long j, long j2, String str, boolean z, TimeZone timeZone) {
        long j3 = j;
        long j4 = j2;
        Validate.isTrue(j3 <= j4, "startMillis must not be greater than endMillis", new Object[0]);
        Token[] lexx = lexx(str);
        Calendar instance = Calendar.getInstance(timeZone);
        instance.setTime(new Date(j3));
        Calendar instance2 = Calendar.getInstance(timeZone);
        instance2.setTime(new Date(j4));
        int i = instance2.get(14) - instance.get(14);
        int i2 = instance2.get(13) - instance.get(13);
        int i3 = instance2.get(12) - instance.get(12);
        int i4 = instance2.get(11) - instance.get(11);
        int i5 = instance2.get(5) - instance.get(5);
        int i6 = instance2.get(2) - instance.get(2);
        int i7 = instance2.get(1) - instance.get(1);
        while (i < 0) {
            i += 1000;
            i2--;
        }
        while (i2 < 0) {
            i2 += 60;
            i3--;
        }
        while (i3 < 0) {
            i3 += 60;
            i4--;
        }
        while (i4 < 0) {
            i4 += 24;
            i5--;
        }
        if (Token.containsTokenWithValue(lexx, f68M)) {
            while (i5 < 0) {
                i5 += instance.getActualMaximum(5);
                i6--;
                instance.add(2, 1);
            }
            while (i6 < 0) {
                i6 += 12;
                i7--;
            }
            if (!(Token.containsTokenWithValue(lexx, f73y) || i7 == 0)) {
                while (i7 != 0) {
                    i6 += i7 * 12;
                    i7 = 0;
                }
            }
        } else {
            if (!Token.containsTokenWithValue(lexx, f73y)) {
                i7 = instance2.get(1);
                if (i6 < 0) {
                    i7--;
                }
                while (instance.get(1) != i7) {
                    i5 += instance.getActualMaximum(6) - instance.get(6);
                    if ((instance instanceof GregorianCalendar) && instance.get(2) == 1 && instance.get(5) == 29) {
                        i5++;
                    }
                    instance.add(1, 1);
                    i5 += instance.get(6);
                }
                i7 = 0;
            }
            while (instance.get(2) != instance2.get(2)) {
                i5 += instance.getActualMaximum(5);
                instance.add(2, 1);
            }
            i6 = 0;
            while (i5 < 0) {
                i5 += instance.getActualMaximum(5);
                i6--;
                instance.add(2, 1);
            }
        }
        if (!Token.containsTokenWithValue(lexx, f70d)) {
            i4 += i5 * 24;
            i5 = 0;
        }
        if (!Token.containsTokenWithValue(lexx, f67H)) {
            i3 += i4 * 60;
            i4 = 0;
        }
        if (!Token.containsTokenWithValue(lexx, f71m)) {
            i2 += i3 * 60;
            i3 = 0;
        }
        if (!Token.containsTokenWithValue(lexx, f72s)) {
            i += i2 * 1000;
            i2 = 0;
        }
        return format(lexx, (long) i7, (long) i6, (long) i5, (long) i4, (long) i3, (long) i2, (long) i, z);
    }

    static String format(Token[] tokenArr, long j, long j2, long j3, long j4, long j5, long j6, long j7, boolean z) {
        Token[] tokenArr2 = tokenArr;
        long j8 = j7;
        boolean z2 = z;
        StringBuilder stringBuilder = new StringBuilder();
        int length = tokenArr2.length;
        int i = 0;
        Object obj = null;
        while (i < length) {
            int i2;
            int i3;
            long j9;
            Token token = tokenArr2[i];
            Object value = token.getValue();
            int count = token.getCount();
            long j10;
            if (value instanceof StringBuilder) {
                stringBuilder.append(value.toString());
                j10 = j3;
                i2 = length;
                i3 = i;
            } else {
                if (value.equals(f73y)) {
                    stringBuilder.append(paddedValue(j, z2, count));
                    j10 = j3;
                    i2 = length;
                    i3 = i;
                } else {
                    long j11 = j;
                    if (value.equals(f68M)) {
                        i3 = i;
                        stringBuilder.append(paddedValue(j2, z2, count));
                        j10 = j3;
                    } else {
                        i3 = i;
                        long j12 = j2;
                        if (value.equals(f70d)) {
                            stringBuilder.append(paddedValue(j3, z2, count));
                        } else {
                            j10 = j3;
                            if (value.equals(f67H)) {
                                i2 = length;
                                stringBuilder.append(paddedValue(j4, z2, count));
                            } else {
                                i2 = length;
                                j9 = j4;
                                if (value.equals(f71m)) {
                                    stringBuilder.append(paddedValue(j5, z2, count));
                                } else {
                                    j9 = j5;
                                    if (value.equals(f72s)) {
                                        stringBuilder.append(paddedValue(j6, z2, count));
                                        obj = 1;
                                    } else {
                                        j9 = j6;
                                        if (value.equals(f69S)) {
                                            if (obj != null) {
                                                i = 3;
                                                if (z2) {
                                                    i = Math.max(3, count);
                                                }
                                                stringBuilder.append(paddedValue(j8, true, i));
                                            } else {
                                                stringBuilder.append(paddedValue(j8, z2, count));
                                            }
                                            obj = null;
                                        }
                                    }
                                    i = i3 + 1;
                                    length = i2;
                                }
                            }
                            j9 = j6;
                            obj = null;
                            i = i3 + 1;
                            length = i2;
                        }
                    }
                    i2 = length;
                }
                obj = null;
            }
            j9 = j6;
            i = i3 + 1;
            length = i2;
        }
        return stringBuilder.toString();
    }

    private static String paddedValue(long j, boolean z, int i) {
        String l = Long.toString(j);
        return z ? StringUtils.leftPad(l, i, '0') : l;
    }

    static Token[] lexx(String str) {
        ArrayList arrayList = new ArrayList(str.length());
        StringBuilder stringBuilder = null;
        Token token = stringBuilder;
        Object obj = null;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (obj == null || charAt == '\'') {
                Object obj2;
                if (charAt != '\'') {
                    if (charAt == 'H') {
                        obj2 = f67H;
                    } else if (charAt == 'M') {
                        obj2 = f68M;
                    } else if (charAt == 'S') {
                        obj2 = f69S;
                    } else if (charAt == 'd') {
                        obj2 = f70d;
                    } else if (charAt == 'm') {
                        obj2 = f71m;
                    } else if (charAt == 's') {
                        obj2 = f72s;
                    } else if (charAt != 'y') {
                        if (stringBuilder == null) {
                            stringBuilder = new StringBuilder();
                            arrayList.add(new Token(stringBuilder));
                        }
                        stringBuilder.append(charAt);
                        obj2 = null;
                    } else {
                        obj2 = f73y;
                    }
                } else if (obj != null) {
                    stringBuilder = null;
                    obj2 = stringBuilder;
                    obj = null;
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    arrayList.add(new Token(stringBuilder2));
                    obj2 = null;
                    stringBuilder = stringBuilder2;
                    obj = 1;
                }
                if (obj2 != null) {
                    if (token == null || !token.getValue().equals(obj2)) {
                        token = new Token(obj2);
                        arrayList.add(token);
                    } else {
                        token.increment();
                    }
                    stringBuilder = null;
                }
            } else {
                stringBuilder.append(charAt);
            }
        }
        if (obj == null) {
            return (Token[]) arrayList.toArray(new Token[arrayList.size()]);
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Unmatched quote in format: ");
        stringBuilder3.append(str);
        throw new IllegalArgumentException(stringBuilder3.toString());
    }
}
