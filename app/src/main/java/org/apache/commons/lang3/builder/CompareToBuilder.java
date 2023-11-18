package org.apache.commons.lang3.builder;

import java.util.Collection;
import java.util.Comparator;

public class CompareToBuilder implements Builder<Integer> {
    private int comparison = 0;

    public static int reflectionCompare(Object obj, Object obj2) {
        return reflectionCompare(obj, obj2, false, null, new String[0]);
    }

    public static int reflectionCompare(Object obj, Object obj2, boolean z) {
        return reflectionCompare(obj, obj2, z, null, new String[0]);
    }

    public static int reflectionCompare(Object obj, Object obj2, Collection<String> collection) {
        return reflectionCompare(obj, obj2, ReflectionToStringBuilder.toNoNullStringArray((Collection) collection));
    }

    public static int reflectionCompare(Object obj, Object obj2, String... strArr) {
        return reflectionCompare(obj, obj2, false, null, strArr);
    }

    public static int reflectionCompare(Object obj, Object obj2, boolean z, Class<?> cls, String... strArr) {
        if (obj == obj2) {
            return null;
        }
        if (obj != null) {
            if (obj2 != null) {
                Class<?> cls2 = obj.getClass();
                if (cls2.isInstance(obj2)) {
                    CompareToBuilder compareToBuilder = new CompareToBuilder();
                    reflectionAppend(obj, obj2, cls2, compareToBuilder, z, strArr);
                    while (cls2.getSuperclass() != null && cls2 != cls) {
                        cls2 = cls2.getSuperclass();
                        reflectionAppend(obj, obj2, cls2, compareToBuilder, z, strArr);
                    }
                    return compareToBuilder.toComparison();
                }
                throw new ClassCastException();
            }
        }
        throw new NullPointerException();
    }

    private static void reflectionAppend(java.lang.Object r4, java.lang.Object r5, java.lang.Class<?> r6, org.apache.commons.lang3.builder.CompareToBuilder r7, boolean r8, java.lang.String[] r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r6 = r6.getDeclaredFields();
        r0 = 1;
        java.lang.reflect.AccessibleObject.setAccessible(r6, r0);
        r0 = 0;
    L_0x0009:
        r1 = r6.length;
        if (r0 >= r1) goto L_0x0055;
    L_0x000c:
        r1 = r7.comparison;
        if (r1 != 0) goto L_0x0055;
    L_0x0010:
        r1 = r6[r0];
        r2 = r1.getName();
        r2 = org.apache.commons.lang3.ArrayUtils.contains(r9, r2);
        if (r2 != 0) goto L_0x0052;
    L_0x001c:
        r2 = r1.getName();
        r3 = "$";
        r2 = r2.contains(r3);
        if (r2 != 0) goto L_0x0052;
    L_0x0028:
        if (r8 != 0) goto L_0x0034;
    L_0x002a:
        r2 = r1.getModifiers();
        r2 = java.lang.reflect.Modifier.isTransient(r2);
        if (r2 != 0) goto L_0x0052;
    L_0x0034:
        r2 = r1.getModifiers();
        r2 = java.lang.reflect.Modifier.isStatic(r2);
        if (r2 != 0) goto L_0x0052;
    L_0x003e:
        r2 = r1.get(r4);	 Catch:{ IllegalAccessException -> 0x004a }
        r1 = r1.get(r5);	 Catch:{ IllegalAccessException -> 0x004a }
        r7.append(r2, r1);	 Catch:{ IllegalAccessException -> 0x004a }
        goto L_0x0052;
    L_0x004a:
        r4 = new java.lang.InternalError;
        r5 = "Unexpected IllegalAccessException";
        r4.<init>(r5);
        throw r4;
    L_0x0052:
        r0 = r0 + 1;
        goto L_0x0009;
    L_0x0055:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.builder.CompareToBuilder.reflectionAppend(java.lang.Object, java.lang.Object, java.lang.Class, org.apache.commons.lang3.builder.CompareToBuilder, boolean, java.lang.String[]):void");
    }

    public CompareToBuilder appendSuper(int i) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = i;
        return this;
    }

    public CompareToBuilder append(Object obj, Object obj2) {
        return append(obj, obj2, null);
    }

    public CompareToBuilder append(Object obj, Object obj2, Comparator<?> comparator) {
        if (this.comparison != 0 || obj == obj2) {
            return this;
        }
        if (obj == null) {
            this.comparison = -1;
            return this;
        } else if (obj2 == null) {
            this.comparison = 1;
            return this;
        } else {
            if (obj.getClass().isArray()) {
                appendArray(obj, obj2, comparator);
            } else if (comparator == null) {
                this.comparison = ((Comparable) obj).compareTo(obj2);
            } else {
                this.comparison = comparator.compare(obj, obj2);
            }
            return this;
        }
    }

    private void appendArray(Object obj, Object obj2, Comparator<?> comparator) {
        if (obj instanceof long[]) {
            append((long[]) obj, (long[]) obj2);
        } else if (obj instanceof int[]) {
            append((int[]) obj, (int[]) obj2);
        } else if (obj instanceof short[]) {
            append((short[]) obj, (short[]) obj2);
        } else if (obj instanceof char[]) {
            append((char[]) obj, (char[]) obj2);
        } else if (obj instanceof byte[]) {
            append((byte[]) obj, (byte[]) obj2);
        } else if (obj instanceof double[]) {
            append((double[]) obj, (double[]) obj2);
        } else if (obj instanceof float[]) {
            append((float[]) obj, (float[]) obj2);
        } else if (obj instanceof boolean[]) {
            append((boolean[]) obj, (boolean[]) obj2);
        } else {
            append((Object[]) obj, (Object[]) obj2, (Comparator) comparator);
        }
    }

    public CompareToBuilder append(long j, long j2) {
        if (this.comparison != 0) {
            return this;
        }
        j = j < j2 ? -1 : j > j2 ? 1 : null;
        this.comparison = j;
        return this;
    }

    public CompareToBuilder append(int i, int i2) {
        if (this.comparison != 0) {
            return this;
        }
        i = i < i2 ? -1 : i > i2 ? 1 : 0;
        this.comparison = i;
        return this;
    }

    public CompareToBuilder append(short s, short s2) {
        if (this.comparison != 0) {
            return this;
        }
        s = s < s2 ? (short) -1 : s > s2 ? (short) 1 : (short) 0;
        this.comparison = s;
        return this;
    }

    public CompareToBuilder append(char c, char c2) {
        if (this.comparison != 0) {
            return this;
        }
        c = c < c2 ? '￿' : c > c2 ? '\u0001' : '\u0000';
        this.comparison = c;
        return this;
    }

    public CompareToBuilder append(byte b, byte b2) {
        if (this.comparison != 0) {
            return this;
        }
        b = b < b2 ? (byte) -1 : b > b2 ? (byte) 1 : (byte) 0;
        this.comparison = b;
        return this;
    }

    public CompareToBuilder append(double d, double d2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = Double.compare(d, d2);
        return this;
    }

    public CompareToBuilder append(float f, float f2) {
        if (this.comparison != 0) {
            return this;
        }
        this.comparison = Float.compare(f, f2);
        return this;
    }

    public CompareToBuilder append(boolean z, boolean z2) {
        if (this.comparison != 0 || z == z2) {
            return this;
        }
        if (z) {
            this.comparison = true;
        } else {
            this.comparison = true;
        }
        return this;
    }

    public CompareToBuilder append(Object[] objArr, Object[] objArr2) {
        return append(objArr, objArr2, null);
    }

    public CompareToBuilder append(Object[] objArr, Object[] objArr2, Comparator<?> comparator) {
        if (this.comparison != 0 || objArr == objArr2) {
            return this;
        }
        int i = -1;
        if (objArr == null) {
            this.comparison = -1;
            return this;
        } else if (objArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (objArr.length != objArr2.length) {
            if (objArr.length >= objArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < objArr.length && this.comparison == 0; i++) {
                append(objArr[i], objArr2[i], (Comparator) comparator);
            }
            return this;
        }
    }

    public CompareToBuilder append(long[] jArr, long[] jArr2) {
        if (this.comparison != 0 || jArr == jArr2) {
            return this;
        }
        int i = -1;
        if (jArr == null) {
            this.comparison = -1;
            return this;
        } else if (jArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (jArr.length != jArr2.length) {
            if (jArr.length >= jArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < jArr.length && this.comparison == 0; i++) {
                append(jArr[i], jArr2[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(int[] iArr, int[] iArr2) {
        if (this.comparison != 0 || iArr == iArr2) {
            return this;
        }
        int i = -1;
        if (iArr == null) {
            this.comparison = -1;
            return this;
        } else if (iArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (iArr.length != iArr2.length) {
            if (iArr.length >= iArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < iArr.length && this.comparison == 0; i++) {
                append(iArr[i], iArr2[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(short[] sArr, short[] sArr2) {
        if (this.comparison != 0 || sArr == sArr2) {
            return this;
        }
        int i = -1;
        if (sArr == null) {
            this.comparison = -1;
            return this;
        } else if (sArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (sArr.length != sArr2.length) {
            if (sArr.length >= sArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < sArr.length && this.comparison == 0; i++) {
                append(sArr[i], sArr2[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(char[] cArr, char[] cArr2) {
        if (this.comparison != 0 || cArr == cArr2) {
            return this;
        }
        int i = -1;
        if (cArr == null) {
            this.comparison = -1;
            return this;
        } else if (cArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (cArr.length != cArr2.length) {
            if (cArr.length >= cArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < cArr.length && this.comparison == 0; i++) {
                append(cArr[i], cArr2[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(byte[] bArr, byte[] bArr2) {
        if (this.comparison != 0 || bArr == bArr2) {
            return this;
        }
        int i = -1;
        if (bArr == null) {
            this.comparison = -1;
            return this;
        } else if (bArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (bArr.length != bArr2.length) {
            if (bArr.length >= bArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < bArr.length && this.comparison == 0; i++) {
                append(bArr[i], bArr2[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(double[] dArr, double[] dArr2) {
        if (this.comparison != 0 || dArr == dArr2) {
            return this;
        }
        int i = -1;
        if (dArr == null) {
            this.comparison = -1;
            return this;
        } else if (dArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (dArr.length != dArr2.length) {
            if (dArr.length >= dArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < dArr.length && this.comparison == 0; i++) {
                append(dArr[i], dArr2[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(float[] fArr, float[] fArr2) {
        if (this.comparison != 0 || fArr == fArr2) {
            return this;
        }
        int i = -1;
        if (fArr == null) {
            this.comparison = -1;
            return this;
        } else if (fArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (fArr.length != fArr2.length) {
            if (fArr.length >= fArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < fArr.length && this.comparison == 0; i++) {
                append(fArr[i], fArr2[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(boolean[] zArr, boolean[] zArr2) {
        if (this.comparison != 0 || zArr == zArr2) {
            return this;
        }
        int i = -1;
        if (zArr == null) {
            this.comparison = -1;
            return this;
        } else if (zArr2 == null) {
            this.comparison = 1;
            return this;
        } else if (zArr.length != zArr2.length) {
            if (zArr.length >= zArr2.length) {
                i = 1;
            }
            this.comparison = i;
            return this;
        } else {
            for (i = 0; i < zArr.length && this.comparison == 0; i++) {
                append(zArr[i], zArr2[i]);
            }
            return this;
        }
    }

    public int toComparison() {
        return this.comparison;
    }

    public Integer build() {
        return Integer.valueOf(toComparison());
    }
}
