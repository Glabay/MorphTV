package com.squareup.duktape;

import android.support.annotation.Keep;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Keep
public final class DuktapeException extends RuntimeException {
    private static final String STACK_TRACE_CLASS_NAME = "JavaScript";
    private static final Pattern STACK_TRACE_PATTERN = Pattern.compile("\\s*at ([^\\s^\\[]+) \\(([^\\s]+):(\\d+)\\).*$");

    public DuktapeException(String str) {
        super(getErrorMessage(str));
        addDuktapeStack(this, str);
    }

    static void addDuktapeStack(Throwable th, String str) {
        str = str.split("\n", -1);
        if (str.length > 1) {
            List arrayList = new ArrayList();
            Object obj = null;
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                if (obj == null && stackTraceElement.isNativeMethod() && stackTraceElement.getClassName().equals(Duktape.class.getName()) && stackTraceElement.getMethodName().equals("evaluate")) {
                    for (int i = 1; i < str.length; i++) {
                        StackTraceElement toStackTraceElement = toStackTraceElement(str[i]);
                        if (toStackTraceElement != null) {
                            arrayList.add(toStackTraceElement);
                        }
                    }
                    obj = 1;
                }
                arrayList.add(stackTraceElement);
            }
            th.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[arrayList.size()]));
        }
    }

    private static String getErrorMessage(String str) {
        int indexOf = str.indexOf(10);
        return indexOf > 0 ? str.substring(0, indexOf) : str;
    }

    private static StackTraceElement toStackTraceElement(String str) {
        str = STACK_TRACE_PATTERN.matcher(str);
        if (str.matches()) {
            return new StackTraceElement(STACK_TRACE_CLASS_NAME, str.group(1), str.group(2), Integer.parseInt(str.group(3)));
        }
        return null;
    }
}
