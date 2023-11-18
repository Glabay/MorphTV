package org.threeten.bp.format;

import com.google.android.exoplayer2.extractor.ogg.DefaultOggSeeker;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.IsoFields;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.ValueRange;
import org.threeten.bp.temporal.WeekFields;
import org.threeten.bp.zone.ZoneRulesProvider;

public final class DateTimeFormatterBuilder {
    private static final Map<Character, TemporalField> FIELD_MAP = new HashMap();
    static final Comparator<String> LENGTH_SORT = new C15573();
    private static final TemporalQuery<ZoneId> QUERY_REGION_ONLY = new C15551();
    private DateTimeFormatterBuilder active;
    private final boolean optional;
    private char padNextChar;
    private int padNextWidth;
    private final DateTimeFormatterBuilder parent;
    private final List<DateTimePrinterParser> printerParsers;
    private int valueParserIndex;

    /* renamed from: org.threeten.bp.format.DateTimeFormatterBuilder$1 */
    static class C15551 implements TemporalQuery<ZoneId> {
        C15551() {
        }

        public ZoneId queryFrom(TemporalAccessor temporalAccessor) {
            ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zoneId());
            return (zoneId == null || (zoneId instanceof ZoneOffset)) ? null : zoneId;
        }
    }

    /* renamed from: org.threeten.bp.format.DateTimeFormatterBuilder$3 */
    static class C15573 implements Comparator<String> {
        C15573() {
        }

        public int compare(String str, String str2) {
            return str.length() == str2.length() ? str.compareTo(str2) : str.length() - str2.length();
        }
    }

    interface DateTimePrinterParser {
        int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i);

        boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder);
    }

    static final class CharLiteralPrinterParser implements DateTimePrinterParser {
        private final char literal;

        CharLiteralPrinterParser(char c) {
            this.literal = c;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            stringBuilder.append(this.literal);
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (i == charSequence.length()) {
                return i ^ -1;
            }
            return dateTimeParseContext.charEquals(this.literal, charSequence.charAt(i)) == null ? i ^ -1 : i + 1;
        }

        public String toString() {
            if (this.literal == '\'') {
                return "''";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("'");
            stringBuilder.append(this.literal);
            stringBuilder.append("'");
            return stringBuilder.toString();
        }
    }

    static final class ChronoPrinterParser implements DateTimePrinterParser {
        private final TextStyle textStyle;

        ChronoPrinterParser(TextStyle textStyle) {
            this.textStyle = textStyle;
        }

        public boolean print(org.threeten.bp.format.DateTimePrintContext r4, java.lang.StringBuilder r5) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r3 = this;
            r0 = org.threeten.bp.temporal.TemporalQueries.chronology();
            r0 = r4.getValue(r0);
            r0 = (org.threeten.bp.chrono.Chronology) r0;
            if (r0 != 0) goto L_0x000e;
        L_0x000c:
            r4 = 0;
            return r4;
        L_0x000e:
            r1 = r3.textStyle;
            if (r1 != 0) goto L_0x001a;
        L_0x0012:
            r4 = r0.getId();
            r5.append(r4);
            goto L_0x003d;
        L_0x001a:
            r1 = "org.threeten.bp.format.ChronologyText";
            r4 = r4.getLocale();
            r2 = org.threeten.bp.format.DateTimeFormatterBuilder.class;
            r2 = r2.getClassLoader();
            r4 = java.util.ResourceBundle.getBundle(r1, r4, r2);
            r1 = r0.getId();	 Catch:{ MissingResourceException -> 0x0036 }
            r4 = r4.getString(r1);	 Catch:{ MissingResourceException -> 0x0036 }
            r5.append(r4);	 Catch:{ MissingResourceException -> 0x0036 }
            goto L_0x003d;
        L_0x0036:
            r4 = r0.getId();
            r5.append(r4);
        L_0x003d:
            r4 = 1;
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.format.DateTimeFormatterBuilder.ChronoPrinterParser.print(org.threeten.bp.format.DateTimePrintContext, java.lang.StringBuilder):boolean");
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (i >= 0) {
                if (i <= charSequence.length()) {
                    Chronology chronology = null;
                    int i2 = -1;
                    for (Chronology chronology2 : Chronology.getAvailableChronologies()) {
                        CharSequence id = chronology2.getId();
                        int length = id.length();
                        if (length > i2 && dateTimeParseContext.subSequenceEquals(charSequence, i, id, 0, length)) {
                            chronology = chronology2;
                            i2 = length;
                        }
                    }
                    if (chronology == null) {
                        return i ^ -1;
                    }
                    dateTimeParseContext.setParsed(chronology);
                    return i + i2;
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    static final class CompositePrinterParser implements DateTimePrinterParser {
        private final boolean optional;
        private final DateTimePrinterParser[] printerParsers;

        CompositePrinterParser(List<DateTimePrinterParser> list, boolean z) {
            this((DateTimePrinterParser[]) list.toArray(new DateTimePrinterParser[list.size()]), z);
        }

        CompositePrinterParser(DateTimePrinterParser[] dateTimePrinterParserArr, boolean z) {
            this.printerParsers = dateTimePrinterParserArr;
            this.optional = z;
        }

        public CompositePrinterParser withOptional(boolean z) {
            if (z == this.optional) {
                return this;
            }
            return new CompositePrinterParser(this.printerParsers, z);
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            int length = stringBuilder.length();
            if (this.optional) {
                dateTimePrintContext.startOptional();
            }
            try {
                DateTimePrinterParser[] dateTimePrinterParserArr = this.printerParsers;
                int length2 = dateTimePrinterParserArr.length;
                int i = 0;
                while (i < length2) {
                    if (dateTimePrinterParserArr[i].print(dateTimePrintContext, stringBuilder)) {
                        i++;
                    } else {
                        stringBuilder.setLength(length);
                        return true;
                    }
                }
                if (this.optional != null) {
                    dateTimePrintContext.endOptional();
                }
                return true;
            } finally {
                if (this.optional) {
                    dateTimePrintContext.endOptional();
                }
            }
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int i2 = 0;
            int length;
            if (this.optional) {
                dateTimeParseContext.startOptional();
                int i3 = i;
                for (DateTimePrinterParser parse : this.printerParsers) {
                    i3 = parse.parse(dateTimeParseContext, charSequence, i3);
                    if (i3 < 0) {
                        dateTimeParseContext.endOptional(false);
                        return i;
                    }
                }
                dateTimeParseContext.endOptional(true);
                return i3;
            }
            DateTimePrinterParser[] dateTimePrinterParserArr = this.printerParsers;
            length = dateTimePrinterParserArr.length;
            while (i2 < length) {
                i = dateTimePrinterParserArr[i2].parse(dateTimeParseContext, charSequence, i);
                if (i < 0) {
                    break;
                }
                i2++;
            }
            return i;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.printerParsers != null) {
                stringBuilder.append(this.optional ? "[" : "(");
                for (Object append : this.printerParsers) {
                    stringBuilder.append(append);
                }
                stringBuilder.append(this.optional ? "]" : ")");
            }
            return stringBuilder.toString();
        }
    }

    static class DefaultingParser implements DateTimePrinterParser {
        private final TemporalField field;
        private final long value;

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            return true;
        }

        DefaultingParser(TemporalField temporalField, long j) {
            this.field = temporalField;
            this.value = j;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (dateTimeParseContext.getParsed(this.field) == null) {
                dateTimeParseContext.setParsedField(this.field, this.value, i, i);
            }
            return i;
        }
    }

    static final class FractionPrinterParser implements DateTimePrinterParser {
        private final boolean decimalPoint;
        private final TemporalField field;
        private final int maxWidth;
        private final int minWidth;

        FractionPrinterParser(TemporalField temporalField, int i, int i2, boolean z) {
            Jdk8Methods.requireNonNull(temporalField, "field");
            if (temporalField.range().isFixed()) {
                if (i >= 0) {
                    if (i <= 9) {
                        if (i2 >= 1) {
                            if (i2 <= 9) {
                                if (i2 < i) {
                                    z = new StringBuilder();
                                    z.append("Maximum width must exceed or equal the minimum width but ");
                                    z.append(i2);
                                    z.append(" < ");
                                    z.append(i);
                                    throw new IllegalArgumentException(z.toString());
                                }
                                this.field = temporalField;
                                this.minWidth = i;
                                this.maxWidth = i2;
                                this.decimalPoint = z;
                                return;
                            }
                        }
                        i = new StringBuilder();
                        i.append("Maximum width must be from 1 to 9 inclusive but was ");
                        i.append(i2);
                        throw new IllegalArgumentException(i.toString());
                    }
                }
                i2 = new StringBuilder();
                i2.append("Minimum width must be from 0 to 9 inclusive but was ");
                i2.append(i);
                throw new IllegalArgumentException(i2.toString());
            }
            i2 = new StringBuilder();
            i2.append("Field must have a fixed set of values: ");
            i2.append(temporalField);
            throw new IllegalArgumentException(i2.toString());
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            Long value = dateTimePrintContext.getValue(this.field);
            int i = 0;
            if (value == null) {
                return false;
            }
            dateTimePrintContext = dateTimePrintContext.getSymbols();
            BigDecimal convertToFraction = convertToFraction(value.longValue());
            if (convertToFraction.scale() != 0) {
                String convertNumberToI18N = dateTimePrintContext.convertNumberToI18N(convertToFraction.setScale(Math.min(Math.max(convertToFraction.scale(), this.minWidth), this.maxWidth), RoundingMode.FLOOR).toPlainString().substring(2));
                if (this.decimalPoint) {
                    stringBuilder.append(dateTimePrintContext.getDecimalSeparator());
                }
                stringBuilder.append(convertNumberToI18N);
            } else if (this.minWidth > 0) {
                if (this.decimalPoint) {
                    stringBuilder.append(dateTimePrintContext.getDecimalSeparator());
                }
                while (i < this.minWidth) {
                    stringBuilder.append(dateTimePrintContext.getZeroDigit());
                    i++;
                }
            }
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int i2 = dateTimeParseContext.isStrict() ? this.minWidth : 0;
            int i3 = dateTimeParseContext.isStrict() ? this.maxWidth : 9;
            int length = charSequence.length();
            if (i == length) {
                if (i2 > 0) {
                    i ^= -1;
                }
                return i;
            }
            if (this.decimalPoint) {
                if (charSequence.charAt(i) != dateTimeParseContext.getSymbols().getDecimalSeparator()) {
                    if (i2 > 0) {
                        i ^= -1;
                    }
                    return i;
                }
                i++;
            }
            int i4 = i;
            i2 += i4;
            if (i2 > length) {
                return i4 ^ -1;
            }
            int i5;
            i = Math.min(i3 + i4, length);
            int i6 = i4;
            i3 = 0;
            while (i6 < i) {
                length = i6 + 1;
                i6 = dateTimeParseContext.getSymbols().convertToDigit(charSequence.charAt(i6));
                if (i6 >= 0) {
                    i3 = (i3 * 10) + i6;
                    i6 = length;
                } else if (length < i2) {
                    return i4 ^ -1;
                } else {
                    i5 = length - 1;
                    return dateTimeParseContext.setParsedField(this.field, convertFromFraction(new BigDecimal(i3).movePointLeft(i5 - i4)), i4, i5);
                }
            }
            i5 = i6;
            return dateTimeParseContext.setParsedField(this.field, convertFromFraction(new BigDecimal(i3).movePointLeft(i5 - i4)), i4, i5);
        }

        private BigDecimal convertToFraction(long j) {
            ValueRange range = this.field.range();
            range.checkValidValue(j, this.field);
            BigDecimal valueOf = BigDecimal.valueOf(range.getMinimum());
            j = BigDecimal.valueOf(j).subtract(valueOf).divide(BigDecimal.valueOf(range.getMaximum()).subtract(valueOf).add(BigDecimal.ONE), 9, RoundingMode.FLOOR);
            return j.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : j.stripTrailingZeros();
        }

        private long convertFromFraction(BigDecimal bigDecimal) {
            ValueRange range = this.field.range();
            BigDecimal valueOf = BigDecimal.valueOf(range.getMinimum());
            return bigDecimal.multiply(BigDecimal.valueOf(range.getMaximum()).subtract(valueOf).add(BigDecimal.ONE)).setScale(0, RoundingMode.FLOOR).add(valueOf).longValueExact();
        }

        public String toString() {
            String str = this.decimalPoint ? ",DecimalPoint" : "";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fraction(");
            stringBuilder.append(this.field);
            stringBuilder.append(",");
            stringBuilder.append(this.minWidth);
            stringBuilder.append(",");
            stringBuilder.append(this.maxWidth);
            stringBuilder.append(str);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static final class InstantPrinterParser implements DateTimePrinterParser {
        private static final long SECONDS_0000_TO_1970 = 62167219200L;
        private static final long SECONDS_PER_10000_YEARS = 315569520000L;
        private final int fractionalDigits;

        public String toString() {
            return "Instant()";
        }

        InstantPrinterParser(int i) {
            this.fractionalDigits = i;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            InstantPrinterParser instantPrinterParser = this;
            StringBuilder stringBuilder2 = stringBuilder;
            Long value = dateTimePrintContext.getValue(ChronoField.INSTANT_SECONDS);
            Long valueOf = Long.valueOf(0);
            if (dateTimePrintContext.getTemporal().isSupported(ChronoField.NANO_OF_SECOND)) {
                valueOf = Long.valueOf(dateTimePrintContext.getTemporal().getLong(ChronoField.NANO_OF_SECOND));
            }
            int i = 0;
            if (value == null) {
                return false;
            }
            long longValue = value.longValue();
            int checkValidIntValue = ChronoField.NANO_OF_SECOND.checkValidIntValue(valueOf.longValue());
            long j;
            long floorDiv;
            if (longValue >= -62167219200L) {
                j = (longValue - SECONDS_PER_10000_YEARS) + SECONDS_0000_TO_1970;
                floorDiv = Jdk8Methods.floorDiv(j, (long) SECONDS_PER_10000_YEARS) + 1;
                LocalDateTime ofEpochSecond = LocalDateTime.ofEpochSecond(Jdk8Methods.floorMod(j, (long) SECONDS_PER_10000_YEARS) - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC);
                if (floorDiv > 0) {
                    stringBuilder2.append('+');
                    stringBuilder2.append(floorDiv);
                }
                stringBuilder2.append(ofEpochSecond);
                if (ofEpochSecond.getSecond() == 0) {
                    stringBuilder2.append(":00");
                }
            } else {
                floorDiv = longValue + SECONDS_0000_TO_1970;
                j = floorDiv / SECONDS_PER_10000_YEARS;
                floorDiv %= SECONDS_PER_10000_YEARS;
                LocalDateTime ofEpochSecond2 = LocalDateTime.ofEpochSecond(floorDiv - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC);
                int length = stringBuilder.length();
                stringBuilder2.append(ofEpochSecond2);
                if (ofEpochSecond2.getSecond() == 0) {
                    stringBuilder2.append(":00");
                }
                if (j < 0) {
                    if (ofEpochSecond2.getYear() == -10000) {
                        stringBuilder2.replace(length, length + 2, Long.toString(j - 1));
                    } else if (floorDiv == 0) {
                        stringBuilder2.insert(length, j);
                    } else {
                        stringBuilder2.insert(length + 1, Math.abs(j));
                    }
                }
            }
            if (instantPrinterParser.fractionalDigits == -2) {
                if (checkValidIntValue != 0) {
                    stringBuilder2.append('.');
                    if (checkValidIntValue % 1000000 == 0) {
                        stringBuilder2.append(Integer.toString((checkValidIntValue / 1000000) + 1000).substring(1));
                    } else if (checkValidIntValue % 1000 == 0) {
                        stringBuilder2.append(Integer.toString((checkValidIntValue / 1000) + 1000000).substring(1));
                    } else {
                        stringBuilder2.append(Integer.toString(checkValidIntValue + 1000000000).substring(1));
                    }
                }
            } else if (instantPrinterParser.fractionalDigits > 0 || (instantPrinterParser.fractionalDigits == -1 && checkValidIntValue > 0)) {
                stringBuilder2.append('.');
                int i2 = 100000000;
                while (true) {
                    if ((instantPrinterParser.fractionalDigits != -1 || checkValidIntValue <= 0) && i >= instantPrinterParser.fractionalDigits) {
                        break;
                    }
                    int i3 = checkValidIntValue / i2;
                    stringBuilder2.append((char) (i3 + 48));
                    checkValidIntValue -= i3 * i2;
                    i2 /= 10;
                    i++;
                }
            }
            stringBuilder2.append('Z');
            return true;
        }

        public int parse(org.threeten.bp.format.DateTimeParseContext r17, java.lang.CharSequence r18, int r19) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r16 = this;
            r0 = r16;
            r7 = r19;
            r1 = r17.copy();
            r2 = r0.fractionalDigits;
            r3 = 0;
            if (r2 >= 0) goto L_0x000f;
        L_0x000d:
            r2 = 0;
            goto L_0x0011;
        L_0x000f:
            r2 = r0.fractionalDigits;
        L_0x0011:
            r4 = r0.fractionalDigits;
            if (r4 >= 0) goto L_0x0018;
        L_0x0015:
            r4 = 9;
            goto L_0x001a;
        L_0x0018:
            r4 = r0.fractionalDigits;
        L_0x001a:
            r5 = new org.threeten.bp.format.DateTimeFormatterBuilder;
            r5.<init>();
            r6 = org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE;
            r5 = r5.append(r6);
            r6 = 84;
            r5 = r5.appendLiteral(r6);
            r6 = org.threeten.bp.temporal.ChronoField.HOUR_OF_DAY;
            r8 = 2;
            r5 = r5.appendValue(r6, r8);
            r6 = 58;
            r5 = r5.appendLiteral(r6);
            r9 = org.threeten.bp.temporal.ChronoField.MINUTE_OF_HOUR;
            r5 = r5.appendValue(r9, r8);
            r5 = r5.appendLiteral(r6);
            r6 = org.threeten.bp.temporal.ChronoField.SECOND_OF_MINUTE;
            r5 = r5.appendValue(r6, r8);
            r6 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;
            r8 = 1;
            r2 = r5.appendFraction(r6, r2, r4, r8);
            r4 = 90;
            r2 = r2.appendLiteral(r4);
            r2 = r2.toFormatter();
            r2 = r2.toPrinterParser(r3);
            r4 = r18;
            r6 = r2.parse(r1, r4, r7);
            if (r6 >= 0) goto L_0x0066;
        L_0x0065:
            return r6;
        L_0x0066:
            r2 = org.threeten.bp.temporal.ChronoField.YEAR;
            r2 = r1.getParsed(r2);
            r4 = r2.longValue();
            r2 = org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;
            r2 = r1.getParsed(r2);
            r10 = r2.intValue();
            r2 = org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH;
            r2 = r1.getParsed(r2);
            r11 = r2.intValue();
            r2 = org.threeten.bp.temporal.ChronoField.HOUR_OF_DAY;
            r2 = r1.getParsed(r2);
            r2 = r2.intValue();
            r9 = org.threeten.bp.temporal.ChronoField.MINUTE_OF_HOUR;
            r9 = r1.getParsed(r9);
            r13 = r9.intValue();
            r9 = org.threeten.bp.temporal.ChronoField.SECOND_OF_MINUTE;
            r9 = r1.getParsed(r9);
            r12 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;
            r1 = r1.getParsed(r12);
            if (r9 == 0) goto L_0x00ab;
        L_0x00a6:
            r9 = r9.intValue();
            goto L_0x00ac;
        L_0x00ab:
            r9 = 0;
        L_0x00ac:
            if (r1 == 0) goto L_0x00b3;
        L_0x00ae:
            r1 = r1.intValue();
            goto L_0x00b4;
        L_0x00b3:
            r1 = 0;
        L_0x00b4:
            r12 = (int) r4;
            r12 = r12 % 10000;
            r14 = 24;
            r15 = 59;
            if (r2 != r14) goto L_0x00c7;
        L_0x00bd:
            if (r13 != 0) goto L_0x00c7;
        L_0x00bf:
            if (r9 != 0) goto L_0x00c7;
        L_0x00c1:
            if (r1 != 0) goto L_0x00c7;
        L_0x00c3:
            r14 = r9;
            r2 = 0;
            r3 = 1;
            goto L_0x00d8;
        L_0x00c7:
            r8 = 23;
            if (r2 != r8) goto L_0x00d7;
        L_0x00cb:
            if (r13 != r15) goto L_0x00d7;
        L_0x00cd:
            r8 = 60;
            if (r9 != r8) goto L_0x00d7;
        L_0x00d1:
            r17.setParsedLeapSecond();
            r14 = 59;
            goto L_0x00d8;
        L_0x00d7:
            r14 = r9;
        L_0x00d8:
            r15 = 0;
            r9 = r12;
            r12 = r2;
            r2 = org.threeten.bp.LocalDateTime.of(r9, r10, r11, r12, r13, r14, r15);	 Catch:{ RuntimeException -> 0x010c }
            r8 = (long) r3;	 Catch:{ RuntimeException -> 0x010c }
            r2 = r2.plusDays(r8);	 Catch:{ RuntimeException -> 0x010c }
            r3 = org.threeten.bp.ZoneOffset.UTC;	 Catch:{ RuntimeException -> 0x010c }
            r2 = r2.toEpochSecond(r3);	 Catch:{ RuntimeException -> 0x010c }
            r8 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;	 Catch:{ RuntimeException -> 0x010c }
            r4 = r4 / r8;	 Catch:{ RuntimeException -> 0x010c }
            r8 = 315569520000; // 0x497968bd80 float:7.5528524E34 double:1.559120587066E-312;	 Catch:{ RuntimeException -> 0x010c }
            r4 = org.threeten.bp.jdk8.Jdk8Methods.safeMultiply(r4, r8);	 Catch:{ RuntimeException -> 0x010c }
            r8 = 0;
            r8 = r2 + r4;
            r2 = org.threeten.bp.temporal.ChronoField.INSTANT_SECONDS;
            r10 = r1;
            r1 = r17;
            r3 = r8;
            r5 = r7;
            r6 = r1.setParsedField(r2, r3, r5, r6);
            r2 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;
            r3 = (long) r10;
            r1 = r1.setParsedField(r2, r3, r5, r6);
            return r1;
        L_0x010c:
            r1 = r7 ^ -1;
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.format.DateTimeFormatterBuilder.InstantPrinterParser.parse(org.threeten.bp.format.DateTimeParseContext, java.lang.CharSequence, int):int");
        }
    }

    static final class LocalizedOffsetPrinterParser implements DateTimePrinterParser {
        private final TextStyle style;

        public LocalizedOffsetPrinterParser(TextStyle textStyle) {
            this.style = textStyle;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            Long value = dateTimePrintContext.getValue(ChronoField.OFFSET_SECONDS);
            if (value == null) {
                return null;
            }
            stringBuilder.append("GMT");
            if (this.style == TextStyle.FULL) {
                return new OffsetIdPrinterParser("", "+HH:MM:ss").print(dateTimePrintContext, stringBuilder);
            }
            dateTimePrintContext = Jdk8Methods.safeToInt(value.longValue());
            if (dateTimePrintContext != null) {
                int abs = Math.abs((dateTimePrintContext / 3600) % 100);
                int abs2 = Math.abs((dateTimePrintContext / 60) % 60);
                int abs3 = Math.abs(dateTimePrintContext % 60);
                stringBuilder.append(dateTimePrintContext < null ? "-" : "+");
                stringBuilder.append(abs);
                if (abs2 > 0 || abs3 > 0) {
                    stringBuilder.append(":");
                    stringBuilder.append((char) ((abs2 / 10) + 48));
                    stringBuilder.append((char) ((abs2 % 10) + 48));
                    if (abs3 > 0) {
                        stringBuilder.append(":");
                        stringBuilder.append((char) ((abs3 / 10) + 48));
                        stringBuilder.append((char) ((abs3 % 10) + 48));
                    }
                }
            }
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            CharSequence charSequence2 = charSequence;
            if (!dateTimeParseContext.subSequenceEquals(charSequence2, i, "GMT", 0, 3)) {
                return i ^ -1;
            }
            int i2 = i + 3;
            if (this.style == TextStyle.FULL) {
                return new OffsetIdPrinterParser("", "+HH:MM:ss").parse(dateTimeParseContext, charSequence2, i2);
            }
            DateTimeParseContext dateTimeParseContext2 = dateTimeParseContext;
            int length = charSequence.length();
            if (i2 == length) {
                return dateTimeParseContext2.setParsedField(ChronoField.OFFSET_SECONDS, 0, i2, i2);
            }
            char charAt = charSequence2.charAt(i2);
            if (charAt != '+' && charAt != '-') {
                return dateTimeParseContext2.setParsedField(ChronoField.OFFSET_SECONDS, 0, i2, i2);
            }
            int i3 = charAt == '-' ? -1 : 1;
            if (i2 == length) {
                return i2 ^ -1;
            }
            i2++;
            char charAt2 = charSequence2.charAt(i2);
            if (charAt2 >= '0') {
                if (charAt2 <= '9') {
                    i2++;
                    int i4 = charAt2 - 48;
                    if (i2 != length) {
                        char charAt3 = charSequence2.charAt(i2);
                        if (charAt3 >= '0' && charAt3 <= '9') {
                            i4 = (i4 * 10) + (charAt3 - 48);
                            if (i4 > 23) {
                                return i2 ^ -1;
                            }
                            i2++;
                        }
                    }
                    int i5 = i2;
                    if (i5 != length) {
                        if (charSequence2.charAt(i5) == ':') {
                            i5++;
                            i2 = length - 2;
                            if (i5 > i2) {
                                return i5 ^ -1;
                            }
                            char charAt4 = charSequence2.charAt(i5);
                            if (charAt4 >= '0') {
                                if (charAt4 <= '9') {
                                    i5++;
                                    int i6 = charAt4 - 48;
                                    char charAt5 = charSequence2.charAt(i5);
                                    if (charAt5 >= '0') {
                                        if (charAt5 <= '9') {
                                            i5++;
                                            i6 = (i6 * 10) + (charAt5 - 48);
                                            if (i6 > 59) {
                                                return i5 ^ -1;
                                            }
                                            if (i5 != length) {
                                                if (charSequence2.charAt(i5) == ':') {
                                                    i5++;
                                                    if (i5 > i2) {
                                                        return i5 ^ -1;
                                                    }
                                                    char charAt6 = charSequence2.charAt(i5);
                                                    if (charAt6 >= '0') {
                                                        if (charAt6 <= '9') {
                                                            i5++;
                                                            i2 = charAt6 - 48;
                                                            char charAt7 = charSequence2.charAt(i5);
                                                            if (charAt7 >= '0') {
                                                                if (charAt7 <= '9') {
                                                                    i5++;
                                                                    i2 = (i2 * 10) + (charAt7 - 48);
                                                                    if (i2 > 59) {
                                                                        return i5 ^ -1;
                                                                    }
                                                                    return dateTimeParseContext2.setParsedField(ChronoField.OFFSET_SECONDS, (long) (i3 * (((i4 * 3600) + (i6 * 60)) + i2)), i5, i5);
                                                                }
                                                            }
                                                            return i5 ^ -1;
                                                        }
                                                    }
                                                    return i5 ^ -1;
                                                }
                                            }
                                            return dateTimeParseContext2.setParsedField(ChronoField.OFFSET_SECONDS, (long) (i3 * ((i4 * 3600) + (i6 * 60))), i5, i5);
                                        }
                                    }
                                    return i5 ^ -1;
                                }
                            }
                            return i5 ^ -1;
                        }
                    }
                    return dateTimeParseContext2.setParsedField(ChronoField.OFFSET_SECONDS, (long) ((i3 * 3600) * i4), i5, i5);
                }
            }
            return i2 ^ -1;
        }
    }

    static final class LocalizedPrinterParser implements DateTimePrinterParser {
        private final FormatStyle dateStyle;
        private final FormatStyle timeStyle;

        LocalizedPrinterParser(FormatStyle formatStyle, FormatStyle formatStyle2) {
            this.dateStyle = formatStyle;
            this.timeStyle = formatStyle2;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            return formatter(dateTimePrintContext.getLocale(), Chronology.from(dateTimePrintContext.getTemporal())).toPrinterParser(false).print(dateTimePrintContext, stringBuilder);
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            return formatter(dateTimeParseContext.getLocale(), dateTimeParseContext.getEffectiveChronology()).toPrinterParser(false).parse(dateTimeParseContext, charSequence, i);
        }

        private DateTimeFormatter formatter(Locale locale, Chronology chronology) {
            return DateTimeFormatStyleProvider.getInstance().getFormatter(this.dateStyle, this.timeStyle, chronology, locale);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Localized(");
            stringBuilder.append(this.dateStyle != null ? this.dateStyle : "");
            stringBuilder.append(",");
            stringBuilder.append(this.timeStyle != null ? this.timeStyle : "");
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static class NumberPrinterParser implements DateTimePrinterParser {
        static final int[] EXCEED_POINTS = new int[]{0, 10, 100, 1000, 10000, DefaultOggSeeker.MATCH_BYTE_RANGE, 1000000, 10000000, 100000000, 1000000000};
        final TemporalField field;
        final int maxWidth;
        final int minWidth;
        final SignStyle signStyle;
        final int subsequentWidth;

        long getValue(DateTimePrintContext dateTimePrintContext, long j) {
            return j;
        }

        NumberPrinterParser(TemporalField temporalField, int i, int i2, SignStyle signStyle) {
            this.field = temporalField;
            this.minWidth = i;
            this.maxWidth = i2;
            this.signStyle = signStyle;
            this.subsequentWidth = null;
        }

        private NumberPrinterParser(TemporalField temporalField, int i, int i2, SignStyle signStyle, int i3) {
            this.field = temporalField;
            this.minWidth = i;
            this.maxWidth = i2;
            this.signStyle = signStyle;
            this.subsequentWidth = i3;
        }

        NumberPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, -1);
        }

        NumberPrinterParser withSubsequentWidth(int i) {
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, this.subsequentWidth + i);
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            Long value = dateTimePrintContext.getValue(this.field);
            int i = 0;
            if (value == null) {
                return false;
            }
            String str;
            long value2 = getValue(dateTimePrintContext, value.longValue());
            dateTimePrintContext = dateTimePrintContext.getSymbols();
            if (value2 == Long.MIN_VALUE) {
                str = "9223372036854775808";
            } else {
                str = Long.toString(Math.abs(value2));
            }
            if (str.length() > this.maxWidth) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Field ");
                stringBuilder.append(this.field);
                stringBuilder.append(" cannot be printed as the value ");
                stringBuilder.append(value2);
                stringBuilder.append(" exceeds the maximum print width of ");
                stringBuilder.append(this.maxWidth);
                throw new DateTimeException(stringBuilder.toString());
            }
            str = dateTimePrintContext.convertNumberToI18N(str);
            if (value2 < 0) {
                switch (this.signStyle) {
                    case EXCEEDS_PAD:
                    case ALWAYS:
                    case NORMAL:
                        stringBuilder.append(dateTimePrintContext.getNegativeSign());
                        break;
                    case NOT_NEGATIVE:
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Field ");
                        stringBuilder.append(this.field);
                        stringBuilder.append(" cannot be printed as the value ");
                        stringBuilder.append(value2);
                        stringBuilder.append(" cannot be negative according to the SignStyle");
                        throw new DateTimeException(stringBuilder.toString());
                    default:
                        break;
                }
            }
            switch (this.signStyle) {
                case EXCEEDS_PAD:
                    if (this.minWidth < 19 && value2 >= ((long) EXCEED_POINTS[this.minWidth])) {
                        stringBuilder.append(dateTimePrintContext.getPositiveSign());
                        break;
                    }
                case ALWAYS:
                    stringBuilder.append(dateTimePrintContext.getPositiveSign());
                    break;
                default:
                    break;
            }
            while (i < this.minWidth - str.length()) {
                stringBuilder.append(dateTimePrintContext.getZeroDigit());
                i++;
            }
            stringBuilder.append(str);
            return true;
        }

        boolean isFixedWidth(DateTimeParseContext dateTimeParseContext) {
            if (this.subsequentWidth != -1) {
                if (this.subsequentWidth <= null || this.minWidth != this.maxWidth || this.signStyle != SignStyle.NOT_NEGATIVE) {
                    return null;
                }
            }
            return true;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int parse(org.threeten.bp.format.DateTimeParseContext r22, java.lang.CharSequence r23, int r24) {
            /*
            r21 = this;
            r6 = r21;
            r0 = r24;
            r1 = r23.length();
            if (r0 != r1) goto L_0x000d;
        L_0x000a:
            r0 = r0 ^ -1;
            return r0;
        L_0x000d:
            r2 = r23.charAt(r24);
            r3 = r22.getSymbols();
            r3 = r3.getPositiveSign();
            r4 = 0;
            r5 = 1;
            if (r2 != r3) goto L_0x003b;
        L_0x001d:
            r2 = r6.signStyle;
            r3 = r22.isStrict();
            r7 = r6.minWidth;
            r8 = r6.maxWidth;
            if (r7 != r8) goto L_0x002b;
        L_0x0029:
            r7 = 1;
            goto L_0x002c;
        L_0x002b:
            r7 = 0;
        L_0x002c:
            r2 = r2.parse(r5, r3, r7);
            if (r2 != 0) goto L_0x0035;
        L_0x0032:
            r0 = r0 ^ -1;
            return r0;
        L_0x0035:
            r0 = r0 + 1;
            r7 = r0;
            r0 = 0;
            r2 = 1;
            goto L_0x0074;
        L_0x003b:
            r3 = r22.getSymbols();
            r3 = r3.getNegativeSign();
            if (r2 != r3) goto L_0x0062;
        L_0x0045:
            r2 = r6.signStyle;
            r3 = r22.isStrict();
            r7 = r6.minWidth;
            r8 = r6.maxWidth;
            if (r7 != r8) goto L_0x0053;
        L_0x0051:
            r7 = 1;
            goto L_0x0054;
        L_0x0053:
            r7 = 0;
        L_0x0054:
            r2 = r2.parse(r4, r3, r7);
            if (r2 != 0) goto L_0x005d;
        L_0x005a:
            r0 = r0 ^ -1;
            return r0;
        L_0x005d:
            r0 = r0 + 1;
            r7 = r0;
            r0 = 1;
            goto L_0x0073;
        L_0x0062:
            r2 = r6.signStyle;
            r3 = org.threeten.bp.format.SignStyle.ALWAYS;
            if (r2 != r3) goto L_0x0071;
        L_0x0068:
            r2 = r22.isStrict();
            if (r2 == 0) goto L_0x0071;
        L_0x006e:
            r0 = r0 ^ -1;
            return r0;
        L_0x0071:
            r7 = r0;
            r0 = 0;
        L_0x0073:
            r2 = 0;
        L_0x0074:
            r3 = r22.isStrict();
            if (r3 != 0) goto L_0x0083;
        L_0x007a:
            r3 = r21.isFixedWidth(r22);
            if (r3 == 0) goto L_0x0081;
        L_0x0080:
            goto L_0x0083;
        L_0x0081:
            r3 = 1;
            goto L_0x0085;
        L_0x0083:
            r3 = r6.minWidth;
        L_0x0085:
            r8 = r7 + r3;
            if (r8 <= r1) goto L_0x008c;
        L_0x0089:
            r0 = r7 ^ -1;
            return r0;
        L_0x008c:
            r9 = r22.isStrict();
            if (r9 != 0) goto L_0x009c;
        L_0x0092:
            r9 = r21.isFixedWidth(r22);
            if (r9 == 0) goto L_0x0099;
        L_0x0098:
            goto L_0x009c;
        L_0x0099:
            r9 = 9;
            goto L_0x009e;
        L_0x009c:
            r9 = r6.maxWidth;
        L_0x009e:
            r10 = r6.subsequentWidth;
            r10 = java.lang.Math.max(r10, r4);
            r9 = r9 + r10;
        L_0x00a5:
            r10 = 0;
            r11 = 2;
            if (r4 >= r11) goto L_0x011d;
        L_0x00a9:
            r9 = r9 + r7;
            r9 = java.lang.Math.min(r9, r1);
            r11 = r10;
            r14 = 0;
            r10 = r7;
        L_0x00b2:
            if (r10 >= r9) goto L_0x0102;
        L_0x00b4:
            r16 = r10 + 1;
            r12 = r23;
            r10 = r12.charAt(r10);
            r13 = r22.getSymbols();
            r10 = r13.convertToDigit(r10);
            if (r10 >= 0) goto L_0x00cd;
        L_0x00c6:
            r10 = r16 + -1;
            if (r10 >= r8) goto L_0x0104;
        L_0x00ca:
            r0 = r7 ^ -1;
            return r0;
        L_0x00cd:
            r13 = r16 - r7;
            r5 = 18;
            if (r13 <= r5) goto L_0x00ed;
        L_0x00d3:
            if (r11 != 0) goto L_0x00d9;
        L_0x00d5:
            r11 = java.math.BigInteger.valueOf(r14);
        L_0x00d9:
            r5 = java.math.BigInteger.TEN;
            r5 = r11.multiply(r5);
            r10 = (long) r10;
            r10 = java.math.BigInteger.valueOf(r10);
            r11 = r5.add(r10);
            r20 = r8;
            r19 = r9;
            goto L_0x00fa;
        L_0x00ed:
            r17 = 10;
            r14 = r14 * r17;
            r20 = r8;
            r19 = r9;
            r8 = (long) r10;
            r17 = r14 + r8;
            r14 = r17;
        L_0x00fa:
            r10 = r16;
            r9 = r19;
            r8 = r20;
            r5 = 1;
            goto L_0x00b2;
        L_0x0102:
            r12 = r23;
        L_0x0104:
            r20 = r8;
            r5 = r6.subsequentWidth;
            if (r5 <= 0) goto L_0x011a;
        L_0x010a:
            if (r4 != 0) goto L_0x011a;
        L_0x010c:
            r10 = r10 - r7;
            r5 = r6.subsequentWidth;
            r10 = r10 - r5;
            r9 = java.lang.Math.max(r3, r10);
            r4 = r4 + 1;
            r8 = r20;
            r5 = 1;
            goto L_0x00a5;
        L_0x011a:
            r5 = r10;
            r10 = r11;
            goto L_0x0120;
        L_0x011d:
            r5 = r7;
            r14 = 0;
        L_0x0120:
            if (r0 == 0) goto L_0x0150;
        L_0x0122:
            if (r10 == 0) goto L_0x013c;
        L_0x0124:
            r0 = java.math.BigInteger.ZERO;
            r0 = r10.equals(r0);
            if (r0 == 0) goto L_0x0137;
        L_0x012c:
            r0 = r22.isStrict();
            if (r0 == 0) goto L_0x0137;
        L_0x0132:
            r0 = 1;
            r7 = r7 - r0;
            r0 = r7 ^ -1;
            return r0;
        L_0x0137:
            r10 = r10.negate();
            goto L_0x0170;
        L_0x013c:
            r0 = 1;
            r1 = 0;
            r3 = (r14 > r1 ? 1 : (r14 == r1 ? 0 : -1));
            if (r3 != 0) goto L_0x014d;
        L_0x0143:
            r1 = r22.isStrict();
            if (r1 == 0) goto L_0x014d;
        L_0x0149:
            r7 = r7 - r0;
            r0 = r7 ^ -1;
            return r0;
        L_0x014d:
            r0 = -r14;
            r2 = r0;
            goto L_0x0171;
        L_0x0150:
            r0 = r6.signStyle;
            r1 = org.threeten.bp.format.SignStyle.EXCEEDS_PAD;
            if (r0 != r1) goto L_0x0170;
        L_0x0156:
            r0 = r22.isStrict();
            if (r0 == 0) goto L_0x0170;
        L_0x015c:
            r0 = r5 - r7;
            if (r2 == 0) goto L_0x0169;
        L_0x0160:
            r1 = r6.minWidth;
            if (r0 > r1) goto L_0x0170;
        L_0x0164:
            r0 = 1;
            r7 = r7 - r0;
            r0 = r7 ^ -1;
            return r0;
        L_0x0169:
            r1 = r6.minWidth;
            if (r0 <= r1) goto L_0x0170;
        L_0x016d:
            r0 = r7 ^ -1;
            return r0;
        L_0x0170:
            r2 = r14;
        L_0x0171:
            if (r10 == 0) goto L_0x0190;
        L_0x0173:
            r0 = r10.bitLength();
            r1 = 63;
            if (r0 <= r1) goto L_0x0183;
        L_0x017b:
            r0 = java.math.BigInteger.TEN;
            r10 = r10.divide(r0);
            r5 = r5 + -1;
        L_0x0183:
            r2 = r10.longValue();
            r0 = r6;
            r1 = r22;
            r4 = r7;
            r0 = r0.setValue(r1, r2, r4, r5);
            return r0;
        L_0x0190:
            r0 = r6;
            r1 = r22;
            r4 = r7;
            r0 = r0.setValue(r1, r2, r4, r5);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.format.DateTimeFormatterBuilder.NumberPrinterParser.parse(org.threeten.bp.format.DateTimeParseContext, java.lang.CharSequence, int):int");
        }

        int setValue(DateTimeParseContext dateTimeParseContext, long j, int i, int i2) {
            return dateTimeParseContext.setParsedField(this.field, j, i, i2);
        }

        public String toString() {
            StringBuilder stringBuilder;
            if (this.minWidth == 1 && this.maxWidth == 19 && this.signStyle == SignStyle.NORMAL) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Value(");
                stringBuilder.append(this.field);
                stringBuilder.append(")");
                return stringBuilder.toString();
            } else if (this.minWidth == this.maxWidth && this.signStyle == SignStyle.NOT_NEGATIVE) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Value(");
                stringBuilder.append(this.field);
                stringBuilder.append(",");
                stringBuilder.append(this.minWidth);
                stringBuilder.append(")");
                return stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Value(");
                stringBuilder.append(this.field);
                stringBuilder.append(",");
                stringBuilder.append(this.minWidth);
                stringBuilder.append(",");
                stringBuilder.append(this.maxWidth);
                stringBuilder.append(",");
                stringBuilder.append(this.signStyle);
                stringBuilder.append(")");
                return stringBuilder.toString();
            }
        }
    }

    static final class OffsetIdPrinterParser implements DateTimePrinterParser {
        static final OffsetIdPrinterParser INSTANCE_ID = new OffsetIdPrinterParser("Z", "+HH:MM:ss");
        static final String[] PATTERNS = new String[]{"+HH", "+HHmm", "+HH:mm", "+HHMM", "+HH:MM", "+HHMMss", "+HH:MM:ss", "+HHMMSS", "+HH:MM:SS"};
        private final String noOffsetText;
        private final int type;

        OffsetIdPrinterParser(String str, String str2) {
            Jdk8Methods.requireNonNull(str, "noOffsetText");
            Jdk8Methods.requireNonNull(str2, "pattern");
            this.noOffsetText = str;
            this.type = checkPattern(str2);
        }

        private int checkPattern(String str) {
            for (int i = 0; i < PATTERNS.length; i++) {
                if (PATTERNS[i].equals(str)) {
                    return i;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid zone offset pattern: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            dateTimePrintContext = dateTimePrintContext.getValue(ChronoField.OFFSET_SECONDS);
            if (dateTimePrintContext == null) {
                return null;
            }
            dateTimePrintContext = Jdk8Methods.safeToInt(dateTimePrintContext.longValue());
            if (dateTimePrintContext == null) {
                stringBuilder.append(this.noOffsetText);
            } else {
                int abs = Math.abs((dateTimePrintContext / 3600) % 100);
                int abs2 = Math.abs((dateTimePrintContext / 60) % 60);
                int abs3 = Math.abs(dateTimePrintContext % 60);
                int length = stringBuilder.length();
                stringBuilder.append(dateTimePrintContext < null ? "-" : "+");
                stringBuilder.append((char) ((abs / 10) + 48));
                stringBuilder.append((char) ((abs % 10) + 48));
                if (this.type >= 3 || (this.type >= 1 && abs2 > 0)) {
                    stringBuilder.append(this.type % 2 == null ? ":" : "");
                    stringBuilder.append((char) ((abs2 / 10) + 48));
                    stringBuilder.append((char) ((abs2 % 10) + 48));
                    abs += abs2;
                    if (this.type >= 7 || (this.type >= 5 && abs3 > 0)) {
                        stringBuilder.append(this.type % 2 == null ? ":" : "");
                        stringBuilder.append((char) ((abs3 / 10) + 48));
                        stringBuilder.append((char) ((abs3 % 10) + 48));
                        abs += abs3;
                    }
                }
                if (abs == 0) {
                    stringBuilder.setLength(length);
                    stringBuilder.append(this.noOffsetText);
                }
            }
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            CharSequence charSequence2 = charSequence;
            int i2 = i;
            int length = charSequence.length();
            int length2 = this.noOffsetText.length();
            if (length2 == 0) {
                if (i2 == length) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0, i2, i2);
                }
            } else if (i2 == length) {
                return i2 ^ -1;
            } else {
                if (dateTimeParseContext.subSequenceEquals(charSequence2, i2, r0.noOffsetText, 0, length2)) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0, i2, i2 + length2);
                }
            }
            char charAt = charSequence.charAt(i);
            if (charAt == '+' || charAt == '-') {
                Object obj;
                length = charAt == '-' ? -1 : 1;
                int[] iArr = new int[4];
                iArr[0] = i2 + 1;
                if (!parseNumber(iArr, 1, charSequence2, true)) {
                    if (!parseNumber(iArr, 2, charSequence2, r0.type >= 3)) {
                        if (!parseNumber(iArr, 3, charSequence2, false)) {
                            obj = null;
                            if (obj == null) {
                                return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, (((((long) iArr[1]) * 3600) + (((long) iArr[2]) * 60)) + ((long) iArr[3])) * ((long) length), i2, iArr[0]);
                            }
                        }
                    }
                }
                obj = 1;
                if (obj == null) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, (((((long) iArr[1]) * 3600) + (((long) iArr[2]) * 60)) + ((long) iArr[3])) * ((long) length), i2, iArr[0]);
                }
            }
            if (length2 != 0) {
                return i2 ^ -1;
            }
            return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0, i2, i2 + length2);
        }

        private boolean parseNumber(int[] iArr, int i, CharSequence charSequence, boolean z) {
            if ((this.type + 3) / 2 < i) {
                return false;
            }
            int i2;
            int i3 = iArr[0];
            if (this.type % 2 == 0 && i > 1) {
                i2 = i3 + 1;
                if (i2 <= charSequence.length()) {
                    if (charSequence.charAt(i3) == ':') {
                        i3 = i2;
                    }
                }
                return z;
            }
            if (i3 + 2 > charSequence.length()) {
                return z;
            }
            i2 = i3 + 1;
            char charAt = charSequence.charAt(i3);
            int i4 = i2 + 1;
            charSequence = charSequence.charAt(i2);
            if (charAt >= '0' && charAt <= '9' && charSequence >= 48) {
                if (charSequence <= 57) {
                    i3 = ((charAt - 48) * 10) + (charSequence - 48);
                    if (i3 >= 0) {
                        if (i3 <= 59) {
                            iArr[i] = i3;
                            iArr[0] = i4;
                            return false;
                        }
                    }
                    return z;
                }
            }
            return z;
        }

        public String toString() {
            String replace = this.noOffsetText.replace("'", "''");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Offset(");
            stringBuilder.append(PATTERNS[this.type]);
            stringBuilder.append(",'");
            stringBuilder.append(replace);
            stringBuilder.append("')");
            return stringBuilder.toString();
        }
    }

    static final class PadPrinterParserDecorator implements DateTimePrinterParser {
        private final char padChar;
        private final int padWidth;
        private final DateTimePrinterParser printerParser;

        PadPrinterParserDecorator(DateTimePrinterParser dateTimePrinterParser, int i, char c) {
            this.printerParser = dateTimePrinterParser;
            this.padWidth = i;
            this.padChar = c;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            int length = stringBuilder.length();
            int i = 0;
            if (this.printerParser.print(dateTimePrintContext, stringBuilder) == null) {
                return false;
            }
            dateTimePrintContext = stringBuilder.length() - length;
            if (dateTimePrintContext > this.padWidth) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot print as output of ");
                stringBuilder2.append(dateTimePrintContext);
                stringBuilder2.append(" characters exceeds pad width of ");
                stringBuilder2.append(this.padWidth);
                throw new DateTimeException(stringBuilder2.toString());
            }
            while (i < this.padWidth - dateTimePrintContext) {
                stringBuilder.insert(length, this.padChar);
                i++;
            }
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            boolean isStrict = dateTimeParseContext.isStrict();
            boolean isCaseSensitive = dateTimeParseContext.isCaseSensitive();
            if (i > charSequence.length()) {
                throw new IndexOutOfBoundsException();
            } else if (i == charSequence.length()) {
                return i ^ -1;
            } else {
                int i2 = this.padWidth + i;
                if (i2 > charSequence.length()) {
                    if (isStrict) {
                        return i ^ -1;
                    }
                    i2 = charSequence.length();
                }
                int i3 = i;
                while (i3 < i2) {
                    if (!isCaseSensitive) {
                        if (!dateTimeParseContext.charEquals(charSequence.charAt(i3), this.padChar)) {
                            break;
                        }
                    } else if (charSequence.charAt(i3) != this.padChar) {
                        break;
                    }
                    i3++;
                }
                dateTimeParseContext = this.printerParser.parse(dateTimeParseContext, charSequence.subSequence(0, i2), i3);
                return (dateTimeParseContext == i2 || !isStrict) ? dateTimeParseContext : (i + i3) ^ -1;
            }
        }

        public String toString() {
            String str;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Pad(");
            stringBuilder.append(this.printerParser);
            stringBuilder.append(",");
            stringBuilder.append(this.padWidth);
            if (this.padChar == ' ') {
                str = ")";
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(",'");
                stringBuilder2.append(this.padChar);
                stringBuilder2.append("')");
                str = stringBuilder2.toString();
            }
            stringBuilder.append(str);
            return stringBuilder.toString();
        }
    }

    static final class ReducedPrinterParser extends NumberPrinterParser {
        static final LocalDate BASE_DATE = LocalDate.of(2000, 1, 1);
        private final ChronoLocalDate baseDate;
        private final int baseValue;

        ReducedPrinterParser(TemporalField temporalField, int i, int i2, int i3, ChronoLocalDate chronoLocalDate) {
            super(temporalField, i, i2, SignStyle.NOT_NEGATIVE);
            if (i >= 1) {
                if (i <= 10) {
                    if (i2 >= 1) {
                        if (i2 <= 10) {
                            if (i2 < i) {
                                throw new IllegalArgumentException("The maxWidth must be greater than the width");
                            }
                            if (chronoLocalDate == null) {
                                long j = (long) i3;
                                if (temporalField.range().isValidValue(j) == null) {
                                    throw new IllegalArgumentException("The base value must be within the range of the field");
                                } else if (j + ((long) EXCEED_POINTS[i]) > Integer.MAX_VALUE) {
                                    throw new DateTimeException("Unable to add printer-parser as the range exceeds the capacity of an int");
                                }
                            }
                            this.baseValue = i3;
                            this.baseDate = chronoLocalDate;
                            return;
                        }
                    }
                    i = new StringBuilder();
                    i.append("The maxWidth must be from 1 to 10 inclusive but was ");
                    i.append(i2);
                    throw new IllegalArgumentException(i.toString());
                }
            }
            i2 = new StringBuilder();
            i2.append("The width must be from 1 to 10 inclusive but was ");
            i2.append(i);
            throw new IllegalArgumentException(i2.toString());
        }

        private ReducedPrinterParser(TemporalField temporalField, int i, int i2, int i3, ChronoLocalDate chronoLocalDate, int i4) {
            super(temporalField, i, i2, SignStyle.NOT_NEGATIVE, i4);
            this.baseValue = i3;
            this.baseDate = chronoLocalDate;
        }

        long getValue(DateTimePrintContext dateTimePrintContext, long j) {
            long abs = Math.abs(j);
            int i = this.baseValue;
            if (this.baseDate != null) {
                i = Chronology.from(dateTimePrintContext.getTemporal()).date(this.baseDate).get(this.field);
            }
            if (j < ((long) i) || j >= ((long) (i + EXCEED_POINTS[this.minWidth]))) {
                return abs % ((long) EXCEED_POINTS[this.maxWidth]);
            }
            return abs % ((long) EXCEED_POINTS[this.minWidth]);
        }

        int setValue(DateTimeParseContext dateTimeParseContext, long j, int i, int i2) {
            int i3;
            long j2;
            int i4 = this.baseValue;
            if (this.baseDate != null) {
                i3 = dateTimeParseContext.getEffectiveChronology().date(r6.baseDate).get(r6.field);
                dateTimeParseContext.addChronologyChangedParser(r6, j, i, i2);
            } else {
                i3 = i4;
            }
            if (i2 - i != r6.minWidth || j < 0) {
                j2 = j;
            } else {
                long j3 = (long) EXCEED_POINTS[r6.minWidth];
                j2 = (long) i3;
                long j4 = j2 - (j2 % j3);
                long j5 = i3 > 0 ? j4 + j : j4 - j;
                j2 = j5 < j2 ? j5 + j3 : j5;
            }
            return dateTimeParseContext.setParsedField(r6.field, j2, i, i2);
        }

        NumberPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, -1);
        }

        ReducedPrinterParser withSubsequentWidth(int i) {
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, this.subsequentWidth + i);
        }

        boolean isFixedWidth(DateTimeParseContext dateTimeParseContext) {
            if (dateTimeParseContext.isStrict()) {
                return super.isFixedWidth(dateTimeParseContext);
            }
            return null;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ReducedValue(");
            stringBuilder.append(this.field);
            stringBuilder.append(",");
            stringBuilder.append(this.minWidth);
            stringBuilder.append(",");
            stringBuilder.append(this.maxWidth);
            stringBuilder.append(",");
            stringBuilder.append(this.baseDate != null ? this.baseDate : Integer.valueOf(this.baseValue));
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    enum SettingsParser implements DateTimePrinterParser {
        SENSITIVE,
        INSENSITIVE,
        STRICT,
        LENIENT;

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            switch (ordinal()) {
                case null:
                    dateTimeParseContext.setCaseSensitive(true);
                    break;
                case 1:
                    dateTimeParseContext.setCaseSensitive(false);
                    break;
                case 2:
                    dateTimeParseContext.setStrict(true);
                    break;
                case 3:
                    dateTimeParseContext.setStrict(false);
                    break;
                default:
                    break;
            }
            return i;
        }

        public String toString() {
            switch (ordinal()) {
                case 0:
                    return "ParseCaseSensitive(true)";
                case 1:
                    return "ParseCaseSensitive(false)";
                case 2:
                    return "ParseStrict(true)";
                case 3:
                    return "ParseStrict(false)";
                default:
                    throw new IllegalStateException("Unreachable");
            }
        }
    }

    static final class StringLiteralPrinterParser implements DateTimePrinterParser {
        private final String literal;

        StringLiteralPrinterParser(String str) {
            this.literal = str;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            stringBuilder.append(this.literal);
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (i <= charSequence.length()) {
                if (i >= 0) {
                    if (dateTimeParseContext.subSequenceEquals(charSequence, i, this.literal, 0, this.literal.length()) == null) {
                        return i ^ -1;
                    }
                    return i + this.literal.length();
                }
            }
            throw new IndexOutOfBoundsException();
        }

        public String toString() {
            String replace = this.literal.replace("'", "''");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("'");
            stringBuilder.append(replace);
            stringBuilder.append("'");
            return stringBuilder.toString();
        }
    }

    static final class TextPrinterParser implements DateTimePrinterParser {
        private final TemporalField field;
        private volatile NumberPrinterParser numberPrinterParser;
        private final DateTimeTextProvider provider;
        private final TextStyle textStyle;

        TextPrinterParser(TemporalField temporalField, TextStyle textStyle, DateTimeTextProvider dateTimeTextProvider) {
            this.field = temporalField;
            this.textStyle = textStyle;
            this.provider = dateTimeTextProvider;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return null;
            }
            String text = this.provider.getText(this.field, value.longValue(), this.textStyle, dateTimePrintContext.getLocale());
            if (text == null) {
                return numberPrinterParser().print(dateTimePrintContext, stringBuilder);
            }
            stringBuilder.append(text);
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int length = charSequence.length();
            if (i >= 0) {
                if (i <= length) {
                    Iterator textIterator = this.provider.getTextIterator(this.field, dateTimeParseContext.isStrict() ? this.textStyle : null, dateTimeParseContext.getLocale());
                    if (textIterator != null) {
                        while (textIterator.hasNext()) {
                            Entry entry = (Entry) textIterator.next();
                            String str = (String) entry.getKey();
                            if (dateTimeParseContext.subSequenceEquals(str, 0, charSequence, i, str.length())) {
                                return dateTimeParseContext.setParsedField(this.field, ((Long) entry.getValue()).longValue(), i, i + str.length());
                            }
                        }
                        if (dateTimeParseContext.isStrict()) {
                            return i ^ -1;
                        }
                    }
                    return numberPrinterParser().parse(dateTimeParseContext, charSequence, i);
                }
            }
            throw new IndexOutOfBoundsException();
        }

        private NumberPrinterParser numberPrinterParser() {
            if (this.numberPrinterParser == null) {
                this.numberPrinterParser = new NumberPrinterParser(this.field, 1, 19, SignStyle.NORMAL);
            }
            return this.numberPrinterParser;
        }

        public String toString() {
            if (this.textStyle == TextStyle.FULL) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Text(");
                stringBuilder.append(this.field);
                stringBuilder.append(")");
                return stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Text(");
            stringBuilder.append(this.field);
            stringBuilder.append(",");
            stringBuilder.append(this.textStyle);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static final class WeekFieldsPrinterParser implements DateTimePrinterParser {
        private final int count;
        private final char letter;

        public WeekFieldsPrinterParser(char c, int i) {
            this.letter = c;
            this.count = i;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            return evaluate(WeekFields.of(dateTimePrintContext.getLocale())).print(dateTimePrintContext, stringBuilder);
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            return evaluate(WeekFields.of(dateTimeParseContext.getLocale())).parse(dateTimeParseContext, charSequence, i);
        }

        private DateTimePrinterParser evaluate(WeekFields weekFields) {
            char c = this.letter;
            if (c == 'W') {
                return new NumberPrinterParser(weekFields.weekOfMonth(), 1, 2, SignStyle.NOT_NEGATIVE);
            }
            if (c != 'Y') {
                if (c == 'c') {
                    return new NumberPrinterParser(weekFields.dayOfWeek(), r0.count, 2, SignStyle.NOT_NEGATIVE);
                }
                if (c == 'e') {
                    return new NumberPrinterParser(weekFields.dayOfWeek(), r0.count, 2, SignStyle.NOT_NEGATIVE);
                }
                if (c != 'w') {
                    return null;
                }
                return new NumberPrinterParser(weekFields.weekOfWeekBasedYear(), r0.count, 2, SignStyle.NOT_NEGATIVE);
            } else if (r0.count == 2) {
                return new ReducedPrinterParser(weekFields.weekBasedYear(), 2, 2, 0, ReducedPrinterParser.BASE_DATE);
            } else {
                return new NumberPrinterParser(weekFields.weekBasedYear(), r0.count, 19, r0.count < 4 ? SignStyle.NORMAL : SignStyle.EXCEEDS_PAD, -1);
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(30);
            stringBuilder.append("Localized(");
            if (this.letter != 'Y') {
                if (this.letter != 'c') {
                    if (this.letter != 'e') {
                        if (this.letter == 'w') {
                            stringBuilder.append("WeekOfWeekBasedYear");
                        } else if (this.letter == 'W') {
                            stringBuilder.append("WeekOfMonth");
                        }
                        stringBuilder.append(",");
                        stringBuilder.append(this.count);
                    }
                }
                stringBuilder.append("DayOfWeek");
                stringBuilder.append(",");
                stringBuilder.append(this.count);
            } else if (this.count == 1) {
                stringBuilder.append("WeekBasedYear");
            } else if (this.count == 2) {
                stringBuilder.append("ReducedValue(WeekBasedYear,2,2,2000-01-01)");
            } else {
                stringBuilder.append("WeekBasedYear,");
                stringBuilder.append(this.count);
                stringBuilder.append(",");
                stringBuilder.append(19);
                stringBuilder.append(",");
                stringBuilder.append(this.count < 4 ? SignStyle.NORMAL : SignStyle.EXCEEDS_PAD);
            }
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static final class ZoneIdPrinterParser implements DateTimePrinterParser {
        private static volatile Entry<Integer, SubstringTree> cachedSubstringTree;
        private final String description;
        private final TemporalQuery<ZoneId> query;

        private static final class SubstringTree {
            final int length;
            private final Map<CharSequence, SubstringTree> substringMap;
            private final Map<String, SubstringTree> substringMapCI;

            private SubstringTree(int i) {
                this.substringMap = new HashMap();
                this.substringMapCI = new HashMap();
                this.length = i;
            }

            private SubstringTree get(CharSequence charSequence, boolean z) {
                if (z) {
                    return (SubstringTree) this.substringMap.get(charSequence);
                }
                return (SubstringTree) this.substringMapCI.get(charSequence.toString().toLowerCase(Locale.ENGLISH));
            }

            private void add(String str) {
                int length = str.length();
                if (length == this.length) {
                    this.substringMap.put(str, null);
                    this.substringMapCI.put(str.toLowerCase(Locale.ENGLISH), null);
                } else if (length > this.length) {
                    String substring = str.substring(0, this.length);
                    SubstringTree substringTree = (SubstringTree) this.substringMap.get(substring);
                    if (substringTree == null) {
                        substringTree = new SubstringTree(length);
                        this.substringMap.put(substring, substringTree);
                        this.substringMapCI.put(substring.toLowerCase(Locale.ENGLISH), substringTree);
                    }
                    substringTree.add(str);
                }
            }
        }

        ZoneIdPrinterParser(TemporalQuery<ZoneId> temporalQuery, String str) {
            this.query = temporalQuery;
            this.description = str;
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            ZoneId zoneId = (ZoneId) dateTimePrintContext.getValue(this.query);
            if (zoneId == null) {
                return null;
            }
            stringBuilder.append(zoneId.getId());
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int length = charSequence.length();
            if (i > length) {
                throw new IndexOutOfBoundsException();
            } else if (i == length) {
                return i ^ -1;
            } else {
                char charAt = charSequence.charAt(i);
                if (charAt != '+') {
                    if (charAt != '-') {
                        int i2 = i + 2;
                        if (length >= i2) {
                            char charAt2 = charSequence.charAt(i + 1);
                            if (dateTimeParseContext.charEquals(charAt, 'U') && dateTimeParseContext.charEquals(charAt2, 'T')) {
                                int i3 = i + 3;
                                if (length < i3 || !dateTimeParseContext.charEquals(charSequence.charAt(i2), 'C')) {
                                    return parsePrefixedOffset(dateTimeParseContext, charSequence, i, i2);
                                }
                                return parsePrefixedOffset(dateTimeParseContext, charSequence, i, i3);
                            } else if (dateTimeParseContext.charEquals(charAt, 'G')) {
                                int i4 = i + 3;
                                if (length >= i4 && dateTimeParseContext.charEquals(charAt2, 'M') && dateTimeParseContext.charEquals(charSequence.charAt(i2), 'T')) {
                                    return parsePrefixedOffset(dateTimeParseContext, charSequence, i, i4);
                                }
                            }
                        }
                        Set availableZoneIds = ZoneRulesProvider.getAvailableZoneIds();
                        int size = availableZoneIds.size();
                        Entry entry = cachedSubstringTree;
                        if (entry == null || ((Integer) entry.getKey()).intValue() != size) {
                            synchronized (this) {
                                entry = cachedSubstringTree;
                                if (entry == null || ((Integer) entry.getKey()).intValue() != size) {
                                    entry = new SimpleImmutableEntry(Integer.valueOf(size), prepareParser(availableZoneIds));
                                    cachedSubstringTree = entry;
                                }
                            }
                        }
                        SubstringTree substringTree = (SubstringTree) entry.getValue();
                        String str = null;
                        String str2 = null;
                        while (substringTree != null) {
                            int i5 = substringTree.length + i;
                            if (i5 > length) {
                                break;
                            }
                            CharSequence obj = charSequence.subSequence(i, i5).toString();
                            substringTree = substringTree.get(obj, dateTimeParseContext.isCaseSensitive());
                            CharSequence charSequence2 = obj;
                            str2 = str;
                            CharSequence charSequence3 = charSequence2;
                        }
                        ZoneId convertToZone = convertToZone(availableZoneIds, str, dateTimeParseContext.isCaseSensitive());
                        if (convertToZone == null) {
                            convertToZone = convertToZone(availableZoneIds, str2, dateTimeParseContext.isCaseSensitive());
                            if (convertToZone != null) {
                                str = str2;
                            } else if (dateTimeParseContext.charEquals(charAt, 'Z') == null) {
                                return i ^ -1;
                            } else {
                                dateTimeParseContext.setParsed(ZoneOffset.UTC);
                                return i + 1;
                            }
                        }
                        dateTimeParseContext.setParsed(convertToZone);
                        return i + str.length();
                    }
                }
                DateTimeParseContext copy = dateTimeParseContext.copy();
                charSequence = OffsetIdPrinterParser.INSTANCE_ID.parse(copy, charSequence, i);
                if (charSequence < null) {
                    return charSequence;
                }
                dateTimeParseContext.setParsed(ZoneOffset.ofTotalSeconds((int) copy.getParsed(ChronoField.OFFSET_SECONDS).longValue()));
                return charSequence;
            }
        }

        private ZoneId convertToZone(Set<String> set, String str, boolean z) {
            ZoneId zoneId = null;
            if (str == null) {
                return null;
            }
            if (z) {
                if (set.contains(str) != null) {
                    zoneId = ZoneId.of(str);
                }
                return zoneId;
            }
            for (String str2 : set) {
                if (str2.equalsIgnoreCase(str)) {
                    return ZoneId.of(str2);
                }
            }
            return null;
        }

        private int parsePrefixedOffset(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i, int i2) {
            i = charSequence.subSequence(i, i2).toString().toUpperCase();
            DateTimeParseContext copy = dateTimeParseContext.copy();
            if (i2 >= charSequence.length() || !dateTimeParseContext.charEquals(charSequence.charAt(i2), 'Z')) {
                charSequence = OffsetIdPrinterParser.INSTANCE_ID.parse(copy, charSequence, i2);
                if (charSequence < null) {
                    dateTimeParseContext.setParsed(ZoneId.ofOffset(i, ZoneOffset.UTC));
                    return i2;
                }
                dateTimeParseContext.setParsed(ZoneId.ofOffset(i, ZoneOffset.ofTotalSeconds((int) copy.getParsed(ChronoField.OFFSET_SECONDS).longValue())));
                return charSequence;
            }
            dateTimeParseContext.setParsed(ZoneId.ofOffset(i, ZoneOffset.UTC));
            return i2;
        }

        private static SubstringTree prepareParser(Set<String> set) {
            List<String> arrayList = new ArrayList(set);
            Collections.sort(arrayList, DateTimeFormatterBuilder.LENGTH_SORT);
            set = new SubstringTree(((String) arrayList.get(0)).length());
            for (String access$300 : arrayList) {
                set.add(access$300);
            }
            return set;
        }

        public String toString() {
            return this.description;
        }
    }

    static final class ZoneTextPrinterParser implements DateTimePrinterParser {
        private static final Comparator<String> LENGTH_COMPARATOR = new C15591();
        private final TextStyle textStyle;

        /* renamed from: org.threeten.bp.format.DateTimeFormatterBuilder$ZoneTextPrinterParser$1 */
        static class C15591 implements Comparator<String> {
            C15591() {
            }

            public int compare(String str, String str2) {
                int length = str2.length() - str.length();
                return length == 0 ? str.compareTo(str2) : length;
            }
        }

        ZoneTextPrinterParser(TextStyle textStyle) {
            this.textStyle = (TextStyle) Jdk8Methods.requireNonNull(textStyle, "textStyle");
        }

        public boolean print(DateTimePrintContext dateTimePrintContext, StringBuilder stringBuilder) {
            ZoneId zoneId = (ZoneId) dateTimePrintContext.getValue(TemporalQueries.zoneId());
            int i = 0;
            if (zoneId == null) {
                return false;
            }
            if (zoneId.normalized() instanceof ZoneOffset) {
                stringBuilder.append(zoneId.getId());
                return true;
            }
            boolean isDaylightSavings;
            TemporalAccessor temporal = dateTimePrintContext.getTemporal();
            if (temporal.isSupported(ChronoField.INSTANT_SECONDS)) {
                isDaylightSavings = zoneId.getRules().isDaylightSavings(Instant.ofEpochSecond(temporal.getLong(ChronoField.INSTANT_SECONDS)));
            } else {
                isDaylightSavings = false;
            }
            TimeZone timeZone = TimeZone.getTimeZone(zoneId.getId());
            if (this.textStyle.asNormal() == TextStyle.FULL) {
                i = 1;
            }
            stringBuilder.append(timeZone.getDisplayName(isDaylightSavings, i, dateTimePrintContext.getLocale()));
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            String str;
            Map treeMap = new TreeMap(LENGTH_COMPARATOR);
            for (String str2 : ZoneId.getAvailableZoneIds()) {
                treeMap.put(str2, str2);
                TimeZone timeZone = TimeZone.getTimeZone(str2);
                int i2 = this.textStyle.asNormal() == TextStyle.FULL ? 1 : 0;
                treeMap.put(timeZone.getDisplayName(false, i2, dateTimeParseContext.getLocale()), str2);
                treeMap.put(timeZone.getDisplayName(true, i2, dateTimeParseContext.getLocale()), str2);
            }
            for (Entry entry : treeMap.entrySet()) {
                str2 = (String) entry.getKey();
                if (dateTimeParseContext.subSequenceEquals(charSequence, i, str2, 0, str2.length())) {
                    dateTimeParseContext.setParsed(ZoneId.of((String) entry.getValue()));
                    return i + str2.length();
                }
            }
            return i ^ -1;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ZoneText(");
            stringBuilder.append(this.textStyle);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static {
        FIELD_MAP.put(Character.valueOf('G'), ChronoField.ERA);
        FIELD_MAP.put(Character.valueOf('y'), ChronoField.YEAR_OF_ERA);
        FIELD_MAP.put(Character.valueOf('u'), ChronoField.YEAR);
        FIELD_MAP.put(Character.valueOf('Q'), IsoFields.QUARTER_OF_YEAR);
        FIELD_MAP.put(Character.valueOf('q'), IsoFields.QUARTER_OF_YEAR);
        FIELD_MAP.put(Character.valueOf('M'), ChronoField.MONTH_OF_YEAR);
        FIELD_MAP.put(Character.valueOf('L'), ChronoField.MONTH_OF_YEAR);
        FIELD_MAP.put(Character.valueOf('D'), ChronoField.DAY_OF_YEAR);
        FIELD_MAP.put(Character.valueOf('d'), ChronoField.DAY_OF_MONTH);
        FIELD_MAP.put(Character.valueOf('F'), ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        FIELD_MAP.put(Character.valueOf('E'), ChronoField.DAY_OF_WEEK);
        FIELD_MAP.put(Character.valueOf('c'), ChronoField.DAY_OF_WEEK);
        FIELD_MAP.put(Character.valueOf('e'), ChronoField.DAY_OF_WEEK);
        FIELD_MAP.put(Character.valueOf('a'), ChronoField.AMPM_OF_DAY);
        FIELD_MAP.put(Character.valueOf('H'), ChronoField.HOUR_OF_DAY);
        FIELD_MAP.put(Character.valueOf('k'), ChronoField.CLOCK_HOUR_OF_DAY);
        FIELD_MAP.put(Character.valueOf('K'), ChronoField.HOUR_OF_AMPM);
        FIELD_MAP.put(Character.valueOf('h'), ChronoField.CLOCK_HOUR_OF_AMPM);
        FIELD_MAP.put(Character.valueOf('m'), ChronoField.MINUTE_OF_HOUR);
        FIELD_MAP.put(Character.valueOf('s'), ChronoField.SECOND_OF_MINUTE);
        FIELD_MAP.put(Character.valueOf('S'), ChronoField.NANO_OF_SECOND);
        FIELD_MAP.put(Character.valueOf('A'), ChronoField.MILLI_OF_DAY);
        FIELD_MAP.put(Character.valueOf('n'), ChronoField.NANO_OF_SECOND);
        FIELD_MAP.put(Character.valueOf('N'), ChronoField.NANO_OF_DAY);
    }

    public static String getLocalizedDateTimePattern(FormatStyle formatStyle, FormatStyle formatStyle2, Chronology chronology, Locale locale) {
        Jdk8Methods.requireNonNull(locale, "locale");
        Jdk8Methods.requireNonNull(chronology, "chrono");
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either dateStyle or timeStyle must be non-null");
        }
        if (formatStyle == null) {
            formatStyle = DateFormat.getTimeInstance(formatStyle2.ordinal(), locale);
        } else if (formatStyle2 != null) {
            formatStyle = DateFormat.getDateTimeInstance(formatStyle.ordinal(), formatStyle2.ordinal(), locale);
        } else {
            formatStyle = DateFormat.getDateInstance(formatStyle.ordinal(), locale);
        }
        if ((formatStyle instanceof SimpleDateFormat) != null) {
            return ((SimpleDateFormat) formatStyle).toPattern();
        }
        throw new IllegalArgumentException("Unable to determine pattern");
    }

    public DateTimeFormatterBuilder() {
        this.active = this;
        this.printerParsers = new ArrayList();
        this.valueParserIndex = -1;
        this.parent = null;
        this.optional = false;
    }

    private DateTimeFormatterBuilder(DateTimeFormatterBuilder dateTimeFormatterBuilder, boolean z) {
        this.active = this;
        this.printerParsers = new ArrayList();
        this.valueParserIndex = -1;
        this.parent = dateTimeFormatterBuilder;
        this.optional = z;
    }

    public DateTimeFormatterBuilder parseCaseSensitive() {
        appendInternal(SettingsParser.SENSITIVE);
        return this;
    }

    public DateTimeFormatterBuilder parseCaseInsensitive() {
        appendInternal(SettingsParser.INSENSITIVE);
        return this;
    }

    public DateTimeFormatterBuilder parseStrict() {
        appendInternal(SettingsParser.STRICT);
        return this;
    }

    public DateTimeFormatterBuilder parseLenient() {
        appendInternal(SettingsParser.LENIENT);
        return this;
    }

    public DateTimeFormatterBuilder parseDefaulting(TemporalField temporalField, long j) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        appendInternal(new DefaultingParser(temporalField, j));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        appendValue(new NumberPrinterParser(temporalField, 1, 19, SignStyle.NORMAL));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField, int i) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        if (i >= 1) {
            if (i <= 19) {
                appendValue(new NumberPrinterParser(temporalField, i, i, SignStyle.NOT_NEGATIVE));
                return this;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The width must be from 1 to 19 inclusive but was ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField, int i, int i2, SignStyle signStyle) {
        if (i == i2 && signStyle == SignStyle.NOT_NEGATIVE) {
            return appendValue(temporalField, i2);
        }
        Jdk8Methods.requireNonNull(temporalField, "field");
        Jdk8Methods.requireNonNull(signStyle, "signStyle");
        if (i >= 1) {
            if (i <= 19) {
                if (i2 >= 1) {
                    if (i2 <= 19) {
                        if (i2 < i) {
                            signStyle = new StringBuilder();
                            signStyle.append("The maximum width must exceed or equal the minimum width but ");
                            signStyle.append(i2);
                            signStyle.append(" < ");
                            signStyle.append(i);
                            throw new IllegalArgumentException(signStyle.toString());
                        }
                        appendValue(new NumberPrinterParser(temporalField, i, i2, signStyle));
                        return this;
                    }
                }
                i = new StringBuilder();
                i.append("The maximum width must be from 1 to 19 inclusive but was ");
                i.append(i2);
                throw new IllegalArgumentException(i.toString());
            }
        }
        i2 = new StringBuilder();
        i2.append("The minimum width must be from 1 to 19 inclusive but was ");
        i2.append(i);
        throw new IllegalArgumentException(i2.toString());
    }

    public DateTimeFormatterBuilder appendValueReduced(TemporalField temporalField, int i, int i2, int i3) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        appendValue(new ReducedPrinterParser(temporalField, i, i2, i3, null));
        return this;
    }

    public DateTimeFormatterBuilder appendValueReduced(TemporalField temporalField, int i, int i2, ChronoLocalDate chronoLocalDate) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        Jdk8Methods.requireNonNull(chronoLocalDate, "baseDate");
        appendValue(new ReducedPrinterParser(temporalField, i, i2, 0, chronoLocalDate));
        return this;
    }

    private DateTimeFormatterBuilder appendValue(NumberPrinterParser numberPrinterParser) {
        if (this.active.valueParserIndex < 0 || !(this.active.printerParsers.get(this.active.valueParserIndex) instanceof NumberPrinterParser)) {
            this.active.valueParserIndex = appendInternal(numberPrinterParser);
        } else {
            Object withSubsequentWidth;
            int i = this.active.valueParserIndex;
            NumberPrinterParser numberPrinterParser2 = (NumberPrinterParser) this.active.printerParsers.get(i);
            if (numberPrinterParser.minWidth == numberPrinterParser.maxWidth && numberPrinterParser.signStyle == SignStyle.NOT_NEGATIVE) {
                withSubsequentWidth = numberPrinterParser2.withSubsequentWidth(numberPrinterParser.maxWidth);
                appendInternal(numberPrinterParser.withFixedWidth());
                this.active.valueParserIndex = i;
            } else {
                withSubsequentWidth = numberPrinterParser2.withFixedWidth();
                this.active.valueParserIndex = appendInternal(numberPrinterParser);
            }
            this.active.printerParsers.set(i, withSubsequentWidth);
        }
        return this;
    }

    public DateTimeFormatterBuilder appendFraction(TemporalField temporalField, int i, int i2, boolean z) {
        appendInternal(new FractionPrinterParser(temporalField, i, i2, z));
        return this;
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField) {
        return appendText(temporalField, TextStyle.FULL);
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField, TextStyle textStyle) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        Jdk8Methods.requireNonNull(textStyle, "textStyle");
        appendInternal(new TextPrinterParser(temporalField, textStyle, DateTimeTextProvider.getInstance()));
        return this;
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField, Map<Long, String> map) {
        Jdk8Methods.requireNonNull(temporalField, "field");
        Jdk8Methods.requireNonNull(map, "textLookup");
        final LocaleStore localeStore = new LocaleStore(Collections.singletonMap(TextStyle.FULL, new LinkedHashMap(map)));
        appendInternal(new TextPrinterParser(temporalField, TextStyle.FULL, new DateTimeTextProvider() {
            public String getText(TemporalField temporalField, long j, TextStyle textStyle, Locale locale) {
                return localeStore.getText(j, textStyle);
            }

            public Iterator<Entry<String, Long>> getTextIterator(TemporalField temporalField, TextStyle textStyle, Locale locale) {
                return localeStore.getTextIterator(textStyle);
            }
        }));
        return this;
    }

    public DateTimeFormatterBuilder appendInstant() {
        appendInternal(new InstantPrinterParser(-2));
        return this;
    }

    public DateTimeFormatterBuilder appendInstant(int i) {
        if (i >= -1) {
            if (i <= 9) {
                appendInternal(new InstantPrinterParser(i));
                return this;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid fractional digits: ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public DateTimeFormatterBuilder appendOffsetId() {
        appendInternal(OffsetIdPrinterParser.INSTANCE_ID);
        return this;
    }

    public DateTimeFormatterBuilder appendOffset(String str, String str2) {
        appendInternal(new OffsetIdPrinterParser(str2, str));
        return this;
    }

    public DateTimeFormatterBuilder appendLocalizedOffset(TextStyle textStyle) {
        Jdk8Methods.requireNonNull(textStyle, TtmlNode.TAG_STYLE);
        if (textStyle == TextStyle.FULL || textStyle == TextStyle.SHORT) {
            appendInternal(new LocalizedOffsetPrinterParser(textStyle));
            return this;
        }
        throw new IllegalArgumentException("Style must be either full or short");
    }

    public DateTimeFormatterBuilder appendZoneId() {
        appendInternal(new ZoneIdPrinterParser(TemporalQueries.zoneId(), "ZoneId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneRegionId() {
        appendInternal(new ZoneIdPrinterParser(QUERY_REGION_ONLY, "ZoneRegionId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneOrOffsetId() {
        appendInternal(new ZoneIdPrinterParser(TemporalQueries.zone(), "ZoneOrOffsetId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneText(TextStyle textStyle) {
        appendInternal(new ZoneTextPrinterParser(textStyle));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneText(TextStyle textStyle, Set<ZoneId> set) {
        Jdk8Methods.requireNonNull(set, "preferredZones");
        appendInternal(new ZoneTextPrinterParser(textStyle));
        return this;
    }

    public DateTimeFormatterBuilder appendChronologyId() {
        appendInternal(new ChronoPrinterParser(null));
        return this;
    }

    public DateTimeFormatterBuilder appendChronologyText(TextStyle textStyle) {
        Jdk8Methods.requireNonNull(textStyle, "textStyle");
        appendInternal(new ChronoPrinterParser(textStyle));
        return this;
    }

    public DateTimeFormatterBuilder appendLocalized(FormatStyle formatStyle, FormatStyle formatStyle2) {
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either the date or time style must be non-null");
        }
        appendInternal(new LocalizedPrinterParser(formatStyle, formatStyle2));
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(char c) {
        appendInternal(new CharLiteralPrinterParser(c));
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(String str) {
        Jdk8Methods.requireNonNull(str, "literal");
        if (str.length() > 0) {
            if (str.length() == 1) {
                appendInternal(new CharLiteralPrinterParser(str.charAt(0)));
            } else {
                appendInternal(new StringLiteralPrinterParser(str));
            }
        }
        return this;
    }

    public DateTimeFormatterBuilder append(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        appendInternal(dateTimeFormatter.toPrinterParser(false));
        return this;
    }

    public DateTimeFormatterBuilder appendOptional(DateTimeFormatter dateTimeFormatter) {
        Jdk8Methods.requireNonNull(dateTimeFormatter, "formatter");
        appendInternal(dateTimeFormatter.toPrinterParser(true));
        return this;
    }

    public DateTimeFormatterBuilder appendPattern(String str) {
        Jdk8Methods.requireNonNull(str, "pattern");
        parsePattern(str);
        return this;
    }

    private void parsePattern(String str) {
        int i = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            int i2;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            if ((charAt >= 'A' && charAt <= 'Z') || (charAt >= 'a' && charAt <= 'z')) {
                int i3 = i + 1;
                while (i3 < str.length() && str.charAt(i3) == charAt) {
                    i3++;
                }
                i = i3 - i;
                if (charAt == 'p') {
                    if (i3 < str.length()) {
                        charAt = str.charAt(i3);
                        if ((charAt >= 'A' && charAt <= 'Z') || (charAt >= 'a' && charAt <= 'z')) {
                            int i4 = i3 + 1;
                            while (i4 < str.length() && str.charAt(i4) == charAt) {
                                i4++;
                            }
                            i2 = i4 - i3;
                            i3 = i4;
                            if (i != 0) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Pad letter 'p' must be followed by valid pad pattern: ");
                                stringBuilder.append(str);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                            padNext(i);
                            i = i2;
                        }
                    }
                    i2 = i;
                    i = 0;
                    if (i != 0) {
                        padNext(i);
                        i = i2;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Pad letter 'p' must be followed by valid pad pattern: ");
                        stringBuilder.append(str);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                TemporalField temporalField = (TemporalField) FIELD_MAP.get(Character.valueOf(charAt));
                if (temporalField != null) {
                    parseField(charAt, i, temporalField);
                } else if (charAt == 'z') {
                    if (i > 4) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Too many pattern letters: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    } else if (i == 4) {
                        appendZoneText(TextStyle.FULL);
                    } else {
                        appendZoneText(TextStyle.SHORT);
                    }
                } else if (charAt == 'V') {
                    if (i != 2) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Pattern letter count must be 2: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                    appendZoneId();
                } else if (charAt == 'Z') {
                    if (i < 4) {
                        appendOffset("+HHMM", "+0000");
                    } else if (i == 4) {
                        appendLocalizedOffset(TextStyle.FULL);
                    } else if (i == 5) {
                        appendOffset("+HH:MM:ss", "Z");
                    } else {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Too many pattern letters: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                } else if (charAt == 'O') {
                    if (i == 1) {
                        appendLocalizedOffset(TextStyle.SHORT);
                    } else if (i == 4) {
                        appendLocalizedOffset(TextStyle.FULL);
                    } else {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Pattern letter count must be 1 or 4: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                } else if (charAt == 'X') {
                    if (i > 5) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Too many pattern letters: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                    appendOffset(OffsetIdPrinterParser.PATTERNS[i + (i == 1 ? 0 : 1)], "Z");
                } else if (charAt == 'x') {
                    if (i > 5) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Too many pattern letters: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                    String str2 = i == 1 ? "+00" : i % 2 == 0 ? "+0000" : "+00:00";
                    appendOffset(OffsetIdPrinterParser.PATTERNS[i + (i == 1 ? 0 : 1)], str2);
                } else if (charAt == 'W') {
                    if (i > 1) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Too many pattern letters: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                    appendInternal(new WeekFieldsPrinterParser('W', i));
                } else if (charAt == 'w') {
                    if (i > 2) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Too many pattern letters: ");
                        stringBuilder2.append(charAt);
                        throw new IllegalArgumentException(stringBuilder2.toString());
                    }
                    appendInternal(new WeekFieldsPrinterParser('w', i));
                } else if (charAt == 'Y') {
                    appendInternal(new WeekFieldsPrinterParser('Y', i));
                } else {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Unknown pattern letter: ");
                    stringBuilder2.append(charAt);
                    throw new IllegalArgumentException(stringBuilder2.toString());
                }
                i = i3 - 1;
            } else if (charAt == '\'') {
                i++;
                int i5 = i;
                while (i5 < str.length()) {
                    if (str.charAt(i5) == '\'') {
                        i2 = i5 + 1;
                        if (i2 >= str.length() || str.charAt(i2) != '\'') {
                            break;
                        }
                        i5 = i2;
                    }
                    i5++;
                }
                if (i5 >= str.length()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Pattern ends with an incomplete string literal: ");
                    stringBuilder.append(str);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                String substring = str.substring(i, i5);
                if (substring.length() == 0) {
                    appendLiteral('\'');
                } else {
                    appendLiteral(substring.replace("''", "'"));
                }
                i = i5;
            } else if (charAt == '[') {
                optionalStart();
            } else if (charAt != ']') {
                if (!(charAt == '{' || charAt == '}')) {
                    if (charAt != '#') {
                        appendLiteral(charAt);
                    }
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Pattern includes reserved character: '");
                stringBuilder2.append(charAt);
                stringBuilder2.append("'");
                throw new IllegalArgumentException(stringBuilder2.toString());
            } else if (this.active.parent == null) {
                throw new IllegalArgumentException("Pattern invalid as it contains ] without previous [");
            } else {
                optionalEnd();
            }
            i++;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseField(char r3, int r4, org.threeten.bp.temporal.TemporalField r5) {
        /*
        r2 = this;
        r0 = 1;
        r1 = 2;
        switch(r3) {
            case 68: goto L_0x01ca;
            case 69: goto L_0x019e;
            case 70: goto L_0x0181;
            case 71: goto L_0x019e;
            case 72: goto L_0x015d;
            default: goto L_0x0005;
        };
    L_0x0005:
        switch(r3) {
            case 75: goto L_0x015d;
            case 76: goto L_0x0124;
            case 77: goto L_0x00eb;
            default: goto L_0x0008;
        };
    L_0x0008:
        switch(r3) {
            case 99: goto L_0x0099;
            case 100: goto L_0x015d;
            case 101: goto L_0x005e;
            default: goto L_0x000b;
        };
    L_0x000b:
        switch(r3) {
            case 81: goto L_0x00eb;
            case 83: goto L_0x0056;
            case 97: goto L_0x0036;
            case 104: goto L_0x015d;
            case 107: goto L_0x015d;
            case 109: goto L_0x015d;
            case 113: goto L_0x0124;
            case 115: goto L_0x015d;
            case 117: goto L_0x001a;
            case 121: goto L_0x001a;
            default: goto L_0x000e;
        };
    L_0x000e:
        if (r4 != r0) goto L_0x0015;
    L_0x0010:
        r2.appendValue(r5);
        goto L_0x01d6;
    L_0x0015:
        r2.appendValue(r5, r4);
        goto L_0x01d6;
    L_0x001a:
        if (r4 != r1) goto L_0x0023;
    L_0x001c:
        r3 = org.threeten.bp.format.DateTimeFormatterBuilder.ReducedPrinterParser.BASE_DATE;
        r2.appendValueReduced(r5, r1, r1, r3);
        goto L_0x01d6;
    L_0x0023:
        r3 = 4;
        r0 = 19;
        if (r4 >= r3) goto L_0x002f;
    L_0x0028:
        r3 = org.threeten.bp.format.SignStyle.NORMAL;
        r2.appendValue(r5, r4, r0, r3);
        goto L_0x01d6;
    L_0x002f:
        r3 = org.threeten.bp.format.SignStyle.EXCEEDS_PAD;
        r2.appendValue(r5, r4, r0, r3);
        goto L_0x01d6;
    L_0x0036:
        if (r4 != r0) goto L_0x003f;
    L_0x0038:
        r3 = org.threeten.bp.format.TextStyle.SHORT;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x003f:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x0056:
        r3 = org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;
        r5 = 0;
        r2.appendFraction(r3, r4, r4, r5);
        goto L_0x01d6;
    L_0x005e:
        switch(r4) {
            case 1: goto L_0x008d;
            case 2: goto L_0x008d;
            case 3: goto L_0x0086;
            case 4: goto L_0x007f;
            case 5: goto L_0x0078;
            default: goto L_0x0061;
        };
    L_0x0061:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x0078:
        r3 = org.threeten.bp.format.TextStyle.NARROW;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x007f:
        r3 = org.threeten.bp.format.TextStyle.FULL;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x0086:
        r3 = org.threeten.bp.format.TextStyle.SHORT;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x008d:
        r3 = new org.threeten.bp.format.DateTimeFormatterBuilder$WeekFieldsPrinterParser;
        r5 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r3.<init>(r5, r4);
        r2.appendInternal(r3);
        goto L_0x01d6;
    L_0x0099:
        switch(r4) {
            case 1: goto L_0x00df;
            case 2: goto L_0x00c8;
            case 3: goto L_0x00c1;
            case 4: goto L_0x00ba;
            case 5: goto L_0x00b3;
            default: goto L_0x009c;
        };
    L_0x009c:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x00b3:
        r3 = org.threeten.bp.format.TextStyle.NARROW_STANDALONE;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x00ba:
        r3 = org.threeten.bp.format.TextStyle.FULL_STANDALONE;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x00c1:
        r3 = org.threeten.bp.format.TextStyle.SHORT_STANDALONE;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x00c8:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Invalid number of pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x00df:
        r3 = new org.threeten.bp.format.DateTimeFormatterBuilder$WeekFieldsPrinterParser;
        r5 = 99;
        r3.<init>(r5, r4);
        r2.appendInternal(r3);
        goto L_0x01d6;
    L_0x00eb:
        switch(r4) {
            case 1: goto L_0x011f;
            case 2: goto L_0x011a;
            case 3: goto L_0x0113;
            case 4: goto L_0x010c;
            case 5: goto L_0x0105;
            default: goto L_0x00ee;
        };
    L_0x00ee:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x0105:
        r3 = org.threeten.bp.format.TextStyle.NARROW;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x010c:
        r3 = org.threeten.bp.format.TextStyle.FULL;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x0113:
        r3 = org.threeten.bp.format.TextStyle.SHORT;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x011a:
        r2.appendValue(r5, r1);
        goto L_0x01d6;
    L_0x011f:
        r2.appendValue(r5);
        goto L_0x01d6;
    L_0x0124:
        switch(r4) {
            case 1: goto L_0x0158;
            case 2: goto L_0x0153;
            case 3: goto L_0x014c;
            case 4: goto L_0x0145;
            case 5: goto L_0x013e;
            default: goto L_0x0127;
        };
    L_0x0127:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x013e:
        r3 = org.threeten.bp.format.TextStyle.NARROW_STANDALONE;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x0145:
        r3 = org.threeten.bp.format.TextStyle.FULL_STANDALONE;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x014c:
        r3 = org.threeten.bp.format.TextStyle.SHORT_STANDALONE;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x0153:
        r2.appendValue(r5, r1);
        goto L_0x01d6;
    L_0x0158:
        r2.appendValue(r5);
        goto L_0x01d6;
    L_0x015d:
        if (r4 != r0) goto L_0x0164;
    L_0x015f:
        r2.appendValue(r5);
        goto L_0x01d6;
    L_0x0164:
        if (r4 != r1) goto L_0x016a;
    L_0x0166:
        r2.appendValue(r5, r4);
        goto L_0x01d6;
    L_0x016a:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x0181:
        if (r4 != r0) goto L_0x0187;
    L_0x0183:
        r2.appendValue(r5);
        goto L_0x01d6;
    L_0x0187:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x019e:
        switch(r4) {
            case 1: goto L_0x01c4;
            case 2: goto L_0x01c4;
            case 3: goto L_0x01c4;
            case 4: goto L_0x01be;
            case 5: goto L_0x01b8;
            default: goto L_0x01a1;
        };
    L_0x01a1:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
    L_0x01b8:
        r3 = org.threeten.bp.format.TextStyle.NARROW;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x01be:
        r3 = org.threeten.bp.format.TextStyle.FULL;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x01c4:
        r3 = org.threeten.bp.format.TextStyle.SHORT;
        r2.appendText(r5, r3);
        goto L_0x01d6;
    L_0x01ca:
        if (r4 != r0) goto L_0x01d0;
    L_0x01cc:
        r2.appendValue(r5);
        goto L_0x01d6;
    L_0x01d0:
        r0 = 3;
        if (r4 > r0) goto L_0x01d7;
    L_0x01d3:
        r2.appendValue(r5, r4);
    L_0x01d6:
        return;
    L_0x01d7:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r0 = "Too many pattern letters: ";
        r5.append(r0);
        r5.append(r3);
        r3 = r5.toString();
        r4.<init>(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.format.DateTimeFormatterBuilder.parseField(char, int, org.threeten.bp.temporal.TemporalField):void");
    }

    public DateTimeFormatterBuilder padNext(int i) {
        return padNext(i, ' ');
    }

    public DateTimeFormatterBuilder padNext(int i, char c) {
        if (i < 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The pad width must be at least one but was ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.active.padNextWidth = i;
        this.active.padNextChar = c;
        this.active.valueParserIndex = '￿';
        return this;
    }

    public DateTimeFormatterBuilder optionalStart() {
        this.active.valueParserIndex = -1;
        this.active = new DateTimeFormatterBuilder(this.active, true);
        return this;
    }

    public DateTimeFormatterBuilder optionalEnd() {
        if (this.active.parent == null) {
            throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
        }
        if (this.active.printerParsers.size() > 0) {
            DateTimePrinterParser compositePrinterParser = new CompositePrinterParser(this.active.printerParsers, this.active.optional);
            this.active = this.active.parent;
            appendInternal(compositePrinterParser);
        } else {
            this.active = this.active.parent;
        }
        return this;
    }

    private int appendInternal(DateTimePrinterParser dateTimePrinterParser) {
        Jdk8Methods.requireNonNull(dateTimePrinterParser, "pp");
        if (this.active.padNextWidth > 0) {
            if (dateTimePrinterParser != null) {
                dateTimePrinterParser = new PadPrinterParserDecorator(dateTimePrinterParser, this.active.padNextWidth, this.active.padNextChar);
            }
            this.active.padNextWidth = 0;
            this.active.padNextChar = '\u0000';
        }
        this.active.printerParsers.add(dateTimePrinterParser);
        this.active.valueParserIndex = -1;
        return this.active.printerParsers.size() - 1;
    }

    public DateTimeFormatter toFormatter() {
        return toFormatter(Locale.getDefault());
    }

    public DateTimeFormatter toFormatter(Locale locale) {
        Jdk8Methods.requireNonNull(locale, "locale");
        while (this.active.parent != null) {
            optionalEnd();
        }
        return new DateTimeFormatter(new CompositePrinterParser(this.printerParsers, false), locale, DecimalStyle.STANDARD, ResolverStyle.SMART, null, null, null);
    }

    DateTimeFormatter toFormatter(ResolverStyle resolverStyle) {
        return toFormatter().withResolverStyle(resolverStyle);
    }
}
