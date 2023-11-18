package org.apache.commons.lang3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class FieldUtils {
    public static Field getField(Class<?> cls, String str) {
        cls = getField(cls, str, false);
        MemberUtils.setAccessibleWorkaround(cls);
        return cls;
    }

    public static java.lang.reflect.Field getField(java.lang.Class<?> r7, java.lang.String r8, boolean r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 1;
        r1 = 0;
        if (r7 == 0) goto L_0x0006;
    L_0x0004:
        r2 = 1;
        goto L_0x0007;
    L_0x0006:
        r2 = 0;
    L_0x0007:
        r3 = "The class must not be null";
        r4 = new java.lang.Object[r1];
        org.apache.commons.lang3.Validate.isTrue(r2, r3, r4);
        r2 = org.apache.commons.lang3.StringUtils.isNotBlank(r8);
        r3 = "The field name must not be blank/empty";
        r4 = new java.lang.Object[r1];
        org.apache.commons.lang3.Validate.isTrue(r2, r3, r4);
        r2 = r7;
    L_0x001a:
        if (r2 == 0) goto L_0x0035;
    L_0x001c:
        r3 = r2.getDeclaredField(r8);	 Catch:{ NoSuchFieldException -> 0x0030 }
        r4 = r3.getModifiers();	 Catch:{ NoSuchFieldException -> 0x0030 }
        r4 = java.lang.reflect.Modifier.isPublic(r4);	 Catch:{ NoSuchFieldException -> 0x0030 }
        if (r4 != 0) goto L_0x002f;	 Catch:{ NoSuchFieldException -> 0x0030 }
    L_0x002a:
        if (r9 == 0) goto L_0x0030;	 Catch:{ NoSuchFieldException -> 0x0030 }
    L_0x002c:
        r3.setAccessible(r0);	 Catch:{ NoSuchFieldException -> 0x0030 }
    L_0x002f:
        return r3;
    L_0x0030:
        r2 = r2.getSuperclass();
        goto L_0x001a;
    L_0x0035:
        r9 = 0;
        r2 = org.apache.commons.lang3.ClassUtils.getAllInterfaces(r7);
        r2 = r2.iterator();
    L_0x003e:
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x0061;
    L_0x0044:
        r3 = r2.next();
        r3 = (java.lang.Class) r3;
        r3 = r3.getField(r8);	 Catch:{ NoSuchFieldException -> 0x003e }
        if (r9 != 0) goto L_0x0052;	 Catch:{ NoSuchFieldException -> 0x003e }
    L_0x0050:
        r4 = 1;	 Catch:{ NoSuchFieldException -> 0x003e }
        goto L_0x0053;	 Catch:{ NoSuchFieldException -> 0x003e }
    L_0x0052:
        r4 = 0;	 Catch:{ NoSuchFieldException -> 0x003e }
    L_0x0053:
        r5 = "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.";	 Catch:{ NoSuchFieldException -> 0x003e }
        r6 = 2;	 Catch:{ NoSuchFieldException -> 0x003e }
        r6 = new java.lang.Object[r6];	 Catch:{ NoSuchFieldException -> 0x003e }
        r6[r1] = r8;	 Catch:{ NoSuchFieldException -> 0x003e }
        r6[r0] = r7;	 Catch:{ NoSuchFieldException -> 0x003e }
        org.apache.commons.lang3.Validate.isTrue(r4, r5, r6);	 Catch:{ NoSuchFieldException -> 0x003e }
        r9 = r3;
        goto L_0x003e;
    L_0x0061:
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.FieldUtils.getField(java.lang.Class, java.lang.String, boolean):java.lang.reflect.Field");
    }

    public static Field getDeclaredField(Class<?> cls, String str) {
        return getDeclaredField(cls, str, false);
    }

    public static java.lang.reflect.Field getDeclaredField(java.lang.Class<?> r5, java.lang.String r6, boolean r7) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 1;
        r1 = 0;
        if (r5 == 0) goto L_0x0006;
    L_0x0004:
        r2 = 1;
        goto L_0x0007;
    L_0x0006:
        r2 = 0;
    L_0x0007:
        r3 = "The class must not be null";
        r4 = new java.lang.Object[r1];
        org.apache.commons.lang3.Validate.isTrue(r2, r3, r4);
        r2 = org.apache.commons.lang3.StringUtils.isNotBlank(r6);
        r3 = "The field name must not be blank/empty";
        r1 = new java.lang.Object[r1];
        org.apache.commons.lang3.Validate.isTrue(r2, r3, r1);
        r1 = 0;
        r5 = r5.getDeclaredField(r6);	 Catch:{ NoSuchFieldException -> 0x002c }
        r6 = org.apache.commons.lang3.reflect.MemberUtils.isAccessible(r5);	 Catch:{ NoSuchFieldException -> 0x002c }
        if (r6 != 0) goto L_0x002b;	 Catch:{ NoSuchFieldException -> 0x002c }
    L_0x0024:
        if (r7 == 0) goto L_0x002a;	 Catch:{ NoSuchFieldException -> 0x002c }
    L_0x0026:
        r5.setAccessible(r0);	 Catch:{ NoSuchFieldException -> 0x002c }
        goto L_0x002b;
    L_0x002a:
        return r1;
    L_0x002b:
        return r5;
    L_0x002c:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField(java.lang.Class, java.lang.String, boolean):java.lang.reflect.Field");
    }

    public static Field[] getAllFields(Class<?> cls) {
        cls = getAllFieldsList(cls);
        return (Field[]) cls.toArray(new Field[cls.size()]);
    }

    public static List<Field> getAllFieldsList(Class<?> cls) {
        Validate.isTrue(cls != null, "The class must not be null", new Object[0]);
        List<Field> arrayList = new ArrayList();
        while (cls != null) {
            for (Object add : cls.getDeclaredFields()) {
                arrayList.add(add);
            }
            cls = cls.getSuperclass();
        }
        return arrayList;
    }

    public static Field[] getFieldsWithAnnotation(Class<?> cls, Class<? extends Annotation> cls2) {
        cls = getFieldsListWithAnnotation(cls, cls2);
        return (Field[]) cls.toArray(new Field[cls.size()]);
    }

    public static List<Field> getFieldsListWithAnnotation(Class<?> cls, Class<? extends Annotation> cls2) {
        Validate.isTrue(cls2 != null, "The annotation class must not be null", new Object[0]);
        cls = getAllFieldsList(cls);
        List<Field> arrayList = new ArrayList();
        cls = cls.iterator();
        while (cls.hasNext()) {
            Field field = (Field) cls.next();
            if (field.getAnnotation(cls2) != null) {
                arrayList.add(field);
            }
        }
        return arrayList;
    }

    public static Object readStaticField(Field field) throws IllegalAccessException {
        return readStaticField(field, false);
    }

    public static Object readStaticField(Field field, boolean z) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", field.getName());
        return readField(field, null, z);
    }

    public static Object readStaticField(Class<?> cls, String str) throws IllegalAccessException {
        return readStaticField(cls, str, false);
    }

    public static Object readStaticField(Class<?> cls, String str, boolean z) throws IllegalAccessException {
        Field field = getField(cls, str, z);
        Validate.isTrue(field != null, "Cannot locate field '%s' on %s", str, cls);
        return readStaticField(field, false);
    }

    public static Object readDeclaredStaticField(Class<?> cls, String str) throws IllegalAccessException {
        return readDeclaredStaticField(cls, str, false);
    }

    public static Object readDeclaredStaticField(Class<?> cls, String str, boolean z) throws IllegalAccessException {
        Field declaredField = getDeclaredField(cls, str, z);
        Validate.isTrue(declaredField != null, "Cannot locate declared field %s.%s", cls.getName(), str);
        return readStaticField(declaredField, false);
    }

    public static Object readField(Field field, Object obj) throws IllegalAccessException {
        return readField(field, obj, false);
    }

    public static Object readField(Field field, Object obj, boolean z) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        if (!z || field.isAccessible()) {
            MemberUtils.setAccessibleWorkaround(field);
        } else {
            field.setAccessible(true);
        }
        return field.get(obj);
    }

    public static Object readField(Object obj, String str) throws IllegalAccessException {
        return readField(obj, str, false);
    }

    public static Object readField(Object obj, String str, boolean z) throws IllegalAccessException {
        Validate.isTrue(obj != null, "target object must not be null", new Object[0]);
        Field field = getField(obj.getClass(), str, z);
        Validate.isTrue(field != null, "Cannot locate field %s on %s", str, obj.getClass());
        return readField(field, obj, false);
    }

    public static Object readDeclaredField(Object obj, String str) throws IllegalAccessException {
        return readDeclaredField(obj, str, false);
    }

    public static Object readDeclaredField(Object obj, String str, boolean z) throws IllegalAccessException {
        Validate.isTrue(obj != null, "target object must not be null", new Object[0]);
        Field declaredField = getDeclaredField(obj.getClass(), str, z);
        Validate.isTrue(declaredField != null, "Cannot locate declared field %s.%s", obj.getClass(), str);
        return readField(declaredField, obj, false);
    }

    public static void writeStaticField(Field field, Object obj) throws IllegalAccessException {
        writeStaticField(field, obj, false);
    }

    public static void writeStaticField(Field field, Object obj, boolean z) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", field.getDeclaringClass().getName(), field.getName());
        writeField(field, null, obj, z);
    }

    public static void writeStaticField(Class<?> cls, String str, Object obj) throws IllegalAccessException {
        writeStaticField(cls, str, obj, false);
    }

    public static void writeStaticField(Class<?> cls, String str, Object obj, boolean z) throws IllegalAccessException {
        Field field = getField(cls, str, z);
        Validate.isTrue(field != null, "Cannot locate field %s on %s", str, cls);
        writeStaticField(field, obj, false);
    }

    public static void writeDeclaredStaticField(Class<?> cls, String str, Object obj) throws IllegalAccessException {
        writeDeclaredStaticField(cls, str, obj, false);
    }

    public static void writeDeclaredStaticField(Class<?> cls, String str, Object obj, boolean z) throws IllegalAccessException {
        Field declaredField = getDeclaredField(cls, str, z);
        Validate.isTrue(declaredField != null, "Cannot locate declared field %s.%s", cls.getName(), str);
        writeField(declaredField, null, obj, false);
    }

    public static void writeField(Field field, Object obj, Object obj2) throws IllegalAccessException {
        writeField(field, obj, obj2, false);
    }

    public static void writeField(Field field, Object obj, Object obj2, boolean z) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        if (!z || field.isAccessible()) {
            MemberUtils.setAccessibleWorkaround(field);
        } else {
            field.setAccessible(true);
        }
        field.set(obj, obj2);
    }

    public static void removeFinalModifier(Field field) {
        removeFinalModifier(field, true);
    }

    public static void removeFinalModifier(java.lang.reflect.Field r5, boolean r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 1;
        r1 = 0;
        if (r5 == 0) goto L_0x0006;
    L_0x0004:
        r2 = 1;
        goto L_0x0007;
    L_0x0006:
        r2 = 0;
    L_0x0007:
        r3 = "The field must not be null";
        r4 = new java.lang.Object[r1];
        org.apache.commons.lang3.Validate.isTrue(r2, r3, r4);
        r2 = r5.getModifiers();	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        r2 = java.lang.reflect.Modifier.isFinal(r2);	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        if (r2 == 0) goto L_0x0046;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x0018:
        r2 = java.lang.reflect.Field.class;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        r3 = "modifiers";	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        r2 = r2.getDeclaredField(r3);	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        if (r6 == 0) goto L_0x002a;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x0022:
        r6 = r2.isAccessible();	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        if (r6 != 0) goto L_0x002a;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x0028:
        r6 = 1;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        goto L_0x002b;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x002a:
        r6 = 0;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x002b:
        if (r6 == 0) goto L_0x0030;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x002d:
        r2.setAccessible(r0);	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x0030:
        r0 = r5.getModifiers();	 Catch:{ all -> 0x003f }
        r0 = r0 & -17;	 Catch:{ all -> 0x003f }
        r2.setInt(r5, r0);	 Catch:{ all -> 0x003f }
        if (r6 == 0) goto L_0x0046;
    L_0x003b:
        r2.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        goto L_0x0046;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x003f:
        r5 = move-exception;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
        if (r6 == 0) goto L_0x0045;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x0042:
        r2.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x0045:
        throw r5;	 Catch:{ NoSuchFieldException -> 0x0046, NoSuchFieldException -> 0x0046 }
    L_0x0046:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.reflect.FieldUtils.removeFinalModifier(java.lang.reflect.Field, boolean):void");
    }

    public static void writeField(Object obj, String str, Object obj2) throws IllegalAccessException {
        writeField(obj, str, obj2, false);
    }

    public static void writeField(Object obj, String str, Object obj2, boolean z) throws IllegalAccessException {
        Validate.isTrue(obj != null, "target object must not be null", new Object[0]);
        Field field = getField(obj.getClass(), str, z);
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", obj.getClass().getName(), str);
        writeField(field, obj, obj2, false);
    }

    public static void writeDeclaredField(Object obj, String str, Object obj2) throws IllegalAccessException {
        writeDeclaredField(obj, str, obj2, false);
    }

    public static void writeDeclaredField(Object obj, String str, Object obj2, boolean z) throws IllegalAccessException {
        Validate.isTrue(obj != null, "target object must not be null", new Object[0]);
        Field declaredField = getDeclaredField(obj.getClass(), str, z);
        Validate.isTrue(declaredField != null, "Cannot locate declared field %s.%s", obj.getClass().getName(), str);
        writeField(declaredField, obj, obj2, false);
    }
}
