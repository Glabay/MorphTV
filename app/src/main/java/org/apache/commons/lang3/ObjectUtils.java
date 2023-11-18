package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;
import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.text.StrBuilder;

public class ObjectUtils {
    public static final Null NULL = new Null();

    public static class Null implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        Null() {
        }

        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }

    public static byte CONST(byte b) {
        return b;
    }

    public static char CONST(char c) {
        return c;
    }

    public static double CONST(double d) {
        return d;
    }

    public static float CONST(float f) {
        return f;
    }

    public static int CONST(int i) {
        return i;
    }

    public static long CONST(long j) {
        return j;
    }

    public static <T> T CONST(T t) {
        return t;
    }

    public static short CONST(short s) {
        return s;
    }

    public static boolean CONST(boolean z) {
        return z;
    }

    public static <T> T defaultIfNull(T t, T t2) {
        return t != null ? t : t2;
    }

    public static <T> T firstNonNull(T... tArr) {
        if (tArr != null) {
            for (T t : tArr) {
                if (t != null) {
                    return t;
                }
            }
        }
        return null;
    }

    public static boolean anyNotNull(Object... objArr) {
        return firstNonNull(objArr) != null ? 1 : null;
    }

    public static boolean allNotNull(Object... objArr) {
        if (objArr == null) {
            return false;
        }
        for (Object obj : objArr) {
            if (obj == null) {
                return false;
            }
        }
        return 1;
    }

    @Deprecated
    public static boolean equals(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj != null) {
            if (obj2 != null) {
                return obj.equals(obj2);
            }
        }
        return null;
    }

    public static boolean notEqual(Object obj, Object obj2) {
        return equals(obj, obj2) ^ 1;
    }

    @Deprecated
    public static int hashCode(Object obj) {
        return obj == null ? null : obj.hashCode();
    }

    @Deprecated
    public static int hashCodeMulti(Object... objArr) {
        int i = 1;
        if (objArr != null) {
            for (Object hashCode : objArr) {
                i = (i * 31) + hashCode(hashCode);
            }
        }
        return i;
    }

    public static String identityToString(Object obj) {
        if (obj == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        identityToString(stringBuilder, obj);
        return stringBuilder.toString();
    }

    public static void identityToString(Appendable appendable, Object obj) throws IOException {
        if (obj == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        appendable.append(obj.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(obj)));
    }

    public static void identityToString(StrBuilder strBuilder, Object obj) {
        if (obj == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        strBuilder.append(obj.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(obj)));
    }

    public static void identityToString(StringBuffer stringBuffer, Object obj) {
        if (obj == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        stringBuffer.append(obj.getClass().getName());
        stringBuffer.append('@');
        stringBuffer.append(Integer.toHexString(System.identityHashCode(obj)));
    }

    public static void identityToString(StringBuilder stringBuilder, Object obj) {
        if (obj == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        stringBuilder.append(obj.getClass().getName());
        stringBuilder.append('@');
        stringBuilder.append(Integer.toHexString(System.identityHashCode(obj)));
    }

    @Deprecated
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    @Deprecated
    public static String toString(Object obj, String str) {
        return obj == null ? str : obj.toString();
    }

    public static <T extends Comparable<? super T>> T min(T... tArr) {
        T t = null;
        if (tArr != null) {
            for (T t2 : tArr) {
                if (compare(t2, t, true) < 0) {
                    t = t2;
                }
            }
        }
        return t;
    }

    public static <T extends Comparable<? super T>> T max(T... tArr) {
        if (tArr == null) {
            return null;
        }
        T t = null;
        for (T t2 : tArr) {
            if (compare(t2, t, false) > 0) {
                t = t2;
            }
        }
        return t;
    }

    public static <T extends Comparable<? super T>> int compare(T t, T t2) {
        return compare(t, t2, false);
    }

    public static <T extends Comparable<? super T>> int compare(T t, T t2, boolean z) {
        if (t == t2) {
            return null;
        }
        int i = -1;
        if (t == null) {
            if (z) {
                i = 1;
            }
            return i;
        } else if (t2 != null) {
            return t.compareTo(t2);
        } else {
            if (!z) {
                i = 1;
            }
            return i;
        }
    }

    public static <T extends Comparable<? super T>> T median(T... tArr) {
        Validate.notEmpty((Object[]) tArr);
        Validate.noNullElements((Object[]) tArr);
        TreeSet treeSet = new TreeSet();
        Collections.addAll(treeSet, tArr);
        return (Comparable) treeSet.toArray()[(treeSet.size() - 1) / 2];
    }

    public static <T> T median(Comparator<T> comparator, T... tArr) {
        Validate.notEmpty((Object[]) tArr, "null/empty items", new Object[0]);
        Validate.noNullElements((Object[]) tArr);
        Validate.notNull(comparator, "null comparator", new Object[0]);
        TreeSet treeSet = new TreeSet(comparator);
        Collections.addAll(treeSet, tArr);
        return treeSet.toArray()[(treeSet.size() - 1) / 2];
    }

    public static <T> T mode(T... tArr) {
        if (!ArrayUtils.isNotEmpty((Object[]) tArr)) {
            return null;
        }
        int intValue;
        HashMap hashMap = new HashMap(tArr.length);
        int i = 0;
        for (Object obj : tArr) {
            MutableInt mutableInt = (MutableInt) hashMap.get(obj);
            if (mutableInt == null) {
                hashMap.put(obj, new MutableInt(1));
            } else {
                mutableInt.increment();
            }
        }
        while (true) {
            T t = null;
            for (Entry entry : hashMap.entrySet()) {
                intValue = ((MutableInt) entry.getValue()).intValue();
                if (intValue != i) {
                    if (intValue > i) {
                        t = entry.getKey();
                        i = intValue;
                    }
                }
            }
            return t;
        }
    }

    public static <T> T clone(T t) {
        StringBuilder stringBuilder;
        if (!(t instanceof Cloneable)) {
            return null;
        }
        T newInstance;
        if (t.getClass().isArray()) {
            Class componentType = t.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                int length = Array.getLength(t);
                newInstance = Array.newInstance(componentType, length);
                while (true) {
                    int i = length - 1;
                    if (length <= 0) {
                        break;
                    }
                    Array.set(newInstance, i, Array.get(t, i));
                    length = i;
                }
            } else {
                t = ((Object[]) t).clone();
                return t;
            }
        }
        try {
            newInstance = t.getClass().getMethod("clone", new Class[0]).invoke(t, new Object[0]);
        } catch (Throwable e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Cloneable type ");
            stringBuilder.append(t.getClass().getName());
            stringBuilder.append(" has no clone method");
            throw new CloneFailedException(stringBuilder.toString(), e);
        } catch (Throwable e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot clone Cloneable type ");
            stringBuilder.append(t.getClass().getName());
            throw new CloneFailedException(stringBuilder.toString(), e2);
        } catch (InvocationTargetException e3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Exception cloning Cloneable type ");
            stringBuilder.append(t.getClass().getName());
            throw new CloneFailedException(stringBuilder.toString(), e3.getCause());
        }
        t = newInstance;
        return t;
    }

    public static <T> T cloneIfPossible(T t) {
        T clone = clone(t);
        return clone == null ? t : clone;
    }

    public static byte CONST_BYTE(int i) throws IllegalArgumentException {
        if (i >= -128) {
            if (i <= 127) {
                return (byte) i;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Supplied value must be a valid byte literal between -128 and 127: [");
        stringBuilder.append(i);
        stringBuilder.append("]");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static short CONST_SHORT(int i) throws IllegalArgumentException {
        if (i >= -32768) {
            if (i <= 32767) {
                return (short) i;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Supplied value must be a valid byte literal between -32768 and 32767: [");
        stringBuilder.append(i);
        stringBuilder.append("]");
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
