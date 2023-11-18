package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumUtils {
    private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
    private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";
    private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
    private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";

    public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> cls) {
        Map<String, E> linkedHashMap = new LinkedHashMap();
        for (Enum enumR : (Enum[]) cls.getEnumConstants()) {
            linkedHashMap.put(enumR.name(), enumR);
        }
        return linkedHashMap;
    }

    public static <E extends Enum<E>> List<E> getEnumList(Class<E> cls) {
        return new ArrayList(Arrays.asList(cls.getEnumConstants()));
    }

    public static <E extends java.lang.Enum<E>> boolean isValidEnum(java.lang.Class<E> r1, java.lang.String r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r2 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        java.lang.Enum.valueOf(r1, r2);	 Catch:{ IllegalArgumentException -> 0x0009 }
        r1 = 1;
        return r1;
    L_0x0009:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.EnumUtils.isValidEnum(java.lang.Class, java.lang.String):boolean");
    }

    public static <E extends java.lang.Enum<E>> E getEnum(java.lang.Class<E> r1, java.lang.String r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        if (r2 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = java.lang.Enum.valueOf(r1, r2);	 Catch:{ IllegalArgumentException -> 0x0009 }
        return r1;
    L_0x0009:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.EnumUtils.getEnum(java.lang.Class, java.lang.String):E");
    }

    public static <E extends Enum<E>> long generateBitVector(Class<E> cls, Iterable<? extends E> iterable) {
        checkBitVectorable(cls);
        Validate.notNull(iterable);
        cls = iterable.iterator();
        long j = 0;
        while (cls.hasNext() != null) {
            Enum enumR = (Enum) cls.next();
            Validate.isTrue(enumR != null, NULL_ELEMENTS_NOT_PERMITTED, new Object[0]);
            j |= 1 << enumR.ordinal();
        }
        return j;
    }

    public static <E extends Enum<E>> long[] generateBitVectors(Class<E> cls, Iterable<? extends E> iterable) {
        asEnum(cls);
        Validate.notNull(iterable);
        EnumSet noneOf = EnumSet.noneOf(cls);
        iterable = iterable.iterator();
        while (true) {
            boolean z = true;
            if (!iterable.hasNext()) {
                break;
            }
            Enum enumR = (Enum) iterable.next();
            if (enumR == null) {
                z = false;
            }
            Validate.isTrue(z, NULL_ELEMENTS_NOT_PERMITTED, new Object[0]);
            noneOf.add(enumR);
        }
        long[] jArr = new long[(((((Enum[]) cls.getEnumConstants()).length - 1) / 64) + 1)];
        iterable = noneOf.iterator();
        while (iterable.hasNext()) {
            Enum enumR2 = (Enum) iterable.next();
            int ordinal = enumR2.ordinal() / 64;
            jArr[ordinal] = jArr[ordinal] | (1 << (enumR2.ordinal() % 64));
        }
        ArrayUtils.reverse(jArr);
        return jArr;
    }

    public static <E extends Enum<E>> long generateBitVector(Class<E> cls, E... eArr) {
        Validate.noNullElements((Object[]) eArr);
        return generateBitVector((Class) cls, Arrays.asList(eArr));
    }

    public static <E extends Enum<E>> long[] generateBitVectors(Class<E> cls, E... eArr) {
        asEnum(cls);
        Validate.noNullElements((Object[]) eArr);
        Object noneOf = EnumSet.noneOf(cls);
        Collections.addAll(noneOf, eArr);
        long[] jArr = new long[(((((Enum[]) cls.getEnumConstants()).length - 1) / 64) + 1)];
        eArr = noneOf.iterator();
        while (eArr.hasNext()) {
            Enum enumR = (Enum) eArr.next();
            int ordinal = enumR.ordinal() / 64;
            jArr[ordinal] = jArr[ordinal] | (1 << (enumR.ordinal() % 64));
        }
        ArrayUtils.reverse(jArr);
        return jArr;
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> cls, long j) {
        checkBitVectorable(cls).getEnumConstants();
        return processBitVectors(cls, j);
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVectors(Class<E> cls, long... jArr) {
        EnumSet<E> noneOf = EnumSet.noneOf(asEnum(cls));
        jArr = ArrayUtils.clone((long[]) Validate.notNull(jArr));
        ArrayUtils.reverse(jArr);
        for (Enum enumR : (Enum[]) cls.getEnumConstants()) {
            int ordinal = enumR.ordinal() / 64;
            if (ordinal < jArr.length && (jArr[ordinal] & (1 << (enumR.ordinal() % 64))) != 0) {
                noneOf.add(enumR);
            }
        }
        return noneOf;
    }

    private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> cls) {
        Validate.isTrue(((Enum[]) asEnum(cls).getEnumConstants()).length <= 64, CANNOT_STORE_S_S_VALUES_IN_S_BITS, Integer.valueOf(((Enum[]) asEnum(cls).getEnumConstants()).length), cls.getSimpleName(), Integer.valueOf(64));
        return cls;
    }

    private static <E extends Enum<E>> Class<E> asEnum(Class<E> cls) {
        Validate.notNull(cls, ENUM_CLASS_MUST_BE_DEFINED, new Object[0]);
        Validate.isTrue(cls.isEnum(), S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE, cls);
        return cls;
    }
}
