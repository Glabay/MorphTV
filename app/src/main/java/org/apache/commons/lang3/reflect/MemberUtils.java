package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;

abstract class MemberUtils {
    private static final int ACCESS_TEST = 7;
    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    private static final class Executable {
        private final boolean isVarArgs;
        private final Class<?>[] parameterTypes;

        private static Executable of(Method method) {
            return new Executable(method);
        }

        private static Executable of(Constructor<?> constructor) {
            return new Executable((Constructor) constructor);
        }

        private Executable(Method method) {
            this.parameterTypes = method.getParameterTypes();
            this.isVarArgs = method.isVarArgs();
        }

        private Executable(Constructor<?> constructor) {
            this.parameterTypes = constructor.getParameterTypes();
            this.isVarArgs = constructor.isVarArgs();
        }

        public Class<?>[] getParameterTypes() {
            return this.parameterTypes;
        }

        public boolean isVarArgs() {
            return this.isVarArgs;
        }
    }

    static boolean isPackageAccess(int i) {
        return (i & 7) == 0;
    }

    MemberUtils() {
    }

    static boolean setAccessibleWorkaround(java.lang.reflect.AccessibleObject r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r3 == 0) goto L_0x0031;
    L_0x0003:
        r1 = r3.isAccessible();
        if (r1 == 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0031;
    L_0x000a:
        r1 = r3;
        r1 = (java.lang.reflect.Member) r1;
        r2 = r3.isAccessible();
        if (r2 != 0) goto L_0x0030;
    L_0x0013:
        r2 = r1.getModifiers();
        r2 = java.lang.reflect.Modifier.isPublic(r2);
        if (r2 == 0) goto L_0x0030;
    L_0x001d:
        r1 = r1.getDeclaringClass();
        r1 = r1.getModifiers();
        r1 = isPackageAccess(r1);
        if (r1 == 0) goto L_0x0030;
    L_0x002b:
        r1 = 1;
        r3.setAccessible(r1);	 Catch:{ SecurityException -> 0x0030 }
        return r1;
    L_0x0030:
        return r0;
    L_0x0031:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.MemberUtils.setAccessibleWorkaround(java.lang.reflect.AccessibleObject):boolean");
    }

    static boolean isAccessible(Member member) {
        return (member != null && Modifier.isPublic(member.getModifiers()) && member.isSynthetic() == null) ? true : null;
    }

    static int compareConstructorFit(Constructor<?> constructor, Constructor<?> constructor2, Class<?>[] clsArr) {
        return compareParameterTypes(Executable.of((Constructor) constructor), Executable.of((Constructor) constructor2), clsArr);
    }

    static int compareMethodFit(Method method, Method method2, Class<?>[] clsArr) {
        return compareParameterTypes(Executable.of(method), Executable.of(method2), clsArr);
    }

    private static int compareParameterTypes(Executable executable, Executable executable2, Class<?>[] clsArr) {
        executable = getTotalTransformationCost(clsArr, executable);
        executable2 = getTotalTransformationCost(clsArr, executable2);
        if (executable < executable2) {
            return -1;
        }
        return executable2 < executable ? 1 : null;
    }

    private static float getTotalTransformationCost(Class<?>[] clsArr, Executable executable) {
        Class[] parameterTypes = executable.getParameterTypes();
        executable = executable.isVarArgs();
        long length = (long) (executable != null ? parameterTypes.length - 1 : parameterTypes.length);
        if (((long) clsArr.length) < length) {
            return 2139095039;
        }
        Object obj = null;
        float f = 0.0f;
        for (int i = 0; ((long) i) < length; i++) {
            f += getObjectTransformationCost(clsArr[i], parameterTypes[i]);
        }
        if (executable != null) {
            executable = clsArr.length < parameterTypes.length ? true : null;
            if (clsArr.length == parameterTypes.length && clsArr[clsArr.length - 1].isArray()) {
                obj = 1;
            }
            Class componentType = parameterTypes[parameterTypes.length - 1].getComponentType();
            if (executable != null) {
                f += getObjectTransformationCost(componentType, Object.class) + 981668463;
            } else if (obj != null) {
                f += getObjectTransformationCost(clsArr[clsArr.length - 1].getComponentType(), componentType) + 981668463;
            } else {
                for (executable = parameterTypes.length - 1; executable < clsArr.length; executable++) {
                    f += getObjectTransformationCost(clsArr[executable], componentType) + 0.001f;
                }
            }
        }
        return f;
    }

    private static float getObjectTransformationCost(Class<?> cls, Class<?> cls2) {
        if (cls2.isPrimitive()) {
            return getPrimitivePromotionCost(cls, cls2);
        }
        float f = 0.0f;
        Class superclass;
        while (superclass != null && !cls2.equals(superclass)) {
            if (cls2.isInterface() && ClassUtils.isAssignable(superclass, (Class) cls2)) {
                f += 0.25f;
                break;
            }
            f += 1.0f;
            superclass = superclass.getSuperclass();
        }
        if (superclass == null) {
            f += 1.5f;
        }
        return f;
    }

    private static float getPrimitivePromotionCost(Class<?> cls, Class<?> cls2) {
        float f;
        if (cls.isPrimitive()) {
            f = 0.0f;
        } else {
            cls = ClassUtils.wrapperToPrimitive(cls);
            f = 0.1f;
        }
        int i = 0;
        while (cls != cls2 && i < ORDERED_PRIMITIVE_TYPES.length) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                f += 0.1f;
                if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
            i++;
        }
        return f;
    }

    static boolean isMatchingMethod(Method method, Class<?>[] clsArr) {
        return isMatchingExecutable(Executable.of(method), clsArr);
    }

    static boolean isMatchingConstructor(Constructor<?> constructor, Class<?>[] clsArr) {
        return isMatchingExecutable(Executable.of((Constructor) constructor), clsArr);
    }

    private static boolean isMatchingExecutable(Executable executable, Class<?>[] clsArr) {
        Class[] parameterTypes = executable.getParameterTypes();
        if (executable.isVarArgs() == null) {
            return ClassUtils.isAssignable((Class[]) clsArr, parameterTypes, true);
        }
        int i = 0;
        while (i < parameterTypes.length - 1 && i < clsArr.length) {
            if (!ClassUtils.isAssignable(clsArr[i], parameterTypes[i], true)) {
                return false;
            }
            i++;
        }
        Class componentType = parameterTypes[parameterTypes.length - 1].getComponentType();
        while (i < clsArr.length) {
            if (!ClassUtils.isAssignable(clsArr[i], componentType, true)) {
                return false;
            }
            i++;
        }
        return true;
    }
}
