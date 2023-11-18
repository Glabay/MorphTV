package org.apache.commons.lang3;

import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.mutable.MutableObject;

public class ClassUtils {
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final String PACKAGE_SEPARATOR = String.valueOf('.');
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    private static final Map<String, String> abbreviationMap;
    private static final Map<String, Class<?>> namePrimitiveMap = new HashMap();
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap();
    private static final Map<String, String> reverseAbbreviationMap;
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap();

    public enum Interfaces {
        INCLUDE,
        EXCLUDE
    }

    static {
        namePrimitiveMap.put(SerializerHandler.TYPE_BOOLEAN, Boolean.TYPE);
        namePrimitiveMap.put("byte", Byte.TYPE);
        namePrimitiveMap.put("char", Character.TYPE);
        namePrimitiveMap.put("short", Short.TYPE);
        namePrimitiveMap.put(SerializerHandler.TYPE_INT, Integer.TYPE);
        namePrimitiveMap.put("long", Long.TYPE);
        namePrimitiveMap.put(SerializerHandler.TYPE_DOUBLE, Double.TYPE);
        namePrimitiveMap.put("float", Float.TYPE);
        namePrimitiveMap.put("void", Void.TYPE);
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        for (Entry entry : primitiveWrapperMap.entrySet()) {
            Class cls = (Class) entry.getKey();
            Class cls2 = (Class) entry.getValue();
            if (!cls.equals(cls2)) {
                wrapperPrimitiveMap.put(cls2, cls);
            }
        }
        Map hashMap = new HashMap();
        hashMap.put(SerializerHandler.TYPE_INT, "I");
        hashMap.put(SerializerHandler.TYPE_BOOLEAN, "Z");
        hashMap.put("float", "F");
        hashMap.put("long", "J");
        hashMap.put("short", "S");
        hashMap.put("byte", "B");
        hashMap.put(SerializerHandler.TYPE_DOUBLE, "D");
        hashMap.put("char", "C");
        Map hashMap2 = new HashMap();
        for (Entry entry2 : hashMap.entrySet()) {
            hashMap2.put(entry2.getValue(), entry2.getKey());
        }
        abbreviationMap = Collections.unmodifiableMap(hashMap);
        reverseAbbreviationMap = Collections.unmodifiableMap(hashMap2);
    }

    public static String getShortClassName(Object obj, String str) {
        return obj == null ? str : getShortClassName(obj.getClass());
    }

    public static String getShortClassName(Class<?> cls) {
        return cls == null ? "" : getShortClassName(cls.getName());
    }

    public static String getShortClassName(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        if (str.startsWith("[")) {
            while (str.charAt(0) == '[') {
                str = str.substring(1);
                stringBuilder.append("[]");
            }
            if (str.charAt(0) == 'L' && str.charAt(str.length() - 1) == ';') {
                str = str.substring(1, str.length() - 1);
            }
            if (reverseAbbreviationMap.containsKey(str)) {
                str = (String) reverseAbbreviationMap.get(str);
            }
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf != -1) {
            i = lastIndexOf + 1;
        }
        i = str.indexOf(36, i);
        str = str.substring(lastIndexOf + 1);
        if (i != -1) {
            str = str.replace('$', '.');
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(stringBuilder);
        return stringBuilder2.toString();
    }

    public static String getSimpleName(Class<?> cls) {
        return cls == null ? "" : cls.getSimpleName();
    }

    public static String getSimpleName(Object obj, String str) {
        return obj == null ? str : getSimpleName(obj.getClass());
    }

    public static String getPackageName(Object obj, String str) {
        return obj == null ? str : getPackageName(obj.getClass());
    }

    public static String getPackageName(Class<?> cls) {
        return cls == null ? "" : getPackageName(cls.getName());
    }

    public static String getPackageName(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        while (str.charAt(0) == '[') {
            str = str.substring(1);
        }
        if (str.charAt(0) == 'L' && str.charAt(str.length() - 1) == ';') {
            str = str.substring(1);
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf == -1) {
            return "";
        }
        return str.substring(0, lastIndexOf);
    }

    public static String getAbbreviatedName(Class<?> cls, int i) {
        return cls == null ? "" : getAbbreviatedName(cls.getName(), i);
    }

    public static String getAbbreviatedName(String str, int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("len must be > 0");
        } else if (str == null) {
            return "";
        } else {
            int countMatches = StringUtils.countMatches((CharSequence) str, '.');
            Object[] objArr = new String[(countMatches + 1)];
            int length = str.length() - 1;
            int i2 = i;
            for (i = countMatches; i >= 0; i--) {
                int lastIndexOf = str.lastIndexOf(46, length);
                String substring = str.substring(lastIndexOf + 1, length + 1);
                i2 -= substring.length();
                if (i > 0) {
                    i2--;
                }
                if (i == countMatches) {
                    objArr[i] = substring;
                } else if (i2 > 0) {
                    objArr[i] = substring;
                } else {
                    objArr[i] = substring.substring(0, 1);
                }
                length = lastIndexOf - 1;
            }
            return StringUtils.join(objArr, '.');
        }
    }

    public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        List<Class<?>> arrayList = new ArrayList();
        for (cls = cls.getSuperclass(); cls != null; cls = cls.getSuperclass()) {
            arrayList.add(cls);
        }
        return arrayList;
    }

    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        Collection linkedHashSet = new LinkedHashSet();
        getAllInterfaces(cls, linkedHashSet);
        return new ArrayList(linkedHashSet);
    }

    private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> hashSet) {
        while (cls != null) {
            for (Class cls2 : cls.getInterfaces()) {
                if (hashSet.add(cls2)) {
                    getAllInterfaces(cls2, hashSet);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    public static java.util.List<java.lang.Class<?>> convertClassNamesToClasses(java.util.List<java.lang.String> r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r3 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = new java.util.ArrayList;
        r2 = r3.size();
        r1.<init>(r2);
        r3 = r3.iterator();
    L_0x0011:
        r2 = r3.hasNext();
        if (r2 == 0) goto L_0x0029;
    L_0x0017:
        r2 = r3.next();
        r2 = (java.lang.String) r2;
        r2 = java.lang.Class.forName(r2);	 Catch:{ Exception -> 0x0025 }
        r1.add(r2);	 Catch:{ Exception -> 0x0025 }
        goto L_0x0011;
    L_0x0025:
        r1.add(r0);
        goto L_0x0011;
    L_0x0029:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.ClassUtils.convertClassNamesToClasses(java.util.List):java.util.List<java.lang.Class<?>>");
    }

    public static List<String> convertClassesToClassNames(List<Class<?>> list) {
        if (list == null) {
            return null;
        }
        List<String> arrayList = new ArrayList(list.size());
        for (Class cls : list) {
            if (cls == null) {
                arrayList.add(null);
            } else {
                arrayList.add(cls.getName());
            }
        }
        return arrayList;
    }

    public static boolean isAssignable(Class<?>[] clsArr, Class<?>... clsArr2) {
        return isAssignable((Class[]) clsArr, (Class[]) clsArr2, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }

    public static boolean isAssignable(Class<?>[] clsArr, Class<?>[] clsArr2, boolean z) {
        if (!ArrayUtils.isSameLength((Object[]) clsArr, (Object[]) clsArr2)) {
            return false;
        }
        if (clsArr == null) {
            clsArr = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (clsArr2 == null) {
            clsArr2 = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < clsArr.length; i++) {
            if (!isAssignable(clsArr[i], clsArr2[i], z)) {
                return false;
            }
        }
        return 1;
    }

    public static boolean isPrimitiveOrWrapper(Class<?> cls) {
        boolean z = false;
        if (cls == null) {
            return false;
        }
        if (cls.isPrimitive() || isPrimitiveWrapper(cls) != null) {
            z = true;
        }
        return z;
    }

    public static boolean isPrimitiveWrapper(Class<?> cls) {
        return wrapperPrimitiveMap.containsKey(cls);
    }

    public static boolean isAssignable(Class<?> cls, Class<?> cls2) {
        return isAssignable((Class) cls, (Class) cls2, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }

    public static boolean isAssignable(Class<?> cls, Class<?> cls2, boolean z) {
        boolean z2 = false;
        if (cls2 == null) {
            return false;
        }
        if (cls == null) {
            return cls2.isPrimitive() ^ 1;
        }
        if (z) {
            if (cls.isPrimitive() && !cls2.isPrimitive()) {
                cls = primitiveToWrapper(cls);
                if (cls == null) {
                    return false;
                }
            }
            if (cls2.isPrimitive() && !cls.isPrimitive()) {
                cls = wrapperToPrimitive(cls);
                if (cls == null) {
                    return false;
                }
            }
        }
        if (cls.equals(cls2)) {
            return true;
        }
        if (!cls.isPrimitive()) {
            return cls2.isAssignableFrom(cls);
        }
        if (!cls2.isPrimitive()) {
            return false;
        }
        if (Integer.TYPE.equals(cls)) {
            if (!(Long.TYPE.equals(cls2) == null && Float.TYPE.equals(cls2) == null && Double.TYPE.equals(cls2) == null)) {
                z2 = true;
            }
            return z2;
        } else if (Long.TYPE.equals(cls)) {
            if (!(Float.TYPE.equals(cls2) == null && Double.TYPE.equals(cls2) == null)) {
                z2 = true;
            }
            return z2;
        } else if (Boolean.TYPE.equals(cls) || Double.TYPE.equals(cls)) {
            return false;
        } else {
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(cls2);
            }
            if (Character.TYPE.equals(cls)) {
                if (!(Integer.TYPE.equals(cls2) == null && Long.TYPE.equals(cls2) == null && Float.TYPE.equals(cls2) == null && Double.TYPE.equals(cls2) == null)) {
                    z2 = true;
                }
                return z2;
            } else if (Short.TYPE.equals(cls)) {
                if (!(Integer.TYPE.equals(cls2) == null && Long.TYPE.equals(cls2) == null && Float.TYPE.equals(cls2) == null && Double.TYPE.equals(cls2) == null)) {
                    z2 = true;
                }
                return z2;
            } else if (Byte.TYPE.equals(cls) == null) {
                return false;
            } else {
                if (!(Short.TYPE.equals(cls2) == null && Integer.TYPE.equals(cls2) == null && Long.TYPE.equals(cls2) == null && Float.TYPE.equals(cls2) == null && Double.TYPE.equals(cls2) == null)) {
                    z2 = true;
                }
                return z2;
            }
        }
    }

    public static Class<?> primitiveToWrapper(Class<?> cls) {
        return (cls == null || !cls.isPrimitive()) ? cls : (Class) primitiveWrapperMap.get(cls);
    }

    public static Class<?>[] primitivesToWrappers(Class<?>... clsArr) {
        if (clsArr == null) {
            return null;
        }
        if (clsArr.length == 0) {
            return clsArr;
        }
        Class<?>[] clsArr2 = new Class[clsArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            clsArr2[i] = primitiveToWrapper(clsArr[i]);
        }
        return clsArr2;
    }

    public static Class<?> wrapperToPrimitive(Class<?> cls) {
        return (Class) wrapperPrimitiveMap.get(cls);
    }

    public static Class<?>[] wrappersToPrimitives(Class<?>... clsArr) {
        if (clsArr == null) {
            return null;
        }
        if (clsArr.length == 0) {
            return clsArr;
        }
        Class<?>[] clsArr2 = new Class[clsArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            clsArr2[i] = wrapperToPrimitive(clsArr[i]);
        }
        return clsArr2;
    }

    public static boolean isInnerClass(Class<?> cls) {
        return (cls == null || cls.getEnclosingClass() == null) ? null : true;
    }

    public static java.lang.Class<?> getClass(java.lang.ClassLoader r4, java.lang.String r5, boolean r6) throws java.lang.ClassNotFoundException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = namePrimitiveMap;	 Catch:{ ClassNotFoundException -> 0x001a }
        r0 = r0.containsKey(r5);	 Catch:{ ClassNotFoundException -> 0x001a }
        if (r0 == 0) goto L_0x0011;	 Catch:{ ClassNotFoundException -> 0x001a }
    L_0x0008:
        r0 = namePrimitiveMap;	 Catch:{ ClassNotFoundException -> 0x001a }
        r0 = r0.get(r5);	 Catch:{ ClassNotFoundException -> 0x001a }
        r0 = (java.lang.Class) r0;	 Catch:{ ClassNotFoundException -> 0x001a }
        goto L_0x0019;	 Catch:{ ClassNotFoundException -> 0x001a }
    L_0x0011:
        r0 = toCanonicalName(r5);	 Catch:{ ClassNotFoundException -> 0x001a }
        r0 = java.lang.Class.forName(r0, r6, r4);	 Catch:{ ClassNotFoundException -> 0x001a }
    L_0x0019:
        return r0;
    L_0x001a:
        r0 = move-exception;
        r1 = 46;
        r1 = r5.lastIndexOf(r1);
        r2 = -1;
        if (r1 == r2) goto L_0x0048;
    L_0x0024:
        r2 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0048 }
        r2.<init>();	 Catch:{ ClassNotFoundException -> 0x0048 }
        r3 = 0;	 Catch:{ ClassNotFoundException -> 0x0048 }
        r3 = r5.substring(r3, r1);	 Catch:{ ClassNotFoundException -> 0x0048 }
        r2.append(r3);	 Catch:{ ClassNotFoundException -> 0x0048 }
        r3 = 36;	 Catch:{ ClassNotFoundException -> 0x0048 }
        r2.append(r3);	 Catch:{ ClassNotFoundException -> 0x0048 }
        r1 = r1 + 1;	 Catch:{ ClassNotFoundException -> 0x0048 }
        r5 = r5.substring(r1);	 Catch:{ ClassNotFoundException -> 0x0048 }
        r2.append(r5);	 Catch:{ ClassNotFoundException -> 0x0048 }
        r5 = r2.toString();	 Catch:{ ClassNotFoundException -> 0x0048 }
        r4 = getClass(r4, r5, r6);	 Catch:{ ClassNotFoundException -> 0x0048 }
        return r4;
    L_0x0048:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.ClassUtils.getClass(java.lang.ClassLoader, java.lang.String, boolean):java.lang.Class<?>");
    }

    public static Class<?> getClass(ClassLoader classLoader, String str) throws ClassNotFoundException {
        return getClass(classLoader, str, true);
    }

    public static Class<?> getClass(String str) throws ClassNotFoundException {
        return getClass(str, true);
    }

    public static Class<?> getClass(String str, boolean z) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            contextClassLoader = ClassUtils.class.getClassLoader();
        }
        return getClass(contextClassLoader, str, z);
    }

    public static java.lang.reflect.Method getPublicMethod(java.lang.Class<?> r2, java.lang.String r3, java.lang.Class<?>... r4) throws java.lang.SecurityException, java.lang.NoSuchMethodException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = r2.getMethod(r3, r4);
        r1 = r0.getDeclaringClass();
        r1 = r1.getModifiers();
        r1 = java.lang.reflect.Modifier.isPublic(r1);
        if (r1 == 0) goto L_0x0013;
    L_0x0012:
        return r0;
    L_0x0013:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = getAllInterfaces(r2);
        r0.addAll(r1);
        r2 = getAllSuperclasses(r2);
        r0.addAll(r2);
        r2 = r0.iterator();
    L_0x002a:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x0054;
    L_0x0030:
        r0 = r2.next();
        r0 = (java.lang.Class) r0;
        r1 = r0.getModifiers();
        r1 = java.lang.reflect.Modifier.isPublic(r1);
        if (r1 != 0) goto L_0x0041;
    L_0x0040:
        goto L_0x002a;
    L_0x0041:
        r0 = r0.getMethod(r3, r4);	 Catch:{ NoSuchMethodException -> 0x002a }
        r1 = r0.getDeclaringClass();
        r1 = r1.getModifiers();
        r1 = java.lang.reflect.Modifier.isPublic(r1);
        if (r1 == 0) goto L_0x002a;
    L_0x0053:
        return r0;
    L_0x0054:
        r2 = new java.lang.NoSuchMethodException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Can't find a public method for ";
        r0.append(r1);
        r0.append(r3);
        r3 = " ";
        r0.append(r3);
        r3 = org.apache.commons.lang3.ArrayUtils.toString(r4);
        r0.append(r3);
        r3 = r0.toString();
        r2.<init>(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.ClassUtils.getPublicMethod(java.lang.Class, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

    private static String toCanonicalName(String str) {
        str = StringUtils.deleteWhitespace(str);
        if (str == null) {
            throw new NullPointerException("className must not be null.");
        } else if (!str.endsWith("[]")) {
            return str;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            while (str.endsWith("[]")) {
                str = str.substring(0, str.length() - 2);
                stringBuilder.append("[");
            }
            String str2 = (String) abbreviationMap.get(str);
            if (str2 != null) {
                stringBuilder.append(str2);
            } else {
                stringBuilder.append("L");
                stringBuilder.append(str);
                stringBuilder.append(";");
            }
            return stringBuilder.toString();
        }
    }

    public static Class<?>[] toClass(Object... objArr) {
        if (objArr == null) {
            return null;
        }
        if (objArr.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            clsArr[i] = objArr[i] == null ? null : objArr[i].getClass();
        }
        return clsArr;
    }

    public static String getShortCanonicalName(Object obj, String str) {
        return obj == null ? str : getShortCanonicalName(obj.getClass().getName());
    }

    public static String getShortCanonicalName(Class<?> cls) {
        return cls == null ? "" : getShortCanonicalName(cls.getName());
    }

    public static String getShortCanonicalName(String str) {
        return getShortClassName(getCanonicalName(str));
    }

    public static String getPackageCanonicalName(Object obj, String str) {
        return obj == null ? str : getPackageCanonicalName(obj.getClass().getName());
    }

    public static String getPackageCanonicalName(Class<?> cls) {
        return cls == null ? "" : getPackageCanonicalName(cls.getName());
    }

    public static String getPackageCanonicalName(String str) {
        return getPackageName(getCanonicalName(str));
    }

    private static String getCanonicalName(String str) {
        str = StringUtils.deleteWhitespace(str);
        if (str == null) {
            return null;
        }
        int i = 0;
        int i2 = 0;
        while (str.startsWith("[")) {
            i2++;
            str = str.substring(1);
        }
        if (i2 < 1) {
            return str;
        }
        if (str.startsWith("L")) {
            int length;
            if (str.endsWith(";")) {
                length = str.length() - 1;
            } else {
                length = str.length();
            }
            str = str.substring(1, length);
        } else if (str.length() > 0) {
            str = (String) reverseAbbreviationMap.get(str.substring(0, 1));
        }
        StringBuilder stringBuilder = new StringBuilder(str);
        while (i < i2) {
            stringBuilder.append("[]");
            i++;
        }
        return stringBuilder.toString();
    }

    public static Iterable<Class<?>> hierarchy(Class<?> cls) {
        return hierarchy(cls, Interfaces.EXCLUDE);
    }

    public static Iterable<Class<?>> hierarchy(final Class<?> cls, Interfaces interfaces) {
        final Iterable c14431 = new Iterable<Class<?>>() {
            public Iterator<Class<?>> iterator() {
                final MutableObject mutableObject = new MutableObject(cls);
                return new Iterator<Class<?>>() {
                    public boolean hasNext() {
                        return mutableObject.getValue() != null;
                    }

                    public Class<?> next() {
                        Class<?> cls = (Class) mutableObject.getValue();
                        mutableObject.setValue(cls.getSuperclass());
                        return cls;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        if (interfaces != Interfaces.INCLUDE) {
            return c14431;
        }
        return new Iterable<Class<?>>() {
            public Iterator<Class<?>> iterator() {
                final Set hashSet = new HashSet();
                final Iterator it = c14431.iterator();
                return new Iterator<Class<?>>() {
                    Iterator<Class<?>> interfaces = Collections.emptySet().iterator();

                    public boolean hasNext() {
                        if (!this.interfaces.hasNext()) {
                            if (!it.hasNext()) {
                                return false;
                            }
                        }
                        return true;
                    }

                    public Class<?> next() {
                        if (this.interfaces.hasNext()) {
                            Class<?> cls = (Class) this.interfaces.next();
                            hashSet.add(cls);
                            return cls;
                        }
                        cls = (Class) it.next();
                        Set linkedHashSet = new LinkedHashSet();
                        walkInterfaces(linkedHashSet, cls);
                        this.interfaces = linkedHashSet.iterator();
                        return cls;
                    }

                    private void walkInterfaces(Set<Class<?>> set, Class<?> cls) {
                        for (Class cls2 : cls.getInterfaces()) {
                            if (!hashSet.contains(cls2)) {
                                set.add(cls2);
                            }
                            walkInterfaces(set, cls2);
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
