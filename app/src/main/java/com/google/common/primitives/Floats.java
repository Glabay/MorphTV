package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@GwtCompatible(emulated = true)
public final class Floats {
    public static final int BYTES = 4;

    public static boolean isFinite(float f) {
        int i = 0;
        int i2 = Float.NEGATIVE_INFINITY < f ? 1 : 0;
        if (f < Float.POSITIVE_INFINITY) {
            i = 1;
        }
        return i2 & i;
    }

    private Floats() {
    }

    public static int hashCode(float f) {
        return Float.valueOf(f).hashCode();
    }

    public static int compare(float f, float f2) {
        return Float.compare(f, f2);
    }

    public static boolean contains(float[] fArr, float f) {
        for (float f2 : fArr) {
            if (f2 == f) {
                return 1;
            }
        }
        return false;
    }

    public static int indexOf(float[] fArr, float f) {
        return indexOf(fArr, f, 0, fArr.length);
    }

    private static int indexOf(float[] fArr, float f, int i, int i2) {
        while (i < i2) {
            if (fArr[i] == f) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int indexOf(float[] fArr, float[] fArr2) {
        Preconditions.checkNotNull(fArr, SerializerHandler.TYPE_ARRAY);
        Preconditions.checkNotNull(fArr2, "target");
        if (fArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (fArr.length - fArr2.length) + 1) {
            int i2 = 0;
            while (i2 < fArr2.length) {
                if (fArr[i + i2] != fArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(float[] fArr, float f) {
        return lastIndexOf(fArr, f, 0, fArr.length);
    }

    private static int lastIndexOf(float[] fArr, float f, int i, int i2) {
        for (i2--; i2 >= i; i2--) {
            if (fArr[i2] == f) {
                return i2;
            }
        }
        return -1;
    }

    public static float min(float... fArr) {
        Preconditions.checkArgument(fArr.length > 0);
        float f = fArr[0];
        for (int i = 1; i < fArr.length; i++) {
            f = Math.min(f, fArr[i]);
        }
        return f;
    }

    public static float max(float... fArr) {
        Preconditions.checkArgument(fArr.length > 0);
        float f = fArr[0];
        for (int i = 1; i < fArr.length; i++) {
            f = Math.max(f, fArr[i]);
        }
        return f;
    }

    public static float[] concat(float[]... fArr) {
        int i = 0;
        for (float[] length : fArr) {
            i += length.length;
        }
        Object obj = new float[i];
        int i2 = 0;
        for (Object obj2 : fArr) {
            System.arraycopy(obj2, 0, obj, i2, obj2.length);
            i2 += obj2.length;
        }
        return obj;
    }

    @Beta
    public static Converter<String, Float> stringConverter() {
        return Floats$FloatConverter.INSTANCE;
    }

    public static float[] ensureCapacity(float[] fArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return fArr.length < i ? copyOf(fArr, i + i2) : fArr;
    }

    private static float[] copyOf(float[] fArr, int i) {
        Object obj = new float[i];
        System.arraycopy(fArr, 0, obj, 0, Math.min(fArr.length, i));
        return obj;
    }

    public static String join(String str, float... fArr) {
        Preconditions.checkNotNull(str);
        if (fArr.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(fArr.length * 12);
        stringBuilder.append(fArr[0]);
        for (int i = 1; i < fArr.length; i++) {
            stringBuilder.append(str);
            stringBuilder.append(fArr[i]);
        }
        return stringBuilder.toString();
    }

    public static Comparator<float[]> lexicographicalComparator() {
        return Floats$LexicographicalComparator.INSTANCE;
    }

    public static float[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof Floats$FloatArrayAsList) {
            return ((Floats$FloatArrayAsList) collection).toFloatArray();
        }
        collection = collection.toArray();
        int length = collection.length;
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = ((Number) Preconditions.checkNotNull(collection[i])).floatValue();
        }
        return fArr;
    }

    public static List<Float> asList(float... fArr) {
        if (fArr.length == 0) {
            return Collections.emptyList();
        }
        return new Floats$FloatArrayAsList(fArr);
    }

    @com.google.common.annotations.GwtIncompatible("regular expressions")
    @javax.annotation.Nullable
    @com.google.common.annotations.Beta
    public static java.lang.Float tryParse(java.lang.String r1) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = com.google.common.primitives.Doubles.FLOATING_POINT_PATTERN;
        r0 = r0.matcher(r1);
        r0 = r0.matches();
        if (r0 == 0) goto L_0x0015;
    L_0x000c:
        r1 = java.lang.Float.parseFloat(r1);	 Catch:{ NumberFormatException -> 0x0015 }
        r1 = java.lang.Float.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0015 }
        return r1;
    L_0x0015:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.primitives.Floats.tryParse(java.lang.String):java.lang.Float");
    }
}
