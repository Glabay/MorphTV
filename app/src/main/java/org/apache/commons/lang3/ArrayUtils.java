package org.apache.commons.lang3;

import com.tonyodev.fetch2.util.FetchDefaults;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public class ArrayUtils {
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int INDEX_NOT_FOUND = -1;

    /* renamed from: org.apache.commons.lang3.ArrayUtils$1 */
    static class C14401 implements Comparator<T> {
        C14401() {
        }

        public int compare(T t, T t2) {
            return t.compareTo(t2);
        }
    }

    public static <T> T[] toArray(T... tArr) {
        return tArr;
    }

    public static String toString(Object obj) {
        return toString(obj, FetchDefaults.EMPTY_JSON_OBJECT_STRING);
    }

    public static String toString(Object obj, String str) {
        return obj == null ? str : new ToStringBuilder(obj, ToStringStyle.SIMPLE_STYLE).append(obj).toString();
    }

    public static int hashCode(Object obj) {
        return new HashCodeBuilder().append(obj).toHashCode();
    }

    @Deprecated
    public static boolean isEquals(Object obj, Object obj2) {
        return new EqualsBuilder().append(obj, obj2).isEquals();
    }

    public static Map<Object, Object> toMap(Object[] objArr) {
        if (objArr == null) {
            return null;
        }
        Map<Object, Object> hashMap = new HashMap((int) (((double) objArr.length) * 1.5d));
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                hashMap.put(entry.getKey(), entry.getValue());
            } else if (obj instanceof Object[]) {
                Object[] objArr2 = (Object[]) obj;
                if (objArr2.length < 2) {
                    r0 = new StringBuilder();
                    r0.append("Array element ");
                    r0.append(i);
                    r0.append(", '");
                    r0.append(obj);
                    r0.append("', has a length less than 2");
                    throw new IllegalArgumentException(r0.toString());
                }
                hashMap.put(objArr2[0], objArr2[1]);
            } else {
                r0 = new StringBuilder();
                r0.append("Array element ");
                r0.append(i);
                r0.append(", '");
                r0.append(obj);
                r0.append("', is neither of type Map.Entry nor an Array");
                throw new IllegalArgumentException(r0.toString());
            }
        }
        return hashMap;
    }

    public static <T> T[] clone(T[] tArr) {
        return tArr == null ? null : (Object[]) tArr.clone();
    }

    public static long[] clone(long[] jArr) {
        return jArr == null ? null : (long[]) jArr.clone();
    }

    public static int[] clone(int[] iArr) {
        return iArr == null ? null : (int[]) iArr.clone();
    }

    public static short[] clone(short[] sArr) {
        return sArr == null ? null : (short[]) sArr.clone();
    }

    public static char[] clone(char[] cArr) {
        return cArr == null ? null : (char[]) cArr.clone();
    }

    public static byte[] clone(byte[] bArr) {
        return bArr == null ? null : (byte[]) bArr.clone();
    }

    public static double[] clone(double[] dArr) {
        return dArr == null ? null : (double[]) dArr.clone();
    }

    public static float[] clone(float[] fArr) {
        return fArr == null ? null : (float[]) fArr.clone();
    }

    public static boolean[] clone(boolean[] zArr) {
        return zArr == null ? null : (boolean[]) zArr.clone();
    }

    public static <T> T[] nullToEmpty(T[] tArr, Class<T[]> cls) {
        if (cls != null) {
            return tArr == null ? (Object[]) cls.cast(Array.newInstance(cls.getComponentType(), 0)) : tArr;
        } else {
            throw new IllegalArgumentException("The type must not be null");
        }
    }

    public static Object[] nullToEmpty(Object[] objArr) {
        return isEmpty(objArr) ? EMPTY_OBJECT_ARRAY : objArr;
    }

    public static Class<?>[] nullToEmpty(Class<?>[] clsArr) {
        return isEmpty((Object[]) clsArr) ? EMPTY_CLASS_ARRAY : clsArr;
    }

    public static String[] nullToEmpty(String[] strArr) {
        return isEmpty((Object[]) strArr) ? EMPTY_STRING_ARRAY : strArr;
    }

    public static long[] nullToEmpty(long[] jArr) {
        return isEmpty(jArr) ? EMPTY_LONG_ARRAY : jArr;
    }

    public static int[] nullToEmpty(int[] iArr) {
        return isEmpty(iArr) ? EMPTY_INT_ARRAY : iArr;
    }

    public static short[] nullToEmpty(short[] sArr) {
        return isEmpty(sArr) ? EMPTY_SHORT_ARRAY : sArr;
    }

    public static char[] nullToEmpty(char[] cArr) {
        return isEmpty(cArr) ? EMPTY_CHAR_ARRAY : cArr;
    }

    public static byte[] nullToEmpty(byte[] bArr) {
        return isEmpty(bArr) ? EMPTY_BYTE_ARRAY : bArr;
    }

    public static double[] nullToEmpty(double[] dArr) {
        return isEmpty(dArr) ? EMPTY_DOUBLE_ARRAY : dArr;
    }

    public static float[] nullToEmpty(float[] fArr) {
        return isEmpty(fArr) ? EMPTY_FLOAT_ARRAY : fArr;
    }

    public static boolean[] nullToEmpty(boolean[] zArr) {
        return isEmpty(zArr) ? EMPTY_BOOLEAN_ARRAY : zArr;
    }

    public static Long[] nullToEmpty(Long[] lArr) {
        return isEmpty((Object[]) lArr) ? EMPTY_LONG_OBJECT_ARRAY : lArr;
    }

    public static Integer[] nullToEmpty(Integer[] numArr) {
        return isEmpty((Object[]) numArr) ? EMPTY_INTEGER_OBJECT_ARRAY : numArr;
    }

    public static Short[] nullToEmpty(Short[] shArr) {
        return isEmpty((Object[]) shArr) ? EMPTY_SHORT_OBJECT_ARRAY : shArr;
    }

    public static Character[] nullToEmpty(Character[] chArr) {
        return isEmpty((Object[]) chArr) ? EMPTY_CHARACTER_OBJECT_ARRAY : chArr;
    }

    public static Byte[] nullToEmpty(Byte[] bArr) {
        return isEmpty((Object[]) bArr) ? EMPTY_BYTE_OBJECT_ARRAY : bArr;
    }

    public static Double[] nullToEmpty(Double[] dArr) {
        return isEmpty((Object[]) dArr) ? EMPTY_DOUBLE_OBJECT_ARRAY : dArr;
    }

    public static Float[] nullToEmpty(Float[] fArr) {
        return isEmpty((Object[]) fArr) ? EMPTY_FLOAT_OBJECT_ARRAY : fArr;
    }

    public static Boolean[] nullToEmpty(Boolean[] boolArr) {
        return isEmpty((Object[]) boolArr) ? EMPTY_BOOLEAN_OBJECT_ARRAY : boolArr;
    }

    public static <T> T[] subarray(T[] tArr, int i, int i2) {
        if (tArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > tArr.length) {
            i2 = tArr.length;
        }
        i2 -= i;
        Class componentType = tArr.getClass().getComponentType();
        if (i2 <= 0) {
            return (Object[]) Array.newInstance(componentType, 0);
        }
        Object[] objArr = (Object[]) Array.newInstance(componentType, i2);
        System.arraycopy(tArr, i, objArr, 0, i2);
        return objArr;
    }

    public static long[] subarray(long[] jArr, int i, int i2) {
        if (jArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > jArr.length) {
            i2 = jArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_LONG_ARRAY;
        }
        Object obj = new long[i2];
        System.arraycopy(jArr, i, obj, 0, i2);
        return obj;
    }

    public static int[] subarray(int[] iArr, int i, int i2) {
        if (iArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > iArr.length) {
            i2 = iArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_INT_ARRAY;
        }
        Object obj = new int[i2];
        System.arraycopy(iArr, i, obj, 0, i2);
        return obj;
    }

    public static short[] subarray(short[] sArr, int i, int i2) {
        if (sArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > sArr.length) {
            i2 = sArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_SHORT_ARRAY;
        }
        Object obj = new short[i2];
        System.arraycopy(sArr, i, obj, 0, i2);
        return obj;
    }

    public static char[] subarray(char[] cArr, int i, int i2) {
        if (cArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > cArr.length) {
            i2 = cArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_CHAR_ARRAY;
        }
        Object obj = new char[i2];
        System.arraycopy(cArr, i, obj, 0, i2);
        return obj;
    }

    public static byte[] subarray(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > bArr.length) {
            i2 = bArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_BYTE_ARRAY;
        }
        Object obj = new byte[i2];
        System.arraycopy(bArr, i, obj, 0, i2);
        return obj;
    }

    public static double[] subarray(double[] dArr, int i, int i2) {
        if (dArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > dArr.length) {
            i2 = dArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        Object obj = new double[i2];
        System.arraycopy(dArr, i, obj, 0, i2);
        return obj;
    }

    public static float[] subarray(float[] fArr, int i, int i2) {
        if (fArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > fArr.length) {
            i2 = fArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        Object obj = new float[i2];
        System.arraycopy(fArr, i, obj, 0, i2);
        return obj;
    }

    public static boolean[] subarray(boolean[] zArr, int i, int i2) {
        if (zArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > zArr.length) {
            i2 = zArr.length;
        }
        i2 -= i;
        if (i2 <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        Object obj = new boolean[i2];
        System.arraycopy(zArr, i, obj, 0, i2);
        return obj;
    }

    public static boolean isSameLength(Object[] objArr, Object[] objArr2) {
        return getLength(objArr) == getLength(objArr2) ? 1 : null;
    }

    public static boolean isSameLength(long[] jArr, long[] jArr2) {
        return getLength(jArr) == getLength(jArr2) ? 1 : null;
    }

    public static boolean isSameLength(int[] iArr, int[] iArr2) {
        return getLength(iArr) == getLength(iArr2) ? 1 : null;
    }

    public static boolean isSameLength(short[] sArr, short[] sArr2) {
        return getLength(sArr) == getLength(sArr2) ? 1 : null;
    }

    public static boolean isSameLength(char[] cArr, char[] cArr2) {
        return getLength(cArr) == getLength(cArr2) ? 1 : null;
    }

    public static boolean isSameLength(byte[] bArr, byte[] bArr2) {
        return getLength(bArr) == getLength(bArr2) ? 1 : null;
    }

    public static boolean isSameLength(double[] dArr, double[] dArr2) {
        return getLength(dArr) == getLength(dArr2) ? 1 : null;
    }

    public static boolean isSameLength(float[] fArr, float[] fArr2) {
        return getLength(fArr) == getLength(fArr2) ? 1 : null;
    }

    public static boolean isSameLength(boolean[] zArr, boolean[] zArr2) {
        return getLength(zArr) == getLength(zArr2) ? 1 : null;
    }

    public static int getLength(Object obj) {
        return obj == null ? null : Array.getLength(obj);
    }

    public static boolean isSameType(Object obj, Object obj2) {
        if (obj != null) {
            if (obj2 != null) {
                return obj.getClass().getName().equals(obj2.getClass().getName());
            }
        }
        throw new IllegalArgumentException("The Array must not be null");
    }

    public static void reverse(Object[] objArr) {
        if (objArr != null) {
            reverse(objArr, 0, objArr.length);
        }
    }

    public static void reverse(long[] jArr) {
        if (jArr != null) {
            reverse(jArr, 0, jArr.length);
        }
    }

    public static void reverse(int[] iArr) {
        if (iArr != null) {
            reverse(iArr, 0, iArr.length);
        }
    }

    public static void reverse(short[] sArr) {
        if (sArr != null) {
            reverse(sArr, 0, sArr.length);
        }
    }

    public static void reverse(char[] cArr) {
        if (cArr != null) {
            reverse(cArr, 0, cArr.length);
        }
    }

    public static void reverse(byte[] bArr) {
        if (bArr != null) {
            reverse(bArr, 0, bArr.length);
        }
    }

    public static void reverse(double[] dArr) {
        if (dArr != null) {
            reverse(dArr, 0, dArr.length);
        }
    }

    public static void reverse(float[] fArr) {
        if (fArr != null) {
            reverse(fArr, 0, fArr.length);
        }
    }

    public static void reverse(boolean[] zArr) {
        if (zArr != null) {
            reverse(zArr, 0, zArr.length);
        }
    }

    public static void reverse(boolean[] zArr, int i, int i2) {
        if (zArr != null) {
            if (i < 0) {
                i = 0;
            }
            i2 = Math.min(zArr.length, i2) - 1;
            for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(boolean[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r3_2 'i' int) = (r3_0 'i' int), (r3_1 'i' int) binds: {(r3_0 'i' int)=B:2:0x0003, (r3_1 'i' int)=B:3:0x0005} in method: org.apache.commons.lang3.ArrayUtils.reverse(boolean[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

            public static void reverse(byte[] bArr, int i, int i2) {
                if (bArr != null) {
                    if (i < 0) {
                        i = 0;
                    }
                    i2 = Math.min(bArr.length, i2) - 1;
                    for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(byte[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r3_2 'i' int) = (r3_0 'i' int), (r3_1 'i' int) binds: {(r3_1 'i' int)=B:3:0x0005, (r3_0 'i' int)=B:2:0x0003} in method: org.apache.commons.lang3.ArrayUtils.reverse(byte[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                    public static void reverse(char[] cArr, int i, int i2) {
                        if (cArr != null) {
                            if (i < 0) {
                                i = 0;
                            }
                            i2 = Math.min(cArr.length, i2) - 1;
                            for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(char[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r3_2 'i' int) = (r3_0 'i' int), (r3_1 'i' int) binds: {(r3_0 'i' int)=B:2:0x0003, (r3_1 'i' int)=B:3:0x0005} in method: org.apache.commons.lang3.ArrayUtils.reverse(char[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                            public static void reverse(double[] dArr, int i, int i2) {
                                if (dArr != null) {
                                    if (i < 0) {
                                        i = 0;
                                    }
                                    i2 = Math.min(dArr.length, i2) - 1;
                                    for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(double[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r5_2 'i' int) = (r5_0 'i' int), (r5_1 'i' int) binds: {(r5_0 'i' int)=B:2:0x0003, (r5_1 'i' int)=B:3:0x0005} in method: org.apache.commons.lang3.ArrayUtils.reverse(double[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                                    public static void reverse(float[] fArr, int i, int i2) {
                                        if (fArr != null) {
                                            if (i < 0) {
                                                i = 0;
                                            }
                                            i2 = Math.min(fArr.length, i2) - 1;
                                            for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(float[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r3_2 'i' int) = (r3_0 'i' int), (r3_1 'i' int) binds: {(r3_1 'i' int)=B:3:0x0005, (r3_0 'i' int)=B:2:0x0003} in method: org.apache.commons.lang3.ArrayUtils.reverse(float[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                                            public static void reverse(int[] iArr, int i, int i2) {
                                                if (iArr != null) {
                                                    if (i < 0) {
                                                        i = 0;
                                                    }
                                                    i2 = Math.min(iArr.length, i2) - 1;
                                                    for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(int[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r3_2 'i' int) = (r3_0 'i' int), (r3_1 'i' int) binds: {(r3_1 'i' int)=B:3:0x0005, (r3_0 'i' int)=B:2:0x0003} in method: org.apache.commons.lang3.ArrayUtils.reverse(int[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                                                    public static void reverse(long[] jArr, int i, int i2) {
                                                        if (jArr != null) {
                                                            if (i < 0) {
                                                                i = 0;
                                                            }
                                                            i2 = Math.min(jArr.length, i2) - 1;
                                                            for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(long[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r5_2 'i' int) = (r5_0 'i' int), (r5_1 'i' int) binds: {(r5_1 'i' int)=B:3:0x0005, (r5_0 'i' int)=B:2:0x0003} in method: org.apache.commons.lang3.ArrayUtils.reverse(long[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                                                            public static void reverse(Object[] objArr, int i, int i2) {
                                                                if (objArr != null) {
                                                                    if (i < 0) {
                                                                        i = 0;
                                                                    }
                                                                    i2 = Math.min(objArr.length, i2) - 1;
                                                                    for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(java.lang.Object[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r3_2 'i' int) = (r3_0 'i' int), (r3_1 'i' int) binds: {(r3_0 'i' int)=B:2:0x0003, (r3_1 'i' int)=B:3:0x0005} in method: org.apache.commons.lang3.ArrayUtils.reverse(java.lang.Object[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                                                                    public static void reverse(short[] sArr, int i, int i2) {
                                                                        if (sArr != null) {
                                                                            if (i < 0) {
                                                                                i = 0;
                                                                            }
                                                                            i2 = Math.min(sArr.length, i2) - 1;
                                                                            for (i = 
/*
Method generation error in method: org.apache.commons.lang3.ArrayUtils.reverse(short[], int, int):void, dex: classes2.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r3_2 'i' int) = (r3_0 'i' int), (r3_1 'i' int) binds: {(r3_1 'i' int)=B:3:0x0005, (r3_0 'i' int)=B:2:0x0003} in method: org.apache.commons.lang3.ArrayUtils.reverse(short[], int, int):void, dex: classes2.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 23 more

*/

                                                                            public static void swap(Object[] objArr, int i, int i2) {
                                                                                if (objArr != null) {
                                                                                    if (objArr.length != 0) {
                                                                                        swap(objArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(long[] jArr, int i, int i2) {
                                                                                if (jArr != null) {
                                                                                    if (jArr.length != 0) {
                                                                                        swap(jArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(int[] iArr, int i, int i2) {
                                                                                if (iArr != null) {
                                                                                    if (iArr.length != 0) {
                                                                                        swap(iArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(short[] sArr, int i, int i2) {
                                                                                if (sArr != null) {
                                                                                    if (sArr.length != 0) {
                                                                                        swap(sArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(char[] cArr, int i, int i2) {
                                                                                if (cArr != null) {
                                                                                    if (cArr.length != 0) {
                                                                                        swap(cArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(byte[] bArr, int i, int i2) {
                                                                                if (bArr != null) {
                                                                                    if (bArr.length != 0) {
                                                                                        swap(bArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(double[] dArr, int i, int i2) {
                                                                                if (dArr != null) {
                                                                                    if (dArr.length != 0) {
                                                                                        swap(dArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(float[] fArr, int i, int i2) {
                                                                                if (fArr != null) {
                                                                                    if (fArr.length != 0) {
                                                                                        swap(fArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(boolean[] zArr, int i, int i2) {
                                                                                if (zArr != null) {
                                                                                    if (zArr.length != 0) {
                                                                                        swap(zArr, i, i2, 1);
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(boolean[] zArr, int i, int i2, int i3) {
                                                                                if (!(zArr == null || zArr.length == 0 || i >= zArr.length)) {
                                                                                    if (i2 < zArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, zArr.length - i), zArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            boolean z = zArr[i];
                                                                                            zArr[i] = zArr[i2];
                                                                                            zArr[i2] = z;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(byte[] bArr, int i, int i2, int i3) {
                                                                                if (!(bArr == null || bArr.length == 0 || i >= bArr.length)) {
                                                                                    if (i2 < bArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, bArr.length - i), bArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            byte b = bArr[i];
                                                                                            bArr[i] = bArr[i2];
                                                                                            bArr[i2] = b;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(char[] cArr, int i, int i2, int i3) {
                                                                                if (!(cArr == null || cArr.length == 0 || i >= cArr.length)) {
                                                                                    if (i2 < cArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, cArr.length - i), cArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            char c = cArr[i];
                                                                                            cArr[i] = cArr[i2];
                                                                                            cArr[i2] = c;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(double[] dArr, int i, int i2, int i3) {
                                                                                if (!(dArr == null || dArr.length == 0 || i >= dArr.length)) {
                                                                                    if (i2 < dArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, dArr.length - i), dArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            double d = dArr[i];
                                                                                            dArr[i] = dArr[i2];
                                                                                            dArr[i2] = d;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(float[] fArr, int i, int i2, int i3) {
                                                                                if (!(fArr == null || fArr.length == 0 || i >= fArr.length)) {
                                                                                    if (i2 < fArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, fArr.length - i), fArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            float f = fArr[i];
                                                                                            fArr[i] = fArr[i2];
                                                                                            fArr[i2] = f;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(int[] iArr, int i, int i2, int i3) {
                                                                                if (!(iArr == null || iArr.length == 0 || i >= iArr.length)) {
                                                                                    if (i2 < iArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, iArr.length - i), iArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            int i5 = iArr[i];
                                                                                            iArr[i] = iArr[i2];
                                                                                            iArr[i2] = i5;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(long[] jArr, int i, int i2, int i3) {
                                                                                if (!(jArr == null || jArr.length == 0 || i >= jArr.length)) {
                                                                                    if (i2 < jArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, jArr.length - i), jArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            long j = jArr[i];
                                                                                            jArr[i] = jArr[i2];
                                                                                            jArr[i2] = j;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(Object[] objArr, int i, int i2, int i3) {
                                                                                if (!(objArr == null || objArr.length == 0 || i >= objArr.length)) {
                                                                                    if (i2 < objArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        i3 = Math.min(Math.min(i3, objArr.length - i), objArr.length - i2);
                                                                                        while (i4 < i3) {
                                                                                            Object obj = objArr[i];
                                                                                            objArr[i] = objArr[i2];
                                                                                            objArr[i2] = obj;
                                                                                            i4++;
                                                                                            i++;
                                                                                            i2++;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void swap(short[] sArr, int i, int i2, int i3) {
                                                                                if (!(sArr == null || sArr.length == 0 || i >= sArr.length)) {
                                                                                    if (i2 < sArr.length) {
                                                                                        int i4 = 0;
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 < 0) {
                                                                                            i2 = 0;
                                                                                        }
                                                                                        if (i != i2) {
                                                                                            i3 = Math.min(Math.min(i3, sArr.length - i), sArr.length - i2);
                                                                                            while (i4 < i3) {
                                                                                                short s = sArr[i];
                                                                                                sArr[i] = sArr[i2];
                                                                                                sArr[i2] = s;
                                                                                                i4++;
                                                                                                i++;
                                                                                                i2++;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(Object[] objArr, int i) {
                                                                                if (objArr != null) {
                                                                                    shift(objArr, 0, objArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(long[] jArr, int i) {
                                                                                if (jArr != null) {
                                                                                    shift(jArr, 0, jArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(int[] iArr, int i) {
                                                                                if (iArr != null) {
                                                                                    shift(iArr, 0, iArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(short[] sArr, int i) {
                                                                                if (sArr != null) {
                                                                                    shift(sArr, 0, sArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(char[] cArr, int i) {
                                                                                if (cArr != null) {
                                                                                    shift(cArr, 0, cArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(byte[] bArr, int i) {
                                                                                if (bArr != null) {
                                                                                    shift(bArr, 0, bArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(double[] dArr, int i) {
                                                                                if (dArr != null) {
                                                                                    shift(dArr, 0, dArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(float[] fArr, int i) {
                                                                                if (fArr != null) {
                                                                                    shift(fArr, 0, fArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(boolean[] zArr, int i) {
                                                                                if (zArr != null) {
                                                                                    shift(zArr, 0, zArr.length, i);
                                                                                }
                                                                            }

                                                                            public static void shift(boolean[] zArr, int i, int i2, int i3) {
                                                                                if (zArr != null && i < zArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= zArr.length) {
                                                                                            i2 = zArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(zArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(zArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(zArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(byte[] bArr, int i, int i2, int i3) {
                                                                                if (bArr != null && i < bArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= bArr.length) {
                                                                                            i2 = bArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(bArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(bArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(bArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(char[] cArr, int i, int i2, int i3) {
                                                                                if (cArr != null && i < cArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= cArr.length) {
                                                                                            i2 = cArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(cArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(cArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(cArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(double[] dArr, int i, int i2, int i3) {
                                                                                if (dArr != null && i < dArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= dArr.length) {
                                                                                            i2 = dArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(dArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(dArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(dArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(float[] fArr, int i, int i2, int i3) {
                                                                                if (fArr != null && i < fArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= fArr.length) {
                                                                                            i2 = fArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(fArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(fArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(fArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(int[] iArr, int i, int i2, int i3) {
                                                                                if (iArr != null && i < iArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= iArr.length) {
                                                                                            i2 = iArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(iArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(iArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(iArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(long[] jArr, int i, int i2, int i3) {
                                                                                if (jArr != null && i < jArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= jArr.length) {
                                                                                            i2 = jArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(jArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(jArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(jArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(Object[] objArr, int i, int i2, int i3) {
                                                                                if (objArr != null && i < objArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= objArr.length) {
                                                                                            i2 = objArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(objArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(objArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(objArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static void shift(short[] sArr, int i, int i2, int i3) {
                                                                                if (sArr != null && i < sArr.length - 1) {
                                                                                    if (i2 > 0) {
                                                                                        if (i < 0) {
                                                                                            i = 0;
                                                                                        }
                                                                                        if (i2 >= sArr.length) {
                                                                                            i2 = sArr.length;
                                                                                        }
                                                                                        i2 -= i;
                                                                                        if (i2 > 1) {
                                                                                            i3 %= i2;
                                                                                            if (i3 < 0) {
                                                                                                i3 += i2;
                                                                                            }
                                                                                            while (i2 > 1 && i3 > 0) {
                                                                                                int i4 = i2 - i3;
                                                                                                if (i3 <= i4) {
                                                                                                    if (i3 >= i4) {
                                                                                                        swap(sArr, i, i4 + i, i3);
                                                                                                        break;
                                                                                                    }
                                                                                                    swap(sArr, i, i + i4, i3);
                                                                                                    i += i3;
                                                                                                    i2 = i4;
                                                                                                } else {
                                                                                                    swap(sArr, i, (i2 + i) - i4, i4);
                                                                                                    int i5 = i3;
                                                                                                    i3 -= i4;
                                                                                                    i2 = i5;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            public static int indexOf(Object[] objArr, Object obj) {
                                                                                return indexOf(objArr, obj, 0);
                                                                            }

                                                                            public static int indexOf(Object[] objArr, Object obj, int i) {
                                                                                if (objArr == null) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                if (obj == null) {
                                                                                    while (i < objArr.length) {
                                                                                        if (objArr[i] == null) {
                                                                                            return i;
                                                                                        }
                                                                                        i++;
                                                                                    }
                                                                                } else {
                                                                                    while (i < objArr.length) {
                                                                                        if (obj.equals(objArr[i])) {
                                                                                            return i;
                                                                                        }
                                                                                        i++;
                                                                                    }
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(Object[] objArr, Object obj) {
                                                                                return lastIndexOf(objArr, obj, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(Object[] objArr, Object obj, int i) {
                                                                                if (objArr == null || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= objArr.length) {
                                                                                    i = objArr.length - 1;
                                                                                }
                                                                                if (obj == null) {
                                                                                    while (i >= 0) {
                                                                                        if (objArr[i] == null) {
                                                                                            return i;
                                                                                        }
                                                                                        i--;
                                                                                    }
                                                                                } else if (objArr.getClass().getComponentType().isInstance(obj)) {
                                                                                    while (i >= 0) {
                                                                                        if (obj.equals(objArr[i])) {
                                                                                            return i;
                                                                                        }
                                                                                        i--;
                                                                                    }
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(Object[] objArr, Object obj) {
                                                                                return indexOf(objArr, obj) != -1 ? 1 : null;
                                                                            }

                                                                            public static int indexOf(long[] jArr, long j) {
                                                                                return indexOf(jArr, j, 0);
                                                                            }

                                                                            public static int indexOf(long[] jArr, long j, int i) {
                                                                                if (jArr == null) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                while (i < jArr.length) {
                                                                                    if (j == jArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(long[] jArr, long j) {
                                                                                return lastIndexOf(jArr, j, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(long[] jArr, long j, int i) {
                                                                                if (jArr == null || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= jArr.length) {
                                                                                    i = jArr.length - 1;
                                                                                }
                                                                                while (i >= 0) {
                                                                                    if (j == jArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(long[] jArr, long j) {
                                                                                return indexOf(jArr, j) != -1 ? 1 : null;
                                                                            }

                                                                            public static int indexOf(int[] iArr, int i) {
                                                                                return indexOf(iArr, i, 0);
                                                                            }

                                                                            public static int indexOf(int[] iArr, int i, int i2) {
                                                                                if (iArr == null) {
                                                                                    return -1;
                                                                                }
                                                                                if (i2 < 0) {
                                                                                    i2 = 0;
                                                                                }
                                                                                while (i2 < iArr.length) {
                                                                                    if (i == iArr[i2]) {
                                                                                        return i2;
                                                                                    }
                                                                                    i2++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(int[] iArr, int i) {
                                                                                return lastIndexOf(iArr, i, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(int[] iArr, int i, int i2) {
                                                                                if (iArr == null || i2 < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i2 >= iArr.length) {
                                                                                    i2 = iArr.length - 1;
                                                                                }
                                                                                while (i2 >= 0) {
                                                                                    if (i == iArr[i2]) {
                                                                                        return i2;
                                                                                    }
                                                                                    i2--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(int[] iArr, int i) {
                                                                                return indexOf(iArr, i) != -1 ? 1 : null;
                                                                            }

                                                                            public static int indexOf(short[] sArr, short s) {
                                                                                return indexOf(sArr, s, 0);
                                                                            }

                                                                            public static int indexOf(short[] sArr, short s, int i) {
                                                                                if (sArr == null) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                while (i < sArr.length) {
                                                                                    if (s == sArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(short[] sArr, short s) {
                                                                                return lastIndexOf(sArr, s, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(short[] sArr, short s, int i) {
                                                                                if (sArr == null || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= sArr.length) {
                                                                                    i = sArr.length - 1;
                                                                                }
                                                                                while (i >= 0) {
                                                                                    if (s == sArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(short[] sArr, short s) {
                                                                                return indexOf(sArr, s) != (short) -1 ? 1 : null;
                                                                            }

                                                                            public static int indexOf(char[] cArr, char c) {
                                                                                return indexOf(cArr, c, 0);
                                                                            }

                                                                            public static int indexOf(char[] cArr, char c, int i) {
                                                                                if (cArr == null) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                while (i < cArr.length) {
                                                                                    if (c == cArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(char[] cArr, char c) {
                                                                                return lastIndexOf(cArr, c, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(char[] cArr, char c, int i) {
                                                                                if (cArr == null || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= cArr.length) {
                                                                                    i = cArr.length - 1;
                                                                                }
                                                                                while (i >= 0) {
                                                                                    if (c == cArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(char[] cArr, char c) {
                                                                                return indexOf(cArr, c) != '￿' ? 1 : null;
                                                                            }

                                                                            public static int indexOf(byte[] bArr, byte b) {
                                                                                return indexOf(bArr, b, 0);
                                                                            }

                                                                            public static int indexOf(byte[] bArr, byte b, int i) {
                                                                                if (bArr == null) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                while (i < bArr.length) {
                                                                                    if (b == bArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(byte[] bArr, byte b) {
                                                                                return lastIndexOf(bArr, b, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(byte[] bArr, byte b, int i) {
                                                                                if (bArr == null || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= bArr.length) {
                                                                                    i = bArr.length - 1;
                                                                                }
                                                                                while (i >= 0) {
                                                                                    if (b == bArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(byte[] bArr, byte b) {
                                                                                return indexOf(bArr, b) != (byte) -1 ? 1 : null;
                                                                            }

                                                                            public static int indexOf(double[] dArr, double d) {
                                                                                return indexOf(dArr, d, 0);
                                                                            }

                                                                            public static int indexOf(double[] dArr, double d, double d2) {
                                                                                return indexOf(dArr, d, 0, d2);
                                                                            }

                                                                            public static int indexOf(double[] dArr, double d, int i) {
                                                                                if (isEmpty(dArr)) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                while (i < dArr.length) {
                                                                                    if (d == dArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int indexOf(double[] dArr, double d, int i, double d2) {
                                                                                if (isEmpty(dArr)) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                double d3 = d - d2;
                                                                                d += d2;
                                                                                while (i < dArr.length) {
                                                                                    if (dArr[i] >= d3 && dArr[i] <= d) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(double[] dArr, double d) {
                                                                                return lastIndexOf(dArr, d, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(double[] dArr, double d, double d2) {
                                                                                return lastIndexOf(dArr, d, Integer.MAX_VALUE, d2);
                                                                            }

                                                                            public static int lastIndexOf(double[] dArr, double d, int i) {
                                                                                if (isEmpty(dArr) || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= dArr.length) {
                                                                                    i = dArr.length - 1;
                                                                                }
                                                                                while (i >= 0) {
                                                                                    if (d == dArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(double[] dArr, double d, int i, double d2) {
                                                                                if (isEmpty(dArr) || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= dArr.length) {
                                                                                    i = dArr.length - 1;
                                                                                }
                                                                                double d3 = d - d2;
                                                                                d += d2;
                                                                                while (i >= 0) {
                                                                                    if (dArr[i] >= d3 && dArr[i] <= d) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(double[] dArr, double d) {
                                                                                return indexOf(dArr, d) != Double.NaN ? 1 : null;
                                                                            }

                                                                            public static boolean contains(double[] dArr, double d, double d2) {
                                                                                return indexOf(dArr, d, 0, d2) != Double.NaN ? 1 : null;
                                                                            }

                                                                            public static int indexOf(float[] fArr, float f) {
                                                                                return indexOf(fArr, f, 0);
                                                                            }

                                                                            public static int indexOf(float[] fArr, float f, int i) {
                                                                                if (isEmpty(fArr)) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                while (i < fArr.length) {
                                                                                    if (f == fArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(float[] fArr, float f) {
                                                                                return lastIndexOf(fArr, f, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(float[] fArr, float f, int i) {
                                                                                if (isEmpty(fArr) || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= fArr.length) {
                                                                                    i = fArr.length - 1;
                                                                                }
                                                                                while (i >= 0) {
                                                                                    if (f == fArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(float[] fArr, float f) {
                                                                                return indexOf(fArr, f) != Float.NaN ? 1 : null;
                                                                            }

                                                                            public static int indexOf(boolean[] zArr, boolean z) {
                                                                                return indexOf(zArr, z, 0);
                                                                            }

                                                                            public static int indexOf(boolean[] zArr, boolean z, int i) {
                                                                                if (isEmpty(zArr)) {
                                                                                    return -1;
                                                                                }
                                                                                if (i < 0) {
                                                                                    i = 0;
                                                                                }
                                                                                while (i < zArr.length) {
                                                                                    if (z == zArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i++;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static int lastIndexOf(boolean[] zArr, boolean z) {
                                                                                return lastIndexOf(zArr, z, Integer.MAX_VALUE);
                                                                            }

                                                                            public static int lastIndexOf(boolean[] zArr, boolean z, int i) {
                                                                                if (isEmpty(zArr) || i < 0) {
                                                                                    return -1;
                                                                                }
                                                                                if (i >= zArr.length) {
                                                                                    i = zArr.length - 1;
                                                                                }
                                                                                while (i >= 0) {
                                                                                    if (z == zArr[i]) {
                                                                                        return i;
                                                                                    }
                                                                                    i--;
                                                                                }
                                                                                return -1;
                                                                            }

                                                                            public static boolean contains(boolean[] zArr, boolean z) {
                                                                                return indexOf(zArr, z) != true ? 1 : null;
                                                                            }

                                                                            public static char[] toPrimitive(Character[] chArr) {
                                                                                if (chArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (chArr.length == 0) {
                                                                                    return EMPTY_CHAR_ARRAY;
                                                                                }
                                                                                char[] cArr = new char[chArr.length];
                                                                                for (int i = 0; i < chArr.length; i++) {
                                                                                    cArr[i] = chArr[i].charValue();
                                                                                }
                                                                                return cArr;
                                                                            }

                                                                            public static char[] toPrimitive(Character[] chArr, char c) {
                                                                                if (chArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (chArr.length == 0) {
                                                                                    return EMPTY_CHAR_ARRAY;
                                                                                }
                                                                                char[] cArr = new char[chArr.length];
                                                                                for (int i = 0; i < chArr.length; i++) {
                                                                                    char c2;
                                                                                    Character ch = chArr[i];
                                                                                    if (ch == null) {
                                                                                        c2 = c;
                                                                                    } else {
                                                                                        c2 = ch.charValue();
                                                                                    }
                                                                                    cArr[i] = c2;
                                                                                }
                                                                                return cArr;
                                                                            }

                                                                            public static Character[] toObject(char[] cArr) {
                                                                                if (cArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (cArr.length == 0) {
                                                                                    return EMPTY_CHARACTER_OBJECT_ARRAY;
                                                                                }
                                                                                Character[] chArr = new Character[cArr.length];
                                                                                for (int i = 0; i < cArr.length; i++) {
                                                                                    chArr[i] = Character.valueOf(cArr[i]);
                                                                                }
                                                                                return chArr;
                                                                            }

                                                                            public static long[] toPrimitive(Long[] lArr) {
                                                                                if (lArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (lArr.length == 0) {
                                                                                    return EMPTY_LONG_ARRAY;
                                                                                }
                                                                                long[] jArr = new long[lArr.length];
                                                                                for (int i = 0; i < lArr.length; i++) {
                                                                                    jArr[i] = lArr[i].longValue();
                                                                                }
                                                                                return jArr;
                                                                            }

                                                                            public static long[] toPrimitive(Long[] lArr, long j) {
                                                                                if (lArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (lArr.length == 0) {
                                                                                    return EMPTY_LONG_ARRAY;
                                                                                }
                                                                                long[] jArr = new long[lArr.length];
                                                                                for (int i = 0; i < lArr.length; i++) {
                                                                                    long j2;
                                                                                    Long l = lArr[i];
                                                                                    if (l == null) {
                                                                                        j2 = j;
                                                                                    } else {
                                                                                        j2 = l.longValue();
                                                                                    }
                                                                                    jArr[i] = j2;
                                                                                }
                                                                                return jArr;
                                                                            }

                                                                            public static Long[] toObject(long[] jArr) {
                                                                                if (jArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (jArr.length == 0) {
                                                                                    return EMPTY_LONG_OBJECT_ARRAY;
                                                                                }
                                                                                Long[] lArr = new Long[jArr.length];
                                                                                for (int i = 0; i < jArr.length; i++) {
                                                                                    lArr[i] = Long.valueOf(jArr[i]);
                                                                                }
                                                                                return lArr;
                                                                            }

                                                                            public static int[] toPrimitive(Integer[] numArr) {
                                                                                if (numArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (numArr.length == 0) {
                                                                                    return EMPTY_INT_ARRAY;
                                                                                }
                                                                                int[] iArr = new int[numArr.length];
                                                                                for (int i = 0; i < numArr.length; i++) {
                                                                                    iArr[i] = numArr[i].intValue();
                                                                                }
                                                                                return iArr;
                                                                            }

                                                                            public static int[] toPrimitive(Integer[] numArr, int i) {
                                                                                if (numArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (numArr.length == 0) {
                                                                                    return EMPTY_INT_ARRAY;
                                                                                }
                                                                                int[] iArr = new int[numArr.length];
                                                                                for (int i2 = 0; i2 < numArr.length; i2++) {
                                                                                    int i3;
                                                                                    Integer num = numArr[i2];
                                                                                    if (num == null) {
                                                                                        i3 = i;
                                                                                    } else {
                                                                                        i3 = num.intValue();
                                                                                    }
                                                                                    iArr[i2] = i3;
                                                                                }
                                                                                return iArr;
                                                                            }

                                                                            public static Integer[] toObject(int[] iArr) {
                                                                                if (iArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (iArr.length == 0) {
                                                                                    return EMPTY_INTEGER_OBJECT_ARRAY;
                                                                                }
                                                                                Integer[] numArr = new Integer[iArr.length];
                                                                                for (int i = 0; i < iArr.length; i++) {
                                                                                    numArr[i] = Integer.valueOf(iArr[i]);
                                                                                }
                                                                                return numArr;
                                                                            }

                                                                            public static short[] toPrimitive(Short[] shArr) {
                                                                                if (shArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (shArr.length == 0) {
                                                                                    return EMPTY_SHORT_ARRAY;
                                                                                }
                                                                                short[] sArr = new short[shArr.length];
                                                                                for (int i = 0; i < shArr.length; i++) {
                                                                                    sArr[i] = shArr[i].shortValue();
                                                                                }
                                                                                return sArr;
                                                                            }

                                                                            public static short[] toPrimitive(Short[] shArr, short s) {
                                                                                if (shArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (shArr.length == 0) {
                                                                                    return EMPTY_SHORT_ARRAY;
                                                                                }
                                                                                short[] sArr = new short[shArr.length];
                                                                                for (int i = 0; i < shArr.length; i++) {
                                                                                    short s2;
                                                                                    Short sh = shArr[i];
                                                                                    if (sh == null) {
                                                                                        s2 = s;
                                                                                    } else {
                                                                                        s2 = sh.shortValue();
                                                                                    }
                                                                                    sArr[i] = s2;
                                                                                }
                                                                                return sArr;
                                                                            }

                                                                            public static Short[] toObject(short[] sArr) {
                                                                                if (sArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (sArr.length == 0) {
                                                                                    return EMPTY_SHORT_OBJECT_ARRAY;
                                                                                }
                                                                                Short[] shArr = new Short[sArr.length];
                                                                                for (int i = 0; i < sArr.length; i++) {
                                                                                    shArr[i] = Short.valueOf(sArr[i]);
                                                                                }
                                                                                return shArr;
                                                                            }

                                                                            public static byte[] toPrimitive(Byte[] bArr) {
                                                                                if (bArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (bArr.length == 0) {
                                                                                    return EMPTY_BYTE_ARRAY;
                                                                                }
                                                                                byte[] bArr2 = new byte[bArr.length];
                                                                                for (int i = 0; i < bArr.length; i++) {
                                                                                    bArr2[i] = bArr[i].byteValue();
                                                                                }
                                                                                return bArr2;
                                                                            }

                                                                            public static byte[] toPrimitive(Byte[] bArr, byte b) {
                                                                                if (bArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (bArr.length == 0) {
                                                                                    return EMPTY_BYTE_ARRAY;
                                                                                }
                                                                                byte[] bArr2 = new byte[bArr.length];
                                                                                for (int i = 0; i < bArr.length; i++) {
                                                                                    byte b2;
                                                                                    Byte b3 = bArr[i];
                                                                                    if (b3 == null) {
                                                                                        b2 = b;
                                                                                    } else {
                                                                                        b2 = b3.byteValue();
                                                                                    }
                                                                                    bArr2[i] = b2;
                                                                                }
                                                                                return bArr2;
                                                                            }

                                                                            public static Byte[] toObject(byte[] bArr) {
                                                                                if (bArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (bArr.length == 0) {
                                                                                    return EMPTY_BYTE_OBJECT_ARRAY;
                                                                                }
                                                                                Byte[] bArr2 = new Byte[bArr.length];
                                                                                for (int i = 0; i < bArr.length; i++) {
                                                                                    bArr2[i] = Byte.valueOf(bArr[i]);
                                                                                }
                                                                                return bArr2;
                                                                            }

                                                                            public static double[] toPrimitive(Double[] dArr) {
                                                                                if (dArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (dArr.length == 0) {
                                                                                    return EMPTY_DOUBLE_ARRAY;
                                                                                }
                                                                                double[] dArr2 = new double[dArr.length];
                                                                                for (int i = 0; i < dArr.length; i++) {
                                                                                    dArr2[i] = dArr[i].doubleValue();
                                                                                }
                                                                                return dArr2;
                                                                            }

                                                                            public static double[] toPrimitive(Double[] dArr, double d) {
                                                                                if (dArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (dArr.length == 0) {
                                                                                    return EMPTY_DOUBLE_ARRAY;
                                                                                }
                                                                                double[] dArr2 = new double[dArr.length];
                                                                                for (int i = 0; i < dArr.length; i++) {
                                                                                    double d2;
                                                                                    Double d3 = dArr[i];
                                                                                    if (d3 == null) {
                                                                                        d2 = d;
                                                                                    } else {
                                                                                        d2 = d3.doubleValue();
                                                                                    }
                                                                                    dArr2[i] = d2;
                                                                                }
                                                                                return dArr2;
                                                                            }

                                                                            public static Double[] toObject(double[] dArr) {
                                                                                if (dArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (dArr.length == 0) {
                                                                                    return EMPTY_DOUBLE_OBJECT_ARRAY;
                                                                                }
                                                                                Double[] dArr2 = new Double[dArr.length];
                                                                                for (int i = 0; i < dArr.length; i++) {
                                                                                    dArr2[i] = Double.valueOf(dArr[i]);
                                                                                }
                                                                                return dArr2;
                                                                            }

                                                                            public static float[] toPrimitive(Float[] fArr) {
                                                                                if (fArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (fArr.length == 0) {
                                                                                    return EMPTY_FLOAT_ARRAY;
                                                                                }
                                                                                float[] fArr2 = new float[fArr.length];
                                                                                for (int i = 0; i < fArr.length; i++) {
                                                                                    fArr2[i] = fArr[i].floatValue();
                                                                                }
                                                                                return fArr2;
                                                                            }

                                                                            public static float[] toPrimitive(Float[] fArr, float f) {
                                                                                if (fArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (fArr.length == 0) {
                                                                                    return EMPTY_FLOAT_ARRAY;
                                                                                }
                                                                                float[] fArr2 = new float[fArr.length];
                                                                                for (int i = 0; i < fArr.length; i++) {
                                                                                    float f2;
                                                                                    Float f3 = fArr[i];
                                                                                    if (f3 == null) {
                                                                                        f2 = f;
                                                                                    } else {
                                                                                        f2 = f3.floatValue();
                                                                                    }
                                                                                    fArr2[i] = f2;
                                                                                }
                                                                                return fArr2;
                                                                            }

                                                                            public static Float[] toObject(float[] fArr) {
                                                                                if (fArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (fArr.length == 0) {
                                                                                    return EMPTY_FLOAT_OBJECT_ARRAY;
                                                                                }
                                                                                Float[] fArr2 = new Float[fArr.length];
                                                                                for (int i = 0; i < fArr.length; i++) {
                                                                                    fArr2[i] = Float.valueOf(fArr[i]);
                                                                                }
                                                                                return fArr2;
                                                                            }

                                                                            public static Object toPrimitive(Object obj) {
                                                                                if (obj == null) {
                                                                                    return null;
                                                                                }
                                                                                Class wrapperToPrimitive = ClassUtils.wrapperToPrimitive(obj.getClass().getComponentType());
                                                                                if (Integer.TYPE.equals(wrapperToPrimitive)) {
                                                                                    return toPrimitive((Integer[]) obj);
                                                                                }
                                                                                if (Long.TYPE.equals(wrapperToPrimitive)) {
                                                                                    return toPrimitive((Long[]) obj);
                                                                                }
                                                                                if (Short.TYPE.equals(wrapperToPrimitive)) {
                                                                                    return toPrimitive((Short[]) obj);
                                                                                }
                                                                                if (Double.TYPE.equals(wrapperToPrimitive)) {
                                                                                    return toPrimitive((Double[]) obj);
                                                                                }
                                                                                return Float.TYPE.equals(wrapperToPrimitive) ? toPrimitive((Float[]) obj) : obj;
                                                                            }

                                                                            public static boolean[] toPrimitive(Boolean[] boolArr) {
                                                                                if (boolArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (boolArr.length == 0) {
                                                                                    return EMPTY_BOOLEAN_ARRAY;
                                                                                }
                                                                                boolean[] zArr = new boolean[boolArr.length];
                                                                                for (int i = 0; i < boolArr.length; i++) {
                                                                                    zArr[i] = boolArr[i].booleanValue();
                                                                                }
                                                                                return zArr;
                                                                            }

                                                                            public static boolean[] toPrimitive(Boolean[] boolArr, boolean z) {
                                                                                if (boolArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (boolArr.length == 0) {
                                                                                    return EMPTY_BOOLEAN_ARRAY;
                                                                                }
                                                                                boolean[] zArr = new boolean[boolArr.length];
                                                                                for (int i = 0; i < boolArr.length; i++) {
                                                                                    boolean z2;
                                                                                    Boolean bool = boolArr[i];
                                                                                    if (bool == null) {
                                                                                        z2 = z;
                                                                                    } else {
                                                                                        z2 = bool.booleanValue();
                                                                                    }
                                                                                    zArr[i] = z2;
                                                                                }
                                                                                return zArr;
                                                                            }

                                                                            public static Boolean[] toObject(boolean[] zArr) {
                                                                                if (zArr == null) {
                                                                                    return null;
                                                                                }
                                                                                if (zArr.length == 0) {
                                                                                    return EMPTY_BOOLEAN_OBJECT_ARRAY;
                                                                                }
                                                                                Boolean[] boolArr = new Boolean[zArr.length];
                                                                                for (int i = 0; i < zArr.length; i++) {
                                                                                    boolArr[i] = zArr[i] ? Boolean.TRUE : Boolean.FALSE;
                                                                                }
                                                                                return boolArr;
                                                                            }

                                                                            public static boolean isEmpty(Object[] objArr) {
                                                                                return getLength(objArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(long[] jArr) {
                                                                                return getLength(jArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(int[] iArr) {
                                                                                return getLength(iArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(short[] sArr) {
                                                                                return getLength(sArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(char[] cArr) {
                                                                                return getLength(cArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(byte[] bArr) {
                                                                                return getLength(bArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(double[] dArr) {
                                                                                return getLength(dArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(float[] fArr) {
                                                                                return getLength(fArr) == null ? 1 : null;
                                                                            }

                                                                            public static boolean isEmpty(boolean[] zArr) {
                                                                                return getLength(zArr) == null ? 1 : null;
                                                                            }

                                                                            public static <T> boolean isNotEmpty(T[] tArr) {
                                                                                return isEmpty((Object[]) tArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(long[] jArr) {
                                                                                return isEmpty(jArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(int[] iArr) {
                                                                                return isEmpty(iArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(short[] sArr) {
                                                                                return isEmpty(sArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(char[] cArr) {
                                                                                return isEmpty(cArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(byte[] bArr) {
                                                                                return isEmpty(bArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(double[] dArr) {
                                                                                return isEmpty(dArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(float[] fArr) {
                                                                                return isEmpty(fArr) ^ 1;
                                                                            }

                                                                            public static boolean isNotEmpty(boolean[] zArr) {
                                                                                return isEmpty(zArr) ^ 1;
                                                                            }

                                                                            public static <T> T[] addAll(T[] tArr, T... tArr2) {
                                                                                if (tArr == null) {
                                                                                    return clone((Object[]) tArr2);
                                                                                }
                                                                                if (tArr2 == null) {
                                                                                    return clone((Object[]) tArr);
                                                                                }
                                                                                Class componentType = tArr.getClass().getComponentType();
                                                                                Object[] objArr = (Object[]) Array.newInstance(componentType, tArr.length + tArr2.length);
                                                                                System.arraycopy(tArr, 0, objArr, 0, tArr.length);
                                                                                try {
                                                                                    System.arraycopy(tArr2, 0, objArr, tArr.length, tArr2.length);
                                                                                    return objArr;
                                                                                } catch (T[] tArr3) {
                                                                                    tArr2 = tArr2.getClass().getComponentType();
                                                                                    if (componentType.isAssignableFrom(tArr2)) {
                                                                                        throw tArr3;
                                                                                    }
                                                                                    StringBuilder stringBuilder = new StringBuilder();
                                                                                    stringBuilder.append("Cannot store ");
                                                                                    stringBuilder.append(tArr2.getName());
                                                                                    stringBuilder.append(" in an array of ");
                                                                                    stringBuilder.append(componentType.getName());
                                                                                    throw new IllegalArgumentException(stringBuilder.toString(), tArr3);
                                                                                }
                                                                            }

                                                                            public static boolean[] addAll(boolean[] zArr, boolean... zArr2) {
                                                                                if (zArr == null) {
                                                                                    return clone(zArr2);
                                                                                }
                                                                                if (zArr2 == null) {
                                                                                    return clone(zArr);
                                                                                }
                                                                                Object obj = new boolean[(zArr.length + zArr2.length)];
                                                                                System.arraycopy(zArr, 0, obj, 0, zArr.length);
                                                                                System.arraycopy(zArr2, 0, obj, zArr.length, zArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static char[] addAll(char[] cArr, char... cArr2) {
                                                                                if (cArr == null) {
                                                                                    return clone(cArr2);
                                                                                }
                                                                                if (cArr2 == null) {
                                                                                    return clone(cArr);
                                                                                }
                                                                                Object obj = new char[(cArr.length + cArr2.length)];
                                                                                System.arraycopy(cArr, 0, obj, 0, cArr.length);
                                                                                System.arraycopy(cArr2, 0, obj, cArr.length, cArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static byte[] addAll(byte[] bArr, byte... bArr2) {
                                                                                if (bArr == null) {
                                                                                    return clone(bArr2);
                                                                                }
                                                                                if (bArr2 == null) {
                                                                                    return clone(bArr);
                                                                                }
                                                                                Object obj = new byte[(bArr.length + bArr2.length)];
                                                                                System.arraycopy(bArr, 0, obj, 0, bArr.length);
                                                                                System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static short[] addAll(short[] sArr, short... sArr2) {
                                                                                if (sArr == null) {
                                                                                    return clone(sArr2);
                                                                                }
                                                                                if (sArr2 == null) {
                                                                                    return clone(sArr);
                                                                                }
                                                                                Object obj = new short[(sArr.length + sArr2.length)];
                                                                                System.arraycopy(sArr, 0, obj, 0, sArr.length);
                                                                                System.arraycopy(sArr2, 0, obj, sArr.length, sArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static int[] addAll(int[] iArr, int... iArr2) {
                                                                                if (iArr == null) {
                                                                                    return clone(iArr2);
                                                                                }
                                                                                if (iArr2 == null) {
                                                                                    return clone(iArr);
                                                                                }
                                                                                Object obj = new int[(iArr.length + iArr2.length)];
                                                                                System.arraycopy(iArr, 0, obj, 0, iArr.length);
                                                                                System.arraycopy(iArr2, 0, obj, iArr.length, iArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static long[] addAll(long[] jArr, long... jArr2) {
                                                                                if (jArr == null) {
                                                                                    return clone(jArr2);
                                                                                }
                                                                                if (jArr2 == null) {
                                                                                    return clone(jArr);
                                                                                }
                                                                                Object obj = new long[(jArr.length + jArr2.length)];
                                                                                System.arraycopy(jArr, 0, obj, 0, jArr.length);
                                                                                System.arraycopy(jArr2, 0, obj, jArr.length, jArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static float[] addAll(float[] fArr, float... fArr2) {
                                                                                if (fArr == null) {
                                                                                    return clone(fArr2);
                                                                                }
                                                                                if (fArr2 == null) {
                                                                                    return clone(fArr);
                                                                                }
                                                                                Object obj = new float[(fArr.length + fArr2.length)];
                                                                                System.arraycopy(fArr, 0, obj, 0, fArr.length);
                                                                                System.arraycopy(fArr2, 0, obj, fArr.length, fArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static double[] addAll(double[] dArr, double... dArr2) {
                                                                                if (dArr == null) {
                                                                                    return clone(dArr2);
                                                                                }
                                                                                if (dArr2 == null) {
                                                                                    return clone(dArr);
                                                                                }
                                                                                Object obj = new double[(dArr.length + dArr2.length)];
                                                                                System.arraycopy(dArr, 0, obj, 0, dArr.length);
                                                                                System.arraycopy(dArr2, 0, obj, dArr.length, dArr2.length);
                                                                                return obj;
                                                                            }

                                                                            public static <T> T[] add(T[] tArr, T t) {
                                                                                Class componentType;
                                                                                if (tArr != null) {
                                                                                    componentType = tArr.getClass().getComponentType();
                                                                                } else if (t != null) {
                                                                                    componentType = t.getClass();
                                                                                } else {
                                                                                    throw new IllegalArgumentException("Arguments cannot both be null");
                                                                                }
                                                                                Object[] objArr = (Object[]) copyArrayGrow1(tArr, componentType);
                                                                                objArr[objArr.length - 1] = t;
                                                                                return objArr;
                                                                            }

                                                                            public static boolean[] add(boolean[] zArr, boolean z) {
                                                                                zArr = (boolean[]) copyArrayGrow1(zArr, Boolean.TYPE);
                                                                                zArr[zArr.length - 1] = z;
                                                                                return zArr;
                                                                            }

                                                                            public static byte[] add(byte[] bArr, byte b) {
                                                                                bArr = (byte[]) copyArrayGrow1(bArr, Byte.TYPE);
                                                                                bArr[bArr.length - 1] = b;
                                                                                return bArr;
                                                                            }

                                                                            public static char[] add(char[] cArr, char c) {
                                                                                cArr = (char[]) copyArrayGrow1(cArr, Character.TYPE);
                                                                                cArr[cArr.length - 1] = c;
                                                                                return cArr;
                                                                            }

                                                                            public static double[] add(double[] dArr, double d) {
                                                                                dArr = (double[]) copyArrayGrow1(dArr, Double.TYPE);
                                                                                dArr[dArr.length - 1] = d;
                                                                                return dArr;
                                                                            }

                                                                            public static float[] add(float[] fArr, float f) {
                                                                                fArr = (float[]) copyArrayGrow1(fArr, Float.TYPE);
                                                                                fArr[fArr.length - 1] = f;
                                                                                return fArr;
                                                                            }

                                                                            public static int[] add(int[] iArr, int i) {
                                                                                iArr = (int[]) copyArrayGrow1(iArr, Integer.TYPE);
                                                                                iArr[iArr.length - 1] = i;
                                                                                return iArr;
                                                                            }

                                                                            public static long[] add(long[] jArr, long j) {
                                                                                jArr = (long[]) copyArrayGrow1(jArr, Long.TYPE);
                                                                                jArr[jArr.length - 1] = j;
                                                                                return jArr;
                                                                            }

                                                                            public static short[] add(short[] sArr, short s) {
                                                                                sArr = (short[]) copyArrayGrow1(sArr, Short.TYPE);
                                                                                sArr[sArr.length - 1] = s;
                                                                                return sArr;
                                                                            }

                                                                            private static Object copyArrayGrow1(Object obj, Class<?> cls) {
                                                                                if (obj == null) {
                                                                                    return Array.newInstance(cls, 1);
                                                                                }
                                                                                cls = Array.getLength(obj);
                                                                                Object newInstance = Array.newInstance(obj.getClass().getComponentType(), cls + 1);
                                                                                System.arraycopy(obj, 0, newInstance, 0, cls);
                                                                                return newInstance;
                                                                            }

                                                                            public static <T> T[] add(T[] tArr, int i, T t) {
                                                                                Class componentType;
                                                                                if (tArr != null) {
                                                                                    componentType = tArr.getClass().getComponentType();
                                                                                } else if (t != null) {
                                                                                    componentType = t.getClass();
                                                                                } else {
                                                                                    throw new IllegalArgumentException("Array and element cannot both be null");
                                                                                }
                                                                                return (Object[]) add(tArr, i, t, componentType);
                                                                            }

                                                                            public static boolean[] add(boolean[] zArr, int i, boolean z) {
                                                                                return (boolean[]) add(zArr, i, Boolean.valueOf(z), Boolean.TYPE);
                                                                            }

                                                                            public static char[] add(char[] cArr, int i, char c) {
                                                                                return (char[]) add(cArr, i, Character.valueOf(c), Character.TYPE);
                                                                            }

                                                                            public static byte[] add(byte[] bArr, int i, byte b) {
                                                                                return (byte[]) add(bArr, i, Byte.valueOf(b), Byte.TYPE);
                                                                            }

                                                                            public static short[] add(short[] sArr, int i, short s) {
                                                                                return (short[]) add(sArr, i, Short.valueOf(s), Short.TYPE);
                                                                            }

                                                                            public static int[] add(int[] iArr, int i, int i2) {
                                                                                return (int[]) add(iArr, i, Integer.valueOf(i2), Integer.TYPE);
                                                                            }

                                                                            public static long[] add(long[] jArr, int i, long j) {
                                                                                return (long[]) add(jArr, i, Long.valueOf(j), Long.TYPE);
                                                                            }

                                                                            public static float[] add(float[] fArr, int i, float f) {
                                                                                return (float[]) add(fArr, i, Float.valueOf(f), Float.TYPE);
                                                                            }

                                                                            public static double[] add(double[] dArr, int i, double d) {
                                                                                return (double[]) add(dArr, i, Double.valueOf(d), Double.TYPE);
                                                                            }

                                                                            private static Object add(Object obj, int i, Object obj2, Class<?> cls) {
                                                                                if (obj != null) {
                                                                                    int length = Array.getLength(obj);
                                                                                    if (i <= length) {
                                                                                        if (i >= 0) {
                                                                                            cls = Array.newInstance(cls, length + 1);
                                                                                            System.arraycopy(obj, 0, cls, 0, i);
                                                                                            Array.set(cls, i, obj2);
                                                                                            if (i < length) {
                                                                                                System.arraycopy(obj, i, cls, i + 1, length - i);
                                                                                            }
                                                                                            return cls;
                                                                                        }
                                                                                    }
                                                                                    obj2 = new StringBuilder();
                                                                                    obj2.append("Index: ");
                                                                                    obj2.append(i);
                                                                                    obj2.append(", Length: ");
                                                                                    obj2.append(length);
                                                                                    throw new IndexOutOfBoundsException(obj2.toString());
                                                                                } else if (i != 0) {
                                                                                    obj2 = new StringBuilder();
                                                                                    obj2.append("Index: ");
                                                                                    obj2.append(i);
                                                                                    obj2.append(", Length: 0");
                                                                                    throw new IndexOutOfBoundsException(obj2.toString());
                                                                                } else {
                                                                                    obj = Array.newInstance(cls, 1);
                                                                                    Array.set(obj, 0, obj2);
                                                                                    return obj;
                                                                                }
                                                                            }

                                                                            public static <T> T[] remove(T[] tArr, int i) {
                                                                                return (Object[]) remove((Object) tArr, i);
                                                                            }

                                                                            public static <T> T[] removeElement(T[] tArr, Object obj) {
                                                                                int indexOf = indexOf((Object[]) tArr, obj);
                                                                                if (indexOf == -1) {
                                                                                    return clone((Object[]) tArr);
                                                                                }
                                                                                return remove((Object[]) tArr, indexOf);
                                                                            }

                                                                            public static boolean[] remove(boolean[] zArr, int i) {
                                                                                return (boolean[]) remove((Object) zArr, i);
                                                                            }

                                                                            public static boolean[] removeElement(boolean[] zArr, boolean z) {
                                                                                int indexOf = indexOf(zArr, z);
                                                                                if (indexOf == true) {
                                                                                    return clone(zArr);
                                                                                }
                                                                                return remove(zArr, indexOf);
                                                                            }

                                                                            public static byte[] remove(byte[] bArr, int i) {
                                                                                return (byte[]) remove((Object) bArr, i);
                                                                            }

                                                                            public static byte[] removeElement(byte[] bArr, byte b) {
                                                                                int indexOf = indexOf(bArr, b);
                                                                                if (indexOf == (byte) -1) {
                                                                                    return clone(bArr);
                                                                                }
                                                                                return remove(bArr, indexOf);
                                                                            }

                                                                            public static char[] remove(char[] cArr, int i) {
                                                                                return (char[]) remove((Object) cArr, i);
                                                                            }

                                                                            public static char[] removeElement(char[] cArr, char c) {
                                                                                int indexOf = indexOf(cArr, c);
                                                                                if (indexOf == '￿') {
                                                                                    return clone(cArr);
                                                                                }
                                                                                return remove(cArr, indexOf);
                                                                            }

                                                                            public static double[] remove(double[] dArr, int i) {
                                                                                return (double[]) remove((Object) dArr, i);
                                                                            }

                                                                            public static double[] removeElement(double[] dArr, double d) {
                                                                                int indexOf = indexOf(dArr, d);
                                                                                if (indexOf == -1) {
                                                                                    return clone(dArr);
                                                                                }
                                                                                return remove(dArr, indexOf);
                                                                            }

                                                                            public static float[] remove(float[] fArr, int i) {
                                                                                return (float[]) remove((Object) fArr, i);
                                                                            }

                                                                            public static float[] removeElement(float[] fArr, float f) {
                                                                                int indexOf = indexOf(fArr, f);
                                                                                if (indexOf == -1) {
                                                                                    return clone(fArr);
                                                                                }
                                                                                return remove(fArr, indexOf);
                                                                            }

                                                                            public static int[] remove(int[] iArr, int i) {
                                                                                return (int[]) remove((Object) iArr, i);
                                                                            }

                                                                            public static int[] removeElement(int[] iArr, int i) {
                                                                                i = indexOf(iArr, i);
                                                                                if (i == -1) {
                                                                                    return clone(iArr);
                                                                                }
                                                                                return remove(iArr, i);
                                                                            }

                                                                            public static long[] remove(long[] jArr, int i) {
                                                                                return (long[]) remove((Object) jArr, i);
                                                                            }

                                                                            public static long[] removeElement(long[] jArr, long j) {
                                                                                int indexOf = indexOf(jArr, j);
                                                                                if (indexOf == -1) {
                                                                                    return clone(jArr);
                                                                                }
                                                                                return remove(jArr, indexOf);
                                                                            }

                                                                            public static short[] remove(short[] sArr, int i) {
                                                                                return (short[]) remove((Object) sArr, i);
                                                                            }

                                                                            public static short[] removeElement(short[] sArr, short s) {
                                                                                int indexOf = indexOf(sArr, s);
                                                                                if (indexOf == (short) -1) {
                                                                                    return clone(sArr);
                                                                                }
                                                                                return remove(sArr, indexOf);
                                                                            }

                                                                            private static Object remove(Object obj, int i) {
                                                                                int length = getLength(obj);
                                                                                if (i >= 0) {
                                                                                    if (i < length) {
                                                                                        int i2 = length - 1;
                                                                                        Object newInstance = Array.newInstance(obj.getClass().getComponentType(), i2);
                                                                                        System.arraycopy(obj, 0, newInstance, 0, i);
                                                                                        if (i < i2) {
                                                                                            System.arraycopy(obj, i + 1, newInstance, i, (length - i) - 1);
                                                                                        }
                                                                                        return newInstance;
                                                                                    }
                                                                                }
                                                                                StringBuilder stringBuilder = new StringBuilder();
                                                                                stringBuilder.append("Index: ");
                                                                                stringBuilder.append(i);
                                                                                stringBuilder.append(", Length: ");
                                                                                stringBuilder.append(length);
                                                                                throw new IndexOutOfBoundsException(stringBuilder.toString());
                                                                            }

                                                                            public static <T> T[] removeAll(T[] tArr, int... iArr) {
                                                                                return (Object[]) removeAll((Object) tArr, iArr);
                                                                            }

                                                                            public static <T> T[] removeElements(T[] tArr, T... tArr2) {
                                                                                if (!isEmpty((Object[]) tArr)) {
                                                                                    if (!isEmpty((Object[]) tArr2)) {
                                                                                        HashMap hashMap = new HashMap(tArr2.length);
                                                                                        for (Object obj : tArr2) {
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(obj);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(obj, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < tArr.length; i++) {
                                                                                            Object obj2 = tArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(obj2);
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(obj2);
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (Object[]) removeAll((Object) tArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone((Object[]) tArr);
                                                                            }

                                                                            public static byte[] removeAll(byte[] bArr, int... iArr) {
                                                                                return (byte[]) removeAll((Object) bArr, iArr);
                                                                            }

                                                                            public static byte[] removeElements(byte[] bArr, byte... bArr2) {
                                                                                if (!isEmpty(bArr)) {
                                                                                    if (!isEmpty(bArr2)) {
                                                                                        Map hashMap = new HashMap(bArr2.length);
                                                                                        for (byte valueOf : bArr2) {
                                                                                            Byte valueOf2 = Byte.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < bArr.length; i++) {
                                                                                            byte b = bArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Byte.valueOf(b));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Byte.valueOf(b));
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (byte[]) removeAll((Object) bArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(bArr);
                                                                            }

                                                                            public static short[] removeAll(short[] sArr, int... iArr) {
                                                                                return (short[]) removeAll((Object) sArr, iArr);
                                                                            }

                                                                            public static short[] removeElements(short[] sArr, short... sArr2) {
                                                                                if (!isEmpty(sArr)) {
                                                                                    if (!isEmpty(sArr2)) {
                                                                                        HashMap hashMap = new HashMap(sArr2.length);
                                                                                        for (short valueOf : sArr2) {
                                                                                            Short valueOf2 = Short.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < sArr.length; i++) {
                                                                                            short s = sArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Short.valueOf(s));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Short.valueOf(s));
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (short[]) removeAll((Object) sArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(sArr);
                                                                            }

                                                                            public static int[] removeAll(int[] iArr, int... iArr2) {
                                                                                return (int[]) removeAll((Object) iArr, iArr2);
                                                                            }

                                                                            public static int[] removeElements(int[] iArr, int... iArr2) {
                                                                                if (!isEmpty(iArr)) {
                                                                                    if (!isEmpty(iArr2)) {
                                                                                        int i;
                                                                                        HashMap hashMap = new HashMap(iArr2.length);
                                                                                        for (int valueOf : iArr2) {
                                                                                            Integer valueOf2 = Integer.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i2 = 0; i2 < iArr.length; i2++) {
                                                                                            i = iArr[i2];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Integer.valueOf(i));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Integer.valueOf(i));
                                                                                                }
                                                                                                bitSet.set(i2);
                                                                                            }
                                                                                        }
                                                                                        return (int[]) removeAll((Object) iArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(iArr);
                                                                            }

                                                                            public static char[] removeAll(char[] cArr, int... iArr) {
                                                                                return (char[]) removeAll((Object) cArr, iArr);
                                                                            }

                                                                            public static char[] removeElements(char[] cArr, char... cArr2) {
                                                                                if (!isEmpty(cArr)) {
                                                                                    if (!isEmpty(cArr2)) {
                                                                                        HashMap hashMap = new HashMap(cArr2.length);
                                                                                        for (char valueOf : cArr2) {
                                                                                            Character valueOf2 = Character.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < cArr.length; i++) {
                                                                                            char c = cArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Character.valueOf(c));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Character.valueOf(c));
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (char[]) removeAll((Object) cArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(cArr);
                                                                            }

                                                                            public static long[] removeAll(long[] jArr, int... iArr) {
                                                                                return (long[]) removeAll((Object) jArr, iArr);
                                                                            }

                                                                            public static long[] removeElements(long[] jArr, long... jArr2) {
                                                                                if (!isEmpty(jArr)) {
                                                                                    if (!isEmpty(jArr2)) {
                                                                                        HashMap hashMap = new HashMap(jArr2.length);
                                                                                        for (long valueOf : jArr2) {
                                                                                            Long valueOf2 = Long.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < jArr.length; i++) {
                                                                                            long j = jArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Long.valueOf(j));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Long.valueOf(j));
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (long[]) removeAll((Object) jArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(jArr);
                                                                            }

                                                                            public static float[] removeAll(float[] fArr, int... iArr) {
                                                                                return (float[]) removeAll((Object) fArr, iArr);
                                                                            }

                                                                            public static float[] removeElements(float[] fArr, float... fArr2) {
                                                                                if (!isEmpty(fArr)) {
                                                                                    if (!isEmpty(fArr2)) {
                                                                                        HashMap hashMap = new HashMap(fArr2.length);
                                                                                        for (float valueOf : fArr2) {
                                                                                            Float valueOf2 = Float.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < fArr.length; i++) {
                                                                                            float f = fArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Float.valueOf(f));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Float.valueOf(f));
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (float[]) removeAll((Object) fArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(fArr);
                                                                            }

                                                                            public static double[] removeAll(double[] dArr, int... iArr) {
                                                                                return (double[]) removeAll((Object) dArr, iArr);
                                                                            }

                                                                            public static double[] removeElements(double[] dArr, double... dArr2) {
                                                                                if (!isEmpty(dArr)) {
                                                                                    if (!isEmpty(dArr2)) {
                                                                                        HashMap hashMap = new HashMap(dArr2.length);
                                                                                        for (double valueOf : dArr2) {
                                                                                            Double valueOf2 = Double.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < dArr.length; i++) {
                                                                                            double d = dArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Double.valueOf(d));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Double.valueOf(d));
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (double[]) removeAll((Object) dArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(dArr);
                                                                            }

                                                                            public static boolean[] removeAll(boolean[] zArr, int... iArr) {
                                                                                return (boolean[]) removeAll((Object) zArr, iArr);
                                                                            }

                                                                            public static boolean[] removeElements(boolean[] zArr, boolean... zArr2) {
                                                                                if (!isEmpty(zArr)) {
                                                                                    if (!isEmpty(zArr2)) {
                                                                                        HashMap hashMap = new HashMap(2);
                                                                                        for (boolean valueOf : zArr2) {
                                                                                            Boolean valueOf2 = Boolean.valueOf(valueOf);
                                                                                            MutableInt mutableInt = (MutableInt) hashMap.get(valueOf2);
                                                                                            if (mutableInt == null) {
                                                                                                hashMap.put(valueOf2, new MutableInt(1));
                                                                                            } else {
                                                                                                mutableInt.increment();
                                                                                            }
                                                                                        }
                                                                                        BitSet bitSet = new BitSet();
                                                                                        for (int i = 0; i < zArr.length; i++) {
                                                                                            boolean z = zArr[i];
                                                                                            MutableInt mutableInt2 = (MutableInt) hashMap.get(Boolean.valueOf(z));
                                                                                            if (mutableInt2 != null) {
                                                                                                if (mutableInt2.decrementAndGet() == 0) {
                                                                                                    hashMap.remove(Boolean.valueOf(z));
                                                                                                }
                                                                                                bitSet.set(i);
                                                                                            }
                                                                                        }
                                                                                        return (boolean[]) removeAll((Object) zArr, bitSet);
                                                                                    }
                                                                                }
                                                                                return clone(zArr);
                                                                            }

                                                                            static Object removeAll(Object obj, int... iArr) {
                                                                                int i;
                                                                                int i2;
                                                                                int length = getLength(obj);
                                                                                iArr = clone(iArr);
                                                                                Arrays.sort(iArr);
                                                                                if (isNotEmpty(iArr)) {
                                                                                    int i3;
                                                                                    int length2 = iArr.length;
                                                                                    i = length;
                                                                                    i2 = 0;
                                                                                    while (true) {
                                                                                        length2--;
                                                                                        if (length2 < 0) {
                                                                                            break;
                                                                                        }
                                                                                        i3 = iArr[length2];
                                                                                        if (i3 < 0) {
                                                                                            break;
                                                                                        } else if (i3 >= length) {
                                                                                            break;
                                                                                        } else if (i3 < i) {
                                                                                            i2++;
                                                                                            i = i3;
                                                                                        }
                                                                                    }
                                                                                    iArr = new StringBuilder();
                                                                                    iArr.append("Index: ");
                                                                                    iArr.append(i3);
                                                                                    iArr.append(", Length: ");
                                                                                    iArr.append(length);
                                                                                    throw new IndexOutOfBoundsException(iArr.toString());
                                                                                }
                                                                                i2 = 0;
                                                                                i = length - i2;
                                                                                Object newInstance = Array.newInstance(obj.getClass().getComponentType(), i);
                                                                                if (i2 < length) {
                                                                                    i2 = iArr.length - 1;
                                                                                    while (i2 >= 0) {
                                                                                        int i4 = iArr[i2];
                                                                                        length -= i4;
                                                                                        if (length > 1) {
                                                                                            length--;
                                                                                            i -= length;
                                                                                            System.arraycopy(obj, i4 + 1, newInstance, i, length);
                                                                                        }
                                                                                        i2--;
                                                                                        length = i4;
                                                                                    }
                                                                                    if (length > 0) {
                                                                                        System.arraycopy(obj, 0, newInstance, 0, length);
                                                                                    }
                                                                                }
                                                                                return newInstance;
                                                                            }

                                                                            static Object removeAll(Object obj, BitSet bitSet) {
                                                                                int length = getLength(obj);
                                                                                Object newInstance = Array.newInstance(obj.getClass().getComponentType(), length - bitSet.cardinality());
                                                                                int i = 0;
                                                                                int i2 = 0;
                                                                                while (true) {
                                                                                    int nextSetBit = bitSet.nextSetBit(i);
                                                                                    if (nextSetBit == -1) {
                                                                                        break;
                                                                                    }
                                                                                    int i3 = nextSetBit - i;
                                                                                    if (i3 > 0) {
                                                                                        System.arraycopy(obj, i, newInstance, i2, i3);
                                                                                        i2 += i3;
                                                                                    }
                                                                                    i = bitSet.nextClearBit(nextSetBit);
                                                                                }
                                                                                length -= i;
                                                                                if (length > 0) {
                                                                                    System.arraycopy(obj, i, newInstance, i2, length);
                                                                                }
                                                                                return newInstance;
                                                                            }

                                                                            public static <T extends Comparable<? super T>> boolean isSorted(T[] tArr) {
                                                                                return isSorted(tArr, new C14401());
                                                                            }

                                                                            public static <T> boolean isSorted(T[] tArr, Comparator<T> comparator) {
                                                                                if (comparator == null) {
                                                                                    throw new IllegalArgumentException("Comparator should not be null.");
                                                                                }
                                                                                if (tArr != null) {
                                                                                    if (tArr.length >= 2) {
                                                                                        T t = tArr[0];
                                                                                        int length = tArr.length;
                                                                                        Object obj = t;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            Object obj2 = tArr[i];
                                                                                            if (comparator.compare(obj, obj2) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            obj = obj2;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(int[] iArr) {
                                                                                if (iArr != null) {
                                                                                    if (iArr.length >= 2) {
                                                                                        int i = iArr[0];
                                                                                        int length = iArr.length;
                                                                                        int i2 = i;
                                                                                        i = 1;
                                                                                        while (i < length) {
                                                                                            int i3 = iArr[i];
                                                                                            if (NumberUtils.compare(i2, i3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            i2 = i3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(long[] jArr) {
                                                                                if (jArr != null) {
                                                                                    if (jArr.length >= 2) {
                                                                                        long j = jArr[0];
                                                                                        int length = jArr.length;
                                                                                        long j2 = j;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            long j3 = jArr[i];
                                                                                            if (NumberUtils.compare(j2, j3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            j2 = j3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(short[] sArr) {
                                                                                if (sArr != null) {
                                                                                    if (sArr.length >= 2) {
                                                                                        short s = sArr[0];
                                                                                        int length = sArr.length;
                                                                                        short s2 = s;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            short s3 = sArr[i];
                                                                                            if (NumberUtils.compare(s2, s3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            s2 = s3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(double[] dArr) {
                                                                                if (dArr != null) {
                                                                                    if (dArr.length >= 2) {
                                                                                        double d = dArr[0];
                                                                                        int length = dArr.length;
                                                                                        double d2 = d;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            double d3 = dArr[i];
                                                                                            if (Double.compare(d2, d3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            d2 = d3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(float[] fArr) {
                                                                                if (fArr != null) {
                                                                                    if (fArr.length >= 2) {
                                                                                        float f = fArr[0];
                                                                                        int length = fArr.length;
                                                                                        float f2 = f;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            float f3 = fArr[i];
                                                                                            if (Float.compare(f2, f3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            f2 = f3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(byte[] bArr) {
                                                                                if (bArr != null) {
                                                                                    if (bArr.length >= 2) {
                                                                                        byte b = bArr[0];
                                                                                        int length = bArr.length;
                                                                                        byte b2 = b;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            byte b3 = bArr[i];
                                                                                            if (NumberUtils.compare(b2, b3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            b2 = b3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(char[] cArr) {
                                                                                if (cArr != null) {
                                                                                    if (cArr.length >= 2) {
                                                                                        char c = cArr[0];
                                                                                        int length = cArr.length;
                                                                                        char c2 = c;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            char c3 = cArr[i];
                                                                                            if (CharUtils.compare(c2, c3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            c2 = c3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean isSorted(boolean[] zArr) {
                                                                                if (zArr != null) {
                                                                                    if (zArr.length >= 2) {
                                                                                        boolean z = zArr[0];
                                                                                        int length = zArr.length;
                                                                                        boolean z2 = z;
                                                                                        int i = 1;
                                                                                        while (i < length) {
                                                                                            boolean z3 = zArr[i];
                                                                                            if (BooleanUtils.compare(z2, z3) > 0) {
                                                                                                return false;
                                                                                            }
                                                                                            i++;
                                                                                            z2 = z3;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                }
                                                                                return true;
                                                                            }

                                                                            public static boolean[] removeAllOccurences(boolean[] zArr, boolean z) {
                                                                                int indexOf = indexOf(zArr, z);
                                                                                if (indexOf == -1) {
                                                                                    return clone(zArr);
                                                                                }
                                                                                int[] iArr = new int[(zArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(zArr, z, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(zArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }

                                                                            public static char[] removeAllOccurences(char[] cArr, char c) {
                                                                                int indexOf = indexOf(cArr, c);
                                                                                if (indexOf == -1) {
                                                                                    return clone(cArr);
                                                                                }
                                                                                int[] iArr = new int[(cArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(cArr, c, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(cArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }

                                                                            public static byte[] removeAllOccurences(byte[] bArr, byte b) {
                                                                                int indexOf = indexOf(bArr, b);
                                                                                if (indexOf == -1) {
                                                                                    return clone(bArr);
                                                                                }
                                                                                int[] iArr = new int[(bArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(bArr, b, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(bArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }

                                                                            public static short[] removeAllOccurences(short[] sArr, short s) {
                                                                                int indexOf = indexOf(sArr, s);
                                                                                if (indexOf == -1) {
                                                                                    return clone(sArr);
                                                                                }
                                                                                int[] iArr = new int[(sArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(sArr, s, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(sArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }

                                                                            public static int[] removeAllOccurences(int[] iArr, int i) {
                                                                                int indexOf = indexOf(iArr, i);
                                                                                if (indexOf == -1) {
                                                                                    return clone(iArr);
                                                                                }
                                                                                int[] iArr2 = new int[(iArr.length - indexOf)];
                                                                                iArr2[0] = indexOf;
                                                                                int i2 = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(iArr, i, iArr2[i2 - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(iArr, Arrays.copyOf(iArr2, i2));
                                                                                    }
                                                                                    int i3 = i2 + 1;
                                                                                    iArr2[i2] = indexOf2;
                                                                                    i2 = i3;
                                                                                }
                                                                            }

                                                                            public static long[] removeAllOccurences(long[] jArr, long j) {
                                                                                int indexOf = indexOf(jArr, j);
                                                                                if (indexOf == -1) {
                                                                                    return clone(jArr);
                                                                                }
                                                                                int[] iArr = new int[(jArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(jArr, j, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(jArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }

                                                                            public static float[] removeAllOccurences(float[] fArr, float f) {
                                                                                int indexOf = indexOf(fArr, f);
                                                                                if (indexOf == -1) {
                                                                                    return clone(fArr);
                                                                                }
                                                                                int[] iArr = new int[(fArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(fArr, f, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(fArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }

                                                                            public static double[] removeAllOccurences(double[] dArr, double d) {
                                                                                int indexOf = indexOf(dArr, d);
                                                                                if (indexOf == -1) {
                                                                                    return clone(dArr);
                                                                                }
                                                                                int[] iArr = new int[(dArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf(dArr, d, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll(dArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }

                                                                            public static <T> T[] removeAllOccurences(T[] tArr, T t) {
                                                                                int indexOf = indexOf((Object[]) tArr, (Object) t);
                                                                                if (indexOf == -1) {
                                                                                    return clone((Object[]) tArr);
                                                                                }
                                                                                int[] iArr = new int[(tArr.length - indexOf)];
                                                                                iArr[0] = indexOf;
                                                                                int i = 1;
                                                                                while (true) {
                                                                                    int indexOf2 = indexOf((Object[]) tArr, (Object) t, iArr[i - 1] + 1);
                                                                                    if (indexOf2 == -1) {
                                                                                        return removeAll((Object[]) tArr, Arrays.copyOf(iArr, i));
                                                                                    }
                                                                                    int i2 = i + 1;
                                                                                    iArr[i] = indexOf2;
                                                                                    i = i2;
                                                                                }
                                                                            }
                                                                        }
