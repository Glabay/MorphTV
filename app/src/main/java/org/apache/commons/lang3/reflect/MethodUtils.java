package org.apache.commons.lang3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ClassUtils.Interfaces;
import org.apache.commons.lang3.Validate;

public class MethodUtils {
    public static Object invokeMethod(Object obj, String str) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(obj, str, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeMethod(Object obj, boolean z, String str) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(obj, z, str, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeMethod(Object obj, String str, Object... objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        return invokeMethod(obj, str, objArr, ClassUtils.toClass(objArr));
    }

    public static Object invokeMethod(Object obj, boolean z, String str, Object... objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        return invokeMethod(obj, z, str, objArr, ClassUtils.toClass(objArr));
    }

    public static Object invokeMethod(Object obj, boolean z, String str, Object[] objArr, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        clsArr = ArrayUtils.nullToEmpty((Class[]) clsArr);
        objArr = ArrayUtils.nullToEmpty(objArr);
        Method method = null;
        boolean z2 = false;
        if (z) {
            try {
                String str2 = "No such method: ";
                clsArr = getMatchingMethod(obj.getClass(), str, clsArr);
                if (clsArr != null) {
                    try {
                        boolean isAccessible = clsArr.isAccessible();
                        if (!isAccessible) {
                            try {
                                clsArr.setAccessible(true);
                            } catch (Throwable th) {
                                obj = th;
                                z2 = isAccessible;
                                method = clsArr;
                                if (!(method == null || !z || method.isAccessible() == z2)) {
                                    method.setAccessible(z2);
                                }
                                throw obj;
                            }
                        }
                        z2 = isAccessible;
                    } catch (Throwable th2) {
                        obj = th2;
                        method = clsArr;
                        method.setAccessible(z2);
                        throw obj;
                    }
                }
            } catch (Throwable th3) {
                obj = th3;
                method.setAccessible(z2);
                throw obj;
            }
        }
        str2 = "No such accessible method: ";
        clsArr = getMatchingAccessibleMethod(obj.getClass(), str, clsArr);
        method = clsArr;
        if (method == null) {
            clsArr = new StringBuilder();
            clsArr.append(str2);
            clsArr.append(str);
            clsArr.append("() on object: ");
            clsArr.append(obj.getClass().getName());
            throw new NoSuchMethodException(clsArr.toString());
        }
        obj = method.invoke(obj, toVarArgs(method, objArr));
        if (!(method == null || !z || method.isAccessible() == z2)) {
            method.setAccessible(z2);
        }
        return obj;
    }

    public static Object invokeMethod(Object obj, String str, Object[] objArr, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(obj, false, str, objArr, clsArr);
    }

    public static Object invokeExactMethod(Object obj, String str) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeExactMethod(obj, str, ArrayUtils.EMPTY_OBJECT_ARRAY, null);
    }

    public static Object invokeExactMethod(Object obj, String str, Object... objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        return invokeExactMethod(obj, str, objArr, ClassUtils.toClass(objArr));
    }

    public static Object invokeExactMethod(Object obj, String str, Object[] objArr, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        clsArr = getAccessibleMethod(obj.getClass(), str, ArrayUtils.nullToEmpty((Class[]) clsArr));
        if (clsArr != null) {
            return clsArr.invoke(obj, objArr);
        }
        clsArr = new StringBuilder();
        clsArr.append("No such accessible method: ");
        clsArr.append(str);
        clsArr.append("() on object: ");
        clsArr.append(obj.getClass().getName());
        throw new NoSuchMethodException(clsArr.toString());
    }

    public static Object invokeExactStaticMethod(Class<?> cls, String str, Object[] objArr, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        clsArr = getAccessibleMethod(cls, str, ArrayUtils.nullToEmpty((Class[]) clsArr));
        if (clsArr != null) {
            return clsArr.invoke(null, objArr);
        }
        clsArr = new StringBuilder();
        clsArr.append("No such accessible method: ");
        clsArr.append(str);
        clsArr.append("() on class: ");
        clsArr.append(cls.getName());
        throw new NoSuchMethodException(clsArr.toString());
    }

    public static Object invokeStaticMethod(Class<?> cls, String str, Object... objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        return invokeStaticMethod(cls, str, objArr, ClassUtils.toClass(objArr));
    }

    public static Object invokeStaticMethod(Class<?> cls, String str, Object[] objArr, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        clsArr = getMatchingAccessibleMethod(cls, str, ArrayUtils.nullToEmpty((Class[]) clsArr));
        if (clsArr != null) {
            return clsArr.invoke(null, toVarArgs(clsArr, objArr));
        }
        clsArr = new StringBuilder();
        clsArr.append("No such accessible method: ");
        clsArr.append(str);
        clsArr.append("() on class: ");
        clsArr.append(cls.getName());
        throw new NoSuchMethodException(clsArr.toString());
    }

    private static Object[] toVarArgs(Method method, Object[] objArr) {
        return method.isVarArgs() ? getVarArgs(objArr, method.getParameterTypes()) : objArr;
    }

    static Object[] getVarArgs(Object[] objArr, Class<?>[] clsArr) {
        if (objArr.length == clsArr.length && objArr[objArr.length - 1].getClass().equals(clsArr[clsArr.length - 1])) {
            return objArr;
        }
        Object obj = new Object[clsArr.length];
        System.arraycopy(objArr, 0, obj, 0, clsArr.length - 1);
        Class componentType = clsArr[clsArr.length - 1].getComponentType();
        int length = (objArr.length - clsArr.length) + 1;
        Object newInstance = Array.newInstance(ClassUtils.primitiveToWrapper(componentType), length);
        System.arraycopy(objArr, clsArr.length - 1, newInstance, 0, length);
        if (componentType.isPrimitive() != null) {
            newInstance = ArrayUtils.toPrimitive(newInstance);
        }
        obj[clsArr.length - 1] = newInstance;
        return obj;
    }

    public static Object invokeExactStaticMethod(Class<?> cls, String str, Object... objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        objArr = ArrayUtils.nullToEmpty(objArr);
        return invokeExactStaticMethod(cls, str, objArr, ClassUtils.toClass(objArr));
    }

    public static java.lang.reflect.Method getAccessibleMethod(java.lang.Class<?> r0, java.lang.String r1, java.lang.Class<?>... r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r0.getMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x0009 }
        r0 = getAccessibleMethod(r0);	 Catch:{ NoSuchMethodException -> 0x0009 }
        return r0;
    L_0x0009:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.MethodUtils.getAccessibleMethod(java.lang.Class, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

    public static Method getAccessibleMethod(Method method) {
        if (!MemberUtils.isAccessible(method)) {
            return null;
        }
        Class declaringClass = method.getDeclaringClass();
        if (Modifier.isPublic(declaringClass.getModifiers())) {
            return method;
        }
        String name = method.getName();
        method = method.getParameterTypes();
        Method accessibleMethodFromInterfaceNest = getAccessibleMethodFromInterfaceNest(declaringClass, name, method);
        if (accessibleMethodFromInterfaceNest == null) {
            accessibleMethodFromInterfaceNest = getAccessibleMethodFromSuperclass(declaringClass, name, method);
        }
        return accessibleMethodFromInterfaceNest;
    }

    private static java.lang.reflect.Method getAccessibleMethodFromSuperclass(java.lang.Class<?> r2, java.lang.String r3, java.lang.Class<?>... r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = r2.getSuperclass();
    L_0x0004:
        r0 = 0;
        if (r2 == 0) goto L_0x001c;
    L_0x0007:
        r1 = r2.getModifiers();
        r1 = java.lang.reflect.Modifier.isPublic(r1);
        if (r1 == 0) goto L_0x0017;
    L_0x0011:
        r2 = r2.getMethod(r3, r4);	 Catch:{ NoSuchMethodException -> 0x0016 }
        return r2;
    L_0x0016:
        return r0;
    L_0x0017:
        r2 = r2.getSuperclass();
        goto L_0x0004;
    L_0x001c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.MethodUtils.getAccessibleMethodFromSuperclass(java.lang.Class, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

    private static java.lang.reflect.Method getAccessibleMethodFromInterfaceNest(java.lang.Class<?> r3, java.lang.String r4, java.lang.Class<?>... r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
    L_0x0000:
        if (r3 == 0) goto L_0x002f;
    L_0x0002:
        r0 = r3.getInterfaces();
        r1 = 0;
    L_0x0007:
        r2 = r0.length;
        if (r1 >= r2) goto L_0x002a;
    L_0x000a:
        r2 = r0[r1];
        r2 = r2.getModifiers();
        r2 = java.lang.reflect.Modifier.isPublic(r2);
        if (r2 != 0) goto L_0x0017;
    L_0x0016:
        goto L_0x0027;
    L_0x0017:
        r2 = r0[r1];	 Catch:{ NoSuchMethodException -> 0x001e }
        r2 = r2.getDeclaredMethod(r4, r5);	 Catch:{ NoSuchMethodException -> 0x001e }
        return r2;
    L_0x001e:
        r2 = r0[r1];
        r2 = getAccessibleMethodFromInterfaceNest(r2, r4, r5);
        if (r2 == 0) goto L_0x0027;
    L_0x0026:
        return r2;
    L_0x0027:
        r1 = r1 + 1;
        goto L_0x0007;
    L_0x002a:
        r3 = r3.getSuperclass();
        goto L_0x0000;
    L_0x002f:
        r3 = 0;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.MethodUtils.getAccessibleMethodFromInterfaceNest(java.lang.Class, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

    public static java.lang.reflect.Method getMatchingAccessibleMethod(java.lang.Class<?> r5, java.lang.String r6, java.lang.Class<?>... r7) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r5.getMethod(r6, r7);	 Catch:{ NoSuchMethodException -> 0x0008 }
        org.apache.commons.lang3.reflect.MemberUtils.setAccessibleWorkaround(r0);	 Catch:{ NoSuchMethodException -> 0x0008 }
        return r0;
    L_0x0008:
        r0 = 0;
        r5 = r5.getMethods();
        r1 = r5.length;
        r2 = 0;
    L_0x000f:
        if (r2 >= r1) goto L_0x0035;
    L_0x0011:
        r3 = r5[r2];
        r4 = r3.getName();
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0032;
    L_0x001d:
        r4 = org.apache.commons.lang3.reflect.MemberUtils.isMatchingMethod(r3, r7);
        if (r4 == 0) goto L_0x0032;
    L_0x0023:
        r3 = getAccessibleMethod(r3);
        if (r3 == 0) goto L_0x0032;
    L_0x0029:
        if (r0 == 0) goto L_0x0031;
    L_0x002b:
        r4 = org.apache.commons.lang3.reflect.MemberUtils.compareMethodFit(r3, r0, r7);
        if (r4 >= 0) goto L_0x0032;
    L_0x0031:
        r0 = r3;
    L_0x0032:
        r2 = r2 + 1;
        goto L_0x000f;
    L_0x0035:
        if (r0 == 0) goto L_0x003a;
    L_0x0037:
        org.apache.commons.lang3.reflect.MemberUtils.setAccessibleWorkaround(r0);
    L_0x003a:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.MethodUtils.getMatchingAccessibleMethod(java.lang.Class, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

    public static Method getMatchingMethod(Class<?> cls, String str, Class<?>... clsArr) {
        int i = 0;
        Validate.notNull(cls, "Null class not allowed.", new Object[0]);
        Validate.notEmpty((CharSequence) str, "Null or blank methodName not allowed.", new Object[0]);
        Object[] declaredMethods = cls.getDeclaredMethods();
        cls = ClassUtils.getAllSuperclasses(cls).iterator();
        while (cls.hasNext()) {
            Method[] methodArr = (Method[]) ArrayUtils.addAll(declaredMethods, ((Class) cls.next()).getDeclaredMethods());
        }
        cls = null;
        int length = declaredMethods.length;
        while (i < length) {
            Method method = declaredMethods[i];
            if (str.equals(method.getName()) && ArrayUtils.isEquals(clsArr, method.getParameterTypes())) {
                return method;
            }
            if (str.equals(method.getName()) && ClassUtils.isAssignable((Class[]) clsArr, method.getParameterTypes(), true)) {
                if (cls != null) {
                    if (distance(clsArr, method.getParameterTypes()) >= distance(clsArr, cls.getParameterTypes())) {
                    }
                }
                cls = method;
            }
            i++;
        }
        return cls;
    }

    private static int distance(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (!ClassUtils.isAssignable((Class[]) clsArr, (Class[]) clsArr2, true)) {
            return -1;
        }
        int i = 0;
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            if (!clsArr[i2].equals(clsArr2[i2])) {
                i = (!ClassUtils.isAssignable(clsArr[i2], clsArr2[i2], true) || ClassUtils.isAssignable(clsArr[i2], clsArr2[i2], false)) ? i + 2 : i + 1;
            }
        }
        return i;
    }

    public static Set<Method> getOverrideHierarchy(Method method, Interfaces interfaces) {
        Validate.notNull(method);
        Set<Method> linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(method);
        Class[] parameterTypes = method.getParameterTypes();
        Type declaringClass = method.getDeclaringClass();
        interfaces = ClassUtils.hierarchy(declaringClass, interfaces).iterator();
        interfaces.next();
        while (interfaces.hasNext()) {
            Method matchingAccessibleMethod = getMatchingAccessibleMethod((Class) interfaces.next(), method.getName(), parameterTypes);
            if (matchingAccessibleMethod != null) {
                if (Arrays.equals(matchingAccessibleMethod.getParameterTypes(), parameterTypes)) {
                    linkedHashSet.add(matchingAccessibleMethod);
                } else {
                    Map typeArguments = TypeUtils.getTypeArguments(declaringClass, matchingAccessibleMethod.getDeclaringClass());
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!TypeUtils.equals(TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]), TypeUtils.unrollVariables(typeArguments, matchingAccessibleMethod.getGenericParameterTypes()[i]))) {
                            break;
                        }
                    }
                    linkedHashSet.add(matchingAccessibleMethod);
                }
            }
        }
        return linkedHashSet;
    }

    public static Method[] getMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> cls2) {
        cls = getMethodsListWithAnnotation(cls, cls2);
        return (Method[]) cls.toArray(new Method[cls.size()]);
    }

    public static List<Method> getMethodsListWithAnnotation(Class<?> cls, Class<? extends Annotation> cls2) {
        boolean z = true;
        int i = 0;
        Validate.isTrue(cls != null, "The class must not be null", new Object[0]);
        if (cls2 == null) {
            z = false;
        }
        Validate.isTrue(z, "The annotation class must not be null", new Object[0]);
        cls = cls.getMethods();
        List<Method> arrayList = new ArrayList();
        int length = cls.length;
        while (i < length) {
            Method method = cls[i];
            if (method.getAnnotation(cls2) != null) {
                arrayList.add(method);
            }
            i++;
        }
        return arrayList;
    }
}
