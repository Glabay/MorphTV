package org.apache.commons.lang3.time;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.exception.ExceptionUtils;

class CalendarReflection {
    private static final Method GET_WEEK_YEAR = getCalendarMethod("getWeekYear", new Class[0]);
    private static final Method IS_WEEK_DATE_SUPPORTED = getCalendarMethod("isWeekDateSupported", new Class[0]);

    CalendarReflection() {
    }

    private static java.lang.reflect.Method getCalendarMethod(java.lang.String r1, java.lang.Class<?>... r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = java.util.Calendar.class;	 Catch:{ Exception -> 0x0007 }
        r1 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x0007 }
        return r1;
    L_0x0007:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.time.CalendarReflection.getCalendarMethod(java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

    static boolean isWeekDateSupported(Calendar calendar) {
        try {
            boolean z = false;
            if (!(IS_WEEK_DATE_SUPPORTED == null || ((Boolean) IS_WEEK_DATE_SUPPORTED.invoke(calendar, new Object[0])).booleanValue() == null)) {
                z = true;
            }
            return z;
        } catch (Calendar calendar2) {
            return ((Boolean) ExceptionUtils.rethrow(calendar2)).booleanValue();
        }
    }

    public static int getWeekYear(Calendar calendar) {
        try {
            if (isWeekDateSupported(calendar)) {
                return ((Integer) GET_WEEK_YEAR.invoke(calendar, new Object[0])).intValue();
            }
            int i = calendar.get(1);
            if (IS_WEEK_DATE_SUPPORTED == null && (calendar instanceof GregorianCalendar)) {
                int i2 = calendar.get(2);
                if (i2 != 0) {
                    if (i2 == 11) {
                        if (calendar.get(3) == 1) {
                            i++;
                        }
                    }
                } else if (calendar.get(3) >= 52) {
                    i--;
                }
            }
            return i;
        } catch (Calendar calendar2) {
            return ((Integer) ExceptionUtils.rethrow(calendar2)).intValue();
        }
    }
}
