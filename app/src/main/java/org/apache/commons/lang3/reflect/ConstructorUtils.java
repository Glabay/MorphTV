package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class ConstructorUtils {
    public static <T> T invokeConstructor(Class<T> cls, Object... objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        return invokeConstructor(cls, objArr, ClassUtils.toClass(objArr));
    }

    public static <T> T invokeConstructor(Class<T> cls, Object[] objArr, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        clsArr = getMatchingAccessibleConstructor(cls, ArrayUtils.nullToEmpty((Class[]) clsArr));
        if (clsArr == null) {
            clsArr = new StringBuilder();
            clsArr.append("No such accessible constructor on object: ");
            clsArr.append(cls.getName());
            throw new NoSuchMethodException(clsArr.toString());
        }
        if (clsArr.isVarArgs() != null) {
            objArr = MethodUtils.getVarArgs(objArr, clsArr.getParameterTypes());
        }
        return clsArr.newInstance(objArr);
    }

    public static <T> T invokeExactConstructor(Class<T> cls, Object... objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        return invokeExactConstructor(cls, objArr, ClassUtils.toClass(objArr));
    }

    public static <T> T invokeExactConstructor(Class<T> cls, Object[] objArr, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        clsArr = getAccessibleConstructor(cls, ArrayUtils.nullToEmpty((Class[]) clsArr));
        if (clsArr != null) {
            return clsArr.newInstance(objArr);
        }
        clsArr = new StringBuilder();
        clsArr.append("No such accessible constructor on object: ");
        clsArr.append(cls.getName());
        throw new NoSuchMethodException(clsArr.toString());
    }

    public static <T> java.lang.reflect.Constructor<T> getAccessibleConstructor(java.lang.Class<T> r2, java.lang.Class<?>... r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = "class cannot be null";
        r1 = 0;
        r1 = new java.lang.Object[r1];
        org.apache.commons.lang3.Validate.notNull(r2, r0, r1);
        r2 = r2.getConstructor(r3);	 Catch:{ NoSuchMethodException -> 0x0011 }
        r2 = getAccessibleConstructor(r2);	 Catch:{ NoSuchMethodException -> 0x0011 }
        return r2;
    L_0x0011:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.ConstructorUtils.getAccessibleConstructor(java.lang.Class, java.lang.Class[]):java.lang.reflect.Constructor<T>");
    }

    public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> constructor) {
        Validate.notNull(constructor, "constructor cannot be null", new Object[0]);
        return (MemberUtils.isAccessible(constructor) && isAccessible(constructor.getDeclaringClass())) ? constructor : null;
    }

    public static <T> java.lang.reflect.Constructor<T> getMatchingAccessibleConstructor(java.lang.Class<T> r5, java.lang.Class<?>... r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = "class cannot be null";
        r1 = 0;
        r2 = new java.lang.Object[r1];
        org.apache.commons.lang3.Validate.notNull(r5, r0, r2);
        r0 = r5.getConstructor(r6);	 Catch:{ NoSuchMethodException -> 0x0010 }
        org.apache.commons.lang3.reflect.MemberUtils.setAccessibleWorkaround(r0);	 Catch:{ NoSuchMethodException -> 0x0010 }
        return r0;
    L_0x0010:
        r0 = 0;
        r5 = r5.getConstructors();
        r2 = r5.length;
    L_0x0016:
        if (r1 >= r2) goto L_0x0035;
    L_0x0018:
        r3 = r5[r1];
        r4 = org.apache.commons.lang3.reflect.MemberUtils.isMatchingConstructor(r3, r6);
        if (r4 == 0) goto L_0x0032;
    L_0x0020:
        r3 = getAccessibleConstructor(r3);
        if (r3 == 0) goto L_0x0032;
    L_0x0026:
        org.apache.commons.lang3.reflect.MemberUtils.setAccessibleWorkaround(r3);
        if (r0 == 0) goto L_0x0031;
    L_0x002b:
        r4 = org.apache.commons.lang3.reflect.MemberUtils.compareConstructorFit(r3, r0, r6);
        if (r4 >= 0) goto L_0x0032;
    L_0x0031:
        r0 = r3;
    L_0x0032:
        r1 = r1 + 1;
        goto L_0x0016;
    L_0x0035:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.ConstructorUtils.getMatchingAccessibleConstructor(java.lang.Class, java.lang.Class[]):java.lang.reflect.Constructor<T>");
    }

    private static boolean isAccessible(Class<?> cls) {
        while (cls != null) {
            if (!Modifier.isPublic(cls.getModifiers())) {
                return null;
            }
            cls = cls.getEnclosingClass();
        }
        return true;
    }
}
