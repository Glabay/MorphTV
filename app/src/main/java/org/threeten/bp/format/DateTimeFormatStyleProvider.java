package org.threeten.bp.format;

import java.util.Locale;
import org.threeten.bp.chrono.Chronology;

abstract class DateTimeFormatStyleProvider {
    public abstract DateTimeFormatter getFormatter(FormatStyle formatStyle, FormatStyle formatStyle2, Chronology chronology, Locale locale);

    DateTimeFormatStyleProvider() {
    }

    static DateTimeFormatStyleProvider getInstance() {
        return new SimpleDateTimeFormatStyleProvider();
    }

    public Locale[] getAvailableLocales() {
        throw new UnsupportedOperationException();
    }
}
