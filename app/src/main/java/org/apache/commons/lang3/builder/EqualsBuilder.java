package org.apache.commons.lang3.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class EqualsBuilder implements Builder<Boolean> {
    private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal();
    private boolean isEquals = true;

    static Set<Pair<IDKey, IDKey>> getRegistry() {
        return (Set) REGISTRY.get();
    }

    static Pair<IDKey, IDKey> getRegisterPair(Object obj, Object obj2) {
        return Pair.of(new IDKey(obj), new IDKey(obj2));
    }

    static boolean isRegistered(Object obj, Object obj2) {
        Set registry = getRegistry();
        obj = getRegisterPair(obj, obj2);
        return (registry == null || (registry.contains(obj) == null && registry.contains(Pair.of(obj.getLeft(), obj.getRight())) == null)) ? null : true;
    }

    private static void register(Object obj, Object obj2) {
        Set registry = getRegistry();
        if (registry == null) {
            registry = new HashSet();
            REGISTRY.set(registry);
        }
        registry.add(getRegisterPair(obj, obj2));
    }

    private static void unregister(Object obj, Object obj2) {
        Set registry = getRegistry();
        if (registry != null) {
            registry.remove(getRegisterPair(obj, obj2));
            if (registry.isEmpty() != null) {
                REGISTRY.remove();
            }
        }
    }

    public static boolean reflectionEquals(Object obj, Object obj2, Collection<String> collection) {
        return reflectionEquals(obj, obj2, ReflectionToStringBuilder.toNoNullStringArray((Collection) collection));
    }

    public static boolean reflectionEquals(Object obj, Object obj2, String... strArr) {
        return reflectionEquals(obj, obj2, false, null, strArr);
    }

    public static boolean reflectionEquals(Object obj, Object obj2, boolean z) {
        return reflectionEquals(obj, obj2, z, null, new String[0]);
    }

    public static boolean reflectionEquals(java.lang.Object r11, java.lang.Object r12, boolean r13, java.lang.Class<?> r14, java.lang.String... r15) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r11 != r12) goto L_0x0004;
    L_0x0002:
        r11 = 1;
        return r11;
    L_0x0004:
        r0 = 0;
        if (r11 == 0) goto L_0x0063;
    L_0x0007:
        if (r12 != 0) goto L_0x000b;
    L_0x0009:
        goto L_0x0063;
    L_0x000b:
        r1 = r11.getClass();
        r2 = r12.getClass();
        r3 = r1.isInstance(r12);
        if (r3 == 0) goto L_0x0020;
    L_0x0019:
        r3 = r2.isInstance(r11);
        if (r3 != 0) goto L_0x002e;
    L_0x001f:
        goto L_0x002d;
    L_0x0020:
        r3 = r2.isInstance(r11);
        if (r3 == 0) goto L_0x0062;
    L_0x0026:
        r3 = r1.isInstance(r12);
        if (r3 != 0) goto L_0x002d;
    L_0x002c:
        goto L_0x002e;
    L_0x002d:
        r1 = r2;
    L_0x002e:
        r10 = new org.apache.commons.lang3.builder.EqualsBuilder;
        r10.<init>();
        r2 = r1.isArray();	 Catch:{ IllegalArgumentException -> 0x0061 }
        if (r2 == 0) goto L_0x003d;	 Catch:{ IllegalArgumentException -> 0x0061 }
    L_0x0039:
        r10.append(r11, r12);	 Catch:{ IllegalArgumentException -> 0x0061 }
        goto L_0x005c;	 Catch:{ IllegalArgumentException -> 0x0061 }
    L_0x003d:
        r4 = r11;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r5 = r12;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r6 = r1;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r7 = r10;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r8 = r13;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r9 = r15;	 Catch:{ IllegalArgumentException -> 0x0061 }
        reflectionAppend(r4, r5, r6, r7, r8, r9);	 Catch:{ IllegalArgumentException -> 0x0061 }
    L_0x0046:
        r2 = r1.getSuperclass();	 Catch:{ IllegalArgumentException -> 0x0061 }
        if (r2 == 0) goto L_0x005c;	 Catch:{ IllegalArgumentException -> 0x0061 }
    L_0x004c:
        if (r1 == r14) goto L_0x005c;	 Catch:{ IllegalArgumentException -> 0x0061 }
    L_0x004e:
        r1 = r1.getSuperclass();	 Catch:{ IllegalArgumentException -> 0x0061 }
        r2 = r11;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r3 = r12;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r4 = r1;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r5 = r10;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r6 = r13;	 Catch:{ IllegalArgumentException -> 0x0061 }
        r7 = r15;	 Catch:{ IllegalArgumentException -> 0x0061 }
        reflectionAppend(r2, r3, r4, r5, r6, r7);	 Catch:{ IllegalArgumentException -> 0x0061 }
        goto L_0x0046;
    L_0x005c:
        r11 = r10.isEquals();
        return r11;
    L_0x0061:
        return r0;
    L_0x0062:
        return r0;
    L_0x0063:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals(java.lang.Object, java.lang.Object, boolean, java.lang.Class, java.lang.String[]):boolean");
    }

    private static void reflectionAppend(java.lang.Object r4, java.lang.Object r5, java.lang.Class<?> r6, org.apache.commons.lang3.builder.EqualsBuilder r7, boolean r8, java.lang.String[] r9) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = isRegistered(r4, r5);
        if (r0 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        register(r4, r5);	 Catch:{ all -> 0x006b }
        r6 = r6.getDeclaredFields();	 Catch:{ all -> 0x006b }
        r0 = 1;	 Catch:{ all -> 0x006b }
        java.lang.reflect.AccessibleObject.setAccessible(r6, r0);	 Catch:{ all -> 0x006b }
        r0 = 0;	 Catch:{ all -> 0x006b }
    L_0x0013:
        r1 = r6.length;	 Catch:{ all -> 0x006b }
        if (r0 >= r1) goto L_0x0067;	 Catch:{ all -> 0x006b }
    L_0x0016:
        r1 = r7.isEquals;	 Catch:{ all -> 0x006b }
        if (r1 == 0) goto L_0x0067;	 Catch:{ all -> 0x006b }
    L_0x001a:
        r1 = r6[r0];	 Catch:{ all -> 0x006b }
        r2 = r1.getName();	 Catch:{ all -> 0x006b }
        r2 = org.apache.commons.lang3.ArrayUtils.contains(r9, r2);	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0064;	 Catch:{ all -> 0x006b }
    L_0x0026:
        r2 = r1.getName();	 Catch:{ all -> 0x006b }
        r3 = "$";	 Catch:{ all -> 0x006b }
        r2 = r2.contains(r3);	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0064;	 Catch:{ all -> 0x006b }
    L_0x0032:
        if (r8 != 0) goto L_0x003e;	 Catch:{ all -> 0x006b }
    L_0x0034:
        r2 = r1.getModifiers();	 Catch:{ all -> 0x006b }
        r2 = java.lang.reflect.Modifier.isTransient(r2);	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0064;	 Catch:{ all -> 0x006b }
    L_0x003e:
        r2 = r1.getModifiers();	 Catch:{ all -> 0x006b }
        r2 = java.lang.reflect.Modifier.isStatic(r2);	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0064;	 Catch:{ all -> 0x006b }
    L_0x0048:
        r2 = org.apache.commons.lang3.builder.EqualsExclude.class;	 Catch:{ all -> 0x006b }
        r2 = r1.isAnnotationPresent(r2);	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0064;
    L_0x0050:
        r2 = r1.get(r4);	 Catch:{ IllegalAccessException -> 0x005c }
        r1 = r1.get(r5);	 Catch:{ IllegalAccessException -> 0x005c }
        r7.append(r2, r1);	 Catch:{ IllegalAccessException -> 0x005c }
        goto L_0x0064;
    L_0x005c:
        r6 = new java.lang.InternalError;	 Catch:{ all -> 0x006b }
        r7 = "Unexpected IllegalAccessException";	 Catch:{ all -> 0x006b }
        r6.<init>(r7);	 Catch:{ all -> 0x006b }
        throw r6;	 Catch:{ all -> 0x006b }
    L_0x0064:
        r0 = r0 + 1;
        goto L_0x0013;
    L_0x0067:
        unregister(r4, r5);
        return;
    L_0x006b:
        r6 = move-exception;
        unregister(r4, r5);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.builder.EqualsBuilder.reflectionAppend(java.lang.Object, java.lang.Object, java.lang.Class, org.apache.commons.lang3.builder.EqualsBuilder, boolean, java.lang.String[]):void");
    }

    public EqualsBuilder appendSuper(boolean z) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = z;
        return this;
    }

    public EqualsBuilder append(Object obj, Object obj2) {
        if (!this.isEquals || obj == obj2) {
            return this;
        }
        if (obj != null) {
            if (obj2 != null) {
                if (obj.getClass().isArray()) {
                    appendArray(obj, obj2);
                } else {
                    this.isEquals = obj.equals(obj2);
                }
                return this;
            }
        }
        setEquals(null);
        return this;
    }

    private void appendArray(Object obj, Object obj2) {
        if (obj.getClass() != obj2.getClass()) {
            setEquals(null);
        } else if (obj instanceof long[]) {
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
            append((Object[]) obj, (Object[]) obj2);
        }
    }

    public EqualsBuilder append(long j, long j2) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = j == j2 ? 1 : null;
        return this;
    }

    public EqualsBuilder append(int i, int i2) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = i == i2 ? 1 : 0;
        return this;
    }

    public EqualsBuilder append(short s, short s2) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = s == s2 ? (short) 1 : (short) 0;
        return this;
    }

    public EqualsBuilder append(char c, char c2) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = c == c2 ? '\u0001' : '\u0000';
        return this;
    }

    public EqualsBuilder append(byte b, byte b2) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = b == b2 ? (byte) 1 : (byte) 0;
        return this;
    }

    public EqualsBuilder append(double d, double d2) {
        if (this.isEquals) {
            return append(Double.doubleToLongBits(d), Double.doubleToLongBits(d2));
        }
        return this;
    }

    public EqualsBuilder append(float f, float f2) {
        if (this.isEquals) {
            return append(Float.floatToIntBits(f), Float.floatToIntBits(f2));
        }
        return this;
    }

    public EqualsBuilder append(boolean z, boolean z2) {
        if (!this.isEquals) {
            return this;
        }
        this.isEquals = z == z2;
        return this;
    }

    public EqualsBuilder append(Object[] objArr, Object[] objArr2) {
        if (!this.isEquals || objArr == objArr2) {
            return this;
        }
        int i = 0;
        if (objArr != null) {
            if (objArr2 != null) {
                if (objArr.length != objArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < objArr.length && this.isEquals) {
                    append(objArr[i], objArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(long[] jArr, long[] jArr2) {
        if (!this.isEquals || jArr == jArr2) {
            return this;
        }
        int i = 0;
        if (jArr != null) {
            if (jArr2 != null) {
                if (jArr.length != jArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < jArr.length && this.isEquals) {
                    append(jArr[i], jArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(int[] iArr, int[] iArr2) {
        if (!this.isEquals || iArr == iArr2) {
            return this;
        }
        int i = 0;
        if (iArr != null) {
            if (iArr2 != null) {
                if (iArr.length != iArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < iArr.length && this.isEquals) {
                    append(iArr[i], iArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(short[] sArr, short[] sArr2) {
        if (!this.isEquals || sArr == sArr2) {
            return this;
        }
        int i = 0;
        if (sArr != null) {
            if (sArr2 != null) {
                if (sArr.length != sArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < sArr.length && this.isEquals) {
                    append(sArr[i], sArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(char[] cArr, char[] cArr2) {
        if (!this.isEquals || cArr == cArr2) {
            return this;
        }
        int i = 0;
        if (cArr != null) {
            if (cArr2 != null) {
                if (cArr.length != cArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < cArr.length && this.isEquals) {
                    append(cArr[i], cArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(byte[] bArr, byte[] bArr2) {
        if (!this.isEquals || bArr == bArr2) {
            return this;
        }
        int i = 0;
        if (bArr != null) {
            if (bArr2 != null) {
                if (bArr.length != bArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < bArr.length && this.isEquals) {
                    append(bArr[i], bArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(double[] dArr, double[] dArr2) {
        if (!this.isEquals || dArr == dArr2) {
            return this;
        }
        int i = 0;
        if (dArr != null) {
            if (dArr2 != null) {
                if (dArr.length != dArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < dArr.length && this.isEquals) {
                    append(dArr[i], dArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(float[] fArr, float[] fArr2) {
        if (!this.isEquals || fArr == fArr2) {
            return this;
        }
        int i = 0;
        if (fArr != null) {
            if (fArr2 != null) {
                if (fArr.length != fArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < fArr.length && this.isEquals) {
                    append(fArr[i], fArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public EqualsBuilder append(boolean[] zArr, boolean[] zArr2) {
        if (!this.isEquals || zArr == zArr2) {
            return this;
        }
        int i = 0;
        if (zArr != null) {
            if (zArr2 != null) {
                if (zArr.length != zArr2.length) {
                    setEquals(false);
                    return this;
                }
                while (i < zArr.length && this.isEquals) {
                    append(zArr[i], zArr2[i]);
                    i++;
                }
                return this;
            }
        }
        setEquals(false);
        return this;
    }

    public boolean isEquals() {
        return this.isEquals;
    }

    public Boolean build() {
        return Boolean.valueOf(isEquals());
    }

    protected void setEquals(boolean z) {
        this.isEquals = z;
    }

    public void reset() {
        this.isEquals = true;
    }
}
