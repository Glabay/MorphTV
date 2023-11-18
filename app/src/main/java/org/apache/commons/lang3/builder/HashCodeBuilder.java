package org.apache.commons.lang3.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Validate;

public class HashCodeBuilder implements Builder<Integer> {
    private static final int DEFAULT_INITIAL_VALUE = 17;
    private static final int DEFAULT_MULTIPLIER_VALUE = 37;
    private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal();
    private final int iConstant;
    private int iTotal;

    static Set<IDKey> getRegistry() {
        return (Set) REGISTRY.get();
    }

    static boolean isRegistered(Object obj) {
        Set registry = getRegistry();
        return (registry == null || registry.contains(new IDKey(obj)) == null) ? null : true;
    }

    private static void reflectionAppend(java.lang.Object r5, java.lang.Class<?> r6, org.apache.commons.lang3.builder.HashCodeBuilder r7, boolean r8, java.lang.String[] r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = isRegistered(r5);
        if (r0 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        register(r5);	 Catch:{ all -> 0x0063 }
        r6 = r6.getDeclaredFields();	 Catch:{ all -> 0x0063 }
        r0 = 1;	 Catch:{ all -> 0x0063 }
        java.lang.reflect.AccessibleObject.setAccessible(r6, r0);	 Catch:{ all -> 0x0063 }
        r0 = r6.length;	 Catch:{ all -> 0x0063 }
        r1 = 0;	 Catch:{ all -> 0x0063 }
    L_0x0014:
        if (r1 >= r0) goto L_0x005f;	 Catch:{ all -> 0x0063 }
    L_0x0016:
        r2 = r6[r1];	 Catch:{ all -> 0x0063 }
        r3 = r2.getName();	 Catch:{ all -> 0x0063 }
        r3 = org.apache.commons.lang3.ArrayUtils.contains(r9, r3);	 Catch:{ all -> 0x0063 }
        if (r3 != 0) goto L_0x005c;	 Catch:{ all -> 0x0063 }
    L_0x0022:
        r3 = r2.getName();	 Catch:{ all -> 0x0063 }
        r4 = "$";	 Catch:{ all -> 0x0063 }
        r3 = r3.contains(r4);	 Catch:{ all -> 0x0063 }
        if (r3 != 0) goto L_0x005c;	 Catch:{ all -> 0x0063 }
    L_0x002e:
        if (r8 != 0) goto L_0x003a;	 Catch:{ all -> 0x0063 }
    L_0x0030:
        r3 = r2.getModifiers();	 Catch:{ all -> 0x0063 }
        r3 = java.lang.reflect.Modifier.isTransient(r3);	 Catch:{ all -> 0x0063 }
        if (r3 != 0) goto L_0x005c;	 Catch:{ all -> 0x0063 }
    L_0x003a:
        r3 = r2.getModifiers();	 Catch:{ all -> 0x0063 }
        r3 = java.lang.reflect.Modifier.isStatic(r3);	 Catch:{ all -> 0x0063 }
        if (r3 != 0) goto L_0x005c;	 Catch:{ all -> 0x0063 }
    L_0x0044:
        r3 = org.apache.commons.lang3.builder.HashCodeExclude.class;	 Catch:{ all -> 0x0063 }
        r3 = r2.isAnnotationPresent(r3);	 Catch:{ all -> 0x0063 }
        if (r3 != 0) goto L_0x005c;
    L_0x004c:
        r2 = r2.get(r5);	 Catch:{ IllegalAccessException -> 0x0054 }
        r7.append(r2);	 Catch:{ IllegalAccessException -> 0x0054 }
        goto L_0x005c;
    L_0x0054:
        r6 = new java.lang.InternalError;	 Catch:{ all -> 0x0063 }
        r7 = "Unexpected IllegalAccessException";	 Catch:{ all -> 0x0063 }
        r6.<init>(r7);	 Catch:{ all -> 0x0063 }
        throw r6;	 Catch:{ all -> 0x0063 }
    L_0x005c:
        r1 = r1 + 1;
        goto L_0x0014;
    L_0x005f:
        unregister(r5);
        return;
    L_0x0063:
        r6 = move-exception;
        unregister(r5);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.builder.HashCodeBuilder.reflectionAppend(java.lang.Object, java.lang.Class, org.apache.commons.lang3.builder.HashCodeBuilder, boolean, java.lang.String[]):void");
    }

    public static int reflectionHashCode(int i, int i2, Object obj) {
        return reflectionHashCode(i, i2, obj, false, null, new String[0]);
    }

    public static int reflectionHashCode(int i, int i2, Object obj, boolean z) {
        return reflectionHashCode(i, i2, obj, z, null, new String[0]);
    }

    public static <T> int reflectionHashCode(int i, int i2, T t, boolean z, Class<? super T> cls, String... strArr) {
        if (t == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        }
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(i, i2);
        i = t.getClass();
        reflectionAppend(t, i, hashCodeBuilder, z, strArr);
        while (i.getSuperclass() != 0 && i != cls) {
            i = i.getSuperclass();
            reflectionAppend(t, i, hashCodeBuilder, z, strArr);
        }
        return hashCodeBuilder.toHashCode();
    }

    public static int reflectionHashCode(Object obj, boolean z) {
        return reflectionHashCode(17, 37, obj, z, null, new String[0]);
    }

    public static int reflectionHashCode(Object obj, Collection<String> collection) {
        return reflectionHashCode(obj, ReflectionToStringBuilder.toNoNullStringArray((Collection) collection));
    }

    public static int reflectionHashCode(Object obj, String... strArr) {
        return reflectionHashCode(17, 37, obj, false, null, strArr);
    }

    private static void register(Object obj) {
        Set registry = getRegistry();
        if (registry == null) {
            registry = new HashSet();
            REGISTRY.set(registry);
        }
        registry.add(new IDKey(obj));
    }

    private static void unregister(Object obj) {
        Set registry = getRegistry();
        if (registry != null) {
            registry.remove(new IDKey(obj));
            if (registry.isEmpty() != null) {
                REGISTRY.remove();
            }
        }
    }

    public HashCodeBuilder() {
        this.iTotal = 0;
        this.iConstant = 37;
        this.iTotal = 17;
    }

    public HashCodeBuilder(int i, int i2) {
        this.iTotal = 0;
        boolean z = true;
        Validate.isTrue(i % 2 != 0, "HashCodeBuilder requires an odd initial value", new Object[0]);
        if (i2 % 2 == 0) {
            z = false;
        }
        Validate.isTrue(z, "HashCodeBuilder requires an odd multiplier", new Object[0]);
        this.iConstant = i2;
        this.iTotal = i;
    }

    public HashCodeBuilder append(boolean z) {
        this.iTotal = (this.iTotal * this.iConstant) + (z ^ 1);
        return this;
    }

    public HashCodeBuilder append(boolean[] zArr) {
        if (zArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (boolean append : zArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(byte b) {
        this.iTotal = (this.iTotal * this.iConstant) + b;
        return this;
    }

    public HashCodeBuilder append(byte[] bArr) {
        if (bArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (byte append : bArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(char c) {
        this.iTotal = (this.iTotal * this.iConstant) + c;
        return this;
    }

    public HashCodeBuilder append(char[] cArr) {
        if (cArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (char append : cArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(double d) {
        return append(Double.doubleToLongBits(d));
    }

    public HashCodeBuilder append(double[] dArr) {
        if (dArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (double append : dArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(float f) {
        this.iTotal = (this.iTotal * this.iConstant) + Float.floatToIntBits(f);
        return this;
    }

    public HashCodeBuilder append(float[] fArr) {
        if (fArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (float append : fArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(int i) {
        this.iTotal = (this.iTotal * this.iConstant) + i;
        return this;
    }

    public HashCodeBuilder append(int[] iArr) {
        if (iArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (int append : iArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(long j) {
        this.iTotal = (this.iTotal * this.iConstant) + ((int) (j ^ (j >> 32)));
        return this;
    }

    public HashCodeBuilder append(long[] jArr) {
        if (jArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (long append : jArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(Object obj) {
        if (obj == null) {
            this.iTotal *= this.iConstant;
        } else if (obj.getClass().isArray()) {
            appendArray(obj);
        } else {
            this.iTotal = (this.iTotal * this.iConstant) + obj.hashCode();
        }
        return this;
    }

    private void appendArray(Object obj) {
        if (obj instanceof long[]) {
            append((long[]) obj);
        } else if (obj instanceof int[]) {
            append((int[]) obj);
        } else if (obj instanceof short[]) {
            append((short[]) obj);
        } else if (obj instanceof char[]) {
            append((char[]) obj);
        } else if (obj instanceof byte[]) {
            append((byte[]) obj);
        } else if (obj instanceof double[]) {
            append((double[]) obj);
        } else if (obj instanceof float[]) {
            append((float[]) obj);
        } else if (obj instanceof boolean[]) {
            append((boolean[]) obj);
        } else {
            append((Object[]) obj);
        }
    }

    public HashCodeBuilder append(Object[] objArr) {
        if (objArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (Object append : objArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder append(short s) {
        this.iTotal = (this.iTotal * this.iConstant) + s;
        return this;
    }

    public HashCodeBuilder append(short[] sArr) {
        if (sArr == null) {
            this.iTotal *= this.iConstant;
        } else {
            for (short append : sArr) {
                append(append);
            }
        }
        return this;
    }

    public HashCodeBuilder appendSuper(int i) {
        this.iTotal = (this.iTotal * this.iConstant) + i;
        return this;
    }

    public int toHashCode() {
        return this.iTotal;
    }

    public Integer build() {
        return Integer.valueOf(toHashCode());
    }

    public int hashCode() {
        return toHashCode();
    }
}
