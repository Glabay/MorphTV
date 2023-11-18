package org.threeten.bp.format;

import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.actions.SearchIntents;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.chrono.IsoChronology;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.IsoFields;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQuery;

public final class DateTimeFormatter {
    public static final DateTimeFormatter BASIC_ISO_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(ChronoField.YEAR, 4).appendValue(ChronoField.MONTH_OF_YEAR, 2).appendValue(ChronoField.DAY_OF_MONTH, 2).optionalStart().appendOffset("+HHMMss", "Z").toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_DATE).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_DATE_TIME = new DateTimeFormatterBuilder().append(ISO_LOCAL_DATE_TIME).optionalStart().appendOffsetId().optionalStart().appendLiteral('[').parseCaseSensitive().appendZoneRegionId().appendLiteral(']').toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_INSTANT = new DateTimeFormatterBuilder().parseCaseInsensitive().appendInstant().toFormatter(ResolverStyle.STRICT);
    public static final DateTimeFormatter ISO_LOCAL_DATE = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_DATE).appendLiteral('T').append(ISO_LOCAL_TIME).toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_LOCAL_TIME = new DateTimeFormatterBuilder().appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter(ResolverStyle.STRICT);
    public static final DateTimeFormatter ISO_OFFSET_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_DATE).appendOffsetId().toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_DATE_TIME).appendOffsetId().toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_OFFSET_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_TIME).appendOffsetId().toFormatter(ResolverStyle.STRICT);
    public static final DateTimeFormatter ISO_ORDINAL_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.DAY_OF_YEAR, 3).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(ISO_LOCAL_TIME).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT);
    public static final DateTimeFormatter ISO_WEEK_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(IsoFields.WEEK_BASED_YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral("-W").appendValue(IsoFields.WEEK_OF_WEEK_BASED_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_WEEK, 1).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_ZONED_DATE_TIME = new DateTimeFormatterBuilder().append(ISO_OFFSET_DATE_TIME).optionalStart().appendLiteral('[').parseCaseSensitive().appendZoneRegionId().appendLiteral(']').toFormatter(ResolverStyle.STRICT).withChronology(IsoChronology.INSTANCE);
    private static final TemporalQuery<Period> PARSED_EXCESS_DAYS = new C15531();
    private static final TemporalQuery<Boolean> PARSED_LEAP_SECOND = new C15542();
    public static final DateTimeFormatter RFC_1123_DATE_TIME;
    private final Chronology chrono;
    private final DecimalStyle decimalStyle;
    private final Locale locale;
    private final CompositePrinterParser printerParser;
    private final Set<TemporalField> resolverFields;
    private final ResolverStyle resolverStyle;
    private final ZoneId zone;

    /* renamed from: org.threeten.bp.format.DateTimeFormatter$1 */
    static class C15531 implements TemporalQuery<Period> {
        C15531() {
        }

        public Period queryFrom(TemporalAccessor temporalAccessor) {
            if (temporalAccessor instanceof DateTimeBuilder) {
                return ((DateTimeBuilder) temporalAccessor).excessDays;
            }
            return Period.ZERO;
        }
    }

    /* renamed from: org.threeten.bp.format.DateTimeFormatter$2 */
    static class C15542 implements TemporalQuery<Boolean> {
        C15542() {
        }

        public Boolean queryFrom(TemporalAccessor temporalAccessor) {
            if (temporalAccessor instanceof DateTimeBuilder) {
                return Boolean.valueOf(((DateTimeBuilder) temporalAccessor).leapSecond);
            }
            return Boolean.FALSE;
        }
    }

    static {
        Map hashMap = new HashMap();
        hashMap.put(Long.valueOf(1), "Mon");
        hashMap.put(Long.valueOf(2), "Tue");
        hashMap.put(Long.valueOf(3), "Wed");
        hashMap.put(Long.valueOf(4), "Thu");
        hashMap.put(Long.valueOf(5), "Fri");
        hashMap.put(Long.valueOf(6), "Sat");
        hashMap.put(Long.valueOf(7), "Sun");
        Map hashMap2 = new HashMap();
        hashMap2.put(Long.valueOf(1), "Jan");
        hashMap2.put(Long.valueOf(2), "Feb");
        hashMap2.put(Long.valueOf(3), "Mar");
        hashMap2.put(Long.valueOf(4), "Apr");
        hashMap2.put(Long.valueOf(5), "May");
        hashMap2.put(Long.valueOf(6), "Jun");
        hashMap2.put(Long.valueOf(7), "Jul");
        hashMap2.put(Long.valueOf(8), "Aug");
        hashMap2.put(Long.valueOf(9), "Sep");
        hashMap2.put(Long.valueOf(10), "Oct");
        hashMap2.put(Long.valueOf(11), "Nov");
        hashMap2.put(Long.valueOf(12), "Dec");
        RFC_1123_DATE_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().optionalStart().appendText(ChronoField.DAY_OF_WEEK, hashMap).appendLiteral(", ").optionalEnd().appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(' ').appendText(ChronoField.MONTH_OF_YEAR, hashMap2).appendLiteral(' ').appendValue(ChronoField.YEAR, 4).appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalEnd().appendLiteral(' ').appendOffset("+HHMM", "GMT").toFormatter(ResolverStyle.SMART).withChronology(IsoChronology.INSTANCE);
    }

    public static DateTimeFormatter ofPattern(String str) {
        return new DateTimeFormatterBuilder().appendPattern(str).toFormatter();
    }

    public static DateTimeFormatter ofPattern(String str, Locale locale) {
        return new DateTimeFormatterBuilder().appendPattern(str).toFormatter(locale);
    }

    public static DateTimeFormatter ofLocalizedDate(FormatStyle formatStyle) {
        Jdk8Methods.requireNonNull(formatStyle, "dateStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, null).toFormatter().withChronology(IsoChronology.INSTANCE);
    }

    public static DateTimeFormatter ofLocalizedTime(FormatStyle formatStyle) {
        Jdk8Methods.requireNonNull(formatStyle, "timeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(null, formatStyle).toFormatter().withChronology(IsoChronology.INSTANCE);
    }

    public static DateTimeFormatter ofLocalizedDateTime(FormatStyle formatStyle) {
        Jdk8Methods.requireNonNull(formatStyle, "dateTimeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, formatStyle).toFormatter().withChronology(IsoChronology.INSTANCE);
    }

    public static DateTimeFormatter ofLocalizedDateTime(FormatStyle formatStyle, FormatStyle formatStyle2) {
        Jdk8Methods.requireNonNull(formatStyle, "dateStyle");
        Jdk8Methods.requireNonNull(formatStyle2, "timeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, formatStyle2).toFormatter().withChronology(IsoChronology.INSTANCE);
    }

    public static final TemporalQuery<Period> parsedExcessDays() {
        return PARSED_EXCESS_DAYS;
    }

    public static final TemporalQuery<Boolean> parsedLeapSecond() {
        return PARSED_LEAP_SECOND;
    }

    DateTimeFormatter(CompositePrinterParser compositePrinterParser, Locale locale, DecimalStyle decimalStyle, ResolverStyle resolverStyle, Set<TemporalField> set, Chronology chronology, ZoneId zoneId) {
        this.printerParser = (CompositePrinterParser) Jdk8Methods.requireNonNull(compositePrinterParser, "printerParser");
        this.locale = (Locale) Jdk8Methods.requireNonNull(locale, "locale");
        this.decimalStyle = (DecimalStyle) Jdk8Methods.requireNonNull(decimalStyle, "decimalStyle");
        this.resolverStyle = (ResolverStyle) Jdk8Methods.requireNonNull(resolverStyle, "resolverStyle");
        this.resolverFields = set;
        this.chrono = chronology;
        this.zone = zoneId;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public DateTimeFormatter withLocale(Locale locale) {
        if (this.locale.equals(locale)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, locale, this.decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, this.zone);
    }

    public DecimalStyle getDecimalStyle() {
        return this.decimalStyle;
    }

    public DateTimeFormatter withDecimalStyle(DecimalStyle decimalStyle) {
        if (this.decimalStyle.equals(decimalStyle)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, this.zone);
    }

    public Chronology getChronology() {
        return this.chrono;
    }

    public DateTimeFormatter withChronology(Chronology chronology) {
        if (Jdk8Methods.equals(this.chrono, chronology)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, this.resolverFields, chronology, this.zone);
    }

    public ZoneId getZone() {
        return this.zone;
    }

    public DateTimeFormatter withZone(ZoneId zoneId) {
        if (Jdk8Methods.equals(this.zone, zoneId)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, zoneId);
    }

    public ResolverStyle getResolverStyle() {
        return this.resolverStyle;
    }

    public DateTimeFormatter withResolverStyle(ResolverStyle resolverStyle) {
        Jdk8Methods.requireNonNull(resolverStyle, "resolverStyle");
        if (Jdk8Methods.equals(this.resolverStyle, resolverStyle)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, resolverStyle, this.resolverFields, this.chrono, this.zone);
    }

    public Set<TemporalField> getResolverFields() {
        return this.resolverFields;
    }

    public DateTimeFormatter withResolverFields(TemporalField... temporalFieldArr) {
        if (temporalFieldArr == null) {
            return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, null, this.chrono, this.zone);
        }
        Set hashSet = new HashSet(Arrays.asList(temporalFieldArr));
        if (Jdk8Methods.equals(this.resolverFields, hashSet) != null) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, Collections.unmodifiableSet(hashSet), this.chrono, this.zone);
    }

    public DateTimeFormatter withResolverFields(Set<TemporalField> set) {
        if (set == null) {
            return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, null, this.chrono, this.zone);
        }
        if (Jdk8Methods.equals(this.resolverFields, set)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, Collections.unmodifiableSet(new HashSet(set)), this.chrono, this.zone);
    }

    public String format(TemporalAccessor temporalAccessor) {
        Appendable stringBuilder = new StringBuilder(32);
        formatTo(temporalAccessor, stringBuilder);
        return stringBuilder.toString();
    }

    public void formatTo(TemporalAccessor temporalAccessor, Appendable appendable) {
        Jdk8Methods.requireNonNull(temporalAccessor, "temporal");
        Jdk8Methods.requireNonNull(appendable, "appendable");
        try {
            DateTimePrintContext dateTimePrintContext = new DateTimePrintContext(temporalAccessor, this);
            if ((appendable instanceof StringBuilder) != null) {
                this.printerParser.print(dateTimePrintContext, (StringBuilder) appendable);
                return;
            }
            temporalAccessor = new StringBuilder(32);
            this.printerParser.print(dateTimePrintContext, temporalAccessor);
            appendable.append(temporalAccessor);
        } catch (TemporalAccessor temporalAccessor2) {
            throw new DateTimeException(temporalAccessor2.getMessage(), temporalAccessor2);
        }
    }

    public TemporalAccessor parse(CharSequence charSequence) {
        Jdk8Methods.requireNonNull(charSequence, MimeTypes.BASE_TYPE_TEXT);
        try {
            return parseToBuilder(charSequence, null).resolve(this.resolverStyle, this.resolverFields);
        } catch (CharSequence charSequence2) {
            throw charSequence2;
        } catch (RuntimeException e) {
            throw createError(charSequence2, e);
        }
    }

    public TemporalAccessor parse(CharSequence charSequence, ParsePosition parsePosition) {
        Jdk8Methods.requireNonNull(charSequence, MimeTypes.BASE_TYPE_TEXT);
        Jdk8Methods.requireNonNull(parsePosition, "position");
        try {
            return parseToBuilder(charSequence, parsePosition).resolve(this.resolverStyle, this.resolverFields);
        } catch (CharSequence charSequence2) {
            throw charSequence2;
        } catch (CharSequence charSequence22) {
            throw charSequence22;
        } catch (ParsePosition parsePosition2) {
            throw createError(charSequence22, parsePosition2);
        }
    }

    public <T> T parse(CharSequence charSequence, TemporalQuery<T> temporalQuery) {
        Jdk8Methods.requireNonNull(charSequence, MimeTypes.BASE_TYPE_TEXT);
        Jdk8Methods.requireNonNull(temporalQuery, "type");
        try {
            return parseToBuilder(charSequence, null).resolve(this.resolverStyle, this.resolverFields).build(temporalQuery);
        } catch (CharSequence charSequence2) {
            throw charSequence2;
        } catch (TemporalQuery<T> temporalQuery2) {
            throw createError(charSequence2, temporalQuery2);
        }
    }

    public org.threeten.bp.temporal.TemporalAccessor parseBest(java.lang.CharSequence r5, org.threeten.bp.temporal.TemporalQuery<?>... r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = "text";
        org.threeten.bp.jdk8.Jdk8Methods.requireNonNull(r5, r0);
        r0 = "types";
        org.threeten.bp.jdk8.Jdk8Methods.requireNonNull(r6, r0);
        r0 = r6.length;
        r1 = 2;
        if (r0 >= r1) goto L_0x0016;
    L_0x000e:
        r5 = new java.lang.IllegalArgumentException;
        r6 = "At least two types must be specified";
        r5.<init>(r6);
        throw r5;
    L_0x0016:
        r0 = 0;
        r0 = r4.parseToBuilder(r5, r0);	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r1 = r4.resolverStyle;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r2 = r4.resolverFields;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r0 = r0.resolve(r1, r2);	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r1 = r6.length;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r2 = 0;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
    L_0x0025:
        if (r2 >= r1) goto L_0x0033;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
    L_0x0027:
        r3 = r6[r2];	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r3 = r0.build(r3);	 Catch:{ RuntimeException -> 0x0030, DateTimeParseException -> 0x0054 }
        r3 = (org.threeten.bp.temporal.TemporalAccessor) r3;	 Catch:{ RuntimeException -> 0x0030, DateTimeParseException -> 0x0054 }
        return r3;
    L_0x0030:
        r2 = r2 + 1;
        goto L_0x0025;
    L_0x0033:
        r0 = new org.threeten.bp.DateTimeException;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r1 = new java.lang.StringBuilder;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r1.<init>();	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r2 = "Unable to convert parsed text to any specified type: ";	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r1.append(r2);	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r6 = java.util.Arrays.toString(r6);	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r1.append(r6);	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r6 = r1.toString();	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        r0.<init>(r6);	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
        throw r0;	 Catch:{ DateTimeParseException -> 0x0054, RuntimeException -> 0x004e }
    L_0x004e:
        r6 = move-exception;
        r5 = r4.createError(r5, r6);
        throw r5;
    L_0x0054:
        r5 = move-exception;
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.threeten.bp.format.DateTimeFormatter.parseBest(java.lang.CharSequence, org.threeten.bp.temporal.TemporalQuery[]):org.threeten.bp.temporal.TemporalAccessor");
    }

    private DateTimeParseException createError(CharSequence charSequence, RuntimeException runtimeException) {
        String stringBuilder;
        if (charSequence.length() > 64) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(charSequence.subSequence(0, 64).toString());
            stringBuilder2.append("...");
            stringBuilder = stringBuilder2.toString();
        } else {
            stringBuilder = charSequence.toString();
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Text '");
        stringBuilder3.append(stringBuilder);
        stringBuilder3.append("' could not be parsed: ");
        stringBuilder3.append(runtimeException.getMessage());
        return new DateTimeParseException(stringBuilder3.toString(), charSequence, 0, runtimeException);
    }

    private DateTimeBuilder parseToBuilder(CharSequence charSequence, ParsePosition parsePosition) {
        ParsePosition parsePosition2 = parsePosition != null ? parsePosition : new ParsePosition(0);
        Parsed parseUnresolved0 = parseUnresolved0(charSequence, parsePosition2);
        if (parseUnresolved0 != null && parsePosition2.getErrorIndex() < 0) {
            if (parsePosition != null || parsePosition2.getIndex() >= charSequence.length()) {
                return parseUnresolved0.toBuilder();
            }
        }
        if (charSequence.length() > 64) {
            parsePosition = new StringBuilder();
            parsePosition.append(charSequence.subSequence(0, 64).toString());
            parsePosition.append("...");
            parsePosition = parsePosition.toString();
        } else {
            parsePosition = charSequence.toString();
        }
        if (parsePosition2.getErrorIndex() >= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Text '");
            stringBuilder.append(parsePosition);
            stringBuilder.append("' could not be parsed at index ");
            stringBuilder.append(parsePosition2.getErrorIndex());
            throw new DateTimeParseException(stringBuilder.toString(), charSequence, parsePosition2.getErrorIndex());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Text '");
        stringBuilder.append(parsePosition);
        stringBuilder.append("' could not be parsed, unparsed text found at index ");
        stringBuilder.append(parsePosition2.getIndex());
        throw new DateTimeParseException(stringBuilder.toString(), charSequence, parsePosition2.getIndex());
    }

    public TemporalAccessor parseUnresolved(CharSequence charSequence, ParsePosition parsePosition) {
        return parseUnresolved0(charSequence, parsePosition);
    }

    private Parsed parseUnresolved0(CharSequence charSequence, ParsePosition parsePosition) {
        Jdk8Methods.requireNonNull(charSequence, MimeTypes.BASE_TYPE_TEXT);
        Jdk8Methods.requireNonNull(parsePosition, "position");
        DateTimeParseContext dateTimeParseContext = new DateTimeParseContext(this);
        charSequence = this.printerParser.parse(dateTimeParseContext, charSequence, parsePosition.getIndex());
        if (charSequence < null) {
            parsePosition.setErrorIndex(charSequence ^ -1);
            return null;
        }
        parsePosition.setIndex(charSequence);
        return dateTimeParseContext.toParsed();
    }

    CompositePrinterParser toPrinterParser(boolean z) {
        return this.printerParser.withOptional(z);
    }

    public Format toFormat() {
        return new ClassicFormat(this, null);
    }

    public Format toFormat(TemporalQuery<?> temporalQuery) {
        Jdk8Methods.requireNonNull(temporalQuery, SearchIntents.EXTRA_QUERY);
        return new ClassicFormat(this, temporalQuery);
    }

    public String toString() {
        String compositePrinterParser = this.printerParser.toString();
        return compositePrinterParser.startsWith("[") ? compositePrinterParser : compositePrinterParser.substring(1, compositePrinterParser.length() - 1);
    }
}
