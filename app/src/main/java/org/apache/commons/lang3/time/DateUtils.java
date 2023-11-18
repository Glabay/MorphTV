package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static final long MILLIS_PER_DAY = 86400000;
    public static final long MILLIS_PER_HOUR = 3600000;
    public static final long MILLIS_PER_MINUTE = 60000;
    public static final long MILLIS_PER_SECOND = 1000;
    public static final int RANGE_MONTH_MONDAY = 6;
    public static final int RANGE_MONTH_SUNDAY = 5;
    public static final int RANGE_WEEK_CENTER = 4;
    public static final int RANGE_WEEK_MONDAY = 2;
    public static final int RANGE_WEEK_RELATIVE = 3;
    public static final int RANGE_WEEK_SUNDAY = 1;
    public static final int SEMI_MONTH = 1001;
    private static final int[][] fields;

    static class DateIterator implements Iterator<Calendar> {
        private final Calendar endFinal;
        private final Calendar spot;

        DateIterator(Calendar calendar, Calendar calendar2) {
            this.endFinal = calendar2;
            this.spot = calendar;
            this.spot.add(5, -1);
        }

        public boolean hasNext() {
            return this.spot.before(this.endFinal);
        }

        public Calendar next() {
            if (this.spot.equals(this.endFinal)) {
                throw new NoSuchElementException();
            }
            this.spot.add(5, 1);
            return (Calendar) this.spot.clone();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private enum ModifyType {
        TRUNCATE,
        ROUND,
        CEILING
    }

    static {
        r0 = new int[8][];
        r0[0] = new int[]{14};
        r0[1] = new int[]{13};
        r0[2] = new int[]{12};
        r0[3] = new int[]{11, 10};
        r0[4] = new int[]{5, 5, 9};
        r0[5] = new int[]{2, 1001};
        r0[6] = new int[]{1};
        r0[7] = new int[]{0};
        fields = r0;
    }

    public static boolean isSameDay(Date date, Date date2) {
        if (date != null) {
            if (date2 != null) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                Calendar instance2 = Calendar.getInstance();
                instance2.setTime(date2);
                return isSameDay(instance, instance2);
            }
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static boolean isSameDay(Calendar calendar, Calendar calendar2) {
        if (calendar != null) {
            if (calendar2 != null) {
                if (calendar.get(0) == calendar2.get(0) && calendar.get(1) == calendar2.get(1) && calendar.get(6) == calendar2.get(6)) {
                    return true;
                }
                return false;
            }
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static boolean isSameInstant(Date date, Date date2) {
        if (date != null) {
            if (date2 != null) {
                return date.getTime() == date2.getTime() ? true : null;
            }
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static boolean isSameInstant(Calendar calendar, Calendar calendar2) {
        if (calendar != null) {
            if (calendar2 != null) {
                return calendar.getTime().getTime() == calendar2.getTime().getTime() ? true : null;
            }
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static boolean isSameLocalTime(Calendar calendar, Calendar calendar2) {
        if (calendar != null) {
            if (calendar2 != null) {
                if (calendar.get(14) == calendar2.get(14) && calendar.get(13) == calendar2.get(13) && calendar.get(12) == calendar2.get(12) && calendar.get(11) == calendar2.get(11) && calendar.get(6) == calendar2.get(6) && calendar.get(1) == calendar2.get(1) && calendar.get(0) == calendar2.get(0) && calendar.getClass() == calendar2.getClass()) {
                    return true;
                }
                return false;
            }
        }
        throw new IllegalArgumentException("The date must not be null");
    }

    public static Date parseDate(String str, String... strArr) throws ParseException {
        return parseDate(str, null, strArr);
    }

    public static Date parseDate(String str, Locale locale, String... strArr) throws ParseException {
        return parseDateWithLeniency(str, locale, strArr, true);
    }

    public static Date parseDateStrictly(String str, String... strArr) throws ParseException {
        return parseDateStrictly(str, null, strArr);
    }

    public static Date parseDateStrictly(String str, Locale locale, String... strArr) throws ParseException {
        return parseDateWithLeniency(str, locale, strArr, false);
    }

    private static java.util.Date parseDateWithLeniency(java.lang.String r7, java.util.Locale r8, java.lang.String[] r9, boolean r10) throws java.text.ParseException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r7 == 0) goto L_0x005d;
    L_0x0002:
        if (r9 != 0) goto L_0x0005;
    L_0x0004:
        goto L_0x005d;
    L_0x0005:
        r0 = java.util.TimeZone.getDefault();
        if (r8 != 0) goto L_0x000f;
    L_0x000b:
        r8 = java.util.Locale.getDefault();
    L_0x000f:
        r1 = new java.text.ParsePosition;
        r2 = 0;
        r1.<init>(r2);
        r3 = java.util.Calendar.getInstance(r0, r8);
        r3.setLenient(r10);
        r10 = r9.length;
        r4 = 0;
    L_0x001e:
        if (r4 >= r10) goto L_0x0045;
    L_0x0020:
        r5 = r9[r4];
        r6 = new org.apache.commons.lang3.time.FastDateParser;
        r6.<init>(r5, r0, r8);
        r3.clear();
        r5 = r6.parse(r7, r1, r3);	 Catch:{ IllegalArgumentException -> 0x003f }
        if (r5 == 0) goto L_0x003f;	 Catch:{ IllegalArgumentException -> 0x003f }
    L_0x0030:
        r5 = r1.getIndex();	 Catch:{ IllegalArgumentException -> 0x003f }
        r6 = r7.length();	 Catch:{ IllegalArgumentException -> 0x003f }
        if (r5 != r6) goto L_0x003f;	 Catch:{ IllegalArgumentException -> 0x003f }
    L_0x003a:
        r5 = r3.getTime();	 Catch:{ IllegalArgumentException -> 0x003f }
        return r5;
    L_0x003f:
        r1.setIndex(r2);
        r4 = r4 + 1;
        goto L_0x001e;
    L_0x0045:
        r8 = new java.text.ParseException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Unable to parse the date: ";
        r9.append(r10);
        r9.append(r7);
        r7 = r9.toString();
        r9 = -1;
        r8.<init>(r7, r9);
        throw r8;
    L_0x005d:
        r7 = new java.lang.IllegalArgumentException;
        r8 = "Date and Patterns must not be null";
        r7.<init>(r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.time.DateUtils.parseDateWithLeniency(java.lang.String, java.util.Locale, java.lang.String[], boolean):java.util.Date");
    }

    public static Date addYears(Date date, int i) {
        return add(date, 1, i);
    }

    public static Date addMonths(Date date, int i) {
        return add(date, 2, i);
    }

    public static Date addWeeks(Date date, int i) {
        return add(date, 3, i);
    }

    public static Date addDays(Date date, int i) {
        return add(date, 5, i);
    }

    public static Date addHours(Date date, int i) {
        return add(date, 11, i);
    }

    public static Date addMinutes(Date date, int i) {
        return add(date, 12, i);
    }

    public static Date addSeconds(Date date, int i) {
        return add(date, 13, i);
    }

    public static Date addMilliseconds(Date date, int i) {
        return add(date, 14, i);
    }

    private static Date add(Date date, int i, int i2) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(i, i2);
        return instance.getTime();
    }

    public static Date setYears(Date date, int i) {
        return set(date, 1, i);
    }

    public static Date setMonths(Date date, int i) {
        return set(date, 2, i);
    }

    public static Date setDays(Date date, int i) {
        return set(date, 5, i);
    }

    public static Date setHours(Date date, int i) {
        return set(date, 11, i);
    }

    public static Date setMinutes(Date date, int i) {
        return set(date, 12, i);
    }

    public static Date setSeconds(Date date, int i) {
        return set(date, 13, i);
    }

    public static Date setMilliseconds(Date date, int i) {
        return set(date, 14, i);
    }

    private static Date set(Date date, int i, int i2) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar instance = Calendar.getInstance();
        instance.setLenient(false);
        instance.setTime(date);
        instance.set(i, i2);
        return instance.getTime();
    }

    public static Calendar toCalendar(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    public static Calendar toCalendar(Date date, TimeZone timeZone) {
        timeZone = Calendar.getInstance(timeZone);
        timeZone.setTime(date);
        return timeZone;
    }

    public static Date round(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        modify(instance, i, ModifyType.ROUND);
        return instance.getTime();
    }

    public static Calendar round(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        calendar = (Calendar) calendar.clone();
        modify(calendar, i, ModifyType.ROUND);
        return calendar;
    }

    public static Date round(Object obj, int i) {
        if (obj == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (obj instanceof Date) {
            return round((Date) obj, i);
        } else {
            if (obj instanceof Calendar) {
                return round((Calendar) obj, i).getTime();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not round ");
            stringBuilder.append(obj);
            throw new ClassCastException(stringBuilder.toString());
        }
    }

    public static Date truncate(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        modify(instance, i, ModifyType.TRUNCATE);
        return instance.getTime();
    }

    public static Calendar truncate(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        calendar = (Calendar) calendar.clone();
        modify(calendar, i, ModifyType.TRUNCATE);
        return calendar;
    }

    public static Date truncate(Object obj, int i) {
        if (obj == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (obj instanceof Date) {
            return truncate((Date) obj, i);
        } else {
            if (obj instanceof Calendar) {
                return truncate((Calendar) obj, i).getTime();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not truncate ");
            stringBuilder.append(obj);
            throw new ClassCastException(stringBuilder.toString());
        }
    }

    public static Date ceiling(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        modify(instance, i, ModifyType.CEILING);
        return instance.getTime();
    }

    public static Calendar ceiling(Calendar calendar, int i) {
        if (calendar == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        calendar = (Calendar) calendar.clone();
        modify(calendar, i, ModifyType.CEILING);
        return calendar;
    }

    public static Date ceiling(Object obj, int i) {
        if (obj == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (obj instanceof Date) {
            return ceiling((Date) obj, i);
        } else {
            if (obj instanceof Calendar) {
                return ceiling((Calendar) obj, i).getTime();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not find ceiling of for type: ");
            stringBuilder.append(obj.getClass());
            throw new ClassCastException(stringBuilder.toString());
        }
    }

    private static void modify(Calendar calendar, int i, ModifyType modifyType) {
        Calendar calendar2 = calendar;
        int i2 = i;
        ModifyType modifyType2 = modifyType;
        if (calendar2.get(1) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        } else if (i2 != 14) {
            Date time = calendar.getTime();
            long time2 = time.getTime();
            int i3 = calendar2.get(14);
            if (ModifyType.TRUNCATE == modifyType2 || i3 < 500) {
                time2 -= (long) i3;
            }
            Object obj = i2 == 13 ? 1 : null;
            i3 = calendar2.get(13);
            long j = (obj != null || (ModifyType.TRUNCATE != modifyType2 && i3 >= 30)) ? time2 : time2 - (((long) i3) * 1000);
            if (i2 == 12) {
                obj = 1;
            }
            int i4 = calendar2.get(12);
            if (obj == null && (ModifyType.TRUNCATE == modifyType2 || i4 < 30)) {
                j -= ((long) i4) * 60000;
            }
            if (time.getTime() != j) {
                time.setTime(j);
                calendar2.setTime(time);
            }
            obj = null;
            for (int[] iArr : fields) {
                int i5;
                int i6;
                Object obj2;
                Object obj3;
                int i7;
                Object obj4;
                for (int i62 : iArr) {
                    if (i62 == i2) {
                        if (modifyType2 == ModifyType.CEILING || (modifyType2 == ModifyType.ROUND && obj != null)) {
                            if (i2 == 1001) {
                                if (calendar2.get(5) == 1) {
                                    calendar2.add(5, 15);
                                } else {
                                    calendar2.add(5, -15);
                                    calendar2.add(2, 1);
                                }
                            } else if (i2 != 9) {
                                calendar2.add(iArr[0], 1);
                            } else if (calendar2.get(11) == 0) {
                                calendar2.add(11, 12);
                            } else {
                                calendar2.add(11, -12);
                                calendar2.add(5, 1);
                            }
                        }
                        return;
                    }
                }
                if (i2 != 9) {
                    if (i2 == 1001 && iArr[0] == 5) {
                        i3 = calendar2.get(5) - 1;
                        if (i3 >= 15) {
                            i3 -= 15;
                        }
                        int i8 = i3;
                        obj2 = i8 > 7 ? 1 : null;
                        obj3 = 1;
                        i7 = i8;
                        if (obj3 == null) {
                            i62 = 0;
                            i3 = calendar2.getActualMinimum(iArr[0]);
                            i5 = calendar2.get(iArr[0]) - i3;
                            obj = i5 > (calendar2.getActualMaximum(iArr[0]) - i3) / 2 ? 1 : null;
                        } else {
                            i62 = 0;
                            obj4 = obj2;
                            i5 = i7;
                            obj = obj4;
                        }
                        if (i5 == 0) {
                            calendar2.set(iArr[i62], calendar2.get(iArr[i62]) - i5);
                        }
                    }
                } else if (iArr[0] == 11) {
                    i3 = calendar2.get(11);
                    if (i3 >= 12) {
                        i3 -= 12;
                    }
                    obj2 = i3 >= 6 ? 1 : null;
                    i7 = i3;
                    obj3 = 1;
                    if (obj3 == null) {
                        i62 = 0;
                        obj4 = obj2;
                        i5 = i7;
                        obj = obj4;
                    } else {
                        i62 = 0;
                        i3 = calendar2.getActualMinimum(iArr[0]);
                        i5 = calendar2.get(iArr[0]) - i3;
                        if (i5 > (calendar2.getActualMaximum(iArr[0]) - i3) / 2) {
                        }
                        obj = i5 > (calendar2.getActualMaximum(iArr[0]) - i3) / 2 ? 1 : null;
                    }
                    if (i5 == 0) {
                        calendar2.set(iArr[i62], calendar2.get(iArr[i62]) - i5);
                    }
                }
                obj2 = obj;
                obj3 = null;
                i7 = 0;
                if (obj3 == null) {
                    i62 = 0;
                    i3 = calendar2.getActualMinimum(iArr[0]);
                    i5 = calendar2.get(iArr[0]) - i3;
                    if (i5 > (calendar2.getActualMaximum(iArr[0]) - i3) / 2) {
                    }
                    obj = i5 > (calendar2.getActualMaximum(iArr[0]) - i3) / 2 ? 1 : null;
                } else {
                    i62 = 0;
                    obj4 = obj2;
                    i5 = i7;
                    obj = obj4;
                }
                if (i5 == 0) {
                    calendar2.set(iArr[i62], calendar2.get(iArr[i62]) - i5);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The field ");
            stringBuilder.append(i2);
            stringBuilder.append(" is not supported");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static Iterator<Calendar> iterator(Date date, int i) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return iterator(instance, i);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Iterator<java.util.Calendar> iterator(java.util.Calendar r7, int r8) {
        /*
        if (r7 != 0) goto L_0x000a;
    L_0x0002:
        r7 = new java.lang.IllegalArgumentException;
        r8 = "The date must not be null";
        r7.<init>(r8);
        throw r7;
    L_0x000a:
        r0 = -1;
        r1 = 2;
        r2 = 5;
        r3 = 1;
        r4 = 7;
        switch(r8) {
            case 1: goto L_0x0049;
            case 2: goto L_0x0049;
            case 3: goto L_0x0049;
            case 4: goto L_0x0049;
            case 5: goto L_0x002e;
            case 6: goto L_0x002e;
            default: goto L_0x0012;
        };
    L_0x0012:
        r7 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "The range style ";
        r0.append(r1);
        r0.append(r8);
        r8 = " is not valid.";
        r0.append(r8);
        r8 = r0.toString();
        r7.<init>(r8);
        throw r7;
    L_0x002e:
        r7 = truncate(r7, r1);
        r5 = r7.clone();
        r5 = (java.util.Calendar) r5;
        r5.add(r1, r3);
        r5.add(r2, r0);
        r6 = 6;
        if (r8 != r6) goto L_0x0044;
    L_0x0041:
        r6 = r5;
        r5 = r7;
        goto L_0x0069;
    L_0x0044:
        r6 = r5;
        r1 = 1;
        r5 = r7;
        r7 = 7;
        goto L_0x006d;
    L_0x0049:
        r5 = truncate(r7, r2);
        r6 = truncate(r7, r2);
        switch(r8) {
            case 1: goto L_0x006b;
            case 2: goto L_0x0069;
            case 3: goto L_0x0062;
            case 4: goto L_0x0055;
            default: goto L_0x0054;
        };
    L_0x0054:
        goto L_0x006b;
    L_0x0055:
        r8 = r7.get(r4);
        r1 = r8 + -3;
        r7 = r7.get(r4);
        r7 = r7 + 3;
        goto L_0x006d;
    L_0x0062:
        r1 = r7.get(r4);
        r7 = r1 + -1;
        goto L_0x006d;
    L_0x0069:
        r7 = 1;
        goto L_0x006d;
    L_0x006b:
        r7 = 7;
        r1 = 1;
    L_0x006d:
        if (r1 >= r3) goto L_0x0071;
    L_0x006f:
        r1 = r1 + 7;
    L_0x0071:
        if (r1 <= r4) goto L_0x0075;
    L_0x0073:
        r1 = r1 + -7;
    L_0x0075:
        if (r7 >= r3) goto L_0x0079;
    L_0x0077:
        r7 = r7 + 7;
    L_0x0079:
        if (r7 <= r4) goto L_0x007d;
    L_0x007b:
        r7 = r7 + -7;
    L_0x007d:
        r8 = r5.get(r4);
        if (r8 == r1) goto L_0x0087;
    L_0x0083:
        r5.add(r2, r0);
        goto L_0x007d;
    L_0x0087:
        r8 = r6.get(r4);
        if (r8 == r7) goto L_0x0091;
    L_0x008d:
        r6.add(r2, r3);
        goto L_0x0087;
    L_0x0091:
        r7 = new org.apache.commons.lang3.time.DateUtils$DateIterator;
        r7.<init>(r5, r6);
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.time.DateUtils.iterator(java.util.Calendar, int):java.util.Iterator<java.util.Calendar>");
    }

    public static Iterator<?> iterator(Object obj, int i) {
        if (obj == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (obj instanceof Date) {
            return iterator((Date) obj, i);
        } else {
            if (obj instanceof Calendar) {
                return iterator((Calendar) obj, i);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not iterate based on ");
            stringBuilder.append(obj);
            throw new ClassCastException(stringBuilder.toString());
        }
    }

    public static long getFragmentInMilliseconds(Date date, int i) {
        return getFragment(date, i, TimeUnit.MILLISECONDS);
    }

    public static long getFragmentInSeconds(Date date, int i) {
        return getFragment(date, i, TimeUnit.SECONDS);
    }

    public static long getFragmentInMinutes(Date date, int i) {
        return getFragment(date, i, TimeUnit.MINUTES);
    }

    public static long getFragmentInHours(Date date, int i) {
        return getFragment(date, i, TimeUnit.HOURS);
    }

    public static long getFragmentInDays(Date date, int i) {
        return getFragment(date, i, TimeUnit.DAYS);
    }

    public static long getFragmentInMilliseconds(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.MILLISECONDS);
    }

    public static long getFragmentInSeconds(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.SECONDS);
    }

    public static long getFragmentInMinutes(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.MINUTES);
    }

    public static long getFragmentInHours(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.HOURS);
    }

    public static long getFragmentInDays(Calendar calendar, int i) {
        return getFragment(calendar, i, TimeUnit.DAYS);
    }

    private static long getFragment(Date date, int i, TimeUnit timeUnit) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return getFragment(instance, i, timeUnit);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long getFragment(java.util.Calendar r6, int r7, java.util.concurrent.TimeUnit r8) {
        /*
        if (r6 != 0) goto L_0x000a;
    L_0x0002:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "The date must not be null";
        r6.<init>(r7);
        throw r6;
    L_0x000a:
        r0 = 0;
        r2 = java.util.concurrent.TimeUnit.DAYS;
        if (r8 != r2) goto L_0x0012;
    L_0x0010:
        r2 = 0;
        goto L_0x0013;
    L_0x0012:
        r2 = 1;
    L_0x0013:
        switch(r7) {
            case 1: goto L_0x0028;
            case 2: goto L_0x0018;
            default: goto L_0x0016;
        };
    L_0x0016:
        r4 = r0;
        goto L_0x0037;
    L_0x0018:
        r3 = 5;
        r3 = r6.get(r3);
        r3 = r3 - r2;
        r2 = (long) r3;
        r4 = java.util.concurrent.TimeUnit.DAYS;
        r2 = r8.convert(r2, r4);
        r4 = r2 + r0;
        goto L_0x0037;
    L_0x0028:
        r3 = 6;
        r3 = r6.get(r3);
        r3 = r3 - r2;
        r2 = (long) r3;
        r4 = java.util.concurrent.TimeUnit.DAYS;
        r2 = r8.convert(r2, r4);
        r4 = r2 + r0;
    L_0x0037:
        switch(r7) {
            case 1: goto L_0x005a;
            case 2: goto L_0x005a;
            case 3: goto L_0x003a;
            case 4: goto L_0x003a;
            case 5: goto L_0x005a;
            case 6: goto L_0x005a;
            case 7: goto L_0x003a;
            case 8: goto L_0x003a;
            case 9: goto L_0x003a;
            case 10: goto L_0x003a;
            case 11: goto L_0x0058;
            case 12: goto L_0x0078;
            case 13: goto L_0x0056;
            case 14: goto L_0x0096;
            default: goto L_0x003a;
        };
    L_0x003a:
        r6 = new java.lang.IllegalArgumentException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r0 = "The fragment ";
        r8.append(r0);
        r8.append(r7);
        r7 = " is not supported";
        r8.append(r7);
        r7 = r8.toString();
        r6.<init>(r7);
        throw r6;
    L_0x0056:
        r2 = r4;
        goto L_0x0087;
    L_0x0058:
        r2 = r4;
        goto L_0x0069;
    L_0x005a:
        r7 = 11;
        r7 = r6.get(r7);
        r0 = (long) r7;
        r7 = java.util.concurrent.TimeUnit.HOURS;
        r0 = r8.convert(r0, r7);
        r2 = r4 + r0;
    L_0x0069:
        r7 = 12;
        r7 = r6.get(r7);
        r0 = (long) r7;
        r7 = java.util.concurrent.TimeUnit.MINUTES;
        r0 = r8.convert(r0, r7);
        r4 = r2 + r0;
    L_0x0078:
        r7 = 13;
        r7 = r6.get(r7);
        r0 = (long) r7;
        r7 = java.util.concurrent.TimeUnit.SECONDS;
        r0 = r8.convert(r0, r7);
        r2 = r4 + r0;
    L_0x0087:
        r7 = 14;
        r6 = r6.get(r7);
        r6 = (long) r6;
        r0 = java.util.concurrent.TimeUnit.MILLISECONDS;
        r6 = r8.convert(r6, r0);
        r4 = r2 + r6;
    L_0x0096:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.time.DateUtils.getFragment(java.util.Calendar, int, java.util.concurrent.TimeUnit):long");
    }

    public static boolean truncatedEquals(Calendar calendar, Calendar calendar2, int i) {
        return truncatedCompareTo(calendar, calendar2, i) == null ? true : null;
    }

    public static boolean truncatedEquals(Date date, Date date2, int i) {
        return truncatedCompareTo(date, date2, i) == null ? true : null;
    }

    public static int truncatedCompareTo(Calendar calendar, Calendar calendar2, int i) {
        return truncate(calendar, i).compareTo(truncate(calendar2, i));
    }

    public static int truncatedCompareTo(Date date, Date date2, int i) {
        return truncate(date, i).compareTo(truncate(date2, i));
    }
}
