package org.threeten.bp.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.threeten.bp.chrono.Chronology;

final class SimpleDateTimeFormatStyleProvider extends DateTimeFormatStyleProvider {
    private static final ConcurrentMap<String, Object> FORMATTER_CACHE = new ConcurrentHashMap(16, 0.75f, 2);

    SimpleDateTimeFormatStyleProvider() {
    }

    public Locale[] getAvailableLocales() {
        return DateFormat.getAvailableLocales();
    }

    public DateTimeFormatter getFormatter(FormatStyle formatStyle, FormatStyle formatStyle2, Chronology chronology, Locale locale) {
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Date and Time style must not both be null");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chronology.getId());
        stringBuilder.append('|');
        stringBuilder.append(locale.toString());
        stringBuilder.append('|');
        stringBuilder.append(formatStyle);
        stringBuilder.append(formatStyle2);
        chronology = stringBuilder.toString();
        Object obj = FORMATTER_CACHE.get(chronology);
        if (obj == null) {
            if (formatStyle == null) {
                formatStyle = DateFormat.getTimeInstance(convertStyle(formatStyle2), locale);
            } else if (formatStyle2 != null) {
                formatStyle = DateFormat.getDateTimeInstance(convertStyle(formatStyle), convertStyle(formatStyle2), locale);
            } else {
                formatStyle = DateFormat.getDateInstance(convertStyle(formatStyle), locale);
            }
            if ((formatStyle instanceof SimpleDateFormat) != null) {
                formatStyle = new DateTimeFormatterBuilder().appendPattern(((SimpleDateFormat) formatStyle).toPattern()).toFormatter(locale);
                FORMATTER_CACHE.putIfAbsent(chronology, formatStyle);
                return formatStyle;
            }
            FORMATTER_CACHE.putIfAbsent(chronology, "");
            throw new IllegalArgumentException("Unable to convert DateFormat to DateTimeFormatter");
        } else if (obj.equals("") == null) {
            return (DateTimeFormatter) obj;
        } else {
            throw new IllegalArgumentException("Unable to convert DateFormat to DateTimeFormatter");
        }
    }

    private int convertStyle(FormatStyle formatStyle) {
        return formatStyle.ordinal();
    }
}
