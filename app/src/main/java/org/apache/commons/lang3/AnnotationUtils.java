package org.apache.commons.lang3;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AnnotationUtils {
    private static final ToStringStyle TO_STRING_STYLE = new C14391();

    /* renamed from: org.apache.commons.lang3.AnnotationUtils$1 */
    static class C14391 extends ToStringStyle {
        private static final long serialVersionUID = 1;

        C14391() {
            setDefaultFullDetail(true);
            setArrayContentDetail(true);
            setUseClassName(true);
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
            setContentStart("(");
            setContentEnd(")");
            setFieldSeparator(", ");
            setArrayStart("[");
            setArrayEnd("]");
        }

        protected String getShortClassName(Class<?> cls) {
            Class cls2;
            cls = ClassUtils.getAllInterfaces(cls).iterator();
            while (cls.hasNext()) {
                cls2 = (Class) cls.next();
                if (Annotation.class.isAssignableFrom(cls2)) {
                    break;
                }
            }
            cls2 = null;
            return new StringBuilder(cls2 == null ? "" : cls2.getName()).insert(0, '@').toString();
        }

        protected void appendDetail(StringBuffer stringBuffer, String str, Object obj) {
            if (obj instanceof Annotation) {
                obj = AnnotationUtils.toString((Annotation) obj);
            }
            super.appendDetail(stringBuffer, str, obj);
        }
    }

    public static boolean equals(java.lang.annotation.Annotation r8, java.lang.annotation.Annotation r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 1;
        if (r8 != r9) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r8 == 0) goto L_0x0063;
    L_0x0007:
        if (r9 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0063;
    L_0x000a:
        r2 = r8.annotationType();
        r3 = r9.annotationType();
        r4 = "Annotation %s with null annotationType()";
        r5 = new java.lang.Object[r0];
        r5[r1] = r8;
        org.apache.commons.lang3.Validate.notNull(r2, r4, r5);
        r4 = "Annotation %s with null annotationType()";
        r5 = new java.lang.Object[r0];
        r5[r1] = r9;
        org.apache.commons.lang3.Validate.notNull(r3, r4, r5);
        r3 = r2.equals(r3);
        if (r3 != 0) goto L_0x002b;
    L_0x002a:
        return r1;
    L_0x002b:
        r2 = r2.getDeclaredMethods();	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r3 = r2.length;	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r4 = 0;	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
    L_0x0031:
        if (r4 >= r3) goto L_0x0060;	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
    L_0x0033:
        r5 = r2[r4];	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r6 = r5.getParameterTypes();	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r6 = r6.length;	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        if (r6 != 0) goto L_0x005d;	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
    L_0x003c:
        r6 = r5.getReturnType();	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r6 = isValidAnnotationMemberType(r6);	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        if (r6 == 0) goto L_0x005d;	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
    L_0x0046:
        r6 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r6 = r5.invoke(r8, r6);	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r7 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r7 = r5.invoke(r9, r7);	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r5 = r5.getReturnType();	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        r5 = memberEquals(r5, r6, r7);	 Catch:{ IllegalAccessException -> 0x0062, InvocationTargetException -> 0x0061 }
        if (r5 != 0) goto L_0x005d;
    L_0x005c:
        return r1;
    L_0x005d:
        r4 = r4 + 1;
        goto L_0x0031;
    L_0x0060:
        return r0;
    L_0x0061:
        return r1;
    L_0x0062:
        return r1;
    L_0x0063:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.AnnotationUtils.equals(java.lang.annotation.Annotation, java.lang.annotation.Annotation):boolean");
    }

    public static int hashCode(Annotation annotation) {
        Method[] declaredMethods = annotation.annotationType().getDeclaredMethods();
        int length = declaredMethods.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            Method method = declaredMethods[i];
            try {
                Object invoke = method.invoke(annotation, new Object[0]);
                if (invoke == null) {
                    throw new IllegalStateException(String.format("Annotation method %s returned null", new Object[]{method}));
                }
                i2 += hashMember(method.getName(), invoke);
                i++;
            } catch (Annotation annotation2) {
                throw annotation2;
            } catch (Annotation annotation22) {
                throw new RuntimeException(annotation22);
            }
        }
        return i2;
    }

    public static String toString(Annotation annotation) {
        ToStringBuilder toStringBuilder = new ToStringBuilder(annotation, TO_STRING_STYLE);
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            if (method.getParameterTypes().length <= 0) {
                try {
                    toStringBuilder.append(method.getName(), method.invoke(annotation, new Object[0]));
                } catch (Annotation annotation2) {
                    throw annotation2;
                } catch (Annotation annotation22) {
                    throw new RuntimeException(annotation22);
                }
            }
        }
        return toStringBuilder.build();
    }

    public static boolean isValidAnnotationMemberType(Class<?> cls) {
        boolean z = false;
        if (cls == null) {
            return false;
        }
        if (cls.isArray()) {
            cls = cls.getComponentType();
        }
        if (cls.isPrimitive() || cls.isEnum() || cls.isAnnotation() || String.class.equals(cls) || Class.class.equals(cls) != null) {
            z = true;
        }
        return z;
    }

    private static int hashMember(String str, Object obj) {
        str = str.hashCode() * 127;
        if (obj.getClass().isArray()) {
            return str ^ arrayMemberHash(obj.getClass().getComponentType(), obj);
        }
        if (obj instanceof Annotation) {
            return str ^ hashCode((Annotation) obj);
        }
        return str ^ obj.hashCode();
    }

    private static boolean memberEquals(Class<?> cls, Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj != null) {
            if (obj2 != null) {
                if (cls.isArray()) {
                    return arrayMemberEquals(cls.getComponentType(), obj, obj2);
                }
                if (cls.isAnnotation() != null) {
                    return equals((Annotation) obj, (Annotation) obj2);
                }
                return obj.equals(obj2);
            }
        }
        return null;
    }

    private static boolean arrayMemberEquals(Class<?> cls, Object obj, Object obj2) {
        if (cls.isAnnotation()) {
            return annotationArrayMemberEquals((Annotation[]) obj, (Annotation[]) obj2);
        }
        if (cls.equals(Byte.TYPE)) {
            return Arrays.equals((byte[]) obj, (byte[]) obj2);
        }
        if (cls.equals(Short.TYPE)) {
            return Arrays.equals((short[]) obj, (short[]) obj2);
        }
        if (cls.equals(Integer.TYPE)) {
            return Arrays.equals((int[]) obj, (int[]) obj2);
        }
        if (cls.equals(Character.TYPE)) {
            return Arrays.equals((char[]) obj, (char[]) obj2);
        }
        if (cls.equals(Long.TYPE)) {
            return Arrays.equals((long[]) obj, (long[]) obj2);
        }
        if (cls.equals(Float.TYPE)) {
            return Arrays.equals((float[]) obj, (float[]) obj2);
        }
        if (cls.equals(Double.TYPE)) {
            return Arrays.equals((double[]) obj, (double[]) obj2);
        }
        if (cls.equals(Boolean.TYPE) != null) {
            return Arrays.equals((boolean[]) obj, (boolean[]) obj2);
        }
        return Arrays.equals((Object[]) obj, (Object[]) obj2);
    }

    private static boolean annotationArrayMemberEquals(Annotation[] annotationArr, Annotation[] annotationArr2) {
        if (annotationArr.length != annotationArr2.length) {
            return false;
        }
        for (int i = 0; i < annotationArr.length; i++) {
            if (!equals(annotationArr[i], annotationArr2[i])) {
                return false;
            }
        }
        return 1;
    }

    private static int arrayMemberHash(Class<?> cls, Object obj) {
        if (cls.equals(Byte.TYPE)) {
            return Arrays.hashCode((byte[]) obj);
        }
        if (cls.equals(Short.TYPE)) {
            return Arrays.hashCode((short[]) obj);
        }
        if (cls.equals(Integer.TYPE)) {
            return Arrays.hashCode((int[]) obj);
        }
        if (cls.equals(Character.TYPE)) {
            return Arrays.hashCode((char[]) obj);
        }
        if (cls.equals(Long.TYPE)) {
            return Arrays.hashCode((long[]) obj);
        }
        if (cls.equals(Float.TYPE)) {
            return Arrays.hashCode((float[]) obj);
        }
        if (cls.equals(Double.TYPE)) {
            return Arrays.hashCode((double[]) obj);
        }
        if (cls.equals(Boolean.TYPE) != null) {
            return Arrays.hashCode((boolean[]) obj);
        }
        return Arrays.hashCode((Object[]) obj);
    }
}
