package org.threeten.bp.format;

import java.text.DateFormatSymbols;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.IsoFields;
import org.threeten.bp.temporal.TemporalField;

final class SimpleDateTimeTextProvider extends DateTimeTextProvider {
    private static final ConcurrentMap<Entry<TemporalField, Locale>, Object> CACHE = new ConcurrentHashMap(16, 0.75f, 2);
    private static final Comparator<Entry<String, Long>> COMPARATOR = new C15621();

    /* renamed from: org.threeten.bp.format.SimpleDateTimeTextProvider$1 */
    static class C15621 implements Comparator<Entry<String, Long>> {
        C15621() {
        }

        public int compare(Entry<String, Long> entry, Entry<String, Long> entry2) {
            return ((String) entry2.getKey()).length() - ((String) entry.getKey()).length();
        }
    }

    static final class LocaleStore {
        private final Map<TextStyle, List<Entry<String, Long>>> parsable;
        private final Map<TextStyle, Map<Long, String>> valueTextMap;

        LocaleStore(Map<TextStyle, Map<Long, String>> map) {
            this.valueTextMap = map;
            Map hashMap = new HashMap();
            List arrayList = new ArrayList();
            for (TextStyle textStyle : map.keySet()) {
                Map hashMap2 = new HashMap();
                for (Entry entry : ((Map) map.get(textStyle)).entrySet()) {
                    Object put = hashMap2.put(entry.getValue(), SimpleDateTimeTextProvider.createEntry(entry.getValue(), entry.getKey()));
                }
                Collection arrayList2 = new ArrayList(hashMap2.values());
                Collections.sort(arrayList2, SimpleDateTimeTextProvider.COMPARATOR);
                hashMap.put(textStyle, arrayList2);
                arrayList.addAll(arrayList2);
                hashMap.put(null, arrayList);
            }
            Collections.sort(arrayList, SimpleDateTimeTextProvider.COMPARATOR);
            this.parsable = hashMap;
        }

        String getText(long j, TextStyle textStyle) {
            Map map = (Map) this.valueTextMap.get(textStyle);
            return map != null ? (String) map.get(Long.valueOf(j)) : 0;
        }

        Iterator<Entry<String, Long>> getTextIterator(TextStyle textStyle) {
            List list = (List) this.parsable.get(textStyle);
            return list != null ? list.iterator() : null;
        }
    }

    SimpleDateTimeTextProvider() {
    }

    public Locale[] getAvailableLocales() {
        return DateFormatSymbols.getAvailableLocales();
    }

    public String getText(TemporalField temporalField, long j, TextStyle textStyle, Locale locale) {
        temporalField = findStore(temporalField, locale);
        return (temporalField instanceof LocaleStore) != null ? ((LocaleStore) temporalField).getText(j, textStyle) : null;
    }

    public Iterator<Entry<String, Long>> getTextIterator(TemporalField temporalField, TextStyle textStyle, Locale locale) {
        temporalField = findStore(temporalField, locale);
        return (temporalField instanceof LocaleStore) != null ? ((LocaleStore) temporalField).getTextIterator(textStyle) : null;
    }

    private Object findStore(TemporalField temporalField, Locale locale) {
        Entry createEntry = createEntry(temporalField, locale);
        Object obj = CACHE.get(createEntry);
        if (obj != null) {
            return obj;
        }
        CACHE.putIfAbsent(createEntry, createStore(temporalField, locale));
        return CACHE.get(createEntry);
    }

    private Object createStore(TemporalField temporalField, Locale locale) {
        TemporalField temporalField2 = temporalField;
        DateFormatSymbols instance;
        Map hashMap;
        Long valueOf;
        Long valueOf2;
        Long valueOf3;
        Long valueOf4;
        Long valueOf5;
        Long valueOf6;
        Long valueOf7;
        String[] months;
        Map hashMap2;
        Map map;
        if (temporalField2 == ChronoField.MONTH_OF_YEAR) {
            instance = DateFormatSymbols.getInstance(locale);
            hashMap = new HashMap();
            Long valueOf8 = Long.valueOf(1);
            valueOf = Long.valueOf(2);
            valueOf2 = Long.valueOf(3);
            valueOf3 = Long.valueOf(4);
            valueOf4 = Long.valueOf(5);
            valueOf5 = Long.valueOf(6);
            valueOf6 = Long.valueOf(7);
            Long valueOf9 = Long.valueOf(8);
            Long valueOf10 = Long.valueOf(9);
            Long valueOf11 = Long.valueOf(10);
            Long l = valueOf9;
            Long valueOf12 = Long.valueOf(11);
            valueOf7 = Long.valueOf(12);
            months = instance.getMonths();
            hashMap2 = new HashMap();
            hashMap2.put(valueOf8, months[0]);
            hashMap2.put(valueOf, months[1]);
            hashMap2.put(valueOf2, months[2]);
            hashMap2.put(valueOf3, months[3]);
            hashMap2.put(valueOf4, months[4]);
            hashMap2.put(valueOf5, months[5]);
            hashMap2.put(valueOf6, months[6]);
            Long l2 = l;
            hashMap2.put(l2, months[7]);
            DateFormatSymbols dateFormatSymbols = instance;
            hashMap2.put(valueOf10, months[8]);
            hashMap2.put(valueOf11, months[9]);
            Long l3 = valueOf12;
            hashMap2.put(l3, months[10]);
            hashMap2.put(valueOf7, months[11]);
            hashMap.put(TextStyle.FULL, hashMap2);
            hashMap2 = new HashMap();
            Map map2 = hashMap;
            Long l4 = valueOf7;
            hashMap2.put(valueOf8, months[0].substring(0, 1));
            hashMap2.put(valueOf, months[1].substring(0, 1));
            Long l5 = valueOf;
            hashMap2.put(valueOf2, months[2].substring(0, 1));
            hashMap2.put(valueOf3, months[3].substring(0, 1));
            hashMap2.put(valueOf4, months[4].substring(0, 1));
            hashMap2.put(valueOf5, months[5].substring(0, 1));
            hashMap2.put(valueOf6, months[6].substring(0, 1));
            hashMap2.put(l2, months[7].substring(0, 1));
            hashMap2.put(valueOf10, months[8].substring(0, 1));
            hashMap2.put(valueOf11, months[9].substring(0, 1));
            hashMap2.put(l3, months[10].substring(0, 1));
            valueOf = l4;
            hashMap2.put(valueOf, months[11].substring(0, 1));
            map = map2;
            map.put(TextStyle.NARROW, hashMap2);
            String[] shortMonths = dateFormatSymbols.getShortMonths();
            hashMap2 = new HashMap();
            hashMap2.put(valueOf8, shortMonths[0]);
            hashMap2.put(l5, shortMonths[1]);
            hashMap2.put(valueOf2, shortMonths[2]);
            hashMap2.put(valueOf3, shortMonths[3]);
            hashMap2.put(valueOf4, shortMonths[4]);
            hashMap2.put(valueOf5, shortMonths[5]);
            hashMap2.put(valueOf6, shortMonths[6]);
            hashMap2.put(l2, shortMonths[7]);
            hashMap2.put(valueOf10, shortMonths[8]);
            hashMap2.put(valueOf11, shortMonths[9]);
            hashMap2.put(l3, shortMonths[10]);
            hashMap2.put(valueOf, shortMonths[11]);
            map.put(TextStyle.SHORT, hashMap2);
            return createLocaleStore(map);
        } else if (temporalField2 == ChronoField.DAY_OF_WEEK) {
            instance = DateFormatSymbols.getInstance(locale);
            hashMap = new HashMap();
            valueOf7 = Long.valueOf(1);
            valueOf = Long.valueOf(2);
            valueOf2 = Long.valueOf(3);
            valueOf3 = Long.valueOf(4);
            valueOf4 = Long.valueOf(5);
            valueOf5 = Long.valueOf(6);
            valueOf6 = Long.valueOf(7);
            months = instance.getWeekdays();
            hashMap2 = new HashMap();
            hashMap2.put(valueOf7, months[2]);
            hashMap2.put(valueOf, months[3]);
            hashMap2.put(valueOf2, months[4]);
            hashMap2.put(valueOf3, months[5]);
            hashMap2.put(valueOf4, months[6]);
            hashMap2.put(valueOf5, months[7]);
            hashMap2.put(valueOf6, months[1]);
            hashMap.put(TextStyle.FULL, hashMap2);
            hashMap2 = new HashMap();
            hashMap2.put(valueOf7, months[2].substring(0, 1));
            hashMap2.put(valueOf, months[3].substring(0, 1));
            hashMap2.put(valueOf2, months[4].substring(0, 1));
            hashMap2.put(valueOf3, months[5].substring(0, 1));
            hashMap2.put(valueOf4, months[6].substring(0, 1));
            hashMap2.put(valueOf5, months[7].substring(0, 1));
            hashMap2.put(valueOf6, months[1].substring(0, 1));
            hashMap.put(TextStyle.NARROW, hashMap2);
            r0 = instance.getShortWeekdays();
            map = new HashMap();
            map.put(valueOf7, r0[2]);
            map.put(valueOf, r0[3]);
            map.put(valueOf2, r0[4]);
            map.put(valueOf3, r0[5]);
            map.put(valueOf4, r0[6]);
            map.put(valueOf5, r0[7]);
            map.put(valueOf6, r0[1]);
            hashMap.put(TextStyle.SHORT, map);
            return createLocaleStore(hashMap);
        } else if (temporalField2 == ChronoField.AMPM_OF_DAY) {
            instance = DateFormatSymbols.getInstance(locale);
            hashMap = new HashMap();
            r0 = instance.getAmPmStrings();
            r2 = new HashMap();
            r2.put(Long.valueOf(0), r0[0]);
            r2.put(Long.valueOf(1), r0[1]);
            hashMap.put(TextStyle.FULL, r2);
            hashMap.put(TextStyle.SHORT, r2);
            return createLocaleStore(hashMap);
        } else if (temporalField2 == ChronoField.ERA) {
            instance = DateFormatSymbols.getInstance(locale);
            hashMap = new HashMap();
            r0 = instance.getEras();
            r2 = new HashMap();
            r2.put(Long.valueOf(0), r0[0]);
            r2.put(Long.valueOf(1), r0[1]);
            hashMap.put(TextStyle.SHORT, r2);
            if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
                r2 = new HashMap();
                r2.put(Long.valueOf(0), "Before Christ");
                r2.put(Long.valueOf(1), "Anno Domini");
                hashMap.put(TextStyle.FULL, r2);
            } else {
                hashMap.put(TextStyle.FULL, r2);
            }
            r2 = new HashMap();
            r2.put(Long.valueOf(0), r0[0].substring(0, 1));
            r2.put(Long.valueOf(1), r0[1].substring(0, 1));
            hashMap.put(TextStyle.NARROW, r2);
            return createLocaleStore(hashMap);
        } else if (temporalField2 != IsoFields.QUARTER_OF_YEAR) {
            return "";
        } else {
            Map hashMap3 = new HashMap();
            hashMap = new HashMap();
            hashMap.put(Long.valueOf(1), "Q1");
            hashMap.put(Long.valueOf(2), "Q2");
            hashMap.put(Long.valueOf(3), "Q3");
            hashMap.put(Long.valueOf(4), "Q4");
            hashMap3.put(TextStyle.SHORT, hashMap);
            hashMap = new HashMap();
            hashMap.put(Long.valueOf(1), "1st quarter");
            hashMap.put(Long.valueOf(2), "2nd quarter");
            hashMap.put(Long.valueOf(3), "3rd quarter");
            hashMap.put(Long.valueOf(4), "4th quarter");
            hashMap3.put(TextStyle.FULL, hashMap);
            return createLocaleStore(hashMap3);
        }
    }

    private static <A, B> Entry<A, B> createEntry(A a, B b) {
        return new SimpleImmutableEntry(a, b);
    }

    private static LocaleStore createLocaleStore(Map<TextStyle, Map<Long, String>> map) {
        map.put(TextStyle.FULL_STANDALONE, map.get(TextStyle.FULL));
        map.put(TextStyle.SHORT_STANDALONE, map.get(TextStyle.SHORT));
        if (map.containsKey(TextStyle.NARROW) && !map.containsKey(TextStyle.NARROW_STANDALONE)) {
            map.put(TextStyle.NARROW_STANDALONE, map.get(TextStyle.NARROW));
        }
        return new LocaleStore(map);
    }
}
