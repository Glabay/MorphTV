package org.threeten.bp.chrono;

import java.util.Locale;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;

public interface Era extends TemporalAccessor, TemporalAdjuster {
    String getDisplayName(TextStyle textStyle, Locale locale);

    int getValue();
}
