package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtils {
    private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap();
    private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap();

    static class SyncAvoid {
        private static final List<Locale> AVAILABLE_LOCALE_LIST;
        private static final Set<Locale> AVAILABLE_LOCALE_SET;

        SyncAvoid() {
        }

        static {
            Collection arrayList = new ArrayList(Arrays.asList(Locale.getAvailableLocales()));
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(arrayList);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet(arrayList));
        }
    }

    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return new Locale("", "");
        }
        if (str.contains("#")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid locale format: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        int length = str.length();
        if (length < 2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid locale format: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (str.charAt(0) != '_') {
            String[] split = str.split("_", -1);
            switch (split.length - 1) {
                case 0:
                    if (StringUtils.isAllLowerCase(str) && (length == 2 || length == 3)) {
                        return new Locale(str);
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid locale format: ");
                    stringBuilder.append(str);
                    throw new IllegalArgumentException(stringBuilder.toString());
                case 1:
                    if (StringUtils.isAllLowerCase(split[0]) && ((split[0].length() == 2 || split[0].length() == 3) && split[1].length() == 2 && StringUtils.isAllUpperCase(split[1]))) {
                        return new Locale(split[0], split[1]);
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid locale format: ");
                    stringBuilder.append(str);
                    throw new IllegalArgumentException(stringBuilder.toString());
                case 2:
                    if (StringUtils.isAllLowerCase(split[0]) && ((split[0].length() == 2 || split[0].length() == 3) && ((split[1].length() == 0 || (split[1].length() == 2 && StringUtils.isAllUpperCase(split[1]))) && split[2].length() > 0))) {
                        return new Locale(split[0], split[1], split[2]);
                    }
                default:
                    break;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid locale format: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (length < 3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid locale format: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else {
            char charAt = str.charAt(1);
            char charAt2 = str.charAt(2);
            if (Character.isUpperCase(charAt)) {
                if (Character.isUpperCase(charAt2)) {
                    if (length == 3) {
                        return new Locale("", str.substring(1, 3));
                    }
                    if (length < 5) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid locale format: ");
                        stringBuilder.append(str);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    } else if (str.charAt(3) == '_') {
                        return new Locale("", str.substring(1, 3), str.substring(4));
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid locale format: ");
                        stringBuilder.append(str);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid locale format: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static List<Locale> localeLookupList(Locale locale) {
        return localeLookupList(locale, locale);
    }

    public static List<Locale> localeLookupList(Locale locale, Locale locale2) {
        List arrayList = new ArrayList(4);
        if (locale != null) {
            arrayList.add(locale);
            if (locale.getVariant().length() > 0) {
                arrayList.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }
            if (locale.getCountry().length() > 0) {
                arrayList.add(new Locale(locale.getLanguage(), ""));
            }
            if (arrayList.contains(locale2) == null) {
                arrayList.add(locale2);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public static List<Locale> availableLocaleList() {
        return SyncAvoid.AVAILABLE_LOCALE_LIST;
    }

    public static Set<Locale> availableLocaleSet() {
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    public static boolean isAvailableLocale(Locale locale) {
        return availableLocaleList().contains(locale);
    }

    public static List<Locale> languagesByCountry(String str) {
        if (str == null) {
            return Collections.emptyList();
        }
        List<Locale> list = (List) cLanguagesByCountry.get(str);
        if (list == null) {
            List arrayList = new ArrayList();
            List availableLocaleList = availableLocaleList();
            for (int i = 0; i < availableLocaleList.size(); i++) {
                Locale locale = (Locale) availableLocaleList.get(i);
                if (str.equals(locale.getCountry()) && locale.getVariant().isEmpty()) {
                    arrayList.add(locale);
                }
            }
            cLanguagesByCountry.putIfAbsent(str, Collections.unmodifiableList(arrayList));
            list = (List) cLanguagesByCountry.get(str);
        }
        return list;
    }

    public static List<Locale> countriesByLanguage(String str) {
        if (str == null) {
            return Collections.emptyList();
        }
        List<Locale> list = (List) cCountriesByLanguage.get(str);
        if (list == null) {
            List arrayList = new ArrayList();
            List availableLocaleList = availableLocaleList();
            for (int i = 0; i < availableLocaleList.size(); i++) {
                Locale locale = (Locale) availableLocaleList.get(i);
                if (str.equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().isEmpty()) {
                    arrayList.add(locale);
                }
            }
            cCountriesByLanguage.putIfAbsent(str, Collections.unmodifiableList(arrayList));
            list = (List) cCountriesByLanguage.get(str);
        }
        return list;
    }
}
