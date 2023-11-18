package org.apache.commons.lang3.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class ExceptionUtils {
    private static final String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
    static final String WRAPPED_MARKER = " [wrapped] ";

    @Deprecated
    public static String[] getDefaultCauseMethodNames() {
        return (String[]) ArrayUtils.clone(CAUSE_METHOD_NAMES);
    }

    @Deprecated
    public static Throwable getCause(Throwable th) {
        return getCause(th, null);
    }

    @Deprecated
    public static Throwable getCause(Throwable th, String[] strArr) {
        if (th == null) {
            return null;
        }
        if (strArr == null) {
            strArr = th.getCause();
            if (strArr != null) {
                return strArr;
            }
            strArr = CAUSE_METHOD_NAMES;
        }
        for (String str : r5) {
            if (str != null) {
                Throwable causeUsingMethodName = getCauseUsingMethodName(th, str);
                if (causeUsingMethodName != null) {
                    return causeUsingMethodName;
                }
            }
        }
        return null;
    }

    public static Throwable getRootCause(Throwable th) {
        th = getThrowableList(th);
        return th.size() < 2 ? null : (Throwable) th.get(th.size() - 1);
    }

    private static java.lang.Throwable getCauseUsingMethodName(java.lang.Throwable r4, java.lang.String r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = 0;
        r2 = r4.getClass();	 Catch:{ NoSuchMethodException -> 0x000d, NoSuchMethodException -> 0x000d }
        r3 = new java.lang.Class[r0];	 Catch:{ NoSuchMethodException -> 0x000d, NoSuchMethodException -> 0x000d }
        r5 = r2.getMethod(r5, r3);	 Catch:{ NoSuchMethodException -> 0x000d, NoSuchMethodException -> 0x000d }
        goto L_0x000e;
    L_0x000d:
        r5 = r1;
    L_0x000e:
        if (r5 == 0) goto L_0x0025;
    L_0x0010:
        r2 = java.lang.Throwable.class;
        r3 = r5.getReturnType();
        r2 = r2.isAssignableFrom(r3);
        if (r2 == 0) goto L_0x0025;
    L_0x001c:
        r0 = new java.lang.Object[r0];	 Catch:{ IllegalAccessException -> 0x0025, IllegalAccessException -> 0x0025, IllegalAccessException -> 0x0025 }
        r4 = r5.invoke(r4, r0);	 Catch:{ IllegalAccessException -> 0x0025, IllegalAccessException -> 0x0025, IllegalAccessException -> 0x0025 }
        r4 = (java.lang.Throwable) r4;	 Catch:{ IllegalAccessException -> 0x0025, IllegalAccessException -> 0x0025, IllegalAccessException -> 0x0025 }
        return r4;
    L_0x0025:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.exception.ExceptionUtils.getCauseUsingMethodName(java.lang.Throwable, java.lang.String):java.lang.Throwable");
    }

    public static int getThrowableCount(Throwable th) {
        return getThrowableList(th).size();
    }

    public static Throwable[] getThrowables(Throwable th) {
        th = getThrowableList(th);
        return (Throwable[]) th.toArray(new Throwable[th.size()]);
    }

    public static List<Throwable> getThrowableList(Throwable th) {
        List<Throwable> arrayList = new ArrayList();
        while (th != null && !arrayList.contains(th)) {
            arrayList.add(th);
            th = getCause(th);
        }
        return arrayList;
    }

    public static int indexOfThrowable(Throwable th, Class<?> cls) {
        return indexOf(th, cls, 0, false);
    }

    public static int indexOfThrowable(Throwable th, Class<?> cls, int i) {
        return indexOf(th, cls, i, false);
    }

    public static int indexOfType(Throwable th, Class<?> cls) {
        return indexOf(th, cls, 0, true);
    }

    public static int indexOfType(Throwable th, Class<?> cls, int i) {
        return indexOf(th, cls, i, true);
    }

    private static int indexOf(Throwable th, Class<?> cls, int i, boolean z) {
        if (th != null) {
            if (cls != null) {
                boolean z2;
                if (i < 0) {
                    z2 = false;
                }
                th = getThrowables(th);
                if (z2 >= th.length) {
                    return -1;
                }
                if (z) {
                    while (z2 < th.length) {
                        if (cls.isAssignableFrom(th[z2].getClass())) {
                            return z2;
                        }
                        z2++;
                    }
                } else {
                    while (z2 < th.length) {
                        if (cls.equals(th[z2].getClass())) {
                            return z2;
                        }
                        z2++;
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public static void printRootCauseStackTrace(Throwable th) {
        printRootCauseStackTrace(th, System.err);
    }

    public static void printRootCauseStackTrace(Throwable th, PrintStream printStream) {
        if (th != null) {
            if (printStream == null) {
                throw new IllegalArgumentException("The PrintStream must not be null");
            }
            for (String println : getRootCauseStackTrace(th)) {
                printStream.println(println);
            }
            printStream.flush();
        }
    }

    public static void printRootCauseStackTrace(Throwable th, PrintWriter printWriter) {
        if (th != null) {
            if (printWriter == null) {
                throw new IllegalArgumentException("The PrintWriter must not be null");
            }
            for (String println : getRootCauseStackTrace(th)) {
                printWriter.println(println);
            }
            printWriter.flush();
        }
    }

    public static String[] getRootCauseStackTrace(Throwable th) {
        if (th == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        th = getThrowables(th);
        int length = th.length;
        List arrayList = new ArrayList();
        int i = length - 1;
        List stackFrameList = getStackFrameList(th[i]);
        while (true) {
            length--;
            if (length < 0) {
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
            List stackFrameList2;
            if (length != 0) {
                stackFrameList2 = getStackFrameList(th[length - 1]);
                removeCommonFrames(stackFrameList, stackFrameList2);
            } else {
                stackFrameList2 = stackFrameList;
            }
            if (length == i) {
                arrayList.add(th[length].toString());
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(WRAPPED_MARKER);
                stringBuilder.append(th[length].toString());
                arrayList.add(stringBuilder.toString());
            }
            for (int i2 = 0; i2 < stackFrameList.size(); i2++) {
                arrayList.add(stackFrameList.get(i2));
            }
            stackFrameList = stackFrameList2;
        }
    }

    public static void removeCommonFrames(List<String> list, List<String> list2) {
        if (list != null) {
            if (list2 != null) {
                int size = list.size() - 1;
                int size2 = list2.size() - 1;
                while (size >= 0 && size2 >= 0) {
                    if (((String) list.get(size)).equals((String) list2.get(size2))) {
                        list.remove(size);
                    }
                    size--;
                    size2--;
                }
                return;
            }
        }
        throw new IllegalArgumentException("The List must not be null");
    }

    public static String getStackTrace(Throwable th) {
        Writer stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.getBuffer().toString();
    }

    public static String[] getStackFrames(Throwable th) {
        if (th == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return getStackFrames(getStackTrace(th));
    }

    static String[] getStackFrames(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, SystemUtils.LINE_SEPARATOR);
        str = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            str.add(stringTokenizer.nextToken());
        }
        return (String[]) str.toArray(new String[str.size()]);
    }

    static List<String> getStackFrameList(Throwable th) {
        StringTokenizer stringTokenizer = new StringTokenizer(getStackTrace(th), SystemUtils.LINE_SEPARATOR);
        th = new ArrayList();
        Object obj = null;
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            int indexOf = nextToken.indexOf("at");
            if (indexOf != -1 && nextToken.substring(0, indexOf).trim().isEmpty()) {
                obj = 1;
                th.add(nextToken);
            } else if (obj != null) {
                break;
            }
        }
        return th;
    }

    public static String getMessage(Throwable th) {
        if (th == null) {
            return "";
        }
        String shortClassName = ClassUtils.getShortClassName(th, null);
        th = th.getMessage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(shortClassName);
        stringBuilder.append(": ");
        stringBuilder.append(StringUtils.defaultString(th));
        return stringBuilder.toString();
    }

    public static String getRootCauseMessage(Throwable th) {
        Throwable rootCause = getRootCause(th);
        if (rootCause != null) {
            th = rootCause;
        }
        return getMessage(th);
    }

    public static <R> R rethrow(Throwable th) {
        return typeErasure(th);
    }

    private static <R, T extends Throwable> R typeErasure(Throwable th) throws Throwable {
        throw th;
    }

    public static <R> R wrapAndThrow(Throwable th) {
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        } else if (th instanceof Error) {
            throw ((Error) th);
        } else {
            throw new UndeclaredThrowableException(th);
        }
    }

    public static boolean hasCause(Throwable th, Class<? extends Throwable> cls) {
        if (th instanceof UndeclaredThrowableException) {
            th = th.getCause();
        }
        return cls.isInstance(th);
    }
}
