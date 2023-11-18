package org.apache.commons.lang3.time;

import ir.mahdi.mzip.rar.unpack.ppm.ModelPPM;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public class FastDateParser implements DateParser, Serializable {
    private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1) {
        int modify(FastDateParser fastDateParser, int i) {
            return i < 100 ? fastDateParser.adjustYear(i) : i;
        }
    };
    private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
    private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
    private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(7) {
        int modify(FastDateParser fastDateParser, int i) {
            return i != 7 ? 1 + i : 1;
        }
    };
    private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
    private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10) {
        int modify(FastDateParser fastDateParser, int i) {
            return i == 12 ? 0 : i;
        }
    };
    private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11) {
        int modify(FastDateParser fastDateParser, int i) {
            return i == 24 ? 0 : i;
        }
    };
    private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
    private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
    static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
    private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
    private static final Comparator<String> LONGER_FIRST_LOWERCASE = new C14661();
    private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
    private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
    private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2) {
        int modify(FastDateParser fastDateParser, int i) {
            return i - 1;
        }
    };
    private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
    private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
    private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
    private static final ConcurrentMap<Locale, Strategy>[] caches = new ConcurrentMap[17];
    private static final long serialVersionUID = 3;
    private final int century;
    private final Locale locale;
    private final String pattern;
    private transient List<StrategyAndWidth> patterns;
    private final int startYear;
    private final TimeZone timeZone;

    /* renamed from: org.apache.commons.lang3.time.FastDateParser$1 */
    static class C14661 implements Comparator<String> {
        C14661() {
        }

        public int compare(String str, String str2) {
            return str2.compareTo(str);
        }
    }

    private static abstract class Strategy {
        boolean isNumber() {
            return false;
        }

        abstract boolean parse(FastDateParser fastDateParser, Calendar calendar, String str, ParsePosition parsePosition, int i);

        private Strategy() {
        }
    }

    private static class NumberStrategy extends Strategy {
        private final int field;

        boolean isNumber() {
            return true;
        }

        int modify(FastDateParser fastDateParser, int i) {
            return i;
        }

        NumberStrategy(int i) {
            super();
            this.field = i;
        }

        boolean parse(FastDateParser fastDateParser, Calendar calendar, String str, ParsePosition parsePosition, int i) {
            int index = parsePosition.getIndex();
            int length = str.length();
            if (i == 0) {
                while (index < length) {
                    if (Character.isWhitespace(str.charAt(index)) == 0) {
                        break;
                    }
                    index++;
                }
                parsePosition.setIndex(index);
            } else {
                i += index;
                if (length > i) {
                    length = i;
                }
            }
            while (index < length) {
                if (Character.isDigit(str.charAt(index)) == 0) {
                    break;
                }
                index++;
            }
            if (parsePosition.getIndex() == index) {
                parsePosition.setErrorIndex(index);
                return null;
            }
            str = Integer.parseInt(str.substring(parsePosition.getIndex(), index));
            parsePosition.setIndex(index);
            calendar.set(this.field, modify(fastDateParser, str));
            return true;
        }
    }

    private static abstract class PatternStrategy extends Strategy {
        private Pattern pattern;

        boolean isNumber() {
            return false;
        }

        abstract void setCalendar(FastDateParser fastDateParser, Calendar calendar, String str);

        private PatternStrategy() {
            super();
        }

        void createPattern(StringBuilder stringBuilder) {
            createPattern(stringBuilder.toString());
        }

        void createPattern(String str) {
            this.pattern = Pattern.compile(str);
        }

        boolean parse(FastDateParser fastDateParser, Calendar calendar, String str, ParsePosition parsePosition, int i) {
            str = this.pattern.matcher(str.substring(parsePosition.getIndex()));
            if (str.lookingAt() == 0) {
                parsePosition.setErrorIndex(parsePosition.getIndex());
                return null;
            }
            parsePosition.setIndex(parsePosition.getIndex() + str.end(1));
            setCalendar(fastDateParser, calendar, str.group(1));
            return true;
        }
    }

    private static class CaseInsensitiveTextStrategy extends PatternStrategy {
        private final int field;
        private final Map<String, Integer> lKeyValues;
        final Locale locale;

        CaseInsensitiveTextStrategy(int i, Calendar calendar, Locale locale) {
            super();
            this.field = i;
            this.locale = locale;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("((?iu)");
            this.lKeyValues = FastDateParser.appendDisplayNames(calendar, locale, i, stringBuilder);
            stringBuilder.setLength(stringBuilder.length() - 1);
            stringBuilder.append(")");
            createPattern(stringBuilder);
        }

        void setCalendar(FastDateParser fastDateParser, Calendar calendar, String str) {
            calendar.set(this.field, ((Integer) this.lKeyValues.get(str.toLowerCase(this.locale))).intValue());
        }
    }

    private static class CopyQuotedStrategy extends Strategy {
        private final String formatField;

        boolean isNumber() {
            return false;
        }

        CopyQuotedStrategy(String str) {
            super();
            this.formatField = str;
        }

        boolean parse(FastDateParser fastDateParser, Calendar calendar, String str, ParsePosition parsePosition, int i) {
            calendar = null;
            while (calendar < this.formatField.length()) {
                i = parsePosition.getIndex() + calendar;
                if (i == str.length()) {
                    parsePosition.setErrorIndex(i);
                    return false;
                } else if (this.formatField.charAt(calendar) != str.charAt(i)) {
                    parsePosition.setErrorIndex(i);
                    return false;
                } else {
                    calendar++;
                }
            }
            parsePosition.setIndex(this.formatField.length() + parsePosition.getIndex());
            return true;
        }
    }

    private static class ISO8601TimeZoneStrategy extends PatternStrategy {
        private static final Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
        private static final Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
        private static final Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");

        ISO8601TimeZoneStrategy(String str) {
            super();
            createPattern(str);
        }

        void setCalendar(FastDateParser fastDateParser, Calendar calendar, String str) {
            if (str.equals("Z") != null) {
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                return;
            }
            fastDateParser = new StringBuilder();
            fastDateParser.append("GMT");
            fastDateParser.append(str);
            calendar.setTimeZone(TimeZone.getTimeZone(fastDateParser.toString()));
        }

        static Strategy getStrategy(int i) {
            switch (i) {
                case 1:
                    return ISO_8601_1_STRATEGY;
                case 2:
                    return ISO_8601_2_STRATEGY;
                case 3:
                    return ISO_8601_3_STRATEGY;
                default:
                    throw new IllegalArgumentException("invalid number of X");
            }
        }
    }

    private static class StrategyAndWidth {
        final Strategy strategy;
        final int width;

        StrategyAndWidth(Strategy strategy, int i) {
            this.strategy = strategy;
            this.width = i;
        }

        int getMaxWidth(ListIterator<StrategyAndWidth> listIterator) {
            int i = 0;
            if (this.strategy.isNumber()) {
                if (listIterator.hasNext()) {
                    Strategy strategy = ((StrategyAndWidth) listIterator.next()).strategy;
                    listIterator.previous();
                    if (strategy.isNumber() != null) {
                        i = this.width;
                    }
                    return i;
                }
            }
            return 0;
        }
    }

    private class StrategyParser {
        private int currentIdx;
        private final Calendar definingCalendar;
        private final String pattern;

        StrategyParser(String str, Calendar calendar) {
            this.pattern = str;
            this.definingCalendar = calendar;
        }

        StrategyAndWidth getNextStrategy() {
            if (this.currentIdx >= this.pattern.length()) {
                return null;
            }
            char charAt = this.pattern.charAt(this.currentIdx);
            if (FastDateParser.isFormatLetter(charAt)) {
                return letterPattern(charAt);
            }
            return literal();
        }

        private StrategyAndWidth letterPattern(char c) {
            int i;
            int i2 = this.currentIdx;
            do {
                i = this.currentIdx + 1;
                this.currentIdx = i;
                if (i >= this.pattern.length()) {
                    break;
                }
            } while (this.pattern.charAt(this.currentIdx) == c);
            i = this.currentIdx - i2;
            return new StrategyAndWidth(FastDateParser.this.getStrategy(c, i, this.definingCalendar), i);
        }

        private StrategyAndWidth literal() {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            while (this.currentIdx < this.pattern.length()) {
                char charAt = this.pattern.charAt(this.currentIdx);
                if (i == 0 && FastDateParser.isFormatLetter(charAt)) {
                    break;
                }
                if (charAt == '\'') {
                    int i2 = this.currentIdx + 1;
                    this.currentIdx = i2;
                    if (i2 == this.pattern.length() || this.pattern.charAt(this.currentIdx) != '\'') {
                        i ^= 1;
                    }
                }
                this.currentIdx++;
                stringBuilder.append(charAt);
            }
            if (i != 0) {
                throw new IllegalArgumentException("Unterminated quote");
            }
            String stringBuilder2 = stringBuilder.toString();
            return new StrategyAndWidth(new CopyQuotedStrategy(stringBuilder2), stringBuilder2.length());
        }
    }

    static class TimeZoneStrategy extends PatternStrategy {
        private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
        private static final int ID = 0;
        private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
        private final Locale locale;
        private final Map<String, TzInfo> tzNames = new HashMap();

        private static class TzInfo {
            int dstOffset;
            TimeZone zone;

            TzInfo(TimeZone timeZone, boolean z) {
                this.zone = timeZone;
                this.dstOffset = z ? timeZone.getDSTSavings() : null;
            }
        }

        TimeZoneStrategy(Locale locale) {
            super();
            this.locale = locale;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
            Set<String> treeSet = new TreeSet(FastDateParser.LONGER_FIRST_LOWERCASE);
            for (String[] strArr : DateFormatSymbols.getInstance(locale).getZoneStrings()) {
                String str = strArr[0];
                if (!str.equalsIgnoreCase("GMT")) {
                    TimeZone timeZone = TimeZone.getTimeZone(str);
                    TzInfo tzInfo = new TzInfo(timeZone, false);
                    Object obj = tzInfo;
                    for (int i = 1; i < strArr.length; i++) {
                        if (i == 3) {
                            obj = new TzInfo(timeZone, true);
                        } else if (i == 5) {
                            obj = tzInfo;
                        }
                        String toLowerCase = strArr[i].toLowerCase(locale);
                        if (treeSet.add(toLowerCase)) {
                            this.tzNames.put(toLowerCase, obj);
                        }
                    }
                }
            }
            for (String str2 : treeSet) {
                stringBuilder.append('|');
                FastDateParser.simpleQuote(stringBuilder, str2);
            }
            stringBuilder.append(")");
            createPattern(stringBuilder);
        }

        void setCalendar(FastDateParser fastDateParser, Calendar calendar, String str) {
            if (str.charAt(0) != '+') {
                if (str.charAt(0) != 45) {
                    if (str.regionMatches(true, 0, "GMT", 0, 3) != null) {
                        calendar.setTimeZone(TimeZone.getTimeZone(str.toUpperCase()));
                        return;
                    }
                    TzInfo tzInfo = (TzInfo) this.tzNames.get(str.toLowerCase(this.locale));
                    calendar.set(16, tzInfo.dstOffset);
                    calendar.set(15, tzInfo.zone.getRawOffset());
                    return;
                }
            }
            fastDateParser = new StringBuilder();
            fastDateParser.append("GMT");
            fastDateParser.append(str);
            calendar.setTimeZone(TimeZone.getTimeZone(fastDateParser.toString()));
        }
    }

    private static boolean isFormatLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    protected FastDateParser(String str, TimeZone timeZone, Locale locale) {
        this(str, timeZone, locale, null);
    }

    protected FastDateParser(String str, TimeZone timeZone, Locale locale, Date date) {
        this.pattern = str;
        this.timeZone = timeZone;
        this.locale = locale;
        str = Calendar.getInstance(timeZone, locale);
        if (date != null) {
            str.setTime(date);
            timeZone = str.get(1);
        } else if (locale.equals(JAPANESE_IMPERIAL) != null) {
            timeZone = null;
        } else {
            str.setTime(new Date());
            timeZone = str.get(1) - 80;
        }
        this.century = (timeZone / 100) * 100;
        this.startYear = timeZone - this.century;
        init(str);
    }

    private void init(Calendar calendar) {
        this.patterns = new ArrayList();
        StrategyParser strategyParser = new StrategyParser(this.pattern, calendar);
        while (true) {
            calendar = strategyParser.getNextStrategy();
            if (calendar != null) {
                this.patterns.add(calendar);
            } else {
                return;
            }
        }
    }

    public String getPattern() {
        return this.pattern;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof FastDateParser)) {
            return false;
        }
        FastDateParser fastDateParser = (FastDateParser) obj;
        if (this.pattern.equals(fastDateParser.pattern) && this.timeZone.equals(fastDateParser.timeZone) && this.locale.equals(fastDateParser.locale) != null) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.pattern.hashCode() + ((this.timeZone.hashCode() + (this.locale.hashCode() * 13)) * 13);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FastDateParser[");
        stringBuilder.append(this.pattern);
        stringBuilder.append(",");
        stringBuilder.append(this.locale);
        stringBuilder.append(",");
        stringBuilder.append(this.timeZone.getID());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(Calendar.getInstance(this.timeZone, this.locale));
    }

    public Object parseObject(String str) throws ParseException {
        return parse(str);
    }

    public Date parse(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Date parse = parse(str, parsePosition);
        if (parse != null) {
            return parse;
        }
        if (this.locale.equals(JAPANESE_IMPERIAL)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(The ");
            stringBuilder.append(this.locale);
            stringBuilder.append(" locale does not support dates before 1868 AD)\nUnparseable date: \"");
            stringBuilder.append(str);
            throw new ParseException(stringBuilder.toString(), parsePosition.getErrorIndex());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unparseable date: ");
        stringBuilder.append(str);
        throw new ParseException(stringBuilder.toString(), parsePosition.getErrorIndex());
    }

    public Object parseObject(String str, ParsePosition parsePosition) {
        return parse(str, parsePosition);
    }

    public Date parse(String str, ParsePosition parsePosition) {
        Calendar instance = Calendar.getInstance(this.timeZone, this.locale);
        instance.clear();
        return parse(str, parsePosition, instance) != null ? instance.getTime() : null;
    }

    public boolean parse(String str, ParsePosition parsePosition, Calendar calendar) {
        ListIterator listIterator = this.patterns.listIterator();
        while (listIterator.hasNext()) {
            StrategyAndWidth strategyAndWidth = (StrategyAndWidth) listIterator.next();
            if (!strategyAndWidth.strategy.parse(this, calendar, str, parsePosition, strategyAndWidth.getMaxWidth(listIterator))) {
                return null;
            }
        }
        return true;
    }

    private static StringBuilder simpleQuote(StringBuilder stringBuilder, String str) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '$':
                case '(':
                case ')':
                case '*':
                case '+':
                case '.':
                case '?':
                case '[':
                case '\\':
                case '^':
                case '{':
                case ModelPPM.MAX_FREQ /*124*/:
                    stringBuilder.append(IOUtils.DIR_SEPARATOR_WINDOWS);
                    break;
                default:
                    break;
            }
            stringBuilder.append(charAt);
        }
        return stringBuilder;
    }

    private static Map<String, Integer> appendDisplayNames(Calendar calendar, Locale locale, int i, StringBuilder stringBuilder) {
        Map<String, Integer> hashMap = new HashMap();
        calendar = calendar.getDisplayNames(i, 0, locale);
        i = new TreeSet(LONGER_FIRST_LOWERCASE);
        for (Entry entry : calendar.entrySet()) {
            String toLowerCase = ((String) entry.getKey()).toLowerCase(locale);
            if (i.add(toLowerCase)) {
                hashMap.put(toLowerCase, entry.getValue());
            }
        }
        calendar = i.iterator();
        while (calendar.hasNext() != null) {
            simpleQuote(stringBuilder, (String) calendar.next()).append(ModelPPM.MAX_FREQ);
        }
        return hashMap;
    }

    private int adjustYear(int i) {
        int i2 = this.century + i;
        return i >= this.startYear ? i2 : i2 + 100;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.commons.lang3.time.FastDateParser.Strategy getStrategy(char r2, int r3, java.util.Calendar r4) {
        /*
        r1 = this;
        switch(r2) {
            case 68: goto L_0x0086;
            case 69: goto L_0x0080;
            case 70: goto L_0x007d;
            case 71: goto L_0x0077;
            case 72: goto L_0x0074;
            default: goto L_0x0003;
        };
    L_0x0003:
        r0 = 2;
        switch(r2) {
            case 87: goto L_0x0071;
            case 88: goto L_0x006c;
            case 89: goto L_0x0064;
            case 90: goto L_0x0056;
            default: goto L_0x0007;
        };
    L_0x0007:
        switch(r2) {
            case 121: goto L_0x0064;
            case 122: goto L_0x005d;
            default: goto L_0x000a;
        };
    L_0x000a:
        switch(r2) {
            case 75: goto L_0x0053;
            case 77: goto L_0x0048;
            case 83: goto L_0x0045;
            case 97: goto L_0x003e;
            case 100: goto L_0x003b;
            case 104: goto L_0x0038;
            case 107: goto L_0x0035;
            case 109: goto L_0x0032;
            case 115: goto L_0x002f;
            case 117: goto L_0x002c;
            case 119: goto L_0x0029;
            default: goto L_0x000d;
        };
    L_0x000d:
        r3 = new java.lang.IllegalArgumentException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = "Format '";
        r4.append(r0);
        r4.append(r2);
        r2 = "' not supported";
        r4.append(r2);
        r2 = r4.toString();
        r3.<init>(r2);
        throw r3;
    L_0x0029:
        r2 = WEEK_OF_YEAR_STRATEGY;
        return r2;
    L_0x002c:
        r2 = DAY_OF_WEEK_STRATEGY;
        return r2;
    L_0x002f:
        r2 = SECOND_STRATEGY;
        return r2;
    L_0x0032:
        r2 = MINUTE_STRATEGY;
        return r2;
    L_0x0035:
        r2 = HOUR24_OF_DAY_STRATEGY;
        return r2;
    L_0x0038:
        r2 = HOUR12_STRATEGY;
        return r2;
    L_0x003b:
        r2 = DAY_OF_MONTH_STRATEGY;
        return r2;
    L_0x003e:
        r2 = 9;
        r2 = r1.getLocaleSpecificStrategy(r2, r4);
        return r2;
    L_0x0045:
        r2 = MILLISECOND_STRATEGY;
        return r2;
    L_0x0048:
        r2 = 3;
        if (r3 < r2) goto L_0x0050;
    L_0x004b:
        r2 = r1.getLocaleSpecificStrategy(r0, r4);
        goto L_0x0052;
    L_0x0050:
        r2 = NUMBER_MONTH_STRATEGY;
    L_0x0052:
        return r2;
    L_0x0053:
        r2 = HOUR_STRATEGY;
        return r2;
    L_0x0056:
        if (r3 != r0) goto L_0x005d;
    L_0x0058:
        r2 = org.apache.commons.lang3.time.FastDateParser.ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;
        return r2;
    L_0x005d:
        r2 = 15;
        r2 = r1.getLocaleSpecificStrategy(r2, r4);
        return r2;
    L_0x0064:
        if (r3 <= r0) goto L_0x0069;
    L_0x0066:
        r2 = LITERAL_YEAR_STRATEGY;
        goto L_0x006b;
    L_0x0069:
        r2 = ABBREVIATED_YEAR_STRATEGY;
    L_0x006b:
        return r2;
    L_0x006c:
        r2 = org.apache.commons.lang3.time.FastDateParser.ISO8601TimeZoneStrategy.getStrategy(r3);
        return r2;
    L_0x0071:
        r2 = WEEK_OF_MONTH_STRATEGY;
        return r2;
    L_0x0074:
        r2 = HOUR_OF_DAY_STRATEGY;
        return r2;
    L_0x0077:
        r2 = 0;
        r2 = r1.getLocaleSpecificStrategy(r2, r4);
        return r2;
    L_0x007d:
        r2 = DAY_OF_WEEK_IN_MONTH_STRATEGY;
        return r2;
    L_0x0080:
        r2 = 7;
        r2 = r1.getLocaleSpecificStrategy(r2, r4);
        return r2;
    L_0x0086:
        r2 = DAY_OF_YEAR_STRATEGY;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.time.FastDateParser.getStrategy(char, int, java.util.Calendar):org.apache.commons.lang3.time.FastDateParser$Strategy");
    }

    private static ConcurrentMap<Locale, Strategy> getCache(int i) {
        synchronized (caches) {
            if (caches[i] == null) {
                caches[i] = new ConcurrentHashMap(3);
            }
            i = caches[i];
        }
        return i;
    }

    private Strategy getLocaleSpecificStrategy(int i, Calendar calendar) {
        ConcurrentMap cache = getCache(i);
        Strategy strategy = (Strategy) cache.get(this.locale);
        if (strategy == null) {
            strategy = i == 15 ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(i, calendar, this.locale);
            Strategy strategy2 = (Strategy) cache.putIfAbsent(this.locale, strategy);
            if (strategy2 != null) {
                return strategy2;
            }
        }
        return strategy;
    }
}
